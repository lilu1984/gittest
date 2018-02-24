package com.wonders.engine.auction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.exception.EngineException;
import com.wonders.wsjy.TradeConsts;
/**
 * 对地块连续交易的抽象类.
 * @author sunxin
 *
 */
public abstract class AbstractAuction implements AuctionSupport,Runnable{
	private Logger logger = Logger.getLogger(AbstractAuction.class);
	/**
	 * 客户端通道线程
	 */
	private Thread stageThread = null;
	/**
	 * 各个阶段的开关.
	 */
	private int phaseSwitch = 2;//倒计时各个阶段开关--竞价阶段
	/**
	 * 交易中地块间间隔
	 */
	private int intervalTime = TradeConsts.TRADE_INTERVAL_TIME;//交易中地块间间隔
	/**
	 * 是否是第一次进入每个阶段.
	 */
	private boolean isFirst = true;//倒计时开开关--竞价阶段
	/**
	 * 是否是第一次块地.
	 */
	private boolean isFirstBlock = false;//倒计时开开关--竞价阶段
	/**
	 * 交易地块的排队列表
	 */
	public LinkedList tradeBlockList = null;
	/**
	 * 当前交易地块
	 */
	private TradeBlock curTradeBlock = null;
	/**
	 * 倒计时发送时间的标识；0：不发送；1：发送
	 */
	private long sendTimeSwitch = 0;
	
	public void sendListingTime(Map paramMap){
		//sendTimeSwitch = 1;
		//如果是地块竞价阶段
		if(phaseSwitch ==1){
			sendCommand33(curTradeBlock.getLimitTime(),curTradeBlock.getAppId(), paramMap);
		}else{//如果是间歇期
			sendCommand33(intervalTime, null, paramMap);
		}
	}
	/**
	 * 初始化场景
	 * @param stageNo 场景主键
	 * @param tradeBlockList 交易地块列表
	 * @param isStart 是否马上启动
	 */
	public abstract void initAuction(String stageNo,LinkedList tradeBlockList,boolean isStart);
	
	/**
	 * 恢复竞价时间
	 *
	 */
	public void resumeLimitTime() {
        curTradeBlock.setLimitTime(TradeConsts.TRADE_LIMIT_TIME);
        sendTimeSwitch = 1;
	}

	/**
	 * 更细最高报价.
	 * @param tradePrice 报价对象
	 */
	public void updateTopPrice(TradePrice tradePrice) {
		//设置轮次
		curTradeBlock.setPriceNum(tradePrice.getPriceNum());
		//设置最高竞买
		curTradeBlock.setTopPrice(tradePrice.getPrice());
		//设置最高竞客户端
		curTradeBlock.setTopClientNo(tradePrice.getClientNo());
	}
	/**
	 * 更新参与摇号的号牌
	 * @param conNum
	 * @param flag
	 */
	public void updatePartakeConNum(String conNum,boolean flag){
		LinkedList list = null;
		if(flag){
			list = curTradeBlock.getPartakeConNumList();
			list.add(conNum);
			curTradeBlock.setPartakeConNumList(list);
		}else{
			list = curTradeBlock.getNotPartakeConNumList();
			list.add(conNum);
			curTradeBlock.setNotPartakeConNumList(list);
		}
		this.sendCommand40(curTradeBlock);
	}
	/**
	 * 更细加价幅度.
	 * @param tradePrice 报价对象
	 */
	public void updateAddPrice(long addnum) {
		//设置加价幅度
		curTradeBlock.setPriceAdd(addnum);
	}
	
