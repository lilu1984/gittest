package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CRGG implements Serializable {

	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 出让公告号	NOITCE_NO */
    private String noitceNo;
    
    /** 竞买保证金	MARGIN_AMOUNT */
    private BigDecimal marginAmount;
    
    /** 起叫价	INIT_PRICE */
    private BigDecimal initPrice;
    
    /** 公告发布时间	ISSUE_START_DATE */
    private Timestamp issueStartDate;
    
    /** 出让文件发售开始时间	GET_FILE_START_DATE */
    private Timestamp getFileStartDate;
    
    /** 出让文件发售结束时间	GET_FILE_END_DATE */
    private Timestamp getFileEndDate;
    
    /** 竞买申请受理开始时间	ACC_APP_STAT_DATE */
    private Timestamp accAppStatDate;

    /** 竞买申请受理结束时间	ACC_APP_END_DATE */
    private Timestamp accAppEndDate;
    
    /** 竞买保证金交纳截止时间	MARGIN_END_DATE */
    private Timestamp marginEndDate;
    
    /** 现场踏勘时间	INSP_DATE */
    private Timestamp inspDate;
    
    /** 现场踏勘地点	INSP_LOC */
    private String inspLoc;
    
    /** 拍卖会举办时间	AUCTION_DATE */
    private Timestamp auctionDate;
    
    /** 拍卖会举办地点	AUCTION_LOC */
    private String auctionLoc;
    
    /** 挂牌开始时间	LIST_START_DATE */
    private Timestamp listStartDate;
    
    /** 挂牌结束时间	LIST_END_DATE */
    private Timestamp listEndDate;
    
    /** 现场竞价时间	SCE_BID_DATE */
    private Timestamp sceBidDate;
    
    /** 现场竞价地点	SCE_BID_LOC */
    private String sceBidLoc;
    
    /** 承办机构	PURVEYOR */
    private String purveyor = "青岛市土地储备整理中心";
    
    /** 协办机构	BZ_XBJG */
    private String bzXbjg;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getProjectGuid() {
		return projectGuid;
	}

	public void setProjectGuid(String projectGuid) {
		this.projectGuid = projectGuid;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getNoitceNo() {
		return noitceNo;
	}

	public void setNoitceNo(String noitceNo) {
		this.noitceNo = noitceNo;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public Timestamp getIssueStartDate() {
		return issueStartDate;
	}

	public void setIssueStartDate(Timestamp issueStartDate) {
		this.issueStartDate = issueStartDate;
	}

	public Timestamp getGetFileStartDate() {
		return getFileStartDate;
	}

	public void setGetFileStartDate(Timestamp getFileStartDate) {
		this.getFileStartDate = getFileStartDate;
	}

	public Timestamp getGetFileEndDate() {
		return getFileEndDate;
	}

	public void setGetFileEndDate(Timestamp getFileEndDate) {
		this.getFileEndDate = getFileEndDate;
	}

	public Timestamp getAccAppStatDate() {
		return accAppStatDate;
	}

	public void setAccAppStatDate(Timestamp accAppStatDate) {
		this.accAppStatDate = accAppStatDate;
	}

	public Timestamp getAccAppEndDate() {
		return accAppEndDate;
	}

	public void setAccAppEndDate(Timestamp accAppEndDate) {
		this.accAppEndDate = accAppEndDate;
	}

	public Timestamp getMarginEndDate() {
		return marginEndDate;
	}

	public void setMarginEndDate(Timestamp marginEndDate) {
		this.marginEndDate = marginEndDate;
	}

	public Timestamp getInspDate() {
		return inspDate;
	}

	public void setInspDate(Timestamp inspDate) {
		this.inspDate = inspDate;
	}

	public String getInspLoc() {
		return inspLoc;
	}

	public void setInspLoc(String inspLoc) {
		this.inspLoc = inspLoc;
	}

	public Timestamp getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(Timestamp auctionDate) {
		this.auctionDate = auctionDate;
	}

	public String getAuctionLoc() {
		return auctionLoc;
	}

	public void setAuctionLoc(String auctionLoc) {
		this.auctionLoc = auctionLoc;
	}

	public Timestamp getListStartDate() {
		return listStartDate;
	}

	public void setListStartDate(Timestamp listStartDate) {
		this.listStartDate = listStartDate;
	}

	public Timestamp getListEndDate() {
		return listEndDate;
	}

	public void setListEndDate(Timestamp listEndDate) {
		this.listEndDate = listEndDate;
	}

	public Timestamp getSceBidDate() {
		return sceBidDate;
	}

	public void setSceBidDate(Timestamp sceBidDate) {
		this.sceBidDate = sceBidDate;
	}

	public String getSceBidLoc() {
		return sceBidLoc;
	}

	public void setSceBidLoc(String sceBidLoc) {
		this.sceBidLoc = sceBidLoc;
	}

	public String getPurveyor() {
		return purveyor;
	}

	public void setPurveyor(String purveyor) {
		this.purveyor = purveyor;
	}

	public String getBzXbjg() {
		return bzXbjg;
	}

	public void setBzXbjg(String bzXbjg) {
		this.bzXbjg = bzXbjg;
	}
}
