<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.drift.servlet.MyServletUtil"%>
<%@ page contentType="text/html; charset=utf8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改头像</title>
<script src="http://open.web.meitu.com/sources/xiuxiu.js" type="text/javascript"></script>
<script type="text/javascript">
window.onload=function(){
	xiuxiu.setLaunchVars("quality", 100);
    xiuxiu.embedSWF("altContent",5,"100%","100%");
       /*第1个参数是加载编辑器div容器，第2个参数是编辑器类型，第3个参数是div容器宽，第4个参数是div容器高*/
    xiuxiu.setUploadType(2);
    xiuxiu.setUploadURL("<%=MyServletUtil.entryURL%>" + "/upload");
    xiuxiu.onBeforeUpload = function(data, id){
    	var size = data.size;
    	if(size > 512 * 1024) {
    		alert("图片不能超过512K");
    		return false;
    	}
        xiuxiu.setUploadArgs({filetype: data.type, type: "image", filename: data.name });
        return true;
    }

    xiuxiu.onInit = function ()
    {
        xiuxiu.loadPhoto("");
    }
    xiuxiu.onUploadResponse = function(data)
    {
        if(data){
            alert("头像设置成功");       
            window.close();
            //location.href="edit_profile"; 
            window.opener.location.href="edit_profile";
        }else{
            alert("头像设置失败");    
        }
    }
}
</script>
<style type="text/css">
    html, body { height:100%; overflow:hidden; }
    body { margin:0; }
</style>
</head>
<body>
<div id="altContent">
    <h1>美图秀秀</h1>
</div>
</body>
</html>