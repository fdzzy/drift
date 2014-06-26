<%@ page contentType="text/html; charset=utf8" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ include file="common.jsp" %>
<%@ include file="check_login.jsp" %>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>漂流瓶</title>

<script type="text/javascript">
function checkinfo(f) {
	if(f.content.value.replace(/\s+/g,'')=='') {
		alert('请输入漂流瓶内容!');
		f.content.focus();
		return false;
	}
	return true;
}
</script>

</head>
<body>
<p><center>
<font size=+3><b><%=user.getUsername()%>，欢迎使用漂流瓶应用</b></font>
</center></p><hr/>

<form action="do_post" method="post" onsubmit='return checkinfo(this)'>
<center>
<table>
<tr>
	<td><textarea rows="10" cols="50" name="content" placeholder="请输入漂流瓶内容"></textarea></td>
</tr>
</table>
</center>

<center><br/>
  <input type="submit" value="提交">
  <input type="reset" name="Reset" value="清空"><br/><br/>
  <a href="main.jsp">返回</a>
</center>
</form>

<center><br/>
  
</center>

</body></html>
