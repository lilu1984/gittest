<%@page import="com.wonders.wsjy.bo.TdscTradeView"%>
<%@page language="java" pageEncoding="utf-8"%>
<%@page import="org.apache.struts.util.TokenProcessor"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>选择缴纳银行</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<script src="javascript/jquery-1.12.2.min.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/beAlert/BeAlert.js" type="text/javascript" charset="utf-8"></script>
<link href="jqui/beAlert/BeAlert.css" rel="stylesheet" type="text/css">
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
</head>
<%
TdscTradeView view = (TdscTradeView) request.getAttribute("view");
//申请书申请传递参数
String uploadFileId = (String)request.getAttribute("uploadFileId");
String bidderType = (String)request.getAttribute("bidderType");
String isCreateComp = (String)request.getAttribute("isCreateComp");
%>
<body class="bg-grey">
<%
	TokenProcessor.getInstance().saveToken(request);
 %>
<jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
 <form action='"myTrade.do?method=toBuy&appId=<%=view.getAppId()%>&bankId="+v+""' method="post" id="buyForm">
 	<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="<%=session.getAttribute("org.apache.struts.action.TOKEN")%>" />
 	<input type="hidden" id="uploadFileId" name="uploadFileId" value="<%=uploadFileId%>"/>
	<input type="hidden" id="bidderType" name="bidderType" value="<%=bidderType%>"/>
	<input type="hidden" id="isCreateComp" name="isCreateComp" value="<%=isCreateComp%>"/>
<section>
	<ul class="bank">
	<%if("101".equals(view.getBlockQuality())&&
    		(view.getDistrictId().intValue()==1||view.getDistrictId().intValue()==3||view.getDistrictId().intValue()==4||view.getDistrictId().intValue()==2)){ %>
    	<%if(view.getDistrictId().intValue()==1){ %>
    		<li><a alt="建行迎宾分理处" id="bank21" onclick="selectBank('21')" href="#"><img src="skin/images/bank02.png" >建行迎宾分理处</a></li>
    	<%} %>
    	<%if(view.getDistrictId().intValue()==3){ %>
    		<li><a alt="江苏银行梁青路支行" id="bank16" onclick="selectBank('16')" href="#"><img src="skin/images/bg.png" >江苏银行梁青路支行</a></li>
    	<%} %>
    	<%if(view.getDistrictId().intValue()==4){ %>
    		<li><a alt="建行新区支行" id="bank17" onclick="selectBank('17')" href="#"><img src="skin/images/bank02.png" >建行新区支行</a></li>
    	<%} %>
    	<%if(view.getDistrictId().intValue()==2){ %>
    		<li><a alt="兴业银行惠山支行" id="bank15" onclick="selectBank('15')" href="#"><img src="skin/images/bank04.png" >兴业银行惠山支行</a></li>
    	<%} %>
    <%}else{ %>
    		<li><a alt="兴业银行无锡城南支行" id="bank5" onclick="selectBank('5')" href="#"><img src="skin/images/bank04.png" >兴业银行无锡城南支行</a></li>
    		<li><a alt="宁波银行无锡分行" id="bank19" onclick="selectBank('19')" href="#"><img src="skin/images/bg.png" >宁波银行无锡分行</a></li>
    		<li><a alt="交通银行新区支行" id="bank4" onclick="selectBank('4')" href="#"><img src="skin/images/bg.png" >交通银行新区支行</a></li>
    		<li><a alt="江苏银行梁溪支行" id="bank3" onclick="selectBank('3')" href="#"><img src="skin/images/bg.png" >江苏银行梁溪支行</a></li>
    		<li><a alt="工商银行城中支行" id="bank7" onclick="selectBank('7')" href="#"><img src="skin/images/bank05.png" >工商银行城中支行</a></li>
    		<li><a alt="农业银行南长支行" id="bank1" onclick="selectBank('1')" href="#"><img src="skin/images/bank06.png" >农业银行南长支行</a></li>
    		<li><a alt="农业银行新梁溪支行" id="bank2" onclick="selectBank('2')" href="#"><img src="skin/images/bank06.png" >农业银行新梁溪支行</a></li>
    		<li><a alt="中国建设银行城北支行" id="bank22" onclick="selectBank('22')" href="#"><img src="skin/images/bank02.png" >中国建设银行城北支行</a></li>
    		<li><a alt="中国建设银行城南支行" id="bank9" onclick="selectBank('9')" href="#"><img src="skin/images/bank02.png" >中国建设银行城南支行</a></li>
    		<li><a alt="中国银行无锡分行" id="bank8" onclick="selectBank('8')" href="#"><img src="skin/images/bank01.png" >中国银行无锡分行</a></li>
    		<li><a alt="广东发展银行无锡城北支行" id="bank6" onclick="selectBank('6')" href="#"><img src="skin/images/bg.png" >广东发展银行无锡城北支行</a></li>
    		<li><a alt="无锡华夏银行锡东支行" id="bank26" onclick="selectBank('26')" href="#"><img src="skin/images/bg.png" >无锡华夏银行锡东支行</a></li>
    		<li><a alt="浦发银行无锡分行营业部" id="bank25" onclick="selectBank('25')" href="#"><img src="skin/images/bank03.png" >浦发银行无锡分行营业部</a></li>
    		<li><a alt="中信银行长江路支行" id="bank12" onclick="selectBank('12')" href="#"><img src="skin/images/bg.png" >中信银行长江路支行</a></li>
    <%} %>
    	<li><a alt="其他银行" id="bank99" onclick="selectBank('99')" href="#"><img src="skin/images/bg.png" >其他银行</a></li>
    </ul>
</section>
</form>
<script type="text/javascript">
function selectBank(v){
	var bankName = $("#bank"+v+"").attr("alt");
	confirm("", "是否确认向"+bankName+"交纳保证金？", function (isConfirm) {
        if (isConfirm) {        	       	
        	$("#buyForm").attr("action", "myTrade.do?method=toBuy&appId=<%=view.getAppId()%>&bankId="+v+""); 
        	$("#buyForm").submit();
        } else{}
    }, {confirmButtonText: '是', cancelButtonText: '否', width: 400});
}
</script>
</body>
</html>