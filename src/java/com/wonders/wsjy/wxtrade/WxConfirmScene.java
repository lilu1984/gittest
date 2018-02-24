package com.wonders.wsjy.wxtrade;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradeNotice;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.floorprice.Scene;
import com.wonders.engine.listing.AbstractListing;
import com.wonders.engine.timer.Task;
import com.wonders.wsjy.TradeConsts;
import com.wonders.wsjy.dijia.EngineProcessManager;

/**
 * �׼�ȷ�ϳ�����
 * 
 * ��������Ӧ�������
 * 
 * @author Gordon
 *
 */
public class WxConfirmScene extends Task implements Scene{
	
	private Logger logger = Logger.getLogger(WxConfirmScene.class);
	
	private static final int INIT_WAITING_TIMES = 20;
	
	private static final int INIT_TRADE_TIMES = 5 * 60;
	
	/**
	 * ������Ҫ����ĵؿ���Ϣ
	 */
	private LinkedList confirmBlocks;
	
	/**
	 * ���۵ĵؿ��б�
	 */
	private LinkedList tradeBlocks;
	
	/**
	 * ��ǰ���׵ؿ�
	 */
	private TradeBlock currBlock;
	
	/**
	 * ������
	 */
	//private String noticeId;
	private TradeNotice tradeNotice;
	
	/**
	 * �ȴ���ʱʱ��
	 */
	private int waitTimers = 0;
	
	/**
	 * ��ʱʱ��
	 */
	private int times = 0; // 5����
	
	/**
	 * ���Ƴ���
	 */
	private AbstractListing listingSupport;
	
	public static void buildDijiaConfirmScene(LinkedList blockLinkedList, TradeNotice tradeNotice, AbstractListing listingSupport) {
		new WxConfirmScene(blockLinkedList, tradeNotice, listingSupport);
	}
	
	private WxConfirmScene(){}
	/**
	 * ���췽��
	 * @param blockLinkedList
	 * @param noticeId
	 */
	private WxConfirmScene(LinkedList blocks, TradeNotice tradeNotice, AbstractListing listingSupport) {
		super();
		//this.confirmBlocks = blockLinkedList;
		this.confirmBlocks = confirmBlocks(blocks);
		this.tradeBlocks = blocks;
		this.tradeNotice = tradeNotice;
		this.listingSupport = listingSupport;
		this.listingSupport.triggerFinishGp(tradeNotice);
		if (confirmBlocks != null && confirmBlocks.size() > 0) {
			CoreService.addConfirmScene(this);
			EngineProcessManager.getInstance().addTimerListener(this, 1000);
		} else {
			if (tradeBlocks == null || tradeBlocks.size() == 0) {
				// ���û�н����ֳ��ĵؿ飬����ֱ�����
				listingSupport.triggerFinishWsjy(tradeNotice);
				//�������׽�������
				listingSupport.sendCommand15(tradeNotice.getNoticeId());
			} else {
				listingSupport.triggerStartAuction(tradeNotice, tradeBlocks);
			}
		}
	}
	
	/**
	 * ȡ����Ҫȷ�ϵ׼۵ؿ��б�
	 * 
	 * @param blocks
	 * @return
	 */
	private LinkedList confirmBlocks(LinkedList blocks) {
		LinkedList list = null;
		if (blocks != null) {
			list = new LinkedList();
			for (int i = 0; i < blocks.size(); i++) {
				TradeBlock tradeBlock = (TradeBlock)blocks.get(i);
				if (tradeBlock.isConfirmFloorPrice()) {
					CoreService.getDbSupport().confirmWaitBlock(tradeBlock);
					list.add(tradeBlock);
				}
			}
			blocks.removeAll(list);
		}
		return list;
	}
	
	
	/**
	 * ����ʱ����
	 */
	public void countDown() {
		if (waitTimers != 0) {
			--waitTimers;
			if (waitTimers % 10 == 0)
				// ���͵���ʱ����
				sendCurrentTimes();
			if (waitTimers == 0)
				times  = INIT_TRADE_TIMES;
				//System.out.println("��ǰ�ؿ�" + currBlock + "�ȴ�������..." + waitTimers + "");
		} else {
			if (times != 0) {
				if (times == INIT_TRADE_TIMES) {
					System.out.println("��ʼ���� " + currBlock +" �ؿ�ĵ���ʱ");
					CoreService.getDbSupport().confirmBlock(currBlock);
					Map paramMap = new HashMap();
					paramMap.put("tradeBlock", currBlock);
					paramMap.put("filterNoticeId", tradeNotice.getNoticeId());
					paramMap.put("times", times + "");
					try {
						CoreService.broadcastCommand(TradeConsts.ORDER30, paramMap,null);
					} catch (EngineException e) {
						logger.error(e.getMessage());
					}
				}
				--times;
				// ���͵���ʱ����
				if (times % 30 == 0) {
					//System.out.println("���͵���ʱ����");
					//System.out.println("��ǰ�ؿ� " + currBlock + " ����ʱ������..." + times + "");
					sendCurrentTimes();
				}
			} else {
				elementBlock();
			}
		}
	}
	
