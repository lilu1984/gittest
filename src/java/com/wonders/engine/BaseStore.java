package com.wonders.engine;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;

import com.wonders.engine.auction.AuctionSupport;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.database.DbSupport;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.listing.ListingSupport;
import com.wonders.engine.sms.SmsSupport;
import com.wonders.engine.socket.ClientPipe;
import com.wonders.engine.socket.GmClientPipe;
import com.wonders.engine.socket.event.ChangeEvent;
import com.wonders.engine.socket.handler.HandlerSupport;
import com.wonders.engine.socket.listener.ChangeListener;
import com.wonders.engine.timer.Task;

/**
 * 交易引擎的核心存储和操作区域，仅限于ClientPipe、CoreEngine、CoreStore、CoreService.
 * @author sunxin
 *
 */
public class BaseStore {

	protected static LinkedHashMap tradeBlocks = new LinkedHashMap();//交易地块堆栈--竞价阶段
	
	protected static HashMap clientPool = new HashMap();// client socket pool

	protected static HashMap gmServerPool = new HashMap();//观摩的服务器端池
	
	protected static HashMap gmClientPool = new HashMap();//观摩的websocket连接池
			
	protected static HashMap stagePool = new HashMap();// client socket pool
	
	protected static HashMap confirmPool = new HashMap();
	
	protected static ListingSupport listingSupport = null;
	
	protected static DbSupport dbSupport = null;

	protected static SmsSupport smsSupport = null;

	protected static HandlerSupport handlerSupport = null;

	protected static boolean isSmsService= false;

	protected static int broadcastLevel = 1;
	
	protected static boolean isTimeService = true;
	
	/**
	 * 数据库保存价格.
	 * @param tradePrice 价格对象
	 * @return
	 */
	protected static boolean pushPrice(TradePrice tradePrice){
		TradeBlock tradeBlock = (TradeBlock) tradeBlocks.get(tradePrice.getAppId());
		if(tradeBlock != null && validPrice(tradeBlock,tradePrice)){
			//设置保存时间
			tradePrice.setPriceTime(new Timestamp(System.currentTimeMillis()));
			//存入数据库
			boolean isSuccess = false;
			if("1".equals(tradePrice.getPhase())){
				isSuccess = dbSupport.saveListingPrice(tradeBlock,tradePrice);
			}else{
				isSuccess = dbSupport.saveAuctionPrice(tradeBlock,tradePrice);
			}
			//如果保存成功,更新核心存储区信息
			if(isSuccess){
				//设置轮次
				tradeBlock.setPriceNum(tradeBlock.getPriceNum()+1);
				//设置最高竞买
				tradeBlock.setTopPrice(tradePrice.getPrice());
				//设置价格最高竞客户端编号
				tradeBlock.setTopClientNo(tradePrice.getClientNo());
				//设置价格最高号牌
				tradeBlock.setTopConNum(tradePrice.getConNum());
				//设置竞价轮次
				tradePrice.setPriceNum(tradeBlock.getPriceNum());
			}
			return isSuccess;
		}
		return false;
	}
	
