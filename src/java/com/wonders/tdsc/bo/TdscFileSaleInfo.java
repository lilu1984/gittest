package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/** @author Hibernate CodeGenerator */
public class TdscFileSaleInfo implements Serializable {

    /** identifier field */
    private String fileSaleId;
    
    /** NOTICE_ID */
    private String noticeId;
    
    /** BIDDER_NAME */
    private String bidderName;
    
    /** BIDDER_LXDZ */
    private String bidderLxdz;
    
    /** RESULT_NAME */
    private String resultName;
    
    /** BIDDER_LXDH */
    private String bidderLxdh;
    
    /** BIDDER_YZBM  */
    private String bidderYzbm;
    
    /** RECORD_ID*/
    private String recordId;
    
    /** ACTION_USER */
    private String actionUser;
    
    /** IF_VALIDITY */
    private String ifValidity;
    
    /** ACTION_DATE */
    private Timestamp actionDate;
    
    private String appId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getFileSaleId() {
		return fileSaleId;
	}

	public void setFileSaleId(String fileSaleId) {
		this.fileSaleId = fileSaleId;
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
}
