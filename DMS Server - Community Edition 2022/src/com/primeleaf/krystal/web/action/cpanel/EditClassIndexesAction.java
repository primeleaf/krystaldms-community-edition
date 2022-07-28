/**
 * Created On 09-Jan-2014
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.IndexDefinitionDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class EditClassIndexesAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		if(request.getMethod().equalsIgnoreCase("POST")){
			try{
				String indexName = "";
				String indexDisplayName ="";
				String mandatory ="";
				String indexSequence ="";
				String defaultValue = "";
				short sequence = 0;

				int documentClassId = 0;
				try{
					documentClassId = Integer.parseInt(request.getParameter("classid")!=null?request.getParameter("classid"):"0");
				}catch(Exception e){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
					return (new ManageDocumentClassesAction().execute(request, response));
				}
				DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(documentClassId);
				if(documentClass == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid document class");
					return (new ManageDocumentClassesAction().execute(request, response));
				}
				
				int documentClassIndexCount = documentClass.getIndexCount();

				if(documentClassIndexCount > 0 ){
					for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
						indexName = indexDefinition.getIndexColumnName();
						indexDisplayName = request.getParameter("txtIndexDescription"+indexDefinition.getIndexColumnName()+"")!=null?request.getParameter("txtIndexDescription"+indexDefinition.getIndexColumnName()+""):""; 
						mandatory = request.getParameter(""+indexDefinition.getIndexColumnName()+"radMandatory")!=null?request.getParameter(""+indexDefinition.getIndexColumnName()+"radMandatory"):"";
						indexSequence = request.getParameter(""+indexDefinition.getIndexColumnName()+"sequence")!=null?request.getParameter(""+indexDefinition.getIndexColumnName()+"sequence"):"";
						defaultValue= request.getParameter("txtDefaultValue"+indexDefinition.getIndexColumnName())!=null?request.getParameter("txtDefaultValue"+indexDefinition.getIndexColumnName()):"";
						
						if(!GenericValidator.maxLength(indexDisplayName, 50)){
							request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Value too large for Index");
							return (new ClassIndexesAction().execute(request, response));
						}
						if(!GenericValidator.maxLength(defaultValue, 255)){
							request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Value too large for default value");
							return (new ClassIndexesAction().execute(request, response));
						}
						
						try{
							sequence = Short.parseShort(indexSequence);
						}catch(Exception e){
							request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
							return (new ClassIndexesAction().execute(request, response));
						}
						if(sequence > documentClassIndexCount){
							request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input for Index Order");
							return (new ClassIndexesAction().execute(request, response));
						}

						IndexDefinition newIndexDefinition = IndexDefinitionDAO.getInstance().readIndexDefinition(documentClass.getIndexId(), indexName);
						newIndexDefinition.setMandatory("Y".equalsIgnoreCase(mandatory));
						newIndexDefinition.setSequence(sequence);
						newIndexDefinition.setIndexDisplayName(indexDisplayName);
						newIndexDefinition.setDefaultValue(defaultValue);
						IndexDefinitionDAO.getInstance().updateIndexDefinition(newIndexDefinition);
					}
					AuditLogManager.log(new AuditLogRecord(
							documentClass.getClassId(),
							AuditLogRecord.OBJECT_DOCUMENTCLASS,
							AuditLogRecord.ACTION_EDITED,
							krystalSession.getKrystalUser().getUserName(),
							request.getRemoteAddr(),
							AuditLogRecord.LEVEL_INFO,
							"Name : " +  documentClass.getClassName(),
							"Indexes Updated")
							);
					request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Document class indexes updated successfully");
					return (new ClassIndexesAction().execute(request, response));
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return (new ClassIndexesAction().execute(request, response));
	}
}

