﻿<%@ page contentType="text/html; charset=utf8" %>

<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
%>
<html><head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>

<script type="text/javascript">

var xmlHttp;

function createXMLHttp() {
	if(window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	} else {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

var emailOK = false;
function checkEmail(email) {
	if(email.replace(/\s+/g,'')=='') {
		document.getElementById("msg").innerHTML = "<font color='red'>邮箱不能为空！</font>";
		return;
	}
	createXMLHttp();
	xmlHttp.open("POST", "check_email?email="+encodeURI(email));
	xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState == 4) {
				if(xmlHttp.status == 200) {
					var text = xmlHttp.responseText;
					if(text == "true") {
						emailOK = true;
						document.getElementById("msg").innerHTML = "<font color='green'>可使用此邮箱！</font>";
					} else {
						emailOK = false;
						document.getElementById("msg").innerHTML = "<font color='red'>此邮箱已注册本站！</font>";
					}
				}
			}
	};
	xmlHttp.send(null);
	document.getElementById("msg").innerHTML = "<font color='green'>正在验证...</font>";
}

function checkDate(dateStr) {
	var pattern = new RegExp("\\d{4}\\-\\d{2}-\\d{2}");
	return pattern.test(dateStr);
}

function checkinfo(f) {
	var pattern = new RegExp("");

	if(f.email.value.replace(/\s+/g,'')=='') {
		alert('邮箱不能为空！');
		f.email.focus();
		return false;
	} else if(emailOK == false) {
	    alert('邮箱已注册本站！');
		f.email.focus();
		return false;
	} else if(checkDate(f.birthday.value) == false) {
	    alert('生日错误！');
		f.birthday.focus();
		return false;
	}
		
	return true;
}

</script>

</head>
<body >
<p><center>
<font size=+3><b>欢迎注册漂流瓶应用</b></font>
</center></p><hr>

<%
String f_uid = (String)request.getAttribute("f_uid");
String Name = (String)request.getAttribute("Name");
String ScreenName = (String)request.getAttribute("ScreenName");
String Gender = (String)request.getAttribute("Gender");
String imgUrl = (String)request.getAttribute("imgUrl");
%>

<center>
<p><font color="green">
还需一步，即可完成
</font></p>
</center>

<%
%>

<form action="register?action=foreign_register" method="post" onsubmit='return checkinfo(this);'>
 <input type="hidden" name="f_uid" value="<%=f_uid%>" />
 <input type="hidden" name="username" value="<%=Name%>" />
 <input type="hidden" name="nickname" value="<%=ScreenName%>" />
 <input type="hidden" name="imgUrl" value="<%=imgUrl%>" />
<center>
<table>
<tr>
<td>
<img src='<%=imgUrl%>'/>
</td>
<td>
<table>
<tr>
  <td>用户名：</td><td><%=Name%></td>
</tr><tr>
  <td>昵称：</td><td><%=ScreenName%></td>
</tr><tr>
<% if(Gender.equals("m")) { %>
  <td>性别：</td><td>男</td>
  <td><input type="hidden" name="sex" value="male" /></td>
<% } else if(Gender.equals("f")) { %>
  <td>性别：</td><td>女</td>
  <td><input type="hidden" name="sex" value="female" /></td>
<% } else { %>
  <td><input type="radio" name="sex" value="male" />男
	  <input type="radio" name="sex" value="female"/>女
  </td>  <td>*</td>
<%} %>
</tr>
<tr>
  <td>生日：</td>
  <td><input type="date" name="birthday" value="1990-01-01"></td>
</tr>
<tr>
  <td>学校：</td>
  <td><input type="text" name="school" size="25"></td>
</tr>
<tr>
  <td>院系：</td>
  <td><input type="text" name="department" size="25"></td>
</tr>
<tr>
  <td>专业：</td>
  <td><input type="text" name="major" size="25"></td>
</tr>
<tr>
  <td>入学年份：</td>
  <td><input type="number" name="enrollYear" max="2100" min="0" size="25"></td>
</tr>
<tr>
  <td>邮箱：</td>
  <td><input type="email" name="email" size="25" onblur="checkEmail(this.value)"></td>
  <td>*</td>
</tr><tr>
  <td></td>
  <td id="msg"></td>
</tr>
</table>
</td>
</tr>
</table>
</center>

<center><br/>
  <input type="submit" value="注册">
  <input type="reset" name="Reset" value="清空"><br/><br/>
  <a href="login.jsp">返回</a>
</center>

</form>



</body></html>