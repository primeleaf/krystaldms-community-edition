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
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class NewUserView extends WebView {

	public NewUserView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printNewUserForm();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel/users\">Manage Users</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Add User</li>");
		out.println("</ol>");
	}
	
	private void printNewUserForm() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccess((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		try {
			out.println("<div class=\"card   \">");
			out.println("<div class=\"card-header\"><h4><i class=\"bi bi-person \"></i> Add User</h4></div>");
			out.println("<div class=\"card-body\">");
			out.println("<form action=\"/cpanel/newuser?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmNewUser\" class=\"form-horizontal\">");
			out.println("<div class=\"mb-3 row\">");
			out.println("<div class=\"offset-sm-3 col-sm-9\">");
			out.println("<p>Fields marked with <span style='color:red'>*</span> are mandatory</p>");
			out.println("</div>");
			out.println("</div>");
			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtUserName\" class=\"col-sm-3 col-form-label\">User Name <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"text\" id=\"txtUserName\" name=\"txtUserName\" class=\"required form-control\" title=\"Please enter valid User Name\" maxlength=\"15\">");
			out.println("</div>");
			out.println("</div>");
			
			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtRealName\" class=\"col-sm-3 col-form-label\">Real Name <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"text\" id=\"txtRealName\" name=\"txtRealName\" class=\"required form-control\" title=\"Please enter Real Name\" maxlength=\"50\">");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtPassWord\" class=\"col-sm-3 col-form-label\">Password <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"password\" id=\"txtPassWord\" name=\"txtPassWord\" class=\"required form-control complexPassword\" minlength=\"8\" maxlength=\"50\">");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtConfirmPassWord\" class=\"col-sm-3 col-form-label\">Confirm Password <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"password\" id=\"txtConfirmPassWord\" name=\"txtConfirmPassWord\" class=\"form-control required\" equalTo=\"#txtPassWord\" title=\"Passwords must match\" maxlength=\"50\">");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtUserEmail\" class=\"col-sm-3 col-form-label\">Email ID <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"text\" id=\"txtUserEmail\" name=\"txtUserEmail\"  class=\"required form-control email\" title=\"Please enter a valid Email ID\" maxlength=\"50\">");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtDescription\" class=\"col-sm-3 col-form-label\">User Description <span style='color:red'>*</span> </label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<input type=\"text\" id=\"txtDescription\" name=\"txtDescription\" maxlength=\"50\" class=\"form-control required\" title=\"Please enter User Description\" value=\"\">");
			out.println("</div>");
			out.println("</div>");

			// access control fields
			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"radUserType\" class=\"col-sm-3 col-form-label\">User Type</label>");
			out.println("<div class=\"btn-group col-sm-9\" data-bs-toggle=\"buttons\">");
			out.println("<input class=\"btn-check\" type=\"radio\" id=\"radUserType1\" name=\"radUserType\" value=\""+User.USER_TYPE_ADMIN+"\" checked>");
			out.println("<label for=\"radUserType1\" class=\"btn btn-outline-dark\">Administrator</label>");
			out.println("<input class=\"btn-check\" type=\"radio\" id=\"radUserType2\" name=\"radUserType\"  value=\""+User.USER_TYPE_USER+"\">");
			out.println("<label for=\"radUserType2\" class=\"btn btn-outline-dark\">User</label>");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}