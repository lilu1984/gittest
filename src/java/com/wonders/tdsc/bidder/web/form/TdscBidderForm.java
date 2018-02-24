package com.wonders.tdsc.bidder.web.form;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts.action.ActionForm;

public class TdscBidderForm extends ActionForm {

	private String			isAccptSms;

	private String			pushSmsPhone;

	// 保证金信息ID
	private String			bankAppIds;

	// 公告号
	private String			noticeNo;

	// 如果是意向人，存储意向人 意向地块 App_id
	private String			purposeAppId;

	private String			currentPage;

	// 是否意向竞买人
	private String			isPurposePerson;

	// 保证金银行账户
	private String[]		bzjDzqks;
	// 保证金金额
	/** 到账数额数组 */
	private String			bzjDzses[];

	// 实际缴纳的交易服务费
	private BigDecimal[]	paymentCost;
	// 交易服务费缴纳时间
	private Date[]			paymentDate;
	// 交易服务费缴纳备注
	private String[]		paymentMemo;

	public String[] getBzjDzqks() {
		return bzjDzqks;
	}

	public void setBzjDzqks(String[] bzjDzqks) {
		this.bzjDzqks = bzjDzqks;
	}

	/** nullable persistent field */
	private String			districtId;

	/** nullable persistent field */
	private String			noticeId;

	/** nullable persistent field */
	private String			blockNoticeNo;

	/** nullable persistent field */
	private String			blockName;

	/** nullable persistent field */
	private String			uniteBlockName;

	/** nullable persistent field */
	private String			transferMode;

	/** nullable persistent field */
	private String			certNo;

	/** nullable persistent field */
	private String			reviewResult;

	/** nullable persistent field */
	private String			reviewOpnn;

	/** 竞买人ID数组 */
	private String			bidderPersonIds[];

	/** 到账情况数组 */
	private String			bidderDzqks[];

	/** 证件名称 */
	private String			materialBhs[];

	/** 收件类别 */
	private String			materialTypes[];

	/** 份数 */
	private String			materialCounts[];

	/** 到账时间数组 */
	private Date			bzjDzsjs[];

	/** identifier field */
	private String			bidderId;

	/** nullable persistent field */
	private String			appId;

	/** nullable persistent field */
	private String			appIds[];

	/** nullable persistent field */
	private String			acceptNo;

	/** nullable persistent field */
	private String			yktBh;

	/** nullable persistent field */
	private String			yktXh;

	/** nullable persistent field */
	private String			yktMm;

	/** nullable persistent field */
	private String			bidderType;

	/** nullable persistent field */
	private String			bzjJnfs;

	/** nullable persistent field */
	private String			wtrName;

	/** nullable persistent field */
	private String			wtrZjlx;

	/** nullable persistent field */
	private String			wtrZjhm;

	/** nullable persistent field */
	private String			wtrLxdh;

	/** nullable persistent field */
	private String			wtrLxdz;

	/** nullable persistent field */
	private String			wtrYzbm;

	/** nullable persistent field */
	private String			jsfs;

	/** nullable persistent field */
	private String			acceptPeople;

	/** nullable persistent field */
	private Date			acceptDate;

	/** nullable persistent field */
	private String			bzjztDzqk;

	/** nullable persistent field */
	private String			tenderNo;

	/** nullable persistent field */
	private Date			conTime;

	/** nullable persistent field */
	private String			conNum;

	/** nullable persistent field */
	private String			tranResult;

	/** nullable persistent field */
	private String			tranId;

	/** nullable persistent field */
	private String			isConvert;

	/** nullable persistent field */
	private String			bidderName;

	/** nullable persistent field */
	private String[]		bidderNames;

	/** nullable persistent field */
	private String			bidderZjlx;

	/** nullable persistent field */
	private String			bidderZjhm;

	/** nullable persistent field */
	private String			bidderLxdh;

