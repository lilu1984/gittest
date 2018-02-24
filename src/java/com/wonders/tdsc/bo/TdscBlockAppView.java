package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.wonders.esframework.common.bo.BaseBO;


/** @author Hibernate CodeGenerator */
public class TdscBlockAppView extends BaseBO {

    /** identifier field */
    private String appId;
    
    /** identifier field */
    private String blockId;

    /** nullable persistent field */
    private String blockName;

    /** nullable persistent field */
    private Long districtId;

    /** nullable persistent field */
    private String blockType;

    /** nullable persistent field */
    private String auditedNum;

    /** nullable persistent field */
    private Date auditedDate;

    /** nullable persistent field */
    private String blockLandId;

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
    private String transferLife;

    /** nullable persistent field */
    private Date transferDate;

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
    private String highLimit;

    /** nullable persistent field */
    private BigDecimal chamberAreaRatioLimit;

    /** nullable persistent field */
    private BigDecimal below90FlatRatio;

    /** nullable persistent field */
    private BigDecimal otherProvNumber;

    /** nullable persistent field */
    private String equipCondition;

    /** nullable persistent field */
    private String flatRate;

    /** nullable persistent field */
    private String currentSituationCondition;

    /** nullable persistent field */
    private String status;

    /** nullable persistent field */
    private String blockNoticeNo;

    /** nullable persistent field */
    private String noitceNo;

    /** nullable persistent field */
    private String remisePerson;

    /** nullable persistent field */
    private String endorseDistrict;

    /** nullable persistent field */
    private String transferMode;

    /** nullable persistent field */
    private String localTradeType;

    /** nullable persistent field */
    private BigDecimal marginAmount;

    /** nullable persistent field */
    private Timestamp marginEndDate;
    
    /** nullable persistent field */
    private Timestamp actionDateBlock;

    /** nullable persistent field */
    private String upset;

    /** nullable persistent field */
    private BigDecimal initPrice;

    /** nullable persistent field */
    private BigDecimal addPriceRange;

    /** nullable persistent field */
    private String tranResult;

    /** nullable persistent field */
    private Date resultDate;

    /** nullable persistent field */
    private BigDecimal resultPrice;

    /** 成交总价 */
    private BigDecimal totalPrice;
    
    /** nullable persistent field */
    private String resultCert;

    /** nullable persistent field */
    private String resultName;

    /** nullable persistent field */
    private String nodeId;

    /** nullable persistent field */
    private String statusId;

    /** nullable persistent field */
    private Date recMatStartDate;

    /** nullable persistent field */
    private Date recMatEndDate;

    /** nullable persistent field */
    private Date noticeStartDate;

    /** nullable persistent field */
    private Date noticeEndDate;

    /** nullable persistent field */
    private Date issueStartDate;

    /** nullable persistent field */
    private Date issueEndDate;

    /** nullable persistent field */
    private Date getFileStartDate;

    /** nullable persistent field */
    private Date getFileEndDate;

    /** nullable persistent field */
    private Timestamp inspDate;

    /** nullable persistent field */
    private String inspLoc;

    /** nullable persistent field */
    private Date gatherDate;

    /** nullable persistent field */
    private Date answerDate;

    /** nullable persistent field */
    private String answerLoc;

    /** nullable persistent field */
    private Date relFaqDate;

    /** nullable persistent field */
    private Timestamp accAppStatDate;

    /** nullable persistent field */
    private Timestamp accAppEndDate;

    /** nullable persistent field */
    private Date reviewDate;

    /** nullable persistent field */
    private String reviewLoc;

    /** nullable persistent field */
    private Date tenderStartDate;

    /** nullable persistent field */
    private Date tenderEndDate;

    /** nullable persistent field */
    private Date openingDate;

    /** nullable persistent field */
    private String openingLoc;

    /** nullable persistent field */
    private Date bidEvaDate;

    /** nullable persistent field */
    private String bidEvaLoc;

    /** nullable persistent field */
    private Timestamp auctionDate;

    /** nullable persistent field */
    private String auctionLoc;

