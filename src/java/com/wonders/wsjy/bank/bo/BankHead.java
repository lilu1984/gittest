package com.wonders.wsjy.bank.bo;

/**
 * ����ͷģ��
 * 	TransCode	���״���	C	6	M	
	TransDate	��������	C	8	M	�����գ��м��޷ָ����磺20100306
	TransTime	����ʱ��	C	6	M	ʱ���룬�޷ָ�24Сʱ�ƣ��磺190201
	SeqNo	��ˮ��	C		M	���ظ���ˮ��
 * @author Administrator
 *
 */
public class BankHead {
	private String transCode;
	private String transDate;
	private String transTime;
	private String seqNo;
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	
}
