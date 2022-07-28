/**
 * Created on Sep 24, 2005 
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.DocumentNote;
import com.primeleaf.krystal.model.vo.User;

/**
 * @author Rahul Kubadia
 * @since 2.0
 * @comments User prefrences are stored / retrieved and removed from database
 */
public class DocumentNoteDAO {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	private String SQL_INSERT_JOURNALNOTE="INSERT INTO NOTES(NOTEDATA,USERNAME,DOCUMENTID,NOTETYPE) VALUES (?,?,?,?)";

	private String SQL_UPDATE_JOURNALNOTE_TO_INACTIVATE="UPDATE NOTES SET ACTIVE='N' WHERE ID=?";
	private String SQL_UPDATE_JOURNALNOTE="UPDATE NOTES SET NOTEDATA=?,USERNAME=?,CREATED=?,ACTIVE=?,DOCUMENTID=?,NOTETYPE=?  WHERE ID=?";

	private String SQL_DELETE_JOURNALNOTE="DELETE FROM NOTES WHERE ID=?";

	private String SQL_DELETE_JOURNALNOTE_BY_DOCUMENTID="DELETE FROM NOTES WHERE DOCUMENTID=?";

	private String SQL_SELECT_JOURNALNOTEBYID="SELECT ID,NOTEDATA,USERNAME,CREATED,ACTIVE,DOCUMENTID,NOTETYPE FROM NOTES WHERE ID=?";
	
	private String SQL_SELECT_JOURNALNOTES="SELECT ID,NOTEDATA,USERNAME,CREATED,ACTIVE,DOCUMENTID,NOTETYPE FROM NOTES";

	private static DocumentNoteDAO _instance;

	/**
	 * Default constructor
	 */

	private DocumentNoteDAO() {
		super();
	}

	public static synchronized DocumentNoteDAO getInstance() { 
		if (_instance==null) { 
			_instance = new DocumentNoteDAO(); 
		} 
		return _instance; 
	} 

	public void addJournalNote(DocumentNote documentNote)throws Exception{

		kLogger.fine("Adding journal note for document: id=" + documentNote.getDocumentId());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_JOURNALNOTE);

		int i=1;

		psInsert.setString(i++,documentNote.getNoteData());
		psInsert.setString(i++,documentNote.getUserName().toUpperCase());
		psInsert.setInt(i++,documentNote.getDocumentId());
		psInsert.setString(i++,documentNote.getNoteType());

		int recCount = psInsert.executeUpdate();

