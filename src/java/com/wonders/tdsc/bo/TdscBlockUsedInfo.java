package com.wonders.tdsc.bo;

import java.math.BigDecimal;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscBlockUsedInfo extends BaseBO {

    /** identifier field */
    private String blockUsedId;

    /** nullable persistent field */
    private String blockId;

    /** nullable persistent field */
    private String useId;

    /** nullable persistent field */
    private String landUseType;

    /** nullable persistent field */
    private String planUseMemo;

    /** nullable persistent field */
    private String surveyId;

    /** nullable persistent field */
    private String reportId;

    /** nullable persistent field */
    private BigDecimal buildingArea;

    /** default constructor */
    public TdscBlockUsedInfo() {
    }

    /** minimal constructor */
    public TdscBlockUsedInfo(String blockUsedId) {
        this.blockUsedId = blockUsedId;
    }

    public String getBlockUsedId() {
        return this.blockUsedId;
    }

    public void setBlockUsedId(String blockUsedId) {
        this.blockUsedId = blockUsedId;
    }

    public String getBlockId() {
        return this.blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getUseId() {
        return this.useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public String getLandUseType() {
        return landUseType;
    }

    public void setLandUseType(String landUseType) {
        this.landUseType = landUseType;
    }

    public String getPlanUseMemo() {
        return this.planUseMemo;
    }

    public void setPlanUseMemo(String planUseMemo) {
        this.planUseMemo = planUseMemo;
    }

    public String getSurveyId() {
        return this.surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getReportId() {
        return this.reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public BigDecimal getBuildingArea() {
        return this.buildingArea;
    }

    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }

}
