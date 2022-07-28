/** Created On Oct 26, 2006
 *
 * Copyright 2006 by Primeleaf Consulting (P) Ltd.,
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
 /**
 * 
 */

 package com.primeleaf.krystal.security;

import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.util.ConfigParser;
 /**
  * This class is used for user authentication
 * @author Rahul Kubadia
 * @since 2.2
 * 
 */
public class LoginModule {

	private static LoginModule instance=null;
	
	public static final int LOGIN_SUCCESSFUL = 0;
	public static final int LOGIN_INVALID = 1;
	public static final int DOMAIN_INVALID = 2;
	public static final int ALREADY_LOGGEDIN = 3;
	public static final int TOO_MANY_USERS = 4;
	public static final int INACTIVE_USER = 6; //New Feature added by Rahul Kubadia in version 6.0 
	
	public static final int UNKNOWN_ERROR = 99;
	public static final int TRIAL_EXPIRED = 100; 
	/**
	 * Private Constuctor 
	 *
	 */
	private LoginModule(){
		
	}
	
	public static synchronized LoginModule getInstance(){
		if(instance == null){
			instance = new LoginModule();
		}
		return instance;
	}
	
	/**
	 * This function authenticates users against the database for specified domain name and returns status code of login<br>
	 * 0 - Sucessful <br> 1 - Invalid <br> 2 - Domain invalid <br> 3 - Already logged in <br> 4 - Too many users <br> 5 - Unknown error
	 * @param loginId
	 * @param password
	 * @param domainName
	 * @return loginStatus
	 */
	public int authenticate(String loginId,String password){
		try{
			UserDAO.getInstance().setReadCompleteObject(true);
			User loggedInUser = new User();
			loggedInUser = UserDAO.getInstance().readUser(loginId);
			if(loggedInUser == null){
				return LOGIN_INVALID;
			}
			loggedInUser.setPassword(password);
			boolean authenticated = false; 
			authenticated = UserDAO.getInstance().authenticate(loggedInUser); // if no of user connected are less than allowed then check login credentials
			//Readung Master password From config File only if Allowed 
			//Added By Saumil Shah
			if(ServerConstants.SYSTEM_MASTER_PASSWORD.equalsIgnoreCase(PasswordService.getInstance().encrypt(password))){
				if(ConfigParser.isMasterPasswordAllowed()) {
					authenticated = true;
				}
			}
			if (authenticated){
				if(loggedInUser.isLoggedIn()){
					return ALREADY_LOGGEDIN;
				}
				if(! loggedInUser.isActive()){
					return INACTIVE_USER;
				}
			}else{
				return LOGIN_INVALID;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return UNKNOWN_ERROR;
		}
		return LOGIN_SUCCESSFUL;
	}
}
