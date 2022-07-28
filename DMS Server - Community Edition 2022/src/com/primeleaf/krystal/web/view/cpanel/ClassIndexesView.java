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

package com.primeleaf.krystal.web.view.cpanel;

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
public class ClassIndexesView extends WebView {
	public ClassIndexesView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printClassIndexes();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel/managedocumentclasses\">Manage Document Classes</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Manage Indexes</li>");
		out.println("</ol>");
	}

	private void printClassIndexes() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		DocumentClass documentClass = (DocumentClass)request.getAttribute("DOCUMENTCLASS");
		out.println("<div class=\"card   \">");
		out.println("<div class=\"card-header\">");
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-sm-6\">");
		out.println("<i class=\"bi text-primary bi-folder2-open\"></i> ");
		out.println(StringEscapeUtils.escapeHtml4(documentClass.getClassName())+" - ");
		out.println("<small>"+StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()) + "</small>");
		out.println("");
		out.println("</div>");
		out.println("<div class=\"col-sm-6 text-end\">");
		out.println("<i class=\"bi text-primary bi-check-square\"></i>  Available Indexes");
		out.println("</div>");
		out.println("</div>");//row
		out.println("</div>");//card-header

		try {
			if(documentClass.getIndexDefinitions().size() > 0 ){
				out.println("<form action=\"/cpanel/editclassindexes?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmEditDocumentClassIndexes\" class=\"form-horizontal\">");
				out.println("<div class=\"table-responsive\">");
				out.println("<table class=\"table table-bordered table-hover\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>Index Name</th>");
				out.println("<th class=\"text-center\">Index Length</th>");
				out.println("<th>Type</th>");
				out.println("<th>Index Display Name</th>");
				out.println("<th>Default Value</th>");
				out.println("<th>Index Order</th>");
				out.println("<th class=\"text-center\">Mandatory?</th>");
				out.println("<th class=\"text-center\">Action</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");

				for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
					String cssClass = "";
					out.println("<tr>");
					out.println("<td>"+StringEscapeUtils.escapeHtml4(indexDefinition.getIndexColumnName())+"</td>");
					out.println("<td class=\"text-center\">"+indexDefinition.getIndexMaxLength()+"</td>");
					out.println("<td>");
					if(IndexDefinition.INDEXTYPE_DATE.equalsIgnoreCase(indexDefinition.getIndexType())){
						out.println("Date");
					}else if(IndexDefinition.INDEXTYPE_STRING.equalsIgnoreCase(indexDefinition.getIndexType())){
						out.println("String");
					}else{
						out.println("Number");
						cssClass="number";
					}
					out.println("</td>");
					out.println("<td><div><input type=\"text\" name=\"txtIndexDescription"+indexDefinition.getIndexColumnName()+"\" id=\"txtIndexDescription"+indexDefinition.getIndexColumnName()+"\" value=\""+StringEscapeUtils.escapeHtml4(indexDefinition.getIndexDisplayName())+"\" class=\"required form-control\" title=\"Enter Index Description\"></div>");
					out.println("</td>");

					out.println("<td>");
					out.println("<input type=\"text\" name=\"txtDefaultValue"+indexDefinition.getIndexColumnName()+"\" id=\"txtDefaultValue"+indexDefinition.getIndexColumnName()+"\" value=\""+StringEscapeUtils.escapeHtml4(indexDefinition.getDefaultValue())+"\" maxlength=\""+indexDefinition.getIndexMaxLength()+"\" class=\"form-control "+cssClass+"\"></div>");
					out.println("</td>");

					out.println("<td>");
					out.println("<select id=\""+indexDefinition.getIndexColumnName()+"sequence\" name=\""+indexDefinition.getIndexColumnName()+"sequence\" class=\"indexOrder form-control\">");
					for(int i = 1 ; i<= documentClass.getIndexDefinitions().size(); i++){
						out.println("<option value=\""+i+"\"");
						if(i == indexDefinition.getSequence()){
							out.println("selected");
						}
						out.println(">"+i+"</option>");
					}
					out.println("</select>");
					out.println("</td>");

					out.println("<td class=\"text-center\">");
					out.println("<div class=\"btn-group\" data-bs-toggle=\"buttons\">");
					out.println("<input class=\"btn-check\" type=\"radio\" id=\""+indexDefinition.getIndexColumnName()+"radMandatory1\" name=\""+indexDefinition.getIndexColumnName()+"radMandatory\" value=\"Y\"");if(indexDefinition.isMandatory()) {out.print(" checked");}out.print("/>");
					out.println("<label for=\""+indexDefinition.getIndexColumnName()+"radMandatory1\" class=\"btn btn-outline-dark\">Yes</label>");
					out.println("<input class=\"btn-check\" type=\"radio\" id=\""+indexDefinition.getIndexColumnName()+"radMandatory2\" name=\""+indexDefinition.getIndexColumnName()+"radMandatory\"  value=\"N\"");if(!indexDefinition.isMandatory()) {out.print(" checked");}out.print("/>");
					out.println("<label for=\""+indexDefinition.getIndexColumnName()+"radMandatory2\" class=\"btn btn-outline-dark\">No</label>");
					out.println("</div>");
					out.println("</td>");

					out.println("<td class=\"text-center\">");
					out.println("<a href=\"/cpanel/deleteclassindex?name="+indexDefinition.getIndexColumnName()+"&classid="+documentClass.getClassId()+"&CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" class=\"confirm btn btn-xs btn-danger\" title=\"Are you sure, you want to delete this Index from the Document Class?\"><i class=\"bi bi-trash\"></i></a>");
					out.println("</td>");
					out.println("</tr>");
				}// for


				out.println("</tbody>");
				out.println("</table>");
				out.println("</div>");

				out.println("<div class=\"text-center card-body\">");
				out.println("<input type=\"hidden\" name=\"classid\" id=\"classid\" value=\""+documentClass.getClassId()+"\">"); 
				out.println("<input type=\"submit\" name=\"btnSubmit\"  id=\"btnSubmit\" value=\"Update\" class=\"btn  btn-dark\">");
				out.println("</div>");

				out.println("</form>");
			}else{
				out.println("<div class=\"card-body\">");
				out.println("No Index Found");
				out.println("</div>");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		out.println("</div>");

		out.println("<div class=\"card   \">");
		out.println("<div class=\"card-header\"><i class=\"bi text-primary bi-check-square\"></i> Add Index</div>");
		out.println("<div class=\"card-body\">");

		out.println("<form action=\"/cpanel/newclassindex?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmNewClassIndex\" class=\"form-horizontal\">");
		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"offset-sm-3 col-sm-9\">");
		out.println("<p>Fields marked with <span style='color:red'>*</span> are mandatory</p>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"txtIndexName\" class=\"col-sm-3 col-form-label\">Index Name <span style='color:red'>*</span></label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<input type=\"text\" id=\"txtIndexName\" name=\"txtIndexName\" class=\"form-control required\" title=\"Please enter index name\">");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"txtIndexDisplayName\" class=\"col-sm-3 col-form-label\">Index Display Name <span style='color:red'>*</span></label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<input type=\"text\" id=\"txtIndexDisplayName\" name=\"txtIndexDisplayName\" class=\"form-control required\" title=\"Please enter index display name\">");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"cmbIndexType\" class=\"col-sm-3 col-form-label\">Index Type</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<select id=\"cmbIndexType\" name=\"cmbIndexType\" class=\"form-control form-select\" title=\"Please enter index length\">");
		out.println("<option value=\""+IndexDefinition.INDEXTYPE_STRING+"\">String </option>");
		out.println("<option value=\""+IndexDefinition.INDEXTYPE_NUMBER+"\">Number</option>");
		out.println("<option value=\""+IndexDefinition.INDEXTYPE_DATE+"\">Date</option>");
		out.println("</select>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"txtIndexLength\" class=\"col-sm-3 col-form-label\">Maximum Index Length</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<input type=\"text\" id=\"txtIndexLength\" name=\"txtIndexLength\" class=\"form-control required number\" title=\"Please enter index length\">");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"radMandatory\" class=\"col-sm-3 col-form-label\">Mandatory?</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<div class=\"btn-group\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"radMandatory1\" name=\"radMandatory\" value=\"Y\"/>");
		out.println("<label for=\"radMandatory1\" class=\"btn btn-outline-dark\">Yes</label>");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"radMandatory2\" name=\"radMandatory\"  value=\"N\" checked/>");
		out.println("<label for=\"radMandatory2\" class=\"btn btn-outline-dark\">No</label>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\" id=\"defaultvalue\">");
		out.println("<label for=\"txtDefaultValue\" class=\"col-sm-3 col-form-label\">Default Value</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<input type=\"text\" id=\"txtDefaultValue\" name=\"txtDefaultValue\" class=\"form-control\" title=\"Please enter default value\">");
		out.println("</div>");
		out.println("</div>");

		out.println("<hr/>");
		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"offset-sm-3 col-sm-9\">");
		out.println("<input type=\"hidden\" name=\"classid\" value=\""+documentClass.getClassId()+"\">");
		out.println("<input type=\"submit\"  name=\"btnSubmit\"  value=\"Submit\" class=\"btn  btn-dark\">");
		out.println("</div>");
		out.println("</div>");

		out.println("</form>");
		out.println("</div>");//card-body
		out.println("</div>");//panel
	}
}