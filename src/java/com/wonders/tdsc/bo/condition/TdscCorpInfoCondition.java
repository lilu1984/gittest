package com.wonders.tdsc.bo.condition;

public class TdscCorpInfoCondition {
	private String corpId;

	private String bidderName;

	private String bidderZjlx;

	private String bidderZjhm;

	private String corpFrZjlx;

	private String corpFrZjhm;

	private int currentPage;// Ò³Êý

	private String bidderProperty;

	private String bidderNameLike;
	
	private String validity;

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getBidderName() {
		return bidderName;
	}

	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
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

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getBidderProperty() {
		return bidderProperty;
	}

	public void setBidderProperty(String bidderProperty) {
		this.bidderProperty = bidderProperty;
	}

	public String getBidderNameLike() {
		return bidderNameLike;
	}

	public void setBidderNameLike(String bidderNameLike) {
		this.bidderNameLike = bidderNameLike;
	}

}
