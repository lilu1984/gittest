package com.wonders.tdsc.bo.condition;

import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscXbOrgInfoCondition extends BaseBO {

	private int currentPage;// Ò³Êý

	private String orgInfoId;

	private String batchNo;

	private Date stratDate;

	private String memo;

	private Date acitonDate;

	private Integer nextNo;

	private String currentOrgIds;

	private String status;
	
	private Date queryBeginDate;
	
	private Date queryEndDate;

	public Date getAcitonDate() {
		return acitonDate;
	}

	public void setAcitonDate(Date acitonDate) {
		this.acitonDate = acitonDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getCurrentOrgIds() {
		return currentOrgIds;
	}

	public void setCurrentOrgIds(String currentOrgIds) {
		this.currentOrgIds = currentOrgIds;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getNextNo() {
		return nextNo;
	}

	public void setNextNo(Integer nextNo) {
		this.nextNo = nextNo;
	}

	public String getOrgInfoId() {
		return orgInfoId;
	}

	public void setOrgInfoId(String orgInfoId) {
		this.orgInfoId = orgInfoId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStratDate() {
		return stratDate;
	}

	public void setStratDate(Date stratDate) {
		this.stratDate = stratDate;
	}

	public Date getQueryBeginDate() {
		return queryBeginDate;
	}

	public void setQueryBeginDate(Date queryBeginDate) {
		this.queryBeginDate = queryBeginDate;
	}

	public Date getQueryEndDate() {
		return queryEndDate;
	}

	public void setQueryEndDate(Date queryEndDate) {
		this.queryEndDate = queryEndDate;
	}
	
	
}
