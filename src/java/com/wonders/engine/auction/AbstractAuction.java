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
 * �Եؿ��������׵ĳ�����.
 * @author sunxin
 *
 */
public abstract class AbstractAuction implements AuctionSupport,Runnable{
	private Logger logger = Logger.getLogger(AbstractAuction.class);
	/**
	 * �ͻ���ͨ���߳�
	 */
	private Thread stageThread = null;
	/**
	 * �����׶εĿ���.
	 */
	private int phaseSwitch = 2;//����ʱ�����׶ο���--���۽׶�
	/**
	 * �����еؿ����
	 */
	private int intervalTime = TradeConsts.TRADE_INTERVAL_TIME;//�����еؿ����
	/**
	 * �Ƿ��ǵ�һ�ν���ÿ���׶�.
	 */
	private boolean isFirst = true;//����ʱ������--���۽׶�
	/**
	 * �Ƿ��ǵ�һ�ο��.
	 */
	private boolean isFirstBlock = false;//����ʱ������--���۽׶�
	/**
	 * ���׵ؿ���Ŷ��б�
	 */
	public LinkedList tradeBlockList = null;
	/**
	 * ��ǰ���׵ؿ�
	 */
	private TradeBlock curTradeBlock = null;
	/**
	 * ����ʱ����ʱ��ı�ʶ��0�������ͣ�1������
	 */
	private long sendTimeSwitch = 0;
	
	public void sendListingTime(Map paramMap){
		//sendTimeSwitch = 1;
		//����ǵؿ龺�۽׶�
		if(phaseSwitch ==1){
			sendCommand33(curTradeBlock.getLimitTime(),curTradeBlock.getAppId(), paramMap);
		}else{//����Ǽ�Ъ��
			sendCommand33(intervalTime, null, paramMap);
		}
	}
	/**
	 * ��ʼ������
	 * @param stageNo ��������
	 * @param tradeBlockList ���׵ؿ��б�
	 * @param isStart �Ƿ���������
	 */
	public abstract void initAuction(String stageNo,LinkedList tradeBlockList,boolean isStart);
	
	/**
	 * �ָ�����ʱ��
	 *
	 */
	public void resumeLimitTime() {
        curTradeBlock.setLimitTime(TradeConsts.TRADE_LIMIT_TIME);
        sendTimeSwitch = 1;
	}

	/**
	 * ��ϸ��߱���.
	 * @param tradePrice ���۶���
	 */
	public void updateTopPrice(TradePrice tradePrice) {
		//�����ִ�
		curTradeBlock.setPriceNum(tradePrice.getPriceNum());
		//������߾���
		curTradeBlock.setTopPrice(tradePrice.getPrice());
		//������߾��ͻ���
		curTradeBlock.setTopClientNo(tradePrice.getClientNo());
	}
	/**
	 * ���²���ҡ�ŵĺ���
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
	 * ��ϸ�Ӽ۷���.
	 * @param tradePrice ���۶���
	 */
	public void updateAddPrice(long addnum) {
		//���üӼ۷���
		curTradeBlock.setPriceAdd(addnum);
	}
	
