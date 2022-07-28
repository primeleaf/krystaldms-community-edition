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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.RevisionManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.RevisionRecord;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;
import com.primeleaf.krystal.web.view.console.RevisionHistoryView;

/**
 * Author Rahul Kubadia
 */

public class RevisionHistoryAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String documentid = request.getParameter("documentid").trim();// get object id
		
		int documentId = 0;
		try{
			documentId = Integer.parseInt(documentid);
		}catch(Exception ex){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
			return new AJAXResponseView(request,response);
		}
		Document document = DocumentDAO.getInstance().readDocumentById(documentId);
		if(document == null){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid document");
			return new AJAXResponseView(request,response);
		}
		RevisionManager revisionManager = new RevisionManager();
		ArrayList<RevisionRecord> revisionHistory = revisionManager.getRevisionHistory(documentId);
		DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(document.getClassId());
		request.setAttribute("DOCUMENT", document);
		request.setAttribute("DOCUMENTCLASS", documentClass);
		request.setAttribute("REVISIONHISTORY", revisionHistory);
		return (new RevisionHistoryView(request, response));
	}
}

