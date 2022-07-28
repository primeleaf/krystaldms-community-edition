/**
 * Created On 05-Jan-2014
 * Copyright 2010 by Primeleaf Consulting (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Primeleaf Consulting (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consulting (P) Ltd.
 */

package com.primeleaf.krystal.web.action.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.PasswordHistoryDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.PasswordHistory;
import com.primeleaf.krystal.security.PasswordService;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;

/**
 * @author Rahul Kubadia
 */

public class ChangePasswordAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if("POST".equalsIgnoreCase(request.getMethod())){
			HttpSession session = request.getSession();
			KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
			if(request.getParameter(HTTPConstants.CSRF_STRING)==null) {
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid CSRF Token");
				return (new HomeAction().execute(request,response));
			}
			if(!(request.getParameter(HTTPConstants.CSRF_STRING).equals((String)session.getAttribute(HTTPConstants.CSRF_STRING)))) {
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid CSRF Token");
				return (new HomeAction().execute(request,response));
			}
			try{
				String oldPassword = (request.getParameter("txtOldPassword")!=null?request.getParameter("txtOldPassword"):"").trim();
				String newPassword = (request.getParameter("txtNewPassword")!=null?request.getParameter("txtNewPassword"):"").trim();
				String confirmPassword = (request.getParameter("txtConfirmPassword")!=null?request.getParameter("txtConfirmPassword"):"").trim();
				
				oldPassword = PasswordService.getInstance().encrypt(oldPassword);
				
				if(! newPassword.equals(confirmPassword)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Passwords do not match");
					return (new AJAXResponseView(request, response));
				}
				
				if(! oldPassword.equals(krystalSession.getKrystalUser().getPassword())){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid Password");
					return (new AJAXResponseView(request, response));
				}
				
				if(PasswordHistoryDAO.getInstance().isPasswordExistInHistory(krystalSession.getKrystalUser().getUserId(), newPassword)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Password already used");
					return (new AJAXResponseView(request, response));
				}
				krystalSession.getKrystalUser().setPassword(newPassword);
				UserDAO.getInstance().updateUser(krystalSession.getKrystalUser(),true);

				PasswordHistory passwordHistory = new PasswordHistory();
				passwordHistory.setUserId(krystalSession.getKrystalUser().getUserId());
				passwordHistory.setPassword(newPassword);
				if(PasswordHistoryDAO.getInstance().readByUserId(krystalSession.getKrystalUser().getUserId()).size()>=5){
					PasswordHistoryDAO.getInstance().deleteLastHistory(krystalSession.getKrystalUser().getUserId());
				}
				//Password is again set in krystalSession.getKrystalUser() in encrypted form 
				krystalSession.getKrystalUser().setPassword(PasswordService.getInstance().encrypt(newPassword));
				PasswordHistoryDAO.getInstance().create(passwordHistory);
				AuditLogManager.log(new AuditLogRecord(
						krystalSession.getKrystalUser().getUserId(),
						AuditLogRecord.OBJECT_USER,
						AuditLogRecord.ACTION_PASSWORDCHANGED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"",
						"Password Changed"));
				request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Password changed successfully");
				return (new AJAXResponseView(request, response));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return (new AJAXResponseView(request, response));
	}
}