package com.wonders.common.simplequery.bo;

public class QueryResult {
	private String blockNoticeNo;
	
	private String blockName;
	private String blockType;
	private String districtId;
	private String transferMode;
	private String sceBidLoc;
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}
	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}
	public String getBlockType() {
		return blockType;
	}
	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}
	public String getDistrictId() {
		return districtId;
	}
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	public String getSceBidLoc() {
		return sceBidLoc;
	}
	public void setSceBidLoc(String sceBidLoc) {
		this.sceBidLoc = sceBidLoc;
	}
	public String getTransferMode() {
		return transferMode;
	}
	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}
}
