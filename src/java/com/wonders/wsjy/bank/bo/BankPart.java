package com.wonders.wsjy.bank.bo;
/**
 * ���нӿ�֧����(�ɽ�XML�ļ�ת��Ϊ�˶��󣬻򽫴˶���ת��Ϊxml)
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
