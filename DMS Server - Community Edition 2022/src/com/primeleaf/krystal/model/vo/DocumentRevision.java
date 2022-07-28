/**
 * Created on Oct 6, 2005 
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

import java.io.InputStream;
import java.util.Hashtable;

/**
 * Document contains revisions. These revisions are accessed using this value object
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.model.vo.Document
 */
public class DocumentRevision {

	private int documentId; 
	private String revisionId;
	private int offset;
	private int length;
	private int classId;
	private InputStream documentFile = null;
	private String userName="";
	private Hashtable<String,String> indexRecord = null;
	private String comments = "";
	
	/**
	 * Default constructor 
	 */
	public DocumentRevision() {
		super();
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getRevisionId() {
		return revisionId;
	}
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public InputStream getDocumentFile() {
		return documentFile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setDocumentFile(InputStream documentFile) {
		this.documentFile = documentFile;
	}
	public Hashtable<String, String> getIndexRecord() {
		return indexRecord;
	}
	public void setIndexRecord(Hashtable<String, String> indexRecord) {
		this.indexRecord = indexRecord;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
}
