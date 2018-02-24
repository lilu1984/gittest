package com.wonders.tdsc.bo.condition;

import java.math.BigDecimal;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscBlockMaterialCondition extends BaseCondition {
	private String appId;

	private String materialId;

	private BigDecimal materialNum;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public BigDecimal getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(BigDecimal materialNum) {
		this.materialNum = materialNum;
	}

}
