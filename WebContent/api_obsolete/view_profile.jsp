<%@page import="com.drift.core.User"%>
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

	final int SUCCESS = 41000;
	final int ERR_UNKOWN = 41001;
	final int ERR_BAD_ARGS = 41002;
	final int ERR_NO_SUCH_USER = 41003;

	int status = ERR_UNKOWN;
	Map<String, Object> map = new HashMap<String, Object>();
	
	int rtval = DAO.DB_STATUS_ERR_GENERIC;
	User targetUser = DAO.getUser(uid);
	
	if(targetUser == null) {
		status = ERR_NO_SUCH_USER;
		map.put("result", "User does not exist");
	} else {
		status = SUCCESS;
		map.put("result", "Succeed");
		map.put("profile", targetUser);
	}

	map.put("code", status);
	//System.out.println(status);

	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>