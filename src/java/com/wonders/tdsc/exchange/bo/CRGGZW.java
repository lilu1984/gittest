package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class CRGGZW implements Serializable {
	
	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** ���ù������� */
    private String crggzw;
    
    /** �ĵ���� */
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
