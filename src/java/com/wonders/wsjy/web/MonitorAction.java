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
	 * 取得用户的资格证书编号 2011110025
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
	 * 进入监测观摩首页
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
		//如果不为空，通过命令切换的方式传递过来；如果为空，直接点击交易大厅进入
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//判断参数是否合法,不合法转到登陆页面
			System.out.println("noticeId参数不合法：value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		
		//公告对象
		TdscNoticeApp noticeApp = null;
		//当前正在交易的地块，如果在交易间歇期，此地块为排在最前面的等待地块
		TdscTradeView curTdscTradeView = null;
		//交易地块列表
		List tradeBlockList = new ArrayList();
		//报价详细信息
		List lisingInfoList = null;
		//查询公告下的所有地块
		List allBlockList = this.tradeServer.getTdscTradeViewAppByNoticeId(noticeId);
		
		if (allBlockList != null && allBlockList.size() > 0) {
			TdscTradeView tempTdscTradeView = (TdscTradeView) allBlockList.get(0);
			//如果交易结束或者未开始，直接返回等待页面
			if(tempTdscTradeView.getOnLineEndDate() != null || tempTdscTradeView.getOnLineStatDate() == null){
				// TODO:调转至首页
				return new ActionForward("", true);
			}else{
				//查询当前公告信息
				noticeApp = this.tradeServer.getBlockNoticeAppById(noticeId);
				//排在最前面的等待地块
				TdscTradeView firstWaitTradeView = null;
				for(int i=0;i<allBlockList.size();i++){
					TdscTradeView myTdscTradeView = (TdscTradeView) allBlockList.get(i);
					//正在交易的地块
					if("1".equals(myTdscTradeView.getTradeStatus())){
						curTdscTradeView = myTdscTradeView;
					}
					//设置排在最前面的等待地块
					if(firstWaitTradeView == null && "2".equals(myTdscTradeView.getTradeStatus())){
						firstWaitTradeView = myTdscTradeView;
					}
					if("1".equals(myTdscTradeView.getTradeStatus()) || "2".equals(myTdscTradeView.getTradeStatus()) || "3".equals(myTdscTradeView.getTradeStatus())){
						tradeBlockList.add(myTdscTradeView);
					}
				}
				//如果在交易间歇期，设置当前地块为最前面的等待地块
				if(curTdscTradeView == null){
					curTdscTradeView = firstWaitTradeView;
				}
				//获取报价详细信息列表
				lisingInfoList = this.tradeServer.getListingAppListByAppId(curTdscTradeView.getAppId(), "22");
			}
		}else{
			// TODO:调转至首页
			return new ActionForward("", true);
		}
		
		// 交易结果
		List tdscBlockList = this.tradeServer.queryBlockViewListTheEnd(noticeId);
		request.setAttribute("tdscBlockList", tdscBlockList);
		
		// 该地块所有竞买人
		List bidders = this.tradeServer.queryBidderAppListLikeAppId(curTdscTradeView.getAppId());
		request.setAttribute("bidders", bidders);
		
		
		//设置返回到页面信息
		request.setAttribute("tradeBlockList", tradeBlockList);
		request.setAttribute("curTdscTradeView", curTdscTradeView);
		request.setAttribute("noticeApp", noticeApp);
		request.setAttribute("lisingInfoList", lisingInfoList);
		
		return mapping.findForward("trade");
	}
	
	/**
	 * 查询已经发布的出让公告
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryTradePlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map noticeNoAndPlanMap = new LinkedHashMap();//存放公告号与plan的映射关系
		Map noticeNoAndBlockCountMap = new LinkedHashMap();//存放公告号与公告中地块数的映射关系
		Map noticeNoAndBidderCountMap = new LinkedHashMap();//存放公告号与公告中竞买人数的映射关系
		Map noticeNoAndNoticeIdMap = new LinkedHashMap();//存放公告号与公告ID的映射关系
		
		//查询网上交易的、当前日期在公告发布日期和挂牌截止日期之间的plan
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
	 * 查询公告的挂牌情况
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toViewGuaPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map blockAndListingAppMap = new LinkedHashMap();//存放出让地块ID与最高挂牌记录对象的映射关系
		Map blockAndListingCountMap = new LinkedHashMap();//存放出让地块ID与当前挂牌轮次的映射关系
		Map blockAndBidderCountMap = new LinkedHashMap();//存放出让地块ID与当前挂牌轮次的映射关系
		String noticeId = (String)request.getParameter("noticeId");
		
		List tdscTradeViewList = null;
		if(StringUtils.isNotBlank(noticeId)){
			tdscTradeViewList = tradeServer.getTdscTradeViewAppByNoticeId(noticeId);
		}
		if(tdscTradeViewList!=null && tdscTradeViewList.size()>0){
			for(int i=0; i<tdscTradeViewList.size(); ){
				TdscTradeView tdscTradeView = (TdscTradeView)tdscTradeViewList.get(i);
				if("04".equals(tdscTradeView.getTranResult())){//04表示交易终止
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
	 * 查询出让地块的挂牌情况
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
		String listType = (String)request.getParameter("listType");//报价记录类型，11为挂牌记录，22为竞价记录
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
	 * 进入我的交易
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toTradeResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//判断参数是否合法,不合法转到登陆页面
			System.out.println("noticeId参数不合法：value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		// 取得该公告的进入限时竞价地块列表
		TdscNoticeApp noticeApp = this.tradeServer.getBlockNoticeAppById(noticeId);
		//tranapp信息
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
		//设置返回信息
		request.setAttribute("tranOverBlocks", result);
		//设置返回信息
		request.setAttribute("noticeApp", noticeApp);
		return mapping.findForward("tradeResult");
	}
}