package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscReplyConfInfo implements Serializable {

    /** identifier field */
    private String appId;
    
    /** identifier field */
    private String recordId;

    /** nullable persistent field */
    private String confStat;

    /** nullable persistent field */
    private BigDecimal questionNum;

    /** nullable persistent field */
    private String answerUnit;

    /** nullable persistent field */
    private String faqUrl;

    /** nullable persistent field */
    
    private String ifPublish;

    /** nullable persistent field */
    private Date publishDate;
    
    /** 地块名称*/
    private String blockName;
    
    /** 交易方式 */
    private String transferMode;
    
    /** 土地类型 */
    private String blockType;
    
    /** 审批区县 */
    private String endorseDistrict;

    /** full constructor */
    public TdscReplyConfInfo(String appId, String confStat, BigDecimal questionNum, String answerUnit, String faqUrl,String ifPublish,Date publishDate,String recordId) {
        this.appId = appId;
        this.confStat = confStat;
        this.questionNum = questionNum;
        this.answerUnit = answerUnit;
        this.faqUrl = faqUrl;
        this.ifPublish = ifPublish;
        this.publishDate = publishDate;
        this.recordId = recordId;
    }

    /** default constructor */
    public TdscReplyConfInfo() {
    }

    /** minimal constructor */
    public TdscReplyConfInfo(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getConfStat() {
        return this.confStat;
    }

    public void setConfStat(String confStat) {
        this.confStat = confStat;
    }

    public BigDecimal getQuestionNum() {
        return this.questionNum;
    }

    public void setQuestionNum(BigDecimal questionNum) {
        this.questionNum = questionNum;
    }

    public String getAnswerUnit() {
        return this.answerUnit;
    }

    public void setAnswerUnit(String answerUnit) {
        this.answerUnit = answerUnit;
    }

    public String getFaqUrl() {
        return this.faqUrl;
    }

    public void setFaqUrl(String faqUrl) {
        this.faqUrl = faqUrl;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("appId", getAppId())
            .toString();
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getEndorseDistrict() {
        return endorseDistrict;
    }

    public void setEndorseDistrict(String endorseDistrict) {
        this.endorseDistrict = endorseDistrict;
    }

    public String getIfPublish() {
        return ifPublish;
    }

    public void setIfPublish(String ifPublish) {
        this.ifPublish = ifPublish;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

}
