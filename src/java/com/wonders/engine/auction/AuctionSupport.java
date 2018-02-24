package com.wonders.engine.auction;

import java.util.Map;

import com.wonders.engine.bo.TradeBlock;
/**
 * �����Ľӿ�
 * @author sunxin
 *
 */
public interface AuctionSupport {

	/**
	 * ���ͳ���ʱ�䵹��ʱ.
	 *
	 */
	public abstract void sendListingTime(Map paramMap);
	/**
	 * ��ȡ�����ı��.
	 * @return
	 */
	public abstract String getAuctionNo();
	
	/**
	 * �жϵؿ��Ƿ����ڴ˳���.
	 * @param appId �ؿ齻������
	 * @return
	 */
	public abstract boolean existBlock(String appId);
	
	/**
	 * ���׿�ʼ.
	 */
	public abstract void tradeStart(TradeBlock tradeBlock);
	/**
	 * ���׽���.
	 *
	 */
	
	public abstract void tradeFinish(TradeBlock tradeBlock);
	/**
	 * �ؿ齻�׿�ʼ.
	 * @param tradeBlock ���׿�ʼ�ؿ�
	 */
	
	public abstract void blockTradeStart(TradeBlock tradeBlock);
	
	/**
	 * �ؿ齻�׽���.
	 * @param tradeBlock ������ɵؿ�
	 */
	public abstract void blockTradeFinish(TradeBlock tradeBlock);
	
	/**
	 * �ؿ齻�׼����ʼ.
	 * @param tradeBlock ׼����ʼ�ؿ�
	 */
	public abstract void blockIntervalStart(TradeBlock tradeBlock);
	
	/**
	 * �ؿ齻�׼������.
	 * @param tradeBlock ׼����ʼ�ĵؿ�
	 */
	public abstract void blockIntervalFinish(TradeBlock tradeBlock);
	
	/**
	 * �Ƿ��е׼�
	 * @param tradeBlock
	 * @return
	 */
	public abstract boolean containsBasePrice(TradeBlock tradeBlock);
	
	/**
	 * ���ⲿ���ó�������ʱʱ��
	 *
	 */
	public abstract void setLimitTime(String appId, int times);
	
	/**
	 * ���ͱ�����Ϣ 
	 * @param appId �ؿ���
	 * @param clientNo
	 * @param conNo
	 * @param yktBh
	 */
	public void basePriceSubmit(String appId, String clientNo, String conNo, String yktBh);
	
	/**
	 * ����noticeIdȡ�õ�ǰ�Ľ��׵ؿ�
	 * @param noticeId
	 */
	public TradeBlock getTradeBlock(String noticeId);
}
