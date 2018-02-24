package com.wonders.tdsc.bo;

import java.io.Serializable;

public class TdscBlockRemisemoneyDefray implements Serializable {
	
	private String defrayId;
	
	private String blockId;
	
	private String payBatch;
	
	private String payProportion;
	
	private String payTime;

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getDefrayId() {
		return defrayId;
	}

	public void setDefrayId(String defrayId) {
		this.defrayId = defrayId;
	}

	public String getPayBatch() {
		return payBatch;
	}

	public void setPayBatch(String payBatch) {
		this.payBatch = payBatch;
	}

	public String getPayProportion() {
		return payProportion;
	}

	public void setPayProportion(String payProportion) {
		this.payProportion = payProportion;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	
}