	/**
	 * 设置间隔期时间.
	 * @param intervalTime 间隔期时间
	 */
	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}
	/**
	 * 设置阶段状态.
	 * @param phaseSwitch
	 */
	public void setPhaseSwitch(int phaseSwitch) {
		this.phaseSwitch = phaseSwitch;
	}
	
	/**
	 * 判断地块是否属于此场景.
	 */
	public boolean existBlock(String appId) {
		if(curTradeBlock != null && curTradeBlock.getAppId().equals(appId)){
			return true;
		}
		return false;
	}
	
	/**
	 *启动场景.
	 */
	public void startStage() {
		stageThread = new Thread(this, "stageThread");
		stageThread.start();
		//交易启动
		tradeStart(curTradeBlock);
		System.out.println("--场景已经启动--");
	}
	
	public void run() {
		try {	
			while (true) {
					//如果状态为-1，结束此场景
					if(phaseSwitch == -1){
						break;
					}
					if (phaseSwitch != 0) {//如果是0，倒计时服务未启动，处于等待状态
						if(phaseSwitch ==1){//如果是1，进入交易大厅，进行第一块地的交易
							//交易开始时初始化状态
							if(isFirst){
								//获取第一个地块
						        curTradeBlock = (TradeBlock) tradeBlockList.getFirst();
						        //交易开始方法
								blockTradeStart(curTradeBlock);
								//交易开始
								sendCommand1Series(TradeConsts.ORDER12);
								//交易开始，发送倒计时时间
								isFirst = false;
							}
							//60秒和30秒的时间校正
							if(curTradeBlock.getLimitTime() == 240 ||curTradeBlock.getLimitTime() == 180 ||curTradeBlock.getLimitTime() == 120 || curTradeBlock.getLimitTime() == 60 || curTradeBlock.getLimitTime() == 30){
								sendTimeSwitch = 1;
							}
				            if(sendTimeSwitch == 1){
								 sendCommand33(curTradeBlock.getLimitTime(),curTradeBlock.getAppId(),null);
								 sendTimeSwitch = 0;
							}
				            curTradeBlock.setLimitTime(curTradeBlock.getLimitTime()-1);
				            //交易当前价格是否是最高价
				            if(curTradeBlock.confirmMaxPriceByTopPrice()&&!curTradeBlock.isSendMaxPriceMsg()){
				            	//假如已经达到当前最高报价则向客户端发送35号命令
				            	sendCommand38(curTradeBlock);
				            	curTradeBlock.setSendMaxPriceMsg(true);
				            }
				            //设置间隔时间
				            if(curTradeBlock.getLimitTime() <= 0){
				            	// TODO: 增加底价判断功能
				            	// 1. if (没有开始底价倒计时) {
				            	if (!curTradeBlock.isMiniLimit()) { // 如果没有开始低价倒计时
				            		// 2. if (有底价) {
				            		if (containsBasePrice(curTradeBlock)) { // 有底价
				            			if (!curTradeBlock.isValidTopPrice()) {
				            				// 1. 增加时间 
				            				curTradeBlock.beginMiniPriceLimit();
				            				// 2. 发送时间
				            				sendCommand33(curTradeBlock.getLimitTime(), curTradeBlock.getAppId(), null);
				            				// 3. 发送提示信息 （注意修改短信）
				            				sendCommand34(curTradeBlock);
				            				// 4. 更新当前地块状态（页面刷新时做判断）
							            	// 5. sleep(1000);
				            				Thread.sleep(1000);
							            	// 6. continue;
				            				continue;
				            			}
				            		} 
				            	}
				            	// TODO 结束
				            	//设置交易结果
				            	String traderesult = curTradeBlock.finishTradeEndByMaxPrice();
				            	curTradeBlock.setTradeResult(traderesult);
				            	//发布倒计时
								sendTimeSwitch = 1;
								//发布结果
								sendCommand1Series(TradeConsts.ORDER13);
								if(tradeBlockList.size() == 1){
									//交易完成，发布成果信息
									tradeFinish(curTradeBlock);
									//宣布竞价结束
									stageThread.sleep(10000);
									sendCommand1Series(TradeConsts.ORDER15);
									//设置交易完成的信息
									 setTradeFinishInfo();
								}else{
									//地块交易完成，继续进行下一块地的交易
									blockTradeFinish(curTradeBlock);
									//设置继续交易的信息
									setBlockContinueInfo();
								}
	
				            }
						}else if(phaseSwitch ==2){//地块交易完成后，停20秒钟，随后进行下一块地的进行，状态变为1，如果交易结束；跳转到其他公告倒计时阶段。
							//交易间歇期初始化
							if(isFirst){
								blockIntervalStart(curTradeBlock);
							}
							//发布倒计时
							if(sendTimeSwitch == 1){
								sendCommand33(intervalTime,null,null);
								sendTimeSwitch = 0;
							}

							
							//间隔期间倒计时
							intervalTime = intervalTime-1;
							//间歇期完成，开始地块交易
							if(intervalTime <= 0){
								phaseSwitch=1;
								sendTimeSwitch = 1;
								isFirst = true;
								blockIntervalFinish(curTradeBlock);
							}
						}
					}
					stageThread.sleep(1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("auction error:",e);
			}
	}
	
	/**
	 * 设置交易完成后信息.
	 */
	private void setTradeFinishInfo(){
		phaseSwitch = -1;
		curTradeBlock = null;
		tradeBlockList.removeFirst();
		sendTimeSwitch = 0;
	}
	
	/**
	 * 设置地块交易后续信息.
	 */
	private void setBlockContinueInfo(){
		//调整当前交易地块
		tradeBlockList.removeFirst();
		curTradeBlock = (TradeBlock) tradeBlockList.getFirst();
		//进入交易间隔期
		phaseSwitch=2;
		intervalTime = TradeConsts.TRADE_INTERVAL_TIME;
		isFirst = true;
		isFirstBlock = false;
	}
	
	/**
	 * 启动交易，默认实现,切到交易大厅中.
	 */
	public void tradeStart(TradeBlock tradeBlock){
		if(curTradeBlock == null && tradeBlockList.size()>0){
			curTradeBlock = (TradeBlock) tradeBlockList.getFirst();
			isFirstBlock = true;
			sendCommand1Series(TradeConsts.ORDER17);
		}
	}
	/**
	 * 交易间隔开始，默认实现,切到交易大厅中.
	 */
	public void blockIntervalStart(TradeBlock tradeBlock){
		if(!isFirstBlock && intervalTime==10){
			sendCommand1Series(TradeConsts.ORDER17);
			// 发送倒计时
			//sendTimeSwitch = 1;
			isFirst = false;
		}
	}

	/**
	 * 发送系统1系列命令，主要是12、13、15、17
	 * @param commandNo 命令代号
	 */
	public void sendCommand1Series(String commandNo){
		Map paramMap = new HashMap();
		if(TradeConsts.ORDER12.equals(commandNo)){
			paramMap.put("tradeBlock", curTradeBlock);
			paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
		}else if(TradeConsts.ORDER13.equals(commandNo)){
			paramMap.put("tradeBlock", curTradeBlock);
			paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
			if (!curTradeBlock.isValidTopPrice()) {
				// 如果不是有效报价，那么发送交易失败
				paramMap.put("tradeStatus", "0");
			}
			if(tradeBlockList.size() > 1){
				paramMap.put("nextBlockNo", ((TradeBlock)tradeBlockList.get(1)).getBlockNoticeNo());
			}
		}else if(TradeConsts.ORDER15.equals(commandNo) || TradeConsts.ORDER17.equals(commandNo)){
			//设置公告过滤
			paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
			paramMap.put("noticeId", curTradeBlock.getNoticeId());
		}
		try {
			CoreService.broadcastCommand(commandNo, paramMap, null);
		} catch (EngineException e) {
			e.printStackTrace();
			logger.error("CoreService.broadcastCommand:",e);
		}
	}
	
	/**
	 * 发送33命令
	 * @param limitTime 倒计时秒数
	 * @param appId 地块申请主键，为空时为竞价间隔期
	 */
	public void sendCommand33(long limitTime , String appId,Map paramMap){
		if(paramMap == null){
			paramMap = new HashMap();
		}
		paramMap.put("limitTime", new Long(limitTime));
		paramMap.put("appId", appId);
		if(curTradeBlock !=null){
			paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
		}
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER33, paramMap, null);
		} catch (EngineException e) {
			e.printStackTrace();
			logger.error("CoreService.broadcastCommand(33):",e);
		}
	}
	
	/**
	 * 发送34命令
	 * @param curTradeBlock2
	 */
	private void sendCommand34(TradeBlock curTradeBlock) {
		Map	paramMap = new HashMap();
		paramMap.put("appId", curTradeBlock.getAppId());
		paramMap.put("blockNoticeNo", curTradeBlock.getBlockNoticeNo());
		paramMap.put("basePrice", curTradeBlock.getBasePrice());
		paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
		paramMap.put("topClientNo", curTradeBlock.getTopClientNo());
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER34, paramMap, null);
		} catch (EngineException e) {
			logger.error("CoreService.broadcastCommand(34):",e);
			e.printStackTrace();
		}
	}
	/**
	 * 发送38命令
	 * @param curTradeBlock2
	 */
	private void sendCommand38(TradeBlock curTradeBlock){
		Map	paramMap = new HashMap();
		paramMap.put("appId", curTradeBlock.getAppId());
		paramMap.put("maxPrice", new Double(curTradeBlock.getMaxPrice()));
		paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
		paramMap.put("topClientNo", curTradeBlock.getTopClientNo());
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER38, paramMap, null);
		} catch (EngineException e) {
			logger.error("CoreService.broadcastCommand(38):",e);
			e.printStackTrace();
		}
	}
	/**
	 * 发送40号命令（更新参与摇号的信息）
	 * @param curTradeBlock
	 */
	private void sendCommand40(TradeBlock curTradeBlock){
		Map paramMap = new HashMap(); 
		paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
		paramMap.put("appId", curTradeBlock.getAppId());
		paramMap.put("conNums", curTradeBlock.getStrPartakeConNum());
		try {
		//群发消息更新摇号选择信息
			CoreService.broadcastCommand(TradeConsts.ORDER40,paramMap,null);
		} catch (EngineException e) {
			logger.error("CoreService.broadcastCommand(40):",e);
			e.printStackTrace();
		}
	}
	public void basePriceSubmit(String appId, String clientNo, String conNo, String yktBh) {
		if (curTradeBlock.getAppId().equals(appId)) {
			TradePrice tradePrice = new TradePrice();
			tradePrice.setAppId(appId);
			tradePrice.setPrice(curTradeBlock.getBasePrice().doubleValue());
			tradePrice.setPhase("2");
			tradePrice.setClientNo(clientNo);
			tradePrice.setConNum(conNo);
			tradePrice.setYktBh(yktBh);
			CoreService.pushPrice(tradePrice);
		}
			
	}
	
	public void setLimitTime(String appId, int times) {
		if (curTradeBlock.getAppId().equals(appId))
			curTradeBlock.setLimitTime(times);
	}
	
	public TradeBlock getTradeBlock(String noticeId) {
		return curTradeBlock;
	}
	
}
