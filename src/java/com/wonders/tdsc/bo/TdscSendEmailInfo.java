package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;


/** @author Hibernate CodeGenerator */
public class TdscSendEmailInfo implements Serializable {

    /** identifier field */
    private String emailId;
    
    /** 文件ID*/
    private String noticeId;
    
    /** 文件号*/
    private String noticeNo;

    /** 文件类型：1为出让公告；2为结果公示 */
    private String noticeType;
  
    /** 接收单位 */
    private String acceptOrg;
    
    /** 邮件接收地址 */
    private String acceptAddress;
    
    /** 是否已经发布 */
    private String ifSend;
    
    /** 邮件发送时间 */
    private Timestamp sendTime;

    /**文档编号*/
    private String recordId;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getAcceptOrg() {
		return acceptOrg;
	}

	public void setAcceptOrg(String acceptOrg) {
		this.acceptOrg = acceptOrg;
	}

	public String getAcceptAddress() {
		return acceptAddress;
	}

	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}

	public String getIfSend() {
		return ifSend;
	}

	public void setIfSend(String ifSend) {
		this.ifSend = ifSend;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
}
