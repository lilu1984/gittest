<%@page import="com.wonders.util.PropertiesUtil"%>
<%@page language="java" pageEncoding="utf-8"%>
<%
//生成随机数
	String ca_random = (Math.random()+"").substring(2,15);
	ca_random=ca_random+System.currentTimeMillis();
	//将随机数放入session
	session.setAttribute("ca_random",ca_random);
	
	String isTest = PropertiesUtil.getInstance().getProperty("is_test");
%>



<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>请登录</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<link href="jqui/toastr/toastr.css" rel="stylesheet" type="text/css">
<link href="jqui/beAlert/BeAlert.css" rel="stylesheet" type="text/css">
</head>
<script src="jqui/toastr/toastr.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/beAlert/BeAlert.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>

<script src="javascript/jquery-1.12.2.min.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/toastr/toastr.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/beAlert/BeAlert.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/jquery-1.12.2.min.js" type="text/javascript" charset="utf-8"></script>
<script  type="text/javascript" charset="utf-8">


function login(){
	var pin = document.getElementById("userName").value;
	if(pin == ""){
		showInfo("请输入密码");
		return;
	}
	//搜索设备
	var sof = window.longmai;
	var devices = sof.SOF_EnumDevice();
		
	 if(devices == null){
		 showInfo("没有找到任何蓝牙设备，请确认设备已开启");
	}else{
		var devicess = devices.split("||");
		var device=devicess[0];

		//连接设备
  		var rtn = sof.SOF_Connect(device);
  		if(rtn == 0){
  			//找到容器
  	  		var containers = sof.SOF_EnumContainers();
  			if(containers == null){
  				showInfo("未找到任何容器");
  			}else{
  				var containerss = containers.split("||");
  				var container = containerss[0];
  				//随机数
  				var ca_random = document.getElementById("random").value;
  		  		var rtnLog = sof.SOF_Login(pin);
  		  		if(rtnLog == 0){
  		  			//进行数字签名
  		  			var sign=sof.SOF_SignData(container, ca_random);
  		  			//证书信息
  		  			var msg=sof.SOF_ExportUserCert(container);
  		  			
  					window.location.href="monitor.do?method=toLogin&cert="+msg+"&signTure="+sign;
	  		  		
  		  		
  		  		}else{
  		  			sof.SOF_Disconnect();
  		  			showInfo("登录失败,还有" + rtnLog+"次机会");
  		  		}
  		  		
  		  		
  			}
  		}
	} 
}


</script>

<body class="bg-grey">
<section class="login">
	<header>
    	<span><img src="skin/images/login-user.jpg" alt=""></span>
    </header>
    <h5>请插入CA之后输入密码</h5>
    <form ID="CALoginForm" name="CALoginForm" action="monitor.do?method=toLogin&cert="+msg>
    <%if("0".equals(isTest)){ %>
    <input name="random" type="hidden" id="random" value="<%=ca_random %>" >
<!--     	<input type="password" placeholder="请输入密码"> -->
		<input type="hidden" name="signTure" id="signTure"/>
		<input id="userName" value="" type="password" >
    	<button class="full-btn" type="button" onclick="login()">登录</button>
    <%} else{%>
    <input id="user" value="" type="text"/>
    <button class="full-btn" type="button" onclick="login4test()">登录t</button>
    <%} %>
    </form>
    

</section>
<%if(!"0".equals(isTest)){ %>
<script type="text/javascript">
function login4test(){
	var cert = document.getElementById("user").value;
	window.location.href="monitor.do?method=toLogin&cert="+cert;
}
</script>
<%} %>
</body>
</html>
