package com.wonders.wsjy.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TdscTradeView implements java.io.Serializable {

	private String appId;
	private String blockId;
	private String noticeId;
	private String planId;
	private String blockName;
	private Integer districtId;
	private String landLocation;
	private BigDecimal totalLandArea;
	private BigDecimal totalBuildingArea;
	private String blockStatus;
	private String blockQuality;
	private String noitceNo;
	private Integer xuHao;
	private String isPurposeBlock;
	private String blockNoticeNo;
	private String tradeStatus;
	private BigDecimal initPrice;
	private BigDecimal priceAdd;
	private String resultNo;
	private String tranResult;
	private Timestamp resultDate;
	private BigDecimal resultPrice;
	private String resultCert;
	private String resultName;
	private String transferMode;
	private BigDecimal guapaiCurrPrice;
	private Long guapaiCurrRound;
	private Timestamp guapaiListDate;
	private String guapaiListCert;
	private String ifOnLine;
	private Timestamp listStartDate;
	private Timestamp listEndDate;
	private Timestamp onLineStatDate;
	private Timestamp onLineEndDate;
	private Timestamp resultShowDate;
	
	private BigDecimal maxPrice;
	private String partakeBidderConNum;
	
	private Timestamp accAppStatDate;
	private Timestamp accAppEndDate;

    /** nullable persistent field */
    private String guapaiListNo;

    /** nullable persistent field */
    private Timestamp sceBidDate;
    
    private BigDecimal marginAmount;
    
    private Timestamp marginEndDate;
    
	public Timestamp getSceBidDate() {
		return sceBidDate;
	}
	public void setSceBidDate(Timestamp sceBidDate) {
		this.sceBidDate = sceBidDate;
	}
	public String getGuapaiListNo() {
		return guapaiListNo;
	}
	public void setGuapaiListNo(String guapaiListNo) {
		this.guapaiListNo = guapaiListNo;
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
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}
	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}
	public String getBlockQuality() {
		return blockQuality;
	}
	public void setBlockQuality(String blockQuality) {
		this.blockQuality = blockQuality;
	}
	public String getBlockStatus() {
		return blockStatus;
	}
	public void setBlockStatus(String blockStatus) {
		this.blockStatus = blockStatus;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public BigDecimal getGuapaiCurrPrice() {
		return guapaiCurrPrice;
	}
	public void setGuapaiCurrPrice(BigDecimal guapaiCurrPrice) {
		this.guapaiCurrPrice = guapaiCurrPrice;
	}
	public Long getGuapaiCurrRound() {
		return guapaiCurrRound;
	}
	public void setGuapaiCurrRound(Long guapaiCurrRound) {
		this.guapaiCurrRound = guapaiCurrRound;
	}
	public String getGuapaiListCert() {
		return guapaiListCert;
	}
	public void setGuapaiListCert(String guapaiListCert) {
		this.guapaiListCert = guapaiListCert;
	}
	public Timestamp getGuapaiListDate() {
		return guapaiListDate;
	}
	public void setGuapaiListDate(Timestamp guapaiListDate) {
		this.guapaiListDate = guapaiListDate;
	}
	public String getIfOnLine() {
		return ifOnLine;
	}
	public void setIfOnLine(String ifOnLine) {
		this.ifOnLine = ifOnLine;
	}
	public BigDecimal getInitPrice() {
		return initPrice;
	}
	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}
	public String getIsPurposeBlock() {
		return isPurposeBlock;
	}
	public void setIsPurposeBlock(String isPurposeBlock) {
		this.isPurposeBlock = isPurposeBlock;
	}
	public String getLandLocation() {
		return landLocation;
	}
	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}
	public Timestamp getListEndDate() {
		return listEndDate;
	}
	public void setListEndDate(Timestamp listEndDate) {
		this.listEndDate = listEndDate;
	}
	public Timestamp getListStartDate() {
		return listStartDate;
	}
	public void setListStartDate(Timestamp listStartDate) {
		this.listStartDate = listStartDate;
	}
	public String getNoitceNo() {
		return noitceNo;
	}
	public void setNoitceNo(String noitceNo) {
		this.noitceNo = noitceNo;
	}
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public Timestamp getOnLineEndDate() {
		return onLineEndDate;
	}
	public void setOnLineEndDate(Timestamp onLineEndDate) {
		this.onLineEndDate = onLineEndDate;
	}
	public Timestamp getOnLineStatDate() {
		return onLineStatDate;
	}
	public void setOnLineStatDate(Timestamp onLineStatDate) {
		this.onLineStatDate = onLineStatDate;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public BigDecimal getPriceAdd() {
		return priceAdd;
	}
	public void setPriceAdd(BigDecimal priceAdd) {
		this.priceAdd = priceAdd;
	}
	public String getResultCert() {
		return resultCert;
	}
	public void setResultCert(String resultCert) {
		this.resultCert = resultCert;
	}
	public Timestamp getResultDate() {
		return resultDate;
	}
	public void setResultDate(Timestamp resultDate) {
		this.resultDate = resultDate;
	}
	public String getResultName() {
		return resultName;
	}
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
	public String getResultNo() {
		return resultNo;
	}
	public void setResultNo(String resultNo) {
		this.resultNo = resultNo;
	}
	public BigDecimal getResultPrice() {
		return resultPrice;
	}
	public void setResultPrice(BigDecimal resultPrice) {
		this.resultPrice = resultPrice;
	}
	public Timestamp getResultShowDate() {
		return resultShowDate;
	}
	public void setResultShowDate(Timestamp resultShowDate) {
		this.resultShowDate = resultShowDate;
	}
	public BigDecimal getTotalBuildingArea() {
		return totalBuildingArea;
	}
	public void setTotalBuildingArea(BigDecimal totalBuildingArea) {
		this.totalBuildingArea = totalBuildingArea;
	}
	public BigDecimal getTotalLandArea() {
		return totalLandArea;
	}
	public void setTotalLandArea(BigDecimal totalLandArea) {
		this.totalLandArea = totalLandArea;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getTranResult() {
		return tranResult;
	}
	public void setTranResult(String tranResult) {
		this.tranResult = tranResult;
	}
	public String getTransferMode() {
		return transferMode;
	}
	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}
	public Integer getXuHao() {
		return xuHao;
	}
	public void setXuHao(Integer xuHao) {
		this.xuHao = xuHao;
	}
	public void setPartakeBidderConNum(String partakeBidderConNum) {
		this.partakeBidderConNum = partakeBidderConNum;
	}
	public String getPartakeBidderConNum() {
		return partakeBidderConNum;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
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
	public BigDecimal getMarginAmount() {
		return marginAmount;
	}
	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}
	public Timestamp getMarginEndDate() {
		return marginEndDate;
	}
	public void setMarginEndDate(Timestamp marginEndDate) {
		this.marginEndDate = marginEndDate;
	}
	

}
