package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscBidderApp extends BaseBO {
	private String				isAccptSms;

	private String				pushSmsPhone;
	/** identifier field */
	private String				bidderId;

	/** nullable persistent field */
	private String				appId;

	/** nullable persistent field */
	private String				noticeId;

	/** nullable persistent field */
	private String				acceptNo;

	/** nullable persistent field */
	private String				unionCount;

	/** nullable persistent field */
	private String				yktBh;

	/** nullable persistent field */
	private String				yktXh;

	/** nullable persistent field */
	private String				yktMm;

	/** nullable persistent field */
	private String				bidderType;

	/** nullable persistent field */
	private String				bzjJnfs;

	/** nullable persistent field */
	private String				wtrName;

	/** nullable persistent field */
	private String				wtrZjlx;

	/** nullable persistent field */
	private String				wtrZjhm;

	/** nullable persistent field */
	private String				wtrLxdh;

	/** nullable persistent field */
	private String				wtrLxdz;

	/** nullable persistent field */
	private String				wtrYzbm;

	/** nullable persistent field */
	private String				jsfs;

	/** nullable persistent field */
	private String				acceptPeople;

	/** nullable persistent field */
	private Timestamp				acceptDate;

	/** nullable persistent field */
	private String				bzjztDzqk;

	/** nullable persistent field */
	private String				tenderNo;

	/** nullable persistent field */
	private Timestamp				conTime;

	/** nullable persistent field */
	private String				conNum;

	/** nullable persistent field */
	private String				certNo;

	/** nullable persistent field */
	private String				memo;

	private String				reviewResult;

	private String				reviewOpnn;
	/** 受理用户 */
	private String				appUserId;

	/** nullable persistent field */
	private String				zstrName;

	/** nullable persistent field */
	private String				zstrZjlx;

	/** nullable persistent field */
	private String				zstrZjhm;

	/** nullable persistent field */
	private String				zstrLxdh;

	/** nullable persistent field */
	private String				zstrLxdz;

	/** nullable persistent field */
	private String				zstrYzbm;

	/** nullable persistent field */
	private String				ifzwt;

	/** nullable persistent field */
	private String				ifCommit;

	/** nullable persistent field */
	private String				ifSendGuard;

	/** nullable persistent field */
	private String				ifDownloadZgzs;

	private String				isPurposePerson;

	private BigDecimal			dzse;

	private String				isConvert;

	private Set					tdscBidderPersonApp	= new HashSet();

	private TdscBidderPersonApp	aloneTdscBidderPersonApp;

	/** 设置用户ID */
	private String				userId;
	
	private String 				sqNumber;
	
	private String              bankId;
	
	private String              bankNumber;
	
	private Timestamp              sgDate;
	
	private String              uploadFileId;
	
	private String              isCreateComp;


	public String getUploadFileId() {
		return uploadFileId;
	}

	public void setUploadFileId(String uploadFileId) {
		this.uploadFileId = uploadFileId;
	}

	public String getIsCreateComp() {
		return isCreateComp;
	}

	public void setIsCreateComp(String isCreateComp) {
		this.isCreateComp = isCreateComp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIfDownloadZgzs() {
		return ifDownloadZgzs;
	}

	public void setIfDownloadZgzs(String ifDownloadZgzs) {
		this.ifDownloadZgzs = ifDownloadZgzs;
	}

	public String getIfSendGuard() {
		return ifSendGuard;
	}

	public void setIfSendGuard(String ifSendGuard) {
		this.ifSendGuard = ifSendGuard;
	}

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	/** default constructor */
	public TdscBidderApp() {
	}

	public String getBidderId() {
		return this.bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAcceptNo() {
		return this.acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}

	public String getYktBh() {
		return this.yktBh;
	}

	public void setYktBh(String yktBh) {
		this.yktBh = yktBh;
	}

	public String getYktXh() {
		return this.yktXh;
	}

	public void setYktXh(String yktXh) {
		this.yktXh = yktXh;
	}

	public String getYktMm() {
		return this.yktMm;
	}

	public void setYktMm(String yktMm) {
		this.yktMm = yktMm;
	}

	public String getBidderType() {
		return this.bidderType;
	}

	public void setBidderType(String bidderType) {
		this.bidderType = bidderType;
	}

	public String getBzjJnfs() {
		return this.bzjJnfs;
	}

	public void setBzjJnfs(String bzjJnfs) {
		this.bzjJnfs = bzjJnfs;
	}

	public String getWtrName() {
		return this.wtrName;
	}

	public void setWtrName(String wtrName) {
		this.wtrName = wtrName;
	}

	public String getWtrZjlx() {
		return this.wtrZjlx;
	}

	public void setWtrZjlx(String wtrZjlx) {
		this.wtrZjlx = wtrZjlx;
	}

	public String getWtrZjhm() {
		return this.wtrZjhm;
	}

	public void setWtrZjhm(String wtrZjhm) {
		this.wtrZjhm = wtrZjhm;
	}

	public String getWtrLxdh() {
		return this.wtrLxdh;
	}

	public void setWtrLxdh(String wtrLxdh) {
		this.wtrLxdh = wtrLxdh;
	}

	public String getWtrLxdz() {
		return this.wtrLxdz;
	}

	public void setWtrLxdz(String wtrLxdz) {
		this.wtrLxdz = wtrLxdz;
	}

	public String getWtrYzbm() {
		return this.wtrYzbm;
	}

	public void setWtrYzbm(String wtrYzbm) {
		this.wtrYzbm = wtrYzbm;
	}

	public String getJsfs() {
		return this.jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public String getAcceptPeople() {
		return this.acceptPeople;
	}

	public void setAcceptPeople(String acceptPeople) {
		this.acceptPeople = acceptPeople;
	}

	public Timestamp getAcceptDate() {
		return this.acceptDate;
	}

	public void setAcceptDate(Timestamp acceptDate) {
		this.acceptDate = acceptDate;
	}

	public String getBzjztDzqk() {
		return this.bzjztDzqk;
	}

	public void setBzjztDzqk(String bzjztDzqk) {
		this.bzjztDzqk = bzjztDzqk;
	}

	public String getTenderNo() {
		return this.tenderNo;
	}

	public void setTenderNo(String tenderNo) {
		this.tenderNo = tenderNo;
	}

	public Timestamp getConTime() {
		return this.conTime;
	}

	public void setConTime(Timestamp conTime) {
		this.conTime = conTime;
	}

	public String getConNum() {
		return this.conNum;
	}

	public void setConNum(String conNum) {
		this.conNum = conNum;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUnionCount() {
		return unionCount;
	}

	public void setUnionCount(String unionCount) {
		this.unionCount = unionCount;
	}

	public String getReviewOpnn() {
		return reviewOpnn;
	}

	public void setReviewOpnn(String reviewOpnn) {
		this.reviewOpnn = reviewOpnn;
	}

	public String getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(String reviewResult) {
		this.reviewResult = reviewResult;
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

	public String getIfCommit() {
		return ifCommit;
	}

	public void setIfCommit(String ifCommit) {
		this.ifCommit = ifCommit;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public Set getTdscBidderPersonApp() {
		return tdscBidderPersonApp;
	}

	public void setTdscBidderPersonApp(Set tdscBidderPersonApp) {
		this.tdscBidderPersonApp = tdscBidderPersonApp;
	}

	public String getIsPurposePerson() {
		return isPurposePerson;
	}

	public void setIsPurposePerson(String isPurposePerson) {
		this.isPurposePerson = isPurposePerson;
	}

	public TdscBidderPersonApp getAloneTdscBidderPersonApp() {
		return aloneTdscBidderPersonApp;
	}

	public void setAloneTdscBidderPersonApp(TdscBidderPersonApp aloneTdscBidderPersonApp) {
		this.aloneTdscBidderPersonApp = aloneTdscBidderPersonApp;
	}

	public BigDecimal getDzse() {
		return dzse;
	}

	public void setDzse(BigDecimal dzse) {
		this.dzse = dzse;
	}

	public String getIsConvert() {
		return isConvert;
	}

	public void setIsConvert(String isConvert) {
		this.isConvert = isConvert;
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

	public String getSqNumber() {
		return sqNumber;
	}

	public void setSqNumber(String sqNumber) {
		this.sqNumber = sqNumber;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public Timestamp getSgDate() {
		return sgDate;
	}

	public void setSgDate(Timestamp sgDate) {
		this.sgDate = sgDate;
	}

	
}
