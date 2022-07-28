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

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.IndexDefinitionDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class DeleteClassIndexAction implements Action {
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
			int documentClassId = 0;
			try{
				documentClassId = Integer.parseInt(request.getParameter("classid")!=null?request.getParameter("classid"):"0");
			}catch(Exception e){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
				return (new ManageDocumentClassesAction().execute(request, response));
			}
			DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(documentClassId);
			if(documentClass == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Invalid document class");
				return (new ManageDocumentClassesAction().execute(request, response));
			}
			String indexName = request.getParameter("name")!=null?request.getParameter("name"):"";
			IndexDefinition indexDefinition = IndexDefinitionDAO.getInstance().readIndexDefinition(documentClass.getIndexId(), indexName);

			if(indexDefinition == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Invalid index");
				return (new ClassIndexesAction().execute(request, response));
			}
			IndexDefinitionDAO.getInstance().deleteIndexDefinitionById(documentClass, indexDefinition);
			AuditLogManager.log(new AuditLogRecord(
					documentClass.getClassId(),
					AuditLogRecord.OBJECT_DOCUMENTCLASS,
					AuditLogRecord.ACTION_EDITED,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"Name : " +  documentClass.getClassName(),
					"Index "+ indexName+" deleted")
					);
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Index " +  indexDefinition.getIndexColumnName()  + " deleted successfully");
			return (new ClassIndexesAction().execute(request, response));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return (new ClassIndexesAction().execute(request, response));
	}
}

