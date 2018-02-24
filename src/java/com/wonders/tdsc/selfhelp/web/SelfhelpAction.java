package com.wonders.tdsc.selfhelp.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bidder.service.TdscBidderFundService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.selfhelp.service.SelfhelpService;
import com.wonders.tdsc.smartcardlogin.service.SmartCardLoginService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class SelfhelpAction extends DispatchAction {
	/** ��־ */
	protected static Log				logger	= LogFactory.getLog(SelfhelpAction.class);

	private SelfhelpService				selfhelpService;

	private CommonQueryService			commonQueryService;

	private TdscLocalTradeService		tdscLocalTradeService;

	private TdscBidderFundService		tdscBidderFundService;

	private TdscBidderAppService		tdscBidderAppService;

	private SmartCardLoginService		smartCardLoginService;

	private CommonFlowService			commonFlowService;

	private TdscScheduletableService	tdscScheduletableService;

	private TdscBlockInfoService		tdscBlockInfoService;

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setSelfhelpService(SelfhelpService selfhelpService) {
		this.selfhelpService = selfhelpService;
	}

	public void setTdscLocalTradeService(TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public void setTdscBidderFundService(TdscBidderFundService tdscBidderFundService) {
		this.tdscBidderFundService = tdscBidderFundService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setSmartCardLoginService(SmartCardLoginService smartCardLoginService) {
		this.smartCardLoginService = smartCardLoginService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintCurrentPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1.����Ǹõؿ�������ˣ�����ʾ��ӡ��ִ��ť�����������������
		// ��1����������ʹ�����ؽ��׿��������۹�����ִҳ���ӡ���������۽�����ʱ��
		// ��2����������û��ʹ�����ؽ��׿��������۹�����ִҳ���ӡ�õؿ�ĳ�ʼ���Ƽ۸��������ƿ�ʼʱ��
		// 2.����Ǹõؿ�ľ����ˣ��������������
		// ��1���þ�����ʹ�����ؽ��׿��������۹�������ʾ��ӡ��ִ��ť����ִҳ���ӡ���������۽�����ʱ��
		// ��2����������û��ʹ�����ؽ��׿��������۹���������ʾ��ӡ��ִ��ť

		String bidderId = request.getParameter("bidderId") + "";
		String[] ids = bidderId.split(",");

		String ids1 = "";
		String ids2 = "";
		if (ids != null && ids.length == 2) {
			ids1 = ids[0];
			ids2 = ids[1];
		}

		List bidderPersonList = null;
		TdscBidderPersonApp bidderPersonApp = null;
		TdscListingApp tdscListingApp = new TdscListingApp();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();

		TdscBidderApp tdscBidderApp = tdscBidderAppService.queryBidderAppInfo(ids1);

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(ids2);
		tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// ��ѯ������û���������Ʊ��۹�
		List tdscListingAppList = tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(tdscBidderApp.getYktBh(), ids2);

		if (tdscBidderApp != null) {
			bidderPersonList = tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
			if (bidderPersonList != null && bidderPersonList.size() > 0) {
				bidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(0);
			}

			if ("1".equals(tdscBidderApp.getIsPurposePerson())) {// ������
				if (bidderPersonApp.getPurposeAppId().indexOf(ids2) >= 0) {// ������ѡ��������ؿ���в���
					// ids2.equals(bidderPersonApp.getPurposeAppId())
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// �����˶�����ؿ��������Ʊ��۹�
						tdscListingApp = (TdscListingApp) tdscListingAppList.get(0);
					} else {
						if (tdscBlockAppView != null) {
							tdscListingApp.setListPrice(tdscBlockAppView.getInitPrice());
							tdscListingApp.setListDate(tdscBlockAppView.getListStartDate());
						}
					}
				} else {// �����˲���ѡ������ؿ���в���
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// �����˶Ըõؿ��������Ʊ��۹�
						tdscListingApp = (TdscListingApp) tdscListingAppList.get(0);
					}
				}
			} else {// ���������ˣ�������������ʱ���������ľ�����
				if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// �����˶Ըõؿ��������Ʊ��۹�
					tdscListingApp = (TdscListingApp) tdscListingAppList.get(0);
				} else {
					request.setAttribute("errorMsg", "δ���ۣ����ܴ�ӡ��ִ��");
				}
			}
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscListingApp", tdscListingApp);

		return mapping.findForward("printInitPriceNew");
	}

	/**
	 * �����������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addListingPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ��ȡҳ���о����˵ı�ź������ļ۸�,ҵ��Id
		String appId = request.getParameter("appId");
		// �����˽��׿����� оƬ�ţ�ȫ��Ψһ
		String yktBh = request.getParameter("yktXh");
		String listingPrice = request.getParameter("listingPrice");
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		TdscListingApp tdscListingApp = new TdscListingApp();
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		// ��������Ϣ��ѯ
		tdscBidderApp = this.tdscBidderAppService.getTdscBidderAppByYktBh(yktBh);
		String bidderId = tdscBidderApp.getBidderId();
		tdscBidderApp = this.tdscBidderAppService.queryBidderAppInfo(bidderId);
		TdscBlockTranApp tdscBlockTranApp = this.tdscBidderFundService.queryOneBlockTran(appId);

		tdscListingApp.setListPrice(new BigDecimal(listingPrice));
		tdscListingApp.setListCert(tdscBidderApp.getCertNo());
		tdscListingApp.setPriceType("1");
		tdscListingApp.setAppId(appId);
		tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
		// ����appId ��ѯ listingId
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// ��ҳ�洫����ʼ�۸�,���ӷ���,��ǰ�۸�,��ǰ����;
		if (listingPrice != null && !"".equals(listingPrice)) {
			Timestamp nowTime = new Timestamp(System.currentTimeMillis());
			// ������ƻ�Id������,����һ���¼�¼
			if (!StringUtils.isNotEmpty(listingId)) {
				tdscListingInfo.setAppId(appId);
				tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
				tdscListingInfo.setListDate(nowTime);
				if (tdscBlockTranApp.getIsPurposeBlock() != null && "1".equals(tdscBlockTranApp.getIsPurposeBlock())) {// ��������Ƴ��õؿ飬�ڹ��ƻ��Ϲ��Ʊ��۳ɹ�����һ����tdsc_listing_info������¼ʱ���������������Ϊ2����1�����������˹��Ʊ���
					tdscListingInfo.setCurrRound(new BigDecimal(2));
				} else if (tdscBlockTranApp.getIsPurposeBlock() != null && "0".equals(tdscBlockTranApp.getIsPurposeBlock())) {// ��������Ƴ��õؿ飬�ڹ��ƻ��Ϲ��Ʊ��۳ɹ�����һ����tdsc_listing_info������¼ʱ���������������Ϊ1
					tdscListingInfo.setCurrRound(new BigDecimal(1));
				}
				tdscListingInfo.setYktXh(yktBh);

				this.tdscLocalTradeService.saveListingInfoAndApp(tdscListingInfo, tdscListingApp);
			} else {
				// ������ԭ��¼���޸�
				tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);

				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
				tdscListingInfo.setListDate(nowTime);
				tdscListingInfo.setYktXh(yktBh);
				if (tdscListingInfo.getCurrRound() != null) {
					int round = tdscListingInfo.getCurrRound().intValue();
					round++;
					tdscListingInfo.setCurrRound(new BigDecimal(round));
				} else {
					tdscListingInfo.setCurrRound(new BigDecimal(1));
				}
				this.tdscLocalTradeService.modifyListingInfoAndApp(tdscListingInfo, tdscListingApp);
			}
		}

		return printOfferReply(mapping, appId, tdscBidderApp, request, response);
	}

	/**
	 * ��ҳ�洫�������Ӧ��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddListingPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long begin = System.currentTimeMillis();
		String appId = request.getParameter("appId");
		String yktBh = request.getParameter("yktBh");
		String cardNo = request.getParameter("cardNo") + "";
		String password = request.getParameter("password") + "";
		// ���ƻ���Ϣ��ѯ
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// �ؿ���Ϣ��ѯ
		TdscBlockTranApp tdscBlockTranApp = this.tdscBidderFundService.queryOneBlockTran(appId);
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		if (StringUtils.isNotEmpty(listingId)) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// ��������Ϣ��ѯ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) this.tdscBidderAppService.getBidderAppByYktBh(yktBh);
		// ��ǰ���Ƽ۸�
		if (listingId == null) {
			request.setAttribute("currPrice", tdscBlockTranApp.getInitPrice());
			request.setAttribute("currRound", null);
		} else {
			request.setAttribute("currPrice", tdscListingInfo.getCurrPrice());
			// ��ǰ��������
			request.setAttribute("currRound", tdscListingInfo.getCurrRound());
		}
		request.setAttribute("addPriceRange", tdscBlockTranApp.getAddPriceRange());
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		// request.setAttribute("cardNo", cardNo);
		// request.setAttribute("password",password);
		long end = System.currentTimeMillis();
		logger.debug("====== ��ҳ�洫�������Ӧ��Ϣ������ʱ" + (end - begin) + "����======");
		return mapping.findForward("toSelfhelp");
	}

	/**
	 * �鿴��ʷ���Ƽ�¼
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String appId = request.getParameter("appId");
		String yktXh = request.getParameter("yktXh");
		// ���� TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ���� condition ��ѯTdscBlockAppView
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		// ���bidderId
		String bidderId = this.tdscBidderAppService.queryBidderId(appId, yktXh);
		if (bidderId == null) {
			bidderId = "666";
		}
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		tdscBidderApp = this.tdscBidderAppService.queryBidderAppInfo(bidderId);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("yktXh", yktXh);

		List listingAppList = this.tdscLocalTradeService.queryListingAppByYktXh(yktXh);
		request.setAttribute("listingAppList", listingAppList);

		return mapping.findForward("showHistory");
	}

	/**
	 * ��ӡ�����ʸ�֤��/δ��Χ�ʸ�֤��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintCompetenceSuccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// long begin = System.currentTimeMillis();
		// ��ȡappId,YKI_BH,YKT_MM
		String appId = request.getParameter("appId");
		String acceptNo = request.getParameter("acceptNo");
		// ���þ����˲�ѯ�� TdscBidderCondition
		TdscBidderCondition bidderCondition = new TdscBidderCondition();
		bidderCondition.setAppId(appId);
		bidderCondition.setAcceptNo(acceptNo);
		// ��þ����˵�����
		String bidderName = selfhelpService.getBidderName(bidderCondition);
		request.setAttribute("bidderName", bidderName);
		// ��þ�������Ϣ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) selfhelpService.getTdscBidderApp(bidderCondition);
		if (tdscBidderApp != null && tdscBidderApp.getBidderId() != null) {
			request.setAttribute("bidderId", StringUtils.trimToEmpty(tdscBidderApp.getBidderId()));
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);

		// ���õؿ���Ϣ��ѯ�� TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ͨ��appId��ѯ���ػ�����Ϣ
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		String gphStartTime = "";
		String gphEndTime = "";
		String pmhEndTime = "";
		String pmhLoc = "";
		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null) {
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));

			gphStartTime = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getListStartDate(), "yyyy��MM��dd��HHʱmm��"));
			gphEndTime = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getListEndDate(), "yyyy��MM��dd��HHʱmm��"));
			pmhEndTime = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getAuctionDate(), "yyyy��MM��dd��HHʱmm��"));
			pmhLoc = StringUtils.trimToEmpty(tdscBlockAppView.getAuctionLoc());
		}

		Date rightNow = new Date();
		String rightNowString = StringUtils.trimToEmpty(DateUtil.date2String(rightNow, "yyyy��MM��dd��"));

		request.setAttribute("gphStartTime", gphStartTime);
		request.setAttribute("gphEndTime", gphEndTime);
		request.setAttribute("pmhEndTime", pmhEndTime);
		request.setAttribute("pmhLoc", pmhLoc);
		request.setAttribute("rightNowString", rightNowString);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		// ��á����ý����ʾ����ʱ�䣬��Ϊ�����ʸ�֤�����Ч�������ڣ�
		TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findPlanTableInfo(appId);
		if (tdscBlockPlanTable != null) {
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		}
		// long end = System.currentTimeMillis();
		// logger.debug("======��ӡ�����ʸ�֤�飬����ʱ" + (end - begin) + "����======");
		// if(tdscBlockAppView.getTransferMode()!=null&&"3103".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toPMPrintCompetenceSuccess");
		// }else if(tdscBlockAppView.getTransferMode()!=null&&"3104".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toGPPrintCompetenceSuccess");
		// }else
		// return mapping.findForward("toPrintCompetenceSuccess");

		// iweb��ӡ
		// if(tdscBlockAppView.getTransferMode()!=null&&"3103".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toIwebPMPrintCompetenceSuccess");
		// }else if(tdscBlockAppView.getTransferMode()!=null&&"3104".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toIwebGPPrintCompetenceSuccess");
		// }else
		return mapping.findForward("toPrintCompetenceSuccess");
	}

	public ActionForward toPrintCompetenceFailed(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ��ȡappId,YKI_BH,YKT_MM
		String appId = request.getParameter("appId");
		String acceptNo = request.getParameter("password");

		// String reviewOpnn = StringUtil.ISO88591toGBK(selfhelpService.getReviewOpnn(yktBh));

		TdscBidderCondition bidderCondition = new TdscBidderCondition();
		bidderCondition.setAppId(appId);
		bidderCondition.setAcceptNo(acceptNo);

		TdscBidderApp tdscBidderApp = (TdscBidderApp) selfhelpService.getTdscBidderApp(bidderCondition);
		String bidderName = selfhelpService.getBidderName(bidderCondition);

		request.setAttribute("bidderName", bidderName);

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		// ͨ��appId��ѯ���ػ�����Ϣ
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null) {
			// ����ؿ���
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
		}
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("toPrintCompetenceFailed");
	}

	/**
	 * ���Ŵ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showLoginToLp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("cardNo", request.getParameter("cardNo"));
		request.setAttribute("yktXh", request.getParameter("yktXh"));

		String password = tdscBidderAppService.getPasswordByCardNoAndYktXh(request.getParameter("cardNo"), request.getParameter("yktXh"));
		request.setAttribute("kaPwd", password);

		return mapping.findForward("listingindex");
	}

	/**
	 * ˢ��ҳ��У��ÿ��Ƿ���Ч
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");

		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "";
		String count = "";
		String yktXh = "";
		List tempList = (List) tdscBidderAppService.checkIfUsedYktBh(cardNo);
		if (tempList.size() == 0) {
			count = "0";
		}
		if (tempList.size() == 1) {
			count = "1";
			// ��ý��׿��ı��
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.getBidderAppByYktBh(cardNo);
			if (tdscBidderApp != null) {
				yktXh = tdscBidderApp.getYktXh() + ",";
			}

			// �����Ӧ�Ĺ��ƽ�ֹʱ���ѵ�����ʾ�����ƽ�ֹʱ���ѵ���
			if (tdscBidderApp != null) {

				TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
				cond.setNoticeId(tdscBidderApp.getNoticeId());

				List appViews = commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);
				TdscBlockAppView appView = (TdscBlockAppView) appViews.get(0);

				String endTime = DateUtil.date2String(appView.getListEndDate(), "yyyyMMddHHmmss");
				String nowTime = DateUtil.date2String(new java.util.Date(), "yyyyMMddHHmmss");

				Long f = new Long(endTime);
				Long n = new Long(nowTime);
				if (f.longValue() < n.longValue()) {
					count = "3";
				}
			}
		}
		if (tempList.size() > 1) {
			List tdscBidderAppList = (List) tdscBidderAppService.getBidderAppListByYktBh(cardNo);
			if (tdscBidderAppList != null && !"".equals(tdscBidderAppList)) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(0);
				if (tdscBidderApp != null) {
					yktXh = tdscBidderApp.getYktXh() + ",";
				}
			}
			count = "2";
		}

		retString = count + "," + cardNo + "," + yktXh;

		pw.write(retString);
		pw.close();
		return null;
	}

	/**
	 * ���Ƶ�½
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardLogin_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �����û����ź�����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "";
		// ����ҳ�洫��Ľ��׿����ź�����
		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		// ���ݽ��׿���Ϣ����ѯ
		TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo, password);

		String appId = "";
		String yktXh = "";
		String checkMsg = "false";
		if (tdscBidderApp != null) {
			checkMsg = "true";
			appId = tdscBidderApp.getAppId();
			yktXh = tdscBidderApp.getYktXh();
		}
		retString += checkMsg + ",";
		retString += appId + ",";
		retString += yktXh + ",";
		String nodeTime = "false";
		TdscAppNodeStat appNodeStat = this.commonFlowService.findAppNodeStat(appId, FlowConstants.FLOW_NODE_LISTING);
		if (appNodeStat != null) {
			if (appNodeStat.getNodeStat().equals(FlowConstants.STAT_ACTIVE)) {
				nodeTime = "true";
			} else if (appNodeStat.getNodeStat().equals(FlowConstants.STAT_END) || appNodeStat.getNodeStat().equals(FlowConstants.STAT_TERMINATE)) {
				nodeTime = "end";
			} else if (appNodeStat.getNodeStat().equals(FlowConstants.STAT_INIT)) {
				nodeTime = "init";
			}
		}
		retString += nodeTime + ",";
		String review = "";
		if (tdscBidderApp != null) {
			review = tdscBidderApp.getReviewResult();
		}

		if (review == null) {
			review = "";
		}
		String reviewStatus = "false";
		if (review.equals("0")) {
			reviewStatus = "notPass";
		} else if (review.equals("1")) {
			reviewStatus = "true";
		}
		retString += reviewStatus + ",";

		pw.write(retString);
		pw.close();
		return null;
	}

	/**
	 * �������� �ؿ飬�û����� һ���Զ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �����û����ź�����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "";
		// ����ҳ�洫��Ľ��׿�����,����,�ؿ�appId
		// String cardNo = request.getParameter("cardNo");
		// String password = request.getParameter("password");
		String tmpBidderId = request.getParameter("bidderId");
		String[] tmpTtt = tmpBidderId.split(",");
		String bidderId = tmpTtt[0];
		String appId = tmpTtt[1];
		if (bidderId != null) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
			// TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo, password);
			String yktXh = "";
			// String appId = "";
			String checkMsg = "false";
			if (tdscBidderApp != null) {
				checkMsg = "true";
				yktXh = tdscBidderApp.getYktXh();
				// appId = tdscBidderApp.getAppId();
			}
			retString += checkMsg + ",";
			retString += appId + ",";
			retString += yktXh + ",";
			String nodeTime = "false";

			// ����app_id ��ѯ����� plan_id
			// ��ѯ plan ���õ�ʱ������ж��Ƿ���ƽ���
			TdscBlockTranApp tranApp = tdscLocalTradeService.getTdscBlockTranApp(appId);
			TdscBlockPlanTable planApp = tdscScheduletableService.findPlanTableByPlanId(tranApp.getPlanId());

			String listEndDate = DateUtil.timestamp2String(planApp.getListEndDate(), "yyyyMMddhhmmss");
			// 20110429110000
			long gpEndTime = Long.parseLong(listEndDate);
			long compTime = gpEndTime - 10000;

			// 20110503145532
			String currDate = DateConvertor.getCurrentDateWithTimeZone();
			long currTime = Long.parseLong(currDate);
			if (currTime > compTime) {
				nodeTime = "end";
			} else {
				nodeTime = "true";
			}

			retString += nodeTime + ",";

			String reviewStatus = "true";

			retString += reviewStatus + ",";
		}

		pw.write(retString);
		pw.close();
		return null;
	}

	/**
	 * ����cardNo�ж���������ҳ�������һ��ͨ���ţ�1���õؿ���÷�ʽ�ǲ��ǹ��� 2���õؿ��ǲ�����������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkCardNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retResult = "00";
		List cardNoList = (List) tdscBidderAppService.checkIfUsedYktBh(cardNo);
		// �ж����ݿ���û�иÿ��ţ�
		// û��ʱ��
		if (cardNoList == null || cardNoList.size() == 0) {
			retResult = "01";
		}
		if (cardNoList.size() >= 1) {
			// ���appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp) cardNoList.get(0);
			if (password != null && tdscBidderApp.getYktMm() != null) {
				if (password.equals(tdscBidderApp.getYktMm())) {
					String appId = (String) tdscBidderApp.getAppId();
					String yktXh = (String) tdscBidderApp.getYktXh();
					// ����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
					TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
					tdscBaseQueryCondition.setAppId(appId.substring(0, 32));
					TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
					// ���÷�ʽΪ�����ơ�ʱ
					if (GlobalConstants.DIC_TRANSFER_LISTING.equals(tdscBlockAppView.getTransferMode())) {
						retResult = "99" + appId.substring(0, 32);
						if (yktXh != null) {
							retResult += yktXh;
						}
					} else {
						// ���÷�ʽΪ��������ʱ
						retResult = "02";
					}
					// �������
				} else {
					retResult = "77";
				}
			}
		}
		// �е���Ψһʱ����ѯ���ÿ���Ӧ�����еؿ�����������ĵؿ���Ϣ
		// if (cardNoList.size() > 1) {
		// retResult = "03";
		// }
		// ���ظ��ص������Ĳ���

		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * ��������ҳ��У������Ĺ��Ƽ۸��Ƿ�������ݿ��и��µ����ֵ��Ӽ۷���֮�ͣ�
	 * 
	 */
	public ActionForward checkCurrp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long begin = System.currentTimeMillis();
		String appId = request.getParameter("appId");
		// String yktXh = request.getParameter("yktXh");
		TdscBlockTranApp tdscBlockTranApp = this.tdscBidderFundService.queryOneBlockTran(appId);
		String currp = request.getParameter("currp");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retResult = "00";
		// ���ƻ���Ϣ��ѯ
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		if (StringUtils.isNotEmpty(listingId)) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
			// �Ƚ������ֵ��CurrPrice�Ĵ�С
			if (tdscListingInfo.getCurrPrice() != null) {
				BigDecimal getCurrPrice = tdscListingInfo.getCurrPrice().add(tdscBlockTranApp.getAddPriceRange());
				BigDecimal currpInt = new BigDecimal(currp);
				if (currpInt.compareTo(getCurrPrice) == -1) {
					retResult = "11"; // ���ݿ��ֵ�Ƚϴ�
				}
			}
		}
		pw.write(retResult);
		long end = System.currentTimeMillis();
		logger.debug("======��������ҳ��У�飬����ʱ" + (end - begin) + "����======");
		pw.close();

		return null;
	}

	/**
	 * ��ѯ�ֿ��˾��۵ؿ���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showBlockList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1. �ӵ�½������
		// ����ҳ�洫���ֵ
		String cardNo = (String) request.getParameter("cardNo");
		String password = (String) request.getParameter("password");
		// 2. ���˽�����

		if (("null".equals(cardNo) || StringUtil.isEmpty(cardNo)) && ("null".equals(password) || StringUtil.isEmpty(password))) {
			String bidderId = request.getParameter("bidderId");
			TdscBidderApp tdscBidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
			cardNo = tdscBidderApp.getYktBh();
			password = tdscBidderApp.getYktMm();
		}

		// ���� cardNo��password(���׿����ź�����)��ѯ�ֿ����б�
		// TdscBidderApp tdscBidderApp=(TdscBidderApp)smartCardLoginService.checkSmartCard(cardNo,password);
		List tdscBidderAppList = smartCardLoginService.getBidderAppListByCard(cardNo, password);
		List returnList = new ArrayList();
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			// for(int i=0;i<tdscBidderAppList.size();i++){
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(0);
			if (tdscBidderApp != null && tdscBidderApp.getAppId() != null) {
				// ����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ���ڽ����е�TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				// tdscBaseQueryCondition.setAppId(tdscBidderApp.getAppId());
				String[] appIds = null;
				List appIdList = new ArrayList();
				if (tdscBidderApp != null && StringUtils.isNotBlank(tdscBidderApp.getAppId())) {
					appIds = tdscBidderApp.getAppId().split(",");
				}
				if (appIds != null && appIds.length > 0) {
					for (int j = 0; j < appIds.length; j++) {
						appIdList.add(appIds[j]);
					}
				}
				tdscBaseQueryCondition.setAppIdList(appIdList);
				tdscBaseQueryCondition.setStatus("01");// ������
				List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(tdscBaseQueryCondition);
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
						TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(m);
						tdscBlockAppView.setBzBeizhu(tdscBidderApp.getBidderId());// ��bidderId����BzBeizhu�ֶ���
						returnList.add(tdscBlockAppView);
					}
				}
				// }

				// �жϹ��ƽ�ֹʱ���Ƿ��ѵ�
				TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
				cond.setNoticeId(tdscBidderApp.getNoticeId());

				List appViews = commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);
				TdscBlockAppView appView = (TdscBlockAppView) appViews.get(0);

				String endTime = DateUtil.date2String(appView.getListEndDate(), "yyyyMMddHHmmss");
				String nowTime = DateUtil.date2String(new java.util.Date(), "yyyyMMddHHmmss");

				Long f = new Long(endTime);
				Long n = new Long(nowTime);
				if (f.longValue() > n.longValue()) {// ��δ�����ƽ�ֹʱ�䣬����ȥ����һ����������
					request.setAttribute("toNext", "1");
				}
			}
		}

		request.setAttribute("cardNo", cardNo);
		request.setAttribute("password", password);
		request.setAttribute("tdscBlockAppViewList", returnList);

		return mapping.findForward("showBlockList");
	}

	/**
	 * ����Ĭ�ϵ���ʼҳ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toHuiTui(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// request.setAttribute("cardNo", request.getParameter("cardNo"));
		return mapping.findForward("listingIndexJsp");
	}

	/**
	 * ��ѯ�ֿ�����Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����ҳ�洫���ֵ
		String cardNo = (String) request.getParameter("cardNo");
		String password = (String) request.getParameter("password");
		// ���� cardNo��password(���׿����ź�����)��ѯ�ֿ���
		// TdscBidderApp tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkSmartCard(cardNo, password);
		List tdscBidderAppList = smartCardLoginService.getBidderAppListByCard(cardNo, password);

		List bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(0);
			if (tdscBidderApp != null && tdscBidderApp.getAppId() != null) {
			}
			{
				String bidderId = (String) tdscBidderApp.getBidderId();
				bidlist = (List) tdscBidderAppService.queryBidderPersonList(bidderId);
				String appId = (String) tdscBidderApp.getAppId();
				// ����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				tdscBaseQueryCondition.setAppId(appId);
				tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
			}
		}

		request.setAttribute("bidlist", bidlist);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("cardNo", cardNo);

		return mapping.findForward("infolisting");
	}

	public ActionForward selectedNum(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		String cardNo = request.getParameter("cardNo");
		List list = this.tdscBidderAppService.findAppByNoticeId(noticeId);
		Set set = new HashSet();
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) list.get(i);
			set.add(app.getConNum());
		}
		request.setAttribute("contaisNum", set);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("appId", appId);
		request.setAttribute("certNo", certNo);
		request.setAttribute("cardNo", cardNo);

		return mapping.findForward("selectNum");
	}

	public ActionForward selectConNum(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		String cardNo = request.getParameter("cardNo");
		String conNum = request.getParameter("conNum");
		TdscBidderApp tdscBidderApp = null;
		if (noticeId != null && !"".equals(noticeId) && certNo != null && !"".equals(certNo)) {
			List tdscBidderAppList = tdscLocalTradeService.queryBidderAppListByYktBh(noticeId, cardNo);
			if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
				for (int i = 0; i < tdscBidderAppList.size(); i++) {
					tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
					tdscBidderApp.setConNum(conNum);
					tdscBidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
				}
			}
			tdscBidderAppService.updateTdscBidderAppList(tdscBidderAppList);
		}
		return new ActionForward("selfhelp.do?method=checkBidder&cardNo=" + StringUtils.trimToEmpty(tdscBidderApp.getYktBh()) + "&password="
				+ StringUtils.trimToEmpty(tdscBidderApp.getYktMm()), true);
	}

	/**
	 * ��ѯ�ֿ�����Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����ҳ�洫���ֵ
		String bidderId = (String) request.getParameter("bidderId");
		// ����bidderId(���׿����ź�����)��ѯ�ֿ���
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// TdscBidderApp tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkSmartCard(cardNo, password);
		List bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// ��þ����˵�List
		if (tdscBidderApp != null) {
			bidlist = (List) tdscBidderAppService.queryBidderPersonList(bidderId);
			String appId = (String) tdscBidderApp.getAppId();
			// ����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
			TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			tdscBaseQueryCondition.setAppId(appId);
			tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
		}
		request.setAttribute("bidlist", bidlist);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		return mapping.findForward("infolisting");
	}

	/**
	 * ��ӡ���ƾ��򱨼���Чȷ�ϻ�ִ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printOfferReply(ActionMapping mapping, String appId, TdscBidderApp tdscBidderApp, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ���� TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ���� condition ��ѯTdscBlockAppView
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null) {
			// ����ؿ���
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
		}
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		request.setAttribute("tdscBidderApp", tdscBidderApp);
		// request.setAttribute("yktXh", yktXh);

		List listingAppList = this.tdscLocalTradeService.queryByListCertAndAppId(tdscBidderApp.getCertNo(), tdscBlockAppView.getAppId());
		String listSize = listingAppList.size() + "";
		if (listingAppList != null && listingAppList.size() > 0) {
			TdscListingApp tdscListingApp = (TdscListingApp) listingAppList.get(0);
			request.setAttribute("tdscListingApp", tdscListingApp);
		}
		TdscListingInfo tdscListingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(appId);

		// request.setAttribute("listingAppList", listingAppList);
		request.setAttribute("tdscListingInfo", tdscListingInfo);
		request.setAttribute("listSize", listSize);

		return mapping.findForward("gpjmhz");
	}

	/**
	 * ��������--�ڶ���������Ƽ۸�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkPriceTwice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ���о����˵ı�ź������ļ۸�,ҵ��Id
		String appId = request.getParameter("appId");
		String acceptNo = request.getParameter("acceptNo");
		String listingPrice = request.getParameter("listingPrice");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ���� condition ��ѯTdscBlockAppView
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		request.setAttribute("acceptNo", acceptNo);
		request.setAttribute("listingPrice", listingPrice);
		return mapping.findForward("checkPriceTwice");
	}

	/**
	 * ��ӡ�ʸ�֤������Ž����ͽ��׿���������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */

	public ActionForward sendCardInfoToGuard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		// ��ȡbidderId
		String bidderId = request.getParameter("bidderId");
		// ����bidderId��ѯ��������Ϣ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// ��ʶ�þ������Ѿ���ӡ���ʸ�֤��
		tdscBidderApp.setIfDownloadZgzs("1");
		tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
		PrintWriter pw = response.getWriter();
		pw.write(bidderId);
		pw.close();
		return null;
	}

	// /////////////////////////////////////////////////
	/**
	 * ����--�ʸ�֤���ŵ�½����֤��������Ϣ �����ʸ�֤���ź��ж� 1.�Ƿ���ڸ÷����� 2.�õؿ���÷�ʽ�Ƿ�Ϊ����
	 */
	public ActionForward checkBidByCertNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String certNo = request.getParameter("password");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retResult = "00";
		List certNoList = (List) tdscBidderAppService.checkIfUsedCertNo(certNo);
		// �ж����ݿ���û�и��ʸ�֤���ţ�
		// û��ʱ��
		if (certNoList == null || certNoList.size() == 0) {
			retResult = "01";
		}
		if (certNoList.size() == 1) {
			// ���appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp) certNoList.get(0);
			String appId = (String) tdscBidderApp.getAppId();
			// ����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
			TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			tdscBaseQueryCondition.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
			// ���÷�ʽΪ�����ơ�ʱ
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(tdscBlockAppView.getTransferMode())) {
				retResult = "99" + appId;
				retResult += certNo;
			} else {
				retResult = "02";
			}
		} else {
			retResult = "77";
		}
		// �е���Ψһʱ����ѯ���ÿ���Ӧ�����еؿ�����������ĵؿ���Ϣ
		if (certNoList.size() > 1) {
			retResult = "03";
		}
		// ���ظ��ص������Ĳ���
		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * ����--�ʸ�֤���ŵ�½
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward certNoLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �����û����ź�����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "";
		// ����ҳ�洫����ʸ�֤����
		String certNo = request.getParameter("password");
		// ������������Ϣ����ѯ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.getBidderAppByCertNo(certNo);
		String appId = "";
		String checkMsg = "false";
		if (tdscBidderApp != null) {
			checkMsg = "true";
			appId = tdscBidderApp.getAppId();
		}
		retString += checkMsg + ",";
		retString += appId + ",";
		retString += certNo + ",";
		String nodeTime = "false";
		TdscAppNodeStat appNodeStat = this.commonFlowService.findAppNodeStat(appId, FlowConstants.FLOW_NODE_LISTING);
		if (appNodeStat != null) {
			if (appNodeStat.getNodeStat().equals(FlowConstants.STAT_ACTIVE)) {
				nodeTime = "true";
			} else if (appNodeStat.getNodeStat().equals(FlowConstants.STAT_END) || appNodeStat.getNodeStat().equals(FlowConstants.STAT_TERMINATE)) {
				nodeTime = "end";
			} else if (appNodeStat.getNodeStat().equals(FlowConstants.STAT_INIT)) {
				nodeTime = "init";
			}
		}
		retString += nodeTime + ",";
		String review = "";
		if (tdscBidderApp != null) {
			review = tdscBidderApp.getReviewResult();
		}

		if (review == null) {
			review = "";
		}
		String reviewStatus = "false";
		if (review.equals("0")) {
			reviewStatus = "notPass";
		} else if (review.equals("1")) {
			reviewStatus = "true";
		}
		retString += reviewStatus + ",";

		pw.write(retString);
		pw.close();
		return null;
	}

	public ActionForward checkByCertNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����ҳ�洫���ֵ
		String certNo = (String) request.getParameter("certNo");
		// ���� cardNo��password(���׿����ź�����)��ѯ�ֿ���
		TdscBidderApp tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkByCertNo(certNo);
		List bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// ��þ����˵�List
		if (tdscBidderApp != null) {
			String bidderId = (String) tdscBidderApp.getBidderId();
			bidlist = (List) tdscBidderAppService.queryBidderPersonList(bidderId);
			String appId = (String) tdscBidderApp.getAppId();
			// ����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
			TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			tdscBaseQueryCondition.setAppId(appId);
			tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
		}
		request.setAttribute("bidlist", bidlist);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		return mapping.findForward("infolisting");
	}

	/**
	 * �����ʸ�֤���Ż�ù�����Ӧ��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addPriceByCertNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long begin = System.currentTimeMillis();
		String appId = request.getParameter("appId");
		String bidderId = request.getParameter("bidderId");
		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		// ���ƻ���Ϣ��ѯ
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// �ؿ���Ϣ��ѯ
		TdscBlockTranApp tdscBlockTranApp = this.tdscBidderFundService.queryOneBlockTran(appId);
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		if (StringUtils.isNotEmpty(listingId)) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// ��������Ϣ��ѯ
		// TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.getBidderAppByCertNo(certNo);
		if (bidderId != null && !"".equals(bidderId)) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
		}

		// ��ǰ���Ƽ۸�
		if (listingId == null) {
			request.setAttribute("currPrice", tdscBlockTranApp.getInitPrice());
			if (tdscBlockTranApp.getIsPurposeBlock() != null && "1".equals(tdscBlockTranApp.getIsPurposeBlock())) {// ��������Ƴ��õؿ飬�������δ�ڹ��ƻ��ϳɹ����Ʊ��۹���tdsc_listing_info�����޼�¼�������ĵ�ǰ���������ĳ�ʼֵΪ1
				request.setAttribute("currRound", new BigDecimal(1));
			} else if (tdscBlockTranApp.getIsPurposeBlock() != null && "0".equals(tdscBlockTranApp.getIsPurposeBlock())) {// ��������Ƴ��õؿ飬�������δ�ڹ��ƻ��ϳɹ����Ʊ��۹���tdsc_listing_info�����޼�¼�������ĵ�ǰ���������ĳ�ʼֵΪ0
				request.setAttribute("currRound", new BigDecimal(0));
			} else {
				request.setAttribute("currRound", null);
			}
		} else {
			request.setAttribute("currPrice", tdscListingInfo.getCurrPrice());
			// ��ǰ��������
			request.setAttribute("currRound", tdscListingInfo.getCurrRound());
		}
		request.setAttribute("addPriceRange", tdscBlockTranApp.getAddPriceRange());
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("password", password);
		long end = System.currentTimeMillis();
		logger.debug("====== ��ҳ�洫�������Ӧ��Ϣ������ʱ" + (end - begin) + "����======");
		return mapping.findForward("toSelfhelp");
	}

	/**
	 * ����������� ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addPrice_qd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long begin = System.currentTimeMillis();

		// ��ȡҳ���о����˵ı�ź������ļ۸�,ҵ��Id
		String appId = request.getParameter("appId");
		// �����˽��׿����
		String acceptNo = request.getParameter("acceptNo");
		String listingPrice = request.getParameter("listingPrice");
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		TdscListingApp tdscListingApp = new TdscListingApp();
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		// ��������Ϣ��ѯ
		tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkByAcceptNo(acceptNo);

		tdscListingApp.setListPrice(new BigDecimal(listingPrice));
		tdscListingApp.setListCert(tdscBidderApp.getCertNo());
		tdscListingApp.setPriceType("1");
		tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
		// ����appId ��ѯ listingId
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// ��ҳ�洫����ʼ�۸�,���ӷ���,��ǰ�۸�,��ǰ����;
		if (listingPrice != null && !"".equals(listingPrice)) {
			Timestamp nowTime = new Timestamp(System.currentTimeMillis());
			// ������ƻ�Id������,����һ���¼�¼
			if (!StringUtils.isNotEmpty(listingId)) {
				tdscListingInfo.setAppId(appId);
				tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
				tdscListingInfo.setListDate(nowTime);
				tdscListingInfo.setCurrRound(new BigDecimal(1));
				// tdscListingInfo.setYktXh(yktXh);

				this.tdscLocalTradeService.saveListingInfoAndApp(tdscListingInfo, tdscListingApp);
			} else {
				// ������ԭ��¼���޸�
				tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);

				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
				tdscListingInfo.setListDate(nowTime);
				// tdscListingInfo.setYktXh(yktXh);
				if (tdscListingInfo.getCurrRound() != null) {
					int round = tdscListingInfo.getCurrRound().intValue();
					round++;
					tdscListingInfo.setCurrRound(new BigDecimal(round));
				} else {
					tdscListingInfo.setCurrRound(new BigDecimal(1));
				}
				this.tdscLocalTradeService.modifyListingInfoAndApp(tdscListingInfo, tdscListingApp);
			}
		}

		long end = System.currentTimeMillis();
		logger.debug("======��������������ƣ�����ʱ" + (end - begin) + "����======");
		return printOfferReply(mapping, appId, tdscBidderApp, request, response);
	}

	/**
	 * �鿴��ʷ���Ƽ�¼
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showHistoryByCertNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		// ���� TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ���� condition ��ѯTdscBlockAppView
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkByCertNo(certNo);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		// request.setAttribute("yktXh", yktXh);iu

		request.setAttribute("appId", appId);

		List listingAppList = this.tdscLocalTradeService.queryByListCertAndAppId(certNo, appId);
		request.setAttribute("listingAppList", listingAppList);

		return mapping.findForward("showHistory");
	}

	public ActionForward checkIsPurposePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1.����Ǹõؿ�������ˣ�����ʾ��ӡ��ִ��ť�����������������
		// ��1����������ʹ�����ؽ��׿��������۹�����ִҳ���ӡ���������۽�����ʱ��
		// ��2����������û��ʹ�����ؽ��׿��������۹�����ִҳ���ӡ�õؿ�ĳ�ʼ���Ƽ۸��������ƿ�ʼʱ��
		// 2.����Ǹõؿ�ľ����ˣ��������������
		// ��1���þ�����ʹ�����ؽ��׿��������۹�������ʾ��ӡ��ִ��ť����ִҳ���ӡ���������۽�����ʱ��
		// ��2����������û��ʹ�����ؽ��׿��������۹���������ʾ��ӡ��ִ��ť

		String bidderId = request.getParameter("bidderId") + "";
		String[] ids = bidderId.split(",");

		String ids1 = "";
		String ids2 = "";
		if (ids != null && ids.length == 2) {
			ids1 = ids[0];
			ids2 = ids[1];
		}
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		String returnStr = "0";
		List bidderPersonList = null;
		TdscBidderPersonApp bidderPersonApp = null;
		// TdscListingApp tdscListingApp = new TdscListingApp();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();

		TdscBidderApp tdscBidderApp = tdscBidderAppService.queryBidderAppInfo(ids1);

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(ids2);
		tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// ��ѯ��û���������Ʊ��۹�
		List tdscListingAppList = tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(tdscBidderApp.getYktXh(), ids2);

		if (tdscBidderApp != null) {
			bidderPersonList = tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
			if (bidderPersonList != null && bidderPersonList.size() > 0) {
				bidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(0);
			}

			if ("1".equals(tdscBidderApp.getIsPurposePerson())) {// ������
				if (bidderPersonApp.getPurposeAppId().indexOf(ids2) >= 0) {// ������ѡ��������ؿ���в���
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// �����˶�����ؿ��������Ʊ��۹�
						// tdscListingApp = (TdscListingApp)tdscListingAppList.get(0);
					} else {
						if (tdscBlockAppView != null) {
							// tdscListingApp.setListPrice(tdscBlockAppView.getInitPrice());
							// tdscListingApp.setListDate(tdscBlockAppView.getListStartDate());
						}
					}
					returnStr = "1";// ��ʾ��ӡ��ִ��ť
				} else {// �����˲���ѡ������ؿ���в���
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// �����˶Ըõؿ��������Ʊ��۹�
						// tdscListingApp = (TdscListingApp)tdscListingAppList.get(0);
						returnStr = "1";// ��ʾ��ӡ��ִ��ť
					} else {
						returnStr = "0";// û�������Ʊ��۹�������ʾ��ӡ��ִ��ť
					}
				}
			} else {// ���������ˣ�������������ʱ���������ľ�����
				if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// �����˶Ըõؿ��������Ʊ��۹�
					// tdscListingApp = (TdscListingApp)tdscListingAppList.get(0);
					returnStr = "1";// ��ʾ��ӡ��ִ��ť
				} else {
					returnStr = "0";// û�������Ʊ��۹�������ʾ��ӡ��ִ��ť
				}
			}
		}

		pw.write(returnStr);
		pw.close();
		return null;
	}

}
