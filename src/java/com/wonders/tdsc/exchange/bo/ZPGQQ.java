package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;

public class ZPGQQ implements Serializable {
	
	/** 主键(自建) */
    private String guid;
    
    /** 项目主键（自建）	PROJECT_GUID */
    private String projectGuid;
    
    /** 土地编号 */
    private String blockId;
    
    /** 是否为农田	IF_FARMLAND */
    private String ifFarmland;
    
    /** 是否农转用	IF_FARM_TO_USED */
    private String ifFarmToUsed;
    
    /** 土地供地方式	GONGDI_TYPE */
    private String gongdiType;
    
    /** 供地批文号	AUDITED_NUM */
    private String auditedNum;
    
    /** 是否有规划审批手续	IF_HAS_PROCEDURES */
    private String ifHasProcedures;
    
    /** 出让方案是否合规范	IF_GRANT_CRFA */
    private String ifGrantCrfa;
    
    /** 是否集体会审	IF_GROUP_ASSESS */
    private String ifGroupAssess;
    
    /** 评估机构名称	PG_ORG_NAME */
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
