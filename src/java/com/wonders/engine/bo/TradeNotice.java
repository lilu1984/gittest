package com.wonders.engine.bo;

import java.sql.Timestamp;
/**
 * ���׹�����Ϣ.
 *
 */
public class TradeNotice {
	/**
	 * ��������
	 */
	private String noticeId;
	/**
	 * ��������
	 */
	private String noticeNo;
	/**
	 * �ؿ�ִ������
	 */
	private String planId;
	/**
	 * ���ƿ�ʼ����
	 */
    private Timestamp listStartDate;
	/**
	 * ���ƽ�������
	 */
    private Timestamp listEndDate;

	/**
	 * ���ƽ�������
	 */
    private long surplusTime;
    
	/**
	 * �Ƿ������Ͻ���
	 */
    private boolean isWsjy;
    /**
     * ���ۿ�ʼʱ��
     */
    private Timestamp onLineStatDate;
    
    /**
     * ���۽�ֹʱ��
     */
    private Timestamp onLineEndDate;
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public boolean isWsjy() {
		return isWsjy;
	}
	public void setWsjy(boolean isWsjy) {
		this.isWsjy = isWsjy;
	}
	public Timestamp getListEndDate() {
		return listEndDate;
	}
	public void setListEndDate(Timestamp listEndDate) {
		this.listEndDate = listEndDate;
	}
	public Timestamp getListStartDate() {
		return listStartDate;
	}
	public void setListStartDate(Timestamp listStartDate) {
		this.listStartDate = listStartDate;
	}
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public long getSurplusTime() {
		return surplusTime;
	}
	public void setSurplusTime(long surplusTime) {
		this.surplusTime = surplusTime;
	}
	public String getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	public Timestamp getOnLineStatDate() {
		return onLineStatDate;
	}
	public void setOnLineStatDate(Timestamp onLineStatDate) {
		this.onLineStatDate = onLineStatDate;
	}
	public Timestamp getOnLineEndDate() {
		return onLineEndDate;
	}
	public void setOnLineEndDate(Timestamp onLineEndDate) {
		this.onLineEndDate = onLineEndDate;
	}
    

}
