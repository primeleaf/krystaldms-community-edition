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

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.dao.CheckedOutDocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.cpanel.ManageCheckoutsView;

/**
 * Author Rahul Kubadia
 */

public class ManageCheckoutsAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<DocumentClass> documentClassList = null;
		ArrayList<User> userList=null;
		documentClassList = DocumentClassDAO.getInstance().readDocumentClasses("");
		userList = UserDAO.getInstance().readUsers(""); 
		request.setAttribute("CLASSLIST",documentClassList);
		request.setAttribute("USERLIST",userList);

		if("POST".equalsIgnoreCase(request.getMethod())){
			String classId = request.getParameter("cmbDocumentClass")!=null?request.getParameter("cmbDocumentClass"):"0";
			String userName = request.getParameter("cmbUser")!=null?request.getParameter("cmbUser"):"";
			String checkoutCriteria = " 1=1 ";
			if(! "ALL".equalsIgnoreCase(userName)){
				if(! UserDAO.getInstance().validateUser(userName)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid User");
					return (new ManageCheckoutsView(request, response));
				}
				checkoutCriteria += " AND USERNAME ='" + userName + "'";
			}

			if(! "ALL".equalsIgnoreCase(classId)){
				DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(Integer.parseInt(classId));
				if( documentClass == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid Document Class");
					return (new ManageCheckoutsView(request, response));
				}
				checkoutCriteria += " AND DOCUMENTCLASSES.CLASSID =" + classId + "";
				classId = documentClass.getClassName();
			}	
			ArrayList<CheckedOutDocument> checkedOutDocuments = CheckedOutDocumentDAO.getInstance().readCheckedOutDocuments(checkoutCriteria);
			request.setAttribute("CHECKOUTLIST",checkedOutDocuments);
			request.setAttribute("USER",userName);
			request.setAttribute("DOCUMENTCLASS",classId);
		}
		return (new ManageCheckoutsView(request, response));
	}
}

