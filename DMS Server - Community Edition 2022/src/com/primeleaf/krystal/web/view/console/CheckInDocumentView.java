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

import java.text.DecimalFormat;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class CheckInDocumentView extends WebView {
	public CheckInDocumentView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printCheckInDocumentForm();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Check In</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printCheckInDocumentForm() throws Exception{
		printBreadCrumbs();
		Document document = (Document) request.getAttribute("DOCUMENT");
		DocumentClass documentClass = (DocumentClass) request.getAttribute("DOCUMENTCLASS");
		LinkedHashMap<String,String> documentIndexes = (LinkedHashMap<String,String>) request.getAttribute("DOCUMENTINDEXES");
		
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccess((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		if(document != null){
			try {
				out.println("<div class=\"card\">");
				out.println("<div class=\"card-header\"><i class=\"bi text-primary bi-arrow-right\"></i> Check In - "+ documentClass.getClassName() +"</div>");
				out.println("<div class=\"card-body\">");

				out.println("<form action=\"/console/checkindocument?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmCheckInDocument\" class=\"form-horizontal\" enctype=\"multipart/form-data\" accept-charset=\"utf-8\">");
				out.println("<div class=\"mb-3 row\">");
				out.println("<div class=\"offset-sm-3 col-sm-9\">");
				out.println("<p>Fields marked with <span style='color:red'>*</span> are mandatory</p>");
				out.println("</div>");
				out.println("</div>");
				
				out.println("<div class=\"mb-3 row\">");
				out.println("<label for=\"fileDocument\" class=\"col-sm-3 col-form-label\">Select Document <span style='color:red'>*</span></label>");
				out.println("<div class=\"col-sm-9\">");
				out.println("<input type=\"file\" name=\"fileDocument\" class=\"required form-control checkExtension\" title=\"Select document of type " + document.getExtension()  + " to check-in\">");
				out.println("</div>");
				out.println("</div>");

				for(IndexDefinition indexDefinition :documentClass.getIndexDefinitions()){
					String required = "";
					out.println("<div class=\"mb-3 row\">");
					out.println("<label for=\""+indexDefinition.getIndexColumnName()+"\" class=\"col-sm-3 col-form-label\"> "+StringEscapeUtils.escapeHtml4(indexDefinition.getIndexDisplayName()));
					if(indexDefinition.isMandatory()){
						required = "required";
						out.println(" <span style='color:red'>*</span>");
					}
					out.println("</label>");

					String value = documentIndexes.get(indexDefinition.getIndexDisplayName());
					value = StringEscapeUtils.escapeHtml4(value);
					
					if(indexDefinition.getIndexType().equals(IndexDefinition.INDEXTYPE_DATE)){
						out.println("<div class=\"col-sm-9\">");
						out.println("<div class=\"input-group\">");
						out.println("<input autocomplete=\"off\" type=\"text\" class=\"shortdate isdate form-control "+ required +"\" size=\""+indexDefinition.getIndexMaxLength()+"\" name=\""+indexDefinition.getIndexColumnName()+"\" id=\""+indexDefinition.getIndexColumnName()+"\" value=\""+value+"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
						out.println("</div>");
						out.println("</div>");
					}else if(indexDefinition.getIndexType().equals(IndexDefinition.INDEXTYPE_NUMBER)){
						out.println("<div class=\"col-sm-9\">");
						out.println("<div class=\"input-group\">");
						out.println("<input type=\"text\" class=\"number  form-control "+ required +" autocomplete\"  size=\""+indexDefinition.getIndexMaxLength()+"\"  id=\""+indexDefinition.getIndexColumnName()+"\" name=\""+indexDefinition.getIndexColumnName()+"\" value=\""+value+"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"   cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-hash\"></i></span>");
						out.println("</div>");
						out.println("</div>");
					}else {
						out.println("<div class=\"col-sm-9\">");
						out.println("<div class=\"input-group\">");
						out.println("<input type=\"text\"  class=\"autocomplete form-control "+ required +" \" id=\""+indexDefinition.getIndexColumnName()+"\"  name=\""+indexDefinition.getIndexColumnName()+"\" value=\""+value+"\"maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-type\"></i></span>");
						out.println("</div>");
						out.println("</div>");
					}
					out.println("</div>");
				}
				
				double rev = Double.parseDouble(document.getRevisionId());
				DecimalFormat onePlace = new DecimalFormat("0.0");
				// For minor revision id
				double minorRevisionId = rev + 0.1;
				// For major revision id
				rev = Math.floor(rev);
				double majorRevisionId = rev + 1.0;
				
				// revision number field
				out.println("<div class=\"mb-3 row\">");
				out.println("<label for=\"version\" class=\"col-sm-3 col-form-label\">Version</label>");
				out.println("<div class=\"btn-group col-sm-9\" data-bs-toggle=\"buttons\">");
				out.println("<input class=\"btn-check\" type=\"radio\" id=\"version1\" name=\"version\" value=\"minor\" checked>");
				out.println("<label for=\"version1\" class=\"btn btn-outline-dark\">Minor ("+ onePlace.format(minorRevisionId) +")");
				out.println("</label>");
				out.println("<input class=\"btn-check\" type=\"radio\" id=\"version2\" name=\"version\"  value=\"major\">");
				out.println("<label for=\"version2\" class=\"btn btn-outline-dark\">Major ("+ onePlace.format(majorRevisionId) +")");
				out.println("</label>");
				out.println("</div>");
				out.println("</div>");

				out.println("<div class=\"mb-3 row\">");
				out.println("<label for=\"txtNote\" class=\"col-sm-3 col-form-label\">Note / Comment </label>");
				out.println("<div class=\"col-sm-9\">");
				out.println("<textarea rows=\"3\" name=\"txtNote\" id=\"txtNote\" class=\"form-control\"></textarea>"); 
				out.println("</div>"); 
				out.println("</div>");
				out.println("<hr/>");
				out.println("<div class=\"mb-3 row\">");
				out.println("<div class=\"offset-sm-3 col-sm-9\">");
				out.println("<input type=\"hidden\" name=\"documentid\" value=\""+document.getDocumentId()+"\">");
				out.println("<input type=\"hidden\" name=\"fileExtension\" id=\"fileExtension\" value=\""+document.getExtension().toUpperCase()+"\">");
				out.println("<input type=\"submit\"  name=\"btnSubmit\"  value=\"Check In\" class=\"btn  btn-dark\">");
				out.println("</div>");
				out.println("</div>");
				out.println("</form>");
				
				out.println("</div>"); //card-body
				out.println("</div>"); //panel
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
