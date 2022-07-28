/**
 * Created On 27-Jan-2014
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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.RecycleBinContentView;

/**
 * @author Rahul Kubadia
 */

public class RecycleBinContentAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<Document> documents = null;
		String type = request.getParameter("type")!=null?request.getParameter("type"):Hit.STATUS_DELETED;
		String classid = request.getParameter("classid")!=null?request.getParameter("classid"):"0";
		int classId = 0;
		try{
			classId = Integer.parseInt(classid);
		}catch(Exception ex){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
			return new RecycleBinAction().execute(request, response);
		}
		DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(classId);
		if(documentClass == null){
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document class");
			return new RecycleBinAction().execute(request, response);
		}
		if(Hit.STATUS_DELETED.equalsIgnoreCase(type)){
			documents = DocumentDAO.getInstance().readDocuments("SELECT * FROM DOCUMENTS WHERE STATUS = '"+ Hit.STATUS_DELETED +"' AND CLASSID="+documentClass.getClassId());
		}else{
			documents = DocumentDAO.getInstance().readDocuments("SELECT * FROM DOCUMENTS WHERE STATUS = '"+ Hit.STATUS_EXPIRED +"' AND CLASSID="+documentClass.getClassId());
		}
		request.setAttribute("DOCUMENTCLASS", documentClass);
		request.setAttribute("DOCUMENTLIST", documents);
		return (new RecycleBinContentView(request, response));
	}
}

