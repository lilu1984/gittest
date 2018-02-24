package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBlockMaterial implements Serializable {

    /** identifier field */
    private String materialId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private BigDecimal materialNum;

    /** nullable persistent field */
    private String blockId;

    /** nullable persistent field */
    private String materialNumb;

    /** nullable persistent field */
    private String materialType;
    
    
    private String materialName;

    /** nullable persistent field */
    private String issueType;

    /** nullable persistent field */
    private String issueUnit;

    /** nullable persistent field */
    private String issueUnitMemo;

    /** nullable persistent field */
    private String fileUrl;

    /** nullable persistent field */
    private String memo;

    /** full constructor */
    public TdscBlockMaterial(String materialId, String appId, BigDecimal materialNum, String blockId, String materialNumb,String materialType,String materialName, String issueType, String issueUnit, String issueUnitMemo, String fileUrl, String memo) {
        this.materialId = materialId;
        this.appId = appId;
        this.materialNum = materialNum;
        this.blockId = blockId;
        this.materialNumb = materialNumb;
        this.materialType = materialType;
        this.materialName = materialName;
        this.issueType = issueType;
        this.issueUnit = issueUnit;
        this.issueUnitMemo = issueUnitMemo;
        this.fileUrl = fileUrl;
        this.memo = memo;
    }

    /** default constructor */
    public TdscBlockMaterial() {
    }

    /** minimal constructor */
    public TdscBlockMaterial(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialId() {
        return this.materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public BigDecimal getMaterialNum() {
        return this.materialNum;
    }

    public void setMaterialNum(BigDecimal materialNum) {
        this.materialNum = materialNum;
    }

    public String getBlockId() {
        return this.blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getMaterialNumb() {
        return this.materialNumb;
    }

    public void setMaterialNumb(String materialNumb) {
        this.materialNumb = materialNumb;
    }

    public String getIssueType() {
        return this.issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueUnit() {
        return this.issueUnit;
    }

    public void setIssueUnit(String issueUnit) {
        this.issueUnit = issueUnit;
    }

    public String getIssueUnitMemo() {
        return this.issueUnitMemo;
    }

    public void setIssueUnitMemo(String issueUnitMemo) {
        this.issueUnitMemo = issueUnitMemo;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("materialId", getMaterialId())
            .toString();
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

}
