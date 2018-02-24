package com.wonders.tdsc.bo;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscAppFlow extends BaseBO {
    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private String transferMode;

    /** nullable persistent field */
    private String resultId;

    /** nullable persistent field */
    private String textOpen;

    /** nullable persistent field */
    private SysUser user;
    
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getTransferMode() {
        return transferMode;
    }
    
    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }
    
    public String getResultId() {
        return resultId;
    }
    
    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
    
    public String getTextOpen() {
        return textOpen;
    }
    
    public void setTextOpen(String textOpen) {
        this.textOpen = textOpen;
    }
    
    public SysUser getUser() {
        return user;
    }
    
    public void setUser(SysUser user) {
        this.user = user;
    }
}
