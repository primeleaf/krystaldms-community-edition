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
import com.primeleaf.krystal.model.RevisionManager;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.CancelDocumentCheckoutView;

/**
 * Author Rahul Kubadia
 */

public class CancelCheckoutAdminAction implements Action {
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
			int documentId = 0;
			try{
				documentId=Integer.parseInt(request.getParameter("documentid")!=null?request.getParameter("documentid"):"0");
			}catch(Exception e){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
				return (new CancelDocumentCheckoutView(request,response));
			}
			Document document =  DocumentDAO.getInstance().readDocumentById(documentId);
			if(document == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
				return (new ManageCheckoutsAction().execute(request,response));
			}
			if(! Hit.STATUS_LOCKED.equalsIgnoreCase(document.getStatus())){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,  "Invalid checkout");
				return (new ManageCheckoutsAction().execute(request,response));
			}

			RevisionManager revisionManager = new RevisionManager();
			CheckedOutDocument checkOutObject = new CheckedOutDocument();
			checkOutObject.setDocumentId(documentId);
			checkOutObject.setUserName(krystalSession.getKrystalUser().getUserName());
			revisionManager.cancelCheckOut(checkOutObject,document);
			//add to audit log
			AuditLogManager.log(new AuditLogRecord(
					documentId,
					AuditLogRecord.OBJECT_DOCUMENT,
					AuditLogRecord.ACTION_CHECKOUTCANCEL,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"Document ID :  " + documentId + " Revision ID :" + document.getRevisionId(),
					"Checkout Cancelled" 
					));
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Checkout cancelled successfully.");
		}catch(Exception e){
			e.printStackTrace();
		}
		return (new ManageCheckoutsAction().execute(request,response));
	}
}