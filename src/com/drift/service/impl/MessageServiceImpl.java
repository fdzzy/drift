package com.drift.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.drift.bean.Bottle;
import com.drift.bean.ChatMessage;
import com.drift.bean.NotificationMessage;
import com.drift.bean.User;
import com.drift.dao.BottleDao;
import com.drift.dao.MessageDao;
import com.drift.dao.UserDao;
import com.drift.dao.impl.JdbcBottleDao;
import com.drift.dao.impl.JdbcMessageDao;
import com.drift.dao.impl.JdbcUserDao;
import com.drift.service.MessageService;

public class MessageServiceImpl implements MessageService {
	private UserDao userDao = null;
	private BottleDao bottleDao = null;
	private MessageDao messageDao = null;
	
	public MessageServiceImpl() {
		userDao = new JdbcUserDao();
		bottleDao = new JdbcBottleDao();
		messageDao = new JdbcMessageDao();
	}
	
	@Override
	public int sendBottle(User user, String content) {
		int result = Result.ERR_GENERIC;
		
		if(user == null) {
			return Result.ERR_BAD_ARGS;
		}
		
		try {
			bottleDao.postBottle(user, content);
			result = Result.SUCCESS;
		} catch (SQLException e) {
			// Catch Exception from postBottle
			result = Result.ERR_SQL;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Result receiveBottle(User user) {
		Result result = new Result();
		
		if(user == null) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		try {
			Bottle bottle = bottleDao.getBottle(user);
			if(bottle != null) {
				result.setResultObject(bottle);
				result.setCode(Result.SUCCESS);
			} else {
				result.setCode(Result.ERR_NO_BOTTLE);
			}
		} catch (SQLException e) {
			result.setCode(Result.ERR_SQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int sendBackBottle(int bottleId) {
		int result = Result.ERR_GENERIC;
		
		if(bottleId <= 0) return Result.ERR_BAD_ARGS;
		
		try {
			bottleDao.setBottleUnread(bottleId);
			result = Result.SUCCESS;
		} catch (SQLException e) {
			result = Result.ERR_BOTTLE_ID;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Result replyBottle(User user, int bottleId) {
		Result result = new Result();
		
		if(user == null || bottleId <= 0) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		Bottle bottle = null;
		try {
			bottle = bottleDao.findBottleById(bottleId);
		} catch (SQLException e) {
			result.setCode(Result.ERR_BOTTLE_ID);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result.setResultObject(new Integer(bottle.getSenderId()));
		if(bottle != null) {
			ChatMessage message = new ChatMessage(0, bottle.getSenderId(), bottle.getReceiverId(),
					bottle.getSendTime(), bottle.getContent());
			try {
				messageDao.save(message);
				result.setCode(Result.SUCCESS);
			} catch (SQLException e) {
				result.setCode(Result.ERR_SQL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return result;
	}
	
	@Override
	public Result getMyBottles(User user, int start, int count) {
		Result result = new Result();
		
		if(user == null) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}		
		
		try {
			List<ChatMessage> messages = messageDao.getMyMessages(user, start, count);
			result.setResultObject(messages);
			result.setCode(Result.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Result readAndFlagConversation(int userId, int friendId) {
		Result result = new Result();
		List<ChatMessage> messages = null;
		
		if(userId <= 0 || friendId <= 0) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		User user = null, friend = null;
		try {
			user = userDao.findUserById(userId);
			friend = userDao.findUserById(friendId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user == null) {
			result.setCode(Result.ERR_USER_ID);
		} else if(friend == null) {
			result.setCode(Result.ERR_FRIEND_ID);
		} else {
			try {
				messages = messageDao.readAndFlagConversation(userId, friendId);
				if(messages.isEmpty()) {
					result.setCode(Result.ERR_NO_MESSAGE);
				} else {
					result.setResultObject(messages);
					result.setCode(Result.SUCCESS);
				}
			} catch (SQLException e) {
				result.setCode(Result.ERR_SQL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	//TODO: this is too much similar to the above code, do something
	@Override
	public Result readAndFlagUnreadFromFriend(int userId, int friendId) {
		Result result = new Result();
		List<ChatMessage> messages = null;
		
		if(userId <= 0 || friendId <= 0) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		User user = null, friend = null;
		try {
			user = userDao.findUserById(userId);
			friend = userDao.findUserById(friendId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user == null) {
			result.setCode(Result.ERR_USER_ID);
		} else if(friend == null) {
			result.setCode(Result.ERR_FRIEND_ID);
		} else {
			try {
				messages = messageDao.readAndFlagUnreadFromFriend(userId, friendId);
				if(messages.isEmpty()) {
					result.setCode(Result.ERR_NO_MESSAGE);
				} else {
					result.setResultObject(messages);
					result.setCode(Result.SUCCESS);
				}
			} catch (SQLException e) {
				result.setCode(Result.ERR_SQL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	@Override
	public Result getNewMessageCount(int userId) {
		Result result = new Result();
		
		if(userId <= 0 ) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		User user = null;
		try {
			user = userDao.findUserById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user == null) {
			result.setCode(Result.ERR_USER_ID);
			return result;
		}
		
		try {
			int count = messageDao.getNewMessageCount(userId);
			result.setResultObject(new Integer(count));
			result.setCode(Result.SUCCESS);
		} catch (SQLException e) {
			result.setCode(Result.ERR_NO_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}		
			
		return result;
	}
	
	@Override
	public int sendMessage(int userId, int friendId, String content) {
		if(userId <= 0 || friendId <= 0 || content == null || content.isEmpty()) {
			return Result.ERR_BAD_ARGS;
		}
		
		User user = null, friend = null;
		try {
			user = userDao.findUserById(userId);
			friend = userDao.findUserById(friendId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user == null) {
			return Result.ERR_USER_ID;
		} else if(friend == null) {
			return Result.ERR_FRIEND_ID;
		} else {
			ChatMessage message = new ChatMessage(0, userId, friendId, 0, content);
			try {
				messageDao.save(message);
				return Result.SUCCESS;
			} catch (SQLException e) {
				return Result.ERR_SQL;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return Result.ERR_GENERIC;
	}
	
	@Override
	public Result getNotifications(int userId) {
		Result result = new Result();
		List<NotificationMessage> notifications = null;
		
		if(userId <= 0) {
			result.setCode(Result.ERR_BAD_ARGS);
			return result;
		}
		
		User user = null;
		try {
			user = userDao.findUserById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user == null) {
			result.setCode(Result.ERR_USER_ID);
			return result;
		} else {
			try {
				notifications = messageDao.getNotifications(userId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(notifications == null || notifications.isEmpty()) {
			result.setCode(Result.ERR_NO_MESSAGE);
		} else {
			result.setResultObject(notifications);
			result.setCode(Result.SUCCESS);
		}
		
		return result;
	}

}
