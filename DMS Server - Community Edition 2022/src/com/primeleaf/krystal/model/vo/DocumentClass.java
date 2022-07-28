/**
 * Created on Sep 28, 2005 
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

import java.util.ArrayList;

/**
 * This is a Document Class value object
 * @author Rahul Kubadia
 * @since 2.0
 */
public class DocumentClass {
	private int classId;
	private String className;
	private String classDescription;
	private int indexId;
	private String indexTableName;
	private int indexCount;
	private String dataTableName;
	private boolean revisionControlEnabled;
	private boolean visible;

	private String createdBy;
	private String created;

	//24-Nov-2011 New fields added for controlling the file size and number of documents in the system
	private int maximumFileSize;
	private int documentLimit;

	//10-Sep-2012 New field added for controlling the expiry period in number of days
	private int expiryPeriod;
	
	//08-May-2014 New field added for controlling the expiry notification period in number of days
	private int expiryNotificationPeriod;
	
	//01-Jan-2015 New fields added for storing the total documents and active documents count
	private int totalDocuments;
	private int activeDocuments;
	
	private ArrayList<IndexDefinition> indexDefinitions;

	/**
	 * Default constructor
	 */
	public DocumentClass() {
		super();
	}
	public String getClassDescription() {
		return classDescription;
	}
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getIndexCount() {
		return indexCount;
	}
	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}
	public int getIndexId() {
		return indexId;
	}
	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}
	public String getIndexTableName() {
		return indexTableName;
	}
	public void setIndexTableName(String indexTableName) {
		this.indexTableName = indexTableName;
	}
	public boolean isRevisionControlEnabled() {
		return revisionControlEnabled;
	}
	public void setRevisionControlEnabled(boolean revisionControlEnabled) {
		this.revisionControlEnabled = revisionControlEnabled;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getDataTableName() {
		return dataTableName;
	}
	public void setDataTableName(String dataTableName) {
		this.dataTableName = dataTableName;
	}
	public ArrayList<IndexDefinition> getIndexDefinitions() {
		return indexDefinitions;
	}
	public void setIndexDefinitions(ArrayList<IndexDefinition> indexDefinitions) {
		this.indexDefinitions = indexDefinitions;
	}
	//24-Nov-2011 New fields added for controlling the file size and number of documents in the system
	public int getMaximumFileSize() {
		return maximumFileSize;
	}
	public void setMaximumFileSize(int maximumFileSize) {
		this.maximumFileSize = maximumFileSize;
	}
	public int getDocumentLimit() {
		return documentLimit;
	}
	public void setDocumentLimit(int documentLimit) {
		this.documentLimit = documentLimit;
	}
	public String getCreated() {
		return created;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String toString(){
		return className + " [ " + classDescription +" ]";
	}
	/**
	 * @return the expiryPeriod
	 */
	public int getExpiryPeriod() {
		return expiryPeriod;
	}
	/**
	 * @param expiryPeriod the expiryPeriod to set
	 */
	public void setExpiryPeriod(int expiryPeriod) {
		this.expiryPeriod = expiryPeriod;
	}
	
	/**
	 * @return the expiryNotificationPeriod
	 */
	public int getExpiryNotificationPeriod() {
		return expiryNotificationPeriod;
	}
	/**
	 * @param expiryNotificationPeriod the expiryNotificationPeriod to set
	 */
	public void setExpiryNotificationPeriod(int expiryNotificationPeriod) {
		this.expiryNotificationPeriod = expiryNotificationPeriod;
	}
	
	/**
	 * @return the totalDocuments
	 */
	public int getTotalDocuments() {
		return totalDocuments;
	}
	/**
	 * @param totalDocuments the totalDocuments to set
	 */
	public void setTotalDocuments(int totalDocuments) {
		this.totalDocuments = totalDocuments;
	}
	/**
	 * @return the activeDocuments
	 */
	public int getActiveDocuments() {
		return activeDocuments;
	}
	/**
	 * @param activeDocuments the activeDocuments to set
	 */
	public void setActiveDocuments(int activeDocuments) {
		this.activeDocuments = activeDocuments;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DocumentClass){
			DocumentClass temp = (DocumentClass) obj;
			if(this.classId == temp.classId){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return (this.className.hashCode());        
	}

	public static final String ACCESS_TYPE_PUBLIC="P";
	public static final String ACCESS_TYPE_USER="U";
}
