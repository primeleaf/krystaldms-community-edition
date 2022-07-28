package com.primeleaf.krystal.web.view;

/**
 * @author Saumil Shah
 *
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;

public class ForgotPasswordView extends WebView{

	public ForgotPasswordView(HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printResetPassword();
		template.generateFooter();
	}

	private void printResetPassword() throws Exception{
		out.print("<div class=\"container-fluid mb-4\">");
		out.print("    <form method=\"post\" action=\"/forgotpassword\" name=\"frmForgot\" id=\"frmforgot\" role=\"form\" novalidate=\"novalidate\">");
		out.print("       <div class=\"row\">");
		out.print("          <div class=\"col-lg-3 col-centered\">");
		out.print("             <div class=\"shadow-lg  my-5 row\">");
		out.print("                <div class=\"col-md-12 bg-white p-4\">");
		out.print("                   <p class=\"display-5 mb-4\"><i class=\"bi bi-key me-2\"></i>Forgot Password</p>");
		
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		
		out.print("                   <div class=\"form-floating mb-3\">    <input name=\"txtUserName\" id=\"txtUserName\" type=\"text\" class=\"form-control form-control-sm required\" autocomplete=\"off\" placeholder=\"Username\" title=\"Please enter Username\">    <label for=\"txtLoginId\"><i class=\"bi bi-person h6 me-2\"></i>Username</label>    </div>");
		out.print("                   <div class=\"form-floating mb-3\">    <input name=\"txtUserEmail\" id=\"txtUserEmail\" type=\"email\" class=\"form-control form-control-sm required\" autocomplete=\"off\" placeholder=\"Email\" title=\"Please enter Email\">    <label for=\"txtEmail\"><i class=\"bi bi-envelope h6 me-2\"></i>Email</label>    </div>");
		out.print("                   <div class=\"d-grid gap-2\"><button type=\"submit\" class=\"btn btn-dark\" data-loading-text=\"Please wait...\" id=\"btnLogin\"><i class=\"bi bi-arrow-counterclockwise h6 me-2\"></i>Submit</button>    </div>");
		out.print("                   <p class=\"text-center m-4\"><a href=\"/\">Login</a></p>");
		out.print("                </div>");
		out.print("             </div>");
		out.print("          </div>");
		out.print("       </div>");
		out.print("    </form>");
		out.print(" </div>");
	}
	
}
