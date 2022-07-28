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
package com.primeleaf.krystal.model.vo;


/**Value object for database table CHECKOUT
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.model.RevisionManager
 */

public class CheckedOutDocument {

	private int documentId;
	private String revisionId;
	private String userName;
	private String checkOutPath;
	private String checkOutDate;
	private String comments;
	private int classId; //Version 7.1 - Added by Rahul Kubadia on 19th April 2013
	
	private DocumentClass documentClass = null;
	/**
	 * Default constructor 
	 */
	public CheckedOutDocument() {
		super();
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCheckOutPath() {
		return checkOutPath;
	}

	public void setCheckOutPath(String checkOutPath) {
		this.checkOutPath = checkOutPath;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	/**
	 * @return the classId
	 */
	public int getClassId() {
		return classId;
	}

	/**
	 * @param classId the classId to set
	 */
	public void setClassId(int classId) {
		this.classId = classId;
	}

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
	

}
