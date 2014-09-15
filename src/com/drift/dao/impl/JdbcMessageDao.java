package com.drift.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.drift.bean.ChatMessage;
import com.drift.bean.NotificationMessage;
import com.drift.bean.User;
import com.drift.dao.MessageDao;
import com.drift.dao.UserDao;

public class JdbcMessageDao implements MessageDao {
	UserDao userDao = new JdbcUserDao();
	
	@Override
	public void save(ChatMessage message) throws Exception {
		Connection connection = null;
		PreparedStatement insertPrepStmt = null;
		ResultSet rs = null;
		
		try {
			connection = JdbcConnection.getConnection();
			String insertStatement = "insert into messages (senderID, receiverID, sendTime, readFlag, content)" 
					+ " values (?,?,?,0,?)";
			//System.out.println(insertStatement);
			insertPrepStmt = connection.prepareStatement(insertStatement);
			insertPrepStmt.setInt(1, message.getSenderId());
			insertPrepStmt.setInt(2, message.getReceiverId());
			long ts = message.getTs();
			if(ts == 0) {
				insertPrepStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			} else {
				insertPrepStmt.setTimestamp(3, new Timestamp(ts));
			}
			insertPrepStmt.setString(4, message.getContent());
			//System.out.println(senderId + " " + uid + " " + ts + " " + content);
			if(insertPrepStmt.executeUpdate() == 0) {
				throw new SQLException();
			}			
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(insertPrepStmt);
			JdbcConnection.closeConnection(connection);
		}
	}
	
	@Override
	public List<ChatMessage> getMyMessages(User user, int start, int count) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		
		try {
			connection = JdbcConnection.getConnection();
			/*String selectStatement = "select ID, senderID, sendTime, content from messages where ID in " + 
			"(select max(ID) from messages where receiverID=" + uid + " group by senderID)"
			+ " order by sendTime DESC limit " + start + "," + length;*/
			/*
			 * select ID, senderID, receiverID, sendTime, content from messages where ID in (select max(ID) from (select * from (select ID, senderID as friend, sendTime from messages where receiverID=15) as a UNION (select ID, receiverID as friend, sendTime from messages where senderID=15)) as b group by friend) order by sendTime DESC limit 0, 30;
			 */
			String selectStatement = "select ID, senderID, receiverID, sendTime, content from messages " + 
					"where ID in (select max(ID) from (select * from (select ID, senderID as friend, sendTime " + 
					"from messages where receiverID=?) as a UNION (select ID, receiverID as friend, sendTime " + 
					"from messages where senderID=?)) as b group by friend) order by sendTime DESC limit ?, ?";
				prepStmt = connection.prepareStatement(selectStatement);
				prepStmt.setInt(1, user.getUid());
				prepStmt.setInt(2, user.getUid());
				prepStmt.setInt(3, start);
				prepStmt.setInt(4, count);
			rs = prepStmt.executeQuery();

			while (rs.next()) {
				int messageId = rs.getInt("ID");
				int senderId = rs.getInt("senderID");
				int receiverId = rs.getInt("receiverID");
				Timestamp ts = rs.getTimestamp("sendTime");
				String content = rs.getString("content");
								
				ChatMessage message = new ChatMessage(messageId, senderId, receiverId, ts.getTime(), content);
				messages.add(message);
			}
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		
		return messages;
	}

	// TODO: may need to be put into a transaction
	@Override
	public List<ChatMessage> readAndFlagConversation(int userId, int friendId) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		PreparedStatement updatePrepStmt = null;
		ResultSet rs = null;
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select ID, senderID, receiverID, sendTime, content  " + 
					" from messages where (receiverID=? and senderID=?) or (receiverID=? and " + 
					"senderID=?) order by sendTime ASC, ID ASC";// limit 0, 30";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, userId);
			prepStmt.setInt(2, friendId);
			prepStmt.setInt(3, friendId);
			prepStmt.setInt(4, userId);
			rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				int messageId = rs.getInt("ID");
				int senderId = rs.getInt("senderID");
				int receiverId = rs.getInt("receiverID");
				Timestamp ts = rs.getTimestamp("sendTime");
				String content = rs.getString("content");
								
