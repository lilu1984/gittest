package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscBidderView extends BaseBO {
	private String		bidderId;
	private String		appId;
	private String		acceptNo;
	private String		yktBh;
	private String		yktXh;
	private String		yktMm;
	private String		bidderType;
	private String		bzjJnfs;
	private String		wtrName;
	private String		wtrZjlx;
	private String		wtrZjhm;
	private String		wtrLxdh;
	private String		wtrLxdz;
	private String		wtrYzbm;
	private String		jsfs;
	private String		acceptPeople;
	private Timestamp	acceptDate;
	private String		bzjztDzqk;
	private String		tenderNo;
	private Timestamp	conTime;
	private String		conNum;
	private String		tranResult;
	private String		tranId;
	private String		isConvert;
	private String		certNo;
	private String		reviewResult;
	private String		reviewOpnn;
	private String		appUserId;
	private String		zstrName;
	private String		zstrZjlx;
	private String		zstrZjhm;
	private String		zstrLxdh;
	private String		zstrLxdz;
	private String		zstrYzbm;
	private String		ifZwt;
	private String		ifCommit;
	private String		ifSendGuard;
	private String		ifDownloadZgzs;
	private String		noticeId;
	private String		userId;
	private String		isPurposePerson;
	private String		isAccptSms;
	private String		pushSmsPhone;
	private String		bidderPersonId;
	private String		bidderName;
	private String		bidderZjlx;
	private String		bidderZjhm;
	private String		bidderLxdh;
	private String		bidderLxdz;
	private String		bidderYzbm;
	private String		corpFr;
	private String		corpFrZjlx;
	private String		corpFrZjhm;
	private String		bidderZh;
	private String		bidderProperty;
	private BigDecimal	bidderBzj;
	private String		isHead;
	private String		bzjDzqk;
	private BigDecimal	bzjDzse;
	private Timestamp	bzjDzsj;
	private String		jsFdcZzdj;
	private String		jsJ5nKfTdyt;
	private BigDecimal	jsJ5nKfTdmj;
	private BigDecimal	jsJ5nKfLpgs;
	private String		jsJjnLsCylx;
	private BigDecimal	jsFdcCyns;
	private BigDecimal	jsJ3nZyywll;
	private String		jsQyZxdj;
	private BigDecimal	jsSyjzc;
	private String		corpFddbrZjhm;
	private String		corpFddbrZjlx;
	private String		purposeAppId;
	private String		linkManName;
	private String      sgNumber;
	private String      bankId;
	
	private String blockNoticeNo;
	private String bankNumber;
	private String tradeStatus;
	private String tResult;
	private String partakeBidderConNum;
	private String resultName;
	
	private Timestamp marginEndDate;
	private Timestamp resultDate;
	private Timestamp sgDate;
	
	private BigDecimal marginAmount;
	private BigDecimal initPrice;
	private BigDecimal addPriceRange;
	private BigDecimal resultPrice;
	private BigDecimal maxPrice;
	

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

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAcceptNo() {
		return acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}

	public String getYktBh() {
		return yktBh;
	}

	public void setYktBh(String yktBh) {
		this.yktBh = yktBh;
	}

	public String getYktXh() {
		return yktXh;
	}

	public void setYktXh(String yktXh) {
		this.yktXh = yktXh;
	}

	public String getYktMm() {
		return yktMm;
	}

	public void setYktMm(String yktMm) {
		this.yktMm = yktMm;
	}

	public String getBidderType() {
		return bidderType;
	}

	public void setBidderType(String bidderType) {
		this.bidderType = bidderType;
	}

	public String getBzjJnfs() {
		return bzjJnfs;
	}

	public void setBzjJnfs(String bzjJnfs) {
		this.bzjJnfs = bzjJnfs;
	}

	public String getWtrName() {
		return wtrName;
	}

	public void setWtrName(String wtrName) {
		this.wtrName = wtrName;
	}

	public String getWtrZjlx() {
		return wtrZjlx;
	}

	public void setWtrZjlx(String wtrZjlx) {
		this.wtrZjlx = wtrZjlx;
	}

	public String getWtrZjhm() {
		return wtrZjhm;
	}

	public void setWtrZjhm(String wtrZjhm) {
		this.wtrZjhm = wtrZjhm;
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

	public String getWtrYzbm() {
		return wtrYzbm;
	}

	public void setWtrYzbm(String wtrYzbm) {
		this.wtrYzbm = wtrYzbm;
	}

	public String getJsfs() {
		return jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public String getAcceptPeople() {
		return acceptPeople;
	}

	public void setAcceptPeople(String acceptPeople) {
		this.acceptPeople = acceptPeople;
	}

	public Timestamp getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Timestamp acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getBzjztDzqk() {
		return bzjztDzqk;
	}

	public void setBzjztDzqk(String bzjztDzqk) {
		this.bzjztDzqk = bzjztDzqk;
	}

	public String getTenderNo() {
		return tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public Timestamp getConTime() {
		return conTime;
	}

	public void setConTime(Timestamp conTime) {
		this.conTime = conTime;
	}

	public String getConNum() {
		return conNum;
	}

	public void setConNum(String conNum) {
		this.conNum = conNum;
	}

	public String getTranResult() {
		return tranResult;
	}

	public void setTranResult(String tranResult) {
		this.tranResult = tranResult;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getIsConvert() {
		return isConvert;
	}

	public void setIsConvert(String isConvert) {
		this.isConvert = isConvert;
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

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public String getZstrName() {
		return zstrName;
	}

	public void setZstrName(String zstrName) {
		this.zstrName = zstrName;
	}

	public String getZstrZjlx() {
		return zstrZjlx;
	}

	public void setZstrZjlx(String zstrZjlx) {
		this.zstrZjlx = zstrZjlx;
	}

	public String getZstrZjhm() {
		return zstrZjhm;
	}

	public void setZstrZjhm(String zstrZjhm) {
		this.zstrZjhm = zstrZjhm;
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

	public String getZstrYzbm() {
		return zstrYzbm;
	}

	public void setZstrYzbm(String zstrYzbm) {
		this.zstrYzbm = zstrYzbm;
	}

	public String getIfZwt() {
		return ifZwt;
	}

	public void setIfZwt(String ifZwt) {
		this.ifZwt = ifZwt;
	}

	public String getIfCommit() {
		return ifCommit;
	}

	public void setIfCommit(String ifCommit) {
		this.ifCommit = ifCommit;
	}

	public String getIfSendGuard() {
		return ifSendGuard;
	}

	public void setIfSendGuard(String ifSendGuard) {
		this.ifSendGuard = ifSendGuard;
	}

	public String getIfDownloadZgzs() {
		return ifDownloadZgzs;
	}

	public void setIfDownloadZgzs(String ifDownloadZgzs) {
		this.ifDownloadZgzs = ifDownloadZgzs;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsPurposePerson() {
		return isPurposePerson;
	}

	public void setIsPurposePerson(String isPurposePerson) {
		this.isPurposePerson = isPurposePerson;
	}

	public String getBidderPersonId() {
		return bidderPersonId;
	}

	public void setBidderPersonId(String bidderPersonId) {
		this.bidderPersonId = bidderPersonId;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderZjlx() {
		return bidderZjlx;
	}

	public void setBidderZjlx(String bidderZjlx) {
		this.bidderZjlx = bidderZjlx;
	}

	public String getBidderZjhm() {
		return bidderZjhm;
	}

	public void setBidderZjhm(String bidderZjhm) {
		this.bidderZjhm = bidderZjhm;
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

	public String getBidderYzbm() {
		return bidderYzbm;
	}

	public void setBidderYzbm(String bidderYzbm) {
		this.bidderYzbm = bidderYzbm;
	}

	public String getCorpFr() {
		return corpFr;
	}

	public void setCorpFr(String corpFr) {
		this.corpFr = corpFr;
	}

	public String getCorpFrZjlx() {
		return corpFrZjlx;
	}

	public void setCorpFrZjlx(String corpFrZjlx) {
		this.corpFrZjlx = corpFrZjlx;
	}

	public String getCorpFrZjhm() {
		return corpFrZjhm;
	}

	public void setCorpFrZjhm(String corpFrZjhm) {
		this.corpFrZjhm = corpFrZjhm;
	}

	public String getBidderZh() {
		return bidderZh;
	}

	public void setBidderZh(String bidderZh) {
		this.bidderZh = bidderZh;
	}

	public String getBidderProperty() {
		return bidderProperty;
	}

	public void setBidderProperty(String bidderProperty) {
		this.bidderProperty = bidderProperty;
	}

	public BigDecimal getBidderBzj() {
		return bidderBzj;
	}

	public void setBidderBzj(BigDecimal bidderBzj) {
		this.bidderBzj = bidderBzj;
	}

	public String getIsHead() {
		return isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
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

	public Timestamp getBzjDzsj() {
		return bzjDzsj;
	}

	public void setBzjDzsj(Timestamp bzjDzsj) {
		this.bzjDzsj = bzjDzsj;
	}

	public String getJsFdcZzdj() {
		return jsFdcZzdj;
	}

	public void setJsFdcZzdj(String jsFdcZzdj) {
		this.jsFdcZzdj = jsFdcZzdj;
	}

	public String getJsJ5nKfTdyt() {
		return jsJ5nKfTdyt;
	}

	public void setJsJ5nKfTdyt(String jsJ5nKfTdyt) {
		this.jsJ5nKfTdyt = jsJ5nKfTdyt;
	}

	public BigDecimal getJsJ5nKfTdmj() {
		return jsJ5nKfTdmj;
	}

	public void setJsJ5nKfTdmj(BigDecimal jsJ5nKfTdmj) {
		this.jsJ5nKfTdmj = jsJ5nKfTdmj;
	}

	public BigDecimal getJsJ5nKfLpgs() {
		return jsJ5nKfLpgs;
	}

	public void setJsJ5nKfLpgs(BigDecimal jsJ5nKfLpgs) {
		this.jsJ5nKfLpgs = jsJ5nKfLpgs;
	}

	public String getJsJjnLsCylx() {
		return jsJjnLsCylx;
	}

	public void setJsJjnLsCylx(String jsJjnLsCylx) {
		this.jsJjnLsCylx = jsJjnLsCylx;
	}

	public BigDecimal getJsFdcCyns() {
		return jsFdcCyns;
	}

	public void setJsFdcCyns(BigDecimal jsFdcCyns) {
		this.jsFdcCyns = jsFdcCyns;
	}

	public BigDecimal getJsJ3nZyywll() {
		return jsJ3nZyywll;
	}

	public void setJsJ3nZyywll(BigDecimal jsJ3nZyywll) {
		this.jsJ3nZyywll = jsJ3nZyywll;
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

	public String getPurposeAppId() {
		return purposeAppId;
	}

	public void setPurposeAppId(String purposeAppId) {
		this.purposeAppId = purposeAppId;
	}

	public String getLinkManName() {
		return linkManName;
	}

	public void setLinkManName(String linkManName) {
		this.linkManName = linkManName;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getSgNumber() {
		return sgNumber;
	}

	public void setSgNumber(String sgNumber) {
		this.sgNumber = sgNumber;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String gettResult() {
		return tResult;
	}

	public void settResult(String tResult) {
		this.tResult = tResult;
	}

	public String getPartakeBidderConNum() {
		return partakeBidderConNum;
	}

	public void setPartakeBidderConNum(String partakeBidderConNum) {
		this.partakeBidderConNum = partakeBidderConNum;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public Timestamp getMarginEndDate() {
		return marginEndDate;
	}

	public void setMarginEndDate(Timestamp marginEndDate) {
		this.marginEndDate = marginEndDate;
	}

	public Timestamp getResultDate() {
		return resultDate;
	}

	public void setResultDate(Timestamp resultDate) {
		this.resultDate = resultDate;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public BigDecimal getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(BigDecimal initPrice) {
		this.initPrice = initPrice;
	}

	public BigDecimal getAddPriceRange() {
		return addPriceRange;
	}

	public void setAddPriceRange(BigDecimal addPriceRange) {
		this.addPriceRange = addPriceRange;
	}

	public BigDecimal getResultPrice() {
		return resultPrice;
	}

	public void setResultPrice(BigDecimal resultPrice) {
		this.resultPrice = resultPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Timestamp getSgDate() {
		return sgDate;
	}

	public void setSgDate(Timestamp sgDate) {
		this.sgDate = sgDate;
	}

	
}
