package com.wonders.tdsc.retbail.web.form;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

public class TdscReturnBailForm extends ActionForm {

	private static final long	serialVersionUID	= 1L;

	private String				noticeNo;
	private String				blockNoticeNo;
	private String				blockName;
	private String[]			bidderIds;
	private String[]			planIds;
	private String[]			appIds;
	private String[]			blockIds;
	private String[]			bidderBails;
	private String[]			bailIds;

	private String				canReturn;					// 是否能退还保证金,如果是竞得人不能退
	private String				retStatus;					// 是否已经退还保证金;

	private String				bidderName;

	private String				bailId;
	private String				planId;
	private String				appId;
	private String				blockId;
	private String				bidderId;
	private BigDecimal			bidderBail;
	private String				actionUser;
	private Timestamp			actionDate;

	public String[] getBailIds() {
		return bailIds;
	}

	public void setBailIds(String[] bailIds) {
		this.bailIds = bailIds;
	}

	public String[] getBidderBails() {
		return bidderBails;
	}

	public void setBidderBails(String[] bidderBails) {
		this.bidderBails = bidderBails;
	}

	public String[] getPlanIds() {
		return planIds;
	}

	public void setPlanIds(String[] planIds) {
		this.planIds = planIds;
	}

	public String[] getAppIds() {
		return appIds;
	}

	public void setAppIds(String[] appIds) {
		this.appIds = appIds;
	}

	public String[] getBlockIds() {
		return blockIds;
	}

	public void setBlockIds(String[] blockIds) {
		this.blockIds = blockIds;
	}

	public String[] getBidderIds() {
		return bidderIds;
	}

	public void setBidderIds(String[] bidderIds) {
		this.bidderIds = bidderIds;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getRetStatus() {
		return retStatus;
	}

	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus;
	}

	public String getCanReturn() {
		return canReturn;
	}

	public void setCanReturn(String canReturn) {
		this.canReturn = canReturn;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getBailId() {
		return bailId;
	}

	public void setBailId(String bailId) {
		this.bailId = bailId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public BigDecimal getBidderBail() {
		return bidderBail;
	}

	public void setBidderBail(BigDecimal bidderBail) {
		this.bidderBail = bidderBail;
	}

	public String getActionUser() {
		return actionUser;
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

}
