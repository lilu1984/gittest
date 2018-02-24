package com.wonders.tdsc.bo.condition;

import java.math.BigDecimal;
import java.util.Date;

import com.wonders.esframework.common.bo.BaseCondition;

/** @author Hibernate CodeGenerator */
public class TdscBankAppCondition extends BaseCondition {

	// 唯一标识
	private String id;

	// 缴款人
	private String payName;

	// 代缴人1
	private String payName1;

	// 代缴人2
	private String payName2;

	// 代缴人3
	private String payName3;

	// 代缴人4
	private String payName4;

	// 代缴人5
	private String payName5;

	// 缴款人账号
	private String accountNum;

	// 缴款时间
	private Date payDate;

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

	// 排序关键字
	private String orderKey;

	// 排序关键字类型 升序还是降序
	private String orderType;

	// 状态
	private String status;

	// 竞买人
	private String bidderId;

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
