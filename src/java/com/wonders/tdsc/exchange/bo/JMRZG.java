package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class JMRZG implements Serializable {

	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** �Ƿ�ͨ�����	IF_PASS */
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
