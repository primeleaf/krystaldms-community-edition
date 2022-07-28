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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class DocumentClassAccessHistoryReportView extends WebView {

	public DocumentClassAccessHistoryReportView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printDocumentClassAccessHistoryReports();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel/reports\">System Reports</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Document Class Access History</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printDocumentClassAccessHistoryReports() throws Exception{
		printBreadCrumbs();

		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}

		try{
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\"><i class=\"bi text-primary bi-bar-chart\"></i> Document Class Access History</div>");
			out.println("<div class=\"card-body\">");

			out.println("<form action=\"/cpanel/documentclassaccesshistory?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" autocomplete=\"off\" method=\"post\" id=\"frmReport\" class=\"form-horizontal\" accept-charset=\"utf-8\">");
			out.println("<div class=\"mb-3 row\">");
			out.println("<div class=\"offset-sm-3 col-sm-9\">");
			out.println("<p>Fields marked with <span style='color:red'>*</span> are mandatory</p>");
			out.println("</div>");
			out.println("</div>");
			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"classid\" class=\"col-sm-3 col-form-label\">Document Class <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<select id=\"classid\" name=\"classid\" class=\"form-control required\" title=\"Select Document Class\">");
			out.println("<option value=\"\">Select</option>");
			ArrayList<DocumentClass> documentClassList = (ArrayList<DocumentClass>) request.getAttribute("DOCUMENTCLASSLIST");
			int classId = 0;
			String fromDate = "";
			String toDate = "";
			if(request.getAttribute("DOCUMENTCLASS") !=null){
				fromDate =(String) request.getAttribute("FROMDATE");
				toDate = (String) request.getAttribute("TODATE");
				DocumentClass activeDocumentClass = (DocumentClass) request.getAttribute("DOCUMENTCLASS");	
				classId = activeDocumentClass.getClassId();
			}
			String selected = "";
			for(DocumentClass documentClass : documentClassList){
				selected = "";
				if(classId == documentClass.getClassId()){
					selected  = " selected";
				}
				out.println("<option  value=\""+ documentClass.getClassId() + "\" "+selected+">" + StringEscapeUtils.escapeHtml4(documentClass.getClassName()) +"</option>");
			}
			out.println("</select>");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtFromDate\" class=\"col-sm-3 col-form-label\">From</label>");
			out.println("<div class=\"col-sm-3\">");
			out.println("<div class=\"input-group\">");
			out.println("<input type=\"text\" class=\"shortdate isdate form-control\"  name=\"txtFromDate\" id=\"txtFromDate\" value=\""+fromDate+"\" maxlength=\"12\">");
			out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
			out.println("</div>");
			out.println("</div>");
			out.println("<label for=\"txtToDate\" class=\"col-sm-3 col-form-label\">To </label>");
			out.println("<div class=\"col-sm-3\">");
			out.println("<div class=\"input-group\">");
			out.println("<input type=\"text\" class=\"shortdate isdate form-control\" name=\"txtToDate\" id=\"txtToDate\" value=\""+toDate+"\" maxlength=\"12\">");
			out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
			out.println("</div>");
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

			if(request.getAttribute("ACCESSHISTORY")!=null){
				out.println("<div class=\"card   \">");
				out.println("<div class=\"card-header\"><i class=\"bi text-primary bi-clock\"></i> Document Class Access History</div>");
				
				ArrayList<AuditLogRecord>  accessHistory = (ArrayList<AuditLogRecord>)request.getAttribute("ACCESSHISTORY");
				if(accessHistory.size() > 0){
					out.println("<div class=\"table-responsive\">");
					out.println("<table class=\"table table-bordered\">");
					out.println("<thead>");
					out.println("<tr class=\"text-center\">");
					out.println("<th>Action</th>");
					out.println("<th>User</th>");
					out.println("<th>IP Address</th>");
					out.println("<th>Action Date</th>");
					out.println("<th>Type</th>");
					out.println("<th>Parameters</th>");
					out.println("<th>Comments</th>");
					out.println("</tr>");
					out.println("</thead>");
					out.println("<tbody>");
					for(AuditLogRecord accessRecord : accessHistory){
						out.println("<tr class=\"text-center\">");
						out.println("<td>" + accessRecord.getAction()+ "</td>");
						out.println("<td><a href=\"/console/userdetails?username="+accessRecord.getUserName()+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" data-bs-toggle=\"modal\" data-bs-target=\"#userDetailsModal\" title=\"User Detail\">" + accessRecord.getUserName()+ "</a></td>");
						out.println("<td>" + accessRecord.getIpAddress()+ "</td>");
						out.println("<td>" + StringHelper.formatDate(accessRecord.getActionDate())+ "</td>");
						out.println("<td>" + accessRecord.getObjectDescription()+ "</td>");
						out.println("<td>" + accessRecord.getParameters()+ "</td>");
						out.println("<td>" + StringEscapeUtils.escapeHtml4(accessRecord.getComments())+ "</td>");
						out.println("</tr>");
					}
					out.println("</tbody>");
					out.println("</table>");
					out.println("</div>");//table-responsive
					printModal("userDetailsModal");
				}else{
					out.println("<div class=\"card-body\">");
					out.println("There is no access history currently available for selected document class");
					out.println("</div>");
				}

				
				out.println("</div>");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

