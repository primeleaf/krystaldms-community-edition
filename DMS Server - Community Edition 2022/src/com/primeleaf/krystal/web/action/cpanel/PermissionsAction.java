/**
 * Created On 27-Jan-2014
 * Copyright 2014 by Primeleaf Consulting (P) Ltd.,
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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.PermissionDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Permission;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.PermissionsView;

/**
 * @author Rahul Kubadia
 */

public class PermissionsAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<User> userList =  UserDAO.getInstance().readUsers("USERID > 0");	
		int documentClassId  = 0;
		
		if(request.getParameter(HTTPConstants.CSRF_STRING)==null) {
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid CSRF Token");
			return (new HomeAction().execute(request,response));
		}
		if(!(request.getParameter(HTTPConstants.CSRF_STRING).equals((String)request.getSession().getAttribute(HTTPConstants.CSRF_STRING)))) {
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid CSRF Token");
			return (new HomeAction().execute(request,response));
		}
		
		try{
			documentClassId = Integer.parseInt(request.getParameter("classid")!=null?request.getParameter("classid"):"0");
		}catch(Exception e){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
			return (new ManageDocumentClassesAction().execute(request, response));
		}
		DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(documentClassId);
		if(documentClass == null){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid Document Class");
			return (new ManageDocumentClassesAction().execute(request, response));
		}
		ArrayList<Permission> permissions = PermissionDAO.getInstance().readPermissions(" CLASSID = "+documentClass.getClassId() );
		request.setAttribute("DOCUMENTCLASS", documentClass);
		request.setAttribute("USERLIST",userList);
		request.setAttribute("PERMISSIONS",permissions);
		return (new PermissionsView(request, response));
	}
}

