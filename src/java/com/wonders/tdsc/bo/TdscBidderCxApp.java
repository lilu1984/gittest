package com.wonders.tdsc.bo;

import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;


/**
 * YsqsBidderCxApp entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TdscBidderCxApp extends BaseBO {

	// Fields

	private String cxInfoId;
	private String ifViolate;
	private String memo;
	private String aucitonUserId;
	private Date auctionDate;
	private String violateRuleType;
	private String violateBlockName;
	private String corpId;
	private Date violateDate;
	
	private String violateReason;
	// Constructors

	/** default constructor */
	public TdscBidderCxApp() {
	}

	/** full constructor */
	public TdscBidderCxApp(String ifViolate,
			String memo, String aucitonUserId, Date auctionDate,
			String violateRuleType, String violateBlockName) {
		this.ifViolate = ifViolate;
		this.memo = memo;
		this.aucitonUserId = aucitonUserId;
		this.auctionDate = auctionDate;
		this.violateRuleType = violateRuleType;
		this.violateBlockName = violateBlockName;
	}

	// Property accessors

	public String getCxInfoId() {
		return this.cxInfoId;
	}

	public void setCxInfoId(String cxInfoId) {
		this.cxInfoId = cxInfoId;
	}
	
	public String getIfViolate() {
		return this.ifViolate;
	}

	public void setIfViolate(String ifViolate) {
		this.ifViolate = ifViolate;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getAucitonUserId() {
		return this.aucitonUserId;
	}

	public void setAucitonUserId(String aucitonUserId) {
		this.aucitonUserId = aucitonUserId;
	}

	public Date getAuctionDate() {
		return this.auctionDate;
	}

	public void setAuctionDate(Date auctionDate) {
		this.auctionDate = auctionDate;
	}

	public String getViolateRuleType() {
		return this.violateRuleType;
	}

	public void setViolateRuleType(String violateRuleType) {
		this.violateRuleType = violateRuleType;
	}

	public String getViolateBlockName() {
		return this.violateBlockName;
	}

	public void setViolateBlockName(String violateBlockName) {
		this.violateBlockName = violateBlockName;
	}
	
	public String getId() {
		return cxInfoId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

    public Date getViolateDate() {
        return violateDate;
    }

    public void setViolateDate(Date violateDate) {
        this.violateDate = violateDate;
    }

	public String getViolateReason() {
		return violateReason;
	}

	public void setViolateReason(String violateReason) {
		this.violateReason = violateReason;
	}

    

}