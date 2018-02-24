package com.wonders.engine.socket.command;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradeNotice;
import com.wonders.engine.bo.TradePrice;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.wsjy.TradeConsts;
/**
 * 命令工厂，提供命令字符串，已内置所有命令格式，如果有特殊需要，可以继承CommandExpand接口实现自己的命令格式.
 * @author sunxin
 *
 */
public class CommandFactory {
	
	private static CommandFactory instance = null;
	
	private CommandFactory(){
		
	}
	
	public static CommandFactory getInstance(){
		if(instance == null)
			instance = new CommandFactory();
		return instance;
	}
	
	public TradePrice genTradePrice(String commandString){
	    if (StringUtils.isBlank(commandString)){
	    	return null;
	    }
		TradePrice tradePrice = new TradePrice();
	    JSONObject jsonObject = null;       
	    try {
	    	jsonObject = new JSONObject(commandString);
	    	tradePrice.setOp(jsonObject.getString("op"));
	    	//收到报价
	    	if("22".equals(tradePrice.getOp())){
		    	tradePrice.setPrice(jsonObject.getDouble("price"));
		    	tradePrice.setAppId(jsonObject.getString("appId"));
		    	tradePrice.setPhase(jsonObject.getString("phase"));
		    	//tradePrice.setPhase("1");
	    	}else if("21".equals(tradePrice.getOp())){
		    	tradePrice.setClientNo(jsonObject.getString("clientNo"));
		    	if(!jsonObject.isNull("flag")){
		    		tradePrice.setFlag(jsonObject.getString("flag"));
		    	}
	    	}else if("24".equals(tradePrice.getOp()) || "35".equals(tradePrice.getOp())){
		    	tradePrice.setAppId(jsonObject.getString("noticeId"));
	    	}else if("23".equals(tradePrice.getOp())){
		    	tradePrice.setAppId(jsonObject.getString("noticeId"));
	    	}else if("26".equals(tradePrice.getOp())){
		    	tradePrice.setAppId(jsonObject.getString("noticeId"));
	    	} else if ("27".equals(tradePrice.getOp()) || "37".equals(tradePrice.getOp())) {
	    		tradePrice.setAppId(jsonObject.getString("appId"));
	    		tradePrice.setNoticeId(jsonObject.getString("noticeId"));
	    		tradePrice.setBaseConfig(jsonObject.getString("baseConfig"));
	    	}else if ("39".equals(tradePrice.getOp())){
	    		tradePrice.setAppId(jsonObject.getString("appId"));
	    		tradePrice.setNoticeId(jsonObject.getString("noticeId"));
	    		tradePrice.setFlag(jsonObject.getString("flag"));
	    		tradePrice.setConNum(jsonObject.getString("conNum"));
	    	}
	    }catch(JSONException e){ 
	    	e.printStackTrace(System.err);
	    }    
	    return tradePrice;     
	 }
	 
		 
	/**
	 * 
	 * @param commandNo
	 * @param paramMap 11：参数为tradePrice
	 * @param commandExpandClass 自定义的命令类，需继承CommandExpand接口
	 * @return
	 */
	public String genEngineCommand(String commandNo,Map paramMap,String commandExpandClass){
		 //自定义命令
		 if(StringUtils.isNotBlank(commandExpandClass)){
			 CommandExpand commandExpand = genCommandExpand(commandExpandClass);
			 if(commandExpand !=null){
				 return commandExpand.genCommand(paramMap);
			 }else{
				 return null;
			 }
		 } else if(TradeConsts.ORDER11.equals(commandNo)) { 
			 TradePrice tradePrice = (TradePrice) paramMap.get("tradePrice");
			 return genCommand11(tradePrice);
		 }else if(TradeConsts.ORDER12.equals(commandNo)){
			 TradeBlock tradeBlock = (TradeBlock) paramMap.get("tradeBlock");
			 return genCommand12(tradeBlock);
		 }else if(TradeConsts.ORDER13.equals(commandNo)){
			 TradeBlock tradeBlock = (TradeBlock) paramMap.get("tradeBlock");
			 String nextBlockNo = (String) paramMap.get("nextBlockNo");
			 String tradeStatus = (String) paramMap.get("tradeStatus");
			 return genCommand13(tradeBlock,nextBlockNo, tradeStatus);
		 }else if(TradeConsts.ORDER15.equals(commandNo)){
			 String noticeId = (String) paramMap.get("noticeId");
			 return genCommand15(noticeId);
		 }else if(TradeConsts.ORDER17.equals(commandNo)){
			 String noticeId = (String) paramMap.get("noticeId");
			 return genCommand17(noticeId);
		 }else if(TradeConsts.ORDER18.equals(commandNo)){
			 String noticeId = (String) paramMap.get("noticeId");
			 return genCommand18(noticeId);
		 }else if(TradeConsts.ORDER19.equals(commandNo)){
			 return genCommand19();
		 }else if(TradeConsts.ORDER28.equals(commandNo)){
			 TradeNotice notice = (TradeNotice)paramMap.get("tradeNotice");
			 return genCommand28(notice);
		 }else if(TradeConsts.ORDER29.equals(commandNo)){
			 String returnTimes = (String)paramMap.get("returnTimes");
			 String phase = (String)paramMap.get("phase");
			 String appId = (String)paramMap.get("appId");
			 String blockNoticeNo = (String)paramMap.get("blockNoticeNo");
			 return genCommand29(returnTimes, phase, appId, blockNoticeNo);
		 }else if(TradeConsts.ORDER30.equals(commandNo)){
			 TradeBlock tradeBlock = (TradeBlock) paramMap.get("tradeBlock");
			 String times = (String)paramMap.get("times");
			 return genCommand30(tradeBlock, times);
		 }else if(TradeConsts.ORDER31.equals(commandNo)){
			 return genCommand31();
		 }else if(TradeConsts.ORDER32.equals(commandNo)){
			 List tradeNoticeList = (List) paramMap.get("tradeNoticeList");
			 return genCommand32(tradeNoticeList);
		 }else if(TradeConsts.ORDER33.equals(commandNo)){
			 Long limitTime = (Long) paramMap.get("limitTime");
			 String appId = (String) paramMap.get("appId");
			 return genCommand33(limitTime.longValue(),appId);
		 }else if(TradeConsts.ORDER61.equals(commandNo)){
			 return genCommand61();
		 }else if(TradeConsts.ORDER62.equals(commandNo)){
			 String isSuccess = (String) paramMap.get("isSuccess");
			 return genCommand62(isSuccess);
		 }else if(TradeConsts.ORDER63.equals(commandNo)){
			 return genCommand63();
		 }else if(TradeConsts.ORDER34.equals(commandNo)){
			 String appId = (String)paramMap.get("appId");
			 String blockNoticeNo = (String)paramMap.get("blockNoticeNo");
			 BigDecimal basePrice = (BigDecimal)paramMap.get("basePrice");
			 String topClientNo = (String)paramMap.get("topClientNo");
			 return genCommand34(appId, blockNoticeNo, topClientNo, basePrice);
		 }else if(TradeConsts.ORDER36.equals(commandNo)){
			 TradeBlock tradeBlock = (TradeBlock) paramMap.get("tradeBlock");
			 String nextBlockNo = (String) paramMap.get("nextBlockNo");
			 String tradeStatus = (String) paramMap.get("tradeStatus");
			 String auction = (String) paramMap.get("auction");
			 BigDecimal resultPrice = (BigDecimal) paramMap.get("resultPrice"); 
			 return genCommand36(tradeBlock, nextBlockNo, tradeStatus, auction, NumberUtil.numberDisplay(resultPrice, 0));
		 }else if(TradeConsts.ORDER38.equals(commandNo)){
			 String appId = (String)paramMap.get("appId");
			 Double maxPrice = (Double)paramMap.get("maxPrice");
			 String topClientNo = (String)paramMap.get("topClientNo");
			 return genCommand38(appId, topClientNo, maxPrice);
		 }else if(TradeConsts.ORDER40.equals(commandNo)){
			 String appId = (String)paramMap.get("appId");
			 String conNums = (String)paramMap.get("conNums");
			 return genCommand40(appId, conNums);
		 }
		return null;
	}
	private String genCommand40(String appId,String conNums){
		String jsonString = "{op:'40',appId:'" +appId+ "', conNums:'" +conNums+ "'}";
		return jsonString;
	}
	private String genCommand29(String returnTimes, String phase, String appId, String blockNoticeNo) {
		String jsonString = "{op:'29', appId:'"+appId+"', time:'"+returnTimes+"', phase:'" + phase + "', blockNoticeNo:'"+blockNoticeNo+"'}";
		//System.out.println("command:"+jsonString);
		return jsonString;
	}
	
