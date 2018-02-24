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
<title>订单详情</title>
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
    	<%if(StringUtils.isBlank(bV.getConNum()) && "00".equals(bV.getBzjDzqk())){ %>
    		<li>
    			<h6>我的号牌</h6>
    			<span><a style="text-decoration: underline; color: red" href="myTrade.do?method=toSelectCon&noticeId=<%=bV.getNoticeId()%>&bidderId=<%=bV.getBidderId() %>&appId=<%=bV.getAppId()%>">请选择号牌</a></span>
    		</li>
    	<%} %>
        	<li>
            	<h6>地块编号</h6>
                <span><%= StringUtils.trimToEmpty(tV.getBlockNoticeNo()) %></span>
            </li>
            <li>
            	<h6>地块坐落</h6>
            	<span><%= StringUtils.trimToEmpty(tV.getLandLocation()) %></span>
            </li>
            <li>
            	<h6>起始价</h6>
            	<span><%=NumberUtil.numberDisplay(tV.getInitPrice(), 2) %>万元</span>
            </li>
            <li>
            	<h6>保证金数额</h6>
            	<span><%=NumberUtil.numberDisplay(tV.getMarginAmount(), 2) %>万元</span>
            </li>
            <li>
            	<h6>保证金账号</h6>
            	<span><%=StringUtils.trimToEmpty(bV.getBankNumber()) %></span>
            </li>
            <li>
            	<h6>保证金银行</h6>
            	<span><%=bankName %></span>
            </li>
            <li>
            	<h6>已交纳保证金</h6>
            	<%if(bV.getBzjDzse()!=null){%>
            	<span><%=NumberUtil.numberDisplay(bV.getBzjDzse(), 2) %>万元</span>	
            	<% }else{%>
            	<span>未缴纳</span>	
            	</li>
            	<li>
            	<h6>保证金交纳截止时间</h6>
            	<span><%=DateUtil.date2String(bV.getMarginEndDate(), DateUtil.FORMAT_DATETIME) %></span>
            	<%} %>
            	
            </li>
            <li>
            	<h6>保证金到账时间</h6>
            	<%if(bV.getBzjDzsj()!=null){%>
            		<span><%=DateUtil.date2String(bV.getBzjDzsj(), DateUtil.FORMAT_DATETIME) %></span>
            	<% } else{%>
            		<span>&nbsp;</span>
            	<% }%>            	
            </li>
            <li>
            	<h6>保证金到账情况</h6>
            	<%if(bV.getBzjDzqk()!=null){%>
            		<span><%="00".equals(bV.getBzjDzqk()) ? "按时足额到账" : ("01".equals(bV.getBzjDzqk()) ? "逾期足额到账" : "逾期未足额") %></span>
            	<% } else{%>
            		<span>未足额到账</span>
            	<% }%>
            	
            </li>
            <%if(StringUtils.isNotBlank(bV.getConNum())){ %>
            <li>
            	<h6>我的号牌</h6>
            	<span><%=bV.getConNum() %></span>
            </li>
            <%} %>
        </ul>
    </div>
</section>

</body>
</html>