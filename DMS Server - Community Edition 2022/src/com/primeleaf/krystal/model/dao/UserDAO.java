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
package com.primeleaf.krystal.model.dao;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.MetaColumnPreferences;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.PasswordService;

/**
 * @author Rahul Kubadia
 * @since 2.0
 * @comments <br>Database related activities on User object is carried out in this class
 */

public class UserDAO {
	private boolean readCompleteObject=false;

	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	private String SQL_INSERT_USER="INSERT INTO USERS ( USERNAME, REALNAME, USERDESC, USEREMAIL, USERTYPE,PASSWORD,ACTIVE ) VALUES (?,?,?,?,?,?,?)";

	private String SQL_UPDATE_USER="UPDATE USERS SET USERNAME=?, PASSWORD=?, USERDESC=?, USEREMAIL=?, REALNAME = ?, HITLISTSIZE =?, CHECKOUTPATH=?, METACOLUMN=?, SHOWTHUMBNAIL=? , ACTIVE =?, USERTYPE=? WHERE USERID=?";
	private String SQL_UPDATE_USER_LOGIN="UPDATE USERS SET IPADDRESS=?, LASTLOGINDATE=? WHERE USERID=?";

	private String SQL_DELETE_USER="DELETE FROM USERS WHERE USERID=?";

	private String SQL_SELECT_USERID="SELECT USERID FROM USERS WHERE USERNAME=?";
	private String SQL_SELECT_USERSBYEMAIL="SELECT USERID FROM USERS WHERE USEREMAIL=?";
	private String SQL_SELECT_USERBYID="SELECT USERID,USERNAME,PASSWORD,USERDESC, USEREMAIL,REALNAME,LASTLOGINDATE,HITLISTSIZE,CHECKOUTPATH,SHOWTHUMBNAIL,METACOLUMN , ACTIVE,USERTYPE,LOGGEDIN  FROM USERS  WHERE USERID=?";
	private String SQL_SELECT_USERBYNAME="SELECT USERID,USERNAME,PASSWORD,USERDESC,USEREMAIL,REALNAME,LASTLOGINDATE,HITLISTSIZE,CHECKOUTPATH,SHOWTHUMBNAIL,METACOLUMN, ACTIVE,USERTYPE,LOGGEDIN  FROM USERS  WHERE USERNAME=?";
	private String SQL_SELECT_USERNAME="SELECT USERNAME FROM USERS WHERE USERNAME=?";
	private String SQL_SELECT_USERS="SELECT USERID,USERNAME,PASSWORD,USERDESC,USEREMAIL,REALNAME,IPADDRESS,LASTLOGINDATE,HITLISTSIZE,CHECKOUTPATH,SHOWTHUMBNAIL,METACOLUMN, ACTIVE,USERTYPE,LOGGEDIN FROM USERS";
	private String SQL_SELECT_FOR_AUTH="SELECT USERID ,PASSWORD FROM USERS WHERE USERNAME=?";

	private String SQL_UPDATE_LOGIN_STATUS = "UPDATE USERS SET LOGGEDIN=? WHERE USERID=?"; //New mechanism to check concurrent login  - 20 Feb 2013

	private String SQL_SELECT_USER="SELECT USERID,USERNAME,PASSWORD,USERDESC,USEREMAIL,REALNAME,ACTIVE,USERTYPE,HITLISTSIZE,CHECKOUTPATH,METACOLUMN,SHOWTHUMBNAIL,LASTLOGINDATE , LOGGEDIN  FROM USERS  WHERE USERNAME=? OR USEREMAIL=? ";

	private static UserDAO _instance; 

	/**
	 * Default constructor
	 */

	private UserDAO() {
		super();
	}

	public static synchronized UserDAO getInstance() { 
		if (_instance==null) { 
			_instance = new UserDAO(); 
		} 
		return _instance; 
	} 

	public boolean isReadCompleteObject() {
		return readCompleteObject;
	}

	public void setReadCompleteObject(boolean readCompleteObject) {
		this.readCompleteObject = readCompleteObject;
	}

	public void addUser(User user) throws Exception{

		kLogger.fine("Adding user :" + user.getUserName().toUpperCase());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_USER);

