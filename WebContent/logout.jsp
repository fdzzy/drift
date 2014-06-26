<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="text/html; charset=utf8" %>
<%@ include file="common.jsp" %>

<%
User user = (User)session.getAttribute(MyServletUtil.SESS_USER);
session.invalidate(); //end HTTP connection
if(user == null) {
	response.sendRedirect("login");
	return;
}
response.setHeader("refresh","2,URL=login");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>退出</title>
</head>
<body>

<center>
<h3>再见，<%=user.getUsername()%>!</h3><br>
<strong><a href="login.jsp">2秒后将自动跳转登录页面，如未跳转请点击</a></strong>
</center>

</body></html>