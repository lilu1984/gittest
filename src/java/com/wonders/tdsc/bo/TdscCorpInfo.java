package com.wonders.tdsc.bo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.wonders.esframework.common.bo.BaseBO;


/**
 * YsqsCorpInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TdscCorpInfo extends BaseBO {

	// Fields

	private String corpId;
	private String bidderName;
	private String bidderProperty;
	private String bidderZjlx;
	private String bidderZjhm;
	private String bidderLxdh;
	private String bidderLxdz;
	private String bidderYzbm;
	private String corpFrZjlx;
	private String corpFrZjhm;
	private String corpFr;
	private String corpFddbrZjhm;
	private String corpFddbrZjlx;
	private String addType;
	private String ifInBlockList;
	private String validity;
	private Set tdscBidderCxApp = new HashSet();
	
	private Date endDate;
	// Constructors

	/** default constructor */
	public TdscCorpInfo() {
	}

	/** full constructor */
	public TdscCorpInfo(String bidderName, String bidderProperty, String bidderZjlx, String bidderZjhm,
			String bidderLxdh, String bidderLxdz, String bidderYzbm, String corpFrZjlx, String corpFrZjhm,
			String corpFr, String corpFddbrZjhm, String corpFddbrZjlx) {
		this.bidderName = bidderName;
		this.bidderProperty = bidderProperty;
		this.bidderZjlx = bidderZjlx;
		this.bidderZjhm = bidderZjhm;
		this.bidderLxdh = bidderLxdh;
		this.bidderLxdz = bidderLxdz;
		this.bidderYzbm = bidderYzbm;
		this.corpFrZjlx = corpFrZjlx;
		this.corpFrZjhm = corpFrZjhm;
		this.corpFr = corpFr;
		this.corpFddbrZjhm = corpFddbrZjhm;
		this.corpFddbrZjlx = corpFddbrZjlx;
	}

	// Property accessors

	public String getCorpId() {
		return this.corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getBidderName() {
		return this.bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}

	public String getBidderProperty() {
		return this.bidderProperty;
	}

	public void setBidderProperty(String bidderProperty) {
		this.bidderProperty = bidderProperty;
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

	public String getCorpFr() {
		return this.corpFr;
	}

	public void setCorpFr(String corpFr) {
		this.corpFr = corpFr;
	}

	public String getCorpFddbrZjhm() {
		return this.corpFddbrZjhm;
	}

	public void setCorpFddbrZjhm(String corpFddbrZjhm) {
		this.corpFddbrZjhm = corpFddbrZjhm;
	}

	public String getCorpFddbrZjlx() {
		return this.corpFddbrZjlx;
	}

	public void setCorpFddbrZjlx(String corpFddbrZjlx) {
		this.corpFddbrZjlx = corpFddbrZjlx;
	}

	public String getId() {
		return corpId;
	}

	public String getAddType() {
		return addType;
	}

	public void setAddType(String addType) {
		this.addType = addType;
	}

	public String getIfInBlockList() {
		return ifInBlockList;
	}

	public void setIfInBlockList(String ifInBlockList) {
		this.ifInBlockList = ifInBlockList;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public Set getTdscBidderCxApp() {
		return tdscBidderCxApp;
	}

	public void setTdscBidderCxApp(Set tdscBidderCxApp) {
		this.tdscBidderCxApp = tdscBidderCxApp;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}