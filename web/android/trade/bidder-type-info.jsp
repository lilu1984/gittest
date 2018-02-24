<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.wonders.tdsc.bo.TdscBlockAppView"%>
<%@page import="com.wonders.tdsc.bo.TdscBlockAppView"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	//上传文件业务主键
	String uploadFileId = String.valueOf(System.currentTimeMillis());
	String appId = (String)request.getAttribute("appId");
%>
<html>
<head>
<meta charset="utf-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no;" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>申购方式</title>
<link href="skin/commons.css" rel="stylesheet" type="text/css">
<link href="skin/style.css" rel="stylesheet" type="text/css">
<link href="jqui/toastr/toastr.css" rel="stylesheet" type="text/css">
<link href="jqui/beAlert/BeAlert.css" rel="stylesheet" type="text/css">
<script src="javascript/jquery-1.12.2.min.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/toastr/toastr.js" type="text/javascript" charset="utf-8"></script>
<script src="jqui/beAlert/BeAlert.js" type="text/javascript" charset="utf-8"></script>
<script src="javascript/common.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
function tj(){
	if ($("#bidderTypeSelect option:selected").val()=="请选择") {
		alert('请选择竞买方式');
		return false;
	}

	if ($("#newCompType option:selected").val()=="请选择") {
		alert('请选择是否成立新公司');
		return false;
	}
	//$('#bidderType').val($('input[name="bidderTypeSelect"]:checked').val());
	$('#bidderType').val($("#bidderTypeSelect option:selected").val());
	//$('#isCreateComp').val($('input[name="newCompType"]:checked').val());
	$('#isCreateComp').val($("#newCompType option:selected").val());
	$('#selectForm').attr("action","myTrade.do?method=toBankList");
	$('#selectForm').submit();
}


function showPic(){
    var pic = $("#pic").get(0).files[0];
	showConfirm('您确定要上传文件吗?','',function(confirm){
		if(confirm){
			var html = '<li class="progress"><a><img src="' + window.URL.createObjectURL(pic) + '" alt=""><span>0%</span></a></li>'
			$('.add').before(html);
			uploadFile();
		}
	});
}

function uploadFile(){
    var pic = $("#pic").get(0).files[0];
    var formData = new FormData();
    formData.append("file" , pic);
    var url = "/app/up/uploadify/jmsq?catalog=06&uploadFileId=<%=uploadFileId%>";
    /**   
     * 必须false才会避开jQuery对 formdata 的默认处理   
     * XMLHttpRequest会对 formdata 进行正确的处理   
     */ 
    $.ajax({
       type: "POST",
       url: url,
       data: formData ,
       processData : false,  
       //必须false才会自动加上正确的Content-Type   
       contentType : false , 
       xhr: function(){
            var xhr = $.ajaxSettings.xhr();
            if(onprogress && xhr.upload) {
                xhr.upload.addEventListener("progress" , onprogress, false);
                return xhr;
            }
        },
        success: onsuccess
    });
}


/**
 *    侦查附件上传情况    ,这个方法大概0.05-0.1秒执行一次
 */
function onprogress(evt){
    var loaded = evt.loaded;                  //已经上传大小情况 
    var tot = evt.total;                      //附件总大小 
    var per = Math.floor(100*loaded/tot);      //已经上传的百分比  
    $(".progress span").html( per +"%" );
}
function onsuccess(evt){
	$('.add').prev().removeClass('progress');
	showInfo('文件上传成功!');
}

</script>
</head>

<body class="bg-grey">
<jsp:include page="../commonjsp/bottom_trade.jsp"></jsp:include>
<form id="selectForm" action="mytrade.do?method=toBankList" method="post">
<input type="hidden" name="appId" id="appId" value="<%=appId%>"/>
<input type="hidden" id="uploadFileId" name="uploadFileId" value="<%=uploadFileId%>"/>
<input type="hidden" id="bidderType" name="bidderType" />
<input type="hidden" id="isCreateComp" name="isCreateComp" />
<section>
	<ul class="order-choice">
    	<li>
        	<span>竞买方式</span>
            <select name="bidderTypeSelect" id="bidderTypeSelect">
            	<option>请选择</option>
            	<option value="1">单独竞买</option>
            	<option value="2">联合竞买</option>
            </select>
        </li>
    	<li>
        	<span>是否成立新公司</span>
            <select name="newCompType" id="newCompType">
            	<option>请选择</option>
            	<option value="1">是</option>
            	<option value="0">否</option>
            </select>
        </li>
    </ul>
    <div class="order-upload">
    	<h5>附件上传<span></span></h5>
        <ul>
        <!-- 
        <li><a><img src="images/upload01.jpg" alt=""></a></li>
        	<li><a><img src="images/upload01.jpg" alt=""></a></li>
        	<li><a><img src="images/upload01.jpg" alt=""></a></li>
        	<li><a><img src="images/upload01.jpg" alt=""></a></li>
         -->
        	<li class="add"><a><input type="file" id="pic" onchange="showPic()"></a></li>
        	
        </ul>
    </div>
    <div style="padding:1em;"><button class="full-btn" onclick="tj()" type="button">下&nbsp;&nbsp;一&nbsp;&nbsp;步</button></div>
</section>
</form>
</body>
</html>