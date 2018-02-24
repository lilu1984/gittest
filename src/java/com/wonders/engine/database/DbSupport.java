package com.wonders.engine.database;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradePrice;
/**
 * ���ݿ�ӿ���Ϣ.
 * @author sunxin
 *
 */
public interface DbSupport {
	
	/**
	 * ��ʼ��������Ϣ.
	 * @return key:appId;value:TradeBlock
	 */
	public LinkedHashMap updateEngineBlockInfo();
	/**
	 * ����appId���µؿ���Ϣ��
	 * @param appId
	 */
	public TradeBlock updateEngineBlockInfo(String appId);
    /**
     * ��ʼ���ͻ��˹ܵ�
     * @param clientNo �ͻ��˱��
     * @return [0]���ƣ�[1]:���ŵȱ����Ϣ
     */
	public Map initClient(String clientNo);
	
	/**
	 * �ڶ��ֿͻ��˳�ʼ����Ϣ��ȡ����
	 * 
	 * ���һ���������ܹ��������ؿ���еĵ���
	 * @param clientNo
	 * @return
	 */
	public Map initClientModel2(String clientNo, Map paramMap);
	
	 /**
	  * ������ƽ׶α�����Ϣ.
	  * @param tradeBlock �ؿ����
	  * @param tradePrice ���۶���
	  * @return �Ƿ�ɹ�
	  */
	 public boolean saveListingPrice(TradeBlock tradeBlock,TradePrice tradePrice);
	 
	 /**
	  * ������ʱ���۱�����Ϣ.
	  * @param tradeBlock �ؿ����
	  * @param tradePrice ���۶���
	  * @return �Ƿ�ɹ�
	  */
	 public boolean saveAuctionPrice(TradeBlock tradeBlock,TradePrice tradePrice);

	 /**
	  * ���׿�ʼ.
	  * @param tradeBlockList ��ʼ���׵ؿ�
	  * @return
	  */
	 public boolean startTrade(List tradeBlockList);

	 /**
	  * ���׽���.
	  * @param tradeBlockList �������׵ؿ���Ϣ
	  * @return
	  */
	 public boolean finishTrade(List tradeBlockList);
	 
	 /**
	  * �ؿ龺�ۿ�ʼ.
	  * @param tradeBlock �ؿ����
	  * @return
	  */
	 public boolean startBlockTrade(TradeBlock tradeBlock);
	 
	 /**
	  * �ؿ龺�۽���.
	  * @param tradeBlock �ؿ����
	  * @param isAll �Ƿ������
	  * @return
	  */
	 public boolean finishBlockTrade(TradeBlock tradeBlock,boolean isAll);
	 
	 /**
	  * ȡ�õؿ�׼�
	  * 
	  * @param tradeBlock
	  * @return
	  */
	 public BigDecimal getBlockBasePrice(TradeBlock tradeBlock);
	 
	 /**
	  * ȷ�ϵ׼ۣ����ĵ׼�״̬
	  * 
	  * @param tradeBlock
	  */
	 public void confirmBlock(TradeBlock tradeBlock);
	 
	 /**
	  * ����Ϊ�ȴ�״̬
	  * @param tradeBlock
	  */
	 public void confirmWaitBlock(TradeBlock tradeBlock);
	 
	 /**
	  * �������
	  * 
	  * @param currBlock
	  */
	 public void overBlock(TradeBlock currBlock, boolean flag);
		/**
		 * �Ƿ��һ�α���(true�ǣ�false��)
		 * @param appId
		 * @param conNum
		 * @return
		 */
	 public boolean isFirstSubmitPrice(TradeBlock tradeBlock,TradePrice tradePrice);
}
