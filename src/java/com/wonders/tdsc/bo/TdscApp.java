package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscApp implements Serializable {

    /** identifier field */
    private String tdscAppId;

    /** nullable persistent field */
    private String appType;

    /** nullable persistent field */
    private String transferMode;

    /** nullable persistent field */
    private Timestamp appDate;

    /** nullable persistent field */
    private String appOrgan;

    /** nullable persistent field */
    private String appPersonId;

    /** nullable persistent field */
    private String appPerson;

    /** nullable persistent field */
    private String nodeId;

    /** nullable persistent field */
    private String statusId;

    /** nullable persistent field */
    private Timestamp nodeDate;

    /** nullable persistent field */
    private Timestamp statusDate;

    /** nullable persistent field */
    private String appResult;

    /** nullable persistent field */
    private String lastActnOrgan;

    /** nullable persistent field */
    private String lastActnUserId;

    /** nullable persistent field */
    private String lastActnUser;

    /** nullable persistent field */
    private String currActnOrgan;

    /** nullable persistent field */
    private String currActnUserId;

    /** nullable persistent field */
    private String currActnUser;

    /** nullable persistent field */
    private String assignUserId;

    /** nullable persistent field */
    private String assignUser;
    
    private String pushMonitorEnd;

    /** full constructor */
    public TdscApp(String tdscAppId, String appType, String transferMode, Timestamp appDate, String appOrgan, String appPersonId, String appPerson, String nodeId, String statusId, Timestamp nodeDate, Timestamp statusDate, String appResult, String lastActnOrgan, String lastActnUserId, String lastActnUser, String currActnOrgan, String currActnUserId, String currActnUser, String assignUserId, String assignUser, String pushMonitorEnd) {
        this.tdscAppId = tdscAppId;
        this.appType = appType;
        this.transferMode = transferMode;
        this.appDate = appDate;
        this.appOrgan = appOrgan;
        this.appPersonId = appPersonId;
        this.appPerson = appPerson;
        this.nodeId = nodeId;
        this.statusId = statusId;
        this.nodeDate = nodeDate;
        this.statusDate = statusDate;
        this.appResult = appResult;
        this.lastActnOrgan = lastActnOrgan;
        this.lastActnUserId = lastActnUserId;
        this.lastActnUser = lastActnUser;
        this.currActnOrgan = currActnOrgan;
        this.currActnUserId = currActnUserId;
        this.currActnUser = currActnUser;
        this.assignUserId = assignUserId;
        this.assignUser = assignUser;
        this.pushMonitorEnd = pushMonitorEnd;
    }

    /** default constructor */
    public TdscApp() {
    }

    
    
    public String getPushMonitorEnd() {
		return pushMonitorEnd;
	}

	public void setPushMonitorEnd(String pushMonitorEnd) {
		this.pushMonitorEnd = pushMonitorEnd;
	}

	/** minimal constructor */
    public TdscApp(String tdscAppId) {
        this.tdscAppId = tdscAppId;
    }

    public String getTdscAppId() {
        return this.tdscAppId;
    }

    public void setTdscAppId(String tdscAppId) {
        this.tdscAppId = tdscAppId;
    }

    public String getAppType() {
        return this.appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getTransferMode() {
        return this.transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getAppOrgan() {
        return this.appOrgan;
    }

    public void setAppOrgan(String appOrgan) {
        this.appOrgan = appOrgan;
    }

    public String getAppPersonId() {
        return appPersonId;
    }
    
    public void setAppPersonId(String appPersonId) {
        this.appPersonId = appPersonId;
    }

    public String getAppPerson() {
        return this.appPerson;
    }

    public void setAppPerson(String appPerson) {
        this.appPerson = appPerson;
    }
    
    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getStatusId() {
        return this.statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Timestamp getAppDate() {
		return appDate;
	}

	public void setAppDate(Timestamp appDate) {
		this.appDate = appDate;
	}

	public Timestamp getNodeDate() {
		return nodeDate;
	}

	public void setNodeDate(Timestamp nodeDate) {
		this.nodeDate = nodeDate;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public String getAppResult() {
        return this.appResult;
    }

    public void setAppResult(String appResult) {
        this.appResult = appResult;
    }

    public String getLastActnOrgan() {
        return this.lastActnOrgan;
    }

    public void setLastActnOrgan(String lastActnOrgan) {
        this.lastActnOrgan = lastActnOrgan;
    }

    public String getLastActnUserId() {
        return lastActnUserId;
    }
    
    public void setLastActnUserId(String lastActnUserId) {
        this.lastActnUserId = lastActnUserId;
    }
    
    public String getLastActnUser() {
        return this.lastActnUser;
    }

    public void setLastActnUser(String lastActnUser) {
        this.lastActnUser = lastActnUser;
    }

    public String getCurrActnOrgan() {
        return this.currActnOrgan;
    }

    public void setCurrActnOrgan(String currActnOrgan) {
        this.currActnOrgan = currActnOrgan;
    }

    public String getCurrActnUserId() {
        return currActnUserId;
    }
    
    public void setCurrActnUserId(String currActnUserId) {
        this.currActnUserId = currActnUserId;
    }
    
    public String getCurrActnUser() {
        return this.currActnUser;
    }

    public void setCurrActnUser(String currActnUser) {
        this.currActnUser = currActnUser;
    }

    public String getAssignUserId() {
        return assignUserId;
    }
    
    public void setAssignUserId(String assignUserId) {
        this.assignUserId = assignUserId;
    }
    
    public String getAssignUser() {
        return this.assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("tdscAppId", getTdscAppId())
            .toString();
    }

}
