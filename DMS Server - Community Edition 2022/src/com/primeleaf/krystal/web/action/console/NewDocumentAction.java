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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.DocumentManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.DocumentRevision;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.NewDocumentView;

/**
 * Author Rahul Kubadia
 */

public class NewDocumentAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		String classid = request.getParameter("classid")!=null? request.getParameter("classid"):"0";
		if(request.getMethod().equalsIgnoreCase("POST")){
			response.setContentType("application/json"); 
			PrintWriter out = response.getWriter();
			try{
				int classId = 0;
				String comments = request.getParameter("txtNote")!=null?request.getParameter("txtNote"):"";
				Part filePart = request.getPart("fileDocument"); 
				String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
				String extension = FilenameUtils.getExtension(originalFileName);

				try {
					classId = Integer.parseInt(classid);
				}catch(Exception ex) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.print("{\"error\": \"Invalid input for Document Class\"}");
					return null;
				}

				final DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(classId);
				if(documentClass == null){
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.print("{\"error\": \"Invalid Document Class\"}");
					return null;
				}

				File documentFile = File.createTempFile(krystalSession.getKrystalUser().getUserName(),"."+extension.toUpperCase());
				FileOutputStream fos = new FileOutputStream(documentFile);
				IOUtils.copy(filePart.getInputStream(), fos );
				fos.close();

				Hashtable<String,String> indexRecord = new Hashtable<String, String>();
				for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()) {
					String indexValue = request.getParameter(indexDefinition.getIndexColumnName());
					String errorMessage = "";
					if(indexValue != null){
						if(indexDefinition.isMandatory()){
							if(indexValue.trim().length() <=0){
								errorMessage ="Invalid input for "  + indexDefinition.getIndexDisplayName();
								response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
								out.print("{\"error\": \""+errorMessage+"\"}");
								return null;
							}
						}
						if(IndexDefinition.INDEXTYPE_NUMBER.equalsIgnoreCase(indexDefinition.getIndexType())){
							if(indexValue.trim().length() > 0){
								if(!GenericValidator.matchRegexp(indexValue, HTTPConstants.NUMERIC_REGEXP)){
									errorMessage ="Invalid input for "  + indexDefinition.getIndexDisplayName();
									response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
									out.print("{\"error\": \""+errorMessage+"\"}");
									return null;
								}
							}
						}else if(IndexDefinition.INDEXTYPE_DATE.equalsIgnoreCase(indexDefinition.getIndexType())){
							if(indexValue.trim().length() > 0){
								if(!GenericValidator.isDate(indexValue, "yyyy-MM-dd",true)){
									errorMessage = "Invalid input for "  + indexDefinition.getIndexDisplayName();
									response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
									out.print("{\"error\": \""+errorMessage+"\"}");
									return null;
								}
							}
						}
						if (indexValue.trim().length() > indexDefinition.getIndexMaxLength()){ //code for checking index field length
							errorMessage = 	"Document index size exceeded for " +
									"Index Name : " +
									indexDefinition.getIndexDisplayName() + " [ " +
									"Index Length : " + indexDefinition.getIndexMaxLength() + " , " +
									"Actual Length : " + indexValue.length() + " ]" ;
							response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
							out.print("{\"error\": \""+errorMessage+"\"}");
							return null;
						}
					}
					indexRecord.put(indexDefinition.getIndexColumnName(), indexValue);
				}


				FileInputStream fis = new FileInputStream(documentFile);
				
				
				DocumentRevision documentRevision = new DocumentRevision();
				documentRevision.setClassId(documentClass.getClassId());
				documentRevision.setDocumentId(0);
				documentRevision.setRevisionId("1.0");
				documentRevision.setDocumentFile(fis);
				documentRevision.setUserName(krystalSession.getKrystalUser().getUserName());
				documentRevision.setIndexRecord(indexRecord);
				documentRevision.setComments(comments);

				DocumentManager documentManager = new DocumentManager();
				documentManager.storeDocument(documentRevision, documentClass,extension);

				//Log the entry to audit logs 
				AuditLogManager.log(new AuditLogRecord(
						documentRevision.getDocumentId(),
						AuditLogRecord.OBJECT_DOCUMENT,
						AuditLogRecord.ACTION_CREATED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"",
						"Document created"));

				response.setStatus(HttpServletResponse.SC_OK);
				out.println("Document uploaded and stored successfully.");
			}catch (Exception e) {
				e.printStackTrace(System.out);
			}
			return null;
		}else{
			try{
				ArrayList<DocumentClass> availableDocumentClasses = DocumentClassDAO.getInstance().readDocumentClasses(" ACTIVE = 'Y'");
				ArrayList<DocumentClass> documentClasses = new ArrayList<DocumentClass>();
				AccessControlManager aclManager = new AccessControlManager();
				for(DocumentClass documentClass : availableDocumentClasses){
					ACL acl = aclManager.getACL(documentClass, krystalSession.getKrystalUser());
					if(acl.canCreate()){
						documentClasses.add(documentClass);
					}
				}
				int documentClassId = 0;
				try{
					documentClassId = Integer.parseInt(classid);
				}catch(Exception ex){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new NewDocumentView(request, response));
				}
				if(documentClassId > 0 ){
					DocumentClass selectedDocumentClass = DocumentClassDAO.getInstance().readDocumentClassById(documentClassId);
					request.setAttribute("DOCUMENTCLASS", selectedDocumentClass);
				}
				request.setAttribute("CLASSID", documentClassId);
				request.setAttribute("CLASSLIST", documentClasses);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return (new NewDocumentView(request, response));
	}
}

