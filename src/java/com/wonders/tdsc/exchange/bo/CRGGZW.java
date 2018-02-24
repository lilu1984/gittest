package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class CRGGZW implements Serializable {
	
	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 出让公告正文 */
    private String crggzw;
    
    /** 文档编号 */
    private String fileRecordId;
    

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

	public String getCrggzw() {
		return crggzw;
	}

	public void setCrggzw(String crggzw) {
		this.crggzw = crggzw;
	}

	public String getFileRecordId() {
		return fileRecordId;
	}

	public void setFileRecordId(String fileRecordId) {
		this.fileRecordId = fileRecordId;
	}

}
