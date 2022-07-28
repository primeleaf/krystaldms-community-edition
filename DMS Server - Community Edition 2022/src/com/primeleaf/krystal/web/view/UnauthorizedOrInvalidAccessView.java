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

import com.primeleaf.krystal.constants.HTTPConstants;

/**
 * Author Rahul Kubadia
 */

/**
 * @author Rahul Kubadia
 *
 */
public class UnauthorizedOrInvalidAccessView extends WebView {

	public UnauthorizedOrInvalidAccessView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	
	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printUnauthorizedorInvalidAccessScreen();
		template.generateFooter();
	}
		
	private void printUnauthorizedorInvalidAccessScreen() throws Exception{
		String message = (String) request.getAttribute(HTTPConstants.REQUEST_ERROR);
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-sm-8 offset-sm-2\">");
		out.println("<div class=\"card   \">");
		out.println("<div class=\"card-body\">");
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-sm-4 text-center\">");
		out.println("<h1><i class=\"bi bi-exclamation-triangle text-success\"></i></h1>");
		out.println("</div>"); //col-sm-4
		out.println("<div class=\"col-sm-8\">");
		out.println("<h1 class=\"text-success\">"+message+"</h1>");
		out.println("<hr/>");
		out.println("<h4>Please contact your System Administrator</h4>");
		out.println("<p><a href=\"/console\" title=\"Click here to continue\">Click here to continue</a></p>");
		out.println("</div>");  //col-sm-8
		out.println("</div>"); //row
		out.println("</div>"); //card   Body
		out.println("</div>"); //Panel
		out.println("</div>"); //col-lg-4
		out.println("</div>"); //row
	}


}
 
