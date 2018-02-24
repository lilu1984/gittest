<!doctype html>
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
List blockList = (List) request.getAttribute("blockList");
String format = "MM月dd日 HH:mm";
%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
<%
if(blockList != null && blockList.size() > 0){
	TdscTradeView v = (TdscTradeView) blockList.get(0);
	String planId = v.getPlanId();
%>
<input type="hidden" name="planId" value="<%=planId%>"/>
<section>
	
	
<%
	for(int i = 0; i < blockList.size(); i++){
		TdscTradeView app = (TdscTradeView) blockList.get(i);
		String state_bg = "";
		String state = "";
		/* long today = new Date().getTime();
		if(today >= app.getAccAppStatDate().getTime() && today < app.getListStartDate().getTime()){//当前时间在受理开始时间和挂牌开始时间之间
			state_bg = "受理中";			
			state = "state01";
		}else if(today >= app.getListStartDate().getTime() && today <= app.getListEndDate().getTime()){
			state_bg = "挂牌中";
			state = "state02";
		}
		if(app.getOnLineStatDate() != null && app.getOnLineEndDate() == null){
			if("2".equals(app.getTradeStatus())){
				state_bg = "待竞价";
				state = "state03";
			}
			else{
				state_bg = "竞价中";
				state = "state03";
			}
			
		}
		if(app.getOnLineEndDate() != null){
			state_bg = "已结束";			
			state = "state04";
		} */
		
		
		Date nowDate = new Date();
    	if(!"00".equals(app.getTranResult())){
       		//交易结束的地块
    		state_bg = "已结束";			
			state = "state04";
    	}else if(nowDate.after(app.getAccAppStatDate())&&nowDate.before(app.getListStartDate())){
    		//处于受理还未开始挂牌阶段
    		state_bg = "受理中";			
			state = "state01";
    	}else if(nowDate.after(app.getListStartDate())&&nowDate.before(app.getListEndDate())){
    		//处于挂牌期间
    		state_bg = "挂牌中";
			state = "state02";
    	}else if(app.getTradeStatus()!=null&&
    			nowDate.after(app.getSceBidDate())&&
    			app.getOnLineStatDate()!=null&&
    			app.getOnLineEndDate()==null
    			){
    		//处于竞价期
    		if("3".equals(app.getTradeStatus())){
    			//tradeStatus==2时处于等待期
    			state_bg = "待竞价";
				state = "state03";
    		}else
    		{
    			state_bg = "竞价中";
    			state = "state03";
    		}
    		
    	} 
%>
    <dl class="listed-blocks <%=state %>" onclick="location.href='monitor.do?method=toBlockInfo&appId=<%=app.getAppId()%>'" >
        <dt>
            <h5><%
            if("受理中".equals(state_bg)){
            	//out.println(DateUtil.date2String(app.getListStartDate(), format) + "&nbsp;开始");
            }else if("挂牌中".equals(state_bg)){
            	//out.println(DateUtil.date2String(app.getListEndDate(), format) + "&nbsp;结束");
            }else if("竞价中".equals(state_bg)){
            	//out.println("&nbsp;");
            }else if("已结束".equals(state_bg)){
            	//out.println(DateUtil.date2String(app.getOnLineEndDate(), format) + "&nbsp;交易结束");
            }
            %></h5>
			
            <span class="state-bg"><%=state_bg %></span>
           
            <b class="state-font"><%
            if("受理中".equals(state_bg)){
            	out.println("￥" + app.getInitPrice() + "万元");
            }else if("挂牌中".equals(state_bg)){
            	out.println(app.getGuapaiCurrPrice() != null ? "￥" + app.getGuapaiCurrPrice() + "万元"  : "暂无报价");
            }else if("竞价中".equals(state_bg)){
            	out.println("￥" + app.getGuapaiCurrPrice() + "万元");
            }else if("待竞价".equals(state_bg)){
            	out.println("￥" + app.getGuapaiCurrPrice() + "万元");
            }else if("已结束".equals(state_bg)){
            	if("02".equals(app.getTranResult())){
            		out.println("流拍");
            	}else{
            		out.println("￥" + app.getResultPrice() + "万元");
            	}
            	
            }
            %></b>
        </dt>
        <dd> 
            <h5><%=StringUtils.trimToEmpty(app.getBlockNoticeNo()) %></h5>
            <h6><%=StringUtils.trimToEmpty(app.getLandLocation()) %></h6>
        </dd>
    </dl>
<%
    }
%>
<%
}else{
%>
<dl class="listed-blocks"><dt><h5 align="center">当前公告下无地块，请联系管理员。</h5></dt></dl>
<%} %>
</section>
</body>
</html>
