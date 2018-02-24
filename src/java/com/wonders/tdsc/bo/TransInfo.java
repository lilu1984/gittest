package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/** @author Hibernate CodeGenerator */
public class TransInfo implements Serializable {

	private String blockNoticeNo;

	private String transferMode;

	private String iCountPerson;

	private String countPerson;

	private String currPrice;
	
	private String listDate;

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public String getCountPerson() {
		return countPerson;
	}

	public void setCountPerson(String countPerson) {
		this.countPerson = countPerson;
	}

	public String getCurrPrice() {
		return currPrice;
	}

	public void setCurrPrice(String currPrice) {
		this.currPrice = currPrice;
	}

	public String getICountPerson() {
		return iCountPerson;
	}

	public void setICountPerson(String countPerson) {
		iCountPerson = countPerson;
	}

	public String getListDate() {
		return listDate;
	}

	public void setListDate(String listDate) {
		this.listDate = listDate;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	
}
