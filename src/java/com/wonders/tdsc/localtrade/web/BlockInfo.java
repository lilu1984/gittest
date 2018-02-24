package com.wonders.tdsc.localtrade.web;

public class BlockInfo {

	private static BlockInfo blockInfo = null;
	
	private BlockInfo(){}
	
	public static BlockInfo getInstance(){
		if(blockInfo == null){
			blockInfo = new BlockInfo();
		}
		return blockInfo;
	}
	
	private String appId;
	
	private String blockName;
	
	private String blockNoticeNo;
	
	private String transferMode;
	
	private String sceBidLoc;
	
	private String noticeId;

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
}
