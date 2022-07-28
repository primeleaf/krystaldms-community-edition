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

package com.primeleaf.krystal.web.view.console;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class NewDocumentView extends WebView {
	public NewDocumentView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printNewDocumentForm();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Add Document</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printNewDocumentForm() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccess((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		out.println("<div class=\"card\">");
		out.println("<div class=\"card-header\"><i class=\"bi text-primary me-2 bi-cloud-upload\"></i> Add Document</div>");
		out.println("<div class=\"card-body\">");
		
		try {
			ArrayList <DocumentClass> documentClasses = (ArrayList <DocumentClass>) request.getAttribute("CLASSLIST");
			int classId = 0;
			try {
				classId  = (Integer) request.getAttribute("CLASSID");
				if(classId<1) {
					response.sendRedirect("/");
					return ;
				}
			}catch (Exception e) {
				response.sendRedirect("/");
				return ;
			}
			if(documentClasses.size() > 0){
				out.println("<form action=\"/console/newdocument\" method=\"post\" id=\"frmNewDocument\" name=\"frmNewDocument\" class=\"form-horizontal\" enctype=\"multipart/form-data\" form-type=\"ajaxUpload\" datatarget=\"#uploadResults\" accept-charset=\"utf-8\">");
				out.println("<div class=\"mb-3 row\">");
				out.println("<div class=\"offset-sm-3 col-sm-9\">");
				out.println("<p>Fields marked with <span style='color:red'>*</span> are mandatory</p>");
				out.println("</div>");
				out.println("</div>");

				out.println("<div class=\"mb-3 row\">");
				out.println("<label for=\"classid\" class=\"col-sm-3 col-form-label\">Select Document Class <span style='color:red'>*</span></label>");
				out.println("<div class=\"col-sm-9\">");
				out.println("<select id=\"classid\" name=\"classid\" class=\"form-control form-select required autosubmit\"\">");
				String selected = "";
				for(DocumentClass documentClass : documentClasses){
					selected = "";
					if(classId == documentClass.getClassId()){
						selected  = " selected";
					}
					out.println("<option  value=\""+ documentClass.getClassId() + "\" "+selected+">" + StringEscapeUtils.escapeHtml4(documentClass.getClassName())+"</option>");
				}
				out.println("</select>");
				out.println("</div>");
				out.println("</div>");

				if(classId > 0){
					DocumentClass selectedDocumentClass = (DocumentClass) request.getAttribute("DOCUMENTCLASS");

					out.println("<div class=\"mb-3 row\">");
					out.println("<label for=\"fileDocument\" class=\"col-sm-3 col-form-label\">Select Document <span style='color:red'>*</span></label>");
					out.print("<div class=\"col-sm-9\">");
					out.print("<input type=\"file\" name=\"fileDocument\" id=\"fileDocument\" class=\"form-control required isFileTypeAllowed\" title=\"Please select a document you wish to add / upload\">");
					out.print("</div>");
					out.println("</div>");

					for(IndexDefinition indexDefinition :selectedDocumentClass.getIndexDefinitions()){
						String required = "";

						out.println("<div class=\"mb-3 row\">");
						out.println("<label for=\""+indexDefinition.getIndexColumnName()+"\" class=\"col-sm-3 col-form-label\"> "+StringEscapeUtils.escapeHtml4(indexDefinition.getIndexDisplayName()));
						if(indexDefinition.isMandatory()){
							required = "required";
							out.println(" <span style='color:red'>*</span>");
						}
						out.println("</label>");
						
						int size = indexDefinition.getIndexMaxLength()>60?60:indexDefinition.getIndexMaxLength();
						String value = indexDefinition.getDefaultValue();
						if(indexDefinition.getIndexType().equals(IndexDefinition.INDEXTYPE_DATE)){
							out.println("<div class=\"col-sm-9\">");
							out.println("<div class=\"input-group\">");
							out.println("<input type=\"text\" class=\"shortdate isdate form-control "+ required +"\" size=\""+indexDefinition.getIndexMaxLength()+"\" name=\""+indexDefinition.getIndexColumnName()+"\" id=\""+indexDefinition.getIndexColumnName()+"\" value=\""+value+"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+selectedDocumentClass.getClassId()+"\">");
							out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
							out.println("</div>");
							out.println("</div>");
						}else if(indexDefinition.getIndexType().equals(IndexDefinition.INDEXTYPE_NUMBER)){
							out.println("<div class=\"col-sm-9\">");
							out.println("<div class=\"input-group\">");
							out.println("<input type=\"text\" class=\"number  form-control "+ required +" autocomplete\"  size=\""+indexDefinition.getIndexMaxLength()+"\"  id=\""+indexDefinition.getIndexColumnName()+"\" name=\""+indexDefinition.getIndexColumnName()+"\" value=\""+value+"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"   cid=\""+selectedDocumentClass.getClassId()+"\">");
							out.println("<span class=\"input-group-text\"><i class=\"bi bi-hash\"></i></span>");
							out.println("</div>");
							out.println("</div>");
						}else {
							out.println("<div class=\"col-sm-9\">");
							out.println("<div class=\"input-group\">");
							out.println("<input type=\"text\"  class=\"autocomplete form-control "+ required +" \" size=\""+size+"\" id=\""+indexDefinition.getIndexColumnName()+"\"  name=\""+indexDefinition.getIndexColumnName()+"\" value=\""+value+"\"maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+selectedDocumentClass.getClassId()+"\">");
							out.println("<span class=\"input-group-text\"><i class=\"bi bi-type\"></i></span>");
							out.println("</div>");
							out.println("</div>");
						}
						out.println("</div>");
					}

					out.println("<div class=\"mb-3 row\">");
					out.println("<label for=\"txtNote\" class=\"col-sm-3 col-form-label\">Note / Comments </label>");
					out.println("<div class=\"col-sm-9\">");
					out.println("<textarea rows=\"3\" name=\"txtNote\" id=\"txtNote\" class=\"form-control\"></textarea>"); 
					out.println("</div>"); 
					out.println("</div>");

					out.println("<hr/>");
					
					out.println("<div class=\"row mb-3\">");
					out.println("<div class=\"offset-sm-3 col-sm-9\">");
					out.println("<div class=\"progress d-none\">");
					out.println("<div class=\"progress-bar progress-bar-striped bg-success\" role=\"progressbar\" aria-valuenow=\"0\" aria-valuemin=\"0\" aria-valuemax=\"100\"></div>");
					out.println("</div>");

					out.println("<div id=\"uploadResults\" class=\"my-2\"></div>");//AJAX RESULTS WILL BE LOADED HERE

					out.println("<input type=\"hidden\" name=\"classid\" value=\""+selectedDocumentClass.getClassId()+"\">");
					out.println("<input type=\"submit\"  name=\"btnSubmit\" id=\"btnSubmit\"  value=\"Add Document\" class=\"btn btn-primary btn-wait\" data-loading-text=\"Please wait...\">");
					out.println("<input type=\"reset\" id=\"btnReset\" name=\"btnReset\" value=\"Reset\" class=\"btn btn-light\">");
					out.println("</div>"); //col-sm-9
					out.println("</div>"); //row
				}
				out.println("</form>");
			}else{
				out.println("<p class=\"text-danger\">You do not have permissions to add document</p>");
			}
			out.println("</div>");//card-body
			out.println("</div>");//panel
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}