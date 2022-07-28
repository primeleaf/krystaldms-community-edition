/**
 * Created on Sep 20, 2005
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
package com.primeleaf.krystal.model.vo;

/**
 * @author Rahul Kubadia
 * @since 2.0
 * @comments Bookmark value object
 */
public class Bookmark {

	private int bookmarkId;
	private String bookmarkName;
	private int documentId;
	private String revisionId;
	private String userName;
	private String created;
	
	private DocumentClass documentClass = null;
	
	/**
	 * @return the documentClass
	 */
	public DocumentClass getDocumentClass() {
		return documentClass;
	}
	/**
	 * @param documentClass the documentClass to set
	 */
	public void setDocumentClass(DocumentClass documentClass) {
		this.documentClass = documentClass;
	}
	public int getBookmarkId() {
		return bookmarkId;
	}
	public void setBookmarkId(int bookmarkId) {
		this.bookmarkId = bookmarkId;
	}
	public String getBookmarkName() {
		return bookmarkName;
	}
	public void setBookmarkName(String bookmarkName) {
		this.bookmarkName = bookmarkName;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public String getRevisionId() {
		return revisionId;
	}
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
