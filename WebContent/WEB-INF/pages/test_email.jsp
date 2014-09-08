<%@ page contentType="text/html; charset=utf8" %>

<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
%>
<html><head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>
</head>
<body >
<p><center>
<font size=+3><b>欢迎使用漂流瓶应用</b></font>
</center></p><hr>

<%
String msg = (String)request.getAttribute("msg");

if(msg!=null && !msg.isEmpty()) {
%>

<center>
<p><font color="green">
<%=msg%>
</font></p>
</center>

<%
}
%>

<form action="test_email?action=send" method="post" >
<center>
<table>
<tr>
  <td>标题：</td>
  <td><input type="text" name="subject" size="50"></td>
</tr>
<tr>
  <td>内容：</td>
  <td><input type="text" name="content" size="50"></td>
</tr>
  <td>邮箱：</td>
  <td><input type="email" name="email" size="25"></td>
</tr>
</table>
</center>

<center><br/>
  <input type="submit" value="发送">
  <input type="reset" name="Reset" value="清空"><br/><br/>
  <a href="login">返回</a>
</center>

</form>

</body></html>