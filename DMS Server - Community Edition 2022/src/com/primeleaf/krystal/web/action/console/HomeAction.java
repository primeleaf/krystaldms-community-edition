/**
 * Created On 15-Mar-2014
 * Copyright 2014 by Primeleaf Consulting (P) Ltd.,
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
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.dao.AuditLogRecordDAO;
import com.primeleaf.krystal.model.dao.BookmarkDAO;
import com.primeleaf.krystal.model.dao.CheckedOutDocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.Bookmark;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.HomeView;

/**
 * Author Rahul Kubadia
 */

public class HomeAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		AccessControlManager aclManager = new AccessControlManager();
		ArrayList<DocumentClass> allDocumentClasses = DocumentClassDAO.getInstance().readDocumentClasses("DOCUMENTCLASSES.ACTIVE = 'Y'");
		ArrayList<DocumentClass> userDocumentClasses = new ArrayList<DocumentClass>();
		
		int count   = 1 ;
		for(DocumentClass documentClass : allDocumentClasses){
			ACL acl = aclManager.getACL(documentClass, krystalSession.getKrystalUser());
			if(acl.canRead()){//User has permission to access this document class so add to displayed list
				request.setAttribute(documentClass.getClassName() + "_ACL",acl);
				userDocumentClasses.add(documentClass);
			}
		}
		
		request.setAttribute("DOCUMENTCLASSLIST", userDocumentClasses);

		ArrayList<CheckedOutDocument> checkedOutDocumentList = CheckedOutDocumentDAO.getInstance().readCheckedOutDocumentsByUser(krystalSession.getKrystalUser().getUserName());
		ArrayList<CheckedOutDocument> top5Checkouts = new ArrayList<CheckedOutDocument>();
		count = 1;
		for(CheckedOutDocument checkedOutDocument : checkedOutDocumentList){
			top5Checkouts.add(checkedOutDocument);
		}
		request.setAttribute("CHECKOUTS", top5Checkouts);

		ArrayList<AuditLogRecord> auditLogsRecords  = AuditLogRecordDAO.getInstance().getAuditTrail("USERNAME ='"+krystalSession.getKrystalUser().getUserName()+"' AND AUDITLEVEL = "+AuditLogRecord.LEVEL_INFO+" ORDER BY ACTIONDATE DESC ");
		ArrayList<AuditLogRecord> top10AuditRecords = new ArrayList<AuditLogRecord>();
		count = 1;
		for(AuditLogRecord auditLogRecord : auditLogsRecords){
			top10AuditRecords.add(auditLogRecord);
			count++;
			if(count > 10 )break;
		}
		request.setAttribute("AUDITLOGS", top10AuditRecords);

		ArrayList<Bookmark> bookmarkList = BookmarkDAO.getInstance().readBookmarkByUser(krystalSession.getKrystalUser().getUserName());
		ArrayList<Bookmark> top5Bookmarks = new ArrayList<Bookmark>();
		count = 1;
		for(Bookmark bookmark : bookmarkList){
			top5Bookmarks.add(bookmark);
			count++;
			if(count > 5 )break;
		}
		request.setAttribute("BOOKMARKS", top5Bookmarks);
		return (new HomeView(request, response));
	}
}