	public void elementBlock() {
		if (currBlock == null) {
			Map paramMap = new HashMap();
			paramMap.put("tradeNotice", tradeNotice);
			paramMap.put("filterNoticeId", tradeNotice.getNoticeId());
			try {
				CoreService.broadcastCommand(TradeConsts.ORDER28, paramMap,null);
			} catch (EngineException e) {
				logger.error(e.getMessage());
			}
		}
		// ����ȷ�ϵ׼ۿ�ʼ����
		if (currBlock != null) {
			CoreService.getDbSupport().overBlock(currBlock, confirmBlocks.isEmpty() && tradeBlocks.isEmpty());
			System.out.println("���㵱ǰ�ؿ�" + currBlock);
			// ���ͽ��׽������
			sendCurrentBlockResult();
		}
		waitTimers = INIT_WAITING_TIMES;
		sendCurrentTimes();
		if (!confirmBlocks.isEmpty()) {
			currBlock = (TradeBlock)confirmBlocks.removeFirst();
		} else {
			System.out.println("����ʱ�������");
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.cancel();
			// ��������
			if (tradeBlocks == null || tradeBlocks.size() == 0) {
				// ���û�н����ֳ��ĵؿ飬����ֱ�����
				listingSupport.triggerFinishWsjy(tradeNotice);
				//�������׽�������
				listingSupport.sendCommand15(tradeNotice.getNoticeId());
			} else {
				listingSupport.triggerStartAuction(tradeNotice, tradeBlocks);
			}
		}
	}

	/**
	 * �������
	 */
	public String getSceneId() {
		return tradeNotice.getNoticeId();
	}

	/**
	 * ������ǰ��������һ��
	 */
	public void next() {
		synchronized (this) {
			times = 0;
		}
	}

	public void run() {
		this.countDown();
	}

	public String getTaskId() {
		return tradeNotice.getNoticeId();
	}

	public void sendCurrentTimes() {
		int returnTimes = 0;
		// Ĭ�ϵȴ�״̬
		String phase = "0";
		if (waitTimers != 0) {
			returnTimes = waitTimers;
		} else {
			phase = "1";
			returnTimes = times;
		}
		Map paramMap = new HashMap();
		paramMap.put("returnTimes", returnTimes + "");
		paramMap.put("phase", phase);
		paramMap.put("filterNoticeId", tradeNotice.getNoticeId());
		if (currBlock != null) {
			paramMap.put("appId", currBlock.getAppId());
			paramMap.put("blockNoticeNo", currBlock.getBlockNoticeNo());
		}
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER29, paramMap,null);
		} catch (EngineException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void sendCurrentBlockResult() {
		Map paramMap = new HashMap();
		paramMap.put("tradeBlock", currBlock);
		paramMap.put("filterNoticeId", currBlock.getNoticeId());
		paramMap.put("tradeStatus", currBlock.getTradeResult());
		paramMap.put("resultPrice", currBlock.getBasePrice());
		if (!confirmBlocks.isEmpty()) {
			TradeBlock nextBlock = (TradeBlock)confirmBlocks.getFirst();
			paramMap.put("nextBlockNo", nextBlock.getBlockNoticeNo());
		}
		// �Ƿ������ʱ����
		if (tradeBlocks == null || tradeBlocks.isEmpty()) {
			paramMap.put("auction", "false");
		} else {
			paramMap.put("auction", "true");
		}
		if (currBlock != null)
			paramMap.put("appId", currBlock.getAppId());
		try {
			CoreService.broadcastCommand(TradeConsts.ORDER36, paramMap,null);
		} catch (EngineException e) {
			logger.error(e.getMessage());
		}
	}

	public void basePriceSubmit(String appId, String clientNo, String conNo, String yktBh) {
		if (currBlock.getAppId().equals(appId)) {
			TradePrice tradePrice = new TradePrice();
			tradePrice.setAppId(appId);
			tradePrice.setPrice(currBlock.getBasePrice().doubleValue());
			tradePrice.setPhase("1");
			tradePrice.setClientNo(clientNo);
			tradePrice.setConNum(conNo);
			tradePrice.setYktBh(yktBh);
			if (CoreService.pushPrice(tradePrice)) {
				this.next();
			}
		}
	}

}
