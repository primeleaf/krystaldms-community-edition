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
package com.primeleaf.krystal.model;import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
;


/**
 * This class contains methods which will work on a single index record<br>
 *
 * @author Rahul Kubadia
 * @since 2.0
 */
public class IndexRecordManager {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	private static IndexRecordManager instance;
	/**
	 * Default constructor
	 */

	private IndexRecordManager() {
		super();
	}

	public static IndexRecordManager getInstance(){
		if(instance==null){
			instance=new IndexRecordManager();
		}

		return instance;
	}

	public void createIndexRecord(DocumentClass documentClass, final int documentId , final String revisionId,Hashtable<String,String> indexRecord)throws Exception{
		kLogger.fine("Creating index record for document class" + documentClass.getClassDescription() + " documentId ="+ documentId +" revisionId =" + revisionId );
		Hashtable<String, String> finalIndexRecord = new Hashtable<String, String>();
		for(String key : indexRecord.keySet()){
			String value = indexRecord.get(key);
			finalIndexRecord.put(key, value);
		}
		String indexTableName = documentClass.getIndexTableName();

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		String SQL_INSERT_RECORD="INSERT INTO " + indexTableName + " (DOCUMENTID, REVISIONID ";

		for(String key : finalIndexRecord.keySet()){
			SQL_INSERT_RECORD += ",I" + key;
		}

		SQL_INSERT_RECORD+= " ) VALUES (?,?";

		for(int i =0;i < finalIndexRecord.size();i++){
			SQL_INSERT_RECORD+= ",?";
		}

		SQL_INSERT_RECORD+= ")";

		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_RECORD);

		int i=1;

		psInsert.setInt(i++,documentId);
		psInsert.setString(i++,revisionId);

		for(String value:finalIndexRecord.values()){
			psInsert.setString(i++,value);
		}

