$(function (){
	ip = $("#serverIp").val();
	var lookingNum = $("#lookingNum").val();
	//判断当前观摩人数,若超过逾期人数则停止接入
	if(parseFloat(lookingNum)>maxLookingNum){
		showWarning('当前观摩人员过多，请稍后访问。');
		return;
	}
	
	//内部滚动条调用
    $('#record-scroll').perfectScrollbar();
    price_input_init();
    changeValue();
    //清空倒计时变量
    limit=0;
    var tranStatus = $('#tranStatus').val();
    if(tranStatus=='1'||tranStatus=='2'||tranStatus=='3'){
    	connect();
    }
});
var ip;
//最高观摩人数
var maxLookingNum = 2000;
var webSocket;
function connect() {
	var url = 'ws://'+ ip +'/wsgm/wsClient'; 
    if ('WebSocket' in window) {  
    	webSocket = new WebSocket(url);  
    } else if ('MozWebSocket' in window) {  
    	webSocket = new MozWebSocket(url);  
    } else {  
    	showWarning("您的浏览器不支持WebSocket。");  
        return ;  
    }  
    webSocket.onerror = function(event) {  
        showError("未链上服务器");  
    };
    webSocket.onopen = function(event) {  
    	initClient();
    };
    webSocket.onmessage = function(event) {  
    	opFactory(event);
    };  
}

//向服务器端发送消息
function sendMsg(msg){
	webSocket.send(encodeBase80(msg));
}
/**
 * 断开链接
 */
function disconnect() {  
    if (webSocket != null) {  
    	webSocket.close();  
    	webSocket = null;  
    }  
} 
//将字符串转化为json对象
function parseObj( strData ){
	return (new Function( "return " + strData ))();
}
var limit;//倒计时秒速
var intervalDownTime;//倒计时函数对象（执行downTimeCount到0时将clear）
//倒计时方法(将相差多少时间转化为秒，美秒执行一次)
function showCountDown()
{ 
	var day1=Math.floor(limit/(60*60*24)); 
	var hour=Math.floor((limit-day1*24*60*60)/3600); 
	var minute=Math.floor((limit-day1*24*60*60-hour*3600)/60); 
	var second=Math.floor(limit-day1*24*60*60-hour*3600-minute*60); 
	if($('#tranStatus').val()=='1'){
		$(".time-limit").html(day1+"天"+hour+"小时"+minute+"分"+second+"秒&nbsp;&nbsp;后进入竞价");
	}else{
		$(".time-limit").html(day1+"天"+hour+"小时"+minute+"分"+second+"秒&nbsp;&nbsp;后结束");
	}
	
	if(limit==0){
		window.clearInterval(intervalDownTime);
	}
	limit--;
}
//开始时间倒计时（用于挂牌情况）
function startDownTime(downSecond){
	downSecond--;
	var interval = 1000;
	limit = downSecond;
	window.clearInterval(intervalDownTime);
	intervalDownTime = window.setInterval(showCountDown, interval); 
}
/**
 * 初始化客户端
 */
function initClient(){
	var userId = $('#userId').val(); 
	var isBidder = true;
	var tranStatus = $('#tranStatus').val();
	if(userId==''||userId==null){
		//如果userId为空说明是访客，赋予GM001开头的随机用户
		userId = "GM001"+ new Date().getTime();
		isBidder = false;
	}
	//发送客户端初始化命令
	var strjson = '{"op":"21","clientNo":"'+ userId +'"}';
	sendMsg(strjson);
	if(!isBidder){//如果是游客发送监控申请
		//发送监控的公告id
		var noticeId = $('#noticeId').val(); 
		strjson = '{"op":"26","noticeId":"' + noticeId + '"}';
		sendMsg(strjson);
	}
}

//增加一个加价幅度
function addprice(){
	var maxPrice = $("#topPrice").val();
	if(maxPrice==0){
		maxPrice = $("#initPrice").val()
	}
	maxPrice = parseFloat(maxPrice);
	var addPrice = parseFloat($("#priceAdd").val());
	var xiajia =  0;
	if($("#xj").val()!=''||$("#xj").val()!=null){
		xiajia = parseFloat($("#xj").val());
	}
	if ($("#inputPrice").val() == '' || $("#inputPrice").val() <= maxPrice){
		$("#inputPrice").val(maxPrice + addPrice);
	} else {
		var remainder = ($("#inputPrice").val() - maxPrice) % addPrice;
		$("#inputPrice").val($("#inputPrice").val() - remainder + addPrice);
		//最高价限价
		if(xiajia!=0){
			if($("#inputPrice").val()>=xiajia){
				$("#inputPrice").val(xiajia);
			}
		}
	}
	
}

