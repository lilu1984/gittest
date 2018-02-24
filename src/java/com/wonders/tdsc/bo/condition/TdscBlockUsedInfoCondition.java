package com.wonders.tdsc.bo.condition;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscBlockUsedInfoCondition extends BaseCondition {
	private String blockUsedId;

	private String blockId;

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getBlockUsedId() {
		return blockUsedId;
	}

	public void setBlockUsedId(String blockUsedId) {
		this.blockUsedId = blockUsedId;
	}

}
