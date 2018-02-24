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
	 * ������ƻ򾺼۽���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response){
		String appId = request.getParameter("appId");
		//�ؿ���Ϣ
		TdscTradeView tradeView = appService.getTdscTradeView(appId);
		//������Ϣ
		List listingAppList = appService.findListingApp(appId);
		//��֤��ǰ�û��Ƿ��Ѿ�ȡ���˾����ʸ���ȡ�˺���
		TdscBidderApp bidderApp = this.appService.getCurrUserTradeByAppId(request, appId, true);
		//��ǰ��������
		//��ǰ��ע����
		int bidderNum = this.appService.getBlockBidderNum(appId);
		int lookingNum = CoreService.getClientPool().size();
		//��ȡ��ǰ�ĵؿ�����״̬
		String tranStatus = this.appService.getCurrBlockTranStatus(tradeView);
		String canSendPrice = "0";//��ǰ�Ƿ���Ա���1���ԡ�0������
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
