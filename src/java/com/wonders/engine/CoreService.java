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
 * ���ķ�������������ⲿ���ṩ����
 * @author sunxin
 *
 */
public class CoreService {

	/**
	 * �ͻ���ͨ�������¼�.
	 * @param event �ͻ���ͨ��
	 */
	public synchronized static void handleChangeEvent(ChangeEvent event) throws EngineException {
		BaseStore.noticeStageByBlock(event);
	}

	/**
	 * �۸�ѹջ.
	 * @param tradePrice �۸���Ϣ
	 * @return
	 */
	public synchronized static boolean pushPrice(TradePrice tradePrice){
		return BaseStore.pushPrice(tradePrice);
	}

	/**
	 * ��ͻ��˹ܵ�������Ϣ���˷�����֧�ֶ���Ϣ����.
	 * @param appId �ؿ齻������
	 * @throws EngineException �쳣��Ϣ
	 */
	protected synchronized static void broadcastCommand(String commandString) throws EngineException{
		BaseStore.sendCommand2AllClient(commandString);
	}

	/**
	 * ��ͻ��˹ܵ�������Ϣ��������ڶ���Ϣ����ͬʱ���Ͷ���Ϣ.
	 * @param appId �ؿ齻������
	 * @throws EngineException �쳣��Ϣ
	 */
	public synchronized static void broadcastCommand(String commandNo,Map paramMap,String commandExpandClass) throws EngineException{
		String commandString = CommandFactory.getInstance().genEngineCommand(commandNo, paramMap, commandExpandClass);
		//if(!TradeConsts.ORDER32.equals(commandNo) && !TradeConsts.ORDER33.equals(commandNo)){
		
		 String filterCilentNo = (String) paramMap.get("filterCilentNo");
		 //������ڹ����������˿ͻ��ˣ����򷢸����пͻ���
		 if(StringUtils.isNotBlank(filterCilentNo)){
			 BaseStore.sendCommand2Client(filterCilentNo,commandString);
		 }else{
			 //�ж��Ƿ���˿ͻ��ˣ������Ҫ�������ù��˵Ĺ�������
			 String filterNoticeId = (String) paramMap.get("filterNoticeId");
			 //������ڹ����������˿ͻ��ˣ����򷢸����пͻ���
			 if(StringUtils.isNotBlank(filterNoticeId)){
				 BaseStore.sendCommand2FilterClient(filterNoticeId,commandString);
			 }else{
				 BaseStore.sendCommand2AllClient(commandString);
			 }
		 }

		//}
		//���Ͷ���
		if(BaseStore.isSmsService){
			BaseStore.smsSupport.sendSms(commandNo, paramMap);
		}
	}
	
	/**
	 * ��Ӿ����ֳ�.
	 * @param auctionSupport ���۶���
	 */
	public synchronized static void addAuction(AuctionSupport auctionSupport) {
		//���볡����
		BaseStore.stagePool.put(auctionSupport.getAuctionNo(), auctionSupport);
	}
	
	/**
	 * ��Ӿ����ֳ�.
	 * @param auctionSupport ���۶���
	 */
	public static void addConfirmScene(Task task) {
		//���볡����
		BaseStore.addConfirmPool(task.getTaskId(), task);
	}

	/**
	 * �Ƴ�����
	 * @param stageNo ��������
	 */
	public synchronized static void removeAuction(String stageNo) {
		if(BaseStore.stagePool.containsKey(stageNo)){
			BaseStore.stagePool.remove(stageNo);
		}
	}

	/**
	 * ��ͻ��˳���ӿͻ���ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 * @param clientPipe �ͻ���ͨ��
	 * @throws EngineException 
	 */
	public synchronized static void addClientPipe(String clientNo, ClientPipe clientPipe) throws EngineException {
		//���統ǰ�ͻ���Ϊ��Ħ�û������鵱ǰ��Ħ��������������ֵ����ֹ��½
		BaseStore.clientPool.put(clientNo, clientPipe);
	}

	/**
	 * �Ƴ��ͻ���ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 */
	public synchronized static void removeClientPipe(String clientNo) {
		if(BaseStore.clientPool.containsKey(clientNo)){
			
			BaseStore.clientPool.remove(clientNo);
		}
	}

	/**
	 * ��ȡ���ݿ��������.
	 * @return
	 */
	public static void sendListingTime(Map paramMap){
		 BaseStore.listingSupport.sendListingTime(paramMap);
	}

	/**
	 * ��ȡ���ݿ��������.
	 * @return
	 */
	public static void sendAuctionTime(String auctionNo,Map paramMap){
		if(BaseStore.stagePool.containsKey(auctionNo)){
			AuctionSupport auctionSupport = (AuctionSupport) BaseStore.stagePool.get(auctionNo);
			auctionSupport.sendListingTime(paramMap);
		}
	}
	
	/**
	 * ��ȡ���ݿ��������.
	 * @return
	 */
	public static void sendConfirmTime(String auctionNo,Map paramMap){
		if(BaseStore.confirmPool.containsKey(auctionNo)){
			Task scene = (Task)BaseStore.confirmPool.get(auctionNo);
			scene.sendCurrentTimes();
		}
	}
	
