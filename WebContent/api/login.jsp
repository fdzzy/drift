<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.drift.core.DBResult"%>
<%@page import="com.drift.core.DBConnector"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="application/json; harset=utf8" %>

<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	
	int status = MyServletUtil.API_STATUS_CODE_ERR_UNKONW;
	DBResult result = DBConnector.login(username, password);
	status = MyServletUtil.convertStatusCode(result.getCode());	
	String msg = MyServletUtil.getStatusMessage(status);
	//System.out.println(status);
	
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("code", status);
	map.put("message", msg);
	JSONObject obj = new JSONObject(map);
	out.print(obj.toJSONString());
	out.flush();
%>