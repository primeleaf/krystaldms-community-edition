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
package com.primeleaf.krystal.model.vo;

import java.sql.Timestamp;

/**
 * This class is a value object for Document.
 * @author Rahul Kubadia
 * @since 2.0
 */
public class Document {
	private int documentId;
	private String revisionId="1.0";
	private int classId;
	private String documentType;
	private String status;
	private String created;
	private Timestamp modified;
	private String extension;
	private byte hasNote;
	private Timestamp lastAccessed;
	private int accessCount;
	private String createdBy;
	private Timestamp expiry;
	private String modifiedBy;//This feature is added on 5-Feb-2013, 7.0 by Rahul Kubadia to display user who updated the record last

	/**
	 * Default constructor 
	 */
	
	public Document() {
		super();
	}

	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
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

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public byte getHasNote() {
		return hasNote;
	}

	public void setHasNote(byte hasNote) {
		this.hasNote = hasNote;
	}

	
	public Timestamp getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Timestamp lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public String getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the expiry
	 */
	public Timestamp getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(Timestamp expiry) {
		this.expiry = expiry;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}



	public static final String DATE_CREATED = "CREATED";
	public static final String DATE_MODIFIED = "MODIFIED";
	
	

}
