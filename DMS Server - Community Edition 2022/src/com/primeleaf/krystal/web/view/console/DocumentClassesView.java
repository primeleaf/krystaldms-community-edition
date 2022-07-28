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

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class DocumentClassesView extends WebView {
	public DocumentClassesView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printDocumentClasses();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Document Classes</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printDocumentClasses() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		try {
			out.println("<div class=\"card   \">");
			out.println("<div class=\"card-header\"><h4><i class=\"bi  bi-folder2-open\"></i> Document Classes</h4></div>");

			ArrayList <DocumentClass> 	documentClassList =  (ArrayList <DocumentClass>) request.getAttribute("DOCUMENTCLASSLIST");
			if(documentClassList.size() > 0 ){
				out.println("<ul class=\"list-group\">");
				for(DocumentClass documentClass : documentClassList){
					ACL acl = (ACL)request.getAttribute(documentClass.getClassName()+"_ACL");

					out.println("<li class=\"list-group-item\">");
					out.println("<div class=\"row\">");

					out.println("<div class=\"col-sm-9 col-xs-12\">");
					out.println("<a href=\"/console/opendocumentclass?classid="+ documentClass.getClassId()+"&CSRF="+session.getAttribute("CSRF")+"\"  class=\"text-danger\">");
					out.println("<h4>"+StringEscapeUtils.escapeHtml4(documentClass.getClassName())+"</h4>");
					out.println("</a>");
					out.println("<h5>" + StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()) + "</h5>");

					out.println("<p><h6>");
					out.println("<a href=\"/console/searchdocumentclass?classid="+ documentClass.getClassId()+"?&CSRF="+session.getAttribute("CSRF")+"\">Search</a>");
					if(acl.canCreate()){
						out.println(" | <a href=\"/console/newdocument?classid="+ documentClass.getClassId()+"&CSRF="+session.getAttribute("CSRF")+"\">Add Document</a>");
						out.println(" | <a href=\"/console/bulkupload?classid="+ documentClass.getClassId()+"&CSRF="+session.getAttribute("CSRF")+"\">Bulk Upload</a>");
					}
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/console/subscriptions?classid="+ documentClass.getClassId() + "\">Manage Subscriptions</a>");
					out.println(" | <a href=\"/console/documentclassproperties?classid="+documentClass.getClassId()+"\" data-bs-toggle=\"modal\" data-bs-target=\"#documentClassPropertiesModal\">Properties</a>");
					out.println("</h6></p>");
					out.println("</div>");

					Calendar cal=Calendar.getInstance();
					java.sql.Date to=new java.sql.Date(cal.getTimeInMillis());
					String toDate =  StringHelper.formatDate(to, "dd-MMM-yyyy").toUpperCase();

					cal=Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, 1);
					cal.add(Calendar.DAY_OF_MONTH, -7);
					java.sql.Date from = new java.sql.Date(cal.getTimeInMillis());
					String fromDate = StringHelper.formatDate(from, "dd-MMM-yyyy").toUpperCase();

					int weekCount = (Integer)request.getAttribute(documentClass.getClassName() + "_WEEK_COUNT");

					out.println("<div class=\"col-sm-1 col-xs-4 text-end\">");
					out.println("<a href=\"/console/opendocumentclass?classid="+documentClass.getClassId()+"&fromDate="+fromDate+"&toDate="+toDate+"\"  title=\"New Documents\" class=\"text-info\">");
					out.println("<h3 class=\"odometer weekdocs"+documentClass.getClassId()+"\">0</h3>");
					out.println("<h6>New Documents</h6></a>");
					out.println("</div>");
					if(weekCount > 0 ){
						out.println("<script>setTimeout(function(){$('.weekdocs"+documentClass.getClassId()+"').html('"+weekCount+"');},1000);</script>");
					}
					
					cal=Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, documentClass.getExpiryNotificationPeriod());
					from = new java.sql.Date(cal.getTimeInMillis());
					fromDate = StringHelper.formatDate(from, "dd-MMM-yyyy").toUpperCase();
					int expiringDocuments = (Integer) request.getAttribute(documentClass.getClassName() + "_EXPIRY_COUNT");

					out.println("<div class=\"col-sm-1  col-xs-4 text-end\">");
					out.println("<a href=\"/console/opendocumentclass?fromExpiryDate="+toDate+"&toExpiryDate="+fromDate+"&classid="	+ documentClass.getClassId() + "\" class=\"text-danger\"  title=\"Expiring Documents\">");
					out.println("<h3 class=\"odometer expdocs"+documentClass.getClassId()+"\">0</h3>");
					out.println("<h6>Expiring Documents</h6></a>");
					out.println("</div>");
					if(expiringDocuments > 0 ){
						out.println("<script>setTimeout(function(){$('.expdocs"+documentClass.getClassId()+"').html('"+expiringDocuments+"');},1000);</script>");
					}

					int documentCount = documentClass.getActiveDocuments();
					out.println("<div class=\"col-sm-1 col-xs-4 text-end\">");
					out.println("<a href=\"/console/opendocumentclass?classid="+ documentClass.getClassId()+"\" title=\"Total Documents\" class=\"text-success\">");
					out.println("<h3 class=\"odometer totaldocs"+documentClass.getClassId()+"\">0</h3>");
					out.println("<h6>Total Documents</h6></a>");
					out.println("</div>");
					if(documentCount > 0 ){
						out.println("<script>setTimeout(function(){$('.totaldocs"+documentClass.getClassId()+"').html('"+documentCount+"');},1000);</script>");
					}
					
					out.println("</div>");//row
					out.println("</li>");//list-group-item

				}// for
				out.println("</div>");

				out.println("<div class=\"card-body\">");
				generatePagination("/console/documentclasses");
				out.println("</div>");//card-body
			}else{
				out.println("<div class=\"card-body\">");
				out.println("There are no Document Classes available");
				out.println("</div>");//card-body
			}
			
			out.println("<div class=\"card card-body pr-2 pr-2 row g-2 text-end\">");
			out.println("Time taken to retreive results : <i>" + request.getAttribute("EXECUTIONTIME") + " seconds</i>");
			out.println("</div>");
			
			out.println("</div>"); //panel
			printModal("documentClassPropertiesModal");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}