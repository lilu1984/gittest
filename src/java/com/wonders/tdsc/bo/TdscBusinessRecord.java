package com.wonders.tdsc.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBusinessRecord implements Serializable {

    /** identifier field */
    private String recordId;

    /** nullable persistent field */
    private String userId;

    /** nullable persistent field */
    private Integer regionId;

    /** nullable persistent field */
    private String busiType;

    /** nullable persistent field */
    private String busiId;

    /** nullable persistent field */
    private Integer provideCount;

    /** nullable persistent field */
    private String bakCol1;

    /** nullable persistent field */
    private String bakCol2;

    /** nullable persistent field */
    private String bakCol3;

    /** full constructor */
    public TdscBusinessRecord(String userId, Integer regionId, String busiType, String busiId, Integer provideCount, String bakCol1, String bakCol2, String bakCol3) {
        this.userId = userId;
        this.regionId = regionId;
        this.busiType = busiType;
        this.busiId = busiId;
        this.provideCount = provideCount;
        this.bakCol1 = bakCol1;
        this.bakCol2 = bakCol2;
        this.bakCol3 = bakCol3;
    }

    /** default constructor */
    public TdscBusinessRecord() {
    }

    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getBusiType() {
        return this.busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getBusiId() {
        return this.busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    public Integer getProvideCount() {
        return this.provideCount;
    }

    public void setProvideCount(Integer provideCount) {
        this.provideCount = provideCount;
    }

    public String getBakCol1() {
        return this.bakCol1;
    }

    public void setBakCol1(String bakCol1) {
        this.bakCol1 = bakCol1;
    }

    public String getBakCol2() {
        return this.bakCol2;
    }

    public void setBakCol2(String bakCol2) {
        this.bakCol2 = bakCol2;
    }

    public String getBakCol3() {
        return this.bakCol3;
    }

    public void setBakCol3(String bakCol3) {
        this.bakCol3 = bakCol3;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("recordId", getRecordId())
            .toString();
    }

}
