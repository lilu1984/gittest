package com.wonders.tdsc.bo.condition;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscBlockInfoCondition extends BaseCondition {

    private String blockName;

    private String transferMode;

    private String[] status;
    
    private Long districtId;

    private String blockType;

    private String blockId;
    
    private String auditedNum;

    private String userId;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuditedNum() {
		return auditedNum;
	}

	public void setAuditedNum(String auditedNum) {
		this.auditedNum = auditedNum;
	}

	public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }
    
    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}


}