//减少一个加价幅度
function minusprice(){
	var maxPrice = $("#topPrice").val();
	if(maxPrice==0){
		maxPrice = $("#initPrice").val()
	}
	maxPrice = parseFloat(maxPrice);
	var addPrice = parseFloat($("#priceAdd").val());
	if ($("#inputPrice").val() == '' || $("#inputPrice").val() <= maxPrice){
		$("#inputPrice").val(maxPrice);
	} else {
		var remainder = ($("#inputPrice").val() - maxPrice) % addPrice;
		if (remainder == 0){
			$("#inputPrice").val($("#inputPrice").val() - addPrice);
		} else {
			$("#inputPrice").val($("#inputPrice").val() - remainder);
		}
	}
}
//出价
function sendPrice(){
	var appId = $("#appId").val();
	var price = $("#inputPrice").val();
	var priceAdd = parseFloat($("#priceAdd").val());
	var tranStatus = $("#tranStatus").val();
	var maxPrice = parseFloat($("#topPrice").val());
	var initPrice = parseFloat($("#initPrice").val());
	var validatePrice = parseFloat(priceAdd + maxPrice);
	if(price==''){
		showWarning('请输入报价！');
		return;
	}else if(price<validatePrice){
		showWarning('报价必须大于当前最高报价加一个加价幅度！');
		return;
	}else{
		showConfirm('您确定本次出价吗？','本次报价￥'+price+'万元，一旦确认不能撤销!',function(confirm){
			if(confirm){
				var json = '{"op":"22","appId":"' + appId + '","price":"' + price +'","phase":"'+tranStatus+'"}';
				sendMsg(json);
			}
		});
	}
}

/**
 * 更新溢价率、楼面价
 * @return
 */
function changeValue(){
	var value = $("#topPrice").val();
	if(value != ''){
		// 2显示溢价率
		$("#overPriceRate").html(overPriceRate(value,$("#initPrice").val()));
		// 3楼面价
		$("#buildPrice").html(buildPrice(value));
		return true;
	}
}
/**
 * 四舍五入
 * @param arg1
 * @param arg2
 * @param fix
 * @return
 */
function accAdd(arg1,arg2,fix){
	var fixNum = 2;
	if(accAdd.arguments.length > 2){fixNum = fix;}
	if(!isNaN(arg1) && !isNaN(arg2)){
		var r1,r2,m;
		try{r1=arg1.toString().split(".")[1].length;}catch(e){r1=0;}
		try{r2=arg2.toString().split(".")[1].length;}catch(e){r2=0;}
		m=Math.pow(10,Math.max(r1,r2));
		return ((arg1*m+arg2*m)/m) .toFixed(fixNum);
	}else return 0;
}
/**
 * 溢价率
 * @param num
 * @param initPrice
 * @return
 */
function overPriceRate(num, initPrice){
	// （当前的价格-起始价）/起始价 *100%
	if(num==0){
		return '0%';
	}
	var result = (num - initPrice)/initPrice;
	return accAdd((result * 100),0,2) + '%';
}

/**
 * 楼面地价
 * 
 * @param num
 * @param area
 * @return
 */
function buildPrice(num) {
	var blockQuality = $('#blockQuality').val();
	var getArea = $("#totalBuildingArea").val();
	if ('101' == blockQuality){
		if(num==0){
			var blockInitPrice=$('#initPrice').val();
			return accAdd(blockInitPrice/getArea*666.67,0,2) + "万元/亩";
		}
		return accAdd(num/getArea*666.67,0,2) + "万元/亩";
	} else if ('102' == blockQuality){
		// 当前价格×10000/建筑面积
		if(num==0){
			var blockInitPrice=$('#initPrice').val();
			return accAdd((blockInitPrice * 10000)/getArea,0,2)+"元/㎡";
		}
		return accAdd((num * 10000)/getArea,0,2)+"元/㎡";
	}
	return 0.0;
}
/**
 * 初始化报价按钮
 */
function price_input_init(){
	if($("#inputPrice").length==0){
		return;
	}
	var inputPrice = document.getElementById("inputPrice");
    if(document.all){//ie
    	//屏蔽由输入法输入字母和开头为0
    	inputPrice.onkeyup = function() {
    	    if (/[^\d]/g.test(this.value)) {
    		   this.value = this.value.replace(/[^\d]/g, '');
    		   return true;
    		} 
    		return false;
    	};
    	//不能拷贝
    	inputPrice.onpaste = function() {
            return false;
    	};
    	//不能拖拽
    	inputPrice.ondragenter = function() {
    	    return false;
    	};    	
    }else{//firefox、chrome
    	inputPrice.addEventListener(
        	"keyup",
        	function() {
                if (/[^\d]/g.test(this.value)) {
        		   this.value = this.value.replace(/[^\d]/g, '');
        		   return true;
        		} 
        		return false;
        	},
        	false
    	);
    	inputPrice.addEventListener(
        	"onpaste",
        	function() {
                return false;
        	},
        	false
    	);
    	inputPrice.addEventListener(
        	"ondragenter",
        	function() {
                return false;
        	},
        	false
    	);
    }
}


