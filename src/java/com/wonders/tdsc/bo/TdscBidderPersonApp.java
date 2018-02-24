package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TdscBidderPersonApp implements Serializable {

	// 如果是意向人，存储意向人 意向地块 App_id
	private String		purposeAppId;

	/** identifier field */
	private String		bidderPersonId;

	/** nullable persistent field */
	private String		acceptNo;

	/** nullable persistent field */
	private String		bzjztDzqk;

	/** nullable persistent field */
	private String		bzjJnfs;

	/** nullable persistent field */
	private String		bidderType;

	/** nullable persistent field */
	private String		bidderId;

	/** nullable persistent field */
	private String		bidderName;

	/** nullable persistent field */
	private String		bidderZjlx;

	/** nullable persistent field */
	private String		bidderZjhm;

	/** nullable persistent field */
	private String		bidderLxdh;

	/** nullable persistent field */
	private String		bidderLxdz;

	/** nullable persistent field */
	private String		bidderYzbm;

	/** nullable persistent field */
	private String		corpFr;

	/** 法人证件类型 */
	private String		corpFrZjlx;

	/** 法人证件号码 */
	private String		corpFrZjhm;

	/** 法定代表人证件类型 */
	private String		corpFddbrZjlx;

	/** 法定代表人证件号码 */
	private String		corpFddbrZjhm;

	/** nullable persistent field */
	private String		bidderZh;

	/** nullable persistent field */
	private String		bidderProperty;

	/** nullable persistent field */
	private BigDecimal	bidderBzj;

	/** nullable persistent field */
	private String		isHead;

	/** nullable persistent field */
	private String		bzjDzqk;

	/** nullable persistent field */
	private BigDecimal	bzjDzse;

	/** nullable persistent field */
	private Date		bzjDzsj;

	/** nullable persistent field */
	private String		memo;

	/** nullable persistent field */
	private String		jsFdcZzdj;

	/** nullable persistent field */
	private String		jsJ5nKfTdyt;

	/** nullable persistent field */
	private BigDecimal	jsJ5nKfTdmj;

	/** nullable persistent field */
	private Integer		jsJ5nKfLpgs;

	/** nullable persistent field */
	private String		jsJjnLsCylx;

	/** nullable persistent field */
	private Integer		jsFdcCyns;

	/** nullable persistent field */
	private BigDecimal	jsJ3nZyywll;

	/** nullable persistent field */
	private String		jsQyZxdj;

	/** nullable persistent field */
	private BigDecimal	jsSyjzc;

	/** 竞买人属性（字典）名称 */
	private String		bidderPropertyName;

	private String		linkManName;
	
	private String 		orgNo;

	/** full constructor */
	public TdscBidderPersonApp(String bidderId, String bidderName, String bidderZjlx, String bidderZjhm, String bidderLxdh, String bidderLxdz, String bidderYzbm, String corpFr,
			String corpFrZjlx, String corpFrZjhm, String bidderZh, String bidderProperty, BigDecimal bidderBzj, String isHead, String bzjDzqk, BigDecimal bzjDzse, Date bzjDzsj,
			String memo, String jsFdcZzdj, String jsJ5nKfTdyt, BigDecimal jsJ5nKfTdmj, Integer jsJ5nKfLpgs, String jsJjnLsCylx, Integer jsFdcCyns, BigDecimal jsJ3nZyywll,
			String jsQyZxdj, BigDecimal jsSyjzc, String corpFddbrZjlx, String corpFddbrZjhm) {
		this.bidderId = bidderId;
		this.bidderName = bidderName;
		this.bidderZjlx = bidderZjlx;
		this.bidderZjhm = bidderZjhm;
		this.bidderLxdh = bidderLxdh;
		this.bidderLxdz = bidderLxdz;
		this.bidderYzbm = bidderYzbm;
		this.corpFr = corpFr;
		this.corpFrZjlx = corpFrZjlx;
		this.corpFrZjhm = corpFrZjhm;
		this.bidderZh = bidderZh;
		this.bidderProperty = bidderProperty;
		this.bidderBzj = bidderBzj;
		this.isHead = isHead;
		this.bzjDzqk = bzjDzqk;
		this.bzjDzse = bzjDzse;
		this.bzjDzsj = bzjDzsj;
		this.memo = memo;
		this.jsFdcZzdj = jsFdcZzdj;
		this.jsJ5nKfTdyt = jsJ5nKfTdyt;
		this.jsJ5nKfTdmj = jsJ5nKfTdmj;
		this.jsJ5nKfLpgs = jsJ5nKfLpgs;
		this.jsJjnLsCylx = jsJjnLsCylx;
		this.jsFdcCyns = jsFdcCyns;
		this.jsJ3nZyywll = jsJ3nZyywll;
		this.jsQyZxdj = jsQyZxdj;
		this.jsSyjzc = jsSyjzc;
		this.corpFddbrZjlx = corpFddbrZjlx;
		this.corpFddbrZjhm = corpFddbrZjhm;
	}

	/** default constructor */
	public TdscBidderPersonApp() {
	}

	public String getBidderPersonId() {
		return this.bidderPersonId;
	}

	public void setBidderPersonId(String bidderPersonId) {
		this.bidderPersonId = bidderPersonId;
	}

	public String getBidderId() {
		return this.bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getBidderName() {
		return this.bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderZjlx() {
		return this.bidderZjlx;
	}

	public void setBidderZjlx(String bidderZjlx) {
		this.bidderZjlx = bidderZjlx;
	}

	public String getBidderZjhm() {
		return this.bidderZjhm;
	}

	public void setBidderZjhm(String bidderZjhm) {
		this.bidderZjhm = bidderZjhm;
	}

	public String getBidderLxdh() {
		return this.bidderLxdh;
	}

	public void setBidderLxdh(String bidderLxdh) {
		this.bidderLxdh = bidderLxdh;
	}

	public String getBidderLxdz() {
		return this.bidderLxdz;
	}

	public void setBidderLxdz(String bidderLxdz) {
		this.bidderLxdz = bidderLxdz;
	}

	public String getBidderYzbm() {
		return this.bidderYzbm;
	}

	public void setBidderYzbm(String bidderYzbm) {
		this.bidderYzbm = bidderYzbm;
	}

	public String getCorpFr() {
		return this.corpFr;
	}

	public void setCorpFr(String corpFr) {
		this.corpFr = corpFr;
	}

	public String getCorpFrZjlx() {
		return this.corpFrZjlx;
	}

	public void setCorpFrZjlx(String corpFrZjlx) {
		this.corpFrZjlx = corpFrZjlx;
	}

	public String getCorpFrZjhm() {
		return this.corpFrZjhm;
	}

	public void setCorpFrZjhm(String corpFrZjhm) {
		this.corpFrZjhm = corpFrZjhm;
	}

	public String getBidderZh() {
		return this.bidderZh;
	}

	public void setBidderZh(String bidderZh) {
		this.bidderZh = bidderZh;
	}

	public String getBidderProperty() {
		return this.bidderProperty;
	}

	public void setBidderProperty(String bidderProperty) {
		this.bidderProperty = bidderProperty;
	}

	public BigDecimal getBidderBzj() {
		return this.bidderBzj;
	}

	public void setBidderBzj(BigDecimal bidderBzj) {
		this.bidderBzj = bidderBzj;
	}

	public String getIsHead() {
		return this.isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}

	public String getBzjDzqk() {
		return this.bzjDzqk;
	}

	public void setBzjDzqk(String bzjDzqk) {
		this.bzjDzqk = bzjDzqk;
	}

	public BigDecimal getBzjDzse() {
		return this.bzjDzse;
	}

	public void setBzjDzse(BigDecimal bzjDzse) {
		this.bzjDzse = bzjDzse;
	}

	public Date getBzjDzsj() {
		return this.bzjDzsj;
	}

	public void setBzjDzsj(Date bzjDzsj) {
		this.bzjDzsj = bzjDzsj;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getJsFdcZzdj() {
		return this.jsFdcZzdj;
	}

	public void setJsFdcZzdj(String jsFdcZzdj) {
		this.jsFdcZzdj = jsFdcZzdj;
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

	public String toString() {
		return new ToStringBuilder(this).append("bidderPersonId", getBidderPersonId()).toString();
	}

	public String getBidderType() {
		return bidderType;
	}

	public void setBidderType(String bidderType) {
		this.bidderType = bidderType;
	}

	public String getBzjztDzqk() {
		return bzjztDzqk;
	}

	public void setBzjztDzqk(String bzjztDzqk) {
		this.bzjztDzqk = bzjztDzqk;
	}

	public String getBzjJnfs() {
		return bzjJnfs;
	}

	public void setBzjJnfs(String bzjJnfs) {
		this.bzjJnfs = bzjJnfs;
	}

	public String getBidderPropertyName() {
		return bidderPropertyName;
	}

	public void setBidderPropertyName(String bidderPropertyName) {
		this.bidderPropertyName = bidderPropertyName;
	}

	public String getAcceptNo() {
		return acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
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

	public String getLinkManName() {
		return linkManName;
	}

	public void setLinkManName(String linkManName) {
		this.linkManName = linkManName;
	}

	public String getPurposeAppId() {
		return purposeAppId;
	}

	public void setPurposeAppId(String purposeAppId) {
		this.purposeAppId = purposeAppId;
	}

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	
}
