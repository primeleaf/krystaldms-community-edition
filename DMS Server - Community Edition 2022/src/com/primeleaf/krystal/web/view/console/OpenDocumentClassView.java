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
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class OpenDocumentClassView extends WebView {

	public OpenDocumentClassView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printHitlist();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		DocumentClass documentClass = (DocumentClass) request.getAttribute("DOCUMENTCLASS");
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console/searchdocumentclass?classid="+documentClass.getClassId()+"\">Search Document Class</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Document Hitlist</li>");
		out.println("</ol>");
	}

	@SuppressWarnings("unchecked")
	private void printHitlist() throws Exception{
		printBreadCrumbs();
		
		DocumentClass documentClass = (DocumentClass) request.getAttribute("DOCUMENTCLASS");
		ArrayList<Hit> documentHitlist =  (ArrayList<Hit>)request.getAttribute("HITLIST");
		ACL acl = (ACL) request.getAttribute("ACL");
		int totalHits = (Integer)request.getAttribute("TOTALHITS");
		int currentPage = (Integer)request.getAttribute("PAGE");
		int pageSize = krystalSession.getKrystalUser().getHitlistSize();

		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccess((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		int startRecord = (((currentPage-1) * pageSize) +1);
		int endRecord  = (pageSize * currentPage);
		if(endRecord > totalHits){
			endRecord = totalHits;
		}
		if(documentHitlist.size() <= 0 ){
			startRecord = 0;
		}
		
		out.println("<div class=\"card\">");
		out.println("<div class=\"card-header\">");
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-sm-6 text-start\">");
		out.println("<i class=\"bi text-primary bi-list-ul\"></i> Document Hitlist - ");
		out.println("<small>Showing " +  startRecord  + " to "  + endRecord  + " of " + totalHits+"</small>");
		out.println("</div>");
		out.println("<div class=\"col-sm-6 text-end\">");
		out.println("<i class=\"bi text-primary bi-folder2-open\"></i> ");
		out.println(StringEscapeUtils.escapeHtml4(documentClass.getClassName())+" - ");
		out.println("<small>"+StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()) + "</small>");
		out.println("");
		out.println("</div>");
		out.println("</div>");//row
		out.println("</div>");//card-header
		
		if(documentHitlist.size() > 0 ){
			out.println("<form action=\"/console/bulkdelete\" method=\"post\" name=\"frmBulkAction\" id=\"frmBulkAction\" class=\"form-horizontal\">");
			out.println("<input type=\"hidden\" name=\"CSRF\" value=\""+session.getAttribute(HTTPConstants.CSRF_STRING)+"\">");
			out.println("<div class=\"table-responsive\">");
			out.println("<table id=\"table\" class=\"table table-bordered table-sm table-hover mb-0 hitlist\">");
			out.println("<thead>");
			out.println("<tr>");
			if(acl.canDelete() || acl.canDownload()){
				out.println("<th class=\"text-center align-middle\"><input type=\"checkbox\" id=\"checkAll\"/></th>");
			}
			out.println("<th></th>");
			out.println("<th class=\"text-center align-middle\"><i class=\"bi bi-file-earmark h6 text-primary\"></i></th>");
			if(krystalSession.getKrystalUser().getMetaPreferences().isDocumentIdVisible()){
				printHitListColumnHeader("DOCUMENTID", "Document ID");
			}
			if(krystalSession.getKrystalUser().getMetaPreferences().isRevisionIdVisible()){
				printHitListColumnHeader("REVISIONID", "Revision ID");
			}
			if(krystalSession.getKrystalUser().getMetaPreferences().isCreatedByVisible()){
				printHitListColumnHeader("CREATEDBY", "Created By");
			}
			if(krystalSession.getKrystalUser().getMetaPreferences().isModifiedByVisible()){
				printHitListColumnHeader("MODIFIEDBY", "Last Modified By");
			}
			for (IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
				printHitListColumnHeader(indexDefinition.getIndexColumnName(), StringEscapeUtils.escapeHtml4(indexDefinition.getIndexDisplayName()));
			}
			if(krystalSession.getKrystalUser().getMetaPreferences().isFileSizeVisible()){
				printHitListColumnHeader("LENGTH", "File Size");
			}
			if(krystalSession.getKrystalUser().getMetaPreferences().isCreatedVisible()){
				printHitListColumnHeader("CREATED", "Created On");
			}
			if(krystalSession.getKrystalUser().getMetaPreferences().isModifiedVisible()){
				printHitListColumnHeader("MODIFIED", "Last Modified On");
			}
			if(krystalSession.getKrystalUser().getMetaPreferences().isExpiryOnVisible()){
				printHitListColumnHeader("EXPIRY", "Expiry On");
			}
			out.println("<th></th>");
			out.println("</tr>");
			out.println("</thead>");
			out.println("<tbody>");
			for(Hit hit : documentHitlist){
				if(hit.userName.trim().length() > 0){
					out.println("<tr class=\"table-danger\">");	
				}else{
					out.println("<tr>");
				}
				if(acl.canDelete() || acl.canDownload()){
					if(hit.userName.trim().length() <= 0  && hit.isHeadRevision){
						out.println("<td class=\"text-center align-middle\">");
						out.println("<input type=\"checkbox\" name=\"chkDocumentId\" id=\"chkDocumentID\" value=\""+hit.documentId+"\" class=\"required\" title=\"Please select at least one document to peform an action.\" >");
						out.println("</td>");
					}
				}
				
				out.println("<td class=\"text-center align-middle\">");
				if(hit.userName.trim().length() > 0){
					out.println("<i class=\"bi bi-lock tip\" data-bs-toggle=\"tooltip\" data-bs-placement=\"right\" title=\"Locked By : " + hit.userName.toUpperCase() +"\"></i> ");
					out.println("<td class=\"text-center align-middle\">");
					out.println("&nbsp;");
					out.println("</td>");
				}
				out.println("</td>");
				out.print("<td class=\"text-center\">");
				out.print("<a href=\"/console/viewdocument?view=quick&documentid="+hit.documentId+"&revisionid="+hit.revisionId+"\" class=\"viewdocument tip\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"Quick View Document\">");
				out.println(StringHelper.getIconFileTag(hit.extension));
				out.print("</a>");
				out.println("</td>");
				
				if(krystalSession.getKrystalUser().getMetaPreferences().isDocumentIdVisible()){
					out.println("<td class=\"text-center align-middle\">"+hit.documentId+"</td>");
				}
				if(krystalSession.getKrystalUser().getMetaPreferences().isRevisionIdVisible()){
					out.println("<td class=\"text-center align-middle\">"+hit.revisionId+"</td>");
				}
				if(krystalSession.getKrystalUser().getMetaPreferences().isCreatedByVisible()){
					out.println("<td class=\"text-center align-middle\"><a href=\"/console/userdetails?username="+hit.createdBy+"\" data-bs-toggle=\"modal\" data-bs-target=\"#userDetailsModal\" title=\"User Detail\">" + hit.createdBy+ "</a></td>");
				}
				if(krystalSession.getKrystalUser().getMetaPreferences().isModifiedByVisible()){
					out.println("<td class=\"text-center align-middle\"><a href=\"/console/userdetails?username="+hit.modifiedBy+"\" data-bs-toggle=\"modal\" data-bs-target=\"#userDetailsModal\" title=\"User Detail\">" + hit.modifiedBy+ "</a></td>");
				}
				for (String value : hit.indexValues){
					out.println("<td class=\"text-center align-middle\">"+StringEscapeUtils.escapeHtml4(value)+"</td>");
				}
				if(krystalSession.getKrystalUser().getMetaPreferences().isFileSizeVisible()){
					out.println("<td class=\"text-center align-middle\">"+StringHelper.formatSizeText(hit.fileLength)+"</td>");
				}
				if(krystalSession.getKrystalUser().getMetaPreferences().isCreatedVisible()){
					out.println("<td class=\"text-center align-middle\">"+StringHelper.formatDate(hit.created)+"</td>");
				}
				if(krystalSession.getKrystalUser().getMetaPreferences().isModifiedVisible()){
					out.println("<td class=\"text-center align-middle\">"+StringHelper.formatDate(hit.modified)+"</td>");
				}
				if(krystalSession.getKrystalUser().getMetaPreferences().isExpiryOnVisible()){
					out.println("<td class=\"text-center align-middle\">");
					if(hit.expiryOn != null){
						out.println(StringHelper.formatDate(hit.expiryOn,ServerConstants.FORMAT_SHORT_DATE));
					}else{
						out.println("&nbsp;");
					}
					out.println("</td>");
				}
				out.println("<td class=\"text-center align-middle\" style=\"width:100px;\">");
				out.println("<a href=\"/console/viewdocument?documentid="+hit.documentId+"&revisionid="+hit.revisionId+"\" title=\"View Document\">");
				out.println("View Document");
				out.println("</a>");
				out.println("</td>");

				out.println("</tr>");
			}	
			out.println("</tbody>");
			out.println("</table>");
			out.println("</div>");//table-responsive
			out.println("</form>");

			out.println("<div class=\"card-footer\">");
			generatePagination("/console/opendocumentclass");
			out.println("<div id=\"errorWrapper\"><div id=\"errorDiv\"></div></div>");
			generateHistlistFooter(documentClass,acl);
			out.println("</div>");//card-body
			printModal("userDetailsModal");
		}else{
			out.println("<div class=\"card-body\">");
			out.println("There are no documents found");
			out.println("</div>");//card-body
		}
		out.println("</div>");//panel
		out.println("<script>");
		out.println("    $(document).ready(function(){");
		out.println("  $(\"#searchDocumentHitlist\").on(\"keyup\", function() {");
		out.println("    var value = $(this).val().toLowerCase();"
				+ "console.log(value);");
		out.println("    $(\"#documentHitlistTable tbody tr\").filter(function() {");
		out.println("      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)");
		out.println("    });");
		out.println("  });");
		out.println("});");
		out.println("</script>");
	}

	private void generateHistlistFooter(DocumentClass documentClass, ACL acl) throws Exception{
		Enumeration<String> enumRequest = request.getParameterNames(); // get all the requested parameters
		StringBuffer queryString = new StringBuffer("/console/opendocumentclass?");
		while(enumRequest.hasMoreElements()){
			String parameterName = (String) enumRequest.nextElement();
			if(parameterName.equalsIgnoreCase("page")){
				continue;
			}
			String parameterValue = request.getParameter(parameterName);
			queryString.append("&");
			queryString.append(parameterName);
			queryString.append("=");
			queryString.append(parameterValue);
		}
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-lg-6\">");
		if(acl.canDelete()){
			out.println("<a href=\"javascript:void(0);\" id=\"btnBulkDelete\" class=\"text-danger\"/>Delete</a> | ");
		}
		if(acl.canDownload()){
			out.println("<a href=\"javascript:void(0);\" id=\"btnBulkDownload\"/>Download as Zip</a> | ");
		}
		out.println("<a href=\""+StringEscapeUtils.escapeHtml4(queryString.toString()) +"&mode=pdf&\">Export as PDF</a>");
		out.println("</div>");
		out.println("<div class=\"col-lg-6 text-end\">");
		out.println("<small>Total time taken to retreive the results <i>" + request.getAttribute("EXECUTIONTIME") + " seconds</i></small>");
		out.println("</div>");
		out.println("</div>");
		out.println("<script>"
				+ "$(\"#btnBulkDownload\").click(function() {\r\n"
				+ "	    if ($(\"#frmBulkAction\").valid()) {\r\n"
				+ "        	$(\"#frmBulkAction\").attr('action','/console/bulkdownload');"
				+ "        	$(\"#frmBulkAction\").submit();\r\n"
				+ "        }\r\n"
				+ "	});"
				+ "$(\"#btnBulkDelete\").click(function() {\r\n"
				+ "	    if ($(\"#frmBulkAction\").valid()) {\r\n"
				+ "	    	BootstrapDialog.confirm(\"Are you sure, you want to delete these documents?\", function(result){\r\n"
				+ "	            if(result) {\r\n"
				+ "	            	$(\"#frmBulkAction\").submit();\r\n"
				+ "	            }\r\n"
				+ "	    	});\r\n"
				+ "	    }\r\n"
				+ "	});"
				+ "</script>");
	}
}