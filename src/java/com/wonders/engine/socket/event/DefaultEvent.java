package com.wonders.engine.socket.event;
/**
 * ���������仯���¼���ȱʡʵ��.
 * @author sunxin
 *
 */
public class DefaultEvent implements ChangeEvent {
	/**
	 * ����ؿ�����
	 */
	private String appId = null;
	/**
	 * �¼�����
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
