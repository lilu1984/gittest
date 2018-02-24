package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class CRWJZW  implements Serializable {
	
	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 出让文件正文 */
    private String crwjzw;
    
    /** NOTICE_RECORD_ID */
    private String noticeRecordId;

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

	public String getCrwjzw() {
		return crwjzw;
	}

	public void setCrwjzw(String crwjzw) {
		this.crwjzw = crwjzw;
	}

	public String getNoticeRecordId() {
		return noticeRecordId;
	}

	public void setNoticeRecordId(String noticeRecordId) {
		this.noticeRecordId = noticeRecordId;
	}
}
