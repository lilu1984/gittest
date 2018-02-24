package com.wonders.engine.database;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradePrice;
/**
 * 数据库接口信息.
 * @author sunxin
 *
 */
public interface DbSupport {
	
	/**
	 * 初始化引擎信息.
	 * @return key:appId;value:TradeBlock
	 */
	public LinkedHashMap updateEngineBlockInfo();
	/**
	 * 根据appId更新地块信息。
	 * @param appId
	 */
	public TradeBlock updateEngineBlockInfo(String appId);
    /**
     * 初始化客户端管道
     * @param clientNo 客户端编号
     * @return [0]号牌；[1]:卡号等编号信息
     */
	public Map initClient(String clientNo);
	
	/**
	 * 第二种客户端初始化信息获取方法
	 * 
	 * 针对一个竞买人能够购买多个地块进行的调整
	 * @param clientNo
	 * @return
	 */
	public Map initClientModel2(String clientNo, Map paramMap);
	
	 /**
	  * 保存挂牌阶段报价信息.
	  * @param tradeBlock 地块对象
	  * @param tradePrice 报价对象
	  * @return 是否成功
	  */
	 public boolean saveListingPrice(TradeBlock tradeBlock,TradePrice tradePrice);
	 
	 /**
	  * 保存限时竞价报价信息.
	  * @param tradeBlock 地块对象
	  * @param tradePrice 报价对象
	  * @return 是否成功
	  */
	 public boolean saveAuctionPrice(TradeBlock tradeBlock,TradePrice tradePrice);

	 /**
	  * 交易开始.
	  * @param tradeBlockList 开始交易地块
	  * @return
	  */
	 public boolean startTrade(List tradeBlockList);

	 /**
	  * 交易结束.
	  * @param tradeBlockList 结束交易地块信息
	  * @return
	  */
	 public boolean finishTrade(List tradeBlockList);
	 
	 /**
	  * 地块竞价开始.
	  * @param tradeBlock 地块对象
	  * @return
	  */
	 public boolean startBlockTrade(TradeBlock tradeBlock);
	 
	 /**
	  * 地块竞价结束.
	  * @param tradeBlock 地块对象
	  * @param isAll 是否是最后
	  * @return
	  */
	 public boolean finishBlockTrade(TradeBlock tradeBlock,boolean isAll);
	 
	 /**
	  * 取得地块底价
	  * 
	  * @param tradeBlock
	  * @return
	  */
	 public BigDecimal getBlockBasePrice(TradeBlock tradeBlock);
	 
	 /**
	  * 确认底价，更改底价状态
	  * 
	  * @param tradeBlock
	  */
	 public void confirmBlock(TradeBlock tradeBlock);
	 
	 /**
	  * 设置为等待状态
	  * @param tradeBlock
	  */
	 public void confirmWaitBlock(TradeBlock tradeBlock);
	 
	 /**
	  * 交易完成
	  * 
	  * @param currBlock
	  */
	 public void overBlock(TradeBlock currBlock, boolean flag);
		/**
		 * 是否第一次报价(true是，false否)
		 * @param appId
		 * @param conNum
		 * @return
		 */
	 public boolean isFirstSubmitPrice(TradeBlock tradeBlock,TradePrice tradePrice);
}
