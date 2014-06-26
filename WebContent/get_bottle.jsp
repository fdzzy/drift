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
<body>
<p><center>
<font size=+3><b><%=user.getUsername()%>，欢迎使用漂流瓶应用</b></font>
</center></p><hr/>

<%

Bottle bottle = (Bottle)request.getAttribute("bottle");

if(user == null && bottle == null) {
	out.println("请稍后重试！");
%>
  <a href="main.jsp">返回</a>
<%
	return;
}
int senderUid = bottle.getSenderId();
String photoUrl = null;
try {
	photoUrl = dateDB.get_photo_url(senderUid);
} catch (Exception e) {
	e.printStackTrace();
}
%>

<form action="do_post.jsp" method="post" onsubmit='return checkinfo(this)'>
<center>
<table>
<tr>
  <td><center>来自：<%=bottle.getSenderName()%>
  <img src="<%=photoUrl%>" height="30" width="30"/></center></td>  
</tr>
<tr>
  <td><textarea rows="10" cols="50" name="content" readonly="readonly"><%=bottle.getContent()%></textarea></td>
</tr>
</table>
</center>

<center><br/>
<!-- 
<form action="reply_bottle?action=reply" method="post" >
  <input type="hidden" name="bid" value="<%=bottle.getBottleId() %>"/>
  <input type="submit" value="回复" />  
</form>
<form action="reply_bottle?action=sendback" method="post" >
  <input type="hidden" name="bid" value="<%=bottle.getBottleId() %>"/>
  <input type="submit" value="抛回海里" />  
</form>
-->
  

  <a href="handle_bottle?action=reply&bid=<%=bottle.getBottleId()%>">回复</a>
  &nbsp;
  <a href="handle_bottle?action=sendback&bid=<%=bottle.getBottleId()%>">抛回海里</a> 
  &nbsp;
  <!-- 
  <a href="main.jsp">返回</a>
  -->

</center>
</form>

<center><br/>
  
</center>

</body></html>
