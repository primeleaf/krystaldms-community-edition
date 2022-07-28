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

package com.primeleaf.krystal.web.controller;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;


/**
 * Author Rahul Kubadia
 */

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,maxFileSize = 1024 * 1024 * 1000)

public class ConsoleController extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ConsoleController(){
		super();
	}
	protected void service(HttpServletRequest request, HttpServletResponse response){
		try{
			request.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);
			response.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);

			HttpSession session = request.getSession();
			
			ActionFactory factory =  ActionFactory.getInstance();
			if(session.getAttribute(HTTPConstants.SESSION_KRYSTAL) == null){
				if(ActionFactory.AJAXActions.contains(request.getRequestURI())){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Session Expired");
					WebView webView = new AJAXResponseView(request,response);
					webView.render();
				}else{
					response.sendRedirect("/");
				}
			}else{
				Action webAction = factory.getAction(request);
				WebView webView = webAction.execute(request, response);
				if(webView != null){ //Incase of redirection we do not get view here
					webView.render(	);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}