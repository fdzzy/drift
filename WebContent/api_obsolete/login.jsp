<%@page import="java.util.TreeMap"%>
<%@page import="com.drift.util.JSONUtil"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.drift.core.DBResult"%>
<%@page import="com.drift.core.DAO"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="application/json; charset=utf8" %>

<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	
	int status = MyServletUtil.API_CODE_LOGIN_ERR_UNKOWN;
	DBResult result = DAO.login(username, password);
	//System.out.println(result.getCode());
	status = MyServletUtil.getLoginStatusCode(result.getCode());	
	String msg = MyServletUtil.getLoginMessage(status);
	
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("code", status);
	map.put("result", msg);
	if(status == MyServletUtil.API_CODE_LOGIN_SUCCEED) {
		map.put("uid", result.getUser().getUid());
	}
	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>