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
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.UnauthorizedOrInvalidAccessView;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.SummaryReportView;

/**
 * Author Rahul Kubadia
 */

public class SummaryReportAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		if(! krystalSession.getKrystalUser().getUserName().equalsIgnoreCase(ServerConstants.SYSTEM_ADMIN_USER)){
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Access Denied");
			return (new UnauthorizedOrInvalidAccessView(request, response));
		}
		ArrayList<DocumentClass> documentClasses = DocumentClassDAO.getInstance().readDocumentClasses("");
		
		int documentClassCount = documentClasses.size();
		int userCount = UserDAO.getInstance().readUsers("").size();
		
		int totalDocuments=0;
		double totalSize = 0;
		
		request.setAttribute("DOCUMENTCLASSLIST", documentClasses);
		for(DocumentClass documentClass : documentClasses){
			int documentCount = DocumentDAO.getInstance().countActiveDocuments(documentClass.getClassId());
			double documentSize = DocumentDAO.getInstance().documentSize(documentClass.getClassId());
			User user = UserDAO.getInstance().readUserByName(documentClass.getCreatedBy());
			String ownerName = user.getRealName();
			request.setAttribute(documentClass.getClassName()+"_COUNT", documentCount);
			request.setAttribute(documentClass.getClassName()+"_SIZE", documentSize);
			request.setAttribute(documentClass.getClassName()+"_OWNER", ownerName);
			LinkedHashMap<String, Integer> chartValues = DocumentDAO.getInstance().getDocumentCountMonthWise(documentClass.getClassId());
			request.setAttribute(documentClass.getClassName() + "_CHARTVALUES",chartValues);
			totalDocuments+=documentCount;
			totalSize+=documentSize;
		}
		request.setAttribute("DOCUMENT_CLASSES", documentClassCount);
		request.setAttribute("DOCUMENTS", totalDocuments);
		request.setAttribute("TOTALSIZE", totalSize);
		request.setAttribute("USERS", userCount);
		return (new SummaryReportView(request, response));
	}
}

