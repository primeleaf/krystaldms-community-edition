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

/**
 * Created on 05-Jan-2014
 *
 * Copyright 2003-09 by Primeleaf Consulting (P) Ltd.,
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

package com.primeleaf.krystal.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author Rahul Kubadia
 */

/**
 * @author Rahul Kubadia
 *
 */
public class LogoutView extends WebView {

	public LogoutView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	
	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printLogout();
		template.generateFooter();
	}
		
	private void printLogout() throws Exception{
		out.print("<div class=\"row\">");
		out.print("<div class=\"col-lg-5 col-centered \">");
		out.print("<div class=\"shadow-lg pre-login my-5 row g-0 p-4\">");
		out.print("<p class=\"display-6 mb-4\"><i class=\"bi bi-box-arrow-left me-2\"></i>Logout</p>");
		out.print("<p class=\"display-5 mb-4\">You are logged out successfully.</p>");
		out.print("<a href=\"/\" title=\"Click here to login again.\" class=\"text-primary display-6\"><strong>Click here to login again.</strong></a>");
		out.print("</div>");
		out.print("</div>");
		out.print("</div>");
	}


}
 
