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

package com.primeleaf.krystal.web.action.console;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentNoteDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentNote;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;
import com.primeleaf.krystal.web.view.console.DocumentNotesView;

/**
 * Author Rahul Kubadia
 */

public class DocumentNotesAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		String documentid = request.getParameter("documentid")!=null?request.getParameter("documentid"):"0";
		int documentId = 0;
		try{
			documentId = Integer.parseInt(documentid.trim());
		}catch(Exception ex){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
			return new AJAXResponseView(request,response);
		}
		Document document = DocumentDAO.getInstance().readDocumentById(documentId);
		if(document == null){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid document");
			return new AJAXResponseView(request,response);
		}
		String txtNote = request.getParameter("txtNote")!=null?request.getParameter("txtNote"):"";
		String noteType = request.getParameter("radNoteType")!=null?request.getParameter("radNoteType"):"";
		if(txtNote.trim().length()>0){
			DocumentNote documentNote = new DocumentNote();
			documentNote.setDocumentId(documentId);
			documentNote.setNoteData(txtNote);
			documentNote.setUserName(krystalSession.getKrystalUser().getUserName());
			documentNote.setNoteType(noteType);

			DocumentNoteDAO.getInstance().addJournalNote(documentNote);
			document.setHasNote((byte)(document.getHasNote() | Hit.FLAG_HASNOTE));

			DocumentDAO.getInstance().updateDocument(document);
			AuditLogManager.log(new AuditLogRecord(
					documentId,
					AuditLogRecord.OBJECT_JOURNAL_NOTE,
					AuditLogRecord.ACTION_CREATED,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_FINE,
					"Document ID : " +documentNote.getDocumentId() ,
					"Journal Note Added"));
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Note added successfully");
		}
		String noteid = request.getParameter("noteid")!=null?request.getParameter("noteid"):"";
		if(noteid.trim().length() > 0 ){
			int noteId = 0;
			try{
				noteId = Integer.parseInt(noteid.trim());
			}catch(Exception ex){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
				return new AJAXResponseView(request,response);
			}
			DocumentNote note = DocumentNoteDAO.getInstance().readByNoteId(noteId);
			if(note == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid note");
				return new AJAXResponseView(request,response);
			}

			if(note.getUserName().equalsIgnoreCase(krystalSession.getKrystalUser().getUserName()) || krystalSession.getKrystalUser().isAdmin()){
				DocumentNoteDAO.getInstance().deleteJournalNote(noteId);

				if(DocumentNoteDAO.getInstance().readJournalNotes("DOCUMENTID=" + note.getDocumentId() +" AND ACTIVE='Y' ORDER BY 1 DESC").size() == 0){
					document = DocumentDAO.getInstance().readDocumentById(note.getDocumentId());
					document.setHasNote((byte)0);
					DocumentDAO.getInstance().updateDocument(document);
				}

				AuditLogManager.log(new AuditLogRecord(note.getDocumentId(),
						AuditLogRecord.OBJECT_JOURNAL_NOTE,
						AuditLogRecord.ACTION_DELETED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_FINE,
						"Document ID : " +note.getDocumentId() ,
						"Journal Note Deleted")
						);
				request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Note deleted successfully");
			}else{
				request.setAttribute(HTTPConstants.REQUEST_ERROR,"Access Denied");
			}
		}
		ArrayList<DocumentNote> documentNotes = DocumentNoteDAO.getInstance().readJournalNotes("DOCUMENTID=" + documentId +" AND ACTIVE='Y' ORDER BY 1 DESC");
		ArrayList<DocumentNote> availableDocumentNotes  = new ArrayList<DocumentNote>();
		for(DocumentNote note : documentNotes){
			if(note.getNoteType().equalsIgnoreCase("P") || note.getUserName().equalsIgnoreCase(krystalSession.getKrystalUser().getUserName())){
				availableDocumentNotes.add(note);
			}
		}
		request.setAttribute("DOCUMENT", document );
		request.setAttribute("NOTES",availableDocumentNotes );
		return (new DocumentNotesView(request, response));
	}
}

