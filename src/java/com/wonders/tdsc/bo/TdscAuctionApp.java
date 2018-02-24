package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscAuctionApp implements Serializable {

	/** identifier field */
    private String auctionAppId;

    /** nullable persistent field */
    private String auctionId;

    /** nullable persistent field */
    private BigDecimal auctionSer;

    /** nullable persistent field */
    private String conNum;

    /** nullable persistent field */
    private BigDecimal addRange;

    /** nullable persistent field */
    private BigDecimal addPrice;
    
    private BigDecimal totalPrice;
    
    private Timestamp auctionDate;
    
    private String appId;

    /** ∫≈≈∆ */
    private String haoPai;

    public String getHaoPai() {
		return haoPai;
	}

	public void setHaoPai(String haoPai) {
		this.haoPai = haoPai;
	}

	/** full constructor */
    public TdscAuctionApp(String auctionAppId, String auctionId, BigDecimal auctionSer, String conNum, BigDecimal addRange, BigDecimal addPrice,BigDecimal totalPrice,Timestamp auctionDate,String appId,String haoPai) {
        this.auctionAppId = auctionAppId;
        this.auctionId = auctionId;
        this.auctionSer = auctionSer;
        this.conNum = conNum;
        this.addRange = addRange;
        this.addPrice = addPrice;
        this.totalPrice = totalPrice;
        this.auctionDate = auctionDate;
        this.appId = appId;
        this.haoPai = haoPai;
    }

    /** default constructor */
    public TdscAuctionApp() {
    }

    /** minimal constructor */
    public TdscAuctionApp(String auctionAppId) {
        this.auctionAppId = auctionAppId;
    }

    public String getAuctionAppId() {
        return this.auctionAppId;
    }

    public void setAuctionAppId(String auctionAppId) {
        this.auctionAppId = auctionAppId;
    }

    public String getAuctionId() {
        return this.auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public BigDecimal getAuctionSer() {
        return this.auctionSer;
    }

    public void setAuctionSer(BigDecimal auctionSer) {
        this.auctionSer = auctionSer;
    }

    public String getConNum() {
        return this.conNum;
    }

    public void setConNum(String conNum) {
        this.conNum = conNum;
    }

    public BigDecimal getAddRange() {
        return this.addRange;
    }

    public void setAddRange(BigDecimal addRange) {
        this.addRange = addRange;
    }

    public BigDecimal getAddPrice() {
        return this.addPrice;
    }

    public void setAddPrice(BigDecimal addPrice) {
        this.addPrice = addPrice;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("auctionAppId", getAuctionAppId())
            .toString();
    }

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Timestamp getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(Timestamp auctionDate) {
		this.auctionDate = auctionDate;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
