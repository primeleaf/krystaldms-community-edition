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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.BookmarkDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentRevisionDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Bookmark;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentRevision;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;

/**
 * Author Rahul Kubadia
 */

public class NewBookmarkAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		String documentid=request.getParameter("documentid")!=null?request.getParameter("documentid"):"0";
		String revisionid=request.getParameter("revisionid")!=null?request.getParameter("revisionid"):"0";
		
		request.setAttribute("DOCUMENTID" ,documentid);
		request.setAttribute("REVISIONID" ,revisionid);
		
		if(request.getMethod().equalsIgnoreCase("POST")){
			try {
				String bookmarkName = request.getParameter("txtBookmarkName");
				int documentId = 0;
				try{
					documentId = Integer.parseInt(documentid);
					Double.parseDouble(revisionid);
				}catch(Exception e){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Invalid input");
					return (new AJAXResponseView(request, response));
				}
				if(! GenericValidator.maxLength(bookmarkName, 50)){
					request.setAttribute(HTTPConstants.REQUEST_ERROR,"Value too large for bookmark name");
					return (new AJAXResponseView(request, response));
				}

				Document document = DocumentDAO.getInstance().readDocumentById(documentId);
				if(document == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new AJAXResponseView(request, response));
				}
				DocumentRevision documentRevision = DocumentRevisionDAO.getInstance().readDocumentRevisionById(documentId,revisionid); 
				if(documentRevision == null){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document");
					return (new AJAXResponseView(request, response));
				}

				Bookmark bookmark = new Bookmark();
				bookmark.setDocumentId(documentId);
				bookmark.setRevisionId(revisionid);
				bookmark.setBookmarkName(bookmarkName);
				bookmark.setUserName(krystalSession.getKrystalUser().getUserName());
				BookmarkDAO.getInstance().addBookmark(bookmark);
				AuditLogManager.log(new AuditLogRecord(
						bookmark.getBookmarkId(),
						AuditLogRecord.OBJECT_BOOKMARK,
						AuditLogRecord.ACTION_CREATED,
						krystalSession.getKrystalUser().getUserName(),
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,"",
						"Name : " + bookmarkName )
						);
				request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Bookmark "+ bookmarkName+" added successfully");
				return (new AJAXResponseView(request, response));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (new HomeAction().execute(request, response));
	}
}