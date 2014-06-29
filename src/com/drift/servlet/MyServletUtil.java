package com.drift.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drift.core.DBConnector;
import com.drift.core.User;

public final class MyServletUtil {
	public static final String loginJspPage = "/login.jsp";
	public static final String registerJspPage = "/register.jsp";
	public static final String registerOkJspPage = "/register_ok.jsp";
	public static final String mainJspPage = "/main.jsp";
	public static final String activateJspPage = "/activate_tip.jsp";
	public static final String getBottleJspPage = "/get_bottle.jsp";
	public static final String doPostBottleJspPage = "/do_post.jsp";
	
	
	//public static final String entryURL = "http://driftlove.duapp.com";
	public static final String entryURL = "http://localhost:8080/drift";
	
	// SESSION Variable Constants
	public static final String SESS_UID = "userId";			// Deprecated
	public static final String SESS_USERNAME = "username";	// Deprecated
	public static final String SESS_USER = "user";
	
	// REQUEST Variable Constants
	public static final String REQ_EDIT_PROFILE_USER_STRING = "user";
	
	public static DBConnector getDateDB(ServletContext context) throws Exception {
		DBConnector dateDB = (DBConnector)context.getAttribute("dateDB");
		if(dateDB == null) {
			dateDB = new DBConnector();
			context.setAttribute("dateDB", dateDB);
		}
		return dateDB;
	}
	
	@SuppressWarnings("deprecation")
	public static String timestampToDate(Timestamp ts) {
		Date date = new Date(ts.getTime());
		int year = date.getYear() + 1900;
		String yearString = year + "";
		int month = date.getMonth() + 1;
		String monthString = (month < 10) ? ("0" + month) : ("" + month); 
		
		return yearString + monthString;
	}	
	
	public static void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
	}
	
	// Check to see if logged in, if true then return User
	public static User checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//String user = (String) request.getSession().getAttribute("user");
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(MyServletUtil.SESS_USER);
		
		if(user == null || user.getUid() <= 0) {
			PrintWriter out = response.getWriter();
			out.println("<html><head>");
			out.println("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />");
			out.println("</head><body>");
			out.println("<font color='red' size='4'>");
			out.println("<center>");
			out.println("<b>错误信息：</b>");
			out.println("您访问的页面失效，或您还未登录。");
			out.println("请<a href='login'>重新登录</a>");
			out.println("</center>");
			out.println("</font>");
			out.println("</body></html>");
			request.setAttribute("loginCheck", "fail");
			return null;
		} else {
			return user;
		}
	}

	
	// REST API Login
	public static final int API_CODE_LOGIN_SUCCEED = 10000;
	public static final int API_CODE_LOGIN_ERR_UNKOWN = 10001;
	public static final int API_CODE_LOGIN_BAD_ARGS = 10002;
	public static final int API_CODE_LOGIN_USER_NOT_EXIST = 10003;
	public static final int API_CODE_LOGIN_ERR_PASSWORD = 10004;
	public static final int API_CODE_LOGIN_EMAIL_NOT_EXIST = 10005;
	public static final int API_CODE_LOGIN_USER_NOT_ACTIVATED = 10006;
	
	public static final String[] API_CODE_LOGIN_STRINGS = {
		"Succeed",
		"Unknown Error",
		"Bad Arguments",
		"User name has not been registered",
		"Wrong Password",
		"Email has not been registered",
		"User has not yet activated"		
	};
	
	public static int getLoginStatusCode(int db_code) {		
		int code;
		
		switch (db_code) {
		case DBConnector.DB_STATUS_OK:
			code = API_CODE_LOGIN_SUCCEED;
			break;
		case DBConnector.DB_STATUS_ERR_BAD_ARGS:
			code = API_CODE_LOGIN_BAD_ARGS;
			break;
		case DBConnector.DB_STATUS_ERR_PASSWORD:
			code = API_CODE_LOGIN_ERR_PASSWORD;
			break;
		case DBConnector.DB_STATUS_ERR_USER_NOT_EXIST:
			code = API_CODE_LOGIN_USER_NOT_EXIST;
			break;
		case DBConnector.DB_STATUS_ERR_EMAIL_NOT_EXIST:
			code = API_CODE_LOGIN_EMAIL_NOT_EXIST;
			break;
		case DBConnector.DB_STATUS_ERR_USER_NOT_ACTIVATED:
			code = API_CODE_LOGIN_USER_NOT_ACTIVATED;
			break;
		default:
			code = API_CODE_LOGIN_ERR_UNKOWN;
			break;
		}
		
		return code;
	}
	
	public static String getLoginMessage(int status) {
		return API_CODE_LOGIN_STRINGS[status - MyServletUtil.API_CODE_LOGIN_SUCCEED];
	}
	
	// REST API Register
	public static final int API_CODE_REGISTER_SUCCEED = 11000;
	public static final int API_CODE_REGISTER_ERR_UNKOWN = 11001;
	public static final int API_CODE_REGISTER_BAD_ARGS = 11002;
	public static final int API_CODE_REGISTER_USER_EXIST = 11003;
	public static final int API_CODE_REGISTER_EMAIL_EXIST = 11004;
	
	public static final String[] API_CODE_REGISTER_STRINGS = {
		"Succeed",
		"Unknown Error",
		"Bad Arguments",
		"User name has already been registered",
		"Email has already been registered",
	};
	
	public static int getRegisterStatusCode(int db_code) {		
		int code;
		
		switch (db_code) {
		case DBConnector.DB_STATUS_OK:
			code = API_CODE_REGISTER_SUCCEED;
			break;
		case DBConnector.DB_STATUS_ERR_BAD_ARGS:
			code = API_CODE_REGISTER_BAD_ARGS;
			break;
		case DBConnector.DB_STATUS_ERR_USER_EXISTS:
			code = API_CODE_REGISTER_USER_EXIST;
			break;
		case DBConnector.DB_STATUS_ERR_EMAIL_EXISTS:
			code = API_CODE_REGISTER_EMAIL_EXIST;
			break;
		default:
			code = API_CODE_REGISTER_ERR_UNKOWN;
			break;
		}
		
		return code;
	}
	
	public static String getRegisterMessage(int status) {
		return API_CODE_REGISTER_STRINGS[status - MyServletUtil.API_CODE_REGISTER_SUCCEED];
	}
	
	

	
}
