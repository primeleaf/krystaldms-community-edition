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
import com.primeleaf.krystal.model.TableManager;
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

public class NewClassIndexAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		if(request.getMethod().equalsIgnoreCase("POST")){
			try{
				int documentClassId = 0;
				try{
					documentClassId = Integer.parseInt(request.getParameter("classid")!=null?request.getParameter("classid"):"0");
				}catch(Exception e){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
					return (new ManageDocumentClassesAction().execute(request, response));
				}
				DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(documentClassId);
				if(documentClass == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid document class");
					return (new ManageDocumentClassesAction().execute(request, response));
				}

				String indexName = request.getParameter("txtIndexName")!=null?request.getParameter("txtIndexName"):"";
				String indexDisplayName = request.getParameter("txtIndexDisplayName")!=null?request.getParameter("txtIndexDisplayName"):"";
				String indexType  = request.getParameter("cmbIndexType")!=null?request.getParameter("cmbIndexType"):IndexDefinition.INDEXTYPE_STRING;
				String indexLength = request.getParameter("txtIndexLength")!=null?request.getParameter("txtIndexLength"):"100";
				String mandatory = request.getParameter("radMandatory")!=null?request.getParameter("radMandatory"):"";
				String defaultValue = request.getParameter("txtDefaultValue")!=null?request.getParameter("txtDefaultValue"):"";

				
				int maximumIndexLength  = Integer.parseInt(indexLength) ;
						
				if(!GenericValidator.maxLength(indexName, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Value too large for Index Name");
					return (new ClassIndexesAction().execute(request, response));
				}
				boolean isIndexDefinition = IndexDefinitionDAO.getInstance().validateIndexDefinition(documentClass.getIndexId(), indexName);
				if(isIndexDefinition){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Index " + indexName + " already exist");
					return (new ClassIndexesAction().execute(request, response));
				}
				if(defaultValue.trim().length() > 0){
					if(IndexDefinition.INDEXTYPE_NUMBER.equalsIgnoreCase(indexType)){
						if(!GenericValidator.isDouble(defaultValue)){
							request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input for Default Value : " + defaultValue);
							return (new ClassIndexesAction().execute(request, response));
						}
					}
					if(IndexDefinition.INDEXTYPE_DATE.equalsIgnoreCase(indexType)){
						if(!GenericValidator.isDate(defaultValue,"yyyy-MM-dd",true)){
							request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid input for Default Value : " + defaultValue);
							return (new ClassIndexesAction().execute(request, response));
						}
					}
				}
				if(!GenericValidator.maxLength(defaultValue, maximumIndexLength)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Value too large for Default Value : " +defaultValue);
					return (new ClassIndexesAction().execute(request, response));
				}
				int i = documentClass.getIndexDefinitions().size();

				indexName = indexName.replace(' ','_');
				IndexDefinition indexDefinition = new IndexDefinition();
				indexDefinition.setIndexColumnName(indexName);
				indexDefinition.setIndexDisplayName(indexDisplayName);
				indexDefinition.setDefaultValue(defaultValue);
				indexDefinition.setIndexMaxLength(maximumIndexLength);
				indexDefinition.setIndexType(indexType);
				indexDefinition.setDefaultFilter("");
				indexDefinition.setMandatory("Y".equalsIgnoreCase(mandatory));
				indexDefinition.setSequence((byte)(++i));

				int indexCount = documentClass.getIndexCount();
				if( indexCount == 0 && documentClass.getIndexId()==-1) {
					int indexId = IndexDefinitionDAO.getInstance().getNextIndexId();
					while(DocumentClassDAO.getInstance().readDocumentClasses(" INDEXID = "+indexId).size() > 0){
						indexId++;
					}
					documentClass.setIndexId(indexId);
				}
				indexDefinition.setIndexId(documentClass.getIndexId());

				IndexDefinitionDAO.getInstance().addIndexDefinition(indexDefinition);

				TableManager tableManager = new TableManager();
				tableManager.alterIndexTable(documentClass, indexDefinition);

				indexCount++;
				documentClass.setIndexCount(indexCount);
				DocumentClassDAO.getInstance().updateDocumentClass(documentClass);

				AuditLogManager.log(new AuditLogRecord(
						documentClass.getClassId(),
						AuditLogRecord.OBJECT_DOCUMENTCLASS,
						AuditLogRecord.ACTION_EDITED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"Name : " +  documentClass.getClassName(),
						"Index " + indexName+ "  added")
						);

				request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Index " + indexDefinition.getIndexDisplayName() + " added successfully");
				return (new ClassIndexesAction().execute(request, response));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return (new ClassIndexesAction().execute(request, response));
	}
}
