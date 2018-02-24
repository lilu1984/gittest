package com.wonders.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wonders.engine.auction.AuctionSupport;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.database.DbSupport;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.sms.SmsSupport;
import com.wonders.engine.socket.ClientPipe;
import com.wonders.engine.socket.GmClientPipe;
import com.wonders.engine.socket.GmServerPipe;
import com.wonders.engine.socket.client.websocket.WsClientPipe;
import com.wonders.engine.socket.command.CommandFactory;
import com.wonders.engine.socket.event.ChangeEvent;
import com.wonders.engine.timer.Task;
import com.wonders.wsjy.TradeConsts;
import com.wonders.wsjy.wxtrade.WxConfirmScene;
/**
 * 核心服务，向核心引擎外部分提供服务。
 * @author sunxin
 *
 */
public class CoreService {

	/**
	 * 客户端通道触发事件.
	 * @param event 客户端通道
	 */
	public synchronized static void handleChangeEvent(ChangeEvent event) throws EngineException {
		BaseStore.noticeStageByBlock(event);
	}

	/**
	 * 价格压栈.
	 * @param tradePrice 价格信息
	 * @return
	 */
	public synchronized static boolean pushPrice(TradePrice tradePrice){
		return BaseStore.pushPrice(tradePrice);
	}

	/**
	 * 向客户端管道发布消息，此方法不支持短消息服务.
	 * @param appId 地块交易主键
	 * @throws EngineException 异常信息
	 */
	protected synchronized static void broadcastCommand(String commandString) throws EngineException{
		BaseStore.sendCommand2AllClient(commandString);
	}

	/**
	 * 向客户端管道发布消息，如果存在短消息服务，同时发送短消息.
	 * @param appId 地块交易主键
	 * @throws EngineException 异常信息
	 */
	public synchronized static void broadcastCommand(String commandNo,Map paramMap,String commandExpandClass) throws EngineException{
		String commandString = CommandFactory.getInstance().genEngineCommand(commandNo, paramMap, commandExpandClass);
		//if(!TradeConsts.ORDER32.equals(commandNo) && !TradeConsts.ORDER33.equals(commandNo)){
		
		 String filterCilentNo = (String) paramMap.get("filterCilentNo");
		 //如果存在过滤器，过滤客户端，否则发给所有客户端
		 if(StringUtils.isNotBlank(filterCilentNo)){
			 BaseStore.sendCommand2Client(filterCilentNo,commandString);
		 }else{
			 //判断是否过滤客户端，如果需要过滤设置过滤的公告主键
			 String filterNoticeId = (String) paramMap.get("filterNoticeId");
			 //如果存在过滤器，过滤客户端，否则发给所有客户端
			 if(StringUtils.isNotBlank(filterNoticeId)){
				 BaseStore.sendCommand2FilterClient(filterNoticeId,commandString);
			 }else{
				 BaseStore.sendCommand2AllClient(commandString);
			 }
		 }

		//}
		//发送短信
		if(BaseStore.isSmsService){
			BaseStore.smsSupport.sendSms(commandNo, paramMap);
		}
	}
	
	/**
	 * 添加竞价现场.
	 * @param auctionSupport 竞价对象
	 */
	public synchronized static void addAuction(AuctionSupport auctionSupport) {
		//加入场景池
		BaseStore.stagePool.put(auctionSupport.getAuctionNo(), auctionSupport);
	}
	
	/**
	 * 添加竞价现场.
	 * @param auctionSupport 竞价对象
	 */
	public static void addConfirmScene(Task task) {
		//加入场景池
		BaseStore.addConfirmPool(task.getTaskId(), task);
	}

	/**
	 * 移除场景
	 * @param stageNo 场景主键
	 */
	public synchronized static void removeAuction(String stageNo) {
		if(BaseStore.stagePool.containsKey(stageNo)){
			BaseStore.stagePool.remove(stageNo);
		}
	}

	/**
	 * 向客户端池添加客户端通道.
	 * @param clientNo 客户端标识
	 * @param clientPipe 客户端通道
	 * @throws EngineException 
	 */
	public synchronized static void addClientPipe(String clientNo, ClientPipe clientPipe) throws EngineException {
		//假如当前客户端为观摩用户，则检查当前观摩人数，若超过期值则中止登陆
		BaseStore.clientPool.put(clientNo, clientPipe);
	}

	/**
	 * 移除客户端通道.
	 * @param clientNo 客户端标识
	 */
	public synchronized static void removeClientPipe(String clientNo) {
		if(BaseStore.clientPool.containsKey(clientNo)){
			
			BaseStore.clientPool.remove(clientNo);
		}
	}

	/**
	 * 获取数据库操作服务.
	 * @return
	 */
	public static void sendListingTime(Map paramMap){
		 BaseStore.listingSupport.sendListingTime(paramMap);
	}

