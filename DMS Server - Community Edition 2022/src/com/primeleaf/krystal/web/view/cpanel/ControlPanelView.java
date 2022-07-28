/**
 * Created On 09-Jan-2014
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

package com.primeleaf.krystal.web.view.cpanel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class ControlPanelView extends WebView {

	public ControlPanelView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printDomains();
		template.generateFooter();
	}

	private void printDomains() throws Exception{
		try{
			
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header h4\">");
			out.println("<i class=\"bi text-primary bi-speedometer2\"></i> Control Panel");
			out.println("</div>");
			
			out.println("<div class=\"card-body\">");
			out.println("<div class=\"row row-cols-1\">");
			
			out.println("<div class=\"col\">");
			out.println("<a href=\"/cpanel/users\" class=\"card p-2\">");
			out.println("<h4><i class=\"bi text-primary bi-person me-1 display-4\"></i>Manage Users</h4>");
			out.println("</a>");
			out.println("</div>");

			out.println("<div class=\"col\">");
			out.println("<a href=\"/cpanel/managedocumentclasses\"class=\"card p-2\">");
			out.println("<h4><i class=\"bi text-primary bi-folder2-open me-1 display-4\"></i>Manage Document Classes</h4>");
			out.println("</a>");
			out.println("</div>");

			out.println("<div class=\"col\">");
			out.println("<a href=\"/cpanel/managecheckouts\"  class=\"card p-2\">");
			out.println("<h4><i class=\"bi text-primary bi-lock me-1 display-4\"></i>Manage Checkouts</h4>");
			out.println("</a>");
			out.println("</div>");

			out.println("<div class=\"col\">");
			out.println("<a href=\"/cpanel/recyclebin\"  class=\"card p-2\">");
			out.println("<h4><i class=\"bi text-primary bi-trash me-1 display-4\"></i>Recycle Bin</h4>");
			out.println("</a>");
			out.println("</div>");

			out.println("<div class=\"col\">");
			out.println("<a href=\"/cpanel/reports\" class=\"card p-2\">");
			out.println("<h4><i class=\"bi text-primary bi-bar-chart me-1 display-4\"></i>System Reports</h4>");
			out.println("</a>");
			out.println("</div>");
			
			out.println("</div>");//row
			out.println("</div>");//card-body
			out.println("</div>");//card
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

