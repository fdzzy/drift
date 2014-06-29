<%@page import="com.drift.util.PhotoUtil"%>
<%@page import="com.drift.util.JSONUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.drift.core.DBConnector"%>
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

	final int SUCCESS = 43000;
	final int ERR_UNKOWN = 43001;
	final int ERR_BAD_ARGS = 43002;
	final int ERR_NO_SUCH_USER = 43003;

	int status = ERR_UNKOWN;
	Map<String, Object> map = new HashMap<String, Object>();
	
	if(uid <= 0) {
		map.put("code", status);
		out.print(JSONUtil.toJSONString(map));
		out.flush();
		return;
	}
	
	String path = getServletContext().getRealPath("/photo"); //上传文件目录
	System.out.println(path);
	int dbStatus = PhotoUtil.uploadPhoto(request, path, uid);
	
	if(dbStatus == DBConnector.DB_STATUS_OK) {
		status = SUCCESS;
		map.put("result", "Succeed");
	} else {
		map.put("result", "Unkown Error");
	}

	map.put("code", status);
	//System.out.println(status);

	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>