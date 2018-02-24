package com.wonders.wsjy.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradePrice;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.service.SubscribeService;
import com.wonders.wsjy.service.TradeServer;
import com.wonders.wsjy.wxtrade.WxDbService;

public class MyTransAction extends BaseAction {
	private TradeServer tradeServer;

	private TdscLocalTradeService tdscLocalTradeService;

	private TdscBidderAppService tdscBidderAppService;

	private CommonQueryService commonQueryService;

	private TdscScheduletableService tdscScheduletableService;

	private TdscBlockInfoService tdscBlockInfoService;
	
	private SubscribeService subscribeService;

	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}

	public void setTdscLocalTradeService(TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public ActionForward queryMyTransList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String userId = getClientNo(request);
		
		PersonInfo info = subscribeService.getPersonInfo(userId);
		
		if (info != null) {
			String userName = info.getBidderName();
			request.getSession().setAttribute("userName", userName);
		}

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		HashMap blockHaoPaiMap = new HashMap();// 用来存放appId与号牌关系
		HashMap blockCertNoMap = new HashMap();// 用来存放appId与资格证书号关系
		HashMap selectDiJiaMap = new HashMap();// 用来存放appId与此竞买人是否可以选择底价成交关系
		List tdscBlockAppViewList = new ArrayList();// 用来存放该竞买人竞买的所有地块
		List bidderAppList = tradeServer.getBidderViewListByUserId(userId);
		// 分别获取我竞买的地块列表
		for (int i = 0 ; i < bidderAppList.size(); i++) {
			TdscBidderView bidderView = (TdscBidderView) bidderAppList.get(i);
			TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(bidderView.getAppId());
			if (StringUtils.isNotBlank(bidderView.getNoticeId()) && StringUtils.isNotBlank(bidderView.getAppId())) {// 该竞买人所购地块必须已经公告
				String appId = bidderView.getAppId();
				condition.setAppId(appId);
				TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
				if (!"04".equals(tdscBlockAppView.getTranResult())) {// 终止交易的地块，不需要显示出来
					tdscBlockAppViewList.add(tdscBlockAppView);
					blockHaoPaiMap.put(tdscBlockAppView.getAppId(), bidderView.getConNum());
					blockCertNoMap.put(tdscBlockAppView.getAppId(), bidderView.getCertNo());
					//1为由底价选择，0为无底价选择
					if(listingInfo!=null){
						if(bidderView.getCertNo().equals(listingInfo.getListCert())){
							selectDiJiaMap.put(tdscBlockAppView.getAppId(), "1");
						}else{
							selectDiJiaMap.put(tdscBlockAppView.getAppId(), "0");
						}
					}
				}
			}
		}

		
		/**
		 * 设置我的申购信息
		 * 
		 */
		
		String certNo = (String) request.getSession().getAttribute("certNo");
		List list = subscribeService.getMySubscribeList(certNo);
		request.setAttribute("tdscBidderApp", list);
		request.setAttribute("flag", request.getParameter("flag"));
		
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("blockHaoPaiMap", blockHaoPaiMap);
		request.setAttribute("blockCertNoMap", blockCertNoMap);
		request.setAttribute("selectDiJiaMap", selectDiJiaMap);
		return mapping.findForward("myTransList");
	}

	public ActionForward gpRecordList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		if (!filterParam(appId)) {// 判断参数是否合法,不合法转到登陆页面
			System.out.println("appId参数不合法：value=" + appId);
			return mapping.findForward("tdscCaLogin");
		}
		String priceType = request.getParameter("priceType");
		if (!"11".equals(priceType) && !"22".equals(priceType)) {// 判断参数是否合法,不合法转到登陆页面
			System.out.println("priceType参数不合法：value=" + priceType);
			return mapping.findForward("tdscCaLogin");
		}
		HashMap certNoConNumMap = new HashMap();// 资格证书号与号牌的关系

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		if (null != appId) {
			String listingId = this.tdscLocalTradeService.queryListingId(appId);
			if (null != listingId) {
				List retList = new ArrayList();
				retList = this.tdscLocalTradeService.queryTdscListingAppListByListingId(listingId, priceType);
				if (retList != null && retList.size() > 0) {
					for (int m = 0; m < retList.size(); m++) {
						TdscListingApp tdscListingApp = (TdscListingApp) retList.get(m);
						TdscBidderApp tdscBidderApp = tdscBidderAppService.getBidderAppByCertNo(tdscListingApp.getListCert());
						if (tdscBidderApp != null && tdscListingApp != null)
							certNoConNumMap.put(tdscListingApp.getListCert(), tdscBidderApp.getConNum());
					}
				}
				request.setAttribute("retList", retList);
			}
		}

		request.setAttribute("priceType", priceType);// 竞价类型 11:挂牌 22:限时竞价
		request.setAttribute("certNoConNumMap", certNoConNumMap);

		return mapping.findForward("gpRecordList");
	}

	public ActionForward money(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockId = request.getParameter("blockId");
		if (!filterParam(blockId)) {// 判断参数是否合法,不合法转到登陆页面
			System.out.println("blockId参数不合法：value=" + blockId);
			return mapping.findForward("tdscCaLogin");
		}
		TdscBlockTranApp tdscBlockTranApp = tdscScheduletableService.getBlockTranAppInfo(blockId);
		TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findPlanTableByPlanId(tdscBlockTranApp.getPlanId());

		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		return mapping.findForward("money");
	}

	public ActionForward auction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String planId = "";
		TdscBlockPlanTable blockPlan = null;

		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null & !"".equals(tdscBlockAppView.getBlockId())) {
			planId = tdscBlockAppView.getPlanId();

			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal totalArea = new BigDecimal(0);// 土地面积
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null)
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
			}
			// 目前无锡一个地块里面只有一个tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			// 出让价款单价存在TotalLandArea中,子地块名称拼起 存在UnitebBlockCode中
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));

			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
			if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0)) > 0) {// 填写了地质灾害评估费，并且大于0
				request.setAttribute("geologicalHazard", tdscBlockInfo.getGeologicalHazard());
			} else {
				request.setAttribute("geologicalHazard", new BigDecimal(0));
			}
		}

		/* 按竞买人资格证书编号查看竞得人信息,并得到竞得人的资格证书编号 */
		String certNo = request.getParameter("certNo");
		TdscBidderApp tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfo(certNo);
		List bidderPersonList = null;
		if (null != tdscBidderApp) {
			bidderPersonList = this.tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());
			String resultName = "";
			if (null != bidderPersonList && bidderPersonList.size() > 0) {
				for (int i = 0; i < bidderPersonList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(i);
					resultName += tdscBidderPersonApp.getBidderName() + ",";
				}
			}
			if (!"".equals(resultName)) {
				resultName = resultName.substring(0, resultName.length() - 1);
				request.setAttribute("resultName", resultName);
			}
		}
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("certNo", certNo);

		if (!StringUtils.isEmpty(planId)) {
			blockPlan = tdscLocalTradeService.getTdscBlockPlanTable(planId);
			request.setAttribute("tdscBlockPlanTable", blockPlan);
		}

		if(!"05".equals(tdscBlockAppView.getTranResult()))
			return mapping.findForward("auction");
		else
			return mapping.findForward("bidderRoll");

	}

	/**
	 * 
	 * 取得用户的资格证书编号 2011110025
	 * 
	 * @param request
	 * @return
	 */
	private String getClientNo(HttpServletRequest request) {
		String clientNo = (String) request.getSession().getAttribute("certNo");
		return clientNo;
	}
	
	public ActionForward saveTranResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1为同意，2为不同意
		String flag = request.getParameter("flag");
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		TradePrice tradePrice = new TradePrice();
		TradeBlock tradeBlock = new TradeBlock();
		//当前最高报价的竞买人信息。
		TdscBidderApp tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfo(certNo);
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		//地块信息
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		
		double dijia = tdscBlockAppView.getDiJia().doubleValue();
		WxDbService wxDbService = new WxDbService();
		
		if(!"03".equals(tdscBlockAppView.getTranResult())){
			return mapping.findForward("dijia-alert");
		}
		if("1".equals(flag)){
			//同意的处理逻辑。
			//1，增加当前地块最高报价竞买人的报价信息，新的报价信息等于底价。
			//2,将当前地块状态改为成交
			
			tradePrice.setAppId(appId);
			tradePrice.setClientNo(certNo);
			tradePrice.setConNum(tdscBidderApp.getConNum());
			tradePrice.setPriceTime(new Timestamp(System.currentTimeMillis()));
			tradePrice.setPrice(dijia);
			tradePrice.setYktBh(tdscBidderApp.getYktBh());
			tradePrice.setPhase("1");
			
			tradeBlock.setAppId(appId);
			tradeBlock.setPriceNum(listingInfo.getCurrRound().intValue());
			tradeBlock.setListingId(listingInfo.getListingId());
			tradeBlock.setTradeResult("1");
			tradeBlock.setTopPrice(dijia);
			tradeBlock.setBasePrice(new BigDecimal(dijia));
			tradeBlock.setTopClientNo(certNo);
			wxDbService.saveListingPrice(tradeBlock, tradePrice);
			wxDbService.finishBlockTrade(tradeBlock, false);
		}else if("0".equals(flag)){
			//不同意的处理逻辑。
			//1，将当前地块的状态改为国土局收回。
			tradeBlock.setAppId(appId);
			tradeBlock.setTradeResult("0");
			wxDbService.finishBlockTrade(tradeBlock, false);
			
		}
		return new ActionForward("trans.do?method=queryMyTransList",true);
	}
}
