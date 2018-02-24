package com.wonders.tdsc.bo.condition;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscBusinessRecordCondition extends BaseCondition {

    /** nullable persistent field */
    private String userId;

    /** nullable persistent field */
    private String appId;
    
    /** nullable persistent field */
    private Integer regionId;

    /** nullable persistent field */
    private String busiType;

    /** nullable persistent field */
    private String busiId;

    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