				ChatMessage message = new ChatMessage(messageId, senderId, receiverId,
						ts.getTime(), content);
				messages.add(message);
			}
			if(!messages.isEmpty()) {
				// set readFlag to 1
				String updateStatement = "UPDATE `messages` SET `readFlag`=1 WHERE `receiverID`=?"; 
				updatePrepStmt = connection.prepareStatement(updateStatement);
				updatePrepStmt.setInt(1, userId);
				if(updatePrepStmt.executeUpdate() == 0) {
					throw new SQLException();
				}
			}
		} finally {
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(updatePrepStmt);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		
		return messages;
	}

	@Override
	public List<ChatMessage> readAndFlagUnreadFromFriend(int userId,
			int friendId) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		PreparedStatement updatePrepStmt = null;
		ResultSet rs = null;
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select ID, senderID, receiverID, sendTime, content " + 
					" from messages where receiverID=? and senderID=? and readFlag=0" + 
					" order by sendTime ASC, ID ASC";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, userId);
			prepStmt.setInt(2, friendId);
			rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				int messageId = rs.getInt("ID");
				int senderId = rs.getInt("senderID");
				int receiverId = rs.getInt("receiverID");
				Timestamp ts = rs.getTimestamp("sendTime");
				String content = rs.getString("content");				
				System.out.println(messageId + ", " + senderId + ", " + receiverId + ", " + content);
				
				String updateStatement = "UPDATE `messages` SET `readFlag`=1 WHERE `ID`=?"; 
				prepStmt = connection.prepareStatement(updateStatement);
				prepStmt.setInt(1, messageId);
				if(prepStmt.executeUpdate() != 0) {
					ChatMessage message = new ChatMessage(messageId, senderId, receiverId,
							ts.getTime(), content);
					messages.add(message);
				} else {
					//TODO: resource leak ???
					//throw new SQLException();
				}
			}			
		} finally {
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(updatePrepStmt);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		
		return messages;
	}

	@Override
	public int getNewMessageCount(int userId) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt = null;
		ResultSet rs = null;
		
		int count = 0;
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement = "select COUNT(ID) from messages where receiverID=? and readFlag=0";
			prepStmt = connection.prepareStatement(selectStatement);
			prepStmt.setInt(1, userId);
			rs = prepStmt.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt(1);
			} else {
				throw new SQLException();
			}
		} finally {
			JdbcConnection.closeResultSet(rs);
			JdbcConnection.closePrepStmt(prepStmt);
			JdbcConnection.closeConnection(connection);
		}
		
		return count;
	}

	/* TODO: this may not be good */
	/*
	 *  getNotification
	 *  
	 *  get unread messages, containing the a list of following information:
	 *  	friendId: the user id from which the unread message belongs to
	 *  	ts: the newest timestamp
	 *  	content: the last message body
	 *  	unreadCount: number of unread messages from him/her
	 *  	
	 */
	@Override
	public List<NotificationMessage> getNotifications(int userId) throws Exception {
		Connection connection = null;
		PreparedStatement prepStmt1 = null;
		PreparedStatement prepStmt2 = null;
		PreparedStatement prepStmt3 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		List<NotificationMessage> notifications = new ArrayList<>();
		
		try {
			connection = JdbcConnection.getConnection();
			String selectStatement1 = "SELECT DISTINCT senderID FROM messages WHERE receiverID=? AND readFlag=0";
			prepStmt1 = connection.prepareStatement(selectStatement1);
			prepStmt1.setInt(1, userId);
			rs1 = prepStmt1.executeQuery();

			String selectStatement2 = "SELECT COUNT(*) FROM messages WHERE senderID=? AND receiverID=? AND readFlag=0";
			prepStmt2 = connection.prepareStatement(selectStatement2);
			
			String selectStatement3 = "SELECT sendTime, content FROM messages WHERE " +
					"senderID=? AND receiverID=? AND readFlag=0 ORDER BY sendTime DESC LIMIT 0,1";
			prepStmt3 = connection.prepareStatement(selectStatement3);
			
			while (rs1.next()) {
				int senderID = rs1.getInt(1);
				NotificationMessage message = new NotificationMessage();
				message.setSenderId(senderID);
				User sender = userDao.findUserById(senderID);
				message.setSenderName(sender.getUsername());
				
				prepStmt2.setInt(1, senderID);
				prepStmt2.setInt(2, userId);
				rs2 = prepStmt2.executeQuery();
				int unreadCount = 0;
				if(rs2.next()) {
					unreadCount = rs2.getInt(1);
					message.setUnreadCount(unreadCount);
				}
				
				prepStmt3.setInt(1, senderID);
				prepStmt3.setInt(2, userId);
				rs3 = prepStmt3.executeQuery();
				if(rs3.next()) {
					Timestamp ts = rs3.getTimestamp(1);
					String content = rs3.getString(2);
					message.setTs(ts);
					message.setContent(content);					
				}
				notifications.add(message);
			}			
		//} catch (Exception e) {
		//	e.printStackTrace();
		}finally{
			JdbcConnection.closeResultSet(rs3);
			JdbcConnection.closeResultSet(rs2);
			JdbcConnection.closeResultSet(rs1);
			JdbcConnection.closePrepStmt(prepStmt3);
			JdbcConnection.closePrepStmt(prepStmt2);
			JdbcConnection.closePrepStmt(prepStmt1);
			JdbcConnection.closeConnection(connection);
		}
		
		return notifications;
	}
	
}
