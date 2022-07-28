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

package com.primeleaf.krystal.web.action.cpanel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.CheckedOutDocumentDAO;
import com.primeleaf.krystal.model.dao.PasswordHistoryDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class DeleteUserAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			short userId=0;
			try{
				userId = Short.parseShort(request.getParameter("userid")!=null?request.getParameter("userid"):"0");
			}catch(Exception e){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid input");
				return (new UsersAction().execute(request,response));
			}
			
			User user = UserDAO.getInstance().readUserById(userId);
			
			if(user == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid user");
				return (new UsersAction().execute(request,response));
			}
			
			if(ServerConstants.SYSTEM_ADMIN_USER.equalsIgnoreCase(user.getUserName())){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,  "This user can not be deleted");
				return (new UsersAction().execute(request,response));
			}
			
			if(user.isLoggedIn()){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Currently logged in user can not be deleted");
				return (new UsersAction().execute(request,response));
			}
			if (hasCheckedOutObjects(user.getUserName())) {
				request.setAttribute(HTTPConstants.REQUEST_ERROR,  "User  " + user.getUserName() + " has documents checked out");
				return (new UsersAction().execute(request,response));
			}
			UserDAO.getInstance().deleteUser(userId);
			PasswordHistoryDAO.getInstance().delete(userId);
			AuditLogManager.log(new AuditLogRecord(
					user.getUserId(),
					AuditLogRecord.OBJECT_USER,
					AuditLogRecord.ACTION_DELETED,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"ID :" + user.getUserId(),
					"Name : " + user.getUserName()));
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "User "	+ user.getUserName() + " deleted successfully");
			return (new UsersAction().execute(request,response));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (new UsersAction().execute(request,response));
	}

	// to check whether there are documents checkedout by the user
	private boolean hasCheckedOutObjects(String userName) throws Exception{
		return CheckedOutDocumentDAO.getInstance().readCheckedOutDocumentsByUser(userName).size() > 0;
	}
}

