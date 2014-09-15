package com.drift.service.impl;

//TODO: maybe we can throw some customized Exception when dealing with errors instead of using this class
public class Result {

	private int code;	// status code
	private Object result; 	// result object
	
	public Object getResultObject() {
		return result;
	}

	public void setResultObject(Object result) {
		this.result = result;
	}

	public Result() {
		this.code = ERR_GENERIC;
		this.result = null;
	}	
	
	public Result(int code, Object result) {
		this.code = code;
		this.result = result;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	// Error status code
	public static final int SUCCESS = 0;
	public static final int ERR_GENERIC = -1;
	public static final int ERR_BAD_ARGS = -2;
	public static final int ERR_SQL = -3;
	public static final int ERR_USER_NOT_EXIST = -4;
	public static final int ERR_EMAIL_NOT_EXIST = -5;
	public static final int ERR_USER_EXISTS = -6;
	public static final int ERR_EMAIL_EXISTS = -7;
	public static final int ERR_PASSWORD = -8;
	public static final int ERR_USER_NOT_ACTIVATED = -9;
	public static final int ERR_USER_ID = -10;
	public static final int ERR_BOTTLE_ID = -11;
	public static final int ERR_NO_BOTTLE = -12;
	public static final int ERR_EMAIL_REJECTED = -13;
	public static final int ERR_FRIEND_ID = -14;
	public static final int ERR_NO_MESSAGE = -15;
}
