package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TdscBlockPart implements Serializable {
	// 子地块id
	private String partId;

	// 地块ID
	private String blockId;

	// 地块名称
	private String blockName;

	// 土地用途
	private String landUseType;

	private String greeningRate2;

	private String density2;

	// 容积率说明
	private String volumeRateMemo;

	public String getGreeningRate2() {
		return greeningRate2;
	}

	public void setGreeningRate2(String greeningRate2) {
		this.greeningRate2 = greeningRate2;
	}

	// 四至范围东
	private String rangeEast;

	public String getVolumeRate2() {
		return volumeRate2;
	}

	public void setVolumeRate2(String volumeRate2) {
		this.volumeRate2 = volumeRate2;
	}

	// 四至范围西
	private String rangeWest;

	// 四至范围南
	private String rangeSouth;

	// 四至范围北
	private String rangeNorth;

	// 出让总面积
	private BigDecimal totalLandArea;

	// 出让年限
	private String transferLife;

	// 规划容积率
	private String volumeRate;

	// 规划容积率2，用于介入的选项
	private String volumeRate2;

	// 规划建筑密度
	private String density;

	// 绿化率
	private String greeningRate;

	// 集中绿化率
	private String greeningRateCon;

	// 备注
	private String memo;

	// 地块编号
	private String blockCode;

	// 地块详细用途
	private String blockDetailedUsed;

	// 土地面积
	private BigDecimal blockArea;

	// 规划建筑面积
	private BigDecimal planBuildingArea;

	// 建筑容积率符号
	private String volumeRateSign;

	// 规划建筑密度符号
	private String densitySign;

	// 绿化率符号
	private String greeningRateSign;

	// 交易服务费
	private BigDecimal serviceCharge;
	//规划建筑密度下限(统计)
	private String density1;
	//绿地率下限(统计)
	private String greeningRate1;
	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public TdscBlockPart() {
	}

	public TdscBlockPart(String partId) {
		this.partId = partId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

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

	public String getGreeningRateCon() {
		return greeningRateCon;
	}

	public void setGreeningRateCon(String greeningRateCon) {
		this.greeningRateCon = greeningRateCon;
	}

	public String getLandUseType() {
		return landUseType;
	}

	public void setLandUseType(String landUseType) {
		this.landUseType = landUseType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getRangeEast() {
		return rangeEast;
	}

	public void setRangeEast(String rangeEast) {
		this.rangeEast = rangeEast;
	}

	public String getRangeNorth() {
		return rangeNorth;
	}

	public void setRangeNorth(String rangeNorth) {
		this.rangeNorth = rangeNorth;
	}

	public String getRangeSouth() {
		return rangeSouth;
	}

	public void setRangeSouth(String rangeSouth) {
		this.rangeSouth = rangeSouth;
	}

	public String getRangeWest() {
		return rangeWest;
	}

	public void setRangeWest(String rangeWest) {
		this.rangeWest = rangeWest;
	}

	public BigDecimal getTotalLandArea() {
		return totalLandArea;
	}

	public void setTotalLandArea(BigDecimal totalLandArea) {
		this.totalLandArea = totalLandArea;
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

	public BigDecimal getBlockArea() {
		return blockArea;
	}

	public void setBlockArea(BigDecimal blockArea) {
		this.blockArea = blockArea;
	}

	public String getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	public String getBlockDetailedUsed() {
		return blockDetailedUsed;
	}

	public void setBlockDetailedUsed(String blockDetailedUsed) {
		this.blockDetailedUsed = blockDetailedUsed;
	}

	public BigDecimal getPlanBuildingArea() {
		return planBuildingArea;
	}

	public void setPlanBuildingArea(BigDecimal planBuildingArea) {
		this.planBuildingArea = planBuildingArea;
	}

	public String getDensitySign() {
		return densitySign;
	}

	public void setDensitySign(String densitySign) {
		this.densitySign = densitySign;
	}

	public String getGreeningRateSign() {
		return greeningRateSign;
	}

	public void setGreeningRateSign(String greeningRateSign) {
		this.greeningRateSign = greeningRateSign;
	}

	public String getVolumeRateSign() {
		return volumeRateSign;
	}

	public void setVolumeRateSign(String volumeRateSign) {
		this.volumeRateSign = volumeRateSign;
	}

	public String getDensity2() {
		return density2;
	}

	public void setDensity2(String density2) {
		this.density2 = density2;
	}

	public String getVolumeRateMemo() {
		return volumeRateMemo;
	}

	public void setVolumeRateMemo(String volumeRateMemo) {
		this.volumeRateMemo = volumeRateMemo;
	}

	public String getDensity1() {
		return density1;
	}

	public void setDensity1(String density1) {
		this.density1 = density1;
	}

	public String getGreeningRate1() {
		return greeningRate1;
	}

	public void setGreeningRate1(String greeningRate1) {
		this.greeningRate1 = greeningRate1;
	}
	
}