	private String genCommand36(TradeBlock tradeBlock,String nextBlockNo, String tradeStatus, String auction, String resultPrice){
	 	String jsonString = null;
	 	if ("0".equals(tradeStatus)) {
	 		if(StringUtils.isNotBlank(nextBlockNo)){
	 			jsonString = "{op:'36',msg:'很遗憾，由于最高报价低于底价，"+tradeBlock.getBlockNoticeNo()+"地块由市国土资源局收回，稍后即将进行"+nextBlockNo+"地块的确认底价。',resultPrice:'',nextBlockNo:'" +nextBlockNo+ "',auction:'" +auction+ "',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"', status:'0'}";
	 		} else {
	 			jsonString = "{op:'36',msg:'很遗憾，由于最高报价低于底价，"+tradeBlock.getBlockNoticeNo()+"地块由市国土资源局收回。本次确认底价结束。',resultPrice:'',nextBlockNo:'',auction:'" +auction+ "',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"', status:'0'}";
	 		}
	 	} else {
	 		if(StringUtils.isNotBlank(nextBlockNo)){
		    	jsonString = "{op:'36',msg:'恭喜"+tradeBlock.getTopConNum()+"号竞买人竞得"+tradeBlock.getBlockNoticeNo()+" 地块，稍后即将进行"+nextBlockNo+"地块的确认底价。',resultPrice:'"+resultPrice+"',nextBlockNo:'" +nextBlockNo+ "',auction:'" +auction+ "',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"', status:'1'}";
		    }else{
		    	jsonString = "{op:'36',msg:'恭喜"+tradeBlock.getTopConNum()+"号竞买人竞得"+tradeBlock.getBlockNoticeNo()+" 地块。本次确认底价结束。',resultPrice:'"+resultPrice+"',nextBlockNo:'',auction:'" +auction+ "',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"', status:'1'}";	
		    }
	 	}
	    
		//System.out.println("command:"+jsonString);
	    return jsonString;
	}

