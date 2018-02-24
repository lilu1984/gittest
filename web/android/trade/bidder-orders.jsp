<!DOCTYPE HTML>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.wonders.tdsc.bo.TdscBidderView" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.*" %>
<%@page import="com.wonders.esframework.common.util.DateUtil"%>
<%@page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>我的订单</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<% 
	//设置编码
	response.setCharacterEncoding("utf-8");
	//获得申购列表
	List tdscBidderOrdersList=(List)request.getAttribute("bidderOrdersList");
%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_me.jsp"></jsp:include>
<section>
<%
	//循环列表得出申购信息
	if(tdscBidderOrdersList!=null&&tdscBidderOrdersList.size()>0){
		for(int i=0;i<tdscBidderOrdersList.size();i++){
			TdscBidderView tdscBidderView=(TdscBidderView)tdscBidderOrdersList.get(i);
			String state = "";
			if("足额到账".equals(tdscBidderView.getBzjDzqk()))
				state = "01";
			else 
				state = "03";
			%>
	<dl class="sell-box state<%=state %>" onclick="window.location.href='myTrade.do?method=toMyOrderInfo&appId=<%=tdscBidderView.getAppId()%>&bidderId=<%=tdscBidderView.getBidderId()%>'">
		<dt>
	    	<h5><%=tdscBidderView.getBlockNoticeNo() %></h5>
	        <span class="state-bg"><%=tdscBidderView.getBzjDzqk() %></span>
	    </dt>    
	    <dd >	    	
	    <%if(!"足额到账".equals(tdscBidderView.getBzjDzqk())){ %>
	    	<h5 ">保证金截止<%=DateUtil.date2String(tdscBidderView.getMarginEndDate(), DateUtil.FORMAT_DATETIME)%></h5>
	    <%}else{ 
	    		if(StringUtils.isBlank(tdscBidderView.getConNum())){
	    %>
	    	<h5 ">请点此进入详情并选择号牌</h5>
	    <%} else {%>
	    	<h5 ">已选择号牌：<%=tdscBidderView.getConNum() %></h5>
	    <%} %>
	    </dd>
	</dl>
	    <% 
		}
		%>
	<% }
		}else{%>
		<dl class="listed-blocks"><dt><h5 align="center">目前还未有申购记录。</h5>
	<% }
%>
</section>
</body>
</html>
