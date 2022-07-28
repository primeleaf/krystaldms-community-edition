/**
 * Created on Oct 26, 2005 
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
package com.primeleaf.krystal.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.AuditLogRecord;

/**
 * Every action of user on a particular obect is logged to database using this class<br>
 * Reports based on action dates , type of object along with log level are also generated here.
 * 
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.data.AuditLogRecord
 */
public class AuditLogRecordDAO {

	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	private String SQL_INSERT_AUDITLOGS="INSERT INTO AUDITLOGS(OBJECTID,OBJECTTYPE,ACTIONPERFORMED,USERNAME,IPADDRESS,PARAMETERS,COMMENTS,AUDITLEVEL)VALUES(?,?,?,?,?,?,?,?)"; 

	private String SQL_SELECT_AUDITLOGS="SELECT OBJECTID,OBJECTTYPE,ACTIONPERFORMED,USERNAME,IPADDRESS,ACTIONDATE,PARAMETERS,COMMENTS,AUDITLEVEL FROM AUDITLOGS ";

	private static AuditLogRecordDAO _instance;
	/**
	 * Default constructor
	 */
	private AuditLogRecordDAO() {
		super();
	}

	public static synchronized AuditLogRecordDAO getInstance() { 
		if (_instance==null) { 
			_instance = new AuditLogRecordDAO(); 
		} 
		return _instance; 
	} 
	public void addAuditLog(AuditLogRecord auditLogRecord)throws Exception{
		kLogger.fine("Adding audit record");
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_AUDITLOGS);
		int i=1;
		psInsert.setInt(i++,auditLogRecord.getObjectId());
		psInsert.setString(i++,auditLogRecord.getObjectType());
		psInsert.setString(i++,auditLogRecord.getAction());
		psInsert.setString(i++,auditLogRecord.getUserName());
		psInsert.setString(i++,auditLogRecord.getIpAddress());
		psInsert.setString(i++,auditLogRecord.getParameters());
		psInsert.setString(i++,auditLogRecord.getComments());
		psInsert.setInt(i++,auditLogRecord.getLevel());
		int recCount = psInsert.executeUpdate();

		psInsert.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to add audit log record");
			throw new Exception("Unable to add audit log record");
		}
	}

	public ArrayList<AuditLogRecord> getAuditTrail(String criteria){
		ArrayList<AuditLogRecord> result = new ArrayList<AuditLogRecord>();
		kLogger.fine("Reading audit logs  for criteria :" + criteria);

		try{
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append(SQL_SELECT_AUDITLOGS);

			if(criteria.length()>1){
				sbSelect.append(" WHERE " + criteria);
			}
			Connection connection = ConnectionPoolManager.getInstance().getConnection();

			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());

			ResultSet rs = psSelect.executeQuery();

			while (rs.next()) {
				AuditLogRecord logRecord = new AuditLogRecord(1,"D");
				logRecord.setObjectId(rs.getInt("OBJECTID"));
				logRecord.setObjectType(rs.getString("OBJECTTYPE"));
				logRecord.setAction(rs.getString("ACTIONPERFORMED"));
				logRecord.setUserName(rs.getString("USERNAME"));
				logRecord.setIpAddress(rs.getString("IPADDRESS"));
				logRecord.setActionDate(rs.getString("ACTIONDATE"));
				logRecord.setParameters(rs.getString("PARAMETERS"));
				logRecord.setComments(rs.getString("COMMENTS"));
				logRecord.setLevel(rs.getInt("AUDITLEVEL"));
				result.add(logRecord);
			}
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to read audit logs  for criteria :" + criteria);
		}
		return result;
	}

	public void deleteAllLogs()throws Exception{
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psDelete = connection.prepareStatement("DELETE FROM AUDITLOGS" );
		int recCount = psDelete.executeUpdate();
		psDelete.executeUpdate();
		psDelete.close();
		connection.commit();
		connection.close();
		kLogger.info("Audit log records cleared. " + recCount + " records deleted.");
	}

}
