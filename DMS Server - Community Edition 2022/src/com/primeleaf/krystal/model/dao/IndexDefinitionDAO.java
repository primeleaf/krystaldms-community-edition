/**
 * Created on Oct 1, 2005 
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
import com.primeleaf.krystal.model.TableManager;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.IndexDefinition;

/**
 * Index definitions are stored and retrived from database using this class
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.dms.data.DocumentClassDAO
 */
public class IndexDefinitionDAO {
	
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	private final String SQL_INSERT_INDEXDEFINITION="INSERT INTO INDEXES ( INDEXID, SEQ, IDXCOLNAME, IDXDISPLAYNAME, IDXMAXLEN, IDXTYPE , DEFAULTFILTER, DEFAULTVALUE, MANDATORY) VALUES (?,?,?,?,?,?,?,?,?)";
	private final String SQL_SELECT_MAXID="SELECT MAX(INDEXID) FROM INDEXES";
	private final String SQL_SELECT_INDEXESBYID="SELECT INDEXID, SEQ, IDXCOLNAME, IDXDISPLAYNAME, IDXMAXLEN, IDXTYPE , DEFAULTFILTER, DEFAULTVALUE, MANDATORY FROM INDEXES WHERE INDEXID=? ORDER BY SEQ";
	private final String SQL_SELECT_INDEX="SELECT INDEXID, SEQ, IDXCOLNAME, IDXDISPLAYNAME, IDXMAXLEN, IDXTYPE , DEFAULTFILTER, DEFAULTVALUE, MANDATORY FROM INDEXES WHERE INDEXID=? AND IDXCOLNAME=? ORDER BY SEQ";
	private final String SQL_SELECT_INDEXBYSEQ="SELECT INDEXID, SEQ, IDXCOLNAME, IDXDISPLAYNAME, IDXMAXLEN, IDXTYPE , DEFAULTFILTER, DEFAULTVALUE, MANDATORY FROM INDEXES WHERE INDEXID=? AND SEQ=? ORDER BY SEQ";
	private final String SQL_UPDATE_INDEXDEFINITION="UPDATE INDEXES SET SEQ=?, IDXCOLNAME=?, IDXDISPLAYNAME=?, IDXMAXLEN=?, IDXTYPE=?, DEFAULTFILTER=?, DEFAULTVALUE =? ,  MANDATORY=? WHERE INDEXID=? AND IDXCOLNAME=?  ";
	private final String SQL_DELETE_INDEXESBYID="DELETE FROM INDEXES WHERE INDEXID=?";
	private final String SQL_DELETE_INDEXBYID="DELETE FROM INDEXES WHERE INDEXID=? AND IDXCOLNAME=?";
	
	private static IndexDefinitionDAO _instance;
	
	/**
	 * Default constructor
	 */
	private IndexDefinitionDAO() {
		super();
	}
	
	public static synchronized IndexDefinitionDAO getInstance() { 
		if (_instance==null) { 
			_instance = new IndexDefinitionDAO(); 
		} 
		return _instance; 
	} 
	
	public void addIndexDefinition(IndexDefinition indexDefinition) throws Exception{
		
		kLogger.fine("Adding Index Definition : " + indexDefinition.getIndexColumnName().toUpperCase());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_INDEXDEFINITION);
		
		int i=1;
		psInsert.setInt(i++,indexDefinition.getIndexId());
		psInsert.setShort(i++,indexDefinition.getSequence());
		psInsert.setString(i++,indexDefinition.getIndexColumnName().toUpperCase());
		psInsert.setString(i++,indexDefinition.getIndexDisplayName());
		psInsert.setInt(i++,indexDefinition.getIndexMaxLength());
		psInsert.setString(i++,indexDefinition.getIndexType());
		psInsert.setString(i++,indexDefinition.getDefaultFilter());
		psInsert.setString(i++,indexDefinition.getDefaultValue());
		if(indexDefinition.isMandatory()){
		  psInsert.setString(i++,"Y");
		}else{
		  psInsert.setString(i++,"N");	
		}
		int recCount = psInsert.executeUpdate();
		
