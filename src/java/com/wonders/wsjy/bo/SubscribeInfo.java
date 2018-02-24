package com.wonders.wsjy.bo;

import java.sql.Timestamp;

public class SubscribeInfo {

	private String subscribeId;
	
	private String userId;
	
	private String appId;
	
	private Timestamp createTime;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getSubscribeId() {
		return subscribeId;
	}

	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
