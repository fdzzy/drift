<%@page import="com.drift.core.Bottle"%>
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
	String content = request.getParameter("content");
	int uid = 0;

	try {
		uid = Integer.parseInt(uidStr);
	} catch (Exception e) {
		e.printStackTrace();
	}

	final int SUCCESS = 30000;
	final int ERR_UNKOWN = 30001;
	final int ERR_BAD_ARGS = 30002;
	final int ERR_NO_BOTTLES = 30003;	

	int status = ERR_UNKOWN;
	Map<String, Object> map = new HashMap<String, Object>();
	
	if(uid <= 0) {
		status = ERR_BAD_ARGS;
		map.put("result", "Bad Arguments");
		map.put("code", status);
		out.print(JSONUtil.toJSONString(map));
		out.flush();
		return;
	}

	List<ChatMessage> messages = DAO.getMyBottles(uid, 0, 30);

	if (messages == null || messages.isEmpty()) {
		status = ERR_NO_BOTTLES;
		map.put("result", "No bottles");
	} else {
		status = SUCCESS;
		map.put("result", "Succeed");
		map.put("bottles", messages);
	}
	map.put("code", status);
	//System.out.println(status);

	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>