package com.wonders.wsjy.bank.bo;
/**
 * 报文主题模型
 *  InstCode	入账帐号	C		入账帐号
	InDate	保证金到帐时间	C	14	20060911150101
	InAmount	到帐金额	C		单位（元），2位小数点
	InName	付款人户名	C		
	InAcct	付款人账号	C		
	InMemo	子账号	C		即子账号
	InBankFLCode	银行交易流水号	C		银行交易流水号,用于标示唯一一笔到账。

 * @author Administrator
 *
 */
public class BankBody {
	/**
	 * 入账帐号
	 */
	private String instCode;
	/**
	 * 保证金到帐时间 14	20060911150101
	 */
	private String inDate;
	/**
	 * 到帐金额	单位（元），2位小数点
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
	 * 子账号
	 */
	private String inMemo;
	/**
	 * 银行交易流水号 银行交易流水号,用于标示唯一一笔到账。
	 */
	private String inBankFLCode;
	/**
	 * 竞买号(唯一)相当于订单号
	 */
	private String instSeq;
	/**
	 * MatuDay	到期日 14 	格式年月日（yyyyMMddHHmmss），中间无分隔，如：20060911112233
		到期日第二天子账号将自动失效，但销户时间需要交易结束并保证金已退还
	 */
	private String matuDay;
	

	
	/**
	 * --------------------------------------
	 * --------------------------------------
	 * 处理结果
	 * --------------------------------------
	 * --------------------------------------
	 */
	/**
	 * 处理代码00：成功处理；01：接收重复；09： 其它错误；99：系统错误
	 */
	private String result;
	/**
	 * 处理结果
	 */
	private String addWord;
	/**
	 * 子账号 (子账号由银行核心系统自动生成)
	 */
	private String acctNo;
	/**
	 * 记录数
	 */
	private String count;
	/**
	 * 查询付款人信息或通知未竞得人时使用
	 */
	private BankEntityList bankEntityList;
	
	
	
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
	public String getInMemo() {
		return inMemo;
	}
	public void setInMemo(String inMemo) {
		this.inMemo = inMemo;
	}
	public String getInName() {
		return inName;
	}
	public void setInName(String inName) {
		this.inName = inName;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	public BankEntityList getBankEntityList() {
		return bankEntityList;
	}
	public void setBankEntityList(BankEntityList bankEntityList) {
		this.bankEntityList = bankEntityList;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getInstSeq() {
		return instSeq;
	}
	public void setInstSeq(String instSeq) {
		this.instSeq = instSeq;
	}
	public String getMatuDay() {
		return matuDay;
	}
	public void setMatuDay(String matuDay) {
		this.matuDay = matuDay;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
