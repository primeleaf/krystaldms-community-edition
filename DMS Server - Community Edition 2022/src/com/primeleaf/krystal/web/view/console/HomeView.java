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

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Bookmark;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class HomeView extends WebView {

	public HomeView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printConsole();
		template.generateFooter();
	}

	private void printConsole() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item active\">My Workspace</li>");
		out.println("</ol>");

		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		out.println("<div class=\"row\">");
		out.println("<div class=\"col-lg-8\">");
		printDocumentClasses();
		printCheckouts();
		printRecentActivity();
		out.println("</div>");
		out.println("<div class=\"col-lg-4\">");
		printGreetings();
		printChart();
		printBookmarks();
		out.println("</div>");
		out.println("</div>");
		
	}

	@SuppressWarnings("unchecked")
	private void printDocumentClasses(){
		try {
			ArrayList<DocumentClass> documentClasses = 	(ArrayList<DocumentClass>) request.getAttribute("DOCUMENTCLASSLIST");
			out.println("<div class=\"card ");if(documentClasses.size() > 0){out.print("border-bottom-0");}out.print("\">");
			out.println("<div class=\"card-header\">");
			out.println("<i class=\"bi bi-folder2-open text-primary\"></i> Document Classes");
			out.println("</div>");//card-header
			if(documentClasses.size() > 0){
				out.println("<ul class=\"list-group\">");
				for(DocumentClass documentClass : documentClasses){
					
					ACL acl = (ACL)request.getAttribute(documentClass.getClassName()+"_ACL");
					int documentCount = documentClass.getActiveDocuments();
					
					out.print("<li class=\"list-group-item list-group-item-action border-top-0 border-start-0 border-end-0\">");
					out.print("<div class=\"row\">");
					out.print("<div class=\"col-lg-9\">");
					out.print("<a href=\"/console/opendocumentclass?classid="+documentClass.getClassId()+"\" class=\"display-6 text-primary className\">");
					out.print("<i class=\"bi bi-server text-primary me-1\"></i>"+documentClass.getClassName());
					out.print("</a>");
					out.print("<ul class=\"list-inline mt-2\">");
					out.print("<li class=\"list-inline-item\"><a href=\"/console/searchdocumentclass?classid="+documentClass.getClassId()+"\" title=\"Search\"><i class=\"bi bi-binoculars me-1\"></i>Search</a></li>");
					if(acl.canCreate()){
						out.print("<li class=\"list-inline-item\"><a href=\"/console/newdocument?classid="+documentClass.getClassId()+"\" title=\"Add Document\"><i class=\"bi bi-file-earmark-arrow-up me-1\"></i>Add Document</a></li>");
					}
					out.print("<li class=\"list-inline-item\"><a href=\"/console/opendocumentclass?classid="+documentClass.getClassId()+"\" title=\"View All\"><i class=\"bi bi-eye me-1\"></i>View All</a></li></ul>");
					out.print("</div>");
					out.print("<div class=\"col-lg-3 d-none d-sm-block text-end\">");
					out.print("<a href=\"/console/opendocumentclass?classid="+documentClass.getClassId()+"\" title=\"\" class=\"tip\" data-toggle=\"tooltip\" data-placement=\"left\" data-bs-original-title=\"Available Documents\">");
					out.print("<span class=\"display-4 totaldocs"+documentClass.getClassId()+" odometer odometer-auto-theme\">0</span>");
					out.print("</a>");
					if(documentCount > 0 ){
						out.println("<script>setTimeout(function(){$('.totaldocs"+documentClass.getClassId()+"').html('"+documentCount+"');},1000);</script>");
					}
					out.print("</div>");
					out.print("</div>");
					out.print("</li>");
				}
				out.println("</ul>");
			}else{
				out.println("<div class=\"card-body\">");
				out.println("There are no document classes currently available");
				out.println("</div>");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		out.println("</div>");
	}


	@SuppressWarnings("unchecked")
	private void printCheckouts(){
		try {
			ArrayList<CheckedOutDocument> checkedOutDocumentList = 	(ArrayList<CheckedOutDocument>) request.getAttribute("CHECKOUTS");
			if(checkedOutDocumentList.size()>0) {
				out.println("<div class=\"card border-bottom-0\">");
			}else {
				out.println("<div class=\"card\">");
			}
			out.println("<div class=\"card-header\">");
			out.println("<i class=\"bi bi-lock text-primary\"></i>  Checked Out Documents");
			out.println("</div>");
			if(checkedOutDocumentList.size() > 0){
				out.println("<div class=\"list-group border-bottom-0\">");
				for(CheckedOutDocument checkedOutDocument : checkedOutDocumentList){
					File checkedOutFile = new File(checkedOutDocument.getCheckOutPath().toLowerCase());
					out.println("<li class=\"list-group-item border-0\">");
					out.println("<a href=\"/console/viewdocument?documentid="+checkedOutDocument.getDocumentId()+"&revisionid="+checkedOutDocument.getRevisionId()+"\" class=\"\">");
					out.println("<h4>"+StringEscapeUtils.escapeHtml4(checkedOutFile.getName())+"</h4>");
					out.println("</a>");
					out.println("<h5>Document Class : "+StringEscapeUtils.escapeHtml4(checkedOutDocument.getDocumentClass().getClassName())+"</h5>");
					out.println("<p><h6><a href=\""+HTTPConstants.BASEURL+"/console/viewdocument?documentid="+checkedOutDocument.getDocumentId()+"&revisionid="+checkedOutDocument.getRevisionId()+"\">"+"View Document"+"</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/console/checkindocument?documentid="+checkedOutDocument.getDocumentId()+"&revisionid="+checkedOutDocument.getRevisionId()+"\">Check In</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/console/cancelcheckout?documentid="+checkedOutDocument.getDocumentId()+"&revisionid="+checkedOutDocument.getRevisionId()+"\" class=\"confirm\" title=\"Are you sure? you want to cancel checkout?\">Cancel Checkout</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/console/revisionhistory?documentid="+checkedOutDocument.getDocumentId()+"&revisionid="+checkedOutDocument.getRevisionId()+"\" class=\"revisionhistory\" data-bs-toggle=\"modal\" data-bs-target=\"#revisionHistoryModal\">Revision History</a></h6></p>");
					out.println("</li><hr class=\"m-0\"/>");
					checkedOutFile.delete();
				}
				out.println("</div>");
				printModal("revisionHistoryModal");
			}else{
				out.println("<div class=\"card-body\">");
				out.println("There are no documents checked out currently");
				out.println("</div>");
			}
			out.println("</div>");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void printBookmarks(){
		try{
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\">");
			out.println("<i class=\"bi bi-bookmark text-primary\"></i> Bookmarks");
			out.println("</div>");
			ArrayList<Bookmark> bookmarkList = (ArrayList<Bookmark>)request.getAttribute("BOOKMARKS");
			if(bookmarkList.size() > 0){
				out.println("<ul class=\"list-group\">");
				for(Bookmark bookmark : bookmarkList){
					out.println("<li class=\"list-group-item\">");
					out.println("<h4 class=\"\">" + StringEscapeUtils.escapeHtml4(bookmark.getBookmarkName())+ "</h4>");
					out.println("<h5>Document ID : " +  bookmark.getDocumentId() + "&nbsp;Revision ID : " +  bookmark.getRevisionId() + "</h5>");
					out.println("<p><h6>");
					out.println("<a href=\"/console/viewdocument?documentid="+bookmark.getDocumentId() + "&revisionid="+bookmark.getRevisionId()+"\"  title=\"View Document\">View Document</a>");
					out.println(" | <a href=\"/console/deletebookmark?bookmarkid="+ bookmark.getBookmarkId()+"&CSRF="+session.getAttribute("CSRF")+"\"  class=\"confirm\" title=\"Are you sure you want to delete bookmark?\">Delete Bookmark</a>");
					out.println("</h6></p>");
					out.println("</li>");//list-group-item
				}// for
			}else{
				out.println("<div class=\"card-body\">");
				out.println("There are no bookmarks available currently");
				out.println("</div>");
			}
			out.println("</div>");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	private void printChart(){
		try{
			ArrayList<DocumentClass> documentClasses = 	(ArrayList<DocumentClass>) request.getAttribute("DOCUMENTCLASSLIST");
			if(documentClasses.size() >  0 ){
				out.println("<div class=\"card\">");
				out.println("<div class=\"card-header\">");
				out.println("<i class=\"bi text-primary bi-pie-chart\"></i> Charts");
				out.println("</div>");
				out.println("<div class=\"card-body text-center\">");
				out.println("<canvas id=\"classChart\" height=\"200\"></canvas>");
				out.println("<script>");
				
				out.println("$(document).ready(function(){const ctx = document.getElementById(\'classChart\').getContext(\'2d\');");
				out.println("var dataLabels = []");
				
				for(DocumentClass documentClass : documentClasses){
					out.println("dataLabels.push(\""+StringEscapeUtils.escapeHtml4(documentClass.getClassName())+"\");");
				}
				
				out.println("var dataValues = []");
				
				for(DocumentClass documentClass : documentClasses){
					out.println("dataValues.push("+documentClass.getActiveDocuments()+");");
				}
				out.println("const myChart = new Chart(ctx, {");
				out.println("    type: \'pie\',");
				out.println("    data: {");
				out.println("        labels:dataLabels,");
				out.println("        datasets: [{");
				out.println("            data: dataValues,");
				out.println("            backgroundColor: [");
				out.println("                \'#0B62A4\', \'#3980B5\', \'#679DC6\', \'#95BBD7\', \'#B0CCE1\', \'#095791\', \'#095085\', \'#083E67\', \'#052C48\', \'#042135\'");
				out.println("            ],");
				out.println("            borderColor: [");
				out.println("               \'#fff\'");
				out.println("            ],");
				out.println("            borderWidth: 1");
				out.println("        }]");
				out.println("    },");
				out.println("    options: {");
				out.println("        legend: {");
				out.println("         display: false //This will do the task");
				out.println("      },responsive:true,"
						+ "animation:{"
						+ "duration:1500"
						+ "},"
						+ "aspectRatio:2");
				out.println("    }");
				out.println("});});");
				
				out.println("</script>");
				out.println("</div>");
				out.println("</div>");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void printRecentActivity(){
		out.println("<div class=\"card\">");
		out.println("<div class=\"card-header border-bottom-0\"><i class=\"bi bi-clock text-primary\"></i> Recent Access History</div>");
		out.println("<div class=\"table-responsive\">");
		out.println("<table class=\"table table-bordered table-sm ps-2 mb-0 table-hover\">");

		int count=0; //for showing only first 10 records
		int size=10;
		ArrayList<AuditLogRecord> auditLogs = (ArrayList<AuditLogRecord>)request.getAttribute("AUDITLOGS");
		out.println("<thead>");
		out.println("<tr>");
		out.println("<th>Action</th>");
		out.println("<th>Type</th>");
		out.println("<th>Action Date</th>");
		out.println("<th>IP Address</th>");
		out.println("<th>Comments</th>");
		out.println("</tr>");
		out.println("</thead>");
		out.println("<tbody>");
		for(AuditLogRecord auditLogRecord:auditLogs){
			if(count >= size)break;
			out.println("<tr>");
			out.println("<td>" + auditLogRecord.getAction()+ "</td>");
			out.println("<td>" + auditLogRecord.getObjectDescription()+ "</td>");
			out.println("<td>" + StringHelper.formatDate(auditLogRecord.getActionDate(),"dd-MMM-yyyy HH:mm")+ "</td>");
			out.println("<td>" + auditLogRecord.getIpAddress()+ "</td>");
			out.println("<td>" + StringEscapeUtils.escapeHtml4(auditLogRecord.getComments())+ "</td>");
			out.println("</tr>");
			count++;
		}	
		out.println("</tbody>");
		out.println("</table>");
		out.println("</div>");
		out.println("</div>");
	}

	private void printGreetings(){
		String lastLogin = krystalSession.getKrystalUser().getLastLoginDate()!=null?StringHelper.formatDate(krystalSession.getKrystalUser().getLastLoginDate()):"0000-00-00 00:00:00";
		
		out.print("<div class=\"card mb-3 text-white d-none d-sm-block\">");
		out.print("<div class=\"card-header bg-primary text-center\">");
		out.print("<img src=\"/console/profilepicture?size=large&username="+krystalSession.getKrystalUser().getUserName()+"\" class=\"align-self-center rounded-circle p-2\">");
		out.print("<h5>Welcome "+krystalSession.getKrystalUser().getRealName()+" !</h5>");
		out.print("</div>");
		out.print("<ul class=\"list-group list-group-flush\">");
		out.print("<li class=\"list-group-item list-group-item-action text-dark\">");
		out.print("<i class=\"bi bi-clock me-1 h6\"></i>Last login : "+lastLogin);
		out.print("</li>");
		out.print("</ul>");
		out.print("<div class=\"card-footer d-flex justify-content-between\">");
		out.print("<a href=\"/console/myprofile\" class=\"card-link\" title=\"My Profile\"><i class=\"bi bi-person-badge h6 me-1\"></i>My Profile</a>");
		out.print("<a href=\"/logout\" class=\"confirm card-link\" confirmationmessage=\"Are you sure, you want to logout?\" title=\"Logout\">Logout<i class=\"bi-box-arrow-in-left h6 ms-1\"></i></a>");
		out.print("</div>");
		out.print("</div>");

	}
}
