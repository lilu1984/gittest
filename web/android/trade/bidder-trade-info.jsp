<%@page import="com.wonders.tdsc.bo.TdscBidderView"%>
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
<title>交易详情</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
TdscTradeView tV = (TdscTradeView) request.getAttribute("tradeView");
TdscBidderView bV = (TdscBidderView) request.getAttribute("bidderView");
String bankId = bV.getBankId();
String bankName = "";
Map map = DicDataUtil.getInstance().getDic(18314); 
if( map.get(bankId)!=null){
	bankName = (String)map.get(bankId);
}
%>
<body class="bg-grey">

<section>
	<div class="order">
    	<ul>
            <%if(StringUtils.isNotBlank(bV.getConNum())){ %>
            <li>
            	<h6>我的号牌</h6>
            	<span><%=StringUtils.trimToEmpty(bV.getConNum()) %></span>
            </li>	
            <%} %>
            <li>
            	<h6>地块交易状态</h6>
            	<span><%="00".equals(tV.getTranResult()) ? "交易中" : ("01".equals(tV.getTranResult()) ? "已成交" : ("02".equals(tV.getTranResult()) ? "流拍" : ("04".equals(tV.getTranResult()) ? "已终止" : "等待结果"))) %></span>
            </li>
            <%if("00".equals(tV.getTranResult())){ %>
            <li>
            	<h6>挂牌开始时间</h6>
            	<span><%=DateUtil.date2String(tV.getListStartDate(), DateUtil.FORMAT_DATETIME) %></span>
            </li>
            <li>
            	<h6>挂牌结束时间</h6>
            	<span><%=DateUtil.date2String(tV.getListEndDate(), DateUtil.FORMAT_DATETIME) %></span>
            </li>
            <li>
            	<h6>保证金到账截止</h6>
            	<span><%=DateUtil.date2String(tV.getMarginEndDate(), DateUtil.FORMAT_DATETIME) %></span>
            </li>
            <%}else if("01".equals(tV.getTranResult())){ %>
            <li>
            	<h6>竞得人</h6>
            	<span><%=StringUtils.trimToEmpty(tV.getResultName()) %></span>
            </li>
            <li>
            	<h6>成交价</h6>
            	<span><%=NumberUtil.numberDisplay(tV.getResultPrice(), 2)%>万元</span>
            </li>
            <li>
            	<h6>成交时间</h6>
            	<span><%=DateUtil.date2String(tV.getResultDate(), DateUtil.FORMAT_DATETIME) %></span>
            </li>
            <%}else if("02".equals(tV.getTranResult())){ %>
            
            <%}else if("04".equals(tV.getTranResult())){ %>
            
            <%}else if("05".equals(tV.getTranResult())){ %>
            <li>
            	<h6>等待结果的号牌</h6>
            	<span><%=StringUtils.trimToEmpty(tV.getPartakeBidderConNum()) %></span>
            </li>
            <%} %>
        </ul>
    </div>
</section>

</body>
</html>