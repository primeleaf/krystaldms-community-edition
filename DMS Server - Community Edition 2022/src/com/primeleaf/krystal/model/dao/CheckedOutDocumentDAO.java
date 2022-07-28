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
package com.primeleaf.krystal.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.DocumentClass;

/**
 * Database access mechanism for checked out objects is implemented here
 * @author Rahul Kubadia
 * @since 2.0
 * 
 */
public class CheckedOutDocumentDAO {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	private String SQL_INSERT_CHECKOUT = "INSERT INTO CHECKOUT(DOCUMENTID,REVISIONID,CLASSID,USERNAME,CHECKOUTPATH,COMMENTS)VALUES(?,?,?,?,?,?)";
	private String SQL_SELECT_CHECKOUTBYID="SELECT DOCUMENTID,REVISIONID,CLASSID,USERNAME,CHECKOUTPATH,CHECKOUTDATE,COMMENTS FROM CHECKOUT WHERE DOCUMENTID=? AND REVISIONID=?";
	private String SQL_DELETE_CHECKOUT = "DELETE FROM CHECKOUT WHERE DOCUMENTID=?";
	private String SQL_SELECT_CHECKOUTBYUSER=" SELECT DOCUMENTID,REVISIONID,CHECKOUT.CLASSID,CHECKOUT.USERNAME, CHECKOUTPATH,CHECKOUTDATE,COMMENTS ,DOCUMENTCLASSES.CLASSNAME, CLASSDESC "
											+ " FROM CHECKOUT INNER JOIN DOCUMENTCLASSES ON DOCUMENTCLASSES.CLASSID = CHECKOUT.CLASSID WHERE CHECKOUT.USERNAME=?";

	private static CheckedOutDocumentDAO _instance;
	/**
	 * Default constructor
	 */
	private CheckedOutDocumentDAO() {
		super();
	}
	
	public static synchronized CheckedOutDocumentDAO getInstance() { 
		if (_instance==null) { 
			_instance = new CheckedOutDocumentDAO(); 
		} 
		return _instance; 
	} 
	public void addCheckedOutDocument(CheckedOutDocument CheckedOutDocument)throws Exception{
		kLogger.fine("Adding entry to checkout:documentId= " + CheckedOutDocument.getDocumentId());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_CHECKOUT);
		
		int i=1;
		psInsert.setInt(i++,CheckedOutDocument.getDocumentId());
		psInsert.setString(i++,CheckedOutDocument.getRevisionId());
		psInsert.setInt(i++,CheckedOutDocument.getClassId());
		psInsert.setString(i++,CheckedOutDocument.getUserName());
		psInsert.setString(i++,CheckedOutDocument.getCheckOutPath());
		psInsert.setString(i++,CheckedOutDocument.getComments());
		
		int recCount = psInsert.executeUpdate();
		
