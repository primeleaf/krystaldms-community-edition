/**
 * Created on Sep 29, 2005 
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
 * Store definitions of indexes used in DMS System
 * Index Definition Value Object
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.model.vo.DocumentClass
 */
public class IndexDefinition {
	public static long serialVersionUID =1L;
	
	public static String INDEXTYPE_DATE="D";
	public static String INDEXTYPE_STRING="S";
	public static String INDEXTYPE_NUMBER="N";
	
	public final static String ORDERTYPE_ASCENDING = "A";
	public final static String ORDERTYPE_DESCENDING = "D";
	
	public final static byte OPERATOR_IS=1;
	public final static byte OPERATOR_ISNOT = 2;
	public final static byte OPERATOR_LIKE=3;
	public final static byte OPERATOR_NOTLIKE=4;
	public final static byte OPERATOR_ISGREATERTHAN=5;
	public final static byte OPERATOR_ISLESSERTHAN=6;
	public final static byte OPERATOR_BETWEEN=7;
	//New funtionality added for empty and non empty index values filter by Rahul Kubadia on 23rd October 2012 for Version 6.0
	public final static byte OPERATOR_ISEMPTY=8;
	public final static byte OPERATOR_ISNOTEMPTY=9;

	
	private int indexId;
	private short sequence;
	private String indexColumnName;
	private String indexDisplayName;
	private int indexMaxLength;
	private String indexType;
	private String defaultFilter; 
	private String defaultValue; //Used for storign the default value for the index - 9.0 Rahul Kubadia 27-Jan-2014
	private boolean mandatory;//Used for storing the mandatory flag for the index - 7.0 Rahul Kubadia 26-dec-2012
	
	//non database related fields
	private String parentClassName="";
	private String filterValue="";
	private String filterValue2="";
	private byte filterOperator=0;
	
	
	/**
	 * Default constructor
	 */
	public IndexDefinition() {
		super();
	}
	
	public String getDefaultFilter() {
		return defaultFilter;
	}

	public void setDefaultFilter(String defaultFilter) {
		this.defaultFilter = defaultFilter;
	}

	public String getIndexColumnName() {
		return indexColumnName;
	}

	public void setIndexColumnName(String indexColumnName) {
		this.indexColumnName = indexColumnName;
	}

	public String getIndexDisplayName() {
		return indexDisplayName;
	}

	public void setIndexDisplayName(String indexDisplayName) {
		this.indexDisplayName = indexDisplayName;
	}

	public int getIndexId() {
		return indexId;
	}

	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}

	public int getIndexMaxLength() {
		return indexMaxLength;
	}

	public void setIndexMaxLength(int indexMaxLength) {
		this.indexMaxLength = indexMaxLength;
	}

	public String getIndexType() {
		if(indexType==null)indexType="";
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	public short getSequence() {
		return sequence;
	}

	public void setSequence(short sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the filterOperator
	 */
	public byte getFilterOperator() {
		return filterOperator;
	}

	/**
	 * @param filterOperator the filterOperator to set
	 */
	public void setFilterOperator(byte filterOperator) {
		this.filterOperator = filterOperator;
	}

	/**
	 * @return the filterValue
	 */
	public String getFilterValue() {
		if(filterValue == null){
			filterValue = "";
		}
		return filterValue.trim();
	}

	/**
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	/**
	 * @return the filterValue2
	 */
	public String getFilterValue2() {
		if(filterValue2 == null){
			filterValue2 = "";
		}
		return filterValue2.trim();
	}

	/**
	 * @param filterValue2 the filterValue2 to set
	 */
	public void setFilterValue2(String filterValue2) {
		this.filterValue2 = filterValue2;
	}

	/**
	 * @return the parentClassName
	 */
	public String getParentClassName() {
		return parentClassName;
	}

	/**
	 * @param parentClassName the parentClassName to set
	 */
	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

	/**
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		if(defaultValue == null){
			defaultValue = "";
		}
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
