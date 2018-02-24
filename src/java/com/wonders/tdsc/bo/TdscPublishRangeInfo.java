package com.wonders.tdsc.bo;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscPublishRangeInfo extends BaseBO {

    /** INFO_ID */
    private String infoId;

    /** CN_NAME */
    private String cnName;

    /** EMAIL */
    private String email;

    /** PHONE */
    private String phone;

    /** WEBADDRESS */
    private String webAddress;

    /** PLAN_ID*/
    private String planId;

    /** NOTICE_ID */
    private String noticeId;

    /** FILE_ID */
    private String fileId;

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

}
