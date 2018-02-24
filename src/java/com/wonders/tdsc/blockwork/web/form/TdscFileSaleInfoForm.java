package com.wonders.tdsc.blockwork.web.form;

import java.sql.Timestamp;

import org.apache.struts.action.ActionForm;

public class TdscFileSaleInfoForm extends ActionForm {

	private String	ifResultPublish;

	public String getIfResultPublish() {
		return ifResultPublish;
	}

	public void setIfResultPublish(String ifResultPublish) {
		this.ifResultPublish = ifResultPublish;
	}

	/** identifier field */
	private String		fileSaleInfo;

	/** NOTICE_ID */
	private String		noticeId;

	/** BIDDER_NAME */
	private String		bidderName;

	/** BIDDER_LXDZ */
	private String		bidderLxdz;

	/** RESULT_NAME */
	private String		resultName;

	/** BIDDER_LXDH */
	private String		bidderLxdh;

	/** BIDDER_YZBM */
	private String		bidderYzbm;

	/** RECORD_ID */
	private String		recordId;

	/** ACTION_USER */
	private String		actionUser;

	/** IF_VALIDITY */
	private String		ifValidity;

	/** ACTION_DATE */
	private Timestamp	actionDate;

	private String[]	block_choose;

	private String[]	bidderNames;

	private String[]	bidderLxdzs;

	private String[]	resultNames;

	private String[]	bidderLxdhs;

	private String[]	bidderYzbms;

	private String		noticeNo;

	// ÕÐÅÄ¹Ò±àºÅ
	private String		tradeNum;

	private String		transferMode;

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getFileSaleInfo() {
		return fileSaleInfo;
	}

	public void setFileSaleInfo(String fileSaleInfo) {
		this.fileSaleInfo = fileSaleInfo;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderLxdz() {
		return bidderLxdz;
	}

	public void setBidderLxdz(String bidderLxdz) {
		this.bidderLxdz = bidderLxdz;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getBidderLxdh() {
		return bidderLxdh;
	}

	public void setBidderLxdh(String bidderLxdh) {
		this.bidderLxdh = bidderLxdh;
	}

	public String getBidderYzbm() {
		return bidderYzbm;
	}

	public void setBidderYzbm(String bidderYzbm) {
		this.bidderYzbm = bidderYzbm;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getIfValidity() {
		return ifValidity;
	}

	public void setIfValidity(String ifValidity) {
		this.ifValidity = ifValidity;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public String[] getBlock_choose() {
		return block_choose;
	}

	public void setBlock_choose(String[] block_choose) {
		this.block_choose = block_choose;
	}

	public String[] getBidderNames() {
		return bidderNames;
	}

	public void setBidderNames(String[] bidderNames) {
		this.bidderNames = bidderNames;
	}

	public String[] getBidderLxdzs() {
		return bidderLxdzs;
	}

	public void setBidderLxdzs(String[] bidderLxdzs) {
		this.bidderLxdzs = bidderLxdzs;
	}

	public String[] getResultNames() {
		return resultNames;
	}

	public void setResultNames(String[] resultNames) {
		this.resultNames = resultNames;
	}

	public String[] getBidderLxdhs() {
		return bidderLxdhs;
	}

	public void setBidderLxdhs(String[] bidderLxdhs) {
		this.bidderLxdhs = bidderLxdhs;
	}

	public String[] getBidderYzbms() {
		return bidderYzbms;
	}

	public void setBidderYzbms(String[] bidderYzbms) {
		this.bidderYzbms = bidderYzbms;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

}
