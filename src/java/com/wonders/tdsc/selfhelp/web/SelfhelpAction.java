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
	/** 日志 */
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

		// 1.如果是该地块的意向人，则显示打印回执按钮，并分两种情况处理
		// （1）该意向人使用土地交易卡自助报价过，回执页面打印其自助报价金额及报价时间
		// （2）该意向人没有使用土地交易卡自助报价过，回执页面打印该地块的初始挂牌价格及自助挂牌开始时间
		// 2.如果是该地块的竞买人，分两种情况处理
		// （1）该竞买人使用土地交易卡自助报价过，则显示打印回执按钮，回执页面打印其自助报价金额及报价时间
		// （2）该意向人没有使用土地交易卡自助报价过，，则不显示打印回执按钮

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

		// 查询该人有没有自助挂牌报价过
		List tdscListingAppList = tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(tdscBidderApp.getYktBh(), ids2);

		if (tdscBidderApp != null) {
			bidderPersonList = tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
			if (bidderPersonList != null && bidderPersonList.size() > 0) {
				bidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(0);
			}

			if ("1".equals(tdscBidderApp.getIsPurposePerson())) {// 意向人
				if (bidderPersonApp.getPurposeAppId().indexOf(ids2) >= 0) {// 意向人选择了意向地块进行操作
					// ids2.equals(bidderPersonApp.getPurposeAppId())
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// 意向人对意向地块自助挂牌报价过
						tdscListingApp = (TdscListingApp) tdscListingAppList.get(0);
					} else {
						if (tdscBlockAppView != null) {
							tdscListingApp.setListPrice(tdscBlockAppView.getInitPrice());
							tdscListingApp.setListDate(tdscBlockAppView.getListStartDate());
						}
					}
				} else {// 意向人不是选择意向地块进行操作
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// 意向人对该地块自助挂牌报价过
						tdscListingApp = (TdscListingApp) tdscListingAppList.get(0);
					}
				}
			} else {// 不是意向人，即受理竞买申请时新增进来的竞买人
				if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// 竞买人对该地块自助挂牌报价过
					tdscListingApp = (TdscListingApp) tdscListingAppList.get(0);
				} else {
					request.setAttribute("errorMsg", "未报价，不能打印回执！");
				}
			}
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscListingApp", tdscListingApp);

		return mapping.findForward("printInitPriceNew");
	}

	/**
	 * 完成自助挂牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addListingPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取页面中竞卖人的编号和所出的价格,业务Id
		String appId = request.getParameter("appId");
		// 竞卖人交易卡既是 芯片号，全球唯一
		String yktBh = request.getParameter("yktXh");
		String listingPrice = request.getParameter("listingPrice");
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		TdscListingApp tdscListingApp = new TdscListingApp();
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		// 竞买人信息查询
		tdscBidderApp = this.tdscBidderAppService.getTdscBidderAppByYktBh(yktBh);
		String bidderId = tdscBidderApp.getBidderId();
		tdscBidderApp = this.tdscBidderAppService.queryBidderAppInfo(bidderId);
		TdscBlockTranApp tdscBlockTranApp = this.tdscBidderFundService.queryOneBlockTran(appId);

		tdscListingApp.setListPrice(new BigDecimal(listingPrice));
		tdscListingApp.setListCert(tdscBidderApp.getCertNo());
		tdscListingApp.setPriceType("1");
		tdscListingApp.setAppId(appId);
		tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
		// 根据appId 查询 listingId
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// 向页面传入起始价格,增加幅度,当前价格,当前轮数;
		if (listingPrice != null && !"".equals(listingPrice)) {
			Timestamp nowTime = new Timestamp(System.currentTimeMillis());
			// 如果挂牌会Id不存在,插入一条新记录
			if (!StringUtils.isNotEmpty(listingId)) {
				tdscListingInfo.setAppId(appId);
				tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
				tdscListingInfo.setListDate(nowTime);
				if (tdscBlockTranApp.getIsPurposeBlock() != null && "1".equals(tdscBlockTranApp.getIsPurposeBlock())) {// 有意向挂牌出让地块，在挂牌机上挂牌报价成功，第一次向tdsc_listing_info表插入记录时，其挂牌轮数被设为2，第1轮已由意向人挂牌报价
					tdscListingInfo.setCurrRound(new BigDecimal(2));
				} else if (tdscBlockTranApp.getIsPurposeBlock() != null && "0".equals(tdscBlockTranApp.getIsPurposeBlock())) {// 无意向挂牌出让地块，在挂牌机上挂牌报价成功，第一次向tdsc_listing_info表插入记录时，其挂牌轮数被设为1
					tdscListingInfo.setCurrRound(new BigDecimal(1));
				}
				tdscListingInfo.setYktXh(yktBh);

				this.tdscLocalTradeService.saveListingInfoAndApp(tdscListingInfo, tdscListingApp);
			} else {
				// 否则在原记录上修改
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
	 * 向页面传入挂牌相应信息
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
		// 挂牌会信息查询
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// 地块信息查询
		TdscBlockTranApp tdscBlockTranApp = this.tdscBidderFundService.queryOneBlockTran(appId);
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		if (StringUtils.isNotEmpty(listingId)) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		// 竞买人信息查询
		TdscBidderApp tdscBidderApp = (TdscBidderApp) this.tdscBidderAppService.getBidderAppByYktBh(yktBh);
		// 当前挂牌价格
		if (listingId == null) {
			request.setAttribute("currPrice", tdscBlockTranApp.getInitPrice());
			request.setAttribute("currRound", null);
		} else {
			request.setAttribute("currPrice", tdscListingInfo.getCurrPrice());
			// 当前挂牌轮数
			request.setAttribute("currRound", tdscListingInfo.getCurrRound());
		}
		request.setAttribute("addPriceRange", tdscBlockTranApp.getAddPriceRange());
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		// request.setAttribute("cardNo", cardNo);
		// request.setAttribute("password",password);
		long end = System.currentTimeMillis();
		logger.debug("====== 向页面传入挂牌相应信息，共耗时" + (end - begin) + "毫秒======");
		return mapping.findForward("toSelfhelp");
	}

	/**
	 * 查看历史挂牌记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String appId = request.getParameter("appId");
		String yktXh = request.getParameter("yktXh");
		// 构造 TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 根据 condition 查询TdscBlockAppView
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		// 获得bidderId
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
	 * 打印竞买资格证书/未入围资格证书
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
		// 获取appId,YKI_BH,YKT_MM
		String appId = request.getParameter("appId");
		String acceptNo = request.getParameter("acceptNo");
		// 设置竞买人查询的 TdscBidderCondition
		TdscBidderCondition bidderCondition = new TdscBidderCondition();
		bidderCondition.setAppId(appId);
		bidderCondition.setAcceptNo(acceptNo);
		// 获得竞买人的姓名
		String bidderName = selfhelpService.getBidderName(bidderCondition);
		request.setAttribute("bidderName", bidderName);
		// 获得竞买人信息
		TdscBidderApp tdscBidderApp = (TdscBidderApp) selfhelpService.getTdscBidderApp(bidderCondition);
		if (tdscBidderApp != null && tdscBidderApp.getBidderId() != null) {
			request.setAttribute("bidderId", StringUtils.trimToEmpty(tdscBidderApp.getBidderId()));
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);

		// 设置地块信息查询的 TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 通过appId查询土地基本信息
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		String gphStartTime = "";
		String gphEndTime = "";
		String pmhEndTime = "";
		String pmhLoc = "";
		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null) {
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));

			gphStartTime = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getListStartDate(), "yyyy年MM月dd日HH时mm分"));
			gphEndTime = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getListEndDate(), "yyyy年MM月dd日HH时mm分"));
			pmhEndTime = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getAuctionDate(), "yyyy年MM月dd日HH时mm分"));
			pmhLoc = StringUtils.trimToEmpty(tdscBlockAppView.getAuctionLoc());
		}

		Date rightNow = new Date();
		String rightNowString = StringUtils.trimToEmpty(DateUtil.date2String(rightNow, "yyyy年MM月dd日"));

		request.setAttribute("gphStartTime", gphStartTime);
		request.setAttribute("gphEndTime", gphEndTime);
		request.setAttribute("pmhEndTime", pmhEndTime);
		request.setAttribute("pmhLoc", pmhLoc);
		request.setAttribute("rightNowString", rightNowString);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		// 获得“出让结果公示”的时间，作为竞买资格证书的有效截至日期；
		TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findPlanTableInfo(appId);
		if (tdscBlockPlanTable != null) {
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		}
		// long end = System.currentTimeMillis();
		// logger.debug("======打印竞买资格证书，共耗时" + (end - begin) + "毫秒======");
		// if(tdscBlockAppView.getTransferMode()!=null&&"3103".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toPMPrintCompetenceSuccess");
		// }else if(tdscBlockAppView.getTransferMode()!=null&&"3104".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toGPPrintCompetenceSuccess");
		// }else
		// return mapping.findForward("toPrintCompetenceSuccess");

		// iweb打印
		// if(tdscBlockAppView.getTransferMode()!=null&&"3103".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toIwebPMPrintCompetenceSuccess");
		// }else if(tdscBlockAppView.getTransferMode()!=null&&"3104".equals(tdscBlockAppView.getTransferMode())){
		// return mapping.findForward("toIwebGPPrintCompetenceSuccess");
		// }else
		return mapping.findForward("toPrintCompetenceSuccess");
	}

	public ActionForward toPrintCompetenceFailed(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取appId,YKI_BH,YKT_MM
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

		// 通过appId查询土地基本信息
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null) {
			// 整理地块编号
			tdscBlockAppView.setUnitebBlockCode(tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId()));
		}
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("toPrintCompetenceFailed");
	}

	/**
	 * 卡号传递
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
	 * 刷卡页面校验该卡是否有效
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
			// 获得交易卡的编号
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.getBidderAppByYktBh(cardNo);
			if (tdscBidderApp != null) {
				yktXh = tdscBidderApp.getYktXh() + ",";
			}

			// 如果对应的挂牌截止时间已到，提示“挂牌截止时间已到”
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
	 * 挂牌登陆
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardLogin_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 检验用户卡号和密码
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "";
		// 接受页面传入的交易卡卡号和密码
		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		// 根据交易卡信息做查询
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
	 * 自助挂牌 地块，用户检验 一卡对多地
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 检验用户卡号和密码
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "";
		// 接受页面传入的交易卡卡号,密码,地块appId
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

			// 根据app_id 查询公告和 plan_id
			// 查询 plan 表，得到时间后再判断是否挂牌结束
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
	 * 根据cardNo判断自助挂牌页面输入的一卡通卡号：1、该地块出让方式是不是挂牌 2、该地块是不是正在受理
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
		// 判断数据库有没有该卡号；
		// 没有时：
		if (cardNoList == null || cardNoList.size() == 0) {
			retResult = "01";
		}
		if (cardNoList.size() >= 1) {
			// 获得appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp) cardNoList.get(0);
			if (password != null && tdscBidderApp.getYktMm() != null) {
				if (password.equals(tdscBidderApp.getYktMm())) {
					String appId = (String) tdscBidderApp.getAppId();
					String yktXh = (String) tdscBidderApp.getYktXh();
					// 根据appId通过通用查询，查出该appId对应的TdscBlockAppView
					TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
					tdscBaseQueryCondition.setAppId(appId.substring(0, 32));
					TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
					// 出让方式为“挂牌”时
					if (GlobalConstants.DIC_TRANSFER_LISTING.equals(tdscBlockAppView.getTransferMode())) {
						retResult = "99" + appId.substring(0, 32);
						if (yktXh != null) {
							retResult += yktXh;
						}
					} else {
						// 出让方式为“拍卖”时
						retResult = "02";
					}
					// 密码出错
				} else {
					retResult = "77";
				}
			}
		}
		// 有但不唯一时，查询出该卡对应的所有地块中正在受理的地块信息
		// if (cardNoList.size() > 1) {
		// retResult = "03";
		// }
		// 返回给回调函数的参数

		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * 自助挂牌页面校验填入的挂牌价格是否大于数据库中更新的最大值与加价幅度之和；
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
		// 挂牌会信息查询
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		if (StringUtils.isNotEmpty(listingId)) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
			// 比较输入的值与CurrPrice的大小
			if (tdscListingInfo.getCurrPrice() != null) {
				BigDecimal getCurrPrice = tdscListingInfo.getCurrPrice().add(tdscBlockTranApp.getAddPriceRange());
				BigDecimal currpInt = new BigDecimal(currp);
				if (currpInt.compareTo(getCurrPrice) == -1) {
					retResult = "11"; // 数据库的值比较大
				}
			}
		}
		pw.write(retResult);
		long end = System.currentTimeMillis();
		logger.debug("======自助挂牌页面校验，共耗时" + (end - begin) + "毫秒======");
		pw.close();

		return null;
	}

	/**
	 * 查询持卡人竞价地块信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showBlockList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1. 从登陆进来的
		// 接受页面传入的值
		String cardNo = (String) request.getParameter("cardNo");
		String password = (String) request.getParameter("password");
		// 2. 回退进来的

		if (("null".equals(cardNo) || StringUtil.isEmpty(cardNo)) && ("null".equals(password) || StringUtil.isEmpty(password))) {
			String bidderId = request.getParameter("bidderId");
			TdscBidderApp tdscBidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
			cardNo = tdscBidderApp.getYktBh();
			password = tdscBidderApp.getYktMm();
		}

		// 根据 cardNo，password(交易卡卡号和密码)查询持卡人列表
		// TdscBidderApp tdscBidderApp=(TdscBidderApp)smartCardLoginService.checkSmartCard(cardNo,password);
		List tdscBidderAppList = smartCardLoginService.getBidderAppListByCard(cardNo, password);
		List returnList = new ArrayList();
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			// for(int i=0;i<tdscBidderAppList.size();i++){
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(0);
			if (tdscBidderApp != null && tdscBidderApp.getAppId() != null) {
				// 根据appId通过通用查询，查出该appId对应的在交易中的TdscBlockAppView
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
				tdscBaseQueryCondition.setStatus("01");// 交易中
				List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(tdscBaseQueryCondition);
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
						TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(m);
						tdscBlockAppView.setBzBeizhu(tdscBidderApp.getBidderId());// 将bidderId存入BzBeizhu字段中
						returnList.add(tdscBlockAppView);
					}
				}
				// }

				// 判断挂牌截止时间是否已到
				TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
				cond.setNoticeId(tdscBidderApp.getNoticeId());

				List appViews = commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);
				TdscBlockAppView appView = (TdscBlockAppView) appViews.get(0);

				String endTime = DateUtil.date2String(appView.getListEndDate(), "yyyyMMddHHmmss");
				String nowTime = DateUtil.date2String(new java.util.Date(), "yyyyMMddHHmmss");

				Long f = new Long(endTime);
				Long n = new Long(nowTime);
				if (f.longValue() > n.longValue()) {// 还未到挂牌截止时间，可以去做下一步，即报价
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
	 * 返回默认的起始页
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
	 * 查询持卡人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接受页面传入的值
		String cardNo = (String) request.getParameter("cardNo");
		String password = (String) request.getParameter("password");
		// 根据 cardNo，password(交易卡卡号和密码)查询持卡人
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
				// 根据appId通过通用查询，查出该appId对应的TdscBlockAppView
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
	 * 查询持卡人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接受页面传入的值
		String bidderId = (String) request.getParameter("bidderId");
		// 根据bidderId(交易卡卡号和密码)查询持卡人
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// TdscBidderApp tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkSmartCard(cardNo, password);
		List bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// 获得竞买人的List
		if (tdscBidderApp != null) {
			bidlist = (List) tdscBidderAppService.queryBidderPersonList(bidderId);
			String appId = (String) tdscBidderApp.getAppId();
			// 根据appId通过通用查询，查出该appId对应的TdscBlockAppView
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
	 * 打印挂牌竞买报价有效确认回执
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
		// 构造 TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 根据 condition 查询TdscBlockAppView
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		if (tdscBlockAppView != null && tdscBlockAppView.getBlockId() != null) {
			// 整理地块编号
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
	 * 自助挂牌--第二次输入挂牌价格
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkPriceTwice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面中竞卖人的编号和所出的价格,业务Id
		String appId = request.getParameter("appId");
		String acceptNo = request.getParameter("acceptNo");
		String listingPrice = request.getParameter("listingPrice");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 根据 condition 查询TdscBlockAppView
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		request.setAttribute("acceptNo", acceptNo);
		request.setAttribute("listingPrice", listingPrice);
		return mapping.findForward("checkPriceTwice");
	}

	/**
	 * 打印资格证书后，向门禁发送交易卡及会议信息
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
		// 获取bidderId
		String bidderId = request.getParameter("bidderId");
		// 根据bidderId查询竞买人信息
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// 标识该竞买人已经打印过资格证书
		tdscBidderApp.setIfDownloadZgzs("1");
		tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
		PrintWriter pw = response.getWriter();
		pw.write(bidderId);
		pw.close();
		return null;
	}

	// /////////////////////////////////////////////////
	/**
	 * 挂牌--资格证书编号登陆、验证竞买人信息 输入资格证书编号后，判断 1.是否存在该份申请 2.该地块出让方式是否为挂牌
	 */
	public ActionForward checkBidByCertNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String certNo = request.getParameter("password");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retResult = "00";
		List certNoList = (List) tdscBidderAppService.checkIfUsedCertNo(certNo);
		// 判断数据库有没有该资格证书编号；
		// 没有时：
		if (certNoList == null || certNoList.size() == 0) {
			retResult = "01";
		}
		if (certNoList.size() == 1) {
			// 获得appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp) certNoList.get(0);
			String appId = (String) tdscBidderApp.getAppId();
			// 根据appId通过通用查询，查出该appId对应的TdscBlockAppView
			TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			tdscBaseQueryCondition.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
			// 出让方式为“挂牌”时
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(tdscBlockAppView.getTransferMode())) {
				retResult = "99" + appId;
				retResult += certNo;
			} else {
				retResult = "02";
			}
		} else {
			retResult = "77";
		}
		// 有但不唯一时，查询出该卡对应的所有地块中正在受理的地块信息
		if (certNoList.size() > 1) {
			retResult = "03";
		}
		// 返回给回调函数的参数
		pw.write(retResult);
		pw.close();
		return null;
	}

	/**
	 * 挂牌--资格证书编号登陆
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward certNoLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 检验用户卡号和密码
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "";
		// 接受页面传入的资格证书编号
		String certNo = request.getParameter("password");
		// 根据受理编号信息做查询
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
		// 接受页面传入的值
		String certNo = (String) request.getParameter("certNo");
		// 根据 cardNo，password(交易卡卡号和密码)查询持卡人
		TdscBidderApp tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkByCertNo(certNo);
		List bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// 获得竞买人的List
		if (tdscBidderApp != null) {
			String bidderId = (String) tdscBidderApp.getBidderId();
			bidlist = (List) tdscBidderAppService.queryBidderPersonList(bidderId);
			String appId = (String) tdscBidderApp.getAppId();
			// 根据appId通过通用查询，查出该appId对应的TdscBlockAppView
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
	 * 根据资格证书编号获得挂牌相应信息
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
		// 挂牌会信息查询
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// 地块信息查询
		TdscBlockTranApp tdscBlockTranApp = this.tdscBidderFundService.queryOneBlockTran(appId);
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		if (StringUtils.isNotEmpty(listingId)) {
			tdscListingInfo = this.tdscLocalTradeService.getListingInfo(listingId);
		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);

		// 竞买人信息查询
		// TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.getBidderAppByCertNo(certNo);
		if (bidderId != null && !"".equals(bidderId)) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
		}

		// 当前挂牌价格
		if (listingId == null) {
			request.setAttribute("currPrice", tdscBlockTranApp.getInitPrice());
			if (tdscBlockTranApp.getIsPurposeBlock() != null && "1".equals(tdscBlockTranApp.getIsPurposeBlock())) {// 有意向挂牌出让地块，如果它还未在挂牌机上成功挂牌报价过（tdsc_listing_info表中无记录），它的当前挂牌轮数的初始值为1
				request.setAttribute("currRound", new BigDecimal(1));
			} else if (tdscBlockTranApp.getIsPurposeBlock() != null && "0".equals(tdscBlockTranApp.getIsPurposeBlock())) {// 无意向挂牌出让地块，如果它还未在挂牌机上成功挂牌报价过（tdsc_listing_info表中无记录），它的当前挂牌轮数的初始值为0
				request.setAttribute("currRound", new BigDecimal(0));
			} else {
				request.setAttribute("currRound", null);
			}
		} else {
			request.setAttribute("currPrice", tdscListingInfo.getCurrPrice());
			// 当前挂牌轮数
			request.setAttribute("currRound", tdscListingInfo.getCurrRound());
		}
		request.setAttribute("addPriceRange", tdscBlockTranApp.getAddPriceRange());
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("password", password);
		long end = System.currentTimeMillis();
		logger.debug("====== 向页面传入挂牌相应信息，共耗时" + (end - begin) + "毫秒======");
		return mapping.findForward("toSelfhelp");
	}

	/**
	 * 完成自助挂牌 受理编号
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

		// 获取页面中竞卖人的编号和所出的价格,业务Id
		String appId = request.getParameter("appId");
		// 竞卖人交易卡编号
		String acceptNo = request.getParameter("acceptNo");
		String listingPrice = request.getParameter("listingPrice");
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		TdscListingApp tdscListingApp = new TdscListingApp();
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		// 竞买人信息查询
		tdscBidderApp = (TdscBidderApp) smartCardLoginService.checkByAcceptNo(acceptNo);

		tdscListingApp.setListPrice(new BigDecimal(listingPrice));
		tdscListingApp.setListCert(tdscBidderApp.getCertNo());
		tdscListingApp.setPriceType("1");
		tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
		// 根据appId 查询 listingId
		String listingId = this.tdscLocalTradeService.queryListingId(appId);
		// 向页面传入起始价格,增加幅度,当前价格,当前轮数;
		if (listingPrice != null && !"".equals(listingPrice)) {
			Timestamp nowTime = new Timestamp(System.currentTimeMillis());
			// 如果挂牌会Id不存在,插入一条新记录
			if (!StringUtils.isNotEmpty(listingId)) {
				tdscListingInfo.setAppId(appId);
				tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
				tdscListingInfo.setListDate(nowTime);
				tdscListingInfo.setCurrRound(new BigDecimal(1));
				// tdscListingInfo.setYktXh(yktXh);

				this.tdscLocalTradeService.saveListingInfoAndApp(tdscListingInfo, tdscListingApp);
			} else {
				// 否则在原记录上修改
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
		logger.debug("======结束完成自助挂牌，共耗时" + (end - begin) + "毫秒======");
		return printOfferReply(mapping, appId, tdscBidderApp, request, response);
	}

	/**
	 * 查看历史挂牌记录
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showHistoryByCertNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String appId = request.getParameter("appId");
		String certNo = request.getParameter("certNo");
		// 构造 TdscBaseQueryCondition
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 根据 condition 查询TdscBlockAppView
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
		// 1.如果是该地块的意向人，则显示打印回执按钮，并分两种情况处理
		// （1）该意向人使用土地交易卡自助报价过，回执页面打印其自助报价金额及报价时间
		// （2）该意向人没有使用土地交易卡自助报价过，回执页面打印该地块的初始挂牌价格及自助挂牌开始时间
		// 2.如果是该地块的竞买人，分两种情况处理
		// （1）该竞买人使用土地交易卡自助报价过，则显示打印回执按钮，回执页面打印其自助报价金额及报价时间
		// （2）该意向人没有使用土地交易卡自助报价过，，则不显示打印回执按钮

		String bidderId = request.getParameter("bidderId") + "";
		String[] ids = bidderId.split(",");

		String ids1 = "";
		String ids2 = "";
		if (ids != null && ids.length == 2) {
			ids1 = ids[0];
			ids2 = ids[1];
		}
		// 将内容设置到输出
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

		// 查询有没有自助挂牌报价过
		List tdscListingAppList = tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(tdscBidderApp.getYktXh(), ids2);

		if (tdscBidderApp != null) {
			bidderPersonList = tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
			if (bidderPersonList != null && bidderPersonList.size() > 0) {
				bidderPersonApp = (TdscBidderPersonApp) bidderPersonList.get(0);
			}

			if ("1".equals(tdscBidderApp.getIsPurposePerson())) {// 意向人
				if (bidderPersonApp.getPurposeAppId().indexOf(ids2) >= 0) {// 意向人选择了意向地块进行操作
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// 意向人对意向地块自助挂牌报价过
						// tdscListingApp = (TdscListingApp)tdscListingAppList.get(0);
					} else {
						if (tdscBlockAppView != null) {
							// tdscListingApp.setListPrice(tdscBlockAppView.getInitPrice());
							// tdscListingApp.setListDate(tdscBlockAppView.getListStartDate());
						}
					}
					returnStr = "1";// 显示打印回执按钮
				} else {// 意向人不是选择意向地块进行操作
					if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// 意向人对该地块自助挂牌报价过
						// tdscListingApp = (TdscListingApp)tdscListingAppList.get(0);
						returnStr = "1";// 显示打印回执按钮
					} else {
						returnStr = "0";// 没自助挂牌报价过，不显示打印回执按钮
					}
				}
			} else {// 不是意向人，即受理竞买申请时新增进来的竞买人
				if (tdscListingAppList != null && tdscListingAppList.size() > 0) {// 竞买人对该地块自助挂牌报价过
					// tdscListingApp = (TdscListingApp)tdscListingAppList.get(0);
					returnStr = "1";// 显示打印回执按钮
				} else {
					returnStr = "0";// 没自助挂牌报价过，不显示打印回执按钮
				}
			}
		}

		pw.write(returnStr);
		pw.close();
		return null;
	}

}
