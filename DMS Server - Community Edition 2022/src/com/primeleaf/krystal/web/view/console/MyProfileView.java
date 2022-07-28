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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.HitList;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;


/**
 * @author Rahul Kubadia
 *
 */
public class MyProfileView extends WebView {

	public MyProfileView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		session.setAttribute(HTTPConstants.CSRF_STRING, StringHelper.generateAuthToken());
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printMyProfle();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">My Profile</li>");
		out.println("</ol>");
	}
	
	private void printMyProfle() throws Exception{
		printBreadCrumbs();

		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String) request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String) request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		out.println("<div class=\"row row-cols-1 row-cols-md-3 g-4 mb-3\">");
		out.println("<div class=\"col\">");

		out.println("<div class=\"card h-100\">"); 
		out.println("<div class=\"card-header\"><i class=\"bi bi-person-badge h6 me-1 text-primary\"></i>My Profile</div>"); 
		out.println("<div class=\"card-body\">"); 
		out.println("<form action=\"/console/updateprofilepicture?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmMyProfile\" class=\"form-horizontal\" enctype=\"multipart/form-data\">");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col col-form-label\">User Name</label>");
		out.println("<div class=\"col\">");
		out.println("<p class=\"fw-bold\">"+krystalSession.getKrystalUser().getUserName()+"</p>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col col-form-label \">Real Name </label>");
		out.println("<div class=\"col\">");
		out.println("<p class=\"fw-bold\">"+StringEscapeUtils.escapeHtml4(krystalSession.getKrystalUser().getRealName())+"</p>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col col-form-label \">My Profile Picture</label>");
		out.println("<div class=\"col\">");
		out.println("<div class=\"profile\">");
		out.println("<a href=\"javascript:void(0);\" id=\"profilePic\">");
		out.println("<img src=\"/console/profilepicture?username="+krystalSession.getKrystalUser().getUserName()+"\" class=\"img-thumbnail \"/>");
		out.println("<span class=\"overlay d-flex align-items-center\"><span id=\"profilepiccontent\" style=\"text-align:center\">Change Profile Picture</span></span>");
		out.println("</a>");
		out.println("</div>");
		out.println("<input class=\"btn-check\"  type=\"file\" name=\"profilepicture\" id=\"profilepicture\" class=\"required\" title=\"Select Profile Picture\" style=\"display:none;\">");
		out.println("<br/><a href=\"/console/deleteprofilepicture\" class=\"text-danger confirm\" title=\"Are you sure, you want remove profile picture?\"><i class=\"bi bi-trash h6 me-1\"></i>Remove</a>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col col-form-label\">Email ID</label>");
		out.println("<div class=\"col\">");
		out.println("<p class=\"fw-bold\">"+StringEscapeUtils.escapeHtml4(krystalSession.getKrystalUser().getUserEmail())+"</p>");
		out.println("</div>");
		out.println("</div>");
		
		String lastLogin = krystalSession.getKrystalUser().getLastLoginDate()!=null?StringHelper.formatDate(krystalSession.getKrystalUser().getLastLoginDate()):"0000-00-00 00:00:00";

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col col-form-label\">Last Login Date</label>");
		out.println("<div class=\"col\">");
		out.println("<p class=\"fw-bold\">"+lastLogin+"</p>");
		out.println("</div>");
		out.println("</div>");

		out.println("</form>");
		out.println("</div>");//card-body
		out.println("</div>");//card
		
		out.println("</div>");//col
		
		out.println("<div class=\"col\">");
		out.println("<div class=\"card h-100\">");
		out.println("<div class=\"card-header\"><i class=\"bi bi-sliders me-2 h6 text-primary \"></i>Preferences</div>");
		out.println("<div class=\"card-body\">");
		out.println("<form action=\"/console/preferences?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmPreferences\" form-type=\"ajax\" datatarget=\"#resultPreferences\" onsubmit=\"updateMode()\" class=\"form-horizontal\">");
		out.println("<div id=\"resultPreferences\"></div>");
		
		out.println("<div class=\"mb-3\">");
		out.println("<label for=\"cmbHitListSize\" class=\"col-form-label\">No of Hits per page</label>");
		out.println("<select class=\"form-control form-select\" name=\"cmbHitListSize\">");
		out.println("<option value=\"5\""); 	if(krystalSession.getKrystalUser().getHitlistSize() == 5 ){ out.print(" selected");} out.println(">5</option>");
		out.println("<option value=\"10\""); 	if(krystalSession.getKrystalUser().getHitlistSize() == 10 ){ out.print(" selected");} out.println(">10</option>");
		out.println("<option value=\"25\""); 	if(krystalSession.getKrystalUser().getHitlistSize() == 25 ){ out.print(" selected");} out.println(">25</option>");
		out.println("<option value=\"50\""); 	if(krystalSession.getKrystalUser().getHitlistSize() == 50 ){ out.print(" selected");} out.println(">50</option>");
		out.println("<option value=\"100\""); 	if(krystalSession.getKrystalUser().getHitlistSize() == 100 ){ out.print(" selected");} out.println(">100</option>");
		out.println("</select>");
		out.println("</div>");

		out.println("<div class=\"mb-3\">");
		out.println("<label for=\"none\" class=\"col-form-label\">Hitlist Preferences</label>");
		out.println("</div>");
		
		out.println("<div class=\"row mb-3\">");
		out.println("<div class=\"col-6 fs-5\">");
		out.println("<div class=\" form-check form-switch\">");
		out.println("<input class=\"form-check-input\" type=\"checkbox\" id=\"radCreatedOn1\" name=\""+HitList.META_CREATED+"\"  value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isCreatedVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"radCreatedOn1\" class=\"form-check-label\">Created On</label>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"col-6 fs-5\">");
		out.println("<div class=\" form-check form-switch\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"form-check-input\" type=\"checkbox\" id=\"radActive1\" name=\""+HitList.META_CREATEDBY+"\" value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isCreatedByVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"radActive1\" class=\"form-check-label \">Created By</label>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("</div>");//row

		out.println("<div class=\"row mb-3\">");
		out.println("<div class=\"col-6 fs-5\">");
		out.println("<div class=\" form-check form-switch\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"form-check-input\"  type=\"checkbox\" id=\"modifiedOn1\" name=\""+HitList.META_MODIFIED+"\" value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isModifiedVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"modifiedOn1\" class=\"form-check-label \">Last Modified On</label>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"col-6 fs-5\">");
		out.println("<div class=\" form-check form-switch\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"form-check-input\"  type=\"checkbox\" id=\"modifiedBy1\" name=\""+HitList.META_MODIFIEDBY+"\" value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isModifiedByVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"modifiedBy1\" class=\"form-check-label \">Last Modified By</label>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("</div>"); //row

		
		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"col-6 fs-5\">");
		out.println("<div class=\"form-check form-switch\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"form-check-input\"  type=\"checkbox\" id=\"fileSize1\" name=\""+HitList.META_LENGTH+"\" value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isFileSizeVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"fileSize1\" class=\"form-check-label\">File Size</label>");
		out.println("</div>");
		out.println("</div>");
		
		out.print("<div class=\"col-6 fs-5\">");
		out.println("<div class=\" form-check form-switch\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"form-check-input\"  type=\"checkbox\" id=\"documentId1\" name=\""+HitList.META_DOCUMENTID+"\" value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isDocumentIdVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"documentId1\" class=\"form-check-label \">Document ID</label>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");


		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"col-6 fs-5\">");
		out.println("<div class=\" form-check form-switch\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"form-check-input\"  type=\"checkbox\" id=\"revisionId1\" name=\""+HitList.META_REVISIONID+"\" value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isRevisionIdVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"revisionId1\" class=\"form-check-label \">Revision ID</label>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"col-6 fs-5\">");
		out.println("<div class=\" form-check form-switch\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"form-check-input\"  type=\"checkbox\" id=\"expiryOn1\" name=\""+HitList.META_EXPIRYON+"\" value=\"TRUE\"");if(krystalSession.getKrystalUser().getMetaPreferences().isExpiryOnVisible()) {out.print(" checked");}out.println(">");
		out.println("<label for=\"expiryOn1\" class=\"form-check-label \">Expiry On</label>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("<hr/>");
		out.println("<input class=\"btn btn-dark\" type=\"submit\"  value=\"Submit\" name=\"btnSubmit\">");
		out.println("</form>");
		out.println("</div>");//card-body
		out.println("</div>");//card
		out.println("</div>");//col
		
		out.println("<div class=\"col\">");
		out.println("<div class=\"card h-100\">");
		out.println("<div class=\"card-header\"><i class=\"bi bi-key text-primary h6 me-1\"></i>Change Password</div>"); 
		out.println("<div class=\"card-body\">"); 
		out.println("<div id=\"resultChangePassword\"></div>");
		out.println("<form action=\"/console/changepassword?CSRF="+session.getAttribute(HTTPConstants.CSRF_STRING)+"\" method=\"post\" id=\"frmChangePassword\" form-type=\"ajax\" datatarget=\"#resultChangePassword\" class=\"form-horizontal\">");
		out.println("<div class=\"mb-3\">");
		out.println("<div class=\"form-floating\">");
		out.println("<input class=\"form-control required\"  type=\"password\" maxlength=\"30\" name=\"txtOldPassword\" id=\"txtOldPassword\" class=\"form-control required\" autocomplete=\"off\" placeholder=\"Current Password\" title=\"Please enter password\"><label for=\"txtOldPassword\">Current Password</label>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<div class=\"mb-3\">");
		out.println("<div class=\"form-floating\">");
		out.println("<input class=\"form-control required\"  type=\"password\" maxlength=\"30\" name=\"txtNewPassword\" id=\"txtNewPassword\" class=\"form-control required complexPassword\" placeholder=\"New Password\" minlength=\"8\" autocomplete=\"off\"><label for=\"txtNewPassword\">New Password</label>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3\">");
		out.println("<div class=\"form-floating\">");
		out.println("<input  type=\"password\" maxlength=\"30\" name=\"txtConfirmPassword\" id=\"txtConfirmPassword\" class=\"form-control required\" equalTo= \"#txtNewPassword\" placeholder=\"Confirm Password\" autocomplete=\"off\" title=\"Password must match\"><label for=\"txtConfirmPassword\">Confirm Password</label>");
		out.println("</div>");
		out.println("</div>");

		out.println("<hr/>");
		out.println("<input class=\"btn  btn-dark\" type=\"submit\"  value=\"Submit\" name=\"btnSubmit\">&nbsp;");
		out.println("</form>");
		out.println("</div>");
		
		out.println("</div>");//card-body
		out.println("</div>");//card
		out.println("</div>");//card-deck
	}
}

