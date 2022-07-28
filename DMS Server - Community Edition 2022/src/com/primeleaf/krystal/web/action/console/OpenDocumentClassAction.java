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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.DocumentSearchManager;
import com.primeleaf.krystal.model.dao.CheckedOutDocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentNoteDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.DocumentNote;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.model.vo.SearchFilter;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.OpenDocumentClassView;

/**
 * Author Rahul Kubadia
 */

public class OpenDocumentClassAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		String classid = request.getParameter("classid")!=null?request.getParameter("classid"):"0";
		String pageno = request.getParameter("page")!=null?request.getParameter("page"):"1";
		int pageNo = Integer.parseInt(pageno);
		int classId = 0;
		try{
			classId = Integer.parseInt(classid);
		}catch(Exception ex){
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
			return (new HomeAction().execute(request,response));
		}
		DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(classId);
		if(documentClass == null){
			request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid document class");
			return (new HomeAction().execute(request,response));
		}

		ACL acl = null;
		AccessControlManager aclManager = new AccessControlManager();
		acl = aclManager.getACL(documentClass, krystalSession.getKrystalUser());

		if(!acl.canRead() || ! documentClass.isVisible()){
			request.setAttribute(HTTPConstants.REQUEST_ERROR,"Access Denied");
			return (new HomeAction().execute(request,response));
		}
		User owner = UserDAO.getInstance().readUserByName(documentClass.getCreatedBy());

		request.setAttribute("OWNER",owner);
		request.setAttribute("ACL",acl);


		ArrayList<SearchFilter> searchFilterList = new ArrayList<SearchFilter>();
		boolean isIndexFilterSet = false;
		for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
			String operator = request.getParameter("operator_"+indexDefinition.getIndexColumnName());
			String filterValue1 = request.getParameter(indexDefinition.getIndexColumnName());
			String filterValue2 = request.getParameter(indexDefinition.getIndexColumnName()+"_2");
			if(operator!= null){ //direct hit list so you will not get 
				byte op = Byte.parseByte(operator);
				SearchFilter searchFilter = new SearchFilter(indexDefinition.getIndexColumnName(), indexDefinition.getIndexType(), false, filterValue1, filterValue2, op);
				searchFilterList.add(searchFilter);
				isIndexFilterSet = true;
			}
		}

		String fromDate 	=	request.getParameter("fromDate")==null?"":request.getParameter("fromDate");
		String toDate 		= 	request.getParameter("toDate")==null?"":request.getParameter("toDate");
		SearchFilter createdSearchFilter = new SearchFilter("CREATED", "D", true, fromDate, toDate,SearchFilter.OPERATOR_BETWEEN);
		searchFilterList.add(createdSearchFilter);

		String fromExpiryDate 	=	request.getParameter("fromExpiryDate")==null?"":request.getParameter("fromExpiryDate"); //10-Sep-2012 Rahul Kubadia - Expiry On Filter
		String toExpiryDate 		= 	request.getParameter("toExpiryDate")==null?"":request.getParameter("toExpiryDate"); //10-Sep-2012 Rahul Kubadia - Expiry On Filter
		SearchFilter expirySearchFilter = new SearchFilter("EXPIRY", "D", true, fromExpiryDate,toExpiryDate,SearchFilter.OPERATOR_BETWEEN);
		searchFilterList.add(expirySearchFilter);

		String fromModifiedDate 	=	request.getParameter("fromModifiedDate")==null?"":request.getParameter("fromModifiedDate"); //06-Feb-2013 Rahul Kubadia - Modified On Filter
		String toModifiedDate 		= 	request.getParameter("toModifiedDate")==null?"":request.getParameter("toModifiedDate");  //06-Feb-2013 Rahul Kubadia - Modified On Filter
		SearchFilter modifiedSearchFilter = new SearchFilter("MODIFIED", "D", true, fromModifiedDate,toModifiedDate,SearchFilter.OPERATOR_BETWEEN);
		searchFilterList.add(modifiedSearchFilter);

		String extension 	= 	request.getParameter("extension")==null?"":request.getParameter("extension");
		SearchFilter extensionSearchFilter = new SearchFilter("EXT", "S", true, extension,extension,SearchFilter.OPERATOR_IS);
		searchFilterList.add(extensionSearchFilter);


		String documentId 	= 	request.getParameter("txtDocumentId")!=null?request.getParameter("txtDocumentId"):"0";
		SearchFilter documentIdSearchFilter = new SearchFilter("DOCUMENTID", "N", true, documentId,documentId,SearchFilter.OPERATOR_IS);
		searchFilterList.add(documentIdSearchFilter);

		String createdBy 	= 	request.getParameter("createdBy")==null?"":request.getParameter("createdBy");
		String modifiedBy 	= 	request.getParameter("modifiedBy")==null?"":request.getParameter("modifiedBy"); //06-Feb-2013 Rahul Kubadia - Modified By Filter
		
		SearchFilter createdBySearchFilter = new SearchFilter("CREATEDBY", "S", true, createdBy,createdBy,SearchFilter.OPERATOR_IS);
		searchFilterList.add(createdBySearchFilter);

		SearchFilter modifiedBySearchFilter = new SearchFilter("MODIFIEDBY", "S", true, modifiedBy,modifiedBy,SearchFilter.OPERATOR_IS);
		searchFilterList.add(modifiedBySearchFilter);

		boolean searchAllRevisions = request.getParameter("chkAll")!=null?true:false;

		String orderBy = request.getParameter("orderBy")!=null?request.getParameter("orderBy"):"DOCUMENTID";
		String orderType = request.getParameter("orderType")!=null?request.getParameter("orderType"):"D";

		ArrayList<Hit> hits = DocumentSearchManager.getInstance().searchDocumentClass(documentClass, searchFilterList ,searchAllRevisions,orderBy,orderType);
		ArrayList<Hit> finalHits = new ArrayList<Hit>();

		int startIndex = (pageNo-1) * krystalSession.getKrystalUser().getHitlistSize();
		int endIndex = startIndex + krystalSession.getKrystalUser().getHitlistSize();
		if(endIndex > hits.size()){
			endIndex = hits.size();
		}
		for(int i = startIndex; i< endIndex; i++){
			Hit hit = hits.get(i);
			// Doing a check on the status of the doc, whether it is
			// checked-out or not this is a must because if all document are checked out then query only for documents which 
			// are shown on the hitlist page 
			if (hit.status.equals(Hit.STATUS_LOCKED)) {
				CheckedOutDocument checkedOutDocument = CheckedOutDocumentDAO.getInstance().readCheckedOutDocument(hit.documentId,hit.revisionId);
				hit.userName = checkedOutDocument.getUserName();
			} 
			finalHits.add(hit);
		}

		request.setAttribute("DOCUMENTCLASS", documentClass);
		request.setAttribute("SEARCHFILTERLIST", searchFilterList);
		request.setAttribute("INDEXFILTERSET",isIndexFilterSet);
		request.setAttribute("HITLIST", finalHits);
		request.setAttribute("TOTALHITS", hits.size());
		request.setAttribute("PAGE", pageNo);

		AuditLogManager.log(new AuditLogRecord(
				documentClass.getClassId(),
				AuditLogRecord.OBJECT_DOCUMENTCLASS,
				AuditLogRecord.ACTION_ACCESS,
				krystalSession.getKrystalUser().getUserName(),
				request.getRemoteAddr(),
				AuditLogRecord.LEVEL_INFO,
				"ID : " + documentClass.getClassId(),
				"Name : "+ documentClass.getClassName() )
				);
		long endTime = Calendar.getInstance().getTimeInMillis();
		double executionTime = ((endTime-startTime)/1000.00);
		request.setAttribute("EXECUTIONTIME", executionTime);

		String mode = request.getParameter("mode")!=null? request.getParameter("mode"):"";

		if(mode.trim().length() == 0){
			return (new OpenDocumentClassView(request, response));
		}

		InputStream fis = generatePdfFile(hits,documentClass.getIndexDefinitions(),krystalSession.getKrystalUser());
		String targetName = "";
		targetName = documentClass.getClassName()+"_ExportedDocumentList.pdf";
		int fileSize = fis.available();
		ServletContext servletContext = request.getServletContext();
		String mimeType = servletContext.getMimeType(targetName.toLowerCase());
		response.addHeader("Content-Disposition", "attachment"+ "; filename=\""+targetName+"\"");
		response.setContentType(mimeType);
		response.setContentLength(fileSize);
		OutputStream os = response.getOutputStream();
		byte buf[] = new byte[fileSize];
		fis.read(buf);
		os.write(buf, 0, fileSize);
		fis.close();
		os.flush();
		os.close();
		return null;
	}



	private InputStream generatePdfFile(ArrayList<Hit> hits, ArrayList<IndexDefinition> indexes,User loggedInUser) throws Exception{
		File tempPdf = File.createTempFile("tempPdf", ".pdf");
		FileOutputStream file = new FileOutputStream(tempPdf);
		Document document = new Document(PageSize.A3.rotate());
		PdfWriter.getInstance(document, file);
		document.open();

		Font boldFont12 = new Font(FontFamily.HELVETICA, 12, Font.BOLD); 
		Font normalFont10 = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);

		int colCount = 9; //predefined columns count
		int colSize = indexes.size()+colCount;
		float[] columnWidths = new float[colSize];

		for(int i=0;i < columnWidths.length;i++){
			columnWidths[i] = (100/colSize);
		}

		PdfPTable table = new PdfPTable(columnWidths);
		table.setWidthPercentage(100f);

		if(hits.size() > 0){
			BaseColor bgColor = new BaseColor(225,240,255);

			insertCellPDF(table, "Document ID", Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			insertCellPDF(table, "Revision ID", Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			insertCellPDF(table, "Created By", Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			insertCellPDF(table, "Last Modified By", Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			insertCellPDF(table,"File Size", Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			insertCellPDF(table, "Created On", Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			insertCellPDF(table, "Last Modified On", Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			insertCellPDF(table, "Expiry On", Element.ALIGN_CENTER, 1, boldFont12, bgColor);

			for(IndexDefinition index : indexes){
				insertCellPDF(table, index.getIndexDisplayName(), Element.ALIGN_CENTER, 1, boldFont12, bgColor);
			}

			insertCellPDF(table,"Note",Element.ALIGN_CENTER, 1, boldFont12, bgColor);

			int count = 0;

			for(Hit hit : hits){
				count++;
				if(count % 2 == 0){
					bgColor = new BaseColor(240,240, 240);
				}else{
					bgColor = new BaseColor(210,210, 210);
				}
				insertCellPDF(table, hit.documentId+"", Element.ALIGN_CENTER, 1, normalFont10, bgColor);
				insertCellPDF(table, hit.revisionId, Element.ALIGN_CENTER, 1, normalFont10, bgColor);
				insertCellPDF(table, hit.createdBy, Element.ALIGN_CENTER, 1, normalFont10, bgColor);
				insertCellPDF(table, hit.modifiedBy+"", Element.ALIGN_CENTER, 1, normalFont10, bgColor);
				insertCellPDF(table, StringHelper.formatSizeText(hit.fileLength), Element.ALIGN_CENTER, 1, normalFont10, bgColor);
				insertCellPDF(table, hit.created, Element.ALIGN_CENTER, 1, normalFont10, bgColor);
				insertCellPDF(table, hit.modified, Element.ALIGN_CENTER, 1, normalFont10, bgColor);
				insertCellPDF(table, hit.expiryOn, Element.ALIGN_CENTER, 1, normalFont10, bgColor);

				for(String index : hit.indexValues){
					insertCellPDF(table, index, Element.ALIGN_LEFT, 1, normalFont10, bgColor);
				}

				ArrayList<DocumentNote> documentNotes = DocumentNoteDAO.getInstance().readJournalNotes("DOCUMENTID=" + hit.documentId +" AND ACTIVE ='Y' ORDER BY 1 DESC");
				String notesData = "";
				if(documentNotes.size()>0){
					for (DocumentNote documentNote : documentNotes){
						if(! "P".equalsIgnoreCase(documentNote.getNoteType())){
							if(! loggedInUser.getUserName().equalsIgnoreCase(documentNote.getUserName())){
								continue;
							}
						}
						User user = UserDAO.getInstance().readUserByName(documentNote.getUserName());
						notesData+= documentNote.getNoteData() + "\n";
						notesData+= "Posted By" + " : " + user.getRealName()+"\n";
						notesData+= "Posted On"+ " : " + StringHelper.formatDate(documentNote.getCreated());
						notesData+="\n";
					}
				}
				insertCellPDF(table, notesData, Element.ALIGN_LEFT, 1, normalFont10, bgColor);
			}
		}
		try {
			document.add(table);
			document.close();
			file.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		InputStream fStream = new FileInputStream(tempPdf);
		tempPdf.delete();
		return fStream;
	}

	private void insertCellPDF(PdfPTable table, String text, int align, int colspan, Font font, BaseColor color){
		text = text!=null?text:"";
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setBackgroundColor(color);
		if(text.trim().equalsIgnoreCase("")){
			cell.setMinimumHeight(10f);
		}
		table.addCell(cell);
	}
}

