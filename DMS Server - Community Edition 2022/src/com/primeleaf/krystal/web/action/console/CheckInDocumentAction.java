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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.validator.GenericValidator;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.IndexRecordManager;
import com.primeleaf.krystal.model.RevisionManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.CheckInDocumentView;

/**
 * Author Rahul Kubadia
 */

public class CheckInDocumentAction implements Action {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		try{
			if("POST".equalsIgnoreCase(request.getMethod())){
				Part filePart = request.getPart("fileDocument");
				String errorMessage;
				String tempFilePath = System.getProperty("java.io.tmpdir");

				if ( !(tempFilePath.endsWith("/") || tempFilePath.endsWith("\\")) ){
					tempFilePath += System.getProperty("file.separator");
				}
				tempFilePath+=  krystalSession.getKrystalUser().getUserName() +"_"+ session.getId();

				String revisionId = "", comments = "",  fileName = "", ext = "",  version = "";
				int documentId = 0;
				// Create a factory for disk-based file items
				FileItemFactory factory = new DiskFileItemFactory();
				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				Map<String, List<FileItem>> items2 = upload.parseParameterMap((HttpServletRequest) request);
				
				List<FileItem> items = new ArrayList<FileItem>();
				
				for(Map.Entry<String,List<FileItem>> entry : items2.entrySet()) {
					items.add(entry.getValue().get(0));
				}
				
				upload.setHeaderEncoding(HTTPConstants.CHARACTER_ENCODING);
				//Create a file upload progress listener
				
				InputStream file = filePart.getInputStream();
				try{
					documentId=Integer.parseInt(request.getParameter("documentid"));
				}catch(Exception ex){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new CheckInDocumentView(request,response));
				}
				revisionId = request.getParameter("revisionid");
				comments = request.getParameter("txtNote");
				version = request.getParameter("version");
				fileName = filePart.getSubmittedFileName();
				ext = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

				Document document =  DocumentDAO.getInstance().readDocumentById(documentId);
				if(document == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
					return (new CheckInDocumentView(request,response));
				}
				if( document.getStatus().equalsIgnoreCase(Hit.STATUS_AVAILABLE)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid check-in");
					return (new CheckInDocumentView(request,response));
				}
				revisionId = document.getRevisionId();
				DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
				AccessControlManager aclManager = new AccessControlManager();
				ACL acl = aclManager.getACL(documentClass, krystalSession.getKrystalUser());
				if(! acl.canCheckin()){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Access Denied");
					return (new CheckInDocumentView(request,response));
				}

				if (file.available() <= 0) {
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Zero length document");
					return (new CheckInDocumentView(request,response));
				}
				if (file.available() > documentClass.getMaximumFileSize() ) { //code for checking maximum size of document in a class
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Document size exceeded");
					return (new CheckInDocumentView(request,response));
				}

				String indexValue = "";
				String indexName = "";

