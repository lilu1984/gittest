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
	 * ���� ���Сʱ
	 */
	private static final int HALF_HOUR = 60 * 30; 
	/**
	 * �����߳�
	 */
	private Thread triggerThread = null;
	
	/**
	 * false���������У�true��ֹͣ��ֹͣ��������������
	 */
	private boolean runSwitch = false;
	/**
	 * ˢ�¼������������initTrigger();ˢ��.
	 */
	private long refreshTime = 120;
	/**
	 * ���������ڹ��ƹ����б�
	 */
	public List engineNoticeList  = new ArrayList();
	/**
	 * ����ʱ����ʱ���ʱ�俪�أ�0�������ͣ�1������
	 */
	private long sendTimeSwith = 0;
	
	/**
	 * ���͹���ʱ�䵹��ʱ
	 */
	public void sendListingTime(Map paramMap){
		sendCommand32(paramMap);
	}
	
	/**
	 * 
	 *����������.
	 */
	public void startListing() {
		triggerThread = new Thread(this, "AbstractListing Thread");
		triggerThread.start();
		//��ʼ��
		initListing();
	}
	/**
	 * ֹͣ������.
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
			//���״̬Ϊ-1�������˴�����
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
								  //��Ҫ�ȴ��߳�ִ�в��ܷ�������
								  triggerThread.sleep(500);
								  commandString = "{op:'21',clientNo:'"+ gmClinet +"'}";
								  serverPipe.sendCommand(commandString);
								  triggerThread.sleep(500);
								  commandString = "{op:'26',noticeId:'" + tradeNotice.getNoticeId() + "'}";
								  serverPipe.sendCommand(commandString);
								  CoreService.addGmServerPipe(noticeId, serverPipe);
								  
							  }else{
								  //ÿ��10�������������һ��
								  GmServerPipe serverPipe = (GmServerPipe)gmServerPool.get(noticeId);
								  commandString = "{op:'23',noticeId:'" + noticeId + "'}";
								  serverPipe.sendCommand(commandString);
								  commandString = "{op:'24',noticeId:'" + noticeId + "'}";
								  serverPipe.sendCommand(commandString);
							  }
							  
						  }
					  }
				  }
				//ˢ�µ���ʱ�����б�
				if(timesCount >= refreshTime){
					initListing();
					timesCount=0;
					//���͹���ʱ�俪������Ϊ��
					sendTimeSwith=1;
					//�����Ѿ����׽����Ĺ�Ħ�ͻ���
					Iterator iter = gmServerPool.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry)iter.next();
						if(!noticeMap.containsKey(entry.getKey())){
							GmServerPipe serverPipe = (GmServerPipe)entry.getValue();
							serverPipe.disconnect();
						}
					}
				}
				//�߳�����5��
				triggerThread.sleep(10000);
				//����ʱ�����
				timesCount = timesCount+10;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * ���͹���ʱ�䵹��ʱ.
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
	 * ���ͽ��׽�������.
	 * @param noticeId ��������
	 */
	public void sendCommand15(String noticeId){
		Map paramMap = new HashMap();
		paramMap.put("noticeId", noticeId);
		//���ù������
		paramMap.put("filterNoticeId", noticeId);
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER15, paramMap, null);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ֳ����׹��ƽ�������.
	 * @param noticeId ��������
	 */
	public void sendCommand18(String noticeId){
		Map paramMap = new HashMap();
		paramMap.put("noticeId", noticeId);
		//���ù������
		paramMap.put("filterNoticeId", noticeId);
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER18, paramMap, null);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ��Ҫ�������Ͼ��۵ĵؿ��б�
	 * @param teadeBlockList �����ڵĵؿ�
	 * @return
	 */
	private LinkedList getWsjyList(String noticeId){
		LinkedList resultList = new LinkedList();
		List tradeBlockList = CoreService.findTradeBlockListByNoticeId(noticeId);
		List basePriceConfirmList = new ArrayList();
		for(int i=0;i<tradeBlockList.size();i++){
			TradeBlock tradeBlock = (TradeBlock) tradeBlockList.get(i);
			//�ж��Ƿ�������Ͻ���
			if(isEnterAuction(tradeBlock)){
				resultList.add(tradeBlock);
			} else {
				// TODO: ���� BY �߶���������ǽ������Ͻ��׵ģ����п��ܴ�����Ҫȷ�ϵ׼۵ؿ�
				// 1��ȡ�õ�ǰ�ؿ�ĵ׼���Ϣ

				BigDecimal basePrice = CoreService.getDbSupport().getBlockBasePrice(tradeBlock);
				// 2���ж��Ƿ���ڵ׼�
				if (basePrice.intValue() != 0) {
					if(!"0".equals(tradeBlock.getTradeResult())&&!"".equals(tradeBlock.getTradeResult())){
						// ���ڵ׼�
						// 2���жϵ�ǰ���Ƽ۸�
						tradeBlock.setBasePrice(basePrice);
						if (basePrice.doubleValue() > tradeBlock.getTopPrice()) {
							// 3���жϵ�ǰ���Ƽ۸��Ƿ���ڵ׼ۣ�
							// 4�����С�ڵ׼ۣ����ӵ׼�ȷ�ϱ�ʶ�����Ҽ���ȷ�ϵ׼��б�
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
	 * 1����������Ͻ��׵ؿ��ڹ��ƽ�ֹ����֮ǰһ��Сʱ�����Сʱ������Ϣ
	 * 2��������ֳ����۵ؿ��ڹ��ƽ�ֹ����֮ǰ����Сʱ��һ��Сʱ������Ϣ
	 * 
	 * @param notice
	 */
	private void sendMsgCountDown(TradeNotice notice) {
		if (notice == null) return;
		if (notice.isWsjy()) {
			// ��������Ͻ��׵Ļ� 1��Сʱ�Ͱ��Сʱ
			if (notice.getSurplusTime() == 2 * HALF_HOUR || notice.getSurplusTime() == HALF_HOUR) {
				WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
				List list = wxDbService.findBidderAppList(notice.getNoticeId());
				//Map parmMap = wxDbService.getSmsInfoByAppId(null, tradePrice.getAppId());
				// String message = "�𾴵ľ����ˣ�����!���깺��X�ؿ��������Σ�����X��X��X��XʱX�ֽ�����ʱ���ۣ��ֳ����ۣ���������ǰ���þ���׼����";
				//StringBuffer mobiles = new StringBuffer();
				System.out.println("���ƽ���ǰһ��Сʱ�Ͱ��Сʱ���Ͷ���");
				repeatSendSms(notice,list,wxDbService);
			}
		} else {
			// ������ֳ����׵Ļ� 2��Сʱ��1��Сʱ
			if (notice.getSurplusTime() == 4 * HALF_HOUR || notice.getSurplusTime() == 2*HALF_HOUR) {
				WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
				List list = wxDbService.findBidderAppList(notice.getNoticeId());
				System.out.println("���ƽ���ǰ2��Сʱ��1��Сʱ���Ͷ���");
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
						message = "�𾴵ľ����ˣ�����!���깺��" +blockNames.toString()+ "�ؿ��������Σ�����" + DateUtil.date2String(notice.getListEndDate(), "yyyy��MM��dd�� HHʱmm��") + "������ʱ���ۣ�������ǰ���þ���׼����";
					} else {
						message = "�𾴵ľ����ˣ�����!���깺��" +blockNames.toString()+ "�ؿ��������Σ�����" + DateUtil.date2String(notice.getListEndDate(), "yyyy��MM��dd�� HHʱmm��") + "�����ֳ����ۣ�������ǰ���þ���׼����";
					}
				}
				CoreService.getSmsSupport().sendSms(bidderApp.getPushSmsPhone(), message);
			}
		}
	}

}
