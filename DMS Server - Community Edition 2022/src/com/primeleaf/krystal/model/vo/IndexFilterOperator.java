/** Created On Feb 19, 2007
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
 public class IndexFilterOperator {
	 private byte value;
	 private String description;
	 public IndexFilterOperator(){
		 this.value=1;
		 this.description="is equals";
	 }
	 
	 public IndexFilterOperator(byte value,String description){
		 this.value=value;
		 this.description=description;
	 }
	 
	 public String getDescription() {
		 return description;
	 }

	 public void setDescription(String description) {
		 this.description = description;
	 }

	 public byte getValue() {
		 return value;
	 }

	 public void setValue(byte value) {
		 this.value = value;
	 }

	 public String toString(){
		 return description; 
	 }

}
