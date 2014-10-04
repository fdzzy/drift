<%@page import="com.drift.service.impl.ServiceFactory"%>
<%@page import="com.drift.service.UserService"%>
<%@page import="com.drift.servlet.MyServletUtil"%>
<%@page import="com.drift.bean.User"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@ page contentType="text/html; charset=utf8" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ include file="common.jsp" %>
<%@ include file="check_login.jsp" %>

<%
User friend = (User)request.getAttribute("friend");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>
<style type="text/css">
* {
	margin: auto;
	padding: 0px;
}

html,body {
	width: 90%;
	height: 90%;
	background: #eeeeda;
}

.panel {
	border: 2px solid #0078ae;
	border-radius: 5px;
	width: 100%;
}

.message-area {
	height: 70%;
	resize: none;
	box-sizing: border-box;
}

.input-area {
	background: #0078ae;
	box-shadow: inset 0 0 10px #00568c;
}

.input-area input {
	margin: 0.5em 0em 0.5em 0.5em;
}

.text-field {
	border: 1px solid grey;
	padding: 0.2em;
	box-shadow: 0 0 5px #000000;
}

.button {
	border: none;
	padding: 5px 5px;
	border-radius: 5px;
	width: 60px;
	background: orange;
	box-shadow: inset 0 0 10px #000000;
	font-weight: bold;
}

.button:hover {
	background: yellow;
}

h1 {
	font-size: 1.5em;
	padding: 5px;
	margin: 5px;
}

#messageInput {
	min-width: 80%;
	max-width: 80%;
}
</style>

<script type="text/javascript">

function autoScroll() {
	document.getElementById("messages").scrollTop = document.getElementById("messages").scrollHeight;
}

var xmlHttp = null;
var friendId = <%=friend.getUid()%>;
var friendName = "<%=friend.getUsername()%>";

function createXMLHttp() {
	if(xmlHttp != null)
		return;
	if(window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	} else {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function checkMessage() {
	createXMLHttp();
	var url = "send_receive?action=receive&friend=" + friendId;
	xmlHttp.open("GET", url, true);

	xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState == 4) {
				if(xmlHttp.status == 200) {
					var text = xmlHttp.responseText;
					if(text == "false") {
						window.window.location.href = 'login';
					}
					var res = eval("(" + text +")");
					var newMessageFlag = 0;
					for(x in res) {
						newMessageFlag = 1;
						document.getElementById("messages").innerHTML += res[x].ts + " ";
						document.getElementById("messages").innerHTML += friendName;
						document.getElementById("messages").innerHTML += "\n" + res[x].content + "\n";
					}					
				}
				if(newMessageFlag) {
					autoScroll();
				}
			}
	};
	
	xmlHttp.send(null);
}

function sendMessage() {
	createXMLHttp();
	var url = "send_receive?action=send&friend=" + friendId;
	var params = encodeURI('content=' + document.getElementById("messageInput").value);
	xmlHttp.open("POST", url, true);

	//Send the proper header information along with the request
	xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	//xmlHttp.setRequestHeader("Content-length", params.length);
	//xmlHttp.setRequestHeader("Connection", "keep-alive");

	xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState == 4) {
				if(xmlHttp.status == 200) {
					//var text = xmlHttp.responseText;
					var res = eval("(" + xmlHttp.responseText +")");
					var newMessageFlag = 0;
					//if(res.newMessageCount > 0) {
					//	document.getElementById("messages").innerHTML += "<a href='my_bottle'><font color='green'>您有条" + res.newMessageCount + "新消息</font></a>";
					//} else {
					//	document.getElementById("messages").innerHTML = "";
					//}
					for(x in res) {
						newMessageFlag = 1;
						document.getElementById("messages").innerHTML += res[x].ts + " ";
						if(res[x].senderId == friendId) {
							document.getElementById("messages").innerHTML += friendName;
						} else {
							document.getElementById("messages").innerHTML += "我";
						}
						document.getElementById("messages").innerHTML += "\n" + res[x].content + "\n";
					}	
					if(newMessageFlag) {
						autoScroll();
					}
				}				
			}
	};
	
	document.getElementById("messageInput").value = "";
	xmlHttp.send(params);

}

setInterval(checkMessage, 1000);
window.onload = function() {
	autoScroll();
}
</script>

</head>
<body>
<p><center>
<font size=+3><b><%=user.getUsername()%>，欢迎使用漂流瓶应用</b></font>
</center></p><hr/>
	
<%

List<ChatMessage> messages = (List<ChatMessage>)request.getAttribute("messages");


if(messages == null || friend == null) {
	out.println("请稍后重试！");
%>
  <a href="main">返回</a>
<%
	return;
}

UserService service = ServiceFactory.createUserService();
String friendPhotoUrl = service.getFullPhotoUrl(friend); 
String myPhotoUrl = service.getFullPhotoUrl(user); 
%>

<a href="show_user?id=<%=friend.getUid()%>">
<center><%=friend.getUsername()%>
  <img src="<%=friendPhotoUrl%>" height="30" width="30"/></center></a>

<textarea id="messages" class="panel message-area" disabled="true">
<%
	int myUid = user.getUid();
	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//Collections.reverse(messages);
	for(ChatMessage msg : messages) {
		String tsStr = null;
		try {
			tsStr = sdf.format(msg.getTs());	
		} catch(Exception e) {
		}
		if(tsStr != null) {
			out.print(tsStr);
		} else {
			out.print(msg.getTs());
		}
		if(msg.getSenderId() == myUid) {
			out.println(" 我");
		} else {
			out.println(" " + friend.getUsername() + "");
		}
		out.println(msg.getContent() + "");
	}
%> 
</textarea>
	<div class="panel input-area">
		<input id="messageInput" class="text-field" type="text"
			placeholder="Message"
			onkeydown="if (event.keyCode == 13) sendMessage();" /> 
			<input class="button" type="submit" value="发送" onclick="sendMessage();" />
			<input class="button" type="submit" value="返回" onclick="window.window.location.href = 'my_bottle';" />
	</div>


</body></html>