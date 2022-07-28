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
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.PermissionDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Permission;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 */

public class SetPermissionsAction implements Action {
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
		
		ArrayList<User> userList =  UserDAO.getInstance().readUsers("USERID > 0");	
		int documentClassId  = 0;
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
		try{
			
			PermissionDAO.getInstance().delete(documentClassId); //Delete all existing permissions first
			
			for (User user : userList){
				ACL acl = new ACL();
				acl.setCreate(request.getParameter("cbCreate_"+ user.getUserId()) != null ? true : false);
				acl.setRead(request.getParameter("cbRead_"+ user.getUserId()) != null ? true : false);
				acl.setWrite(request.getParameter("cbWrite_"+ user.getUserId()) != null ? true : false);
				acl.setDelete(request.getParameter("cbDelete_"+ user.getUserId()) != null ? true : false);
				acl.setPrint(request.getParameter("cbPrint_"+ user.getUserId()) != null ? true : false);
				acl.setEmail(request.getParameter("cbEmail_"+ user.getUserId()) != null ? true : false);
				acl.setCheckin(request.getParameter("cbCheckin_"+ user.getUserId()) != null ? true : false);
				acl.setCheckout(request.getParameter("cbCheckout_"+ user.getUserId()) != null ? true : false);
				acl.setDownload(request.getParameter("cbDownload_"+ user.getUserId()) != null ? true : false);
				short aclValue = (short)acl.getACLValue();
				
				Permission permission = new Permission();
				permission.setAclValue(aclValue);
				permission.setClassId(documentClassId);
				permission.setUserId(user.getUserId());
				
				PermissionDAO.getInstance().create(permission);
			}
			
			AuditLogManager.log(new AuditLogRecord(
					documentClass.getClassId(),
					AuditLogRecord.OBJECT_DOCUMENTCLASS,
					AuditLogRecord.ACTION_PERMISSIONCHANGED,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"ID :" + documentClass.getClassId(),
					"Name : " + documentClass.getClassDescription()) );
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE,  "Permissions set for document class " + documentClass.getClassName() + " successfully");
			return (new ManageDocumentClassesAction().execute(request, response));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return (new ManageDocumentClassesAction().execute(request, response));
	}
}

