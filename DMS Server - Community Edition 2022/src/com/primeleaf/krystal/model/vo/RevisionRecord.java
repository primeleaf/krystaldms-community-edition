/**
 * Created on Apr 25, 2005
 *
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
 * 
 */
package com.primeleaf.krystal.model.vo;

/**
 * Revision histroy value object 
 * @author Rahul Kubadia
 * @since 2.0
 * 
 */
public class RevisionRecord {
	private String userName;
	private String userAction;
	private int documentId;
	private String revisionId;
	private String comments;
	private String dateTime;
	
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return Returns the documentId.
	 */
	public int getDocumentId() {
		return documentId;
	}
	/**
	 * @param documentId The documentId to set.
	 */
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	/**
	 * @return Returns the revnId.
	 */
	public String getRevisionId() {
		return revisionId;
	}
	/**
	 * @param revnId The revnId to set.
	 */
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}
	/**
	 * @return Returns the userAction.
	 */
	public String getUserAction() {
		return userAction;
	}
	/**
	 * @param userAction The userAction to set.
	 */
	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return Returns the dateTime.
	 */
	public String getDateTime() {
		return dateTime;
	}
	/**
	 * @param dateTime The dateTime to set.
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
