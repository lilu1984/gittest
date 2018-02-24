package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TdscBlockQqjcInfo implements Serializable {
    //前期监察id
    private String qqjcInfoId;
    //地块ID
    private String blockId;
    //是否为农田 IF_FARMLAND
    private String ifFarmLand;
    //是否农转用 IF_FARM_TO_USED
    private String ifFarmToUsed;
    //土地供地方式 GONGDI_TYPE
    private String gongDiType;
    //供地批文号 GONGDI_NUM
    private String gongDiNum;
    //是否土地预审 IF_BLOCK_PRECOGNITION
    private String ifBlockPrecognition;
    //是否建设项目立项 IF_PROJECT_BUILD
    private String ifProgectBuild;
    //是否有规划审批手续 IF_HAS_PROCEDURES
    private String ifHasProcedures;
    //政府同意出让批文号 GOV_GRANT_NUM
    private String govGrantNum;
    //出让方案是否合规 IF_GRANT_CRFA
    private String ifGrantCrfa;
    //评估机构资质 PG_ORG_GRADE
    private String pgOrgGrade;
    //是否集体会审 IF_GROUP_ASSESS
    private String ifGroupAssess;
    //评估会审价格（加密）UPSET_PRICE
    private BigDecimal upsetPrice;
    //评估机构名称 PG_ORG_NAME
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
