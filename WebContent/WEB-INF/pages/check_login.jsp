<%@page import="com.drift.servlet.MyServletUtil"%>
<%@page import="com.drift.bean.User"%>
<%
//检查会话是否已经失效，或者用户是否已经登入
User user = (User)session.getAttribute(MyServletUtil.SESS_USER);
if(user == null) {
	response.sendRedirect("login");
	return;
}
%>