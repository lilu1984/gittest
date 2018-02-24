package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscBlockSelectAppOld implements Serializable {

    /** identifier field */
    private String selectId;

    /** nullable persistent field */
    private String appId;
    
    /** nullable persistent field */
    private String blockId;

    /** nullable persistent field */
    private String selectType;

    /** nullable persistent field */
    private String selectStage;

    /** nullable persistent field */
    private String selectUser;

    /** nullable persistent field */
    private Date selectDate;

    /** nullable persistent field */
    private String selectedId;

    /** nullable persistent field */
    private String selectedName;

    /** nullable persistent field */
    private String selectedLink;

    /** nullable persistent field */
    private String isPresent;

    /** nullable persistent field */
    private String candidate;

    /** full constructor */
    public TdscBlockSelectAppOld(String selectId, String appId,String blockId, String selectType, String selectStage, String selectUser, Date selectDate, String selectedId, String selectedName, String selectedLink, String isPresent, String candidate) {
        this.selectId = selectId;
        this.appId = appId;
        this.blockId = blockId;
        this.selectType = selectType;
        this.selectStage = selectStage;
        this.selectUser = selectUser;
        this.selectDate = selectDate;
        this.selectedId = selectedId;
        this.selectedName = selectedName;
        this.selectedLink = selectedLink;
        this.isPresent = isPresent;
        this.candidate = candidate;
    }

    /** default constructor */
    public TdscBlockSelectAppOld() {
    }

    /** minimal constructor */
    public TdscBlockSelectAppOld(String selectId) {
        this.selectId = selectId;
    }

    public String getSelectId() {
        return this.selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSelectType() {
        return this.selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getSelectStage() {
        return this.selectStage;
    }

    public void setSelectStage(String selectStage) {
        this.selectStage = selectStage;
    }

    public String getSelectUser() {
        return this.selectUser;
    }

    public void setSelectUser(String selectUser) {
        this.selectUser = selectUser;
    }

    public Date getSelectDate() {
        return this.selectDate;
    }

    public void setSelectDate(Date selectDate) {
        this.selectDate = selectDate;
    }

    public String getSelectedId() {
        return this.selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedName() {
        return this.selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public String getSelectedLink() {
        return this.selectedLink;
    }

    public void setSelectedLink(String selectedLink) {
        this.selectedLink = selectedLink;
    }

    public String getIsPresent() {
        return this.isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getCandidate() {
        return this.candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("selectId", getSelectId())
            .toString();
    }

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

}