	/**
	 * 生成自定义的扩展命令
	 * @param commandExpandClass
	 * @return
	 */
	private CommandExpand genCommandExpand(String commandExpandClass) {
		CommandExpand commandExpand = null;
		try {
			commandExpand = (CommandExpand) Class.forName(commandExpandClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return commandExpand;
	}
	
	/**
	 * 生成内置的11命令.
	 * @param tradePrice
	 * @return
	 */
	private String genCommand11(TradePrice tradePrice){
		 StringBuffer jsonString = new StringBuffer();
		 jsonString.append("{op:'").append("11");
		 jsonString.append("',conNum:'").append(tradePrice.getConNum());
		 jsonString.append("',price:'").append(NumberUtil.numberDisplay(tradePrice.getPrice(),0));
		 jsonString.append("',priceTime:'").append(DateUtil.date2String(tradePrice.getPriceTime(), DateUtil.FORMAT_DATETIME));
		 jsonString.append("',msg:'").append(StringUtils.trimToNull(tradePrice.getMsg()));
		 jsonString.append("',appId:'").append(tradePrice.getAppId());
		 jsonString.append("',clientNo:'").append(tradePrice.getClientNo());
		 jsonString.append("',priceNum:'").append(tradePrice.getPriceNum());
		 jsonString.append("'}");
		 return jsonString.toString();
	 }
	
	/**
	 * 向客户端发送底价确认场景开始命令
	 * 
	 * @param block
	 * @param notice
	 * @return
	 */
	private String genCommand28(TradeNotice notice) {
		StringBuffer buffer = new StringBuffer();
		// {op:'28', noticeId:''}
		buffer.append("{op:'28', noticeId:'").append(notice.getNoticeId()).append("'}");
		
		return buffer.toString();
	}
	
	private String genCommand12(TradeBlock tradeBlock){
		 String jsonString = "{op:'12',noticeId:'"+tradeBlock.getNoticeId()+"',appId:'"+tradeBlock.getAppId()+"',topPrice:'"+NumberUtil.numberDisplay(tradeBlock.getTopPrice(),0)+"',priceAdd:'"+NumberUtil.numberDisplay(tradeBlock.getPriceAdd(),0)+"',blockNoticeNo:'"+tradeBlock.getBlockNoticeNo()+"'}";
		 //System.out.println("command:"+jsonString);
		 return jsonString;
    }
	
	private String genCommand30(TradeBlock tradeBlock, String times){
		 String jsonString = "{op:'30',noticeId:'"+tradeBlock.getNoticeId()+"',appId:'"+tradeBlock.getAppId()+"',topPrice:'"+NumberUtil.numberDisplay(tradeBlock.getTopPrice(),0)+"',priceAdd:'"+NumberUtil.numberDisplay(tradeBlock.getPriceAdd(),0)+"',blockNoticeNo:'"+tradeBlock.getBlockNoticeNo()+"',basePrice:'" + NumberUtil.numberDisplay(tradeBlock.getBasePrice(), 0) + "', times:'" +times+ "'}";
		 //System.out.println("command:"+jsonString);
		 return jsonString;
   }
	
	private String genCommand13(TradeBlock tradeBlock,String nextBlockNo, String tradeStatus){
	 	String jsonString = null;
	 	if (StringUtils.isNotEmpty(tradeStatus)) {
	 		jsonString = "{op:'13',msg:'很遗憾，由于最高报价低于底价，"+tradeBlock.getBlockNoticeNo()+"地块由市国土资源局收回。',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"',conNum:'"+ tradeBlock.getTopConNum() +"', status:'0'}";
	 	} else {
	 		
 			if("5".equals(tradeBlock.getTradeResult())){
 				if(StringUtils.isNotBlank(nextBlockNo)){
 					jsonString = "{op:'13',msg:'"+tradeBlock.getBlockNoticeNo()+" 地块，有"+tradeBlock.getStrPartakeConNum()+"号竞买人参加现场摇号，稍后即将进行"+nextBlockNo+"地块的限时竞价。',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"',conNum:'"+ tradeBlock.getTopConNum() +"', status:'5'}";
 				}else{
 					jsonString = "{op:'13',msg:'"+tradeBlock.getBlockNoticeNo()+" 地块，有"+tradeBlock.getStrPartakeConNum()+"号竞买人参加现场摇号，本次交易结束，稍后自动转入交易结果页面。',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"',conNum:'"+ tradeBlock.getTopConNum() +"', status:'5'}";
 				}
 			}else if("0".equals(tradeBlock.getTradeResult())){
 				if(StringUtils.isNotBlank(nextBlockNo)){
 					jsonString = "{op:'13',msg:'很遗憾，由于该地块无人参与后续摇号，"+tradeBlock.getBlockNoticeNo()+"地块由市国土资源局收回.',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"',conNum:'"+ tradeBlock.getTopConNum() +"', status:'0'}";
 				}else{
 					jsonString = "{op:'13',msg:'很遗憾，由于该地块无人参与后续摇号，"+tradeBlock.getBlockNoticeNo()+"地块由市国土资源局收回，本次交易结束，稍后自动转入交易结果页面。',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"',conNum:'"+ tradeBlock.getTopConNum() +"', status:'0'}";
 				}
 			}else{
 				if(StringUtils.isNotBlank(nextBlockNo)){
 					jsonString = "{op:'13',msg:'恭喜"+tradeBlock.getTopConNum()+"号竞买人竞得"+tradeBlock.getBlockNoticeNo()+" 地块，稍后即将进行"+nextBlockNo+"地块的限时竞价。',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"',conNum:'"+ tradeBlock.getTopConNum() +"', status:'1'}";
 				}else{
 					jsonString = "{op:'13',msg:'恭喜"+tradeBlock.getTopConNum()+"号竞买人竞得"+tradeBlock.getBlockNoticeNo()+" 地块。本次交易结束，稍后自动转入交易结果页面。',appId:'"+tradeBlock.getAppId()+"',clientNo:'"+tradeBlock.getTopClientNo()+"',conNum:'"+ tradeBlock.getTopConNum() +"', status:'1'}";
 				}
		    }
	 	}
	    
		//System.out.println("command:"+jsonString);
	    return jsonString;
	}
	 
	private String genCommand15(String noticeId){
		String jsonString = "{op:'15',noticeId:'"+noticeId+"'}";
		//System.out.println("command:"+jsonString);
		return jsonString;
	}
	 
	private String genCommand17(String noticeId){
		String jsonString = "{op:'17',noticeId:'"+noticeId+"'}";
		//System.out.println("command:"+jsonString);
		return jsonString;
	}
	 
	private String genCommand18(String noticeId){
		String jsonString = "{op:'18',noticeId:'"+noticeId+"'}";
		//System.out.println("command:"+jsonString);
		return jsonString;
	}
	
	private String genCommand19(){
	    String jsonString = "{op:'19',msg:'已添加到客户端池'}";
	    return jsonString;
	}
	
	private String genCommand61(){
	    String jsonString = "{op:'61',msg:'系统消息'}";
	    return jsonString;
	}
	
	private String genCommand62(String isSuccess){
		 String jsonString = null;
		 if("1".equals(isSuccess)){
		     jsonString = "{op:'62',msg:'报价成功'}";
		 }else{
		     jsonString = "{op:'62',msg:'报价不成功'}";
		 }
	    return jsonString;
	}
	
	private String genCommand63(){
	    String jsonString = "{op:'63',msg:'系统在另一地点已登录，系统将退出登录！'}";
	    return jsonString;
	}
	
	private String genCommand31(){
	    String jsonString = "{op:'31',time:'"+DateUtil.timestamp2String(new Timestamp(System.currentTimeMillis()), DateUtil.FORMAT_DATETIME)+"',ms:'"+System.currentTimeMillis()+"'}";
	    return jsonString;
	}
	private String genCommand32(List engineNoticeStatck){
		 String jsonString = null;
		  for(int i=0;i<engineNoticeStatck.size();i++){
			  TradeNotice tradeNotice = (TradeNotice) engineNoticeStatck.get(i);
			  if(tradeNotice.getListStartDate().getTime() <= System.currentTimeMillis()){
				  if(jsonString == null){
					  jsonString = "{op:'32',info:[";
				  }
				  //String[] timeArray = genTime(tradeNotice.getSurplusTime());
				  //System.out.println("time:"+timeArray[0]);
				  if(i+1 == engineNoticeStatck.size()){
					  //jsonString = jsonString + "{noticeId:'"+tradeNotice.getNoticeId()+"',time:'"+timeArray[0]+"',ptime:'"+timeArray[1]+"'}]}";
					  jsonString = jsonString + "{noticeId:'"+tradeNotice.getNoticeId()+"',time:'"+tradeNotice.getSurplusTime()+"'}]}";
				  }else{
					  //jsonString = jsonString + "{noticeId:'"+tradeNotice.getNoticeId()+"',time:'"+timeArray[0]+"',ptime:'"+timeArray[1]+"'},";
					  jsonString = jsonString + "{noticeId:'"+tradeNotice.getNoticeId()+"',time:'"+tradeNotice.getSurplusTime()+"'},";
				  }
			  }
		  }
		  //System.out.println("time:"+jsonString);
		  return jsonString;
	 }
	
	private String genCommand33(long noticeTime,String appId){
		   //String[] timeArray = genTime(noticeTime);
		  // String jsonString = "{op:'33',appId:'"+appId+"',time:'"+timeArray[0]+"',ptime:'"+timeArray[1]+"',ptime1:'"+timeArray1[1]+"'}";
		   String jsonString = "{op:'33',appId:'"+appId+"',time:'"+noticeTime+"'}";
			//System.out.println("command:"+jsonString);
		   return jsonString;
	 }
	
	private String genCommand34(String appId, String blockNoticeNo,String topClientNo, BigDecimal basePrice) {
		String jsonString = "{op:'34',appId:'" +appId+ "', topClientNo:'" +topClientNo+ "', blockNoticeNo:'" +blockNoticeNo+ "',basePrice:'" +NumberUtil.numberDisplay(basePrice, 2)+ "'}";
		return jsonString;
	}
	private String genCommand38(String appId,String topClientNo,Double maxPrice){
		String jsonString = "{op:'38',appId:'" +appId+ "', topClientNo:'" +topClientNo+ "', maxPrice:'" +NumberUtil.numberDisplay(maxPrice, 0)+ "'}";
		return jsonString;
	}
//	 private String splitTime(long time){
//		 StringBuffer ptime = new StringBuffer();
//		 if(time<10){
//			 ptime.append("0-").append(time);
//		 }else{
//			 ptime.append(time/10).append("-").append(time%10);
//		 }
//		 return ptime.toString();
//	 }
	 
//	 private String[] genTime(long noticeTime){
//		 long tian = noticeTime/(24*60*60);
//		 long xiaoshi = (noticeTime/(60*60))%24;
//		 long fenzhong = (noticeTime/60)%60;
//		 long miao = noticeTime%(60);
//		 String time = tian+"天"+xiaoshi+"时"+fenzhong+"分"+miao+"秒";
//		 StringBuffer ptime = new StringBuffer();
//		 ptime.append(splitTime(tian)).append("-").append(splitTime(xiaoshi)).append("-").append(splitTime(fenzhong)).append("-").append(splitTime(miao));
//		 return new String[]{time,ptime.toString()};
//	 }
	

}
