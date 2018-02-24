package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscTenderApp implements Serializable {

    /** identifier field */
    private String tenderAppId;

    /** nullable persistent field */
    private String tenderId;

    /** nullable persistent field */
    private BigDecimal tenderSer;

    /** nullable persistent field */
    private String tenderCert;

    /** nullable persistent field */
    private String norarId;

    /** nullable persistent field */
    private String norar;

    /** nullable persistent field */
    private String worker;

    /** nullable persistent field */
    private Timestamp tenderDate;

    /** nullable persistent field */
    private String tenderPlace;

    /** nullable persistent field */
    private BigDecimal priceShares;

    /** nullable persistent field */
    private BigDecimal businessShares;

    /** nullable persistent field */
    private BigDecimal techShares;

    /** nullable persistent field */
    private BigDecimal docShares;

    /** nullable persistent field */
    private String tenderType;

    /** nullable persistent field */
    private String tenderAppType;
    
    /** nullable persistent field */
    private String tenderNo;

    /** nullable persistent field */
    private String tenderPersonName;
    
    /** nullable persistent field */
    private String tenderMemo;

    /** nullable persistent field */
    private BigDecimal tenderPrice;

    /** full constructor */
    public TdscTenderApp(String tenderAppId, String tenderId, BigDecimal tenderSer, String tenderCert, String norarId, String norar, String worker,String tenderPersonName, Timestamp tenderDate, String tenderPlace, BigDecimal priceShares, BigDecimal businessShares, BigDecimal techShares, BigDecimal docShares,String tenderMemo,String tenderNo, String tenderType, String tenderAppType, BigDecimal tenderPrice) {
        this.tenderAppId = tenderAppId;
        this.tenderId = tenderId;
        this.tenderSer = tenderSer;
        this.tenderCert = tenderCert;
        this.norarId = norarId;
        this.norar = norar;
        this.worker = worker;
        this.tenderDate = tenderDate;
        this.tenderPlace = tenderPlace;
        this.priceShares = priceShares;
        this.businessShares = businessShares;
        this.techShares = techShares;
        this.docShares = docShares;
        this.tenderType = tenderType;
        this.tenderAppType = tenderAppType;
        this.tenderNo = tenderNo;
        this.tenderPersonName = tenderPersonName;
        this.tenderMemo = tenderMemo;
        this.tenderPrice = tenderPrice;
    }

    /** default constructor */
    public TdscTenderApp() {
    }

    /** minimal constructor */
    public TdscTenderApp(String tenderAppId) {
        this.tenderAppId = tenderAppId;
    }

    public String getTenderAppId() {
        return this.tenderAppId;
    }

    public void setTenderAppId(String tenderAppId) {
        this.tenderAppId = tenderAppId;
    }

    public String getTenderId() {
        return this.tenderId;
    }

    public void setTenderId(String tenderId) {
        this.tenderId = tenderId;
    }

    public BigDecimal getTenderSer() {
        return this.tenderSer;
    }

    public void setTenderSer(BigDecimal tenderSer) {
        this.tenderSer = tenderSer;
    }

    public String getTenderCert() {
        return this.tenderCert;
    }

    public void setTenderCert(String tenderCert) {
        this.tenderCert = tenderCert;
    }

    public String getNorarId() {
        return this.norarId;
    }

    public void setNorarId(String norarId) {
        this.norarId = norarId;
    }

    public String getNorar() {
        return this.norar;
    }

    public void setNorar(String norar) {
        this.norar = norar;
    }

    public String getWorker() {
        return this.worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public Timestamp getTenderDate() {
        return this.tenderDate;
    }

    public void setTenderDate(Timestamp tenderDate) {
        this.tenderDate = tenderDate;
    }

    public String getTenderPlace() {
        return this.tenderPlace;
    }

    public void setTenderPlace(String tenderPlace) {
        this.tenderPlace = tenderPlace;
    }

    public BigDecimal getPriceShares() {
        return this.priceShares;
    }

    public void setPriceShares(BigDecimal priceShares) {
        this.priceShares = priceShares;
    }

    public BigDecimal getBusinessShares() {
        return this.businessShares;
    }

    public void setBusinessShares(BigDecimal businessShares) {
        this.businessShares = businessShares;
    }

    public BigDecimal getTechShares() {
        return this.techShares;
    }

    public void setTechShares(BigDecimal techShares) {
        this.techShares = techShares;
    }

    public BigDecimal getDocShares() {
        return this.docShares;
    }

    public void setDocShares(BigDecimal docShares) {
        this.docShares = docShares;
    }

    public String getTenderType() {
        return this.tenderType;
    }

    public void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    public String getTenderAppType() {
        return this.tenderAppType;
    }

    public void setTenderAppType(String tenderAppType) {
        this.tenderAppType = tenderAppType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("tenderAppId", getTenderAppId())
            .toString();
    }

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public String getTenderPersonName() {
		return tenderPersonName;
	}

	public void setTenderPersonName(String tenderPersonName) {
		this.tenderPersonName = tenderPersonName;
	}

	public String getTenderMemo() {
		return tenderMemo;
	}

	public void setTenderMemo(String tenderMemo) {
		this.tenderMemo = tenderMemo;
	}

    public BigDecimal getTenderPrice() {
        return tenderPrice;
    }

    public void setTenderPrice(BigDecimal tenderPrice) {
        this.tenderPrice = tenderPrice;
    }

}
