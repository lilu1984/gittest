package com.wonders.tdsc.bo;

import com.wonders.esframework.common.bo.BaseBO;

/**
 * TdscXbOrgApp entity.
 * 
 * @author wondersInfomation
 */

public class TdscXbOrgApp extends BaseBO {

	// Fields

	private String orgAppId;

	private String orgName;

	private String orgZhucr;

	private String orgLinkMan;

	private String orgLinkPhone;

	private String ifUsed;

	private String memo;

	private String validity;

	private String lotNo;

	// Constructors

	/** default constructor */
	public TdscXbOrgApp() {
	}

	/** full constructor */
	public TdscXbOrgApp(String orgName, String orgZhucr, String orgLinkMan,
			String orgLinkPhone, String ifUsed, String memo, String validity,
			String lotNo) {
		this.orgName = orgName;
		this.orgZhucr = orgZhucr;
		this.orgLinkMan = orgLinkMan;
		this.orgLinkPhone = orgLinkPhone;
		this.ifUsed = ifUsed;
		this.memo = memo;
		this.validity = validity;
		this.lotNo = lotNo;
	}

	// Property accessors

	public String getOrgAppId() {
		return this.orgAppId;
	}

	public void setOrgAppId(String orgAppId) {
		this.orgAppId = orgAppId;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgZhucr() {
		return this.orgZhucr;
	}

	public void setOrgZhucr(String orgZhucr) {
		this.orgZhucr = orgZhucr;
	}

	public String getOrgLinkMan() {
		return this.orgLinkMan;
	}

	public void setOrgLinkMan(String orgLinkMan) {
		this.orgLinkMan = orgLinkMan;
	}

	public String getOrgLinkPhone() {
		return this.orgLinkPhone;
	}

	public void setOrgLinkPhone(String orgLinkPhone) {
		this.orgLinkPhone = orgLinkPhone;
	}

	public String getIfUsed() {
		return this.ifUsed;
	}

	public void setIfUsed(String ifUsed) {
		this.ifUsed = ifUsed;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getValidity() {
		return this.validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

}