<!doctype html>
<%@page import="com.wonders.util.PropertiesUtil"%>
<%@page import="com.wonders.tdsc.bo.TdscBidderApp"%>
<%@page import="com.wonders.android.AppConsts"%>
<%@page import="com.wonders.tdsc.bo.TdscListingApp"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.wonders.esframework.common.util.DateUtil"%>
<%@page import="com.wonders.wsjy.bo.TdscTradeView"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page language="java" pageEncoding="utf-8"%>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>挂牌详情</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<link href="jqui/toastr/toastr.css" rel="stylesheet" type="text/css">
<link href="jqui/beAlert/BeAlert.css" rel="stylesheet" type="text/css">
<link href="skin/perfect-scrollbar.min.css" rel="stylesheet" type="text/css"><!--内部滚动条-->
<script src="javascript/jquery-1.12.2.min.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/perfect-scrollbar.min.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/trade.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/encodeUtils.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/toastr/toastr.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/beAlert/BeAlert.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
	TdscTradeView tradeView = (TdscTradeView)request.getAttribute("tradeView");
	List listingAppList = (List)request.getAttribute("listingAppList");
	String canSendPrice = (String)request.getAttribute("canSendPrice");
	int bidderNum = (Integer)request.getAttribute("bidderNum");
	int lookingNum = (Integer)request.getAttribute("lookingNum");
	String tranStatus = (String)request.getAttribute("tranStatus");
	TdscBidderApp bidderApp = (TdscBidderApp)request.getAttribute("bidderApp");
	String userId = "";
	
	if(request.getAttribute("userId")!=null){
		userId = (String)request.getAttribute("userId");
	}
	//当前价格
	BigDecimal currPrice = new BigDecimal(0);;
	if(listingAppList!=null&&listingAppList.size()>0){
		currPrice = ((TdscListingApp)listingAppList.get(0)).getListPrice();
	}
	String strXj = "无限价";
	if(tradeView.getMaxPrice()!=null){
		strXj = "￥"+tradeView.getMaxPrice() + "万元";
	}
	String stateClass = "state03";
	String strTranStatus = "竞价中";
	String strTimeLimit = "交易未开始";
	if(AppConsts.TRAN_STATUS_ACC.equals(tranStatus)){
		stateClass = "state01";
		strTranStatus = "受理中";
		strTimeLimit = "交易未开始";
	}else if(AppConsts.TRAN_STATUS_LISTING.equals(tranStatus)){
		stateClass = "state02";
		strTranStatus = "挂牌中";
		strTimeLimit = "倒计时加载中......";
	}else if(AppConsts.TRAN_STATUS_AUCTION.equals(tranStatus)){
		stateClass = "state03";
		strTranStatus = "竞价中";
		strTimeLimit = "倒计时加载中......";
	}else if(AppConsts.TRAN_STATUS_WAITING.equals(tranStatus)){
		stateClass = "state03";
		strTranStatus = "待竞价";
		strTimeLimit = "等待竞价中......";
	}else if(AppConsts.TRAN_STATUS_END.equals(tranStatus)){
		stateClass = "state04";
		strTranStatus = "已结束";
		if("02".equals(tradeView.getTranResult())){
			//流拍
			strTimeLimit = "该地块由国土局收回";
		}else if("04".equals(tradeView.getTranResult())){
			strTimeLimit = "该地块已中止交易";
		}else if("01".equals(tradeView.getTranResult())){
			if(tradeView.getOnLineEndDate()!=null){
				//公告已结束，显示成交人
				strTimeLimit = "该地块由"+tradeView.getResultName() + "竞得";
			}else{
				//公告未结束，显示成交号牌
				strTimeLimit = "该地块由"+tradeView.getResultCert() + "号竞买人竞得";
			}
			
		}else if("05".equals(tradeView.getTranResult())){
			stateClass = "state03";
			strTranStatus = "等待结果";
			strTimeLimit = tradeView.getPartakeBidderConNum() +"号竞买人参与现场竞公益配套";
		}
	}
%>

<jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
<body class="bg-grey" onunload="disconnect()">
<input type="hidden" value="<%=userId %>" name="userId" id="userId">
<input type="hidden" value="<%=tradeView.getAppId() %>" name="appId" id="appId">
<input type="hidden" value="<%=tradeView.getNoticeId() %>" name="noticeId" id="noticeId">
<input type="hidden" value="<%=tradeView.getInitPrice() %>" name="initPrice" id="initPrice">
<input type="hidden" value="<%=tradeView.getPriceAdd() %>" name="priceAdd" id="priceAdd">
<input type="hidden" value="<%=tranStatus %>" name="tranStatus" id="tranStatus">
<input type="hidden" value="<%=currPrice %>" name="topPrice" id="topPrice">
<% if("101".equals(tradeView.getBlockQuality())){%>
	<input type="hidden" name="totalBuildingArea" id="totalBuildingArea" value="<%= tradeView.getTotalLandArea() %>"/>
<% }else{%>
	<input type="hidden" name="totalBuildingArea" id="totalBuildingArea" value="<%= tradeView.getTotalBuildingArea() %>"/>
<% }%>
<input type="hidden" value="<%=tradeView.getTotalBuildingArea() %>" name="totalBuildingArea" id="totalBuildingArea">
<input type="hidden" value="<%=tradeView.getBlockQuality()%>" name="blockQuality" id="blockQuality">
<input type="hidden" value="<%=lookingNum %>" name="lookingNum" id="lookingNum">

