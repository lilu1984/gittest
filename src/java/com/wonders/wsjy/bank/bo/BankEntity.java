package com.wonders.wsjy.bank.bo;
/**
 *  InDate	����ʱ��	C	14	20060911150101
	InAmount	���ʽ��	C		��λ��Ԫ����2λС����
	InName	�����˻���	C		
	InAcct	�������˺�	C		
	InBankFLCode	���н�����ˮ��	C		

 * @author Administrator
 *
 */
public class BankEntity {
	/**
	 * ����ʱ��	C	14	20060911150101
	 */
	private String inDate;
	/**
	 * ���ʽ��	C		��λ��Ԫ����2λС����
	 */
	private String inAmount;
	/**
	 * �����˻���
	 */
	private String inName;
	/**
	 * �������˺�
	 */
	private String inAcct;
	/**
	 * ���н�����ˮ��	
	 */
	private String inBankFLCode;
	/**
	 * �տ��˺�
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