    /** nullable persistent field */
    private Timestamp listStartDate;

    /** nullable persistent field */
    private Timestamp listEndDate;

    /** nullable persistent field */
    private Timestamp sceBidDate;

    /** nullable persistent field */
    private String sceBidLoc;

    /** nullable persistent field */
    private Date resultShowDate;

    /** nullable persistent field */
    private BigDecimal tenderMeetingNo;

    /** nullable persistent field */
    private BigDecimal openingMeetingNo;

    /** nullable persistent field */
    private BigDecimal bidEvaMeetingNo;

    /** nullable persistent field */
    private BigDecimal auctionMeetingNo;

    /** nullable persistent field */
    private BigDecimal listMeetingNo;

    /** nullable persistent field */
    private BigDecimal sceBidMeetingNo;

    /** nullable persistent field */
    private String listLoc;    
    
    private String nextAction;

    /** nullable persistent field */
    private String hasSelectCompere;

    /** nullable persistent field */
    private String hasSelectBNotary;

    /** nullable persistent field */
    private String hasSelectCNotary;

    /** nullable persistent field */
    private String hasSelectSpecialist;

	/** nullable persistent field */
	private String noticeId;
	
    private String tempStr;
    
    private String tempStr2;
    
    private Integer tempInt;

    private Long tempLong;

    private Double tempDouble;

    private Float tempFloat;

    private BigDecimal tempBigDecimal;

    private Date tempDate;

    private List tdscBlockUsedInfoList;
    
    /** identifier field */
    private String accessProductType;
    
    private String specialPromise;

    /** identifier field */
    private String timeDiff;
    
    /** nullable persistent field */
    private BigDecimal totalBuildingArea;
    
    /** 折算 亩 */
    private BigDecimal mu;
    
    /** nullable persistent field */
    private Date appDate;
    
    /** nullable persistent field */
    private String unitebBlockCode; 
    
    /** nullable persistent field */
    private String planId;
    
  //动工及竣工时间 BZ_DGJJGSJ
    private String bzDgjjgsj;
    
    //竞买资格及要求 BZ_JMZGJYQ
    private String bzJmzgjyq;
    
    //土地出让价款交纳要求 BZ_CRJKJNYQ
    private String bzCrjkjnyq;
    
    //规划设计要点 BZ_GHSJYD
    private String bzGhsjyd;
    
    //市政配套条件 BZ_SZPTTJ
    private String bzSzpttj;
    
    //土地交付条件 BZ_TDJFTJ
    private String bzTdjftj;
    
    //土地出让附件 BZ_TDCRFJ
    private String bzTdcrfj;
    
    //地块其他说明 BZ_DKQTSM
    private String bzDkqtsm;   
    
    //协办机构 BZ_XBJG
    private String bzXbjg;     
    
    //挂牌手续费 BZ_GPSXF
    private String bzGpsxf;     
    
    //拍卖佣金 BZ_PMYJ
    private String bzPmyj;      
    
    //备注 BZ_BEIZHU
    private String bzBeizhu;
    
    //招拍挂编号 TRADE_NUM
    private String tradeNum;
    
    /**竞买资格截止时间 JMZGQR_END_DATE */
    private Timestamp jmzgqrEndDate;
    
    /** 动作时间 */
    private Timestamp statusDate;
    
    /** CONTRACK_INFO_ID */
    private String contractInfoId;

    /** CONTRACK_SIGN_DATE */
    private Timestamp contractSignDate;
    
    /** ELECTRIC_NUM */
    private String electricNum;
    
    /** CONTRACK_NUM */
    private String contractNum;
    
    /** ACCEPT_PERSON */
    private String acceptPerson;
    
    /** BLOCK_REVIEW_DATE */
    private Timestamp blockReviewDate;
    
    /** TRADE_PAYMENT_DEMAND */
    private String tradePaymentDemand;
    
    /** PROJECT_START_DATE */
    private Timestamp projectStartDate;    
    
    /** PROJECT_END_DATE */
    private Timestamp projectEndDate;   
    
    /** CONTRACK_FILE_URL */
    private String contractFileUrl;
    
