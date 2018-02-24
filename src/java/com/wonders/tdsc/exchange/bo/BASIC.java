package com.wonders.tdsc.exchange.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class BASIC implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 土地编号 */
	private String blockId;

	/** 地块编号 */
	private String blockCode;

	/** 子地块ID */
	private String partId;

	/** 土地面积 BLOCK_AREA */
	private BigDecimal blockArea;

	/** 土地用途 LAND_USE_TYPE */
	private String landUseType;

	/** 出让年限 TRANSFER_LIFE */
	private String transferLife;

	/** 容积率 VOLUME_RATE */
	private String volumeRate;

	/** 建筑密度 DENSITY */
	private String density;

	/** 绿地率 GREENING_RATE */
	private String greeningRate;

	/** 规划建筑面积 PLAN_BUILDING_AREA */
	private BigDecimal planBuildingArea;

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	public BigDecimal getBlockArea() {
		return blockArea;
	}

	public void setBlockArea(BigDecimal blockArea) {
		this.blockArea = blockArea;
	}

	public String getLandUseType() {
		return landUseType;
	}

	public void setLandUseType(String landUseType) {
		this.landUseType = landUseType;
	}

	public String getTransferLife() {
		return transferLife;
	}

	public void setTransferLife(String transferLife) {
		this.transferLife = transferLife;
	}

	public String getVolumeRate() {
		return volumeRate;
	}

	public void setVolumeRate(String volumeRate) {
		this.volumeRate = volumeRate;
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

	public BigDecimal getPlanBuildingArea() {
		return planBuildingArea;
	}

	public void setPlanBuildingArea(BigDecimal planBuildingArea) {
		this.planBuildingArea = planBuildingArea;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}
}