		int i=1;
		psInsert.setString(i++,user.getUserName().toUpperCase());
		psInsert.setString(i++,user.getRealName());
		psInsert.setString(i++,user.getUserDescription());
		psInsert.setString(i++,user.getUserEmail());
		psInsert.setString(i++,user.getUserType());
		psInsert.setString(i++, PasswordService.getInstance().encrypt(user.getPassword()));
		if(user.isActive()){
			psInsert.setString(i++,"Y");
		}else{
			psInsert.setString(i++,"N");
		}
		int recCount = psInsert.executeUpdate();
		psInsert.close();

		if(recCount > 0){
			PreparedStatement psSelectUserId = connection.prepareStatement(SQL_SELECT_USERID);
			psSelectUserId.setString(1,user.getUserName().toUpperCase());
			ResultSet rs = psSelectUserId.executeQuery();

			while(rs.next()){
				user.setUserId(rs.getShort(1));
			}

			rs.close();
			psSelectUserId.close();
			
		}
		connection.commit();
		connection.close();
	}

	public User readUserById(final int id) throws Exception {

		User result = null;

		kLogger.fine("Reading user : id=" + id);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_USERBYID);

		psSelect.setInt(1,id);

		ResultSet rs = psSelect.executeQuery();

		if(rs.next()){
			result = new User();
			result.setUserId(rs.getShort("USERID"));
			result.setUserName(rs.getString("USERNAME"));
			result.setPassword(rs.getString("PASSWORD"));
			result.setUserDescription(rs.getString("USERDESC"));
			result.setUserEmail(rs.getString("USEREMAIL"));
			result.setRealName(rs.getString("REALNAME"));
			result.setLastLoginDate(rs.getString("LASTLOGINDATE"));

			result.setHitlistSize(rs.getInt("HITLISTSIZE"));
			result.setCheckOutPath(rs.getString("CHECKOUTPATH"));
			result.setShowThumbNail(rs.getString("SHOWTHUMBNAIL"));
			result.setMetaPreferences(new MetaColumnPreferences(rs.getInt("METACOLUMN")));

			String active = rs.getString("ACTIVE");
			if("Y".equalsIgnoreCase(active)){
				result.setActive(true);
			}else{
				result.setActive(false);
			}

			result.setUserType(rs.getString("USERTYPE"));
			if("Y".equalsIgnoreCase(rs.getString("LOGGEDIN"))){
				result.setLoggedIn(true);
			}
		}	 		
		rs.close();
		psSelect.close();
		connection.close();

		return result;
	}

	public User readUserByName(final String userName) throws Exception {
		User result = null;
		if(ServerConstants.SYSTEM_USER.equalsIgnoreCase(userName)){
			result = new User();
			result.setUserId((short)0);
			result.setUserName(ServerConstants.SYSTEM_USER);
			result.setRealName("System");
		}else{
			kLogger.fine("Reading user : name=" + userName.toUpperCase());

			Connection connection = ConnectionPoolManager.getInstance().getConnection();

			PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_USERBYNAME);

			psSelect.setString(1,userName.toUpperCase());

			ResultSet rs = psSelect.executeQuery();

			if (rs.next()) {
				result = new User();
				result.setUserId(rs.getShort("USERID"));
				result.setUserName(rs.getString("USERNAME"));
				result.setPassword(rs.getString("PASSWORD"));
				result.setUserDescription(rs.getString("USERDESC"));
				result.setUserEmail(rs.getString("USEREMAIL"));
				result.setRealName(rs.getString("REALNAME"));
				result.setLastLoginDate(rs.getString("LASTLOGINDATE"));

				result.setHitlistSize(rs.getInt("HITLISTSIZE"));
				result.setCheckOutPath(rs.getString("CHECKOUTPATH"));
				result.setShowThumbNail(rs.getString("SHOWTHUMBNAIL"));
				result.setMetaPreferences(new MetaColumnPreferences(rs.getInt("METACOLUMN")));

				String active = rs.getString("ACTIVE");
				if("Y".equalsIgnoreCase(active)){
					result.setActive(true);
				}else{
					result.setActive(false);
				}

				result.setUserType(rs.getString("USERTYPE"));
				if("Y".equalsIgnoreCase(rs.getString("LOGGEDIN"))){
					result.setLoggedIn(true);
				}
				
			}else{
				result = new User();
				result.setRealName(userName);
				result.setUserName(userName);
			}
			rs.close();
			psSelect.close();
			//connection.commit();
			connection.close();
		}
		return result;
	}

	public boolean validateUser(String userName){

		boolean result=false;

		kLogger.fine("Validating user :" +userName.toUpperCase());
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();

			PreparedStatement psValidate = connection.prepareStatement(SQL_SELECT_USERNAME);

			psValidate.setString(1,userName.toUpperCase());

			ResultSet rs=psValidate.executeQuery();

			result = rs.next();

			rs.close();

			psValidate.close();

			connection.close();

		}catch(Exception e){
			kLogger.warning("Unable to validate user :" + userName);			
		}
		return result;
	}

	/**
	 * 
	 * @param user
	 * @param changePassword true for changing password and false for updating profile.
	 * @throws Exception
	 */
	public void updateUser(User user,boolean changePassword)throws Exception{

		kLogger.fine("Updating user :" +user.getUserName().toUpperCase());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_USER);

		int i=1;

		psUpdate.setString(i++,user.getUserName());
		
		if(changePassword){
			psUpdate.setString(i++,PasswordService.getInstance().encrypt(user.getPassword()));
		}else{
			psUpdate.setString(i++,user.getPassword());
		}

		psUpdate.setString(i++,user.getUserDescription());
		psUpdate.setString(i++,user.getUserEmail());
		psUpdate.setString(i++,user.getRealName());

		psUpdate.setInt(i++, user.getHitlistSize());
		psUpdate.setString(i++, user.getCheckOutPath());
		psUpdate.setInt(i++, user.getMetaPreferences().getMetaValue());
		psUpdate.setString(i++, user.getShowThumbNail());
		if(user.isActive()){
			psUpdate.setString(i++, "Y");
		}else{
			psUpdate.setString(i++, "N");
		}
		psUpdate.setString(i++, user.getUserType());
		psUpdate.setShort(i++,user.getUserId());

		int recCount = psUpdate.executeUpdate();

		psUpdate.close();
		psUpdate = null;

		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to update user : id=" + user.getUserId());
			throw (new Exception("Unable to update user : id=" + user.getUserId()));
		}

	}

	public void updateUserLoginHistory(User user)throws Exception{

		kLogger.fine("Updating login date time of user :" +user.getUserName().toUpperCase());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_USER_LOGIN);

		int i=1;

		Date lastLoginDate = new Date();

		psUpdate.setString(i++,user.getIpAddress());
		psUpdate.setTimestamp(i++,new java.sql.Timestamp(lastLoginDate.getTime())); 

		psUpdate.setShort(i++,user.getUserId());
		int recCount = psUpdate.executeUpdate();

		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to update user History : id=" + user.getUserId());
			throw (new Exception("Unable to update user History : id=" + user.getUserId()));
		}
	}

	public void updateLoginStatus(User user) throws Exception{
		kLogger.fine("Updating login status of user :" +user.getUserName().toUpperCase());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE_LOGIN_STATUS);

		int i=1;

		if(user.isLoggedIn()){
			psUpdate.setString(i++,"Y");
		}else{
			psUpdate.setString(i++,"N");
		}

		psUpdate.setShort(i++,user.getUserId());
		int recCount = psUpdate.executeUpdate();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to update login status of user : id=" + user.getUserId());
			throw (new Exception("Unable to update login status of user : id=" + user.getUserId()));
		}
	}

	public void deleteUser(final int id)throws Exception{

		kLogger.fine("Deleting user : id=" + id);

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_USER);

		psDelete.setInt(1,id);

		int recCount = psDelete.executeUpdate();

		psDelete.executeUpdate();

		psDelete.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to delete user : id=" + id);
			throw (new Exception("Unable to delete user : id=" + id));
		}

		User user = new User();
		user.setUserId((short)id); // Just ID parameter is required for the user object
	}

	public ArrayList<User> readUsers(final String criteria){
		ArrayList<User> result = new ArrayList<User>();

		kLogger.fine("Reading users for criteria :" + criteria);

		try{
			StringBuffer sbSelect = new StringBuffer();
			sbSelect.append(SQL_SELECT_USERS);

			if(criteria.length()>1){
				sbSelect.append(" WHERE " + criteria);
			}
			Connection connection = ConnectionPoolManager.getInstance().getConnection();

			PreparedStatement psSelect = connection.prepareStatement(sbSelect.toString());

			ResultSet rs = psSelect.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getShort("USERID"));
				user.setUserName(rs.getString("USERNAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setUserDescription(rs.getString("USERDESC"));
				user.setUserEmail(rs.getString("USEREMAIL"));
				user.setRealName(rs.getString("REALNAME"));

				user.setIpAddress(rs.getString("IPADDRESS"));
				user.setLastLoginDate(rs.getString("LASTLOGINDATE"));

				user.setHitlistSize(rs.getInt("HITLISTSIZE"));
				user.setCheckOutPath(rs.getString("CHECKOUTPATH"));
				user.setShowThumbNail(rs.getString("SHOWTHUMBNAIL"));
				user.setMetaPreferences(new MetaColumnPreferences(rs.getInt("METACOLUMN")));

				String active = rs.getString("ACTIVE");
				if("Y".equalsIgnoreCase(active)){
					user.setActive(true);
				}else{
					user.setActive(false);
				}
				user.setUserType(rs.getString("USERTYPE"));
				if("Y".equalsIgnoreCase(rs.getString("LOGGEDIN"))){
					user.setLoggedIn(true);
				}
				result.add(user);
			} 
			rs.close();
			psSelect.close();
			connection.close();
		}catch(Exception e){
			kLogger.warning("Unable to read users for criteria :" + criteria);
		}
		return result;
	}

	public boolean authenticate(User user){
		kLogger.fine("Authenticating user :name = " + user.getUserName());
		ResultSet res = null;
		boolean validUser = false;
		try {
			// Check if record exists
			Connection connection = ConnectionPoolManager.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement(SQL_SELECT_FOR_AUTH);
			ps.setString(1,user.getUserName().toUpperCase());
			res = ps.executeQuery();
			if (res.next()) {
				String password = res.getString(2);
				String userPassword = PasswordService.getInstance().encrypt(user.getPassword());
				if (password.equalsIgnoreCase(userPassword))
					validUser = true;
				else
					validUser = false;
			}

			res.close();
			ps.close();
			connection.close();

		}catch(Exception e){
			kLogger.warning("Unable to authenticate user="+user.getUserName());
		}
		return validUser;
	}

	public User readUser(final String loginId) throws Exception {
		User user = null;
		kLogger.fine("Reading user : login id = " + loginId);
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_USER);
		psSelect.setString(1,loginId.toUpperCase());
		psSelect.setString(2,loginId.toLowerCase());
		ResultSet rs = psSelect.executeQuery();
		if (rs.next()) {
			user = new User();
			user.setUserId(rs.getShort("USERID"));
			user.setUserName(rs.getString("USERNAME"));
			user.setPassword(rs.getString("PASSWORD"));
			user.setUserDescription(rs.getString("USERDESC"));
			user.setUserEmail(rs.getString("USEREMAIL"));
			user.setRealName(rs.getString("REALNAME"));
			if("Y".equalsIgnoreCase(rs.getString("ACTIVE"))){
				user.setActive(true);
			}
			user.setUserType(rs.getString("USERTYPE"));
			user.setHitlistSize(rs.getInt("HITLISTSIZE"));
			user.setCheckOutPath(rs.getString("CHECKOUTPATH"));
			user.setMetaPreferences(new MetaColumnPreferences(rs.getInt("METACOLUMN")));
			user.setShowThumbNail(rs.getString("SHOWTHUMBNAIL"));
			user.setLastLoginDate(rs.getString("LASTLOGINDATE"));

			if("Y".equalsIgnoreCase(rs.getString("LOGGEDIN"))){
				user.setLoggedIn(true);
			}
			
		}
		rs.close();
		psSelect.close();
		connection.close();
		return user;
	}
	public boolean validateUserEmail(String userEmail){

		boolean result=false;

		kLogger.fine("Validating user Email for user=" +userEmail);
		try{
			Connection connection = ConnectionPoolManager.getInstance().getConnection();

			PreparedStatement psValidate = connection.prepareStatement(SQL_SELECT_USERSBYEMAIL);

			psValidate.setString(1,userEmail);

			ResultSet rs=psValidate.executeQuery();

			result = rs.next();

			rs.close();

			psValidate.close();

			connection.close();

		}catch(Exception e){
			kLogger.warning("Unable to validate email:" + userEmail);			
		}
		return result;
	}

	/**
	 * Following methods are used for storage and retrieval of profile picture
	 * Added by Rahul Kubadia on 19-June-2014 
	 * */  

	public void setProfilePicture(User user) throws Exception{
		FileInputStream fis = null;
		
		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psUpdate = connection.prepareStatement("UPDATE USERS SET PROFILEPICTURE = ? WHERE USERID = ? ");
		//Added By Saumil Shah on NOV 2021
		//Sets profile pic to null if nothing is provided to generate pp
		if(user.getProfilePicture()!=null) {
			fis = new FileInputStream(user.getProfilePicture());
			psUpdate.setBinaryStream(1, fis,user.getProfilePicture().length());
		}else {
			psUpdate = connection.prepareStatement("UPDATE USERS SET PROFILEPICTURE = ? WHERE USERID = ? ");
			psUpdate.setNull(1, java.sql.Types.BLOB);
		}
		psUpdate.setInt(2,user.getUserId());
		psUpdate.executeUpdate();
		psUpdate.close();
		connection.commit();
		connection.close();
		try {
			fis.close();
		}catch (Exception e) {}
	}

	public BufferedImage getProfilePicture(String username) throws Exception{
		Connection connection = null;
		PreparedStatement  psSelect  = null;
		ResultSet rs = null;
		BufferedImage profilePicture = null;
		try {
			User user = UserDAO.getInstance().readUserByName(username.toUpperCase());
			profilePicture = new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB);
			Graphics2D ig2 = profilePicture.createGraphics();

			Color startColor = new Color (192,192,192);
			Color endColor = new Color (160,160,160);
			int startX = 0, startY = 0, endX = 96, endY = 96;
			GradientPaint gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);
			ig2.setPaint(gradient);	

			ig2.fillOval(0, 0, 96, 96);
			ig2.setPaint(Color.white);
			String firstName = user.getRealName().trim().charAt(0)+"";
			String lastName = "";
			if(user.getRealName().trim().indexOf(" ") > 0){
				lastName = user.getRealName().trim().substring( user.getRealName().trim().indexOf(" ") + 1);
				lastName = lastName.charAt(0)+"";
			}
			String name = firstName + lastName;
			ig2.setFont(ig2.getFont().deriveFont(Font.PLAIN,48f));
			FontMetrics fm = ig2.getFontMetrics();
			java.awt.geom.Rectangle2D rect = fm.getStringBounds(name, ig2);
			int textHeight = (int) (rect.getHeight());
			int textWidth = (int) (rect.getWidth());
			int cornerX = 48 - (textWidth / 2);
			int cornerY = 48 - (textHeight / 2) + fm.getAscent();
			ig2.drawString(name.toUpperCase(), cornerX, cornerY); 

			connection = ConnectionPoolManager.getInstance().getConnection();
			psSelect = connection.prepareStatement("SELECT PROFILEPICTURE FROM USERS WHERE USERNAME = ?");
			psSelect.setString(1, username);
			rs = psSelect.executeQuery();
			if(rs.next()){
				InputStream inputStream = rs.getBinaryStream(1);
				if(inputStream != null ){
					profilePicture = ImageIO.read(inputStream);
				}
			}
			//connection.commit();
		}catch (SQLException sqe) {
			kLogger.severe("Unable to read profile picture for username : "+ username );
			sqe.printStackTrace();
		}finally{
			try{
				if(rs != null){
					rs.close();	
				}
				if(psSelect != null){
					psSelect.close();	
				}
				if(connection != null){
					connection.close();
				}
			}catch(Exception ex){
				//We can not do anything about this
			}
		}
		return profilePicture;
	}

	public void updateUserPassword(User user){
		Connection connection=null;
		PreparedStatement psUpdate=null;
		ResultSet rs=null;

		try{

			connection = ConnectionPoolManager.getInstance().getConnection();

			psUpdate = connection.prepareStatement("UPDATE USERS SET PASSWORD =? , PASSWORDCHANGEDATE = ?  WHERE USERID=?");

			int i=1;

			Date passwordChangeDate = new Date();
			psUpdate.setString(i++,PasswordService.getInstance().encrypt(user.getPassword()));
			psUpdate.setTimestamp(i++,new java.sql.Timestamp(passwordChangeDate.getTime())); 

			psUpdate.setInt(i++,user.getUserId()); 

			psUpdate.executeUpdate();

			connection.commit();
		}catch(SQLException sqe){
			String message = "Unable to update user password for " +user.getUserName().toUpperCase();
			kLogger.severe(message);
		}finally{
			try{
				if(rs != null){
					rs.close();	
				}
				if(psUpdate != null){
					psUpdate.close();	
				}
				if(connection != null){
					connection.close();
				}
			}catch(Exception ex){
				//We can not do anything about this
			}
		}
	}
}
