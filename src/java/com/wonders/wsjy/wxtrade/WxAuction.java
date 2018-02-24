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
 * �����ֳ����۴�����.
 * @author sunxin
 *
 */
public class WxAuction extends AbstractAuction implements ChangeListener{
	/**
	 * ����������
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
		//�����д�����ݿ�
		CoreService.finishTradeBlock(tradeBlock.getAppId());
		CoreService.getDbSupport().finishBlockTrade(tradeBlock, false);
		
	}

	public void blockTradeStart(TradeBlock tradeBlock) {
        //���ݿ�����״̬
		CoreService.getDbSupport().startBlockTrade(tradeBlock);
	}
	
	public void tradeFinish(TradeBlock tradeBlock) {
		CoreService.finishTradeBlock(tradeBlock.getAppId());
		CoreService.getDbSupport().finishBlockTrade(tradeBlock, true);
	}
	
	
	
	public void handleChangeEvent(ChangeEvent event) {
		if(ChangeEvent.PRICE_SUBMIT_EVENT.equals(event.getEventType())){
			PriceSubmitEvent changeEvent = (PriceSubmitEvent)event;
			//�������¼۸�
			updateTopPrice(changeEvent.getTradePrice());
			//�ָ�����ʱ��
			resumeLimitTime();
		}else if(ChangeEvent.STAGE_YH_EVENT.equals(event.getEventType())){
			//ѡ���Ƿ����ҡ���¼�
			YhSubmitEvent changeEvent = (YhSubmitEvent)event;
			//���²���ҡ�ŵ���Ա
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
