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

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.ClassIndexesView;
import com.primeleaf.krystal.web.view.cpanel.ManageDocumentClassesView;

/**
 * Author Rahul Kubadia
 */

public class ClassIndexesAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int documentClassId = 0;
		try{
			documentClassId = Integer.parseInt(request.getParameter("classid")!=null?request.getParameter("classid"):"0");
		}catch(Exception e){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
			return (new ManageDocumentClassesView(request, response));
		}
		DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(documentClassId);
		if(documentClass == null){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,   "Invalid document class");
			return (new ManageDocumentClassesView(request, response));
		}
		request.setAttribute("DOCUMENTCLASS",documentClass);
		return (new ClassIndexesView(request, response));
	}
}