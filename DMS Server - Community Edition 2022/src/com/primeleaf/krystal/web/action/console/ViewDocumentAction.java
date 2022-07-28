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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.IndexRecordManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentRevisionDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.DocumentRevision;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.UnauthorizedOrInvalidAccessView;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.ViewDocumentView;

/**
 * Author Rahul Kubadia
 */

public class ViewDocumentAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		Document document  = null;
		try{
			int documentId;
			String documentid = request.getParameter("documentid")!=null?request.getParameter("documentid"):"0";
			try{
				documentId = Integer.parseInt(documentid);
			}catch(Exception ex){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
				return (new UnauthorizedOrInvalidAccessView(request, response));
			}
			document = DocumentDAO.getInstance().readDocumentById(documentId);
			if(document == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
				return (new UnauthorizedOrInvalidAccessView(request, response));
			}

			String revisionId = request.getParameter("revisionid")!=null?request.getParameter("revisionid"):"";
			if(revisionId.trim().length() == 0){
				revisionId = document.getRevisionId();
			}

			DocumentClass documentClass  = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
			if(documentClass == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
				return (new UnauthorizedOrInvalidAccessView(request, response));
			}
			DocumentRevision  documentRevision = 	DocumentRevisionDAO.getInstance().readDocumentRevisionById(documentId,revisionId);
			if(documentRevision == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
				return (new UnauthorizedOrInvalidAccessView(request, response));
			}

			if(! krystalSession.getKrystalUser().isAdmin()){
				if(document.getStatus().equalsIgnoreCase(Hit.STATUS_DELETED) || document.getStatus().equalsIgnoreCase(Hit.STATUS_HIDDEN)  ){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Access Denied");
					return (new UnauthorizedOrInvalidAccessView(request, response));
				}
			}
			// Get final ACL permission for the user
			AccessControlManager aclManager = new AccessControlManager();
			ACL acl = null;
			
			acl = aclManager.getACL(documentClass, krystalSession.getKrystalUser());

			if(!acl.canRead()){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Access Denied");
				return (new UnauthorizedOrInvalidAccessView(request, response));
			}

			request.setAttribute("ACL", acl);

			int accessCount = document.getAccessCount();
			accessCount++;
			Timestamp lastAccess = new Timestamp(Calendar.getInstance().getTime().getTime());
			document.setLastAccessed(lastAccess);
			document.setAccessCount(accessCount);
			DocumentDAO.getInstance().updateDocument(document);

			boolean isHeadRevision = false;
			if(document.getRevisionId().equalsIgnoreCase(revisionId)){
				isHeadRevision = true;
			}

			LinkedHashMap<String,String> documentIndexes = IndexRecordManager.getInstance().readIndexRecord(documentClass,documentId,revisionId);


			//Log the entry to audit logs 
			AuditLogManager.log(new AuditLogRecord(
					document.getDocumentId(),
					AuditLogRecord.OBJECT_DOCUMENT,
					AuditLogRecord.ACTION_ACCESS,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"",
					"Document ID : " + document.getDocumentId() + " accessed"));


			ArrayList<DocumentClass> documentClassList = DocumentClassDAO.getInstance().readDocumentClasses("ACTIVE = 'Y' AND CLASSID != " + document.getClassId());
			ArrayList<DocumentClass> availableDocumentClassList = new ArrayList<DocumentClass>();
			for(DocumentClass documentClassForMove : documentClassList){
				acl = aclManager.getACL(documentClassForMove, krystalSession.getKrystalUser());
				if(acl.canCreate()){
					availableDocumentClassList.add(documentClassForMove); //User has permission to access this document class so add to displayed list
				}
			}
			request.setAttribute("DOCUMENTCLASSLIST", availableDocumentClassList);

			request.setAttribute("DOCUMENT", document);
			request.setAttribute("HEADREVISION", isHeadRevision);
			request.setAttribute("DOCUMENTREVISION", documentRevision);
			request.setAttribute("DOCUMENTID", document.getDocumentId());
			request.setAttribute("REVISIONID", document.getRevisionId());
			request.setAttribute("DOCUMENTCLASS", documentClass);
			request.setAttribute("DOCUMENTINDEXES", documentIndexes);

			return (new ViewDocumentView(request, response));
		}catch(Exception e){
			e.printStackTrace();
		}
		return (new UnauthorizedOrInvalidAccessView(request, response));
	}
}