	/**
	 * ���ü����ʱ��.
	 * @param intervalTime �����ʱ��
	 */
	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}
	/**
	 * ���ý׶�״̬.
	 * @param phaseSwitch
	 */
	public void setPhaseSwitch(int phaseSwitch) {
		this.phaseSwitch = phaseSwitch;
	}
	
	/**
	 * �жϵؿ��Ƿ����ڴ˳���.
	 */
	public boolean existBlock(String appId) {
		if(curTradeBlock != null && curTradeBlock.getAppId().equals(appId)){
			return true;
		}
		return false;
	}
	
	/**
	 *��������.
	 */
	public void startStage() {
		stageThread = new Thread(this, "stageThread");
		stageThread.start();
		//��������
		tradeStart(curTradeBlock);
		System.out.println("--�����Ѿ�����--");
	}
	
	public void run() {
		try {	
			while (true) {
					//���״̬Ϊ-1�������˳���
					if(phaseSwitch == -1){
						break;
					}
					if (phaseSwitch != 0) {//�����0������ʱ����δ���������ڵȴ�״̬
						if(phaseSwitch ==1){//�����1�����뽻�״��������е�һ��صĽ���
							//���׿�ʼʱ��ʼ��״̬
							if(isFirst){
								//��ȡ��һ���ؿ�
						        curTradeBlock = (TradeBlock) tradeBlockList.getFirst();
						        //���׿�ʼ����
								blockTradeStart(curTradeBlock);
								//���׿�ʼ
								sendCommand1Series(TradeConsts.ORDER12);
								//���׿�ʼ�����͵���ʱʱ��
								isFirst = false;
							}
							//60���30���ʱ��У��
							if(curTradeBlock.getLimitTime() == 240 ||curTradeBlock.getLimitTime() == 180 ||curTradeBlock.getLimitTime() == 120 || curTradeBlock.getLimitTime() == 60 || curTradeBlock.getLimitTime() == 30){
								sendTimeSwitch = 1;
							}
				            if(sendTimeSwitch == 1){
								 sendCommand33(curTradeBlock.getLimitTime(),curTradeBlock.getAppId(),null);
								 sendTimeSwitch = 0;
							}
				            curTradeBlock.setLimitTime(curTradeBlock.getLimitTime()-1);
				            //���׵�ǰ�۸��Ƿ�����߼�
				            if(curTradeBlock.confirmMaxPriceByTopPrice()&&!curTradeBlock.isSendMaxPriceMsg()){
				            	//�����Ѿ��ﵽ��ǰ��߱�������ͻ��˷���35������
				            	sendCommand38(curTradeBlock);
				            	curTradeBlock.setSendMaxPriceMsg(true);
				            }
				            //���ü��ʱ��
				            if(curTradeBlock.getLimitTime() <= 0){
				            	// TODO: ���ӵ׼��жϹ���
				            	// 1. if (û�п�ʼ�׼۵���ʱ) {
				            	if (!curTradeBlock.isMiniLimit()) { // ���û�п�ʼ�ͼ۵���ʱ
				            		// 2. if (�е׼�) {
				            		if (containsBasePrice(curTradeBlock)) { // �е׼�
				            			if (!curTradeBlock.isValidTopPrice()) {
				            				// 1. ����ʱ�� 
				            				curTradeBlock.beginMiniPriceLimit();
				            				// 2. ����ʱ��
				            				sendCommand33(curTradeBlock.getLimitTime(), curTradeBlock.getAppId(), null);
				            				// 3. ������ʾ��Ϣ ��ע���޸Ķ��ţ�
				            				sendCommand34(curTradeBlock);
				            				// 4. ���µ�ǰ�ؿ�״̬��ҳ��ˢ��ʱ���жϣ�
							            	// 5. sleep(1000);
				            				Thread.sleep(1000);
							            	// 6. continue;
				            				continue;
				            			}
				            		} 
				            	}
				            	// TODO ����
				            	//���ý��׽��
				            	String traderesult = curTradeBlock.finishTradeEndByMaxPrice();
				            	curTradeBlock.setTradeResult(traderesult);
				            	//��������ʱ
								sendTimeSwitch = 1;
								//�������
								sendCommand1Series(TradeConsts.ORDER13);
								if(tradeBlockList.size() == 1){
									//������ɣ������ɹ���Ϣ
									tradeFinish(curTradeBlock);
									//�������۽���
									stageThread.sleep(10000);
									sendCommand1Series(TradeConsts.ORDER15);
									//���ý�����ɵ���Ϣ
									 setTradeFinishInfo();
								}else{
									//�ؿ齻����ɣ�����������һ��صĽ���
									blockTradeFinish(curTradeBlock);
									//���ü������׵���Ϣ
									setBlockContinueInfo();
								}
	
				            }
						}else if(phaseSwitch ==2){//�ؿ齻����ɺ�ͣ20���ӣ���������һ��صĽ��У�״̬��Ϊ1��������׽�������ת���������浹��ʱ�׶Ρ�
							//���׼�Ъ�ڳ�ʼ��
							if(isFirst){
								blockIntervalStart(curTradeBlock);
							}
							//��������ʱ
							if(sendTimeSwitch == 1){
								sendCommand33(intervalTime,null,null);
								sendTimeSwitch = 0;
							}

							
							//����ڼ䵹��ʱ
							intervalTime = intervalTime-1;
							//��Ъ����ɣ���ʼ�ؿ齻��
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
	 * ���ý�����ɺ���Ϣ.
	 */
	private void setTradeFinishInfo(){
		phaseSwitch = -1;
		curTradeBlock = null;
		tradeBlockList.removeFirst();
		sendTimeSwitch = 0;
	}
	
	/**
	 * ���õؿ齻�׺�����Ϣ.
	 */
	private void setBlockContinueInfo(){
		//������ǰ���׵ؿ�
		tradeBlockList.removeFirst();
		curTradeBlock = (TradeBlock) tradeBlockList.getFirst();
		//���뽻�׼����
		phaseSwitch=2;
		intervalTime = TradeConsts.TRADE_INTERVAL_TIME;
		isFirst = true;
		isFirstBlock = false;
	}
	
	/**
	 * �������ף�Ĭ��ʵ��,�е����״�����.
	 */
	public void tradeStart(TradeBlock tradeBlock){
		if(curTradeBlock == null && tradeBlockList.size()>0){
			curTradeBlock = (TradeBlock) tradeBlockList.getFirst();
			isFirstBlock = true;
			sendCommand1Series(TradeConsts.ORDER17);
		}
	}
	/**
	 * ���׼����ʼ��Ĭ��ʵ��,�е����״�����.
	 */
	public void blockIntervalStart(TradeBlock tradeBlock){
		if(!isFirstBlock && intervalTime==10){
			sendCommand1Series(TradeConsts.ORDER17);
			// ���͵���ʱ
			//sendTimeSwitch = 1;
			isFirst = false;
		}
	}

	/**
	 * ����ϵͳ1ϵ�������Ҫ��12��13��15��17
	 * @param commandNo �������
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
				// ���������Ч���ۣ���ô���ͽ���ʧ��
				paramMap.put("tradeStatus", "0");
			}
			if(tradeBlockList.size() > 1){
				paramMap.put("nextBlockNo", ((TradeBlock)tradeBlockList.get(1)).getBlockNoticeNo());
			}
		}else if(TradeConsts.ORDER15.equals(commandNo) || TradeConsts.ORDER17.equals(commandNo)){
			//���ù������
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
	 * ����33����
	 * @param limitTime ����ʱ����
	 * @param appId �ؿ�����������Ϊ��ʱΪ���ۼ����
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
	 * ����34����
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
	 * ����38����
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
	 * ����40��������²���ҡ�ŵ���Ϣ��
	 * @param curTradeBlock
	 */
	private void sendCommand40(TradeBlock curTradeBlock){
		Map paramMap = new HashMap(); 
		paramMap.put("filterNoticeId", curTradeBlock.getNoticeId());
		paramMap.put("appId", curTradeBlock.getAppId());
		paramMap.put("conNums", curTradeBlock.getStrPartakeConNum());
		try {
		//Ⱥ����Ϣ����ҡ��ѡ����Ϣ
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
