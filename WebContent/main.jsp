<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="text/html; charset=utf8" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ include file="common.jsp" %>
<%@ include file="check_login.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>

<script type="text/javascript">

var xmlHttp = null;

function createXMLHttp() {
	if(xmlHttp != null)
		return;
	if(window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	} else {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function updateState() {
	createXMLHttp();
	xmlHttp.open("POST", "update_state");
	xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState == 4) {
				if(xmlHttp.status == 200) {
					//var text = xmlHttp.responseText;
					var res = eval("(" + xmlHttp.responseText +")");
					if(res.newMessageCount > 0) {
						document.getElementById("msg").innerHTML = "<a href='my_bottle'><font color='green'>您有条" + res.newMessageCount + "新消息</font></a>";
					} else {
						document.getElementById("msg").innerHTML = "";
					}
				}
			}
	};
	xmlHttp.send(null);
}

updateState();
setInterval(updateState, 10000);
</script>

</head>
<body >
<p><center>
<font size=+3><b><%=user.getUsername()%>，欢迎使用漂流瓶应用</b></font>
</center></p><hr/>


<center>
<p id="msg"></p>
<table>
<tr>
  <td><a href="post_bottle.jsp">抛一个</td>
</tr>
<tr>
  <td><a href="get_bottle">捞一个</td>
</tr>
<tr>
  <!-- <td><a href="#" onclick='alert("开发中！")'>我的瓶子</td> -->
  <td><a href="my_bottle">我的瓶子</td>
</tr>
<tr>
  <td><a href="edit_profile">修改资料</td>
</tr>
<%
if(session.getAttribute(MyServletUtil.SESS_FOREIGN_UID) != null) {
%>
<tr>
  <td><a href="get_keywords">获取微博关键字</td>
</tr>
<!-- 
<tr>
  <td>${sessionScope.accessToken}</td>
</tr>
 -->
<%
}
%>
<tr>
  <td><a href="logout.jsp">退出</td>
</tr>
</table>
</center>


</body></html>
