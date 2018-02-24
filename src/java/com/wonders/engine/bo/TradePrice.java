package com.wonders.engine.bo;

import java.sql.Timestamp;
/**
 * 交易出价信息.
 *
 */
public class TradePrice {
	/**
	 * 地块申请主键
	 */
	private String appId;
	/**
	 * 操作
	 */
	private String op;
	/**
	 * 客户端标识
	 */
	private String clientNo;
	/**
	 * 价格
	 */
	private double price;
	/**
	 * 报价成功时间
	 */
	private Timestamp priceTime;
	/**
	 * 返回消息
	 */
	private String msg;
	/**
	 * 阶段 1挂牌 2竞价
	 */
	private String phase;
	/**
	 * 出价轮次
	 */
	private int priceNum;

	/**
	 * 号牌
	 */
	private String conNum;
	/**
	 * 一卡通编号
	 */
    private String yktBh;
    
    /**
     * 特殊标识符
     */
    private String flag;
    
    /**
     * 底价报价标识
     */
    private String baseConfig;
    
    /**
     * 公告编号
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
