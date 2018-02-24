package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class JMRZG implements Serializable {

	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 是否通过审查	IF_PASS */
    private String ifPass ="1";

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getProjectGuid() {
		return projectGuid;
	}

	public void setProjectGuid(String projectGuid) {
		this.projectGuid = projectGuid;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getIfPass() {
		return ifPass;
	}

	public void setIfPass(String ifPass) {
		this.ifPass = ifPass;
	}
}
