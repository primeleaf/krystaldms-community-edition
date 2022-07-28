/**
 * Created On Feb 19, 2014
 * Copyright 2010 by Primeleaf Consulting (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Primeleaf Consulting (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consulting (P) Ltd.
 */

package com.primeleaf.krystal.model.vo;

/**
 * Author Rahul Kubadia
 */

public class SearchFilter {
	private String columnName;
	private String columnType;
	private boolean metaColumn;
	private String value1;
	private String value2;
	private byte operator;
	
	public SearchFilter(String columnName,String columnType,boolean metaColumn,String value1, String value2, byte operator){
		this.columnName = columnName;
		this.columnType = columnType;
		this.metaColumn = metaColumn;
		this.value1 = value1;
		this.value2 = value2;
		this.operator = operator;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @return the columnType
	 */
	public String getColumnType() {
		return columnType;
	}
	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	/**
	 * @return the metaColumn
	 */
	public boolean isMetaColumn() {
		return metaColumn;
	}
	/**
	 * @param metaColumn the metaColumn to set
	 */
	public void setMetaColumn(boolean metaColumn) {
		this.metaColumn = metaColumn;
	}
	/**
	 * @return the value1
	 */
	public String getValue1() {
		return value1;
	}
	/**
	 * @param value1 the value1 to set
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	/**
	 * @return the value2
	 */
	public String getValue2() {
		return value2;
	}
	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	/**
	 * @return the operator
	 */
	public byte getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(byte operator) {
		this.operator = operator;
	}

	public final static byte OPERATOR_IS=1;
	public final static byte OPERATOR_ISNOT = 2;
	public final static byte OPERATOR_LIKE=3;
	public final static byte OPERATOR_NOTLIKE=4;
	public final static byte OPERATOR_ISGREATERTHAN=5;
	public final static byte OPERATOR_ISLESSERTHAN=6;
	public final static byte OPERATOR_BETWEEN=7;
	public final static byte OPERATOR_ISEMPTY=8;
	public final static byte OPERATOR_ISNOTEMPTY=9;

}

