/**
 * Created on Sep 23, 2005 
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

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.tomcat.dbcp.dbcp2.datasources.SharedPoolDataSource;

import com.primeleaf.krystal.constants.ServerConstants;

/**
 * @author Rahul Kubadia
 * @since 2.0
 * @comments Database connection pooling mechanism is implemented here
 */
public class ConnectionPoolManager {

	private static DataSource dataSource;
	private static ConnectionPoolManager instance;  
	/**
	 * Default constructor
	 * @comments Reads from JNDI and creates datasource 
	 */
	private ConnectionPoolManager() {
		super();
		try{
			dataSource = getSharedPoolDataSource();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private DataSource getSharedPoolDataSource()throws Exception {
		DriverAdapterCPDS dacpds = new DriverAdapterCPDS();
		dacpds.setUrl("jdbc:derby:" + System.getProperty("user.dir") + "/data/"+ServerConstants.KRYSTAL_DATABASE+";user="+ServerConstants.KRYSTAL_DATABASEOWNER+";password="+ServerConstants.KRYSTAL_DATABASEPASSWORD+"");
		dacpds.setUser(ServerConstants.KRYSTAL_DATABASEOWNER);
		dacpds.setPassword(ServerConstants.KRYSTAL_DATABASEPASSWORD);	
		SharedPoolDataSource spdc = new SharedPoolDataSource();
		spdc.setConnectionPoolDataSource(dacpds);
		spdc.setDefaultMaxTotal(20);
		spdc.setDefaultMaxIdle(5);
		spdc.setDefaultMaxWaitMillis(500);
		spdc.setDefaultAutoCommit(false);
		return spdc;
	}

	public static synchronized ConnectionPoolManager getInstance(){

		if(instance==null){
			instance = new ConnectionPoolManager();
		}
		return instance;
	}

	/**
	 * returns a sql connection
	 */
	public Connection  getConnection(){
		Connection pooledConnection=null;
		try{
			pooledConnection = dataSource.getConnection();
			if(pooledConnection!=null){
				pooledConnection.setAutoCommit(false);
			}else{
				pooledConnection = dataSource.getConnection();
			}
			pooledConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		}catch(Exception e){
			e.printStackTrace(System.out);
		}
		return pooledConnection;
	}  

	/**
	 * Prints information of the idle connections in the system
	 */
	public void printInfos(){
		SharedPoolDataSource data = (SharedPoolDataSource)dataSource;
		int idle = data.getNumIdle();
		int used = data.getNumActive();
		int runs = 0;

		System.out.println( "[IDLE = " +idle +" , USED = "+ used +" , RUNS = "+runs+"]");

	}
}