	/**
	 * 获取数据库操作服务.
	 * @return
	 */
	public static void sendAuctionTime(String auctionNo,Map paramMap){
		if(BaseStore.stagePool.containsKey(auctionNo)){
			AuctionSupport auctionSupport = (AuctionSupport) BaseStore.stagePool.get(auctionNo);
			auctionSupport.sendListingTime(paramMap);
		}
	}
	
	/**
	 * 获取数据库操作服务.
	 * @return
	 */
	public static void sendConfirmTime(String auctionNo,Map paramMap){
		if(BaseStore.confirmPool.containsKey(auctionNo)){
			Task scene = (Task)BaseStore.confirmPool.get(auctionNo);
			scene.sendCurrentTimes();
		}
	}
	
	/**
	 * 获取数据库操作服务.
	 * @return
	 */
	public static void setLimitTime(String auctionNo, String appId, int times){
		if(BaseStore.stagePool.containsKey(auctionNo)){
			AuctionSupport auctionSupport = (AuctionSupport) BaseStore.stagePool.get(auctionNo);
			auctionSupport.setLimitTime(appId, times);
		}
	}
	
	/**
	 * 执行确认底价操作
	 * @return
	 */
	public static void basePriceSubmit(String auctionNo, String appId, String clientNo, String conNo, String yktBh){
		if(BaseStore.stagePool.containsKey(auctionNo)){
			AuctionSupport auctionSupport = (AuctionSupport) BaseStore.stagePool.get(auctionNo);
			auctionSupport.basePriceSubmit(appId, clientNo, conNo, yktBh);
		}
	}
	
	/**
	 * 执行确认底价操作
	 * @return
	 */
	public static void listingBasePriceSubmit(String auctionNo, String appId, String clientNo, String conNo, String yktBh){
		if(BaseStore.confirmPool.containsKey(auctionNo)){
			Task task = (Task)BaseStore.confirmPool.get(auctionNo);
			WxConfirmScene confirmScene = (WxConfirmScene)task;
			confirmScene.basePriceSubmit(appId, clientNo, conNo, yktBh);
		}
	}
	

	public static void breakBasePriceSubmit(String noticeId) {
		if(BaseStore.confirmPool.containsKey(noticeId)){
			Task task = (Task)BaseStore.confirmPool.get(noticeId);
			WxConfirmScene confirmScene = (WxConfirmScene)task;
			confirmScene.next();
		}
	}
	
	/**
	 * 获取数据库操作服务.
	 * @return
	 */
	public static DbSupport getDbSupport(){
		return BaseStore.dbSupport;
	}
	
	/**
	 * 获取短信操作服务.
	 * @return
	 */
	public static SmsSupport getSmsSupport(){
		return BaseStore.smsSupport;
	}
	
	/**
	 * 获取客户端池.
	 * @return
	 */
	public static HashMap getClientPool(){
		return BaseStore.clientPool;
	}
	
	/**
	 * 获取所有的地块信息.
	 * @return
	 */
	public static Map findAllTradeBlock(){
		return BaseStore.tradeBlocks;
	}
	
	/**
	 * 是否有短信服务.
	 * @return
	 */
	public static boolean isSmsService() {
		return BaseStore.isSmsService;
	}
	
	/**
	 * 是否有时间推送服务.
	 * @return
	 */
	public static boolean isTimeService() {
		return BaseStore.isTimeService;
	} 
	
	/**
	 * 通知核心引擎地块交易完成.
	 * @return
	 */
	public synchronized static void finishTradeBlock(String appId){
		BaseStore.tradeBlocks.remove(appId);
	}
	
	/**
	 * 更新所有的地块信息,已经存在的地块不更新，只更新不存在的地块.
	 * @return
	 */
	public synchronized static void updateTradeBlocks(){
		LinkedHashMap updateBlocks = BaseStore.dbSupport.updateEngineBlockInfo();
		if(updateBlocks != null){
			  Iterator iter = updateBlocks.keySet().iterator();
			  while (iter.hasNext()) {
					Object key = iter.next();
					TradeBlock tradeBlock = (TradeBlock) updateBlocks.get(key);
					if(!BaseStore.tradeBlocks.containsKey(tradeBlock.getAppId())){
						BaseStore.tradeBlocks.put(tradeBlock.getAppId(), tradeBlock);
					} else {
						if (tradeBlock.isAim() && StringUtils.isNotEmpty(tradeBlock.getTopConNum())) {
							TradeBlock block = (TradeBlock)BaseStore.tradeBlocks.get(tradeBlock.getAppId());
							if (StringUtils.isEmpty(block.getTopConNum())) {
								BaseStore.tradeBlocks.put(tradeBlock.getAppId(), tradeBlock);
							}
						}
					}
				}
		}
	}

