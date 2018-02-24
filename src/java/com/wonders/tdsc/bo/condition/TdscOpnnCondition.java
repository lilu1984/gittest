package com.wonders.tdsc.bo.condition;

import java.math.BigDecimal;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscOpnnCondition extends BaseCondition {
    private String appId;
    
    private BigDecimal actionNum;

    private String actionOrgan;

    private String actionUser;

    private String nodeId;
    
    private String ifOpt;
    //ÅÐ¶ÏactionNumÊÇ·ñÎª¿Õ
    private boolean isnull;
    
    public BigDecimal getActionNum() {
        return actionNum;
    }
    
    public void setActionNum(BigDecimal actionNum) {
        this.actionNum = actionNum;
    }
    
    public String getActionOrgan() {
        return actionOrgan;
    }
    
    public void setActionOrgan(String actionOrgan) {
        this.actionOrgan = actionOrgan;
    }
    
    public String getActionUser() {
        return actionUser;
    }
    
    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public String getIfOpt() {
        return ifOpt;
    }
    
    public void setIfOpt(String ifOpt) {
        this.ifOpt = ifOpt;
    }

    public boolean getIsnull() {
        return isnull;
    }

    public void setIsnull(boolean isnull) {
        this.isnull = isnull;
    }
}