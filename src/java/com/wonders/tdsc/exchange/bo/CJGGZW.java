package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class CJGGZW   implements Serializable {
	
	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** �ɽ��������� */
    private String cjggzw;
    
    /** �ɽ�����RecordId */
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
