/**
 * Created on Oct 2, 2005 
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.util.DBStringHelper;

/**
 * Creates / deletes Index tables  and Data tables for given document class 
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.model.vo.DocumentClass
 * @see com.primeleaf.krystal.model.dao.DocumentClassDAO
 */
public class TableManager {

	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	/**
	 * Default constructor
	 */
	public TableManager() {
		super();
	}
	
	public String createIndexTable(DocumentClass documentClass) throws Exception{
		
		kLogger.fine("Creating Index Table for document class : name=" + documentClass.getClassName());

		String indexTableName = formatTableName(documentClass.getClassId(),"I");
		
		String SQL_CREATE_INDEX_TABLE = "CREATE TABLE " + indexTableName +  " (DOCUMENTID int NOT NULL ,  REVISIONID varchar(50) default '1.0' ,  CONTENT "; //Full text index data is stored in this column
		
		SQL_CREATE_INDEX_TABLE+=DBStringHelper.getDBText();
		
		for(IndexDefinition indexDefinition:documentClass.getIndexDefinitions()){
			SQL_CREATE_INDEX_TABLE += ", I"+indexDefinition.getIndexColumnName() + 
								" " +   DBStringHelper.getDBVarchar() + " (" + indexDefinition.getIndexMaxLength() + ") "; 
		}
		SQL_CREATE_INDEX_TABLE += DBStringHelper.getFullTextDefinition("CONTENT");
		
		SQL_CREATE_INDEX_TABLE += " )";
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		Statement statement = connection.createStatement();
		
		statement.executeUpdate(SQL_CREATE_INDEX_TABLE);
		
		connection.commit();
	
		statement.close();
		
		connection.close();
		
		return indexTableName;
	}
	
	public String createDataTable(final int documentClassId)throws Exception{
		kLogger.fine("Creating Data Table for document class : id=" + documentClassId);
		
		String dataTableName=null;
		dataTableName=formatTableName(documentClassId,"Z");
		
		String SQL_CREATE_DATA_TABLE = "";
		
		SQL_CREATE_DATA_TABLE = " CREATE TABLE " + dataTableName + " ( " +
					DBStringHelper.getAutoIncColumn("DATAID") + ", " +
					DBStringHelper.getColumnDef("DATA",DBStringHelper.getDBImage(),""," NOT NULL ","") +
				")";
		

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		Statement statement = connection.createStatement();

		statement.executeUpdate(SQL_CREATE_DATA_TABLE);
		
		connection.commit();
		
		statement.close();
		
		connection.close();
		
		return dataTableName;
	}
	
	public static String formatTableName(final int id,final String prefix){
		String tableName=""+id;
		while(tableName.length()< 7){
			tableName = "0" + tableName;
		}
		tableName = prefix + tableName;
		return tableName;
	}
	
	public void dropIndexTable(final int id) throws Exception{
		kLogger.fine("Deleting Index Table for document class : id=" + id);
		String tableName=formatTableName(id,"I");
		dropTable(tableName);
	}
	
	public void dropDataTable(final int id) throws Exception{
		kLogger.fine("Deleting Data Table for document class : id=" + id);
		String tableName=formatTableName(id,"Z");
		dropTable(tableName);
	}

	private void dropTable(String tableName) throws Exception{
		String SQL_DROP_INDEX_TABLE="DROP TABLE " + tableName;
		Connection connection= ConnectionPoolManager.getInstance().getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(SQL_DROP_INDEX_TABLE);
		statement.close();
		connection.commit();
		connection.close();
	}
	
	public void alterIndexTable(DocumentClass documentClass,IndexDefinition indexDefinition)throws Exception{
		kLogger.fine("Altering Index Table for document class : name=" + documentClass.getClassName());
		kLogger.fine("Adding Index name=" + indexDefinition.getIndexColumnName());
		String indexTableName=documentClass.getIndexTableName();
		String SQL_ALTER_INDEX_TABLE = "ALTER TABLE " + indexTableName +  " ADD I" + indexDefinition.getIndexColumnName() +  " " + DBStringHelper.getDBVarchar()  + "  (" + indexDefinition.getIndexMaxLength() + ")";
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(SQL_ALTER_INDEX_TABLE);
		connection.commit();
		statement.close();
		connection.close();
	}
	
	public void alterTable(String tableName,String columnName,int columnLenth){
		try{
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		String SQL = "ALTER TABLE " + tableName ;
			   SQL+= " MODIFY "+columnName +" VARCHAR(" + columnLenth + ")";
			   connection.createStatement().executeUpdate(SQL);
		connection.close();	 
		}catch( SQLException e ){
			kLogger.severe("Unable to alter index table " +  e.getLocalizedMessage());
	    }
	}
	
	/**
	 * This method drops the column from I00000X table
	 * @param Document Class from which index is to be removed
	 * @param Index Definition to be deleted
	 * @throws Exception
	 * @author Rahul Kubadia
	 * @since 7.1 
	 */
	
	public void dropIndexColumn(DocumentClass documentClass,IndexDefinition indexDefinition)throws Exception{
		kLogger.fine("Altering Index Table for document class : name=" + documentClass.getClassName());
		kLogger.fine("Deleting Index name=" + indexDefinition.getIndexColumnName());
		String indexTableName=documentClass.getIndexTableName();
		String SQL_ALTER_INDEX_TABLE_COLUMN = "ALTER TABLE " + indexTableName +  " DROP COLUMN I" + indexDefinition.getIndexColumnName();
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(SQL_ALTER_INDEX_TABLE_COLUMN);
		statement.close();
		connection.close();
	}
}