	/**
	 * ��ȡ���ݿ��������.
	 * @return
	 */
	public static void setLimitTime(String auctionNo, String appId, int times){
		if(BaseStore.stagePool.containsKey(auctionNo)){
			AuctionSupport auctionSupport = (AuctionSupport) BaseStore.stagePool.get(auctionNo);
			auctionSupport.setLimitTime(appId, times);
		}
	}
	
	/**
	 * ִ��ȷ�ϵ׼۲���
	 * @return
	 */
	public static void basePriceSubmit(String auctionNo, String appId, String clientNo, String conNo, String yktBh){
		if(BaseStore.stagePool.containsKey(auctionNo)){
			AuctionSupport auctionSupport = (AuctionSupport) BaseStore.stagePool.get(auctionNo);
			auctionSupport.basePriceSubmit(appId, clientNo, conNo, yktBh);
		}
	}
	
	/**
	 * ִ��ȷ�ϵ׼۲���
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
	 * ��ȡ���ݿ��������.
	 * @return
	 */
	public static DbSupport getDbSupport(){
		return BaseStore.dbSupport;
	}
	
	/**
	 * ��ȡ���Ų�������.
	 * @return
	 */
	public static SmsSupport getSmsSupport(){
		return BaseStore.smsSupport;
	}
	
	/**
	 * ��ȡ�ͻ��˳�.
	 * @return
	 */
	public static HashMap getClientPool(){
		return BaseStore.clientPool;
	}
	
	/**
	 * ��ȡ���еĵؿ���Ϣ.
	 * @return
	 */
	public static Map findAllTradeBlock(){
		return BaseStore.tradeBlocks;
	}
	
	/**
	 * �Ƿ��ж��ŷ���.
	 * @return
	 */
	public static boolean isSmsService() {
		return BaseStore.isSmsService;
	}
	
	/**
	 * �Ƿ���ʱ�����ͷ���.
	 * @return
	 */
	public static boolean isTimeService() {
		return BaseStore.isTimeService;
	} 
	
	/**
	 * ֪ͨ��������ؿ齻�����.
	 * @return
	 */
	public synchronized static void finishTradeBlock(String appId){
		BaseStore.tradeBlocks.remove(appId);
	}
	
	/**
	 * �������еĵؿ���Ϣ,�Ѿ����ڵĵؿ鲻���£�ֻ���²����ڵĵؿ�.
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
	 * �������л�ȡָ������ĵؿ��б�.
	 * @param noticeId ��������
	 * @return
	 */
	public static List findTradeBlockListByNoticeId(String noticeId){
		//����������Ϊ��
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
	 * ���¼��ظ�����Ϣ������������
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
	 * �ڽ����������ͨ���ͻ��˱�����¼��ظ�����Ϣ������������
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
	 * ����appIdˢ�µؿ���Ϣ��
	 * @param appId
	 */
	public synchronized static void reloadClientPipeByAppId(String appId){
		if (BaseStore.tradeBlocks.containsKey(appId)) {
			//ˢ�µؿ�
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
	 * ���Ħ�ͻ��˳���ӿͻ���ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 * @param clientPipe �ͻ���ͨ��
	 * @throws EngineException 
	 */
	public synchronized static void addGmClientPipe(String clientNo, GmClientPipe clientPipe) throws EngineException {
		//���統ǰ�ͻ���Ϊ��Ħ�û������鵱ǰ��Ħ��������������ֵ����ֹ��½
		BaseStore.gmClientPool.put(clientNo, clientPipe);
	}
	/**
	 * �Ƴ���Ħ�ͻ���ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 */
	public synchronized static void removeGmClientPipe(String clientNo) {
		if(BaseStore.gmClientPool.containsKey(clientNo)){
			BaseStore.gmClientPool.remove(clientNo);
		}
	}

	/**
	 * ���Ħͨ���߳����ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 * @param clientPipe �ͻ���ͨ��
	 * @throws EngineException 
	 */
	public synchronized static void addGmServerPipe(String clientNo, GmServerPipe serverPipe) throws EngineException {
		//���統ǰ�ͻ���Ϊ��Ħ�û������鵱ǰ��Ħ��������������ֵ����ֹ��½
		BaseStore.gmServerPool.put(clientNo, serverPipe);
	}

	/**
	 * �Ƴ���Ħͨ����ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 */
	public synchronized static void removeGmServerPipe(String clientNo) {
		if(BaseStore.gmServerPool.containsKey(clientNo)){
			BaseStore.gmServerPool.remove(clientNo);
		}
	}
	/**
	 * ��ȡ��Ħͨ���߳�.
	 * @return
	 */
	public static HashMap getGmServerPool(){
		return BaseStore.gmServerPool;
	}
	/**
	 * ��ȡ��Ħ�ͻ��˿ͻ��˳�.
	 * @return
	 */
	public static HashMap getGmClientPool(){
		return BaseStore.gmClientPool;
	}
	/**
	 * ���Ħ�ͻ��˹ܵ�������Ϣ
	 * @param commandString ��Ϣ��������
	 * @throws EngineException �쳣��Ϣ
	 */
	public synchronized static void sendCommand2AllGmClient(String noticeId,String commandString) throws EngineException{
		BaseStore.sendCommand2AllGmClient(noticeId,commandString);
	}
	
}
