/**
 * Created on Sep 23, 2009
 *
 * Copyright 2003-09 by Primeleaf Consulting (P) Ltd.,
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

import java.util.ArrayList;

/**
 * This class is Value Object for resultant hitlist 
 * @author Rahul Kubadia
 * @since 3.1 [23<SUP>rd</SUP> September 2009]
 * 
 * @see com.primeleaf.krystal.model.dao.HitListDAO
 * @see com.primeleaf.krystal.model.vo.Hit
 */

public class HitList {

	private int totalHits;
	private ArrayList<Hit> hits;
	/**
	 * @return the totalHits
	 */
	public int getTotalHits() {
		return totalHits;
	}
	/**
	 * @param totalHits the totalHits to set
	 */
	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}
	/**
	 * @return the hits
	 */
	public ArrayList<Hit> getHits() {
		return hits;
	}
	/**
	 * @param hits the hits to set
	 */
	public void setHits(ArrayList<Hit> hits) {
		this.hits = hits;
	}
	
	public static  boolean isMetaDataColumn(String columnName){
		if(columnName.equalsIgnoreCase(META_CREATED)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_REVISIONID)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_HASNOTE)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_STATUS)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_LENGTH)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_CREATEDBY)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_DOCUMENTID)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_EXPIRYON)){
			return true;
		}
		if(columnName.equalsIgnoreCase(METAFILTER_FILENAME)){
			return true;
		}
		if(columnName.equalsIgnoreCase(META_MODIFIED)){
			return true;
		}
		if(columnName.equalsIgnoreCase(METAFILTER_MODIFIEDBY)){
			return true;
		}
		if(columnName.equalsIgnoreCase(METAFILTER_WFSTATUS)){
			return true;
		}

		return false;
	}
	
	public static final String META_EXPIRYON="EXPIRY";
	public static final String META_DOCUMENTID="DOCUMENTID";
	public static final String META_CREATED="CREATED";
	public static final String META_HASNOTE="HASNOTE";
	public static final String META_STATUS="STATUS";
	public static final String META_REVISIONID="REVISIONID";
	public static final String META_MODE="MODE";
	public static final String META_CREATEDBY="CREATEDBY";
	public static final String META_LENGTH="LENGTH";
	public static final String META_FILENAME="FILENAME"; //07-Dec-2012 Rahul Kubadia - File Name Filter
	public static final String META_WFSTATUS="WFSTATUS";//23-July-2013 Rahul Kubadia - Workflow Status Filter Version 8.0
	
	public static final String META_MODIFIED="MODIFIED"; //05-Feb-2013 Rahul Kubadia - Modified Date Filter
	public static final String META_MODIFIEDBY="MODIFIEDBY"; //05-Feb-2013 Rahul Kubadia - Modified By (Username) Filter
	
	public static final String METAFILTER_TODATE="TODATE";
	public static final String METAFILTER_FROMDATE="FROMDATE";
	
	public static final String METAFILTER_TOEXPIRYDATE="TOEXPIRYDATE";//10-Sep-2012 Rahul Kubadia - Expiry On Filter
	public static final String METAFILTER_FROMEXPIRYDATE="FROMEXPIRYDATE";//10-Sep-2012 Rahul Kubadia - Expiry On Filter
	
	public static final String METAFILTER_FILENAME="FILENAME";//07-Dec-2012 Rahul Kubadia - File Name Filter
	
	public static final String METAFILTER_EXTENSION="EXTENSION";
	public static final String METAFILTER_CREATEDBY="CREATEDBY";
	
	public static final String METAFILTER_TOMODIFIEDDATE="TOMODIFIEDDATE"; //05-Feb-2013 Rahul Kubadia - Modified Date Filter
	public static final String METAFILTER_FROMMODIFIEDDATE="FROMMODIFIEDDATE"; //05-Feb-2013 Rahul Kubadia - Modified Date Filter
	
	public static final String METAFILTER_MODIFIEDBY="MODIFIEDBY";//05-Feb-2013 Rahul Kubadia - Modified By (Username) Filter
	public static final String METAFILTER_WFSTATUS="WFSTATUS";//23-July-2013 Rahul Kubadia - Workflow Status Filter Version 8.0
	
	
}
