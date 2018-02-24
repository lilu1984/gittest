package com.wonders.wsjy.wxtrade;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.wonders.engine.CoreService;
import com.wonders.engine.auction.AbstractAuction;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.socket.event.ChangeEvent;
import com.wonders.engine.socket.event.PriceSubmitEvent;
import com.wonders.engine.socket.event.YhSubmitEvent;
import com.wonders.engine.socket.listener.ChangeListener;
import com.wonders.wsjy.TradeConsts;
/**
 * 无锡现场竞价处理场景.
 * @author sunxin
 *
 */
public class WxAuction extends AbstractAuction implements ChangeListener{
	/**
	 * 场景的主键
	 */
	private String auctionNo = null;
	
	public void initAuction(String auctionNo, LinkedList tradeBlockList, boolean isStart) {
		this.auctionNo = auctionNo;
		this.tradeBlockList = tradeBlockList;
		if(isStart){
			this.startStage();
		}
	}

	public void blockTradeFinish(TradeBlock tradeBlock) {
		//将结果写入数据库
		CoreService.finishTradeBlock(tradeBlock.getAppId());
		CoreService.getDbSupport().finishBlockTrade(tradeBlock, false);
		
	}

	public void blockTradeStart(TradeBlock tradeBlock) {
        //数据库设置状态
		CoreService.getDbSupport().startBlockTrade(tradeBlock);
	}
	
	public void tradeFinish(TradeBlock tradeBlock) {
		CoreService.finishTradeBlock(tradeBlock.getAppId());
		CoreService.getDbSupport().finishBlockTrade(tradeBlock, true);
	}
	
	
	
	public void handleChangeEvent(ChangeEvent event) {
		if(ChangeEvent.PRICE_SUBMIT_EVENT.equals(event.getEventType())){
			PriceSubmitEvent changeEvent = (PriceSubmitEvent)event;
			//更新最新价格
			updateTopPrice(changeEvent.getTradePrice());
			//恢复竞价时间
			resumeLimitTime();
		}else if(ChangeEvent.STAGE_YH_EVENT.equals(event.getEventType())){
			//选择是否参与摇号事件
			YhSubmitEvent changeEvent = (YhSubmitEvent)event;
			//更新参与摇号的人员
			updatePartakeConNum(changeEvent.getConNum(),changeEvent.isFlag());
		}
	}

	public String getAuctionNo() {
		return auctionNo;
	}

	public void blockIntervalFinish(TradeBlock tradeBlock) {
		
	}

	public boolean containsBasePrice(TradeBlock tradeBlock) {
		BigDecimal basePrice =  CoreService.getDbSupport().getBlockBasePrice(tradeBlock);
		if (basePrice != null) {
			tradeBlock.setBasePrice(basePrice);
			return true;
		}
		return false;
	}

}
