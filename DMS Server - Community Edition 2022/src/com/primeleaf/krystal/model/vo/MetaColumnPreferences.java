/**
 * Created on Dec 29, 2005 
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
 * @author Ahmed Raza Khan
 * @since 2.0
 * 
 */


public class MetaColumnPreferences {
	
	private final short META_CREATED = 1;
	
	private final short META_CREATEDBY = 2;
	
	private final short META_FILESIZE = 4;
	
	private final short META_REVISIONID = 8;
	
	private final short META_DOCUMENTID=16;
	
	private final short META_EXPIRYON = 32;
	
	private final short META_FILENAME = 64;
	
	private final short META_MODIFIED = 128; //This feature is added on 5-Feb-2013, 7.0 by Rahul Kubadia to display user who updated the record last

	private final short META_MODIFIEDBY = 256; //This feature is added on 5-Feb-2013, 7.0 by Rahul Kubadia to display user who updated the record last
	
	private boolean createdVisible = false;
	private boolean createdByVisible= false;
	private boolean fileSizeVisible = false;
	private boolean revisionIdVisible= false;
	private boolean documentIdVisible= false;
	private boolean expiryOnVisible = false;
	private boolean fileNameVisible = false;
	private boolean modifiedVisible = false;
	private boolean modifiedByVisible = false;
	
	/**
	 * @Default Constructor
	 *
	 */
	
	public MetaColumnPreferences(){
		super();
		createdVisible=true;
		createdByVisible=true;
		fileSizeVisible=true;
		revisionIdVisible=true;
		documentIdVisible = true;
		expiryOnVisible = true;
		fileNameVisible = true;
		modifiedVisible = true;
		modifiedByVisible = true;
	}
	
	
	public boolean isCreatedByVisible() {
		return createdByVisible;
	}


	public void setCreatedByVisible(boolean createdByVisible) {
		this.createdByVisible = createdByVisible;
	}


	public boolean isCreatedVisible() {
		return createdVisible;
	}


	public void setCreatedVisible(boolean createdVisible) {
		this.createdVisible = createdVisible;
	}


	public boolean isFileSizeVisible() {
		return fileSizeVisible;
	}


	public void setFileSizeVisible(boolean fileSizeVisible) {
		this.fileSizeVisible = fileSizeVisible;
	}


	public boolean isRevisionIdVisible() {
		return revisionIdVisible;
	}


	public void setRevisionIdVisible(boolean revisionIdVisible) {
		this.revisionIdVisible = revisionIdVisible;
	}

	public boolean isDocumentIdVisible() {
		return documentIdVisible;
	}
	public void setDocumentIdVisible(boolean documentIdVisible) {
		this.documentIdVisible = documentIdVisible;
	}

	public boolean isExpiryOnVisible() {
		return expiryOnVisible;
	}
	public void setExpiryOnVisible(boolean expiryOnVisible) {
		this.expiryOnVisible = expiryOnVisible;
	}

	public boolean isFileNameVisible() {
		return fileNameVisible;
	}
	public void setFileNameVisible(boolean fileNameVisible) {
		this.fileNameVisible = fileNameVisible;
	}

	/**
	 * @return the modifiedVisible
	 */
	public boolean isModifiedVisible() {
		return modifiedVisible;
	}

	/**
	 * @param modifiedVisible the modifiedVisible to set
	 */
	public void setModifiedVisible(boolean modifiedVisible) {
		this.modifiedVisible = modifiedVisible;
	}

	/**
	 * @return the modifiedByVisible
	 */
	public boolean isModifiedByVisible() {
		return modifiedByVisible;
	}

	/**
	 * @param modifiedByVisible the modifiedByVisible to set
	 */
	public void setModifiedByVisible(boolean modifiedByVisible) {
		this.modifiedByVisible = modifiedByVisible;
	}
	

	public int getMetaValue(){
		int metaValue=0;
		if(createdVisible)
			metaValue = metaValue | this.META_CREATED;
		if(createdByVisible)
			metaValue = metaValue | this.META_CREATEDBY;
		if(fileSizeVisible)
			metaValue = metaValue | this.META_FILESIZE;
		if(revisionIdVisible)
			metaValue = metaValue | this.META_REVISIONID;
		if(documentIdVisible)
			metaValue = metaValue | this.META_DOCUMENTID;
		if(expiryOnVisible)
			metaValue = metaValue | this.META_EXPIRYON;
		if(fileNameVisible)
			metaValue = metaValue | this.META_FILENAME;
		if(modifiedVisible)
			metaValue = metaValue | this.META_MODIFIED;
		if(modifiedByVisible)
			metaValue = metaValue | this.META_MODIFIEDBY;
		
		return metaValue;
	}
	
	public MetaColumnPreferences(int metaPreferences){
		if((metaPreferences & 1) == 1){
			createdVisible=true;
		}
		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			createdByVisible=true;
		}
		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			fileSizeVisible=true;
		}
		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			revisionIdVisible=true;
		}
		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			documentIdVisible=true;
		}

		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			expiryOnVisible=true;
		}
		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			fileNameVisible=true;
		}
		
		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			modifiedVisible=true;
		}
		
		metaPreferences = metaPreferences >> 1;
		if((metaPreferences & 1) == 1){
			modifiedByVisible=true;
		}
	}


	
}
