package com.wonders.wsjy.bank.bo;
/**
 * 银行接口支持类(可将XML文件转换为此对象，或将此对象转化为xml)
 * @author Administrator
 *
 */
public class BankPart {
	private BankHead bankHead;
	private BankBody bankBody;
	public BankBody getBankBody() {
		return bankBody;
	}
	public void setBankBody(BankBody bankBody) {
		this.bankBody = bankBody;
	}
	public BankHead getBankHead() {
		return bankHead;
	}
	public void setBankHead(BankHead bankHead) {
		this.bankHead = bankHead;
	}
}
