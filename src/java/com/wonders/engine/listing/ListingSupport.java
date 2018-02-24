package com.wonders.engine.listing;

import java.util.LinkedList;
import java.util.Map;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradeNotice;

/**
 * 触发器接口.
 * @author sunxin
 *
 */
public interface ListingSupport {
	/**
	 * 发送触发器时间倒计时.
	 *
	 */
	public abstract void sendListingTime(Map paramMap);
	/**
	 * 启动触发器.
	 *
	 */
	public abstract void startListing();
	
	/**
	 * 主要处理网上交易挂牌结束后，没有进入现场的地块，直接完成此次交易.
	 *
	 */
	public abstract void initListing();
	
	/**
	 * 主要处理网上交易挂牌结束后，没有进入现场的地块，直接完成此次交易.
	 * @param tradeNotice 结束挂牌的公告
	 */
	public abstract void triggerFinishWsjy(TradeNotice tradeNotice);
	
	/**
	 * 挂牌终结
	 * @param tradeNotice
	 */
	public abstract void triggerFinishGp(TradeNotice tradeNotice);
	
	/**
	 * 主要处理网上交易挂牌结束后，有进入现场的地块，需启动网上竞价.
	 * @param tradeNotice 结束挂牌的公告
	 * @param wsjyList 进入竞价的地块列表
	 */
	public abstract void triggerStartAuction(TradeNotice tradeNotice,LinkedList wsjyList);
	
	/**
	 * 主要对现场交易挂牌结束后的处理.
	 * @param tradeNotice 结束挂牌的公告
	 */
	public abstract void triggerFinishListing(TradeNotice tradeNotice);

	
	/**
	 * 到转折点的操作，挂牌时间结束.
	 * @param tradeNotice 结束挂牌的公告
	 */
	public abstract void triggerListing(TradeNotice tradeNotice);
	
	/**
	 * 地块是否能进入网上竞价，即进入竞价环节的规则.
	 * @param tradeBlock 地块对象
	 * @return
	 */
	public abstract boolean isEnterAuction(TradeBlock tradeBlock);
}
