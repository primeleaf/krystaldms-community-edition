/**
 * Created on Aug 9, 2004
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

package com.primeleaf.krystal.web;


import java.util.logging.Logger;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.tomcat.util.scan.StandardJarScanner;

import com.primeleaf.krystal.constants.ServerConstants;

/**
 * This class starts the embedded tomcat server and loads the web apps depending upon the configuration settings.
 * @author Rahul Kubadia
 * @since 2.0
 *  
 */

public class WebServerManager extends Thread{

	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	private Tomcat embeddedTomcat = null;

	private boolean isRunning = false;

	public WebServerManager(int port,String appBase) {
		try {
			System.setProperty("java.net.preferIPv4Stack", "true");
			System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");

			Connector dmsConnector = new Connector();
			dmsConnector.setPort(port);
			dmsConnector.setURIEncoding("UTF-8");
			dmsConnector.setProperty("server","KRYSTAL DMS - Community Edition");

			
			embeddedTomcat = new Tomcat();
			embeddedTomcat.setBaseDir(appBase);

			Service service = embeddedTomcat.getService();
			service.addConnector(dmsConnector);

			embeddedTomcat.setConnector(dmsConnector);
			embeddedTomcat.enableNaming();
			embeddedTomcat.getConnector().setRedirectPort(port);
			embeddedTomcat.getConnector().setURIEncoding("UTF-8");
			embeddedTomcat.getEngine().setName("krystal");
			
			StandardContext edmcContext = (StandardContext) embeddedTomcat.addWebapp("", ServerConstants.KRYSTAL_WEB_HOME);

			StandardJarScanner jarScanner = new StandardJarScanner();
			jarScanner.setScanClassPath(false);
			jarScanner.setScanManifest(false);
			edmcContext.setJarScanner(jarScanner);

			edmcContext.setUseHttpOnly(false);
			AccessLogValve alv = new AccessLogValve();
			alv.setDirectory(appBase+"/log");
			alv.setPrefix("krystal_dms_access");
			alv.setPattern("%h %l %u %t %r %s %b"); 
			alv.setSuffix(".log");
			alv.setEnabled(true);
			edmcContext.addValve(alv);
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					if(isRunning) {
						kLogger.info("Stopping the Tomcat server, through shutdown hook");  
						try { 
							if (embeddedTomcat != null) {
								embeddedTomcat.stop();
							}
						} catch (LifecycleException e) {
							kLogger.severe("Error while stopping the Tomcat server, through shutdown hook" + e.getLocalizedMessage()); 
							e.printStackTrace();
						}                     
					}      
				}        
			});        

		} catch (Exception e) {
			kLogger.severe("Could not start TOMCAT server "+ e.getLocalizedMessage());
		}
	}
		public void run(){
			try{
				if(isRunning){
					kLogger.severe("Server already running");
					return;
				}
				embeddedTomcat.start();
				embeddedTomcat.getServer().await();
				isRunning=true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		/**
		 * This method Stops the Tomcat server.
		 */
		public void stopTomcat() throws LifecycleException {
			if(!isRunning){
				kLogger.severe("Tomcat server is not running");
				return;
			}
			// Stop the embedded server
			embeddedTomcat.stop();
			isRunning=false;
		}
	}