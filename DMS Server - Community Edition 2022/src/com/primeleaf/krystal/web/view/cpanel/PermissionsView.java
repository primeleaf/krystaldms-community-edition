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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Permission;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class PermissionsView extends WebView {
	public PermissionsView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printAssingPermissionsForm();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel/managedocumentclasses\">Manage Document Classes</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Manage Permissions</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printAssingPermissionsForm() throws Exception{
		printBreadCrumbs();
		DocumentClass documentClass = (DocumentClass) request.getAttribute("DOCUMENTCLASS");
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
		out.println("<i class=\"bi text-primary bi-shield\"></i> Assign Permissions");
		out.println("</div>");
		out.println("</div>");//row
		out.println("</div>");//card-header
		out.println("<div class=\"card-body\">");
		out.println("<form action=\"/cpanel/setpermissions?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmSetACL\" class=\"form-horizontal\">");
			ArrayList<User> usersList = (ArrayList<User>) request.getAttribute("USERLIST");
			ArrayList<Permission> permissions = (ArrayList<Permission>) request.getAttribute("PERMISSIONS");
			
			if(usersList.size() > 0){
				printHeaderRow("Users");
				for (User user:usersList) {
					ACL acl = new ACL(0);
					for(Permission permission : permissions){
						if(permission.getUserId() == user.getUserId()){
							acl = new ACL(permission.getAclValue());
							break;
						}
					}
					
					out.println("<div class=\"row\">");
					out.println("<div class=\"col \">");
					out.println("<b>"+StringEscapeUtils.escapeHtml4(user.getUserName())+"</b><br/>"+StringEscapeUtils.escapeHtml4(user.getRealName())+"");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbCreate_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canCreate()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbRead_" + user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canRead()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbWrite_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canWrite()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbDelete_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canDelete()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\"name=\"cbPrint_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canPrint()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbEmail_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canEmail()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbCheckin_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canCheckin()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbCheckout_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canCheckout()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" name=\"cbDownload_"+ user.getUserId() + "\" value=\""+ user.getUserId() +"\""); if (acl.canDownload()) {out.println("checked");} out.println(">");
					out.println("</div>");
					out.println("<div class=\"col text-center\">");
					out.println("<input type=\"checkbox\" class=\"selectRow\"	id=\"cbRow_"+ user.getUserId() + "\" name=\"cbRow_"+ user.getUserId() + "\" value=\""+ user.getUserId() + "\">");
					out.println("</div>");
					out.println("</div>");
					out.println("<hr/>");
				}
				printSelectAllRow();
			}else{
				out.println("<div class=\"alert alert-warning\">No user found</div>");
			}
		
		out.println("<hr/>");
		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"col0\">");
		out.println("<input type=\"hidden\" name=\"classid\" value=\""+documentClass.getClassId()+"\"/>");
		out.println("<input type=\"submit\" name=\"btnSubmit\"  value=\"Submit\" class=\"btn  btn-dark\">");
		out.println("</div>");
		out.println("</div>");
		out.println("</form>");//card   body
		out.println("</div>");//card   body
		out.println("</div>");//panel
	}

	private void printHeaderRow(String objectLabel) throws Exception{
		out.println("<div class=\"row\">");
		out.println("<div class=\"col text-primary\"><h6>" + objectLabel + "</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Create</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Read</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Edit</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Delete</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Print</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Email</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Checkin</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Checkout</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Download</h6></div>");
		out.println("<div class=\"col text-center text-primary\"><h6>Select All</h6></div>");
		out.println("</div>");
		out.println("<hr/>");
	}

	private void printSelectAllRow() throws Exception{
		out.println("<div class=\"row\">");
		out.println("<div class=\"col text-primary\"><h6>Select All</h6></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn \" id=\"cbCreateColumn\" 		name=\"cbCreateColumn\"  	value=\"cbCreate\"></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbReadColumn\" 		name=\"cbReadColumn\" 		value=\"cbRead\"></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbWriteColumn\" 		name=\"cbWriteColumn\" 		value=\"cbWrite\"></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbDeleteColumn\" 		name=\"cbDeleteColumn\"  	value=\"cbDelete\"></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbPrintColumn\" 		name=\"cbPrintColumn\" 		value=\"cbPrint\"></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbEmailColumn\" 		name=\"cbEmailColumn\" 		value=\"cbEmail\"></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbCheckinColumn\" 	name=\"cbCheckinColumn\" 	value=\"cbCheckin\"></div>");
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbCheckoutColumn\" 	name=\"cbCheckoutColumn\" 	value=\"cbCheckout\"></div>");;
		out.println("<div class=\"col text-center\"><input type=\"checkbox\" class=\"selectColumn\" id=\"cbDownloadColumn\" 	name=\"cbDownloadColumn\" 	value=\"cbDownload\"></div>");;
		out.println("<div class=\"col text-center\">&nbsp;</div>");
		out.println("</div>");
	}
}