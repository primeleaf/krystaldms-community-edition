/**
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
 */
package com.primeleaf.krystal;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

import com.primeleaf.krystal.dms.DMSServer;
import com.primeleaf.krystal.util.ConfigParser;


/**
 * @author Rahul Kubadia
 * <br>Krystal Server execution starts here. Server execution is controlled 
 * by this class.
 */
public class KRYSTALServer {
	
	private static final String RUNMODE_STOP = "STOP";
	/**
	 * @param args
	 * <br>This is the main method from where the Krystal DMS starts. If
	 * argument is passed then it enables UI. If RUN_MODE is STOP
	 * then Stop the server
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase(RUNMODE_STOP)) {
				stop();
				return;
			}
		}
		start();
		/**
		 * Keep the program running till the time shutdown is called.
		 */
		try{
			while(!testShutdown()){
				Thread.sleep(5000);
			}
			Thread.sleep(10000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void start(){
		ConfigParser.init();//Added By Saumil Shah , To Read Config file Before Satrtup
		
		DMSServer.getInstance();
	}
	public static void stop() {
		String	KRYSTAL_HOME = System.getProperty("krystal.home");
		if (KRYSTAL_HOME == null){
			KRYSTAL_HOME = System.getProperty("user.dir");
		}
		try {
			FileOutputStream fos = new FileOutputStream(KRYSTAL_HOME + "/shutdown");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean testShutdown(){
		String	KRYSTAL_HOME = System.getProperty("krystal.home");
		boolean shutdown=false;
		if (KRYSTAL_HOME == null){
			KRYSTAL_HOME = System.getProperty("user.dir");
		}
		try {
			File f = new File(KRYSTAL_HOME + "/shutdown");
			if(f.exists()){
				shutdown=true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error:Cannot Shutdown Server");
		}
		return shutdown;
	}
}
