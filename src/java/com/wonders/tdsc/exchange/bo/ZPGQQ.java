package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class ZPGQQ implements Serializable {
	
	/** ����(�Խ�) */
    private String guid;
    
    /** ��Ŀ�������Խ���	PROJECT_GUID */
    private String projectGuid;
    
    /** ���ر�� */
    private String blockId;
    
    /** �Ƿ�Ϊũ��	IF_FARMLAND */
    private String ifFarmland;
    
    /** �Ƿ�ũת��	IF_FARM_TO_USED */
    private String ifFarmToUsed;
    
    /** ���ع��ط�ʽ	GONGDI_TYPE */
    private String gongdiType;
    
    /** �������ĺ�	AUDITED_NUM */
    private String auditedNum;
    
    /** �Ƿ��й滮��������	IF_HAS_PROCEDURES */
    private String ifHasProcedures;
    
    /** ���÷����Ƿ�Ϲ淶	IF_GRANT_CRFA */
    private String ifGrantCrfa;
    
    /** �Ƿ������	IF_GROUP_ASSESS */
    private String ifGroupAssess;
    
    /** ������������	PG_ORG_NAME */
    private String pgOrgName;

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

	public String getIfFarmland() {
		return ifFarmland;
	}

	public void setIfFarmland(String ifFarmland) {
		this.ifFarmland = ifFarmland;
	}

	public String getIfFarmToUsed() {
		return ifFarmToUsed;
	}

	public void setIfFarmToUsed(String ifFarmToUsed) {
		this.ifFarmToUsed = ifFarmToUsed;
	}

	public String getGongdiType() {
		return gongdiType;
	}

	public void setGongdiType(String gongdiType) {
		this.gongdiType = gongdiType;
	}

	public String getAuditedNum() {
		return auditedNum;
	}

	public void setAuditedNum(String auditedNum) {
		this.auditedNum = auditedNum;
	}

	public String getIfHasProcedures() {
		return ifHasProcedures;
	}

	public void setIfHasProcedures(String ifHasProcedures) {
		this.ifHasProcedures = ifHasProcedures;
	}

	public String getIfGrantCrfa() {
		return ifGrantCrfa;
	}

	public void setIfGrantCrfa(String ifGrantCrfa) {
		this.ifGrantCrfa = ifGrantCrfa;
	}

	public String getIfGroupAssess() {
		return ifGroupAssess;
	}

	public void setIfGroupAssess(String ifGroupAssess) {
		this.ifGroupAssess = ifGroupAssess;
	}

	public String getPgOrgName() {
		return pgOrgName;
	}

	public void setPgOrgName(String pgOrgName) {
		this.pgOrgName = pgOrgName;
	}
}
