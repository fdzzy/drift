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
<p><font color="red">
<%=msg%>
</font></p>
</center>

<% } %>

<form action="login?action=login" method="post" >
<center>
<table>
<tr>
  <td><input type="text" name="username" size="35" placeholder="请输入用户名或邮箱"></td>
</tr>
<tr>
  <td><input type="password" name="password" size="35" placeholder="请输入密码"></td>
</tr>
</table>
</center>

<center><br/>
  <input type="submit" value="登录">
  <input type="reset" name="Reset" value="清空"><br/><br/>
  <a href="register">本站注册</a>
</center>

</form>

 
<center>
<a href="baiduOauthCode"><img src="img/baidu.png"/></a>&nbsp;
<a href="sinaOauth"><img src="img/sina_weibo.png"/></a>&nbsp;
<a href=""><img src="img/renren.png"/></a>&nbsp;
<!-- 
<a href="">qq</a>&nbsp;
<a href="">腾讯微博</a>&nbsp;
 -->
</center>


</body></html>
