package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/** @author Hibernate CodeGenerator */
public class BaseInfo implements Serializable {

	private String blockNoticeNo;

	private String landName;

	private String landLocation;

	private String totalLandArea;

	private String landUseType;

	private String sellYear;

	private String initPrice;

	private String marginAmount;

	private String transferMode;

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(String initPrice) {
		this.initPrice = initPrice;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public String getLandName() {
		return landName;
	}

	public void setLandName(String landName) {
		this.landName = landName;
	}

	public String getLandUseType() {
		return landUseType;
	}

	public void setLandUseType(String landUseType) {
		this.landUseType = landUseType;
	}

	public String getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(String marginAmount) {
		this.marginAmount = marginAmount;
	}

	public String getSellYear() {
		return sellYear;
	}

	public void setSellYear(String sellYear) {
		this.sellYear = sellYear;
	}

	public String getTotalLandArea() {
		return totalLandArea;
	}

	public void setTotalLandArea(String totalLandArea) {
		this.totalLandArea = totalLandArea;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}
	
	
}
