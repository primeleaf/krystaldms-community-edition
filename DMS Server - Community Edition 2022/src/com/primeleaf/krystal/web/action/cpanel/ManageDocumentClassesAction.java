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

import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.ManageDocumentClassesView;

/**
 * Author Rahul Kubadia
 */

public class ManageDocumentClassesAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<DocumentClass> documentClassList = null;
		documentClassList = DocumentClassDAO.getInstance().readDocumentClasses("");
		request.setAttribute("CLASSLIST",documentClassList);
		return (new ManageDocumentClassesView(request, response));
	}
}

