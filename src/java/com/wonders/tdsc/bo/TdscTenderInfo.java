package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscTenderInfo implements Serializable {

    /** identifier field */
    private String tenderId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private String tenderType;
    
    /** nullable persistent field */
    private String tenderResult;
    
    /** nullable persistent field */
    private String tenderCertNo;
    
    /** nullable persistent field */
    private BigDecimal tenderScore;
    
    /** nullable persistent field */
    private Timestamp tenderResultTime;
    
    /** nullable persistent field */
    private String tenderFailedReason;
    
    /** nullable persistent field */
    private String tenderFailedMemo;
    
    /** nullable persistent field */
    private String tenderNo;

    /** nullable persistent field */
    private BigDecimal resultPrice;
    
    /** full constructor */
    public TdscTenderInfo(String tenderId, String appId, String tenderType,String tenderResult,String tenderCertNo,BigDecimal tenderScore,Timestamp tenderResultTime,String tenderFailedReason,String tenderFailedMemo,String tenderNo,BigDecimal resultPrice) {
        this.tenderId = tenderId;
        this.appId = appId;
        this.tenderType = tenderType;
        this.tenderResult = tenderResult;
        this.tenderCertNo = tenderCertNo;
        this.tenderScore = tenderScore;
        this.tenderResultTime = tenderResultTime;
        this.tenderFailedReason = tenderFailedReason;
        this.tenderFailedMemo = tenderFailedMemo;
        this.tenderNo = tenderNo;
        this.resultPrice = resultPrice;
    }

    /** default constructor */
    public TdscTenderInfo() {
    }

    /** minimal constructor */
    public TdscTenderInfo(String tenderId) {
        this.tenderId = tenderId;
    }

    public String getTenderId() {
        return this.tenderId;
    }

    public void setTenderId(String tenderId) {
        this.tenderId = tenderId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTenderType() {
        return this.tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("tenderId", getTenderId())
            .toString();
    }

	public String getTenderResult() {
		return tenderResult;
	}

	public void setTenderResult(String tenderResult) {
		this.tenderResult = tenderResult;
	}

	public String getTenderCertNo() {
		return tenderCertNo;
	}

	public void setTenderCertNo(String tenderCertNo) {
		this.tenderCertNo = tenderCertNo;
	}

	public BigDecimal getTenderScore() {
		return tenderScore;
	}

	public void setTenderScore(BigDecimal tenderScore) {
		this.tenderScore = tenderScore;
	}

	public String getTenderFailedReason() {
		return tenderFailedReason;
	}

	public void setTenderFailedReason(String tenderFailedReason) {
		this.tenderFailedReason = tenderFailedReason;
	}

	public String getTenderFailedMemo() {
		return tenderFailedMemo;
	}

	public void setTenderFailedMemo(String tenderFailedMemo) {
		this.tenderFailedMemo = tenderFailedMemo;
	}

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public Timestamp getTenderResultTime() {
		return tenderResultTime;
	}

	public void setTenderResultTime(Timestamp tenderResultTime) {
		this.tenderResultTime = tenderResultTime;
	}

    public BigDecimal getResultPrice() {
        return resultPrice;
    }
    
    public void setResultPrice(BigDecimal resultPrice) {
        this.resultPrice = resultPrice;
    }
}
