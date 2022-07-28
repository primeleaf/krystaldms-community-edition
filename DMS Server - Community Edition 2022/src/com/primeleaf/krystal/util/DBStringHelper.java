/**
 * Created on Jul 2, 2005
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.AuditLogRecord;

/**
 * @author Pratik.Makani
 *
 */
public class DBStringHelper {


	/**
	 * @return the data type varchar compatible to the database platform
	 */
	public static String getDBVarchar(){
		return "VARCHAR";
	}

	/**
	 * @return the data type varchar compatible to the database platform
	 */
	public static String getDBImage(){
		return "BLOB(2G)";
	}

	/**
	 * @return the data type date time compatible to the database platform
	 */
	public static String getDBDateTime() {
		return "TIMESTAMP";
	}

	public static String getDBText(){
		return "LONG VARCHAR";
	}

	public static String getAutoIncColumn(String columnName){
		return columnName+" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1) ";
	}

	public static String getFullTextDefinition(String columnName){
		return ""; 
	}

	public static String getColumnDef(String columnName, String dataType, String size, String NULL, String defaultValue){
		String columnDef = "";
		columnDef = " " + columnName + " " + dataType;
		if(size.length() > 0) columnDef += "("+size+")";
		if(NULL.equals("NOT NULL"))
			columnDef += " " + NULL ;
		if(defaultValue.length() > 0) 
			columnDef += " DEFAULT " + defaultValue;
		return columnDef;
	}

	public static String getOracleSequence(String tableName){
		String seq = "create sequence "+tableName+"_seq start with 1 increment by 1";
		return seq;
	}

	public static String getOracleTrigger(String tableName, String primaryKey){
		String trigger = "create or replace trigger  " + tableName + "_insert before insert on " + tableName +
				" for each row begin select  " + tableName + "_seq.nextval into :new." + primaryKey + " from dual;" +
				"end;";
		return trigger;
	}

	public static java.sql.Date getSQLDate(String dateString){
		Date date = new Date();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//Fixed By Saumil
			date = sdf.parse(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new java.sql.Date(date.getTime());
	}

	public static int getGeneratedKey(String columnName,String tableName){
		int key = 0;
		try{
			String SQL = "SELECT MAX("+columnName+") FROM " + tableName;
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(SQL);
			ResultSet rs = psSelect.executeQuery();
			if(rs.next()){
				key = rs.getInt(1);
			}
			rs.close();
			psSelect.close();
			connection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
	public static int daysRemaining(){
		ArrayList<AuditLogRecord> startDateRecords = AuditLogManager.getAuditLogs(" ACTIONDATE = (SELECT MIN(ACTIONDATE) FROM AUDITLOGS)");
		ArrayList<AuditLogRecord> lastDateRecords = AuditLogManager.getAuditLogs(" ACTIONDATE = (SELECT MAX(ACTIONDATE) FROM AUDITLOGS)");
		if(startDateRecords.size() == 0){
			return 0;
		}
		if(lastDateRecords.size() == 0){
			return 0;
		}
		
		AuditLogRecord firstAuditRecord = startDateRecords.get(0);
		AuditLogRecord lastAuditRecord = lastDateRecords.get(0);
		
		Date startDate = StringHelper.getDate(firstAuditRecord.getActionDate());
		Date lastDate = StringHelper.getDate(lastAuditRecord.getActionDate());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.DAY_OF_YEAR, 15);
		Date expiryDate =  cal.getTime();

		Calendar today = Calendar.getInstance();
		Date now = today.getTime();

		if(now.before(startDate)){
			return 0;
		}
		if(expiryDate.before(now)){
			return 0;
		}
		if(now.before(lastDate)){
			return 0;
		}
		long daysLeft = StringHelper.calculateDays(now, expiryDate);
		return (int) daysLeft;
	}
}