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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class SearchDocumentClassView extends WebView {
	public SearchDocumentClassView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printDocumentSearchForm();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Search Document Class</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printDocumentSearchForm() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccess((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		try {
			DocumentClass documentClass = (DocumentClass) request.getAttribute("DOCUMENTCLASS");
			ArrayList<User> userList = (ArrayList<User>) request.getAttribute("USERLIST");
			out.println("<div class=\"card   \">");
			out.println("<div class=\"card-header\">");
			out.println("<div class=\"row\">");
			out.println("<div class=\"col-sm-3\"><i class=\"bi text-primary bi-search\"></i>  Search Document Class</div>");
			out.println("<div class=\"col-sm-9 text-end\">");
			out.println("<i class=\"bi text-primary bi-folder2-open\"></i>  ");
			out.println(StringEscapeUtils.escapeHtml4(documentClass.getClassName()) + " - ");
			out.println("<small>" + StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()) + "</small>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("<div class=\"card-body\">");
			
			out.println("<form action=\"/console/opendocumentclass?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"get\" id=\"frmDocumentFilter\" class=\"form-horizontal\" accept-charset=\"utf-8\">");
			if(documentClass.getIndexDefinitions().size() > 0 ){
				out.println("<fieldset>");
				out.println("<legend>Available Indexes</legend>");
				for(IndexDefinition indexDefinition :documentClass.getIndexDefinitions()){
					out.println("<div class=\"mb-3 row\">");
					out.println("<label for=\""+indexDefinition.getIndexColumnName()+"\" class=\"col-sm-3 col-form-label\"> "+StringEscapeUtils.escapeHtml4(indexDefinition.getIndexDisplayName()));
					out.println("</label>");
					out.println("<div class=\"col-sm-3\">");
					showCriteriaDropdown(indexDefinition.getIndexColumnName(),(byte)1);	
					out.println("</div>");

					if(indexDefinition.getIndexType().equals(IndexDefinition.INDEXTYPE_DATE)){
						out.println("<div class=\"col-sm-3\">");
						out.println("<div class=\"input-group\">");
						out.println("<input autocomplete=\"off\" type=\"text\" class=\"shortdate  col-xs-2 form-control\"  name=\""+indexDefinition.getIndexColumnName()+"\" id=\""+indexDefinition.getIndexColumnName()+"\" value=\"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-event\"></i></span>");
						out.println("</div>");
						out.println("</div>");

						out.println("<div class=\"col-sm-3 hidden\"  id=\""+indexDefinition.getIndexColumnName()+"_div\">");
						out.println("<div class=\"input-group\">");
						out.println("<input type=\"text\" class=\"shortdate  col-xs-2 form-control\" name=\""+indexDefinition.getIndexColumnName()+"_2\" id=\""+indexDefinition.getIndexColumnName()+"\" value=\"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-event\"></i></span>");
						out.println("</div>");
						out.println("</div>");

					}else if(indexDefinition.getIndexType().equals(IndexDefinition.INDEXTYPE_NUMBER)){
						out.println("<div class=\"col-sm-3\">");
						out.println("<div class=\"input-group\">");
						out.println("<input type=\"text\" class=\"number  form-control\" name=\""+indexDefinition.getIndexColumnName()+"\"  id=\""+indexDefinition.getIndexColumnName()+"\"  value=\"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"   cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-hash\"></i></span>");
						out.println("</div>");
						out.println("</div>");
						
						out.println("<div class=\"col-sm-3 hidden\"  id=\""+indexDefinition.getIndexColumnName()+"_div\">");
						out.println("<div class=\"input-group\">");
						out.println("<input type=\"text\" class=\"number  form-control autocomplete\" name=\""+indexDefinition.getIndexColumnName()+"_2\" id=\""+indexDefinition.getIndexColumnName()+"\"value=\"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"   cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-hash\"></i></span>");
						out.println("</div>");
						out.println("</div>");
					}else {
						out.println("<div class=\"col-sm-3\">");
						out.println("<div class=\"input-group\">");
						out.println("<input type=\"text\"  class=\"form-control \" name=\""+indexDefinition.getIndexColumnName()+"\" id=\""+indexDefinition.getIndexColumnName()+"\"   value=\"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-type\"></i></span>");
						out.println("</div>");
						out.println("</div>");

						out.println("<div class=\"col-sm-3 hidden\"  id=\""+indexDefinition.getIndexColumnName()+"_div\">");
						out.println("<div class=\"input-group\">");
						out.println("<input type=\"text\"  class=\"autocomplete form-control \"  name=\""+indexDefinition.getIndexColumnName()+"_2\" id=\""+indexDefinition.getIndexColumnName()+"\" value=\"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\"  cid=\""+documentClass.getClassId()+"\">");
						out.println("<span class=\"input-group-text\"><i class=\"bi bi-type\"></i></span>");
						out.println("</div>");
						out.println("</div>");
					}
					out.println("</div>");
				}
				
			}
			printDocumentPropertiesFilter(documentClass,userList);
			
			out.println("<hr/>");
			out.println("<div class=\"mb-3 row\">");
			out.println("<div class=\"offset-sm-3 col-sm-9\">");
			out.println("<input type=\"hidden\"  name=\"classid\"  value=\""+ documentClass.getClassId() + "\"/>");
			out.println("<input type=\"submit\"  name=\"btnSubmit\"  value=\"Show Hits\" class=\"btn  btn-dark\">");
			out.println("</div>");
			out.println("</div>");
			out.println("</fieldset>");
			out.println("</form>");
			out.println("</div>");//card-body
			out.println("</div>");//panel
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printDocumentPropertiesFilter(DocumentClass documentClass, ArrayList<User> userList) throws Exception {
		
		out.println("<legend>Document Properties</legend>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"txtDocumentId\" class=\"col-sm-3 col-form-label\"> "+"Document ID"+"</label>");
		out.println("<div class=\"col-sm-9\"><div class=\"input-group\">");
		out.println("<input  type=\"text\" name=\"txtDocumentId\" maxlength=\"8\" class=\"form-control digits\">");
		out.println("<span class=\"input-group-text\"><i class=\"bi bi-hash\"></i></span>");
		out.println("</div></div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"fromDate\" class=\"col-sm-3 col-form-label\"> Created On</label>");
		out.println("<div class=\"col-sm-3\">");
		showMetaCriteriaDropdown("created");	
		out.println("</div>");
		out.println("<div class=\"col-sm-3\">");
		out.println("<div class=\"input-group\">");
		out.println("<input  type=\"text\" class=\"shortdate form-control\"  id=\"fromDate\" name=\"fromDate\">");
		out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"col-sm-3\">");
		out.println("<div class=\"input-group\">");
		out.println("<input  type=\"text\" class=\"shortdate form-control\" id=\"toDate\" name=\"toDate\">");
		out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"fromModifiedDate\" class=\"col-sm-3 col-form-label\"> Last Modified On</label>");
		out.println("<div class=\"col-sm-3\">");
		showMetaCriteriaDropdown("modified");	
		out.println("</div>");
		out.println("<div class=\"col-sm-3\">");
		out.println("<div class=\"input-group\">");
		out.println("<input  type=\"text\" class=\"shortdate form-control\"  id=\"fromModifiedDate\" name=\"fromModifiedDate\">");
		out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
		out.println("</div>");
		out.println("</div>");
		out.println("<div class=\"col-sm-3\">");
		out.println("<div class=\"input-group\">");
		out.println("<input  type=\"text\" class=\"shortdate form-control\" id=\"toModifiedDate\" name=\"toModifiedDate\">");
		out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"fromModifiedDate\" class=\"col-sm-3 col-form-label\"> Expiry On</label>");
		out.println("<div class=\"col-sm-3\">");
		showMetaCriteriaDropdown("expiry");	
		out.println("</div>");
		out.println("<div class=\"col-sm-3\">");
		out.println("<div class=\"input-group\">");
		out.println("<input  type=\"text\" class=\"shortdate form-control\"  id=\"fromExpiryDate\" name=\"fromExpiryDate\">");
		out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
		out.println("</div>");
		out.println("</div>");
		out.println("<div class=\"col-sm-3\">");
		out.println("<div class=\"input-group\">");
		out.println("<input  type=\"text\" class=\"shortdate form-control\" id=\"toExpiryDate\" name=\"toExpiryDate\">");
		out.println("<span class=\"input-group-text\"><i class=\"bi bi-calendar-date\"></i></span>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"createdBy\" class=\"col-sm-3 col-form-label\"> Created By</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<select name=\"createdBy\" class=\"form-control form-select\">");
		out.println("<option value=\"\">All Users</option>");
		for(User user:userList){
			out.println("<option value=\""+user.getUserName()+"\">"	+ user.getRealName() +"</option>");
		}
		out.println("</select>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"modifiedBy\" class=\"col-sm-3 col-form-label\"> Last Modified By</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<select name=\"modifiedBy\" class=\"form-control form-select\">");
		out.println("<option value=\"\">All Users</option>");
		for(User user:userList){
			out.println("<option value=\""+user.getUserName()+"\">"	+ user.getRealName() +"</option>");
		}
		out.println("</select>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"offset-sm-3 col-sm-9\">");
		out.println("<div class=\"checkbox\"><label>");
		out.println("<input type=\"checkBox\" value=\"Y\" name=\"chkAll\">&nbsp; Search All Revisions");
		out.println("</label>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
	}
}