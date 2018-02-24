package com.wonders.wsjy.wxtrade;

import java.util.LinkedList;
import java.util.List;

import bjca.org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradeNotice;
import com.wonders.engine.listing.AbstractListing;
import com.wonders.wsjy.TradeConsts;
/**
 * 无锡公告挂牌触发器.
 * @author sunxin
 *
 */
public class WxListing extends AbstractListing{

	private WxDbService wxDbService = (WxDbService) CoreService.getDbSupport();
	private Logger	logger	= Logger.getLogger(WxListing.class);
	
	public void initListing() {
		this.engineNoticeList = wxDbService.updateEngineNoticeInfo();
		//CoreService.updateTradeBlocks();
		setRefreshTime(180);
	}

	public boolean isEnterAuction(TradeBlock tradeBlock) {
		int guapaiPersonCount = wxDbService.getguapaiPersonCount(tradeBlock.getAppId());
		tradeBlock.setTradeResult(""+guapaiPersonCount);
		//如果有意向
		if(tradeBlock.isAim()){
			//有2个人（包括意向人）以上购买可以进入交易大厅
			if(guapaiPersonCount >1){
				return true;
			}else{//意向人竞得或者流拍
				return false;
			}
		}else{//如果无意向
			//有2个人（包括意向人）以上购买可以进入交易大厅
			if(guapaiPersonCount >1){
				return true;
			}if(guapaiPersonCount ==1){//一人出价情况：如果一个人报名，直接竞得；如果多人报名；进入现场
				//报名人数
				int bidderPersonCount = wxDbService.getBidderPersonCount(tradeBlock.getAppId());
				//如果有多个人报名
				if(bidderPersonCount>1){
					return true;
				}else{//如果一个人报名，直接竞得
					return false;
				}
			}else{//无人报价，流拍
				return false;
			}
		}
		
	}

	public void triggerFinishListing(TradeNotice tradeNotice) {
		wxDbService.pushWf(tradeNotice.getNoticeId(),false);
	}

	public void triggerFinishWsjy(TradeNotice tradeNotice) {
		wxDbService.finishTrade(CoreService.findTradeBlockListByNoticeId(tradeNotice.getNoticeId()));
	}

	public void triggerStartAuction(TradeNotice tradeNotice,LinkedList wsjyList) {
		this.initTradeBlockInfo(CoreService.findTradeBlockListByNoticeId(tradeNotice.getNoticeId()));
		wxDbService.updateBlockPlanTableStatus(tradeNotice.getPlanId(), false);
		//添加交易场景
		WxAuction wxAuctionStage = new WxAuction();
		wxAuctionStage.initAuction(tradeNotice.getNoticeId(), wsjyList, true);
		CoreService.addAuction(wxAuctionStage);
	}

	public void triggerFinishGp(TradeNotice tradeNotice) {
		List tradeBlockList = CoreService.findTradeBlockListByNoticeId(tradeNotice.getNoticeId());
		for (int i = 0; i < tradeBlockList.size(); i++) {
			TradeBlock tradeBlock = (TradeBlock) tradeBlockList.get(i);
			if (!isEnterAuction(tradeBlock) && !tradeBlock.isConfirmFloorPrice()) {
				wxDbService.finishBlockTrade(tradeBlock, false);
			}
		}
	}

	public void triggerListing(TradeNotice tradeNotice) {
		
	}
	
	/**
	 * 初始化地块交易信息.
	 * @param tradeBlockList 交易地块信息.
	 * @return
	 */
	private boolean initTradeBlockInfo(List tradeBlockList) {
		for (int i = 0; i < tradeBlockList.size(); i++) {
			TradeBlock tradeBlock = (TradeBlock) tradeBlockList.get(i);
			if (isEnterAuction(tradeBlock)) {
				wxDbService.updateBlockTranApp(tradeBlock, TradeConsts.TRADE_WAIT);
			} else {
				wxDbService.finishBlockTrade(tradeBlock, false);
			}
		}
		return false;
	}
}
