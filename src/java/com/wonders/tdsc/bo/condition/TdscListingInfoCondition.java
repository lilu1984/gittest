package com.wonders.tdsc.bo.condition;

import java.util.List;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscListingInfoCondition extends BaseBO {

	/** nullable persistent field */
	private String listingId;

	private String appId;

	/** nullable persistent field */
	private List appIdList;

	private int currentPage;

	public List getAppIdList() {
		return appIdList;
	}

	public void setAppIdList(List appIdList) {
		this.appIdList = appIdList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
