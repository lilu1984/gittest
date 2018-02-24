package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscBankApp extends BaseBO {

	// Ψһ��ʶ
	private String id;

	// �ɿ���
	private String payName;

	// �ɿ����˺�
	private String accountNum;

	// �ɿ�ʱ��
	private Timestamp payDate;

	// �ɿ���
	private BigDecimal money;

	// ��������
	private String acceptBank;

	// ҵ����ˮ��
	private String serialNum;

	// ����ʱ��
	private Date operatingTime;

	// ������
	private String operator;

	// ��ע
	private String memo;

	// ״̬
	private String status;

	// ������
	private String bidderId;

	// ������������
	private String acceptBankName;
	
	// ����ID
	private String noticeId;
		

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getAcceptBank() {
		return acceptBank;
	}

	public void setAcceptBank(String acceptBank) {
		this.acceptBank = acceptBank;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(Date operatingTime) {
		this.operatingTime = operatingTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Timestamp getPayDate() {
		return payDate;
	}

	public void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAcceptBankName() {
		return acceptBankName;
	}

	public void setAcceptBankName(String acceptBankName) {
		this.acceptBankName = acceptBankName;
	}

}
