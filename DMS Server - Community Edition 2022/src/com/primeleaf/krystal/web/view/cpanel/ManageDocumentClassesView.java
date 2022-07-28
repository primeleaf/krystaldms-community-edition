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
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class ManageDocumentClassesView extends WebView {
	public ManageDocumentClassesView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printDocumentClasses();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Manage Document Classes</li>");
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
		
		
		out.println("<div class=\"card\">");
		out.println("<div class=\"card-header\">");
		out.println("<div class=\"d-flex justify-content-between\">");
		out.println("<div class=\"col-xs-6\">");
		out.println("<i class=\"bi text-primary bi-folder2-open \"></i> Manage Document Classes");
		out.println("</div>");
		out.println("<div class=\"col-xs-6 text-end\">");
		out.println("<a href=\"/cpanel/newdocumentclass\"><i class=\"bi text-primary bi-plus-square me-2\"></i>Add Document Class</a>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		
		try {
			ArrayList<DocumentClass> documentClassList = (ArrayList<DocumentClass>) request.getAttribute("CLASSLIST");
			if(documentClassList.size() > 0 ){
				out.println("<div class=\"table-responsive\">");
				out.println("<table id=\"table\" class=\"table table-bordered table-sm table-hover mb-0\">");
				out.println("<thead>");
				out.println("<tr class=\"text-center\">");
				out.println("<th>ID</th>");
				out.println("<th>Class Name</th>");
				out.println("<th>Class Description</th>");
				out.println("<th>Active</th>");
				out.println("<th>Version Control</th>");
				out.println("<th>Action</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				for (DocumentClass documentClass : documentClassList) {
					out.println("<tr class=\"text-center\">");
					out.println("<td>"+documentClass.getClassId()+"</td>");
					out.println("<td class=\"text-start ps-5 text-primary\"><i class=\"bi bi-server me-3\"></i>" + StringEscapeUtils.escapeHtml4(documentClass.getClassName()) + "</td>");
					out.println("<td>" + StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()) + "</td>");
					if(documentClass.isVisible()){
						out.println("<td><i class=\"bi bi-check-lg text-success\"></i></td>");
					}else{
						out.println("<td><i class=\"bi bi-x-lg text-danger\"></i></td>");
					}
					
					if(documentClass.isRevisionControlEnabled()){
						out.println("<td><i class=\"bi bi-check-lg text-success\"></i></td>");
					}else{
						out.println("<td><i class=\"bi bi-x-lg text-danger\"></i></td>");
					}
					out.print("<td>");
					out.println("<a href=\""+HTTPConstants.BASEURL+"/cpanel/editdocumentclass?classid="+ documentClass.getClassId()+"\">Edit</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/deletedocumentclass?classid="+ documentClass.getClassId()+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\"  title=\"Are you sure, you want to permanently delete this Document Class?\" class=\"confirm\">Delete</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/classindexes?classid="+ documentClass.getClassId() + "\" title=\"Manage Indexes\">Manage Indexes</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/permissions?classid="+ documentClass.getClassId() + "&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" title=\"Manage Permissions\">Manage Permissions</a>");
					out.println("</td>");
					out.println("</tr>");
				}
				out.println("</tbody>");
				out.println("</table>");
				out.println("</div>");//table-responsive
			}else{
				out.println("Currently there are no document classes available in the system.");
			}
			out.println("</div>");//panel
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
