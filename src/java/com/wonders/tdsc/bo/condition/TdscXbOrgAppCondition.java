package com.wonders.tdsc.bo.condition;

import com.wonders.esframework.common.bo.BaseBO;


public class TdscXbOrgAppCondition extends BaseBO {

	private String orgAppId;

	private String orgName;

	private String orgZhucr;

	private String orgLinkMan;

	private String orgLinkPhone;

	private String ifUsed;

	private String memo;

	private String validity;

	private String lotNo;
	
	private int currentPage;// Ò³Êý

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getIfUsed() {
		return ifUsed;
	}

	public void setIfUsed(String ifUsed) {
		this.ifUsed = ifUsed;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgAppId() {
		return orgAppId;
	}

	public void setOrgAppId(String orgAppId) {
		this.orgAppId = orgAppId;
	}

	public String getOrgLinkMan() {
		return orgLinkMan;
	}

	public void setOrgLinkMan(String orgLinkMan) {
		this.orgLinkMan = orgLinkMan;
	}

	public String getOrgLinkPhone() {
		return orgLinkPhone;
	}

	public void setOrgLinkPhone(String orgLinkPhone) {
		this.orgLinkPhone = orgLinkPhone;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgZhucr() {
		return orgZhucr;
	}

	public void setOrgZhucr(String orgZhucr) {
		this.orgZhucr = orgZhucr;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}
}
