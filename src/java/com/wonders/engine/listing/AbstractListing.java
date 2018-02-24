package com.wonders.engine.listing;

import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wonders.engine.BaseStore;
import com.wonders.engine.CoreService;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradeNotice;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.socket.GmServerPipe;
import com.wonders.engine.socket.client.SkClientPipe;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.TradeConsts;
import com.wonders.wsjy.wxtrade.WxConfirmScene;
import com.wonders.wsjy.wxtrade.WxDbService;

public abstract class AbstractListing implements ListingSupport, Runnable{
	
	/**
	 * 常量 半个小时
	 */
	private static final int HALF_HOUR = 60 * 30; 
	/**
	 * 自身线程
	 */
	private Thread triggerThread = null;
	
	/**
	 * false：正常运行；true：停止；停止后不能再重新运行
	 */
	private boolean runSwitch = false;
	/**
	 * 刷新间隔秒数，调用initTrigger();刷新.
	 */
	private long refreshTime = 120;
	/**
	 * 交易中正在挂牌公告列表
	 */
	public List engineNoticeList  = new ArrayList();
	/**
	 * 倒计时发送时间的时间开关：0：不发送；1：发送
	 */
	private long sendTimeSwith = 0;
	
	/**
	 * 发送挂牌时间倒计时
	 */
	public void sendListingTime(Map paramMap){
		sendCommand32(paramMap);
	}
	
	/**
	 * 
	 *启动触发器.
	 */
	public void startListing() {
		triggerThread = new Thread(this, "AbstractListing Thread");
		triggerThread.start();
		//初始化
		initListing();
	}
	/**
	 * 停止触发器.
	 *
	 */
	public void stopTrigger() {
		runSwitch = true;
	}
	
