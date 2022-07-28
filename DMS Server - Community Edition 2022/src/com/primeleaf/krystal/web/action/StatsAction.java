/**
 * Created On 05-Sep-2015
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

package com.primeleaf.krystal.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 * This class provides statistics for the home page for ajax function call
 * @since 10.0 (2016) 
 */

public class StatsAction implements Action {
	private final static Logger kLogger = Logger.getLogger(StatsAction.class.getName());
	
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache"); 
		try{
			StringBuffer sb = new StringBuffer();
			String mode = (request.getParameter("mode")!=null?request.getParameter("mode"):"").trim();
			if("users".equalsIgnoreCase(mode)){
				sb.append(UserDAO.getInstance().readUsers(" LOGGEDIN = 'Y'").size());
			}else {
				sb.append(DocumentDAO.getInstance().countTotalActiveDocuments());
			}
			response.getWriter().write(sb.toString()); 
			response.getWriter().flush();
			response.getWriter().close();	
		}catch(Exception ex){
			String errorMessage = "Unable to perform stats action";
			kLogger.error( errorMessage, ex);
		}
		return null;
	}
}