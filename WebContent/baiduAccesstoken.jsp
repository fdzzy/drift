<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.baidu.api.domain.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Refresh" content="10;main.jsp">
<title>access_token信息展示</title>
</head>
<body>
	<h2>access_token 信息展示</h2>
	 	<div class="wrapper">
			<ul>
				<li>
				   access_token:<br/>
				   <p><%=(String)request.getAttribute("accessToken")%></p>
				</li>
				<li>
					refresh_token：<br/>	
					<p><%=(String)request.getAttribute("refreshToken")%></p>
				</li>
				<li>
					session_secret: <br/>
					<p><%=(String)request.getAttribute("sessionSecret")%></p>
				</li>
				<li>
					session_key: <br/>
					<p><%=(String)request.getAttribute("sessionKey")%></p>
				</li>
			</ul>
		</div>
	
	<%
		User baiduUser = (User)request.getAttribute("user");
		if(baiduUser != null) {
	%>
			<ul>
				<li>
				   uid:<br/>
				   <p><%=baiduUser.getUid()%></p>
				</li>
				<li>
					uname:：<br/>	
					<p><%=baiduUser.getUname()%></p>
				</li>
				<li>
					portrait: <br/>
					<p><%=baiduUser.getPortrait()%></p>
				</li>
			</ul>
		
	<%
		session.setAttribute("user", baiduUser.getUname());
	%>
		
	<%
		}
	%>
	<!-- 
	<p>Error!</p>
	 -->
	<a href="/drift">返回首页</a>
</body>
</html>