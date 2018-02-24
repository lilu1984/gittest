package com.wonders.tdsc.bank.web.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.struts.action.ActionForm;

public class TdscBankAppForm extends ActionForm {

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

	// 关联状态
	private String status;

	// 竞买人ID
	private String bidderId;

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

}
