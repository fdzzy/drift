package com.drift.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.drift.bean.User;
import com.drift.dao.UserDao;
/**
 * JDBC User DAO
 *
 */
public class JdbcUserDao implements UserDao {

	@Override
	public User findUserById(int uid) throws Exception {		
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		User user = null;
		
		if(uid <= 0) {
			return null;
		}
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select NAME, NICKNAME, SEX, SCHOOL, DEPARTMENT, MAJOR, EMAIL" 
					+ ", BIRTHDAY, ENROLLYEAR, REGISTER_TIME, PHOTO from users where ID=?";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, uid);
			rs = prepStmt.executeQuery();

			if (rs.next()) {
				String name = rs.getString("NAME");
				String nickname = rs.getString("NICKNAME");
				int sex = rs.getInt("SEX");
				String school = rs.getString("SCHOOL");
				String department = rs.getString("DEPARTMENT");
				String major = rs.getString("MAJOR");
				String email = rs.getString("EMAIL");
				String birthday = rs.getString("BIRTHDAY");
				int enrollYear = rs.getInt("ENROLLYEAR");
				java.sql.Timestamp ts = rs.getTimestamp("REGISTER_TIME");
				String photoUrl = rs.getString("PHOTO");				
				
				user = new User(uid, name, nickname, sex, school, department, 
						major, email, birthday, enrollYear, ts.getTime(), photoUrl);
			} else {
				//username does not exist in DB
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		return user;
	}

	@Override
	public User findUserByName(String username) throws Exception {		
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		User user = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select ID from users where NAME=? AND TYPE=0";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setString(1, username);
			rs = prepStmt.executeQuery();

			if (rs.next()) {
				int uid = rs.getInt("ID");				
				user = findUserById(uid);
			} else {
				// username doe not exist
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		return user;
	}
	
	@Override
	public User findUserByEmail(String email) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		User user = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select ID from users where EMAIL=?";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setString(1, email);
			rs = prepStmt.executeQuery();

			if (rs.next()) {
				int uid = rs.getInt("ID");				
				user = findUserById(uid);
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		return user;
	}
	
	//TODO: Do not use clear text password
	@Override
	public boolean verifyPassword(User user, String password) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select PASSWORD from users where ID=? AND TYPE=0";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, user.getUid());
			rs = prepStmt.executeQuery();

			if (rs.next()) {
				String dbPassword = rs.getString("PASSWORD");
				result = dbPassword.endsWith(password);
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		return result;
	}

	@Override
	public boolean isActivated(User user) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		boolean activated = false;
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select ACTIVATED from users where ID=?";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, user.getUid());
			rs = prepStmt.executeQuery();

			if (rs.next()) {
				activated = rs.getBoolean("ACTIVATED");
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		return activated;
	}
	
	@Override
	public boolean setActivate(User user) throws Exception {
		Connection connection = null;
		PreparedStatement updatePrepStmt = null;
		ResultSet rs = null;
		boolean activated = false;
		
		try {
			connection = JdbcConnection.getConnection();
			String updateStatement = "UPDATE `users` SET `ACTIVATED`=1 WHERE `ID`=?";
			updatePrepStmt = connection.prepareStatement(updateStatement);
			updatePrepStmt.setInt(1, user.getUid());
			if(updatePrepStmt.executeUpdate() == 0) {
				throw new SQLException();
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(updatePrepStmt);
			JdbcConnection.closeConnection(connection);
		}
		return activated;
	}
	
	@Override
	public User findForeignUser(int type, String foreignUid) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		User user = null;
		
		if(type < 0 || foreignUid == null || foreignUid.isEmpty()) {
			return null;
		}

		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select ID from users where TYPE=? and F_UID=?";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, type);
			prepStmt.setString(2, foreignUid);
			rs = prepStmt.executeQuery();

			if (rs.next()) {
				int uid = rs.getInt(1);
				user = findUserById(uid);
			}
		//} catch (Exception e) {
		//	e.printStackTrace(); 
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		return user;
	}

	@Override
	public void save(User user, String password) throws Exception {		
		Connection connection = null;
		PreparedStatement prepStmt = null;
		PreparedStatement insertPrepStmt = null;
		ResultSet rs = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String insertStatement = "insert into users (NAME, NICKNAME, PASSWORD, SEX, BIRTHDAY, " + 
					"SCHOOL, DEPARTMENT, MAJOR, ENROLLYEAR, EMAIL, REGISTER_TIME) values " + 
					"(?,?,?,?,?,?,?,?,?,?,NOW())";
			insertPrepStmt = connection.prepareStatement(insertStatement);
			insertPrepStmt.setString(1, user.getUsername());
			insertPrepStmt.setString(2, user.getNickname());
			insertPrepStmt.setString(3, password);
			insertPrepStmt.setInt(4, user.getSex().ordinal());
			insertPrepStmt.setString(5, user.getBirthday());
			insertPrepStmt.setString(6, user.getSchool());
			insertPrepStmt.setString(7, user.getDepartment());
			insertPrepStmt.setString(8, user.getMajor());
			insertPrepStmt.setInt(9, user.getEnrollYear());
			insertPrepStmt.setString(10, user.getEmail());
			
			if(insertPrepStmt.executeUpdate() == 0) {
				throw new SQLException();
			}
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
	}
	
	@Override
	public void saveForeign(User user, int siteType, String foreignUid) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		PreparedStatement insertPrepStmt = null;
		ResultSet rs = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String insertStatement = "insert into users (NAME, NICKNAME, SEX, BIRTHDAY, " + 
					"SCHOOL, DEPARTMENT, MAJOR, ENROLLYEAR, EMAIL, REGISTER_TIME, " + 
					"TYPE, F_UID) values " + 
					"(?,?,?,?,?,?,?,?,?,NOW(),?,?)";
			insertPrepStmt = connection.prepareStatement(insertStatement);
			insertPrepStmt.setString(1, user.getUsername());
			insertPrepStmt.setString(2, user.getNickname());
			insertPrepStmt.setInt(3, user.getSex().ordinal());
			insertPrepStmt.setString(4, user.getBirthday());
			insertPrepStmt.setString(5, user.getSchool());
			insertPrepStmt.setString(6, user.getDepartment());
			insertPrepStmt.setString(7, user.getMajor());
			insertPrepStmt.setInt(8, user.getEnrollYear());
			insertPrepStmt.setString(9, user.getEmail());
			insertPrepStmt.setInt(10, siteType);
			insertPrepStmt.setString(11, foreignUid);
			
			if(insertPrepStmt.executeUpdate() == 0) {
				throw new SQLException();
			}
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
	}

	@Override
	public void update(User user) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String updateStatement = "UPDATE `users` SET `BIRTHDAY`=?,`SCHOOL`=?,`DEPARTMENT`=?," +
					"`MAJOR`=?,`NICKNAME`=?,`ENROLLYEAR`=?, `PHOTO`=? WHERE `ID`=?";
			prepStmt = connection.prepareStatement(updateStatement);
			prepStmt.setString(1, user.getBirthday());
			prepStmt.setString(2, user.getSchool());
			prepStmt.setString(3, user.getDepartment());
			prepStmt.setString(4, user.getMajor());
			prepStmt.setString(5, user.getNickname());
			prepStmt.setInt(6, user.getEnrollYear());
			prepStmt.setString(7, user.getPhotoUrl());
			prepStmt.setInt(8, user.getUid());
			
			if(prepStmt.executeUpdate() == 0) {
				throw new SQLException();
			}
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}		
	}



}
