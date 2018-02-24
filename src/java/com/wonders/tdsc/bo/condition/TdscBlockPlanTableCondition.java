package com.wonders.tdsc.bo.condition;

import java.util.Date;
import java.util.List;

import com.wonders.esframework.common.bo.BaseCondition;

/** @author Hibernate CodeGenerator */
public class TdscBlockPlanTableCondition extends BaseCondition {

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private Integer planNum;

    /** nullable persistent field */
    private String status;

    /** nullable persistent field */
    private String plusCondition;
    
	//查询条件：年
	private String conditionYear;

	//查询条件：月
    private String conditionMonth;

    /** nullable persistent field */
    private String planId;

    /** nullable persistent field */
    private String blockName;

    /** nullable persistent field */
    private String uniteBlockCode;

    /** nullable persistent field */
    private String ifPCommit;
    
    private String transferMode;
    
    //招拍挂编号
    private String tradeNum;
    
    private List statusFlowList;
    
    
    /** nullable persistent field */
    private String landLocation;
    
    private String userId;
    
    private String countUse;
    
    private String blockQuality;
    
    private Date resultDate;
    
    private Long districtId;
    
    private Date resultDate1;
    
    public Date getResultDate1() {
		return resultDate1;
	}

	public void setResultDate1(Date resultDate1) {
		this.resultDate1 = resultDate1;
	}
    
    public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getBlockQuality() {
		return blockQuality;
	}

	public void setBlockQuality(String blockQuality) {
		this.blockQuality = blockQuality;
	}

	public String getCountUse() {
		return countUse;
	}

	public void setCountUse(String countUse) {
		this.countUse = countUse;
	}

	public Date getResultDate() {
		return resultDate;
	}

	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}

    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
    public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getIfPCommit() {
		return ifPCommit;
	}

	public void setIfPCommit(String ifPCommit) {
		this.ifPCommit = ifPCommit;
	}

	public String getUniteBlockCode() {
		return uniteBlockCode;
	}

	public void setUniteBlockCode(String uniteBlockCode) {
		this.uniteBlockCode = uniteBlockCode;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getConditionMonth() {
		return conditionMonth;
	}

	public void setConditionMonth(String conditionMonth) {
		this.conditionMonth = conditionMonth;
	}

	public String getConditionYear() {
		return conditionYear;
	}

	public void setConditionYear(String conditionYear) {
		this.conditionYear = conditionYear;
	}

	public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getPlanNum() {
        return this.planNum;
    }

    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlusCondition() {
        return plusCondition;
    }

    public void setPlusCondition(String plusCondition) {
        this.plusCondition = plusCondition;
    }

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public List getStatusFlowList() {
		return statusFlowList;
	}

	public void setStatusFlowList(List statusFlowList) {
		this.statusFlowList = statusFlowList;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}
    
}