<%@ page contentType="text/html; charset=utf8" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ include file="common.jsp" %>
<%@ include file="check_login.jsp" %>

<html><head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>
</head>
<body>
<p><center>
<font size=+3><b><%=user.getUsername()%>，欢迎使用漂流瓶应用</b></font>
</center></p><hr/>
<center>
<%
User targetUser = null;

int id = Integer.parseInt(request.getParameter("id"));

//System.out.println("\"" + user + "\"" + "发送了消息：\"" + content + "\"");

int rtval = 0;
String photoUrl = "";
try {
	targetUser = dateDB.getUser(id);
	photoUrl = dateDB.getPhotoUrl(id);
}catch(Exception e) {
	e.printStackTrace();
}
if(targetUser == null) {
	out.println("请稍后重试！");
%>
  <a href="main.jsp">返回</a>
<%
	return;
}
%>

<center>
<table>
<tr>
<td>
<img src="<%=photoUrl %>" />
</td>
<td>
&nbsp;
</td>
<td>
<table>
<tr>
  <td>用户名：</td><td><%=targetUser.getUsername()%></td>  
</tr>
<tr>
  <td>昵称：</td><td><%=targetUser.getNickname()%></td>  
</tr>
<tr>
  <td>性别：</td><td><%=targetUser.getSex()%></td>  
</tr>
<tr>
  <td>生日：</td><td><%=targetUser.getBirthday()%></td>  
</tr>
<tr>
  <td>学校：</td><td><%=targetUser.getSchool()%></td>  
</tr>
<tr>
  <td>院系：</td><td><%=targetUser.getDepartment()%></td>  
</tr>
<tr>
  <td>专业：</td><td><%=targetUser.getMajor()%></td>  
</tr>
<tr>
  <td>入学年份：</td><td><%=targetUser.getEnrollYear()%></td>  
</tr>
<tr>
  <center><td>邮箱：</td><td><%=targetUser.getEmail()%></td>  </center>
</tr>

</table>
</td>
</tr>
</table>
</center>

<input class="button" type="submit" value="返回" onclick="window.history.back(-1);" />
</body>
</html>