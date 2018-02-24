package com.wonders.tdsc.kyq.bo;

import java.util.List;

import com.wonders.esframework.common.bo.BaseCondition;

public class KyqBaseQueryCondition extends BaseCondition {

	private String miningName;

	private String transferMode;

	private String noticeNumber;

	private String noticeId;

	private String bidderName;

	private String tranAppId;

	private List tranAppIds;

	private String tranStatus;

	private List tranStatusList;
	
	private String orderKey;
	
	private String ifResultPublish;
	
	public String getIfResultPublish() {
		return ifResultPublish;
	}

	public void setIfResultPublish(String ifResultPublish) {
		this.ifResultPublish = ifResultPublish;
	}

	public String getTranAppId() {
		return tranAppId;
	}

	public void setTranAppId(String tranAppId) {
		this.tranAppId = tranAppId;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeNumber() {
		return noticeNumber;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getMiningName() {
		return miningName;
	}

	public void setMiningName(String miningName) {
		this.miningName = miningName;
	}

	public String getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}

	public List getTranAppIds() {
		return tranAppIds;
	}

	public void setTranAppIds(List tranAppIds) {
		this.tranAppIds = tranAppIds;
	}

	public List getTranStatusList() {
		return tranStatusList;
	}

	public void setTranStatusList(List tranStatusList) {
		this.tranStatusList = tranStatusList;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

}
