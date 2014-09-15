package com.drift.service;

import com.drift.bean.User;
import com.drift.service.impl.Result;

public interface MessageService {

	/**
	 * getMyBottels
	 * 
	 * Get a list of friends that the specific user had conversation to,
	 * Also get the latest message with that friend
	 * @param user
	 * @param start
	 * @param count
	 * @return List<ChatMessage>
	 */
	Result getMyBottles(User user, int start, int count);
	/**
	 * Send out a bottle
	 * @param user
	 * @param content
	 * @return
	 */
	int sendBottle(User user, String content);
	/**
	 * Receive a bottle from sea
	 * @param user
	 * @return Bottle
	 */
	Result receiveBottle(User user);
	/**
	 * Send the bottle back to sea
	 * @param bottleId
	 * @return
	 */
	int sendBackBottle(int bottleId);
	/**
	 * Reply a bottle
	 * @param bottleId
	 * @return senderId
	 */
	Result replyBottle(User user, int bottleId);
	/**
	 * Read bidirectional messages with the specific friend, and set all the messages with a 'read flag'
	 * @param userId
	 * @param friendId
	 * @return List<ChatMessage>
	 */
	Result readAndFlagConversation(int userId, int friendId);
	/**
	 * Read unidirectional messages from friend, and set them with a 'read flag'
	 * @param userId
	 * @param friendId
	 * @return List<ChatMessage>
	 */
	Result readAndFlagUnreadFromFriend(int userId, int friendId);
	/**
	 * Get the new message count for a specified user
	 * @param userId
	 * @return message count
	 */
	Result getNewMessageCount(int userId);
	/**
	 * Send a message to friend
	 * @param userId
	 * @param friendId
	 * @param content
	 * @return
	 */
	int sendMessage(int userId, int friendId, String content);
	/**
	 * get unread messages, containing the a list of following information:
	 *  	friendId: the user id from which the unread message belongs to
	 *  	ts: the newest timestamp
	 *  	content: the last message body
	 *  	unreadCount: number of unread messages from him/her
	 * @param userId
	 * @return
	 */
	Result getNotifications(int userId);

}
