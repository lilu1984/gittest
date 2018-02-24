package com.wonders.tdsc.bo.condition;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscBlockConInfoCondition extends BaseCondition {
	private String blockId;

	private String blockConId;

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getBlockConId() {
		return blockConId;
	}

	public void setBlockConId(String blockConId) {
		this.blockConId = blockConId;
	}

}
