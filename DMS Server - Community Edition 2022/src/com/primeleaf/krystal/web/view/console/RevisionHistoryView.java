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

import com.primeleaf.krystal.model.vo.RevisionRecord;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class RevisionHistoryView extends WebView {

	public RevisionHistoryView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		printRevisionHistory();
	}

	@SuppressWarnings("unchecked")
	private void printRevisionHistory() throws Exception{
		try {
			ArrayList<RevisionRecord>  revisionHistory = (ArrayList<RevisionRecord>)request.getAttribute("REVISIONHISTORY");
			out.println("<div class=\"modal-header\">");
			out.println("<h5 class=\"modal-title\" id=\"myModalLabel\"><i class=\"bi text-primary bi-clock \"></i> Revision History</h5>");
			out.println("<button type=\"button\" class=\"btn\" data-bs-dismiss=\"modal\" aria-hidden=\"true\"><i class=\"bi-x-circle\"></i></button>");
			out.println("</div>");
			out.println("<div class=\"modal-body\">");
			if(revisionHistory.size() > 0){
				out.println("<div class=\"table-responsive\">");
				out.println("<table class=\"table table-bordered table-sm\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th class=\"text-center\">Revision ID</th>");
				out.println("<th>Action</th>");
				out.println("<th>User</th>");
				out.println("<th>Date Time</th>");
				out.println("<th>&nbsp;</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				for(RevisionRecord revisionRecord : revisionHistory){
					out.println("<tr>");
					out.println("<td class=\"text-center\">" + revisionRecord.getRevisionId()+ "</td>");
					out.println("<td>" + revisionRecord.getUserAction()+ "</td>");
					out.println("<td><a href=\"/console/userdetails?username="+revisionRecord.getUserName()+"\" data-bs-toggle=\"modal\" data-bs-target=\"#userDetailsModal\" title=\"User Detail\">" + revisionRecord.getUserName()+ "</a></td>");
					out.println("<td>" + StringHelper.formatDate(revisionRecord.getDateTime())+ "</td>");
					out.println("<td style=\"width:150px;\" class=\"text-center\"><a href=\"/console/viewdocument?documentid="+revisionRecord.getDocumentId()+"&revisionid="+revisionRecord.getRevisionId()+"\" target=\"_new\">"+"View Document"+"</a></td>");
					out.println("</tr>");
				}
				out.println("</tbody>");
				out.println("</table>");
				out.println("</div>");//table-responsive
			}else{
				printInfo("There is no revision history available for selected document");
			}
			out.println("</div>");//modal-body
			
			out.println("<div class=\"modal-footer\">");
			out.println("<button type=\"button\" class=\"btn  btn-dark\" data-bs-dismiss=\"modal\">Close</button>");
			out.println("</div>");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}