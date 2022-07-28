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

package com.primeleaf.krystal.web.view.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class CheckoutDocumentView extends WebView {

	public CheckoutDocumentView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		WebPageTemplate edmcTemplate = new WebPageTemplate(request, response);
		edmcTemplate.generateHeader();
		deleteDocumentResponse();
		edmcTemplate.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Checkout</li>");
		out.println("</ol>");
	}
	private void deleteDocumentResponse() throws Exception{
		try {
			Document document = (Document) request.getAttribute("DOCUMENT");
			printBreadCrumbs();
			
			out.println("<div class=\"card   \">");
			out.println("<div class=\"card-header\"><i class=\"bi bi-lock text-primary\"></i> Checkout</div>");
			out.println("<div class=\"card-body\">");
			if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
				out.println("<h5><i class=\"bi  bi-check-circle text-success\"></i> "+request.getAttribute(HTTPConstants.REQUEST_MESSAGE) +"</h5>");
				String downloadUrl = "/console/downloaddocument?documentid="+document.getDocumentId();
				out.println("<iframe height=\"0\" width=\"0\" border=\"0\" src=\""+downloadUrl+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" style=\"border:0px;\"></iframe>");
			}
			if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
				out.println("<h5><i class=\"bi  bi-x-circle text-danger\"></i> "+request.getAttribute(HTTPConstants.REQUEST_ERROR) +"</h5>");
			}
			out.println("</div>");//card-body
			out.println("</div>");//panel
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}