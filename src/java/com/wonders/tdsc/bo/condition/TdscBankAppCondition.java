package com.wonders.tdsc.bo.condition;

import java.math.BigDecimal;
import java.util.Date;

import com.wonders.esframework.common.bo.BaseCondition;

/** @author Hibernate CodeGenerator */
public class TdscBankAppCondition extends BaseCondition {

	// Ψһ��ʶ
	private String id;

	// �ɿ���
	private String payName;

	// ������1
	private String payName1;

	// ������2
	private String payName2;

	// ������3
	private String payName3;

	// ������4
	private String payName4;

	// ������5
	private String payName5;

	// �ɿ����˺�
	private String accountNum;

	// �ɿ�ʱ��
	private Date payDate;

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

	// ����ؼ���
	private String orderKey;

	// ����ؼ������� �����ǽ���
	private String orderType;

	// ״̬
	private String status;

	// ������
	private String bidderId;

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

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayName1() {
		return payName1;
	}

	public void setPayName1(String payName1) {
		this.payName1 = payName1;
	}

	public String getPayName2() {
		return payName2;
	}

	public void setPayName2(String payName2) {
		this.payName2 = payName2;
	}

	public String getPayName3() {
		return payName3;
	}

	public void setPayName3(String payName3) {
		this.payName3 = payName3;
	}

	public String getPayName4() {
		return payName4;
	}

	public void setPayName4(String payName4) {
		this.payName4 = payName4;
	}

	public String getPayName5() {
		return payName5;
	}

	public void setPayName5(String payName5) {
		this.payName5 = payName5;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
