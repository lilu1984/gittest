package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class TdscReturnBail implements Serializable {

	private String		bailId;
	private String		planId;
	private String		appId;
	private String		blockId;
	private String		bidderId;
	private BigDecimal	bidderBail;

	private String		actionUser;
	private Timestamp	actionDate;

	private String		bzjBank;
	private String		ifReturn;

	public String getBzjBank() {
		return bzjBank;
	}

	public void setBzjBank(String bzjBank) {
		this.bzjBank = bzjBank;
	}

	public String getIfReturn() {
		return ifReturn;
	}

	public void setIfReturn(String ifReturn) {
		this.ifReturn = ifReturn;
	}

	public String getBailId() {
		return this.bailId;
	}

	public void setBailId(String bailId) {
		this.bailId = bailId;
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getBidderId() {
		return this.bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getActionUser() {
		return this.actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public BigDecimal getBidderBail() {
		return bidderBail;
	}

	public void setBidderBail(BigDecimal bidderBail) {
		this.bidderBail = bidderBail;
	}

}