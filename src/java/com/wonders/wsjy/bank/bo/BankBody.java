package com.wonders.wsjy.bank.bo;
/**
 * ��������ģ��
 *  InstCode	�����ʺ�	C		�����ʺ�
	InDate	��֤����ʱ��	C	14	20060911150101
	InAmount	���ʽ��	C		��λ��Ԫ����2λС����
	InName	�����˻���	C		
	InAcct	�������˺�	C		
	InMemo	���˺�	C		�����˺�
	InBankFLCode	���н�����ˮ��	C		���н�����ˮ��,���ڱ�ʾΨһһ�ʵ��ˡ�

 * @author Administrator
 *
 */
public class BankBody {
	/**
	 * �����ʺ�
	 */
	private String instCode;
	/**
	 * ��֤����ʱ�� 14	20060911150101
	 */
	private String inDate;
	/**
	 * ���ʽ��	��λ��Ԫ����2λС����
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
	 * ���˺�
	 */
	private String inMemo;
	/**
	 * ���н�����ˮ�� ���н�����ˮ��,���ڱ�ʾΨһһ�ʵ��ˡ�
	 */
	private String inBankFLCode;
	/**
	 * �����(Ψһ)�൱�ڶ�����
	 */
	private String instSeq;
	/**
	 * MatuDay	������ 14 	��ʽ�����գ�yyyyMMddHHmmss�����м��޷ָ����磺20060911112233
		�����յڶ������˺Ž��Զ�ʧЧ��������ʱ����Ҫ���׽�������֤�����˻�
	 */
	private String matuDay;
	

	
	/**
	 * --------------------------------------
	 * --------------------------------------
	 * ������
	 * --------------------------------------
	 * --------------------------------------
	 */
	/**
	 * �������00���ɹ�����01�������ظ���09�� ��������99��ϵͳ����
	 */
	private String result;
	/**
	 * ������
	 */
	private String addWord;
	/**
	 * ���˺� (���˺������к���ϵͳ�Զ�����)
	 */
	private String acctNo;
	/**
	 * ��¼��
	 */
	private String count;
	/**
	 * ��ѯ��������Ϣ��֪ͨδ������ʱʹ��
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
