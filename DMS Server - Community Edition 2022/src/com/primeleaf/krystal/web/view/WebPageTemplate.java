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
 * of Primeleaf Consulting (P) Ltd. (\"Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consulting (P) Ltd.
 */
package com.primeleaf.krystal.web.view;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.util.ConfigParser;
import com.primeleaf.krystal.web.KrystalSession;


/**
 * @author Rahul Kubadia
 *
 */
public class WebPageTemplate {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected PrintWriter out;

	public WebPageTemplate (HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception{
		request = httpRequest;
		response =  httpResponse;
		out = httpResponse.getWriter();
		session = (HttpSession) request.getSession();
	} 

	public void generatePopupHeader() throws Exception{
		generateHead();
		out.println("<div class=\"container-fluid\">");
	}
	public void generatePopupFooter() throws Exception{
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

	private void generateHead() throws Exception{
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>KRYSTAL DMS  " + ServerConstants.SERVER_VERSION + " - " + ServerConstants.SERVER_EDITION + "</title>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text-html; charset=utf-8\">");
		out.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">");
		out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		out.println("<link rel=\"stylesheet\" href=\"/css/bootstrap.css\"/>");
		out.println("<link rel=\"stylesheet\" href=\"/css/bootstrap-dialog.css\"/>");
		out.println("<link rel=\"stylesheet\" href=\"/css/datepicker.css\"/>");
		out.println("<link rel=\"stylesheet\" href=\"/css/sticky-footer-navbar.css\"/>");
		out.println("<link rel=\"stylesheet\" href=\"/css/colorbox.css\"/>");
		out.println("<link rel=\"stylesheet\" href=\"/css/krystal.css\"/>");
		out.println("<link rel=\"stylesheet\" href=\"/css/bootstrap-icons.css\">");
		out.println("<link rel=\"SHORTCUT ICON\" href=\"/images/favicon.ico\"/>");
		out.println("<script src=\"/js/jquery.js\"></script>");
		out.println("<script src=\"/js/jquery.validate.min.js\"></script>");
		out.println("<script src=\"/js/additional-methods.min.js\"></script>");
		out.println("<script src=\"/js/odometer.min.js\"></script>");
		out.println("<script src=\"/js/bootstrap-dialog.js\"></script>");
		out.println("<script src=\"/js/bootstrap-datepicker.js\"></script>");
		out.println("<script src=\"/js/bootstrap.bundle.js\"></script>");
		out.println("<script src=\"/js/typeahead.min.js\"></script>");
		out.println("<script src=\"/js/jquery.colorbox-min.js\"></script>");
		out.println("<script src=\"/js/jquery.highlight.js\"></script>");
		out.println("<script src=\"/js/jquery.countdown.js\"></script>");
		out.println("<script src=\"/js/Chart.min.js\"></script>");
		out.println("<script src=\"/js/scripts.js\"></script>");
		
		int timeout = ConfigParser.getSessionTimeout()*1000;
		out.println("<script>");
		out.println("var timeout = new Date().getTime() + "+timeout +";");
		//Added by Rahul Kubadia on 25-Jan-2016 for 2016 R2
		out.println("var uptime = new Date().getTime() - " + ManagementFactory.getRuntimeMXBean().getUptime()  +";");
		out.println("</script>");
		
		out.println("</head>");
		out.println("<body>");
	}

	public void generateHeader() throws Exception{
		HttpSession session = request.getSession();
		generateHead();
		if(session.getAttribute(HTTPConstants.SESSION_KRYSTAL) == null){ //Login or forgot password page hence show login bg
			out.println("<div id=\"wrap\" class=\"cover\">");
		}else{
			out.println("<div id=\"wrap\">");
		}
		if(session.getAttribute(HTTPConstants.SESSION_KRYSTAL) == null){
			//Added New Navbar , Saumil Shah
			out.print("<nav class=\"navbar navbar-expand-xl navbar-dark bg-dark fixed-top\">");
			out.print("   <a class=\"navbar-brand\" href=\"https://www.krystaldms.in/products/enterprise\" target=\"_new\"><img src=\"/images/krystal.png\" class=\"d-inline-block align-text-top mx-1 krystallogo\"> KRYSTAL DMS</a><button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#collapsibleNavbar\" aria-controls=\"collapsibleNavbar\" aria-expanded=\"false\" aria-label=\"Toggle navigation\" data-toggle=\"collapse\" data-target=\"#collapsibleNavbar\"><span class=\"navbar-toggler-icon\"></span></button>");
			out.print("   <div class=\"collapse navbar-collapse\" id=\"collapsibleNavbar\">");
			out.print("      <ul class=\"navbar-nav	ms-auto\">");
			out.print("         <li class=\"nav-item bg-danger\" title=\"docs\"><a href=\"javascript:void(0);\" class=\"nav-link tip fw-bold text-white\" data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\" title=\"\" data-bs-original-title=\"Total Documents\"><i class=\"bi bi-files h6 me-2\"></i><span id=\"hpDocumentCount\" class=\"odometer fw-bold\"></span></a></li>");
			out.print("         <li class=\"nav-item bg-primary\"><a href=\"javascript:void(0);\" class=\"nav-link tip fw-bold text-white\" data-bs-toggle=\"tooltip\" data-bs-placement=\"bottom\" title=\"\" data-bs-original-title=\"Currently logged in users\"><i class=\"bi bi-people h6 me-2\"></i><span id=\"hpUserCount\" class=\"odometer  fw-bold\"></span></a></li>");
			out.print("			<li class=\"nav-item active\"><a href=\"javascript:void(0);\"  class=\"nav-link tip fw-bold text-white\" data-bs-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Server Uptime\"><i class=\"bi bi-clock fw-bold h6 me-2\"></i><b id=\"uptime\">00:00:00:00</b></a></li>");//UPTIME
			out.print("      </ul>");
			out.print("   </div>");
			out.print("</nav>");
		}else{
			KrystalSession krystalSession = (KrystalSession) session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
			String cssClass="";
			out.print("<nav class=\"navbar navbar-expand-xl navbar-dark bg-dark fixed-top\">");
			out.print("<a class=\"navbar-brand\" href=\"https://www.krystaldms.in/products/community\" target=\"_new\">");
			out.print("<img src=\"/images/krystal.png\" class=\"d-inline-block align-text-top mx-1 krystallogo\">");
			out.print("KRYSTAL DMS");
			out.print("</a>");
			out.print("<button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#collapsibleNavbar\" aria-controls=\"collapsibleNavbar\" aria-expanded=\"false\" aria-label=\"Toggle navigation\" data-toggle=\"collapse\" data-target=\"#collapsibleNavbar\">");
			out.print("<span class=\"navbar-toggler-icon\"></span>");
			out.print("</button>");
			out.print("<div class=\"collapse navbar-collapse\" id=\"collapsibleNavbar\">");
			out.print("<ul class=\"navbar-nav\">");
			if(request.getServletPath().startsWith("/console")){
				cssClass ="bg-primary";
			}
			out.print("<li class=\"nav-item "+cssClass+"\">");
			out.print("<a class=\"nav-link text-white\" href=\"/console\" title=\"My Workspace\"><i class=\"bi bi-house me-2 h5\"></i>My Workspace</a>");
			out.print("</li>");
			cssClass="";
			if(krystalSession.getKrystalUser().isAdmin()){
				if(request.getServletPath().startsWith("/cpanel")){
					cssClass = "bg-primary";
				}
				out.print("<li class=\"nav-item "+cssClass+"\">");
				out.print("<a class=\"nav-link text-white\" href=\"/cpanel\" title=\"Control Panel\"><i class=\"bi bi-speedometer me-2 h5\"></i>Control Panel</a>");
				out.print("</li>");
				cssClass="";
			}
			out.print("</ul>");
			out.print("<form class=\"form-inline d-inline w-100 mx-2\" role=\"form\" action=\"/console/search\" accept-charset=\"utf-8\" method=\"get\" id=\"frmGlobalSearch\" novalidate=\"novalidate\">");
			out.print("<div class=\"input-group \">");
			out.print("<input type=\"text\" name=\"txtSearch\" id=\"txtSearch\" placeholder=\"Search Documents\" class=\"form-control required\" minlength=\"3\" value=\"\">");
			out.print("");
			out.print("<button type=\"submit\" class=\"btn  btn-primary\"><i class=\"bi bi-search h5\"></i></button>");
			out.print("</div>");
			out.print("</form>");
			out.print("<ul class=\"navbar-nav ms-auto\">");
			out.print("<li class=\"nav-item bg-dark dropdown\">");
			out.println("<a href=\"javascript:void(0);\" class=\"nav-link dropdown-toggle text-white\" data-bs-toggle=\"dropdown\" title=\"My Profile\">");
			out.println("<img src=\"/console/profilepicture?size=small&username="+krystalSession.getKrystalUser().getUserName()+"\" class=\"rounded-circle\">");
			out.println("</a> ");
			out.print("<ul class=\"dropdown-menu dropdown-menu-dark dropdown-menu-end dropdown-main\">");
			out.print("<li><a href=\"/console/myprofile\" class=\"dropdown-item\"><i class=\"bi bi-person-badge h6 me-2\"></i>My Profile</a></li>");
			out.print("<li><hr class=\"dropdown-divider\"></li>");
			out.print("<li><a href=\"/logout\" class=\"confirm dropdown-item\" confirmationmessage=\"Are you sure, you want to logout?\" title=\"Logout\"><i class=\"bi bi-box-arrow-in-left h6 me-2\"></i>Logout</a></li>");
			out.print("</ul>");
			out.print("</li>");
			
			out.println("<li class=\"nav-item\">");
			out.println("<a href=\"javascript:void(0);\" class=\"nav-link tip text-white fw-bold\" id=\"timeout\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Session Timeout\"><i class=\"bi bi-hourglass-split me-2 h6\"></i><b id=\"clock\">00:00:00</b></a>");
			out.println("</li>");
			
			out.print("<li class=\"nav-item dropdown\">");
			out.print("<a href=\"javascript:void(0);\" class=\"nav-link dropdown-toggle text-white\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\"><i class=\"bi bi-question-lg me-2 h5\"></i></a>");
			out.print("<ul class=\"dropdown-menu dropdown-menu-dark dropdown-menu-end dropdown-main\">");
			out.print("<li><a href=\"https://www.krystaldms.in/resources/documentation/community/2022/user/\" class=\"dropdown-item\" target=\"_new\"><i class=\"bi bi-book h6 me-2\"></i> Users Guide</a></li>");
			if(krystalSession.getKrystalUser().isAdmin()){
				out.print("<li><a href=\"https://www.krystaldms.in/resources/documentation/community/2022/admin/\" class=\"dropdown-item\" target=\"_new\"><i class=\"bi bi-book h6 me-2\"></i> Administrators Guide</a></li>");
			}
			out.print("</ul>");
			out.print("</li>");
			out.print("</ul>");
			out.print("</div>");
			out.print("</nav>");
			
			
		}
		out.print("<div class=\"container-notification\" style=\"display:block;\"></div>");
		out.println("<div class=\"container-fluid\">");
	}

	public void generateFooter() throws Exception{
		out.println("</div>");//continer-fluid
		out.println("</div>");/*wrap ends here*/
		out.println("<div class=\"bg-dark py-3\">");
		out.print("<div class=\"container-fluid\">");
		out.print("<div class=\"row\">");
		out.print("<div class=\"col-3 text-start text-white\">");
		out.print("Community Edition 2022");
		out.print("</div>");
		out.print("<div class=\"col-6 text-sm-center text-white text-center\">");
		out.print("&nbsp;");
		out.print("</div>");
		out.print("<div class=\"col-3 text-end\">");
		out.print("<a href=\"https://www.primeleaf.in\" target=\"_new\" class=\"text-white\">Primeleaf Consulting (P) Ltd.</a>");
		out.print("</div>");
		out.print("</div>");
		out.print("</div>");
		out.print("</div>");
		out.println("</body>");
		out.println("</html>");
		
	}

}

