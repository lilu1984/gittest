package com.wonders.tdsc.stat.web.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;

public class TdscTradeStatisticsForm extends ActionForm {

	private static final long	serialVersionUID	= 2521175954112562099L;

	private String	reportDate;

	// 宗地基本情况
	private String	blockName;
	private String	bidderName;
	private String	blockLocation;
	private String	blockUsed;
	private String	blockArea;
	// 规划建设条件
	private String	buildArea;
	private String	buildUsed;
	private String	greenRate;		// 绿化率
	private String	volumeRate;	// 容积率
	private String	densityRate;	// 建筑密度

	// 交易情况
	private String	noticeNo;
	private String	noticeDate;
	private String	auctionCount;
	private String	tranTime;
	private String	initPrice;
	private String	iBlockPrice;	// 初始价 / 地块面积
	private String	iBuildPrice;	// 初始价 / 规划建筑面积
	private String	tranPrice;
	private String	tBlockPrice;
	private String	tBuildPrice;

	private String	tranMode;

	private String countUse;
    
    private String blockQuality;
    
    private Date resultDate;
    
    private Long districtId;
    
    private Date resultDate1;
    
    
    public Date getResultDate1() {
		return resultDate1;
	}

	public void setResultDate1(Date resultDate1) {
		this.resultDate1 = resultDate1;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getBlockQuality() {
		return blockQuality;
	}

	public void setBlockQuality(String blockQuality) {
		this.blockQuality = blockQuality;
	}

	public String getCountUse() {
		return countUse;
	}

	public void setCountUse(String countUse) {
		this.countUse = countUse;
	}

	public Date getResultDate() {
		return resultDate;
	}

	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBlockLocation() {
		return blockLocation;
	}

	public void setBlockLocation(String blockLocation) {
		this.blockLocation = blockLocation;
	}

	public String getBlockUsed() {
		return blockUsed;
	}

	public void setBlockUsed(String blockUsed) {
		this.blockUsed = blockUsed;
	}

	public String getBlockArea() {
		return blockArea;
	}

	public void setBlockArea(String blockArea) {
		this.blockArea = blockArea;
	}

	public String getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(String buildArea) {
		this.buildArea = buildArea;
	}

	public String getBuildUsed() {
		return buildUsed;
	}

	public void setBuildUsed(String buildUsed) {
		this.buildUsed = buildUsed;
	}

	public String getGreenRate() {
		return greenRate;
	}

	public void setGreenRate(String greenRate) {
		this.greenRate = greenRate;
	}

	public String getVolumeRate() {
		return volumeRate;
	}

	public void setVolumeRate(String volumeRate) {
		this.volumeRate = volumeRate;
	}

	public String getDensityRate() {
		return densityRate;
	}

	public void setDensityRate(String densityRate) {
		this.densityRate = densityRate;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(String noticeDate) {
		this.noticeDate = noticeDate;
	}

	public String getAuctionCount() {
		return auctionCount;
	}

	public void setAuctionCount(String auctionCount) {
		this.auctionCount = auctionCount;
	}

	public String getTranTime() {
		return tranTime;
	}

	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}

	public String getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(String initPrice) {
		this.initPrice = initPrice;
	}

	public String getiBlockPrice() {
		return iBlockPrice;
	}

	public void setiBlockPrice(String iBlockPrice) {
		this.iBlockPrice = iBlockPrice;
	}

	public String getiBuildPrice() {
		return iBuildPrice;
	}

	public void setiBuildPrice(String iBuildPrice) {
		this.iBuildPrice = iBuildPrice;
	}

	public String getTranPrice() {
		return tranPrice;
	}

	public void setTranPrice(String tranPrice) {
		this.tranPrice = tranPrice;
	}

	public String gettBlockPrice() {
		return tBlockPrice;
	}

	public void settBlockPrice(String tBlockPrice) {
		this.tBlockPrice = tBlockPrice;
	}

	public String gettBuildPrice() {
		return tBuildPrice;
	}

	public void settBuildPrice(String tBuildPrice) {
		this.tBuildPrice = tBuildPrice;
	}

	public String getTranMode() {
		return tranMode;
	}

	public void setTranMode(String tranMode) {
		this.tranMode = tranMode;
	}

}
