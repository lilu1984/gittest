package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscBlockTranApp extends BaseBO {

	/** identifier field */
	private String appId;

	/** nullable persistent field */
	private String blockId;

	/** nullable persistent field */
	private String noticeId;

	/** nullable persistent field */
	private String blockNoticeNo;

	/** nullable persistent field */
	private String noitceNo;

	/** nullable persistent field */
	private BigDecimal marginAmount;

	/** nullable persistent field */
	private String marginAccount;

	/** nullable persistent field */
	private String marginLinker;

	/** nullable persistent field */
	private String marginTel;

	/** nullable persistent field */
	private Timestamp marginEndDate;

	/** nullable persistent field */
	private String accessProductType;

	/** nullable persistent field */
	private String investAmount;

	/** nullable persistent field */
	private String fixInvestAmount;

	/** nullable persistent field */
	private String energyCost;

	/** nullable persistent field */
	private String localTradeType;

	private String textMemo;

	private String specialPromise;

	/** nullable persistent field */
	private String waterCost;

	/** nullable persistent field */
	private String saleOutput;

	/** nullable persistent field */
	private String taxOutput;

	/** nullable persistent field */
	private BigDecimal employeeNum;

	/** nullable persistent field */
	private String otherRequire;

	/** nullable persistent field */
	private String domesticOwnerComp;

	/** nullable persistent field */
	private String domesticCreditLevel;

	/** nullable persistent field */
	private BigDecimal domesticSameTypeTime;

	/** nullable persistent field */
	private BigDecimal domestic3yearProfit;

	/** nullable persistent field */
	private BigDecimal domesticJointPercent;

	/** nullable persistent field */
	private String abroadOwnerComp;

	/** nullable persistent field */
	private BigDecimal abroadBankDeposit;

	/** nullable persistent field */
	private String abroadCreditLevel;

	/** nullable persistent field */
	private BigDecimal abroadSameTypeTime;

	/** nullable persistent field */
	private BigDecimal abroad3yearProfit;

	/** nullable persistent field */
	private BigDecimal abroadJointPercent;

	/** nullable persistent field */
	private String upset;

	/** nullable persistent field */
	private BigDecimal initPrice;

	/** nullable persistent field */
	private BigDecimal addPriceRange;

	/** nullable persistent field */
	private String linkAddr;

	/** nullable persistent field */
	private String linkTel;

	/** nullable persistent field */
	private String linkPerson;

	/** nullable persistent field */
	private BigDecimal appSupNum;

	/** nullable persistent field */
	private String tranResult;

	/** nullable persistent field */
	private Timestamp resultDate;

	/** nullable persistent field */
	private BigDecimal resultPrice;

	/** nullable persistent field */
	private BigDecimal totalPrice;

	/** nullable persistent field */
	private String resultCert;

	/** nullable persistent field */
	private String resultName;

	/** nullable persistent field */
	private String initType;

	/** nullable persistent field */
	private String analysisReportUrl;

	/** nullable persistent field */
	private String analysisReportPerson;

	/** nullable persistent field */
	private Date analysisReportDate;

	/** nullable persistent field */
	private String jsJ5nKfTdyt;

	/** nullable persistent field */
	private BigDecimal jsJ5nKfTdmj;

	/** nullable persistent field */
	private Integer jsJ5nKfLpgs;

	/** nullable persistent field */
	private String jsJjnLsCylx;

	/** nullable persistent field */
	private String jsFdcZzdj;

	/** nullable persistent field */
	private Integer jsFdcCyns;

	/** nullable persistent field */
	private BigDecimal jsJ3nZyywll;

	/** nullable persistent field */
	private String jsQyZxdj;

	/** nullable persistent field */
	private BigDecimal jsSyjzc;

	/** nullable persistent field */
	private String remisePerson;

	/** nullable persistent field */
	private String timeDiff;

	/** nullable persistent field */
	private String jsJzc;

	/** nullable persistent field */
	private String jsBankDeposit;

	/** nullable persistent field */
	private String endorseDistrict;

	/** nullable persistent field */
	private String transferMode;

	/** nullable persistent field */
	private String hasSelectCompere;

	/** nullable persistent field */
	private String hasSelectBNotary;

	/** nullable persistent field */
	private String hasSelectCNotary;

	/** nullable persistent field */
	private String hasSelectSpecialist;

	/** nullable persistent field */
	private String ifPublish;

	/** nullable persistent field */
	private Date publishDate;

	// 起叫价(楼面地价)
	private BigDecimal initPriceLmdJ;

	// 评估楼面地价
	private BigDecimal evaluteFloorLandValue;

	// 评估土地单价
	private BigDecimal landSignPrice;

	// 进度安排表ID
	private String planId;

	// 动工及竣工时间 BZ_DGJJGSJ
	private String bzDgjjgsj;

	// 竞买资格及要求 BZ_JMZGJYQ
	private String bzJmzgjyq;

	// 土地出让价款交纳要求 BZ_CRJKJNYQ
	private String bzCrjkjnyq;

	// 规划设计要点 BZ_GHSJYD
	private String bzGhsjyd;

	// 市政配套条件 BZ_SZPTTJ
	private String bzSzpttj;

	// 土地交付条件 BZ_TDJFTJ
	private String bzTdjftj;

	// 土地出让附件 BZ_TDCRFJ
	private String bzTdcrfj;

	// 地块其他说明 BZ_DKQTSM
	private String bzDkqtsm;

	// 协办机构 BZ_XBJG
	private String bzXbjg;

	// 挂牌手续费 BZ_GPSXF
	private String bzGpsxf;

	// 拍卖佣金 BZ_PMYJ
	private String bzPmyj;

	// 备注 BZ_BEIZHU
	private String bzBeizhu;

	// 公告地块序号
	private String xuHao;

	// 是否是有意向出让地块
	private String isPurposeBlock;

	// 投资强度
	private BigDecimal investStrength;

	private String pingguUnit;

	private String pingguPerson;

	private Date pingguDate;

	private String pingguMethod;

	private String pingguResult;

	private String guihuaCertNo;

	private String blockCatalog;

	private BigDecimal spotAddPriceRange;

	// 实际缴纳的交易服务费
	private BigDecimal paymentCost;

	// 交易服务费缴纳时间
	private Date paymentDate;

	// 交易服务费缴纳备注
	private String paymentMemo;

	private String tradeStatus;

	// 起始价含费用项
	private String contain;

	// 起始价不含费用项
	private String notContain;
	//底价
	private BigDecimal diJia;
	

	// 是否是租赁模式
	private String isZulin;

	/**
	 * 最高限价
	 */
	private BigDecimal maxPrice;
	/**
	 * 参与的竞买人号牌(如:1,5,8)
	 */
	private String partakeBidderConNum;
	
	
	public String getPartakeBidderConNum() {
		return partakeBidderConNum;
	}

	public void setPartakeBidderConNum(String partakeBidderConNum) {
		this.partakeBidderConNum = partakeBidderConNum;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getIsZulin() {
		return isZulin;
	}

	public void setIsZulin(String isZulin) {
		this.isZulin = isZulin;
	}

	public BigDecimal getDiJia() {
		return diJia;
	}

	public void setDiJia(BigDecimal diJia) {
		this.diJia = diJia;
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

	public String getBlockCatalog() {
		return blockCatalog;
	}

	public void setBlockCatalog(String blockCatalog) {
		this.blockCatalog = blockCatalog;
	}

	public String getGuihuaCertNo() {
		return guihuaCertNo;
	}

	public void setGuihuaCertNo(String guihuaCertNo) {
		this.guihuaCertNo = guihuaCertNo;
	}

	public Date getPingguDate() {
		return pingguDate;
	}

	public void setPingguDate(Date pingguDate) {
		this.pingguDate = pingguDate;
	}

	public String getPingguMethod() {
		return pingguMethod;
	}

	public void setPingguMethod(String pingguMethod) {
		this.pingguMethod = pingguMethod;
	}

	public String getPingguPerson() {
		return pingguPerson;
	}

	public void setPingguPerson(String pingguPerson) {
		this.pingguPerson = pingguPerson;
	}

	public String getPingguResult() {
		return pingguResult;
	}

	public void setPingguResult(String pingguResult) {
		this.pingguResult = pingguResult;
	}

	public String getPingguUnit() {
		return pingguUnit;
	}

	public void setPingguUnit(String pingguUnit) {
		this.pingguUnit = pingguUnit;
	}

	public BigDecimal getInvestStrength() {
		return investStrength;
	}

	public void setInvestStrength(BigDecimal investStrength) {
		this.investStrength = investStrength;
	}

	public String getXuHao() {
		return xuHao;
	}

	public void setXuHao(String xuHao) {
		this.xuHao = xuHao;
	}

	public String getBzDgjjgsj() {
		return bzDgjjgsj;
	}

	public void setBzDgjjgsj(String bzDgjjgsj) {
		this.bzDgjjgsj = bzDgjjgsj;
	}

	public String getBzJmzgjyq() {
		return bzJmzgjyq;
	}

	public void setBzJmzgjyq(String bzJmzgjyq) {
		this.bzJmzgjyq = bzJmzgjyq;
	}

	public String getBzCrjkjnyq() {
		return bzCrjkjnyq;
	}

	public void setBzCrjkjnyq(String bzCrjkjnyq) {
		this.bzCrjkjnyq = bzCrjkjnyq;
	}

	public String getBzGhsjyd() {
		return bzGhsjyd;
	}

	public void setBzGhsjyd(String bzGhsjyd) {
		this.bzGhsjyd = bzGhsjyd;
	}

	public String getBzSzpttj() {
		return bzSzpttj;
	}

	public void setBzSzpttj(String bzSzpttj) {
		this.bzSzpttj = bzSzpttj;
	}

	public String getBzTdjftj() {
		return bzTdjftj;
	}

	public void setBzTdjftj(String bzTdjftj) {
		this.bzTdjftj = bzTdjftj;
	}

	public String getBzTdcrfj() {
		return bzTdcrfj;
	}

	public void setBzTdcrfj(String bzTdcrfj) {
		this.bzTdcrfj = bzTdcrfj;
	}

	public String getBzDkqtsm() {
		return bzDkqtsm;
	}

	public void setBzDkqtsm(String bzDkqtsm) {
		this.bzDkqtsm = bzDkqtsm;
	}

	public String getBzXbjg() {
		return bzXbjg;
	}

	public void setBzXbjg(String bzXbjg) {
		this.bzXbjg = bzXbjg;
	}

	public String getBzGpsxf() {
		return bzGpsxf;
	}

	public void setBzGpsxf(String bzGpsxf) {
		this.bzGpsxf = bzGpsxf;
	}

	public String getBzPmyj() {
		return bzPmyj;
	}

	public void setBzPmyj(String bzPmyj) {
		this.bzPmyj = bzPmyj;
	}

	public String getBzBeizhu() {
		return bzBeizhu;
	}

	public void setBzBeizhu(String bzBeizhu) {
		this.bzBeizhu = bzBeizhu;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getIfPublish() {
		return ifPublish;
	}

	public void setIfPublish(String ifPublish) {
		this.ifPublish = ifPublish;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getEndorseDistrict() {
		return endorseDistrict;
	}

	public void setEndorseDistrict(String endorseDistrict) {
		this.endorseDistrict = endorseDistrict;
	}

	/** default constructor */
	public TdscBlockTranApp() {
	}

	/** minimal constructor */
	public TdscBlockTranApp(String appId) {
		this.appId = appId;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getBlockNoticeNo() {
		return this.blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getNoitceNo() {
		return this.noitceNo;
	}

	public void setNoitceNo(String noitceNo) {
		this.noitceNo = noitceNo;
	}

	public BigDecimal getMarginAmount() {
		return this.marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public String getMarginAccount() {
		return this.marginAccount;
	}

	public void setMarginAccount(String marginAccount) {
		this.marginAccount = marginAccount;
	}

	public String getMarginLinker() {
		return this.marginLinker;
	}

	public void setMarginLinker(String marginLinker) {
		this.marginLinker = marginLinker;
	}

	public String getMarginTel() {
		return this.marginTel;
	}

	public void setMarginTel(String marginTel) {
		this.marginTel = marginTel;
	}

	public Timestamp getMarginEndDate() {
		return marginEndDate;
	}

	public void setMarginEndDate(Timestamp marginEndDate) {
		this.marginEndDate = marginEndDate;
	}

	public String getAccessProductType() {
		return this.accessProductType;
	}

	public void setAccessProductType(String accessProductType) {
		this.accessProductType = accessProductType;
	}

	public String getInvestAmount() {
		return this.investAmount;
	}

	public void setInvestAmount(String investAmount) {
		this.investAmount = investAmount;
	}

	public String getFixInvestAmount() {
		return this.fixInvestAmount;
	}

	public void setFixInvestAmount(String fixInvestAmount) {
		this.fixInvestAmount = fixInvestAmount;
	}

	public String getEnergyCost() {
		return this.energyCost;
	}

	public void setEnergyCost(String energyCost) {
		this.energyCost = energyCost;
	}

	public String getWaterCost() {
		return this.waterCost;
	}

	public void setWaterCost(String waterCost) {
		this.waterCost = waterCost;
	}

	public String getSaleOutput() {
		return this.saleOutput;
	}

	public void setSaleOutput(String saleOutput) {
		this.saleOutput = saleOutput;
	}

	public String getTaxOutput() {
		return this.taxOutput;
	}

	public void setTaxOutput(String taxOutput) {
		this.taxOutput = taxOutput;
	}

	public BigDecimal getEmployeeNum() {
		return this.employeeNum;
	}

	public void setEmployeeNum(BigDecimal employeeNum) {
		this.employeeNum = employeeNum;
	}

	public String getOtherRequire() {
		return this.otherRequire;
	}

	public void setOtherRequire(String otherRequire) {
		this.otherRequire = otherRequire;
	}

	public String getDomesticOwnerComp() {
		return this.domesticOwnerComp;
	}

	public void setDomesticOwnerComp(String domesticOwnerComp) {
		this.domesticOwnerComp = domesticOwnerComp;
	}

	public String getDomesticCreditLevel() {
		return this.domesticCreditLevel;
	}

	public void setDomesticCreditLevel(String domesticCreditLevel) {
		this.domesticCreditLevel = domesticCreditLevel;
	}

	public BigDecimal getDomesticSameTypeTime() {
		return this.domesticSameTypeTime;
	}

	public void setDomesticSameTypeTime(BigDecimal domesticSameTypeTime) {
		this.domesticSameTypeTime = domesticSameTypeTime;
	}

	public BigDecimal getDomestic3yearProfit() {
		return this.domestic3yearProfit;
	}

	public void setDomestic3yearProfit(BigDecimal domestic3yearProfit) {
		this.domestic3yearProfit = domestic3yearProfit;
	}

	public BigDecimal getDomesticJointPercent() {
		return this.domesticJointPercent;
	}

	public void setDomesticJointPercent(BigDecimal domesticJointPercent) {
		this.domesticJointPercent = domesticJointPercent;
	}

	public String getAbroadOwnerComp() {
		return this.abroadOwnerComp;
	}

	public void setAbroadOwnerComp(String abroadOwnerComp) {
		this.abroadOwnerComp = abroadOwnerComp;
	}

	public BigDecimal getAbroadBankDeposit() {
		return this.abroadBankDeposit;
	}

	public void setAbroadBankDeposit(BigDecimal abroadBankDeposit) {
		this.abroadBankDeposit = abroadBankDeposit;
	}

	public String getAbroadCreditLevel() {
		return this.abroadCreditLevel;
	}

	public void setAbroadCreditLevel(String abroadCreditLevel) {
		this.abroadCreditLevel = abroadCreditLevel;
	}

	public BigDecimal getAbroadSameTypeTime() {
		return this.abroadSameTypeTime;
	}

	public void setAbroadSameTypeTime(BigDecimal abroadSameTypeTime) {
		this.abroadSameTypeTime = abroadSameTypeTime;
	}

	public BigDecimal getAbroad3yearProfit() {
		return this.abroad3yearProfit;
	}

	public void setAbroad3yearProfit(BigDecimal abroad3yearProfit) {
		this.abroad3yearProfit = abroad3yearProfit;
	}

	public BigDecimal getAbroadJointPercent() {
		return this.abroadJointPercent;
	}

	public void setAbroadJointPercent(BigDecimal abroadJointPercent) {
		this.abroadJointPercent = abroadJointPercent;
	}

	public String getUpset() {
		return this.upset;
	}

	public void setUpset(String upset) {
		this.upset = upset;
	}

	public BigDecimal getInitPrice() {
		return this.initPrice;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public BigDecimal getAddPriceRange() {
		return this.addPriceRange;
	}

	public void setAddPriceRange(BigDecimal addPriceRange) {
		this.addPriceRange = addPriceRange;
	}

	public String getLinkAddr() {
		return this.linkAddr;
	}

	public void setLinkAddr(String linkAddr) {
		this.linkAddr = linkAddr;
	}

	public String getLinkTel() {
		return this.linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkPerson() {
		return this.linkPerson;
	}

	public void setLinkPerson(String linkPerson) {
		this.linkPerson = linkPerson;
	}

	public BigDecimal getAppSupNum() {
		return this.appSupNum;
	}

	public void setAppSupNum(BigDecimal appSupNum) {
		this.appSupNum = appSupNum;
	}

	public String getTranResult() {
		return this.tranResult;
	}

	public void setTranResult(String tranResult) {
		this.tranResult = tranResult;
	}

	public Timestamp getResultDate() {
		return resultDate;
	}

	public void setResultDate(Timestamp resultDate) {
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

	public String getInitType() {
		return this.initType;
	}

	public void setInitType(String initType) {
		this.initType = initType;
	}

	public String getAnalysisReportUrl() {
		return this.analysisReportUrl;
	}

	public void setAnalysisReportUrl(String analysisReportUrl) {
		this.analysisReportUrl = analysisReportUrl;
	}

	public String getAnalysisReportPerson() {
		return this.analysisReportPerson;
	}

	public void setAnalysisReportPerson(String analysisReportPerson) {
		this.analysisReportPerson = analysisReportPerson;
	}

	public Date getAnalysisReportDate() {
		return this.analysisReportDate;
	}

	public void setAnalysisReportDate(Date analysisReportDate) {
		this.analysisReportDate = analysisReportDate;
	}

	public String getJsJ5nKfTdyt() {
		return this.jsJ5nKfTdyt;
	}

	public void setJsJ5nKfTdyt(String jsJ5nKfTdyt) {
		this.jsJ5nKfTdyt = jsJ5nKfTdyt;
	}

	public BigDecimal getJsJ5nKfTdmj() {
		return this.jsJ5nKfTdmj;
	}

	public void setJsJ5nKfTdmj(BigDecimal jsJ5nKfTdmj) {
		this.jsJ5nKfTdmj = jsJ5nKfTdmj;
	}

	public Integer getJsJ5nKfLpgs() {
		return this.jsJ5nKfLpgs;
	}

	public void setJsJ5nKfLpgs(Integer jsJ5nKfLpgs) {
		this.jsJ5nKfLpgs = jsJ5nKfLpgs;
	}

	public String getJsJjnLsCylx() {
		return this.jsJjnLsCylx;
	}

	public void setJsJjnLsCylx(String jsJjnLsCylx) {
		this.jsJjnLsCylx = jsJjnLsCylx;
	}

	public String getJsFdcZzdj() {
		return this.jsFdcZzdj;
	}

	public void setJsFdcZzdj(String jsFdcZzdj) {
		this.jsFdcZzdj = jsFdcZzdj;
	}

	public Integer getJsFdcCyns() {
		return this.jsFdcCyns;
	}

	public void setJsFdcCyns(Integer jsFdcCyns) {
		this.jsFdcCyns = jsFdcCyns;
	}

	public BigDecimal getJsJ3nZyywll() {
		return this.jsJ3nZyywll;
	}

	public void setJsJ3nZyywll(BigDecimal jsJ3nZyywll) {
		this.jsJ3nZyywll = jsJ3nZyywll;
	}

	public String getJsQyZxdj() {
		return this.jsQyZxdj;
	}

	public void setJsQyZxdj(String jsQyZxdj) {
		this.jsQyZxdj = jsQyZxdj;
	}

	public BigDecimal getJsSyjzc() {
		return this.jsSyjzc;
	}

	public void setJsSyjzc(BigDecimal jsSyjzc) {
		this.jsSyjzc = jsSyjzc;
	}

	public String getRemisePerson() {
		return remisePerson;
	}

	public void setRemisePerson(String remisePerson) {
		this.remisePerson = remisePerson;
	}

	public String getTimeDiff() {
		return timeDiff;
	}

	public void setTimeDiff(String timeDiff) {
		this.timeDiff = timeDiff;
	}

	public String getJsBankDeposit() {
		return jsBankDeposit;
	}

	public void setJsBankDeposit(String jsBankDeposit) {
		this.jsBankDeposit = jsBankDeposit;
	}

	public String getJsJzc() {
		return jsJzc;
	}

	public void setJsJzc(String jsJzc) {
		this.jsJzc = jsJzc;
	}

	public String getTransferMode() {
		return this.transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getHasSelectBNotary() {
		return hasSelectBNotary;
	}

	public void setHasSelectBNotary(String hasSelectBNotary) {
		this.hasSelectBNotary = hasSelectBNotary;
	}

	public String getHasSelectCNotary() {
		return hasSelectCNotary;
	}

	public void setHasSelectCNotary(String hasSelectCNotary) {
		this.hasSelectCNotary = hasSelectCNotary;
	}

	public String getHasSelectCompere() {
		return hasSelectCompere;
	}

	public void setHasSelectCompere(String hasSelectCompere) {
		this.hasSelectCompere = hasSelectCompere;
	}

	public String getHasSelectSpecialist() {
		return hasSelectSpecialist;
	}

	public void setHasSelectSpecialist(String hasSelectSpecialist) {
		this.hasSelectSpecialist = hasSelectSpecialist;
	}

	public String getSpecialPromise() {
		return specialPromise;
	}

	public void setSpecialPromise(String specialPromise) {
		this.specialPromise = specialPromise;
	}

	public String getTextMemo() {
		return textMemo;
	}

	public void setTextMemo(String textMemo) {
		this.textMemo = textMemo;
	}

	public String getLocalTradeType() {
		return localTradeType;
	}

	public void setLocalTradeType(String localTradeType) {
		this.localTradeType = localTradeType;
	}

	public BigDecimal getEvaluteFloorLandValue() {
		return evaluteFloorLandValue;
	}

	public void setEvaluteFloorLandValue(BigDecimal evaluteFloorLandValue) {
		this.evaluteFloorLandValue = evaluteFloorLandValue;
	}

	public BigDecimal getInitPriceLmdJ() {
		return initPriceLmdJ;
	}

	public void setInitPriceLmdJ(BigDecimal initPriceLmdJ) {
		this.initPriceLmdJ = initPriceLmdJ;
	}

	public BigDecimal getLandSignPrice() {
		return landSignPrice;
	}

	public void setLandSignPrice(BigDecimal landSignPrice) {
		this.landSignPrice = landSignPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getIsPurposeBlock() {
		return isPurposeBlock;
	}

	public void setIsPurposeBlock(String isPurposeBlock) {
		this.isPurposeBlock = isPurposeBlock;
	}

	public BigDecimal getSpotAddPriceRange() {
		return spotAddPriceRange;
	}

	public void setSpotAddPriceRange(BigDecimal spotAddPriceRange) {
		this.spotAddPriceRange = spotAddPriceRange;
	}

	public BigDecimal getPaymentCost() {
		return paymentCost;
	}

	public void setPaymentCost(BigDecimal paymentCost) {
		this.paymentCost = paymentCost;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentMemo() {
		return paymentMemo;
	}

	public void setPaymentMemo(String paymentMemo) {
		this.paymentMemo = paymentMemo;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((appId == null) ? 0 : appId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TdscBlockTranApp other = (TdscBlockTranApp) obj;
		if (appId == null) {
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		return true;
	}

}
