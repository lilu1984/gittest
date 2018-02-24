package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CRGG implements Serializable {

	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** ���ù����	NOITCE_NO */
    private String noitceNo;
    
    /** ����֤��	MARGIN_AMOUNT */
    private BigDecimal marginAmount;
    
    /** ��м�	INIT_PRICE */
    private BigDecimal initPrice;
    
    /** ���淢��ʱ��	ISSUE_START_DATE */
    private Timestamp issueStartDate;
    
    /** �����ļ����ۿ�ʼʱ��	GET_FILE_START_DATE */
    private Timestamp getFileStartDate;
    
    /** �����ļ����۽���ʱ��	GET_FILE_END_DATE */
    private Timestamp getFileEndDate;
    
    /** ������������ʼʱ��	ACC_APP_STAT_DATE */
    private Timestamp accAppStatDate;

    /** ���������������ʱ��	ACC_APP_END_DATE */
    private Timestamp accAppEndDate;
    
    /** ����֤���ɽ�ֹʱ��	MARGIN_END_DATE */
    private Timestamp marginEndDate;
    
    /** �ֳ�̤��ʱ��	INSP_DATE */
    private Timestamp inspDate;
    
    /** �ֳ�̤���ص�	INSP_LOC */
    private String inspLoc;
    
    /** ������ٰ�ʱ��	AUCTION_DATE */
    private Timestamp auctionDate;
    
    /** ������ٰ�ص�	AUCTION_LOC */
    private String auctionLoc;
    
    /** ���ƿ�ʼʱ��	LIST_START_DATE */
    private Timestamp listStartDate;
    
    /** ���ƽ���ʱ��	LIST_END_DATE */
    private Timestamp listEndDate;
    
    /** �ֳ�����ʱ��	SCE_BID_DATE */
    private Timestamp sceBidDate;
    
    /** �ֳ����۵ص�	SCE_BID_LOC */
    private String sceBidLoc;
    
    /** �а����	PURVEYOR */
    private String purveyor = "�ൺ�����ش�����������";
    
    /** Э�����	BZ_XBJG */
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
