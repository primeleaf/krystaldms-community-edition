/**
 * Created on Oct 5, 2005 
 *
 * Copyright 2005 by Arysys Technologies (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Arysys Technologies (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Arysys Technologies (P) Ltd. 
 */
package com.primeleaf.krystal.model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.dao.CheckedOutDocumentDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.DocumentRevision;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.model.vo.RevisionRecord;

/**
 * All activities associated with revisions are carried out here.
 * @author Rahul Kubadia
 * @since 2.0
 */

public class RevisionManager {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	private String SQL_INSERT_REVISIONHISTORY = "INSERT INTO REVISIONHISTORY(DOCUMENTID,REVISIONID,USERNAME,USERACTION,COMMENTS)VALUES(?,?,?,?,?)";
	private String SQL_SELECT_REVISIONHISTORYBYDOCUMENTID = "SELECT DOCUMENTID,REVISIONID,USERNAME,USERACTION,COMMENTS,DATEOFACTION FROM REVISIONHISTORY WHERE DOCUMENTID=? ORDER BY DATEOFACTION DESC";
	private String SQL_DELETE_REVISIONHISTORY = "DELETE FROM REVISIONHISTORY WHERE DOCUMENTID = ?";	
	public static String ACTION_CHECKOUT="OBJECT CHECKED OUT";
	public static String ACTION_CHECKIN="OBJECT CHECKED IN";
	public static String ACTION_CANCELCHECKOUT="CANCELLED CHECKOUT";
	
	/**
	 * Default constructor
	 */
	
	public RevisionManager() {
		super();
	}
	
