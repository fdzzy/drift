<%@page import="com.drift.core.Bottle"%>
<%@page import="com.drift.util.JSONUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.drift.core.DAO"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="application/json; charset=utf8" %>

<%
	String uidStr = request.getParameter("uid");
	int uid = 0;

	try {
		uid = Integer.parseInt(uidStr);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	final int SUCCESS = 20000;
	final int ERR_UNKOWN = 20001;
	final int ERR_BAD_ARGS = 20002;
	final int ERR_NO_BOTTLE = 20003; 
		
	int status = ERR_UNKOWN;
	Map<String, Object> map = new HashMap<String, Object>();
	
	if(uid > 0) {
		Bottle bottle = DAO.getBottle(uid);
		if(bottle != null) {
	status = SUCCESS;
	map.put("message", "Succeed");
	map.put("bottleID", bottle.getBottleId());
	map.put("content", bottle.getContent());
	map.put("senderID", bottle.getSenderId());
	map.put("senderName", bottle.getSenderName());
		} else {
	status = ERR_NO_BOTTLE;
	map.put("result", "No Bottle");
		}
	} else {
		status = ERR_BAD_ARGS;
		map.put("result", "Bad Arguments");
	}
	map.put("code", status);
	//System.out.println(status);
	
	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>