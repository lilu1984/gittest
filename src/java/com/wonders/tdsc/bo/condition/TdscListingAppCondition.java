package com.wonders.tdsc.bo.condition;

import java.util.List;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscListingAppCondition extends BaseCondition {

	/** identifier field */
	private String listingAppId;

	private String appId;

	private List appIdList;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getListingAppId() {
		return listingAppId;
	}

	public void setListingAppId(String listingAppId) {
		this.listingAppId = listingAppId;
	}

	public List getAppIdList() {
		return appIdList;
	}

	public void setAppIdList(List appIdList) {
		this.appIdList = appIdList;
	}

}