<%	String xjStr = "";
	if(tradeView.getMaxPrice()!=null){ 
		xjStr = tradeView.getMaxPrice().toString();	
	}
%>
<input type="hidden" value='<%=xjStr %>' name="xj" id="xj">
<input type="hidden" value='<%=canSendPrice %>' name="canSendPrice" id="canSendPrice">
<input type="hidden" value='<%=bidderApp.getConNum() %>' name="conNum" id="conNum">
<input type="hidden" value='<%=PropertiesUtil.getInstance().getProperty("app_ip") %>' name="serverIp" id="serverIp">
<section>
	<div class="details">
    	<h5>(<%=tradeView.getBlockNoticeNo() %>)<%=tradeView.getBlockName() %></h5>
        <!-- h6><%=tradeView.getLandLocation() %></h6-->
        <div class="time-limit"> <%=strTimeLimit %></div>
        <div class="price <%=stateClass%>">
        	<%if("00".equals(tradeView.getTranResult())){ %>
        	当前价<span class="state-font" id="curTopPrice"><%=((currPrice.intValue()==0)?"无":"￥" + currPrice + "万元") %></span>
        	<%}else if("01".equals(tradeView.getTranResult())){ %>
        	成交价<span class="state-font" id="curTopPrice">￥<%=tradeView.getResultPrice() %> 万元</span>
			<%}else if("02".equals(tradeView.getTranResult())||"04".equals(tradeView.getTranResult())){ %>
				<span class="state-font" id="curTopPrice"></span>
			<%} %>
			<b class="state-bg"><img src="skin/images/listed-tit.png" alt=""><%=strTranStatus%></b>
        </div>
        <ul class="details-price <%=stateClass%>">
        	<li>溢价率&nbsp;<span id='overPriceRate' class="state-font">300%</span></li>
			<li>&nbsp;&nbsp;</li>
			<li>楼面价&nbsp;<span id='buildPrice' class="state-font">22000</span></li>
        </ul>
        <ul class="details-info">
        	<li><img src="skin/images/time-limit03.png" alt=""><%=bidderApp.getConNum() %>号</li>
        	<li><img src="skin/images/time-limit01.png" alt=""><%=bidderNum %>人报名</li>
        	<li><img src="skin/images/time-limit02.png" alt=""><%=lookingNum %>人关注</li>
        </ul>

    </div>
    <ul class="rule">
    	<li>起始价<span>￥<%=tradeView.getInitPrice() %>万元</span></li>
    	<li>加价幅度<span>￥<%=tradeView.getPriceAdd() %>万元</span></li>
    	<li>限价<span><%=strXj %></span></li>
    </ul>
    <div class="record">
    	<h5>出价记录<span id="priceCount">(<%=listingAppList.size() %>)</span><b>下滑查看更多</b></h5>
        <div class="record-scroll" id="record-scroll">
        	<ul>
        	<%for(int i=0;i<listingAppList.size();i++){ 
        		TdscListingApp listingApp = (TdscListingApp)listingAppList.get(i);
        		String priceTypeCn = "挂";
        		if("11".equals(listingApp.getPriceType())){
        			priceTypeCn = "挂";
        		}else if("22".equals(listingApp.getPriceType())){
        			priceTypeCn = "竞";
        		}
        	%>
            	<li <%if(i==0){ %>class="first"<%} %>>
                	<dl>
                    	<dt>(<%=priceTypeCn %>)<%=listingApp.getListNo() %>号<br>第<%=listingApp.getPriceNum() %>轮</dt>
                        <dd><span>￥<%=listingApp.getListPrice() %></span>万元</dd>
                    </dl>
                </li>
             <%} %>
            </ul>
        </div>
    </div>
    <%if("1".equals(canSendPrice)){ %>
    <div class="my-operate">
        <div class="do-list">
        	<ul>
            	<li></li>
            	<li></li>
            	<li></li>
            </ul>
            <button class="full-btn" id="do-list-btn" type="button">我要挂牌</button>
        </div>
    
    	<div class="do-quote">
        	<dl>
            	<dt><button class="quote-min" type="button" onclick='minusprice()'></button></dt>
            	<dd>
                	<span>￥</span>
                    <input type="text" value="" id="inputPrice">
                	<span>万元</span>
                </dd>
            	<dt><button class="quote-plus" type="button" onclick='addprice()'></button></dt>
            </dl>
            <button class="full-btn" id="do-quote-btn" type="button" onclick='sendPrice()'>确认出价</button>
        </div>
    </div>
    <%} %>
</section>
 <jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
</body>
</html>
