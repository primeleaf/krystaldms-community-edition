/**
 * Added by Saumil Shah
 * on 22nd november 2021
 * this class deletes profile picture by setting it to empty image 
 */

package com.primeleaf.krystal.web.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;

public class DeleteProfilePictureAction implements Action {
	
	@Override
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		if(krystalSession.getKrystalUser()!=null) {
			try {
			krystalSession.getKrystalUser().setProfilePicture(null);
			UserDAO.getInstance().setProfilePicture(krystalSession.getKrystalUser());
			AuditLogManager.log(new AuditLogRecord(
					krystalSession.getKrystalUser().getUserId(),
					AuditLogRecord.OBJECT_USER,
					AuditLogRecord.ACTION_EDITED,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"",
					"Profile picture removed"));
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Profile picture removed successfully");
			}catch (Exception e) {
				System.out.println(e);
			}
		}
		return (new MyProfileAction().execute(request, response));
	}
}