	/**
	 * 从引擎中获取指定公告的地块列表.
	 * @param noticeId 公告主键
	 * @return
	 */
	public static List findTradeBlockListByNoticeId(String noticeId){
		//公告主键不为空
		if(StringUtils.isBlank(noticeId)){
			return null;
		}
		List resultList = new ArrayList();
		  Iterator iter = BaseStore.tradeBlocks.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				TradeBlock tradeBlock = (TradeBlock) BaseStore.tradeBlocks.get(key);
				 if(noticeId.equals(tradeBlock.getNoticeId())){
					 resultList.add(tradeBlock);
				 }
			}
		return resultList;
	}

	/**
	 * 重新加载个人信息至交易引擎中
	 * 
	 * @param clientNo
	 * @param pipe
	 * @throws EngineException
	 */
	public static void reloadClientPipe(String clientNo, ClientPipe pipe) throws EngineException {
		// TODO Auto-generated method stub
		if (BaseStore.clientPool.containsKey(clientNo)) {
			ClientPipe getClientPipe = (ClientPipe)BaseStore.clientPool.get(clientNo);
			getClientPipe.reload(pipe);
			return;
		}
		addClientPipe(clientNo, pipe);
	}
	
	/**
	 * 在交易引擎外表通过客户端编号重新加载个人信息至交易引擎中
	 * 
	 * @param clientNo
	 */
	public synchronized static void reloadClientPipe(String clientNo) {
		if (BaseStore.clientPool.containsKey(clientNo)) {
			ClientPipe pipe = (ClientPipe)BaseStore.clientPool.get(clientNo);
			Map paramMap = getDbSupport().initClientModel2(clientNo, null);
			Collection values = paramMap.values();
			pipe.setClientNodes(new ArrayList(values));
			pipe.setClientNo(clientNo);
		}
	}
	/**
	 * 根据appId刷新地块信息。
	 * @param appId
	 */
	public synchronized static void reloadClientPipeByAppId(String appId){
		if (BaseStore.tradeBlocks.containsKey(appId)) {
			//刷新地块
			TradeBlock newBlock = getDbSupport().updateEngineBlockInfo(appId);
			BaseStore.tradeBlocks.remove(appId);
			BaseStore.tradeBlocks.put(appId, newBlock);
		}
	}
	
	public synchronized static TradeBlock getTradeBlock(String noticeId) {
		if(BaseStore.stagePool.containsKey(noticeId)){
			AuctionSupport auctionSupport = (AuctionSupport) BaseStore.stagePool.get(noticeId);
			return auctionSupport.getTradeBlock(noticeId);
		}
		return null;
	}
	
	
	
	
	
	
	/**
	 * 向观摩客户端池添加客户端通道.
	 * @param clientNo 客户端标识
	 * @param clientPipe 客户端通道
	 * @throws EngineException 
	 */
	public synchronized static void addGmClientPipe(String clientNo, GmClientPipe clientPipe) throws EngineException {
		//假如当前客户端为观摩用户，则检查当前观摩人数，若超过期值则中止登陆
		BaseStore.gmClientPool.put(clientNo, clientPipe);
	}
	/**
	 * 移除观摩客户端通道.
	 * @param clientNo 客户端标识
	 */
	public synchronized static void removeGmClientPipe(String clientNo) {
		if(BaseStore.gmClientPool.containsKey(clientNo)){
			BaseStore.gmClientPool.remove(clientNo);
		}
	}

	/**
	 * 向观摩通报者池添加通道.
	 * @param clientNo 客户端标识
	 * @param clientPipe 客户端通道
	 * @throws EngineException 
	 */
	public synchronized static void addGmServerPipe(String clientNo, GmServerPipe serverPipe) throws EngineException {
		//假如当前客户端为观摩用户，则检查当前观摩人数，若超过期值则中止登陆
		BaseStore.gmServerPool.put(clientNo, serverPipe);
	}

	/**
	 * 移除观摩通报者通道.
	 * @param clientNo 客户端标识
	 */
	public synchronized static void removeGmServerPipe(String clientNo) {
		if(BaseStore.gmServerPool.containsKey(clientNo)){
			BaseStore.gmServerPool.remove(clientNo);
		}
	}
	/**
	 * 获取观摩通报者池.
	 * @return
	 */
	public static HashMap getGmServerPool(){
		return BaseStore.gmServerPool;
	}
	/**
	 * 获取观摩客户端客户端池.
	 * @return
	 */
	public static HashMap getGmClientPool(){
		return BaseStore.gmClientPool;
	}
	/**
	 * 向观摩客户端管道发布消息
	 * @param commandString 消息具体内容
	 * @throws EngineException 异常信息
	 */
	public synchronized static void sendCommand2AllGmClient(String noticeId,String commandString) throws EngineException{
		BaseStore.sendCommand2AllGmClient(noticeId,commandString);
	}
	
}
