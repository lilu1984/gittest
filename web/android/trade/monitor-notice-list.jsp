<!doctype html>
<%@page import="com.wonders.android.AppConsts"%>
<%@page import="com.wonders.esframework.common.util.DateUtil"%>
<%@page import="com.wonders.wsjy.bo.TdscTradeView"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>挂牌出让公告</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<link href="jqui/toastr/toastr.css" rel="stylesheet" type="text/css">
<link href="jqui/beAlert/BeAlert.css" rel="stylesheet" type="text/css">
<script src="javascript/jquery-1.12.2.min.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/toastr/toastr.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/beAlert/BeAlert.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
List noticeList = (List) request.getAttribute("planList");
%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
<%
if(noticeList != null && noticeList.size() > 0){
%>
<section>
<%
	for(int i = 0; i < noticeList.size(); i++){
		TdscTradeView app = (TdscTradeView) noticeList.get(i);
		String state_bg = "";
		String state = "";
		if(AppConsts.TRAN_STATUS_ACC.equals(app.getTradeStatus())){
			state_bg = "受理中";			
			state = "state01";
		}else if(AppConsts.TRAN_STATUS_LISTING.equals(app.getTradeStatus())){
			state_bg = "挂牌中";
			state = "state02";
		}else if(AppConsts.TRAN_STATUS_AUCTION.equals(app.getTradeStatus())){
			state_bg = "竞价中";
			state = "state03";
		}else if(AppConsts.TRAN_STATUS_END.equals(app.getTradeStatus())){
			state_bg = "已结束";			
			state = "state04";
		}
%>
    <dl class="sell-box <%=state %>" onclick="location.href='monitor.do?method=toBlockList&planId=<%=app.getPlanId()%>'">
        <dt>
            <h5><%=StringUtils.trimToEmpty(app.getNoitceNo())%></h5>
            <span class="state-bg"><%=state_bg %></span>
        </dt>
        <dd>
            <h5><%
            	long today = new Date().getTime();
            	if("受理中".equals(state_bg)){
            		out.println("受理截止时间");
            	}else if(!"已结束".equals(state_bg)){
            		if(today <= app.getAccAppEndDate().getTime()){
            			out.println("受理截止时间");
            		}else{
            			out.println("挂牌截止时间");
            		}
            	}else{
            		out.println("交易结束时间");
            	}
            %></h5>
            <span><%
            	if("受理中".equals(state_bg)){
            		out.println(DateUtil.date2String(app.getAccAppEndDate(), DateUtil.FORMAT_DATETIME));
            	}else if(!"已结束".equals(state_bg)){
            		if(today <= app.getAccAppEndDate().getTime()){
            			out.println(DateUtil.date2String(app.getAccAppEndDate(), DateUtil.FORMAT_DATETIME));
            		}else{
            			out.println(DateUtil.date2String(app.getListEndDate(), DateUtil.FORMAT_DATETIME));
            		}
            	}else{
            		out.println(DateUtil.date2String(app.getOnLineEndDate(), DateUtil.FORMAT_DATETIME));
            	}%></span>
        </dd>
    </dl>
<%
    }
%>
<%
}else{
%>
<dl class="listed-blocks"><dt><h5 align="center">当前无公告正在受理或交易。</h5>
<%} %>

</section>
</body>
</html>
