package com.wonders.tdsc.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

public class TdscBankApp extends BaseBO {

	// 唯一标识
	private String id;

	// 缴款人
	private String payName;

	// 缴款人账号
	private String accountNum;

	// 缴款时间
	private Timestamp payDate;

	// 缴款金额
	private BigDecimal money;

	// 接收银行
	private String acceptBank;

	// 业务流水号
	private String serialNum;

	// 操作时间
	private Date operatingTime;

	// 操作人
	private String operator;

	// 备注
	private String memo;

	// 状态
	private String status;

	// 竞买人
	private String bidderId;

	// 接收银行名称
	private String acceptBankName;
	
	// 公告ID
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
