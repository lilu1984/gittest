package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.util.List;

/** @author Hibernate CodeGenerator */
public class BlockAuctionInfo implements Serializable {

	private String blockNoticeNo;

	private List list;

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	
}
