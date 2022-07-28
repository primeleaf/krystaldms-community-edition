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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.NewDocumentClassView;

/**
 * Author Rahul Kubadia
 */

public class NewDocumentClassAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		if(request.getMethod().equalsIgnoreCase("POST")){
			try {
				String className = request.getParameter("txtClassName") != null ? request.getParameter("txtClassName"): "";
				String classDescription = request.getParameter("txtClassDescription") != null ? request.getParameter("txtClassDescription"): "";
				String isRevCtl = request.getParameter("radRevisionControl") != null ? request.getParameter("radRevisionControl"): "N";
				String active = request.getParameter("radActive") != null ? request.getParameter("radActive"): "N";
				
				//24-Nov-2011 New fields added for controlling the file size and number of documents in the system
				int maximumFileSize = Integer.MAX_VALUE;

				int documentLimit = Integer.MAX_VALUE;

				//10-Sep-2012 New field added for controlling the expiry period of the doucment
				int expiryPeriod = 0;

				if(!GenericValidator.maxLength(className, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Value too large for Document Class Name");
					return (new ManageDocumentClassesAction().execute(request, response));
				}
				if(!GenericValidator.maxLength(classDescription, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Value too large for Document Class Description");
					return (new ManageDocumentClassesAction().execute(request, response));
				}
				try{
					expiryPeriod=request.getParameter("txtExpiryPeriod")!=null ? Integer.parseInt(request.getParameter("txtExpiryPeriod")):0;
				}catch(Exception e){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid input");
					return (new ManageDocumentClassesAction().execute(request, response));
				}
				if(!GenericValidator.isInRange(expiryPeriod, 0, 9999)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new ManageDocumentClassesAction().execute(request, response));
				}
				
				
				if(!"Y".equals(active)){
					active="N";
				}
				if(!"Y".equals(isRevCtl)){
					isRevCtl="N";
				}
				
				boolean isDocumentClassExist= DocumentClassDAO.getInstance().validateDocumentClass(className);

				if(isDocumentClassExist){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Document class " + className + " already exist");
					return (new ManageDocumentClassesAction().execute(request, response));
				}

				DocumentClass documentClass = new DocumentClass();
				documentClass.setClassName(className);
				documentClass.setClassDescription(classDescription);

				if(isRevCtl.equals("Y")){
					documentClass.setRevisionControlEnabled(true);
				}else{
					documentClass.setRevisionControlEnabled(false);
				}
				if(active.equals("Y")){
					documentClass.setVisible(true);
				}else{
					documentClass.setVisible(false);
				}
				documentClass.setIndexId(-1);

				documentClass.setIndexCount(0);

				//24-Nov-2011 New fields added for controlling the file size and number of documents in the system
				documentClass.setMaximumFileSize(maximumFileSize);
				documentClass.setDocumentLimit(documentLimit);
				documentClass.setIndexTableName("");

				documentClass.setCreatedBy(krystalSession.getKrystalUser().getUserName());

				documentClass.setDataTableName("");

				documentClass.setExpiryPeriod(expiryPeriod); //10-Sep-2012 Rahul Kubadia
				documentClass.setExpiryNotificationPeriod(-1);//08-May-2014 Rahul Kubadia
				

				ArrayList<IndexDefinition> indexDefinitions = new ArrayList<IndexDefinition>();
				documentClass.setIndexDefinitions(indexDefinitions);
				DocumentClassDAO.getInstance().addDocumentClass(documentClass);

				AuditLogManager.log(new AuditLogRecord(
						documentClass.getClassId(),
						AuditLogRecord.OBJECT_DOCUMENTCLASS,
						AuditLogRecord.ACTION_CREATED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"ID : " +  documentClass.getClassId(),
						"Name : " + documentClass.getClassName() )
						);
				request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Document class " + className + " added successfully");
				return (new ManageDocumentClassesAction().execute(request, response));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (new NewDocumentClassView(request, response));
	}
}

