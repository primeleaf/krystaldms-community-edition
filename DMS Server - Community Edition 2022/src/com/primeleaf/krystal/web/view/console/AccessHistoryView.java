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

import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class AccessHistoryView extends WebView {

	public AccessHistoryView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		printAccessHistory();
	}

	@SuppressWarnings("unchecked")
	private void printAccessHistory() throws Exception{
		try {
			ArrayList<AuditLogRecord>  accessHistory = (ArrayList<AuditLogRecord>)request.getAttribute("ACCESSHISTORY");
			
			if(accessHistory.size() > 0){
				out.println("<div class=\"card\">");
				out.println("<div class=\"card-header\"><i class=\"bi text-primary bi-clock \"></i> Access History</div>");
				out.println("<div class=\"table-responsive\">");
				out.println("<table class=\"table text-center table-bordered mb-0 table-sm\">");
				out.println("<thead>");
				out.println("<tr>");
				out.println("<th>Action</th>");
				out.println("<th>User</th>");
				out.println("<th>IP Address</th>");
				out.println("<th>Action Date</th>");
				out.println("<th>Type</th>");
				out.println("<th>Parameters</th>");
				out.println("<th>Comments</th>");
				out.println("</tr>");
				out.println("</thead>");
				out.println("<tbody>");
				for(AuditLogRecord accessRecord : accessHistory){
					out.println("<tr>");
					out.println("<td>" + accessRecord.getAction()+ "</td>");
					out.println("<td><a href=\"/console/userdetails?username="+accessRecord.getUserName()+"\" data-bs-toggle=\"modal\" data-bs-target=\"#userDetailsModal\" title=\"User Detail\">" + accessRecord.getUserName()+ "</a></td>");
					out.println("<td>" + accessRecord.getIpAddress()+ "</td>");
					out.println("<td>" + StringHelper.formatDate(accessRecord.getActionDate())+ "</td>");
					out.println("<td>" + accessRecord.getObjectDescription()+ "</td>");
					out.println("<td>" + accessRecord.getParameters()+ "</td>");
					out.println("<td>" + StringEscapeUtils.escapeHtml4(accessRecord.getComments())+ "</td>");
					out.println("</tr>");
				}
				out.println("</tbody>");
				out.println("</table>");
				out.println("</div>");//table-responsive
				out.println("</div>");//panel
			}else{
				printInfo("No access history available for selected document");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}