		psInsert.close();

		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to add  checkout:documentId= " + CheckedOutDocument.getDocumentId());
			throw new Exception("Unable to add  checkout:documentId= " + CheckedOutDocument.getDocumentId());
		}

	}
	
	public void deleteCheckedOutDocument(CheckedOutDocument CheckedOutDocument)throws Exception{
		kLogger.fine("Deleting checkout : id= " + CheckedOutDocument.getDocumentId());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_CHECKOUT);
		
		psDelete.setInt(1,CheckedOutDocument.getDocumentId());
		
		int recCount = psDelete.executeUpdate();
		
		psDelete.close();
		
		connection.commit();

		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to delete checkout : id= " + CheckedOutDocument.getDocumentId());
			throw new Exception("Unable to delete checkout : id= " + CheckedOutDocument.getDocumentId());
		}
	}
	
	public CheckedOutDocument readCheckedOutDocument(final int documentId,final String revisionId)throws Exception{
		CheckedOutDocument result=new CheckedOutDocument();
		
		kLogger.fine("Reading checkout details for document id =" + documentId + ", revisionId =" + revisionId);
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_CHECKOUTBYID);
		
		psSelect.setInt(1,documentId);
		psSelect.setString(2,revisionId);
		
		ResultSet rs = psSelect.executeQuery();
		
		if(rs.next()){
			result = new CheckedOutDocument();
			result.setDocumentId(rs.getInt("DOCUMENTID"));
			result.setRevisionId(rs.getString("REVISIONID"));
			result.setClassId(rs.getInt("CLASSID"));
			result.setUserName(rs.getString("USERNAME"));
			result.setCheckOutPath(rs.getString("CHECKOUTPATH"));
			result.setCheckOutDate(rs.getString("CHECKOUTDATE"));
			result.setComments(rs.getString("COMMENTS"));
		}
		
		rs.close();
		psSelect.close();
		connection.close();
		return result;
		
	}
	
	public ArrayList<CheckedOutDocument> readCheckedOutDocumentsByUser(final String userName){
		ArrayList<CheckedOutDocument> result = new ArrayList<CheckedOutDocument>();
		
		kLogger.fine("Reading checkout by user : name = " + userName);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			
			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_CHECKOUTBYUSER);
			
			psSelect.setString(1,userName);
			
			ResultSet rs = psSelect.executeQuery();
			
			while(rs.next()){
				CheckedOutDocument checkedOutDocument = new CheckedOutDocument();
				checkedOutDocument.setDocumentId(rs.getInt("DOCUMENTID"));
				checkedOutDocument.setRevisionId(rs.getString("REVISIONID"));
				checkedOutDocument.setClassId(rs.getInt("CLASSID"));
				checkedOutDocument.setUserName(rs.getString("USERNAME"));
				checkedOutDocument.setCheckOutPath(rs.getString("CHECKOUTPATH"));
				checkedOutDocument.setCheckOutDate(rs.getString("CHECKOUTDATE"));
				checkedOutDocument.setComments(rs.getString("COMMENTS"));
				
				DocumentClass documentClass = new DocumentClass();
				documentClass.setClassId(checkedOutDocument.getClassId());
				documentClass.setClassName(rs.getString("CLASSNAME"));
				documentClass.setClassDescription(rs.getString("CLASSDESC"));
				
				checkedOutDocument.setDocumentClass(documentClass);
				result.add(checkedOutDocument);
			}
			
			rs.close();
			psSelect.close();
			connection.close();
			
		}catch(Exception e){
			e.printStackTrace();
			kLogger.warning("Unable to read checkouts by user :  =" + userName);
		}
		return result;
	}
	
	public ArrayList<CheckedOutDocument> readCheckedOutDocuments(final String criteria){
		ArrayList<CheckedOutDocument> result = new ArrayList<CheckedOutDocument>();
		
		kLogger.fine("Reading checkout by criteria : criteria= " + criteria);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			
			String SQL_SELECT_CHECKOUTS="SELECT DOCUMENTID,REVISIONID,CHECKOUT.CLASSID,CHECKOUT.USERNAME,CHECKOUTPATH,CHECKOUTDATE,COMMENTS, DOCUMENTCLASSES.CLASSNAME, CLASSDESC FROM CHECKOUT"
					+ " INNER JOIN DOCUMENTCLASSES ON DOCUMENTCLASSES.CLASSID = CHECKOUT.CLASSID";
			if(criteria.trim().length()>0){
				SQL_SELECT_CHECKOUTS+=" WHERE  " + criteria;
			}
			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_CHECKOUTS);
			
			ResultSet rs = psSelect.executeQuery();
			
			while(rs.next()){
				CheckedOutDocument checkedOutDocument = new CheckedOutDocument();
				checkedOutDocument.setDocumentId(rs.getInt("DOCUMENTID"));
				checkedOutDocument.setRevisionId(rs.getString("REVISIONID"));
				checkedOutDocument.setClassId(rs.getInt("CLASSID"));
				checkedOutDocument.setUserName(rs.getString("USERNAME"));
				checkedOutDocument.setCheckOutPath(rs.getString("CHECKOUTPATH"));
				checkedOutDocument.setCheckOutDate(rs.getString("CHECKOUTDATE"));
				checkedOutDocument.setComments(rs.getString("COMMENTS"));
				
				DocumentClass documentClass = new DocumentClass();
				documentClass.setClassId(checkedOutDocument.getClassId());
				documentClass.setClassName(rs.getString("CLASSNAME"));
				documentClass.setClassDescription(rs.getString("CLASSDESC"));
				checkedOutDocument.setDocumentClass(documentClass);
				result.add(checkedOutDocument);
			}
			
			rs.close();
			psSelect.close();
			connection.close();
			
		}catch(Exception e){
			e.printStackTrace();
			kLogger.warning("Unable to read checkouts by criteria : criteria=" + criteria);
		}
		return result;
	}
	
}
