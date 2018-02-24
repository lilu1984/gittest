package com.wonders.tdsc.localtrade.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.model.Pager;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.web.form.TdscDicBean;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscAuctionApp;
import com.wonders.tdsc.bo.TdscAuctionInfo;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscCompereInfo;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNotaryInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscTenderApp;
import com.wonders.tdsc.bo.TdscTenderInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.common.util.MoneyUtils;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.localtrade.web.form.LocalTradeForm;
import com.wonders.tdsc.randomselect.service.TdscSelectService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class VendueAction extends BaseAction {

	private CommonQueryService		commonQueryService;

	private TdscLocalTradeService	tdscLocalTradeService;

	private CommonFlowService		commonFlowService;

	private AppFlowService			appFlowService;

	private TdscBidderAppService	tdscBidderAppService;

	private TdscBlockInfoService	tdscBlockInfoService;

	private TdscSelectService		tdscSelectService;

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setTdscLocalTradeService(TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscSelectService(TdscSelectService tdscSelectService) {
		this.tdscSelectService = tdscSelectService;
	}

	private static final String	TAKE_VENDUE_CARD	= "1";

	private static final String	LISTING				= "2";

	private static final String	VENDUE_RESULT		= "3";

	private static final String	VENDUE_PROCESS		= "4";

	public ActionForward toTestFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// String appId = "3";
		// commonFlowService.initAppFlow(appId, "03");
		// appId = "4";
		// commonFlowService.initAppFlow(appId, "02");
		// List list = new ArrayList();
		// list.add(appId);
		// List appIdList = commonFlowService.queryAppList("01");
		//
		// return mapping.findForward("takeVendueCardsBidderList");
		return null;
	}

	/**
	 * 拍卖出让结束
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endVendue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String transferMode = request.getParameter("transferMode");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		try {			
			TdscBlockTranApp tdscBlockTranApp = tdscLocalTradeService.getTdscBlockTranApp(appId);			
			tdscBlockTranApp.setTranResult("02");// 交易结果  00未交易 01 交易成功；02 交易失败（流标）；04 终止交易；
			tdscLocalTradeService.updateTdscBlockTranApp(tdscBlockTranApp);
			
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			tdscBlockInfo.setStatus("02");//(00-未交易;01-交易中;02-交易结束)
			tdscBlockInfoService.updateTdscBlockInfo(tdscBlockInfo);
			
			this.appFlowService.saveOpnn(appId, transferMode, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String forwardString = "vendue.do?method=queryBlockListForResult&enterWay=3&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	public ActionForward toRefreshVendueResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("toRefreshVendueResult");
	}

	public ActionForward refreshBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BlockInfo blockInfo = BlockInfo.getInstance();

		StringBuffer result = new StringBuffer();
		result.append("[{blockNoticeNo:'").append(StringUtils.trimToEmpty(blockInfo.getBlockNoticeNo())).append("',blockName:'")
				.append(StringUtils.trimToEmpty(blockInfo.getBlockName())).append("',transferMode:'").append(StringUtils.trimToEmpty(blockInfo.getTransferMode()))
				.append("',sceBidLoc:'").append(StringUtils.trimToEmpty(blockInfo.getSceBidLoc())).append("'}]");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		out.print(result.toString());
		return null;
	}

	public ActionForward refreshResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BlockInfo blockInfo = BlockInfo.getInstance();
		String appId = blockInfo.getAppId();
		List list = this.tdscLocalTradeService.findAppListByAppIddesc(appId);
		StringBuffer result = new StringBuffer();
		result.append("[");
		BigDecimal max = null;
		for (int i = 0; null != list && i < list.size(); i++) {
			StringBuffer sb = new StringBuffer();
			TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) list.get(i);
			max = tdscAuctionApp.getTotalPrice();
			BigDecimal sub = new BigDecimal(0);
			if (i + 1 != list.size()) {
				TdscAuctionApp temp = (TdscAuctionApp) list.get(i + 1);
				BigDecimal value = temp.getTotalPrice();
				sub = max.subtract(value);
			} else {
				sub = new BigDecimal(0);
			}
			sb.append("{row:'").append(NumberUtil.numberDisplay(tdscAuctionApp.getAuctionSer(), 0)).append("',haopai:'").append(tdscAuctionApp.getHaoPai());
			sb.append("',addPrice:'").append(NumberUtil.numberDisplay(tdscAuctionApp.getAddPrice(), 2)).append("',totalPrice:'")
					.append(NumberUtil.numberDisplay(tdscAuctionApp.getTotalPrice(), 2));
			sb.append("',subValue:'").append(NumberUtil.numberDisplay(sub, 2));
			sb.append("'}");
			if (i + 1 != list.size())
				sb.append(",");
			result.append(sb.toString());
		}
		result.append("]");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		out.print(result.toString());
		return null;
	}

	/**
	 * 招标出让结束
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endInvite(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String transferMode = request.getParameter("transferMode");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		try {

			this.appFlowService.saveOpnn(appId, transferMode, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String forwardString = "vendue.do?method=queryBlockListForResult&enterWay=3&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 原以地块为单位查询“换领号牌”功能
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockListForVendue_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(request.getParameter("blockName"));
		condition.setBlockNoticeNo(request.getParameter("blockNoticeNo"));
		condition.setTransferMode(request.getParameter("transferMode"));
		// 确定入口URL,换领号牌为"1",挂牌信息为"2",拍卖录入为"3",结果录入为"4"
		condition.setEnterWay(request.getParameter("enterWay"));

		List flowConditionList = new ArrayList();
		TdscFlowCondition node1 = new TdscFlowCondition();
		node1.setNodeId(FlowConstants.FLOW_NODE_AUCTION);
		node1.setStatusId(FlowConstants.FLOW_STATUS_AUCTION_CHANGE);
		flowConditionList.add(node1);
		TdscFlowCondition node2 = new TdscFlowCondition();
		node2.setNodeId(FlowConstants.FLOW_NODE_LISTING_SENCE);
		node2.setStatusId(FlowConstants.FLOW_STATUS_LISTING_SENCE_CHANGE);
		flowConditionList.add(node2);
		condition.setFlowConditonList(flowConditionList);

		condition.setCurrentPage(cPage);
		condition.setUser(user);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("queryBlockList");
	}

	/**
	 * 以出让文件（公告）为单位查询“换领号牌”
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockListForVendue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}
		String blockName = request.getParameter("blockName");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		bindObject(condition, form);
		condition.setBlockName(StringUtil.GBKtoISO88591(blockName));
		condition.setBlockNoticeNo(request.getParameter("blockNoticeNo"));
		condition.setTransferMode(request.getParameter("transferMode"));
		List flowConditionList = new ArrayList();
		// 查询出让方式为“拍卖”的地块
		TdscFlowCondition node1 = new TdscFlowCondition();
		node1.setNodeId(FlowConstants.FLOW_NODE_AUCTION);
		node1.setStatusId(FlowConstants.FLOW_STATUS_AUCTION_CHANGE);
		flowConditionList.add(node1);
		// 查询出让方式为“挂牌”的地块
		TdscFlowCondition node2 = new TdscFlowCondition();
		node2.setNodeId(FlowConstants.FLOW_NODE_LISTING_SENCE);
		node2.setStatusId(FlowConstants.FLOW_STATUS_LISTING_SENCE_CHANGE);
		flowConditionList.add(node2);
		// 查询终止交易的地块
		TdscFlowCondition node3 = new TdscFlowCondition();
		node3.setNodeId(FlowConstants.FLOW_NODE_FINISH);
		node3.setStatusId(FlowConstants.FLOW_STATUS_END_TRADE);
		flowConditionList.add(node3);
		// 查询在挂牌信息管理模块中，结束挂牌操作时，结束交易不进行现场竞价（换领号牌）的地块
		TdscFlowCondition node4 = new TdscFlowCondition();
		node4.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node4.setStatusId(FlowConstants.FLOW_STATUS_LISTING_RESULT);
		flowConditionList.add(node4);
		// 设置查询条件LIST
		condition.setFlowConditonList(flowConditionList);

		condition.setCurrentPage(cPage);
		condition.setUser(user);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
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
		// 查询符合“换领号牌”条件的地块信息
		List tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewList(condition);
		List returnNoticeList = new ArrayList();
		if (tdscBlockAppViewPageList != null && tdscBlockAppViewPageList.size() > 0) {
			// 查询出让文件（公告）已经发布，但公告交易结果尚未发布的公告信息
			List noticeIdListInTrade = (List) tdscLocalTradeService.queryNoticeIdListInTrade();
			if (noticeIdListInTrade != null && noticeIdListInTrade.size() > 0) {
				for (int i = 0; i < noticeIdListInTrade.size(); i++) {
					String noticeId = (String) noticeIdListInTrade.get(i);
					// 公告中地块LIST
					List viewAppList = (List) tdscLocalTradeService.queryAppViewListByNoticeId(noticeId);
					// 判断是否一个公告中的所有地块都在“换领号牌”节点
					if (viewAppList != null && viewAppList.size() > 0) {
						int tempCount = 0;
						for (int k = 0; k < viewAppList.size(); k++) {
							// 判断每份公告所包含的地块是否都已经在“换领号牌”节点
							TdscBlockAppView app = (TdscBlockAppView) viewAppList.get(k);
							for (int j = 0; j < tdscBlockAppViewPageList.size(); j++) {
								TdscBlockAppView appView = (TdscBlockAppView) tdscBlockAppViewPageList.get(j);
								if (app.getAppId().equals(appView.getAppId())) {
									tempCount++;
								}
							}
						}
						// 如果每个地块都在“换领号牌”节点，则加入LIST
						if (tempCount == viewAppList.size()) {
							TdscNoticeAppCondition tdscNoticeAppCondition = new TdscNoticeAppCondition();
							tdscNoticeAppCondition.setNoticeId(StringUtils.trimToEmpty(noticeId));
							tdscNoticeAppCondition.setTradeNum(StringUtils.trimToEmpty(condition.getTradeNum()));
							tdscNoticeAppCondition.setNoticeNo(StringUtils.trimToEmpty(StringUtil.GBKtoISO88591(condition.getNoticeNo())));

							// TdscNoticeApp tdscNoticeApp = (TdscNoticeApp)tdscLocalTradeService.queryNoticeListByNoticeId(noticeId);
							// LZ+
							List tdscNoticeAppList = (List) tdscLocalTradeService.queryNoticeListByCondition(tdscNoticeAppCondition);
							TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
							if (tdscNoticeAppList != null && tdscNoticeAppList.size() > 0) {
								tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppList.get(0);
							}

							if (tdscNoticeApp.getNoticeId() != null && !"".equals(tdscNoticeApp.getNoticeId())) {
								TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
								conditionBlock.setNoticeId(tdscNoticeApp.getNoticeId());
								// 所有地块信息表
								List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
								String blockNameAll = "";
								if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
									for (int j = 0; j < tdscBlockAppViewList.size(); j++) {

										TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);

										blockNameAll += blockapp.getBlockName() + ",";
										tdscNoticeApp.setTranManTel(blockapp.getTradeNum());// 将"招拍挂编号"存在此字段中
										tdscNoticeApp.setTranManAddr(blockapp.getTransferMode());// 将"出让方式"存在此字段中
									}
									blockNameAll = blockNameAll.substring(0, blockNameAll.length() - 1);
									tdscNoticeApp.setTranManName(blockNameAll);// 将地块名称存在此字段中
								}
								returnNoticeList.add(tdscNoticeApp);
							}
							// LZ END
						}
					}
				}
			}
		}
		// 构造分页
		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		if (returnNoticeList != null && returnNoticeList.size() > 0)
			pageList = PageUtil.getPageList(returnNoticeList, pageSize, cPage);

		request.setAttribute("pageList", pageList);
		condition.setBlockName(blockName);
		request.setAttribute("condition", condition);
		return mapping.findForward("queryNoticeList");
	}

	public ActionForward queryBlockListForProcess_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String transferMode = request.getParameter("transferMode");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}
		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(request.getParameter("blockName"));
		condition.setBlockNoticeNo(request.getParameter("blockNoticeNo"));
		condition.setTransferMode(transferMode);
		condition.setUser(user);
		// 确定入口URL,换领号牌为"1",挂牌信息为"2",拍卖录入为"3",结果录入为"4"
		condition.setEnterWay(request.getParameter("enterWay"));
		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		List nodeList = new ArrayList();
		nodeList.add(FlowConstants.FLOW_NODE_FINISH);
		nodeList.add(FlowConstants.FLOW_NODE_RESULT_SHOW);
		condition.setNodeList(nodeList);
		PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		// List list = new ArrayList();
		// List tempList = tdscBlockAppViewPageList.getList();
		// if(null!=tempList && tempList.size()>0){
		// for(int i=0;i<tempList.size();i++){
		// TdscBlockAppView commonInfo = (TdscBlockAppView)tempList.get(i);
		// if(null!=commonInfo && !"9002".equals(commonInfo.getStatusId())){
		// list.add(commonInfo);
		// }
		// }
		// }
		// tdscBlockAppViewPageList.setList(list);
		// // 拼装分页信息
		// int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		// tdscBlockAppViewPageList = PageUtil.getPageList(list,pageSize,currentPage);
		request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("queryBlockList");
	}

	private boolean autoEndListing(SysUser user, List buttonList) {

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		// condition.setBlockName(request.getParameter("blockName"));
		// condition.setBlockType(blockType);
		condition.setUser(user);
		// 确定入口URL,换领号牌为"1",挂牌信息为"2",拍卖录入为"3",结果录入为"4"
		condition.setEnterWay("2");

		List flowConditionList = new ArrayList();
		TdscFlowCondition node = new TdscFlowCondition();
		node.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node.setStatusId(FlowConstants.FLOW_STATUS_LISTING_CONFIRM);
		flowConditionList.add(node);
		condition.setFlowConditonList(flowConditionList);
		condition.setOrderKey("blockNoticeNo");
		condition.setOrderType("desc");

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
		PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		if (tdscBlockAppViewPageList != null && tdscBlockAppViewPageList.getList().size() > 0) {
			List tdscBlockAppViewList = tdscBlockAppViewPageList.getList();
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {

				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				String appId = tdscBlockAppView.getAppId();

				Long nowTime = new Long(DateConvertor.getCurrentDateWithTimeZone());
				Long listEndTime = new Long(DateUtil.date2String(tdscBlockAppView.getListEndDate(), "yyyyMMddHHmmss"));

				if (nowTime.longValue() >= listEndTime.longValue()) {
					TdscAppFlow appFlow = new TdscAppFlow();
					appFlow.setAppId(appId);
					appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_SENCE_CHANGE);
					appFlow.setTransferMode(tdscBlockAppView.getTransferMode());
					appFlow.setUser(user);
					// 如果出让方式是挂牌的，加流
					try {
						this.appFlowService.saveOpnn(appFlow);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}

		return true;
	}

	/**
	 * 以公告为单位，交易过程管理，录入过程信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockListForProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 自动结束挂牌，目前注释掉
		//autoEndListing(user, buttonList);

		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}
		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(request.getParameter("blockName"));
		condition.setBlockNoticeNo(request.getParameter("blockNoticeNo"));
		condition.setTransferMode(request.getParameter("transferMode"));
		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
		// 确定入口URL,换领号牌为"1",挂牌信息为"2",拍卖录入为"3",结果录入为"4"
		condition.setEnterWay(request.getParameter("enterWay"));

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
		List flowConditionList = new ArrayList();
		TdscFlowCondition node1 = new TdscFlowCondition();
		node1.setNodeId(FlowConstants.FLOW_NODE_BID_OPENNING_APPROVAL);
		node1.setStatusId(FlowConstants.FLOW_STATUS_BID_OPENNING_WRITE);
		flowConditionList.add(node1);
		TdscFlowCondition node2 = new TdscFlowCondition();
		node2.setNodeId(FlowConstants.FLOW_NODE_AUCTION);
		node2.setStatusId(FlowConstants.FLOW_STATUS_AUCTION_RESULT);
		flowConditionList.add(node2);
		TdscFlowCondition node3 = new TdscFlowCondition();
		node3.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node3.setStatusId(FlowConstants.FLOW_STATUS_LISTING_RESULT);
		flowConditionList.add(node3);
		TdscFlowCondition node4 = new TdscFlowCondition();
		node4.setNodeId(FlowConstants.FLOW_NODE_LISTING_SENCE);
		node4.setStatusId(FlowConstants.FLOW_STATUS_LISTING_SENCE_RESULT);
		flowConditionList.add(node4);
		condition.setFlowConditonList(flowConditionList);
		condition.setOrderKey("blockNoticeNo");
		condition.setIfOnLine("0");//查询现场交易的地块（ifOnLine=0）
		
		//PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);

		List tempList = null;
		//if (tdscBlockAppViewPageList != null) {
			//tempList = tdscBlockAppViewPageList.getList();
			tempList = tdscBlockAppViewList;

			// 过滤不用进入现场录入的地块, add by xys, 2011-02-15
			tempList = filterBlock(tempList);

			if (null != tempList && tempList.size() > 0) {
				for (int i = 0; i < tempList.size(); i++) {
					TdscBlockAppView commonInfo = (TdscBlockAppView) tempList.get(i);

					if (!StringUtil.isEmpty(commonInfo.getAppId())) {
						TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(commonInfo.getAppId());
						if (tdscListingInfo != null) {
							commonInfo.setResultCert(tdscListingInfo.getListCert());
							commonInfo.setResultDate(tdscListingInfo.getListDate());
						}
					}
				}
			}
		//}
		
		// 构造分页
		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		
		pageList = PageUtil.getPageList(tempList, pageSize, cPage);
		
		request.setAttribute("tdscBlockAppViewPageList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("queryBlockList");
	}

	private List filterBlock(List blockList) {
		// （1）招标出让地块
		// （2）报名人数不足三家的拍卖出让地块
		// （3）有意向挂牌地块
		//		1.无人报名的，直接由意向人竞得，不进入现场竞价
		//		2.其他竞买人报名，但竞买人都没报价的，直接由意向人竞得，不进入现场竞价
		// （4）无意向挂牌地块
		//		1.无人报名的，不进入现场竞价
		//		2.仅一人报名，且该人报过价的，直接由该人竞得，不进入现场竞价
		//		3.仅一人报名，且该人没有报过价的，不进入现场竞价
		//		3.两人或两人以上报名，只要该地块无报价的，不进入现场竞价		
		if (blockList != null && blockList.size() > 0)
			for (int i = 0; i < blockList.size();) {
				TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);
				List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
				List listingAppList = tdscLocalTradeService.getListingAppListByAppId(app.getAppId(), null, "11");
				
				//有意向挂牌地块
				if ("1".equals(app.getIsPurposeBlock())){
					if (personList.size()==1) {//仅是意向人，无人报名
						blockList.remove(i);
						continue;
					}else if(personList.size()>1 && listingAppList.size()<2){//有竞买人报名，但仅是意向人的挂牌价，无竞买人报价
						blockList.remove(i);
						continue;
					}else{
						i++;
					}
				}
				//无意向挂牌地块
				if ("0".equals(app.getIsPurposeBlock())){
					if (personList.size()==0) {//无人报名
						blockList.remove(i);
						continue;
					}else if(personList.size()==1 && listingAppList.size()>=1){//仅一人报名，且该人有报价
						blockList.remove(i);
						continue;
					}else if(personList.size()==1 && listingAppList.size()==0){//仅一人报名，且该人没有报价
						blockList.remove(i);
						continue;
					}else if(personList.size()>1 && listingAppList.size()==0){//多人报名，但都无报价
						blockList.remove(i);
						continue;
					}else{
						i++;
					}
				}
			}
		return blockList;
	}

	/**
	 * 以公告为单位，交易过结果管理，录入结果信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockListForResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}
		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(request.getParameter("blockName"));
		condition.setBlockNoticeNo(request.getParameter("blockNoticeNo"));
		condition.setTransferMode(request.getParameter("transferMode"));
		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
		// 确定入口URL,换领号牌为"1",挂牌信息为"2",拍卖录入为"3",结果录入为"4"
		condition.setEnterWay(request.getParameter("enterWay"));
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
		List flowConditionList = new ArrayList();
		TdscFlowCondition node1 = new TdscFlowCondition();
		node1.setNodeId(FlowConstants.FLOW_NODE_BID_OPENNING_APPROVAL);
		node1.setStatusId(FlowConstants.FLOW_STATUS_BID_OPENNING_WRITE);
		flowConditionList.add(node1);
		TdscFlowCondition node2 = new TdscFlowCondition();
		node2.setNodeId(FlowConstants.FLOW_NODE_AUCTION);
		node2.setStatusId(FlowConstants.FLOW_STATUS_AUCTION_RESULT);
		flowConditionList.add(node2);
		TdscFlowCondition node3 = new TdscFlowCondition();
		node3.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node3.setStatusId(FlowConstants.FLOW_STATUS_LISTING_RESULT);
		flowConditionList.add(node3);
		TdscFlowCondition node4 = new TdscFlowCondition();
		node4.setNodeId(FlowConstants.FLOW_NODE_LISTING_SENCE);
		node4.setStatusId(FlowConstants.FLOW_STATUS_LISTING_SENCE_RESULT);
		flowConditionList.add(node4);
		condition.setFlowConditonList(flowConditionList);
		condition.setOrderKey("blockNoticeNo");
		condition.setIfOnLine("0");//只需要对现场交易（ifOnLine=0）的地块做交易结果管理;网上交易地块无需做交易结果管理，它会自动做出让结束
		// condition.setOrderType("desc");

		// 2011-03-21 需要把过滤掉的数据加入到list 并显示出来
		// 意向地块没有人竞买的地块
		PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		if (tdscBlockAppViewPageList != null) {
			List tempList = tdscBlockAppViewPageList.getList();
			if (null != tempList && tempList.size() > 0) {
				for (int i = 0; i < tempList.size(); i++) {
					TdscBlockAppView commonInfo = (TdscBlockAppView) tempList.get(i);
					if (null != commonInfo.getAppId() && !"".equals(commonInfo.getAppId())) {
						TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(commonInfo.getAppId());
						if (tdscListingInfo != null) {
							commonInfo.setResultCert(tdscListingInfo.getListCert());
							commonInfo.setResultDate(tdscListingInfo.getListDate());
						}
					}

					// // 意向地块没有人竞买的地块
					// //tempList.add();
					// if("1".equals(commonInfo.getIsPurposeBlock())){
					//
					// List tmpList = tdscBidderAppService.queryBidderAppListLikeAppId(commonInfo.getAppId());
					// if(tmpList!=null && tmpList.size()==1){
					// TdscBidderApp tdscBidderApp = (TdscBidderApp)tmpList.get(0);
					// if("1".equals(tdscBidderApp.getIsPurposePerson())){
					// commonInfo.setResultCert(resultCert);
					// commonInfo.setResultDate(tdscListingInfo.getListDate());
					// }
					// }
					// }

				}
			}

		}
		request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("queryBlockList");
	}

	/**
	 * 挂牌换领号牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward takeVendueCardsBidderList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String transferMode = request.getParameter("transferMode");
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
		// 通过竞买信息表获得参加拍卖会换领号牌的人数
		List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("appId", appId);

		// return mapping.findForward("takeVendueCardsBidderList");
		String forwardString = "listing.do?method=queryBlockListForListing&enterWay=2&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 换领号牌list页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward vendueCardList_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// SysUser user = (SysUser) request.getSession().getAttribute("user");
		// String transferMode = request.getParameter("transferMode");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		// 通过竞买信息表获得参加拍卖会换领号牌的人数
		List tdscBidderAppList = tdscLocalTradeService.queryBidderListSrc(appId);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("appId", appId);

		return mapping.findForward("qd_huanpaiList");

	}

	/**
	 * 以公告为单位，进行“换领号牌操作” 1交易卡对应多个地块
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward vendueCardList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");

		// 根据公告查询地块信息列表
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		// 页面地块公告号从小到大排序
		condition.setOrderKey("blockNoticeNo");
		// 查询条件结束
		List appViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

		// 通过地块信息表获得参加拍卖会换领号牌的人数（机审通过）
		// List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByNoticeId(noticeId);

		// 获得所有不同交易卡的信息列表start lz+
		List bidderAppList = (List) tdscBidderAppService.queryBidderAppListByNoticeId(noticeId);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			for (int i = 0; i < bidderAppList.size(); i++) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
				if (tdscBidderApp != null) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.tidyBidderByBidderId(tdscBidderApp.getBidderId());
					if (tdscBidderPersonApp != null)
						tdscBidderApp.setAcceptPeople(tdscBidderPersonApp.getMemo());
				}
			}
		}
		// 获得所有不同交易卡的信息列表end

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("appViewList", appViewList);
		request.setAttribute("tdscBidderAppList", bidderAppList);

		return mapping.findForward("qd_huanpaiList");
	}

	/**
	 * 打印竞买单位情况表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printJmrList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");

		// 根据noticeId获取所有地块appIdList和招牌挂编号
		String transferMode = "";
		String trade_num = "";
		String beiZhu = "";

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				if (tdscBlockAppView != null) {
					transferMode = tdscBlockAppView.getTransferMode();
					trade_num = tdscBlockAppView.getTradeNum();
					beiZhu += tdscBlockAppView.getXuHao() + "号地块";

					// 拼接备注,每个地块竞买人数
					List jmrList = tdscBidderAppService.queryJoinAuctionList(tdscBlockAppView.getAppId());
					if (jmrList != null) {
						beiZhu += " " + jmrList.size() + "家";
					} else {
						beiZhu += " 0家";
					}
					beiZhu += "，";
				}
			}
			if (beiZhu != null && beiZhu.length() > 0) {
				beiZhu = beiZhu.substring(0, beiZhu.length() - 1) + "。";
			}
		}

		List returnList = new ArrayList();

		if (noticeId != null && !"".equals(noticeId)) {
			// 根据noticeId取得所有yktbh不相同的竞买人信息列表
			// List tdscBidderAppList = tdscBidderAppService.findBidderListByAppIdList(appIdList);

			List tdscBidderAppList = (List) tdscBidderAppService.queryBidderAppListByNoticeId(noticeId);

			if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
				for (int n = 0; n < tdscBidderAppList.size(); n++) {
					TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(n);
					String unionBlockXuhao = "";
					String unionPersonkName = "";// 竞买投标人名称
					String unionCorpFr = "";// 法定代表人姓名
					String unionPersonTel = "";// 竞买投标人电话

					if (tdscBidderApp != null && tdscBidderApp.getConNum() != null) {
						// 根据竞买人bidderId取得竞买人名称
						List tdscBidderPersonAppList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
						if (tdscBidderPersonAppList != null && tdscBidderPersonAppList.size() > 0) {
							for (int k = 0; k < tdscBidderPersonAppList.size(); k++) {
								TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppList.get(k);
								if (tdscBidderPersonApp != null) {
									if (tdscBidderPersonApp.getBidderName() != null)
										unionPersonkName += tdscBidderPersonApp.getBidderName() + "、";
									if (tdscBidderPersonApp.getCorpFr() != null)
										unionCorpFr += tdscBidderPersonApp.getCorpFr() + "、";
									if (tdscBidderPersonApp.getBidderLxdh() != null)
										unionPersonTel += tdscBidderPersonApp.getBidderLxdh() + "、";
								}
							}
							if (unionPersonkName != null && unionPersonkName.length() > 0)
								unionPersonkName = unionPersonkName.substring(0, unionPersonkName.length() - 1);
							if (unionCorpFr != null && unionCorpFr.length() > 0)
								unionCorpFr = unionCorpFr.substring(0, unionCorpFr.length() - 1);
							if (unionPersonTel != null && unionPersonTel.length() > 0)
								unionPersonTel = unionPersonTel.substring(0, unionPersonTel.length() - 1);

							if ("2".equals(tdscBidderApp.getBidderType())) {
								unionPersonkName = unionPersonkName + " （联合竞买）";// 联合竞买
							}
						}

						// 根据交易卡卡号取得单个竞买人的所有竞买地块列表
						List bidderAppList = tdscBidderAppService.getBidderAppListByYktBh(tdscBidderApp.getYktBh());
						List appIdList2 = new ArrayList();
						if (bidderAppList != null && bidderAppList.size() > 0) {
							for (int i = 0; i < bidderAppList.size(); i++) {
								TdscBidderApp tdscBidderApp2 = (TdscBidderApp) bidderAppList.get(i);
								appIdList2.add(tdscBidderApp2.getAppId());
							}

							TdscBaseQueryCondition condition2 = new TdscBaseQueryCondition();
							condition2.setAppIdList(appIdList2);
							List tdscBlockAppViewList2 = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition2);
							if (tdscBlockAppViewList2 != null && tdscBlockAppViewList2.size() > 0) {
								for (int j = 0; j < tdscBlockAppViewList2.size(); j++) {
									TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList2.get(j);
									if (tdscBlockAppView != null) {
										unionBlockXuhao += tdscBlockAppView.getXuHao() + "、";
									}
								}
							}
							if (unionBlockXuhao != null && unionBlockXuhao.length() > 0)
								unionBlockXuhao = unionBlockXuhao.substring(0, unionBlockXuhao.length() - 1);
						}

						tdscBidderApp.setAcceptPeople(unionBlockXuhao);// 地块名称存入AcceptPeople
						tdscBidderApp.setAppUserId(unionPersonkName);// 竞买单位名称存入AppUserId
						tdscBidderApp.setZstrLxdh(unionPersonTel);// 将竞买人联系电话存入ZstrLxdh
						// 如果受托人名称为null，则读取法定代表人
						if (tdscBidderApp.getWtrName() == null || "".equals(tdscBidderApp.getWtrName()))
							tdscBidderApp.setWtrName(unionCorpFr);

						returnList.add(tdscBidderApp);
					}
				}
			}
		}

		request.setAttribute("transferMode", transferMode);
		request.setAttribute("beiZhu", beiZhu);
		request.setAttribute("trade_num", trade_num);
		request.setAttribute("tdscBidderAppList", returnList);

		return mapping.findForward("printJmrList");
	}

	/**
	 * 打印最后竞买单位信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printLastJmrInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");

		List returnList = new ArrayList();
		String listEndDate = "";
		String trade_num = "";
		String transferMode = "";

		if (noticeId != null && !"".equals(noticeId)) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);
			List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
					TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
					listEndDate = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getListEndDate(), "yyyy年MM月dd日HH时"));
					trade_num = tdscBlockAppView.getTradeNum();
					transferMode = tdscBlockAppView.getTransferMode();

					if (tdscBlockAppView != null) {
						TdscListingInfo tdscListingInfo = tdscBlockInfoService.findBlistingInfo(tdscBlockAppView.getAppId());
						if (tdscListingInfo != null) {
							tdscBlockAppView.setResultPrice(tdscListingInfo.getCurrPrice());// 最后报价

							TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.getBidderAppByAppIdYktXh(tdscListingInfo.getAppId(), tdscListingInfo.getYktXh());
							if (tdscBidderApp != null) {
								String peasonName = tdscBidderAppService.getPersonNameByBidderId(tdscBidderApp.getBidderId());
								tdscBlockAppView.setResultName(peasonName);// 最后竞买人
								tdscBlockAppView.setContractNum(tdscBidderApp.getConNum());// 号牌号
							}
						}
						String unionBlockCode = tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId());
						tdscBlockAppView.setUnitebBlockCode(unionBlockCode);// 子地块编号集合

						returnList.add(tdscBlockAppView);
					}
				}
			}
		}

		request.setAttribute("transferMode", transferMode);
		request.setAttribute("trade_num", trade_num);
		request.setAttribute("listEndDate", listEndDate);
		request.setAttribute("tdscBlockAppViewList", returnList);

		return mapping.findForward("printLastJmrInfo");
	}

	/**
	 * 以公告为单位，进行“换领号牌操作”
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward vendueCardList_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		// 根据公告查询地块信息列表
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		List appViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

		// 通过地块信息表获得参加拍卖会所有换领的号牌列表（机审通过）
		List certNolist = tdscLocalTradeService.queryCertNolistByNoticeId(noticeId);
		List resultList = new ArrayList();
		String flag = "false";

		if (certNolist != null && certNolist.size() > 0) {
			for (int i = 0; i < certNolist.size(); i++) {
				String certNo = (String) certNolist.get(i);// 号牌号
				String yktXhAll = "";// 一卡通卡号拼接

				// 取得该号牌号在公告中所有对应的竞买人信息
				if (certNo != null && !"".equals(certNo)) {
					List tdscBidderAppListForCertNo = tdscLocalTradeService.queryBidderAppListByCertNo(noticeId, certNo);

					// 取得该公告中公告号为certNo的所有交易卡卡号

					if (tdscBidderAppListForCertNo != null && tdscBidderAppListForCertNo.size() > 0) {
						for (int j = 0; j < tdscBidderAppListForCertNo.size(); j++) {
							TdscBidderApp tdscBidderAppForCertNo = (TdscBidderApp) tdscBidderAppListForCertNo.get(j);
							if (tdscBidderAppForCertNo.getYktXh() != null && !"".equals(tdscBidderAppForCertNo.getYktXh()))
								yktXhAll += tdscBidderAppForCertNo.getYktXh() + "、";
						}
						yktXhAll = yktXhAll.substring(0, yktXhAll.length() - 1);
					}
				}

				if (certNo != null && !"".equals(yktXhAll)) {
					TdscBidderApp tdscBidderApp = new TdscBidderApp();
					tdscBidderApp.setConNum(certNo);
					tdscBidderApp.setYktXh(yktXhAll);
					resultList.add(tdscBidderApp);
				}
			}
		}

		// 查询号牌号为空的列表
		List tdscBidderAppListWithNoCertNo = tdscLocalTradeService.queryBidderAppListWithNoCertNo(noticeId);

		request.setAttribute("tdscBidderAppListWithNoCertNo", tdscBidderAppListWithNoCertNo);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("appViewList", appViewList);
		request.setAttribute("tdscBidderAppList", resultList);

		return mapping.findForward("qd_huanpaiList");
	}

	/**
	 * 书面报价--进入身份验证页面（打印密封报价单）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// 通过竞买信息表获得参加拍卖会换领号牌的人数
		List tdscBidderAppList = tdscLocalTradeService.queryBidderListSrc(appId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("appId", appId);
		return mapping.findForward("qd_sfyzList");

	}

	/**
	 * 增加、修改竞买人的号牌
	 */
	public ActionForward toAddVendueCard_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");
		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		List tdscBidderAppList = tdscLocalTradeService.queryBidderListSrc(appId);
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("certNo", certNo);
		return mapping.findForward("toAddVendueCard");
	}

	/**
	 * 以公告为单位，增加竞买人号牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// 传入的是号牌号
		String noticeId = request.getParameter("noticeId");
		// 查询机审合格的竞买人信息列表
		List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByNoticeId(noticeId);

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("certNo", certNo);
		return mapping.findForward("toAddVendueCard");
	}

	/**
	 * 以公告为单位，群体更新竞买人号牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateVendueCards(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LocalTradeForm localTradeForm = (LocalTradeForm) form;

		String[] cardNos = localTradeForm.getCardNos();// 传入的是号牌号
		String[] certNos = localTradeForm.getCertNos();// 传入的是交易卡号

		String noticeId = request.getParameter("noticeId");

		// 查询机审合格的竞买人信息列表
		if (cardNos != null && cardNos.length > 0 && certNos != null && certNos.length > 0) {
			for (int j = 0; j < cardNos.length; j++) {
				String certNo = certNos[j];// 交易卡号
				String cardNo = cardNos[j];// 号牌号

				if (noticeId != null && !"".equals(noticeId) && certNo != null && !"".equals(certNo)) {
					List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByYktBh(noticeId, certNo);
					if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
						for (int i = 0; i < tdscBidderAppList.size(); i++) {
							TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
							tdscBidderApp.setConNum(cardNo);
							tdscBidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
							tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
						}
					}
				}
			}
		}

		request.setAttribute("noticeId", noticeId);
		String forwardString = "vendue.do?method=vendueCardList&noticeId=" + noticeId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 转到号牌更新页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toUpdateVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// 号牌号
		String yktXh = request.getParameter("yktXh");// 一卡通序号拼接
		String noticeId = request.getParameter("noticeId");
		// 查询机审合格的竞买人信息列表
		// List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByCertNo(noticeId, certNo);

		// request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("certNo", certNo);
		request.setAttribute("yktXh", yktXh);
		return mapping.findForward("updateVendueCard");
	}

	/**
	 * 以公告为单位，更新竞买人号牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// 原号牌号
		String cardNo = request.getParameter("cardNo");// 新号牌号
		String yktXh = request.getParameter("yktXh");// 一卡通序号拼接
		String noticeId = request.getParameter("noticeId");
		// 查询机审合格的竞买人信息列表
		List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByCertNo(noticeId, certNo);
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			for (int i = 0; i < tdscBidderAppList.size(); i++) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
				tdscBidderApp.setConNum(cardNo);
				tdscBidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
				tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
			}
		}

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("certNo", certNo);
		request.setAttribute("yktXh", yktXh);
		request.setAttribute("message", "保存成功！");

		return mapping.findForward("updateVendueCard");
	}

	/**
	 * 以公告为单位，删除竞买人号牌（置空）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// 号牌号
		String noticeId = request.getParameter("noticeId");
		// 查询机审合格的竞买人信息列表
		List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByCertNo(noticeId, certNo);
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			for (int i = 0; i < tdscBidderAppList.size(); i++) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
				tdscBidderApp.setConNum("");
				tdscBidderApp.setConTime(null);
				tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
			}
		}

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("certNo", certNo);

		String forwardString = "vendue.do?method=vendueCardList&noticeId=" + noticeId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 进入刷卡页面
	 */
	public ActionForward showLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);

		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		return mapping.findForward("toshowLogin");
	}

	/**
	 * 验证所刷交易卡是否符合条件
	 */
	public ActionForward checkYktNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		String yktNo = request.getParameter("yktNo");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String flag = "false";
		String conNum = "";
		// 验证该交易卡是否存在并已入围
		TdscBidderApp tdscBidderApp = tdscBidderAppService.getBidderAppByAppIdYktXh(appId, yktNo);
		if (tdscBidderApp != null && "1".equals(tdscBidderApp.getReviewResult())) {
			flag = "true";
			conNum = tdscBidderApp.getConNum();
		}
		String retString = flag + "，" + conNum;
		pw.write(retString);
		pw.close();
		return null;
	}

	/**
	 * 根据资格证书编号验证竞买人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");

		// 验证该交易卡是否存在并已入围
		TdscBidderApp tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfo(certNo);
		List tdscBidderPersonList = null;
		if (tdscBidderApp != null && "1".equals(tdscBidderApp.getReviewResult())) {
			tdscBidderPersonList = tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
			request.setAttribute("tdscBidderApp", tdscBidderApp);
			request.setAttribute("tdscBidderPersonList", tdscBidderPersonList);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("checkLogin");
	}

	public ActionForward endTakeVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardString = "vendue.do?method=queryBlockList&enterWay=3";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 增加一个号牌换领结果
	 */
	public ActionForward addVendueCard_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// 交易卡卡号或交易卡编号
		String certNo = request.getParameter("certNo");
		String cardNo = request.getParameter("cardNo");
		Map vendueCardMap = new HashMap();
		vendueCardMap.put("certNo", certNo);
		vendueCardMap.put("cardNo", cardNo);
		this.tdscLocalTradeService.saveTakeCardBidder(vendueCardMap);

		String forwardString = "vendue.do?method=vendueCardList&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 增加一个号牌换领结果
	 */
	public ActionForward addVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LocalTradeForm localTradeForm = (LocalTradeForm) form;

		// 交易卡卡号
		String[] certNos = localTradeForm.getCertNos();
		// 号牌号
		String cardNo = request.getParameter("cardNo");
		Map vendueCardMap = new HashMap();

		if (certNos != null && certNos.length > 0) {
			for (int i = 0; i < certNos.length; i++) {
				vendueCardMap.put("certNo", certNos[i]);
				vendueCardMap.put("cardNo", cardNo);

				this.tdscLocalTradeService.saveTakeCardBidder(vendueCardMap);
			}
		}

		request.setAttribute("message", "保存成功！");
		return mapping.findForward("toAddVendueCard");
		// String forwardString = "vendue.do?method=vendueCardList";
		// ActionForward f = new ActionForward(forwardString, true);
		// return f;
	}

	/**
	 * 主持人准备填写交易结果
	 */
	public ActionForward toAddVendueResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		// 地块信息view
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		String transferMode = tdscBlockAppView.getTransferMode();// 交易类型
		request.setAttribute("transferMode", transferMode);
		String resultStatus = tdscBlockAppView.getTranResult();// 交易结果状态
		// 挂牌
		if (transferMode.equals("3104")) {
			if (resultStatus != null) {
				// 取得最高的挂牌价格
				TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
				if (null != tdscListingInfo) {
					String maxPrice = tdscLocalTradeService.getMaxPrice(tdscListingInfo.getListingId());
					request.setAttribute("maxPrice", maxPrice);
				} else {
					tdscListingInfo = new TdscListingInfo();
				}
				// 交易成功
				if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS)) {
					// 取得竞得人信息
					TdscBlockTranApp tdscBlockTranApp = tdscBlockInfoService.getTdscBlockTranAppByAppId(appId);

					TdscBidderApp tdscBidderApp = tdscBidderAppService.getBidderAppByAppIdConNo(appId, tdscBlockTranApp.getResultCert());
					// tdscBidderApp = this.tdscBidderAppService.queryBidderAppListLikeAppIdAndCardNo(appId, cardNo);

					List bidderPersonAppList = null;
					if (null != tdscBidderApp) {
						bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());
					}
					// TdscListingInfo tdscListingInfo=tdscLocalTradeService.getTdscListingInfoByAppId(appId);
					request.setAttribute("bidderPersonAppList", bidderPersonAppList);
					request.setAttribute("tdscBidderApp", tdscBidderApp);
					request.setAttribute("appId", appId);
					request.setAttribute("tdscBlockAppView", tdscBlockAppView);
					if (null != tdscListingInfo.getResultPrice()) {
						request.setAttribute("venduePrice", tdscListingInfo.getResultPrice() + "");
					} else {
						request.setAttribute("venduePrice", "0");
					}

					if (null != tdscListingInfo.getResultCert()) {
						request.setAttribute("resultCert", tdscListingInfo.getResultCert());
					} else {
						request.setAttribute("resultCert", tdscBidderApp.getCertNo());
					}
					return mapping.findForward("detailListingResult");
					// 交易失败
				} else if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
					// 取得交易失败原因
					request.setAttribute("vendueFailedMemo", tdscListingInfo.getListFailMeno());
					request.setAttribute("vendueFailedReason", tdscListingInfo.getListFailReason());
					request.setAttribute("appId", appId);
					request.setAttribute("tdscBlockAppView", tdscBlockAppView);
					return mapping.findForward("tradeFailedGP");
					// 交易中
				} else if (!resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS) && !resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
					// 竞买人信息
					List tdscAuctionAppList = this.tdscLocalTradeService.findAppListByAppId(appId);
					if (tdscAuctionAppList != null && tdscAuctionAppList.size() > 0) {
						TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) tdscAuctionAppList.get(tdscAuctionAppList.size() - 1);
						request.setAttribute("tdscAuctionApp", tdscAuctionApp);
					} else {
						TdscAuctionApp tdscAuctionApp = new TdscAuctionApp();
						request.setAttribute("tdscAuctionApp", tdscAuctionApp);
					}
					List listingAppList = tdscLocalTradeService.getListingAppListByAppId(appId, null, "11");
					request.setAttribute("listingAppList", listingAppList);
					List tdscBidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appId);
					request.setAttribute("tdscBidderAppList", tdscBidderAppList);
					return mapping.findForward("toAddVendueResult");
				}

			}
			// 竞买人信息
			List tdscBidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appId);
			request.setAttribute("tdscBidderAppList", tdscBidderAppList);
			return mapping.findForward("toAddVendueResult");
		}
		// 拍卖
		if (transferMode.equals("3103")) {
			if (resultStatus != null) {
				TdscAuctionInfo tdscAuctionInfo = this.tdscLocalTradeService.getTdscAuctionInfoByAppId(appId);
				if (null != tdscAuctionInfo) {
					// 交易成功
					if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS)) {
						// 取得竞得人的信息
						String cardNo = tdscAuctionInfo.getResultNo();
						TdscBidderApp tdscBidderApp = null;
						if (cardNo != null) {
							tdscBidderApp = this.tdscLocalTradeService.getTdscBidderAppByAppidCardNo(appId, cardNo);
						}
						List bidderPersonAppList = null;
						if (tdscBidderApp != null) {
							bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());
						}

						request.setAttribute("venduePrice", tdscAuctionInfo.getResultPrice());
						request.setAttribute("bidderPersonAppList", bidderPersonAppList);
						request.setAttribute("tdscBidderApp", tdscBidderApp);
						request.setAttribute("appId", appId);
						request.setAttribute("tdscBlockAppView", tdscBlockAppView);
						request.setAttribute("resultCert", tdscAuctionInfo.getResultCert());
						return mapping.findForward("addVendueResult");
						// 交易失败
					} else if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
						// 取得交易失败原因
						request.setAttribute("vendueFailedReason", tdscAuctionInfo.getAuctionFailReason());
						request.setAttribute("vendueFailedMemo", tdscAuctionInfo.getAuctionFailMeno());
						request.setAttribute("appId", appId);
						request.setAttribute("tdscBlockAppView", tdscBlockAppView);
						request.setAttribute("resultCert", tdscAuctionInfo.getResultCert());
						return mapping.findForward("saveVendueResult");
						// 交易中
					} else if (!resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS) && !resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
						// 竞买人信息
						List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);
						request.setAttribute("tdscBidderAppList", tdscBidderAppList);
						// request.setAttribute("resultCert", tdscAuctionInfo.getResultCert());
						return mapping.findForward("toAddVendueResult");
					}
				}
			}
			List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);
			request.setAttribute("tdscBidderAppList", tdscBidderAppList);
			return mapping.findForward("toAddVendueResult");
		}
		// 招标
		if (transferMode != null && transferMode.equals("3107")) {
			if (resultStatus != null) {
				String tenderId = this.tdscLocalTradeService.queryTenderIdByAppId(appId);
				TdscTenderInfo tdscTenderInfo = null;
				if (null != tenderId && StringUtils.isNotEmpty(tenderId)) {
					tdscTenderInfo = this.tdscLocalTradeService.getTdscTenderInfo(tenderId);
				}
				if (null != tdscTenderInfo) {
					// 交易成功
					if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS)) {
						String tenderNo = tdscTenderInfo.getTenderNo();
						// 取得中标人信息
						List bidderPersonAppList = null;
						// String bidderId = tdscBidderApp.getBidderId();
						String bidderId = tdscBidderAppService.queryBidderId(appId, tenderNo);
						if (bidderId != null) {
							bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(bidderId);
						}
						// 取得最高价格
						String resultPrice = "";
						if (tdscTenderInfo.getResultPrice() != null) {
							resultPrice = tdscTenderInfo.getResultPrice().toString();
						}
						request.setAttribute("venduePrice", resultPrice);
						request.setAttribute("bidderPersonAppList", bidderPersonAppList);
						// request.setAttribute("tdscBidderApp", tdscBidderApp);
						request.setAttribute("appId", appId);
						request.setAttribute("tdscBlockAppView", tdscBlockAppView);
						request.setAttribute("resultNo", tdscTenderInfo.getTenderCertNo());
						return mapping.findForward("saveInviteSuccess");
						// 中标失败
					} else if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
						// 取得中标失败原因
						request.setAttribute("vendueFailedReason", tdscTenderInfo.getTenderFailedReason());
						request.setAttribute("vendueFailedMemo", tdscTenderInfo.getTenderFailedMemo());
						request.setAttribute("appId", appId);
						request.setAttribute("tdscBlockAppView", tdscBlockAppView);
						return mapping.findForward("saveInviteFailed");
						// 交易中
					} else if (!resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS) && !resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
						List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);
						request.setAttribute("tdscBidderAppList", tdscBidderAppList);
						return mapping.findForward("toAddVendueResult");
					}
				}
			}
			List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);
			request.setAttribute("tdscBidderAppList", tdscBidderAppList);
			return mapping.findForward("toAddVendueResult");
		}
		return null;
	}

	/**
	 * 保存拍卖成交结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addVendueResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		String appId = request.getParameter("appId");
		String venduePrice = request.getParameter("venduePrice");// 单价
		BigDecimal totalPrice = new BigDecimal(venduePrice);// 总价
		BigDecimal crjkPrice = new BigDecimal(0.00);// 出让价款

		// 地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// 获取所有参与拍卖的竞买信息列表
		// List tdscBidderAppList =
		// this.tdscLocalTradeService.queryTdscBidderAppList(appId);
		// 封装进拍卖信息表中
		TdscAuctionInfo tdscAuctionInfo = new TdscAuctionInfo();
		tdscAuctionInfo.setAppId(appId);
		tdscAuctionInfo.setAuctionDate(tdscBlockAppView.getAuctionDate());
		tdscAuctionInfo.setAutcionResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// 字典
		tdscAuctionInfo.setAutcionResultDate(new Date(System.currentTimeMillis()));
		List joinAuctionList = this.tdscLocalTradeService.queryJoinAuctionList(appId);// 获取参与拍卖的人的列表，即换号牌的人的列表
		tdscAuctionInfo.setJoinerNum(new BigDecimal(joinAuctionList.size()));
		List selectAppList = this.tdscSelectService.queryBlockSelectAppListByAppId(appId);
		String compere = "";
		String notary = "";
		if (selectAppList.size() > 0) {
			for (int i = 0; i < selectAppList.size(); i++) {
				TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) selectAppList.get(i);
				if (tdscBlockSelectApp.getSelectType().equals("06")) {
					String compereId = tdscBlockSelectApp.getSelectedId();
					if (compereId != null) {
						TdscCompereInfo tdscCompereInfo = this.tdscSelectService.queryCompereInfoByCompereId(compereId);
						compere = tdscCompereInfo.getCompereName();
					}
				}
				if (tdscBlockSelectApp.getSelectType().equals("04")) {
					String notaryId = tdscBlockSelectApp.getSelectedId();
					if (notaryId != null) {
						TdscNotaryInfo tdscNotaryInfo = this.tdscSelectService.queryNotaryInfoBynotaryId(notaryId);
						notary = tdscNotaryInfo.getNotaryName();
						break;
					}
				}
			}
		}
		tdscAuctionInfo.setAuctionModerator(compere);// 主持人
		tdscAuctionInfo.setAuctionNorar(notary);// 公证处
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		tdscBidderApp = this.tdscLocalTradeService.getTdscBidderAppByAppidCardNo(appId, cardNo);// 用号牌编号和地块业务ID,确定唯一的一个竞买信息
		tdscAuctionInfo.setResultCert(tdscBidderApp.getCertNo());
		tdscAuctionInfo.setResultNo(cardNo);
		tdscAuctionInfo.setResultPrice(new BigDecimal(venduePrice));
		List bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());// 将竞买信息中项目的竞买人公布
		// 整理竞得人名称
		String resultName = "";
		if (null != tdscBidderApp) {
			List bidderPersonList = this.tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
			if (null != bidderPersonList && bidderPersonList.size() > 0) {
				for (int i = 0; i < bidderPersonList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(i);
					resultName += tdscBidderPersonApp.getBidderName() + ",";
				}
			}
		}
		if (resultName != null && !"".equals(resultName)) {
			resultName = resultName.substring(0, resultName.length() - 1);
		}
		tdscAuctionInfo.setResultName(resultName);
		// this.tdscLocalTradeService.saveTdscAuctionInfo(tdscAuctionInfo);// 保存TDSC_AUCTION_INFO表
		// 更改地块交易表
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"交易成功
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		tdscBlockTranApp.setResultPrice(new BigDecimal(venduePrice));
		tdscBlockTranApp.setResultCert(tdscBidderApp.getCertNo());
		tdscBlockTranApp.setResultName(resultName);
		// 更新TdscBlockInfo表
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		TdscBlockPart tdscBlockPart = new TdscBlockPart();
		List tdscBlockPartList = new ArrayList();
		BigDecimal totalArea = new BigDecimal(0.00);// 总面积
		BigDecimal totalBlockArea = new BigDecimal(0.00);// 总土地面积
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// 根据block_id查询TdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockTranApp.getBlockId());
			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					totalBlockArea = totalBlockArea.add(tdscBlockPart.getBlockArea());

					if (tdscBlockPart.getPlanBuildingArea() != null && tdscBlockPart.getPlanBuildingArea().compareTo(new BigDecimal("0.00")) == 1)
						totalArea = totalArea.add(tdscBlockPart.getPlanBuildingArea());
					else
						totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				}
			}
		}
		// 若规划建筑面积不为空，出让价款成交总价为成交楼面地价与规划建筑面积的乘积；
		// 若规划建筑面积为空，出让价款成交总价为成交楼面地价与土地面积的乘积
		totalPrice = totalArea.multiply(new BigDecimal(venduePrice));

		crjkPrice = totalPrice.divide(totalBlockArea, 2, BigDecimal.ROUND_HALF_UP);

		tdscBlockTranApp.setTotalPrice(totalPrice);

		// 根据block_id查询TdscBlockInfo
		tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
		// 保存交易结果信息
		tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
		tdscBlockInfo.setResultDate(new Date(System.currentTimeMillis()));
		tdscBlockInfo.setResultPrice(new BigDecimal(venduePrice));
		tdscBlockInfo.setResultCert(tdscBidderApp.getCertNo());
		tdscBlockInfo.setResultName(resultName);
		tdscBlockInfo.setStatus("02");

		// this.tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
		// 保存拍卖成交结果
		this.tdscLocalTradeService.updateVendueTranResult(tdscBlockTranApp, tdscBlockInfo, tdscAuctionInfo);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("venduePrice", venduePrice);
		request.setAttribute("totalPrice", totalPrice);
		request.setAttribute("crjkPrice", crjkPrice);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", this.commonQueryService.getTdscBlockAppView(condition));
		request.setAttribute("bidderPersonAppList", bidderPersonAppList);
		request.setAttribute("resultCert", tdscBidderApp.getCertNo());
		return mapping.findForward("addVendueResult");
	}

	/**
	 * 保存招标成交结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addTenderResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		String appId = request.getParameter("appId");
		String resultPrice = request.getParameter("venduePrice");
		// 地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.getBidderAppByYktBh(cardNo);

		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo.setAppId(appId);
		tdscTenderInfo.setTenderNo(tdscBidderApp.getYktXh());
		tdscTenderInfo.setTenderCertNo(tdscBidderApp.getYktXh());
		tdscTenderInfo.setResultPrice(new BigDecimal(resultPrice));
		tdscTenderInfo.setTenderResultTime(new Timestamp(System.currentTimeMillis()));

		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);

		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"交易成功
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		tdscBlockTranApp.setResultPrice(new BigDecimal(resultPrice));
		tdscBlockTranApp.setResultCert(tdscBidderApp.getCertNo());
		if (null != tdscBidderApp) {
			List bidderPersonList = this.tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
			request.setAttribute("bidderPersonAppList", bidderPersonList);
			String resultName = "";
			if (null != bidderPersonList && bidderPersonList.size() > 0) {
				for (int i = 0; i < bidderPersonList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(i);
					resultName += tdscBidderPersonApp.getBidderName() + ",";
				}
			}
			if (!"".equals(resultName)) {
				resultName = resultName.substring(0, resultName.length() - 1);
			}
			tdscBlockTranApp.setResultName(resultName);
			// 更新TdscBlockInfo表
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
				// 根据block_id查询TdscBlockInfo
				tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
				// 保存交易结果信息
				tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
				tdscBlockInfo.setResultDate(new Timestamp(System.currentTimeMillis()));
				tdscBlockInfo.setResultPrice(new BigDecimal(resultPrice));
				tdscBlockInfo.setResultCert(tdscBidderApp.getCertNo());
				tdscBlockInfo.setResultName(resultName);
				tdscBlockInfo.setStatus("02");
			}

			this.tdscLocalTradeService.updateTenderResult(tdscTenderInfo, tdscBlockTranApp, tdscBlockInfo);
			request.setAttribute("resultName", tdscBlockTranApp.getResultName());
		}
		TdscBlockAppView returnTdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("resultNo", tdscTenderInfo.getTenderCertNo());
		request.setAttribute("venduePrice", resultPrice);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", returnTdscBlockAppView);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("resultCert", tdscBidderApp.getCertNo());
		return mapping.findForward("addVendueResult");

	}

	/**
	 * 保存未成交结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveVendueResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String vendueFailedReason = request.getParameter("vendueFailedReason");
		String vendueFailedMemo = request.getParameter("vendueFailedMemo");
		if (vendueFailedMemo.equals("")) {
			vendueFailedMemo = "";
		}
		// 地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// 封装进拍卖信息表中
		TdscAuctionInfo tdscAuctionInfo = new TdscAuctionInfo();
		tdscAuctionInfo.setAppId(appId);
		tdscAuctionInfo.setAuctionDate(tdscBlockAppView.getAuctionDate());
		tdscAuctionInfo.setAutcionResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// 字典
		tdscAuctionInfo.setAutcionResultDate(new Date(System.currentTimeMillis()));
		List joinAuctionList = this.tdscLocalTradeService.queryJoinAuctionList(appId);// 获取参与拍卖的人的列表，即换号牌的人的列表
		tdscAuctionInfo.setJoinerNum(new BigDecimal(joinAuctionList.size()));
		List selectAppList = this.tdscSelectService.queryBlockSelectAppListByAppId(appId);
		String compere = "";
		String notary = "";
		if (selectAppList.size() > 0) {
			for (int i = 0; i < selectAppList.size(); i++) {
				TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) selectAppList.get(i);
				if (tdscBlockSelectApp.getSelectType().equals("06")) {
					String compereId = tdscBlockSelectApp.getSelectedId();
					if (compereId != null) {
						TdscCompereInfo tdscCompereInfo = this.tdscSelectService.queryCompereInfoByCompereId(compereId);
						compere = tdscCompereInfo.getCompereName();
					}
				}
				if (tdscBlockSelectApp.getSelectType().equals("04")) {
					String notaryId = tdscBlockSelectApp.getSelectedId();
					if (notaryId != null) {
						TdscNotaryInfo tdscNotaryInfo = this.tdscSelectService.queryNotaryInfoBynotaryId(notaryId);
						notary = tdscNotaryInfo.getNotaryName();
						break;
					}
				}
			}
		}
		tdscAuctionInfo.setAuctionModerator(compere);// 主持人
		tdscAuctionInfo.setAuctionNorar(notary);// 公证处
		tdscAuctionInfo.setAuctionFailReason(vendueFailedReason);
		tdscAuctionInfo.setAuctionFailMeno(vendueFailedMemo);

		// 更改地块交易表
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"交易失败
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		// 对TDSC_block_info表进行交易结果数据的纪录
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// 根据block_id查询TdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// 保存交易结果信息
			tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
			tdscBlockInfo.setResultDate(new Date(System.currentTimeMillis()));
			tdscBlockInfo.setStatus("02");

		}
		// 保存拍卖交易结果
		this.tdscLocalTradeService.updateVendueTranResult(tdscBlockTranApp, tdscBlockInfo, tdscAuctionInfo);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", this.commonQueryService.getTdscBlockAppView(condition));
		request.setAttribute("vendueFailedReason", vendueFailedReason);
		request.setAttribute("vendueFailedMemo", vendueFailedMemo);

		return mapping.findForward("saveVendueResult");
	}

	/**
	 * 显示拍卖或者挂牌现场竞价录入记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddTradeProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		BlockInfo blockInfo = BlockInfo.getInstance();
		blockInfo.setAppId(appId);
		blockInfo.setNoticeId(tdscBlockAppView.getNodeId());
		blockInfo.setBlockNoticeNo(StringUtils.trimToEmpty(tdscBlockAppView.getBlockNoticeNo()));
		blockInfo.setBlockName(StringUtils.trimToEmpty(tdscBlockAppView.getBlockName()));
		String transferMode = StringUtils.trimToEmpty(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_BLOCK_TRANSFER, tdscBlockAppView.getTransferMode()));
		blockInfo.setTransferMode(transferMode);
		blockInfo.setSceBidLoc(StringUtils.trimToEmpty(tdscBlockAppView.getSceBidLoc()));

		if (tdscBlockAppView.getBlockId() != null && !"".equals(tdscBlockAppView.getBlockId())) {
			List tdscBlockPartList = new ArrayList();
			// 根据block_id查询TdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// 总面积
				BigDecimal planBuildingArea = new BigDecimal(0.00);// 总土地面积
				TdscBlockPart tdscBlockPart = new TdscBlockPart();

				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					if (tdscBlockPart.getBlockArea() != null) {
						totalArea = totalArea.add(tdscBlockPart.getBlockArea());
						tdscBlockAppView.setTotalLandArea(totalArea);
					}
					if (tdscBlockPart.getPlanBuildingArea() != null) {
						planBuildingArea = planBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
						tdscBlockAppView.setTotalBuildingArea(planBuildingArea);
					}
				}
			}
		}

		List tdscAuctionAppList = this.tdscLocalTradeService.findAppListByAppId(appId);
		// List tdscBidderAppList = this.tdscLocalTradeService.queryTdscBidderAppList(appId);
		List tdscListInfoList = this.tdscLocalTradeService.queryListingRecord(appId);

		// request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("tdscAuctionAppList", tdscAuctionAppList);
		request.setAttribute("tdscListInfoList", tdscListInfoList);
		// 返回页面
		// LocalTradeType现场竞价类型（1=现场书面报价|2=举牌竞价）
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);

		if (StringUtils.isNotBlank(tdscBlockAppView.getTransferMode()) && !"3107".equals(tdscBlockAppView.getTransferMode())) {
			tdscLocalTradeService.produceXcjjXml(appId);
		}

		return mapping.findForward("toAddVendueProcess");

	}

	/**
	 * 保存记录的拍卖过程
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addTradeProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map vendueAppMap = new HashMap();
		vendueAppMap.put("appId", request.getParameter("appId"));
		vendueAppMap.put("serial", request.getParameterValues("serial"));
		vendueAppMap.put("cardNo", request.getParameterValues("cardNo"));
		vendueAppMap.put("price", request.getParameterValues("price"));
		this.tdscLocalTradeService.saveTradeProcess(vendueAppMap);
		String forwardString = "vendue.do?method=toAddTradeProcess&appId=" + request.getParameter("appId");
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 为招标生成成交按钮
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
		request.setAttribute("displayStatus", displayStatus);
		String displayOver = request.getParameter("displayOver");
		request.setAttribute("displayOver", displayOver);
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		String certNo = request.getParameter("certNo");
		TdscBidderApp tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfo(certNo);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		List bidderPersonAppList = new ArrayList();
		String bidderId = tdscBidderApp.getBidderId();
		bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(bidderId);
		request.setAttribute("bidderPersonAppList", bidderPersonAppList);
		String venduePrice = request.getParameter("venduePrice");
		request.setAttribute("venduePrice", venduePrice);
		return mapping.findForward("saveInviteSuccess");
	}

	/**
	 * 为拍卖生成成交按钮
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward displayPrintButtonForVendue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String displayStatus = request.getParameter("displayStatus");
		request.setAttribute("displayStatus", displayStatus);
		String displayOver = request.getParameter("displayOver");
		request.setAttribute("displayOver", displayOver);
		String tranStatus = "1";
		request.setAttribute("tranStatus", tranStatus);
		// 查询view
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		// 根据竞买人资格证书编号查询该竞买人信息
		String certNo = request.getParameter("certNo");
		request.setAttribute("resultCert", certNo);
		TdscBidderApp tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfo(certNo);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		// 查询竞买人详细信息
		List bidderPersonAppList = null;
		if (tdscBidderApp != null) {
			bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());
		}
		request.setAttribute("bidderPersonAppList", bidderPersonAppList);
		// 查询竞得人姓名
		TdscBlockTranApp tdscBlockTranApp = null;
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		if (null != tdscBlockTranApp) {
			request.setAttribute("resultName", tdscBlockTranApp.getResultName());
		}
		String venduePrice = request.getParameter("venduePrice");
		request.setAttribute("venduePrice", venduePrice);
		return mapping.findForward("displayPrintButtonVendue");
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
		if ("1".equals(display)) {
			request.setAttribute("display", display);
			String appId = request.getParameter("appId");
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
			request.setAttribute("vendueFailedReason", vendueFailedReason);
			request.setAttribute("vendueFailedMemo", vendueFailedMemo);

		}
		return mapping.findForward("displayPrintResult");
	}

	/**
	 * 打印成交信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintTradeSuccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 按APPID查看视图 */
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String planId = "";
		TdscBlockPlanTable blockPlan = null;

		// String blockUnitName = "";
		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null & !"".equals(tdscBlockAppView.getBlockId())) {
			planId = tdscBlockAppView.getPlanId();

			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal totalArea = new BigDecimal(0);// 土地面积
			BigDecimal crjkPrice = tdscBlockAppView.getResultPrice();// 出让价款单价
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null)
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				// if(tdscBlockPart.getBlockCode()!=null)
				// blockUnitName += tdscBlockPart.getBlockCode()+"、";
			}
			// 目前无锡一个地块里面只有一个tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0 && (totalArea.compareTo(new BigDecimal("0.00")) == 1) && tdscBlockAppView.getTotalPrice() != null) {
				crjkPrice = tdscBlockAppView.getTotalPrice().divide(totalArea, 2, BigDecimal.ROUND_HALF_UP);
				// blockUnitName = blockUnitName.substring(0,blockUnitName.length()-1);
			}
			// 出让价款单价存在TotalLandArea中,子地块名称拼起 存在UnitebBlockCode中
			// tdscBlockAppView.setTotalLandArea(crjkPrice);
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
			
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
			if(tdscBlockInfo!=null && tdscBlockInfo.getGeologicalHazard()!=null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//填写了地质灾害评估费，并且大于0
				request.setAttribute("geologicalHazard", tdscBlockInfo.getGeologicalHazard());
			}else{
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

		if ("3103".equals(tdscBlockAppView.getTransferMode()))
			return mapping.findForward("toPrintInfoSuccessPM");
		else
			return mapping.findForward("toPrintInfoSuccessGPSy");
	}
	
	
	/**
	 * 打印成交信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintTradeSuccesss(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 按APPID查看视图 */
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String planId = "";
		TdscBlockPlanTable blockPlan = null;
		BigDecimal serviceCharge = new BigDecimal(0);

		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null & !"".equals(tdscBlockAppView.getBlockId())) {
			planId = tdscBlockAppView.getPlanId();

			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal totalArea = new BigDecimal(0);// 土地面积
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null){
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				}
				serviceCharge = tdscBlockPart.getServiceCharge();
			}
			// 目前无锡一个地块里面只有一个tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
			
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
			if(tdscBlockInfo!=null && tdscBlockInfo.getGeologicalHazard()!=null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//填写了地质灾害评估费，并且大于0
				request.setAttribute("geologicalHazard", tdscBlockInfo.getGeologicalHazard());
			}else{
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
		request.setAttribute("serviceCharge", serviceCharge==null?new BigDecimal(0):serviceCharge);

		if (!StringUtils.isEmpty(planId)) {
			blockPlan = tdscLocalTradeService.getTdscBlockPlanTable(planId);
			request.setAttribute("tdscBlockPlanTable", blockPlan);
		}

		if ("3103".equals(tdscBlockAppView.getTransferMode()))
			return mapping.findForward("toPrintInfoSuccessPMs");
		else
			return mapping.findForward("toPrintInfoSuccessGPSys");
	}
	
	/**
	 * 打印地质灾害评估费通知书信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintGeologicalNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 按APPID查看视图 */
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String geologicalHazard = "";
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		String planId = "";
		TdscBlockPlanTable blockPlan = new TdscBlockPlanTable();

		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null & !"".equals(tdscBlockAppView.getBlockId())) {
			planId = tdscBlockAppView.getPlanId();
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());
			//目前无锡一个地块里面只有一个tdscBlockPart
			if(tdscBlockPartList!=null && tdscBlockPartList.size()>0){
				tdscBlockPart = (TdscBlockPart)tdscBlockPartList.get(0);
			}			
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());
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
		
		tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
		if(tdscBlockInfo!=null && tdscBlockInfo.getGeologicalHazard()!=null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//填写了地质灾害评估费，并且大于0
			String dzzhpgfUpper = MoneyUtils.NumToRMBStrWithJiao(Double.parseDouble(tdscBlockInfo.getGeologicalHazard() + ""));//地质灾害评估费大写
			geologicalHazard = NumberUtil.numberDisplay(tdscBlockInfo.getGeologicalHazard(), 2);
			
			request.setAttribute("dzzhpgfUpper", dzzhpgfUpper + "");
			request.setAttribute("geologicalHazard", geologicalHazard+"");
		
			List bankDicList = tdscBlockInfoService.queryGeologyAssessUintDicList();
			if(StringUtils.isNotBlank(tdscBlockInfo.getGeologyAssessUint())){
				if (bankDicList != null && bankDicList.size() > 0) {
					for (int i = 0; i < bankDicList.size(); i++) {
						TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
						if (tdscBlockInfo.getGeologyAssessUint().equals(dicBank.getDicCode())) {
							request.setAttribute("geologyAssessUint", dicBank.getDicValue());//户名
							request.setAttribute("geologyAssessUintBankName", dicBank.getDicDescribe().split(",")[0]);//DIC_DESCRIBE里面用逗号分割银行名称和银行账号
							request.setAttribute("geologyAssessUintBankNum", dicBank.getDicDescribe().split(",")[1]);//DIC_DESCRIBE里面用逗号分割银行名称和银行账号
						}
					}
				}
			}		
		}
		
		if (!StringUtils.isEmpty(planId)) {
			blockPlan = tdscLocalTradeService.getTdscBlockPlanTable(planId);
			request.setAttribute("tdscBlockPlanTable", blockPlan);
		}
		
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("certNo", certNo);

		return mapping.findForward("toPrintGeologicalNotice");
	}

	/**
	 * 打印成交信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintIwebofficeTradeSuccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 按APPID查看视图 */
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		String blockUnitName = "";
		if (tdscBlockAppView.getBlockId() != null & !"".equals(tdscBlockAppView.getBlockId())) {
			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal totalArea = new BigDecimal(0.00);// 土地面积
			BigDecimal crjkPrice = tdscBlockAppView.getResultPrice();// 出让价款单价
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null)
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				if (tdscBlockPart.getBlockCode() != null) {
					if (j == 0) {
						blockUnitName = tdscBlockPart.getBlockCode();
					} else {
						blockUnitName += "、" + tdscBlockPart.getBlockCode();
					}
				}
			}
			// 无锡目前一块地就只有一个tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0 && (totalArea.compareTo(new BigDecimal("0.00")) == 1) && tdscBlockAppView.getTotalPrice() != null) {
				crjkPrice = tdscBlockAppView.getTotalPrice().divide(totalArea, 2, BigDecimal.ROUND_HALF_UP);
				blockUnitName = blockUnitName.substring(0, blockUnitName.length());
			}

			// 出让价款单价存在TotalLandArea中,子地块名称拼起 存在UnitebBlockCode中
			tdscBlockAppView.setTotalLandArea(crjkPrice);
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
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
		request.setAttribute("transferMode", tdscBlockAppView.getTransferMode());

		return mapping.findForward("toPrintIwebofficeSuccessFirst");

	}

	/**
	 * 打印未成交信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintInfoFailed(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		String vendueFailedReason = request.getParameter("vendueFailedReason");
		String vendueFailedMemo = request.getParameter("vendueFailedMemo");
		String resultCert = request.getParameter("certNo");
		request.setAttribute("vendueFailedReason", vendueFailedReason);
		request.setAttribute("vendueFailedMemo", vendueFailedMemo);
		String tranStatus = request.getParameter("tranStatus");
		request.setAttribute("tranStatus", tranStatus);
		// 通过appId土地基本信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("appId", appId);

		tdscBlockAppView.setBlockName(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		// 取得竟得人的证书编号和姓名
		TdscBidderApp tdscBidderApp = null;
		String resultName = "";
		tdscBidderApp = this.tdscLocalTradeService.getTdscBidderAppByAppidCardNo(appId, resultCert);
		if (null != tdscBidderApp) {
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
			}
		}
		if ("3107".equals(tdscBlockAppView.getTransferMode())) {
		}

		// 通过appId查询TDSC_BIDDER_APP表中所有竟买本土地的人，返回list
		List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);

		// 取得未竟得人列表
		List bidderIdList = new ArrayList();
		if (resultCert != null) {
			List bidderFailedList = new ArrayList();
			if ("3107".equals(tdscBlockAppView.getTransferMode())) {
				String tenderId = tdscLocalTradeService.queryTenderIdByAppId(appId);
				TdscTenderInfo tenderInfo = tdscLocalTradeService.getTdscTenderInfo(tenderId);
				String resultNo = "";
				if (null != tenderInfo)
					resultNo = tenderInfo.getTenderCertNo();
				bidderFailedList = tdscLocalTradeService.getBidderFailedListZB(resultNo, tdscBidderAppList);
			} else
				bidderFailedList = tdscLocalTradeService.getBidderFailedList(resultCert, resultName, tdscBidderAppList);

			request.setAttribute("bidderFailedList", bidderFailedList);
			if (bidderFailedList != null && bidderFailedList.size() > 0) {
				for (int i = 0; i < bidderFailedList.size(); i++) {
					TdscBidderApp temp = (TdscBidderApp) bidderFailedList.get(i);
					if (temp != null)
						bidderIdList.add(temp.getBidderId());
				}
			}
			List bidderNameList = tdscLocalTradeService.getBidderNameListByBidderId(bidderIdList);
			request.setAttribute("bidderNameList", bidderNameList);
		} else {
			for (int i = 0; i < tdscBidderAppList.size(); i++) {
				TdscBidderApp temp = (TdscBidderApp) tdscBidderAppList.get(i);
				if (temp != null)
					bidderIdList.add(temp.getBidderId());
			}
			List bidderNameList = tdscLocalTradeService.getBidderNameListByBidderId(bidderIdList);
			request.setAttribute("bidderNameList", bidderNameList);
			request.setAttribute("bidderFailedList", tdscBidderAppList);

		}
		// return mapping.findForward("toPrintInfoFailed");
		if ("3107".equals(tdscBlockAppView.getTransferMode()))
			return mapping.findForward("toPrintInfoFailedZB");
		else
			return mapping.findForward("toPrintInfoFailedGPPM");
	}

	/**
	 * 打印保证金退转名单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		String blockId = request.getParameter("blockId");
		// 根据APP_ID查询地块的“地块编号”
		String blockCodes = (String) tdscBlockInfoService.tidyBlockCodeByBlockId(blockId);
		// 根据竞得人资格证书编号和appId查看竞得人信息*/
		TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getBidderAppByAppIdCertNo(appId, certNo);
		// 获得竞得人的名称及保证金金额总数
		if (tdscBidderApp != null) {
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.tidyBidderByBidderId(tdscBidderApp.getBidderId());
			request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		}
		// 通过appId查询TDSC_BIDDER_APP表中所有竟买土地的人
		List tdscBidderAppList = (List) tdscBidderAppService.queryBidderAppList(appId);
		// 按竞得人资格证书编号查看未竞得人信息
		List bidderList = new ArrayList();
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			for (int i = 0; i < tdscBidderAppList.size(); i++) {
				TdscBidderApp tempbidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
				if (null != tempbidderApp && null != tempbidderApp.getCertNo()) {
					if (!tempbidderApp.getCertNo().equals(certNo)) {
						TdscBidderPersonApp tempApp = (TdscBidderPersonApp) tdscBidderAppService.tidyBidderByBidderId(tempbidderApp.getBidderId());
						bidderList.add(tempApp);
					}
				}
			}
		}
		request.setAttribute("bidderList", bidderList);
		request.setAttribute("blockCodes", blockCodes);
		return mapping.findForward("toPrintInfoBack");
	}

	/**
	 * 保存招标成功结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveInviteSuccessResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// 业务Id
		// 暂时为输入交易卡编号
		String tenderNo = request.getParameter("tenderNo");// 评标编号
		String invitePrice = request.getParameter("invitePrice");// 成交价格

		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// 当前时间
		// 查询地块信息view
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// 用评标编号和地块业务ID,确定唯一的一个竞买信息
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		// tdscBidderApp = this.tdscBidderAppService
		// .queryTenderInfoByAppIdTenderId(appId, tenderNo);
		// String bidderId = tdscBidderApp.getBidderId();
		String bidderId = tdscBidderAppService.queryBidderId(appId, tenderNo);
		List bidderPersonList = this.tdscBidderAppService.queryBidderPersonList(bidderId);
		String resultName = "";
		if (null != tdscBidderApp) {
			request.setAttribute("bidderPersonAppList", bidderPersonList);
			if (null != bidderPersonList && bidderPersonList.size() > 0) {
				for (int i = 0; i < bidderPersonList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(i);
					resultName += tdscBidderPersonApp.getBidderName() + ",";
				}
			}
		}
		if (!"".equals(resultName)) {
			resultName = resultName.substring(0, resultName.length() - 1);
		}
		// 保存招标结果
		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo.setAppId(appId);// 需要保存
		tdscTenderInfo.setTenderCertNo(tenderNo);// 需要保存
		tdscTenderInfo.setResultPrice(new BigDecimal(invitePrice));
		tdscTenderInfo.setTenderNo(tenderNo);
		tdscTenderInfo.setTenderResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"交易成功
		// tdscTenderInfo.setTenderResultTime(tdscBlockAppView.getOpeningDate());
		// this.tdscLocalTradeService.saveTenderInfo(tdscTenderInfo);
		// 更改地块交易表
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"交易成功
		tdscBlockTranApp.setResultDate(nowTime);
		tdscBlockTranApp.setResultPrice(new BigDecimal(invitePrice));
		tdscBlockTranApp.setResultCert(tdscBidderApp.getCertNo());

		tdscBlockTranApp.setResultName(resultName);
		// this.tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
		// 对TDSC_block_info表进行交易结果数据的纪录
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// 根据block_id查询TdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// 保存交易结果信息
			tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
			tdscBlockInfo.setResultDate(nowTime);
			tdscBlockInfo.setResultPrice(new BigDecimal(invitePrice));
			tdscBlockInfo.setResultCert(tdscBidderApp.getCertNo());
			tdscBlockInfo.setResultName(resultName);
			tdscBlockInfo.setStatus("02");
		}
		// 保存招标结果
		this.tdscLocalTradeService.updateTenderResult(tdscTenderInfo, tdscBlockTranApp, tdscBlockInfo);
		request.setAttribute("venduePrice", invitePrice);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("enterWay", VENDUE_RESULT);
		request.setAttribute("resultNo", tenderNo);
		return mapping.findForward("saveInviteSuccess");
	}

	/**
	 * 保存招标失败结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveInviteFailedResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// 业务Id
		String failedReason = request.getParameter("failedReason");// 流标原因
		String failedMemo = request.getParameter("failedMemo");// 流标备注
		if (failedMemo == null) {
			failedMemo = "";
		}

		// 查询地块信息view
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// 保存招标结果
		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo.setAppId(appId);// 需要保存
		tdscTenderInfo.setTenderFailedMemo(failedMemo);
		tdscTenderInfo.setTenderFailedReason(failedReason);
		// tdscTenderInfo.setTenderResultTime(tdscBlockAppView.getOpeningDate());
		tdscTenderInfo.setTenderResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"交易失败
		this.tdscLocalTradeService.saveTenderInfo(tdscTenderInfo);

		// 更改地块交易表
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"交易失败
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		this.tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("vendueFailedReason", failedReason);
		request.setAttribute("vendueFailedMemo", failedMemo);
		return mapping.findForward("saveInviteFailed");
	}

	/**
	 * 弹出招标过程录入窗口
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addInviteWindow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = (String) request.getParameter("appId");
		request.setAttribute("appId", appId);
		return mapping.findForward("addInviteWindow");
	}

	/**
	 * 增加招标过程记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addInviteprocess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
		condition.setAppId(appId);
		TdscBlockPlanTable tdscBlockPlanTable = this.tdscLocalTradeService.queryBlockPlanTable(condition);
		// 招标会Id
		String tenderId = this.tdscLocalTradeService.queryTenderIdByAppId(appId);
		// 投标人证书号
		String certNo = request.getParameter("tenderCert");
		// 评标编号
		String tenderNo = request.getParameter("pingBiaoNo");
		// 投标人姓名
		String inviteName = request.getParameter("inviteName");
		// 投标价格
		String price = request.getParameter("price");
		BigDecimal priceShares = new BigDecimal(price);
		// 商务标得分
		String businessScore = request.getParameter("businessScore");
		BigDecimal businessShares = new BigDecimal(businessScore);
		// 技术标得分
		String techScore = request.getParameter("techScore");
		BigDecimal techShares = new BigDecimal(techScore);
		// 综合分
		String totalScore = request.getParameter("totalScore");
		BigDecimal docShares = new BigDecimal(totalScore);
		// 投标时间
		Timestamp openingDate = tdscBlockPlanTable.getOpeningDate();
		// 备注
		String tenderMemo = request.getParameter("inviteMemo");
		// 招标类型
		String inviteType = request.getParameter("inviteType");
		// logger.debug("inviteType========"+inviteType);
		// 保存增加的过程
		TdscTenderApp tdscTenderApp = new TdscTenderApp();
		tdscTenderApp.setTenderId(tenderId);
		tdscTenderApp.setTenderType(inviteType);
		tdscTenderApp.setTenderCert(certNo);
		tdscTenderApp.setTenderNo(tenderNo);
		tdscTenderApp.setTenderPersonName(inviteName);
		tdscTenderApp.setPriceShares(priceShares);
		tdscTenderApp.setBusinessShares(businessShares);
		tdscTenderApp.setTechShares(techShares);
		tdscTenderApp.setDocShares(docShares);
		tdscTenderApp.setTenderDate(openingDate);
		tdscTenderApp.setTenderMemo(tenderMemo);
		// 查询是否有招标记录,如果有则在原记录上修改
		String tenderAppId = request.getParameter("tenderAppId");
		if (!StringUtil.isEmpty(tenderAppId)) {
			tdscTenderApp.setTenderAppId(tenderAppId);
			this.tdscLocalTradeService.modifyTenderAppRecord(tdscTenderApp);
		} else {
			this.tdscLocalTradeService.saveTenderAppInfo(tdscTenderApp);
		}
		String forwardString = "vendue.do?method=toAddTradeProcess&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 修改招标记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toModifyInvite(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String certNo = request.getParameter("certNo");
		TdscTenderApp tdscTenderApp = this.tdscLocalTradeService.queryTenderAppListByCertNo(certNo);
		request.setAttribute("tdscTenderApp", tdscTenderApp);
		return mapping.findForward("toModifyInvite");
	}

	/**
	 * 删除一条投标记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toDeleteInvite(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		TdscTenderApp tdscTenderApp = this.tdscLocalTradeService.queryTenderAppListByCertNo(certNo);
		this.tdscLocalTradeService.removeTenderAppRecord(tdscTenderApp);
		String forwardString = "vendue.do?method=toAddTradeProcess&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
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
	public ActionForward displayPrintResultForInvite(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String display = request.getParameter("display");
		String vendueFailedReason = request.getParameter("vendueFailedReason");
		String vendueFailedMemo = request.getParameter("vendueFailedMemo");
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

		return mapping.findForward("displayPrintResultInvite");
	}

	/**
	 * 检查挂牌地块是否进行过程录入
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// 查询地块信息view
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// 查询挂牌会信息
		TdscListingInfo tdscListingInfo = (TdscListingInfo) this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		String localType = tdscBlockAppView.getLocalTradeType();
		String status = "false";
		if (tdscListingInfo != null) {
			if (tdscListingInfo.getListResult() != null && tdscListingInfo.getSceneResult() != null) {
				if (tdscListingInfo.getListResult().equals(GlobalConstants.DIC_LISTING_SCENE) && tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_CHANGE)
						&& tdscListingInfo.getSceneResult().equals(GlobalConstants.DIC_SCENE_SUCCESS)) {
					status = "true";
				} else if (tdscListingInfo.getListResult().equals(GlobalConstants.DIC_LISTING_SCENE)
						&& tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_NO_CHANGE)
						&& tdscListingInfo.getSceneResult().equals(GlobalConstants.DIC_SCENE_SUCCESS)) {
					status = "true";
				}
			}
		}

		String str = status + "," + appId + "," + localType;
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		// 返回给回调函数的参数
		pw.write(str);
		pw.close();
		return null;
	}

	/**
	 * 保存价格招标过程
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveInvitPriceProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map tenderAppMap = new HashMap();
		tenderAppMap.put("appId", request.getParameter("appId"));
		tenderAppMap.put("inviteType", request.getParameter("inviteTypePrice"));
		tenderAppMap.put("serialPrice", request.getParameterValues("serialPrice"));
		tenderAppMap.put("certNoPrice", request.getParameterValues("certNoPrice"));
		tenderAppMap.put("tenderNoPrice", request.getParameterValues("tenderNoPrice"));
		tenderAppMap.put("invitePrice", request.getParameterValues("invitePrice"));
		this.tdscLocalTradeService.saveInviteProcess(tenderAppMap);
		String forwardString = "vendue.do?method=toAddTradeProcess&appId=" + request.getParameter("appId");
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 换牌结束
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endForVendue_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// 查询地块信息view
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String transferMode = tdscBlockAppView.getTransferMode();
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		try {
			this.appFlowService.saveOpnn(appId, transferMode, user);// 参数依次为appId、出让方式和用户信息
		} catch (Exception e) {
			e.printStackTrace();
		}
		String forwardString = "vendue.do?method=queryBlockListForVendue&enterWay=1";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 以公告为单位，结束换牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endForVendue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 先保存号牌
		LocalTradeForm localTradeForm = (LocalTradeForm) form;
		String[] cardNos = localTradeForm.getCardNos();// 传入的是号牌号
		String[] certNos = localTradeForm.getCertNos();// 传入的是交易卡号
		String noticeId = request.getParameter("noticeId");
		// 查询机审合格的竞买人信息列表
		if (cardNos != null && cardNos.length > 0 && certNos != null && certNos.length > 0) {
			for (int j = 0; j < cardNos.length; j++) {
				String certNo = certNos[j];// 交易卡号
				String cardNo = cardNos[j];// 号牌号

				if (noticeId != null && !"".equals(noticeId) && certNo != null && !"".equals(certNo)) {
					List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByYktBh(noticeId, certNo);
					if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
						for (int i = 0; i < tdscBidderAppList.size(); i++) {
							TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
							tdscBidderApp.setConNum(cardNo);
							tdscBidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
							tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
						}
					}
				}
			}
		}

		// 结束挂牌保存流程
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		List tempList = (List) tdscLocalTradeService.queryAppViewListByNoticeId(noticeId);

		if (tempList != null && tempList.size() > 0) {
			tdscLocalTradeService.saveOpnnJSHP(tempList, user);
		}
		String forwardString = "vendue.do?method=queryBlockListForVendue&enterWay=1";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 拍卖结果录入出让结束
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endVendueForResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String transferMode = request.getParameter("transferMode");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		try {

			this.appFlowService.saveOpnn(appId, transferMode, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String forwardString = "vendue.do?method=queryBlockListForProcess&enterWay=3&appId=" + appId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	// add by smw

	/**
	 * 判断一个号牌在一个地块的现场竞价会中是否重复使用
	 */
	public ActionForward checkHaoPai_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		String cardNo = request.getParameter("cardNo");
		String certNo = request.getParameter("certNo");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retResult = "00";
		String tempCertNo = (String) tdscBidderAppService.checkConnumByCertNo(appId, cardNo);
		// 判断数据库有没有该卡号；
		if (!"".equals(tempCertNo) && tempCertNo != null) {
			if (!certNo.equals(tempCertNo)) {
				retResult = "77";
			}
		}
		// 返回给回调函数的参数
		pw.write(retResult);
		pw.close();
		return null;
	}

	public ActionForward checkHaoPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");
		String cardNo = request.getParameter("cardNo");// 原 资格证书编号 ，现传 一卡通编号 的值
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retResult = "00";
		List tempList = (List) tdscLocalTradeService.queryBidderAppListByNoticeId(noticeId);
		if (cardNo != null && !"".endsWith(cardNo)) {
			if (tempList != null && tempList.size() > 0) {
				for (int i = 0; i < tempList.size(); i++) {
					TdscBidderApp tdscBidderApp = (TdscBidderApp) tempList.get(i);
					if (cardNo.equals(tdscBidderApp.getConNum())) {
						retResult = "77";
						break;
					}
				}
			}
		}
		// 返回给回调函数的参数
		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * 校验输入的受理编号是否存在
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkCertNoByAppId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retResult = "00";
		// 根据appId,certNo查询certNo是否存在且唯一
		List tempList = (List) tdscBidderAppService.checkCertNoByAppId(appId, certNo);
		if (tempList != null && tempList.size() == 1) {
			retResult = "77";
		} else if (tempList.size() > 1) {
			retResult = "22";
		}
		// 返回给回调函数的参数
		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * 保存招标未成交结果
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addTenderFailedResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String tenderFailedReason = request.getParameter("vendueFailedReason");
		String tenderFailedMemo = request.getParameter("vendueFailedMemo");
		if (tenderFailedMemo.equals("")) {
			tenderFailedMemo = "";
		}
		// 地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo.setAppId(appId);
		tdscTenderInfo.setTenderFailedReason(tenderFailedReason);
		tdscTenderInfo.setTenderFailedMemo(tenderFailedMemo);

		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"交易失败
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		// 更新TdscBlockInfo表
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// 根据block_id查询TdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// 保存交易结果信息
			tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
			tdscBlockInfo.setResultDate(new Timestamp(System.currentTimeMillis()));
		}

		this.tdscLocalTradeService.updateTenderResult(tdscTenderInfo, tdscBlockTranApp, tdscBlockInfo);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("vendueFailedReason", tenderFailedReason);
		request.setAttribute("vendueFailedMemo", tenderFailedMemo);

		return mapping.findForward("saveVendueResult");
	}

	public ActionForward queryJjgcjlIframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		if (tdscBlockAppView.getBlockId() != null && !"".equals(tdscBlockAppView.getBlockId())) {
			List tdscBlockPartList = new ArrayList();
			// 根据block_id查询TdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal transferArea = new BigDecimal(0);

			BigDecimal volumeRate = new BigDecimal(0);

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// 总面积
				BigDecimal planBuildingArea = new BigDecimal(0.00);// 总土地建筑面积
				TdscBlockPart tdscBlockPart = new TdscBlockPart();

				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					if (tdscBlockPart.getBlockArea() != null) {
						transferArea = tdscBlockPart.getBlockArea();
						// volumeRate = tdscBlockPart.getVolumeRate();

						// 使用该字段，存放规划建筑面积
						volumeRate = tdscBlockPart.getPlanBuildingArea();

						totalArea = totalArea.add(tdscBlockPart.getBlockArea());
					}
				}
			}
			request.setAttribute("transferArea", transferArea);
			request.setAttribute("volumeRate", volumeRate);
		}
		List tdscAuctionAppList = this.tdscLocalTradeService.findAppListByAppId(appId);
		List tdscBidderAppList = this.tdscLocalTradeService.queryTdscBidderAppList(appId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("tdscAuctionAppList", tdscAuctionAppList);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		return mapping.findForward("list_jjgcjl_iframe");
	}

	/**
	 * 查看入口
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toViewTradeProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		if (tdscBlockAppView.getBlockId() != null && !"".equals(tdscBlockAppView.getBlockId())) {
			List tdscBlockPartList = new ArrayList();
			// 根据block_id查询TdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// 总面积
				BigDecimal planBuildingArea = new BigDecimal(0.00);// 总土地面积
				TdscBlockPart tdscBlockPart = new TdscBlockPart();
				// 如果若出让地块为工业地块（系统中把规划建筑面积为0的地块默认为工业地块），
				// 在过程录入页面录入价格时，将页面中的“规划建筑面积(㎡)”字眼改为“土地面积(㎡)”并读取土地面积的数值，
				// 将页面中的“楼面地价(元／㎡)”字眼改为“土地单价(元／㎡)”，同时，自动计算其总价，工业地块的计算公式为土地面积*土地单价。
				// 注意：方法中将页面中的“规划建筑面积(㎡)”或者“土地面积(㎡)”的值设置在totalLandArea字段中
				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					if (tdscBlockPart.getBlockArea() != null) {
						totalArea = totalArea.add(tdscBlockPart.getBlockArea());
						if (totalArea.compareTo(new BigDecimal("0")) == 1) {
							tdscBlockAppView.setBlockType(GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY + "");
							tdscBlockAppView.setTotalLandArea(totalArea);
						}
					}
					if (tdscBlockPart.getPlanBuildingArea() != null) {
						planBuildingArea = planBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
						if (planBuildingArea.compareTo(new BigDecimal("0")) == 1) {
							tdscBlockAppView.setBlockType(GlobalConstants.DIC_BLOCK_TYPE_COMMERCE + "");
							tdscBlockAppView.setTotalLandArea(planBuildingArea);
						}
					}
				}
			}
		}

		List tdscAuctionAppList = this.tdscLocalTradeService.findAppListByAppId(appId);
		List tdscBidderAppList = this.tdscLocalTradeService.queryTdscBidderAppList(appId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("tdscAuctionAppList", tdscAuctionAppList);
		// 返回页面
		// LocalTradeType现场竞价类型（1=现场书面报价|2=举牌竞价）
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		return mapping.findForward("viewVendueProcess");

	}

	/**
	 * 查看
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward viewJjgcjlIframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		if (tdscBlockAppView.getBlockId() != null && !"".equals(tdscBlockAppView.getBlockId())) {
			List tdscBlockPartList = new ArrayList();
			// 根据block_id查询TdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal transferArea = new BigDecimal(0);
			String volumeRate = "";

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// 总面积
				BigDecimal planBuildingArea = new BigDecimal(0.00);// 总土地面积
				TdscBlockPart tdscBlockPart = new TdscBlockPart();

				// 如果若出让地块为工业地块（系统中把规划建筑面积为0的地块默认为工业地块），
				// 在过程录入页面录入价格时，将页面中的“规划建筑面积(㎡)”字眼改为“土地面积(㎡)”并读取土地面积的数值，
				// 将页面中的“楼面地价(元／㎡)”字眼改为“土地单价(元／㎡)”，同时，自动计算其总价，工业地块的计算公式为土地面积*土地单价。
				// 注意：方法中将页面中的“规划建筑面积(㎡)”或者“土地面积(㎡)”的值设置在totalLandArea字段中
				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					if (tdscBlockPart.getBlockArea() != null) {
						transferArea = tdscBlockPart.getBlockArea();
						volumeRate = tdscBlockPart.getVolumeRate();
						totalArea = totalArea.add(tdscBlockPart.getBlockArea());
						if (totalArea.compareTo(new BigDecimal("0")) == 1) {
							tdscBlockAppView.setBlockType(GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY + "");
							tdscBlockAppView.setTotalLandArea(totalArea);
						}
					}
					if (tdscBlockPart.getPlanBuildingArea() != null) {
						planBuildingArea = planBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
						if (planBuildingArea.compareTo(new BigDecimal("0")) == 1) {
							tdscBlockAppView.setBlockType(GlobalConstants.DIC_BLOCK_TYPE_COMMERCE + "");
							tdscBlockAppView.setTotalLandArea(planBuildingArea);
						}
					}
				}
			}
			request.setAttribute("transferArea", transferArea);
			request.setAttribute("volumeRate", volumeRate);
		}
		List tdscAuctionAppList = this.tdscLocalTradeService.findAppListByAppIdDesc(appId);
		List tdscBidderAppList = this.tdscLocalTradeService.queryTdscBidderAppList(appId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("tdscAuctionAppList", tdscAuctionAppList);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		return mapping.findForward("view_jjgcjl_iframe");
	}

	/***
	 * 保存现场竞价过程信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveJjgcjlIframe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LocalTradeForm localTradeForm = (LocalTradeForm) form;
		String appId = (String) localTradeForm.getAppId();
		/** 报价顺序 */
		String[] numbers = (String[]) localTradeForm.getNumbers();
		/** 号牌list */
		String[] haoPais = (String[]) localTradeForm.getHaoPais();
		/** 所报单价 */
		String[] addPrices = (String[]) localTradeForm.getAddPrices();
		/** 总价 */
		String[] totalPrices = (String[]) localTradeForm.getTotalPrices();
		List tdscAuctionAppList = new ArrayList();
		if (addPrices == null) {

		} else {
			// 设置保存时间
			Timestamp auctionDate = new Timestamp(System.currentTimeMillis());
			for (int i = 0; i < addPrices.length; i++) {
				if (!"".equals(addPrices[i]) && addPrices[i] != null) {
					TdscAuctionApp tdscAuctionApp = new TdscAuctionApp();
					// 设置APP_ID
					tdscAuctionApp.setAppId(appId);
					tdscAuctionApp.setAuctionDate(auctionDate);
					tdscAuctionApp.setAuctionSer(new BigDecimal(numbers[i]));
					tdscAuctionApp.setHaoPai(haoPais[i]);
					tdscAuctionApp.setAddPrice(new BigDecimal(addPrices[i]));
					tdscAuctionApp.setTotalPrice(new BigDecimal(totalPrices[i]));
					tdscAuctionAppList.add(tdscAuctionApp);
				}
			}
		}
		tdscLocalTradeService.tidyAppListByAppId(appId, tdscAuctionAppList);

		tdscLocalTradeService.produceXcjjXml(appId);

		return new ActionForward("vendue.do?method=queryJjgcjlIframe&appId=" + appId, true);
	}

}