		psInsert.executeUpdate();
		psInsert.close();
		connection.commit();
		connection.close();
	}

	public void updateIndexRecord(DocumentClass documentClass, final int documentId, final String revisionId,Hashtable<String,String> indexRecord)throws Exception{
		kLogger.fine("Updating index record for document class" + documentClass.getClassDescription() + " documentId ="+ documentId +" revisionId =" + revisionId );
		String indexTableName = documentClass.getIndexTableName();

		if(documentClass.getIndexDefinitions().size() > 0){
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			String SQL_UPDATE_RECORD="UPDATE " + indexTableName + " SET  ";

			for(String key : indexRecord.keySet()){
				SQL_UPDATE_RECORD += "I" + key + "=?,";
			}

			SQL_UPDATE_RECORD= SQL_UPDATE_RECORD.substring(0,SQL_UPDATE_RECORD.length()-1) + " WHERE DOCUMENTID=? AND REVISIONID=?";

			PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_RECORD);

			int i=1;

			for(String value: indexRecord.values()){
				psUpdate.setString(i++,value);
			}

			psUpdate.setInt(i++,documentId);

			psUpdate.setString(i++,revisionId);

			psUpdate.executeUpdate();

			psUpdate.close();

			connection.commit();

			connection.close();
		}

	}

	public void deleteIndexRecord(int documentClassId, final int documentId , final String revisionId)throws Exception{

		String indexTableName= TableManager.formatTableName(documentClassId,"I");

		Connection connection= ConnectionPoolManager.getInstance().getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate("DELETE FROM "+indexTableName+" WHERE DOCUMENTID="+documentId+" AND REVISIONID='"+revisionId+"'");

		statement.close();
		connection.commit();
		connection.close();

	}

	public LinkedHashMap<String,String> readIndexRecord(DocumentClass documentClass , final int documentId,final String revisionId)throws Exception{
		LinkedHashMap<String,String> indexRecord =new LinkedHashMap<String,String>();
		kLogger.fine("Reading index record for document class " + documentClass.getClassDescription() + " documentId =" + documentId + " , revisionId = " + revisionId);
		String indexTableName = documentClass.getIndexTableName();

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement psSelect = connection.prepareStatement("SELECT * FROM " + indexTableName + " WHERE  DOCUMENTID = ? AND REVISIONID =?");
		psSelect.setInt(1,documentId);
		psSelect.setString(2,revisionId);
		rs = psSelect.executeQuery();
		String columnDisplayName,indexValue;
		if(rs.next()){
			for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
				columnDisplayName = indexDefinition.getIndexDisplayName();
				String indexName = indexDefinition.getIndexColumnName();
				indexName= "I"+indexName;
				indexValue = rs.getString(indexName)!=null?rs.getString(indexName):"";//Code for testing null values in ORACLE
				indexRecord.put(columnDisplayName,indexValue);
			}
		}

		rs.close();
		psSelect.close();
		connection.close();

		return indexRecord;
	}

	public String getHeadRevisionId(final int documentId)throws Exception{
		String revisionId="";
		kLogger.fine("Reading head revision id for document: id= " +   documentId );

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement psSelect = connection.prepareStatement("SELECT REVISIONID FROM DOCUMENTS  WHERE  DOCUMENTID = ?");
		psSelect.setInt(1,documentId);
		rs = psSelect.executeQuery();
		if(rs.next()){
			revisionId=rs.getString(1);
		}
		rs.close();
		psSelect.close();
		connection.close();


		return revisionId;
	}

	public String getIndexValue(final int documentId,final String revisionId, final String indexColumnName,String indexTableName)throws Exception{
		String result="";
		kLogger.fine("Reading index value ["+ indexColumnName + "] for document: documentId= " +   documentId + ", revisionId =" + revisionId );

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement psSelect = connection.prepareStatement("SELECT I"+indexColumnName+ " FROM " + indexTableName +"  WHERE  DOCUMENTID = ? AND REVISIONID=?");
		psSelect.setInt(1,documentId);
		psSelect.setString(2,revisionId);
		rs = psSelect.executeQuery();
		if(rs.next()){
			result=rs.getString(1);
		}
		rs.close();
		psSelect.close();
		connection.close();


		return result;
	}

	public ArrayList<String> getDistinctIndexValues(String className, String indexColumnName)throws Exception{
		ArrayList<String> indexValues = new ArrayList<String>();
		kLogger.fine("Reading index value ["+ indexColumnName + "] for document: document class name= " +   className );

		DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassByName(className);
		String indexTableName = documentClass.getIndexTableName();
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement psSelect = connection.prepareStatement("SELECT DISTINCT I"+indexColumnName+ " FROM " + indexTableName);
		rs = psSelect.executeQuery();
		while(rs.next()){
			String indexValue = rs.getString(1);
			if(indexValue!=null){//Code for testing null values in ORACLE
				indexValues.add(indexValue);
			}
		}
		rs.close();
		psSelect.close();
		connection.close();

		return indexValues;
	}
	
	/**
	 * This method checks if the given index value exist in the given index column in the document class
	 * @author Rahul Kubadia 
	 * @since 8.0 (30-Sep-2013)
	 * @param indexValue
	 * @param indexColumnName
	 * @param indexTableName
	 * @return valueExistOrNot
	 */

	public boolean valueExist(String indexValue, String indexColumnName,String indexTableName){
		boolean result = false;
		kLogger.fine("Checking index value ["+ indexColumnName + "] for in " + indexTableName);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			ResultSet rs = null;
			PreparedStatement psSelect = connection.prepareStatement("SELECT I"+indexColumnName+ " FROM " + indexTableName +"  WHERE  I"+indexColumnName+ " = ?");
			psSelect.setString(1,indexValue);
			rs = psSelect.executeQuery();
			result = rs.next();
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.fine("Checking index value ["+ indexColumnName + "] for in " + indexTableName + " failed");
			e.printStackTrace();
		}
		return result;
	}
}