	public ArrayList<RevisionRecord> getRevisionHistory(final int documentId){
		ArrayList<RevisionRecord> result = new ArrayList<RevisionRecord>();		
		kLogger.fine("Reading revision history for document: documentId= " + documentId);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();			
			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_REVISIONHISTORYBYDOCUMENTID);			
			psSelect.setInt(1,documentId);			
			ResultSet rs = psSelect.executeQuery();			
			while(rs.next()){
				RevisionRecord revisionRecord = new RevisionRecord();				
				int i=1;				
				revisionRecord.setDocumentId(rs.getInt(i++));
				revisionRecord.setRevisionId(rs.getString(i++));
				revisionRecord.setUserName(rs.getString(i++));
				revisionRecord.setUserAction(rs.getString(i++));
				revisionRecord.setComments(rs.getString(i++));
				revisionRecord.setDateTime(rs.getString(i++));				
				result.add(revisionRecord);
			}			
			rs.close();
			psSelect.close();
			connection.close();			
		}catch(Exception e){
			kLogger.warning("Unable to read revision history for document: documentId= " + documentId);
		}
		return result;
	}
	
	public void checkIn(CheckedOutDocument checkedOutDocument,DocumentClass documentClass,Hashtable<String,String> indexRecord,InputStream imageFile,String comments,String ext,String userName)throws Exception{
		//delete entry from checkout table 		
		CheckedOutDocumentDAO.getInstance().deleteCheckedOutDocument(checkedOutDocument);		
		//generate new revision id		
		double revisionId = 0.0;
	    DecimalFormat onePlace = new DecimalFormat("0.0");
		revisionId = Double.parseDouble(checkedOutDocument.getRevisionId());
		revisionId += 0.1;
		checkedOutDocument.setRevisionId(onePlace.format(revisionId));
		// 	Store the new revision 
		DocumentRevision documentRevision = new DocumentRevision();
		documentRevision.setClassId(documentClass.getClassId());
		documentRevision.setDocumentId(checkedOutDocument.getDocumentId());
		documentRevision.setRevisionId(onePlace.format(revisionId)+"");
		documentRevision.setDocumentFile(imageFile);
		documentRevision.setUserName(userName);
		documentRevision.setIndexRecord(indexRecord);
		documentRevision.setComments(comments);
		
		DocumentManager documentManager = new DocumentManager();
		documentManager.storeDocument(documentRevision, documentClass,ext);
		//	Update document status		
		Document document = DocumentDAO.getInstance().readDocumentById(checkedOutDocument.getDocumentId());
		document.setRevisionId(checkedOutDocument.getRevisionId());
		document.setStatus(Hit.STATUS_AVAILABLE);
		Timestamp modified = new Timestamp(Calendar.getInstance().getTime().getTime());
		document.setModified(modified);
		document.setModifiedBy(userName);
		document.setLastAccessed(modified);
		document.setAccessCount(document.getAccessCount() + 1);
		Hit hit = new Hit();
		hit.setProperties(document.getHasNote());
		hit.hasAnnotation = false;
		document.setHasNote(hit.getProperties());
		DocumentDAO.getInstance().updateDocument(document);		
		//	add record to revision history		
		RevisionRecord revisionRecord = new RevisionRecord();	
		revisionRecord.setDocumentId(checkedOutDocument.getDocumentId());
		revisionRecord.setRevisionId(checkedOutDocument.getRevisionId());
		revisionRecord.setUserName(checkedOutDocument.getUserName());
		revisionRecord.setUserAction(ACTION_CHECKIN);
		revisionRecord.setComments("Document checked in");		
		addRevisionRecord(revisionRecord);
	}
	
	public String checkOut(CheckedOutDocument checkedOutDocument)throws Exception{
		String fileName="";		
		//Update document status		
		Document document = DocumentDAO.getInstance().readDocumentById(checkedOutDocument.getDocumentId());		
		//DOCUMENT IS already checked out so return blank filename 
		if(document.getStatus().equalsIgnoreCase(Hit.STATUS_LOCKED)) return fileName;		
		document.setStatus(Hit.STATUS_LOCKED);
		
		Date lastAccessed = new Date();
		document.setLastAccessed(new java.sql.Timestamp(lastAccessed.getTime()));
		
		DocumentDAO.getInstance().updateDocument(document);		
		//get document class name 		
		fileName = "Krystal_" + checkedOutDocument.getDocumentId() + "_" + checkedOutDocument.getRevisionId().replace('.','_') + "_Obj." + document.getExtension().toUpperCase();		
		// add entry to checkout table 		
		checkedOutDocument.setClassId(document.getClassId());//Added By Saumil Shah on NOV 2021 selects specified revision
		
		String checkOutFolder = checkedOutDocument.getCheckOutPath();
		if ( !(checkOutFolder.endsWith("/") || checkOutFolder.endsWith("\\")) ){
			checkOutFolder += System.getProperty("file.separator");
		}
		checkedOutDocument.setCheckOutPath(checkOutFolder+""+fileName);

		CheckedOutDocumentDAO.getInstance().addCheckedOutDocument(checkedOutDocument);		
		// add record to revision history 		
		RevisionRecord revisionRecord = new RevisionRecord();
		revisionRecord.setDocumentId(checkedOutDocument.getDocumentId());
		revisionRecord.setRevisionId(checkedOutDocument.getRevisionId());
		revisionRecord.setUserName(checkedOutDocument.getUserName());
		revisionRecord.setUserAction(ACTION_CHECKOUT);
		revisionRecord.setComments("Document checked out");				
		addRevisionRecord(revisionRecord);		
		//return the name of file along with extension		
		return fileName;
	}

	public void cancelCheckOut(CheckedOutDocument checkedOutDocument, Document document)throws Exception{
		//	delete from checkout object 		
		CheckedOutDocumentDAO.getInstance().deleteCheckedOutDocument(checkedOutDocument);		
		// 	update document status 		
		document.setStatus(Hit.STATUS_AVAILABLE);
		DocumentDAO.getInstance().updateDocument(document);		
		//	add record to revision history		
		RevisionRecord revisionRecord = new RevisionRecord();		
		revisionRecord.setDocumentId(checkedOutDocument.getDocumentId());
		revisionRecord.setRevisionId(document.getRevisionId());
		revisionRecord.setUserAction(ACTION_CANCELCHECKOUT);
		revisionRecord.setUserName(checkedOutDocument.getUserName());
		revisionRecord.setComments("Checkout cancelled");		
		addRevisionRecord(revisionRecord);
	}
	
	private void addRevisionRecord(RevisionRecord revisionRecord)throws Exception{
		kLogger.fine("Adding revision record : documentId= " + revisionRecord.getDocumentId());		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_REVISIONHISTORY);		
		int i=1;
		psInsert.setInt(i++,revisionRecord.getDocumentId());
		psInsert.setString(i++,revisionRecord.getRevisionId());
		psInsert.setString(i++,revisionRecord.getUserName());
		psInsert.setString(i++,revisionRecord.getUserAction());
		psInsert.setString(i++,revisionRecord.getComments());		
		int recCount = psInsert.executeUpdate();		
		psInsert.close();
		connection.commit();		
		connection.close();		
		if(recCount == 0){
			kLogger.warning("Unable to add revision record : documentId= " + revisionRecord.getDocumentId());
			throw new Exception("Unable to add revision record : documentId= " + revisionRecord.getDocumentId());
		}
	}
	
	public void deleteRevisionRecord(final int documentId) throws Exception{
		    kLogger.fine("Deleting revision history for document: documentId= " + documentId);		
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_REVISIONHISTORY);
			psDelete.setInt(1,documentId);
			psDelete.executeUpdate();
			psDelete.close();
			connection.commit();
			connection.close();		
	}
}
