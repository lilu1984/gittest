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
	 * �������ý���
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
			tdscBlockTranApp.setTranResult("02");// ���׽��  00δ���� 01 ���׳ɹ���02 ����ʧ�ܣ����꣩��04 ��ֹ���ף�
			tdscLocalTradeService.updateTdscBlockTranApp(tdscBlockTranApp);
			
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			tdscBlockInfo.setStatus("02");//(00-δ����;01-������;02-���׽���)
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
	 * �б���ý���
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
	 * ԭ�Եؿ�Ϊ��λ��ѯ��������ơ�����
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
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
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
	 * �Գ����ļ������棩Ϊ��λ��ѯ��������ơ�
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
		// ��ѯ���÷�ʽΪ���������ĵؿ�
		TdscFlowCondition node1 = new TdscFlowCondition();
		node1.setNodeId(FlowConstants.FLOW_NODE_AUCTION);
		node1.setStatusId(FlowConstants.FLOW_STATUS_AUCTION_CHANGE);
		flowConditionList.add(node1);
		// ��ѯ���÷�ʽΪ�����ơ��ĵؿ�
		TdscFlowCondition node2 = new TdscFlowCondition();
		node2.setNodeId(FlowConstants.FLOW_NODE_LISTING_SENCE);
		node2.setStatusId(FlowConstants.FLOW_STATUS_LISTING_SENCE_CHANGE);
		flowConditionList.add(node2);
		// ��ѯ��ֹ���׵ĵؿ�
		TdscFlowCondition node3 = new TdscFlowCondition();
		node3.setNodeId(FlowConstants.FLOW_NODE_FINISH);
		node3.setStatusId(FlowConstants.FLOW_STATUS_END_TRADE);
		flowConditionList.add(node3);
		// ��ѯ�ڹ�����Ϣ����ģ���У��������Ʋ���ʱ���������ײ������ֳ����ۣ�������ƣ��ĵؿ�
		TdscFlowCondition node4 = new TdscFlowCondition();
		node4.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node4.setStatusId(FlowConstants.FLOW_STATUS_LISTING_RESULT);
		flowConditionList.add(node4);
		// ���ò�ѯ����LIST
		condition.setFlowConditonList(flowConditionList);

		condition.setCurrentPage(cPage);
		condition.setUser(user);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		// ��ѯ���ϡ�������ơ������ĵؿ���Ϣ
		List tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewList(condition);
		List returnNoticeList = new ArrayList();
		if (tdscBlockAppViewPageList != null && tdscBlockAppViewPageList.size() > 0) {
			// ��ѯ�����ļ������棩�Ѿ������������潻�׽����δ�����Ĺ�����Ϣ
			List noticeIdListInTrade = (List) tdscLocalTradeService.queryNoticeIdListInTrade();
			if (noticeIdListInTrade != null && noticeIdListInTrade.size() > 0) {
				for (int i = 0; i < noticeIdListInTrade.size(); i++) {
					String noticeId = (String) noticeIdListInTrade.get(i);
					// �����еؿ�LIST
					List viewAppList = (List) tdscLocalTradeService.queryAppViewListByNoticeId(noticeId);
					// �ж��Ƿ�һ�������е����еؿ鶼�ڡ�������ơ��ڵ�
					if (viewAppList != null && viewAppList.size() > 0) {
						int tempCount = 0;
						for (int k = 0; k < viewAppList.size(); k++) {
							// �ж�ÿ�ݹ����������ĵؿ��Ƿ��Ѿ��ڡ�������ơ��ڵ�
							TdscBlockAppView app = (TdscBlockAppView) viewAppList.get(k);
							for (int j = 0; j < tdscBlockAppViewPageList.size(); j++) {
								TdscBlockAppView appView = (TdscBlockAppView) tdscBlockAppViewPageList.get(j);
								if (app.getAppId().equals(appView.getAppId())) {
									tempCount++;
								}
							}
						}
						// ���ÿ���ؿ鶼�ڡ�������ơ��ڵ㣬�����LIST
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
								// ���еؿ���Ϣ��
								List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
								String blockNameAll = "";
								if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
									for (int j = 0; j < tdscBlockAppViewList.size(); j++) {

										TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);

										blockNameAll += blockapp.getBlockName() + ",";
										tdscNoticeApp.setTranManTel(blockapp.getTradeNum());// ��"���Ĺұ��"���ڴ��ֶ���
										tdscNoticeApp.setTranManAddr(blockapp.getTransferMode());// ��"���÷�ʽ"���ڴ��ֶ���
									}
									blockNameAll = blockNameAll.substring(0, blockNameAll.length() - 1);
									tdscNoticeApp.setTranManName(blockNameAll);// ���ؿ����ƴ��ڴ��ֶ���
								}
								returnNoticeList.add(tdscNoticeApp);
							}
							// LZ END
						}
					}
				}
			}
		}
		// �����ҳ
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
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
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
		// // ƴװ��ҳ��Ϣ
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
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
		condition.setEnterWay("2");

		List flowConditionList = new ArrayList();
		TdscFlowCondition node = new TdscFlowCondition();
		node.setNodeId(FlowConstants.FLOW_NODE_LISTING);
		node.setStatusId(FlowConstants.FLOW_STATUS_LISTING_CONFIRM);
		flowConditionList.add(node);
		condition.setFlowConditonList(flowConditionList);
		condition.setOrderKey("blockNoticeNo");
		condition.setOrderType("desc");

		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
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
					// ������÷�ʽ�ǹ��Ƶģ�����
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
	 * �Թ���Ϊ��λ�����׹��̹���¼�������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockListForProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// �Զ��������ƣ�Ŀǰע�͵�
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
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
		condition.setEnterWay(request.getParameter("enterWay"));

		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
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
		condition.setIfOnLine("0");//��ѯ�ֳ����׵ĵؿ飨ifOnLine=0��
		
		//PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageList(condition);
		List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);

		List tempList = null;
		//if (tdscBlockAppViewPageList != null) {
			//tempList = tdscBlockAppViewPageList.getList();
			tempList = tdscBlockAppViewList;

			// ���˲��ý����ֳ�¼��ĵؿ�, add by xys, 2011-02-15
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
		
		// �����ҳ
		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		
		pageList = PageUtil.getPageList(tempList, pageSize, cPage);
		
		request.setAttribute("tdscBlockAppViewPageList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("queryBlockList");
	}

	private List filterBlock(List blockList) {
		// ��1���б���õؿ�
		// ��2�����������������ҵ��������õؿ�
		// ��3����������Ƶؿ�
		//		1.���˱����ģ�ֱ���������˾��ã��������ֳ�����
		//		2.���������˱������������˶�û���۵ģ�ֱ���������˾��ã��������ֳ�����
		// ��4����������Ƶؿ�
		//		1.���˱����ģ��������ֳ�����
		//		2.��һ�˱������Ҹ��˱����۵ģ�ֱ���ɸ��˾��ã��������ֳ�����
		//		3.��һ�˱������Ҹ���û�б����۵ģ��������ֳ�����
		//		3.���˻��������ϱ�����ֻҪ�õؿ��ޱ��۵ģ��������ֳ�����		
		if (blockList != null && blockList.size() > 0)
			for (int i = 0; i < blockList.size();) {
				TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);
				List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
				List listingAppList = tdscLocalTradeService.getListingAppListByAppId(app.getAppId(), null, "11");
				
				//��������Ƶؿ�
				if ("1".equals(app.getIsPurposeBlock())){
					if (personList.size()==1) {//���������ˣ����˱���
						blockList.remove(i);
						continue;
					}else if(personList.size()>1 && listingAppList.size()<2){//�о����˱����������������˵Ĺ��Ƽۣ��޾����˱���
						blockList.remove(i);
						continue;
					}else{
						i++;
					}
				}
				//��������Ƶؿ�
				if ("0".equals(app.getIsPurposeBlock())){
					if (personList.size()==0) {//���˱���
						blockList.remove(i);
						continue;
					}else if(personList.size()==1 && listingAppList.size()>=1){//��һ�˱������Ҹ����б���
						blockList.remove(i);
						continue;
					}else if(personList.size()==1 && listingAppList.size()==0){//��һ�˱������Ҹ���û�б���
						blockList.remove(i);
						continue;
					}else if(personList.size()>1 && listingAppList.size()==0){//���˱����������ޱ���
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
	 * �Թ���Ϊ��λ�����׹��������¼������Ϣ
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
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
		condition.setEnterWay(request.getParameter("enterWay"));
		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
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
		condition.setIfOnLine("0");//ֻ��Ҫ���ֳ����ף�ifOnLine=0���ĵؿ������׽������;���Ͻ��׵ؿ����������׽�����������Զ������ý���
		// condition.setOrderType("desc");

		// 2011-03-21 ��Ҫ�ѹ��˵������ݼ��뵽list ����ʾ����
		// ����ؿ�û���˾���ĵؿ�
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

					// // ����ؿ�û���˾���ĵؿ�
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
	 * ���ƻ������
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
		// ������÷�ʽ�ǹ��Ƶģ�����
		try {
			this.appFlowService.saveOpnn(appFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ͨ��������Ϣ���òμ������ỻ����Ƶ�����
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
	 * �������listҳ��
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
		// ͨ��������Ϣ���òμ������ỻ����Ƶ�����
		List tdscBidderAppList = tdscLocalTradeService.queryBidderListSrc(appId);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("appId", appId);

		return mapping.findForward("qd_huanpaiList");

	}

	/**
	 * �Թ���Ϊ��λ�����С�������Ʋ����� 1���׿���Ӧ����ؿ�
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

		// ���ݹ����ѯ�ؿ���Ϣ�б�
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		// ҳ��ؿ鹫��Ŵ�С��������
		condition.setOrderKey("blockNoticeNo");
		// ��ѯ��������
		List appViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

		// ͨ���ؿ���Ϣ���òμ������ỻ����Ƶ�����������ͨ����
		// List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByNoticeId(noticeId);

		// ������в�ͬ���׿�����Ϣ�б�start lz+
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
		// ������в�ͬ���׿�����Ϣ�б�end

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("appViewList", appViewList);
		request.setAttribute("tdscBidderAppList", bidderAppList);

		return mapping.findForward("qd_huanpaiList");
	}

	/**
	 * ��ӡ����λ�����
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

		// ����noticeId��ȡ���еؿ�appIdList�����ƹұ��
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
					beiZhu += tdscBlockAppView.getXuHao() + "�ŵؿ�";

					// ƴ�ӱ�ע,ÿ���ؿ龺������
					List jmrList = tdscBidderAppService.queryJoinAuctionList(tdscBlockAppView.getAppId());
					if (jmrList != null) {
						beiZhu += " " + jmrList.size() + "��";
					} else {
						beiZhu += " 0��";
					}
					beiZhu += "��";
				}
			}
			if (beiZhu != null && beiZhu.length() > 0) {
				beiZhu = beiZhu.substring(0, beiZhu.length() - 1) + "��";
			}
		}

		List returnList = new ArrayList();

		if (noticeId != null && !"".equals(noticeId)) {
			// ����noticeIdȡ������yktbh����ͬ�ľ�������Ϣ�б�
			// List tdscBidderAppList = tdscBidderAppService.findBidderListByAppIdList(appIdList);

			List tdscBidderAppList = (List) tdscBidderAppService.queryBidderAppListByNoticeId(noticeId);

			if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
				for (int n = 0; n < tdscBidderAppList.size(); n++) {
					TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(n);
					String unionBlockXuhao = "";
					String unionPersonkName = "";// ����Ͷ��������
					String unionCorpFr = "";// ��������������
					String unionPersonTel = "";// ����Ͷ���˵绰

					if (tdscBidderApp != null && tdscBidderApp.getConNum() != null) {
						// ���ݾ�����bidderIdȡ�þ���������
						List tdscBidderPersonAppList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
						if (tdscBidderPersonAppList != null && tdscBidderPersonAppList.size() > 0) {
							for (int k = 0; k < tdscBidderPersonAppList.size(); k++) {
								TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppList.get(k);
								if (tdscBidderPersonApp != null) {
									if (tdscBidderPersonApp.getBidderName() != null)
										unionPersonkName += tdscBidderPersonApp.getBidderName() + "��";
									if (tdscBidderPersonApp.getCorpFr() != null)
										unionCorpFr += tdscBidderPersonApp.getCorpFr() + "��";
									if (tdscBidderPersonApp.getBidderLxdh() != null)
										unionPersonTel += tdscBidderPersonApp.getBidderLxdh() + "��";
								}
							}
							if (unionPersonkName != null && unionPersonkName.length() > 0)
								unionPersonkName = unionPersonkName.substring(0, unionPersonkName.length() - 1);
							if (unionCorpFr != null && unionCorpFr.length() > 0)
								unionCorpFr = unionCorpFr.substring(0, unionCorpFr.length() - 1);
							if (unionPersonTel != null && unionPersonTel.length() > 0)
								unionPersonTel = unionPersonTel.substring(0, unionPersonTel.length() - 1);

							if ("2".equals(tdscBidderApp.getBidderType())) {
								unionPersonkName = unionPersonkName + " �����Ͼ���";// ���Ͼ���
							}
						}

						// ���ݽ��׿�����ȡ�õ��������˵����о���ؿ��б�
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
										unionBlockXuhao += tdscBlockAppView.getXuHao() + "��";
									}
								}
							}
							if (unionBlockXuhao != null && unionBlockXuhao.length() > 0)
								unionBlockXuhao = unionBlockXuhao.substring(0, unionBlockXuhao.length() - 1);
						}

						tdscBidderApp.setAcceptPeople(unionBlockXuhao);// �ؿ����ƴ���AcceptPeople
						tdscBidderApp.setAppUserId(unionPersonkName);// ����λ���ƴ���AppUserId
						tdscBidderApp.setZstrLxdh(unionPersonTel);// ����������ϵ�绰����ZstrLxdh
						// �������������Ϊnull�����ȡ����������
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
	 * ��ӡ�����λ��Ϣ
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
					listEndDate = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getListEndDate(), "yyyy��MM��dd��HHʱ"));
					trade_num = tdscBlockAppView.getTradeNum();
					transferMode = tdscBlockAppView.getTransferMode();

					if (tdscBlockAppView != null) {
						TdscListingInfo tdscListingInfo = tdscBlockInfoService.findBlistingInfo(tdscBlockAppView.getAppId());
						if (tdscListingInfo != null) {
							tdscBlockAppView.setResultPrice(tdscListingInfo.getCurrPrice());// ��󱨼�

							TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.getBidderAppByAppIdYktXh(tdscListingInfo.getAppId(), tdscListingInfo.getYktXh());
							if (tdscBidderApp != null) {
								String peasonName = tdscBidderAppService.getPersonNameByBidderId(tdscBidderApp.getBidderId());
								tdscBlockAppView.setResultName(peasonName);// �������
								tdscBlockAppView.setContractNum(tdscBidderApp.getConNum());// ���ƺ�
							}
						}
						String unionBlockCode = tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId());
						tdscBlockAppView.setUnitebBlockCode(unionBlockCode);// �ӵؿ��ż���

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
	 * �Թ���Ϊ��λ�����С�������Ʋ�����
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
		// ���ݹ����ѯ�ؿ���Ϣ�б�
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		List appViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

		// ͨ���ؿ���Ϣ���òμ����������л���ĺ����б�����ͨ����
		List certNolist = tdscLocalTradeService.queryCertNolistByNoticeId(noticeId);
		List resultList = new ArrayList();
		String flag = "false";

		if (certNolist != null && certNolist.size() > 0) {
			for (int i = 0; i < certNolist.size(); i++) {
				String certNo = (String) certNolist.get(i);// ���ƺ�
				String yktXhAll = "";// һ��ͨ����ƴ��

				// ȡ�øú��ƺ��ڹ��������ж�Ӧ�ľ�������Ϣ
				if (certNo != null && !"".equals(certNo)) {
					List tdscBidderAppListForCertNo = tdscLocalTradeService.queryBidderAppListByCertNo(noticeId, certNo);

					// ȡ�øù����й����ΪcertNo�����н��׿�����

					if (tdscBidderAppListForCertNo != null && tdscBidderAppListForCertNo.size() > 0) {
						for (int j = 0; j < tdscBidderAppListForCertNo.size(); j++) {
							TdscBidderApp tdscBidderAppForCertNo = (TdscBidderApp) tdscBidderAppListForCertNo.get(j);
							if (tdscBidderAppForCertNo.getYktXh() != null && !"".equals(tdscBidderAppForCertNo.getYktXh()))
								yktXhAll += tdscBidderAppForCertNo.getYktXh() + "��";
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

		// ��ѯ���ƺ�Ϊ�յ��б�
		List tdscBidderAppListWithNoCertNo = tdscLocalTradeService.queryBidderAppListWithNoCertNo(noticeId);

		request.setAttribute("tdscBidderAppListWithNoCertNo", tdscBidderAppListWithNoCertNo);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("appViewList", appViewList);
		request.setAttribute("tdscBidderAppList", resultList);

		return mapping.findForward("qd_huanpaiList");
	}

	/**
	 * ���汨��--���������֤ҳ�棨��ӡ�ܷⱨ�۵���
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
		// ͨ��������Ϣ���òμ������ỻ����Ƶ�����
		List tdscBidderAppList = tdscLocalTradeService.queryBidderListSrc(appId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("appId", appId);
		return mapping.findForward("qd_sfyzList");

	}

	/**
	 * ���ӡ��޸ľ����˵ĺ���
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
	 * �Թ���Ϊ��λ�����Ӿ����˺���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// ������Ǻ��ƺ�
		String noticeId = request.getParameter("noticeId");
		// ��ѯ����ϸ�ľ�������Ϣ�б�
		List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByNoticeId(noticeId);

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("certNo", certNo);
		return mapping.findForward("toAddVendueCard");
	}

	/**
	 * �Թ���Ϊ��λ��Ⱥ����¾����˺���
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

		String[] cardNos = localTradeForm.getCardNos();// ������Ǻ��ƺ�
		String[] certNos = localTradeForm.getCertNos();// ������ǽ��׿���

		String noticeId = request.getParameter("noticeId");

		// ��ѯ����ϸ�ľ�������Ϣ�б�
		if (cardNos != null && cardNos.length > 0 && certNos != null && certNos.length > 0) {
			for (int j = 0; j < cardNos.length; j++) {
				String certNo = certNos[j];// ���׿���
				String cardNo = cardNos[j];// ���ƺ�

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
	 * ת�����Ƹ���ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toUpdateVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// ���ƺ�
		String yktXh = request.getParameter("yktXh");// һ��ͨ���ƴ��
		String noticeId = request.getParameter("noticeId");
		// ��ѯ����ϸ�ľ�������Ϣ�б�
		// List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByCertNo(noticeId, certNo);

		// request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("certNo", certNo);
		request.setAttribute("yktXh", yktXh);
		return mapping.findForward("updateVendueCard");
	}

	/**
	 * �Թ���Ϊ��λ�����¾����˺���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// ԭ���ƺ�
		String cardNo = request.getParameter("cardNo");// �º��ƺ�
		String yktXh = request.getParameter("yktXh");// һ��ͨ���ƴ��
		String noticeId = request.getParameter("noticeId");
		// ��ѯ����ϸ�ľ�������Ϣ�б�
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
		request.setAttribute("message", "����ɹ���");

		return mapping.findForward("updateVendueCard");
	}

	/**
	 * �Թ���Ϊ��λ��ɾ�������˺��ƣ��ÿգ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String certNo = request.getParameter("certNo");// ���ƺ�
		String noticeId = request.getParameter("noticeId");
		// ��ѯ����ϸ�ľ�������Ϣ�б�
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
	 * ����ˢ��ҳ��
	 */
	public ActionForward showLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);

		request.setAttribute("tdscBidderAppList", tdscBidderAppList);
		return mapping.findForward("toshowLogin");
	}

	/**
	 * ��֤��ˢ���׿��Ƿ��������
	 */
	public ActionForward checkYktNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		String yktNo = request.getParameter("yktNo");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String flag = "false";
		String conNum = "";
		// ��֤�ý��׿��Ƿ���ڲ�����Χ
		TdscBidderApp tdscBidderApp = tdscBidderAppService.getBidderAppByAppIdYktXh(appId, yktNo);
		if (tdscBidderApp != null && "1".equals(tdscBidderApp.getReviewResult())) {
			flag = "true";
			conNum = tdscBidderApp.getConNum();
		}
		String retString = flag + "��" + conNum;
		pw.write(retString);
		pw.close();
		return null;
	}

	/**
	 * �����ʸ�֤������֤��������Ϣ
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

		// ��֤�ý��׿��Ƿ���ڲ�����Χ
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
	 * ����һ�����ƻ�����
	 */
	public ActionForward addVendueCard_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// ���׿����Ż��׿����
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
	 * ����һ�����ƻ�����
	 */
	public ActionForward addVendueCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LocalTradeForm localTradeForm = (LocalTradeForm) form;

		// ���׿�����
		String[] certNos = localTradeForm.getCertNos();
		// ���ƺ�
		String cardNo = request.getParameter("cardNo");
		Map vendueCardMap = new HashMap();

		if (certNos != null && certNos.length > 0) {
			for (int i = 0; i < certNos.length; i++) {
				vendueCardMap.put("certNo", certNos[i]);
				vendueCardMap.put("cardNo", cardNo);

				this.tdscLocalTradeService.saveTakeCardBidder(vendueCardMap);
			}
		}

		request.setAttribute("message", "����ɹ���");
		return mapping.findForward("toAddVendueCard");
		// String forwardString = "vendue.do?method=vendueCardList";
		// ActionForward f = new ActionForward(forwardString, true);
		// return f;
	}

	/**
	 * ������׼����д���׽��
	 */
	public ActionForward toAddVendueResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		// �ؿ���Ϣview
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		String transferMode = tdscBlockAppView.getTransferMode();// ��������
		request.setAttribute("transferMode", transferMode);
		String resultStatus = tdscBlockAppView.getTranResult();// ���׽��״̬
		// ����
		if (transferMode.equals("3104")) {
			if (resultStatus != null) {
				// ȡ����ߵĹ��Ƽ۸�
				TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
				if (null != tdscListingInfo) {
					String maxPrice = tdscLocalTradeService.getMaxPrice(tdscListingInfo.getListingId());
					request.setAttribute("maxPrice", maxPrice);
				} else {
					tdscListingInfo = new TdscListingInfo();
				}
				// ���׳ɹ�
				if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS)) {
					// ȡ�þ�������Ϣ
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
					// ����ʧ��
				} else if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
					// ȡ�ý���ʧ��ԭ��
					request.setAttribute("vendueFailedMemo", tdscListingInfo.getListFailMeno());
					request.setAttribute("vendueFailedReason", tdscListingInfo.getListFailReason());
					request.setAttribute("appId", appId);
					request.setAttribute("tdscBlockAppView", tdscBlockAppView);
					return mapping.findForward("tradeFailedGP");
					// ������
				} else if (!resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS) && !resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
					// ��������Ϣ
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
			// ��������Ϣ
			List tdscBidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appId);
			request.setAttribute("tdscBidderAppList", tdscBidderAppList);
			return mapping.findForward("toAddVendueResult");
		}
		// ����
		if (transferMode.equals("3103")) {
			if (resultStatus != null) {
				TdscAuctionInfo tdscAuctionInfo = this.tdscLocalTradeService.getTdscAuctionInfoByAppId(appId);
				if (null != tdscAuctionInfo) {
					// ���׳ɹ�
					if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS)) {
						// ȡ�þ����˵���Ϣ
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
						// ����ʧ��
					} else if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
						// ȡ�ý���ʧ��ԭ��
						request.setAttribute("vendueFailedReason", tdscAuctionInfo.getAuctionFailReason());
						request.setAttribute("vendueFailedMemo", tdscAuctionInfo.getAuctionFailMeno());
						request.setAttribute("appId", appId);
						request.setAttribute("tdscBlockAppView", tdscBlockAppView);
						request.setAttribute("resultCert", tdscAuctionInfo.getResultCert());
						return mapping.findForward("saveVendueResult");
						// ������
					} else if (!resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS) && !resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
						// ��������Ϣ
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
		// �б�
		if (transferMode != null && transferMode.equals("3107")) {
			if (resultStatus != null) {
				String tenderId = this.tdscLocalTradeService.queryTenderIdByAppId(appId);
				TdscTenderInfo tdscTenderInfo = null;
				if (null != tenderId && StringUtils.isNotEmpty(tenderId)) {
					tdscTenderInfo = this.tdscLocalTradeService.getTdscTenderInfo(tenderId);
				}
				if (null != tdscTenderInfo) {
					// ���׳ɹ�
					if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS)) {
						String tenderNo = tdscTenderInfo.getTenderNo();
						// ȡ���б�����Ϣ
						List bidderPersonAppList = null;
						// String bidderId = tdscBidderApp.getBidderId();
						String bidderId = tdscBidderAppService.queryBidderId(appId, tenderNo);
						if (bidderId != null) {
							bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(bidderId);
						}
						// ȡ����߼۸�
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
						// �б�ʧ��
					} else if (resultStatus.equals(GlobalConstants.DIC_TRANSFER_RESULT_FAIL)) {
						// ȡ���б�ʧ��ԭ��
						request.setAttribute("vendueFailedReason", tdscTenderInfo.getTenderFailedReason());
						request.setAttribute("vendueFailedMemo", tdscTenderInfo.getTenderFailedMemo());
						request.setAttribute("appId", appId);
						request.setAttribute("tdscBlockAppView", tdscBlockAppView);
						return mapping.findForward("saveInviteFailed");
						// ������
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
	 * ���������ɽ����
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
		String venduePrice = request.getParameter("venduePrice");// ����
		BigDecimal totalPrice = new BigDecimal(venduePrice);// �ܼ�
		BigDecimal crjkPrice = new BigDecimal(0.00);// ���üۿ�

		// �ؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// ��ȡ���в��������ľ�����Ϣ�б�
		// List tdscBidderAppList =
		// this.tdscLocalTradeService.queryTdscBidderAppList(appId);
		// ��װ��������Ϣ����
		TdscAuctionInfo tdscAuctionInfo = new TdscAuctionInfo();
		tdscAuctionInfo.setAppId(appId);
		tdscAuctionInfo.setAuctionDate(tdscBlockAppView.getAuctionDate());
		tdscAuctionInfo.setAutcionResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// �ֵ�
		tdscAuctionInfo.setAutcionResultDate(new Date(System.currentTimeMillis()));
		List joinAuctionList = this.tdscLocalTradeService.queryJoinAuctionList(appId);// ��ȡ�����������˵��б��������Ƶ��˵��б�
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
		tdscAuctionInfo.setAuctionModerator(compere);// ������
		tdscAuctionInfo.setAuctionNorar(notary);// ��֤��
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		tdscBidderApp = this.tdscLocalTradeService.getTdscBidderAppByAppidCardNo(appId, cardNo);// �ú��Ʊ�ź͵ؿ�ҵ��ID,ȷ��Ψһ��һ��������Ϣ
		tdscAuctionInfo.setResultCert(tdscBidderApp.getCertNo());
		tdscAuctionInfo.setResultNo(cardNo);
		tdscAuctionInfo.setResultPrice(new BigDecimal(venduePrice));
		List bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());// ��������Ϣ����Ŀ�ľ����˹���
		// ������������
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
		// this.tdscLocalTradeService.saveTdscAuctionInfo(tdscAuctionInfo);// ����TDSC_AUCTION_INFO��
		// ���ĵؿ齻�ױ�
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"���׳ɹ�
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		tdscBlockTranApp.setResultPrice(new BigDecimal(venduePrice));
		tdscBlockTranApp.setResultCert(tdscBidderApp.getCertNo());
		tdscBlockTranApp.setResultName(resultName);
		// ����TdscBlockInfo��
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		TdscBlockPart tdscBlockPart = new TdscBlockPart();
		List tdscBlockPartList = new ArrayList();
		BigDecimal totalArea = new BigDecimal(0.00);// �����
		BigDecimal totalBlockArea = new BigDecimal(0.00);// ���������
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// ����block_id��ѯTdscBlockPart
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
		// ���滮���������Ϊ�գ����üۿ�ɽ��ܼ�Ϊ�ɽ�¥��ؼ���滮��������ĳ˻���
		// ���滮�������Ϊ�գ����üۿ�ɽ��ܼ�Ϊ�ɽ�¥��ؼ�����������ĳ˻�
		totalPrice = totalArea.multiply(new BigDecimal(venduePrice));

		crjkPrice = totalPrice.divide(totalBlockArea, 2, BigDecimal.ROUND_HALF_UP);

		tdscBlockTranApp.setTotalPrice(totalPrice);

		// ����block_id��ѯTdscBlockInfo
		tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
		// ���潻�׽����Ϣ
		tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
		tdscBlockInfo.setResultDate(new Date(System.currentTimeMillis()));
		tdscBlockInfo.setResultPrice(new BigDecimal(venduePrice));
		tdscBlockInfo.setResultCert(tdscBidderApp.getCertNo());
		tdscBlockInfo.setResultName(resultName);
		tdscBlockInfo.setStatus("02");

		// this.tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
		// ���������ɽ����
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
	 * �����б�ɽ����
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
		// �ؿ���Ϣ
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

		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"���׳ɹ�
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
			// ����TdscBlockInfo��
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
				// ����block_id��ѯTdscBlockInfo
				tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
				// ���潻�׽����Ϣ
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
	 * ����δ�ɽ����
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
		// �ؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// ��װ��������Ϣ����
		TdscAuctionInfo tdscAuctionInfo = new TdscAuctionInfo();
		tdscAuctionInfo.setAppId(appId);
		tdscAuctionInfo.setAuctionDate(tdscBlockAppView.getAuctionDate());
		tdscAuctionInfo.setAutcionResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// �ֵ�
		tdscAuctionInfo.setAutcionResultDate(new Date(System.currentTimeMillis()));
		List joinAuctionList = this.tdscLocalTradeService.queryJoinAuctionList(appId);// ��ȡ�����������˵��б��������Ƶ��˵��б�
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
		tdscAuctionInfo.setAuctionModerator(compere);// ������
		tdscAuctionInfo.setAuctionNorar(notary);// ��֤��
		tdscAuctionInfo.setAuctionFailReason(vendueFailedReason);
		tdscAuctionInfo.setAuctionFailMeno(vendueFailedMemo);

		// ���ĵؿ齻�ױ�
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"����ʧ��
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		// ��TDSC_block_info����н��׽�����ݵļ�¼
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// ����block_id��ѯTdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// ���潻�׽����Ϣ
			tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
			tdscBlockInfo.setResultDate(new Date(System.currentTimeMillis()));
			tdscBlockInfo.setStatus("02");

		}
		// �����������׽��
		this.tdscLocalTradeService.updateVendueTranResult(tdscBlockTranApp, tdscBlockInfo, tdscAuctionInfo);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", this.commonQueryService.getTdscBlockAppView(condition));
		request.setAttribute("vendueFailedReason", vendueFailedReason);
		request.setAttribute("vendueFailedMemo", vendueFailedMemo);

		return mapping.findForward("saveVendueResult");
	}

	/**
	 * ��ʾ�������߹����ֳ�����¼���¼
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
			// ����block_id��ѯTdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// �����
				BigDecimal planBuildingArea = new BigDecimal(0.00);// ���������
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
		// ����ҳ��
		// LocalTradeType�ֳ��������ͣ�1=�ֳ����汨��|2=���ƾ��ۣ�
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);

		if (StringUtils.isNotBlank(tdscBlockAppView.getTransferMode()) && !"3107".equals(tdscBlockAppView.getTransferMode())) {
			tdscLocalTradeService.produceXcjjXml(appId);
		}

		return mapping.findForward("toAddVendueProcess");

	}

	/**
	 * �����¼����������
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
	 * Ϊ�б����ɳɽ���ť
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
	 * Ϊ�������ɳɽ���ť
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
		// ��ѯview
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		// ���ݾ������ʸ�֤���Ų�ѯ�þ�������Ϣ
		String certNo = request.getParameter("certNo");
		request.setAttribute("resultCert", certNo);
		TdscBidderApp tdscBidderApp = this.tdscLocalTradeService.queryBidderAppInfo(certNo);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		// ��ѯ��������ϸ��Ϣ
		List bidderPersonAppList = null;
		if (tdscBidderApp != null) {
			bidderPersonAppList = tdscLocalTradeService.queryTdscBidderPersonAppList(tdscBidderApp.getBidderId());
		}
		request.setAttribute("bidderPersonAppList", bidderPersonAppList);
		// ��ѯ����������
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
	 * ����δ�ɽ���ť
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
	 * ��ӡ�ɽ���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintTradeSuccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* ��APPID�鿴��ͼ */
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

			BigDecimal totalArea = new BigDecimal(0);// �������
			BigDecimal crjkPrice = tdscBlockAppView.getResultPrice();// ���üۿ��
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null)
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				// if(tdscBlockPart.getBlockCode()!=null)
				// blockUnitName += tdscBlockPart.getBlockCode()+"��";
			}
			// Ŀǰ����һ���ؿ�����ֻ��һ��tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0 && (totalArea.compareTo(new BigDecimal("0.00")) == 1) && tdscBlockAppView.getTotalPrice() != null) {
				crjkPrice = tdscBlockAppView.getTotalPrice().divide(totalArea, 2, BigDecimal.ROUND_HALF_UP);
				// blockUnitName = blockUnitName.substring(0,blockUnitName.length()-1);
			}
			// ���üۿ�۴���TotalLandArea��,�ӵؿ�����ƴ�� ����UnitebBlockCode��
			// tdscBlockAppView.setTotalLandArea(crjkPrice);
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
			
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
			if(tdscBlockInfo!=null && tdscBlockInfo.getGeologicalHazard()!=null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//��д�˵����ֺ������ѣ����Ҵ���0
				request.setAttribute("geologicalHazard", tdscBlockInfo.getGeologicalHazard());
			}else{
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

		if ("3103".equals(tdscBlockAppView.getTransferMode()))
			return mapping.findForward("toPrintInfoSuccessPM");
		else
			return mapping.findForward("toPrintInfoSuccessGPSy");
	}
	
	
	/**
	 * ��ӡ�ɽ���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintTradeSuccesss(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* ��APPID�鿴��ͼ */
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

			BigDecimal totalArea = new BigDecimal(0);// �������
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null){
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				}
				serviceCharge = tdscBlockPart.getServiceCharge();
			}
			// Ŀǰ����һ���ؿ�����ֻ��һ��tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
			
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
			if(tdscBlockInfo!=null && tdscBlockInfo.getGeologicalHazard()!=null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//��д�˵����ֺ������ѣ����Ҵ���0
				request.setAttribute("geologicalHazard", tdscBlockInfo.getGeologicalHazard());
			}else{
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
	 * ��ӡ�����ֺ�������֪ͨ����Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintGeologicalNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* ��APPID�鿴��ͼ */
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
			//Ŀǰ����һ���ؿ�����ֻ��һ��tdscBlockPart
			if(tdscBlockPartList!=null && tdscBlockPartList.size()>0){
				tdscBlockPart = (TdscBlockPart)tdscBlockPartList.get(0);
			}			
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());
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
		
		tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(tdscBlockAppView.getBlockId());
		if(tdscBlockInfo!=null && tdscBlockInfo.getGeologicalHazard()!=null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//��д�˵����ֺ������ѣ����Ҵ���0
			String dzzhpgfUpper = MoneyUtils.NumToRMBStrWithJiao(Double.parseDouble(tdscBlockInfo.getGeologicalHazard() + ""));//�����ֺ������Ѵ�д
			geologicalHazard = NumberUtil.numberDisplay(tdscBlockInfo.getGeologicalHazard(), 2);
			
			request.setAttribute("dzzhpgfUpper", dzzhpgfUpper + "");
			request.setAttribute("geologicalHazard", geologicalHazard+"");
		
			List bankDicList = tdscBlockInfoService.queryGeologyAssessUintDicList();
			if(StringUtils.isNotBlank(tdscBlockInfo.getGeologyAssessUint())){
				if (bankDicList != null && bankDicList.size() > 0) {
					for (int i = 0; i < bankDicList.size(); i++) {
						TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
						if (tdscBlockInfo.getGeologyAssessUint().equals(dicBank.getDicCode())) {
							request.setAttribute("geologyAssessUint", dicBank.getDicValue());//����
							request.setAttribute("geologyAssessUintBankName", dicBank.getDicDescribe().split(",")[0]);//DIC_DESCRIBE�����ö��ŷָ��������ƺ������˺�
							request.setAttribute("geologyAssessUintBankNum", dicBank.getDicDescribe().split(",")[1]);//DIC_DESCRIBE�����ö��ŷָ��������ƺ������˺�
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
	 * ��ӡ�ɽ���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintIwebofficeTradeSuccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* ��APPID�鿴��ͼ */
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		String blockUnitName = "";
		if (tdscBlockAppView.getBlockId() != null & !"".equals(tdscBlockAppView.getBlockId())) {
			List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal totalArea = new BigDecimal(0.00);// �������
			BigDecimal crjkPrice = tdscBlockAppView.getResultPrice();// ���üۿ��
			TdscBlockPart tdscBlockPart = new TdscBlockPart();
			for (int j = 0; j < tdscBlockPartList.size(); j++) {
				tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
				if (tdscBlockPart.getBlockArea() != null)
					totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				if (tdscBlockPart.getBlockCode() != null) {
					if (j == 0) {
						blockUnitName = tdscBlockPart.getBlockCode();
					} else {
						blockUnitName += "��" + tdscBlockPart.getBlockCode();
					}
				}
			}
			// ����Ŀǰһ��ؾ�ֻ��һ��tdscBlockPart
			request.setAttribute("landUseType", tdscBlockPart.getLandUseType());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0 && (totalArea.compareTo(new BigDecimal("0.00")) == 1) && tdscBlockAppView.getTotalPrice() != null) {
				crjkPrice = tdscBlockAppView.getTotalPrice().divide(totalArea, 2, BigDecimal.ROUND_HALF_UP);
				blockUnitName = blockUnitName.substring(0, blockUnitName.length());
			}

			// ���üۿ�۴���TotalLandArea��,�ӵؿ�����ƴ�� ����UnitebBlockCode��
			tdscBlockAppView.setTotalLandArea(crjkPrice);
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
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
		request.setAttribute("transferMode", tdscBlockAppView.getTransferMode());

		return mapping.findForward("toPrintIwebofficeSuccessFirst");

	}

	/**
	 * ��ӡδ�ɽ���Ϣ
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
		// ͨ��appId���ػ�����Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("appId", appId);

		tdscBlockAppView.setBlockName(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		// ȡ�þ����˵�֤���ź�����
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

		// ͨ��appId��ѯTDSC_BIDDER_APP�������о������ص��ˣ�����list
		List tdscBidderAppList = tdscLocalTradeService.queryTdscBidderAppList(appId);

		// ȡ��δ�������б�
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
	 * ��ӡ��֤����ת����
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
		// ����APP_ID��ѯ�ؿ�ġ��ؿ��š�
		String blockCodes = (String) tdscBlockInfoService.tidyBlockCodeByBlockId(blockId);
		// ���ݾ������ʸ�֤���ź�appId�鿴��������Ϣ*/
		TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getBidderAppByAppIdCertNo(appId, certNo);
		// ��þ����˵����Ƽ���֤��������
		if (tdscBidderApp != null) {
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.tidyBidderByBidderId(tdscBidderApp.getBidderId());
			request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		}
		// ͨ��appId��ѯTDSC_BIDDER_APP�������о������ص���
		List tdscBidderAppList = (List) tdscBidderAppService.queryBidderAppList(appId);
		// ���������ʸ�֤���Ų鿴δ��������Ϣ
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
	 * �����б�ɹ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveInviteSuccessResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// ҵ��Id
		// ��ʱΪ���뽻�׿����
		String tenderNo = request.getParameter("tenderNo");// ������
		String invitePrice = request.getParameter("invitePrice");// �ɽ��۸�

		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// ��ǰʱ��
		// ��ѯ�ؿ���Ϣview
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// �������ź͵ؿ�ҵ��ID,ȷ��Ψһ��һ��������Ϣ
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
		// �����б���
		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo.setAppId(appId);// ��Ҫ����
		tdscTenderInfo.setTenderCertNo(tenderNo);// ��Ҫ����
		tdscTenderInfo.setResultPrice(new BigDecimal(invitePrice));
		tdscTenderInfo.setTenderNo(tenderNo);
		tdscTenderInfo.setTenderResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"���׳ɹ�
		// tdscTenderInfo.setTenderResultTime(tdscBlockAppView.getOpeningDate());
		// this.tdscLocalTradeService.saveTenderInfo(tdscTenderInfo);
		// ���ĵؿ齻�ױ�
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"���׳ɹ�
		tdscBlockTranApp.setResultDate(nowTime);
		tdscBlockTranApp.setResultPrice(new BigDecimal(invitePrice));
		tdscBlockTranApp.setResultCert(tdscBidderApp.getCertNo());

		tdscBlockTranApp.setResultName(resultName);
		// this.tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
		// ��TDSC_block_info����н��׽�����ݵļ�¼
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// ����block_id��ѯTdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// ���潻�׽����Ϣ
			tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
			tdscBlockInfo.setResultDate(nowTime);
			tdscBlockInfo.setResultPrice(new BigDecimal(invitePrice));
			tdscBlockInfo.setResultCert(tdscBidderApp.getCertNo());
			tdscBlockInfo.setResultName(resultName);
			tdscBlockInfo.setStatus("02");
		}
		// �����б���
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
	 * �����б�ʧ�ܽ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveInviteFailedResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// ҵ��Id
		String failedReason = request.getParameter("failedReason");// ����ԭ��
		String failedMemo = request.getParameter("failedMemo");// ���걸ע
		if (failedMemo == null) {
			failedMemo = "";
		}

		// ��ѯ�ؿ���Ϣview
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// �����б���
		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo.setAppId(appId);// ��Ҫ����
		tdscTenderInfo.setTenderFailedMemo(failedMemo);
		tdscTenderInfo.setTenderFailedReason(failedReason);
		// tdscTenderInfo.setTenderResultTime(tdscBlockAppView.getOpeningDate());
		tdscTenderInfo.setTenderResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"����ʧ��
		this.tdscLocalTradeService.saveTenderInfo(tdscTenderInfo);

		// ���ĵؿ齻�ױ�
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"����ʧ��
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		this.tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("vendueFailedReason", failedReason);
		request.setAttribute("vendueFailedMemo", failedMemo);
		return mapping.findForward("saveInviteFailed");
	}

	/**
	 * �����б����¼�봰��
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
	 * �����б���̼�¼
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
		// �б��Id
		String tenderId = this.tdscLocalTradeService.queryTenderIdByAppId(appId);
		// Ͷ����֤���
		String certNo = request.getParameter("tenderCert");
		// ������
		String tenderNo = request.getParameter("pingBiaoNo");
		// Ͷ��������
		String inviteName = request.getParameter("inviteName");
		// Ͷ��۸�
		String price = request.getParameter("price");
		BigDecimal priceShares = new BigDecimal(price);
		// �����÷�
		String businessScore = request.getParameter("businessScore");
		BigDecimal businessShares = new BigDecimal(businessScore);
		// ������÷�
		String techScore = request.getParameter("techScore");
		BigDecimal techShares = new BigDecimal(techScore);
		// �ۺϷ�
		String totalScore = request.getParameter("totalScore");
		BigDecimal docShares = new BigDecimal(totalScore);
		// Ͷ��ʱ��
		Timestamp openingDate = tdscBlockPlanTable.getOpeningDate();
		// ��ע
		String tenderMemo = request.getParameter("inviteMemo");
		// �б�����
		String inviteType = request.getParameter("inviteType");
		// logger.debug("inviteType========"+inviteType);
		// �������ӵĹ���
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
		// ��ѯ�Ƿ����б��¼,���������ԭ��¼���޸�
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
	 * �޸��б��¼
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
	 * ɾ��һ��Ͷ���¼
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
	 * ����δ�ɽ���ť
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
	 * �����Ƶؿ��Ƿ���й���¼��
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
		// ��ѯ�ؿ���Ϣview
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// ��ѯ���ƻ���Ϣ
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
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		// ���ظ��ص������Ĳ���
		pw.write(str);
		pw.close();
		return null;
	}

	/**
	 * ����۸��б����
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
	 * ���ƽ���
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
		// ��ѯ�ؿ���Ϣview
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String transferMode = tdscBlockAppView.getTransferMode();
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		try {
			this.appFlowService.saveOpnn(appId, transferMode, user);// ��������ΪappId�����÷�ʽ���û���Ϣ
		} catch (Exception e) {
			e.printStackTrace();
		}
		String forwardString = "vendue.do?method=queryBlockListForVendue&enterWay=1";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * �Թ���Ϊ��λ����������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endForVendue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �ȱ������
		LocalTradeForm localTradeForm = (LocalTradeForm) form;
		String[] cardNos = localTradeForm.getCardNos();// ������Ǻ��ƺ�
		String[] certNos = localTradeForm.getCertNos();// ������ǽ��׿���
		String noticeId = request.getParameter("noticeId");
		// ��ѯ����ϸ�ľ�������Ϣ�б�
		if (cardNos != null && cardNos.length > 0 && certNos != null && certNos.length > 0) {
			for (int j = 0; j < cardNos.length; j++) {
				String certNo = certNos[j];// ���׿���
				String cardNo = cardNos[j];// ���ƺ�

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

		// �������Ʊ�������
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
	 * �������¼����ý���
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
	 * �ж�һ��������һ���ؿ���ֳ����ۻ����Ƿ��ظ�ʹ��
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
		// �ж����ݿ���û�иÿ��ţ�
		if (!"".equals(tempCertNo) && tempCertNo != null) {
			if (!certNo.equals(tempCertNo)) {
				retResult = "77";
			}
		}
		// ���ظ��ص������Ĳ���
		pw.write(retResult);
		pw.close();
		return null;
	}

	public ActionForward checkHaoPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");
		String cardNo = request.getParameter("cardNo");// ԭ �ʸ�֤���� ���ִ� һ��ͨ��� ��ֵ
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
		// ���ظ��ص������Ĳ���
		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * У��������������Ƿ����
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
		// ����appId,certNo��ѯcertNo�Ƿ������Ψһ
		List tempList = (List) tdscBidderAppService.checkCertNoByAppId(appId, certNo);
		if (tempList != null && tempList.size() == 1) {
			retResult = "77";
		} else if (tempList.size() > 1) {
			retResult = "22";
		}
		// ���ظ��ص������Ĳ���
		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * �����б�δ�ɽ����
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
		// �ؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo.setAppId(appId);
		tdscTenderInfo.setTenderFailedReason(tenderFailedReason);
		tdscTenderInfo.setTenderFailedMemo(tenderFailedMemo);

		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"����ʧ��
		tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));
		// ����TdscBlockInfo��
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// ����block_id��ѯTdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// ���潻�׽����Ϣ
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
			// ����block_id��ѯTdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal transferArea = new BigDecimal(0);

			BigDecimal volumeRate = new BigDecimal(0);

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// �����
				BigDecimal planBuildingArea = new BigDecimal(0.00);// �����ؽ������
				TdscBlockPart tdscBlockPart = new TdscBlockPart();

				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					if (tdscBlockPart.getBlockArea() != null) {
						transferArea = tdscBlockPart.getBlockArea();
						// volumeRate = tdscBlockPart.getVolumeRate();

						// ʹ�ø��ֶΣ���Ź滮�������
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
	 * �鿴���
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
			// ����block_id��ѯTdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// �����
				BigDecimal planBuildingArea = new BigDecimal(0.00);// ���������
				TdscBlockPart tdscBlockPart = new TdscBlockPart();
				// ��������õؿ�Ϊ��ҵ�ؿ飨ϵͳ�аѹ滮�������Ϊ0�ĵؿ�Ĭ��Ϊ��ҵ�ؿ飩��
				// �ڹ���¼��ҳ��¼��۸�ʱ����ҳ���еġ��滮�������(�O)�����۸�Ϊ���������(�O)������ȡ�����������ֵ��
				// ��ҳ���еġ�¥��ؼ�(Ԫ���O)�����۸�Ϊ�����ص���(Ԫ���O)����ͬʱ���Զ��������ܼۣ���ҵ�ؿ�ļ��㹫ʽΪ�������*���ص��ۡ�
				// ע�⣺�����н�ҳ���еġ��滮�������(�O)�����ߡ��������(�O)����ֵ������totalLandArea�ֶ���
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
		// ����ҳ��
		// LocalTradeType�ֳ��������ͣ�1=�ֳ����汨��|2=���ƾ��ۣ�
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		return mapping.findForward("viewVendueProcess");

	}

	/**
	 * �鿴
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
			// ����block_id��ѯTdscBlockPart
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());

			BigDecimal transferArea = new BigDecimal(0);
			String volumeRate = "";

			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				BigDecimal totalArea = new BigDecimal(0.00);// �����
				BigDecimal planBuildingArea = new BigDecimal(0.00);// ���������
				TdscBlockPart tdscBlockPart = new TdscBlockPart();

				// ��������õؿ�Ϊ��ҵ�ؿ飨ϵͳ�аѹ滮�������Ϊ0�ĵؿ�Ĭ��Ϊ��ҵ�ؿ飩��
				// �ڹ���¼��ҳ��¼��۸�ʱ����ҳ���еġ��滮�������(�O)�����۸�Ϊ���������(�O)������ȡ�����������ֵ��
				// ��ҳ���еġ�¥��ؼ�(Ԫ���O)�����۸�Ϊ�����ص���(Ԫ���O)����ͬʱ���Զ��������ܼۣ���ҵ�ؿ�ļ��㹫ʽΪ�������*���ص��ۡ�
				// ע�⣺�����н�ҳ���еġ��滮�������(�O)�����ߡ��������(�O)����ֵ������totalLandArea�ֶ���
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
	 * �����ֳ����۹�����Ϣ
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
		/** ����˳�� */
		String[] numbers = (String[]) localTradeForm.getNumbers();
		/** ����list */
		String[] haoPais = (String[]) localTradeForm.getHaoPais();
		/** �������� */
		String[] addPrices = (String[]) localTradeForm.getAddPrices();
		/** �ܼ� */
		String[] totalPrices = (String[]) localTradeForm.getTotalPrices();
		List tdscAuctionAppList = new ArrayList();
		if (addPrices == null) {

		} else {
			// ���ñ���ʱ��
			Timestamp auctionDate = new Timestamp(System.currentTimeMillis());
			for (int i = 0; i < addPrices.length; i++) {
				if (!"".equals(addPrices[i]) && addPrices[i] != null) {
					TdscAuctionApp tdscAuctionApp = new TdscAuctionApp();
					// ����APP_ID
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
