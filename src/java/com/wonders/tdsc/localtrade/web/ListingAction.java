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
	 * �ؿ������Ϣ
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
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
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
							tdscBlockAppView.setRangeEast(tdscListingInfo.getListCert());// �ʸ�֤����
						}
						if (tdscListingInfo.getCurrPrice() != null && !"".equals(tdscListingInfo.getCurrPrice())) {
							tdscBlockAppView.setAddPriceRange(tdscListingInfo.getCurrPrice());// ��ǰ���Ƽ۸�
						}
						if (tdscListingInfo.getListDate() != null && !"".equals(tdscListingInfo.getListDate())) {
							tdscBlockAppView.setAuctionDate(tdscListingInfo.getListDate());// ����ʱ��
						}
						if (tdscListingInfo.getCurrRound() != null && !"".equals(tdscListingInfo.getCurrRound())) {
							tdscBlockAppView.setOpeningMeetingNo(tdscListingInfo.getCurrRound());// �ִ�
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
	 * �ؿ������Ϣ
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
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
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
							tdscBlockAppView.setRangeEast(tdscListingInfo.getListCert());// �ʸ�֤����
						}
						if (tdscListingInfo.getCurrPrice() != null && !"".equals(tdscListingInfo.getCurrPrice())) {
							tdscBlockAppView.setAddPriceRange(tdscListingInfo.getCurrPrice());// ��ǰ���Ƽ۸�
						}
						if (tdscListingInfo.getListDate() != null && !"".equals(tdscListingInfo.getListDate())) {
							tdscBlockAppView.setAuctionDate(tdscListingInfo.getListDate());// ����ʱ��
						}
						if (tdscListingInfo.getCurrRound() != null && !"".equals(tdscListingInfo.getCurrRound())) {
							tdscBlockAppView.setOpeningMeetingNo(tdscListingInfo.getCurrRound());// �ִ�
						}
					}
				}
				
				// �Զ��������� add by xys
				//Long nowTime = new Long(DateConvertor.getCurrentDateWithTimeZone());
				//Long listEndTime = new Long(DateUtil.date2String(tdscBlockAppView.getListEndDate(), "yyyyMMddHHmmss"));				
				//��ǰʱ���Ѿ�������ƽ�ֹʱ�䣬���Ҹõؿ����ֳ����ף�ifOnLine=0���ؿ飬�����Զ����������ƶ����̵Ĵ���
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
		// appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_NO_SENCE); // ��Ҫ�ö������ID��
		// appFlow.setTransferMode(transferMode); // ���÷�ʽ
		// appFlow.setUser(user); // �û���Ϣ
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
		// ������÷�ʽ�ǹ��Ƶģ�����
		try {
			this.appFlowService.saveOpnn(appFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * �鿴�ؿ���Ƽ�¼
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward listingInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �ؿ���Ϣ��ѯ
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
		// ���Ƽ�¼�鿴
		// List listingInfoList = this.tdscLocalTradeService.queryListingRecord(appId);
		// request.setAttribute("listingInfoList", listingInfoList);

		// lz+ 20090708
		if (null != appId) {
			// ����appId��listingId;
			String listingId = this.tdscLocalTradeService.queryListingId(appId);
			if (null != listingId) {
				List retList = new ArrayList();
				// ����listingId������б�;
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
	 * ת���ֳ�
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
		// ����Ϣ�����tdsc_listing_app_info��
		// ����vendueAction�еķ���,ת��������¼����
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
	 * ���ƽ���
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
		// ����Ϣ�����tdsc_listing_app_info��
		// �޸�TDSC_BLOCK_TRAN_APP
		// ���ص������б�

		// ��ҳ��ӵ�һ��ҵ��ID,����ҵ��ID�õ�ListingId,�����Ӧ���ƽ��
		String appId = request.getParameter("appId");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String transferMode = request.getParameter("transferMode");
		String listingId = this.tdscLocalTradeService.queryListingId(appId);

		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(appId); // appId
		appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_NO_SENCE); // ��Ҫ�ö������ID��
		appFlow.setTransferMode(transferMode); // ���÷�ʽ
		appFlow.setUser(user); // �û���Ϣ

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
			tdscBlockInfo.setStatus("02");//(00-δ����;01-������;02-���׽���)
			tdscBlockInfo.setTranResult("01");//���׽��(01=���׳ɹ�|02=����ʧ��|03=����ȡ��|04=������ֹ)
			tdscBlockInfoService.updateTdscBlockInfo(tdscBlockInfo);
			
			TdscBlockTranApp tdscBlockTranApp = tdscLocalTradeService.getTdscBlockTranApp(appId);			
			tdscBlockTranApp.setTranResult("01");// ���׽��  00δ���� 01 ���׳ɹ���02 ����ʧ�ܣ����꣩��04 ��ֹ���ף�
			tdscLocalTradeService.updateTdscBlockTranApp(tdscBlockTranApp);
			
			this.appFlowService.saveOpnn(appId, transferMode, user);// ��������ΪappId�����÷�ʽ���û���Ϣ
		} catch (Exception e) {
			e.printStackTrace();
		}

		// String forwardString = "listing.do?method=queryBlockListForListing&appId="+ appId + "&enterWay=3";
		String forwardString = "vendue.do?method=queryBlockListForResult&appId=" + appId + "&enterWay=3";

		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * ���ɳɽ���ť
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
		/* �ɽ��۸� */
		String venduePrice = request.getParameter("venduePrice");
		String appId = request.getParameter("appId");
		String resultCert = request.getParameter("certNo");
		request.setAttribute("resultCert", resultCert);
		/* ��APPID�鿴��ͼ */
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		/* ��APPID�鿴���ƻ���Ϣ,���õ������˵��ʸ�֤���� */
		TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		TdscBidderApp tdscBidderApp = null;
		if (null != tdscListingInfo) {
			/* ���������ʸ�֤���Ų鿴��������Ϣ,���õ��ɽ��˵��ʸ�֤���� */
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

		// ��������Ϣ

		// TdscListingInfo tdscListingInfo=tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("displayOver", displayOver);
		request.setAttribute("displayStatus", displayStatus);
		request.setAttribute("appId", appId);
		request.setAttribute("venduePrice", venduePrice);
		return mapping.findForward("displayPrintButton");
	}

	/**
	 * ����δ�ɽ����
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
	 * ��ӡ�ܷⱨ�۵�
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

		// ��ѯ���ƻ���Ϣ
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
	 * ��ӡ���Ʊ��ۼ�¼
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

		// ��ѯ������ʷ��¼��Ϣ
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
	 * һ��һ����
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
		appFlow.setResultId(FlowConstants.FLOW_LISTING_RESULT_SENCE_PRINT); // ��Ҫ�ö������ID��
		appFlow.setTransferMode(transferMode); // ���÷�ʽ
		appFlow.setUser(user); // �û���Ϣ
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
	 * ������Ƴɹ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addListingResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// ҵ��Id
		String cardNo = request.getParameter("cardNo");// ���ۺ��ƻ�һ��ͨ���
		String venduePrice = request.getParameter("venduePrice");// �ɽ��۸�
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// ��ǰʱ��

		// ��ѯ�ؿ���Ϣview
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// ����ɽ�,���þ��ۺ��ƻ��ʸ�֤���ź͵ؿ�ҵ��ID,ȷ��Ψһ��һ��������Ϣ
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
		// ��ѯ���ƻ���Ϣ
		TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(appId);
		// ���Ĺ��ƻ���Ϣ
		if (null != tdscListingInfo && null != tdscBidderApp) {
			// ����ת���ֳ�����,������,node=17
			if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING_SENCE) && tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_CHANGE)) {
				tdscListingInfo.setResultCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscListingInfo.setListResult("01");// "01"���ƽ�����ת���ֳ�����
				tdscListingInfo.setListResultDate(nowTime);
				tdscListingInfo.setSceneResult("01");// "01"�ֳ����۳ɹ�
				tdscListingInfo.setSceneResultDate(nowTime);
				tdscListingInfo.setResultNo(cardNo);
				tdscListingInfo.setYktXh(tdscBidderApp.getYktBh());
			}
			// һ��һ����
			if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING_SENCE) && tdscBlockAppView.getLocalTradeType().equals(GlobalConstants.DIC_SCENE_TYPE_NO_CHANGE)) {
				tdscListingInfo.setResultCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				// tdscListingInfo.setResultNo(cardNo);
				tdscListingInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscListingInfo.setYktXh(tdscBidderApp.getYktXh());
				tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_SCENE);// "01"���ƽ�����ת���ֳ�����
				tdscListingInfo.setListResultDate(nowTime);
				tdscListingInfo.setSceneResult(GlobalConstants.DIC_SCENE_SUCCESS);// "01"�ֳ����۳ɹ�
				tdscListingInfo.setSceneResultDate(nowTime);
				tdscListingInfo.setYktXh(tdscBidderApp.getYktBh());
			}
			// ����û��ת���ֳ�,node=16
			if (tdscBlockAppView.getNodeId().equals(FlowConstants.FLOW_NODE_LISTING)) {
				tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_SUCCESS);// "02"���Ƴɹ�
				tdscListingInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscListingInfo.setListResultDate(nowTime);
				tdscListingInfo.setResultCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
			}
			// this.tdscLocalTradeService.modifyListingInfo(tdscListingInfo);
		}

		// ���ĵؿ齻�ױ�
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		// �����ܼ۲�����TdscBlockTranApp
		TdscBlockPart tdscBlockPart = new TdscBlockPart();
		List tdscBlockPartList = new ArrayList();
		BigDecimal totalPrice = new BigDecimal(venduePrice);// ���üۿ�ɽ��ܼ�

		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// ����block_id��ѯTdscBlockPart
			BigDecimal totalArea = new BigDecimal(0.00);// �����
			tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockTranApp.getBlockId());
			if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
				for (int i = 0; i < tdscBlockPartList.size(); i++) {
					tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
					// ���滮���������Ϊ�գ����Ƿǹ�ҵ�ؿ飬���üۿ�ɽ��ܼ�Ϊ�ɽ�¥��ؼ���滮��������ĳ˻���
					// ���滮�������Ϊ�գ����ǹ�ҵ�ؿ飬���üۿ�ɽ��ܼ�Ϊ�ɽ�¥��ؼ�����������ĳ˻���
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
			tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);// "01"���׳ɹ�
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
			// ��TDSC_block_info����н��׽�����ݵļ�¼
			TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
			if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
				// ����block_id��ѯTdscBlockInfo
				tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
				// ���潻�׽����Ϣ
				tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
				tdscBlockInfo.setResultDate(nowTime);
				tdscBlockInfo.setResultPrice(new BigDecimal(venduePrice));
				tdscBlockInfo.setResultCert(certNo);
				tdscBlockInfo.setResultName(resultName);
				tdscBlockInfo.setStatus("02");
			}
			// ������ƽ��׽��
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
	 * �������ʧ�ܽ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addListingFailedResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");// ҵ��Id
		String vendueFailedReason = request.getParameter("vendueFailedReason");// δ�ɽ�ԭ��
		String vendueFailedMemo = request.getParameter("vendueFailedMemo");// δ�ɽ���ע
		if (vendueFailedMemo == null) {
			vendueFailedMemo = "";
		}
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// ��ǰʱ��
		// ��ѯ�ؿ���Ϣview
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// ��Ӧ���ƻ���Ϣ
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		if (listingId != null) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
		}
		// ��APP_ID���ø�tdscListingInfo
		tdscListingInfo.setAppId(appId);

		// ����ת���ֳ�����,node=17
		String nodeId = tdscBlockAppView.getNodeId();
		if (nodeId.equals(FlowConstants.FLOW_NODE_LISTING_SENCE)) {
			if (listingId != null) {
				tdscListingInfo.setListingId(listingId);
			}
			tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_SCENE);// "01"���ƽ�����ת���ֳ�����
			tdscListingInfo.setListResultDate(nowTime);
			tdscListingInfo.setSceneResult(GlobalConstants.DIC_SCENE_FAIL);// "02"�ֳ�����ʧ��
			tdscListingInfo.setListFailReason(vendueFailedReason);
			tdscListingInfo.setListFailMeno(vendueFailedMemo);
			tdscListingInfo.setSceneResultDate(nowTime);

			// if(listingId != null){
			// this.tdscLocalTradeService.modifyListingInfo(tdscListingInfo);
			// }else{
			// this.tdscLocalTradeService.saveListingInfo(tdscListingInfo);
			// }

		}
		// ����û��ת���ֳ�,node=16
		if (nodeId.equals(FlowConstants.FLOW_NODE_LISTING)) {
			tdscListingInfo.setListingId(listingId);
			tdscListingInfo.setListResult(GlobalConstants.DIC_LISTING_FAIL);// "03"����ʧ��
			tdscListingInfo.setListFailReason(vendueFailedReason);
			tdscListingInfo.setListFailMeno(vendueFailedMemo);
			tdscListingInfo.setListResultDate(nowTime);
			// this.tdscLocalTradeService.modifyListingInfo(tdscListingInfo);
		}
		// ���ĵؿ齻�ױ�
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_FAIL);// "02"����ʧ��
		tdscBlockTranApp.setResultDate(nowTime);
		TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
		if (tdscBlockTranApp.getBlockId() != null && !tdscBlockTranApp.getBlockId().equals("")) {
			// ����block_id��ѯTdscBlockInfo
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockTranApp.getBlockId());
			// ���潻�׽����Ϣ
			tdscBlockInfo.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_SUCCESS);
			tdscBlockInfo.setResultDate(nowTime);
			tdscBlockInfo.setStatus("02");
		}
		// ������ƽ��׽��
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
