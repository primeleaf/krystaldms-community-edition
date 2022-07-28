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
public class DefaultView extends WebView {

	public DefaultView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printLoginForm();
		template.generateFooter();
	}

	private void printLoginForm() throws Exception{
		
		out.print("<div class=\"container-fluid mb-4\">");
		out.print("    <form method=\"post\" action=\"/login\" name=\"frmLogin\" id=\"frmLogin\" role=\"form\" novalidate=\"novalidate\">");
		out.print("       <div class=\"row\">");
		out.print("          <div class=\"col-lg-3 col-centered\">");
		out.print("             <div class=\"shadow-lg my-5 row \">");
		out.print("                <div class=\"col-md-12 bg-white p-4\">");
		out.print("                   <p class=\"display-5 mb-4\"><i class=\"bi bi-box-arrow-in-right me-2\"></i>Login</p>");
		printLoginError();
		out.print("                   <div class=\"form-floating mb-3\">    <input name=\"txtLoginId\" id=\"txtUserName\" type=\"text\" class=\"form-control form-control-sm required\" autocomplete=\"off\" placeholder=\"Username Or Email\" title=\"Please enter username\">    <label for=\"txtUserName\"><i class=\"bi bi-person h6 me-2\"></i>Username Or Email</label>    </div>");
		out.print("                   <div class=\"form-floating mb-3\">    <input name=\"txtPassword\" id=\"txtPassword\" type=\"password\" class=\"form-control form-control-sm required\" autocomplete=\"off\" placeholder=\"Password\" title=\"Please enter password\">    <label for=\"txtPassword\"><i class=\"bi bi-key h6 me-2\"></i>Password</label>    </div>");
		out.print("                   <div class=\"d-grid gap-2\">    <button type=\"submit\" class=\"btn btn-dark\" id=\"btnLogin\"><i class=\"bi bi-box-arrow-in-right h6 me-2\"></i>Login</button>    </div>");
		out.print("                   <p class=\"text-center m-4\"><a href=\"/forgotpassword\">Forgot password?</a></p>");
		out.print("                </div>");
		out.print("             </div>");
		out.print("          </div>");
		out.print("       </div>");
		out.print("    </form>");
		out.print(" </div>");


	}

	private void printLoginError() throws Exception{
		String loginError = (String)request.getAttribute(HTTPConstants.REQUEST_ERROR);
		if(loginError != null){
			String errorMessage="";
			if(loginError.equals(HTTPConstants.ERROR_INVALIDUSER)){
				errorMessage = "Invalid Login";
			}else if(loginError.equals(HTTPConstants.ERROR_TOOMANYUSER)){
				errorMessage ="Too many users";
			}else if(loginError.equals(HTTPConstants.ERROR_NOTANADMIN)){
				errorMessage = "Insufficient privileges";
			}else if(loginError.equals(HTTPConstants.ERROR_ALREADY_LOGGED_IN)){
				errorMessage = "User already logged-in";
			}else if(loginError.equals(HTTPConstants.ERROR_ACCESS_DENIED)){
				errorMessage = "Access denied";
			}else if(loginError.equals(HTTPConstants.ERROR_INACTIVEUSER)){
				errorMessage = "Inactive User";
			}else{
				errorMessage = "Unknown error. Please contact support@krystaldms.in";
			}
			out.println("<div class=\"alert alert-danger alert-dismissable d-flex align-items-center justify-content-between\"><span>" + errorMessage + "</span><button type=\"button\" class=\"btn float-right\" data-bs-dismiss=\"alert\" aria-hidden=\"true\"><i class=\"bi text-white bi-x-circle\"></i></button></div>");
			request.removeAttribute(HTTPConstants.REQUEST_ERROR);
		}
	}
}