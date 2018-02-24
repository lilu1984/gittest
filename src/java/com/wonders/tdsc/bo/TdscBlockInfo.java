package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscBlockInfo extends BaseBO {

	/** identifier field */
	private String blockId;

	/** nullable persistent field */
	private String landId;

	/** nullable persistent field */
	private String blockNoticeNo;

	/** nullable persistent field */
	private String blockName;

	/** nullable persistent field */
	private Long districtId;

	/** nullable persistent field */
	private String districtLinker;

	/** nullable persistent field */
	private String marketType;

	/** nullable persistent field */
	private String blockType;

	/** nullable persistent field */
	private String blockSubType;

	/** nullable persistent field */
	private String bizProgramDocNum;

	/** nullable persistent field */
	private String auditedNum;

	/** nullable persistent field */
	private Date auditedDate;

	/** nullable persistent field */
	private String batchFileNum;

	/** nullable persistent field */
	private String retrieveFileNum;

	/** nullable persistent field */
	private String reserveFileNum;

	/** nullable persistent field */
	private String landIdCollection;

	/** nullable persistent field */
	private String landLocation;

	/** nullable persistent field */
	private String rangeEast;

	/** nullable persistent field */
	private String rangeWest;

	/** nullable persistent field */
	private String rangeSouth;

	/** nullable persistent field */
	private String rangeNorth;

	/** nullable persistent field */
	private BigDecimal totalLandArea;

	/** nullable persistent field */
	private BigDecimal otherLandArea;

	/** nullable persistent field */
	private BigDecimal suppliedLandSum;

	/** nullable persistent field */
	private BigDecimal newConstructionArea;

	/** nullable persistent field */
	private BigDecimal totalBuildingArea;

	/** nullable persistent field */
	private BigDecimal aboveTerraBuilldingArea;

	/** nullable persistent field */
	private String layoutProperty;

	/** nullable persistent field */
	private String volumeRate;

	/** nullable persistent field */
	private String volumeRateBelow;

	/** nullable persistent field */
	private String density;

	/** nullable persistent field */
	private String densityMin;

	/** nullable persistent field */
	private String greeningRate;

	/** nullable persistent field */
	private String greeningRateCon;

	/** nullable persistent field */
	private String equipCondition;

	/** nullable persistent field */
	private String highLimit;

	/** nullable persistent field */
	private BigDecimal chamberAreaRatioLimit;

	/** nullable persistent field */
	private BigDecimal below90FlatRatio;

	/** nullable persistent field */
	private String priceComponent;

	/** nullable persistent field */
	private BigDecimal priceLimit;

	/** nullable persistent field */
	private Date transferDate;

	/** nullable persistent field */
	private Date reserveDate;

	/** nullable persistent field */
	private String tranGroundSitu;

	/** nullable persistent field */
	private Date startDate;

	/** nullable persistent field */
	private Date compDate;

	/** nullable persistent field */
	private BigDecimal otherProvNumber;

	/** nullable persistent field */
	private String attachMap;

	/** nullable persistent field */
	private String ownershipReport;

	/** nullable persistent field */
	private String planDoc;

	/** nullable persistent field */
	private String subInfo;

	/** nullable persistent field */
	private String tranResult;

	/** nullable persistent field */
	private Date resultDate;

	/** nullable persistent field */
	private BigDecimal resultPrice;

	/** nullable persistent field */
	private String resultCert;

	/** nullable persistent field */
	private String resultName;

	/** nullable persistent field */
	private String status;

	/** nullable persistent field */
	private String isValid;

	/** nullable persistent field */
	private String memo;

	/** nullable persistent field */
	private String programAuditedOrg;

	/** nullable persistent field */
	private String programNum;

	/** nullable persistent field */
	private String planAuditedOrg;

	/** nullable persistent field */
	private String planNum;

	/** nullable persistent field */
	private String landAuditedDoc;

	/** nullable persistent field */
	private String supplyCaseDoc;

	/** nullable persistent field */
	private String flatRate;

	/** nullable persistent field */
	private String currentSituationCondition;

	/** nullable persistent field */
	private String transferLife;

	/** weedlu 20071220 用于webservice对外提供数据 */
	private List usedInfoList;

	/** nullable persistent field */
	private String blockLandId;

	/** nullable persistent field */
	private Timestamp actionDateBlock;

	private String userId;

	private String countUse;

	private String buildingHeight;

	private String blockQuality;

	private String sellYear;

	private String buildingTime;

	private String peitao;

	private String buildingCondition;

	private String surrenderTime;

	private String otherBuildingCondition;

	// 城市基础设施配套费单价
	private BigDecimal cityFacilityFee;

	// 城市基础设施配套费总额
	private BigDecimal totalCityFacilityFee;

	// 教育设施配套费单价
	private BigDecimal educationFacilityFee;

	// 教育设施配套费总额
	private BigDecimal totalEducationFacilityFee;

	// 生活垃圾转运设施代建资金单价
	private BigDecimal rubbishTransportFee;

	// 生活垃圾转运设施代建资金总额
	private BigDecimal totalRubbishTransportFee;

	// 是否修改竞买资格及要求
	private String isModify;

	// 竞买资格及要求
	private String competeQualifications;

	// 法人或其他组织应提交文件
	private String unitFile;

	// 自然人应提交文件
	private String personalFile;

	// 保证金账户
	private String marginBank;

	// 账户名称
	private String accountName;

	// 土地开发程度
	private String blockDevDegree;

	// 地块投资额
	private BigDecimal blockInvestAmount;

	// 地块投资总额
	private BigDecimal blockInvestTotalAmount;

	// 实施建设规定
	private String blockBuildRule;

	// 企业注册资本
	private String regCapital;

	// 其它要求
	private String blockOtherReq;

	// 地质灾害评估费
	private BigDecimal geologicalHazard;

	// 境外自然人应提交文件
	private String jwPersonalFile;

	// 境外法人或其他组织应提交文件
	private String jwUnitFile;
	
	// 地质灾害评估单位
	private String geologyAssessUint;
	
	// 代收费用付款期限
	private String feePayTimes;
	
	public String getFeePayTimes() {
		return feePayTimes;
	}

	public void setFeePayTimes(String feePayTimes) {
		this.feePayTimes = feePayTimes;
	}

	public String getGeologyAssessUint() {
		return geologyAssessUint;
	}

	public void setGeologyAssessUint(String geologyAssessUint) {
		this.geologyAssessUint = geologyAssessUint;
	}

	public String getJwPersonalFile() {
		return jwPersonalFile;
	}

	public void setJwPersonalFile(String jwPersonalFile) {
		this.jwPersonalFile = jwPersonalFile;
	}

	public String getJwUnitFile() {
		return jwUnitFile;
	}

	public void setJwUnitFile(String jwUnitFile) {
		this.jwUnitFile = jwUnitFile;
	}

	public BigDecimal getGeologicalHazard() {
		return geologicalHazard;
	}

	public void setGeologicalHazard(BigDecimal geologicalHazard) {
		this.geologicalHazard = geologicalHazard;
	}

	public String getBlockOtherReq() {
		return blockOtherReq;
	}

	public void setBlockOtherReq(String blockOtherReq) {
		this.blockOtherReq = blockOtherReq;
	}

	public String getBlockDevDegree() {
		return blockDevDegree;
	}

	public void setBlockDevDegree(String blockDevDegree) {
		this.blockDevDegree = blockDevDegree;
	}

	public BigDecimal getBlockInvestAmount() {
		return blockInvestAmount;
	}

	public void setBlockInvestAmount(BigDecimal blockInvestAmount) {
		this.blockInvestAmount = blockInvestAmount;
	}

	public BigDecimal getBlockInvestTotalAmount() {
		return blockInvestTotalAmount;
	}

	public void setBlockInvestTotalAmount(BigDecimal blockInvestTotalAmount) {
		this.blockInvestTotalAmount = blockInvestTotalAmount;
	}

	public String getBlockBuildRule() {
		return blockBuildRule;
	}

	public void setBlockBuildRule(String blockBuildRule) {
		this.blockBuildRule = blockBuildRule;
	}

	public String getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
	}

	public List getUsedInfoList() {
		return usedInfoList;
	}

	public void setUsedInfoList(List usedInfoList) {
		this.usedInfoList = usedInfoList;
	}

	public String getCurrentSituationCondition() {
		return currentSituationCondition;
	}

	public void setCurrentSituationCondition(String currentSituationCondition) {
		this.currentSituationCondition = currentSituationCondition;
	}

	public String getFlatRate() {
		return flatRate;
	}

	public void setFlatRate(String flatRate) {
		this.flatRate = flatRate;
	}

	/** default constructor */
	public TdscBlockInfo() {
	}

	public Date getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getLandId() {
		return this.landId;
	}

	public void setLandId(String landId) {
		this.landId = landId;
	}

	public String getBlockNoticeNo() {
		return this.blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getBlockName() {
		return this.blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Long getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getDistrictLinker() {
		return this.districtLinker;
	}

	public void setDistrictLinker(String districtLinker) {
		this.districtLinker = districtLinker;
	}

	public String getMarketType() {
		return this.marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public String getBlockType() {
		return this.blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public String getBlockSubType() {
		return this.blockSubType;
	}

	public void setBlockSubType(String blockSubType) {
		this.blockSubType = blockSubType;
	}

	public String getBizProgramDocNum() {
		return this.bizProgramDocNum;
	}

	public void setBizProgramDocNum(String bizProgramDocNum) {
		this.bizProgramDocNum = bizProgramDocNum;
	}

	public String getAuditedNum() {
		return this.auditedNum;
	}

	public void setAuditedNum(String auditedNum) {
		this.auditedNum = auditedNum;
	}

	public Date getAuditedDate() {
		return this.auditedDate;
	}

	public void setAuditedDate(Date auditedDate) {
		this.auditedDate = auditedDate;
	}

	public String getBatchFileNum() {
		return this.batchFileNum;
	}

	public void setBatchFileNum(String batchFileNum) {
		this.batchFileNum = batchFileNum;
	}

	public String getRetrieveFileNum() {
		return this.retrieveFileNum;
	}

	public void setRetrieveFileNum(String retrieveFileNum) {
		this.retrieveFileNum = retrieveFileNum;
	}

	public String getReserveFileNum() {
		return this.reserveFileNum;
	}

	public void setReserveFileNum(String reserveFileNum) {
		this.reserveFileNum = reserveFileNum;
	}

	public String getLandIdCollection() {
		return this.landIdCollection;
	}

	public void setLandIdCollection(String landIdCollection) {
		this.landIdCollection = landIdCollection;
	}

	public String getLandLocation() {
		return this.landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public String getRangeEast() {
		return this.rangeEast;
	}

	public void setRangeEast(String rangeEast) {
		this.rangeEast = rangeEast;
	}

	public String getRangeWest() {
		return this.rangeWest;
	}

	public void setRangeWest(String rangeWest) {
		this.rangeWest = rangeWest;
	}

	public String getRangeSouth() {
		return this.rangeSouth;
	}

	public void setRangeSouth(String rangeSouth) {
		this.rangeSouth = rangeSouth;
	}

	public String getRangeNorth() {
		return this.rangeNorth;
	}

	public void setRangeNorth(String rangeNorth) {
		this.rangeNorth = rangeNorth;
	}

	public BigDecimal getTotalLandArea() {
		return this.totalLandArea;
	}

	public void setTotalLandArea(BigDecimal totalLandArea) {
		this.totalLandArea = totalLandArea;
	}

	public BigDecimal getOtherLandArea() {
		return this.otherLandArea;
	}

	public void setOtherLandArea(BigDecimal otherLandArea) {
		this.otherLandArea = otherLandArea;
	}

	public BigDecimal getSuppliedLandSum() {
		return this.suppliedLandSum;
	}

	public void setSuppliedLandSum(BigDecimal suppliedLandSum) {
		this.suppliedLandSum = suppliedLandSum;
	}

	public BigDecimal getNewConstructionArea() {
		return this.newConstructionArea;
	}

	public void setNewConstructionArea(BigDecimal newConstructionArea) {
		this.newConstructionArea = newConstructionArea;
	}

	public BigDecimal getTotalBuildingArea() {
		return this.totalBuildingArea;
	}

	public void setTotalBuildingArea(BigDecimal totalBuildingArea) {
		this.totalBuildingArea = totalBuildingArea;
	}

	public BigDecimal getAboveTerraBuilldingArea() {
		return this.aboveTerraBuilldingArea;
	}

	public void setAboveTerraBuilldingArea(BigDecimal aboveTerraBuilldingArea) {
		this.aboveTerraBuilldingArea = aboveTerraBuilldingArea;
	}

	public String getLayoutProperty() {
		return this.layoutProperty;
	}

	public void setLayoutProperty(String layoutProperty) {
		this.layoutProperty = layoutProperty;
	}

	public String getEquipCondition() {
		return this.equipCondition;
	}

	public void setEquipCondition(String equipCondition) {
		this.equipCondition = equipCondition;
	}

	public BigDecimal getChamberAreaRatioLimit() {
		return this.chamberAreaRatioLimit;
	}

	public void setChamberAreaRatioLimit(BigDecimal chamberAreaRatioLimit) {
		this.chamberAreaRatioLimit = chamberAreaRatioLimit;
	}

	public BigDecimal getBelow90FlatRatio() {
		return this.below90FlatRatio;
	}

	public void setBelow90FlatRatio(BigDecimal below90FlatRatio) {
		this.below90FlatRatio = below90FlatRatio;
	}

	public String getPriceComponent() {
		return this.priceComponent;
	}

	public void setPriceComponent(String priceComponent) {
		this.priceComponent = priceComponent;
	}

	public BigDecimal getPriceLimit() {
		return this.priceLimit;
	}

	public void setPriceLimit(BigDecimal priceLimit) {
		this.priceLimit = priceLimit;
	}

	public Date getTransferDate() {
		return this.transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public String getTranGroundSitu() {
		return this.tranGroundSitu;
	}

	public void setTranGroundSitu(String tranGroundSitu) {
		this.tranGroundSitu = tranGroundSitu;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCompDate() {
		return this.compDate;
	}

	public void setCompDate(Date compDate) {
		this.compDate = compDate;
	}

	public BigDecimal getOtherProvNumber() {
		return this.otherProvNumber;
	}

	public void setOtherProvNumber(BigDecimal otherProvNumber) {
		this.otherProvNumber = otherProvNumber;
	}

	public String getAttachMap() {
		return this.attachMap;
	}

	public void setAttachMap(String attachMap) {
		this.attachMap = attachMap;
	}

	public String getOwnershipReport() {
		return this.ownershipReport;
	}

	public void setOwnershipReport(String ownershipReport) {
		this.ownershipReport = ownershipReport;
	}

	public String getPlanDoc() {
		return this.planDoc;
	}

	public void setPlanDoc(String planDoc) {
		this.planDoc = planDoc;
	}

	public String getSubInfo() {
		return this.subInfo;
	}

	public void setSubInfo(String subInfo) {
		this.subInfo = subInfo;
	}

	public String getTranResult() {
		return this.tranResult;
	}

	public void setTranResult(String tranResult) {
		this.tranResult = tranResult;
	}

	public Date getResultDate() {
		return this.resultDate;
	}

	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}

	public BigDecimal getResultPrice() {
		return this.resultPrice;
	}

	public void setResultPrice(BigDecimal resultPrice) {
		this.resultPrice = resultPrice;
	}

	public String getResultCert() {
		return this.resultCert;
	}

	public void setResultCert(String resultCert) {
		this.resultCert = resultCert;
	}

	public String getResultName() {
		return this.resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getProgramAuditedOrg() {
		return this.programAuditedOrg;
	}

	public void setProgramAuditedOrg(String programAuditedOrg) {
		this.programAuditedOrg = programAuditedOrg;
	}

	public String getProgramNum() {
		return this.programNum;
	}

	public void setProgramNum(String programNum) {
		this.programNum = programNum;
	}

	public String getPlanAuditedOrg() {
		return this.planAuditedOrg;
	}

	public void setPlanAuditedOrg(String planAuditedOrg) {
		this.planAuditedOrg = planAuditedOrg;
	}

	public String getPlanNum() {
		return this.planNum;
	}

	public void setPlanNum(String planNum) {
		this.planNum = planNum;
	}

	public String getLandAuditedDoc() {
		return this.landAuditedDoc;
	}

	public void setLandAuditedDoc(String landAuditedDoc) {
		this.landAuditedDoc = landAuditedDoc;
	}

	public String getSupplyCaseDoc() {
		return this.supplyCaseDoc;
	}

	public void setSupplyCaseDoc(String supplyCaseDoc) {
		this.supplyCaseDoc = supplyCaseDoc;
	}

	public void setVolumeRate(String volumeRate) {
		this.volumeRate = volumeRate;
	}

	public String getVolumeRate() {
		return volumeRate;
	}

	public void setGreeningRate(String greeningRate) {
		this.greeningRate = greeningRate;
	}

	public String getGreeningRate() {
		return greeningRate;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public void setDensityMin(String densityMin) {
		this.densityMin = densityMin;
	}

	public void setGreeningRateCon(String greeningRateCon) {
		this.greeningRateCon = greeningRateCon;
	}

	public void setHighLimit(String highLimit) {
		this.highLimit = highLimit;
	}

	public void setVolumeRateBelow(String volumeRateBelow) {
		this.volumeRateBelow = volumeRateBelow;
	}

	public String getDensity() {
		return density;
	}

	public String getDensityMin() {
		return densityMin;
	}

	public String getGreeningRateCon() {
		return greeningRateCon;
	}

	public String getHighLimit() {
		return highLimit;
	}

	public String getVolumeRateBelow() {
		return volumeRateBelow;
	}

	public String getTransferLife() {
		return transferLife;
	}

	public void setTransferLife(String transferLife) {
		this.transferLife = transferLife;
	}

	public String getBlockLandId() {
		return blockLandId;
	}

	public void setBlockLandId(String blockLandId) {
		this.blockLandId = blockLandId;
	}

	public Timestamp getActionDateBlock() {
		return actionDateBlock;
	}

	public void setActionDateBlock(Timestamp actionDateBlock) {
		this.actionDateBlock = actionDateBlock;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBlockQuality() {
		return blockQuality;
	}

	public void setBlockQuality(String blockQuality) {
		this.blockQuality = blockQuality;
	}

	public String getBuildingCondition() {
		return buildingCondition;
	}

	public void setBuildingCondition(String buildingCondition) {
		this.buildingCondition = buildingCondition;
	}

	public String getBuildingHeight() {
		return buildingHeight;
	}

	public void setBuildingHeight(String buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public String getBuildingTime() {
		return buildingTime;
	}

	public void setBuildingTime(String buildingTime) {
		this.buildingTime = buildingTime;
	}

	public String getCountUse() {
		return countUse;
	}

	public void setCountUse(String countUse) {
		this.countUse = countUse;
	}

	public String getPeitao() {
		return peitao;
	}

	public void setPeitao(String peitao) {
		this.peitao = peitao;
	}

	public String getSellYear() {
		return sellYear;
	}

	public void setSellYear(String sellYear) {
		this.sellYear = sellYear;
	}

	public String getSurrenderTime() {
		return surrenderTime;
	}

	public void setSurrenderTime(String surrenderTime) {
		this.surrenderTime = surrenderTime;
	}

	public String getOtherBuildingCondition() {
		return otherBuildingCondition;
	}

	public void setOtherBuildingCondition(String otherBuildingCondition) {
		this.otherBuildingCondition = otherBuildingCondition;
	}

	public BigDecimal getCityFacilityFee() {
		return cityFacilityFee;
	}

	public void setCityFacilityFee(BigDecimal cityFacilityFee) {
		this.cityFacilityFee = cityFacilityFee;
	}

	public BigDecimal getEducationFacilityFee() {
		return educationFacilityFee;
	}

	public void setEducationFacilityFee(BigDecimal educationFacilityFee) {
		this.educationFacilityFee = educationFacilityFee;
	}

	public BigDecimal getRubbishTransportFee() {
		return rubbishTransportFee;
	}

	public void setRubbishTransportFee(BigDecimal rubbishTransportFee) {
		this.rubbishTransportFee = rubbishTransportFee;
	}

	public BigDecimal getTotalCityFacilityFee() {
		return totalCityFacilityFee;
	}

	public void setTotalCityFacilityFee(BigDecimal totalCityFacilityFee) {
		this.totalCityFacilityFee = totalCityFacilityFee;
	}

	public BigDecimal getTotalEducationFacilityFee() {
		return totalEducationFacilityFee;
	}

	public void setTotalEducationFacilityFee(BigDecimal totalEducationFacilityFee) {
		this.totalEducationFacilityFee = totalEducationFacilityFee;
	}

	public BigDecimal getTotalRubbishTransportFee() {
		return totalRubbishTransportFee;
	}

	public void setTotalRubbishTransportFee(BigDecimal totalRubbishTransportFee) {
		this.totalRubbishTransportFee = totalRubbishTransportFee;
	}

	public String getCompeteQualifications() {
		return competeQualifications;
	}

	public void setCompeteQualifications(String competeQualifications) {
		this.competeQualifications = competeQualifications;
	}

	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

	public String getPersonalFile() {
		return personalFile;
	}

	public void setPersonalFile(String personalFile) {
		this.personalFile = personalFile;
	}

	public String getUnitFile() {
		return unitFile;
	}

	public void setUnitFile(String unitFile) {
		this.unitFile = unitFile;
	}

	public String getMarginBank() {
		return marginBank;
	}

	public void setMarginBank(String marginBank) {
		this.marginBank = marginBank;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
