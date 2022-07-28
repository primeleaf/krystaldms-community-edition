/**
 * Created On 27-Nov-2014
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

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.DeleteDocumentView;

/**
 * Author Rahul Kubadia
 */

public class BulkDeleteDocumentAction implements Action {
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
			String[] documentids = request.getParameterValues("chkDocumentId");
			for(String documentID  : documentids){
				int documentId = 0;
				try{
					documentId=Integer.parseInt(documentID);
				}catch(Exception e){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new DeleteDocumentView(request,response));
				}
				Document document =  DocumentDAO.getInstance().readDocumentById(documentId);
				if(document == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
					return (new DeleteDocumentView(request,response));
				}

				if(Hit.STATUS_LOCKED.equalsIgnoreCase(document.getStatus())){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Delete failed. Document is checked out");
					return (new DeleteDocumentView(request,response));
				}
				if(!Hit.STATUS_AVAILABLE.equalsIgnoreCase(document.getStatus())){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid document");
					return (new DeleteDocumentView(request,response));
				}

				AccessControlManager aclManager = new AccessControlManager();
				DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
				ACL acl = aclManager.getACL(documentClass, krystalSession.getKrystalUser());

				if(! acl.canDelete()){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Access Denied");
					return (new DeleteDocumentView(request,response));
				}
			}
			for(String documentID  : documentids){
				int documentId = 0;
				try{
					documentId=Integer.parseInt(documentID);
				}catch(Exception e){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new DeleteDocumentView(request,response));
				}
				Document document =  DocumentDAO.getInstance().readDocumentById(documentId);
				document.setStatus(Hit.STATUS_DELETED);
				Timestamp modified = new Timestamp(Calendar.getInstance().getTime().getTime());
				document.setModified(modified);
				document.setModifiedBy(krystalSession.getKrystalUser().getUserName());
				DocumentDAO.getInstance().updateDocument(document);
				DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
				String userName = krystalSession.getKrystalUser().getUserName();
				
				//Decrease the document count in the document class
				DocumentClassDAO.getInstance().decreaseActiveDocumentCount(documentClass);
				
				AuditLogManager.log(new AuditLogRecord(
						documentId,
						AuditLogRecord.OBJECT_DOCUMENT,
						AuditLogRecord.ACTION_DELETED,
						userName,
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"",
						"Document ID  " + documentId + " deleted"));
			}
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Documents deleted successfully");
		}catch(Exception e){
			e.printStackTrace();
		}
		return (new DeleteDocumentView(request,response));
	}
}

