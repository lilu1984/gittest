package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.List;

/** @author Hibernate CodeGenerator */
public class BlockAuctionPrice implements Serializable {

	private String currentNum;
	
	private String haoPai;
	
	private String currentPrice;

	private String unitPrice;
	
	private String premiumRate;

	public String getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(String currentNum) {
		this.currentNum = currentNum;
	}

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getHaoPai() {
		return haoPai;
	}

	public void setHaoPai(String haoPai) {
		this.haoPai = haoPai;
	}

	public String getPremiumRate() {
		return premiumRate;
	}

	public void setPremiumRate(String premiumRate) {
		this.premiumRate = premiumRate;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	
}
