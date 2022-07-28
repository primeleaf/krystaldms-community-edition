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

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.DocumentManager;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentRevisionDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentRevision;
import com.primeleaf.krystal.model.vo.SharedDocument;
import com.primeleaf.krystal.util.EmailMessage;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;

/**
 * @author Rahul Kubadia
 */

public class ShareDocumentAction implements Action {
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
			String documentid = request.getParameter("documentid")!=null?request.getParameter("documentid"):"";
			String revisionid = request.getParameter("revisionid")!=null?request.getParameter("revisionid"):"";
			String emailId =  request.getParameter("txtEmail")!=null?request.getParameter("txtEmail"):"";
			String userComments = request.getParameter("txtComments")!=null?request.getParameter("txtComments"):"";
			String emailIDs[]  = emailId.split(",");

			for(String emailAddress : emailIDs){
				if(! GenericValidator.isEmail(emailAddress)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new AJAXResponseView(request,response) );
				}
			}

			int documentId = Integer.parseInt(documentid);
			Document document = DocumentDAO.getInstance().readDocumentById(documentId);
			if(document == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
				return (new AJAXResponseView(request,response) );
			}
			DocumentRevision documentRevision = DocumentRevisionDAO.getInstance().readDocumentRevisionById(documentId, revisionid);
			if(documentRevision == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
				return (new AJAXResponseView(request,response) );
			}
			document.setRevisionId(revisionid);
			for(String emailAddress : emailIDs){
				SharedDocument sharedDocument = new SharedDocument();

				sharedDocument.setUsername(krystalSession.getKrystalUser().getUserName());
				sharedDocument.setObjectId(documentId);
				sharedDocument.setRevisionId(revisionid);
				sharedDocument.setEmailId(emailAddress);
				String comments = "";

				EmailMessage emailMessage = new EmailMessage();
				emailMessage.setSubject("Shared Document");

				StringBuffer message = new StringBuffer();
				message.append("Hi,");
				message.append("<p>" + krystalSession.getKrystalUser().getRealName() + " has shared a document with you.</p>");
				emailMessage.setTo(emailAddress);

				sharedDocument.setValidity(Integer.MAX_VALUE);
				DocumentManager dm = new DocumentManager();
				documentRevision = dm.retreiveDocument(document);
				File tempFile = File.createTempFile("Krystal_"+documentId,"."+document.getExtension());
				FileUtils.copyInputStreamToFile(documentRevision.getDocumentFile(), tempFile);
				emailMessage.setAttachmentFile(tempFile);
				message.append("<p>Please find attached document.</p>");

				if(userComments.trim().length()>0){//User has put some comments
					message.append("<p>Message from :" + krystalSession.getKrystalUser().getRealName() + "</p>");
					message.append("<p><i>"+userComments+"</i></p>");
				}

				message.append("<p>Dhanyawad,<br/>DMS Administrator</p>");
				emailMessage.setMessage(message.toString());
				emailMessage.send();	
				tempFile.delete();
				document = DocumentDAO.getInstance().readDocumentById(documentId);
				Date lastAccessed = new Date();
				document.setLastAccessed(new java.sql.Timestamp(lastAccessed.getTime()));
				DocumentDAO.getInstance().updateDocument(document);

				AuditLogManager.log(new AuditLogRecord(
						Integer.parseInt(documentid),
						AuditLogRecord.OBJECT_DOCUMENT,
						AuditLogRecord.ACTION_SHARED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						emailAddress,
						comments)
						);
			}
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Document shared successfully");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return (new AJAXResponseView(request,response) );

	}

}