function opFactory(event){
	var str = decodeBase80(event.data);
	var obj = parseObj(str);
	var optionType = obj.op;
	if(optionType==11){
		//更新报价命令
		op11(obj);
	}else if(optionType==19){
		//已经添加如客户端连接池
		op19(obj);
	}else if(optionType==32){
		//更新挂牌倒计时
		op32(obj);
	}else if(optionType==17){
		//切换为竞价状态
		op17(obj);
	}else if(optionType==33){
		//接收到更新倒计时
		op33(obj);
	}else if(optionType==12){
		//接收到开始竞价命令
		op12(obj);
	}else if(optionType==13){
		//接收到一块地交易完成命令
		op13(obj);
	}else if(optionType==15){
		//接收到当前公告交易完成命令
		op15(obj);
	}else if (optionType == 63){ // 在其他地点登陆，退出
		showWarning("系统在另一地点已登录，系统将退出登录！");
		disconnect();
		window.setTimeout(logout,2000);
	}else if (optionType == 62){ // 报价成功
		op62(obj);
	}else if(optionType == 38) {
		//接收更新达到最高限价信息
		op38(obj);
		$("main_frame").contentWindow.op38order(obj);
	}else if(optionType == 40) {
		//接收更新摇号信息
		op40(obj);
	}
}


function op19(obj){
	initClient();
}

/**
 * 处理挂牌倒计时命令
 */
function op32(obj){
	var noticeId = $('#noticeId').val();
	for(var i=0;i<obj.info.length;i++){
    	if(noticeId==obj.info[i].noticeId){
    		var time = obj.info[i].time;
    		startDownTime(parseInt(time-1));
		}
    }
}
/**
 * 更新报价信息
 * @param obj
 */
function op11(obj){
	var currAppId = $('#appId').val();
	var conNum = obj.conNum;//号牌
	var price = obj.price;//价格
	var appId = obj.appId;//地块
	var priceNum = obj.priceNum;//轮次
	var phase = obj.phase;
	if(appId==currAppId){
		var stPriceType = "竞";
		if("1"==phase){
			stPriceType = "挂";
		}else if("2"==phase){
			stPriceType = "竞";
		}
		$("#curTopPrice").html("￥" + price + " 万元");
		$("#topPrice").val(price);
		changeValue();
		showInfo(conNum+"号报价"+price+"万元");
		var count = $("#priceCount").html().replace("(","").replace(")","");
		count = parseInt(count)+1;
		$("#priceCount").html("("+count+")");
		$("#record-scroll").find("ul li:first-child").each(function(){
			$(this).removeClass("first");
		});
		var insertHtml = '<li class="first"><dl><dt>('+stPriceType+')'+conNum+'号<br>第'+priceNum+'轮</dt><dd><span>￥'+price+'</span>万元</dd></dl></li>';
		$("#record-scroll").find("ul").prepend(insertHtml);
	}
}
/**
 * 切换到竞价状态
 * @param obj
 */
function op17(obj){
	var curNotice = $("#noticeId").val();
	if(curNotice==obj.noticeId){
		$('.state02').addClass('state03').removeClass('state02');
		$('.time-limit').html('等待竞价中......');
		$('.state-bg').html('<img src="skin/images/listed-tit.png">待竞价');
		closeShow();
		$("#tranStatus").val("2");
		if($('.my-operate').length!=0){
			$('.my-operate').hide();
		}
	}
}
/**
 * 更新竞价倒计时
 */
function op33(obj){
	var curAppId = $('#appId').val();
	if(curAppId==obj.appId){
		var time = obj.time;
		startDownTime(parseInt(time));
	}
}
/**
 * 开始竞价
 * @param obj
 */
function op12(obj){
	var curAppId = $('#appId').val();
	$("#tranStatus").val("2");
	if(curAppId==obj.appId){
		$('.state-bg').html('<img src="skin/images/listed-tit.png">竞价中');
		if($('.my-operate').length!=0){
			if($('#userId').length!=0){
				//竞买人情况显示报价
				$('.my-operate').show();
			}
		}
		
		//向当前服务器端索要竞价倒计时
		var strjson = '{"op":"24","noticeId":"' + obj.noticeId + '"}';
		sendMsg(strjson);
	}
}
/**
 * 一块地交易完成
 * @param obj
 */
