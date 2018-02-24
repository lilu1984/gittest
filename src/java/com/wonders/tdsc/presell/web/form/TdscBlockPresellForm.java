package com.wonders.tdsc.presell.web.form;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class TdscBlockPresellForm extends ActionForm {
	
	private String[] selectedIds;
	
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
	
	private FormFile guihuaWenben;
	
	private String district;
	
	private String nominate;
	
	private String nominateImageId;
	
	private FormFile nominateImageFile;
	

	private String remarks;
	
	private String[] fileName;
	
	private ArrayList formFileList;
	public ArrayList getFormFileList() {
		return formFileList;
	}

	public void setFormFileList(ArrayList formFileList) {
		this.formFileList = formFileList;
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

	public FormFile getNominateImageFile() {
		return nominateImageFile;
	}

	public void setNominateImageFile(FormFile nominateImageFile) {
		this.nominateImageFile = nominateImageFile;
	}

	public String getNominateImageId() {
		return nominateImageId;
	}

	public void setNominateImageId(String nominateImageId) {
		this.nominateImageId = nominateImageId;
	}

	public String[] getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String[] selectedIds) {
		this.selectedIds = selectedIds;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public FormFile getGuihuaWenben() {
		return guihuaWenben;
	}

	public void setGuihuaWenben(FormFile guihuaWenben) {
		this.guihuaWenben = guihuaWenben;
	}

	public String[] getFileName() {
		return fileName;
	}

	public void setFileName(String[] fileName) {
		this.fileName = fileName;
	}
	public String getBlockNo() {
		return blockNo;
	}

	public void setBlockNo(String blockNo) {
		this.blockNo = blockNo;
	}
	
}
