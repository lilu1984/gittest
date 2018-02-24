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
	 * 进入系统整体页面
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
	 * 进入首页
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward redirectMain(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 取得资格证书编号
		String clientNo = getClientNo(request);
		List certNo = getCertNo(clientNo, null);
		// 获得竞买信息
		List bidderAppList = this.tradeServer.getBidderAppListByCertNo(certNo);
		//获取竞买地块的主键，用逗号分隔
		String appIds = this.getBidderAppIds(bidderAppList, null);
		//查询购买地块信息
		List guapaiBlockList = this.tradeServer.findGuapaiBlockByAppIds(appIds);
		//判断是否有有意向地块
		List yxList = this.tradeServer.fixMyYiXiangList(clientNo);
		String hasYx = "false";
		if(yxList!=null&&yxList.size()>0){
			hasYx = "true";
		}
		//判断是否有已申购的地块未领取号牌
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
		//设置返回信息
		request.setAttribute("hasHaoPai", hasHaoPai);
		request.setAttribute("guapaiBlockList", guapaiBlockList);
		request.setAttribute("hasYx", hasYx);
		return mapping.findForward("main");
	}
	
	public ActionForward toSelectGp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 查询正在挂牌的公告
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
	 * 进入挂牌室
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toGuapai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//查询正在挂牌的公告
		List tradePlanTableList = tradeServer.findTradeNoticeNotStart();
		String noticeId = request.getParameter("noticeId");
		if(tradePlanTableList !=null && tradePlanTableList.size()>0){
			// 取得该用户的资格证书编号
			String userId = getClientNo(request);
			
			List certNoList = getCertNo(userId, noticeId);
			// 获得竞买信息
			List bidderAppList = this.tradeServer.getBidderAppListByUserId(userId);
			//获取竞买地块的主键，用逗号分隔
			String appIds = this.getBidderAppIds(bidderAppList, noticeId);
			//查询购买地块信息
			List guapaiBlockList = this.tradeServer.findGuapaiBlockByAppIds(appIds);
			//组装Map--key：noticeApp ；value：tdscTradeView
			Map noticeBlockMap = this.genNoticeBlockMap(bidderAppList, guapaiBlockList);
			//组装Map--key：appId ；value：最高价格
			Map myPriceMap = this.findMyGuapaiPrice(guapaiBlockList, certNoList);
			
			//设置返回信息
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
	 * 进入挂牌list即地块列表页面
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
		if(!filterParam(appId)){//判断参数是否合法,不合法转到登陆页面
			System.out.println("appId参数不合法，value="+appId);
			return mapping.findForward("tdscCaLogin");
		}
		String userId = getClientNo(request);
		TdscBidderView myBidder = tradeServer.getTdscBidderViewByAppId(appId, userId);
		// 取得挂牌信息
		List lisingInfoList = this.tradeServer.getListingAppListByAppId(appId, "11");
		//获取交易相关信息
		TdscTradeView tdscTradeView = this.tradeServer.getTdscTradeViewAppById(appId);
		
		//设置返回信息
		request.setAttribute("myBidder", myBidder);
		request.setAttribute("lisingInfoList", lisingInfoList);
		request.setAttribute("tdscTradeView", tdscTradeView);
		return mapping.findForward("gupai_hall");
	}
	
	/**
	 * 由于一个人可以申购多个公告多个地块，所以存在两个公告在同一阶段的时候，在进入竞价之前给予选择
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
		
		// 取得我的竞买信息
		List bidderAppList = tradeServer.getBidderViewListByUserId(userId);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			// 分别获取我竞买的地块列表
			List appIds = new ArrayList();
			for (int i = 0 ; i < bidderAppList.size(); i++) {
				TdscBidderView bidderView = (TdscBidderView) bidderAppList.get(i);
				appIds.add(bidderView.getAppId());
			}
			// 得到地块的详细信息列表
			List myTradeViewList = tradeServer.queryMyTradeNoticeBlock(null, appIds);
			int count = myTradeViewList.size();
			List containsNoticeId = new ArrayList();
			List resultList = new ArrayList();
			// 循环找到处于竞价中的地块列表
			for (int i = 0 ; i < count; i++) {
				// 每个地块循环 判断是否处于竞价中
				TdscTradeView tdscTradeView = (TdscTradeView)myTradeViewList.get(i);
				if (!containsNoticeId.contains(tdscTradeView.getNoticeId())){
					if (tdscTradeView.getOnLineStatDate() != null && tdscTradeView.getOnLineEndDate() == null) {
						// 首先判断公告列表中是不是已经存在这个公告了。如果存在，就不需要该公告中的地块信息了
						// 如果公告列表中没有，那么判断这个当前取得公告是否处于竞价状态，如果处于竞价状态,添加进来
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
			// 如果我购买的公告个数为0 则等待
			if (containsNoticeId.size() == 0) {
				//如果无公告则等待
				return mapping.findForward("trade_hall_fail");
			} else if (containsNoticeId.size() == 1) {
				// 如果等于1 取得该公告
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
	 * 交易大厅
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toTradeHall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 我的资格证书编号
		//List certNoList = this.getCertNo(request);
		String userId = getClientNo(request);
		//如果不为空，通过命令切换的方式传递过来；如果为空，直接点击交易大厅进入
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//判断参数是否合法,不合法转到登陆页面
			System.out.println("noticeId参数不合法：value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		// 获得竞买信息
		List bidderAppList = this.tradeServer.queryBidderAppListForme(userId, noticeId);
		//如果查不到竞买信息，直接返回等待页面
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
		//公告对象
		TdscNoticeApp noticeApp = null;
		//当前正在交易的地块，如果在交易间歇期，此地块为排在最前面的等待地块
		TdscTradeView curTdscTradeView = null;
		//我的申购地块列表
		List myTradeBlockList = new ArrayList();
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
				return mapping.findForward("trade_hall_fail");
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
					//设置我申购的交易地块列表
					if(myBidderAppListId.contains(myTdscTradeView.getAppId())){
						myTradeBlockList.add(myTdscTradeView);
					}
					if("1".equals(myTdscTradeView.getTradeStatus()) || "2".equals(myTdscTradeView.getTradeStatus()) || "3".equals(myTdscTradeView.getTradeStatus())|| "5".equals(myTdscTradeView.getTradeStatus())){
						tradeBlockList.add(myTdscTradeView);
					}
				}
				//如果在交易间歇期，设置当前地块为最前面的等待地块
				if(curTdscTradeView == null){
					curTdscTradeView = firstWaitTradeView;
				}
				//获取报价详细信息列表
				if(curTdscTradeView != null)
					lisingInfoList = this.tradeServer.getListingAppListByAppId(curTdscTradeView.getAppId(), "22");
			}
		}else{
			return mapping.findForward("trade_hall_fail");
		}
		
		//设置返回到页面信息
		request.setAttribute("tradeBlockList", tradeBlockList);
		request.setAttribute("myTradeBlocks", myTradeBlockList);
		request.setAttribute("myTradeAppIds", myBidderAppListId);
		request.setAttribute("curTdscTradeView", curTdscTradeView);
		request.setAttribute("noticeApp", noticeApp);
		request.setAttribute("lisingInfoList", lisingInfoList);
		
		return mapping.findForward("trade_hall");
	}


	/**
	 * 进入交易大厅
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toHall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 首先判断公告是否进入底价确认阶段
		// 1.挂牌是否截止
		// 2.是否有地块有底价并且符合底价确认条件
		
		// 我的资格证书编号
		// List certNoList = this.getCertNo(request);
		String userId = getClientNo(request);
		// 如果不为空，通过命令切换的方式传递过来；如果为空，直接点击交易大厅进入
		String noticeId = request.getParameter("noticeId");
		if(!filterParam(noticeId)){//判断参数是否合法,不合法转到登陆页面
			System.out.println("noticeId参数不合法：value="+noticeId);
			return mapping.findForward("tdscCaLogin");
		}
		
		List blockList = tradeServer.findConfirmBlockList(noticeId);
		if (blockList == null || blockList.size() == 0)
			return toTradeHall(mapping, form, request, response);

		request.setAttribute("blockList", blockList);
		
		List bidderAppList = this.tradeServer.queryBidderAppListForme(userId, noticeId);
		// 如果查不到竞买信息，直接返回等待页面
		List myBidderAppListId = null;
		if(bidderAppList == null || bidderAppList.size() == 0){
			// 进入等待页面
			return mapping.findForward("trade_hall_fail");
		} else {
			myBidderAppListId = new ArrayList();
			for (int i = 0; i < bidderAppList.size(); i++) {
				TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(i);
				myBidderAppListId.add(bidderApp.getAppId());
				
			}
		}
		
		// 公告对象
		TdscNoticeApp noticeApp = null;
		// 当前正在交易的地块，如果在交易间歇期，此地块为排在最前面的等待地块
		TdscTradeView curTdscTradeView = null;
		// 我的申购地块列表
		List myTradeBlockList = new ArrayList();
		//交易地块列表
		List tradeBlockList = new ArrayList();
		//报价详细信息
		List lisingInfoList = null;
		if (blockList != null && blockList.size() > 0) {
			TdscTradeView tempTdscTradeView = (TdscTradeView) blockList.get(0);
			//如果已经开始竞价或已经结束竞价
			if(tempTdscTradeView.getOnLineEndDate() != null || tempTdscTradeView.getOnLineStatDate() != null){
				return toTradeHall(mapping, form, request, response);
			}else{
				//查询当前公告信息
				noticeApp = this.tradeServer.getBlockNoticeAppById(noticeId);
				//排在最前面的等待地块
				TdscTradeView firstWaitTradeView = null;
				for(int i=0; i < blockList.size(); i++){
					TdscTradeView myTdscTradeView = (TdscTradeView) blockList.get(i);
					//正在交易的地块
					if("11".equals(myTdscTradeView.getTradeStatus())){
						curTdscTradeView = myTdscTradeView;
					}
					//设置排在最前面的等待地块
					if(firstWaitTradeView == null && "22".equals(myTdscTradeView.getTradeStatus())){
						firstWaitTradeView = myTdscTradeView;
					}
					//设置我申购的交易地块列表
					if(myBidderAppListId.contains(myTdscTradeView.getAppId())){
						myTradeBlockList.add(myTdscTradeView);
					}
					if("11".equals(myTdscTradeView.getTradeStatus()) || "22".equals(myTdscTradeView.getTradeStatus()) || "33".equals(myTdscTradeView.getTradeStatus())){
						tradeBlockList.add(myTdscTradeView);
					}
				}
				//如果在交易间歇期，设置当前地块为最前面的等待地块
				if(curTdscTradeView == null){
					curTdscTradeView = firstWaitTradeView;
				}
				//获取报价详细信息列表
				if(curTdscTradeView != null)
					lisingInfoList = this.tradeServer.getListingAppListByAppId(curTdscTradeView.getAppId(), "11");
			}
		}else{
			return toTradeHall(mapping, form, request, response);
		}
		//是否为挂牌最高价竞买人0否，1是
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
		
		// 设置返回到页面信息
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
		request.setAttribute("tranOverBlocks", blockAppViewList);
		request.setAttribute("noticeApp", noticeApp);
		return mapping.findForward("tradeResult");
	}
	

	/**
	 * 设置地块公告与地块对应关系信息列表
	 * @param bidderAppList 竞买申请信息
	 * @param guapaiBlockList 地块信息
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
	 * 获取我的挂牌最高价格
	 * @param guapaiBlockList 地块列表
	 * @param certNoList 资格证书编号
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
	 * 查询正在出让的公告中地块交易的情况
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