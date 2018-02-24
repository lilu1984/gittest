package com.wonders.tdsc.localtrade.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bidder.service.TdscBidderFundService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class ListingAction extends BaseAction {
	private TdscLocalTradeService	tdscLocalTradeService;

	private TdscBlockInfoService	tdscBlockInfoService;

	private CommonQueryService		commonQueryService;

	private TdscBidderFundService	tdscBidderFundService;

	private AppFlowService			appFlowService;

	private TdscBidderAppService	tdscBidderAppService;

	public void setTdscLocalTradeService(TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscBidderFundService(TdscBidderFundService tdscBidderFundService) {
		this.tdscBidderFundService = tdscBidderFundService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	/**
	 * 地块挂牌信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward viewZgbjListing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockType = request.getParameter("blockType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(StringUtil.GBKtoISO88591(request.getParameter("blockName")));
		condition.setBlockType(blockType);
		condition.setUser(user);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(request.getParameter("blockNoticeNo")));
		condition.setTransferMode(request.getParameter("transferMode"));
		condition.setCurrentPage(cPage);
		// 确定入口URL,换领号牌为"1",挂牌信息为"2",拍卖录入为"3",结果录入为"4"
		String enterWay = request.getParameter("enterWay");
		condition.setEnterWay(enterWay);

		List flowConditionList = new ArrayList();
		TdscFlowCondition node = new TdscFlowCondition();
		node.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node.setStatusId(FlowConstants.FLOW_STATUS_LISTING_CONFIRM);
		flowConditionList.add(node);
		condition.setFlowConditonList(flowConditionList);
		condition.setOrderKey("blockNoticeNo");
		condition.setOrderType("asc");
		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// 判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		condition.setTranResult("00");

		PageList tdscBlockAppViewPageList = null;
		if ("2".equals(enterWay)) {
			tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);
		} else {
			tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		}
		
		if (tdscBlockAppViewPageList != null && tdscBlockAppViewPageList.getList().size() > 0) {
			List tdscBlockAppViewList = tdscBlockAppViewPageList.getList();
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				if (tdscBlockAppView.getAppId() != null && !"".equals(tdscBlockAppView.getAppId())) {
					TdscListingInfo tdscListingInfo = tdscBlockInfoService.findBlistingInfo(tdscBlockAppView.getAppId());
					if (tdscListingInfo != null) {
						if (tdscListingInfo.getListCert() != null && !"".equals(tdscListingInfo.getListCert())) {
							tdscBlockAppView.setRangeEast(tdscListingInfo.getListCert());// 资格证书编号
						}
						if (tdscListingInfo.getCurrPrice() != null && !"".equals(tdscListingInfo.getCurrPrice())) {
							tdscBlockAppView.setAddPriceRange(tdscListingInfo.getCurrPrice());// 当前挂牌价格
						}
						if (tdscListingInfo.getListDate() != null && !"".equals(tdscListingInfo.getListDate())) {
							tdscBlockAppView.setAuctionDate(tdscListingInfo.getListDate());// 报价时间
						}
						if (tdscListingInfo.getCurrRound() != null && !"".equals(tdscListingInfo.getCurrRound())) {
							tdscBlockAppView.setOpeningMeetingNo(tdscListingInfo.getCurrRound());// 轮次
						}
					}
				}
			}
		}
		/*condition.setBlockName(StringUtil.GBKtoISO88591(request.getParameter("blockName")));
		condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(request.getParameter("blockNoticeNo")));*/
		request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
		request.setAttribute("condition", condition);
		
		return mapping.findForward("vZjbjList");
	}

	/**
	 * 地块挂牌信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockListForListing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockType = request.getParameter("blockType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(request.getParameter("blockName"));
		condition.setBlockType(blockType);
		condition.setUser(user);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setBlockNoticeNo(request.getParameter("blockNoticeNo"));
		condition.setTransferMode(request.getParameter("transferMode"));
		condition.setCurrentPage(cPage);
		// 确定入口URL,换领号牌为"1",挂牌信息为"2",拍卖录入为"3",结果录入为"4"
		String enterWay = request.getParameter("enterWay");
		condition.setEnterWay(enterWay);

		List flowConditionList = new ArrayList();
		TdscFlowCondition node = new TdscFlowCondition();
		node.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node.setStatusId(FlowConstants.FLOW_STATUS_LISTING_CONFIRM);
		flowConditionList.add(node);
		condition.setFlowConditonList(flowConditionList);
		condition.setOrderKey("blockNoticeNo");
		condition.setOrderType("asc");
		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// 判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		condition.setTranResult("00");

		PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		
		if (tdscBlockAppViewPageList != null && tdscBlockAppViewPageList.getList().size() > 0) {
			List tdscBlockAppViewList = tdscBlockAppViewPageList.getList();
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				if (tdscBlockAppView.getAppId() != null && !"".equals(tdscBlockAppView.getAppId())) {
					TdscListingInfo tdscListingInfo = tdscBlockInfoService.findBlistingInfo(tdscBlockAppView.getAppId());
					if (tdscListingInfo != null) {
						if (tdscListingInfo.getListCert() != null && !"".equals(tdscListingInfo.getListCert())) {
							tdscBlockAppView.setRangeEast(tdscListingInfo.getListCert());// 资格证书编号
						}
						if (tdscListingInfo.getCurrPrice() != null && !"".equals(tdscListingInfo.getCurrPrice())) {
							tdscBlockAppView.setAddPriceRange(tdscListingInfo.getCurrPrice());// 当前挂牌价格
						}
						if (tdscListingInfo.getListDate() != null && !"".equals(tdscListingInfo.getListDate())) {
							tdscBlockAppView.setAuctionDate(tdscListingInfo.getListDate());// 报价时间
						}
						if (tdscListingInfo.getCurrRound() != null && !"".equals(tdscListingInfo.getCurrRound())) {
							tdscBlockAppView.setOpeningMeetingNo(tdscListingInfo.getCurrRound());// 轮次
						}
					}
				}
				
				// 自动结束挂牌 add by xys
				//Long nowTime = new Long(DateConvertor.getCurrentDateWithTimeZone());
				//Long listEndTime = new Long(DateUtil.date2String(tdscBlockAppView.getListEndDate(), "yyyyMMddHHmmss"));				
				//当前时间已经到达挂牌截止时间，并且该地块是现场交易（ifOnLine=0）地块，则作自动结束挂牌推动流程的处理
				//if (nowTime.longValue() >= listEndTime.longValue() && "0".equals(tdscBlockAppView.getIfOnLine())) {
				//	System.out.println("AppId = "+tdscBlockAppView.getAppId()+" Auto End Listing......");
				//	autoEndListing(tdscBlockAppView.getAppId(), tdscBlockAppView.getTransferMode(), user);
				//}

			}
		}
		request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("queryBlockList");
	}

	private boolean autoEndListing(String appId, String transferMode, SysUser user) {
		// String appId = request.getParameter("appId");
		// SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// String transferMode = request.getParameter("transferMode");

		// String listingId = this.tdscLocalTradeService.queryListingId(appId);
		//
		// TdscAppFlow appFlow = new TdscAppFlow();
		// appFlow.setAppId(appId); // appId
		// appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_NO_SENCE); // 不要擅动（结果ID）
		// appFlow.setTransferMode(transferMode); // 出让方式
		// appFlow.setUser(user); // 用户信息
		//
		// TdscListingInfo tdscListingInfo = new TdscListingInfo();
		// if (listingId == null) {
		// tdscListingInfo.setAppId(appId);
		// this.tdscLocalTradeService.endListAndSaveOpnn(tdscListingInfo, appFlow);
		// } else {
		// try {
		// this.appFlowService.saveOpnn(appFlow);
		// } catch (Exception e) {
		// e.printStackTrace();
		// return false;
		// }
		// }
		// return true;

		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(appId);
		appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_SENCE_CHANGE);
		appFlow.setTransferMode(transferMode);
		appFlow.setUser(user);
		// 如果出让方式是挂牌的，加流
		try {
			this.appFlowService.saveOpnn(appFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 查看地块挂牌记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listingInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 地块信息查询
		String flag = request.getParameter("flag");
		request.setAttribute("flag", flag);
		String appId = request.getParameter("appId");
		String blockId = request.getParameter("blockId");

		int currentPage = 0;

		PageList pageList = new PageList();

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		// 挂牌记录查看
		// List listingInfoList = this.tdscLocalTradeService.queryListingRecord(appId);
		// request.setAttribute("listingInfoList", listingInfoList);

		// lz+ 20090708
		if (null != appId) {
			// 根据appId查listingId;
			String listingId = this.tdscLocalTradeService.queryListingId(appId);
			if (null != listingId) {
				List retList = new ArrayList();
				// 根据listingId查挂牌列表;
				retList = this.tdscLocalTradeService.queryTdscListingAppListByListingId(listingId);
				if (null != retList) {
					pageList.setList(retList);
				}

				int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();

				pageList = PageUtil.getPageList(retList, pageSize, currentPage);
				request.setAttribute("retList", retList);
				request.setAttribute("pageList", pageList);
			}

		}
		request.setAttribute("pageList", pageList);
		// lz end

		Map opnnMap = new HashMap();
		if (appId != null) {
			try {
				opnnMap = this.appFlowService.queryOpnnInfo(appId);
				request.setAttribute("opnninfo", opnnMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return mapping.findForward("toListingInfo");

	}

	/**
	 * 转入现场
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listingToScene(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO
		// 将信息保存进tdsc_listing_app_info表
		// 调用vendueAction中的方法,转到主持人录入结果
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("appId", appId);
		return mapping.findForward("toAddListingResult");

	}

	/**
	 * 挂牌结束
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listingToEnd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO
		// 将信息保存进tdsc_listing_app_info表
		// 修改TDSC_BLOCK_TRAN_APP
		// 返回到土地列表

		// 从页面接到一个业务ID,根据业务ID得到ListingId,存入对应挂牌结果
		String appId = request.getParameter("appId");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String transferMode = request.getParameter("transferMode");
		String listingId = this.tdscLocalTradeService.queryListingId(appId);

		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(appId); // appId
		appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_NO_SENCE); // 不要擅动（结果ID）
		appFlow.setTransferMode(transferMode); // 出让方式
		appFlow.setUser(user); // 用户信息

		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		if (listingId == null) {
			tdscListingInfo.setAppId(appId);
			this.tdscLocalTradeService.endListAndSaveOpnn(tdscListingInfo, appFlow);
		} else {
			try {
				this.appFlowService.saveOpnn(appFlow);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ActionForward("/tdsc/listing.do?method=queryBlockListForListing&enterWay=2");
	}

	public ActionForward endListing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String transferMode = tdscBlockAppView.getTransferMode();

		try {
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockAppView.getBlockId());
			tdscBlockInfo.setStatus("02");//(00-未交易;01-交易中;02-交易结束)
			tdscBlockInfo.setTranResult("01");//交易结果(01=交易成功|02=交易失败|03=交易取消|04=交易终止)
			tdscBlockInfoService.updateTdscBlockInfo(tdscBlockInfo);
			
			TdscBlockTranApp tdscBlockTranApp = tdscLocalTradeService.getTdscBlockTranApp(appId);			
			tdscBlockTranApp.setTranResult("01");// 交易结果  00未交易 01 交易成功；02 交易失败（流标）；04 终止交易；
			tdscLocalTradeService.updateTdscBlockTranApp(tdscBlockTranApp);
			
			this.appFlowService.saveOpnn(appId, transferMode, user);// 参数依次为appId、出让方式和用户信息
		} catch (Exception e) {
			e.printStackTrace();
		}

		// String forwardString = "listing.do?method=queryBlockListForListing&appId="+ appId + "&enterWay=3";
		String forwardString = "vendue.do?method=queryBlockListForResult&appId=" + appId + "&enterWay=3";

		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 生成成交按钮
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward displayPrintButton(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String displayStatus = request.getParameter("displayStatus");
		String displayOver = request.getParameter("displayOver");
		/* 成交价格 */
		String venduePrice = request.getParameter("venduePrice");
		String appId = request.getParameter("appId");
		String resultCert = request.getParameter("certNo");
		request.setAttribute("resultCert", resultCert);
		/* 按APPID查看视图 */
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		/* 按APPID查看挂牌会信息,并得到竞得人的资格证书编号 */
		TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		TdscBidderApp tdscBidderApp = null;
		if (null != tdscListingInfo) {
			/* 按竞买人资格证书编号查看竞得人信息,并得到成交人的资格证书编号 */
			List bidderPersonAppList = null;
			if (StringUtils.isNotEmpty(tdscListingInfo.getResultCert())) {
				tdscBidderApp = this.tdscBidderAppService.getBidderAppByCertNo(tdscListingInfo.getResultCert());
				if (null != tdscBidderApp) {
					bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());
					request.setAttribute("bidderPersonAppList", bidderPersonAppList);
					request.setAttribute("tdscBidderApp", tdscBidderApp);

				}
			}
		}

		// 竞买人信息

		// TdscListingInfo tdscListingInfo=tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("displayOver", displayOver);
		request.setAttribute("displayStatus", displayStatus);
		request.setAttribute("appId", appId);
		request.setAttribute("venduePrice", venduePrice);
		return mapping.findForward("displayPrintButton");
	}

	/**
	 * 保存未成交结果
	 */
	public ActionForward saveVenduefailedResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String vendueFailedReason = request.getParameter("vendueFailedReason");
		String vendueFailedMemo = request.getParameter("vendueFailedMemo");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("vendueFailedReason", vendueFailedReason);
		request.setAttribute("vendueFailedMemo", vendueFailedMemo);

		return mapping.findForward("saveVenduefailedResult");
	}

	/**
	 * 生成未成交按钮
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward displayPrintResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String display = request.getParameter("display");
		String vendueFailedReason = request.getParameter("vendueFailedReason");
		String vendueFailedMemo = request.getParameter("vendueFailedMemo");
		if (vendueFailedMemo.equals("null")) {
			vendueFailedMemo = "";
		}
		if ("1".equals(display)) {
			request.setAttribute("display", display);
			String appId = request.getParameter("appId");
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
			request.setAttribute("vendueFailedReason", vendueFailedReason);
			request.setAttribute("vendueFailedMemo", vendueFailedMemo);
			request.setAttribute("appId", appId);
		}

		return mapping.findForward("saveVendueFailedResult");
	}

	/**
	 * 打印密封报价单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printSceneQuotedPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// 查询挂牌会信息
		TdscListingInfo tdscListingInfo = null;
		tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		if (tdscListingInfo != null) {
			request.setAttribute("tdscListingInfo", tdscListingInfo);
		}
		request.setAttribute("certNo", certNo);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("printScenePrice");
	}

	/**
	 * 打印挂牌报价记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintListingRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// 查询挂牌历史纪录信息
		TdscListingInfo tdscListingInfo = null;
		List tdscListingAppList = null;
		tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		if (tdscListingInfo != null) {
			tdscListingAppList = this.tdscLocalTradeService.queryTdscListingAppListByListingId(tdscListingInfo.getListingId());
			if (null != tdscListingAppList && tdscListingAppList.size() > 0) {
				request.setAttribute("tdscListingAppList", tdscListingAppList);
			}
		}
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("printListingRecord");
	}

	/**
	 * 一翻一瞪眼
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward dengYan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String transferMode = request.getParameter("transferMode");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(appId); // appId
		appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_SENCE_PRINT); // 不要擅动（结果ID）
		appFlow.setTransferMode(transferMode); // 出让方式
		appFlow.setUser(user); // 用户信息
		try {
			this.appFlowService.saveOpnn(appFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String forwardString = "vendue.do?method=queryBlockListForProcess&enterWay=4&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 保存挂牌成功结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addListingResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// 业务Id
		String cardNo = request.getParameter("cardNo");// 竞价号牌或一卡通编号
		String venduePrice = request.getParameter("venduePrice");// 成交价格
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// 当前时间

		// 查询地块信息view
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// 如果成交,则用竞价号牌或资格证书编号和地块业务ID,确定唯一的一个竞买信息
		TdscBidderApp tdscBidderApp = null;
		if (cardNo != null && !"".equals(cardNo)) {
			// if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING_SENCE) && tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_CHANGE))
			// {
			// tdscBidderApp = this.tdscLocalTradeService.getTdscBidderAppByAppidCardNo(appId, cardNo);
			// } else {
			// tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfoByYktXh(cardNo);
			// }

			if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING_SENCE) && tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_CHANGE)) {
				tdscBidderApp = this.tdscBidderAppService.queryBidderAppListLikeAppIdAndCardNo(appId, cardNo);
			}
		}
		// 查询挂牌会信息
		TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		// 更改挂牌会信息
		if (null != tdscListingInfo && null != tdscBidderApp) {
			// 挂牌转入现场竞价,并举牌,node=17
			if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING_SENCE) && tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_CHANGE)) {
				tdscListingInfo.setResultCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscListingInfo.setListResult("01");// "01"挂牌结束，转入现场竞价
				tdscListingInfo.setListResultDate(nowTime);
				tdscListingInfo.setSceneResult("01");// "01"现场竞价成功
				tdscListingInfo.setSceneResultDate(nowTime);
				tdscListingInfo.setResultNo(cardNo);
				tdscListingInfo.setYktXh(tdscBidderApp.getYktBh());
			}
			// 一翻一瞪眼
			if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING_SENCE) && tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_NO_CHANGE)) {
				tdscListingInfo.setResultCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				// tdscListingInfo.setResultNo(cardNo);
				tdscListingInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscListingInfo.setYktXh(tdscBidderApp.getYktXh());
				tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_SCENE);// "01"挂牌结束，转入现场竞价
				tdscListingInfo.setListResultDate(nowTime);
				tdscListingInfo.setSceneResult(GlobalConstants.DIC_SCENE_SUCCESS);// "01"现场竞价成功
				tdscListingInfo.setSceneResultDate(nowTime);
				tdscListingInfo.setYktXh(tdscBidderApp.getYktBh());
			}
			// 挂牌没有转入现场,node=16
			if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING)) {
				tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_SUCCESS);// "02"挂牌成功
				tdscListingInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscListingInfo.setListResultDate(nowTime);
				tdscListingInfo.setResultCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
			}
			// this.tdscLocalTradeService.modifyListingInfo(tdscListingInfo);
		}

		// 更改地块交易表
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		// 计算总价并存入TdscBlockTranApp
		TdscBlockPart tdscBlockPart = new TdscBlockPart();
		List tdscBlockPartList = new ArrayList();
		BigDecimal totalPrice = new BigDecimal(venduePrice);// 出让价款成交总价

		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// 根据block_id查询TdscBlockPart
			BigDecimal totalArea = new BigDecimal(0.00);// 总面积
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockTranApp.getBlockId());
			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					// 若规划建筑面积不为空，则是非工业地块，出让价款成交总价为成交楼面地价与规划建筑面积的乘积；
					// 若规划建筑面积为空，则是工业地块，出让价款成交总价为成交楼面地价与土地面积的乘积；
					if (tdscBlockPart.getPlanBuildingArea() != null && tdscBlockPart.getPlanBuildingArea().compareTo(new BigDecimal("0.00")) == 1)
						totalArea = totalArea.add(tdscBlockPart.getPlanBuildingArea());
					else
						totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				}
			}
			totalPrice = totalArea.multiply(new BigDecimal(venduePrice));
		}

		String certNo = "";
		if (null != tdscBlockTranApp) {
			tdscBlockTranApp.setTotalPrice(totalPrice);
			tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"交易成功
			tdscBlockTranApp.setResultDate(nowTime);
			tdscBlockTranApp.setResultPrice(new BigDecimal(venduePrice));

			String resultName = "";
			if (null != tdscBidderApp) {
				certNo = tdscBidderApp.getCertNo();
				tdscBlockTranApp.setResultCert(certNo);

				List bidderPersonList = this.tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
				request.setAttribute("bidderPersonAppList", bidderPersonList);
				if (null != bidderPersonList && bidderPersonList.size() > 0) {
					for (int i = 0; i < bidderPersonList.size(); i++) {
						TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(i);
						resultName += tdscBidderPersonApp.getBidderName() + ",";
					}
				}
				if (!"".equals(resultName)) {
					resultName = resultName.substring(0, resultName.length() - 1);
					tdscBlockTranApp.setResultName(resultName);
				}
			}
			// 对TDSC_block_info表进行交易结果数据的纪录
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
				// 根据block_id查询TdscBlockInfo
				tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
				// 保存交易结果信息
				tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
				tdscBlockInfo.setResultDate(nowTime);
				tdscBlockInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscBlockInfo.setResultCert(certNo);
				tdscBlockInfo.setResultName(resultName);
				tdscBlockInfo.setStatus("02");
			}
			// 保存挂牌交易结果
			tdscBlockTranApp.setResultCert(cardNo);
			tdscBlockTranApp.setResultName(tdscBlockTranApp.getResultName());
			this.tdscBlockInfoService.updateListingTranResult(tdscBlockTranApp, tdscBlockInfo, tdscListingInfo);
			request.setAttribute("resultName", tdscBlockTranApp.getResultName());
		}
		request.setAttribute("venduePrice", venduePrice);
		request.setAttribute("appId", appId);
		request.setAttribute("resultDate", DateUtil.date2String(nowTime, DateUtil.FORMAT_DATETIME));
		TdscBlockAppView returnAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", returnAppView);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("resultCert", certNo);
		return mapping.findForward("addVendueResult");

	}

	/**
	 * 保存挂牌失败结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addListingFailedResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// 业务Id
		String vendueFailedReason = request.getParameter("vendueFailedReason");// 未成交原因
		String vendueFailedMemo = request.getParameter("vendueFailedMemo");// 未成交备注
		if (vendueFailedMemo == null) {
			vendueFailedMemo = "";
		}
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// 当前时间
		// 查询地块信息view
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// 对应挂牌会信息
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		if (listingId != null) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
		}
		// 将APP_ID设置给tdscListingInfo
		tdscListingInfo.setAppId(appId);

		// 挂牌转入现场竞价,node=17
		String nodeId = tdscBlockAppView.getNodeId();
		if (nodeId.equals(FlowConstants.FLOW_NODE_LISTING_SENCE)) {
			if (listingId != null) {
				tdscListingInfo.setListingId(listingId);
			}
			tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_SCENE);// "01"挂牌结束，转入现场竞价
			tdscListingInfo.setListResultDate(nowTime);
			tdscListingInfo.setSceneResult(GlobalConstants.DIC_SCENE_FAIL);// "02"现场竞价失败
			tdscListingInfo.setListFailReason(vendueFailedReason);
			tdscListingInfo.setListFailMeno(vendueFailedMemo);
			tdscListingInfo.setSceneResultDate(nowTime);

			// if(listingId != null){
			// this.tdscLocalTradeService.modifyListingInfo(tdscListingInfo);
			// }else{
			// this.tdscLocalTradeService.saveListingInfo(tdscListingInfo);
			// }

		}
		// 挂牌没有转入现场,node=16
		if (nodeId.equals(FlowConstants.FLOW_NODE_LISTING)) {
			tdscListingInfo.setListingId(listingId);
			tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_FAIL);// "03"挂牌失败
			tdscListingInfo.setListFailReason(vendueFailedReason);
			tdscListingInfo.setListFailMeno(vendueFailedMemo);
			tdscListingInfo.setListResultDate(nowTime);
			// this.tdscLocalTradeService.modifyListingInfo(tdscListingInfo);
		}
		// 更改地块交易表
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"交易失败
		tdscBlockTranApp.setResultDate(nowTime);
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// 根据block_id查询TdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// 保存交易结果信息
			tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
			tdscBlockInfo.setResultDate(nowTime);
			tdscBlockInfo.setStatus("02");
		}
		// 保存挂牌交易结果
		this.tdscBlockInfoService.updateListingTranResult(tdscBlockTranApp, tdscBlockInfo, tdscListingInfo);

		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", this.commonQueryService.getTdscBlockAppView(condition));
		request.setAttribute("vendueFailedReason", vendueFailedReason);
		request.setAttribute("vendueFailedMemo", vendueFailedMemo);
		if ("3104".equals(tdscBlockAppView.getTransferMode()))
			return mapping.findForward("tradeFailedGP");
		return mapping.findForward("saveVendueFailedResult");
	}
}
