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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class UsersView extends WebView {

	public UsersView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printUsers();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Manage Users</li>");
		out.println("</ol>");
	}

	@SuppressWarnings("unchecked")
	private void printUsers() throws Exception{
		printBreadCrumbs();
		
		
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		
		out.println("<div class=\"card\">");
		out.println("<div class=\"card-header\">");
		out.println("<div class=\"d-flex justify-content-between\">");
		out.println("<div class=\"col-xs-6\">");
		out.println("<i class=\"bi bi-person text-primary\"></i> Manage Users");
		out.println("</div>");
		out.println("<div class=\"col-xs-6 text-end\">");
		out.println("<a href=\"/cpanel/newuser?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\"><i class=\"bi text-primary bi-plus-square me-2\"></i>Add User</a>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		try {
			ArrayList<User> userList= (ArrayList<User>)request.getAttribute("USERLIST");
			if(userList.size() > 0 ){
				out.println("<div class=\"table-responsive\">");
				out.println("<table id=\"table\" class=\"table table-bordered table-sm table-hover\" style=\"margin-bottom:0rem;font-size:12px;\">");
				out.println("<thead>");
				out.println("<tr class=\"text-center\">");
				out.println("<th>ID</th>");
				out.println("<th>User Name</th>");
				out.println("<th>Real Name</th>");
				out.println("<th>Email ID</th>");
				out.println("<th>Type</th>");
				out.println("<th>Active</th>");
				out.println("<th>Logged In</th>");
				out.println("<th>Action</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				
				for (User user:userList) {
					out.println("<tr class=\"text-center\">");
					out.println("<td>"+user.getUserId()+"</td>");
					out.println("<td class=\"text-primary\"><i class=\"me-2 bi bi-person\"></i>"+user.getUserName()+"</td>");
					out.println("<td>"+StringEscapeUtils.escapeHtml4(user.getRealName())+"</td>");
					
					out.println("<td>"+StringEscapeUtils.escapeHtml4(user.getUserEmail())+"</td>");
					if(user.getUserType().equals("A")){
						out.println("<td>Admin</td>");
					}else{
						out.println("<td>User</td>");
					}
					if(user.isActive()){
						out.println("<td><i class=\"bi bi-check-lg text-success\"></i></td>");
					}else{
						out.println("<td><i class=\"bi bi-x-lg text-danger\"></i></td>");
					}
					if(user.isLoggedIn()){
						out.println("<td><i class=\"bi bi-check-lg text-success\"></i></td>");
					}else{
						out.println("<td><i class=\"bi bi-x-lg text-danger\"></i></td>");
					}
					out.print("<td>");
					out.println("<a href=\""+HTTPConstants.BASEURL+"/cpanel/edituser?userid="+user.getUserId()+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\">Edit</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/changeuserpassword?userid="+user.getUserId()+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\"  title=\"Change Password\" data-bs-toggle=\"modal\" data-bs-target=\"#changePasswordModal\">Change Password</a>");
					if(user.getUserId() != 1 && ! user.isLoggedIn() && krystalSession.getKrystalUser().getUserId() != user.getUserId()){
						out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/deleteuser?userid="+user.getUserId()+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\"  title=\"Are you sure, you want to delete this user?\" class=\"confirm\">Delete</a>");
					}
					out.println("</tr>");
					out.print("</td>");
				}
				
				out.println("</tbody>");
				out.println("</table>");
				out.println("</div>");//table-responsive
				printModal("changePasswordModal");
			}else{
				out.println("No users");
			}
			
			out.println("</div>");//card
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

