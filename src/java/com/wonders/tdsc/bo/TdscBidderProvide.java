package com.wonders.tdsc.bo;

import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscBidderProvide extends BaseBO {

    /** identifier field */
    private String provideId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private String userId;

    /** nullable persistent field */
    private Integer regionId;

    /** nullable persistent field */
    private String bidType;

    /** nullable persistent field */
    private String provideBm;

    /** nullable persistent field */
    private Date provideDate;

    /** nullable persistent field */
    private String ifApp;

    /** nullable persistent field */
    private Date appDate;

    /** nullable persistent field */
    private String sourceType;

    /** nullable persistent field */
    private String memo;

    /** default constructor */
    public TdscBidderProvide() {
    }

    /** minimal constructor */
    public TdscBidderProvide(String provideId) {
        this.provideId = provideId;
    }

    public String getProvideId() {
        return this.provideId;
    }

    public void setProvideId(String provideId) {
        this.provideId = provideId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRegionId() {
        return this.regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getBidType() {
        return this.bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getProvideBm() {
        return this.provideBm;
    }

    public void setProvideBm(String provideBm) {
        this.provideBm = provideBm;
    }

    public Date getProvideDate() {
        return this.provideDate;
    }

    public void setProvideDate(Date provideDate) {
        this.provideDate = provideDate;
    }

    public String getIfApp() {
        return this.ifApp;
    }

    public void setIfApp(String ifApp) {
        this.ifApp = ifApp;
    }

    public Date getAppDate() {
        return this.appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
