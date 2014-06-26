<%@ page contentType="text/html; charset=utf8" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ include file="common.jsp" %>
<%@ include file="check_login.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>
</head>
<body>
<p><center>
<font size=+3><b><%=user.getUsername()%>，欢迎使用漂流瓶应用</b></font>
</center></p><hr/>
<center>
<%
String msg = (String)request.getAttribute("msg");
out.print(msg);
%>
<br/>
<a href="post_bottle.jsp">再发一个</a>&nbsp;
<a href="main.jsp">返回</a>
</center>
</body>
</html>