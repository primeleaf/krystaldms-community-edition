package com.primeleaf.krystal.web.view;

/**
 * @author Saumil Shah
 *
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvalidUserView extends WebView{
	public InvalidUserView(HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	
	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printUser404();
		template.generateFooter();
	}
		
	private void printUser404() throws Exception{
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-lg-3 col-centered pre-login\">");
		out.println("<div class=\"card mb-3 mt-3\">");
		out.println("<div class=\"card-header\">");
		out.println("<h4 class=\"text-danger\"><i class=\"bi bi-exclamation-triangle me-2\"></i>Error</h4>");
		out.println("</div>"); //card   - heading
		out.println("<div class=\"card-body\">"); //card   Body
		out.println("<h4 class=\"text-danger\">Error ! No Such user Found</h4>");
		out.println("<p><a class=\"text-danger\" href=\"/forgotpassword\" title=\"Click here try Again\">Click here to login</a></p>");
		out.println("</div>"); //card   Body
		out.println("</div>"); //Panel
		out.println("</div>"); //col-lg-4
		out.println("</div>"); //row
	
	}
	
}
