package com.drift.core;

public class DBResult {


	private int code;	// status code
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DBResult() {
		this.code = DBConnector.DB_STATUS_ERR_GENERIC;
		this.user = null;
	}	
	
	public DBResult(int code, User user) {
		this.code = code;
		this.user = user;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

}
