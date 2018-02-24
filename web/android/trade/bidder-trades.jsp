<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.wonders.tdsc.bo.TdscBidderView"%>
<%@page import="java.util.List"%>
<%@page language="java" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>我的交易</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
List list = (List) request.getAttribute("list");
%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_me.jsp"></jsp:include>
<section>
<%
if(list != null && list.size() > 0){
	for(int i=0; i < list.size(); i++){
		TdscBidderView view = (TdscBidderView) list.get(i);
		String state = "";
		String bg = "";
		if("00".equals(view.gettResult())){
			bg = "交易中";
			state = "state02";
		}else if("01".equals(view.gettResult())){
			bg = "已成交";
			state = "state01";
		}else if("02".equals(view.gettResult())){
			bg = "流拍";
			state = "state04";
		}else if("04".equals(view.gettResult())){
			bg = "已终止";
			state = "state04";
		}else if("05".equals(view.gettResult())){
			bg = "等待结果";
			state = "state03";
		}
%>
    <dl class="sell-box <%=state %>"  onclick="window.open('myTrade.do?method=toMyTradeInfo&appId=<%=view.getAppId()%>&bidderId=<%=view.getBidderId()%>')">
        <dt>
            <h5><%=StringUtils.trimToEmpty(view.getBlockNoticeNo()) %></h5>
            <span class="state-bg"><%=bg %></span>
        </dt>
        <dd>
            <h5 style="width: 100%">
			<%
				if("00".equals(view.gettResult()))
					out.println("起始价"+view.getInitPrice()+"万元");
				else if("01".equals(view.gettResult()))
					out.println(view.getResultName()+"以"+view.getResultPrice()+"万元竞得");
				else if("02".equals(view.gettResult()))
					out.println("由国土资源局收回");
				else if("04".equals(view.gettResult()))
					out.println("由国土资源局终止");
				else if("05".equals(view.gettResult()))
					out.println(view.getPartakeBidderConNum() + "号等待结果");
			%>
			</h5>
        </dd>
    </dl>
<%
	}
}else{
%>
<dl class="listed-blocks"><dt><h5 align="center">目前还未有交易记录。</h5>
<%
}
%>
</section>

</body>
</html>
