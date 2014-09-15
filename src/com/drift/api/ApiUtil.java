package com.drift.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drift.service.impl.Result;
import com.drift.servlet.MyServletUtil;

public class ApiUtil {
	
	public static final String API_ROOT = "/api";
	
	/* REST API Status code */
	// success code
	public static final int API_ACTION_OK = 100;
	// generic error
	public static final int API_ERR_OTHER = 200;
	public static final int API_ERR_BAD_ARGS = 201;
	public static final int API_ERR_SQL = 202;	/* better not to use this one */
	// account related
	public static final int API_ERR_USERNAME_NOT_EXIST = 300;
	public static final int API_ERR_EMAIL_NOT_EXIST = 301;
	public static final int API_ERR_PASSWORD = 302;
	public static final int API_ERR_USERNAME_UNAVALABLE = 303;
	public static final int API_ERR_EMAIL_UNAVALABLE = 304;
	public static final int API_ERR_USER_NOT_ACTIVATED = 305;
	public static final int API_ERR_BAD_USER_ID = 306;
	public static final int API_ERR_BAD_FRIEND_ID = 307;
	// bottle related
	public static final int API_ERR_NO_BOTTLE = 400;
	public static final int API_ERR_BAD_BOTTLE_ID = 401;
	
	// message related
	public static final int API_ERR_NO_MESSAGE = 500;
	
	public static final Map<Integer, String> API_CODE_STRINGS = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(API_ACTION_OK, "OK");
			
			put(API_ERR_OTHER, "Unkown Error");
			put(API_ERR_BAD_ARGS, "Bad Arguments");
			
			put(API_ERR_USERNAME_NOT_EXIST, "Username has not been registered");
			put(API_ERR_EMAIL_NOT_EXIST, "Email has not been registered");
			put(API_ERR_PASSWORD, "Wrong Password");
			put(API_ERR_USERNAME_UNAVALABLE, "Username not available");
			put(API_ERR_EMAIL_UNAVALABLE, "Email has already been registered");
			put(API_ERR_USER_NOT_ACTIVATED, "User has not yet activated");
			put(API_ERR_BAD_USER_ID, "Bad User ID");
			put(API_ERR_BAD_FRIEND_ID, "Bad friend ID");
			
			put(API_ERR_NO_BOTTLE, "No bottles");
			put(API_ERR_BAD_BOTTLE_ID, "Bad bottle ID");
			
			put(API_ERR_NO_MESSAGE, "No messages");
		}
	};
	
	public static int mapCode(int internalCode) {		
		int code;
		
		switch (internalCode) {
		case Result.SUCCESS:
			code = API_ACTION_OK;
			break;
		case Result.ERR_BAD_ARGS:
			code = API_ERR_BAD_ARGS;
			break;
		case Result.ERR_PASSWORD:
			code = API_ERR_PASSWORD;
			break;
		case Result.ERR_USER_NOT_EXIST:
			code = API_ERR_USERNAME_NOT_EXIST;
			break;
		case Result.ERR_EMAIL_NOT_EXIST:
			code = API_ERR_EMAIL_NOT_EXIST;
			break;
		case Result.ERR_USER_NOT_ACTIVATED:
			code = API_ERR_USER_NOT_ACTIVATED;
			break;
		case Result.ERR_USER_EXISTS:
			code = API_ERR_USERNAME_UNAVALABLE;
			break;
		case Result.ERR_EMAIL_EXISTS:
			code = API_ERR_EMAIL_UNAVALABLE;
			break;
		case Result.ERR_USER_ID:
			code = API_ERR_BAD_USER_ID;
			break;
		case Result.ERR_FRIEND_ID:
			code = API_ERR_BAD_FRIEND_ID;
			break;
		case Result.ERR_BOTTLE_ID:
			code = API_ERR_BAD_BOTTLE_ID;
			break;
		case Result.ERR_NO_BOTTLE:
			code = API_ERR_NO_BOTTLE;
			break;
		case Result.ERR_NO_MESSAGE:
			code = API_ERR_NO_MESSAGE;
			break;
		default:
			code = API_ERR_OTHER;
			break;
		}
		
		return code;
	}
	
	public static void doCommonTasks(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MyServletUtil.setCharacterEncoding(request, response);
		response.setContentType("application/json");
		
		response.setHeader("Access-Control-Allow-Origin", "*");
	}

}
