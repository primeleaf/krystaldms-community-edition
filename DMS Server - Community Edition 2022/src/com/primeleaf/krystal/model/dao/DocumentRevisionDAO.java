/**
 * Created on Oct 6, 2005 
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
package com.primeleaf.krystal.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.IndexRecordManager;
import com.primeleaf.krystal.model.vo.DocumentRevision;

/**
 * Database access mechanism for DocumentRevision is implemented here
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.data.DocumentRevision
 * @see com.primeleaf.krystal.data.Document
 * @see com.primeleaf.dms.data.DocumentDAO
 */
public class DocumentRevisionDAO {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	private final String SQL_INSERT_DOCUMENTREVISION="INSERT INTO DOCUMENTREVISIONS (DOCUMENTID,REVISIONID,SOFFSET,LENGTH)VALUES(?,?,?,?)";
	
	private final String SQL_UPDATE_DOCUMENTREVISION="UPDATE DOCUMENTREVISIONS SET DOCUMENTID = ?,REVISIONID =?,SOFFSET=?,LENGTH=? WHERE DOCUMENTID=? AND REVISIONID=?";
	
	private final String SQL_DELETE_DOCUMENTREVISION="DELETE FROM DOCUMENTREVISIONS WHERE DOCUMENTID=? AND REVISIONID=?";
	private final String SQL_DELETE_DOCUMENTREVISIONBYID="DELETE FROM DOCUMENTREVISIONS WHERE DOCUMENTID=?";
	
	private final String SQL_SELECT_DOCUMENTREVISIONBYID="SELECT DOCUMENTID,REVISIONID,SOFFSET,LENGTH FROM DOCUMENTREVISIONS  WHERE DOCUMENTID=? AND REVISIONID=?";
	private final String SQL_SELECT_DOCUMENTREVISIONS="SELECT DOCUMENTID,REVISIONID,SOFFSET,LENGTH FROM DOCUMENTREVISIONS  WHERE DOCUMENTID=?";
	
	private static DocumentRevisionDAO _instance; 
	
	/**
	 * Default constructor
	 */
	
	private DocumentRevisionDAO() {
		super();
	}
	
	public static synchronized DocumentRevisionDAO getInstance() { 
		if (_instance==null) { 
			_instance = new DocumentRevisionDAO(); 
		} 
		return _instance; 
	} 
	public void addDocumentRevision(DocumentRevision documentRevision)throws Exception{
		kLogger.fine("Adding document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_DOCUMENTREVISION);
		
		int i=1;
		psInsert.setInt(i++,documentRevision.getDocumentId());
		psInsert.setString(i++,documentRevision.getRevisionId());
		psInsert.setInt(i++,documentRevision.getOffset());
		psInsert.setInt(i++,documentRevision.getLength());		

		int recCount = psInsert.executeUpdate();
		
		psInsert.close();
		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to add document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
			throw new Exception("Unable to add document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
		}
	}
	
	public void updateDocumentRevision(DocumentRevision documentRevision)throws Exception{
		kLogger.fine("Updating document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_DOCUMENTREVISION);
		
		int i=1;
		
		psUpdate.setInt(i++,documentRevision.getDocumentId());
		psUpdate.setString(i++,documentRevision.getRevisionId());
		psUpdate.setInt(i++,documentRevision.getOffset());
		psUpdate.setInt(i++,documentRevision.getLength());		

		psUpdate.setInt(i++,documentRevision.getDocumentId());
		psUpdate.setString(i++,documentRevision.getRevisionId());
		
		int recCount = psUpdate.executeUpdate();
		
		psUpdate.close();
		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to update document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
			throw new Exception("Unable to update document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
		}
	}
	
	public void deleteDocumentRevision(DocumentRevision documentRevision)throws Exception{
		kLogger.fine("Deleting document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
		
		IndexRecordManager indexRecordManager =IndexRecordManager.getInstance();
		//DataHandler dataHandler = new DataHandler();
		//dataHandler.setFolderName(TableManager.formatTableName(documentRevision.getClassId(),"Z"));

		indexRecordManager.deleteIndexRecord(documentRevision.getClassId(),documentRevision.getDocumentId(),documentRevision.getRevisionId());
		//dataHandler.deleteDataRecord(documentRevision.getOffset());
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_DOCUMENTREVISION);
		int i=1;
		psDelete.setInt(i++,documentRevision.getDocumentId());
		psDelete.setString(i++,documentRevision.getRevisionId());
		int recCount = psDelete.executeUpdate();
		
		psDelete.close();
		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to delete document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
			throw new Exception("Unable to delete document revision : documentId = " + documentRevision.getDocumentId() + " , revisionId = " + documentRevision.getRevisionId());
		}
	}

	public void deleteDocumentRevisionById(final int documentId)throws Exception{
		kLogger.fine("Deleting document revision : documentId = " + documentId );
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_DOCUMENTREVISIONBYID);
		
		int i=1;
		
		psDelete.setInt(i++,documentId);
		
		int recCount = psDelete.executeUpdate();
		
		psDelete.close();
		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to delete document revision : documentId = " + documentId);
			throw new Exception("Unable to delete document revision : documentId = " + documentId);
		}
	}

	public DocumentRevision readDocumentRevisionById(final int documentId,final String revisionId)throws Exception{
		DocumentRevision result = null;
		
		kLogger.fine("Reading document revision : documentId = " + documentId + " , revisionId = " + revisionId);
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_DOCUMENTREVISIONBYID);
		
		psSelect.setInt(1,documentId);
		psSelect.setString(2,revisionId);
		
		ResultSet rs = psSelect.executeQuery();
		
		if (rs.next()) {
			result = new DocumentRevision();
			result.setDocumentId(rs.getInt("DOCUMENTID"));
			result.setRevisionId(rs.getString("REVISIONID"));
			result.setOffset(rs.getInt("SOFFSET"));
			result.setLength(rs.getInt("LENGTH"));
		}
		rs.close();
		psSelect.close();
		connection.close();
		return result;
	}
	
	public ArrayList<DocumentRevision> readDocumentRevisionsById(final int documentId)throws Exception{
		ArrayList<DocumentRevision> result = new ArrayList<DocumentRevision>();
		
		kLogger.fine("Reading document revisions : documentId = " + documentId);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_DOCUMENTREVISIONS);
			
			psSelect.setInt(1,documentId);
			
			ResultSet rs = psSelect.executeQuery();
			
			while(rs.next()) {
				DocumentRevision documentRevision= new DocumentRevision();
				documentRevision.setDocumentId(rs.getInt("DOCUMENTID"));
				documentRevision.setRevisionId(rs.getString("REVISIONID"));
				documentRevision.setOffset(rs.getInt("SOFFSET"));
				documentRevision.setLength(rs.getInt("LENGTH"));
				
				result.add(documentRevision);
			}
			rs.close();
			psSelect.close();
			connection.close();
		}catch (Exception e){
			kLogger.warning("Unable to read document revisions : documentId = " + documentId );
		}
		
		return result;
	}
	public double documentSize(){
		double result=0;
		try{
			StringBuffer sbSelect = new StringBuffer("SELECT SUM(LENGTH) FROM DOCUMENTREVISIONS");
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			ResultSet rs = psSelect.executeQuery();
			result = rs.next()?rs.getDouble(1):0;
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to count documents size...");
			e.printStackTrace();
		}
		return result;
	}
}
