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
 * 底价确认场景。
 * 
 * 多个公告对应多个场景
 * 
 * @author Gordon
 *
 */
public class WxConfirmScene extends Task implements Scene{
	
	private Logger logger = Logger.getLogger(WxConfirmScene.class);
	
	private static final int INIT_WAITING_TIMES = 20;
	
	private static final int INIT_TRADE_TIMES = 5 * 60;
	
	/**
	 * 公告需要处理的地块信息
	 */
	private LinkedList confirmBlocks;
	
	/**
	 * 竞价的地块列表
	 */
	private LinkedList tradeBlocks;
	
	/**
	 * 当前交易地块
	 */
	private TradeBlock currBlock;
	
	/**
	 * 公告编号
	 */
	//private String noticeId;
	private TradeNotice tradeNotice;
	
	/**
	 * 等待计时时间
	 */
	private int waitTimers = 0;
	
	/**
	 * 计时时间
	 */
	private int times = 0; // 5分钟
	
	/**
	 * 挂牌场景
	 */
	private AbstractListing listingSupport;
	
	public static void buildDijiaConfirmScene(LinkedList blockLinkedList, TradeNotice tradeNotice, AbstractListing listingSupport) {
		new WxConfirmScene(blockLinkedList, tradeNotice, listingSupport);
	}
	
	private WxConfirmScene(){}
	/**
	 * 构造方法
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
				// 如果没有进入现场的地块，交易直接完成
				listingSupport.triggerFinishWsjy(tradeNotice);
				//发布交易结束命令
				listingSupport.sendCommand15(tradeNotice.getNoticeId());
			} else {
				listingSupport.triggerStartAuction(tradeNotice, tradeBlocks);
			}
		}
	}
	
	/**
	 * 取得需要确认底价地块列表
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
	 * 倒计时处理
	 */
	public void countDown() {
		if (waitTimers != 0) {
			--waitTimers;
			if (waitTimers % 10 == 0)
				// 发送倒计时命令
				sendCurrentTimes();
			if (waitTimers == 0)
				times  = INIT_TRADE_TIMES;
				//System.out.println("当前地块" + currBlock + "等待运行中..." + waitTimers + "");
		} else {
			if (times != 0) {
				if (times == INIT_TRADE_TIMES) {
					System.out.println("开始进行 " + currBlock +" 地块的倒计时");
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
				// 发送倒计时命令
				if (times % 30 == 0) {
					//System.out.println("发送倒计时命令");
					//System.out.println("当前地块 " + currBlock + " 倒计时运行中..." + times + "");
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
		// 发送确认底价开始命令
		if (currBlock != null) {
			CoreService.getDbSupport().overBlock(currBlock, confirmBlocks.isEmpty() && tradeBlocks.isEmpty());
			System.out.println("结算当前地块" + currBlock);
			// 发送交易结果命令
			sendCurrentBlockResult();
		}
		waitTimers = INIT_WAITING_TIMES;
		sendCurrentTimes();
		if (!confirmBlocks.isEmpty()) {
			currBlock = (TradeBlock)confirmBlocks.removeFirst();
		} else {
			System.out.println("倒计时进行完毕");
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.cancel();
			// 启动竞价
			if (tradeBlocks == null || tradeBlocks.size() == 0) {
				// 如果没有进入现场的地块，交易直接完成
				listingSupport.triggerFinishWsjy(tradeNotice);
				//发布交易结束命令
				listingSupport.sendCommand15(tradeNotice.getNoticeId());
			} else {
				listingSupport.triggerStartAuction(tradeNotice, tradeBlocks);
			}
		}
	}

	/**
	 * 场景编号
	 */
	public String getSceneId() {
		return tradeNotice.getNoticeId();
	}

	/**
	 * 结束当前，进行下一个
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
		// 默认等待状态
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
		// 是否进入限时竞价
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
