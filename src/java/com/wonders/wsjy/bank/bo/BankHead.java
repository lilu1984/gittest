package com.wonders.wsjy.bank.bo;

/**
 * 报文头模型
 * 	TransCode	交易代码	C	6	M	
	TransDate	交易日期	C	8	M	年月日，中间无分隔，如：20100306
	TransTime	交易时间	C	6	M	时分秒，无分隔24小时制，如：190201
	SeqNo	流水号	C		M	不重复流水号
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
