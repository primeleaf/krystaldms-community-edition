/**
 * Created on Sep 20, 2005
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
package com.primeleaf.krystal.model.vo;

import java.io.File;

/**
 * @author Rahul Kubadia
 * @since 2.0
 * @comments User value object
 * @see com.primeleaf.krystal.model.vo.Role
 * @see com.primeleaf.krystal.model.vo.Group
 * 
 */
public class User {

	private short userId;
	private String userName;
	private String password;
	private String userEmail;
	private String realName;
	private String userDescription;
	private boolean active = false;
	private String userType;
	private String ipAddress;
	private String lastLoginDate;
	private boolean loggedIn = false;
	
	private File profilePicture;//Added on 15th June 2014 by Rahul Kubadia
	
	/**
	 * Moved from User Preferences to Users
	 * @author Rahul Kubadia
	 * @since 3.5
	 * @date Jun 20, 2011
	 */
	
	private String checkOutPath;
	private int hitlistSize;
	private MetaColumnPreferences metaPreferences;
	private String showThumbNail;
	
	/**
	 * @return Returns the ipAddress.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress The ipAddress to set.
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return Returns the lastLoginDate.
	 */
	public String getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * @param lastLoginDate The lastLoginDate to set.
	 */
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public short getUserId() {
		return userId;
	}

	public void setUserId(short userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isAdmin() {
		if(getUserType().equalsIgnoreCase("A")){
			return true;
		}
		return false;
	}
	
	/**
	 * This is default constructor for User Value Object
	 */
	public User() {
		super();
	}

	/**
	 * @return the checkOutPath
	 */
	public String getCheckOutPath() {
		return checkOutPath;
	}

	/**
	 * @param checkOutPath the checkOutPath to set
	 */
	public void setCheckOutPath(String checkOutPath) {
		this.checkOutPath = checkOutPath;
	}

	/**
	 * @return the hitlistSize
	 */
	public int getHitlistSize() {
		return hitlistSize;
	}

	/**
	 * @param hitlistSize the hitlistSize to set
	 */
	public void setHitlistSize(int hitlistSize) {
		this.hitlistSize = hitlistSize;
	}

	/**
	 * @return the metaPreferences
	 */
	public MetaColumnPreferences getMetaPreferences() {
		return metaPreferences;
	}

	/**
	 * @param metaPreferences the metaPreferences to set
	 */
	public void setMetaPreferences(MetaColumnPreferences metaPreferences) {
		this.metaPreferences = metaPreferences;
	}

	/**
	 * @return the showThumbNail
	 */
	public String getShowThumbNail() {
		return showThumbNail;
	}

	/**
	 * @param showThumbNail the showThumbNail to set
	 */
	public void setShowThumbNail(String showThumbNail) {
		this.showThumbNail = showThumbNail;
	}

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn the loggedIn to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public File getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(File profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	public static final String USER_TYPE_ADMIN="A";
	public static final String USER_TYPE_USER="U";
	
}
