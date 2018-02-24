package com.wonders.tdsc.bo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscAppWorkflowInstanceRel implements Serializable {

    /** identifier field */
    private String processInstanceId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private String businessCodeId;

    /** full constructor */
    public TdscAppWorkflowInstanceRel(String processInstanceId, String appId, String businessCodeId) {
        this.processInstanceId = processInstanceId;
        this.appId = appId;
        this.businessCodeId = businessCodeId;
    }

    /** default constructor */
    public TdscAppWorkflowInstanceRel() {
    }

    /** minimal constructor */
    public TdscAppWorkflowInstanceRel(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getBusinessCodeId() {
        return businessCodeId;
    }
    
    public void setBusinessCodeId(String businessCodeId) {
        this.businessCodeId = businessCodeId;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("processInstanceId", getProcessInstanceId())
            .toString();
    }

}
