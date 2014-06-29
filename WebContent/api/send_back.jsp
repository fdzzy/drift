<%@page import="com.drift.core.Bottle"%>
<%@page import="com.drift.util.JSONUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.drift.core.DBConnector"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="application/json; charset=utf8" %>

<%
	String bidStr = request.getParameter("bid");
	int bid = 0;

	try {
		bid = Integer.parseInt(bidStr);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	final int SUCCESS = 21000;
	final int ERR_UNKOWN = 21001;
	final int ERR_BAD_ARGS = 21002;
		
	int status = ERR_UNKOWN;
	Map<String, Object> map = new HashMap<String, Object>();
	
	int result = DBConnector.setBottleUnread(bid);
	switch(result) {
	case DBConnector.DB_STATUS_OK:
		status = SUCCESS;
		map.put("result", "Succeed");
		break;
	case DBConnector.DB_STATUS_ERR_BAD_ARGS:
		status = ERR_BAD_ARGS;
		map.put("result", "Bad Arguments");
		break;
	default:
		map.put("result", "Unknown Error");
	}
	map.put("code", status);
	//System.out.println(status);
	
	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>