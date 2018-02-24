package com.wonders.engine.auction;

import java.util.Map;

import com.wonders.engine.bo.TradeBlock;
/**
 * 场景的接口
 * @author sunxin
 *
 */
public interface AuctionSupport {

	/**
	 * 发送场景时间倒计时.
	 *
	 */
	public abstract void sendListingTime(Map paramMap);
	/**
	 * 获取场景的编号.
	 * @return
	 */
	public abstract String getAuctionNo();
	
	/**
	 * 判断地块是否属于此场景.
	 * @param appId 地块交易主键
	 * @return
	 */
	public abstract boolean existBlock(String appId);
	
	/**
	 * 交易开始.
	 */
	public abstract void tradeStart(TradeBlock tradeBlock);
	/**
	 * 交易结束.
	 *
	 */
	
	public abstract void tradeFinish(TradeBlock tradeBlock);
	/**
	 * 地块交易开始.
	 * @param tradeBlock 交易开始地块
	 */
	
	public abstract void blockTradeStart(TradeBlock tradeBlock);
	
	/**
	 * 地块交易结束.
	 * @param tradeBlock 交易完成地块
	 */
	public abstract void blockTradeFinish(TradeBlock tradeBlock);
	
	/**
	 * 地块交易间隔开始.
	 * @param tradeBlock 准备开始地块
	 */
	public abstract void blockIntervalStart(TradeBlock tradeBlock);
	
	/**
	 * 地块交易间隔结束.
	 * @param tradeBlock 准备开始的地块
	 */
	public abstract void blockIntervalFinish(TradeBlock tradeBlock);
	
	/**
	 * 是否含有底价
	 * @param tradeBlock
	 * @return
	 */
	public abstract boolean containsBasePrice(TradeBlock tradeBlock);
	
	/**
	 * 从外部设置场景倒计时时间
	 *
	 */
	public abstract void setLimitTime(String appId, int times);
	
	/**
	 * 发送报价信息 
	 * @param appId 地块编号
	 * @param clientNo
	 * @param conNo
	 * @param yktBh
	 */
	public void basePriceSubmit(String appId, String clientNo, String conNo, String yktBh);
	
	/**
	 * 根据noticeId取得当前的交易地块
	 * @param noticeId
	 */
	public TradeBlock getTradeBlock(String noticeId);
}
