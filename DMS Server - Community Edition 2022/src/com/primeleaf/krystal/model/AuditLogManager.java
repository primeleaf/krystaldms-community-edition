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
package com.primeleaf.krystal.model;

import java.util.ArrayList;

import com.primeleaf.krystal.model.dao.AuditLogRecordDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;

/**
 * Storage and retrieval for audit logs are handled via this class.
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.model.vo.AuditLogRecord
 * @see com.primeleaf.dms.data.AuditLogRecrodDAO
 */
public class AuditLogManager {

	/**
	 * Default constructor 
	 */
	public AuditLogManager() {
		super();
	}
	
	public static void log(AuditLogRecord auditLogRecord ){
		try{
			AuditLogRecordDAO.getInstance().addAuditLog(auditLogRecord);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static ArrayList<AuditLogRecord> getAuditLogs(String criteria){
		ArrayList<AuditLogRecord> result  = new ArrayList<AuditLogRecord>();
		try{
			result =  AuditLogRecordDAO.getInstance().getAuditTrail(criteria);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
