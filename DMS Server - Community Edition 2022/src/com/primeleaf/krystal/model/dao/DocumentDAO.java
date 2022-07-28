/**
 * Created on Oct 4, 2005 
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentRevision;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.util.DBStringHelper;
import com.primeleaf.krystal.util.StringHelper;
/**
 * Database access to the Document value object is performed here
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.data.Document
 */
public class DocumentDAO {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	private String SQL_INSERT_DOCUMENT = " INSERT INTO DOCUMENTS (REVISIONID,CLASSID,DOCUMENTTYPE,STATUS,EXT,HASNOTE,ACCESSCOUNT,CREATEDBY,EXPIRY,MODIFIEDBY,MODIFIED) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	private String SQL_UPDATE_DOCUMENT=  " UPDATE DOCUMENTS SET REVISIONID=?, CLASSID=?, DOCUMENTTYPE=?, STATUS=?, EXT=?, HASNOTE = ?, ACCESSCOUNT=?, LASTACCESS=?, MODIFIED=?, MODIFIEDBY=?, EXPIRY=? WHERE DOCUMENTID=?";
	private String SQL_DELETE_DOCUMENT=  " DELETE FROM DOCUMENTS WHERE DOCUMENTID=?";
	private String SQL_SELECT_DOCUMENTBYID= "SELECT DOCUMENTID,REVISIONID,CLASSID,DOCUMENTTYPE,STATUS,CREATED,EXT,HASNOTE,LASTACCESS,ACCESSCOUNT,CREATEDBY,EXPIRY,MODIFIED,MODIFIEDBY FROM DOCUMENTS WHERE DOCUMENTID=?";

	private static DocumentDAO _instance;

	/**
	 * Default constructor
	 */

	private DocumentDAO() {
		super();
	}
	public static synchronized DocumentDAO getInstance() { 
		if (_instance==null) { 
			_instance = new DocumentDAO(); 
		} 
		return _instance; 
	}

	public void addDocument(Document document) throws Exception{
		kLogger.fine("Adding document  to class :classId= " + document.getClassId() );

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_DOCUMENT);

		int i=1;

		psInsert.setString(i++,document.getRevisionId());
		psInsert.setInt(i++,document.getClassId());
		psInsert.setString(i++,document.getDocumentType());
		psInsert.setString(i++,document.getStatus());
		psInsert.setString(i++,document.getExtension().toUpperCase());
		psInsert.setInt(i++,0);
		psInsert.setInt(i++,0);
		psInsert.setString(i++,document.getCreatedBy());
		psInsert.setTimestamp(i++, document.getExpiry());
		psInsert.setString(i++,document.getModifiedBy());
		
		Timestamp modified = new Timestamp(Calendar.getInstance().getTime().getTime());
		psInsert.setTimestamp(i++, modified);

		int recCount = psInsert.executeUpdate();

		psInsert.close();
		connection.commit();
		connection.close();

		document.setDocumentId(DBStringHelper.getGeneratedKey("DOCUMENTID", "DOCUMENTS"));

