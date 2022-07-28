/**
 * Created on Aug 1, 2003
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

package com.primeleaf.krystal.dms;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;

import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.util.ConfigParser;
import com.primeleaf.krystal.util.DerbyQueries;
import com.primeleaf.krystal.util.ExpiryProcessor;
import com.primeleaf.krystal.web.WebServerManager;

/**
 * @author Rahul Kubadia
 * @since 1.0
 * @comments Krystal Server initialized , started and stopped using methods in this class.
 * @see com.primeleaf.krystal.web.KrystalSession
 */


public class DMSServer {
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());
	private String SERVER_VERSION = "X.X";
	private static WebServerManager webServerManager;
	private static DMSServer serverInstance;
	public static DocumentConverter openOfficeDocumentConverter = null;//Added By Saumil Shah on 26 Nov 2021
	
	public static synchronized DMSServer getInstance(){
		if(serverInstance == null){
			serverInstance = new DMSServer();
		}
		return serverInstance;
	}
	private DMSServer() {
		try {
			setEnvironment();
			configureDataStore();
			startWebApplications();
			updateLoginStatus();
			adjustDocumentCount();
			initJOD();//Added By Saumil Shah on 26 Nov 2021 inits JODconverter
			processExpiry();
		} catch (Exception e) {
			kLogger.severe("Unable to start " + SERVER_VERSION + " due to " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void setEnvironment() {
		SERVER_VERSION =   "KRYSTAL DMS " + ServerConstants.SERVER_VERSION + " - " + ServerConstants.SERVER_EDITION ;
		//If shutdown file exist then delete it before starting the server
		File f = new File(ServerConstants.KRYSTAL_HOME + "/shutdown");
		if (f.exists()) {
			f.delete();
		}
		kLogger.info("Initializing " + SERVER_VERSION + " On : " + System.getProperty("java.version"));
		kLogger.info("Starting " + SERVER_VERSION + "...");
	}

	private static void startWebApplications() {
		try{
			int httpPort=ConfigParser.getHTTPPort();
			webServerManager = new WebServerManager(httpPort,ServerConstants.KRYSTAL_HOME);
			webServerManager.start();
		}catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	private static void initJOD() {//Added By Saumil Shah on 26 Nov 2021
		final LocalOfficeManager.Builder builder = LocalOfficeManager.builder();
		builder
			.officeHome(ConfigParser.getOpenOfficePath())
			.workingDir(ServerConstants.KRYSTAL_HOME +"/log");
			
		OfficeManager officeManager = builder.build();
		try {
			officeManager.start();
		} catch (OfficeException e) {
			e.printStackTrace();
		}
		openOfficeDocumentConverter = LocalConverter.builder().officeManager(officeManager).build();
	}
	
	/**
	 * This method sets login status of the all the users to "N"
	 * @author Rahul Kubadia
	 * @since 6.0
	 */
	private void updateLoginStatus(){
		try{
			for(User user: UserDAO.getInstance().readUsers("")){
				user.setLoggedIn(false);
				UserDAO.getInstance().updateLoginStatus(user);
			}
			//AuditLogRecordDAO.getInstance().deleteAllLogs();
		}catch(Exception e){
			kLogger.severe("Error updating login status : " + e.getLocalizedMessage());
			e.printStackTrace(System.err);
		}
	}

	private void configureDataStore(){
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").getDeclaredConstructor().newInstance();
			String connectionString = "jdbc:derby:" + ServerConstants.KRYSTAL_HOME +  "/data/" + ServerConstants.KRYSTAL_DATABASE + ";";
			Connection databaseConnection = null;
			try{
				databaseConnection = DriverManager.getConnection(connectionString);
				if(databaseConnection != null){
					databaseConnection.close();
				}
			}catch(Exception ex){ //Database does not exist hence create it
				connectionString = "jdbc:derby:" + System.getProperty("user.dir") + "/data/" + ServerConstants.KRYSTAL_DATABASE + ";create=true;";
				databaseConnection = DriverManager.getConnection(connectionString);
				ResourceBundle queryResource =  DerbyQueries.getBundle("com.primeleaf.krystal.util.DerbyQueries");
				Statement stat = databaseConnection.createStatement();
				stat.execute(" CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user."+ ServerConstants.KRYSTAL_DATABASEOWNER + "', '" + ServerConstants.KRYSTAL_DATABASEPASSWORD + "')");
				stat.execute(" CREATE SCHEMA "+ServerConstants.KRYSTAL_DATABASEOWNER+" ");
				databaseConnection.close();
				connectionString += ";user=" + ServerConstants.KRYSTAL_DATABASEOWNER + ";password=" + ServerConstants.KRYSTAL_DATABASEPASSWORD;
				databaseConnection = DriverManager.getConnection(connectionString);
				stat = databaseConnection.createStatement();
				stat.execute(" SET SCHEMA "+ServerConstants.KRYSTAL_DATABASEOWNER+" ");
				Statement stmt = databaseConnection.createStatement();
				for (int i = 1; i <= 14; i++) {
					String sqlStat = queryResource.getString("query" + i);
					stmt.execute(sqlStat);
				}
				databaseConnection.commit();
				databaseConnection.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void adjustDocumentCount(){
		try{
			for(DocumentClass documentClass: DocumentClassDAO.getInstance().readDocumentClasses("")){
				DocumentClassDAO.getInstance().adjustDocumentCounts(documentClass);
			}
		}catch(Exception e){
			kLogger.severe("Error adjusting document count  : " + e.getLocalizedMessage());
			e.printStackTrace(System.err);
		}
	}
	
	private void processExpiry(){
		try{
			TimerTask task  = new ExpiryProcessor();
			Timer timer = new Timer();
			Date scheduleDate = Calendar.getInstance().getTime();
			timer.scheduleAtFixedRate(task, scheduleDate, 24L * 60L * 60L * 1000L );
		}catch(Exception e){
			kLogger.severe("Error processing document expiry  : " + e.getLocalizedMessage());
			e.printStackTrace(System.err);
		}
	}
}