		psInsert.close();
		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to add Index Definition : " + indexDefinition.getIndexColumnName().toUpperCase());
			throw new Exception("Unable to add Index Definition : " + indexDefinition.getIndexColumnName().toUpperCase());
		}
	}
	/**
	 * This method deletes the index from Indexes Table basis the document class and index passed
	 * @param documentClass
	 * @param indexDefinition
	 * @throws Exception
	 * @author Rahul Kubadia
	 * @since 7.1
	 */
	public void deleteIndexDefinitionById(DocumentClass documentClass,IndexDefinition indexDefinition)throws Exception{
		int indexId = documentClass.getIndexId();
		try{
			kLogger.fine("Deleting index definition :id=" + indexId);
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			
			PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_INDEXBYID);
			psDelete.setInt(1,indexId);
			psDelete.setString(2,indexDefinition.getIndexColumnName());
			psDelete.executeUpdate();
			psDelete.close();
			connection.commit();
			connection.close();
			
			short sequence = indexDefinition.getSequence();
			ArrayList<IndexDefinition> indexDefinitions = readIndexDefinitions("INDEXID="+indexId+" AND SEQ >"+ sequence + " ORDER BY SEQ");
			
			for(IndexDefinition indexDef : indexDefinitions){
				indexDef.setSequence(sequence++);
				updateIndexDefinition(indexDef);
			}
			documentClass.setIndexCount(documentClass.getIndexCount()-1);
			DocumentClassDAO.getInstance().updateDocumentClass(documentClass);
			
			TableManager tableManager = new TableManager();
			tableManager.dropIndexColumn(documentClass, indexDefinition);	
			
			
		}catch(Exception e){
			kLogger.warning("Unable to delete index definition :id=" + indexId);
		}
	}
	public void updateIndexDefinition(IndexDefinition indexDefinition)throws Exception{
		kLogger.fine("Updating Index Definition : " + indexDefinition.getIndexColumnName().toUpperCase());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_INDEXDEFINITION);
		
		int i=1;
		
		psUpdate.setShort(i++,indexDefinition.getSequence());
		psUpdate.setString(i++,indexDefinition.getIndexColumnName());
		psUpdate.setString(i++,indexDefinition.getIndexDisplayName());
		psUpdate.setInt(i++,indexDefinition.getIndexMaxLength());
		psUpdate.setString(i++,indexDefinition.getIndexType());
		psUpdate.setString(i++,indexDefinition.getDefaultFilter());
		psUpdate.setString(i++,indexDefinition.getDefaultValue());
		if(indexDefinition.isMandatory()){
			  psUpdate.setString(i++,"Y");
		}else{
			  psUpdate.setString(i++,"N");	
		}
		psUpdate.setInt(i++,indexDefinition.getIndexId());
		psUpdate.setString(i++,indexDefinition.getIndexColumnName());
		
		int recCount = psUpdate.executeUpdate();
		
		psUpdate.close();
		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to update Index Definition : " + indexDefinition.getIndexColumnName().toUpperCase());
			throw new Exception("Unable to update Index Definition : " + indexDefinition.getIndexColumnName().toUpperCase());
		}
	}
		
	public void deleteIndexDefinitionsById(final int indexId) throws Exception{
		kLogger.fine("Deleting index definitions :id=" + indexId);
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_INDEXESBYID);
		psDelete.setInt(1,indexId);
		psDelete.executeUpdate();
		psDelete.close();
		connection.commit();
		connection.close();
	}
	
	public IndexDefinition readIndexDefinition(final int indexId,final String indexName) throws Exception{
		IndexDefinition indexDefinition = null;
		
		kLogger.fine("Reading index definition:indexId=" + indexId +" ,indexName = " + indexName);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_INDEX);
		psSelect.setInt(1,indexId);
		psSelect.setString(2,indexName);
		
		ResultSet rs = psSelect.executeQuery();

		if(rs.next()){
			indexDefinition = new IndexDefinition();

			indexDefinition.setIndexId(rs.getInt("INDEXID"));
			indexDefinition.setSequence(rs.getByte("SEQ"));
			indexDefinition.setIndexColumnName(rs.getString("IDXCOLNAME"));
			indexDefinition.setIndexDisplayName(rs.getString("IDXDISPLAYNAME"));
			indexDefinition.setIndexMaxLength(rs.getShort("IDXMAXLEN"));
			indexDefinition.setIndexType(rs.getString("IDXTYPE"));
			indexDefinition.setDefaultFilter(rs.getString("DEFAULTFILTER"));
			indexDefinition.setDefaultValue(rs.getString("DEFAULTVALUE"));
			
			if("Y".equalsIgnoreCase(rs.getString("MANDATORY"))){
				indexDefinition.setMandatory(true);
			}else{
				indexDefinition.setMandatory(false);
			}			
			
		}
		rs.close();
		psSelect.close();
		connection.close();
		return indexDefinition;
	}
	
	public IndexDefinition readIndexDefinition(final int indexId,final int sequence) throws Exception{
		IndexDefinition indexDefinition = null;
		
		kLogger.fine("Reading index definition:indexId=" + indexId +" ,sequence = " + sequence);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_INDEXBYSEQ);
		psSelect.setInt(1,indexId);
		psSelect.setInt(2,sequence);
		
		ResultSet rs = psSelect.executeQuery();

		if(rs.next()){
			indexDefinition = new IndexDefinition();

			indexDefinition.setIndexId(rs.getInt("INDEXID"));
			indexDefinition.setSequence(rs.getByte("SEQ"));
			indexDefinition.setIndexColumnName(rs.getString("IDXCOLNAME"));
			indexDefinition.setIndexDisplayName(rs.getString("IDXDISPLAYNAME"));
			indexDefinition.setIndexMaxLength(rs.getShort("IDXMAXLEN"));
			indexDefinition.setIndexType(rs.getString("INDEXTYPE"));
			indexDefinition.setDefaultFilter(rs.getString("DEFAULTFILTER"));
			indexDefinition.setDefaultValue(rs.getString("DEFAULTVALUE"));
			if("Y".equalsIgnoreCase(rs.getString("MANDATORY"))){
				indexDefinition.setMandatory(true);
			}else{
				indexDefinition.setMandatory(false);
			}	
		}
		rs.close();
		psSelect.close();
		connection.close();
		return indexDefinition;
	}

	
	public ArrayList<IndexDefinition> readIndexDefinitionsById(final int indexId){
		
		kLogger.fine("Reading index definitions :id=" + indexId);
		
		ArrayList<IndexDefinition> result = new ArrayList<IndexDefinition>();
		
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			
			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_INDEXESBYID);
			
			psSelect.setInt(1,indexId);
			
			ResultSet rs = psSelect.executeQuery();
			
			while (rs.next()) {
				IndexDefinition indexDefinition = new IndexDefinition();
				indexDefinition.setIndexId(rs.getInt("INDEXID"));
				indexDefinition.setSequence(rs.getByte("SEQ"));
				indexDefinition.setIndexColumnName(rs.getString("IDXCOLNAME"));
				indexDefinition.setIndexDisplayName(rs.getString("IDXDISPLAYNAME"));
				indexDefinition.setIndexMaxLength(rs.getShort("IDXMAXLEN"));
				indexDefinition.setIndexType(rs.getString("IDXTYPE"));
				indexDefinition.setDefaultFilter(rs.getString("DEFAULTFILTER"));
				indexDefinition.setDefaultValue(rs.getString("DEFAULTVALUE"));
				if("Y".equalsIgnoreCase(rs.getString("MANDATORY"))){
					indexDefinition.setMandatory(true);
				}else{
					indexDefinition.setMandatory(false);
				}	
				result.add(indexDefinition);				
			}
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){		
			kLogger.warning("Unable to read index definitions : id = " + indexId);		
		}
		return result;
	}
	
	public ArrayList<IndexDefinition> readIndexDefinitions(final String criteria){
		
		ArrayList<IndexDefinition> result = new ArrayList<IndexDefinition>();
		
		kLogger.fine("Reading index definitions for criteria :" + criteria);
		
		try{
			StringBuffer sbSelect = new StringBuffer();
			
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			
			sbSelect.append("SELECT INDEXID, SEQ, IDXCOLNAME, IDXDISPLAYNAME, IDXMAXLEN, IDXTYPE, DEFAULTFILTER, DEFAULTVALUE, MANDATORY FROM INDEXES");
			
			if(criteria.length()>1){
				sbSelect.append(" WHERE " + criteria);
			}
			
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			
			ResultSet rs = psSelect.executeQuery();
			
			while (rs.next()) {
				IndexDefinition indexDefinition = new IndexDefinition();
				
				indexDefinition.setIndexId(rs.getInt("INDEXID"));
				indexDefinition.setSequence(rs.getByte("SEQ"));
				indexDefinition.setIndexColumnName(rs.getString("IDXCOLNAME"));
				indexDefinition.setIndexDisplayName(rs.getString("IDXDISPLAYNAME"));
				indexDefinition.setIndexMaxLength(rs.getShort("IDXMAXLEN"));
				indexDefinition.setIndexType(rs.getString("IDXTYPE"));
				indexDefinition.setDefaultFilter(rs.getString("DEFAULTFILTER"));
				indexDefinition.setDefaultValue(rs.getString("DEFAULTVALUE"));
				if("Y".equalsIgnoreCase(rs.getString("MANDATORY"))){
					indexDefinition.setMandatory(true);
				}else{
					indexDefinition.setMandatory(false);
				}					
				result.add(indexDefinition);
			} 
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to read index definitions for criteria :" + criteria);
		}
		
		return result;
	}
	public int getNextIndexId(){
		kLogger.fine("Getting next available index id");
		int indexId=0;
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			
			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_MAXID);
			
			ResultSet rs = psSelect.executeQuery();
			
			while (rs.next()) {
				indexId=rs.getInt(1);
			}
			rs.close();
			psSelect.close();
			
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to get next index id");
		}
		return indexId+1;
	}
	
	/**
	 * This method validates if the same index id and index name combination already exist in the database
	 * @author Rahul Kubadia
	 * @since 6.0
	 * @param indexId
	 * @param indexName
	 * @return true if exist else false
	 * @throws Exception
	 */
	
	public boolean validateIndexDefinition(final int indexId,final String indexName) throws Exception{
		boolean result  = false;
		
		kLogger.fine("Validating index definition:indexId=" + indexId +" ,indexName = " + indexName);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_INDEX);
		psSelect.setInt(1,indexId);
		psSelect.setString(2,indexName);
		
		ResultSet rs = psSelect.executeQuery();

		result = rs.next();
		rs.close();
		psSelect.close();
		connection.close();
		return result;
	}
}
