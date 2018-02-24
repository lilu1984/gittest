package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBidderMaterial implements Serializable {

    /** identifier field */
    private String bidderMaterialId;

    /** nullable persistent field */
    private String bidderId;

    /** nullable persistent field */
    private String bidderPersonId;

    /** nullable persistent field */
    private String materialCode;

    /** nullable persistent field */
    private String materialBh;

    /** nullable persistent field */
    private String materialType;

    /** nullable persistent field */
    private Integer materialCount;

    /** nullable persistent field */
    private String memo;

    /** full constructor */
    public TdscBidderMaterial(String bidderMaterialId, String bidderId, String bidderPersonId, String materialCode, String materialBh, String materialType, Integer materialCount, String memo) {
        this.bidderMaterialId = bidderMaterialId;
        this.bidderId = bidderId;
        this.bidderPersonId = bidderPersonId;
        this.materialCode = materialCode;
        this.materialBh = materialBh;
        this.materialType = materialType;
        this.materialCount = materialCount;
        this.memo = memo;
    }

    /** default constructor */
    public TdscBidderMaterial() {
    }

    /** minimal constructor */
    public TdscBidderMaterial(String bidderMaterialId) {
        this.bidderMaterialId = bidderMaterialId;
    }

    public String getBidderMaterialId() {
        return this.bidderMaterialId;
    }

    public void setBidderMaterialId(String bidderMaterialId) {
        this.bidderMaterialId = bidderMaterialId;
    }

    public String getBidderId() {
        return this.bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getBidderPersonId() {
        return this.bidderPersonId;
    }

    public void setBidderPersonId(String bidderPersonId) {
        this.bidderPersonId = bidderPersonId;
    }

    public String getMaterialCode() {
        return this.materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialBh() {
        return this.materialBh;
    }

    public void setMaterialBh(String materialBh) {
        this.materialBh = materialBh;
    }

    public String getMaterialType() {
        return this.materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public Integer getMaterialCount() {
        return this.materialCount;
    }

    public void setMaterialCount(Integer materialCount) {
        this.materialCount = materialCount;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("bidderMaterialId", getBidderMaterialId())
            .toString();
    }

}