	/**
	 * 
	 * @param refreshTime
	 */
	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}
	
	public void run() {
		String wsjySocketIp = PropertiesUtil.getInstance().getProperty("wsjy_socket_ip");
		String wsjySocketPort = PropertiesUtil.getInstance().getProperty("wsjy_socket_port");
		int socketTimeout = 0;
		long timesCount = 0;
		while (true) {
			//如果状态为-1，结束此触发器
			if(runSwitch){
				break;
			}
			try{
				HashMap gmServerPool = CoreService.getGmServerPool();
				HashMap noticeMap = new HashMap();
				if(engineNoticeList !=null && engineNoticeList.size()>0){
					  for(int i=0;i<engineNoticeList.size();i++){
						  TradeNotice tradeNotice = (TradeNotice) engineNoticeList.get(i);
						  if(tradeNotice.isWsjy()){
							  String gmClinet = "GM001"+tradeNotice.getNoticeId();
							  String noticeId = tradeNotice.getNoticeId();
							  String commandString = "";
							  noticeMap.put(noticeId,tradeNotice);
							  if(!gmServerPool.containsKey(noticeId)){
								  GmServerPipe serverPipe = new GmServerPipe(noticeId,new Socket(wsjySocketIp, Integer.parseInt(wsjySocketPort)), socketTimeout);
								  //需要等待线程执行才能发送数据
								  triggerThread.sleep(500);
								  commandString = "{op:'21',clientNo:'"+ gmClinet +"'}";
								  serverPipe.sendCommand(commandString);
								  triggerThread.sleep(500);
								  commandString = "{op:'26',noticeId:'" + tradeNotice.getNoticeId() + "'}";
								  serverPipe.sendCommand(commandString);
								  CoreService.addGmServerPipe(noticeId, serverPipe);
								  
							  }else{
								  //每隔10秒向服务器请求一次
								  GmServerPipe serverPipe = (GmServerPipe)gmServerPool.get(noticeId);
								  commandString = "{op:'23',noticeId:'" + noticeId + "'}";
								  serverPipe.sendCommand(commandString);
								  commandString = "{op:'24',noticeId:'" + noticeId + "'}";
								  serverPipe.sendCommand(commandString);
							  }
							  
						  }
					  }
				  }
				//刷新倒计时公告列表
				if(timesCount >= refreshTime){
					initListing();
					timesCount=0;
					//发送挂牌时间开关设置为开
					sendTimeSwith=1;
					//清理已经交易结束的观摩客户端
					Iterator iter = gmServerPool.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry)iter.next();
						if(!noticeMap.containsKey(entry.getKey())){
							GmServerPipe serverPipe = (GmServerPipe)entry.getValue();
							serverPipe.disconnect();
						}
					}
				}
				//线程休眠5秒
				triggerThread.sleep(10000);
				//设置时间计数
				timesCount = timesCount+10;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 发送公告时间倒计时.
	 *
	 */
	public void sendCommand32(Map paramMap){
		try {
			paramMap.put("tradeNoticeList", engineNoticeList);
			CoreService.broadcastCommand(TradeConsts.ORDER32, paramMap, null);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送交易结束命令.
	 * @param noticeId 公告主键
	 */
	public void sendCommand15(String noticeId){
		Map paramMap = new HashMap();
		paramMap.put("noticeId", noticeId);
		//设置公告过滤
		paramMap.put("filterNoticeId", noticeId);
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER15, paramMap, null);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 现场交易挂牌结束命令.
	 * @param noticeId 公告主键
	 */
	public void sendCommand18(String noticeId){
		Map paramMap = new HashMap();
		paramMap.put("noticeId", noticeId);
		//设置公告过滤
		paramMap.put("filterNoticeId", noticeId);
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER18, paramMap, null);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取需要进入网上竞价的地块列表
	 * @param teadeBlockList 公告内的地块
	 * @return
	 */
	private LinkedList getWsjyList(String noticeId){
		LinkedList resultList = new LinkedList();
		List tradeBlockList = CoreService.findTradeBlockListByNoticeId(noticeId);
		List basePriceConfirmList = new ArrayList();
		for(int i=0;i<tradeBlockList.size();i++){
			TradeBlock tradeBlock = (TradeBlock) tradeBlockList.get(i);
			//判断是否进入网上交易
			if(isEnterAuction(tradeBlock)){
				resultList.add(tradeBlock);
			} else {
				// TODO: 增加 BY 高东，如果不是进入网上交易的，其中可能存在需要确认底价地块
				// 1、取得当前地块的底价信息

				BigDecimal basePrice = CoreService.getDbSupport().getBlockBasePrice(tradeBlock);
				// 2、判断是否存在底价
				if (basePrice.intValue() != 0) {
					if(!"0".equals(tradeBlock.getTradeResult())&&!"".equals(tradeBlock.getTradeResult())){
						// 存在底价
						// 2、判断当前挂牌价格
						tradeBlock.setBasePrice(basePrice);
						if (basePrice.doubleValue() > tradeBlock.getTopPrice()) {
							// 3、判断当前挂牌价格是否高于底价，
							// 4、如果小于底价，增加底价确认标识，并且加入确认底价列表
							tradeBlock.confirm();
							basePriceConfirmList.add(tradeBlock);
						}
					}

				}
			}
		}
		resultList.addAll(0, basePriceConfirmList);
		return resultList;
	}
	
	/**
	 * 1、如果是网上交易地块在挂牌截止日期之前一个小时、半个小时发送信息
	 * 2、如果是现场竞价地块在挂牌截止日期之前两个小时、一个小时发送信息
	 * 
	 * @param notice
	 */
	private void sendMsgCountDown(TradeNotice notice) {
		if (notice == null) return;
		if (notice.isWsjy()) {
			// 如果是网上交易的话 1个小时和半个小时
			if (notice.getSurplusTime() == 2 * HALF_HOUR || notice.getSurplusTime() == HALF_HOUR) {
				WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
				List list = wxDbService.findBidderAppList(notice.getNoticeId());
				//Map parmMap = wxDbService.getSmsInfoByAppId(null, tradePrice.getAppId());
				// String message = "尊敬的竞买人，您好!您申购的X地块所在批次，将在X年X月X日X时X分进入限时竞价（现场竞价），请您提前做好竞价准备。";
				//StringBuffer mobiles = new StringBuffer();
				System.out.println("挂牌结束前一个小时和半个小时发送短信");
				repeatSendSms(notice,list,wxDbService);
			}
		} else {
			// 如果是现场交易的话 2个小时和1个小时
			if (notice.getSurplusTime() == 4 * HALF_HOUR || notice.getSurplusTime() == 2*HALF_HOUR) {
				WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
				List list = wxDbService.findBidderAppList(notice.getNoticeId());
				System.out.println("挂牌结束前2个小时和1个小时发送短信");
				repeatSendSms(notice,list,wxDbService);
			}
		}
	}
	
	private void repeatSendSms(TradeNotice notice, List list, WxDbService wxDbService) {
		for (int i = 0 ; i < list.size(); i++) {
			StringBuffer blockNames = new StringBuffer();
			TdscBidderApp bidderApp = (TdscBidderApp)list.get(i);
			if (GlobalConstants.DIC_VALUE_YESNO_YES.equals(bidderApp.getIsAccptSms())) {
				String appIds = bidderApp.getAppId();
				String message = null;
				if (StringUtils.isNotEmpty(appIds)) {
					String[] appIdArray = appIds.split(",");
					if (appIdArray != null) {
						int length = appIdArray.length;
						for (int j = 0 ; appIdArray != null && j < length; j++) {
							if (StringUtils.isNotEmpty(appIdArray[j])) {
								TdscBlockTranApp tdscBlockTranApp = wxDbService.getTranAppByAppId(appIdArray[j]);
								blockNames.append(tdscBlockTranApp.getBlockNoticeNo());
								if (j + 1 != length)
									blockNames.append(",");
							}
						}
					}
					if (notice.isWsjy()) {
						message = "尊敬的竞买人，您好!您申购的" +blockNames.toString()+ "地块所在批次，将在" + DateUtil.date2String(notice.getListEndDate(), "yyyy年MM月dd日 HH时mm分") + "进入限时竞价，请您提前做好竞价准备。";
					} else {
						message = "尊敬的竞买人，您好!您申购的" +blockNames.toString()+ "地块所在批次，将在" + DateUtil.date2String(notice.getListEndDate(), "yyyy年MM月dd日 HH时mm分") + "进入现场竞价，请您提前做好竞价准备。";
					}
				}
				CoreService.getSmsSupport().sendSms(bidderApp.getPushSmsPhone(), message);
			}
		}
	}

}
