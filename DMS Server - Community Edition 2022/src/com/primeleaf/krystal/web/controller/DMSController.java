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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class DMSController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DMSController(){
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response){
		try{
			request.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);
			response.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);
			
			Action webAction = ActionFactory.getInstance().getAction(request);
			WebView webView = webAction.execute(request, response);
			if(webView != null){ //Incase of redirection we do not get view here
				webView.render();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}