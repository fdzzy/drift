<%@page import="com.drift.servlet.MyServletUtil"%>
<%@page import="com.drift.core.User"%>
<%@ page contentType="text/html; charset=utf8" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ include file="common.jsp" %>
<%@ include file="check_login.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>
</head>
<body >
<p><center>
<font size=+3><b><%=user.getUsername()%>，欢迎使用漂流瓶应用</b></font>
</center></p><hr/>
<script type="text/javascript">
<!--
function openwin() {
	window.open("edit_photo","", "height=500,width=800,toolbars=0,status=0,resizable=1,scrollbars=0");
}
function openwin2() {
	window.open("edit_photo2.jsp","", "height=500,width=800,toolbars=0,status=0,resizable=1,scrollbars=0");
}
-->
</script>

</head>
<body>

<%

if(user == null) {
	out.println("请稍后重试！");
%>
  <a href="main">返回</a>
<%
	return;
}

// the profile may have changed
user = DAO.getUser(user.getUid());
session.setAttribute(MyServletUtil.SESS_USER, user);

String msg =(String)request.getAttribute("msg");
String photoUrl = (String)request.getAttribute("photoUrl");
if(msg!=null && !msg.isEmpty()){
%>

<center>
<p><font color="red">
<%=msg%>
</font></p>
</center>

<% } %>


<center>
<form action="edit_profile?action=edit" method="post">
<table>
<tr>
<td>
<p>
<img src="<%=photoUrl%>" onclick='openwin()'
style="cursor:pointer" alt="修改头像" title="修改头像"/>
</p>
<p>
<center>
<a href='#' onclick='openwin()'>修改头像</a>
<!-- 
<br/>
<a href='#' onclick='openwin2()'>修改头像_octet_stream</a>
 -->
</center>
</p>
</td>
<td>
&nbsp;
</td>
<td>
<table>
<tr>
  <td>用户名：</td><td><%=user.getUsername()%></td>  
</tr>
<tr>
  <td>性别：</td><td><%=user.getSex()%></td>  
</tr>
<tr>
  <td>昵称：</td>
  <td><input type="text" name="nickname" value="<%=user.getNickname()%>"></td>
</tr>
<tr>
  <td>生日：</td>
  <td><input type="date" name="birthday" value="<%=user.getBirthday()%>"></td>
</tr>
<tr>
  <td>学校：</td>
  <td><input type="text" name="school" size="25" value="<%=user.getSchool()%>"></td>
</tr>
<tr>
  <td>院系：</td>
  <td><input type="text" name="department" size="25" value="<%=user.getDepartment()%>"></td>
</tr>
<tr>
  <td>专业：</td>
  <td><input type="text" name="major" size="25" value="<%=user.getMajor()%>"></td>
</tr>
<tr>
  <td>入学年份：</td>
  <td><input type="number" name="enrollYear" max="2100" min="0" size="25" value="<%=user.getEnrollYear()%>"></td>
</tr>
<tr>
  <td>邮箱：</td><td><%=user.getEmail()%></td>
</tr>
</table>
</td>
</tr>
</table>
</center>

<center><br/>
  <input type="submit" value="修改">
  <input type="reset" name="Reset" value="清空"><br/><br/>
</center>

</form>


<center>
<p>
<a href='main'>返回</a>
</p>
</center>
</body>
</html>