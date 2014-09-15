<%@ page contentType="text/html; charset=utf8" %>

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

function checkUsername(uname) {
	createXMLHttp();
	xmlHttp.open("GET", "check_username?username="+encodeURI(uname));
	xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState == 4) {
				if(xmlHttp.status == 200) {
					var text = xmlHttp.responseText;
					if(text == "true") {
						document.getElementById("uname_msg").innerHTML = "<font color='green'>此用户名未被占用！</font>";
					} else {
						document.getElementById("uname_msg").innerHTML = "<font color='red'>此用户名已被占用！</font>";
					}
				}
			}
	};
	xmlHttp.send(null);
	document.getElementById("uname_msg").innerHTML = "<font color='green'>正在验证...</font>";
}

var emailOK = false;
function checkEmail(email) {
	if(email.replace(/\s+/g,'')=='') {
		document.getElementById("email_msg").innerHTML = "<font color='red'>邮箱不能为空！</font>";
		return;
	}
	createXMLHttp();
	xmlHttp.open("GET", "check_email?email="+encodeURI(email));
	xmlHttp.onreadystatechange = function(){
			if(xmlHttp.readyState == 4) {
				if(xmlHttp.status == 200) {
					var text = xmlHttp.responseText;
					if(text == "ok") {
						emailOK = true;
						document.getElementById("email_msg").innerHTML = "<font color='green'>可使用此邮箱！</font>";
					} else if(text == "exist"){
						emailOK = false;
						document.getElementById("email_msg").innerHTML = "<font color='red'>此邮箱已被占用！</font>";
					} else {
						emailOK = false;
						document.getElementById("email_msg").innerHTML = "<font color='red'>此邮箱不可用！</font>";
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

function onChangePassword() {
	document.getElementById("msg").innerHTML = "";
}

function checkPassword() {
	var p1 = document.getElementById("pwd").value;
	var p2 = document.getElementById("pwd2").value;
	if(p1!=p2) {
		document.getElementById("msg").innerHTML = "<font color='red'>两次密码不一致</font>";
		//document.getElementById("pwd2").focus();
		return false;
	} else {
		if(p1.replace(/\s+/g,'')=='') {
			document.getElementById("msg").innerHTML = "<font color='red'>密码不能为空</font>";
			return false;		
		} else {
			document.getElementById("msg").innerHTML = "<font color='green'>两次密码一致</font>";
			return true;		
		}
	}
}

function checkinfo(f) {
	var pattern = new RegExp("");

	if(f.username.value.replace(/\s+/g,'')=='') {
		alert('用户名不能为空！');
		f.username.focus();
		return false;
	} else if(f.password.value.replace(/\s+/g,'')=='') {
		alert('密码不能为空！');
		f.password.focus();
		return false;
	} else if(checkPassword() == false) {
		alert('密码不一致');
		f.password.focus();
		return false;
	} else if(f.sex.value.replace(/\s+/g,'')=='') {
		alert('性别不能为空！');
		//f.sex.focus();
		return false;
	} else if(f.email.value.replace(/\s+/g,'')=='') {
		alert('邮箱不能为空！');
		f.email.focus();
		return false;
	} else if(checkDate(f.birthday.value) == false) {
	    alert('生日错误！');
		f.birthday.focus();
		return false;
	} else if(emailOK == false) {
		alert('邮箱错误！');
		f.email.focus();
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
String msg = (String)request.getAttribute("msg");

if(msg!=null && !msg.isEmpty()) {
%>

<center>
<p><font color="red">
<%=msg%>
</font></p>
</center>

<%
}
%>

<form action="register?action=register" method="post" onsubmit='return checkinfo(this);'>
<center>
<table>
<tr>
  <td>用户名：</td>
  <td><input type="text" name="username" size="25" onblur="checkUsername(this.value)"></td>
  <td>*</td>
</tr>
</tr><tr>
  <td></td>
  <td id="uname_msg"></td>
 </tr><tr>
<tr>
  <td>昵称：</td>
  <td><input type="text" name="nickname" size="25"></td>
</tr>
<tr>
  <td>密码：</td>
  <td><input type="password" name="password" id="pwd" size="25" onfocus="onChangePassword()" onblur="checkPassword()"></td>
  <td>*</td>
</tr>
<tr>
  <td>确认密码：</td>
  <td><input type="password" name="password2" id="pwd2" size="25" onfocus="onChangePassword()" onblur="checkPassword()"></td>
  <td>*</td>
</tr><tr>
  <td></td>
  <td id="msg"></td>
 </tr><tr>
  <td>性别：</td>
  <td><input type="radio" name="sex" value="0" />男
	  <input type="radio" name="sex" value="1"/>女
  </td>  <td>*</td>
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
</tr>
<tr>
  <td></td>
  <td id="email_msg"></td>
</tr>
</table>
</center>

<center><br/>
  <input type="submit" value="注册">
  <input type="reset" name="Reset" value="清空"><br/><br/>
  <a href="login">返回</a>
</center>

</form>



</body></html>