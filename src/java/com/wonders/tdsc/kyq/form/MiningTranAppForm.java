package com.wonders.tdsc.kyq.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.struts.action.ActionForm;

public class MiningTranAppForm extends ActionForm {
	private String tranAppId;

	private String noticeId;

	private String noticeNo;

	private String miningName;

	private String remisePerson;

	private String transferMode;

	private String miningMineral;

	private BigDecimal miningArea;

	private BigDecimal resources;

	private String resourcesDw;

	private String sellYearStart;

	private String sellYearEnd;

	private BigDecimal marginAmount;

	private Integer xuHao;

	private String usbKeyId;

	private String transResult;

	private String resultName;

	private String resultBidderId;

	private String bidderId;

	private String bidderName;

	private String bidderProperty;

	private String zjlx;

	private String zjhm;

	private String lxdh;

	private String lxdz;

	private String yzbm;

	private String corpFddbrZjlx;

	private String corpFddbrZjhm;

	private String miningId;

	private String certNo;

	private String bankName;

	private String haoPai;

	private String noticeNumber;

	private String linkManName;

	private Timestamp accAppStatDate;

	private Timestamp accAppEndDate;

	private Timestamp listStartDate;

	private Timestamp listEndDate;

	private Timestamp sceBidDate;

	private Timestamp resultShowDate;

	private Timestamp marginEndDate;

	private String appIds[];

	private String tranStatus;

	private String isPurposeBlock;

	private BigDecimal initPrice;

	private BigDecimal spotAddPriceRange;

	private String purposeAppId;

	private String ifOnLine;

	private String lxr;

	private String isAccptSms;

	private String pushSmsPhone;

	private String recordId;

	private String ifReleased;

	private String ifResultPublish;

	public String getIfResultPublish() {
		return ifResultPublish;
	}

	public void setIfResultPublish(String ifResultPublish) {
		this.ifResultPublish = ifResultPublish;
	}

	public String getIfReleased() {
		return ifReleased;
	}

	public void setIfReleased(String ifReleased) {
		this.ifReleased = ifReleased;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getIsAccptSms() {
		return isAccptSms;
	}

	public void setIsAccptSms(String isAccptSms) {
		this.isAccptSms = isAccptSms;
	}

	public String getPushSmsPhone() {
		return pushSmsPhone;
	}

	public void setPushSmsPhone(String pushSmsPhone) {
		this.pushSmsPhone = pushSmsPhone;
	}

	public String getLxr() {
		return lxr;
	}

	public void setLxr(String lxr) {
		this.lxr = lxr;
	}

	public String getTranStatus() {
		return tranStatus;
	}

	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}

	private Timestamp noticePublishDate;

	public Timestamp getNoticePublishDate() {
		return noticePublishDate;
	}

	public void setNoticePublishDate(Timestamp noticePublishDate) {
		this.noticePublishDate = noticePublishDate;
	}

	public String[] getAppIds() {
		return appIds;
	}

	public void setAppIds(String[] appIds) {
		this.appIds = appIds;
	}

	public String getTranAppId() {
		return tranAppId;
	}

	public void setTranAppId(String tranAppId) {
		this.tranAppId = tranAppId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getMiningName() {
		return miningName;
	}

	public void setMiningName(String miningName) {
		this.miningName = miningName;
	}

	public String getRemisePerson() {
		return remisePerson;
	}

	public void setRemisePerson(String remisePerson) {
		this.remisePerson = remisePerson;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getMiningMineral() {
		return miningMineral;
	}

	public void setMiningMineral(String miningMineral) {
		this.miningMineral = miningMineral;
	}

	public BigDecimal getMiningArea() {
		return miningArea;
	}

	public void setMiningArea(BigDecimal miningArea) {
		this.miningArea = miningArea;
	}

	public BigDecimal getResources() {
		return resources;
	}

	public void setResources(BigDecimal resources) {
		this.resources = resources;
	}

	public String getResourcesDw() {
		return resourcesDw;
	}

	public void setResourcesDw(String resourcesDw) {
		this.resourcesDw = resourcesDw;
	}

	public String getSellYearStart() {
		return sellYearStart;
	}

	public void setSellYearStart(String sellYearStart) {
		this.sellYearStart = sellYearStart;
	}

	public String getSellYearEnd() {
		return sellYearEnd;
	}

	public void setSellYearEnd(String sellYearEnd) {
		this.sellYearEnd = sellYearEnd;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public Integer getXuHao() {
		return xuHao;
	}

	public void setXuHao(Integer xuHao) {
		this.xuHao = xuHao;
	}

	public String getUsbKeyId() {
		return usbKeyId;
	}

	public void setUsbKeyId(String usbKeyId) {
		this.usbKeyId = usbKeyId;
	}

	public String getTransResult() {
		return transResult;
	}

	public void setTransResult(String transResult) {
		this.transResult = transResult;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getResultBidderId() {
		return resultBidderId;
	}

	public void setResultBidderId(String resultBidderId) {
		this.resultBidderId = resultBidderId;
	}

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderProperty() {
		return bidderProperty;
	}

	public void setBidderProperty(String bidderProperty) {
		this.bidderProperty = bidderProperty;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getLxdz() {
		return lxdz;
	}

	public void setLxdz(String lxdz) {
		this.lxdz = lxdz;
	}

	public String getYzbm() {
		return yzbm;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}

	public String getCorpFddbrZjlx() {
		return corpFddbrZjlx;
	}

	public void setCorpFddbrZjlx(String corpFddbrZjlx) {
		this.corpFddbrZjlx = corpFddbrZjlx;
	}

	public String getCorpFddbrZjhm() {
		return corpFddbrZjhm;
	}

	public void setCorpFddbrZjhm(String corpFddbrZjhm) {
		this.corpFddbrZjhm = corpFddbrZjhm;
	}

	public String getMiningId() {
		return miningId;
	}

	public void setMiningId(String miningId) {
		this.miningId = miningId;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getHaoPai() {
		return haoPai;
	}

	public void setHaoPai(String haoPai) {
		this.haoPai = haoPai;
	}

	public String getNoticeNumber() {
		return noticeNumber;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public String getLinkManName() {
		return linkManName;
	}

	public void setLinkManName(String linkManName) {
		this.linkManName = linkManName;
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

	public Timestamp getResultShowDate() {
		return resultShowDate;
	}

	public void setResultShowDate(Timestamp resultShowDate) {
		this.resultShowDate = resultShowDate;
	}

	public Timestamp getMarginEndDate() {
		return marginEndDate;
	}

	public void setMarginEndDate(Timestamp marginEndDate) {
		this.marginEndDate = marginEndDate;
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

	public BigDecimal getSpotAddPriceRange() {
		return spotAddPriceRange;
	}

	public void setSpotAddPriceRange(BigDecimal spotAddPriceRange) {
		this.spotAddPriceRange = spotAddPriceRange;
	}

	public String getPurposeAppId() {
		return purposeAppId;
	}

	public void setPurposeAppId(String purposeAppId) {
		this.purposeAppId = purposeAppId;
	}

	public String getIfOnLine() {
		return ifOnLine;
	}

	public void setIfOnLine(String ifOnLine) {
		this.ifOnLine = ifOnLine;
	}

}
