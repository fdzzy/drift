package com.drift.dao;

import java.util.List;

import com.drift.bean.ChatMessage;
import com.drift.bean.NotificationMessage;
import com.drift.bean.User;

public interface MessageDao {

	/**
	 * Save a message into DB
	 * @param message
	 * @throws Exception
	 */
	void save(ChatMessage message) throws Exception;
	/**
	 * getMyMessages
	 * 
	 * Get a list of friends that the specific user had conversation to,
	 * Also get the latest message with that friend
	 * 
	 * @param user
	 * @param start
	 * @param count
	 * @return
	 * @throws Exception 
	 */
	List<ChatMessage> getMyMessages(User user, int start, int count) throws Exception;
	/**
	 * Read bidirectional messages with the specific friend, and set all the messages with a 'read flag'
	 * @param userId
	 * @param friendId
	 * @return
	 * @throws Exception
	 */
	List<ChatMessage> readAndFlagConversation(int userId, int friendId)	throws Exception;
	List<ChatMessage> readAndFlagUnreadFromFriend(int userId, int friendId) throws Exception;
	int getNewMessageCount(int userId) throws Exception;
	List<NotificationMessage> getNotifications(int userId) throws Exception;

}
