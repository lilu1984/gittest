<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.wonders.wsjy.bo.PersonInfo"%>
<%@page language="java" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>个人中心</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>

<%
PersonInfo person = (PersonInfo) request.getAttribute("person");
%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_me.jsp"></jsp:include>
<section>
	<div class="myself">
    	<div class="myself-header">
        	<span><img src="skin/images/company.jpg" alt=""></span>
            <h4><%=StringUtils.trimToEmpty(person.getBidderName()) %></h4>
            <h5>数字证书有效期至</h5>
            <h6><%=session.getAttribute("date") %></h6>
        </div>
        <ul>
            <li><a href="myTrade.do?method=toMyOrders"><img src="skin/images/myself01.png" alt="">我的订单</a></li>
            <li><a href="myTrade.do?method=toMyTrades"><img src="skin/images/myself02.png" alt="">我的交易</a></li>
            <li><a href="#"><img src="skin/images/myself03.png" alt="">我的信息</a></li>
            <li><a href="javascript:logout()"><img src="skin/images/myself04.png" alt="">退出账号</a></li>
        </ul>
    </div>
</section>

</body>
</html>
