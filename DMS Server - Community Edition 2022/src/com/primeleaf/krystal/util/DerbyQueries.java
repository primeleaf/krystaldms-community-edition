/**
 * Created on May 1, 2005
 *
 *
 * Copyright 2005 by Arysys Technologies (P) Ltd.,
 * #3,Shop line,
 * Sasmira Marg,
 * Worli,Mumbai 400 025
 * India
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Arysys Technologies (P) Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Arysys Technologies (P) Ltd.
 *
 */
package com.primeleaf.krystal.util;

import java.util.ListResourceBundle;

/**
 * @author Pratik.Makani
 */
public class DerbyQueries extends ListResourceBundle{
	private static final Object[][] contents = {
			
			{
				"query1","CREATE TABLE AUDITLOGS("+
				" OBJECTID INTEGER,"+
				" OBJECTTYPE VARCHAR(2) ,"+
				" ACTIONPERFORMED VARCHAR(50) ,"+
				" USERNAME VARCHAR(32) ,"+
				" IPADDRESS VARCHAR(50) ,"+
				" ACTIONDATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,"+
				" PARAMETERS VARCHAR(255) ,"+
				" COMMENTS VARCHAR(500) ,"+
				" AUDITLEVEL INT"+
				")"
			},
			
			{
				"query2","CREATE TABLE BOOKMARKS(" +
				" ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1),"+
				" NAME VARCHAR(50) 	,"+
				" DOCUMENTID INTEGER,"+
				" REVISIONID VARCHAR(50) ,"+
				" USERNAME VARCHAR(32) ,"+
				" CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
				")"
			},
						
			{
				"query3","CREATE TABLE CHECKOUT(" +
				" DOCUMENTID INTEGER NOT NULL,"+
				" REVISIONID VARCHAR(50) DEFAULT '1.0' NOT NULL,"+
				" CLASSID INTEGER NOT NULL,"+
				" USERNAME VARCHAR(32) ,"+
				" CHECKOUTPATH VARCHAR(1000) ,"+
				" CHECKOUTDATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
				" COMMENTS VARCHAR(1024) " +
				")"
			},
			
			{
				"query4","CREATE TABLE DOCUMENTCLASSES(" +
				" CLASSID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1),"+
				" CLASSNAME VARCHAR(64) NOT NULL,"+
				" CLASSDESC VARCHAR(128) ,"+
				" INDEXID INTEGER ,"+
				" INDEXTABLENAME VARCHAR(10) ,"+
				" INDEXCOUNT INTEGER,"+
				" DATATABLENAME VARCHAR(16) ,"+
				" REVISIONCTL VARCHAR(1) ,"+
				" MAXFILESIZE INTEGER NOT NULL DEFAULT 0,"+
				" DOCUMENTLIMIT INTEGER NOT NULL DEFAULT 0,"+
				" EXPIRYPERIOD INTEGER NOT NULL DEFAULT 0," +	
				" EXPIRYNOTIFICATIONPERIOD INTEGER NOT NULL DEFAULT 7," + 
				" CREATEDBY VARCHAR(32) ,"+
				" CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
				" ACTIVE VARCHAR(1) DEFAULT 'Y', " +
				" TOTALDOCUMENTS INT DEFAULT 0, " +
				" ACTIVEDOCUMENTS INT DEFAULT 0 " +
				")"
			},
			
			{
				"query5","CREATE TABLE DOCUMENTS(" +
				" DOCUMENTID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1),"+
				" REVISIONID VARCHAR (50) DEFAULT '1.0' ,"+
				" CLASSID INTEGER ,"+
				" DOCUMENTTYPE VARCHAR (8) ,"+
				" STATUS VARCHAR (8) ,"+
				" CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
				" MODIFIED TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
				" EXPIRY TIMESTAMP ," + 	
				" EXT VARCHAR (16) ,"+
				" HASNOTE INTEGER ,"+
				" LASTACCESS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+
				" ACCESSCOUNT INTEGER," +
				" CREATEDBY VARCHAR(32) ,"+
				" MODIFIEDBY  VARCHAR(32) "+
				")"
			},
			
			{
				"query6","CREATE TABLE DOCUMENTREVISIONS (" +
				" DOCUMENTID INTEGER NOT NULL," +
				" REVISIONID VARCHAR(50) NOT NULL," +
				" SOFFSET INT ," +
				" LENGTH INT " +
				")"
			},
			
			{
				"query7","CREATE TABLE INDEXES(" +
				" INDEXID INT NOT NULL," +
				" SEQ INT NOT NULL," +
				" IDXCOLNAME VARCHAR(50) NOT NULL," +
				" IDXDISPLAYNAME VARCHAR(50) NOT NULL," +
				" IDXMAXLEN INTEGER	," +
				" IDXTYPE VARCHAR(1) DEFAULT 'S' ," +
				" DEFAULTFILTER VARCHAR(255)," +
				" DEFAULTVALUE VARCHAR(255)," +
				" MANDATORY VARCHAR(1) DEFAULT 'N'" +
				")"
			},
			
			
			
			{
				"query8","CREATE TABLE NOTES(" +
				" ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1)," +
				" NOTEDATA VARCHAR (2048) NOT NULL," +
				" DOCUMENTID INT," +
				" USERNAME VARCHAR(32)," +
				" ACTIVE VARCHAR(1) DEFAULT 'Y'," +
				" NOTETYPE VARCHAR(1) DEFAULT 'P',"+
				" CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
				")"
			},
			
			{
				"query9","CREATE TABLE PASSWORDHISTORY(" +
				" USERID INTEGER NOT NULL," +
				" PASSWORD VARCHAR(255)," +
				" CHANGEDATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
				")"
			},
			
			{
				"query10","CREATE TABLE PERMISSIONS(" +
				" ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1)," +
				" CLASSID INTEGER,"+
				" USERID INTEGER,"+
				" VALUE INTEGER DEFAULT 511"+
				")"
			},
			
			
			{
				"query11","CREATE TABLE REVISIONHISTORY(" +
				" DOCUMENTID INTEGER,"+
				" REVISIONID VARCHAR(50) ,"+
				" USERNAME VARCHAR(32) ,"+
				" USERACTION VARCHAR(50) ,"+
				" COMMENTS VARCHAR(256) ,"+
				" DATEOFACTION TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
				")"
			},
						
			
			
			{
				"query12","CREATE TABLE USERS(" +
				" USERID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1)," +
				" USERNAME VARCHAR(32) NOT NULL UNIQUE," +
				" PASSWORD VARCHAR(255)," +
				" USERDESC VARCHAR(64)," +
				" USEREMAIL VARCHAR(64) ," +
				" USERTYPE VARCHAR(1) ," +
				" REALNAME VARCHAR(64)," +
				" IPADDRESS VARCHAR(32),"+
				" LASTLOGINDATE TIMESTAMP, "+
				" HITLISTSIZE INTEGER DEFAULT 10," +
				" CHECKOUTPATH VARCHAR(255) DEFAULT 'C:/TEMP'," +
				" SHOWTHUMBNAIL VARCHAR(8) DEFAULT 'TRUE'," +	
				" METACOLUMN INT DEFAULT 511," +
				" ACTIVE VARCHAR(1) DEFAULT 'Y'," +
				" LOGGEDIN 	VARCHAR(1) DEFAULT 'N'," +	
				" PROFILEPICTURE 	 BLOB(4M) " + 
				")"
			},
			{
				"query13","INSERT INTO USERS(USERNAME,PASSWORD,USERDESC,USEREMAIL,USERTYPE,REALNAME,HITLISTSIZE,CHECKOUTPATH,METACOLUMN,SHOWTHUMBNAIL)" +
				" VALUES( 'ADMINISTRATOR','0DPiKuNIrrVmD8IUCuw1hQxNqZc=','DMS Administrator','rahul.kubadia@primeleaf.in','A', 'Administrator',10,'c:/temp/',511,'TRUE')"
			},
			{
				"query14","INSERT INTO PASSWORDHISTORY(USERID,PASSWORD) VALUES (1,'0DPiKuNIrrVmD8IUCuw1hQxNqZc=')"
			},
			
	};
	
	public Object[][] getContents(){
		return contents;
	}
	
}
