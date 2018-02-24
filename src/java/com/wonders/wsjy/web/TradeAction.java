package com.wonders.wsjy.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.engine.BaseStore;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.service.SubscribeService;
import com.wonders.wsjy.service.TradeServer;

public class TradeAction extends BaseAction {

	private TradeServer tradeServer;
	private SubscribeService subscribeService;
	public SubscribeService getSubscribeService() {
		return subscribeService;
	}

	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}

	public TradeServer getTradeServer() {
		return tradeServer;
	}

	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	/**
	 * ����ϵͳ����ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward redirectIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String clientNo = getClientNo(request);
		request.setAttribute("clientNo", clientNo);
		return mapping.findForward("index");
	}
	
    
	/**
	 * ������ҳ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward redirectMain(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ȡ���ʸ�֤����
		String clientNo = getClientNo(request);
		List certNo = getCertNo(clientNo, null);
		// ��þ�����Ϣ
		List bidderAppList = this.tradeServer.getBidderAppListByCertNo(certNo);
		//��ȡ����ؿ���������ö��ŷָ�
		String appIds = this.getBidderAppIds(bidderAppList, null);
		//��ѯ����ؿ���Ϣ
		List guapaiBlockList = this.tradeServer.findGuapaiBlockByAppIds(appIds);
		//�ж��Ƿ���������ؿ�
		List yxList = this.tradeServer.fixMyYiXiangList(clientNo);
		String hasYx = "false";
		if(yxList!=null&&yxList.size()>0){
			hasYx = "true";
		}
		//�ж��Ƿ������깺�ĵؿ�δ��ȡ����
		List list = subscribeService.getMySubscribeList(clientNo);
		String hasHaoPai = "false";
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				TdscBidderApp bidderApp = (TdscBidderApp)list.get(i);
				if("1".equals(bidderApp.getIfCommit()) && StringUtils.isEmpty(bidderApp.getConNum())){
					hasHaoPai = "true";
					break;
				}
			}
		}
		//���÷�����Ϣ
		request.setAttribute("hasHaoPai", hasHaoPai);
		request.setAttribute("guapaiBlockList", guapaiBlockList);
		request.setAttribute("hasYx", hasYx);
		return mapping.findForward("main");
	}
	
	public ActionForward toSelectGp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ѯ���ڹ��ƵĹ���
		String userId = getClientNo(request); 
		List tradePlanTableList = tradeServer.findTradeNoticeNotStart();
		if(tradePlanTableList !=null && tradePlanTableList.size()>0){
			List planIdList = new ArrayList();
			for (int i = 0; i < tradePlanTableList.size(); i++) {
				TdscBlockPlanTable blockPlanTable = (TdscBlockPlanTable)tradePlanTableList.get(i);
				planIdList.add(blockPlanTable.getPlanId());
			}
			List appIds = new ArrayList();
			List myBidderAppId = tradeServer.getBidderAppListByUserId(userId);
			for (int i = 0; myBidderAppId != null && i < myBidderAppId.size(); i++) {
				TdscBidderApp bidderApp = (TdscBidderApp)myBidderAppId.get(i);
				appIds.add(bidderApp.getAppId());
			}
			List myBidderNoticeList = tradeServer.queryMyTradeNoticeBlock(planIdList, appIds);
			if (myBidderNoticeList != null) {
				List noticeIds = new ArrayList();
				for (int i = 0 ; i < myBidderNoticeList.size();  ) {
					TdscTradeView tdscTradeView = (TdscTradeView)myBidderNoticeList.get(i);
					if (noticeIds.contains(tdscTradeView.getNoticeId())) {
						myBidderNoticeList.remove(i);
					} else {
						noticeIds.add(tdscTradeView.getNoticeId());
						i++;
					}
				}
				if (noticeIds.size() == 0) {
					return mapping.findForward("gupai_list");
				} else if (noticeIds.size() == 1) {
					String noticeId = (String)noticeIds.get(0);
					return new ActionForward("trade.do?method=toGuapai&noticeId=" + noticeId, true);
				} else { 
					request.setAttribute("myBidderNoticeList", myBidderNoticeList);
				}
			} else {
				return mapping.findForward("gupai_list");
			}
		}
		return mapping.findForward("selectGp");
	}
	/**
	 * ���������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toGuapai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//��ѯ���ڹ��ƵĹ���
		List tradePlanTableList = tradeServer.findTradeNoticeNotStart();
		String noticeId = request.getParameter("noticeId");
		if(tradePlanTableList !=null && tradePlanTableList.size()>0){
			// ȡ�ø��û����ʸ�֤����
			String userId = getClientNo(request);
			
			List certNoList = getCertNo(userId, noticeId);
			// ��þ�����Ϣ
			List bidderAppList = this.tradeServer.getBidderAppListByUserId(userId);
			//��ȡ����ؿ���������ö��ŷָ�
			String appIds = this.getBidderAppIds(bidderAppList, noticeId);
			//��ѯ����ؿ���Ϣ
			List guapaiBlockList = this.tradeServer.findGuapaiBlockByAppIds(appIds);
			//��װMap--key��noticeApp ��value��tdscTradeView
			Map noticeBlockMap = this.genNoticeBlockMap(bidderAppList, guapaiBlockList);
			//��װMap--key��appId ��value����߼۸�
			Map myPriceMap = this.findMyGuapaiPrice(guapaiBlockList, certNoList);
			
			//���÷�����Ϣ
			request.setAttribute("noticeBlockMap", noticeBlockMap);
			request.setAttribute("myPriceMap", myPriceMap);
		}
		return mapping.findForward("gupai_list");
	}

	private List getCertNo(String userId, String noticeId) {
		List list = tradeServer.getCertNoList(userId, noticeId);
		return list;
	}

	/**
	 * �������list���ؿ��б�ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toGuapaiHall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		if(!filterParam(appId)){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("appId�������Ϸ���value="+appId);
			return mapping.findForward("tdscCaLogin");
		}
		String userId = getClientNo(request);
		TdscBidderView myBidder = tradeServer.getTdscBidderViewByAppId(appId, userId);
		// ȡ�ù�����Ϣ
		List lisingInfoList = this.tradeServer.getListingAppListByAppId(appId, "11");
		//��ȡ���������Ϣ
		TdscTradeView tdscTradeView = this.tradeServer.getTdscTradeViewAppById(appId);
		
		//���÷�����Ϣ
		request.setAttribute("myBidder", myBidder);
		request.setAttribute("lisingInfoList", lisingInfoList);
		request.setAttribute("tdscTradeView", tdscTradeView);
		return mapping.findForward("gupai_hall");
	}
	
	/**
	 * ����һ���˿����깺����������ؿ飬���Դ�������������ͬһ�׶ε�ʱ���ڽ��뾺��֮ǰ����ѡ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toSelectTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String userId = getClientNo(request);
		
		// ȡ���ҵľ�����Ϣ
		List bidderAppList = tradeServer.getBidderViewListByUserId(userId);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// �ֱ��ȡ�Ҿ���ĵؿ��б�
			List appIds = new ArrayList();
			for (int i = 0 ; i < bidderAppList.size(); i++) {
				TdscBidderView bidderView = (TdscBidderView) bidderAppList.get(i);
				appIds.add(bidderView.getAppId());
			}
			// �õ��ؿ����ϸ��Ϣ�б�
			List myTradeViewList = tradeServer.queryMyTradeNoticeBlock(null, appIds);
			int count = myTradeViewList.size();
			List containsNoticeId = new ArrayList();
			List resultList = new ArrayList();
			// ѭ���ҵ����ھ����еĵؿ��б�
			for (int i = 0 ; i < count; i++) {
				// ÿ���ؿ�ѭ�� �ж��Ƿ��ھ�����
				TdscTradeView tdscTradeView = (TdscTradeView)myTradeViewList.get(i);
				if (!containsNoticeId.contains(tdscTradeView.getNoticeId())){
					if (tdscTradeView.getOnLineStatDate() != null && tdscTradeView.getOnLineEndDate() == null) {
						// �����жϹ����б����ǲ����Ѿ�������������ˡ�������ڣ��Ͳ���Ҫ�ù����еĵؿ���Ϣ��
						// ��������б���û�У���ô�ж������ǰȡ�ù����Ƿ��ھ���״̬��������ھ���״̬,��ӽ���
						containsNoticeId.add(tdscTradeView.getNoticeId());
						resultList.add(tdscTradeView);
					} else if (tdscTradeView.getOnLineStatDate() == null && tdscTradeView.getOnLineEndDate() == null) {
						if ("11".equals(tdscTradeView.getTradeStatus()) || "22".equals(tdscTradeView.getTradeStatus()) || "33".equals(tdscTradeView.getTradeStatus())) {
							containsNoticeId.add(tdscTradeView.getNoticeId());
							resultList.add(tdscTradeView);
						} else if ("0".equals(tdscTradeView.getTradeStatus())) {
							if (BaseStore.inConfirmPool(tdscTradeView.getNoticeId())) {
								containsNoticeId.add(tdscTradeView.getNoticeId());
								resultList.add(tdscTradeView);
							}
						}
					}
				}
			}
			// ����ҹ���Ĺ������Ϊ0 ��ȴ�
			if (containsNoticeId.size() == 0) {
				//����޹�����ȴ�
				return mapping.findForward("trade_hall_fail");
			} else if (containsNoticeId.size() == 1) {
				// �������1 ȡ�øù���
				if (resultList == null || resultList.size() < 1) {
					return mapping.findForward("trade_hall_fail");
				}
				TdscTradeView tdscTradeView = (TdscTradeView)resultList.get(0);
				if(tdscTradeView.getOnLineEndDate() != null || tdscTradeView.getOnLineStatDate() == null){
					if (!"11".equals(tdscTradeView.getTradeStatus()) && !"22".equals(tdscTradeView.getTradeStatus()) && !"33".equals(tdscTradeView.getTradeStatus())) {
						if (!BaseStore.inConfirmPool(tdscTradeView.getNoticeId())) {
							return mapping.findForward("trade_hall_fail");
						}
					}
				}
				String noticeId = (String)containsNoticeId.get(0);
				//return new ActionForward("trade.do?method=toTradeHall&noticeId=" + noticeId, true);
				return new ActionForward("trade.do?method=toHall&noticeId=" + noticeId, true);
			} else if (containsNoticeId.size() > 1) {
				request.setAttribute("myTradeViewList", resultList);
				return mapping.findForward("selectTrade");
			} else {
				return mapping.findForward("trade_hall_fail");
			}
		}
		return mapping.findForward("trade_hall_fail");
	}

	/**
	 * ���״���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toTradeHall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �ҵ��ʸ�֤����
		//List certNoList = this.getCertNo(request);
		String userId = getClientNo(request);
		//�����Ϊ�գ�ͨ�������л��ķ�ʽ���ݹ��������Ϊ�գ�ֱ�ӵ�����״�������
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("noticeId�������Ϸ���value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		// ��þ�����Ϣ
		List bidderAppList = this.tradeServer.queryBidderAppListForme(userId, noticeId);
		//����鲻��������Ϣ��ֱ�ӷ��صȴ�ҳ��
		List myBidderAppListId = null;
		if(bidderAppList == null || bidderAppList.size() == 0){
			return mapping.findForward("trade_hall_fail");
		} else {
			myBidderAppListId = new ArrayList();
			for (int i = 0; i < bidderAppList.size(); i++) {
				TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(i);
				myBidderAppListId.add(bidderApp.getAppId());
			}
		}
		//�������
		TdscNoticeApp noticeApp = null;
		//��ǰ���ڽ��׵ĵؿ飬����ڽ��׼�Ъ�ڣ��˵ؿ�Ϊ������ǰ��ĵȴ��ؿ�
		TdscTradeView curTdscTradeView = null;
		//�ҵ��깺�ؿ��б�
		List myTradeBlockList = new ArrayList();
		//���׵ؿ��б�
		List tradeBlockList = new ArrayList();
		//������ϸ��Ϣ
		List lisingInfoList = null;
		//��ѯ�����µ����еؿ�
		List allBlockList = this.tradeServer.getTdscTradeViewAppByNoticeId(noticeId);
		if (allBlockList != null && allBlockList.size() > 0) {
			TdscTradeView tempTdscTradeView = (TdscTradeView) allBlockList.get(0);
			//������׽�������δ��ʼ��ֱ�ӷ��صȴ�ҳ��
			if(tempTdscTradeView.getOnLineEndDate() != null || tempTdscTradeView.getOnLineStatDate() == null){
				return mapping.findForward("trade_hall_fail");
			}else{
				//��ѯ��ǰ������Ϣ
				noticeApp = this.tradeServer.getBlockNoticeAppById(noticeId);
				//������ǰ��ĵȴ��ؿ�
				TdscTradeView firstWaitTradeView = null;
				for(int i=0;i<allBlockList.size();i++){
					TdscTradeView myTdscTradeView = (TdscTradeView) allBlockList.get(i);
					//���ڽ��׵ĵؿ�
					if("1".equals(myTdscTradeView.getTradeStatus())){
						curTdscTradeView = myTdscTradeView;
					}
					//����������ǰ��ĵȴ��ؿ�
					if(firstWaitTradeView == null && "2".equals(myTdscTradeView.getTradeStatus())){
						firstWaitTradeView = myTdscTradeView;
					}
					//�������깺�Ľ��׵ؿ��б�
					if(myBidderAppListId.contains(myTdscTradeView.getAppId())){
						myTradeBlockList.add(myTdscTradeView);
					}
					if("1".equals(myTdscTradeView.getTradeStatus()) || "2".equals(myTdscTradeView.getTradeStatus()) || "3".equals(myTdscTradeView.getTradeStatus())|| "5".equals(myTdscTradeView.getTradeStatus())){
						tradeBlockList.add(myTdscTradeView);
					}
				}
				//����ڽ��׼�Ъ�ڣ����õ�ǰ�ؿ�Ϊ��ǰ��ĵȴ��ؿ�
				if(curTdscTradeView == null){
					curTdscTradeView = firstWaitTradeView;
				}
				//��ȡ������ϸ��Ϣ�б�
				if(curTdscTradeView != null)
					lisingInfoList = this.tradeServer.getListingAppListByAppId(curTdscTradeView.getAppId(), "22");
			}
		}else{
			return mapping.findForward("trade_hall_fail");
		}
		
		//���÷��ص�ҳ����Ϣ
		request.setAttribute("tradeBlockList", tradeBlockList);
		request.setAttribute("myTradeBlocks", myTradeBlockList);
		request.setAttribute("myTradeAppIds", myBidderAppListId);
		request.setAttribute("curTdscTradeView", curTdscTradeView);
		request.setAttribute("noticeApp", noticeApp);
		request.setAttribute("lisingInfoList", lisingInfoList);
		
		return mapping.findForward("trade_hall");
	}


	/**
	 * ���뽻�״���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toHall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// �����жϹ����Ƿ����׼�ȷ�Ͻ׶�
		// 1.�����Ƿ��ֹ
		// 2.�Ƿ��еؿ��е׼۲��ҷ��ϵ׼�ȷ������
		
		// �ҵ��ʸ�֤����
		// List certNoList = this.getCertNo(request);
		String userId = getClientNo(request);
		// �����Ϊ�գ�ͨ�������л��ķ�ʽ���ݹ��������Ϊ�գ�ֱ�ӵ�����״�������
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("noticeId�������Ϸ���value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		
		List blockList = tradeServer.findConfirmBlockList(noticeId);
		if (blockList == null || blockList.size() == 0)
			return toTradeHall(mapping, form, request, response);

		request.setAttribute("blockList", blockList);
		
		List bidderAppList = this.tradeServer.queryBidderAppListForme(userId, noticeId);
		// ����鲻��������Ϣ��ֱ�ӷ��صȴ�ҳ��
		List myBidderAppListId = null;
		if(bidderAppList == null || bidderAppList.size() == 0){
			// ����ȴ�ҳ��
			return mapping.findForward("trade_hall_fail");
		} else {
			myBidderAppListId = new ArrayList();
			for (int i = 0; i < bidderAppList.size(); i++) {
				TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(i);
				myBidderAppListId.add(bidderApp.getAppId());
				
			}
		}
		
		// �������
		TdscNoticeApp noticeApp = null;
		// ��ǰ���ڽ��׵ĵؿ飬����ڽ��׼�Ъ�ڣ��˵ؿ�Ϊ������ǰ��ĵȴ��ؿ�
		TdscTradeView curTdscTradeView = null;
		// �ҵ��깺�ؿ��б�
		List myTradeBlockList = new ArrayList();
		//���׵ؿ��б�
		List tradeBlockList = new ArrayList();
		//������ϸ��Ϣ
		List lisingInfoList = null;
		if (blockList != null && blockList.size() > 0) {
			TdscTradeView tempTdscTradeView = (TdscTradeView) blockList.get(0);
			//����Ѿ���ʼ���ۻ��Ѿ���������
			if(tempTdscTradeView.getOnLineEndDate() != null || tempTdscTradeView.getOnLineStatDate() != null){
				return toTradeHall(mapping, form, request, response);
			}else{
				//��ѯ��ǰ������Ϣ
				noticeApp = this.tradeServer.getBlockNoticeAppById(noticeId);
				//������ǰ��ĵȴ��ؿ�
				TdscTradeView firstWaitTradeView = null;
				for(int i=0; i < blockList.size(); i++){
					TdscTradeView myTdscTradeView = (TdscTradeView) blockList.get(i);
					//���ڽ��׵ĵؿ�
					if("11".equals(myTdscTradeView.getTradeStatus())){
						curTdscTradeView = myTdscTradeView;
					}
					//����������ǰ��ĵȴ��ؿ�
					if(firstWaitTradeView == null && "22".equals(myTdscTradeView.getTradeStatus())){
						firstWaitTradeView = myTdscTradeView;
					}
					//�������깺�Ľ��׵ؿ��б�
					if(myBidderAppListId.contains(myTdscTradeView.getAppId())){
						myTradeBlockList.add(myTdscTradeView);
					}
					if("11".equals(myTdscTradeView.getTradeStatus()) || "22".equals(myTdscTradeView.getTradeStatus()) || "33".equals(myTdscTradeView.getTradeStatus())){
						tradeBlockList.add(myTdscTradeView);
					}
				}
				//����ڽ��׼�Ъ�ڣ����õ�ǰ�ؿ�Ϊ��ǰ��ĵȴ��ؿ�
				if(curTdscTradeView == null){
					curTdscTradeView = firstWaitTradeView;
				}
				//��ȡ������ϸ��Ϣ�б�
				if(curTdscTradeView != null)
					lisingInfoList = this.tradeServer.getListingAppListByAppId(curTdscTradeView.getAppId(), "11");
			}
		}else{
			return toTradeHall(mapping, form, request, response);
		}
		//�Ƿ�Ϊ������߼۾�����0��1��
		String isMaxPriceBidder = "0";
		for (int i = 0; bidderAppList!=null&&i < bidderAppList.size(); i++) {
			TdscBidderApp tempBidderApp = (TdscBidderApp)bidderAppList.get(i);
			if(curTdscTradeView.getAppId().equals(tempBidderApp.getAppId())){
				if(lisingInfoList!=null){
					TdscListingInfo listingInfo = (TdscListingInfo)lisingInfoList.get(0);
					if(tempBidderApp.getCertNo().equals(listingInfo.getListCert())){
						isMaxPriceBidder = "1";
					}
				}

			}
		}
		
		// ���÷��ص�ҳ����Ϣ
		request.setAttribute("isMaxPriceBidder", isMaxPriceBidder);
		request.setAttribute("tradeBlockList", tradeBlockList);
		request.setAttribute("myTradeBlocks", myTradeBlockList);
		request.setAttribute("myTradeAppIds", myBidderAppListId);
		request.setAttribute("curTdscTradeView", curTdscTradeView);
		request.setAttribute("noticeApp", noticeApp);
		request.setAttribute("lisingInfoList", lisingInfoList);
		return mapping.findForward("confirm_hall");
	}
	
	/**
	 * �����ҵĽ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toTradeResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("noticeId�������Ϸ���value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		// ȡ�øù���Ľ�����ʱ���۵ؿ��б�
		TdscNoticeApp noticeApp = this.tradeServer.getBlockNoticeAppById(noticeId);
		//tranapp��Ϣ
		List blockAppViewList = this.tradeServer.getBlockInfoByNoticeId(noticeId, false);
		
		List result = new ArrayList();
		for (int i = 0; i < blockAppViewList.size(); i++) {
			TdscBlockTranApp temp = (TdscBlockTranApp)blockAppViewList.get(i);
			if (!"04".equals(temp.getTranResult())) {
				TdscBlockInfo blockInfo = this.tradeServer.getBlockInfoByAppId(temp.getBlockId());
				if (!"00".equals(blockInfo.getStatus())) {
					result.add(temp);
				}
			}
		}
		//���÷�����Ϣ
		request.setAttribute("tranOverBlocks", result);
		//���÷�����Ϣ
		request.setAttribute("tranOverBlocks", blockAppViewList);
		request.setAttribute("noticeApp", noticeApp);
		return mapping.findForward("tradeResult");
	}
	

	/**
	 * ���õؿ鹫����ؿ��Ӧ��ϵ��Ϣ�б�
	 * @param bidderAppList ����������Ϣ
	 * @param guapaiBlockList �ؿ���Ϣ
	 * @return
	 */
	private Map genNoticeBlockMap(List bidderAppList,List guapaiBlockList){
		Map map = new HashMap();
		for (int i = 0; i < bidderAppList.size(); i++) {
			TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(i);
			List list = new ArrayList();
			TdscNoticeApp noticeApp = new TdscNoticeApp();
			noticeApp.setNoticeId(bidderApp.getNoticeId());
			for(int j=0;j<guapaiBlockList.size();j++){
				TdscTradeView tdscTradeView = (TdscTradeView)guapaiBlockList.get(j);
				if(tdscTradeView.getNoticeId().equals(bidderApp.getNoticeId())){
					noticeApp.setNoticeNo(tdscTradeView.getNoitceNo());
					noticeApp.setNoticeStatus(tdscTradeView.getIfOnLine());
					noticeApp.setStatusDate(tdscTradeView.getSceBidDate());
					list.add(tdscTradeView);
				}
			}
			if(list.size()>0){
				map.put(noticeApp, list);
			}
		}
		return map;
	}
	/**
	 * ��ȡ�ҵĹ�����߼۸�
	 * @param guapaiBlockList �ؿ��б�
	 * @param certNoList �ʸ�֤����
	 * @return
	 */
	private Map findMyGuapaiPrice(List guapaiBlockList,List certNoList){
		Map map = new HashMap();
		for(int j=0;guapaiBlockList!= null && j<guapaiBlockList.size();j++){
			TdscTradeView tdscTradeView = (TdscTradeView)guapaiBlockList.get(j);
			map.put(tdscTradeView.getAppId(), this.tradeServer.findMyGuapaiPrice(tdscTradeView.getAppId(),certNoList));
		}
		return map;
	}


	/**
	 * 
	 * ȡ���û����ʸ�֤���� 2011110025
	 * 
	 * @param request
	 * @return
	 */
	private List getCertNo(HttpServletRequest request) {
		String certNo = (String) request.getSession().getAttribute("certNo");
		List list = new ArrayList();
		list.add(certNo);
		return list;
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
	

    private String getBidderAppIds(List bidderAppList, String noticeId){
    	StringBuffer appIds = new StringBuffer();
    	if(bidderAppList != null ){
	    	for(int i=0;i<bidderAppList.size();i++){
	    		TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(i);
	    		if (StringUtils.isNotEmpty(noticeId)) {
	    			if (noticeId.equals(bidderApp.getNoticeId())) {
	    				appIds.append(bidderApp.getAppId()).append(",");
	    			}
	    		} else {
	    			appIds.append(bidderApp.getAppId()).append(",");
	    		}
	    	}
    	}
    	return appIds.toString();
    }

	/**
	 * ��ѯ���ڳ��õĹ����еؿ齻�׵����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryTradeBlockList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List certNoList = this.getCertNo(request);
		
		LinkedHashMap noticeAndBlockTranMap = new LinkedHashMap();
		LinkedHashMap noticeAndBlockListingMap = new LinkedHashMap();

		List planTableList = tradeServer.findPlanTableListInTrade();
		for (int i = 0; null != planTableList && i < planTableList.size(); i++) {
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) planTableList.get(i);
			List blockTranAppList = tradeServer.findBlockTranAppList(tdscBlockPlanTable.getPlanId());

			List appIdList = new ArrayList();
			LinkedHashMap blockTranMap = new LinkedHashMap();
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();

			for (int m = 0; null != blockTranAppList && m < blockTranAppList.size(); m++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) blockTranAppList.get(m);
				if (StringUtils.isBlank(tdscNoticeApp.getNoticeId())) {
					tdscNoticeApp = tradeServer.getBlockNoticeAppById(tdscBlockTranApp.getNoticeId());
				}
				if (!"04".equals(tdscBlockTranApp.getTranResult())) {
					appIdList.add(tdscBlockTranApp.getAppId());
					blockTranMap.put(tdscBlockTranApp.getAppId(), tdscBlockTranApp);
				}
			}
			Map blockListingMap = tradeServer.getBlockMaxListingPrice(appIdList, certNoList, null);

			noticeAndBlockTranMap.put(tdscNoticeApp, blockTranMap);
			noticeAndBlockListingMap.put(tdscNoticeApp, blockListingMap);
		}
		
		request.setAttribute("noticeAndBlockTranMap", noticeAndBlockTranMap);
		request.setAttribute("noticeAndBlockListingMap", noticeAndBlockListingMap);

		return mapping.findForward("trade_notice_list");
	}
}