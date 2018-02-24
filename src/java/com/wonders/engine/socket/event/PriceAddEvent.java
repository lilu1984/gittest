package com.wonders.engine.socket.event;

/**
 * 添加竞价幅度事件.
 * @author sunxin
 *
 */
public class PriceAddEvent implements ChangeEvent {
	/**
	 * 报价对象
	 */
	private long priceAdd = 0;
	/**
	 * 对象主键
	 */
	private String appId = null;
	
	public PriceAddEvent(){
		 
	}
	
    public PriceAddEvent(String appId,long priceAdd){
    	this.priceAdd = priceAdd;
    	this.appId = appId;
    }
	
	public String getAppId() {
		return appId;
	}

	public String getEventType() {
		return ChangeEvent.PRICE_ADD_EVENT;
	}

	public long getPriceAdd() {
		return priceAdd;
	}

	public void setPriceAdd(long priceAdd) {
		this.priceAdd = priceAdd;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	

}
