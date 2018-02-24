package com.wonders.wsjy.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.engine.CoreService;
import com.wonders.engine.socket.ClientPipe;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.service.TradeServer;

public class MonitorAction extends BaseAction {

	private TradeServer tradeServer;

	public TradeServer getTradeServer() {
		return tradeServer;
	}

	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
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
	 * �������Ħ��ҳ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward redirectMain(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List certNoList = getCertNo(request);
		String clientNo = (String) certNoList.get(0);
		request.setAttribute("clientNo", clientNo);
		return mapping.findForward("index");
	}
	
	/**
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward monitorTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//�����Ϊ�գ�ͨ�������л��ķ�ʽ���ݹ��������Ϊ�գ�ֱ�ӵ�����״�������
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("noticeId�������Ϸ���value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		
		//�������
		TdscNoticeApp noticeApp = null;
		//��ǰ���ڽ��׵ĵؿ飬����ڽ��׼�Ъ�ڣ��˵ؿ�Ϊ������ǰ��ĵȴ��ؿ�
		TdscTradeView curTdscTradeView = null;
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
				// TODO:��ת����ҳ
				return new ActionForward("", true);
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
					if("1".equals(myTdscTradeView.getTradeStatus()) || "2".equals(myTdscTradeView.getTradeStatus()) || "3".equals(myTdscTradeView.getTradeStatus())){
						tradeBlockList.add(myTdscTradeView);
					}
				}
				//����ڽ��׼�Ъ�ڣ����õ�ǰ�ؿ�Ϊ��ǰ��ĵȴ��ؿ�
				if(curTdscTradeView == null){
					curTdscTradeView = firstWaitTradeView;
				}
				//��ȡ������ϸ��Ϣ�б�
				lisingInfoList = this.tradeServer.getListingAppListByAppId(curTdscTradeView.getAppId(), "22");
			}
		}else{
			// TODO:��ת����ҳ
			return new ActionForward("", true);
		}
		
		// ���׽��
		List tdscBlockList = this.tradeServer.queryBlockViewListTheEnd(noticeId);
		request.setAttribute("tdscBlockList", tdscBlockList);
		
		// �õؿ����о�����
		List bidders = this.tradeServer.queryBidderAppListLikeAppId(curTdscTradeView.getAppId());
		request.setAttribute("bidders", bidders);
		
		
		//���÷��ص�ҳ����Ϣ
		request.setAttribute("tradeBlockList", tradeBlockList);
		request.setAttribute("curTdscTradeView", curTdscTradeView);
		request.setAttribute("noticeApp", noticeApp);
		request.setAttribute("lisingInfoList", lisingInfoList);
		
		return mapping.findForward("trade");
	}
	
	/**
	 * ��ѯ�Ѿ������ĳ��ù���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryTradePlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map noticeNoAndPlanMap = new LinkedHashMap();//��Ź������plan��ӳ���ϵ
		Map noticeNoAndBlockCountMap = new LinkedHashMap();//��Ź�����빫���еؿ�����ӳ���ϵ
		Map noticeNoAndBidderCountMap = new LinkedHashMap();//��Ź�����빫���о���������ӳ���ϵ
		Map noticeNoAndNoticeIdMap = new LinkedHashMap();//��Ź�����빫��ID��ӳ���ϵ
		
		//��ѯ���Ͻ��׵ġ���ǰ�����ڹ��淢�����ں͹��ƽ�ֹ����֮���plan
		List planTableList = tradeServer.findPlanTableListInPublicTrading();
		
		if(planTableList!=null && planTableList.size()>0){
			for(int i=0; i<planTableList.size(); i++){
				String noticeId = "";
				String noticeNo = "";
				TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable)planTableList.get(i);
				List tdscTradeViewList = tradeServer.getTdscTradeViewAppByPlanId(tdscBlockPlanTable.getPlanId());
				if(tdscTradeViewList!=null && tdscTradeViewList.size()>0){
					TdscTradeView tdscTradeView= (TdscTradeView)tdscTradeViewList.get(0);
					noticeId = tdscTradeView.getNoticeId();
					noticeNo = tdscTradeView.getNoitceNo();
					noticeNoAndPlanMap.put(noticeNo, tdscBlockPlanTable);
					noticeNoAndBlockCountMap.put(noticeNo, tdscTradeViewList.size()+"");
					noticeNoAndNoticeIdMap.put(noticeNo, noticeId);
				}
				
				List bidderAppList = tradeServer.findBidderAppListByNoticeId(noticeId);
				if(tdscTradeViewList!=null && tdscTradeViewList.size()>0){
					int size = (bidderAppList!=null)?bidderAppList.size():0;
					noticeNoAndBidderCountMap.put(noticeNo, size + "");
				}
			}			
		}
		
		request.setAttribute("noticeNoAndPlanMap", noticeNoAndPlanMap);
		request.setAttribute("noticeNoAndBlockCountMap", noticeNoAndBlockCountMap);
		request.setAttribute("noticeNoAndBidderCountMap", noticeNoAndBidderCountMap);
		request.setAttribute("noticeNoAndNoticeIdMap", noticeNoAndNoticeIdMap);
		List jmrClient = new ArrayList();
		for (Iterator it = CoreService.getClientPool().values().iterator();it.hasNext();) {
			ClientPipe clientPipe = (ClientPipe)it.next();
			if (StringUtils.trimToEmpty(clientPipe.getClientNo()).indexOf("JK") < 0) {
				jmrClient.add(clientPipe);
			}
		}
		int clientSize = jmrClient.size();
		request.setAttribute("clientPool", clientSize+"");
		
		return mapping.findForward("tradePlanList");
	}
	
	/**
	 * ��ѯ����Ĺ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toViewGuaPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map blockAndListingAppMap = new LinkedHashMap();//��ų��õؿ�ID����߹��Ƽ�¼�����ӳ���ϵ
		Map blockAndListingCountMap = new LinkedHashMap();//��ų��õؿ�ID�뵱ǰ�����ִε�ӳ���ϵ
		Map blockAndBidderCountMap = new LinkedHashMap();//��ų��õؿ�ID�뵱ǰ�����ִε�ӳ���ϵ
		String noticeId = (String)request.getParameter("noticeId");
		
		List tdscTradeViewList = null;
		if(StringUtils.isNotBlank(noticeId)){
			tdscTradeViewList = tradeServer.getTdscTradeViewAppByNoticeId(noticeId);
		}
		if(tdscTradeViewList!=null && tdscTradeViewList.size()>0){
			for(int i=0; i<tdscTradeViewList.size(); ){
				TdscTradeView tdscTradeView = (TdscTradeView)tdscTradeViewList.get(i);
				if("04".equals(tdscTradeView.getTranResult())){//04��ʾ������ֹ
					tdscTradeViewList.remove(i);
				}else{
					List listingAppList = tradeServer.findListingAppList(tdscTradeView.getAppId(), null, "11");
					List bidderAppList = tradeServer.queryBiddersByAppId(tdscTradeView.getAppId());
					
					if(listingAppList!=null && listingAppList.size()>0){
						TdscListingApp tdscListingApp = (TdscListingApp)listingAppList.get(0);
						blockAndListingAppMap.put(tdscTradeView.getAppId(), tdscListingApp);
						blockAndListingCountMap.put(tdscTradeView.getAppId(), listingAppList.size()+"");
					}
					
					if(bidderAppList!=null && bidderAppList.size()>0){
						blockAndBidderCountMap.put(tdscTradeView.getAppId(), bidderAppList.size()+"");
					}
					i++;
				}
			}
		}
		
		request.setAttribute("tdscTradeViewList", tdscTradeViewList);
		request.setAttribute("blockAndListingAppMap", blockAndListingAppMap);
		request.setAttribute("blockAndListingCountMap", blockAndListingCountMap);
		request.setAttribute("blockAndBidderCountMap", blockAndBidderCountMap);
		
		return mapping.findForward("guaPaiBlockList");		
	}
	
	/**
	 * ��ѯ���õؿ�Ĺ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toViewGuaPaiRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String appId = (String)request.getParameter("appId");
		String listType = (String)request.getParameter("listType");//���ۼ�¼���ͣ�11Ϊ���Ƽ�¼��22Ϊ���ۼ�¼
		if (StringUtils.isEmpty(listType)) {
			listType = "11";
		}
		
		TdscTradeView tdscTradeView = tradeServer.getTdscTradeViewAppById(appId);		
		List listingAppList = tradeServer.findListingAppList(appId, null, listType);
		
		request.setAttribute("tdscTradeView", tdscTradeView);
		request.setAttribute("listingAppList", listingAppList);
		request.setAttribute("listType", listType);
		
		return mapping.findForward("guaPaiRecordList");		
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
		request.setAttribute("noticeApp", noticeApp);
		return mapping.findForward("tradeResult");
	}
}