package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscOpnn implements Serializable {

    /** identifier field */
    private String tdscOpnnId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private BigDecimal actionNum;

    /** nullable persistent field */
    private String actionId;

    /** nullable persistent field */
    private String actionOrgan;

    /** nullable persistent field */
    private String actionUserId;

    /** nullable persistent field */
    private String actionUser;

    /** nullable persistent field */
    private Timestamp actionDate;

    /** nullable persistent field */
    private String resultId;

    /** nullable persistent field */
    private String resultName;

    /** nullable persistent field */
    private String textOpen;

    /** nullable persistent field */
    private Timestamp firstDate;

    /** nullable persistent field */
    private Timestamp accDate;

    /** nullable persistent field */
    private String isOpt;

    /** nullable persistent field */
    private String memo;

    /** full constructor */
    public TdscOpnn(String tdscOpnnId, String appId, BigDecimal actionNum, String actionId, String actionOrgan, String actionUserId, String actionUser, Timestamp actionDate, String resultId, String resultName, String textOpen, Timestamp firstDate, Timestamp accDate, String isOpt, String memo) {
        this.tdscOpnnId = tdscOpnnId;
        this.appId = appId;
        this.actionNum = actionNum;
        this.actionId = actionId;
        this.actionOrgan = actionOrgan;
        this.actionUserId = actionUserId;
        this.actionUser = actionUser;
        this.actionDate = actionDate;
        this.resultId = resultId;
        this.resultName = resultName;
        this.textOpen = textOpen;
        this.firstDate = firstDate;
        this.accDate = accDate;
        this.isOpt = isOpt;
        this.memo = memo;
    }

    /** default constructor */
    public TdscOpnn() {
    }

    /** minimal constructor */
    public TdscOpnn(String tdscOpnnId) {
        this.tdscOpnnId = tdscOpnnId;
    }

    public String getTdscOpnnId() {
        return this.tdscOpnnId;
    }

    public void setTdscOpnnId(String tdscOpnnId) {
        this.tdscOpnnId = tdscOpnnId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public BigDecimal getActionNum() {
        return this.actionNum;
    }

    public void setActionNum(BigDecimal actionNum) {
        this.actionNum = actionNum;
    }

    public String getActionId() {
        return this.actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionOrgan() {
        return this.actionOrgan;
    }

    public void setActionOrgan(String actionOrgan) {
        this.actionOrgan = actionOrgan;
    }

    public String getActionUserId() {
        return actionUserId;
    }
    
    public void setActionUserId(String actionUserId) {
        this.actionUserId = actionUserId;
    }
    
    public String getActionUser() {
        return this.actionUser;
    }

    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }

    public String getResultId() {
        return this.resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public String getTextOpen() {
        return this.textOpen;
    }

    public void setTextOpen(String textOpen) {
        this.textOpen = textOpen;
    }


    public String getIsOpt() {
        return this.isOpt;
    }

    public void setIsOpt(String isOpt) {
        this.isOpt = isOpt;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    
    public Timestamp getAccDate() {
		return accDate;
	}

	public void setAccDate(Timestamp accDate) {
		this.accDate = accDate;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public Timestamp getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Timestamp firstDate) {
		this.firstDate = firstDate;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("tdscOpnnId", getTdscOpnnId())
            .toString();
    }

}
