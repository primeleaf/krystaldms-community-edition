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

import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class UserDetailsView extends WebView {

	public UserDetailsView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		printUserDetails();
	}
	private void printUserDetails() {
		out.println("<div class=\"modal-body\">");
		out.println("<div class=\"card mb-0\">");
		out.println("<div class=\"card-body text-center\">");
		out.println("<img src=\"/console/profilepicture?username="+request.getAttribute("username")+"\" class=\"img-fluid rounded-circle\">");
		out.println("<h5 class=\"card-title mt-3\">"+request.getAttribute("realName")+"</h5>");
		out.println("<p class=\"card-text\">"+request.getAttribute("description")+"</p>");
		out.println("</div>");
		out.println("<ul class=\"list-group list-group-flush\">");
		out.println("<li class=\"list-group-item\"><i class=\"bi bi-person h6 me-1\"></i>"+request.getAttribute("username")+"</li>");
		out.println("<li class=\"list-group-item\"><i class=\"bi bi-envelope h6 me-1\"></i>"+request.getAttribute("email")+"</li>");
		if(request.getAttribute("type").equals("A")) {
			out.println("<li class=\"list-group-item\"><i class=\"bi bi-person-badge h6 me-1\"></i>Admin</li>");
		}else {
			out.println("<li class=\"list-group-item\"><i class=\"bi bi-person-badge h6 me-1\"></i>User</li>");
		}
		out.println("<li class=\"list-group-item\"><i class=\"bi bi-clock-history h6 me-1\"></i>"+request.getAttribute("lastLogin")+"</li>");
		out.println("</ul>");
		out.println("</div>");
		out.println("</div>");//modal-body

		out.println("<div class=\"modal-footer\">");
		out.println("<button type=\"button\" class=\"btn btn-sm btn-primary\" data-bs-dismiss=\"modal\">Close</button>");
		out.println("</div>");
	}

}