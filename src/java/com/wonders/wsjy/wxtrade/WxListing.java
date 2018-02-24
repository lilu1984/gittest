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
 * ����������ƴ�����.
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
		//���������
		if(tradeBlock.isAim()){
			//��2���ˣ����������ˣ����Ϲ�����Խ��뽻�״���
			if(guapaiPersonCount >1){
				return true;
			}else{//�����˾��û�������
				return false;
			}
		}else{//���������
			//��2���ˣ����������ˣ����Ϲ�����Խ��뽻�״���
			if(guapaiPersonCount >1){
				return true;
			}if(guapaiPersonCount ==1){//һ�˳�����������һ���˱�����ֱ�Ӿ��ã�������˱����������ֳ�
				//��������
				int bidderPersonCount = wxDbService.getBidderPersonCount(tradeBlock.getAppId());
				//����ж���˱���
				if(bidderPersonCount>1){
					return true;
				}else{//���һ���˱�����ֱ�Ӿ���
					return false;
				}
			}else{//���˱��ۣ�����
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
		//��ӽ��׳���
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
	 * ��ʼ���ؿ齻����Ϣ.
	 * @param tradeBlockList ���׵ؿ���Ϣ.
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
