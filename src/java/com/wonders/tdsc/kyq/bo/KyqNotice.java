package com.wonders.tdsc.kyq.bo;

import java.sql.Timestamp;

public class KyqNotice {
	private String noticeId;

	private String noticeNumber;

	private String transferMode;

	private String linkManName;

	private Timestamp accAppStatDate;

	private Timestamp accAppEndDate;

	private Timestamp listStartDate;

	private Timestamp listEndDate;

	private Timestamp sceBidDate;

	private Timestamp resultShowDate;

	private Timestamp marginEndDate;

	private String status;

	private Timestamp noticePublishDate;

	private String recordId;
	
	private Timestamp onLineStatDate;
	
	private Timestamp onLineEndDate;
	
	private String ifOnLine;
	
	private String ifReleased;
	
	private Timestamp statusDate;
	
	private String ifResultPublish;
	
	private String resultRecordId;
		
	public String getResultRecordId() {
		return resultRecordId;
	}

	public void setResultRecordId(String resultRecordId) {
		this.resultRecordId = resultRecordId;
	}

	public String getIfResultPublish() {
		return ifResultPublish;
	}

	public void setIfResultPublish(String ifResultPublish) {
		this.ifResultPublish = ifResultPublish;
	}

	public Timestamp getNoticePublishDate() {
		return noticePublishDate;
	}

	public void setNoticePublishDate(Timestamp noticePublishDate) {
		this.noticePublishDate = noticePublishDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getIfOnLine() {
		return ifOnLine;
	}

	public void setIfOnLine(String ifOnLine) {
		this.ifOnLine = ifOnLine;
	}

	public String getIfReleased() {
		return ifReleased;
	}

	public void setIfReleased(String ifReleased) {
		this.ifReleased = ifReleased;
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

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

}
