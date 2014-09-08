<%@ page contentType="text/html; charset=utf8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>
</head>
<body >
<p><center>
<font size=+3><b>欢迎使用漂流瓶应用</b></font>
</center></p><hr>

<%
String msg =(String)request.getAttribute("msg");
if(msg!=null && !msg.isEmpty()){
%>

<center>
<p>
<%=msg%>
</p>
</center>

<% } %>


</body></html>
