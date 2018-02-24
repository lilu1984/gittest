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
 * ��������ĺ��Ĵ洢�Ͳ������򣬽�����ClientPipe��CoreEngine��CoreStore��CoreService.
 * @author sunxin
 *
 */
public class BaseStore {

	protected static LinkedHashMap tradeBlocks = new LinkedHashMap();//���׵ؿ��ջ--���۽׶�
	
	protected static HashMap clientPool = new HashMap();// client socket pool

	protected static HashMap gmServerPool = new HashMap();//��Ħ�ķ������˳�
	
	protected static HashMap gmClientPool = new HashMap();//��Ħ��websocket���ӳ�
			
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
	 * ���ݿⱣ��۸�.
	 * @param tradePrice �۸����
	 * @return
	 */
	protected static boolean pushPrice(TradePrice tradePrice){
		TradeBlock tradeBlock = (TradeBlock) tradeBlocks.get(tradePrice.getAppId());
		if(tradeBlock != null && validPrice(tradeBlock,tradePrice)){
			//���ñ���ʱ��
			tradePrice.setPriceTime(new Timestamp(System.currentTimeMillis()));
			//�������ݿ�
			boolean isSuccess = false;
			if("1".equals(tradePrice.getPhase())){
				isSuccess = dbSupport.saveListingPrice(tradeBlock,tradePrice);
			}else{
				isSuccess = dbSupport.saveAuctionPrice(tradeBlock,tradePrice);
			}
			//�������ɹ�,���º��Ĵ洢����Ϣ
			if(isSuccess){
				//�����ִ�
				tradeBlock.setPriceNum(tradeBlock.getPriceNum()+1);
				//������߾���
				tradeBlock.setTopPrice(tradePrice.getPrice());
				//���ü۸���߾��ͻ��˱��
				tradeBlock.setTopClientNo(tradePrice.getClientNo());
				//���ü۸���ߺ���
				tradeBlock.setTopConNum(tradePrice.getConNum());
				//���þ����ִ�
				tradePrice.setPriceNum(tradeBlock.getPriceNum());
			}
			return isSuccess;
		}
		return false;
	}
	
	/**
	 * �����пͻ��˷�������.
	 * @param commandString ��������
	 * @throws EngineException �����쳣
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
	 * �����пͻ��˷�������.
	 * @param commandString ��������
	 * @throws EngineException �����쳣
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
	 * ��ָ���ͻ��˷�������.
	 * @param clientId �ͻ��˱�ʶ
	 * @param commandString ��������
	 * @throws EngineException �����쳣
	 */
	protected  static void sendCommand2Client(String clientId, String commandString)throws EngineException {
		if(StringUtils.isNotBlank(commandString) && clientPool.containsKey(clientId)){
			ClientPipe clientPipe = (ClientPipe) clientPool.get(clientId);
			clientPipe.sendCommand(commandString);
		}
	}
	
	/**
	 * ��ͻ��˳���ӿͻ���ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 * @param clientPipe �ͻ���ͨ��
	 */
	protected static void addClientPipe(String clientNo, ClientPipe clientPipe) {
		clientPool.put(clientNo, clientPipe);
	}
	
	/**
	 * �Ƴ�����.
	 * @param stageNo ��������
	 */
	protected static void removeStage(String stageNo) {
		stagePool.remove(stageNo);
	}
	
	/**
	 * �Ƴ��ͻ���ͨ��.
	 * @param clientNo �ͻ��˱�ʶ
	 */
	protected static  void removeClientPipe(String clientNo) {
		clientPool.remove(clientNo);
	}
	

	/**
	 * ֪ͨ��������ͻ���ͨ���¼�.
	 * @param event �ͻ���ͨ��
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
     * ���ۼ۸�ѹջ
     *2016��10��25����ӹ��ܱ���У�飬���ƽ׶�ÿ��������ֻ�ܱ���һ�Σ���ֻ�ܼ���һ���Ӽ۷���
     */
    private static boolean validPrice(TradeBlock tradeBlock,TradePrice tradePrice){
    	if (StringUtils.isEmpty(tradePrice.getAppId())) return false;
    	//������޼۵�����£�����������޼۵������ͨ����
		if(tradeBlock != null&&tradeBlock.getHaveMaxPrice()){
			if(tradePrice.getPrice()>tradeBlock.getMaxPrice()){
				return false;
			}
		}
//    	if("1".equals(tradePrice.getPhase())){
//    		//���ƽ׶εļ۸�У��
//    		//1��ȡ�õ�ǰ�ؿ鵱ǰ�����˵����б��ۡ������ڱ����򷵻�false
//    		//2���жϵ�ǰ���˹���������۸��Ƿ���ڵ�����ʼ�ۡ�С�ڵ��ڵ�ǰ��߼�+һ���Ӽ۷��ȡ�
//    		//3���жϵ�ǰ���˹���������۸��Ƿ����ǰ��߼�+һ���Ӽ۷��ȡ�
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
    		//���۽׶εļ۸�У��
    		if(tradeBlock != null && tradeBlock.getPriceNum()==0 && tradePrice.getPrice()>= tradeBlock.getTopPrice()){
	    		// ���û�б��ۣ���ǰ���۴��ڵ�����߱��ۼ���
	    		return true;
	    	} else if(tradeBlock != null && tradePrice.getPrice()>=(tradeBlock.getTopPrice()+tradeBlock.getPriceAdd())){
	    		// ����Ļ� ��ǰ����Ҫ���ڵ�����߱���+�Ӽ۷���
				return true;
			} else if(tradeBlock != null && tradePrice.getPrice()>= tradeBlock.getTopPrice()){
				//���۸������߼ۺͼӼ۷���֮����ж��Ƿ��е׼ۣ����е׼��ж�����۸��Ƿ���ڵ׼ۣ����ڵ׼���ͨ��������ͨ��
				if(tradeBlock.getBasePrice()!=null&&tradePrice.getPrice() == tradeBlock.getBasePrice().doubleValue()){
					//�е׼�
					return true;
				}else{
					//�޵׼�
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
	 * �����й�Ħ�ͻ��˷�������.
	 * @param commandString ��������
	 * @throws EngineException �����쳣
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
