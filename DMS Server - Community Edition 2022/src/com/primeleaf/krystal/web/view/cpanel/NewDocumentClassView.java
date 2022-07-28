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

package com.primeleaf.krystal.web.view.cpanel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class NewDocumentClassView extends WebView {

	public NewDocumentClassView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printNewDocumentClassForm();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel/managedocumentclasses\">Manage Document Classes</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Add Document Class</li>");
		out.println("</ol>");
	}
	private void printNewDocumentClassForm() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccess((String) request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		try {
			out.println("<div class=\"card   \">");
			out.println("<div class=\"card-header\"><i class=\"bi bi-folder2-open text-primary\"></i> Add Document Class</div>");
			out.println("<div class=\"card-body\">");
			
			out.println("<form action=\"/cpanel/newdocumentclass\" method=\"post\" id=\"frmNewDocumentClass\" class=\"form-horizontal\">");
			out.println("<div class=\"mb-3 row\">");
			out.println("<div class=\"offset-sm-3 col-sm-9\">");
			out.println("<p>Fields marked with <span style='color:red'>*</span> are mandatory</p>");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtClassName\" class=\"col-sm-3 col-form-label\">Document Class Name <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"text\" id=\"txtClassName\" name=\"txtClassName\" class=\"required form-control\" title=\"Please enter Document Class Name\" maxlength=\"50\">");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtClassDescription\" class=\"col-sm-3 col-form-label\">Document Class Description <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"text\" id=\"txtClassDescription\" name=\"txtClassDescription\" class=\"required form-control\" title=\"Please enter Document Class Description\" maxlength=\"50\">");
			out.println("</div>");
			out.println("</div>");

			
			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtExpiryPeriod\" class=\"col-sm-3 col-form-label\">Default Expiry Period <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-2\"><input type=\"text\" id=\"txtExpiryPeriod\" name=\"txtExpiryPeriod\"  class=\"required form-control digits range\" min=\"0\" max=\"9999\" title=\"Please enter valid expiry period\" value=\"0\" maxlength=\"4\"></div>" );
			out.println("<div class=\"col-sm-1 d-flex align-items-center\">Days</div>");
			out.println("</div>");
			
			
			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"radActive\" class=\"col-sm-3 col-form-label\">Active ?</label>");
			out.println("<div class=\"btn-group col-sm-2\" data-bs-toggle=\"buttons\">");
			out.println("<input class=\"btn-check\" type=\"radio\" id=\"radActive1\" name=\"radActive\" value=\"Y\" checked>");
			out.println("<label for=\"radActive1\" class=\"btn btn-outline-dark\">Yes");
			out.println("</label>");
			out.println("<input class=\"btn-check\" type=\"radio\" id=\"radActive2\" name=\"radActive\"  value=\"N\">");
			out.println("<label for=\"radActive2\" class=\"btn btn-outline-dark\">No");
			out.println("</label>");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"radRevisionControl\" class=\"col-sm-3 col-form-label\">Enable Version Control?</label>");
			out.println("<div class=\"btn-group col-sm-2\" data-bs-toggle=\"buttons\">");
			out.println("<input class=\"btn-check\" type=\"radio\" id=\"radRevisionControl1\" name=\"radRevisionControl\" value=\"Y\" checked>");
			out.println("<label for=\"radRevisionControl1\" class=\"btn btn-outline-dark\">Yes");
			out.println("</label>");
			out.println("<input class=\"btn-check\" type=\"radio\" id=\"radRevisionControl2\" name=\"radRevisionControl\"  value=\"N\">");
			out.println("<label for=\"radRevisionControl2\" class=\"btn btn-outline-dark\">No");
			out.println("</label>");
			out.println("</div>");
			out.println("</div>");

			out.println("<hr/>");
			out.println("<div class=\"mb-3 row\">");
			out.println("<div class=\"offset-sm-3 col-sm-9\">");
			out.println("<input type=\"submit\"  name=\"btnSubmit\"  value=\"Submit\" class=\"btn  btn-dark\">");
			out.println("</div>");
			out.println("</div>");

			out.println("</form>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
						
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}