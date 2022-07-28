/**
 * Created On 16-Jan-2015
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

package com.primeleaf.krystal.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.Permission;

/**
 * Author Rahul.Kubadia
 */

public class PermissionDAO {
	
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	
	private String SQL_INSERT="INSERT INTO PERMISSIONS ( CLASSID, USERID, VALUE ) VALUES (?,?,?)";
	private String SQL_SELECT = "SELECT ID,CLASSID,USERID,VALUE FROM PERMISSIONS ";
	private String SQL_DELETE_BY_CLASSID = "DELETE FROM PERMISSIONS WHERE CLASSID = ?  ";
	
	private static PermissionDAO instance; 
	private PermissionDAO() {
		super();
	}
	public static synchronized PermissionDAO getInstance() {
		if (instance == null) { 
			instance = new PermissionDAO(); 
		} 
		return instance; 
	} 
	
	public void create(Permission permission) throws Exception{
		kLogger.fine("Adding Permissions");
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT);
		int i=1;
		psInsert.setInt(i++,permission.getClassId());
		psInsert.setInt(i++,permission.getUserId());
		psInsert.setInt(i++,permission.getAclValue());
		int recCount = psInsert.executeUpdate();
		psInsert.close();
		connection.commit();
		connection.close();
		if(recCount == 0){
			kLogger.warning("Unable to add Permissions");
			throw new Exception("Unable to add Permissions");
		}
	}
	
	public ArrayList<Permission> readPermissions(final String criteria){
		ArrayList<Permission> result = new ArrayList<Permission>();
		kLogger.fine("Reading permissions for criteria :" + criteria);
		try{
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append(SQL_SELECT);

			if(criteria.length()>1){
				sbSelect.append(" WHERE " + criteria);
			}
			Connection connection = ConnectionPoolManager.getInstance().getConnection();
			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				Permission permission = new Permission();
				permission.setId(rs.getInt("ID"));
				permission.setClassId(rs.getInt("CLASSID"));
				permission.setUserId(rs.getInt("USERID"));
				permission.setAclValue(rs.getInt("VALUE"));
				result.add(permission);
			} 
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to read permissions for criteria :" + criteria);
		}
		return result;
	}
	
	public void delete(final int classId)throws Exception{
		kLogger.fine("Deleting permissions : class id =" + classId);
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_BY_CLASSID);
		psDelete.setInt(1,classId);
		psDelete.executeUpdate();
		psDelete.close();
		connection.commit();
		connection.close();
	}
}