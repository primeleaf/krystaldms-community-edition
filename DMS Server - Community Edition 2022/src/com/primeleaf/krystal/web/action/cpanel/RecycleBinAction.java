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

import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.RecycleBinView;

/**
 * @author Rahul Kubadia
 */

public class RecycleBinAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<DocumentClass> classListWithDeletedDocuments = new ArrayList<DocumentClass>();
		ArrayList<DocumentClass> classList = null;
		classList=DocumentClassDAO.getInstance().readDocumentClasses("");

		for(DocumentClass documentClass : classList){
			int deletedDocumentCount = DocumentDAO.getInstance().countDocuments(Hit.STATUS_DELETED, "CLASSID="+documentClass.getClassId());
			int expiredDocumentCount = DocumentDAO.getInstance().countDocuments(Hit.STATUS_EXPIRED, "CLASSID="+documentClass.getClassId());
			if(deletedDocumentCount>0 || expiredDocumentCount > 0 ){
				classListWithDeletedDocuments.add(documentClass);
				request.setAttribute(documentClass.getClassName()+"_DELETED", deletedDocumentCount);
				request.setAttribute(documentClass.getClassName()+"_EXPIRED", expiredDocumentCount);
			}
		}
		request.setAttribute("CLASSLIST", classListWithDeletedDocuments);
		return (new RecycleBinView(request, response));
	}
}

