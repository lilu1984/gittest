package com.wonders.android.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.android.AppConsts;
import com.wonders.android.service.AppService;
import com.wonders.engine.CoreService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.wsjy.bo.TdscTradeView;

public class TradeAction extends BaseAction{
	private AppService appService;

	public void setAppService(AppService appService) {
		this.appService = appService;
	}
	
	
	/**
	 * 进入挂牌或竞价界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		//地块信息
		TdscTradeView tradeView = appService.getTdscTradeView(appId);
		//报价信息
		List listingAppList = appService.findListingApp(appId);
		//验证当前用户是否已经取得了竞买资格并领取了号牌
		TdscBidderApp bidderApp = this.appService.getCurrUserTradeByAppId(request, appId, true);
		//当前报名人数
		//当前关注人数
		int bidderNum = this.appService.getBlockBidderNum(appId);
		int lookingNum = CoreService.getClientPool().size();
		//获取当前的地块所处状态
		String tranStatus = this.appService.getCurrBlockTranStatus(tradeView);
		String canSendPrice = "0";//当前是否可以报价1可以、0不可以
		if(bidderApp!=null && (AppConsts.TRAN_STATUS_LISTING.equals(tranStatus)||AppConsts.TRAN_STATUS_AUCTION.equals(tranStatus))){
			canSendPrice = "1";
		}
		request.setAttribute("bidderApp", bidderApp);
		request.setAttribute("userId", this.appService.getUserInfo(request));
		request.setAttribute("tradeView", tradeView);
		request.setAttribute("listingAppList", listingAppList);
		request.setAttribute("canSendPrice", canSendPrice);
		request.setAttribute("bidderNum", bidderNum);
		request.setAttribute("lookingNum", lookingNum);
		request.setAttribute("tranStatus", tranStatus);
		return mapping.findForward("block-info");
	}
}