function op13(obj){
	closeShow();
	var curAppId = $('#appId').val();
	var status = obj.status;
	if(curAppId==obj.appId){
		if(status=='1'){
			$('.state03').addClass('state04').removeClass('state03');
			window.clearInterval(intervalDownTime);
			$('.time-limit').html('该地块由'+ obj.conNum + '号竞买人竞得');
			var priceHtml = $('.price').html();
			$('.price').html(priceHtml.replace('当前价','成交价'));
			$('.state-bg').html('<img src="skin/images/listed-tit.png">已结束');
			//隐藏
			if($('.my-operate').length>0){
				$('.my-operate').hide();
			}
		}else if(status=='0'){
			$('.state03').addClass('state04').removeClass('state03');
			window.clearInterval(intervalDownTime);
			$('.time-limit').html('该地块由'+ obj.conNum + '号竞买人竞得');
			var priceHtml = $('.price').html();
			$('.price').html(priceHtml.replace('当前价','成交价'));
			$('.state-bg').html('<img src="skin/images/listed-tit.png">已结束');
			//隐藏
			if($('.my-operate').length>0){
				$('.my-operate').hide();
			}
		}else if(status=='5'){
			window.clearInterval(intervalDownTime);
			$('.time-limit').html($('#curTopPrice').html()+'号竞买人参与现场竞公益配套');
//			var conNums = $('#joinShakeNumber').val();
//			$('.time-limit').html(conNums+'号竞买人参与摇号');
			$('.state-bg').html('<img src="skin/images/listed-tit.png">等待结果');
			//隐藏
			if($('.my-operate').length>0){
				$('.my-operate').hide();
			}
		}
		showInfo('交易结束');
	}
}
/**
 * 当前公告交易完成
 * @param obj
 */
function op15(obj){
	var curNoticeId = $('#noticeId').val();
	if(obj.noticeId==curNoticeId){
		//处理所有地块交易结束
	}
}
/**
 * 报价后提示信息
 * @param obj
 */
function op62(obj){
	showInfo(obj.msg);
}
/**
 * 接收到达到最高限价信息
 * @param obj
 */
function op38(obj){
	var userId = $('#userId').val();
	var topClientNo = obj.topClientNo;
	var conNum = $('#conNum').val();
	var maxPrice = obj.maxPrice;
	var appId = obj.appId;
	var canSendPrice = $('#canSendPrice').val();
	//隐藏
	if($('.my-operate').length>0){
		$('.my-operate').hide();
	}
	if(userId==topClientNo){
		json = '{"op":"39","appId":"' + appId + '","noticeId":"' + $('#noticeId').val() +'","flag":"YES","conNum":"'+ conNum +'"}';
		sendMsg(json);
		return;
	}else{
		if(canSendPrice=='1'){
			showConfirm('是否参与后续的现场竞公益配套？','&nbsp;&nbsp;&nbsp;&nbsp;最新报价已达到最高限价，如果仅1家竞买人同意参加现场竞公益配套面积，则系统以最高限价自动确认竞得人；如仍有2家或2家以上单位要求继续竞买的，将停止网上限时竞价，改为现场竞公益配套面积确定竞得人，竞得人的成交价为最高限价；不再继续参与的竞买人将退出该幅地块的竞买,参与竞公益配套面积的竞买人请在倒计时结束后至“我的交易”打印《国有建设用地使用权网上交易现场资格审查通知书》。<br>&nbsp;&nbsp;&nbsp;&nbsp;您是否接受最高限价并继续参加现场竞公益配套？',function(confirm){
				if(confirm){
					var json = '{"op":"39","appId":"' + appId + '","noticeId":"' + $('#noticeId').val() +'","flag":"YES","conNum":"'+ conNum +'"}';
					//op40(obj);
					sendMsg(json);
				}else{
					json = '{"op":"39","appId":"' + appId + '","noticeId":"' + $('#noticeId').val() +'","flag":"NO","conNum":"'+ conNum +'"}';
					sendMsg(json);
				}
			});
		}
	}
}
/**
 * 接收更新参与摇号人员信息
 * @param obj
 */
function op40(obj){
	var appId = obj.appId;
	var conNums = obj.conNums;
	//conNums+'号竞买人选择参与摇号'
	$('.price').html(priceHtml.replace('当前价','参与者'));
	$('#curTopPrice').html(obj.conNums);
	$('.state-bg').html('<img src="skin/images/listed-tit.png">等待结果');
	//var joinNums=$('.joinShakeNumber').val()+'   '+conNums;
	//$('.joinShakeNumber').val(joinNums);
	showInfo('已有'+conNums+'号竞买人选择参与现场竞公益配套');
}