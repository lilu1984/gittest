package com.wonders.tdsc.bo.condition;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscBlockTranCondition extends BaseCondition{
	private String blockName;
	private String blockNoticeNo;
	private String transferMode;
	private String districtName;
	private String tranResult;

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getTranResult() {
		return tranResult;
	}

	public void setTranResult(String tranResult) {
		this.tranResult = tranResult;
	}


}