		if(recCount == 0){
			kLogger.warning("Unable to add document to class : classid= " + document.getClassId());
			throw new Exception("Unable to add document to class : classid= " + document.getClassId());
		}
	}
	public void updateDocument(Document document)throws Exception{
		kLogger.fine("Updating document : id= " + document.getDocumentId());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_DOCUMENT);

		int i=1;
		psUpdate.setString(i++,document.getRevisionId());
		psUpdate.setInt(i++,document.getClassId());
		psUpdate.setString(i++,document.getDocumentType());
		psUpdate.setString(i++,document.getStatus());
		psUpdate.setString(i++,document.getExtension().toUpperCase());
		psUpdate.setInt(i++,document.getHasNote());
		psUpdate.setInt(i++,document.getAccessCount());
		psUpdate.setTimestamp(i++,document.getLastAccessed());
		psUpdate.setTimestamp(i++,document.getModified());
		psUpdate.setString(i++,document.getModifiedBy());
		psUpdate.setTimestamp(i++,document.getExpiry());

		psUpdate.setInt(i++,document.getDocumentId());
		int recCount = psUpdate.executeUpdate();

		psUpdate.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to update document : id= " + document.getDocumentId());
			throw new Exception("Unable to update document : id= " + document.getDocumentId());
		}
	}
	public void deleteDocument(Document document)throws Exception{
		kLogger.fine("Deleting document : id= " + document.getDocumentId());

		for(DocumentRevision documentRevision : DocumentRevisionDAO.getInstance().readDocumentRevisionsById(document.getDocumentId())){
			documentRevision.setClassId(document.getClassId());
			DocumentRevisionDAO.getInstance().deleteDocumentRevision(documentRevision);
		}

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_DOCUMENT);

		psDelete.setInt(1,document.getDocumentId());

		int recCount = psDelete.executeUpdate();

		psDelete.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to delete document : id= " + document.getDocumentId());
			throw new Exception("Unable to delete document : id= " + document.getDocumentId());
		}
	}

	public Document readDocumentById(final int documentId) throws Exception{
		Document result = null;
		kLogger.fine("Reading document : id= " + documentId);
		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_DOCUMENTBYID);

		psSelect.setInt(1,documentId);

		ResultSet rs = psSelect.executeQuery();

		if (rs.next()) {
			result = new Document();
			result.setDocumentId(rs.getInt("DOCUMENTID"));
			result.setRevisionId(rs.getString("REVISIONID"));
			result.setClassId(rs.getInt("CLASSID"));
			result.setDocumentType(rs.getString("DOCUMENTTYPE"));
			result.setStatus(rs.getString("STATUS"));
			result.setCreated(rs.getString("CREATED"));
			result.setExtension(rs.getString("EXT"));
			result.setHasNote(rs.getByte("HASNOTE"));
			result.setLastAccessed(rs.getTimestamp("LASTACCESS"));
			result.setAccessCount(rs.getInt("ACCESSCOUNT"));
			result.setCreatedBy(rs.getString("CREATEDBY"));
			result.setExpiry(rs.getTimestamp("EXPIRY"));
			result.setModified(rs.getTimestamp("MODIFIED"));
			result.setModifiedBy(rs.getString("MODIFIEDBY"));
		}
		rs.close();
		psSelect.close();
		connection.close();

		return result;
	}

	/**
         This function is used by RetentionProcessor to get the collection of documents for deletion/archiving
	 */
	public ArrayList<Document> readDocuments(final String query){
		ArrayList<Document> result = new ArrayList<Document>();
		kLogger.fine("Reading Documents .....");
		try{
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append(query);
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			ResultSet rs = psSelect.executeQuery();

			while (rs.next()) {
				Document document = new Document();

				document.setDocumentId(rs.getInt("DOCUMENTID"));
				document.setRevisionId(rs.getString("REVISIONID"));
				document.setClassId(rs.getInt("CLASSID"));
				document.setDocumentType(rs.getString("DOCUMENTTYPE"));
				document.setStatus(rs.getString("STATUS"));
				document.setCreated(rs.getString("CREATED"));
				document.setModified(rs.getTimestamp("MODIFIED"));
				document.setModifiedBy(rs.getString("MODIFIEDBY"));
				document.setExpiry(rs.getTimestamp("EXPIRY"));
				document.setExtension(rs.getString("EXT"));
				document.setHasNote(rs.getByte("HASNOTE"));
				document.setLastAccessed(rs.getTimestamp("LASTACCESS"));
				document.setAccessCount(rs.getInt("ACCESSCOUNT"));
				document.setCreatedBy(rs.getString("CREATEDBY"));

				result.add(document);
			}
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to read documents...");
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * This function returns count of documents with required status along with 
	 * additional query if any.  
	 * @param documentStatus (I-Inline , L-Locked , D-Deleted)
	 * @param query ("" for empty query)
	 * @return count of documents with required status along with additional query if any 
	 */
	public int countDocuments(final String documentStatus,final String query){
		int result = 0;
		kLogger.fine("Counting Documents .....");
		try{
			StringBuffer sbSelect = new StringBuffer("SELECT COUNT(*) FROM DOCUMENTS WHERE STATUS = '" +documentStatus + "'");
			if(query.length()>1){
				sbSelect.append(" AND " + query);
			}
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			ResultSet rs = psSelect.executeQuery();
			result = rs.next()?rs.getInt(1):0;

			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to count documents...");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This function returns count of available documents (inline + locked) documents in a document class 
	 * @since 3.0
	 * @param classId
	 * @return count of documents in the document class
	 */
	public int countActiveDocuments(int classId){
		int result = 0;
		kLogger.fine("Counting Total Documents in Document Class...");
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement("SELECT COUNT(*) FROM DOCUMENTS WHERE CLASSID = ? AND (STATUS = '"+ Hit.STATUS_AVAILABLE+"' OR STATUS='" + Hit.STATUS_LOCKED + "')" );
			psSelect.setInt(1, classId);
			ResultSet rs = psSelect.executeQuery();
			result = rs.next()?rs.getInt(1):0;
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to count total documents in a document class. Class ID:" + classId);
			e.printStackTrace();
		}
		return result;
	}
	
	public int countTotalDocuments(int classId){
		int result = 0;
		kLogger.fine("Counting total documents in Document Class : Class ID =" + classId);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement("SELECT COUNT(*) FROM DOCUMENTS WHERE CLASSID = ? ");
			psSelect.setInt(1, classId);
			ResultSet rs = psSelect.executeQuery();
			result = rs.next()?rs.getInt(1):0;
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to count total documents in Document Class : Class ID =" + classId);
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * This function returns count of documents in repository 
	 * @since 3.0
	 * @return count of documents
	 */
	public int countTotalDocuments(){
		int result = 0;
		kLogger.fine("Counting Total Documents in Repository...");
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement("SELECT COUNT(*) FROM DOCUMENTS");
			ResultSet rs = psSelect.executeQuery();
			result = rs.next()?rs.getInt(1):0;
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to count total documents...");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This function returns the count of documents in a document class for given status and interval
	 * @since 3.0
	 * @param documentStatus
	 * @param classId
	 * @param interval
	 * @return count of documents
	 */

	public int countActiveDocumentsForInterval(int classId,int interval,String type){
		int count = 0;
		try{
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE,59);
			cal.set(Calendar.SECOND,59);
			java.sql.Date to=new java.sql.Date(cal.getTimeInMillis());

			Calendar cal1=Calendar.getInstance();
			cal1.add(Calendar.DAY_OF_MONTH, -interval+1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND,0);
			java.sql.Date from=new java.sql.Date(cal1.getTimeInMillis());

			StringBuffer sbSelect = new StringBuffer("SELECT COUNT(*) FROM DOCUMENTS WHERE STATUS IN ('"+ Hit.STATUS_AVAILABLE+"' , '" + Hit.STATUS_LOCKED + "') AND CLASSID=? AND "+ type.toUpperCase() +" BETWEEN ? AND ? ");

			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());

			psSelect.setInt(1, classId);
			psSelect.setDate(2, from);
			psSelect.setDate(3, to);

			ResultSet rs = psSelect.executeQuery();                 
			count = rs.next()?rs.getInt(1):0;

			rs.close();
			psSelect.close();
			connection.close();
		}catch (Exception e) {
			kLogger.warning("Unable to count documents...");
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * This function returns the count of documents expiring in a document class for given status and interval
	 * @since 5.1
	 * @param documentStatus
	 * @param classId
	 * @param interval in number of days
	 * @return count of documents expiring
	 */

	public int countExpiringDocumentsForInterval(int classId,int interval){
		int count = 0;
		try{
			Calendar cal=Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND,0);
			java.sql.Date from=new java.sql.Date(cal.getTimeInMillis());
			Calendar cal1=Calendar.getInstance();
			cal1.add(Calendar.DAY_OF_MONTH, interval);
			cal1.set(Calendar.HOUR_OF_DAY, 23);
			cal1.set(Calendar.MINUTE, 59);
			cal1.set(Calendar.SECOND,59);

			java.sql.Date to=new java.sql.Date(cal1.getTimeInMillis());

			StringBuffer sbSelect = new StringBuffer("SELECT COUNT(*) FROM DOCUMENTS WHERE STATUS IN ('"+ Hit.STATUS_AVAILABLE+"' , '" + Hit.STATUS_LOCKED + "') AND CLASSID=? AND EXPIRY BETWEEN ? AND ?");

			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());

			psSelect.setInt(1, classId);
			psSelect.setDate(2, from);
			psSelect.setDate(3, to);

			ResultSet rs = psSelect.executeQuery();			
			count = rs.next()?rs.getInt(1):0;

			rs.close();
			psSelect.close();
			connection.close();

		}catch (Exception e) {
			kLogger.warning("Unable to count documents...");
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * This method gets total size of documents in document class having classId
	 * @param classId
	 * @return
	 */
	public double documentSize(int classId){
		double result=0;
		try{
			StringBuffer sbSelect = new StringBuffer("SELECT SUM(LENGTH) FROM DOCUMENTREVISIONS WHERE DOCUMENTID IN (SELECT DOCUMENTID FROM DOCUMENTS WHERE CLASSID=? ) ");
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			psSelect.setInt(1, classId);
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

	public LinkedHashMap<String, Integer> getDocumentCountMonthWise(int classId){
		LinkedHashMap<String ,Integer> result = new LinkedHashMap<String,Integer>();
		try{
			for(int i = 6; i >= 0 ; i--){
				Calendar firstDay =  Calendar.getInstance();

				firstDay.add(Calendar.MONTH, -i);
				firstDay.set(Calendar.DAY_OF_MONTH, 1);
				firstDay.set(Calendar.HOUR_OF_DAY, 0);
				firstDay.set(Calendar.MINUTE,0);
				firstDay.set(Calendar.SECOND,0);

				java.sql.Timestamp from = new java.sql.Timestamp(firstDay.getTimeInMillis());

				Calendar monthDay =  firstDay;

				int numberOfDaysInMonth = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
				firstDay.set(Calendar.DAY_OF_MONTH, numberOfDaysInMonth);
				firstDay.set(Calendar.HOUR_OF_DAY, 23);
				firstDay.set(Calendar.MINUTE,59);
				firstDay.set(Calendar.SECOND,59);

				java.sql.Timestamp to = new java.sql.Timestamp(firstDay.getTimeInMillis());
				StringBuffer sbSelect = new StringBuffer("SELECT COUNT(*) FROM DOCUMENTS WHERE CLASSID=? AND CREATED BETWEEN ? AND ? ");

				Connection connection = ConnectionPoolManager.getInstance().getConnection();
				PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());

				psSelect.setInt(1, classId);
				psSelect.setTimestamp(2, from);
				psSelect.setTimestamp(3, to);

				ResultSet rs = psSelect.executeQuery();			
				int count = rs.next()?rs.getInt(1):0;

				result.put(StringHelper.formatDate(monthDay.getTime(),"yyyy-M"), Integer.valueOf(count));
				rs.close();

				psSelect.close();
				connection.close();
			}

		}catch (Exception e) {
			kLogger.warning("Unable to count documents...");
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * This function returns count of documents in repository 
	 * @since 3.0
	 * @return count of documents
	 */
	public int countTotalActiveDocuments() {
		int result = 0;
		Connection connection=null;
		PreparedStatement psSelect=null;
		ResultSet rs=null;
		try{
			connection = ConnectionPoolManager.getInstance().getConnection();
			psSelect = connection.prepareStatement("SELECT COUNT(*) FROM DOCUMENTS WHERE STATUS IN ('"+Hit.STATUS_AVAILABLE+"','"+Hit.STATUS_LOCKED+"') ");
			rs = psSelect.executeQuery();
			result = rs.next()?rs.getInt(1):0;
		}catch(SQLException sqe){
			kLogger.warning("Unable to count documents...");
			sqe.printStackTrace();
		}finally{
			try{
				if(rs != null){
					rs.close();	
				}
				if(psSelect != null){
					psSelect.close();	
				}
				if(connection != null){
					connection.close();
				}
			}catch(Exception ex){
				//We can not do anything about this
			}
		}
		return result;
	}
}
