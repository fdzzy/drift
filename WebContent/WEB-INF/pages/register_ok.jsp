<%@ page contentType="text/html; charset=utf8" %>

<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
%>
<html><head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>

</head>
<body>
<p><center>
<font size=+3><b>欢迎注册漂流瓶应用</b></font>
</center></p><hr>

<center>
<p>
恭喜您<%=request.getAttribute("username")%>，您已注册成功！<br/>
已发送激活邮件至<%=request.getAttribute("email") %>，请尽快登录邮箱激活！
</p>
<p><a href="login">返回</a></p>
</center>

</body></html>