    /** CONTRACK_FILE_NAME */
    private String contractFileName;
    
    /** ZONGDI_NUM */
    private String zongDiNum;
    
    /** ACTION_DATE */
    private Timestamp actionDate;   
    
    /** ACTION_USER */
    private String actionUser;
    
    /** IF_VALIDITY */
    private String ifValidity;
    
    /** 公告地块序号 */
    private String xuHao;
    
    private String userId;
    
    private String landUseTypes;
    
    private String transferLifes;
    
    private String volumeRates;
    
    private String densitys;
    
    private String greeningRates;
    
    private String buildingHeight;
    
    private String sellYear;
    
    // 是否是有意向出让地块
    private String isPurposeBlock;

    private BigDecimal spotAddPriceRange;
    
    private BigDecimal cityFacilityFee;
    
    private BigDecimal totalCityFacilityFee;
    
    private BigDecimal educationFacilityFee;
    
    private BigDecimal totalEducationFacilityFee;
    
    private BigDecimal rubbishTransportFee;
    
    private BigDecimal totalRubbishTransportFee;
    
    private String blockQuality;
    
    //保证金账户
    private String marginBank;
    
    //账户名称
    private String accountName;
    
    //实际缴纳的交易服务费
    private BigDecimal paymentCost;
    //交易服务费缴纳时间
    private Date paymentDate;
    //交易服务费缴纳备注
    private String paymentMemo;
    //地质灾害评估费
    private BigDecimal geologicalHazard;
    
    private String tradeStatus;
    
    private String ifOnLine;
    
    private BigDecimal diJia;
    //统计用途
    private String countUse;
    //成交人竞买申请书Id(虚字段)
    private String jmsqFileId;
    //成交人联合竞买申请书Id(虚字段)
    private String lhjmFileId;
    

	// 是否是租赁模式
	private String isZulin;
	
	private BigDecimal maxPrice;
	private String partakeBidderConNum;
	
	public String getIsZulin() {
		return isZulin;
	}

	public void setIsZulin(String isZulin) {
		this.isZulin = isZulin;
	}
    
    public String getJmsqFileId() {
		return jmsqFileId;
	}

	public void setJmsqFileId(String jmsqFileId) {
		this.jmsqFileId = jmsqFileId;
	}

	public String getLhjmFileId() {
		return lhjmFileId;
	}

	public void setLhjmFileId(String lhjmFileId) {
		this.lhjmFileId = lhjmFileId;
	}

	public String getCountUse() {
		return countUse;
	}

	public void setCountUse(String countUse) {
		this.countUse = countUse;
	}

	public BigDecimal getDiJia() {
		return diJia;
	}

	public void setDiJia(BigDecimal diJia) {
		this.diJia = diJia;
	}

	public String getIfOnLine() {
		return ifOnLine;
	}

