package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class TdscBlockPart implements Serializable {
	// �ӵؿ�id
	private String partId;

	// �ؿ�ID
	private String blockId;

	// �ؿ�����
	private String blockName;

	// ������;
	private String landUseType;

	private String greeningRate2;

	private String density2;

	// �ݻ���˵��
	private String volumeRateMemo;

	public String getGreeningRate2() {
		return greeningRate2;
	}

	public void setGreeningRate2(String greeningRate2) {
		this.greeningRate2 = greeningRate2;
	}

	// ������Χ��
	private String rangeEast;

	public String getVolumeRate2() {
		return volumeRate2;
	}

	public void setVolumeRate2(String volumeRate2) {
		this.volumeRate2 = volumeRate2;
	}

	// ������Χ��
	private String rangeWest;

	// ������Χ��
	private String rangeSouth;

	// ������Χ��
	private String rangeNorth;

	// ���������
	private BigDecimal totalLandArea;

	// ��������
	private String transferLife;

	// �滮�ݻ���
	private String volumeRate;

	// �滮�ݻ���2�����ڽ����ѡ��
	private String volumeRate2;

	// �滮�����ܶ�
	private String density;

	// �̻���
	private String greeningRate;

	// �����̻���
	private String greeningRateCon;

	// ��ע
	private String memo;

	// �ؿ���
	private String blockCode;

	// �ؿ���ϸ��;
	private String blockDetailedUsed;

	// �������
	private BigDecimal blockArea;

	// �滮�������
	private BigDecimal planBuildingArea;

	// �����ݻ��ʷ���
	private String volumeRateSign;

	// �滮�����ܶȷ���
	private String densitySign;

	// �̻��ʷ���
	private String greeningRateSign;

	// ���׷����
	private BigDecimal serviceCharge;
	//�滮�����ܶ�����(ͳ��)
	private String density1;
	//�̵�������(ͳ��)
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
