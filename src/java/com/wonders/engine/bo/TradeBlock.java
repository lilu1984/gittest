package com.wonders.engine.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 交易地块信息.
 *
 */
public class TradeBlock {
	/**
	 * 地块申请主键
	 */
	private String appId;
	/**
	 * 竞价倒计时
	 */
	private long limitTime;
	/**
	 * 出价轮次
	 */
	private int priceNum;
	/**
	 * 加价幅度
	 */
	private double priceAdd;
	/**
	 * 挂牌会主键
	 */
	private String listingId;

	/**
	 * 公告主键
	 */
	private String noticeId;

	/**
	 * 最高价
	 */
	private double topPrice;

	/**
	 * 高价出价的客户端
	 */
	private String topClientNo;

	/**
	 * 高价出价的号牌
	 */
	private String topConNum;

	/**
	 * 地块公告号
	 */
	private String blockNoticeNo;

	/**
	 * 交易结果：0流拍；1直接成交；2竞价后成交
	 */
	private String tradeResult;
	
	/**
	 * 是否确认底价
	 */
	private boolean confirmBasePrice = false;
	
	/**
	 * 是否意向竞买
	 */
	private boolean isAim;
	/**
	 * 是否有限价0否，1是
	 */
	private boolean haveMaxPrice;
	/**
	 * 最高限价
	 */
	private double maxPrice;
	/**
	 * 参与摇号的人员号牌列表
	 */
	private LinkedList partakeConNumList = new LinkedList();
	/**
	 * 不参与摇号的人员号牌列表
	 */
	private LinkedList notPartakeConNumList = new LinkedList();
	/**
	 * 是否已经发送询问最高价格信息
	 */
	private boolean sendMaxPriceMsg = false;
	

	public boolean isSendMaxPriceMsg() {
		return sendMaxPriceMsg;
	}
	public void setSendMaxPriceMsg(boolean sendMaxPriceMsg) {
		this.sendMaxPriceMsg = sendMaxPriceMsg;
	}
	public LinkedList getPartakeConNumList() {
		return partakeConNumList;
	}
	public void setPartakeConNumList(LinkedList partakeConNumList) {
		this.partakeConNumList = partakeConNumList;
	}
	public LinkedList getNotPartakeConNumList() {
		return notPartakeConNumList;
	}
	public void setNotPartakeConNumList(LinkedList notPartakeConNumList) {
		this.notPartakeConNumList = notPartakeConNumList;
	}
	/**
	 * 是否有最高限价
	 * @return
	 */
	public boolean getHaveMaxPrice() {
		if(!Double.isNaN(maxPrice)&&maxPrice!=0){
			return true;
		}
		return false;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	public String getTopConNum() {
		return topConNum;
	}
	public void setTopConNum(String topConNum) {
		this.topConNum = topConNum;
	}
	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}
	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}
	public String getTopClientNo() {
		return topClientNo;
	}
	public void setTopClientNo(String topClientNo) {
		this.topClientNo = topClientNo;
	}
	public boolean isAim() {
		return isAim;
	}
	public void setAim(boolean isAim) {
		this.isAim = isAim;
	}
	public double getTopPrice() {
		return topPrice;
	}
	public void setTopPrice(double topPrice) {
		this.topPrice = topPrice;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public long getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public double getPriceAdd() {
		return priceAdd;
	}
	public void setPriceAdd(double priceAdd) {
		this.priceAdd = priceAdd;
	}
	public int getPriceNum() {
		return priceNum;
	}
	public void setPriceNum(int priceNum) {
		this.priceNum = priceNum;
	}
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public String getTradeResult() {
		return tradeResult;
	}
	public void setTradeResult(String tradeResult) {
		this.tradeResult = tradeResult;
	}
	
	// 是否开始倒计时
	private boolean isMiniLimit = false;
	
	// 底价
	private BigDecimal basePrice = null;
	
	/**
	 * 开始底价倒计时
	 */
	public void beginMiniPriceLimit() {
		this.limitTime = 60*5;
		this.isMiniLimit = true;
	}
	
	public void endMiniPriceLimit() {
		this.limitTime = 0;
	}
	
	public boolean isMiniLimit() {
		return isMiniLimit;
	}
	
	public BigDecimal getBasePrice() {
		return basePrice;
	}
	
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
	/**
	 * 获取字符串类型的所有参与人员列表
	 * @return
	 */
	public String getStrPartakeConNum(){
		String result = "";
		for(int i=0;this.getPartakeConNumList()!=null&&i<this.getPartakeConNumList().size();i++){
			String conNum = (String)this.getPartakeConNumList().get(i);
			result += conNum;
			if(i!=this.getPartakeConNumList().size()-1){
				result += ",";
			}
		}
		return result;
	}
	/**
	 * 当前最高价是否达到最高限价
	 * @return
	 */
	public boolean confirmMaxPriceByTopPrice(){
		if(this.getHaveMaxPrice()){
			BigDecimal topPrice = new BigDecimal(this.topPrice);
			if(topPrice.doubleValue()<this.maxPrice){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	/**
	 * 校验交易结束时达到当前最高限价后的交易结果状态（假如只有一家竞买人参与摇号则直接成交,若两家及两家以上则摇号，无人选择则流拍）
	 * 2，可以正常交易结束，0流拍。5等待摇号
	 * @return
	 */
	public String finishTradeEndByMaxPrice(){
		if(this.confirmMaxPriceByTopPrice()){
			if(this.partakeConNumList==null||this.partakeConNumList.size()==0){
				return "0";
			}else{
				if(this.partakeConNumList.size()==1){
					return "2";
				}else{
					return "5";
				}
			}
		}
		return "2";
	}
	public boolean isValidTopPrice() {
		BigDecimal topPrice = new BigDecimal(this.topPrice);
		if (basePrice == null||topPrice.compareTo(basePrice) > -1) {
			// 如果当前最高报价大于等于底价则为有效报价
			return true;
		}
		// 否则为无效报价
		return false;
	}
	
	public String toString() {
		return appId;
	}
	
	public void confirm() {
		confirmBasePrice = true;
	}
	
	public boolean isConfirmFloorPrice() {
		return confirmBasePrice;
	}
	
	
}
