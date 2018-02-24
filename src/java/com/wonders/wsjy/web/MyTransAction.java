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
		HashMap blockHaoPaiMap = new HashMap();// �������appId����ƹ�ϵ
		HashMap blockCertNoMap = new HashMap();// �������appId���ʸ�֤��Ź�ϵ
		HashMap selectDiJiaMap = new HashMap();// �������appId��˾������Ƿ����ѡ��׼۳ɽ���ϵ
		List tdscBlockAppViewList = new ArrayList();// ������Ÿþ����˾�������еؿ�
		List bidderAppList = tradeServer.getBidderViewListByUserId(userId);
		// �ֱ��ȡ�Ҿ���ĵؿ��б�
		for (int i = 0 ; i < bidderAppList.size(); i++) {
			TdscBidderView bidderView = (TdscBidderView) bidderAppList.get(i);
			TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(bidderView.getAppId());
			if (StringUtils.isNotBlank(bidderView.getNoticeId()) && StringUtils.isNotBlank(bidderView.getAppId())) {// �þ����������ؿ�����Ѿ�����
				String appId = bidderView.getAppId();
				condition.setAppId(appId);
				TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
				if (!"04".equals(tdscBlockAppView.getTranResult())) {// ��ֹ���׵ĵؿ飬����Ҫ��ʾ����
					tdscBlockAppViewList.add(tdscBlockAppView);
					blockHaoPaiMap.put(tdscBlockAppView.getAppId(), bidderView.getConNum());
					blockCertNoMap.put(tdscBlockAppView.getAppId(), bidderView.getCertNo());
					//1Ϊ�ɵ׼�ѡ��0Ϊ�޵׼�ѡ��
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
		 * �����ҵ��깺��Ϣ
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
		if (!filterParam(appId)) {// �жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("appId�������Ϸ���value=" + appId);
			return mapping.findForward("tdscCaLogin");
		}
		String priceType = request.getParameter("priceType");
		if (!"11".equals(priceType) && !"22".equals(priceType)) {// �жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("priceType�������Ϸ���value=" + priceType);
			return mapping.findForward("tdscCaLogin");
		}
		HashMap certNoConNumMap = new HashMap();// �ʸ�֤�������ƵĹ�ϵ

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

		request.setAttribute("priceType", priceType);// �������� 11:���� 22:��ʱ����
		request.setAttribute("certNoConNumMap", certNoConNumMap);

		return mapping.findForward("gpRecordList");
	}

	public ActionForward money(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockId = request.getParameter("blockId");
		if (!filterParam(blockId)) {// �жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("blockId�������Ϸ���value=" + blockId);
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

			BigDecimal totalArea = new BigDecimal(0);// �������
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null)
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
			}
			// Ŀǰ����һ���ؿ�����ֻ��һ��tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			// ���üۿ�۴���TotalLandArea��,�ӵؿ�����ƴ�� ����UnitebBlockCode��
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));

			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
			if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0)) > 0) {// ��д�˵����ֺ������ѣ����Ҵ���0
				request.setAttribute("geologicalHazard", tdscBlockInfo.getGeologicalHazard());
			} else {
				request.setAttribute("geologicalHazard", new BigDecimal(0));
			}
		}

		/* ���������ʸ�֤���Ų鿴��������Ϣ,���õ������˵��ʸ�֤���� */
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
	 * ȡ���û����ʸ�֤���� 2011110025
	 * 
	 * @param request
	 * @return
	 */
	private String getClientNo(HttpServletRequest request) {
		String clientNo = (String) request.getSession().getAttribute("certNo");
		return clientNo;
	}
	
	public ActionForward saveTranResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1Ϊͬ�⣬2Ϊ��ͬ��
		String flag = request.getParameter("flag");
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		TradePrice tradePrice = new TradePrice();
		TradeBlock tradeBlock = new TradeBlock();
		//��ǰ��߱��۵ľ�������Ϣ��
		TdscBidderApp tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfo(certNo);
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		//�ؿ���Ϣ
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		
		double dijia = tdscBlockAppView.getDiJia().doubleValue();
		WxDbService wxDbService = new WxDbService();
		
		if(!"03".equals(tdscBlockAppView.getTranResult())){
			return mapping.findForward("dijia-alert");
		}
		if("1".equals(flag)){
			//ͬ��Ĵ����߼���
			//1�����ӵ�ǰ�ؿ���߱��۾����˵ı�����Ϣ���µı�����Ϣ���ڵ׼ۡ�
			//2,����ǰ�ؿ�״̬��Ϊ�ɽ�
			
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
			//��ͬ��Ĵ����߼���
			//1������ǰ�ؿ��״̬��Ϊ�������ջء�
			tradeBlock.setAppId(appId);
			tradeBlock.setTradeResult("0");
			wxDbService.finishBlockTrade(tradeBlock, false);
			
		}
		return new ActionForward("trans.do?method=queryMyTransList",true);
	}
}
