package com.drift.dao.impl;

import com.drift.dao.UserDao;

public class DaoFactory {
	
	public static UserDao createUserDao() {
		return new JdbcUserDao();
	}

}
