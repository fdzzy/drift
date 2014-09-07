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

	final int SUCCESS = 42000;
	final int ERR_UNKOWN = 42001;
	final int ERR_BAD_ARGS = 42002;
	final int ERR_NO_SUCH_USER = 42003;

	int status = ERR_UNKOWN;
	Map<String, Object> map = new HashMap<String, Object>();
	
	if(uid <= 0) {
		map.put("code", status);
		out.print(JSONUtil.toJSONString(map));
		out.flush();
		return;
	}
	
	String photoUrl = DAO.getPhotoUrl(uid);
	
	if(photoUrl == null) {
		status = ERR_NO_SUCH_USER;
		map.put("result", "User not exist");
	} else {
		status = SUCCESS;
		map.put("result", "Succeed");
		map.put("photoUrl", photoUrl);
	}

	map.put("code", status);
	//System.out.println(status);

	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>