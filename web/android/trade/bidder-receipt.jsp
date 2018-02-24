<%@page import="com.wonders.esframework.common.util.DateUtil"%>
<%@page import="com.wonders.esframework.util.NumberUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.wonders.esframework.dic.util.DicDataUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.wonders.tdsc.bo.TdscBidderApp"%>
<%@page import="com.wonders.wsjy.bo.TdscTradeView"%>
<%@page language="java" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>申购订单</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
TdscTradeView view = (TdscTradeView) request.getAttribute("tdscTradeView");
TdscBidderApp bidder = (TdscBidderApp) request.getAttribute("bidderApp");
String bankId = (String) request.getAttribute("bankId");
String bankName = "";
Map map = DicDataUtil.getInstance().getDic(18314); 
if( map.get(bankId)!=null){
	bankName = (String)map.get(bankId);
}
%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
<section>
	<div class="order">
    	<ul>
        	<li>
            	<h6>竞买号</h6>
                <span><%= StringUtils.trimToEmpty(bidder.getSqNumber()) %></span>
            </li>
        	<li>
            	<h6>地块编号</h6>
                <span><%= StringUtils.trimToEmpty(view.getBlockNoticeNo()) %></span>
            </li>
        	<li>
            	<h6>保证金帐号</h6>
                <span><%= StringUtils.trimToEmpty(bidder.getBankNumber()) %></span>
            </li>
        	<li>
            	<h6>开户银行</h6>
                <span><%= StringUtils.trimToEmpty(bankName) %></span>
            </li>
        	<li>
            	<h6>开户名称</h6>
                <span><%=DicDataUtil.getInstance().getDicItemName(18317,bankId) %></span>
            </li>
        	<li>
            	<h6>保证金金额</h6>
                <span><%= NumberUtil.numberDisplay(view.getMarginAmount(), 2) %>万元</span>
            </li>
        	<li>
            	<h6>交纳开始时间</h6>
                <span><%= DateUtil.date2String(view.getAccAppStatDate(), DateUtil.FORMAT_DATETIME) %></span>
            </li>
        	<li>
            	<h6>到账截止时间</h6>
                <span><%= DateUtil.date2String(view.getMarginEndDate(), DateUtil.FORMAT_DATETIME) %></span>
            </li>
        </ul>
        <h5>注：凭此子帐号，请到相关银行交纳保证金</h5>
        <div style="padding:0 1em;"><button class="full-btn" onclick="location.href='myTrade.do?method=toMyOrders'" type="button">完成</button></div>
    </div>
</section>

</body>
</html>