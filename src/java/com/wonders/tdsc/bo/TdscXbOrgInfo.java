package com.wonders.tdsc.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.bo.BaseBO;

/**
 * TdscXbOrgInfo entity.
 * 
 * @author wondersInfomation
 */

public class TdscXbOrgInfo extends BaseBO {

	// Fields

	private String orgInfoId;

	private String batchNo;

	private Date stratDate;

	private String memo;

	private Date acitonDate;

	private Integer nextNo;

	private String currentOrgId;

	private String status;

	private String nextOrgId;
	// Constructors

	/** default constructor */
	public TdscXbOrgInfo() {
	}

	/** full constructor */
	public TdscXbOrgInfo(String batchNo, Date stratDate, String memo,
			Date acitonDate, Integer nextNo, String currentOrgIds,
			String status) {
		this.batchNo = batchNo;
		this.stratDate = stratDate;
		this.memo = memo;
		this.acitonDate = acitonDate;
		this.nextNo = nextNo;
		this.currentOrgId = currentOrgIds;
		this.status = status;
	}

	// Property accessors

	public String getOrgInfoId() {
		return this.orgInfoId;
	}

	public void setOrgInfoId(String orgInfoId) {
		this.orgInfoId = orgInfoId;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getStratDate() {
		return this.stratDate;
	}

	public void setStratDate(Date stratDate) {
		this.stratDate = stratDate;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getAcitonDate() {
		return this.acitonDate;
	}

	public void setAcitonDate(Date acitonDate) {
		this.acitonDate = acitonDate;
	}

	public Integer getNextNo() {
		return this.nextNo;
	}

	public void setNextNo(Integer nextNo) {
		this.nextNo = nextNo;
	}

	public String getCurrentOrgId() {
		return this.currentOrgId;
	}

	public void setCurrentOrgId(String currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List getOrgIds(){
		List list = new ArrayList();
		if (StringUtils.isNotEmpty(this.currentOrgId)){
			String[] ids = this.currentOrgId.split(",");
			for (int i = 0 ; i < ids.length; i++) {
				list.add(ids[i]);
			}
			return list;
		}
		return list;
	}

	public String getNextOrgId() {
		return nextOrgId;
	}

	public void setNextOrgId(String nextOrgId) {
		this.nextOrgId = nextOrgId;
	}

}