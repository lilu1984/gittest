package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscListingApp extends BaseBO {

    /** identifier field */
    private String listingAppId;

    /** nullable persistent field */
    private String listingId;

    /** nullable persistent field */
    private BigDecimal listingSer;

    /** nullable persistent field */
    private String priceType;

    /** nullable persistent field */
    private BigDecimal priceNum;

    /** nullable persistent field */
    private String listCert;

    /** nullable persistent field */
    private String listNo;

    /** nullable persistent field */
    private BigDecimal listPrice;

    /** nullable persistent field */
    private BigDecimal addPrice;

    /** nullable persistent field */
    private Timestamp listDate;

    /** nullable persistent field */
    private String listPlace;

    /** nullable persistent field */
    private String listModeratpr;

    /** nullable persistent field */
    private String listNorar;

    /** nullable persistent field */
    private String currPrice;

    /** nullable persistent field */
    private String yktXh;
    
    private String appId;

    /** default constructor */
    public TdscListingApp() {
    }

    /** minimal constructor */
    public TdscListingApp(String listingAppId) {
        this.listingAppId = listingAppId;
    }

    public String getListingAppId() {
        return this.listingAppId;
    }

    public void setListingAppId(String listingAppId) {
        this.listingAppId = listingAppId;
    }

    public String getListingId() {
        return this.listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public BigDecimal getListingSer() {
        return this.listingSer;
    }

    public void setListingSer(BigDecimal listingSer) {
        this.listingSer = listingSer;
    }

    public String getPriceType() {
        return this.priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPriceNum() {
        return this.priceNum;
    }

    public void setPriceNum(BigDecimal priceNum) {
        this.priceNum = priceNum;
    }

    public String getListCert() {
        return this.listCert;
    }

    public void setListCert(String listCert) {
        this.listCert = listCert;
    }

    public String getListNo() {
        return this.listNo;
    }

    public void setListNo(String listNo) {
        this.listNo = listNo;
    }

    public BigDecimal getListPrice() {
        return this.listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getAddPrice() {
        return this.addPrice;
    }

    public void setAddPrice(BigDecimal addPrice) {
        this.addPrice = addPrice;
    }

    public Timestamp getListDate() {
        return this.listDate;
    }

    public void setListDate(Timestamp listDate) {
        this.listDate = listDate;
    }

    public String getListPlace() {
        return this.listPlace;
    }

    public void setListPlace(String listPlace) {
        this.listPlace = listPlace;
    }

    public String getListModeratpr() {
        return this.listModeratpr;
    }

    public void setListModeratpr(String listModeratpr) {
        this.listModeratpr = listModeratpr;
    }

    public String getListNorar() {
        return this.listNorar;
    }

    public void setListNorar(String listNorar) {
        this.listNorar = listNorar;
    }

    public String getCurrPrice() {
        return currPrice;
    }

    public void setCurrPrice(String currPrice) {
        this.currPrice = currPrice;
    }

    public String getYktXh() {
        return yktXh;
    }

    public void setYktXh(String yktXh) {
        this.yktXh = yktXh;
    }

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
