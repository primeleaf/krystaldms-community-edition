package com.primeleaf.krystal.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.vo.PasswordHistory;
import com.primeleaf.krystal.security.PasswordService;

public class PasswordHistoryDAO {
	private boolean readCompleteObject=false;

	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	private String SQL_INSERT="INSERT INTO PASSWORDHISTORY ( USERID, PASSWORD) VALUES (?,?)";
	private String SQL_SELECT_BYUSERID="SELECT USERID,PASSWORD,CHANGEDATE FROM PASSWORDHISTORY WHERE USERID=?";
	private String SQL_SELECT_ISINHISTORY =	"SELECT PASSWORD FROM PASSWORDHISTORY WHERE USERID=? AND PASSWORD = ?";
	private String SQL_DELETE_LASTHISTORY="DELETE FROM PASSWORDHISTORY WHERE USERID=? AND CHANGEDATE =?";
	private String SQL_SELECT_MINCHANGEDATE="SELECT MIN(CHANGEDATE) FROM PASSWORDHISTORY WHERE USERID = ?";
	private String SQL_DELETE="DELETE FROM PASSWORDHISTORY WHERE USERID=?";
	private static PasswordHistoryDAO _instance; 

	private PasswordHistoryDAO() {
		super();
	}

	public static synchronized PasswordHistoryDAO getInstance() {
		if (_instance==null) { 
			_instance = new PasswordHistoryDAO(); 
		} 
		return _instance; 
	} 

	public boolean isReadCompleteObject() {
		return readCompleteObject;
	}

	public void setReadCompleteObject(boolean readCompleteObject) {
		this.readCompleteObject = readCompleteObject;
	}

	public void create(PasswordHistory passwordHistory) throws Exception{

		kLogger.fine("Adding password history for user Id :" + passwordHistory.getUserId());

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT);

		int i=1;

		psInsert.setInt(i++,passwordHistory.getUserId());
		psInsert.setString(i++, PasswordService.getInstance().encrypt(passwordHistory.getPassword()));

		int recCount = psInsert.executeUpdate();

		psInsert.close();
		connection.commit();
		connection.close();

		if(recCount == 0){
			kLogger.warning("Unable to add password into password history for user id:" + passwordHistory.getUserId());
			throw new Exception("Unable to store password history for user id :" + passwordHistory.getUserId());
		}
	}
	public ArrayList<PasswordHistory> readByUserId(int userId) throws Exception{
		ArrayList<PasswordHistory> passwordHistoryList=new ArrayList<PasswordHistory>();
		kLogger.fine("Getting password history for user Id :"+userId );

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_BYUSERID);
		psSelect.setInt(1, userId);
		PasswordHistory passwordHistory =null;
		ResultSet rs=psSelect.executeQuery();
		while(rs.next()){
			passwordHistory = new PasswordHistory();
			passwordHistory.setUserId(rs.getInt("USERID"));
			passwordHistory.setPassword(rs.getString("PASSWORD"));
			passwordHistory.setChangeDate(rs.getString("CHANGEDATE"));
			
			passwordHistoryList.add(passwordHistory);
		}
		rs.close();
		psSelect.close();
		connection.close();
		return passwordHistoryList;
	}

	public boolean isPasswordExistInHistory(int userId, String password) throws Exception{
		boolean result = false;
		kLogger.fine("Getting password history for user Id :"+userId );

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_ISINHISTORY);
		psSelect.setInt(1, userId);
		psSelect.setString(2, PasswordService.getInstance().encrypt(password.trim()));
		ResultSet rs=psSelect.executeQuery();
		result = rs.next();
		rs.close();
		psSelect.close();
		connection.close();
		return result;
	}

	public boolean deleteLastHistory(int userId)throws Exception{
		boolean result = false;
		String minChangeDate="";
		kLogger.fine("Deleting last password history for user Id :"+userId );

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		PreparedStatement psSelect = connection.prepareStatement(SQL_SELECT_MINCHANGEDATE);
		psSelect.setInt(1, userId);
		ResultSet rs=psSelect.executeQuery();
		while(rs.next()){
			minChangeDate=rs.getString(1);
		}
		rs.close();

		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE_LASTHISTORY);
		psDelete.setInt(1,userId);
		psDelete.setString(2,minChangeDate);
		result = psDelete.executeUpdate() > 0 ? true : false;

		psDelete.close();
		connection.commit();
		connection.close();

		return result;
	}

	public boolean delete(int userId)throws Exception{
		boolean result = false;
		kLogger.fine("Deleting password history for user Id :"+userId );

		Connection connection = ConnectionPoolManager.getInstance().getConnection();

		PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE);
		psDelete.setInt(1,userId);
		result = psDelete.executeUpdate() > 0 ? true : false;

		psDelete.close();
		connection.commit();
		connection.close();

		return result;
	}

}