	/** nullable persistent field */
	private String			bidderLxdz;

	/** nullable persistent field */
	private String			bidderYzbm;

	/** nullable persistent field */
	private String			corpFr;

	/** 法人证件类型 */
	private String			corpFrZjlx;

	/** 法人证件号码 */
	private String			corpFrZjhm;

	/** 法定代表人证件类型 */
	private String			corpFddbrZjlx;

	/** 法定代表人证件号码 */
	private String			corpFddbrZjhm;

	/** nullable persistent field */
	private String			bidderZh;

	/** nullable persistent field */
	private String			bidderProperty;

	/** nullable persistent field */
	private BigDecimal		bidderBzj;

	/** nullable persistent field */
	private String			isHead;

	/** nullable persistent field */
	private String			bzjDzqk;

	/** nullable persistent field */
	private BigDecimal		bzjDzse;

	/** nullable persistent field */
	private Date			bzjDzsj;

	/** nullable persistent field */
	private String			jsFdcZzdj;

	/** nullable persistent field */
	private String			jsJ5nKfTdyt;

	/** nullable persistent field */
	private BigDecimal		jsJ5nKfTdmj;

	/** nullable persistent field */
	private Integer			jsJ5nKfLpgs;

	/** nullable persistent field */
	private String			jsJjnLsCylx;

	/** nullable persistent field */
	private Integer			jsFdcCyns;

	/** nullable persistent field */
	private BigDecimal		jsJ3nZyywll;

	/** nullable persistent field */
	private String			jsQyZxdj;

	/** nullable persistent field */
	private BigDecimal		jsSyjzc;

	/** identifier field */
	private String			bidderMaterialId;

	/** nullable persistent field */
	private String			bidderPersonId;

	/** nullable persistent field */
	private String			materialCode;

	/** nullable persistent field */
	private String			materialBh;

	/** nullable persistent field */
	private String			materialType;

	/** nullable persistent field */
	private Integer			materialCount;

	/** identifier field */
	private String			provideId;

	/** nullable persistent field */
	private String			userId;

	/** nullable persistent field */
	private Integer			regionId;

	/** nullable persistent field */
	private String			bidType;

	/** nullable persistent field */
	private String			provideBm;

	/** nullable persistent field */
	private Date			provideDate;

	/** nullable persistent field */
	private String			ifApp;

	/** nullable persistent field */
	private Date			appDate;

	/** nullable persistent field */
	private String			sourceType;

	/** nullable persistent field */
	private String			memo;

	/** 组织名称 */
	private String			organizName;

	/** 保存类型 '11'为暂存 */
	private String			type;

	/** 是否打印 ”11“为要求打印 */
	private String			ifprint;

	/** nullable persistent field */
	private String			zstrName;

	/** nullable persistent field */
	private String			zstrZjlx;

	/** nullable persistent field */
	private String			zstrZjhm;

	/** nullable persistent field */
	private String			zstrLxdh;

	/** nullable persistent field */
	private String			zstrLxdz;

	/** nullable persistent field */
	private String			zstrYzbm;

	/** nullable persistent field */
	private String			ifzwt;

	/** 证件名称 （手动增加） */
	private String			otherMaterialBhs[];

	/** 收件类别 （手动增加） */
	private String			otherMaterialTypes[];

	/** 份数 （手动增加） */
	private String			otherMaterialCounts[];

	/** 文件名称（手动增加） */
	private String			materialNames[];

	/** 字典表中文件名称 */
	private String			dicMaterialNames[];

	private BigDecimal[]	bidderBzjDzse;

	private String			linkManName;

	// 招拍挂编号
	private String			tradeNum;

	public String getPurposeAppId() {
		return purposeAppId;
	}

	public void setPurposeAppId(String purposeAppId) {
		this.purposeAppId = purposeAppId;
	}

	public String getIsPurposePerson() {
		return isPurposePerson;
	}

