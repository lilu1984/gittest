package com.wonders.tdsc.kyq.bo;

import java.math.BigDecimal;
import java.util.Date;

public class KyqListing {
	private String		listingId;
	private String		certNo;
	private Date		listDate;
	private String		miningId;
	private BigDecimal	listPrice;
	private String		haoPai;

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public Date getListDate() {
		return listDate;
	}

	public void setListDate(Date listDate) {
		this.listDate = listDate;
	}

	public String getMiningId() {
		return miningId;
	}

	public void setMiningId(String miningId) {
		this.miningId = miningId;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getHaoPai() {
		return haoPai;
	}

	public void setHaoPai(String haoPai) {
		this.haoPai = haoPai;
	}

}
