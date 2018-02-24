package com.wonders.engine.bo;

import java.sql.Timestamp;
/**
 * 交易公告信息.
 *
 */
public class TradeNotice {
	/**
	 * 公告主键
	 */
	private String noticeId;
	/**
	 * 公告主键
	 */
	private String noticeNo;
	/**
	 * 地块执行主键
	 */
	private String planId;
	/**
	 * 挂牌开始日期
	 */
    private Timestamp listStartDate;
	/**
	 * 挂牌结束日期
	 */
    private Timestamp listEndDate;

	/**
	 * 挂牌结束日期
	 */
    private long surplusTime;
    
	/**
	 * 是否是网上交易
	 */
    private boolean isWsjy;
    /**
     * 竞价开始时间
     */
    private Timestamp onLineStatDate;
    
    /**
     * 竞价截止时间
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
