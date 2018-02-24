(function($, window, document) {
	 $.fn.scroll2Load = function(options) {
		 var settings = {
				 	first       :	0,	//加载内容起始序号,0为第一条
				 	//服务器地址
				 	//注意：1.url中需要串入pcIds作为父级栏目Id数组
				 	//		2.若页面cms_content_list排序orderBy不是默认的0，则需要在此路径串入orderBy
				 	//示例：url : "${base}/mobile/content.jspx?pcIds=${channel.id}&orderBy=4"
				 	url			:	"",	
				 	count		:	5,	//每次加载条数
				 	container	:	"#contentList",//内容展示页面容器
				 	progressBar :   "#hook-div",//加载动作条
				 	currentPage :2,
				 	end			:   "<div>No More To Load!</div>",//无内容可加载时的提示语				 
				 	callBack    :	function(){}//回调函数，加载结束后执行
		        };
		 
		 if(options) {//获取页面初始化参数
	            $.extend(settings, options);
	        }
		$(window).scroll(function(){
			var scrollTop = $(this).scrollTop();
			var scrollHeight = $(document).height();
			var windowHeight = $(this).height();
			//滑动到底部
			if(scrollTop >= scrollHeight - windowHeight){
				$.ajax({
					type:"get",
					dataType: "json",
					url: settings.url+"&currentPage="+settings.currentPage,
					success: function(data){
						//页面填充
						fillData(data);
						//变更下次加载的初始序号
						settings.currentPage = settings.currentPage + 1;
						//执行回调
						//alert("执行回调");
						//settings.callBack();
					},
					
					beforeSend : function(){
						},
						
					complete : function(){
							}
				
				});
			}
		});
	
		//填充得到的数据到html中
		function fillData(data){
			var innerhtml ="";					
			if(data.textlist != null ){
				if(data.currentPage<data.totalPage){
					for(var i=0;i<data.pageSize;i++){
						innerhtml+="<table  id=\"contentTable\" border=\"0\" width=\"100%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"  onClick=\"window.open(\' "+data.textlist.list[i].url+"\')\">"+							
						"<tr>"+
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;</td>"+
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;&nbsp;</td>"+	
				        "<td align=\"left\" colSpan=\"2\"><span class=\"title_font\">"+data.title+"</span></td>"+   
				        "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> "+   
				  	    "<td width=\"1%\">&nbsp;&nbsp;&nbsp;</td>"+  		      			
				  	"</tr>"+		
				  	"<tr>"+	
				  		"<td height=\"1px\" colSpan=\"5\"></td>"+
				  	"</tr>"+		
				  	"<tr>"+	
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;</td>"+
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;&nbsp;</td>"+	
						"<td align=\"left\"><p class=\"text-muted small\">"+data.textlist.list[i].noticeNo+"</p></td> "+   
				        "<td align=\"right\"><p class=\"text-muted small\">"+data.textlist.list[i].stringDate+"</p></td>"+    	      		
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"+	 
				  		"</tr>"+		
				"</table>"+  		
				"<HR style=\"margin:5px\">";	
					}	
				}
				if(data.currentPage==data.totalPage){
					for(var i=0;i<data.syRows;i++){
						innerhtml+="<table id=\"contentTable\" border=\"0\" width=\"100%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"  onClick=\"location.href=\' "+data.textlist.list[i].url+"\'\">"+							
						"<tr>"+
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;</td>"+
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;&nbsp;</td>"+	
				        "<td align=\"left\" colSpan=\"2\"><span class=\"title_font\">"+data.title+"</span></td>"+   
				        "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> "+   
				  	    "<td width=\"1%\">&nbsp;&nbsp;&nbsp;</td>"+  		      			
				  	"</tr>"+		
				  	"<tr>"+	
				  		"<td height=\"5px\" colSpan=\"5\"></td>"+
				  	"</tr>"+		
				  	"<tr>"+	
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;</td>"+
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;&nbsp;</td>"+	
						"<td align=\"left\"><p class=\"text-muted small\">"+data.textlist.list[i].noticeNo+"</p></td> "+   
				        "<td align=\"right\"><p class=\"text-muted small\">"+data.textlist.list[i].stringDate+"</p></td>"+    	      		
				  		"<td width=\"1%\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"+	 
				  		"</tr>"+		
				"</table>"+  		
				"<HR style=\"margin:5px\">";	
					}	
				
				}
					
				if(data.currentPage==data.totalPage+1){
					innerhtml  +="<table align='center'><tr><td class='text-muted'>已经没有更多可加载内容了！</td></tr></table>";
				}			
			}else{//数据条数为零
				innerhtml  +="<table align='center'><tr><td class='text-muted'>已经没有更多可加载内容了！</td></tr></table>";
				$(settings.progressBar).hide();
                $(window).unbind("scroll");
			}
			$(innerhtml).appendTo(settings.container);
		}
	
	 };
})(jQuery, window, document);