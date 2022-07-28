/**
 * Created on Sep 20, 2005
 *
 * Copyright 2005 by Arysys Technologies (P) Ltd.,
 * #3,Shop line,
 * Sasmira Marg,
 * Worli,Mumbai 400 025
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
import com.primeleaf.krystal.model.vo.Bookmark;

/**
 * @author Rahul Kubadia
 * @since 2.0
 * @comments Database related activities on Bookmark object runs through this class
 */
public class BookmarkDAO {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	
	private String SQL_INSERT_BOOKMARK="INSERT INTO BOOKMARKS (NAME, DOCUMENTID, REVISIONID, USERNAME )VALUES(?,?,?,?)";
	
	private String SQL_UPDATE_BOOKMARKBYID="UPDATE BOOKMARKS SET NAME=?, DOCUMENTID = ?, REVISIONID =?, USERNAME=? WHERE ID=?";
	
	private String SQL_DELETE_BOOKMARKBYID="DELETE FROM BOOKMARKS WHERE ID=?";
	
	private String SQL_SELECT_BOOKMARKBYID="SELECT * FROM BOOKMARKS  WHERE ID =?";
	
	private String SQL_SELECT_BOOKMARKBYUSER="SELECT * FROM BOOKMARKS WHERE USERNAME=? ORDER BY ID DESC";

	private static BookmarkDAO _instance;
	
	/**
	 * Default constructor
	 */
	private BookmarkDAO(){
		
	}
	
	public static synchronized BookmarkDAO getInstance() { 
		if (_instance==null) { 
			_instance = new BookmarkDAO(); 
		} 
		return _instance; 
	} 
	
	public void addBookmark(Bookmark bookmark)throws Exception{
		
		kLogger.fine("Adding bookmark :" + bookmark.getBookmarkName());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_BOOKMARK);
		
		int i=1;
		
		psInsert.setString(i++,bookmark.getBookmarkName());
		psInsert.setInt(i++,bookmark.getDocumentId());
		psInsert.setString(i++,bookmark.getRevisionId());
		psInsert.setString(i++,bookmark.getUserName());
		
		int recCount = psInsert.executeUpdate();

		psInsert.close();
		
		connection.commit();
		
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to add bookmark :" + bookmark.getBookmarkName());
			throw new Exception("Unable to add bookmark :" + bookmark.getBookmarkName());
		}
	}

	public Bookmark readBookmarkById(final int id) throws Exception {
		Bookmark result = null;
		
		kLogger.fine("Reading bookmark : id=" + id);
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_BOOKMARKBYID);
		
		psSelect.setInt(1,id);
		
		ResultSet rs = psSelect.executeQuery();
		
		if (rs.next()) {
			result = new Bookmark();
			result.setBookmarkId(rs.getInt("ID"));
			result.setBookmarkName(rs.getString("NAME"));
			result.setDocumentId(rs.getInt("DOCUMENTID"));
			result.setRevisionId(rs.getString("REVISIONID"));
			result.setUserName(rs.getString("USERNAME"));
			result.setCreated(rs.getString("CREATED"));
		}
		rs.close();
		psSelect.close();
		connection.close();
		return result;
	}	
	
	public void updateBookmark(Bookmark bookmark)throws Exception{
		kLogger.fine("Updating bookmark :" +bookmark.getBookmarkName());
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_BOOKMARKBYID);

		int i=1;
		
		psUpdate.setString(i++,bookmark.getBookmarkName());
		psUpdate.setInt(i++,bookmark.getDocumentId());
		psUpdate.setString(i++,bookmark.getRevisionId());
		psUpdate.setString(i++,bookmark.getUserName());
		psUpdate.setInt(i++,bookmark.getBookmarkId());
		
		int recCount = psUpdate.executeUpdate();
		psUpdate.close();
		connection.commit();
		connection.close();
		if(recCount == 0){
			kLogger.warning("Unable to update bookmark : id=" + bookmark.getBookmarkId());
			throw (new Exception("Unable to update bookmark : id=" + bookmark.getBookmarkId()));
		}
	}
	
	public void deleteBookmark(final int id)throws Exception{
		kLogger.fine("Deleting bookmark : id=" + id);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_BOOKMARKBYID);
		
		psDelete.setInt(1,id);
		
		int recCount = psDelete.executeUpdate();
	
		psDelete.close();
		
		connection.commit();
		connection.close();
		
		if(recCount == 0){
			kLogger.warning("Unable to delete bookmark : id=" + id);
			throw (new Exception("Unable to delete bookmark : id=" + id));
		}
	}

	public ArrayList<Bookmark> readBookmarkByUser(final String userName){
		ArrayList<Bookmark> result = new ArrayList<Bookmark>();
		kLogger.fine("Reading bookmarks for user : id =  " + userName);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_BOOKMARKBYUSER);
			psSelect.setString(1,userName.trim());
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				Bookmark bookmark = new Bookmark();
				bookmark.setBookmarkId(rs.getInt("ID"));
				bookmark.setBookmarkName(rs.getString("NAME"));
				bookmark.setDocumentId(rs.getInt("DOCUMENTID"));
				bookmark.setRevisionId(rs.getString("REVISIONID"));
				bookmark.setUserName(rs.getString("USERNAME"));
				bookmark.setCreated(rs.getString("CREATED"));
				result.add(bookmark);
			} 
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to read bookmarks for user : id = " + userName);
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Bookmark> readBookmarks(final String criteria){
		ArrayList<Bookmark> result = new ArrayList<Bookmark>();
		
		kLogger.fine("Reading bookmarks for criteria :" + criteria);
		
		try{
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append("SELECT * FROM BOOKMARKS");
			
			if(criteria.length()>1){
				sbSelect.append(" WHERE " + criteria);
			}
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			
			ResultSet rs = psSelect.executeQuery();
			
			while (rs.next()) {
				Bookmark bookmark = new Bookmark();
				bookmark.setBookmarkId(rs.getInt("ID"));
				bookmark.setBookmarkName(rs.getString("NAME"));
				bookmark.setDocumentId(rs.getInt("DOCUMENTID"));
				bookmark.setRevisionId(rs.getString("REVISIONID"));
				bookmark.setUserName(rs.getString("USERNAME"));
				bookmark.setCreated(rs.getString("CREATED"));
				result.add(bookmark);
			}
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to read bookmarks for criteria :" + criteria);
		}
		return result;
	}

}
