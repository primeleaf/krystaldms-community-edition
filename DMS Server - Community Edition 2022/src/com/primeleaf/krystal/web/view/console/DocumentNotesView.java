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
import com.primeleaf.krystal.model.vo.DocumentNote;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class DocumentNotesView extends WebView {

	public DocumentNotesView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		printDocumentNotes();
	}

	@SuppressWarnings("unchecked")
	private void printDocumentNotes() throws Exception{
		try {
			if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
				printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
			}
			if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
				printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
			}
			ArrayList<DocumentNote>  documentNotes = (ArrayList<DocumentNote>)request.getAttribute("NOTES");
			out.println("<div class=\"card   \">");
			out.println("<div class=\"card-header\"><i class=\"bi bi-chat-left-dots \"></i> Notes</div>");
			
			if(documentNotes.size() > 0){
				out.println("<ul class=\"list-group\">");
				for(DocumentNote note : documentNotes){
					String cssClass="";
					if(!"P".equalsIgnoreCase(note.getNoteType())){
						cssClass = "text-muted";
					}
					out.println("<li class=\"list-group-item "+cssClass+"\">");
					out.println("<figure><blockquote class=\"blockquote fs-4 mb-4\">"+StringEscapeUtils.escapeHtml4(note.getNoteData())+"</blockquote>");
					out.println("<figcaption class=\"blockquote-footer fs-6\">"+StringEscapeUtils.escapeHtml4(note.getUserName()));
					out.println("<cite title=\"Posted On\">" + StringHelper.getFriendlyDateTime(note.getCreated())+"</cite>");
					if(note.getUserName().equalsIgnoreCase(krystalSession.getKrystalUser().getUserName())){
						out.println("<p class=\"my-3\"><a href=\"/console/documentnotes?documentid="+note.getDocumentId()+"&noteid="+note.getNoteId()+"\" class=\"confirm text-danger fs-6\" datatarget=\"#resultNotes\" title=\"Are you sure, you want to delete this note?\">Delete</a></p>");
					}
					out.println("</figcaption></figure></li>");
				}
				out.println("</ul>");
			}else{
				out.println("<div class=\"card-body\">");
				out.println("There are no notes /comments on this document");
				out.println("</div>");//card-body
			}
			out.println("</div>");//panel
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}