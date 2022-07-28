package com.primeleaf.krystal.web.view;

/**
 * @author Saumil Shah
 *
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewPasswordView extends WebView{
	public NewPasswordView(HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	
	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printNewPasswordSent();
		template.generateFooter();
	}
		
	private void printNewPasswordSent() throws Exception{
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-lg-3 col-centered pre-login\">");
		out.println("<div class=\"card mb-3 mt-3\">");
		out.println("<div class=\"card-header\">");
		out.println("<h4 class=\"text-success\"><i class=\"bi bi-check-lg me-2\"></i>Success !</h4>");
		out.println("</div>"); //card   - heading
		out.println("<div class=\"card-body\">"); //card   Body
		out.println("<h4 class=\"text-success\" >New Password Has been sent to your registered Email</h4>");
		out.println("<p><a class=\"text-success\" href=\"/\" title=\"Click here to try Again\">Click here to login</a></p>");
		out.println("</div>"); //card   Body
		out.println("</div>"); //Panel
		out.println("</div>"); //col-lg-4
		out.println("</div>"); //row
	
	}
}