/** Created On Jun 29, 2006
 *
 * Copyright 2006 by Primeleaf Consulting (P) Ltd.,
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

import java.util.Enumeration;
import java.util.Vector;
 /**
 * @author Rahul Kubadia
 * @since 2.1
 */
public class IndexFilter {

	private String indexName="";
	private String value1="";
	private String value2="";
	private byte operator=1;
	
	private static Vector<IndexFilterOperator> vIndexOperator ;
	/**
	 * Constuctors for the class 
	 */
	
	public IndexFilter() {
		super();
	}
	
	public IndexFilter(String indexName){
		this.indexName=indexName;
	}
	
	public IndexFilter(String indexName,byte operator,String val1){
		this(indexName);
		this.operator=operator;
		this.value1=val1;
	}
	
	public IndexFilter(String indexName,byte operator,String val1,String val2){
		this(indexName,operator,val1);
		this.value2=val2;
		
	}

	/**
	 * Getter Setter Methods
	 */
	
	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public byte getOperator() {
		return operator;
	}

	public void setOperator(byte operator) {
		this.operator = operator;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}
	
	
	public static Vector<IndexFilterOperator> getOperators(){
		if(vIndexOperator == null){
			vIndexOperator = new Vector<IndexFilterOperator>();
			IndexFilterOperator operatorIs = new IndexFilterOperator(IndexDefinition.OPERATOR_IS,"is equal");
			IndexFilterOperator operatorIsNot = new IndexFilterOperator(IndexDefinition.OPERATOR_ISNOT,"is not equals");
			IndexFilterOperator operatorLike = new IndexFilterOperator(IndexDefinition.OPERATOR_LIKE,"is like");
			IndexFilterOperator operatorNotLike = new IndexFilterOperator(IndexDefinition.OPERATOR_NOTLIKE,"is not like");
			IndexFilterOperator operatorIsGreaterThan = new IndexFilterOperator(IndexDefinition.OPERATOR_ISGREATERTHAN,"is greater than");
			IndexFilterOperator operatorIsLessThan = new IndexFilterOperator(IndexDefinition.OPERATOR_ISLESSERTHAN,"is less than");
			IndexFilterOperator operatorBetween = new IndexFilterOperator(IndexDefinition.OPERATOR_BETWEEN,"is between");
			IndexFilterOperator operatorIsEmpty = new IndexFilterOperator(IndexDefinition.OPERATOR_ISEMPTY,"is empty"); //Added on 15th October 2013 by Rahul Kubadia for Export Document Wizard
			IndexFilterOperator operatorIsNotEmpty = new IndexFilterOperator(IndexDefinition.OPERATOR_ISNOTEMPTY,"is not empty"); //Added on 15th October 2013 by Rahul Kubadia for Export Document Wizard
			
			vIndexOperator.add(operatorIs);
			vIndexOperator.add(operatorIsNot);
			vIndexOperator.add(operatorLike);
			vIndexOperator.add(operatorNotLike);
			vIndexOperator.add(operatorIsGreaterThan);
			vIndexOperator.add(operatorIsLessThan);
			vIndexOperator.add(operatorBetween);
			vIndexOperator.add(operatorIsEmpty);//Added on 15th October 2013 by Rahul Kubadia for Export Document Wizard
			vIndexOperator.add(operatorIsNotEmpty);//Added on 15th October 2013 by Rahul Kubadia for Export Document Wizard
		}
		return vIndexOperator;
	}
	
	public static String getDescription(byte operator) {
		Enumeration<IndexFilterOperator> e = vIndexOperator.elements();
		while(e.hasMoreElements()){
			IndexFilterOperator o = (IndexFilterOperator)e.nextElement();
			if(o.getValue()==operator){
				return o.getDescription();
			}
		}
		return "";
	}

	public String toString(){
		return this.indexName + "(" + this.value1 + "," + this.value2 + "[" + this.operator + "])";
	}
}
