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

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.UnauthorizedOrInvalidAccessView;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class RestoreDocumentAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		
		String documentid = request.getParameter("documentid")!=null?request.getParameter("documentid"):"0";
		int documentId = 0;
		try{
			documentId = Integer.parseInt(documentid);
		}catch(Exception ex){
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
			return (new RecycleBinAction().execute(request, response));
		}
		Document document = DocumentDAO.getInstance().readDocumentById(documentId);
		if(document == null){
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
			return (new UnauthorizedOrInvalidAccessView(request, response));
		}
		if(document.getStatus().equalsIgnoreCase(Hit.STATUS_EXPIRED) || document.getStatus().equalsIgnoreCase(Hit.STATUS_DELETED)){
			document.setStatus(Hit.STATUS_AVAILABLE);
			DocumentDAO.getInstance().updateDocument(document);
			DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
			DocumentClassDAO.getInstance().increaseActiveDocumentCount(documentClass);
			AuditLogManager.log(new AuditLogRecord(documentId,
					AuditLogRecord.OBJECT_DOCUMENT,
					AuditLogRecord.ACTION_RESTORED,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),AuditLogRecord.LEVEL_INFO,"", 
					"Document ID  " + documentId + " restored")
					);
			
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Document restored successfully");
		}else{
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
		}
		return (new RecycleBinAction().execute(request, response));
	}
}