		psInsert.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to add journal note for document: id=" + documentNote.getDocumentId());
			throw new Exception("Unable to add journal note for document: id=" + documentNote.getDocumentId());
		}
	}

	public DocumentNote readByNoteId(final int noteId) throws Exception {

		DocumentNote documentNote = null;

		kLogger.fine("Reading journal note :id=" + noteId);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_JOURNALNOTEBYID);

		psSelect.setInt(1,noteId);

		ResultSet rs = psSelect.executeQuery();

		if (rs.next()) {
			documentNote = new DocumentNote();
			documentNote.setNoteId(rs.getInt("ID"));
			documentNote.setNoteData(rs.getString("NOTEDATA"));
			documentNote.setUserName(rs.getString("USERNAME"));
			documentNote.setCreated(rs.getString("CREATED"));
			if("Y".equalsIgnoreCase(rs.getString("ACTIVE"))){
				documentNote.setActive(true);
			}else{
				documentNote.setActive(false);
			}
			documentNote.setDocumentId(rs.getInt("DOCUMENTID"));
			documentNote.setNoteType(rs.getString("NOTETYPE"));
		}
		rs.close();
		psSelect.close();
		connection.close();

		return documentNote;
	}

	public ArrayList<DocumentNote> readJournalNotes(final String criteria) {
		ArrayList<DocumentNote> documentNotes = new ArrayList<DocumentNote>();
		kLogger.fine("Reading journal note for criteria=" + criteria);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append(SQL_SELECT_JOURNALNOTES);
			if(criteria.length()>1){
				sbSelect.append(" WHERE " + criteria);
			}
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				DocumentNote documentNote = new DocumentNote();
				documentNote.setNoteId(rs.getInt("ID"));
				documentNote.setNoteData(rs.getString("NOTEDATA"));
				documentNote.setUserName(rs.getString("USERNAME"));
				documentNote.setCreated(rs.getString("CREATED"));
				if("Y".equalsIgnoreCase(rs.getString("ACTIVE"))){
					documentNote.setActive(true);
				}else{
					documentNote.setActive(false);
				}
				documentNote.setDocumentId(rs.getInt("DOCUMENTID"));
				documentNote.setNoteType(rs.getString("NOTETYPE"));

				documentNotes.add(documentNote);
			}
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
			kLogger.warning("Unable to read journal note criteria:" + criteria);	
		}
		return documentNotes;
	}

	public void updateJournalNote(DocumentNote documentNote)throws Exception{

		kLogger.fine("Updating journal notes id=" + documentNote.getNoteId());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_JOURNALNOTE);

		int i=1;

		psUpdate.setString(i++,documentNote.getNoteData());
		psUpdate.setString(i++,documentNote.getUserName().toUpperCase());
		psUpdate.setString(i++,documentNote.getCreated());
		if(documentNote.isActive()){
			psUpdate.setString(i++,"Y");
		}else{
			psUpdate.setString(i++,"N");
		}
		psUpdate.setInt(i++,documentNote.getDocumentId());
		psUpdate.setString(i++,documentNote.getNoteType());

		psUpdate.setInt(i++,documentNote.getNoteId());

		int recCount = psUpdate.executeUpdate();

		psUpdate.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to update journal notes id=" + documentNote.getNoteId());
			throw new Exception("Unable to update journal notes id=" + documentNote.getNoteId());
		}
	}

	public void inActivateJournalNote(final int noteId)throws Exception{

		kLogger.fine("Inactivating journal notes id=" + noteId);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_JOURNALNOTE_TO_INACTIVATE);

		psUpdate.setInt(1,noteId);

		int recCount = psUpdate.executeUpdate();

		psUpdate.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to inactivate journal notes id=" + noteId);
			throw new Exception("Unable to inactivate journal notes id=" + noteId);
		}
	}

	public ArrayList<DocumentNote> searchDocumentNotes(final String searchTerm, final ArrayList<User> users, final ArrayList<DocumentClass> documentClasses){
		ArrayList<DocumentNote> result = new ArrayList<DocumentNote>();

		kLogger.fine("Searching journal notes for search term =" + searchTerm);

		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			String likeClause = "";
			StringTokenizer strTok = new StringTokenizer(searchTerm, " ");
			while (strTok.hasMoreTokens()) {
				String value = strTok.nextElement().toString().toUpperCase();
				value = value.replaceAll("'","''");
				likeClause += "UPPER(NOTEDATA) LIKE '%"	+ value	+ "%' OR ";
			}
			likeClause += " UPPER(NOTEDATA) = 'XXXXXXXXX'";

			String documentClassCriteria= "0";
			for(DocumentClass documentClass : documentClasses){
				documentClassCriteria += "," +documentClass.getClassId();
			}

			String userNameCriteria="'PUBLIC'";
			for(User user : users){
				userNameCriteria += ",'" +user.getUserName() + "'";
			}

			String query = "SELECT ID, NOTEDATA, NOTES.USERNAME, NOTES.CREATED,DOCUMENTS.DOCUMENTID,DOCUMENTS.CLASSID,NOTETYPE FROM "
					+ "NOTES,DOCUMENTCLASSES,DOCUMENTS WHERE ("
					+ likeClause
					+ ") AND "
					+ "DOCUMENTCLASSES.CLASSID=DOCUMENTS.CLASSID "
					+ "AND DOCUMENTS.DOCUMENTID=NOTES.DOCUMENTID AND DOCUMENTCLASSES.CLASSID IN"
					+ "("
					+ documentClassCriteria
					+ ") AND NOTES.USERNAME IN ("
					+ userNameCriteria
					+ ") AND NOTES.ACTIVE='Y' ORDER BY DOCUMENTCLASSES.CLASSDESC,DOCUMENTS.DOCUMENTID";

			Statement statement = connection.createStatement();

			ResultSet rs  = statement.executeQuery(query);

			while(rs.next()){
				DocumentNote jNote = new DocumentNote();
				jNote.setNoteId(rs.getInt("ID"));
				jNote.setNoteData(rs.getString("NOTEDATA"));
				jNote.setUserName(rs.getString("USERNAME"));
				jNote.setCreated(rs.getString("CREATED"));
				jNote.setDocumentId(rs.getInt("DOCUMENTID"));
				jNote.setActive(true);
				jNote.setClassId(rs.getInt("CLASSID"));
				jNote.setNoteType(rs.getString("NOTETYPE"));

				result.add(jNote);
			}
			rs.close();
			statement.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to search journal notes for term : " + searchTerm);
			e.printStackTrace();
		}
		return result; 
	}

	public void deleteJournalNote(final int noteId)throws Exception{

		kLogger.fine("Deleting journal notes id=" + noteId);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_JOURNALNOTE);

		psDelete.setInt(1,noteId);

		int recCount = psDelete.executeUpdate();

		psDelete.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to delete journal notes id=" + noteId);

		}
	}

	public void deleteJournalNoteByDocumentId(final int documnetId)throws Exception{

		kLogger.fine("Deleting journal notes for document id=" + documnetId);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_JOURNALNOTE_BY_DOCUMENTID);
		psDelete.setInt(1,documnetId);

		psDelete.executeUpdate();

		psDelete.close();
		connection.commit();
		connection.close();	
	}

}
