package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class CJGGZW   implements Serializable {
	
	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 成交公告正文 */
    private String cjggzw;
    
    /** 成交公告RecordId */
    private String resultRecordId;

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

	public String getCjggzw() {
		return cjggzw;
	}

	public void setCjggzw(String cjggzw) {
		this.cjggzw = cjggzw;
	}

	public String getResultRecordId() {
		return resultRecordId;
	}

	public void setResultRecordId(String resultRecordId) {
		this.resultRecordId = resultRecordId;
	}
}
