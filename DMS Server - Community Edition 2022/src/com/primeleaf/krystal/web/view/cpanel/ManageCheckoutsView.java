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

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class ManageCheckoutsView extends WebView {
	public ManageCheckoutsView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printCheckOuts();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Manage Checked Out Documents</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printCheckOuts() throws Exception{
		printBreadCrumbs() ;

		
		
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		try {
			ArrayList<DocumentClass> documentClassList = (ArrayList<DocumentClass>) request.getAttribute("CLASSLIST");
			ArrayList<User> userList = (ArrayList<User>) request.getAttribute("USERLIST");
			String selectedDocumentClass = (String) request.getAttribute("DOCUMENTCLASS");
			String selectedUser = (String) request.getAttribute("USER");

			ArrayList<CheckedOutDocument> checkedOutDocuments =  (ArrayList<CheckedOutDocument>)request.getAttribute("CHECKOUTLIST");
			if(checkedOutDocuments != null){
				if(checkedOutDocuments.size() > 0 ){
					out.println("<div class=\"card\">");
					out.println("<div class=\"card-header\">");
					out.println("<i class=\"bi bi-card-checklist text-primary\"></i> Checked Out Documents");
					out.println("</div>");

					out.println("<div class=\"card-body p-2\">");
					out.println("<div class=\"row ps-3 pe-3\"><div class=\"col-sm-9 fs-6 text-primary d-md-flex align-items-center justify-content-end\"><span> Search : </span></div>");
					out.println("<input class=\"required form-control input-md search col\" id=\"searchTable\" type=\"text\"></div>");
					out.println("<div class=\"table-responsive mt-3\">");
					out.println("<table id=\"table\" class=\"table table-bordered table-sm table-hover\" style=\"margin-bottom:0rem;font-size:12px;\">");
					out.println("<thead>");
					out.println("<tr class=\"text-center\">");
					out.println("<th>Document ID</th>");
					out.println("<th>Revision ID</th>");
					out.println("<th>Document Name</th>");
					out.println("<th>Document Class</th>");
					out.println("<th>User Name</th>");
					out.println("<th>Date Time</th>");
					out.println("<th>Action</th>");
					out.println("</tr>");
					out.println("</thead>");
					out.println("<tbody>");
					
					for (CheckedOutDocument checkedOutDocument : checkedOutDocuments) {
						File checkedOutFile = new File(checkedOutDocument.getCheckOutPath().toLowerCase());
						out.println("<tr class=\"text-center\">");
						out.println("<td>"+checkedOutDocument.getDocumentId()+"</td>");
						out.println("<td>"+checkedOutDocument.getRevisionId()+"</td>");
						out.println("<td>"+checkedOutFile.getName()+"</td>");
						out.println("<td>"+checkedOutDocument.getDocumentClass().getClassName()+"</td>");
						out.println("<td><a href=\"/console/userdetails?username="+checkedOutDocument.getUserName()+"\" data-bs-toggle=\"modal\" data-bs-target=\"#userDetailsModal\" title=\"User Detail\">" + checkedOutDocument.getUserName()+ "</a></td>");
						out.println("<td>"+StringHelper.formatDate(checkedOutDocument.getCheckOutDate()) + " ,  " + StringHelper.getFriendlyDateTime( checkedOutDocument.getCheckOutDate())+"</td>");
						out.print("<td>");
						out.println("<a href=\"/cpanel/cancelcheckoutadmin?documentid="+checkedOutDocument.getDocumentId()+"&revisionid="+checkedOutDocument.getRevisionId()+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" class=\"confirm\" title=\"Are you sure, you want to cancel this checkout?\">Cancel Checkout</a>");
						out.println(" | <a href=\"/console/viewdocument?documentid="+checkedOutDocument.getDocumentId()+"&revisionid="+checkedOutDocument.getRevisionId()+"&\">View Document</a>");
						out.println(" | <a href=\"/console/checkindocument?documentid="+checkedOutDocument.getDocumentId()+"\">CheckIn Document</a>");
						out.print("</td>");
						out.println("</tr>");
					}
					
					out.println("</tbody>");
					out.println("</table>");
					out.println("</div>");//table-responsive
					out.println("</div>");
					out.println("</div>");
				}else{
					printInfoDismissable("There are no documents checked out");
				}
				
			}
			
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\">");
			out.println("<i class=\"bi bi-lock text-primary\"></i> Manage Checkouts");
			out.println("</div>");

			out.println("<div class=\"card-body\">");
			

			out.println("<form action=\"/cpanel/managecheckouts\" method=\"post\" id=\"frmManageCheckouts\" class=\"form-horizontal\">");
			out.println("<div class=\"mb-3 row\">");
			out.println("<div class=\"offset-sm-3 col-sm-9\">");
			out.println("<p>Fields marked with <span style='color:red'>*</span> are mandatory</p>");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"cmbDocumentClass\" class=\"col-sm-3 col-form-label\">Document Class <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<select name=\"cmbDocumentClass\" class=\"form-control form-select required\">");
			out.println("<option value=\"ALL\">All Document Classes</option>");
			String selected = "";
			for(DocumentClass documentClass : documentClassList){
				selected = "";
				if(documentClass.getClassName().equalsIgnoreCase(selectedDocumentClass)){
					selected = " selected";
				}
				out.println("<option value=\""+documentClass.getClassId()+"\"" +selected+ ">"+documentClass.getClassName()+"</option>");
			}
			out.println("</select>");
			out.println("</div>");
			out.println("</div>");

			out.println("<div class=\"mb-3 row\">");
			out.println("<label for=\"txtListDescription\" class=\"col-sm-3 col-form-label\">User <span style='color:red'>*</span></label>");
			out.println("<div class=\"col-sm-9\">");
			out.println("<select name=\"cmbUser\" class=\"form-control form-select required\">");
			out.println("<option value=\"ALL\">All Users</option>");
			for(User user : userList){
				selected = "";
				if(user.getUserName().equalsIgnoreCase(selectedUser)){
					selected = " selected";
				}
				out.println("<option value=\""+user.getUserName()+"\"" +selected+ ">"+user.getUserName()+" ("+ user.getRealName()+")</option>");
			}
			out.println("</select>");
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
			printModal("userDetailsModal");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}