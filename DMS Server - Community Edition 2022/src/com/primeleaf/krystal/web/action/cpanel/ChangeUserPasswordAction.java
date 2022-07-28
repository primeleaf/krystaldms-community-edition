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
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.PasswordHistoryDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.PasswordHistory;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.PasswordService;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;
import com.primeleaf.krystal.web.view.cpanel.ChangeUserPasswordView;

/**
 * @author Rahul Kubadia
 */

public class ChangeUserPasswordAction implements Action {
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
		
		if("POST".equalsIgnoreCase(request.getMethod())) {
			try{
				String newPassword = request.getParameter("txtNewPassword")!=null?request.getParameter("txtNewPassword"):"";
				String confirmPassword = request.getParameter("txtConfirmPassword")!=null?request.getParameter("txtConfirmPassword"):"";
				String userid = request.getParameter("userid")!=null? request.getParameter("userid"):"";
				int userId = 0;
				try{
					userId=Short.parseShort(userid);
				}catch(Exception e){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
					return (new UsersAction().execute(request,response));
				}
				UserDAO.getInstance().setReadCompleteObject(true);
				User user = UserDAO.getInstance().readUserById(userId);
				UserDAO.getInstance().setReadCompleteObject(false);
				if(user == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid user");
					return (new UsersAction().execute(request,response));
				}
				
				if(! newPassword.equals(confirmPassword)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Password do not match");
					return (new AJAXResponseView(request, response));
				}

				if(PasswordHistoryDAO.getInstance().isPasswordExistInHistory(user.getUserId(), newPassword)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Password already used");
					return (new AJAXResponseView(request, response));
				}
				user.setPassword(newPassword);
				UserDAO.getInstance().updateUser(user,true);

				PasswordHistory passwordHistory = new PasswordHistory();
				passwordHistory.setUserId(user.getUserId());
				passwordHistory.setPassword(newPassword);
				if(PasswordHistoryDAO.getInstance().readByUserId(user.getUserId()).size()>=5){
					PasswordHistoryDAO.getInstance().deleteLastHistory(user.getUserId());
				}
				//Password is again set in krystalSession.getKrystalUser(). in encrypted form 
				user.setPassword(PasswordService.getInstance().encrypt(newPassword));
				PasswordHistoryDAO.getInstance().create(passwordHistory);
				AuditLogManager.log(new AuditLogRecord(
						user.getUserId(),
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
			return (new AJAXResponseView(request, response));
		}else{
			String userid = request.getParameter("userid")!=null? request.getParameter("userid"):"";
			int userId = 0;
			try{
				userId=Short.parseShort(userid);
			}catch(Exception e){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
				return (new UsersAction().execute(request,response));
			}
			UserDAO.getInstance().setReadCompleteObject(true);
			User user = UserDAO.getInstance().readUserById(userId);
			UserDAO.getInstance().setReadCompleteObject(false);
			if(user == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid user");
				return (new UsersAction().execute(request,response));
			}
			request.setAttribute("USER", user);
			return (new ChangeUserPasswordView(request, response));
		}
	}
}