	public void setIsPurposePerson(String isPurposePerson) {
		this.isPurposePerson = isPurposePerson;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String[] getDicMaterialNames() {
		return dicMaterialNames;
	}

	public void setDicMaterialNames(String[] dicMaterialNames) {
		this.dicMaterialNames = dicMaterialNames;
	}

	public String getIfprint() {
		return ifprint;
	}

	public void setIfprint(String ifprint) {
		this.ifprint = ifprint;
	}

	public String getOrganizName() {
		return organizName;
	}

	public void setOrganizName(String organizName) {
		this.organizName = organizName;
	}

	public Date getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getAcceptNo() {
		return acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}

	public String getAcceptPeople() {
		return acceptPeople;
	}

	public void setAcceptPeople(String acceptPeople) {
		this.acceptPeople = acceptPeople;
	}

	public BigDecimal getBidderBzj() {
		return bidderBzj;
	}

	public void setBidderBzj(BigDecimal bidderBzj) {
		this.bidderBzj = bidderBzj;
	}

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getBidderLxdh() {
		return bidderLxdh;
	}

	public void setBidderLxdh(String bidderLxdh) {
		this.bidderLxdh = bidderLxdh;
	}

	public String getBidderLxdz() {
		return bidderLxdz;
	}

	public void setBidderLxdz(String bidderLxdz) {
		this.bidderLxdz = bidderLxdz;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderPersonId() {
		return bidderPersonId;
	}

	public void setBidderPersonId(String bidderPersonId) {
		this.bidderPersonId = bidderPersonId;
	}

	public String getBidderProperty() {
		return bidderProperty;
	}

	public void setBidderProperty(String bidderProperty) {
		this.bidderProperty = bidderProperty;
	}

	public String getBidderType() {
		return bidderType;
	}

	public void setBidderType(String bidderType) {
		this.bidderType = bidderType;
	}

	public String getBidderYzbm() {
		return bidderYzbm;
	}

	public void setBidderYzbm(String bidderYzbm) {
		this.bidderYzbm = bidderYzbm;
	}

	public String getBidderZh() {
		return bidderZh;
	}

	public void setBidderZh(String bidderZh) {
		this.bidderZh = bidderZh;
	}

	public String getBidderZjhm() {
		return bidderZjhm;
	}

	public void setBidderZjhm(String bidderZjhm) {
		this.bidderZjhm = bidderZjhm;
	}

	public String getBidderZjlx() {
		return bidderZjlx;
	}

	public void setBidderZjlx(String bidderZjlx) {
		this.bidderZjlx = bidderZjlx;
	}

	public String getBzjDzqk() {
		return bzjDzqk;
	}

	public void setBzjDzqk(String bzjDzqk) {
		this.bzjDzqk = bzjDzqk;
	}

	public BigDecimal getBzjDzse() {
		return bzjDzse;
	}

	public void setBzjDzse(BigDecimal bzjDzse) {
		this.bzjDzse = bzjDzse;
	}

	public Date getBzjDzsj() {
		return bzjDzsj;
	}

	public void setBzjDzsj(Date bzjDzsj) {
		this.bzjDzsj = bzjDzsj;
	}

	public String getBzjJnfs() {
		return bzjJnfs;
	}

	public void setBzjJnfs(String bzjJnfs) {
		this.bzjJnfs = bzjJnfs;
	}

	public String getBzjztDzqk() {
		return bzjztDzqk;
	}

	public void setBzjztDzqk(String bzjztDzqk) {
		this.bzjztDzqk = bzjztDzqk;
	}

	public String getConNum() {
		return conNum;
	}

	public void setConNum(String conNum) {
		this.conNum = conNum;
	}

	public Date getConTime() {
		return conTime;
	}

	public void setConTime(Date conTime) {
		this.conTime = conTime;
	}

	public String getCorpFr() {
		return corpFr;
	}

	public void setCorpFr(String corpFr) {
		this.corpFr = corpFr;
	}

	public String getCorpFrZjhm() {
		return corpFrZjhm;
	}

	public void setCorpFrZjhm(String corpFrZjhm) {
		this.corpFrZjhm = corpFrZjhm;
	}

	public String getCorpFrZjlx() {
		return corpFrZjlx;
	}

	public void setCorpFrZjlx(String corpFrZjlx) {
		this.corpFrZjlx = corpFrZjlx;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getIsConvert() {
		return isConvert;
	}

	public void setIsConvert(String isConvert) {
		this.isConvert = isConvert;
	}

	public String getIsHead() {
		return isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}

	public Integer getJsFdcCyns() {
		return jsFdcCyns;
	}

	public void setJsFdcCyns(Integer jsFdcCyns) {
		this.jsFdcCyns = jsFdcCyns;
	}

	public String getJsFdcZzdj() {
		return jsFdcZzdj;
	}

	public void setJsFdcZzdj(String jsFdcZzdj) {
		this.jsFdcZzdj = jsFdcZzdj;
	}

	public String getJsfs() {
		return jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public BigDecimal getJsJ3nZyywll() {
		return jsJ3nZyywll;
	}

	public void setJsJ3nZyywll(BigDecimal jsJ3nZyywll) {
		this.jsJ3nZyywll = jsJ3nZyywll;
	}

	public Integer getJsJ5nKfLpgs() {
		return jsJ5nKfLpgs;
	}

	public void setJsJ5nKfLpgs(Integer jsJ5nKfLpgs) {
		this.jsJ5nKfLpgs = jsJ5nKfLpgs;
	}

	public BigDecimal getJsJ5nKfTdmj() {
		return jsJ5nKfTdmj;
	}

	public void setJsJ5nKfTdmj(BigDecimal jsJ5nKfTdmj) {
		this.jsJ5nKfTdmj = jsJ5nKfTdmj;
	}

	public String getJsJ5nKfTdyt() {
		return jsJ5nKfTdyt;
	}

	public void setJsJ5nKfTdyt(String jsJ5nKfTdyt) {
		this.jsJ5nKfTdyt = jsJ5nKfTdyt;
	}

	public String getJsJjnLsCylx() {
		return jsJjnLsCylx;
	}

	public void setJsJjnLsCylx(String jsJjnLsCylx) {
		this.jsJjnLsCylx = jsJjnLsCylx;
	}

	public String getJsQyZxdj() {
		return jsQyZxdj;
	}

	public void setJsQyZxdj(String jsQyZxdj) {
		this.jsQyZxdj = jsQyZxdj;
	}

	public BigDecimal getJsSyjzc() {
		return jsSyjzc;
	}

	public void setJsSyjzc(BigDecimal jsSyjzc) {
		this.jsSyjzc = jsSyjzc;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getTranResult() {
		return tranResult;
	}

	public void setTranResult(String tranResult) {
		this.tranResult = tranResult;
	}

	public String getWtrLxdh() {
		return wtrLxdh;
	}

	public void setWtrLxdh(String wtrLxdh) {
		this.wtrLxdh = wtrLxdh;
	}

	public String getWtrLxdz() {
		return wtrLxdz;
	}

	public void setWtrLxdz(String wtrLxdz) {
		this.wtrLxdz = wtrLxdz;
	}

	public String getWtrName() {
		return wtrName;
	}

	public void setWtrName(String wtrName) {
		this.wtrName = wtrName;
	}

	public String getWtrYzbm() {
		return wtrYzbm;
	}

	public void setWtrYzbm(String wtrYzbm) {
		this.wtrYzbm = wtrYzbm;
	}

	public String getWtrZjhm() {
		return wtrZjhm;
	}

	public void setWtrZjhm(String wtrZjhm) {
		this.wtrZjhm = wtrZjhm;
	}

	public String getWtrZjlx() {
		return wtrZjlx;
	}

	public void setWtrZjlx(String wtrZjlx) {
		this.wtrZjlx = wtrZjlx;
	}

	public String getYktBh() {
		return yktBh;
	}

	public void setYktBh(String yktBh) {
		this.yktBh = yktBh;
	}

	public String getYktMm() {
		return yktMm;
	}

	public void setYktMm(String yktMm) {
		this.yktMm = yktMm;
	}

	public String getYktXh() {
		return yktXh;
	}

	public void setYktXh(String yktXh) {
		this.yktXh = yktXh;
	}

	public String getBidderMaterialId() {
		return bidderMaterialId;
	}

	public void setBidderMaterialId(String bidderMaterialId) {
		this.bidderMaterialId = bidderMaterialId;
	}

	public String getMaterialBh() {
		return materialBh;
	}

	public void setMaterialBh(String materialBh) {
		this.materialBh = materialBh;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public Integer getMaterialCount() {
		return materialCount;
	}

	public void setMaterialCount(Integer materialCount) {
		this.materialCount = materialCount;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String[] getBidderDzqks() {
		return bidderDzqks;
	}

	public void setBidderDzqks(String[] bidderDzqks) {
		this.bidderDzqks = bidderDzqks;
	}

	public String[] getBidderPersonIds() {
		return bidderPersonIds;
	}

	public void setBidderPersonIds(String[] bidderPersonIds) {
		this.bidderPersonIds = bidderPersonIds;
	}

	public String[] getBzjDzses() {
		return bzjDzses;
	}

	public void setBzjDzses(String[] bzjDzses) {
		this.bzjDzses = bzjDzses;
	}

	public Date[] getBzjDzsjs() {
		return bzjDzsjs;
	}

	public void setBzjDzsjs(Date[] bzjDzsjs) {
		this.bzjDzsjs = bzjDzsjs;
	}

	public String[] getMaterialBhs() {
		return materialBhs;
	}

	public void setMaterialBhs(String[] materialBhs) {
		this.materialBhs = materialBhs;
	}

	public String[] getMaterialCounts() {
		return materialCounts;
	}

	public void setMaterialCounts(String[] materialCounts) {
		this.materialCounts = materialCounts;
	}

	public String[] getMaterialTypes() {
		return materialTypes;
	}

	public void setMaterialTypes(String[] materialTypes) {
		this.materialTypes = materialTypes;
	}

	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBidType() {
		return bidType;
	}

	public void setBidType(String bidType) {
		this.bidType = bidType;
	}

	public String getIfApp() {
		return ifApp;
	}

	public void setIfApp(String ifApp) {
		this.ifApp = ifApp;
	}

	public String getProvideBm() {
		return provideBm;
	}

	public void setProvideBm(String provideBm) {
		this.provideBm = provideBm;
	}

	public Date getProvideDate() {
		return provideDate;
	}

	public void setProvideDate(Date provideDate) {
		this.provideDate = provideDate;
	}

	public String getProvideId() {
		return provideId;
	}

	public void setProvideId(String provideId) {
		this.provideId = provideId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(String reviewResult) {
		this.reviewResult = reviewResult;
	}

	public String getReviewOpnn() {
		return reviewOpnn;
	}

	public void setReviewOpnn(String reviewOpnn) {
		this.reviewOpnn = reviewOpnn;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIfzwt() {
		return ifzwt;
	}

	public void setIfzwt(String ifzwt) {
		this.ifzwt = ifzwt;
	}

	public String getZstrLxdh() {
		return zstrLxdh;
	}

	public void setZstrLxdh(String zstrLxdh) {
		this.zstrLxdh = zstrLxdh;
	}

	public String getZstrLxdz() {
		return zstrLxdz;
	}

	public void setZstrLxdz(String zstrLxdz) {
		this.zstrLxdz = zstrLxdz;
	}

	public String getZstrName() {
		return zstrName;
	}

	public void setZstrName(String zstrName) {
		this.zstrName = zstrName;
	}

	public String getZstrYzbm() {
		return zstrYzbm;
	}

	public void setZstrYzbm(String zstrYzbm) {
		this.zstrYzbm = zstrYzbm;
	}

	public String getZstrZjhm() {
		return zstrZjhm;
	}

	public void setZstrZjhm(String zstrZjhm) {
		this.zstrZjhm = zstrZjhm;
	}

	public String getZstrZjlx() {
		return zstrZjlx;
	}

	public void setZstrZjlx(String zstrZjlx) {
		this.zstrZjlx = zstrZjlx;
	}

	public String getCorpFddbrZjhm() {
		return corpFddbrZjhm;
	}

	public void setCorpFddbrZjhm(String corpFddbrZjhm) {
		this.corpFddbrZjhm = corpFddbrZjhm;
	}

	public String getCorpFddbrZjlx() {
		return corpFddbrZjlx;
	}

	public void setCorpFddbrZjlx(String corpFddbrZjlx) {
		this.corpFddbrZjlx = corpFddbrZjlx;
	}

	public String[] getOtherMaterialBhs() {
		return otherMaterialBhs;
	}

	public void setOtherMaterialBhs(String[] otherMaterialBhs) {
		this.otherMaterialBhs = otherMaterialBhs;
	}

	public String[] getOtherMaterialCounts() {
		return otherMaterialCounts;
	}

	public String[] getMaterialNames() {
		return materialNames;
	}

	public void setMaterialNames(String[] materialNames) {
		this.materialNames = materialNames;
	}

	public void setOtherMaterialCounts(String[] otherMaterialCounts) {
		this.otherMaterialCounts = otherMaterialCounts;
	}

	public String[] getOtherMaterialTypes() {
		return otherMaterialTypes;
	}

	public void setOtherMaterialTypes(String[] otherMaterialTypes) {
		this.otherMaterialTypes = otherMaterialTypes;
	}

	public String[] getBidderNames() {
		return bidderNames;
	}

	public void setBidderNames(String[] bidderNames) {
		this.bidderNames = bidderNames;
	}

	public String getUniteBlockName() {
		return uniteBlockName;
	}

	public void setUniteBlockName(String uniteBlockName) {
		this.uniteBlockName = uniteBlockName;
	}

	public String[] getAppIds() {
		return appIds;
	}

	public void setAppIds(String[] appIds) {
		this.appIds = appIds;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public BigDecimal[] getBidderBzjDzse() {
		return bidderBzjDzse;
	}

	public void setBidderBzjDzse(BigDecimal[] bidderBzjDzse) {
		this.bidderBzjDzse = bidderBzjDzse;
	}

	public String getLinkManName() {
		return linkManName;
	}

	public void setLinkManName(String linkManName) {
		this.linkManName = linkManName;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public BigDecimal[] getPaymentCost() {
		return paymentCost;
	}

	public void setPaymentCost(BigDecimal[] paymentCost) {
		this.paymentCost = paymentCost;
	}

	public Date[] getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date[] paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String[] getPaymentMemo() {
		return paymentMemo;
	}

	public void setPaymentMemo(String[] paymentMemo) {
		this.paymentMemo = paymentMemo;
	}

	public String getBankAppIds() {
		return bankAppIds;
	}

	public void setBankAppIds(String bankAppIds) {
		this.bankAppIds = bankAppIds;
	}

	public String getIsAccptSms() {
		return isAccptSms;
	}

	public void setIsAccptSms(String isAccptSms) {
		this.isAccptSms = isAccptSms;
	}

	public String getPushSmsPhone() {
		return pushSmsPhone;
	}

	public void setPushSmsPhone(String pushSmsPhone) {
		this.pushSmsPhone = pushSmsPhone;
	}

}
