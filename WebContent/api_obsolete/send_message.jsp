<%@page import="com.drift.core.User"%>
<%@page import="com.drift.core.ChatMessage"%>
<%@page import="com.drift.util.JSONUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.drift.core.DAO"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="application/json; charset=utf8" %>

<%
	String uidStr = request.getParameter("uid");
	String friendIdStr = request.getParameter("friendId");
	String content = request.getParameter("content");
	int uid = 0, friendId = 0;
	try {
		uid = Integer.parseInt(uidStr);
		friendId = Integer.parseInt(friendIdStr);
	} catch (Exception e) {
		e.printStackTrace();
	}

	final int SUCCESS = 51000;
	final int ERR_UNKOWN = 51001;
	final int ERR_BAD_ARGS = 51002;
	final int ERR_NO_MESSAGES = 51003;

	int status = ERR_UNKOWN;
	Map<String, Object> map = new HashMap<String, Object>();
	
	int result = DAO.sendMessage(uid, friendId, content);
	
	//if(messages == null || messages.isEmpty()) {
	switch (result) {
	case DAO.DB_STATUS_ERR_BAD_ARGS:
		status = ERR_BAD_ARGS;
		map.put("result", "Bad Arguments");
		break;
	case DAO.DB_STATUS_OK:
		status = SUCCESS;
		map.put("result", "Succeed");
		break;
	default:
		status = ERR_UNKOWN;
		map.put("result", "Unknown Error");		
	}
	map.put("code", status);
	//System.out.println(status);

	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>