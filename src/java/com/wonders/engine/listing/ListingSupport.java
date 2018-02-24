package com.wonders.engine.listing;

import java.util.LinkedList;
import java.util.Map;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradeNotice;

/**
 * �������ӿ�.
 * @author sunxin
 *
 */
public interface ListingSupport {
	/**
	 * ���ʹ�����ʱ�䵹��ʱ.
	 *
	 */
	public abstract void sendListingTime(Map paramMap);
	/**
	 * ����������.
	 *
	 */
	public abstract void startListing();
	
	/**
	 * ��Ҫ�������Ͻ��׹��ƽ�����û�н����ֳ��ĵؿ飬ֱ����ɴ˴ν���.
	 *
	 */
	public abstract void initListing();
	
	/**
	 * ��Ҫ�������Ͻ��׹��ƽ�����û�н����ֳ��ĵؿ飬ֱ����ɴ˴ν���.
	 * @param tradeNotice �������ƵĹ���
	 */
	public abstract void triggerFinishWsjy(TradeNotice tradeNotice);
	
	/**
	 * �����ս�
	 * @param tradeNotice
	 */
	public abstract void triggerFinishGp(TradeNotice tradeNotice);
	
	/**
	 * ��Ҫ�������Ͻ��׹��ƽ������н����ֳ��ĵؿ飬���������Ͼ���.
	 * @param tradeNotice �������ƵĹ���
	 * @param wsjyList ���뾺�۵ĵؿ��б�
	 */
	public abstract void triggerStartAuction(TradeNotice tradeNotice,LinkedList wsjyList);
	
	/**
	 * ��Ҫ���ֳ����׹��ƽ�����Ĵ���.
	 * @param tradeNotice �������ƵĹ���
	 */
	public abstract void triggerFinishListing(TradeNotice tradeNotice);

	
	/**
	 * ��ת�۵�Ĳ���������ʱ�����.
	 * @param tradeNotice �������ƵĹ���
	 */
	public abstract void triggerListing(TradeNotice tradeNotice);
	
	/**
	 * �ؿ��Ƿ��ܽ������Ͼ��ۣ������뾺�ۻ��ڵĹ���.
	 * @param tradeBlock �ؿ����
	 * @return
	 */
	public abstract boolean isEnterAuction(TradeBlock tradeBlock);
}
