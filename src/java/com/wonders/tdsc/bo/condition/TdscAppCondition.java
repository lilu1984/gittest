package com.wonders.tdsc.bo.condition;

import com.wonders.esframework.common.bo.BaseBO;


/** @author Hibernate CodeGenerator */
public class TdscAppCondition extends BaseBO{

    /** nullable persistent field */
    private String tdscAppId;

    /** nullable persistent field */
    private String appType;

    /** nullable persistent field */
    private String transferMode;

    /** nullable persistent field */
    private String appOrgan;

    /** nullable persistent field */
    private String appPersonId;

    /** nullable persistent field */
    private String nodeId;

    /** nullable persistent field */
    private String statusId;

    /** nullable persistent field */
    private String appResult;

    /** nullable persistent field */
    private String lastActnOrgan;

    /** nullable persistent field */
    private String lastActnUserId;

    /** nullable persistent field */
    private String currActnOrgan;

    /** nullable persistent field */
    private String currActnUserId;

    /** nullable persistent field */
    private String assignUserId;
    
    private String pushMonitorEnd;

    public String getPushMonitorEnd() {
		return pushMonitorEnd;
	}
	public void setPushMonitorEnd(String pushMonitorEnd) {
		this.pushMonitorEnd = pushMonitorEnd;
	}
	public String getAppOrgan() {
        return appOrgan;
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
    public String getAppResult() {
        return appResult;
    }
    public void setAppResult(String appResult) {
        this.appResult = appResult;
    }
    public String getAppType() {
        return appType;
    }
    public void setAppType(String appType) {
        this.appType = appType;
    }
    public String getAssignUserId() {
        return assignUserId;
    }
    public void setAssignUserId(String assignUserId) {
        this.assignUserId = assignUserId;
    }
    public String getCurrActnOrgan() {
        return currActnOrgan;
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
    public String getLastActnOrgan() {
        return lastActnOrgan;
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
    public String getNodeId() {
        return nodeId;
    }
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    public String getStatusId() {
        return statusId;
    }
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
    public String getTdscAppId() {
        return tdscAppId;
    }
    public void setTdscAppId(String tdscAppId) {
        this.tdscAppId = tdscAppId;
    }
    public String getTransferMode() {
        return transferMode;
    }
    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }
}
