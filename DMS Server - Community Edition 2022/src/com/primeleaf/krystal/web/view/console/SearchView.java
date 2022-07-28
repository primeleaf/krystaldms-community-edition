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
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class SearchView extends WebView {
	public SearchView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printSearchResults();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Search Results</li>");
		out.println("</ol>");
	}
	private void printSearchResults() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}

		String searchText = request.getAttribute("SEARCHTEXT").toString();
		searchText = StringEscapeUtils.escapeHtml4(searchText);

		out.println("<div id=\"searchresults\">");

		out.println("<div class=\"card\">");
		out.println("<div class=\"card-header\"><i class=\"bi text-primary bi-search\"></i> Search Results : " + searchText + " </div>");
		out.println("<div class=\"card-body\">");
		out.println("Total time taken to retreive results : " + request.getAttribute("EXECUTIONTIME") + " seconds");
		out.println("</div>");
		out.println("</div>");

		printDocuments();

		out.println("</div>");

		String[] searchWords = searchText.split("\\s+");
		String classNames[] = {"highlight","btn-danger","btn-success","btn-warning","btn-dark"};
		int i = 0;
		for(String searchWord : searchWords){
			String className = classNames[i % 5];
			i++;
			out.println("<script>$(\"#searchresults \").highlight(\""+StringEscapeUtils.escapeHtml4(searchWord)+"\",  { element: 'span', className: '"+className+"' });</script>");
		}
	}


	@SuppressWarnings("unchecked")
	private void printDocuments(){
		ArrayList<DocumentClass> documentClasses = (ArrayList<DocumentClass>) request.getAttribute("DOCUMENTCLASSLIST");
		for(DocumentClass documentClass : documentClasses){
			ArrayList<Hit> hits = (ArrayList<Hit>) request.getAttribute(documentClass.getClassId() + "_HITS");
			if(hits.size() > 0 ) {
				out.println("<div class=\"card\">");
				out.println("<div class=\"card-header\">");
				out.println("<div class=\"d-flex justify-content-between\">");
				out.println("<div class=\"col-xs-8\">");
				out.println("<i class=\"bi bi-folder2-open h6 me-1 text-primary\"></i>"+StringEscapeUtils.escapeHtml4(documentClass.getClassName()) + "");
				out.println(" - "+ StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()));
				out.println("</div>");
				out.println("<div class=\"col-xs-4 text-end\">");
				out.println(hits.size() + " Documents");
				out.println("</div>");
				out.println("</div>");//row
				out.println("</div>");//card-header

				out.println("<div class=\"table-responsive\">");
				out.println("<table class=\"table table-bordered table-sm table-hover mb-0\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th class=\"text-center\">Document ID</th>");
				if(documentClass.isRevisionControlEnabled()){
					out.println("<th class=\"text-center\">Revision ID</th>");
				}
				for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
					String indexDescription = indexDefinition.getIndexDisplayName();
					out.println("<th>"+StringEscapeUtils.escapeHtml4(indexDescription)+"</th>");
				}
				out.println("<th class=\"text-end\">&nbsp;</th>");
				out.println("</tr>");
				out.println("</thead>");

				out.println("<tbody>");
				for(Hit hit : hits){
					out.println("<tr>");
					out.println("<td class=\"text-center\">"+ hit.documentId+ "</td>");
					if(documentClass.isRevisionControlEnabled()){
						out.println("<td class=\"text-center\">"+ hit.revisionId+ "</td>");
					}
					for (String value : hit.indexValues){
						out.println("<td>"+StringEscapeUtils.escapeHtml4(value)+"</td>");
					}
					out.println("<td class=\"text-end\">");
					out.println("<a href=\""+HTTPConstants.BASEURL+"/console/viewdocument?documentid=" + hit.documentId +"&revisionid="+ hit.revisionId+"\" title=\"View Document\">View Document</a>");
					out.println("</tr>");
				}
				out.println("</tbody>");
				out.println("</table>");
				out.println("</div>");//table-responsive
				out.println("</div>");//panel
			}
		}
	}

}
