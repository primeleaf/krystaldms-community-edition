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

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.BookmarkDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Bookmark;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class DeleteBookmarkAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		if(request.getParameter(HTTPConstants.CSRF_STRING)==null) {
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid CSRF Token");
			return (new HomeAction().execute(request,response));
		}
		if(!(request.getParameter(HTTPConstants.CSRF_STRING).equals((String)session.getAttribute(HTTPConstants.CSRF_STRING)))) {
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid CSRF Token");
			return (new HomeAction().execute(request,response));
		}
		try{
			int bookmarkId = 0;
			try{
				bookmarkId=Integer.parseInt(request.getParameter("bookmarkid")!=null?request.getParameter("bookmarkid"):"0");
			}catch(Exception e){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
				return (new HomeAction().execute(request,response));
			}
			Bookmark bookmark =  BookmarkDAO.getInstance().readBookmarkById(bookmarkId);
			if(bookmark == null){
				request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid bookmark");
				return (new HomeAction().execute(request,response));
			}
			
			String bookmarkName = bookmark.getBookmarkName();
			BookmarkDAO.getInstance().deleteBookmark(bookmarkId);
			AuditLogManager.log(new AuditLogRecord(
					bookmark.getBookmarkId(),
					AuditLogRecord.OBJECT_BOOKMARK,
					AuditLogRecord.ACTION_DELETED,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"ID :" + bookmarkId,
					"Name : " + bookmarkName)
					);
			request.setAttribute(HTTPConstants.REQUEST_MESSAGE, " Bookmark "+bookmark.getBookmarkName()+" deleted successfully");
			return (new HomeAction().execute(request,response));
		}catch(Exception e){
			e.printStackTrace();
		}
		return (new HomeAction().execute(request,response));
	}
}

