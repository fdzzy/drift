package com.drift.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.drift.bean.Bottle;
import com.drift.bean.User;
import com.drift.dao.BottleDao;

public class JdbcBottleDao implements BottleDao {
	
	@Override
	public Bottle findBottleById(int bottleId) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		Bottle bottle = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select SENDER, RECEIVER, SENDTIME, CONTENT from bottles where ID=?";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, bottleId);
			rs = prepStmt.executeQuery();
			if(rs.next()) {
				int senderId = rs.getInt("SENDER");
				int receiverId = rs.getInt("RECEIVER");
				Timestamp ts = rs.getTimestamp("SENDTIME");
				String content = rs.getString("CONTENT");
				bottle = new Bottle(bottleId, senderId, receiverId, ts.getTime(), content);				
			} else {
				throw new SQLException();
			}
		} finally {
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		
		return bottle;
	}
	
	/* TODO: This function may need to be synchronized */
	@Override
	public Bottle getBottle(User user) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		PreparedStatement updatePrepStmt = null;
		ResultSet rs = null;
		Bottle bottle = null;
		
		try {
			connection = JdbcConnection.getConnection();
			int sex = user.getSex().ordinal();
			sex = sex ^ 1;
			//String selectStatement = "select users.name, bottles.id, bottles.sender, bottles.content from bottles, users "
			//		+ "where bottles.sender=users.id and users.sex=? and bottles.receiver=0 order by rand() limit 1";
			String selectStatement = "select bottles.id, bottles.sender, bottles.sendtime, bottles.content from bottles, users "
					+ "where bottles.sender=users.id and users.sex=? and bottles.receiver=0 order by rand() limit 1";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, sex);
			rs = prepStmt.executeQuery();
			if(rs.next()) {
				int bottleId = rs.getInt(1);
				int senderId= rs.getInt(2);
				Timestamp ts = rs.getTimestamp(3);
				String content = rs.getString(4);
				String updateStatement = "UPDATE `bottles` SET `receiver`=? WHERE `ID`=?"; 
				updatePrepStmt = connection.prepareStatement(updateStatement);
				updatePrepStmt.setInt(1, user.getUid());
				updatePrepStmt.setInt(2, bottleId);
				if(updatePrepStmt.executeUpdate() != 0) {
					bottle = new Bottle(bottleId, senderId, user.getUid(), ts.getTime(), content);
				} else {
					// SQL error
					throw new SQLException();
				}
			} else {
				// No bottles
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		} finally {
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		
		return bottle;
	}
	
	@Override
	public void setBottleUnread(int bottleId) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String updateStatement = "UPDATE `bottles` SET `receiver`=0 WHERE `ID`=?"; 
			prepStmt = connection.prepareStatement(updateStatement);
			prepStmt.setInt(1, bottleId);
			if(prepStmt.executeUpdate() == 0) {
				throw new SQLException();
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		} finally {
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
	}
	
//	@Override
//	public void setBottleReceiver(int bottleId, User user) throws Exception {
//		Connection connection = null;
//		PreparedStatement prepStmt = null;
//		ResultSet rs = null;
//		
//		try {
//			connection = JdbcConnection.getConnection();
//			String selectStatement = "select sender, sendtime, content from bottles where ID=?";
//			prepStmt = connection.prepareStatement(selectStatement);
//			prepStmt.setInt(1, bottleId);
//			rs = prepStmt.executeQuery();
//			if(rs.next()) {
//				
//			}
//		} finally {
//			JdbcConnection.closeResultSet(rs);
//			JdbcConnection.closePrepStmt(prepStmt);
//			JdbcConnection.closeConnection(connection);
//		}
//	}

	@Override
	public void postBottle(User user, String content) throws Exception {	
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String insertStatement = "insert into bottles (SENDER, CONTENT, SENDTIME) values (?,?, NOW())";
			prepStmt = connection.prepareStatement(insertStatement);
			prepStmt.setInt(1, user.getUid());
			prepStmt.setString(2, content);
			if(prepStmt.executeUpdate() == 0) {
				throw new SQLException();
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		} finally {
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
	}

}
