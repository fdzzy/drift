<%@page import="com.drift.util.JSONUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.drift.core.DBResult"%>
<%@page import="com.drift.core.DAO"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="application/json; charset=utf8" %>

<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String sex = request.getParameter("sex");
	String nickname = request.getParameter("nickname");
	String birthday = request.getParameter("birthday");
	String school = request.getParameter("school");
	String department = request.getParameter("department");
	String major = request.getParameter("major");
	String enrollYear = request.getParameter("enrollYear");
	String email = request.getParameter("email");
	
	if(birthday == null || birthday.isEmpty())
		birthday = "1990-01-01";
	
	int status = MyServletUtil.API_CODE_REGISTER_ERR_UNKOWN;
	DBResult result = DAO.register(username, nickname, password, sex, birthday, 
		school, department, major, enrollYear, email);
	status = MyServletUtil.getRegisterStatusCode(result.getCode());	
	String msg = MyServletUtil.getRegisterMessage(status);
	//System.out.println(status);
	
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("code", status);
	map.put("result", msg);
	out.print(JSONUtil.toJSONString(map));
	out.flush();
%>