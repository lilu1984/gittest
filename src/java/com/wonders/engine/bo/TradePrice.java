package com.wonders.engine.bo;

import java.sql.Timestamp;
/**
 * ���׳�����Ϣ.
 *
 */
public class TradePrice {
	/**
	 * �ؿ���������
	 */
	private String appId;
	/**
	 * ����
	 */
	private String op;
	/**
	 * �ͻ��˱�ʶ
	 */
	private String clientNo;
	/**
	 * �۸�
	 */
	private double price;
	/**
	 * ���۳ɹ�ʱ��
	 */
	private Timestamp priceTime;
	/**
	 * ������Ϣ
	 */
	private String msg;
	/**
	 * �׶� 1���� 2����
	 */
	private String phase;
	/**
	 * �����ִ�
	 */
	private int priceNum;

	/**
	 * ����
	 */
	private String conNum;
	/**
	 * һ��ͨ���
	 */
    private String yktBh;
    
    /**
     * �����ʶ��
     */
    private String flag;
    
    /**
     * �׼۱��۱�ʶ
     */
    private String baseConfig;
    
    /**
     * ������
     */
    private String noticeId;

	public String getYktBh() {
		return yktBh;
	}

	public void setYktBh(String yktBh) {
		this.yktBh = yktBh;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public int getPriceNum() {
		return priceNum;
	}

	public void setPriceNum(int priceNum) {
		this.priceNum = priceNum;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getClientNo() {
		return clientNo;
	}

	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Timestamp getPriceTime() {
		return priceTime;
	}

	public void setPriceTime(Timestamp priceTime) {
		this.priceTime = priceTime;
	}

	public String getConNum() {
		return conNum;
	}

	public void setConNum(String conNum) {
		this.conNum = conNum;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBaseConfig() {
		return baseConfig;
	}

	public void setBaseConfig(String baseConfig) {
		this.baseConfig = baseConfig;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	
}
