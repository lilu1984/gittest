package com.wonders.engine.socket.event;
/**
 * 触发场景变化的事件的缺省实现.
 * @author sunxin
 *
 */
public class DefaultEvent implements ChangeEvent {
	/**
	 * 申请地块主键
	 */
	private String appId = null;
	/**
	 * 事件类型
	 */
	private String eventType = null;

	public String getAppId() {
		return appId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

}
