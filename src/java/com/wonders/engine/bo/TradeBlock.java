package com.wonders.engine.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * ���׵ؿ���Ϣ.
 *
 */
public class TradeBlock {
	/**
	 * �ؿ���������
	 */
	private String appId;
	/**
	 * ���۵���ʱ
	 */
	private long limitTime;
	/**
	 * �����ִ�
	 */
	private int priceNum;
	/**
	 * �Ӽ۷���
	 */
	private double priceAdd;
	/**
	 * ���ƻ�����
	 */
	private String listingId;

	/**
	 * ��������
	 */
	private String noticeId;

	/**
	 * ��߼�
	 */
	private double topPrice;

	/**
	 * �߼۳��۵Ŀͻ���
	 */
	private String topClientNo;

	/**
	 * �߼۳��۵ĺ���
	 */
	private String topConNum;

	/**
	 * �ؿ鹫���
	 */
	private String blockNoticeNo;

	/**
	 * ���׽����0���ģ�1ֱ�ӳɽ���2���ۺ�ɽ�
	 */
	private String tradeResult;
	
	/**
	 * �Ƿ�ȷ�ϵ׼�
	 */
	private boolean confirmBasePrice = false;
	
	/**
	 * �Ƿ�������
	 */
	private boolean isAim;
	/**
	 * �Ƿ����޼�0��1��
	 */
	private boolean haveMaxPrice;
	/**
	 * ����޼�
	 */
	private double maxPrice;
	/**
	 * ����ҡ�ŵ���Ա�����б�
	 */
	private LinkedList partakeConNumList = new LinkedList();
	/**
	 * ������ҡ�ŵ���Ա�����б�
	 */
	private LinkedList notPartakeConNumList = new LinkedList();
	/**
	 * �Ƿ��Ѿ�����ѯ����߼۸���Ϣ
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
	 * �Ƿ�������޼�
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
	
	// �Ƿ�ʼ����ʱ
	private boolean isMiniLimit = false;
	
	// �׼�
	private BigDecimal basePrice = null;
	
	/**
	 * ��ʼ�׼۵���ʱ
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
	 * ��ȡ�ַ������͵����в�����Ա�б�
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
	 * ��ǰ��߼��Ƿ�ﵽ����޼�
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
	 * У�齻�׽���ʱ�ﵽ��ǰ����޼ۺ�Ľ��׽��״̬������ֻ��һ�Ҿ����˲���ҡ����ֱ�ӳɽ�,�����Ҽ�����������ҡ�ţ�����ѡ�������ģ�
	 * 2�������������׽�����0���ġ�5�ȴ�ҡ��
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
			// �����ǰ��߱��۴��ڵ��ڵ׼���Ϊ��Ч����
			return true;
		}
		// ����Ϊ��Ч����
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
