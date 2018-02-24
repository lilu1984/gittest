<%@page import="com.wonders.tdsc.common.GlobalConstants"%>
<%@page import="com.wonders.esframework.dic.util.DicDataUtil"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page language="java" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>选择竞买号牌</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<style type="text/css">
.btn1{font-size:30px; width:75px; height:75px; line-height:75px; text-align:center; color:#fff; font-family:Arial, Helvetica, sans-serif; background:url(../skin/images/num_available.png)  no-repeat; border:0;}
.btn11{font-size:30px; width:75px; height:75px; line-height:75px; text-align:center; color:#fff; font-family:Arial, Helvetica, sans-serif; background:url(../skin/images/num_unavailable.png)  no-repeat; border:0;}
</style>
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
Set set = (Set)request.getAttribute("containsNum");
String noticeId = (String)request.getAttribute("noticeId");
String bidderId = (String)request.getAttribute("bidderId");
String appId = (String) request.getAttribute("appId");
Map map = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_CON_NUM);
Collection valueSet = map.values();
Iterator it = valueSet.iterator();

String msg = (String) request.getAttribute("msg");
%>
<body class="bg-grey">
<form action="#" name="conNumForm" method="post">
<input type="hidden" name="noticeId" value="<%= StringUtils.trimToEmpty(noticeId) %>">
<input type="hidden" name="bidderId" value="<%= StringUtils.trimToEmpty(bidderId) %>">
<input type="hidden" name="appId" value="<%= StringUtils.trimToEmpty(appId) %>">
<input type="hidden" name="conNum">
</form>
<section>
<%if("conflict".equals(msg)){ %>
<script>
alert("此号牌已被其他竞买人选取，请另外选取号牌!");
</script>
<%} %>
<div align="center">
<ul>
<%
int tr = 0;
int td = 0;
while(it.hasNext()) { 
	String getNum = (String)it.next();
%>
<% if (td%4==0) {%>
<% if (td!=0) { %></li><% } %><li><% } %>
<% if(set.contains(getNum)) {%>
				<input type="button" name="conNum" class="btn11" value="<%= getNum %>" disabled="disabled"/>
			<%} else {%>
				<input type="button" name="conNum" class="btn1" value="<%= getNum %>" onclick="selectNum(this.value)"/>
			<%} %>
<%
	td++;
}
%>
</ul>
</div>
</section>
<script>
function selectNum(value) {
	if(confirm('您选择的号牌为：' + value + '，是否确认选择该号牌?')){
		document.conNumForm.conNum.value = value;
		document.conNumForm.action="myTrade.do?method=saveCon";
		document.conNumForm.submit();
	}
}
</script>
</body>
</html>