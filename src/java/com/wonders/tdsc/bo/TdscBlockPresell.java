package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscBlockPresell extends BaseBO {

	private String presellId;

	private String blockName;
	private String blockNo;
	private String landLocation;

	private BigDecimal landArea;

	private String landUse;

	private String volumeRate1;

	private String volumeRate2;

	private String volumeRateSign;

	private String greeningRate;

	private String density;
	
	private String district;
	
	private String nominate;
	
	private String nominateImageId;
	
	private String available;
	
	private String remarks;
	
	private Date createTime;
	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getGreeningRate() {
		return greeningRate;
	}

	public void setGreeningRate(String greeningRate) {
		this.greeningRate = greeningRate;
	}

	public BigDecimal getLandArea() {
		return landArea;
	}

	public void setLandArea(BigDecimal landArea) {
		this.landArea = landArea;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public String getLandUse() {
		return landUse;
	}

	public void setLandUse(String landUse) {
		this.landUse = landUse;
	}

	public String getPresellId() {
		return presellId;
	}

	public void setPresellId(String presellId) {
		this.presellId = presellId;
	}

	public String getVolumeRate1() {
		return volumeRate1;
	}

	public void setVolumeRate1(String volumeRate1) {
		this.volumeRate1 = volumeRate1;
	}

	public String getVolumeRate2() {
		return volumeRate2;
	}

	public void setVolumeRate2(String volumeRate2) {
		this.volumeRate2 = volumeRate2;
	}

	public String getVolumeRateSign() {
		return volumeRateSign;
	}

	public void setVolumeRateSign(String volumeRateSign) {
		this.volumeRateSign = volumeRateSign;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getNominate() {
		return nominate;
	}

	public void setNominate(String nominate) {
		this.nominate = nominate;
	}

	public String getNominateImageId() {
		return nominateImageId;
	}

	public void setNominateImageId(String nominateImageId) {
		this.nominateImageId = nominateImageId;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBlockNo() {
		return blockNo;
	}

	public void setBlockNo(String blockNo) {
		this.blockNo = blockNo;
	}
	
	
	
}
