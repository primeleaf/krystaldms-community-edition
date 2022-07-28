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

import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.PasswordHistoryDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.PasswordHistory;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.NewUserView;

/**
 * Author Rahul Kubadia
 */

public class NewUserAction implements Action {
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
		
		if(request.getMethod().equalsIgnoreCase("POST")){

			String realName = request.getParameter("txtRealName")!=null?request.getParameter("txtRealName"):"";
			String userName = request.getParameter("txtUserName")!=null?request.getParameter("txtUserName"):"";
			String password = request.getParameter("txtPassWord")!=null?request.getParameter("txtPassWord"):"";
			String confirmPassword = request.getParameter("txtConfirmPassWord")!=null?request.getParameter("txtConfirmPassWord"):"";
			String userEmail = request.getParameter("txtUserEmail")!=null?request.getParameter("txtUserEmail"):"";
			String userDescription = request.getParameter("txtDescription")!=null?request.getParameter("txtDescription"):"";
			String userType = request.getParameter("radUserType")!=null?request.getParameter("radUserType"):"A";

			userName = userName.replace(' ', '_');

			try {
				if(! GenericValidator.matchRegexp(userName, HTTPConstants.ALPHA_REGEXP)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input for User Name");
					return (new UsersAction().execute(request,response));
				}
				if(!GenericValidator.maxLength(userName, 15)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Value too large for User Name");
					return (new UsersAction().execute(request,response));
				}
				if(!GenericValidator.maxLength(realName, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Value too large for Real Name");
					return (new UsersAction().execute(request,response));
				}
				if(!GenericValidator.maxLength(password, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Value too large for Password");
					return (new UsersAction().execute(request,response));
				}

				if(!GenericValidator.minLength(password, 8)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Invalid Password");
					return (new UsersAction().execute(request,response));
				}
				if(!GenericValidator.matchRegexp(password, HTTPConstants.PASSWORD_PATTERN_REGEXP)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid Password");
					return (new UsersAction().execute(request,response));
				}
				if(!GenericValidator.minLength(confirmPassword, 8)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Invalid Password");
					return (new UsersAction().execute(request,response));
				}

				if(!GenericValidator.matchRegexp(confirmPassword, HTTPConstants.PASSWORD_PATTERN_REGEXP)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Invalid input for Password");
					return (new UsersAction().execute(request,response));
				}
				if(!password.equals(confirmPassword)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Password do not match");
					return (new UsersAction().execute(request,response));
				}
				if(! GenericValidator.isEmail(userEmail)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid Email ID");
					return (new UsersAction().execute(request,response));
				}
				if(! GenericValidator.maxLength(userEmail, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Value too large for Email ID");
					return (new UsersAction().execute(request,response));
				}
				if(!GenericValidator.maxLength(userDescription, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Value too large for User Description");
					return (new UsersAction().execute(request,response));
				}
				if(! User.USER_TYPE_ADMIN.equalsIgnoreCase(userType)){
					userType=User.USER_TYPE_USER;
				}

				
				boolean userExist = UserDAO.getInstance().validateUser(userName);
				boolean emailExist = UserDAO.getInstance().validateUserEmail(userEmail);
				if (userExist) {
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "User with this username already exist");
					return (new UsersAction().execute(request,response));
				}
				if(emailExist){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,  "User with this email Id already exist");
					return (new UsersAction().execute(request,response));
				}

				User user = new User();
				user.setUserName(userName);
				user.setPassword(password);
				user.setUserDescription(userDescription);
				user.setUserEmail(userEmail);
				user.setRealName(realName);
				user.setActive(true);
				user.setUserType(userType);

				UserDAO.getInstance().addUser(user);

				user = UserDAO.getInstance().readUserByName(userName);

				PasswordHistory passwordHistory = new PasswordHistory();
				passwordHistory.setUserId(user.getUserId());
				passwordHistory.setPassword(password);
				PasswordHistoryDAO.getInstance().create(passwordHistory);
				
				AuditLogManager.log(new AuditLogRecord(
						user.getUserId(),
						AuditLogRecord.OBJECT_USER,
						AuditLogRecord.ACTION_CREATED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"ID :" + user.getUserId(),
						"Name : " + user.getUserName()));

				request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"User " + userName.toUpperCase() + " added successfully");
				return (new UsersAction().execute(request,response));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return (new NewUserView(request, response));
	}
}

