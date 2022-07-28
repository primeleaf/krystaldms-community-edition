/**
 * Created on Oct 14, 2005 
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
package com.primeleaf.krystal.constants;

/**
 * This class stores all the HTTP Request and Session variable used by Krystal(tm) EDMS
 * @author Rahul Kubadia
 * @since 2.0
 *
 */
public class HTTPConstants {
	/**
	 * Web Applications name and description
	 */
		
	public static final String AUTH_EDMC_COOKIE="AUTH_EDMC_COOKIE";
	public static final String AUTH_DELIMETER="####";
	
	/**
	 * Following are session variables 
	 */
	
	public static final String REQUEST_ERROR="REQUEST_ERROR";
	public static final String REQUEST_MESSAGE="REQUEST_MESSAGE";
	
	public static final String SESSION_USER ="KRYSTAL_USER";
	public static final String SESSION_KRYSTAL="KRYSTAL_SESSION";
	public static final String SESSION_LOGIN_ERROR="LOGIN_ERROR";
	public static final String SESSION_REDIRECT_PATH="REDIRECT_PATH";
		
	public static final String ERROR_INVALIDUSER="INVALID_USER";
	public static final String ERROR_INACTIVEUSER="INACTIVE_USER";
	public static final String ERROR_TOOMANYUSER="TOMANY_USER";
	public static final String ERROR_ACCESS_DENIED="ACCESS_DENIED";
	public static final String ERROR_NOTANADMIN="NOT_AN_ADMIN";
	public static final String ERROR_INVALIDDOMAIN="INVALID DOMAIN";
	public static final String ERROR_ALREADY_LOGGED_IN="ALREADY_LOGGED_IN";
	public static final String ERROR_TRIAL_EXPIRED="TRIAL_EXPIRED";

	public static final String CHARACTER_ENCODING = "UTF-8";
	
	public static final String ALPHA_REGEXP = "^[A-Za-z0-9_ ]+$";
	public static final String ALPHA_NUMERIC_REGEXP = "^[A-Za-z0-9-_ ,.~\\/\\[\\]\\(\\){}\\\\!:*\\r\\n]+$";
	public static final String NUMERIC_REGEXP = "^[+-]?(?:\\d+\\.?\\d*|\\d*\\.\\d+)$";
	public static final String PASSWORD_PATTERN_REGEXP = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,50}$";
	public static final String IP_PATTERN_REGEXP = "^(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[0-9]{2}|[0-9])(.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[0-9]{2}|[0-9])){3}$";
	public static final String TIME_PATTERN_REGEXP = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$";

	public static final String BASEURL="";
	
	public static final String URL_PARAM_NAME_SN = "SN"; /**- store name - in teh future we have multiple stores */
	public static final String URL_PARAM_NAME_TYPE = "TYPE"; /** type of the object e.g: TASK, INSTANCE, FOLDERS, SEARCHES*/
	public static final String URL_PARAM_NAME_ID ="ID"; /** id of the object e.g: Queue id, folder id */
	public static final String URL_PARAM_NAME_NAME = "NAME"; /** optional name of the object- eg: queue id could be  CSM while the name is Customer service manager*/
	public static final String URL_PARAM_NAME_VERSION = "VERSION"; /**option version of the object if you are requesting */
	public static final String URL_PARAM_NAME_PAGE ="PAGE"; /**page number*/
	public static final String URL_PARAM_NAME_ACTION ="ACTION"; /** some action performed on the page*/
	public static final String URL_PARAM_SORT_COLUMN = "SORT_COLUMN";
	public static final String URL_PARAM_SORT_ORDER= "SORT_ORDER";
	public static final String URL_PARAM_SEARCH = "SEARCH";

	public static final String URL_PARAM_TEMPLATEID = "TMI";
	public static final String URL_PARAM_TEMPLATEDESC= "TMID";
	public static final String URL_PARAM_TEMPLATEDEFINTIONID = "TDI";
	public static final String URL_PARAM_SEARCHID = "SH";
	public static final String URL_PARAM_INSTANCEID = "II";


    public static final String URL_PARAM_SEARCHNAME ="_SEARCHNAME";
    public static final String URL_PARAM_SEARCHDESC ="_SEARCHDESC";

	public static final String SESSION_TASKLIST_STORE_NAME= "KTASKLIST";
	public static final String SESSION_COMPLETEDINSTANCELIST_STORE_NAME= "KCINSTANCELIST";
	public static final String SESSION_SEARCHINSTANCELIST_STORE_NAME = "KSINSTANCELIST";
	public static final String SESSION_PAGESIZE_STORE_NAME = "pageSize";
	public static final String IDXNAME_PREFIX = "_IDX";
	public static final String CSRF_STRING = "CSRF";
}