	public void setIfOnLine(String ifOnLine) {
		this.ifOnLine = ifOnLine;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public BigDecimal getGeologicalHazard() {
		return geologicalHazard;
	}

	public void setGeologicalHazard(BigDecimal geologicalHazard) {
		this.geologicalHazard = geologicalHazard;
	}

	public String getIsPurposeBlock() {
		return isPurposeBlock;
	}

	public void setIsPurposeBlock(String isPurposeBlock) {
		this.isPurposeBlock = isPurposeBlock;
	}

	public String getSellYear() {
		return sellYear;
	}

	public void setSellYear(String sellYear) {
		this.sellYear = sellYear;
	}

	public String getBuildingHeight() {
		return buildingHeight;
	}

	public void setBuildingHeight(String buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public String getDensitys() {
		return densitys;
	}

	public void setDensitys(String densitys) {
		this.densitys = densitys;
	}

	public String getGreeningRates() {
		return greeningRates;
	}

	public void setGreeningRates(String greeningRates) {
		this.greeningRates = greeningRates;
	}

	public String getTransferLifes() {
		return transferLifes;
	}

	public void setTransferLifes(String transferLifes) {
		this.transferLifes = transferLifes;
	}

	public String getVolumeRates() {
		return volumeRates;
	}

	public void setVolumeRates(String volumeRates) {
		this.volumeRates = volumeRates;
	}

	public String getLandUseTypes() {
		return landUseTypes;
	}

	public void setLandUseTypes(String landUseTypes) {
		this.landUseTypes = landUseTypes;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	/** default constructor */
    public TdscBlockAppView() {
    }
    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
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

    public String getBlockType() {
        return this.blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
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

    public String getBlockLandId() {
        return this.blockLandId;
    }

    public void setBlockLandId(String blockLandId) {
        this.blockLandId = blockLandId;
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

    public String getTransferLife() {
        return this.transferLife;
    }

    public void setTransferLife(String transferLife) {
        this.transferLife = transferLife;
    }

    public Date getTransferDate() {
        return this.transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getVolumeRate() {
        return this.volumeRate;
    }

    public void setVolumeRate(String volumeRate) {
        this.volumeRate = volumeRate;
    }

    public String getVolumeRateBelow() {
        return this.volumeRateBelow;
    }

    public void setVolumeRateBelow(String volumeRateBelow) {
        this.volumeRateBelow = volumeRateBelow;
    }

    public String getDensity() {
        return this.density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getDensityMin() {
        return this.densityMin;
    }

    public void setDensityMin(String densityMin) {
        this.densityMin = densityMin;
    }

    public String getGreeningRate() {
        return this.greeningRate;
    }

    public void setGreeningRate(String greeningRate) {
        this.greeningRate = greeningRate;
    }

    public String getGreeningRateCon() {
        return this.greeningRateCon;
    }

    public void setGreeningRateCon(String greeningRateCon) {
        this.greeningRateCon = greeningRateCon;
    }

    public String getHighLimit() {
        return this.highLimit;
    }

    public void setHighLimit(String highLimit) {
        this.highLimit = highLimit;
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

    public BigDecimal getOtherProvNumber() {
        return this.otherProvNumber;
    }

    public void setOtherProvNumber(BigDecimal otherProvNumber) {
        this.otherProvNumber = otherProvNumber;
    }

    public String getEquipCondition() {
        return this.equipCondition;
    }

    public void setEquipCondition(String equipCondition) {
        this.equipCondition = equipCondition;
    }

    public String getFlatRate() {
        return this.flatRate;
    }

    public void setFlatRate(String flatRate) {
        this.flatRate = flatRate;
    }

    public String getCurrentSituationCondition() {
        return this.currentSituationCondition;
    }

    public void setCurrentSituationCondition(String currentSituationCondition) {
        this.currentSituationCondition = currentSituationCondition;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRemisePerson() {
        return this.remisePerson;
    }

    public void setRemisePerson(String remisePerson) {
        this.remisePerson = remisePerson;
    }

    public String getEndorseDistrict() {
        return this.endorseDistrict;
    }

    public void setEndorseDistrict(String endorseDistrict) {
        this.endorseDistrict = endorseDistrict;
    }

    public String getTransferMode() {
        return this.transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getLocalTradeType() {
        return this.localTradeType;
    }

    public void setLocalTradeType(String localTradeType) {
        this.localTradeType = localTradeType;
    }

    public BigDecimal getMarginAmount() {
        return this.marginAmount;
    }

    public void setMarginAmount(BigDecimal marginAmount) {
        this.marginAmount = marginAmount;
    }

    public Timestamp getMarginEndDate() {
		return marginEndDate;
	}

	public void setMarginEndDate(Timestamp marginEndDate) {
		this.marginEndDate = marginEndDate;
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

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getStatusId() {
        return this.statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Date getRecMatStartDate() {
        return this.recMatStartDate;
    }

    public void setRecMatStartDate(Date recMatStartDate) {
        this.recMatStartDate = recMatStartDate;
    }

    public Date getRecMatEndDate() {
        return this.recMatEndDate;
    }

    public void setRecMatEndDate(Date recMatEndDate) {
        this.recMatEndDate = recMatEndDate;
    }

    public Date getNoticeStartDate() {
        return this.noticeStartDate;
    }

    public void setNoticeStartDate(Date noticeStartDate) {
        this.noticeStartDate = noticeStartDate;
    }

    public Date getNoticeEndDate() {
        return this.noticeEndDate;
    }

    public void setNoticeEndDate(Date noticeEndDate) {
        this.noticeEndDate = noticeEndDate;
    }

    public Date getIssueStartDate() {
        return this.issueStartDate;
    }

    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }

    public Date getIssueEndDate() {
        return this.issueEndDate;
    }

    public void setIssueEndDate(Date issueEndDate) {
        this.issueEndDate = issueEndDate;
    }

    public Date getGetFileStartDate() {
        return this.getFileStartDate;
    }

    public void setGetFileStartDate(Date getFileStartDate) {
        this.getFileStartDate = getFileStartDate;
    }

    public Date getGetFileEndDate() {
        return this.getFileEndDate;
    }

    public void setGetFileEndDate(Date getFileEndDate) {
        this.getFileEndDate = getFileEndDate;
    }

	public Timestamp getInspDate() {
		return inspDate;
	}

	public void setInspDate(Timestamp inspDate) {
		this.inspDate = inspDate;
	}

	public String getInspLoc() {
        return this.inspLoc;
    }

    public void setInspLoc(String inspLoc) {
        this.inspLoc = inspLoc;
    }

    public Date getGatherDate() {
        return this.gatherDate;
    }

    public void setGatherDate(Date gatherDate) {
        this.gatherDate = gatherDate;
    }

    public Date getAnswerDate() {
        return this.answerDate;
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = answerDate;
    }

    public String getAnswerLoc() {
        return this.answerLoc;
    }

    public void setAnswerLoc(String answerLoc) {
        this.answerLoc = answerLoc;
    }

    public Date getRelFaqDate() {
        return this.relFaqDate;
    }

    public void setRelFaqDate(Date relFaqDate) {
        this.relFaqDate = relFaqDate;
    }

    public Timestamp getAccAppStatDate() {
		return accAppStatDate;
	}

	public void setAccAppStatDate(Timestamp accAppStatDate) {
		this.accAppStatDate = accAppStatDate;
	}

	public Timestamp getAccAppEndDate() {
		return accAppEndDate;
	}

	public void setAccAppEndDate(Timestamp accAppEndDate) {
		this.accAppEndDate = accAppEndDate;
	}

	public Date getReviewDate() {
        return this.reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewLoc() {
        return this.reviewLoc;
    }

    public void setReviewLoc(String reviewLoc) {
        this.reviewLoc = reviewLoc;
    }

    public Date getTenderStartDate() {
        return this.tenderStartDate;
    }

    public void setTenderStartDate(Date tenderStartDate) {
        this.tenderStartDate = tenderStartDate;
    }

    public Date getTenderEndDate() {
        return this.tenderEndDate;
    }

    public void setTenderEndDate(Date tenderEndDate) {
        this.tenderEndDate = tenderEndDate;
    }

    public Date getOpeningDate() {
        return this.openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public String getOpeningLoc() {
        return this.openingLoc;
    }

    public void setOpeningLoc(String openingLoc) {
        this.openingLoc = openingLoc;
    }

    public Date getBidEvaDate() {
        return this.bidEvaDate;
    }

    public void setBidEvaDate(Date bidEvaDate) {
        this.bidEvaDate = bidEvaDate;
    }

    public String getBidEvaLoc() {
        return this.bidEvaLoc;
    }

    public void setBidEvaLoc(String bidEvaLoc) {
        this.bidEvaLoc = bidEvaLoc;
    }

    public String getAuctionLoc() {
        return this.auctionLoc;
    }

    public void setAuctionLoc(String auctionLoc) {
        this.auctionLoc = auctionLoc;
    }

    public Timestamp getListStartDate() {
		return listStartDate;
	}

	public void setListStartDate(Timestamp listStartDate) {
		this.listStartDate = listStartDate;
	}

	public Timestamp getListEndDate() {
		return listEndDate;
	}

	public void setListEndDate(Timestamp listEndDate) {
		this.listEndDate = listEndDate;
	}

	public String getSceBidLoc() {
        return this.sceBidLoc;
    }

    public void setSceBidLoc(String sceBidLoc) {
        this.sceBidLoc = sceBidLoc;
    }

    public Date getResultShowDate() {
        return this.resultShowDate;
    }

    public void setResultShowDate(Date resultShowDate) {
        this.resultShowDate = resultShowDate;
    }

    public BigDecimal getTenderMeetingNo() {
        return this.tenderMeetingNo;
    }

    public void setTenderMeetingNo(BigDecimal tenderMeetingNo) {
        this.tenderMeetingNo = tenderMeetingNo;
    }

    public BigDecimal getOpeningMeetingNo() {
        return this.openingMeetingNo;
    }

    public void setOpeningMeetingNo(BigDecimal openingMeetingNo) {
        this.openingMeetingNo = openingMeetingNo;
    }

    public BigDecimal getBidEvaMeetingNo() {
        return this.bidEvaMeetingNo;
    }

    public void setBidEvaMeetingNo(BigDecimal bidEvaMeetingNo) {
        this.bidEvaMeetingNo = bidEvaMeetingNo;
    }

    public BigDecimal getAuctionMeetingNo() {
        return this.auctionMeetingNo;
    }

    public void setAuctionMeetingNo(BigDecimal auctionMeetingNo) {
        this.auctionMeetingNo = auctionMeetingNo;
    }

    public BigDecimal getListMeetingNo() {
        return this.listMeetingNo;
    }

    public void setListMeetingNo(BigDecimal listMeetingNo) {
        this.listMeetingNo = listMeetingNo;
    }

    public BigDecimal getSceBidMeetingNo() {
        return this.sceBidMeetingNo;
    }

    public void setSceBidMeetingNo(BigDecimal sceBidMeetingNo) {
        this.sceBidMeetingNo = sceBidMeetingNo;
    }

    public String getListLoc() {
        return this.listLoc;
    }

    public void setListLoc(String listLoc) {
        this.listLoc = listLoc;
    }

	public List getTdscBlockUsedInfoList() {
		return tdscBlockUsedInfoList;
	}

	public void setTdscBlockUsedInfoList(List tdscBlockUsedInfoList) {
		this.tdscBlockUsedInfoList = tdscBlockUsedInfoList;
	}

	public BigDecimal getTempBigDecimal() {
		return tempBigDecimal;
	}

	public void setTempBigDecimal(BigDecimal tempBigDecimal) {
		this.tempBigDecimal = tempBigDecimal;
	}

	public Date getTempDate() {
		return tempDate;
	}

	public void setTempDate(Date tempDate) {
		this.tempDate = tempDate;
	}

	public Double getTempDouble() {
		return tempDouble;
	}

	public void setTempDouble(Double tempDouble) {
		this.tempDouble = tempDouble;
	}

	public Float getTempFloat() {
		return tempFloat;
	}

	public void setTempFloat(Float tempFloat) {
		this.tempFloat = tempFloat;
	}

	public Integer getTempInt() {
		return tempInt;
	}

	public void setTempInt(Integer tempInt) {
		this.tempInt = tempInt;
	}

	public Long getTempLong() {
		return tempLong;
	}

	public void setTempLong(Long tempLong) {
		this.tempLong = tempLong;
	}

	public String getTempStr() {
		return tempStr;
	}

	public void setTempStr(String tempStr) {
		this.tempStr = tempStr;
	}

	public String getTempStr2() {
		return tempStr2;
	}

	public void setTempStr2(String tempStr2) {
		this.tempStr2 = tempStr2;
	}

	public String getAccessProductType() {
		return accessProductType;
	}

	public void setAccessProductType(String accessProductType) {
		this.accessProductType = accessProductType;
	}

	public String getSpecialPromise() {
		return specialPromise;
	}

	public void setSpecialPromise(String specialPromise) {
		this.specialPromise = specialPromise;
	}

	public String getTimeDiff() {
		return timeDiff;
	}

	public void setTimeDiff(String timeDiff) {
		this.timeDiff = timeDiff;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public BigDecimal getTotalBuildingArea() {
		return totalBuildingArea;
	}

	public void setTotalBuildingArea(BigDecimal totalBuildingArea) {
		this.totalBuildingArea = totalBuildingArea;
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

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getUnitebBlockCode() {
		return unitebBlockCode;
	}

	public void setUnitebBlockCode(String unitebBlockCode) {
		this.unitebBlockCode = unitebBlockCode;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public Timestamp getAuctionDate() {
		return auctionDate;
	}

	public void setAuctionDate(Timestamp auctionDate) {
		this.auctionDate = auctionDate;
	}

	public Timestamp getSceBidDate() {
		return sceBidDate;
	}

	public void setSceBidDate(Timestamp sceBidDate) {
		this.sceBidDate = sceBidDate;
	}

	public String getContractInfoId() {
		return contractInfoId;
	}

	public void setContractInfoId(String contractInfoId) {
		this.contractInfoId = contractInfoId;
	}

	public Timestamp getContractSignDate() {
		return contractSignDate;
	}

	public void setContractSignDate(Timestamp contractSignDate) {
		this.contractSignDate = contractSignDate;
	}

	public String getElectricNum() {
		return electricNum;
	}

	public void setElectricNum(String electricNum) {
		this.electricNum = electricNum;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getAcceptPerson() {
		return acceptPerson;
	}

	public void setAcceptPerson(String acceptPerson) {
		this.acceptPerson = acceptPerson;
	}

	public Timestamp getBlockReviewDate() {
		return blockReviewDate;
	}

	public void setBlockReviewDate(Timestamp blockReviewDate) {
		this.blockReviewDate = blockReviewDate;
	}

	public String getTradePaymentDemand() {
		return tradePaymentDemand;
	}

	public void setTradePaymentDemand(String tradePaymentDemand) {
		this.tradePaymentDemand = tradePaymentDemand;
	}

	public Timestamp getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(Timestamp projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public Timestamp getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(Timestamp projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public String getContractFileUrl() {
		return contractFileUrl;
	}

	public void setContractFileUrl(String contractFileUrl) {
		this.contractFileUrl = contractFileUrl;
	}

	public String getContractFileName() {
		return contractFileName;
	}

	public void setContractFileName(String contractFileName) {
		this.contractFileName = contractFileName;
	}

	public String getZongDiNum() {
		return zongDiNum;
	}

	public void setZongDiNum(String zongDiNum) {
		this.zongDiNum = zongDiNum;
	}

	public Timestamp getActionDate() {
		return actionDate;
	}

	public void setActionDate(Timestamp actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getIfValidity() {
		return ifValidity;
	}

	public void setIfValidity(String ifValidity) {
		this.ifValidity = ifValidity;
	}

	public BigDecimal getMu() {
		return mu;
	}

	public void setMu(BigDecimal mu) {
		this.mu = mu;
	}

	public Timestamp getJmzgqrEndDate() {
		return jmzgqrEndDate;
	}

	public void setJmzgqrEndDate(Timestamp jmzgqrEndDate) {
		this.jmzgqrEndDate = jmzgqrEndDate;
	}

	public Timestamp getActionDateBlock() {
		return actionDateBlock;
	}

	public void setActionDateBlock(Timestamp actionDateBlock) {
		this.actionDateBlock = actionDateBlock;
	}

	public BigDecimal getSpotAddPriceRange() {
		return spotAddPriceRange;
	}

	public void setSpotAddPriceRange(BigDecimal spotAddPriceRange) {
		this.spotAddPriceRange = spotAddPriceRange;
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

	public String getBlockQuality() {
		return blockQuality;
	}

	public void setBlockQuality(String blockQuality) {
		this.blockQuality = blockQuality;
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

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setPartakeBidderConNum(String partakeBidderConNum) {
		this.partakeBidderConNum = partakeBidderConNum;
	}

	public String getPartakeBidderConNum() {
		return partakeBidderConNum;
	}

}
