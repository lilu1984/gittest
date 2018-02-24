package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TdscBlockQqjcInfo implements Serializable {
    //ǰ�ڼ��id
    private String qqjcInfoId;
    //�ؿ�ID
    private String blockId;
    //�Ƿ�Ϊũ�� IF_FARMLAND
    private String ifFarmLand;
    //�Ƿ�ũת�� IF_FARM_TO_USED
    private String ifFarmToUsed;
    //���ع��ط�ʽ GONGDI_TYPE
    private String gongDiType;
    //�������ĺ� GONGDI_NUM
    private String gongDiNum;
    //�Ƿ�����Ԥ�� IF_BLOCK_PRECOGNITION
    private String ifBlockPrecognition;
    //�Ƿ�����Ŀ���� IF_PROJECT_BUILD
    private String ifProgectBuild;
    //�Ƿ��й滮�������� IF_HAS_PROCEDURES
    private String ifHasProcedures;
    //����ͬ��������ĺ� GOV_GRANT_NUM
    private String govGrantNum;
    //���÷����Ƿ�Ϲ� IF_GRANT_CRFA
    private String ifGrantCrfa;
    //������������ PG_ORG_GRADE
    private String pgOrgGrade;
    //�Ƿ������ IF_GROUP_ASSESS
    private String ifGroupAssess;
    //��������۸񣨼��ܣ�UPSET_PRICE
    private BigDecimal upsetPrice;
    //������������ PG_ORG_NAME
    private String pgOrgName;
    
    
    public TdscBlockQqjcInfo()
    {}

	public String getQqjcInfoId() {
		return qqjcInfoId;
	}

	public void setQqjcInfoId(String qqjcInfoId) {
		this.qqjcInfoId = qqjcInfoId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getIfFarmLand() {
		return ifFarmLand;
	}

	public void setIfFarmLand(String ifFarmLand) {
		this.ifFarmLand = ifFarmLand;
	}

	public String getIfFarmToUsed() {
		return ifFarmToUsed;
	}

	public void setIfFarmToUsed(String ifFarmToUsed) {
		this.ifFarmToUsed = ifFarmToUsed;
	}

	public String getGongDiType() {
		return gongDiType;
	}

	public void setGongDiType(String gongDiType) {
		this.gongDiType = gongDiType;
	}

	public String getGongDiNum() {
		return gongDiNum;
	}

	public void setGongDiNum(String gongDiNum) {
		this.gongDiNum = gongDiNum;
	}

	public String getIfBlockPrecognition() {
		return ifBlockPrecognition;
	}

	public void setIfBlockPrecognition(String ifBlockPrecognition) {
		this.ifBlockPrecognition = ifBlockPrecognition;
	}

	public String getIfProgectBuild() {
		return ifProgectBuild;
	}

	public void setIfProgectBuild(String ifProgectBuild) {
		this.ifProgectBuild = ifProgectBuild;
	}

	public String getIfHasProcedures() {
		return ifHasProcedures;
	}

	public void setIfHasProcedures(String ifHasProcedures) {
		this.ifHasProcedures = ifHasProcedures;
	}

	public String getGovGrantNum() {
		return govGrantNum;
	}

	public void setGovGrantNum(String govGrantNum) {
		this.govGrantNum = govGrantNum;
	}

	public String getIfGrantCrfa() {
		return ifGrantCrfa;
	}

	public void setIfGrantCrfa(String ifGrantCrfa) {
		this.ifGrantCrfa = ifGrantCrfa;
	}

	public String getPgOrgGrade() {
		return pgOrgGrade;
	}

	public void setPgOrgGrade(String pgOrgGrade) {
		this.pgOrgGrade = pgOrgGrade;
	}

	public String getIfGroupAssess() {
		return ifGroupAssess;
	}

	public void setIfGroupAssess(String ifGroupAssess) {
		this.ifGroupAssess = ifGroupAssess;
	}

	public BigDecimal getUpsetPrice() {
		return upsetPrice;
	}

	public void setUpsetPrice(BigDecimal upsetPrice) {
		this.upsetPrice = upsetPrice;
	}

	public String getPgOrgName() {
		return pgOrgName;
	}

	public void setPgOrgName(String pgOrgName) {
		this.pgOrgName = pgOrgName;
	}
}
