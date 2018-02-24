package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;


/** @author Hibernate CodeGenerator */
public class TdscSendEmailInfo implements Serializable {

    /** identifier field */
    private String emailId;
    
    /** �ļ�ID*/
    private String noticeId;
    
    /** �ļ���*/
    private String noticeNo;

    /** �ļ����ͣ�1Ϊ���ù��棻2Ϊ�����ʾ */
    private String noticeType;
  
    /** ���յ�λ */
    private String acceptOrg;
    
    /** �ʼ����յ�ַ */
    private String acceptAddress;
    
    /** �Ƿ��Ѿ����� */
    private String ifSend;
    
    /** �ʼ�����ʱ�� */
    private Timestamp sendTime;

    /**�ĵ����*/
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
