<%@page import="java.sql.Timestamp"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.text.*"%>
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

<%
	List<ChatMessage> messages = (List<ChatMessage>) request.getAttribute("messages");
%>

<center>
<p id="msg"></p>
<table border="1px">
<tr>
<th>好友名称</th>
<th>时间</th>
<th>消息</th>
<th>操作</th>
</tr>
<%
	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	for(ChatMessage message : messages) {
		int senderId = message.getSenderId();
		int receiverId = message.getReceiverId();
		int friendId = (receiverId == user.getUid()) ? senderId : receiverId;
		User friend = DBConnector.getUser(friendId);
		String photoUrl = DBConnector.getPhotoUrl(friendId);
		out.println("<tr>");
		out.println("<td><a href='user.jsp?id=" + friendId + "'><img src=" + photoUrl + " height='30' width='30'/>" 
			+ friend.getUsername()  + "</a></td>");
		out.println("<td>" + sdf.format(message.getTs()) + "</td>");
		String content = message.getContent();
		if(content.length() > 15) {
			content = content.substring(0, 15);
		}
		out.println("<td>" + content + "</td>");
		out.println("<td><a href='send_receive?friend=" + friendId + "'>回复</a></td>");
		out.println("</tr>");
	}
%>
</table>
<br/>
<a href="main.jsp">返回</a>
</center>


</body></html>
