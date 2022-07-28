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
import java.util.Calendar;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.IndexRecordManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;

/**
 * @author Rahul Kubadia
 */

public class EditDocumentIndexesAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		try{
			int documentId = 0;
			try{
				documentId = Integer.parseInt(request.getParameter("documentid"));
			}catch (Exception e) {
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
				return (new AJAXResponseView(request,response) );
			}
			//	read HTTP request parameters
			String revisionId=request.getParameter("revisionid");

			Document document = DocumentDAO.getInstance().readDocumentById(documentId);

			if(document == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
				return (new AJAXResponseView(request,response) );
			}
			if(Hit.STATUS_LOCKED.equalsIgnoreCase(document.getStatus())){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Document already checkout");
				return (new AJAXResponseView(request,response) );
			}
			AccessControlManager aclManager = new AccessControlManager();
			
			DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
			ACL acl = aclManager.getACL(documentClass, krystalSession.getKrystalUser());

			if(! acl.canWrite()){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Access Denied");
				return (new AJAXResponseView(request,response) );
			}
			boolean isHeadRevision = false;
			if(document.getRevisionId().equalsIgnoreCase(revisionId)){
				isHeadRevision = true;
			}
			if(!isHeadRevision){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Access Denied");
				aclManager = null;		 			
				return (new AJAXResponseView(request,response) );
			}
			//local variables
			String indexName="",indexValue="";
			String expiryDate = request.getParameter("txtExpiryDate")!=null? request.getParameter("txtExpiryDate"):"";
			Hashtable<String,String> indexRecord = new Hashtable<String,String>();

			for(IndexDefinition indexDefinition :documentClass.getIndexDefinitions()){
				indexName = indexDefinition.getIndexColumnName();
				indexValue = request.getParameter(indexName);
				if(indexValue != null){
					String errorMessage = "";
					if(indexDefinition.isMandatory()){
						if(indexValue.trim().length() <=0){
							errorMessage =  "Invalid input for "  + indexDefinition.getIndexDisplayName();
							request.setAttribute(HTTPConstants.REQUEST_ERROR,errorMessage);
							return (new AJAXResponseView(request,response) );
						}
					}
					if(IndexDefinition.INDEXTYPE_NUMBER.equalsIgnoreCase(indexDefinition.getIndexType())){
						if(indexValue.trim().length() > 0){
							if(!GenericValidator.matchRegexp(indexValue, HTTPConstants.NUMERIC_REGEXP)){
								errorMessage = "Invalid input for "  + indexDefinition.getIndexDisplayName();
								request.setAttribute(HTTPConstants.REQUEST_ERROR,errorMessage);
								return (new AJAXResponseView(request,response) );
							}
						}
					}else if(IndexDefinition.INDEXTYPE_DATE.equalsIgnoreCase(indexDefinition.getIndexType())){
						if(indexValue.trim().length() > 0){
							if(!GenericValidator.isDate(indexValue, "yyyy-MM-dd",true)){
								errorMessage = "Invalid input for "  + indexDefinition.getIndexDisplayName();
								request.setAttribute(HTTPConstants.REQUEST_ERROR,errorMessage);
								return (new AJAXResponseView(request,response) );
							}
						}
					}
					if (indexValue.trim().length() > indexDefinition.getIndexMaxLength()){ //code for checking maximum length of index field
						errorMessage = 	"Document index length exceeded " +
								"Index Name : " +
								indexDefinition.getIndexDisplayName() + " [ " +
								"Index Length : " + indexDefinition.getIndexMaxLength() + " , " +
								"Actual Length : " + indexValue.length() + " ]" ;

						request.setAttribute(HTTPConstants.REQUEST_ERROR,errorMessage);
						return (new AJAXResponseView(request,response) );
					}
				}else{
					indexValue = "";
				}
				indexRecord.put(indexName,indexValue);
			}

			IndexRecordManager.getInstance().updateIndexRecord(documentClass,documentId,revisionId,indexRecord);
			//Update document details like access count last modified etc

			Timestamp modified = new Timestamp(Calendar.getInstance().getTime().getTime());
			document.setStatus(Hit.STATUS_AVAILABLE);
			document.setModified(modified);
			document.setLastAccessed(modified);
			document.setAccessCount(document.getAccessCount() + 1);
			document.setModifiedBy(krystalSession.getKrystalUser().getUserName());

			if(expiryDate.trim().length() > 0){
				Timestamp expiry  = new Timestamp(StringHelper.getDate(expiryDate).getTime());
				document.setExpiry(expiry);
			}else{
				document.setExpiry(null);
			}

			DocumentDAO.getInstance().updateDocument(document);


			//Log the entry to audit logs 
			String userName=krystalSession.getKrystalUser().getUserName();
			AuditLogManager.log(new AuditLogRecord(documentId,AuditLogRecord.OBJECT_DOCUMENT,AuditLogRecord.ACTION_EDITED,userName,request.getRemoteAddr(),AuditLogRecord.LEVEL_INFO,"Revision No :"+revisionId));

			request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Indexes updated successfully");

		}catch(Exception e){
			e.printStackTrace();
		}
		return (new AJAXResponseView(request,response) );
	}
}

