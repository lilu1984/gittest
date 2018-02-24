package com.wonders.engine.socket.event;

import com.wonders.engine.bo.TradePrice;
/**
 * 价格提交事件。
 * @author sunxin
 *
 */
public class PriceSubmitEvent implements ChangeEvent {
	/**
	 * 报价对象
	 */
	private TradePrice tradePrice = null;
	/**
	 * 对象主键
	 */
	private String appId = null;
	
	
	public PriceSubmitEvent(TradePrice tradePrice) {
		this.tradePrice = tradePrice;
		this.appId = tradePrice.getAppId();
	}

	public String getAppId() {
		return appId;
	}

	public String getEventType() {
		return ChangeEvent.PRICE_SUBMIT_EVENT;
	}

	public TradePrice getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(TradePrice tradePrice) {
		this.tradePrice = tradePrice;
	}

}
