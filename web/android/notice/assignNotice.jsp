<!DOCTYPE HTML>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="com.wonders.tdsc.bo.TdscNoticeApp" %>
<%@page import="java.util.*" %>
<%@page import="com.wonders.esframework.common.model.PageList" %>
<%@page import="com.wonders.esframework.common.model.Pager" %>
<%@page import="com.wonders.tdsc.bo.condition.TdscNoticeAppCondition" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.wonders.esframework.util.DateUtil"%>
<html>
<head>
<meta charset="utf-8" name="viewport" content="width=device-width,initial-scale=1.0"/>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<script src="../android/javascript/jquery.min.js"></script>
<script src="../android/javascript/scroll2Load.js"></script>
<script src="../android/javascript/bootstrap.min.js"></script>
<link href="../android/skin/hook.css" type="text/css" rel="stylesheet"/>
<link rel="stylesheet" href="../android/skin/bootstrap.min.css">
<title>国有用地使用权出让公告</title>
<style>
.title_font{font-size:115%}
</style>
<script type="text/javascript">
$(function() {
	var asssignUrl=$("#asssignUrl").val();
    $("#contentList").scroll2Load({
    	first       :	10,
	 	url			:	asssignUrl,
	 	count		:	5,
	 	progressBar :   "#hook-div",
	 	end			:   "<table align='center'><tr><td class='text-muted'>已经没有更多可加载内容了！</td></tr></table>"
		  });
});

</script>
</head>
<%
	//获得已出让公告列表信息
	PageList pageList=(PageList)request.getAttribute("tdscNoticeAppPageList");
	String assignNoticeUrl=(String)request.getAttribute("assignNoticeUrl");
	List queryNewNoticeList=null;
	Pager pager=null;
	if(pageList!=null){
		queryNewNoticeList=pageList.getList();
		pager=pageList.getPager();
	}
	
	%>
<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_index.jsp"></jsp:include>
<input type="hidden" name="asssignUrl" id="asssignUrl" value="<%=assignNoticeUrl%>" />
<section>
<article id="contentList">
<HR style="margin-bottom:5px;margin-top:10px" >
<%if(queryNewNoticeList!=null&&queryNewNoticeList.size()>0){
		for(int i=0;i<queryNewNoticeList.size();i++){
		TdscNoticeApp tdscNoticeApp=(TdscNoticeApp)queryNewNoticeList.get(i);
								%>
<table id="contentTable" border="0" width="100%" align="center" cellpadding="0" cellspacing="0"  onClick="window.open('<%=tdscNoticeApp.getUrl()%>')">
  		<tr>
  			<td width="1%">&nbsp;&nbsp;&nbsp;</td>
            <td width="1%">&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td align="left" colSpan="2"><span class="title_font">国有用地使用权出让公告</span></td>
  	      	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 	      			
  			<td width="1%">&nbsp;&nbsp;&nbsp;</td>
  			
  		</tr>
  		<tr>
  			<td height="5px" colSpan="5"></td> 			
  		</tr>
  		<tr>
  			<td width="1%">&nbsp;&nbsp;&nbsp;</td>
            <td width="1%">&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td align="left"><p class="text-muted small"><%=tdscNoticeApp.getNoticeNo() %></p></td> 	      		
  			<td align="right"><p class="text-muted small"><%=DateUtil.date2String(tdscNoticeApp.getNoticeDate(),DateUtil.FORMAT_DATE )%></p></td> 
  			<td width="1%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
  		</tr>
</table>
<HR style="margin:5px" >
	<%} 						
} %>
</article>

<div id="hook-div" style="margin:0 auto;text-align: center; width:200px;">
<div class="hook-loader" align="center"><div class="hook-spinner"/></div></div>
<div id="aaa"></div>
</div>

</section>

</body>
</html>