package com.wonders.wsjy.bank.bo;
/**
 *  InDate	到帐时间	C	14	20060911150101
	InAmount	到帐金额	C		单位（元），2位小数点
	InName	付款人户名	C		
	InAcct	付款人账号	C		
	InBankFLCode	银行交易流水号	C		

 * @author Administrator
 *
 */
public class BankEntity {
	/**
	 * 到帐时间	C	14	20060911150101
	 */
	private String inDate;
	/**
	 * 到帐金额	C		单位（元），2位小数点
	 */
	private String inAmount;
	/**
	 * 付款人户名
	 */
	private String inName;
	/**
	 * 付款人账号
	 */
	private String inAcct;
	/**
	 * 银行交易流水号	
	 */
	private String inBankFLCode;
	/**
	 * 收款账号
	 */
	private String inMemo;
	public String getInAcct() {
		return inAcct;
	}
	public void setInAcct(String inAcct) {
		this.inAcct = inAcct;
	}
	public String getInAmount() {
		return inAmount;
	}
	public void setInAmount(String inAmount) {
		this.inAmount = inAmount;
	}
	public String getInBankFLCode() {
		return inBankFLCode;
	}
	public void setInBankFLCode(String inBankFLCode) {
		this.inBankFLCode = inBankFLCode;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getInName() {
		return inName;
	}
	public void setInName(String inName) {
		this.inName = inName;
	}
	public String getInMemo() {
		return inMemo;
	}
	public void setInMemo(String inMemo) {
		this.inMemo = inMemo;
	}
}
