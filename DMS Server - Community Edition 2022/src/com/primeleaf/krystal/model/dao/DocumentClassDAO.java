/**
 * Created on Sep 29, 2005 
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
import com.primeleaf.krystal.model.RevisionManager;
import com.primeleaf.krystal.model.TableManager;
import com.primeleaf.krystal.model.vo.CheckedOutDocument;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.util.DBStringHelper;
//import com.primeleaf.dms.data.Bookmark;

/**
 * Database related actions on Document Class value object is 
 * carried out in this class.
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.data.DocumentClass
 */
public class DocumentClassDAO {

	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());	

	private final String SQL_INSERT_DOCUMENTCLASS="INSERT INTO DOCUMENTCLASSES (CLASSNAME, CLASSDESC, INDEXID, INDEXTABLENAME, INDEXCOUNT, DATATABLENAME,REVISIONCTL,MAXFILESIZE,DOCUMENTLIMIT, ACTIVE , CREATEDBY , EXPIRYPERIOD,EXPIRYNOTIFICATIONPERIOD) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String SQL_SELECT_DOCUMENTCLASSBYID="SELECT CLASSID , CLASSNAME, CLASSDESC, INDEXID, INDEXTABLENAME, INDEXCOUNT, DATATABLENAME,REVISIONCTL,MAXFILESIZE,DOCUMENTLIMIT, ACTIVE, CREATEDBY, CREATED, EXPIRYPERIOD, EXPIRYNOTIFICATIONPERIOD, TOTALDOCUMENTS, ACTIVEDOCUMENTS FROM DOCUMENTCLASSES WHERE CLASSID=?";
	private String SQL_SELECT_DOCUMENTCLASSES="SELECT CLASSID , CLASSNAME, CLASSDESC, INDEXID, INDEXTABLENAME, INDEXCOUNT,  DATATABLENAME, REVISIONCTL , MAXFILESIZE,DOCUMENTLIMIT, ACTIVE, CREATEDBY, CREATED,EXPIRYPERIOD , EXPIRYNOTIFICATIONPERIOD, TOTALDOCUMENTS, ACTIVEDOCUMENTS  FROM DOCUMENTCLASSES WHERE CLASSID > 0";
	private final String SQL_SELECT_DOCUMENTCLASSBYNAME="SELECT CLASSID , CLASSNAME, CLASSDESC, INDEXID, INDEXTABLENAME, INDEXCOUNT, DATATABLENAME,REVISIONCTL,MAXFILESIZE,DOCUMENTLIMIT, ACTIVE, CREATEDBY, CREATED,EXPIRYPERIOD, EXPIRYNOTIFICATIONPERIOD, TOTALDOCUMENTS, ACTIVEDOCUMENTS  FROM DOCUMENTCLASSES WHERE CLASSNAME=?";
	private final String SQL_UPDATE_DOCUMENTCLASS="UPDATE DOCUMENTCLASSES SET  CLASSNAME=?, CLASSDESC=?, INDEXID=?, INDEXTABLENAME=?, INDEXCOUNT=?, DATATABLENAME=?,REVISIONCTL=?, MAXFILESIZE=?,DOCUMENTLIMIT=?, ACTIVE=? , EXPIRYPERIOD=? , EXPIRYNOTIFICATIONPERIOD= ? , TOTALDOCUMENTS= ? , ACTIVEDOCUMENTS = ?  WHERE CLASSID = ?";
	private final String SQL_DELETE_DOCUMENTCLASS="DELETE FROM DOCUMENTCLASSES WHERE CLASSID=?";	
	private static DocumentClassDAO instance;

	/**
	 * Default Constructor 
	 */
	private DocumentClassDAO() {
		super();
	}

	public static synchronized DocumentClassDAO getInstance() { 
		if (instance==null) { 
			instance = new DocumentClassDAO(); 
		} 
		return instance; 
	} 

	public void addDocumentClass(DocumentClass documentClass)throws Exception{		
		kLogger.fine("Adding Document Class : " + documentClass.getClassName().toUpperCase());		

		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_DOCUMENTCLASS);		

		int i=1;		
		psInsert.setString(i++,documentClass.getClassName().toUpperCase());
		psInsert.setString(i++,documentClass.getClassDescription());
		psInsert.setInt(i++,documentClass.getIndexId());
		psInsert.setString(i++,documentClass.getIndexTableName());
		psInsert.setInt(i++,documentClass.getIndexCount());
		psInsert.setString(i++,documentClass.getDataTableName());		
		if(documentClass.isRevisionControlEnabled()){
			psInsert.setString(i++,"Y");
		}else{
			psInsert.setString(i++,"N");
		}	
		//25-Nov-2011 New fields added for controlling the file size and number of documents in the system
		psInsert.setInt(i++, documentClass.getMaximumFileSize());
		psInsert.setInt(i++,documentClass.getDocumentLimit());
		if(documentClass.isVisible()){
			psInsert.setString(i++,"Y");
		}else{
			psInsert.setString(i++,"N");
		}	
		psInsert.setString(i++,documentClass.getCreatedBy());
		//23-Oct-2012 New field added for controlling the expiry period in number of days
		psInsert.setInt(i++, documentClass.getExpiryPeriod());
		//08-May-2014 New field added for expiry notification 
		psInsert.setInt(i++,documentClass.getExpiryNotificationPeriod());

		int recCount = psInsert.executeUpdate();		
		psInsert.close();		
		connection.commit();
		connection.close();

		short classId = (short) DBStringHelper.getGeneratedKey("CLASSID", "DOCUMENTCLASSES");
		documentClass.setClassId(classId);

		if(recCount == 0){
			kLogger.warning("Unable to add Document Class :" + documentClass.getClassName().toUpperCase());
			throw new Exception("Unable to add Document Class :" + documentClass.getClassName().toUpperCase());
		}	

		if(documentClass.getIndexDefinitions().size() > 0){
			int indexId = addIndexDefinitions(documentClass.getIndexDefinitions());		
			documentClass.setIndexId(indexId);			
		}

		TableManager tableManager = new TableManager();
		try{
			String indexTableName = tableManager.createIndexTable(documentClass);			
			String dataTableName = tableManager.createDataTable(documentClass.getClassId());			
			documentClass.setIndexTableName(indexTableName);			
			documentClass.setDataTableName(dataTableName);
		}catch(Exception e){
			throw new Exception("Unable to create data / index table for  :" + documentClass.getClassName().toUpperCase() ,e);
		}		

		updateDocumentClass(documentClass); // Update table names in database	
		
	}

	public void updateDocumentClass(DocumentClass documentClass)throws Exception{		
		kLogger.fine("Updating Document Class : " + documentClass.getClassName().toUpperCase());		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_DOCUMENTCLASS);		
		int i=1;
		psUpdate.setString(i++,documentClass.getClassName().toUpperCase());
		psUpdate.setString(i++,documentClass.getClassDescription());
		psUpdate.setInt(i++,documentClass.getIndexId());
		psUpdate.setString(i++,documentClass.getIndexTableName());
		psUpdate.setInt(i++,documentClass.getIndexCount());
		psUpdate.setString(i++,documentClass.getDataTableName());		
		if(documentClass.isRevisionControlEnabled()){
			psUpdate.setString(i++,"Y");
		}else{
			psUpdate.setString(i++,"N");
		}	
		//25-Nov-2011 New fields added for controlling the file size and number of documents in the system
		psUpdate.setInt(i++,documentClass.getMaximumFileSize());
		psUpdate.setInt(i++,documentClass.getDocumentLimit());
		if(documentClass.isVisible()){
			psUpdate.setString(i++,"Y");
		}else{
			psUpdate.setString(i++,"N");
		}
		//23-Oct-2012 New field added for controlling the expiry period in number of days
		psUpdate.setInt(i++,documentClass.getExpiryPeriod());
		psUpdate.setInt(i++,documentClass.getExpiryNotificationPeriod());

		psUpdate.setInt(i++,documentClass.getTotalDocuments());//01-Jan-2015 new field added for storing total documents
		psUpdate.setInt(i++,documentClass.getActiveDocuments());//01-Jan-2015 new field added for storing active documents

		psUpdate.setInt(i++,documentClass.getClassId());		
		int recCount = psUpdate.executeUpdate();

		psUpdate.close();
		connection.commit();		
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to update Document Class :" + documentClass.getClassName().toUpperCase());
			throw new Exception("Unable to update Document Class :" + documentClass.getClassName().toUpperCase());
		}		
	}

	public void deleteDocumentClass(DocumentClass documentClass) throws Exception{
		kLogger.fine("Deleting document class : name=" + documentClass.getClassName());		
		ArrayList<Document> documentList = DocumentDAO.getInstance().readDocuments("SELECT * FROM DOCUMENTS WHERE CLASSID ="+documentClass.getClassId());

		for(Document document : documentList){		
			RevisionManager revisionManager=new RevisionManager();
			revisionManager.deleteRevisionRecord(document.getDocumentId());

			CheckedOutDocument checkedOutDocument = CheckedOutDocumentDAO.getInstance().readCheckedOutDocument(document.getDocumentId(), document.getRevisionId());
			if(checkedOutDocument.getDocumentId()!=0){
				CheckedOutDocumentDAO.getInstance().deleteCheckedOutDocument(checkedOutDocument);
			}			
			DocumentNoteDAO.getInstance().deleteJournalNoteByDocumentId(document.getDocumentId());
			DocumentDAO.getInstance().deleteDocument(document);
		}			

		deleteIndexDefinitions(documentClass.getIndexId());	

		TableManager tableManager = new TableManager();
		tableManager.dropDataTable(documentClass.getClassId());
		tableManager.dropIndexTable(documentClass.getClassId());		

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_DOCUMENTCLASS);		
		psDelete.setInt(1,documentClass.getClassId());		
		int recCount = psDelete.executeUpdate();
		psDelete.close();
		connection.commit();		
		connection.close();	

		if(recCount == 0){
			kLogger.warning("Unable to delete document class : name=" + documentClass.getClassName());
			throw (new Exception("Unable to delete document class : name=" + documentClass.getClassName()));
		}	
	}

	public boolean validateDocumentClass(final String className)throws Exception{
		boolean result=false;		
		kLogger.fine("Validating document class : name=" + className);		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_DOCUMENTCLASSBYNAME);		
		psSelect.setString(1,className.toUpperCase());		
		ResultSet rs = psSelect.executeQuery();		
		result = rs.next();		
		rs.close();
		psSelect.close();
		connection.close();		
		return result;
	}

	//read during insertion of new retention policy in krystalretentionpolicy.java 
	public DocumentClass readDocumentClassById(final int classId) throws Exception{		
		DocumentClass documentClass = null;		
		kLogger.fine("Reading document class : id=" + classId);		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_DOCUMENTCLASSBYID);		
		psSelect.setInt(1,classId);		
		ResultSet rs = psSelect.executeQuery();		
		if (rs.next()) {
			documentClass = new DocumentClass();
			documentClass.setClassId(rs.getShort("CLASSID"));
			documentClass.setClassName(rs.getString("CLASSNAME"));
			documentClass.setClassDescription(rs.getString("CLASSDESC"));
			documentClass.setIndexId(rs.getInt("INDEXID"));
			documentClass.setIndexTableName(rs.getString("INDEXTABLENAME"));
			documentClass.setIndexCount(rs.getInt("INDEXCOUNT"));
			documentClass.setDataTableName(rs.getString("DATATABLENAME"));			
			if("Y".equalsIgnoreCase(rs.getString("REVISIONCTL"))){
				documentClass.setRevisionControlEnabled(true);
			}else{
				documentClass.setRevisionControlEnabled(false);
			}
			//25-Nov-2011 New fields added for controlling the file size and number of documents in the system
			documentClass.setMaximumFileSize(rs.getInt("MAXFILESIZE"));
			documentClass.setDocumentLimit(rs.getInt("DOCUMENTLIMIT"));

			if("Y".equalsIgnoreCase(rs.getString("ACTIVE"))){
				documentClass.setVisible(true);
			}else{
				documentClass.setVisible(false);
			}		

			//01-Apr-2012 New fields added for displaying user and date on which it was created
			documentClass.setCreatedBy(rs.getString("CREATEDBY"));
			documentClass.setCreated(rs.getString("CREATED"));

			//23-Oct-2012 New field added for controlling the expiry period in number of days
			documentClass.setExpiryPeriod(rs.getInt("EXPIRYPERIOD"));
			documentClass.setExpiryNotificationPeriod(rs.getInt("EXPIRYNOTIFICATIONPERIOD"));
			
			documentClass.setTotalDocuments(rs.getInt("TOTALDOCUMENTS"));
			documentClass.setActiveDocuments(rs.getInt("ACTIVEDOCUMENTS"));

			documentClass.setIndexDefinitions(readIndexDefinitions(documentClass.getIndexId()));				
		}
		rs.close();
		psSelect.close();
		connection.close();	

		return documentClass;
	}

	public DocumentClass readDocumentClassByName(final String className) throws Exception{		
		DocumentClass documentClass = null;		
		kLogger.fine("Reading document class : name=" + className);		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_DOCUMENTCLASSBYNAME);		
		psSelect.setString(1,className.toUpperCase());		
		ResultSet rs = psSelect.executeQuery();		
		if (rs.next()) {
			documentClass = new DocumentClass();
			documentClass.setClassId(rs.getShort("CLASSID"));
			documentClass.setClassName(rs.getString("CLASSNAME"));
			documentClass.setClassDescription(rs.getString("CLASSDESC"));
			documentClass.setIndexId(rs.getInt("INDEXID"));
			documentClass.setIndexTableName(rs.getString("INDEXTABLENAME"));
			documentClass.setIndexCount(rs.getInt("INDEXCOUNT"));
			documentClass.setDataTableName(rs.getString("DATATABLENAME"));
			if("Y".equalsIgnoreCase(rs.getString("REVISIONCTL"))){
				documentClass.setRevisionControlEnabled(true);
			}else{
				documentClass.setRevisionControlEnabled(false);
			}
			//25-Nov-2011 New fields added for controlling the file size and number of documents in the system
			documentClass.setMaximumFileSize(rs.getInt("MAXFILESIZE"));
			documentClass.setDocumentLimit(rs.getInt("DOCUMENTLIMIT"));

			if("Y".equalsIgnoreCase(rs.getString("ACTIVE"))){
				documentClass.setVisible(true);
			}else{
				documentClass.setVisible(false);
			}
			//01-Apr-2012 New fields added for displaying user and date on which it was created
			documentClass.setCreatedBy(rs.getString("CREATEDBY"));
			documentClass.setCreated(rs.getString("CREATED"));

			//23-Oct-2012 New field added for controlling the expiry period in number of days
			documentClass.setExpiryPeriod(rs.getInt("EXPIRYPERIOD"));
			documentClass.setExpiryNotificationPeriod(rs.getInt("EXPIRYNOTIFICATIONPERIOD"));

			documentClass.setTotalDocuments(rs.getInt("TOTALDOCUMENTS"));
			documentClass.setActiveDocuments(rs.getInt("ACTIVEDOCUMENTS"));

			documentClass.setIndexDefinitions(readIndexDefinitions(documentClass.getIndexId()));
		}
		rs.close();
		psSelect.close();
		connection.close();		

		return documentClass;
	}

	public ArrayList<DocumentClass> readDocumentClasses(final String criteria){
		ArrayList<DocumentClass> result = new ArrayList<DocumentClass>();		
		kLogger.fine("Reading document classes for criteria :" + criteria);
		try{
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append(SQL_SELECT_DOCUMENTCLASSES);
			if(criteria.length()>1){
				sbSelect.append(" AND " + criteria);
			}
			sbSelect.append(" ORDER BY CLASSNAME  ");
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());			
			ResultSet rs = psSelect.executeQuery();			
			while (rs.next()) {
				DocumentClass documentClass  = new DocumentClass();
				documentClass.setClassId(rs.getShort("CLASSID"));
				documentClass.setClassName(rs.getString("CLASSNAME"));
				documentClass.setClassDescription(rs.getString("CLASSDESC"));
				documentClass.setIndexId(rs.getInt("INDEXID"));
				documentClass.setIndexTableName(rs.getString("INDEXTABLENAME"));
				documentClass.setIndexCount(rs.getInt("INDEXCOUNT"));
				documentClass.setDataTableName(rs.getString("DATATABLENAME"));
				if("Y".equalsIgnoreCase(rs.getString("REVISIONCTL"))){
					documentClass.setRevisionControlEnabled(true);
				}else{
					documentClass.setRevisionControlEnabled(false);
				}
				//25-Nov-2011 New fields added for controlling the file size and number of documents in the system
				documentClass.setMaximumFileSize(rs.getInt("MAXFILESIZE"));
				documentClass.setDocumentLimit(rs.getInt("DOCUMENTLIMIT"));
				if("Y".equalsIgnoreCase(rs.getString("ACTIVE"))){
					documentClass.setVisible(true);
				}else{
					documentClass.setVisible(false);
				}	
				//01-Apr-2012 New fields added for displaying user and date on which it was created
				documentClass.setCreatedBy(rs.getString("CREATEDBY"));
				documentClass.setCreated(rs.getString("CREATED"));

				//23-Oct-2012 New field added for controlling the expiry period in number of days
				documentClass.setExpiryPeriod(rs.getInt("EXPIRYPERIOD"));
				documentClass.setExpiryNotificationPeriod(rs.getInt("EXPIRYNOTIFICATIONPERIOD"));
				
				documentClass.setTotalDocuments(rs.getInt("TOTALDOCUMENTS"));
				documentClass.setActiveDocuments(rs.getInt("ACTIVEDOCUMENTS"));

				documentClass.setIndexDefinitions(readIndexDefinitions(documentClass.getIndexId()));				

				result.add(documentClass);
			}
			rs.close();			
			psSelect.close();			
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
			kLogger.warning("Unable to read document class  for criteria :" + criteria);
		}
		return result;
	}

	/**
	 * These methods work with detailed record for Index Definitions
	 */

	public ArrayList<IndexDefinition> readIndexDefinitions(final int indexId){
		ArrayList<IndexDefinition> result  = new ArrayList<IndexDefinition>();		
		kLogger.fine("Reading indexDefinitions for indexId:" + indexId);		
		try{
			result = IndexDefinitionDAO.getInstance().readIndexDefinitionsById(indexId);			
		}catch(Exception e){
			kLogger.warning("Unable to read indexDefinitions for indexId:" + indexId);
		}
		return result;
	}

	private int addIndexDefinitions(ArrayList<IndexDefinition> indexDefinitions) throws Exception {
		kLogger.fine("adding indexDefinitions ");
		int indexId= IndexDefinitionDAO.getInstance().getNextIndexId();
		try{
			for(IndexDefinition indexDefinition : indexDefinitions){
				indexDefinition.setIndexId(indexId);
				IndexDefinitionDAO.getInstance().addIndexDefinition(indexDefinition);
			}
		}catch(Exception e){
			kLogger.warning("Unable to add indexDefinitions");
			e.printStackTrace();
		}		
		return indexId;
	}

	private void deleteIndexDefinitions(int indexId) throws Exception{
		kLogger.fine("Deleting indexDefinitions for indexId:" + indexId);
		try{
			IndexDefinitionDAO.getInstance().deleteIndexDefinitionsById(indexId);
		}catch(Exception e){
			kLogger.warning("Unable to read indexeDefinitions for indexId:" + indexId);
			e.printStackTrace();
		}
	}

	
	/**
	 * These new methods added on 01-Jan-2015 by Rahul Kubadia is used to adjust the document counts in document classes table.
	 * Now we do not have to query each documents table to get the count of active document everytime as it will be stored in the one of the columns of the 
	 * document classes table.
	 * */ 
	
	/**
	 * This method will adjust the total document and active document count for a give document class. This will be run automatically on startup of the system.
	 * @param documentClass
	 * @throws Exception
	 */
	public void adjustDocumentCounts(DocumentClass documentClass) throws Exception {
		Connection connection = ConnectionPoolManager.getInstance().getConnection();	
		
		int activeDocuments = DocumentDAO.getInstance().countActiveDocuments(documentClass.getClassId());
		int totalDocuments  = DocumentDAO.getInstance().countTotalDocuments(documentClass.getClassId());
		
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE DOCUMENTCLASSES SET TOTALDOCUMENTS=?, ACTIVEDOCUMENTS=? WHERE CLASSID=?");
		psUpdate.setInt(1, totalDocuments);
		psUpdate.setInt(2, activeDocuments);
		psUpdate.setInt(3,documentClass.getClassId());
		
		psUpdate.executeUpdate();
		connection.commit();
		
		psUpdate.close();
		connection.close();
	}
	
	public void increaseDocumentCount(DocumentClass documentClass) throws Exception{
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE DOCUMENTCLASSES SET TOTALDOCUMENTS = (TOTALDOCUMENTS+1) WHERE CLASSID=? ");
		psUpdate.setInt(1, documentClass.getClassId());
		psUpdate.executeUpdate();
		connection.commit();

		psUpdate.close();
		connection.close();
	}
	
	public void decreaseDocumentCount(DocumentClass documentClass) throws Exception{
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE DOCUMENTCLASSES SET TOTALDOCUMENTS = (TOTALDOCUMENTS-1)  WHERE CLASSID=? ");
		psUpdate.setInt(1, documentClass.getClassId());
		
		psUpdate.executeUpdate();
		connection.commit();

		psUpdate.close();
		connection.close();
	}

	public void increaseActiveDocumentCount(DocumentClass documentClass) throws Exception{
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE DOCUMENTCLASSES SET ACTIVEDOCUMENTS=(ACTIVEDOCUMENTS+1) WHERE CLASSID=? ");
		psUpdate.setInt(1, documentClass.getClassId());
		
		psUpdate.executeUpdate();
		connection.commit();

		psUpdate.close();
		connection.close();
	}
	
	public void decreaseActiveDocumentCount(DocumentClass documentClass) throws Exception{
		Connection connection = ConnectionPoolManager.getInstance().getConnection();		
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE DOCUMENTCLASSES SET ACTIVEDOCUMENTS=(ACTIVEDOCUMENTS-1) WHERE CLASSID=? ");
		psUpdate.setInt(1, documentClass.getClassId());
		
		psUpdate.executeUpdate();
		connection.commit();

		psUpdate.close();
		connection.close();
	}

	

}

