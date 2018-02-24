<%@page import="java.math.BigDecimal"%>
<%@page import="com.wonders.tdsc.common.util.MoneyUtils"%>
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
<title>竞买申请书</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
TdscTradeView view = (TdscTradeView) request.getAttribute("view");
%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
<section>
	<div class="application">
    	<h5>无锡市国土资源局：</h5>
        <p>经认真阅读编号为<%=view.getBlockNoticeNo() %>的挂牌出让文件及《无锡市国有建设用地使用权网上交易规则（试行）》（以下简称《规则》），
        	我方完全接受并愿意遵守你局国有建设用地使用权挂牌出让文件和《规则》中的全部规定和要求，对所有文件均无异议。</p>
        <p>我方现正式申请参加<%=view.getNoitceNo() %>国有建设用地使用权挂牌网上交易竞买活动。</p>
        <p>我方承诺履行国有建设用地使用权挂牌出让文件的规定和要求，如有违反，承担相应的法律责任。</p>
        <p>若委托他人的，我方委托唯一受托人参加本次出让活动。</p>
        <p>特此申请。</p>
        <button class="full-btn" type="button" onclick="location.href='myTrade.do?method=chooseBidderType&appId=<%=view.getAppId()%>'">下一步</button>
    </div>
</section>

</body>
</html>