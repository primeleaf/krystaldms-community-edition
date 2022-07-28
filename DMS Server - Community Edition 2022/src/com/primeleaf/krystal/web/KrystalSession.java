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

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.User;

/**
 * @author Rahul Kubadia
 * @since 1.0
 */

/**
 * Krystal user session <BR>
 * Details of logged in user along with exception occurred during the current session are stored here.
 */
public class KrystalSession implements HttpSessionBindingListener {
	private static final Logger kLogger = Logger.getLogger(KrystalSession.class.getName());

	private User krystalUser ;

	public User getKrystalUser() {
		return krystalUser;
	}

	public void setKrystalUser(User krystalUser) {
		this.krystalUser = krystalUser;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		String eName = event.getName();
		int loggedInUserCount =  0;
		if (HTTPConstants.SESSION_KRYSTAL.equalsIgnoreCase(eName)) {
			try{
				krystalUser.setLoggedIn(true);
				UserDAO.getInstance().updateLoginStatus(krystalUser);
				loggedInUserCount = UserDAO.getInstance().readUsers(" LOGGEDIN = 'Y'").size();
			}catch(Exception e){
				e.printStackTrace(System.err);
			}
			kLogger.info(krystalUser.getUserName().toUpperCase() + " logged in");
			kLogger.info("Total User(s) connected :"+ loggedInUserCount);
		}
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		String eName = event.getName();
		int loggedInUserCount =  0;
		if (HTTPConstants.SESSION_KRYSTAL.equalsIgnoreCase(eName)) {
			krystalUser.setLoggedIn(false);
			try {
				UserDAO.getInstance().updateLoginStatus(krystalUser);
				loggedInUserCount = UserDAO.getInstance().readUsers(" LOGGEDIN = 'Y'").size();
				kLogger.info(krystalUser.getUserName().toUpperCase() + " logged out");
				kLogger.info("Total User(s) connected :"+ loggedInUserCount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}