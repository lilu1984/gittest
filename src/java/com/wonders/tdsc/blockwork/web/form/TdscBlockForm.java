package com.wonders.tdsc.blockwork.web.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.omg.CORBA.PRIVATE_MEMBER;

public class TdscBlockForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	//附件名称
	private String[] accessoryName;


	public String[] getAccessoryName() {
		return accessoryName;
	}

	public void setAccessoryName(String[] accessoryName) {
		this.accessoryName = accessoryName;
	}
	// 容积率说明
	private String volumeRateMemo;

	// 代收费用付款期限
	private String feePayTimes;

	// 起始价含费用项
	private String contain;

	// 起始价不含费用项
	private String notContain;

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

	private BigDecimal geologicalHazard;

	// 境外自然人应提交文件
	private String jwPersonalFile;

	// 境外法人或其他组织应提交文件
	private String jwUnitFile;

	// 地质灾害评估单位
	private String geologyAssessUint;

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

	public String getBlockOtherReq() {
		return blockOtherReq;
	}

	public void setBlockOtherReq(String blockOtherReq) {
		this.blockOtherReq = blockOtherReq;
	}

	/** nullable persistent field */
	private String aboveTerraBuilldingArea;

	/** nullable persistent field */
	private String abroad3yearProfit;

	/** nullable persistent field */
	private BigDecimal abroadBankDeposit;

	/** nullable persistent field */
	private String abroadCreditLevel;

	/** nullable persistent field */
	private String abroadJointPercent;

	/** nullable persistent field */
	private String abroadOwnerComp;

	/** nullable persistent field */
	private String abroadSameTypeTime;

	/** nullable persistent field */
	private String accessProductType;

	// 账户名称
	private String accountName;

	/** nullable persistent field */
	private BigDecimal addPriceRange;

	/** nullable persistent field */
	private String allocationCompensation;

	/** identifier field */
	private String appId;

	/** nullable persistent field */
	private String appSupNum;

	// 经适房配建规划建筑面积(平方米)
	private BigDecimal areaBuildJjf;

	// 廉租房配建规划建筑面积(平方米)
	private BigDecimal areaBuildLzf;

	// 限价房规划建筑面积(平方米)
	private BigDecimal areaBuildXjf;

	// 经适房配建土地面积(平方米)
	private BigDecimal areaLandJjf;

	// 限价房土地面积(平方米)
	private BigDecimal areaLandLjf;

	// 廉租房配建土地面积(平方米)
	private BigDecimal areaLandLzf;

	/** nullable persistent field */
	private String attachMap;

	/** nullable persistent field */
	private Date auditedDate;

	/** nullable persistent field */
	private String auditedNum;

	// 供地批文号1
	private String auditedNum1;

	// 供地批文号2
	private String auditedNum2;

	/** nullable persistent field */
	private String batchFileNum;

	// ---BlockInfo土地基本信息----
	/** identifier field */
	private BigDecimal below90FlatRatio;

	private String bidderId;

	private String bidderPersonId;

	// 竞买人详细信息 List<TdscBidderForm>
	private List bidders;

	/** nullable persistent field */
	private String bizProgramDocNum;

	// 土地面积
	private BigDecimal blockArea;

	// 子地块土地面积
	private String[] blockAreas;

	// 入室审核当前状态
	private String blockAuditStatus;

	private String blockCatalog;

	// 地块编号
	private String blockCode;

	// 地块编号
	private String[] blockCodes;

	// -------TdscBlockConInfo
	/** identifier field */
	private String blockConId;

	// 地块详细用途
	private String blockDetailedUsed;

	/** identifier field */
	private String blockId;

	// ---------BlockTranApp----------
	/** identifier field */
	private String blockLandId;

	/** nullable persistent field */
	private String blockName;

	/** nullable persistent field */
	private String blockNoticeNo;

	private String blockQuality;

	/** nullable persistent field */
	private String blockSubType;

	/** nullable persistent field */
	private String blockType;

	// ----------BlockUsedInfo---------
	/** identifier field */
	private String blockUsedId;

	private String buildingCondition;

	private String buildingHeight;

	private String buildingTime;

	// 是否是租赁模式
	private String isZulin;
	
	
	public String getIsZulin() {
		return isZulin;
	}

	public void setIsZulin(String isZulin) {
		this.isZulin = isZulin;
	}
	// 备注 BZ_BEIZHU
	private String bzBeizhu;

	// 土地出让价款交纳要求 BZ_CRJKJNYQ
	private String bzCrjkjnyq;

	// 动工及竣工时间 BZ_DGJJGSJ
	private String bzDgjjgsj;

	// 地块其他说明 BZ_DKQTSM
	private String bzDkqtsm;

	// 规划设计要点 BZ_GHSJYD
	private String bzGhsjyd;

	// 挂牌手续费 BZ_GPSXF
	private String bzGpsxf;

	// 竞买资格及要求 BZ_JMZGJYQ
	private String bzJmzgjyq;

	// 拍卖佣金 BZ_PMYJ
	private String bzPmyj;

	// 市政配套条件 BZ_SZPTTJ
	private String bzSzpttj;

	// 土地出让附件 BZ_TDCRFJ
	private String bzTdcrfj;

	// 土地交付条件 BZ_TDJFTJ
	private String bzTdjftj;

	// 协办机构 BZ_XBJG
	private String bzXbjg;

	/** nullable persistent field */
	private String cblockId;

	/** nullable persistent field */
	private BigDecimal chamberAreaRatioLimit;

	// 城市基础设施配套费单价
	private BigDecimal cityFacilityFee;

	// 规划设计要点，clob字段，保存在TdscEsClob表中（ID：TdscBlockTranApp表的bzGhsjyd字段）
	private String clobContent;

	/** nullable persistent field */
	private Date compDate;

	// 竞买资格及要求
	private String competeQualifications;

	/** nullable persistent field */
	private String contractNum;

	/** nullable persistent field */
	private String contractSignedDate;

	/** nullable persistent field */
	private String copyNumber;

	// 经适房配建套数
	private Integer countJjsyf;

	// 廉租房配建套数
	private Integer countLzfpj;

	// 购买人数
	private String countSalePerson;

	private String countUse;

	/** nullable persistent field */
	private String currentSituationCondition;

	// 当前最高价人号牌
	private String CurrMaxPersonNum;

	// 当前最高报价
	private String currMaxPrice;

	/** nullable persistent field */
	private String demolishCompesationMethod;

	/** nullable persistent field */
	private String density;

	/** nullable persistent field */
	private String densityMin;

	// 规划建筑密度
	private String[] densitys;

	// 建筑密度符号
	private String[] densitySigns;

	/** nullable persistent field */
	private String depositeBalance;

	/** nullable persistent field */
	private String depositeDate;

	/** nullable persistent field */
	private String depositSum;

	/** nullable persistent field */
	private Long districtId;

	/** nullable persistent field */
	private String districtLinker;

	/** nullable persistent field */
	private String domestic3yearProfit;

	/** nullable persistent field */
	private String domesticCreditLevel;

	/** nullable persistent field */
	private String domesticJointPercent;

	/** nullable persistent field */
	private String domesticOwnerComp;

	/** nullable persistent field */
	private String domesticSameTypeTime;

	// 教育设施配套费单价
	private BigDecimal educationFacilityFee;

	/** nullable persistent field */
	private BigDecimal employeeNum;

	/** nullable persistent field */
	private String endorseDistrict;

	/** nullable persistent field */
	private String energyCost;

	/** nullable persistent field */
	private String equipCondition;

	// 评估楼面地价
	private BigDecimal evaluteFloorLandValue;

	private FormFile[] fileName;

	/** nullable persistent field */
	private String fixInvestAmount;

	/** nullable persistent field */
	private String flatRate;

	// 供地批文号 GONGDI_NUM
	private String gongDiNum;

	// 土地供地方式 GONGDI_TYPE
	private String gongDiType;

	// 政府同意出让批文号 GOV_GRANT_NUM
	private String govGrantNum;

	/** nullable persistent field */
	private String greeningRate;

	private String greeningRate2;

	private String[] greeningRates2;

	private String density2;

	private String[] densitys2;

	public String[] getDensitys2() {
		return densitys2;
	}

	public void setDensitys2(String[] densitys2) {
		this.densitys2 = densitys2;
	}

	public String getGreeningRate2() {
		return greeningRate2;
	}

	public void setGreeningRate2(String greeningRate2) {
		this.greeningRate2 = greeningRate2;
	}

	public String[] getGreeningRates2() {
		return greeningRates2;
	}

	public void setGreeningRates2(String[] greeningRates2) {
		this.greeningRates2 = greeningRates2;
	}

	/** nullable persistent field */
	private String greeningRateCon;

	// 绿地率
	private String[] greeningRates;

	// 绿地率符号
	private String[] greeningRateSigns;

	private String guihuaCertNo;

	/** nullable persistent field */
	private String[] gy_appIds;

	/** nullable persistent field */
	private String[] gy_blockIds;

	/** nullable persistent field */
	private String[] gy_fileUrl;

	/** nullable persistent field */
	private String[] gy_issueType;

	/** nullable persistent field */
	private String[] gy_issueUnit;

	/** nullable persistent field */
	private String[] gy_issueUnitMemo;

	// --------BlockMaterial---------
	/** identifier field */
	private String[] gy_materialId;

	private String[] gy_materialName;

	/** nullable persistent field */
	private String[] gy_materialNum;

	/** nullable persistent field */
	private String[] gy_materialNumb;

	/** nullable persistent field */
	private String[] gy_materialType;

	/** nullable persistent field */
	private String[] gy_memo;

	/** nullable persistent field */
	private String highLimit;

	// 是否土地预审 IF_BLOCK_PRECOGNITION
	private String ifBlockPrecognition;

	// 是否为农田 IF_FARMLAND
	private String ifFarmLand;

	// 是否农转用 IF_FARM_TO_USED
	private String ifFarmToUsed;

	// 出让方案是否合规 IF_GRANT_CRFA
	private String ifGrantCrfa;

	// 是否集体会审 IF_GROUP_ASSESS
	private String ifGroupAssess;

	// 是否有规划审批手续 IF_HAS_PROCEDURES
	private String ifHasProcedures;

	// 是否建设项目立项 IF_PROJECT_BUILD
	private String ifProgectBuild;

	/** nullable persistent field */
	private BigDecimal initPrice;

	// 起叫价(楼面地价)
	private BigDecimal initPriceLmdJ;

	/** nullable persistent field */
	private String initType;

	/** nullable persistent field */
	private String investAmount;

	// 投资强度
	private BigDecimal investStrength;

	// 是否修改竞买资格及要求
	private String isModify;

	// 是否是有意向出让地块
	private String isPurposeBlock;

	/** nullable persistent field */
	private String isValid;

	/** nullable persistent field */
	private String jsBankDeposit;

	/** nullable persistent field */
	private BigDecimal jsFdcCyns;

	/** nullable persistent field */
	private String jsFdcZzdj;

	/** nullable persistent field */
	private BigDecimal jsJ3nZyywll;

	/** nullable persistent field */
	private BigDecimal jsJ5nKfLpgs;

	/** nullable persistent field */
	private BigDecimal jsJ5nKfTdmj;

	/** nullable persistent field */
	private String jsJ5nKfTdyt;

	/** nullable persistent field */
	private String jsJjnLsCylx;

	/** nullable persistent field */
	private String jsJzc;

	/** nullable persistent field */
	private String jsQyZxdj;

	/** nullable persistent field */
	private BigDecimal jsSyjzc;

	/** nullable persistent field */
	private String landId;

	/** nullable persistent field */
	private String landIdCollection;

	/** nullable persistent field */
	private String landLocation;

	// 评估土地单价
	private BigDecimal landSignPrice;

	/** nullable persistent field */
	private String landUseExpirationDate;

	/** nullable persistent field */
	private String landUseType;

	// 子地块土地用途
	private String[] landUseTypes;

	/** nullable persistent field */
	private String layoutProperty;

	/** nullable persistent field */
	private String linkAddr;

	/** nullable persistent field */
	private String linkPerson;

	/** nullable persistent field */
	private String linkTel;

	/** nullable persistent field */
	private String localTradeType;

	/** nullable persistent field */
	private String marginAccount;

	/** nullable persistent field */
	private BigDecimal marginAmount;

	// 保证金账户
	private String marginBank;

	/** nullable persistent field */
	private Date marginEndDate;

	/** nullable persistent field */
	private String marginLinker;

	/** nullable persistent field */
	private String marginTel;

	/** nullable persistent field */
	private String marketType;

	private String memo;

	/** nullable persistent field */
	private String newConstructionArea;

	private String nodeId;

	/** nullable persistent field */
	private String noitceNo;

	/** nullable persistent field */
	private String noticeId;

	// 其他建设条件
	private String otherBuildingCondition;

	/** nullable persistent field */
	private BigDecimal otherLandArea;

	/** nullable persistent field */
	private BigDecimal otherProvNumber;

	/** nullable persistent field */
	private String otherRequire;

	/** nullable persistent field */
	private String ownershipReport;

	private String partId;

	// 子地块ID
	private String[] partIds;

	// 出让金支付批次
	private String[] payBatchs;

	// 出让金支付比例
	private String[] payProportions;

	// 出让金支付时间
	private String[] payTimes;

	private String peitao;

	// 自然人应提交文件
	private String personalFile;

	// 评估机构资质 PG_ORG_GRADE
	private String pgOrgGrade;

	// 评估机构名称 PG_ORG_NAME
	private String pgOrgName;

	private Date pingguDate;

	private String pingguMethod;

	private String pingguPerson;

	private String pingguResult;

	private String pingguUnit;

	// 配建类型
	private String[] pjlxs;

	// 配建信息ID
	private String pjxxInfoId;

	// 配建说明
	private String pjxxMemo;

	/** nullable persistent field */
	private String planAuditedOrg;

	// 规划建筑面积
	private BigDecimal planBuildingArea;

	// 子地块规划建筑面积
	private String[] planBuildingAreas;

	/** nullable persistent field */
	private String planDoc;

	/** nullable persistent field */
	private String planNum;

	/** nullable persistent field */
	private String planUseMemo;

	/** nullable persistent field */
	private String priceComponent;

	/** nullable persistent field */
	private String priceLimit;

	// 限定房价(元/平方米)
	private BigDecimal priceXdfj;

	/** nullable persistent field */
	private String programAuditedOrg;

	/** nullable persistent field */
	private String programNum;

	// 前期监察id
	private String qqjcInfoId;

	/** nullable persistent field */
	private String rangeEast;

	/** nullable persistent field */
	private String rangeNorth;

	/** nullable persistent field */
	private String rangeSouth;

	/** nullable persistent field */
	private String rangeWest;

	/** nullable persistent field */
	private String remiseAccountReceivable;

	/** nullable persistent field */
	private String remiseBalance;

	/** nullable persistent field */
	private String remiseExpirationDate;

	/** nullable persistent field */
	private String remisePerson;

	/** nullable persistent field */
	private Date reserveDate;

	/** nullable persistent field */
	private String reserveFileNum;

	/** nullable persistent field */
	private String resultCert;

	/** nullable persistent field */
	private Date resultDate;

	private String resultId;

	/** nullable persistent field */
	private String resultName;

	/** nullable persistent field */
	private String resultPrice;

	/** nullable persistent field */
	private String retrieveFileNum;

	// 生活垃圾转运设施代建资金单价
	private BigDecimal rubbishTransportFee;

	/** nullable persistent field */
	private String saleOutput;

	// 保存类型
	private String saveType;

	private String sellYear;

	// 土地交易服务费
	private BigDecimal[] serviceCharge;

	private String specialPromise;

	// 现场加价幅度
	private BigDecimal spotAddPriceRange;

	/** nullable persistent field */
	private Date startDate;

	/** nullable persistent field */
	private String status;

	/** nullable persistent field */
	private String subInfo;

	/** nullable persistent field */
	private String suppliedLandSum;

	private String surrenderTime;

	/** nullable persistent field */
	private String[] sy_appIds;

	/** nullable persistent field */
	private String[] sy_blockIds;

	/** nullable persistent field */
	private String[] sy_fileUrl;

	/** nullable persistent field */
	private String[] sy_issueType;

	/** nullable persistent field */
	private String[] sy_issueUnit;

	/** nullable persistent field */
	private String[] sy_issueUnitMemo;

	/** identifier field */
	private String[] sy_materialId;

	/** nullable persistent field */
	private String[] sy_materialName;

	/** nullable persistent field */
	private String[] sy_materialNum;

	/** nullable persistent field */
	private String[] sy_materialNumb;

	/** nullable persistent field */
	private String[] sy_materialType;

	/** nullable persistent field */
	private String[] sy_memo;

	/** nullable persistent field */
	private String taxOutput;

	/** nullable persistent field */
	private String tblockId;

	/** nullable persistent field */
	private String tblockNoticeNo;

	/** nullable persistent field */
	private String textMemo;

	private String textOpen;

	/** nullable persistent field */
	private String timeDiff;

	/** nullable persistent field */
	private BigDecimal totalBuildingArea;

	// 城市基础设施配套费总额
	private BigDecimal totalCityFacilityFee;

	// 教育设施配套费总额
	private BigDecimal totalEducationFacilityFee;

	/** nullable persistent field */
	private BigDecimal totalLandArea;

	// 生活垃圾转运设施代建资金总额
	private BigDecimal totalRubbishTransportFee;

	/** nullable persistent field */
	private String tranGroundSitu;

	/** nullable persistent field */
	private String tranResult;

	/** nullable persistent field */
	private Date transferDate;

	/** nullable persistent field */
	private BigDecimal transferLife;

	// 子地块出让年限
	private String[] transferLifes;

	/** nullable persistent field */
	private String transferMode;

	/** nullable persistent field */
	private String tresultCert;

	/** nullable persistent field */
	private String tresultDate;

	/** nullable persistent field */
	private String tresultName;

	/** nullable persistent field */
	private String tresultPrice;

	/** nullable persistent field */
	private String ttranResult;

	/** nullable persistent field */
	private String ublockId;

	// 法人或其他组织应提交文件
	private String unitFile;

	/** nullable persistent field */
	private String upset;

	// 评估会审价格（加密）UPSET_PRICE
	private BigDecimal upsetPrice;

	/** nullable persistent field */
	private String useId;

	private String userId;

	/** nullable persistent field */
	private String volumeRate;

	private String volumeRate2;

	public String getVolumeRate2() {
		return volumeRate2;
	}

	public void setVolumeRate2(String volumeRate2) {
		this.volumeRate2 = volumeRate2;
	}

	/** nullable persistent field */
	private String volumeRateBelow;

	// 规划容积率
	private String[] volumeRates;

	private String[] volumeRates2;

	public String[] getVolumeRates2() {
		return volumeRates2;
	}

	public void setVolumeRates2(String[] volumeRates2) {
		this.volumeRates2 = volumeRates2;
	}

	// 规划容积率符号
	private String[] volumeRateSigns;

	/** nullable persistent field */
	private String waterCost;

	private String yixiangPersonName;
	
	private String yixiangOrgNo;
	private String[] densitys1;
	private String[] greeningRates1;
	
	//最高限价
	private BigDecimal maxPrice;
	
	public String[] getDensitys1() {
		return densitys1;
	}

	public void setDensitys1(String[] densitys1) {
		this.densitys1 = densitys1;
	}

	public String[] getGreeningRates1() {
		return greeningRates1;
	}

	public void setGreeningRates1(String[] greeningRates1) {
		this.greeningRates1 = greeningRates1;
	}

	public String getAboveTerraBuilldingArea() {
		return aboveTerraBuilldingArea;
	}

	public String getAbroad3yearProfit() {
		return abroad3yearProfit;
	}

	public BigDecimal getAbroadBankDeposit() {
		return abroadBankDeposit;
	}

	public String getAbroadCreditLevel() {
		return abroadCreditLevel;
	}

	public String getAbroadJointPercent() {
		return abroadJointPercent;
	}

	public String getAbroadOwnerComp() {
		return abroadOwnerComp;
	}

	public String getAbroadSameTypeTime() {
		return abroadSameTypeTime;
	}

	public String getAccessProductType() {
		return accessProductType;
	}

	public String getAccountName() {
		return accountName;
	}

	public BigDecimal getAddPriceRange() {
		return addPriceRange;
	}

	public String getAllocationCompensation() {
		return allocationCompensation;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppSupNum() {
		return appSupNum;
	}

	public BigDecimal getAreaBuildJjf() {
		return areaBuildJjf;
	}

	public BigDecimal getAreaBuildLzf() {
		return areaBuildLzf;
	}

	public BigDecimal getAreaBuildXjf() {
		return areaBuildXjf;
	}

	public BigDecimal getAreaLandJjf() {
		return areaLandJjf;
	}

	public BigDecimal getAreaLandLjf() {
		return areaLandLjf;
	}

	public BigDecimal getAreaLandLzf() {
		return areaLandLzf;
	}

	public String getAttachMap() {
		return attachMap;
	}

	public Date getAuditedDate() {
		return auditedDate;
	}

	public String getAuditedNum() {
		return auditedNum;
	}

	public String getAuditedNum1() {
		return auditedNum1;
	}

	public String getAuditedNum2() {
		return auditedNum2;
	}

	public String getBatchFileNum() {
		return batchFileNum;
	}

	public BigDecimal getBelow90FlatRatio() {
		return below90FlatRatio;
	}

	public String getBidderId() {
		return bidderId;
	}

	public String getBidderPersonId() {
		return bidderPersonId;
	}

	public List getBidders() {
		return bidders;
	}

	public String getBizProgramDocNum() {
		return bizProgramDocNum;
	}

	public BigDecimal getBlockArea() {
		return blockArea;
	}

	public String[] getBlockAreas() {
		return blockAreas;
	}

	public String getBlockAuditStatus() {
		return blockAuditStatus;
	}

	public String getBlockCatalog() {
		return blockCatalog;
	}

	public String getBlockCode() {
		return blockCode;
	}

	public String[] getBlockCodes() {
		return blockCodes;
	}

	public String getBlockConId() {
		return blockConId;
	}

	public String getBlockDetailedUsed() {
		return blockDetailedUsed;
	}

	public String getBlockId() {
		return blockId;
	}

	public String getBlockLandId() {
		return blockLandId;
	}

	public String getBlockName() {
		return blockName;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public String getBlockQuality() {
		return blockQuality;
	}

	public String getBlockSubType() {
		return blockSubType;
	}

	public String getBlockType() {
		return blockType;
	}

	public String getBlockUsedId() {
		return blockUsedId;
	}

	public String getBuildingCondition() {
		return buildingCondition;
	}

	public String getBuildingHeight() {
		return buildingHeight;
	}

	public String getBuildingTime() {
		return buildingTime;
	}

	public String getBzBeizhu() {
		return bzBeizhu;
	}

	public String getBzCrjkjnyq() {
		return bzCrjkjnyq;
	}

	public String getBzDgjjgsj() {
		return bzDgjjgsj;
	}

	public String getBzDkqtsm() {
		return bzDkqtsm;
	}

	public String getBzGhsjyd() {
		return bzGhsjyd;
	}

	public String getBzGpsxf() {
		return bzGpsxf;
	}

	public String getBzJmzgjyq() {
		return bzJmzgjyq;
	}

	public String getBzPmyj() {
		return bzPmyj;
	}

	public String getBzSzpttj() {
		return bzSzpttj;
	}

	public String getBzTdcrfj() {
		return bzTdcrfj;
	}

	public String getBzTdjftj() {
		return bzTdjftj;
	}

	public String getBzXbjg() {
		return bzXbjg;
	}

	public String getCblockId() {
		return cblockId;
	}

	public BigDecimal getChamberAreaRatioLimit() {
		return chamberAreaRatioLimit;
	}

	public BigDecimal getCityFacilityFee() {
		return cityFacilityFee;
	}

	public String getClobContent() {
		return clobContent;
	}

	public Date getCompDate() {
		return compDate;
	}

	public String getCompeteQualifications() {
		return competeQualifications;
	}

	public String getContractNum() {
		return contractNum;
	}

	public String getContractSignedDate() {
		return contractSignedDate;
	}

	public String getCopyNumber() {
		return copyNumber;
	}

	public Integer getCountJjsyf() {
		return countJjsyf;
	}

	public Integer getCountLzfpj() {
		return countLzfpj;
	}

	public String getCountSalePerson() {
		return countSalePerson;
	}

	public String getCountUse() {
		return countUse;
	}

	public String getCurrentSituationCondition() {
		return currentSituationCondition;
	}

	public String getCurrMaxPersonNum() {
		return CurrMaxPersonNum;
	}

	public String getCurrMaxPrice() {
		return currMaxPrice;
	}

	public String getDemolishCompesationMethod() {
		return demolishCompesationMethod;
	}

	public String getDensity() {
		return density;
	}

	public String getDensityMin() {
		return densityMin;
	}

	public String[] getDensitys() {
		return densitys;
	}

	public String[] getDensitySigns() {
		return densitySigns;
	}

	public String getDepositeBalance() {
		return depositeBalance;
	}

	public String getDepositeDate() {
		return depositeDate;
	}

	public String getDepositSum() {
		return depositSum;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public String getDistrictLinker() {
		return districtLinker;
	}

	public String getDomestic3yearProfit() {
		return domestic3yearProfit;
	}

	public String getDomesticCreditLevel() {
		return domesticCreditLevel;
	}

	public String getDomesticJointPercent() {
		return domesticJointPercent;
	}

	public String getDomesticOwnerComp() {
		return domesticOwnerComp;
	}

	public String getDomesticSameTypeTime() {
		return domesticSameTypeTime;
	}

	public BigDecimal getEducationFacilityFee() {
		return educationFacilityFee;
	}

	public BigDecimal getEmployeeNum() {
		return employeeNum;
	}

	public String getEndorseDistrict() {
		return endorseDistrict;
	}

	public String getEnergyCost() {
		return energyCost;
	}

	public String getEquipCondition() {
		return equipCondition;
	}

	public BigDecimal getEvaluteFloorLandValue() {
		return evaluteFloorLandValue;
	}

	public FormFile[] getFileName() {
		return fileName;
	}

	public String getFixInvestAmount() {
		return fixInvestAmount;
	}

	public String getFlatRate() {
		return flatRate;
	}

	public String getGongDiNum() {
		return gongDiNum;
	}

	public String getGongDiType() {
		return gongDiType;
	}

	public String getGovGrantNum() {
		return govGrantNum;
	}

	public String getGreeningRate() {
		return greeningRate;
	}

	public String getGreeningRateCon() {
		return greeningRateCon;
	}

	public String[] getGreeningRates() {
		return greeningRates;
	}

	public String[] getGreeningRateSigns() {
		return greeningRateSigns;
	}

	public String getGuihuaCertNo() {
		return guihuaCertNo;
	}

	public String[] getGy_appIds() {
		return gy_appIds;
	}

	public String[] getGy_blockIds() {
		return gy_blockIds;
	}

	public String[] getGy_fileUrl() {
		return gy_fileUrl;
	}

	public String[] getGy_issueType() {
		return gy_issueType;
	}

	public String[] getGy_issueUnit() {
		return gy_issueUnit;
	}

	public String[] getGy_issueUnitMemo() {
		return gy_issueUnitMemo;
	}

	public String[] getGy_materialId() {
		return gy_materialId;
	}

	public String[] getGy_materialName() {
		return gy_materialName;
	}

	public String[] getGy_materialNum() {
		return gy_materialNum;
	}

	public String[] getGy_materialNumb() {
		return gy_materialNumb;
	}

	public String[] getGy_materialType() {
		return gy_materialType;
	}

	public String[] getGy_memo() {
		return gy_memo;
	}

	public String getHighLimit() {
		return highLimit;
	}

	public String getIfBlockPrecognition() {
		return ifBlockPrecognition;
	}

	public String getIfFarmLand() {
		return ifFarmLand;
	}

	public String getIfFarmToUsed() {
		return ifFarmToUsed;
	}

	public String getIfGrantCrfa() {
		return ifGrantCrfa;
	}

	public String getIfGroupAssess() {
		return ifGroupAssess;
	}

	public String getIfHasProcedures() {
		return ifHasProcedures;
	}

	public String getIfProgectBuild() {
		return ifProgectBuild;
	}

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	public BigDecimal getInitPriceLmdJ() {
		return initPriceLmdJ;
	}

	public String getInitType() {
		return initType;
	}

	public String getInvestAmount() {
		return investAmount;
	}

	public BigDecimal getInvestStrength() {
		return investStrength;
	}

	public String getIsModify() {
		return isModify;
	}

	public String getIsPurposeBlock() {
		return isPurposeBlock;
	}

	public String getIsValid() {
		return isValid;
	}

	public String getJsBankDeposit() {
		return jsBankDeposit;
	}

	public BigDecimal getJsFdcCyns() {
		return jsFdcCyns;
	}

	public String getJsFdcZzdj() {
		return jsFdcZzdj;
	}

	public BigDecimal getJsJ3nZyywll() {
		return jsJ3nZyywll;
	}

	public BigDecimal getJsJ5nKfLpgs() {
		return jsJ5nKfLpgs;
	}

	public BigDecimal getJsJ5nKfTdmj() {
		return jsJ5nKfTdmj;
	}

	public String getJsJ5nKfTdyt() {
		return jsJ5nKfTdyt;
	}

	public String getJsJjnLsCylx() {
		return jsJjnLsCylx;
	}

	public String getJsJzc() {
		return jsJzc;
	}

	public String getJsQyZxdj() {
		return jsQyZxdj;
	}

	public BigDecimal getJsSyjzc() {
		return jsSyjzc;
	}

	public String getLandId() {
		return landId;
	}

	public String getLandIdCollection() {
		return landIdCollection;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public BigDecimal getLandSignPrice() {
		return landSignPrice;
	}

	public String getLandUseExpirationDate() {
		return landUseExpirationDate;
	}

	public String getLandUseType() {
		return landUseType;
	}

	public String[] getLandUseTypes() {
		return landUseTypes;
	}

	public String getLayoutProperty() {
		return layoutProperty;
	}

	public String getLinkAddr() {
		return linkAddr;
	}

	public String getLinkPerson() {
		return linkPerson;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public String getLocalTradeType() {
		return localTradeType;
	}

	public String getMarginAccount() {
		return marginAccount;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public String getMarginBank() {
		return marginBank;
	}

	public Date getMarginEndDate() {
		return marginEndDate;
	}

	public String getMarginLinker() {
		return marginLinker;
	}

	public String getMarginTel() {
		return marginTel;
	}

	public String getMarketType() {
		return marketType;
	}

	public String getMemo() {
		return memo;
	}

	public String getNewConstructionArea() {
		return newConstructionArea;
	}

	public String getNodeId() {
		return nodeId;
	}

	public String getNoitceNo() {
		return noitceNo;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public String getOtherBuildingCondition() {
		return otherBuildingCondition;
	}

	public BigDecimal getOtherLandArea() {
		return otherLandArea;
	}

	public BigDecimal getOtherProvNumber() {
		return otherProvNumber;
	}

	public String getOtherRequire() {
		return otherRequire;
	}

	public String getOwnershipReport() {
		return ownershipReport;
	}

	public String getPartId() {
		return partId;
	}

	public String[] getPartIds() {
		return partIds;
	}

	public String[] getPayBatchs() {
		return payBatchs;
	}

	public String[] getPayProportions() {
		return payProportions;
	}

	public String[] getPayTimes() {
		return payTimes;
	}

	public String getPeitao() {
		return peitao;
	}

	public String getPersonalFile() {
		return personalFile;
	}

	public String getPgOrgGrade() {
		return pgOrgGrade;
	}

	public String getPgOrgName() {
		return pgOrgName;
	}

	public Date getPingguDate() {
		return pingguDate;
	}

	public String getPingguMethod() {
		return pingguMethod;
	}

	public String getPingguPerson() {
		return pingguPerson;
	}

	public String getPingguResult() {
		return pingguResult;
	}

	public String getPingguUnit() {
		return pingguUnit;
	}

	public String[] getPjlxs() {
		return pjlxs;
	}

	public String getPjxxInfoId() {
		return pjxxInfoId;
	}

	public String getPjxxMemo() {
		return pjxxMemo;
	}

	public String getPlanAuditedOrg() {
		return planAuditedOrg;
	}

	public BigDecimal getPlanBuildingArea() {
		return planBuildingArea;
	}

	public String[] getPlanBuildingAreas() {
		return planBuildingAreas;
	}

	public String getPlanDoc() {
		return planDoc;
	}

	public String getPlanNum() {
		return planNum;
	}

	public String getPlanUseMemo() {
		return planUseMemo;
	}

	public String getPriceComponent() {
		return priceComponent;
	}

	public String getPriceLimit() {
		return priceLimit;
	}

	public BigDecimal getPriceXdfj() {
		return priceXdfj;
	}

	public String getProgramAuditedOrg() {
		return programAuditedOrg;
	}

	public String getProgramNum() {
		return programNum;
	}

	public String getQqjcInfoId() {
		return qqjcInfoId;
	}

	public String getRangeEast() {
		return rangeEast;
	}

	public String getRangeNorth() {
		return rangeNorth;
	}

	public String getRangeSouth() {
		return rangeSouth;
	}

	public String getRangeWest() {
		return rangeWest;
	}

	public String getRemiseAccountReceivable() {
		return remiseAccountReceivable;
	}

	public String getRemiseBalance() {
		return remiseBalance;
	}

	public String getRemiseExpirationDate() {
		return remiseExpirationDate;
	}

	public String getRemisePerson() {
		return remisePerson;
	}

	public Date getReserveDate() {
		return reserveDate;
	}

	public String getReserveFileNum() {
		return reserveFileNum;
	}

	public String getResultCert() {
		return resultCert;
	}

	public Date getResultDate() {
		return resultDate;
	}

	public String getResultId() {
		return resultId;
	}

	public String getResultName() {
		return resultName;
	}

	public String getResultPrice() {
		return resultPrice;
	}

	public String getRetrieveFileNum() {
		return retrieveFileNum;
	}

	public BigDecimal getRubbishTransportFee() {
		return rubbishTransportFee;
	}

	public String getSaleOutput() {
		return saleOutput;
	}

	public String getSaveType() {
		return saveType;
	}

	public String getSellYear() {
		return sellYear;
	}

	public BigDecimal[] getServiceCharge() {
		return serviceCharge;
	}

	public String getSpecialPromise() {
		return specialPromise;
	}

	public BigDecimal getSpotAddPriceRange() {
		return spotAddPriceRange;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getStatus() {
		return status;
	}

	public String getSubInfo() {
		return subInfo;
	}

	public String getSuppliedLandSum() {
		return suppliedLandSum;
	}

	public String getSurrenderTime() {
		return surrenderTime;
	}

	public String[] getSy_appIds() {
		return sy_appIds;
	}

	public String[] getSy_blockIds() {
		return sy_blockIds;
	}

	public String[] getSy_fileUrl() {
		return sy_fileUrl;
	}

	public String[] getSy_issueType() {
		return sy_issueType;
	}

	public String[] getSy_issueUnit() {
		return sy_issueUnit;
	}

	public String[] getSy_issueUnitMemo() {
		return sy_issueUnitMemo;
	}

	public String[] getSy_materialId() {
		return sy_materialId;
	}

	public String[] getSy_materialName() {
		return sy_materialName;
	}

	public String[] getSy_materialNum() {
		return sy_materialNum;
	}

	public String[] getSy_materialNumb() {
		return sy_materialNumb;
	}

	public String[] getSy_materialType() {
		return sy_materialType;
	}

	public String[] getSy_memo() {
		return sy_memo;
	}

	public String getTaxOutput() {
		return taxOutput;
	}

	public String getTblockId() {
		return tblockId;
	}

	public String getTblockNoticeNo() {
		return tblockNoticeNo;
	}

	public String getTextMemo() {
		return textMemo;
	}

	public String getTextOpen() {
		return textOpen;
	}

	public String getTimeDiff() {
		return timeDiff;
	}

	public BigDecimal getTotalBuildingArea() {
		return totalBuildingArea;
	}

	public BigDecimal getTotalCityFacilityFee() {
		return totalCityFacilityFee;
	}

	public BigDecimal getTotalEducationFacilityFee() {
		return totalEducationFacilityFee;
	}

	public BigDecimal getTotalLandArea() {
		return totalLandArea;
	}

	public BigDecimal getTotalRubbishTransportFee() {
		return totalRubbishTransportFee;
	}

	public String getTranGroundSitu() {
		return tranGroundSitu;
	}

	public String getTranResult() {
		return tranResult;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public BigDecimal getTransferLife() {
		return transferLife;
	}

	public String[] getTransferLifes() {
		return transferLifes;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public String getTresultCert() {
		return tresultCert;
	}

	public String getTresultDate() {
		return tresultDate;
	}

	public String getTresultName() {
		return tresultName;
	}

	public String getTresultPrice() {
		return tresultPrice;
	}

	public String getTtranResult() {
		return ttranResult;
	}

	public String getUblockId() {
		return ublockId;
	}

	public String getUnitFile() {
		return unitFile;
	}

	public String getUpset() {
		return upset;
	}

	public BigDecimal getUpsetPrice() {
		return upsetPrice;
	}

	public String getUseId() {
		return useId;
	}

	public String getUserId() {
		return userId;
	}

	public String getVolumeRate() {
		return volumeRate;
	}

	public String getVolumeRateBelow() {
		return volumeRateBelow;
	}

	public String[] getVolumeRates() {
		return volumeRates;
	}

	public String[] getVolumeRateSigns() {
		return volumeRateSigns;
	}

	public String getWaterCost() {
		return waterCost;
	}

	public String getYixiangPersonName() {
		return yixiangPersonName;
	}

	public void setAboveTerraBuilldingArea(String aboveTerraBuilldingArea) {
		this.aboveTerraBuilldingArea = aboveTerraBuilldingArea;
	}

	public void setAbroad3yearProfit(String abroad3yearProfit) {
		this.abroad3yearProfit = abroad3yearProfit;
	}

	public void setAbroadBankDeposit(BigDecimal abroadBankDeposit) {
		this.abroadBankDeposit = abroadBankDeposit;
	}

	public void setAbroadCreditLevel(String abroadCreditLevel) {
		this.abroadCreditLevel = abroadCreditLevel;
	}

	public void setAbroadJointPercent(String abroadJointPercent) {
		this.abroadJointPercent = abroadJointPercent;
	}

	public void setAbroadOwnerComp(String abroadOwnerComp) {
		this.abroadOwnerComp = abroadOwnerComp;
	}

	public void setAbroadSameTypeTime(String abroadSameTypeTime) {
		this.abroadSameTypeTime = abroadSameTypeTime;
	}

	public void setAccessProductType(String accessProductType) {
		this.accessProductType = accessProductType;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAddPriceRange(BigDecimal addPriceRange) {
		this.addPriceRange = addPriceRange;
	}

	public void setAllocationCompensation(String allocationCompensation) {
		this.allocationCompensation = allocationCompensation;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setAppSupNum(String appSupNum) {
		this.appSupNum = appSupNum;
	}

	public void setAreaBuildJjf(BigDecimal areaBuildJjf) {
		this.areaBuildJjf = areaBuildJjf;
	}

	public void setAreaBuildLzf(BigDecimal areaBuildLzf) {
		this.areaBuildLzf = areaBuildLzf;
	}

	public void setAreaBuildXjf(BigDecimal areaBuildXjf) {
		this.areaBuildXjf = areaBuildXjf;
	}

	public void setAreaLandJjf(BigDecimal areaLandJjf) {
		this.areaLandJjf = areaLandJjf;
	}

	public void setAreaLandLjf(BigDecimal areaLandLjf) {
		this.areaLandLjf = areaLandLjf;
	}

	public void setAreaLandLzf(BigDecimal areaLandLzf) {
		this.areaLandLzf = areaLandLzf;
	}

	public void setAttachMap(String attachMap) {
		this.attachMap = attachMap;
	}

	public void setAuditedDate(Date auditedDate) {
		this.auditedDate = auditedDate;
	}

	public void setAuditedNum(String auditedNum) {
		this.auditedNum = auditedNum;
	}

	public void setAuditedNum1(String auditedNum1) {
		this.auditedNum1 = auditedNum1;
	}

	public void setAuditedNum2(String auditedNum2) {
		this.auditedNum2 = auditedNum2;
	}

	public void setBatchFileNum(String batchFileNum) {
		this.batchFileNum = batchFileNum;
	}

	public void setBelow90FlatRatio(BigDecimal below90FlatRatio) {
		this.below90FlatRatio = below90FlatRatio;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public void setBidderPersonId(String bidderPersonId) {
		this.bidderPersonId = bidderPersonId;
	}

	public void setBidders(List bidders) {
		this.bidders = bidders;
	}

	public void setBizProgramDocNum(String bizProgramDocNum) {
		this.bizProgramDocNum = bizProgramDocNum;
	}

	public void setBlockArea(BigDecimal blockArea) {
		this.blockArea = blockArea;
	}

	public void setBlockAreas(String[] blockAreas) {
		this.blockAreas = blockAreas;
	}

	public void setBlockAuditStatus(String blockAuditStatus) {
		this.blockAuditStatus = blockAuditStatus;
	}

	public void setBlockCatalog(String blockCatalog) {
		this.blockCatalog = blockCatalog;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	public void setBlockCodes(String[] blockCodes) {
		this.blockCodes = blockCodes;
	}

	public void setBlockConId(String blockConId) {
		this.blockConId = blockConId;
	}

	public void setBlockDetailedUsed(String blockDetailedUsed) {
		this.blockDetailedUsed = blockDetailedUsed;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public void setBlockLandId(String blockLandId) {
		this.blockLandId = blockLandId;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public void setBlockQuality(String blockQuality) {
		this.blockQuality = blockQuality;
	}

	public void setBlockSubType(String blockSubType) {
		this.blockSubType = blockSubType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}

	public void setBlockUsedId(String blockUsedId) {
		this.blockUsedId = blockUsedId;
	}

	public void setBuildingCondition(String buildingCondition) {
		this.buildingCondition = buildingCondition;
	}

	public void setBuildingHeight(String buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public void setBuildingTime(String buildingTime) {
		this.buildingTime = buildingTime;
	}

	public void setBzBeizhu(String bzBeizhu) {
		this.bzBeizhu = bzBeizhu;
	}

	public void setBzCrjkjnyq(String bzCrjkjnyq) {
		this.bzCrjkjnyq = bzCrjkjnyq;
	}

	public void setBzDgjjgsj(String bzDgjjgsj) {
		this.bzDgjjgsj = bzDgjjgsj;
	}

	public void setBzDkqtsm(String bzDkqtsm) {
		this.bzDkqtsm = bzDkqtsm;
	}

	public void setBzGhsjyd(String bzGhsjyd) {
		this.bzGhsjyd = bzGhsjyd;
	}

	public void setBzGpsxf(String bzGpsxf) {
		this.bzGpsxf = bzGpsxf;
	}

	public void setBzJmzgjyq(String bzJmzgjyq) {
		this.bzJmzgjyq = bzJmzgjyq;
	}

	public void setBzPmyj(String bzPmyj) {
		this.bzPmyj = bzPmyj;
	}

	public void setBzSzpttj(String bzSzpttj) {
		this.bzSzpttj = bzSzpttj;
	}

	public void setBzTdcrfj(String bzTdcrfj) {
		this.bzTdcrfj = bzTdcrfj;
	}

	public void setBzTdjftj(String bzTdjftj) {
		this.bzTdjftj = bzTdjftj;
	}

	public void setBzXbjg(String bzXbjg) {
		this.bzXbjg = bzXbjg;
	}

	public void setCblockId(String cblockId) {
		this.cblockId = cblockId;
	}

	public void setChamberAreaRatioLimit(BigDecimal chamberAreaRatioLimit) {
		this.chamberAreaRatioLimit = chamberAreaRatioLimit;
	}

	public void setCityFacilityFee(BigDecimal cityFacilityFee) {
		this.cityFacilityFee = cityFacilityFee;
	}

	public void setClobContent(String clobContent) {
		this.clobContent = clobContent;
	}

	public void setCompDate(Date compDate) {
		this.compDate = compDate;
	}

	public void setCompeteQualifications(String competeQualifications) {
		this.competeQualifications = competeQualifications;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public void setContractSignedDate(String contractSignedDate) {
		this.contractSignedDate = contractSignedDate;
	}

	public void setCopyNumber(String copyNumber) {
		this.copyNumber = copyNumber;
	}

	public void setCountJjsyf(Integer countJjsyf) {
		this.countJjsyf = countJjsyf;
	}

	public void setCountLzfpj(Integer countLzfpj) {
		this.countLzfpj = countLzfpj;
	}

	public void setCountSalePerson(String countSalePerson) {
		this.countSalePerson = countSalePerson;
	}

	public void setCountUse(String countUse) {
		this.countUse = countUse;
	}

	public void setCurrentSituationCondition(String currentSituationCondition) {
		this.currentSituationCondition = currentSituationCondition;
	}

	public void setCurrMaxPersonNum(String currMaxPersonNum) {
		CurrMaxPersonNum = currMaxPersonNum;
	}

	public void setCurrMaxPrice(String currMaxPrice) {
		this.currMaxPrice = currMaxPrice;
	}

	public void setDemolishCompesationMethod(String demolishCompesationMethod) {
		this.demolishCompesationMethod = demolishCompesationMethod;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public void setDensityMin(String densityMin) {
		this.densityMin = densityMin;
	}

	public void setDensitys(String[] densitys) {
		this.densitys = densitys;
	}

	public void setDensitySigns(String[] densitySigns) {
		this.densitySigns = densitySigns;
	}

	public void setDepositeBalance(String depositeBalance) {
		this.depositeBalance = depositeBalance;
	}

	public void setDepositeDate(String depositeDate) {
		this.depositeDate = depositeDate;
	}

	public void setDepositSum(String depositSum) {
		this.depositSum = depositSum;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public void setDistrictLinker(String districtLinker) {
		this.districtLinker = districtLinker;
	}

	public void setDomestic3yearProfit(String domestic3yearProfit) {
		this.domestic3yearProfit = domestic3yearProfit;
	}

	public void setDomesticCreditLevel(String domesticCreditLevel) {
		this.domesticCreditLevel = domesticCreditLevel;
	}

	public void setDomesticJointPercent(String domesticJointPercent) {
		this.domesticJointPercent = domesticJointPercent;
	}

	public void setDomesticOwnerComp(String domesticOwnerComp) {
		this.domesticOwnerComp = domesticOwnerComp;
	}

	public void setDomesticSameTypeTime(String domesticSameTypeTime) {
		this.domesticSameTypeTime = domesticSameTypeTime;
	}

	public void setEducationFacilityFee(BigDecimal educationFacilityFee) {
		this.educationFacilityFee = educationFacilityFee;
	}

	public void setEmployeeNum(BigDecimal employeeNum) {
		this.employeeNum = employeeNum;
	}

	public void setEndorseDistrict(String endorseDistrict) {
		this.endorseDistrict = endorseDistrict;
	}

	public void setEnergyCost(String energyCost) {
		this.energyCost = energyCost;
	}

	public void setEquipCondition(String equipCondition) {
		this.equipCondition = equipCondition;
	}

	public void setEvaluteFloorLandValue(BigDecimal evaluteFloorLandValue) {
		this.evaluteFloorLandValue = evaluteFloorLandValue;
	}

	public void setFileName(FormFile[] fileName) {
		this.fileName = fileName;
	}

	public void setFixInvestAmount(String fixInvestAmount) {
		this.fixInvestAmount = fixInvestAmount;
	}

	public void setFlatRate(String flatRate) {
		this.flatRate = flatRate;
	}

	public void setGongDiNum(String gongDiNum) {
		this.gongDiNum = gongDiNum;
	}

	public void setGongDiType(String gongDiType) {
		this.gongDiType = gongDiType;
	}

	public void setGovGrantNum(String govGrantNum) {
		this.govGrantNum = govGrantNum;
	}

	public void setGreeningRate(String greeningRate) {
		this.greeningRate = greeningRate;
	}

	public void setGreeningRateCon(String greeningRateCon) {
		this.greeningRateCon = greeningRateCon;
	}

	public void setGreeningRates(String[] greeningRates) {
		this.greeningRates = greeningRates;
	}

	public void setGreeningRateSigns(String[] greeningRateSigns) {
		this.greeningRateSigns = greeningRateSigns;
	}

	public void setGuihuaCertNo(String guihuaCertNo) {
		this.guihuaCertNo = guihuaCertNo;
	}

	public void setGy_appIds(String[] gy_appIds) {
		this.gy_appIds = gy_appIds;
	}

	public void setGy_blockIds(String[] gy_blockIds) {
		this.gy_blockIds = gy_blockIds;
	}

	public void setGy_fileUrl(String[] gy_fileUrl) {
		this.gy_fileUrl = gy_fileUrl;
	}

	public void setGy_issueType(String[] gy_issueType) {
		this.gy_issueType = gy_issueType;
	}

	public void setGy_issueUnit(String[] gy_issueUnit) {
		this.gy_issueUnit = gy_issueUnit;
	}

	public void setGy_issueUnitMemo(String[] gy_issueUnitMemo) {
		this.gy_issueUnitMemo = gy_issueUnitMemo;
	}

	public void setGy_materialId(String[] gy_materialId) {
		this.gy_materialId = gy_materialId;
	}

	public void setGy_materialName(String[] gy_materialName) {
		this.gy_materialName = gy_materialName;
	}

	public void setGy_materialNum(String[] gy_materialNum) {
		this.gy_materialNum = gy_materialNum;
	}

	public void setGy_materialNumb(String[] gy_materialNumb) {
		this.gy_materialNumb = gy_materialNumb;
	}

	public void setGy_materialType(String[] gy_materialType) {
		this.gy_materialType = gy_materialType;
	}

	public void setGy_memo(String[] gy_memo) {
		this.gy_memo = gy_memo;
	}

	public void setHighLimit(String highLimit) {
		this.highLimit = highLimit;
	}

	public void setIfBlockPrecognition(String ifBlockPrecognition) {
		this.ifBlockPrecognition = ifBlockPrecognition;
	}

	public void setIfFarmLand(String ifFarmLand) {
		this.ifFarmLand = ifFarmLand;
	}

	public void setIfFarmToUsed(String ifFarmToUsed) {
		this.ifFarmToUsed = ifFarmToUsed;
	}

	public void setIfGrantCrfa(String ifGrantCrfa) {
		this.ifGrantCrfa = ifGrantCrfa;
	}

	public void setIfGroupAssess(String ifGroupAssess) {
		this.ifGroupAssess = ifGroupAssess;
	}

	public void setIfHasProcedures(String ifHasProcedures) {
		this.ifHasProcedures = ifHasProcedures;
	}

	public void setIfProgectBuild(String ifProgectBuild) {
		this.ifProgectBuild = ifProgectBuild;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public void setInitPriceLmdJ(BigDecimal initPriceLmdJ) {
		this.initPriceLmdJ = initPriceLmdJ;
	}

	public void setInitType(String initType) {
		this.initType = initType;
	}

	public void setInvestAmount(String investAmount) {
		this.investAmount = investAmount;
	}

	public void setInvestStrength(BigDecimal investStrength) {
		this.investStrength = investStrength;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

	public void setIsPurposeBlock(String isPurposeBlock) {
		this.isPurposeBlock = isPurposeBlock;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public void setJsBankDeposit(String jsBankDeposit) {
		this.jsBankDeposit = jsBankDeposit;
	}

	public void setJsFdcCyns(BigDecimal jsFdcCyns) {
		this.jsFdcCyns = jsFdcCyns;
	}

	public void setJsFdcZzdj(String jsFdcZzdj) {
		this.jsFdcZzdj = jsFdcZzdj;
	}

	public void setJsJ3nZyywll(BigDecimal jsJ3nZyywll) {
		this.jsJ3nZyywll = jsJ3nZyywll;
	}

	public void setJsJ5nKfLpgs(BigDecimal jsJ5nKfLpgs) {
		this.jsJ5nKfLpgs = jsJ5nKfLpgs;
	}

	public void setJsJ5nKfTdmj(BigDecimal jsJ5nKfTdmj) {
		this.jsJ5nKfTdmj = jsJ5nKfTdmj;
	}

	public void setJsJ5nKfTdyt(String jsJ5nKfTdyt) {
		this.jsJ5nKfTdyt = jsJ5nKfTdyt;
	}

	public void setJsJjnLsCylx(String jsJjnLsCylx) {
		this.jsJjnLsCylx = jsJjnLsCylx;
	}

	public void setJsJzc(String jsJzc) {
		this.jsJzc = jsJzc;
	}

	public void setJsQyZxdj(String jsQyZxdj) {
		this.jsQyZxdj = jsQyZxdj;
	}

	public void setJsSyjzc(BigDecimal jsSyjzc) {
		this.jsSyjzc = jsSyjzc;
	}

	public void setLandId(String landId) {
		this.landId = landId;
	}

	public void setLandIdCollection(String landIdCollection) {
		this.landIdCollection = landIdCollection;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public void setLandSignPrice(BigDecimal landSignPrice) {
		this.landSignPrice = landSignPrice;
	}

	public void setLandUseExpirationDate(String landUseExpirationDate) {
		this.landUseExpirationDate = landUseExpirationDate;
	}

	public void setLandUseType(String landUseType) {
		this.landUseType = landUseType;
	}

	public void setLandUseTypes(String[] landUseTypes) {
		this.landUseTypes = landUseTypes;
	}

	public void setLayoutProperty(String layoutProperty) {
		this.layoutProperty = layoutProperty;
	}

	public void setLinkAddr(String linkAddr) {
		this.linkAddr = linkAddr;
	}

	public void setLinkPerson(String linkPerson) {
		this.linkPerson = linkPerson;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public void setLocalTradeType(String localTradeType) {
		this.localTradeType = localTradeType;
	}

	public void setMarginAccount(String marginAccount) {
		this.marginAccount = marginAccount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public void setMarginBank(String marginBank) {
		this.marginBank = marginBank;
	}

	public void setMarginEndDate(Date marginEndDate) {
		this.marginEndDate = marginEndDate;
	}

	public void setMarginLinker(String marginLinker) {
		this.marginLinker = marginLinker;
	}

	public void setMarginTel(String marginTel) {
		this.marginTel = marginTel;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setNewConstructionArea(String newConstructionArea) {
		this.newConstructionArea = newConstructionArea;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public void setNoitceNo(String noitceNo) {
		this.noitceNo = noitceNo;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public void setOtherBuildingCondition(String otherBuildingCondition) {
		this.otherBuildingCondition = otherBuildingCondition;
	}

	public void setOtherLandArea(BigDecimal otherLandArea) {
		this.otherLandArea = otherLandArea;
	}

	public void setOtherProvNumber(BigDecimal otherProvNumber) {
		this.otherProvNumber = otherProvNumber;
	}

	public void setOtherRequire(String otherRequire) {
		this.otherRequire = otherRequire;
	}

	public void setOwnershipReport(String ownershipReport) {
		this.ownershipReport = ownershipReport;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public void setPartIds(String[] partIds) {
		this.partIds = partIds;
	}

	public void setPayBatchs(String[] payBatchs) {
		this.payBatchs = payBatchs;
	}

	public void setPayProportions(String[] payProportions) {
		this.payProportions = payProportions;
	}

	public void setPayTimes(String[] payTimes) {
		this.payTimes = payTimes;
	}

	public void setPeitao(String peitao) {
		this.peitao = peitao;
	}

	public void setPersonalFile(String personalFile) {
		this.personalFile = personalFile;
	}

	public void setPgOrgGrade(String pgOrgGrade) {
		this.pgOrgGrade = pgOrgGrade;
	}

	public void setPgOrgName(String pgOrgName) {
		this.pgOrgName = pgOrgName;
	}

	public void setPingguDate(Date pingguDate) {
		this.pingguDate = pingguDate;
	}

	public void setPingguMethod(String pingguMethod) {
		this.pingguMethod = pingguMethod;
	}

	public void setPingguPerson(String pingguPerson) {
		this.pingguPerson = pingguPerson;
	}

	public void setPingguResult(String pingguResult) {
		this.pingguResult = pingguResult;
	}

	public void setPingguUnit(String pingguUnit) {
		this.pingguUnit = pingguUnit;
	}

	public void setPjlxs(String[] pjlxs) {
		this.pjlxs = pjlxs;
	}

	public void setPjxxInfoId(String pjxxInfoId) {
		this.pjxxInfoId = pjxxInfoId;
	}

	public void setPjxxMemo(String pjxxMemo) {
		this.pjxxMemo = pjxxMemo;
	}

	public void setPlanAuditedOrg(String planAuditedOrg) {
		this.planAuditedOrg = planAuditedOrg;
	}

	public void setPlanBuildingArea(BigDecimal planBuildingArea) {
		this.planBuildingArea = planBuildingArea;
	}

	public void setPlanBuildingAreas(String[] planBuildingAreas) {
		this.planBuildingAreas = planBuildingAreas;
	}

	public void setPlanDoc(String planDoc) {
		this.planDoc = planDoc;
	}

	public void setPlanNum(String planNum) {
		this.planNum = planNum;
	}

	public void setPlanUseMemo(String planUseMemo) {
		this.planUseMemo = planUseMemo;
	}

	public void setPriceComponent(String priceComponent) {
		this.priceComponent = priceComponent;
	}

	public void setPriceLimit(String priceLimit) {
		this.priceLimit = priceLimit;
	}

	public void setPriceXdfj(BigDecimal priceXdfj) {
		this.priceXdfj = priceXdfj;
	}

	public void setProgramAuditedOrg(String programAuditedOrg) {
		this.programAuditedOrg = programAuditedOrg;
	}

	public void setProgramNum(String programNum) {
		this.programNum = programNum;
	}

	public void setQqjcInfoId(String qqjcInfoId) {
		this.qqjcInfoId = qqjcInfoId;
	}

	public void setRangeEast(String rangeEast) {
		this.rangeEast = rangeEast;
	}

	public void setRangeNorth(String rangeNorth) {
		this.rangeNorth = rangeNorth;
	}

	public void setRangeSouth(String rangeSouth) {
		this.rangeSouth = rangeSouth;
	}

	public void setRangeWest(String rangeWest) {
		this.rangeWest = rangeWest;
	}

	public void setRemiseAccountReceivable(String remiseAccountReceivable) {
		this.remiseAccountReceivable = remiseAccountReceivable;
	}

	public void setRemiseBalance(String remiseBalance) {
		this.remiseBalance = remiseBalance;
	}

	public void setRemiseExpirationDate(String remiseExpirationDate) {
		this.remiseExpirationDate = remiseExpirationDate;
	}

	public void setRemisePerson(String remisePerson) {
		this.remisePerson = remisePerson;
	}

	public void setReserveDate(Date reserveDate) {
		this.reserveDate = reserveDate;
	}

	public void setReserveFileNum(String reserveFileNum) {
		this.reserveFileNum = reserveFileNum;
	}

	public void setResultCert(String resultCert) {
		this.resultCert = resultCert;
	}

	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public void setResultPrice(String resultPrice) {
		this.resultPrice = resultPrice;
	}

	public void setRetrieveFileNum(String retrieveFileNum) {
		this.retrieveFileNum = retrieveFileNum;
	}

	public void setRubbishTransportFee(BigDecimal rubbishTransportFee) {
		this.rubbishTransportFee = rubbishTransportFee;
	}

	public void setSaleOutput(String saleOutput) {
		this.saleOutput = saleOutput;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public void setSellYear(String sellYear) {
		this.sellYear = sellYear;
	}

	public void setServiceCharge(BigDecimal[] serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public void setSpecialPromise(String specialPromise) {
		this.specialPromise = specialPromise;
	}

	public void setSpotAddPriceRange(BigDecimal spotAddPriceRange) {
		this.spotAddPriceRange = spotAddPriceRange;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSubInfo(String subInfo) {
		this.subInfo = subInfo;
	}

	public void setSuppliedLandSum(String suppliedLandSum) {
		this.suppliedLandSum = suppliedLandSum;
	}

	public void setSurrenderTime(String surrenderTime) {
		this.surrenderTime = surrenderTime;
	}

	public void setSy_appIds(String[] sy_appIds) {
		this.sy_appIds = sy_appIds;
	}

	public void setSy_blockIds(String[] sy_blockIds) {
		this.sy_blockIds = sy_blockIds;
	}

	public void setSy_fileUrl(String[] sy_fileUrl) {
		this.sy_fileUrl = sy_fileUrl;
	}

	public void setSy_issueType(String[] sy_issueType) {
		this.sy_issueType = sy_issueType;
	}

	public void setSy_issueUnit(String[] sy_issueUnit) {
		this.sy_issueUnit = sy_issueUnit;
	}

	public void setSy_issueUnitMemo(String[] sy_issueUnitMemo) {
		this.sy_issueUnitMemo = sy_issueUnitMemo;
	}

	public void setSy_materialId(String[] sy_materialId) {
		this.sy_materialId = sy_materialId;
	}

	public void setSy_materialName(String[] sy_materialName) {
		this.sy_materialName = sy_materialName;
	}

	public void setSy_materialNum(String[] sy_materialNum) {
		this.sy_materialNum = sy_materialNum;
	}

	public void setSy_materialNumb(String[] sy_materialNumb) {
		this.sy_materialNumb = sy_materialNumb;
	}

	public void setSy_materialType(String[] sy_materialType) {
		this.sy_materialType = sy_materialType;
	}

	public void setSy_memo(String[] sy_memo) {
		this.sy_memo = sy_memo;
	}

	public void setTaxOutput(String taxOutput) {
		this.taxOutput = taxOutput;
	}

	public void setTblockId(String tblockId) {
		this.tblockId = tblockId;
	}

	public void setTblockNoticeNo(String tblockNoticeNo) {
		this.tblockNoticeNo = tblockNoticeNo;
	}

	public void setTextMemo(String textMemo) {
		this.textMemo = textMemo;
	}

	public void setTextOpen(String textOpen) {
		this.textOpen = textOpen;
	}

	public void setTimeDiff(String timeDiff) {
		this.timeDiff = timeDiff;
	}

	public void setTotalBuildingArea(BigDecimal totalBuildingArea) {
		this.totalBuildingArea = totalBuildingArea;
	}

	public void setTotalCityFacilityFee(BigDecimal totalCityFacilityFee) {
		this.totalCityFacilityFee = totalCityFacilityFee;
	}

	public void setTotalEducationFacilityFee(BigDecimal totalEducationFacilityFee) {
		this.totalEducationFacilityFee = totalEducationFacilityFee;
	}

	public void setTotalLandArea(BigDecimal totalLandArea) {
		this.totalLandArea = totalLandArea;
	}

	public void setTotalRubbishTransportFee(BigDecimal totalRubbishTransportFee) {
		this.totalRubbishTransportFee = totalRubbishTransportFee;
	}

	public void setTranGroundSitu(String tranGroundSitu) {
		this.tranGroundSitu = tranGroundSitu;
	}

	public void setTranResult(String tranResult) {
		this.tranResult = tranResult;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public void setTransferLife(BigDecimal transferLife) {
		this.transferLife = transferLife;
	}

	public void setTransferLifes(String[] transferLifes) {
		this.transferLifes = transferLifes;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public void setTresultCert(String tresultCert) {
		this.tresultCert = tresultCert;
	}

	public void setTresultDate(String tresultDate) {
		this.tresultDate = tresultDate;
	}

	public void setTresultName(String tresultName) {
		this.tresultName = tresultName;
	}

	public void setTresultPrice(String tresultPrice) {
		this.tresultPrice = tresultPrice;
	}

	public void setTtranResult(String ttranResult) {
		this.ttranResult = ttranResult;
	}

	public void setUblockId(String ublockId) {
		this.ublockId = ublockId;
	}

	public void setUnitFile(String unitFile) {
		this.unitFile = unitFile;
	}

	public void setUpset(String upset) {
		this.upset = upset;
	}

	public void setUpsetPrice(BigDecimal upsetPrice) {
		this.upsetPrice = upsetPrice;
	}

	public void setUseId(String useId) {
		this.useId = useId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setVolumeRate(String volumeRate) {
		this.volumeRate = volumeRate;
	}

	public void setVolumeRateBelow(String volumeRateBelow) {
		this.volumeRateBelow = volumeRateBelow;
	}

	public void setVolumeRates(String[] volumeRates) {
		this.volumeRates = volumeRates;
	}

	public void setVolumeRateSigns(String[] volumeRateSigns) {
		this.volumeRateSigns = volumeRateSigns;
	}

	public void setWaterCost(String waterCost) {
		this.waterCost = waterCost;
	}

	public void setYixiangPersonName(String yixiangPersonName) {
		this.yixiangPersonName = yixiangPersonName;
	}

	public String getDensity2() {
		return density2;
	}

	public void setDensity2(String density2) {
		this.density2 = density2;
	}

	public String getContain() {
		return contain;
	}

	public void setContain(String contain) {
		this.contain = contain;
	}

	public String getNotContain() {
		return notContain;
	}

	public void setNotContain(String notContain) {
		this.notContain = notContain;
	}

	public String getFeePayTimes() {
		return feePayTimes;
	}

	public void setFeePayTimes(String feePayTimes) {
		this.feePayTimes = feePayTimes;
	}

	public String getVolumeRateMemo() {
		return volumeRateMemo;
	}

	public void setVolumeRateMemo(String volumeRateMemo) {
		this.volumeRateMemo = volumeRateMemo;
	}

	public String getYixiangOrgNo() {
		return yixiangOrgNo;
	}

	public void setYixiangOrgNo(String yixiangOrgNo) {
		this.yixiangOrgNo = yixiangOrgNo;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	
}
