package com.wonders.engine.socket.event;

/**
 * ��Ӿ��۷����¼�.
 * @author sunxin
 *
 */
public class PriceAddEvent implements ChangeEvent {
	/**
	 * ���۶���
	 */
	private long priceAdd = 0;
	/**
	 * ��������
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
