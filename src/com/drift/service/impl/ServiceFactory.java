package com.drift.service.impl;

import com.drift.service.MessageService;
import com.drift.service.UserService;

public class ServiceFactory {
	
	public static UserService createUserService() {
		return new UserServiceImpl();
	}

	public static MessageService createMessageService() {
		return new MessageServiceImpl();
	}
}