				Hashtable indexRecord = new Hashtable();
				for (IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
					indexName = indexDefinition.getIndexColumnName();
					Iterator itemsIterator = items.iterator();
					while (itemsIterator.hasNext()) {
						FileItem fileItem = (FileItem) itemsIterator.next();
						if (fileItem.isFormField()) {
							String name = fileItem.getFieldName();
							String value = fileItem.getString(HTTPConstants.CHARACTER_ENCODING);
							if (name.equals(indexName)) {
								indexValue = value;
								if(indexValue != null){
									if(indexDefinition.isMandatory()){
										if(indexValue.trim().length() <=0){
											errorMessage =  "Invalid input for "  + indexDefinition.getIndexDisplayName();
											request.setAttribute(HTTPConstants.REQUEST_ERROR,errorMessage);
											return (new CheckInDocumentView(request,response));
										}
									}
									if(IndexDefinition.INDEXTYPE_NUMBER.equalsIgnoreCase(indexDefinition.getIndexType())){
										if(indexValue.trim().length() > 0){
											if(!GenericValidator.matchRegexp(indexValue, HTTPConstants.NUMERIC_REGEXP)){
												errorMessage = "Invalid input for "  + indexDefinition.getIndexDisplayName();
												request.setAttribute(HTTPConstants.REQUEST_ERROR,errorMessage);
												return (new CheckInDocumentView(request,response));
											}
										}
									}else if(IndexDefinition.INDEXTYPE_DATE.equalsIgnoreCase(indexDefinition.getIndexType())){
										if(indexValue.trim().length() > 0){
											if(!GenericValidator.isDate(indexValue, "yyyy-MM-dd",true)){
												errorMessage = "Invalid input for "  + indexDefinition.getIndexDisplayName();
												request.setAttribute(HTTPConstants.REQUEST_ERROR, errorMessage);
												return (new CheckInDocumentView(request,response));
											}
										}
									}

									if (indexValue.trim().length() > indexDefinition.getIndexMaxLength()){ //code for checking maximum length of index field
										errorMessage = 	"Document index length exceeded.  Index Name :" +

												indexDefinition.getIndexDisplayName() + " [ " +
												"Index Length : " + indexDefinition.getIndexMaxLength() + " , " +
												"Actual Length  : " + indexValue.length() + " ]" ;
										request.setAttribute(HTTPConstants.REQUEST_ERROR, errorMessage );
										return (new CheckInDocumentView(request,response));
									}
								}
								indexRecord.put(indexName,indexValue);
							}
						}
						fileItem = null;
					}// while iter
					itemsIterator = null;
				}// while indexDefinitionItr

				CheckedOutDocument checkedOutDocument = new CheckedOutDocument();
				checkedOutDocument.setDocumentId(documentId);
				// Added by Viral Visaria. For the Version Control minor and major.
				// In minor revision increment by 0.1. (No Changes required for the minor revision its handled in the core logic) 
				// In major revision increment by 1.0  (Below chages are incremented by 0.9 and rest 0.1 will be added in the core logic. (0.9 + 0.1 = 1.0)
				double rev = Double.parseDouble(revisionId);
				if("major".equals(version)){
					rev = Math.floor(rev);
					rev = rev + 0.9;
					revisionId = String.valueOf(rev);
				}
				checkedOutDocument.setRevisionId(revisionId);
				checkedOutDocument.setUserName(krystalSession.getKrystalUser().getUserName());
				RevisionManager revisionManager = new RevisionManager();
				revisionManager.checkIn(checkedOutDocument,documentClass,indexRecord,file,comments,ext,krystalSession.getKrystalUser().getUserName());

				//revision id incremented by 0.1 for making entry in audit log 
				rev += 0.1;
				revisionId = String.valueOf(rev);
				//add to audit log 
				AuditLogManager.log(new AuditLogRecord(
						documentId,
						AuditLogRecord.OBJECT_DOCUMENT,
						AuditLogRecord.ACTION_CHECKIN,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"Document ID :  " + documentId + " Revision ID :" + revisionId,
						"Checked In" 
						));
				request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Document checked in successfully");
				return (new CheckInDocumentView(request,response));
			}
			int documentId = 0;
			try{
				documentId=Integer.parseInt(request.getParameter("documentid")!=null?request.getParameter("documentid"):"0");
			}catch(Exception e){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
				return (new CheckInDocumentView(request,response));
			}
			Document document =  DocumentDAO.getInstance().readDocumentById(documentId);
			if(document == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
				return (new CheckInDocumentView(request,response));
			}
			if(! Hit.STATUS_LOCKED.equalsIgnoreCase(document.getStatus())){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid checkin");
				return (new CheckInDocumentView(request,response));
			}
			DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
			LinkedHashMap<String,String> documentIndexes = IndexRecordManager.getInstance().readIndexRecord(documentClass, documentId, document.getRevisionId());

			request.setAttribute("DOCUMENTCLASS", documentClass);
			request.setAttribute("DOCUMENT", document);
			request.setAttribute("DOCUMENTINDEXES", documentIndexes);

		}catch(Exception e){
			e.printStackTrace();
		}
		return (new CheckInDocumentView(request,response));
	}
}