	/**
	 * 向所有客户端发送命令.
	 * @param commandString 命令内容
	 * @throws EngineException 引擎异常
	 */
	protected  static void sendCommand2AllClient(String commandString) throws EngineException {
		if(StringUtils.isNotBlank(commandString)){
			Iterator iter = clientPool.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				ClientPipe clientPipe = (ClientPipe) clientPool.get(key);
				clientPipe.sendCommand(commandString);
			}
		}
	}
	/**
	 * 向所有客户端发送命令.
	 * @param commandString 命令内容
	 * @throws EngineException 引擎异常
	 */
	protected  static void sendCommand2FilterClient(String filterNoticeId,String commandString) throws EngineException {
		if(StringUtils.isNotBlank(commandString)){
			Iterator iter = clientPool.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				ClientPipe clientPipe = (ClientPipe) clientPool.get(key);
				if(clientPipe.isBidderNotice(filterNoticeId)){
					clientPipe.sendCommand(commandString);
				}
			}
		}
	}
	
	/**
	 * 向指定客户端发送命令.
	 * @param clientId 客户端标识
	 * @param commandString 命令内容
	 * @throws EngineException 引擎异常
	 */
	protected  static void sendCommand2Client(String clientId, String commandString)throws EngineException {
		if(StringUtils.isNotBlank(commandString) && clientPool.containsKey(clientId)){
			ClientPipe clientPipe = (ClientPipe) clientPool.get(clientId);
			clientPipe.sendCommand(commandString);
		}
	}
	
	/**
	 * 向客户端池添加客户端通道.
	 * @param clientNo 客户端标识
	 * @param clientPipe 客户端通道
	 */
	protected static void addClientPipe(String clientNo, ClientPipe clientPipe) {
		clientPool.put(clientNo, clientPipe);
	}
	
	/**
	 * 移除场景.
	 * @param stageNo 场景主键
	 */
	protected static void removeStage(String stageNo) {
		stagePool.remove(stageNo);
	}
	
	/**
	 * 移除客户端通道.
	 * @param clientNo 客户端标识
	 */
	protected static  void removeClientPipe(String clientNo) {
		clientPool.remove(clientNo);
	}
	

	/**
	 * 通知场景处理客户端通道事件.
	 * @param event 客户端通道
	 */
	protected  static void noticeStageByBlock(ChangeEvent event) {
		Iterator iter = BaseStore.stagePool.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			AuctionSupport stageSupport = (AuctionSupport) BaseStore.stagePool.get(key);
			if(stageSupport.existBlock(event.getAppId()) && stageSupport instanceof ChangeListener){
				ChangeListener changeListener = (ChangeListener)stageSupport;
				changeListener.handleChangeEvent(event);
			}
		}
	}
	
    /**
     * 竞价价格压栈
     *2016年10月25日添加功能报价校验，挂牌阶段每个竞买人只能报价一次，且只能加上一个加价幅度
     */
    private static boolean validPrice(TradeBlock tradeBlock,TradePrice tradePrice){
    	if (StringUtils.isEmpty(tradePrice.getAppId())) return false;
    	//有最高限价的情况下，报大于最高限价的情况则不通过。
		if(tradeBlock != null&&tradeBlock.getHaveMaxPrice()){
			if(tradePrice.getPrice()>tradeBlock.getMaxPrice()){
				return false;
			}
		}
//    	if("1".equals(tradePrice.getPhase())){
//    		//挂牌阶段的价格校验
//    		//1、取得当前地块当前报价人的所有报价、若存在报价则返回false
//    		//2、判断当前无人挂牌情况，价格是否大于等于起始价。小于等于当前最高价+一个加价幅度。
//    		//3、判断当前有人挂牌情况，价格是否等于前最高价+一个加价幅度。
//    		if(!dbSupport.isFirstSubmitPrice(tradeBlock, tradePrice)){
//    			return false;
//    		}else if(tradeBlock.getPriceNum()==0){
//    			if(tradePrice.getPrice()>= tradeBlock.getTopPrice()&&tradePrice.getPrice()<=(tradeBlock.getTopPrice()+tradeBlock.getPriceAdd())){
//    				return true;
//    			}else{
//    				return false;
//    			}
//    		}else if(tradePrice.getPrice()==(tradeBlock.getTopPrice()+tradeBlock.getPriceAdd())){
//    			return true;
//    		}else{
//    			return false;
//    		}
//    	}else{
    		//竞价阶段的价格校验
    		if(tradeBlock != null && tradeBlock.getPriceNum()==0 && tradePrice.getPrice()>= tradeBlock.getTopPrice()){
	    		// 如果没有报价，则当前报价大于等于最高报价即可
	    		return true;
	    	} else if(tradeBlock != null && tradePrice.getPrice()>=(tradeBlock.getTopPrice()+tradeBlock.getPriceAdd())){
	    		// 否则的话 当前报价要大于等于最高报价+加价幅度
				return true;
			} else if(tradeBlock != null && tradePrice.getPrice()>= tradeBlock.getTopPrice()){
				//若价格介于最高价和加价幅度之间的判断是否有底价，若有底价判断输入价格是否等于底价，等于底价则通过，否则不通过
				if(tradeBlock.getBasePrice()!=null&&tradePrice.getPrice() == tradeBlock.getBasePrice().doubleValue()){
					//有底价
					return true;
				}else{
					//无底价
					return false;
				}
			}
    	//}
    	return false;
    }

	public static HandlerSupport getHandlerSupport() {
		return handlerSupport;
	}

	public static void addConfirmPool(String sceneId, Task task) {
		synchronized (confirmPool) {
			confirmPool.put(sceneId, task);
		}
	}
	
	public static boolean inConfirmPool(String sceneId) {
		return confirmPool.containsKey(sceneId);
	}
	
	
	/**
	 * 向所有观摩客户端发送命令.
	 * @param commandString 命令内容
	 * @throws EngineException 引擎异常
	 */
	protected  static void sendCommand2AllGmClient(String noticeId,String commandString) throws EngineException {
		if(StringUtils.isNotBlank(commandString)){
			Iterator iter = gmClientPool.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				GmClientPipe gmClientPipe = (GmClientPipe) gmClientPool.get(key);
				if(noticeId.equals(gmClientPipe.getNoticeId())){
					gmClientPipe.sendCommand(commandString);
				}
				
			}
		}
	}
}
