package com.wonders.tdsc.bidder.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.bank.service.TdscBankAppService;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.blockwork.web.form.TdscBlockForm;
import com.wonders.tdsc.blockwork.web.form.TdscDicBean;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBankApp;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderMaterial;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderProvide;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscReturnBail;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.retbail.service.TdscReturnBailService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.tdscbase.service.TdscBidderViewService;

public class TdscBidderAppAction extends BaseAction {

	private TdscScheduletableService tdscScheduletableService;

	private CommonQueryService commonQueryService;

	private TdscBidderViewService tdscBidderViewService;

	private TdscBidderAppService tdscBidderAppService;

	private CommonFlowService commonFlowService;

	private TdscLocalTradeService tdscLocalTradeService;

	private TdscReturnBailService tdscReturnBailService;

	private TdscBankAppService tdscBankAppService;
	private TdscBlockInfoService tdscBlockInfoService;
	public void setTdscBankAppService(TdscBankAppService tdscBankAppService) {
		this.tdscBankAppService = tdscBankAppService;
	}

	public void setTdscBidderViewService(TdscBidderViewService tdscBidderViewService) {
		this.tdscBidderViewService = tdscBidderViewService;
	}

	public TdscBlockInfoService getTdscBlockInfoService() {
		return tdscBlockInfoService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscLocalTradeService(TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setTdscReturnBailService(TdscReturnBailService tdscReturnBailService) {
		this.tdscReturnBailService = tdscReturnBailService;
	}

	private static String OPERATOR_ADD = "ADD";

	private static String OPERATOR_MOD = "MODIFY";

	public ActionForward printSalesConfirmation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fromMenu = request.getParameter("fromMenu");

		String type = request.getParameter("type");

		String strTradeStatus = request.getParameter("tradeStatus") + "";

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
		condition.setBlockName(StringUtil.GBKtoISO88591(request.getParameter("blockName")));
		condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(request.getParameter("blockNoticeNo")));
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

		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {// 历史交易
			request.setAttribute("tradeStatus", strTradeStatus);
			condition.setStatus("02");// (00-未交易;01-交易中;02-交易结束)
			condition.setTranResult("01");// 交易结果 00未交易 01 交易成功；02 交易失败（流标）；04
											// 终止交易；
		} else {// 当前交易
			condition.setStatus("01");// (00-未交易;01-交易中;02-交易结束)
		}

		condition.setOrderKey("blockNoticeNo");
		// condition.setOrderType("desc");
		// 2011-03-21 需要把过滤掉的数据加入到list 并显示出来
		// 意向地块没有人竞买的地块
		PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);

		if (tdscBlockAppViewPageList != null) {
			List tempList = tdscBlockAppViewPageList.getList();
			if (null != tempList && tempList.size() > 0) {
				HashMap appIdMap = new HashMap();
				for (int i = 0; i < tempList.size(); i++) {
					TdscBlockAppView commonInfo = (TdscBlockAppView) tempList.get(i);
					if (null != commonInfo.getAppId() && !"".equals(commonInfo.getAppId())) {
						TdscBlockInfo tdscBlockInfo = tdscScheduletableService.getBlockInfoApp(commonInfo.getBlockId());
						if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0)) > 0) {
							appIdMap.put(commonInfo.getAppId(), "1");
						}
						TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(commonInfo.getAppId());
						if (tdscListingInfo != null) {
							commonInfo.setResultCert(tdscListingInfo.getListCert());
							commonInfo.setResultDate(tdscListingInfo.getListDate());
						}
						//处理联合竞买申请表显示
						TdscBidderApp bidderApp = tdscBidderAppService.getBidderAppByCertNo(commonInfo.getResultCert());
						if(bidderApp!=null){
							List jmsqFileList = tdscBlockInfoService.findFileRefByBusId(bidderApp.getBidderId(), GlobalConstants.JMSQ_FILE);
							List lhjmFileList = tdscBlockInfoService.findFileRefByBusId(bidderApp.getBidderId(), GlobalConstants.LHJM_FILE);
							if(jmsqFileList!=null&&jmsqFileList.size()>0){
								FileRef jmsqFile = (FileRef)jmsqFileList.get(0);
								commonInfo.setJmsqFileId(jmsqFile.getFileId()+"."+jmsqFile.getFileType());
							}
							if(lhjmFileList!=null&&lhjmFileList.size()>0){
								FileRef lhjmFile = (FileRef)lhjmFileList.get(0);
								commonInfo.setLhjmFileId(lhjmFile.getFileId()+"."+lhjmFile.getFileType());
							}
						}
					}
				}
				request.setAttribute("appIdMap", appIdMap);
			}
		}
		
		request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
		request.setAttribute("condition", condition);
		request.setAttribute("type", type);

		return mapping.findForward("toPrintSalesConfirmationPage");
	}

	/**
	 * 打印所有竞得人
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printAllBidderPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");

		String stitle = "";
		List retList = new ArrayList();
		// 根据公告查询所有地块
		// 根据地块，查询所有竞得人
		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setNoticeId(noticeId);
		cond.setOrderKey("blockNoticeNo");
		List tmpList = commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);
		if (tmpList != null && tmpList.size() > 0) {
			for (int i = 0; i < tmpList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) tmpList.get(i);

				stitle = appView.getNoitceNo().substring(0, 10) + Integer.parseInt(appView.getNoitceNo().substring(10, 12)) + "号";
				TdscBlockTranApp tranApp = tdscLocalTradeService.getTdscBlockTranApp(appView.getAppId());
				if (tranApp != null && !StringUtil.isEmpty(tranApp.getResultName())) {
					TdscBidderView tdscBidderView = tdscBidderViewService.getBidderViewByPersonName(tranApp.getResultName());

					TdscBidderForm bean = new TdscBidderForm();

					// appView.getBlockNoticeNo();// 地块编号
					// appView.getBlockName();// 地块名称
					// tranApp.getResultName(); // 竞得单位名称
					//
					// tdscBidderView.getLinkManName(); //联系人
					// tdscBidderView.getBidderLxdh();//联系电话

					bean.setBlockNoticeNo(appView.getBlockNoticeNo());
					bean.setBlockName(appView.getBlockName());
					bean.setBidderName(tranApp.getResultName());
					bean.setLinkManName(tdscBidderView.getLinkManName());
					bean.setBidderLxdh(tdscBidderView.getBidderLxdh());

					retList.add(bean);
				}
			}
		}

		request.setAttribute("rptTitle", stitle + "竞得单位联系人清单");
		request.setAttribute("retList", retList);

		return mapping.findForward("printBidderedPersonList");

	}

	/**
	 * 打印竞买资格确认书 按照 竞买单位名称、卡号查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintBidderZGZS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderName = request.getParameter("bidderName");
		String kahao = request.getParameter("kahao");// 芯片号
		String fromMenu = request.getParameter("fromMenu");
		if (!"Y".equals(fromMenu) && (!isEmpty(bidderName) || !isEmpty(kahao))) {
			String pageNo = request.getParameter("currentPage");

			// 查询列表
			TdscBidderCondition bidderCond = new TdscBidderCondition();
			if (!isEmpty(bidderName))
				bidderCond.setBidderName(StringUtil.GBKtoISO88591(bidderName));
			if (!isEmpty(kahao))
				bidderCond.setYktBh(kahao);

			List retList = new ArrayList();

			List queryList = (List) tdscBidderViewService.queryBidderByNameKahao(bidderCond);
			if (queryList != null && queryList.size() > 0) {
				for (int i = 0; i < queryList.size(); i++) {
					TdscBidderView bidderView = (TdscBidderView) queryList.get(i);
					if (!StringUtil.isEmpty(bidderView.getNoticeId())) {
						System.out.println("NoticeId is : " + bidderView.getNoticeId());
						try {
							// 此处如果noticeId 找不到记录，仅对于非正常流程数据，使用该方法使程序继续走下去

							String noticeNo = tdscBidderAppService.getNoticeNoByNoticeId(bidderView.getNoticeId());
							bidderView.setNoticeId(noticeNo);

							retList.add(bidderView);
						} catch (Exception e) {
							continue;
						}
					} else {
						continue;
					}
				}
			}

			PageList pageList = new PageList();
			int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
			int currentPage = 0;
			if (pageNo != null && pageNo.trim().equals("") == false)
				currentPage = Integer.parseInt(pageNo);

			if (pageNo == null || Integer.parseInt(pageNo) < 1)
				currentPage = 1;

			pageList = PageUtil.getPageList(retList, pageSize, currentPage);

			request.setAttribute("queryList", retList);
			request.setAttribute("pageList", pageList);
			if (!isEmpty(bidderName))
				bidderCond.setBidderName(bidderName);
			request.setAttribute("condition", bidderCond);
		}
		return mapping.findForward("toPrintBidderZGZS");

	}

	public ActionForward saveSelectHaoPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strBidderId = (String) request.getParameter("bidderId");
		String strConNum = (String) request.getParameter("conNum");

		tdscBidderAppService.saveSelectHaoPai(strBidderId, strConNum);

		// 如果是意向人，在其报名选择号牌后，需要对他的意向地块插入第一轮挂牌记录，挂牌价格为起始价，时间为公告起始时间
		TdscBidderApp tdscBidderApp = tdscBidderAppService.queryBidderAppInfo(strBidderId);
		if ("1".equals(tdscBidderApp.getIsPurposePerson())) {
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(strBidderId);
			String purposeAppId = tdscBidderPersonApp.getPurposeAppId();
			tdscLocalTradeService.insertListingAppOfPurposePerson(tdscBidderApp, purposeAppId);
		}

		request.setAttribute("saveFlag", "SUCCESS");

		return mapping.findForward("bidderSelectNumPage");
	}

	/**
	 * 选择号牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward selectHaoPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");
		String bidderId = (String) request.getParameter("bidderId");

		List list = this.tdscBidderAppService.findAppByNoticeId(noticeId);
		Set set = new HashSet();
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) list.get(i);
			set.add(app.getConNum());
		}
		request.setAttribute("contaisNum", set);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("bidderId", bidderId);

		return mapping.findForward("bidderSelectNumPage");
	}

	/**
	 * 打印竞买资格确认书
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toBidderCredentials(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderName = (String) request.getParameter("bidderName");
		// String bidderCardNum = (String)
		// request.getParameter("bidderCardNum");

		List retList = tdscBidderAppService.queryBidderCredList(bidderName);

		request.setAttribute("BidderCredList", retList);
		return mapping.findForward("toBidderCredentialsPage");

	}

	/**
	 * 删除竞买人
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");
		String bidderId = (String) request.getParameter("bidderId");
		String bidderPersonId = (String) request.getParameter("bidderPersonId");
		// 根据ID删除竞买人 TDSC_BIDDER_APP /TDSC_BIDDER_PERSON_APP
		tdscBidderAppService.delBidderInfo(bidderId, bidderPersonId);

		StringBuffer sb = new StringBuffer("bidderApp.do?method=toListBidders");
		sb.append("&noticeId=").append(noticeId);
		ActionForward f = new ActionForward(sb.toString(), true);
		return f;

	}

	/**
	 * 跳转到 新增竞买人页面 by xys 2011-02-22
	 */
	public ActionForward toAddBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");

		List bankDicList = tdscBidderViewService.queryBankDicList();
		if (bankDicList != null && bankDicList.size() > 0) {
			for (int i = 0; i < bankDicList.size();) {
				TdscDicBean tdscDicBean = (TdscDicBean) bankDicList.get(i);
				if ("0".equals(tdscDicBean.getIsValidity())) {
					bankDicList.remove(i);
				} else {
					i++;
				}
			}
		}
		request.setAttribute("bankDicList", bankDicList);

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (!StringUtil.isEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			condition.setStatus("01");
			condition.setOrderKey("xuHao");
			// 通过通用接口查询土地视图
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		request.setAttribute("noticeId", noticeId);

		request.setAttribute("opt", OPERATOR_ADD);

		return mapping.findForward("toAddBidPerPage");
	}

	/**
	 * 转到修改竞买人的页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toModBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");
		// String appId = (String) request.getParameter("appId");
		String bidderId = (String) request.getParameter("bidderId");
		String bidderPersonId = (String) request.getParameter("bidderPersonId");
		String isPurposePerson = (String) request.getParameter("isPurposePerson");
		String strSaveOk = (String) request.getParameter("saveOk");

		request.setAttribute("saveOk", strSaveOk);

		String ifPrint = (String) request.getParameter("ifPrint");
		if (!StringUtil.isEmpty(ifPrint) && "Y".equals(ifPrint))
			request.setAttribute("ifPrint", "Y");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (!StringUtil.isEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			//condition.setStatus("01");// 仅查询交易中的地块，过滤终止的地块
			condition.setOrderKey("xuHao");
			// 通过通用接口查询土地视图
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		TdscBidderPersonApp bidderPersonApp = null;
		List tdscBidderMaterial = null;

		// 1. 查询所有的 appIds 和 保证金
		// 2. 查询文件列表和其他文件列表
		String appIds = "";
		// String bzjDzse = "";
		TdscBidderApp bidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
		appIds = bidderApp.getAppId();
		bidderPersonApp = tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());
		tdscBidderMaterial = tdscBidderAppService.queryOnePerMat(bidderPersonApp.getBidderPersonId());

		// 3. 查询受托人信息, 在 bidderApp 中
		// 4. 查询转托人信息, 在 bidderApp 中
		// 5. 查询竞买人信息, 在 bidderPersonApp 中
		// 6. 查询交易卡信息, 在 bidderApp 中

		request.setAttribute("bidderApp", bidderApp);
		request.setAttribute("bidderPersonApp", bidderPersonApp);
		request.setAttribute("tdscBidderMaterial", tdscBidderMaterial);
		request.setAttribute("appIds", appIds);
		// request.setAttribute("bzjDzse", bzjDzse);

		// 从退还保证金信息表中，读取保证金，银行信息；
		List bzjInfoList = tdscBidderAppService.queryTdscReturnBailList(bidderId);
		request.setAttribute("bzjInfoList", bzjInfoList);

		// 保证金银行字典
		List bankDicList = tdscBidderViewService.queryBankDicList();
		if (bankDicList != null && bankDicList.size() > 0) {
			for (int i = 0; i < bankDicList.size();) {
				TdscDicBean tdscDicBean = (TdscDicBean) bankDicList.get(i);
				if ("0".equals(tdscDicBean.getIsValidity())) {
					bankDicList.remove(i);
				} else {
					i++;
				}
			}
		}
		request.setAttribute("bankDicList", bankDicList);

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("isPurposePerson", isPurposePerson);

		request.setAttribute("opt", OPERATOR_MOD);

		if (StringUtils.isNotBlank(bidderApp.getConNum())) {
			request.setAttribute("saveFlag", "SUCCESS");
		}

		// 查询该竞买人的保证金关联情况
		List bankAppList = tdscBankAppService.queryTdscBankAppListByBidderId(bidderId);
		request.setAttribute("bankAppList", bankAppList);

		return mapping.findForward("toAddBidPerPage");
	}

	private void mergeYiXiangRen(String noticeId) {

		// 1. 删除一条数据
		// 2. 合并被删除数据的 app_id
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		condition.setNoticeId(noticeId);
		condition.setOrderKey("blockNoticeNo");
		// 通过通用接口查询土地视图
		List appViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		List tdscBidderPersonAppList = new ArrayList();

		if (appViewList != null && appViewList.size() > 0) {
			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) appViewList.get(i);
				if ("1".equals(appView.getIsPurposeBlock())) {// 仅对意向地块进行处理
					// 没有受理过的意向人，只有 appId 和 IsPurposeBlock
					TdscBidderApp tdscBidderApp = tdscBidderAppService.getYixiangPerson(appView.getAppId());
					if (tdscBidderApp != null) {
						TdscBidderPersonApp bidderPersonApp = tdscBidderAppService.getTdscBidderPersonByBidderId(tdscBidderApp.getBidderId());
						tdscBidderPersonAppList.add(bidderPersonApp);
					}
				}
			}
		}

		// 对tdscBidderPersonAppList按竞买人/意向人名称排序
		if (tdscBidderPersonAppList != null && tdscBidderPersonAppList.size() > 2) {
			Collections.sort(tdscBidderPersonAppList, new Comparator() {
				public int compare(Object o1, Object o2) {
					TdscBidderPersonApp bidderPersonApp1 = (TdscBidderPersonApp) o1;
					TdscBidderPersonApp bidderPersonApp2 = (TdscBidderPersonApp) o2;
					return bidderPersonApp1.getBidderName().compareTo(bidderPersonApp2.getBidderName());
				}
			});
		}

		String yixiangAppIds = "";
		String bidderId = "";
		String bidderPersonId = "";
		String bidderName = "";

		if (tdscBidderPersonAppList != null && tdscBidderPersonAppList.size() > 2) {
			for (int i = 0; i < tdscBidderPersonAppList.size(); i++) {
				TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppList.get(i);
				if (bidderName.equals(bidderPersonApp.getBidderName())) {
					bidderId += bidderPersonApp.getBidderId() + ",";
					bidderPersonId += bidderPersonApp.getBidderPersonId() + ",";
					yixiangAppIds += bidderPersonApp.getPurposeAppId() + ",";

					if (!StringUtil.isEmpty(bidderId) && !StringUtil.isEmpty(bidderPersonId)) {
						// 删除数据 TDSC_BIDDER_APP , TDSC_BIDDER_PERSON_APP
						String[] tmpBidderIds = bidderId.split(",");
						String[] tmpBidderPersonIds = bidderPersonId.split(",");
						for (int m = 0; m < tmpBidderIds.length - 1; m++) {
							tdscBidderAppService.delBidderInfo(tmpBidderIds[m], tmpBidderPersonIds[m]);
						}
						tdscBidderAppService.modifyPurposePersonAppIds(tmpBidderIds[tmpBidderIds.length - 1], tmpBidderPersonIds[tmpBidderPersonIds.length - 1], yixiangAppIds);
					}
				} else {
					bidderName = bidderPersonApp.getBidderName();
					bidderId = bidderPersonApp.getBidderId() + ",";
					bidderPersonId = bidderPersonApp.getBidderPersonId() + ",";
					yixiangAppIds = bidderPersonApp.getPurposeAppId() + ",";
				}
			}
		}
	}

	/**
	 * 查询本次公告下的所有竞买人
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	public ActionForward toListBidders(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		String noticeId = (String) request.getParameter("noticeId");

		// 需要处理合并意向人名称相同的数据；
		mergeYiXiangRen(noticeId);

		List retList = new ArrayList();
		Map map = new HashMap();
		if (!StringUtil.isEmpty(noticeId)) {
			String bidderName = "";

			// 查询本次公告所有的竞买人/意向人
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);

			List appViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (appViewList != null && appViewList.size() > 0)
				for (int i = 0; i < appViewList.size(); i++) {
					TdscBlockAppView appView = (TdscBlockAppView) appViewList.get(i);

					List bidderAppList = null;
					// if ("tdsc".equals(user.getUserId()) ||
					// "万钧".equals(user.getDisplayName()) ||
					// "唐珣".equals(user.getDisplayName()))
					// if ("tdsc".equals(user.getUserId()))
					// bidderAppList =
					// tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());
					// else {
					// bidderAppList =
					// tdscBidderAppService.queryBidderAppListLikeAppIdAndUserId(appView.getAppId(),
					// user.getUserId());
					// // 需要加入所有意向人的单位
					// List tempList =
					// tdscBidderAppService.queryBidderAppYixiangList(noticeId);
					// // bidderAppList.addAll();
					// System.out.println(tempList);
					// }

					bidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());

					if (bidderAppList != null && bidderAppList.size() > 0)
						for (int m = 0; m < bidderAppList.size(); m++) {
							TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);

							if ("1".equals(bidderApp.getIsPurposePerson())) {
								TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());

								TdscBidderForm bean = new TdscBidderForm();

								bean.setNoticeId(noticeId);
								bean.setAppId(bidderApp.getAppId());
								System.out.println("bidderApp.getBidderId()=" + bidderApp.getBidderId());
								bean.setBidderId(bidderApp.getBidderId());
								bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
								bean.setBidderName(bidderPersonApp.getBidderName());
								bean.setAcceptNo(bidderApp.getAcceptNo());
								bean.setCertNo(bidderApp.getCertNo());

								bean.setIsPurposePerson(bidderApp.getIsPurposePerson());

								map.put(bidderPersonApp.getBidderName(), bean);

							} else {
								if (StringUtils.trimToEmpty(bidderApp.getUserId()).equals(user.getUserId()) || "tdsc".equals(user.getUserId())) {
									TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());

									TdscBidderForm bean = new TdscBidderForm();

									bean.setNoticeId(noticeId);
									bean.setAppId(bidderApp.getAppId());
									System.out.println("bidderApp.getBidderId()=" + bidderApp.getBidderId());
									bean.setBidderId(bidderApp.getBidderId());
									bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
									bean.setBidderName(bidderPersonApp.getBidderName());
									bean.setAcceptNo(bidderApp.getAcceptNo());
									bean.setCertNo(bidderApp.getCertNo());

									bean.setIsPurposePerson(bidderApp.getIsPurposePerson());

									map.put(bidderPersonApp.getBidderName(), bean);
								}
							}

						}

				}

		}

		if (map.size() != 0) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				// Object key = entry.getKey();
				Object value = entry.getValue();
				retList.add((TdscBidderForm) value);
			}
			request.setAttribute("allBidderList", retList);
		}

		return mapping.findForward("toListBiddersPage");
	}

	/**
	 * 新增修改竞买人/意向受让人
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveJmrInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String operatorType = request.getParameter("opt") + "";

		String noticeId = request.getParameter("noticeId") + "";
		// List bidderIds = new ArrayList();

		// 获得区县信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		// 构造 提交材料的 List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// 添加实体属性
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				// tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());

				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				// tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		String appIds[] = tdscBidderForm.getAppIds();

		// 保证金银行
		String[] bzjDzqk = tdscBidderForm.getBzjDzqks();
		bzjDzqk = trimBank(bzjDzqk);
		// save into TDSC_RETURN_BAIL
		List tdscReturnBailList = new ArrayList();

		// 资格证书编号 唯一
		if (OPERATOR_ADD.equals(operatorType)) {
//			String certNo = tdscBidderAppService.generateCertNo();
//			tdscBidderForm.setCertNo(certNo);
//			tdscBidderForm.setAcceptNo(certNo);
//
//			tdscBidderForm.setYktXh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡编号
//			tdscBidderForm.setYktBh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡芯片号
		}

		String bidderId = "";
		boolean isPurposePerson = false;

		String strAppId = "";

		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {

				String appId = appIds[i];

				String bzjBank = bzjDzqk[i];

				strAppId += appIds[i] + ",";

				TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
				condition.setAppId(appId);
				TdscBlockAppView appView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);

				TdscReturnBail returnBail = new TdscReturnBail();
				returnBail.setPlanId(appView.getPlanId());
				returnBail.setAppId(appId);
				returnBail.setBlockId(appView.getBlockId());
				// returnBail.setBidderId(bidderId);
				returnBail.setBidderBail(appView.getMarginAmount());
				returnBail.setBzjBank(bzjBank);
				returnBail.setIfReturn("00");// 未退还

				tdscReturnBailList.add(returnBail);

				// 地块是否有意向人
				if ("1".equals(appView.getIsPurposeBlock())) {
					isPurposePerson = true;
				} else {
					// TODO 无意向竞买人的地块，新增的第一个竞买人即第一轮挂牌，需写入表 TDSC_LISTING_APP and
					// TDSC_LISTING_INFO
					// 判断该地块是否已经有记录在表中

					// 1. save TDSC_LISTING_INFO (listing_id=+1, app_id,
					// curr_price=该块土地的初始价格, curr_round=1, list_date=now date,
					// ykt_xh=卡号, list_cert=资格确认书编号)
					// 2. save TDSC_LISTING_APP (list_app_id=+1, listing_id,
					// listing_ser=1, price_type=1, list_cert=, list_price=,
					// list_date=, ytk_xh=, app_id)
					// String yktXh = tdscBidderForm.getYktXh();
					// BigDecimal initPrice = appView.getInitPrice();

					// tdscLocalTradeService.makeFirstPersonIsInitPrice(appId,
					// yktXh, certNo, initPrice);
				}
			}
		}

		String bankAppIdStr = tdscBidderForm.getBankAppIds();
		String[] bankAppIds = null;
		if (StringUtils.isNotBlank(bankAppIdStr)) {
			bankAppIds = bankAppIdStr.split(",");
		}

		if (OPERATOR_ADD.equals(operatorType)) {
			bidderId = tdscBidderAppService.saveJmrInfo(strAppId, false, tdscBidderForm, tdscBidMatList, user);
			// 保证金信息保存
			tdscBidderAppService.saveReturnBzjInfo(bidderId, tdscReturnBailList);

			if (bankAppIds != null && bankAppIds.length > 0) {
				for (int i = 0; i < bankAppIds.length; i++) {
					TdscBankApp tdscBankApp = tdscBankAppService.getTdscBankAppById(bankAppIds[i]);
					tdscBankApp.setStatus("1");// 状态，1为已关联
					tdscBankApp.setBidderId(bidderId);
					tdscBankApp.setNoticeId(tdscBidderForm.getNoticeId());
					tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankApp);
				}
			}
		}
		if (OPERATOR_MOD.equals(operatorType)) {
			TdscBidderApp tdscBidderApp = tdscBidderAppService.queryBidderAppInfo(tdscBidderForm.getBidderId());
			if ("1".equals(tdscBidderApp.getIsPurposePerson()))
				isPurposePerson = true;
			else
				isPurposePerson = false;

			String purposeAppId = "";
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(tdscBidderForm.getBidderId());
			purposeAppId = tdscBidderPersonApp.getPurposeAppId();
			tdscBidderForm.setPurposeAppId(purposeAppId);
			tdscBidderForm.setYktXh(tdscBidderApp.getYktXh());// 取消交易卡，用资格证书编号代替交易卡编号
			tdscBidderForm.setYktBh(tdscBidderApp.getYktBh());// 取消交易卡，用资格证书编号代替交易卡芯片号

			bidderId = tdscBidderAppService.updateJmrInfo(strAppId, isPurposePerson, tdscBidderForm, tdscBidMatList, user);
			// 保证金信息保存
			tdscBidderAppService.saveReturnBzjInfo(bidderId, tdscReturnBailList);

			// 先清除掉该竞买人的保证金记录信息，再做保存
			List tdscBankAppOldList = tdscBankAppService.queryTdscBankAppListByBidderId(bidderId);
			if (tdscBankAppOldList != null && tdscBankAppOldList.size() > 0) {
				for (int i = 0; i < tdscBankAppOldList.size(); i++) {
					TdscBankApp tdscBankAppOld = (TdscBankApp) tdscBankAppOldList.get(i);
					tdscBankAppOld.setStatus("0");// 状态，0为未关联
					tdscBankAppOld.setBidderId("");// 清除与该竞买人的关联关系
					tdscBankAppOld.setNoticeId("");
					tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankAppOld);
				}
			}
			for (int i = 0; null != bankAppIds && i < bankAppIds.length; i++) {
				TdscBankApp tdscBankApp = tdscBankAppService.getTdscBankAppById(bankAppIds[i]);
				tdscBankApp.setStatus("1");// 状态，1为已关联
				tdscBankApp.setBidderId(bidderId);
				tdscBankApp.setNoticeId(tdscBidderForm.getNoticeId());
				tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankApp);
			}
		}

		request.setAttribute("opt", OPERATOR_MOD);
		// request.setAttribute("ifPrint", "Y");

		// return mapping.findForward("saveJmrInfoOK");
		TdscBidderPersonApp bidderPersonApp = tdscBidderAppService.queryBidPerson(bidderId);

		StringBuffer sb = new StringBuffer("bidderApp.do?method=toModBidder");
		sb.append("&noticeId=").append(noticeId);
		sb.append("&bidderId=").append(bidderId);
		sb.append("&bidderPersonId=").append(bidderPersonApp.getBidderPersonId());
		sb.append("&isPurposePerson=").append(isPurposePerson);
		sb.append("&ifPrint=Y");
		sb.append("&saveOk=SUCCESS");
		ActionForward f = new ActionForward(sb.toString(), true);
		return f;
	}

	private String[] trimBank(String[] bzjBank) {
		String tmp = "";
		if (bzjBank != null && bzjBank.length > 0) {
			for (int i = 0; i < bzjBank.length; i++) {
				if (!StringUtils.isEmpty(bzjBank[i])) {
					tmp += bzjBank[i] + ",";
				}
			}
		}
		return tmp.split(",");
	}

	/**
	 * 根据条件获得竞买土地列表
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */

	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscNoticeAppCondition noticeCondition = new TdscNoticeAppCondition();
		List appIdList = new ArrayList();
		List noticeIdList = new ArrayList();
		List retList = new ArrayList();

		// 如果是流程中的节点需要设置节点编号 RECORD_BIDDER_APPLY
		condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
		condition.setOrderKey("blockNoticeNo");
		/* 设置页面行数 */
		// condition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// PageList pageList = (PageList)
		// commonQueryService.queryTdscBlockAppViewPageList(condition);
		// 获取用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

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
			// condition.setUserId(user.getUserId());
		}
		// 根据条件查询在受理状态的地块appId列表
		List tdscblockAppViewList = (List) commonQueryService.queryTdscBlockAppViewList(condition);
		if (tdscblockAppViewList != null && tdscblockAppViewList.size() > 0) {
			for (int i = 0; i < tdscblockAppViewList.size(); i++) {
				TdscBlockAppView tdscblockAppView = (TdscBlockAppView) tdscblockAppViewList.get(i);
				if (tdscblockAppView != null)
					appIdList.add(tdscblockAppView.getAppId());
			}
			noticeIdList = tdscBidderAppService.findNoticeIdListByAppIdList(appIdList);
			if (noticeIdList != null && noticeIdList.size() > 0) {
				bindObject(noticeCondition, form);
				if (!StringUtil.isEmpty(noticeCondition.getUniteBlockName()))
					noticeCondition.setUniteBlockName(StringUtil.GBKtoISO88591(noticeCondition.getUniteBlockName()));
				if (!StringUtil.isEmpty(noticeCondition.getTradeNum()))
					noticeCondition.setTradeNum(StringUtil.GBKtoISO88591(noticeCondition.getTradeNum()));
				if (!StringUtil.isEmpty(noticeCondition.getTransferMode()))
					noticeCondition.setTransferMode(StringUtil.GBKtoISO88591(noticeCondition.getTransferMode()));

				noticeCondition.setNoticeIdList(noticeIdList);
				retList = tdscBidderAppService.findNoticeListByCondition(noticeCondition);

				// 查询该公告下所有地块是否存在意向受让人，如果其中一个地块存在，则UI “操作” 中显示 “管理竞买人”，
				// 如果没有任何一个地块存在，只显示“新增”
				if (retList != null && retList.size() > 0)
					for (int i = 0; i < retList.size(); i++) {
						TdscNoticeApp noticeApp = (TdscNoticeApp) retList.get(i);
						TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
						cond.setNoticeId(noticeApp.getNoticeId());
						List appList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);
						if (appList != null && appList.size() > 0)
							for (int j = 0; j < appList.size(); j++) {
								TdscBlockAppView appView = (TdscBlockAppView) appList.get(j);
								if ("1".equals(appView.getIsPurposeBlock())) {
									noticeApp.setLinkManTel("displayManager"); // 表示此地块有意向人，此公告可以显示“管理竞买人”
									// 按钮
									break;
								}

								// 查询所有竞买人列表，appId 需要 like
								List tmpList = tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());

								if (tmpList != null && tmpList.size() > 0) {
									noticeApp.setLinkManTel("displayManager"); // 表示此地块有竞买人，此公告可以显示“管理竞买人”
									// 按钮
									break;
								}
							}
					}
			}
		}

		// 拼装分页信息
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage")))
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		PageList retPageList = PageUtil.getPageList(retList, pageSize, currentPage);

		request.setAttribute("condition", noticeCondition);
		request.setAttribute("pageList", retPageList);

		return mapping.findForward("jmrNoticeList");
	}

	// deleted by xys 2011-02-22
	// public ActionForward query(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) throws
	// Exception {
	// // 获取页面参数
	// TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
	// bindObject(condition, form);
	// // 获得用户信息
	// SysUser user = (SysUser)
	// request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
	// String appUserId = String.valueOf(user.getUserId());
	// // 判断该用户是否为“受理部总受理”
	// List buttonList = (List)
	// request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
	// Map buttonMap = new HashMap();
	// for (int j = 0; j < buttonList.size(); j++) {
	// String id = (String) buttonList.get(j);
	// buttonMap.put(id, buttonList.get(j));
	// }
	// // 判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
	// if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
	// condition.setUserId(user.getUserId());
	// }
	// // 如果是流程中的节点需要设置节点编号 RECORD_BIDDER_APPLY
	// condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
	// /* 设置页面行数 */
	// condition.setPageSize(((Integer)
	// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
	// condition.setOrderKey("blockNoticeNo");
	//
	// // 查询列表
	// PageList pageList = (PageList)
	// commonQueryService.queryTdscBlockAppViewPageList(condition);
	// request.setAttribute("pageList", pageList);
	// request.setAttribute("condition", condition);
	// // 显示每个地块的竞买申请份数
	// if (pageList != null) {
	// List appList = (List) pageList.getList();
	// if (appList != null && appList.size() > 0) {
	// List retCountList = new ArrayList();
	// for (int i = 0; i < appList.size(); i++) {
	// TdscBlockAppView app = (TdscBlockAppView) appList.get(i);
	//
	// List countList = new ArrayList();
	// // 如果是“受理部总受理”，则查询所有数据；否则就只查询本窗口受理的数据
	// if (buttonMap != null) {
	// if (buttonMap.get(GlobalConstants.BUTTON_ID_JMSQZSL) != null) {
	// countList = (List)
	// tdscBidderAppService.findPageBidderList(app.getAppId());
	// } else {
	// countList = (List)
	// tdscBidderAppService.findPageBidderListByUserId(app.getAppId(),
	// appUserId);
	// }
	// }
	// retCountList.add(countList);
	// }
	// request.setAttribute("retCountList", retCountList);
	// }
	// }
	//
	// return mapping.findForward("bidderAppList");
	// }

	/**
	 * 根据条件获得竞买土地单位列表
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward daochudwInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		bindObject(condition, form);

		String appId = (String) request.getParameter("appId");
		String blockNoticeNo = (String) request.getParameter("blockNoticeNo");
		// String blockNoticeNo_like = blockNoticeNo.substring(3);

		// 取得招牌挂编号和出让方式
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		if (appId != null && !"".equals(appId)) {
			condition.setAppId(appId);
			tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		}

		List countList = new ArrayList();
		List personList = new ArrayList();

		TdscBidderCondition Listcondition = new TdscBidderCondition();
		Listcondition.setNoticeId(tdscBlockAppView.getNoticeId());
		// Listcondition.setAcceptNo(blockNoticeNo_like);
		countList = (List) tdscBidderAppService.findBidderListByJMSQZSL(Listcondition);

		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		// List countList2 = (List)
		// tdscBidderAppService.findPageBidderList(appId);
		if (countList != null && countList.size() > 0) {
			for (int i = 0; i < countList.size(); i++) {
				TdscBidderApp app = (TdscBidderApp) countList.get(i);
				String bidderId = app.getBidderId();
				String app_id = app.getAppId();
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBidderAppService.findTdscBlockTranApp(app_id);
				blockNoticeNo = tdscBlockTranApp.getBlockNoticeNo();
				app.setReviewResult(blockNoticeNo);

				String peasonName = "";
				personList = (List) tdscBidderAppService.queryBidderPersonList(bidderId);

				if (personList != null & personList.size() > 0) {
					for (int j = 0; j < personList.size(); j++) {
						tdscBidderPersonApp = (TdscBidderPersonApp) personList.get(j);
						// 单独竞买
						if (j == 0)
							peasonName = tdscBidderPersonApp.getBidderName();

						// 联合竞买
						if (j > 0) {
							peasonName += "、" + tdscBidderPersonApp.getBidderName();
						}
					}
				}
				app.setReviewOpnn(peasonName);
			}
		}

		request.setAttribute("tradeNum", tdscBlockAppView.getTradeNum());
		request.setAttribute("transferMode", tdscBlockAppView.getTransferMode());
		request.setAttribute("countList", countList);

		return mapping.findForward("daochudwInfo");
	}

	/**
	 * 查询所有在办的出让公告
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAllNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String bidderType = (String) request.getParameter("bidderType");
		String type = (String) request.getParameter("type");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		TdscNoticeAppCondition noticeCondition = new TdscNoticeAppCondition();
		List appIdList = new ArrayList();
		List noticeIdList = new ArrayList();
		List retList = new ArrayList();

		// 如果是流程中的节点需要设置节点编号 RECORD_BIDDER_APPLY
		condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
		condition.setOrderKey("blockNoticeNo");
		/* 设置页面行数 */
		// condition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// PageList pageList = (PageList)
		// commonQueryService.queryTdscBlockAppViewPageList(condition);
		// 获取用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

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
		// 根据条件查询在受理状态的地块appId列表
		List tdscblockAppViewList = (List) commonQueryService.queryTdscBlockAppViewList(condition);
		if (tdscblockAppViewList != null && tdscblockAppViewList.size() > 0) {
			for (int i = 0; i < tdscblockAppViewList.size(); i++) {
				TdscBlockAppView tdscblockAppView = (TdscBlockAppView) tdscblockAppViewList.get(i);
				if (tdscblockAppView != null)
					appIdList.add(tdscblockAppView.getAppId());
			}

			noticeIdList = tdscBidderAppService.findNoticeIdListByAppIdList(appIdList);
			if (noticeIdList != null && noticeIdList.size() > 0) {
				bindObject(noticeCondition, form);

				if (noticeCondition.getUniteBlockName() != null && !"".equals(noticeCondition.getUniteBlockName()))
					noticeCondition.setUniteBlockName(StringUtil.GBKtoISO88591(noticeCondition.getUniteBlockName()));
				if (noticeCondition.getTradeNum() != null && !"".equals(noticeCondition.getTradeNum())) {
					noticeCondition.setTradeNum(StringUtil.GBKtoISO88591(noticeCondition.getTradeNum()));
				}

				noticeCondition.setNoticeIdList(noticeIdList);
				retList = tdscBidderAppService.findNoticeListByCondition(noticeCondition);
			}
		}

		// 拼装分页信息
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();

		PageList retPageList = PageUtil.getPageList(retList, pageSize, currentPage);

		if (noticeCondition.getUniteBlockName() != null && !"".equals(noticeCondition.getUniteBlockName())) {
			noticeCondition.setUniteBlockName(StringUtil.ISO88591toGBK(noticeCondition.getUniteBlockName()));
		}
		if (noticeCondition.getTradeNum() != null && !"".equals(noticeCondition.getTradeNum())) {
			noticeCondition.setTradeNum(StringUtil.ISO88591toGBK(noticeCondition.getTradeNum()));
		}
		request.setAttribute("condition", noticeCondition);
		request.setAttribute("bidderType", bidderType);
		request.setAttribute("type", type);
		request.setAttribute("pageList", retPageList);

		return mapping.findForward("jmrNoticeList");
	}

	/**
	 * 跳转到 新增竞买人页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddBidPer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = (String) request.getParameter("noticeId");
		String bidType = (String) request.getParameter("bidderType");
		String bidderId = (String) request.getParameter("bidderId");
		if (StringUtils.isNotEmpty(bidderId)) {
			TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
			TdscBidderPersonApp tdscBidderPersonApp = this.tdscBidderAppService.getTdscBidderPersonByBidderId(bidderId);
			request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);

		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (noticeId != null) {
			condition.setNoticeId(noticeId);
			condition.setOrderKey("blockNoticeNo");
			// 通过通用接口查询土地视图
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		request.setAttribute("bidType", bidType);
		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("toAddBidPerPage");
	}

	/**
	 * **跳转到 联合竞买--新增竞买人页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addUnionBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String jnfs = (String) request.getParameter("jnfs");
		String hasHead = (String) request.getParameter("hasHead");
		String transferMode = (String) request.getParameter("transferMode");
		String[] appIds = tdscBidderForm.getAppIds();
		// 根据appId查询与该条有关的信息
		if (appIds != null && appIds.length > 0) {
			TdscBaseQueryCondition tdscBaseQuerycondition = new TdscBaseQueryCondition();
			tdscBaseQuerycondition.setAppId(appIds[0]);
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQuerycondition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		}// 设置返回的blockType transferMode bzjJnfs
		request.setAttribute("bzjJnfs", jnfs);
		request.setAttribute("hasHead", hasHead);
		request.setAttribute("transferMode", transferMode);

		return mapping.findForward("addUnionBidderPage");
	}

	/**
	 * 联合竞买---添加多个竞买人
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addUnBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String noticeId = tdscBidderForm.getNoticeId();
		String yktBh = tdscBidderForm.getYktBh();
		String ifCommit = request.getParameter("ifCommit");
		String type = request.getParameter("type"); // jmrgl就是转到管理页面，否则转到新增页面
		String ifprint = request.getParameter("ifprint");// 判断是否打印：”11“为打印收件收据；“bzj”为打印保证金（暂存+打印）;
		String provideBm = (String) tdscBidderForm.getProvideBm();

		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		bindObject(tdscBidderApp, form);

		// 添加新增的所有联合的竞买人
		String appIds[] = tdscBidderForm.getAppIds();
		String bidderPersonIds[] = tdscBidderForm.getBidderPersonIds();
		List bidPerList = new ArrayList();
		List bidMatList = new ArrayList();
		if (bidderPersonIds != null) {
			if (bidderPersonIds.length > 0) {
				for (int i = 0; i < bidderPersonIds.length; i++) {
					TdscBidderPersonApp bidPerApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonIds[i]);
					if (bidPerApp != null) {
						bidPerList.add(bidPerApp);
						List perMatList = tdscBidderAppService.queryMatList(bidPerApp.getBidderPersonId());
						if (perMatList != null && perMatList.size() > 0) {
							for (int j = 0; j < perMatList.size(); j++) {
								TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) perMatList.get(j);
								if (tdscBidderMaterial != null)
									bidMatList.add(tdscBidderMaterial);
							}
						}
					}
				}
			}
		}

		// 保存竞买人及其材料列表，先判断在该公告中有无该交易卡号的竞买人，如有，全删
		List appIdList = new ArrayList();
		if (noticeId != null && !"".equals(noticeId) && yktBh != null && !"".equals(yktBh)) {
			// 根据公告查询所有地块appIdList，查询该公告中的所有用该交易卡编号的用户
			TdscBidderCondition bidderCondition = new TdscBidderCondition();
			bidderCondition.setNoticeId(noticeId);
			bidderCondition.setYktBh(yktBh);
			List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);

			if (bidderAppList != null && bidderAppList.size() > 0) {
				for (int nn = 0; nn < bidderAppList.size(); nn++) {
					TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(nn);
					if (bidderApp != null)
						tdscBidderAppService.delOneBidderByBidderId(bidderApp.getBidderId());
				}
			}
		}
		// 删除结束

		// 获得区县信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 添加受理窗口的UserID
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));
		// 判断是否提交:1为提交；0为不提交。
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}
		String bidderId = "";
		if (appIds != null && appIds.length > 0) {
			for (int m = 0; m < appIds.length; m++) {
				if (!"".equals(appIds[m])) {
					tdscBidderApp.setAppId(appIds[m]);
					// copy原联合竞买人数据重新存入
					bidderId = tdscBidderAppService.addUnionBidder(provideBm, tdscBidderApp, bidPerList, bidMatList);
				}
			}
			// 删除原联合竞买人信息
			if (bidPerList != null && bidPerList.size() > 0)
				tdscBidderAppService.delLJBidderPer(bidPerList);
		}

		// TdscBidderForm retForm = new TdscBidderForm();
		// retForm.setAppId(tdscBidderForm.getAppId());
		// if("11".equals(type)){
		TdscBidderForm tempForm = new TdscBidderForm();
		tempForm.setBidderId(bidderId);
		tempForm.setNoticeId(noticeId);
		if ("11".equals(ifprint) || "22".equals(ifprint)) {
			tempForm.setIfprint(ifprint);
			return queryOneBidder(mapping, tempForm, request, response);
		}

		// }
		// return queryBidder(mapping, retForm, request, response);

		String forwardString = "bidderApp.do?method=query";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 联合竞买---添加多个已存在的竞买人
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addExistUnBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String type = request.getParameter("type");
		String ifprint = request.getParameter("ifprint");
		String ifCommit = request.getParameter("ifCommit");
		// String provideBm = (String) tdscBidderForm.getProvideBm();
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		bindObject(tdscBidderApp, form);
		// 添加新增的竞买人
		String bidderPersonIds[] = tdscBidderForm.getBidderPersonIds();
		List bidPerList = new ArrayList();
		if (bidderPersonIds != null) {
			if (bidderPersonIds.length > 0) {
				for (int i = 0; i < bidderPersonIds.length; i++) {
					TdscBidderPersonApp bidPerApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonIds[i]);
					bidPerList.add(bidPerApp);
				}
			}
		}

		// 获得区县信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// Integer regionID = new Integer(user.getRegionID());
		// 添加受理窗口的UserID
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));
		// 判断是否提交:1为提交；0为不提交。
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}
		// 添加这次竞买的受理编号
		// String acceptNo =
		// tdscBidderAppService.createAcceptNo(tdscBidderForm.getAppId());

		// String
		// bidderId=tdscBidderAppService.addExistUnionBidder(provideBm,tdscBidderApp,
		// bidPerList);
		String bidderId = tdscBidderAppService.addExistUnionBidder(tdscBidderApp, bidPerList);
		TdscBidderForm retForm = new TdscBidderForm();
		retForm.setAppId(tdscBidderForm.getAppId());
		if ("11".equals(type)) {
			TdscBidderForm tempForm = new TdscBidderForm();
			tempForm.setBidderId(bidderId);
			tempForm.setType(type);
			if ("11".equals(ifprint)) {
				tempForm.setIfprint(ifprint);
			}
			return queryOneBidder(mapping, tempForm, request, response);
		}
		return queryBidder(mapping, retForm, request, response);
	}

	/**
	 * 竞买人管理--修改多个竞买人--保存
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUnBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// 获取页面参数
		String type = request.getParameter("type");
		String ifprint = request.getParameter("ifprint");
		String ifCommit = request.getParameter("ifCommit");
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		bindObject(tdscBidderApp, form);
		// 添加受理窗口的userID
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));

		String bidderId = (String) tdscBidderForm.getBidderId();
		tdscBidderApp.setBidderId(bidderId);
		// 判断是否提交:1为提交；0为不提交。
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}
		// 查询原来的受理编号
		TdscBidderApp oldBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// 获得原来的保证金总体到账情况,机审结果 ,资格证书编号 赋给新的实体 ;
		// TdscBidderApp bidderApp = (TdscBidderApp)
		// tdscBidderAppService.queryBidderAppInfo(tdscBidderApp.getBidderId());
		if (oldBidderApp != null) {
			if (oldBidderApp.getBzjztDzqk() != null && oldBidderApp.getBzjztDzqk() != "") {
				tdscBidderApp.setBzjztDzqk(oldBidderApp.getBzjztDzqk());
			}
			if (oldBidderApp.getReviewResult() != null && oldBidderApp.getReviewResult() != "") {
				tdscBidderApp.setReviewResult(oldBidderApp.getReviewResult());
			}
			if (oldBidderApp.getCertNo() != null && oldBidderApp.getCertNo() != "") {
				tdscBidderApp.setCertNo(oldBidderApp.getCertNo());
			}
			// 把原来的受理编号赋给新的实体
			if (oldBidderApp.getAcceptNo() != null) {
				tdscBidderApp.setAcceptNo(oldBidderApp.getAcceptNo());
			}
		}
		// 添加新的竞买人
		String bidderPersonIds[] = tdscBidderForm.getBidderPersonIds();
		// 查询原TdscBidderApp实体属性
		TdscBidderApp delBidApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		List bidPerList = new ArrayList();
		// 修改时没有加入新的联合竞买人 只修改TdscBidderApp
		if (bidderPersonIds == null) {
			if (delBidApp.getAcceptDate() != null) {
				tdscBidderApp.setAcceptDate(delBidApp.getAcceptDate());
			} else {
				tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));
			}
			tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
			TdscBidderForm retForm = new TdscBidderForm();
			retForm.setAppId(tdscBidderForm.getAppId());
			if ("11".equals(type)) {
				TdscBidderForm tempForm = new TdscBidderForm();
				tempForm.setBidderId(bidderId);
				tempForm.setType(type);
				if ("11".equals(ifprint)) {
					tempForm.setIfprint(ifprint);
				}
				return queryOneBidder(mapping, tempForm, request, response);
			}
			return queryBidder(mapping, retForm, request, response);
		}

		// 修改时加入了新的联合竞买人
		if (bidderPersonIds.length > 0) {
			for (int i = 0; i < bidderPersonIds.length; i++) {
				TdscBidderPersonApp bidPerApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonIds[i]);
				bidPerList.add(bidPerApp);
			}
		}

		tdscBidderAppService.updateUnionBidder(tdscBidderApp, bidPerList);
		TdscBidderForm retForm = new TdscBidderForm();
		retForm.setAppId(tdscBidderForm.getAppId());
		if ("11".equals(type)) {
			TdscBidderForm tempForm = new TdscBidderForm();
			tempForm.setBidderId(bidderId);
			tempForm.setType(type);
			if ("11".equals(ifprint)) {
				tempForm.setIfprint(ifprint);
			}
			return queryOneBidder(mapping, tempForm, request, response);
		}
		return queryBidder(mapping, retForm, request, response);
	}

	/**
	 * 根据 bidderId删除 竞买信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delOneBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面的参数
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String bidderId = (String) request.getParameter("bidderId");

		if (bidderId != null) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
			// 保存竞买人及其材料列表，先判断在该公告中有无该交易卡号的竞买人，如有，全删\
			if (tdscBidderApp != null) {
				TdscBidderCondition bidderCondition = new TdscBidderCondition();
				bidderCondition.setNoticeId(tdscBidderApp.getNoticeId());
				bidderCondition.setYktBh(tdscBidderApp.getYktBh());
				List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);

				if (bidderAppList != null && bidderAppList.size() > 0) {
					for (int nn = 0; nn < bidderAppList.size(); nn++) {
						TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(nn);
						if (bidderApp != null)
							tdscBidderAppService.delOneBidderByBidderId(bidderApp.getBidderId());
					}
				}
			}
			// 删除结束
		}

		// // 根据bidderId查询出该条竞买信息
		// List bidderList = (List)
		// tdscBidderAppService.queryByBidderId(bidderId);
		// tdscBidderAppService.delOneBidder(bidderList);

		return queryBidder(mapping, tdscBidderForm, request, response);
	}

	/**
	 * 根据条件获得申请人列表
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward querySqr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBidderCondition condition = new TdscBidderCondition();
		ActionForm actionForm = (ActionForm) request.getSession().getAttribute("lmrList");

		if (actionForm != null)
			bindObject(condition, form);

		// 设置页面行数
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// 查询列表
		if (actionForm == null)
			request.setAttribute("pageList", null);
		else
			request.setAttribute("pageList", tdscBidderAppService.findPersonPageList(condition));
		request.setAttribute("condition", condition);
		return mapping.findForward("bidderPersonAppList");
	}

	public ActionForward removeSqr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		tdscBidderAppService.removeById(request.getParameter("bidderPersonId"));
		return querySqr(mapping, form, request, response);
	}

	/**
	 * 根据条件获得竞买人列表 list_jmrgl.jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = (String) request.getParameter("noticeId");
		TdscBidderCondition condition = new TdscBidderCondition();
		List bidderGlList = new ArrayList();

		if (noticeId != null && !"".equals(noticeId)) {
			// 获得用户信息
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			String appUserId = String.valueOf(user.getUserId());

			// 判断该用户是否为“受理部总受理”
			List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
			Map buttonMap = new HashMap();
			for (int j = 0; j < buttonList.size(); j++) {
				String id = (String) buttonList.get(j);
				buttonMap.put(id, buttonList.get(j));
			}

			// 判断是否是页面上的“返回”按钮的提交
			String buttonType = (String) request.getParameter("buttonType");
			if ("00".equals(buttonType)) {
				// 若是“返回”按钮，则只给condition 设置 appId
				// condition.setAppId(appId);
			} else {
				bindObject(condition, form);
			}

			condition.setNoticeId(noticeId);

			// 更新 将意向受托人更新至该招牌挂信息中
			this.tdscBidderAppService.updateBidderNoticeId(noticeId);

			List bidderIdList = new ArrayList();
			// 根据noticeId取得所有yktbh不相同的竞买人信息列表
			List bidderAppList = (List) tdscBidderAppService.queryBidderAppListByNoticeId(noticeId);
			if (bidderAppList != null && bidderAppList.size() > 0) {
				for (int i = 0; i < bidderAppList.size(); i++) {
					TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
					if (tdscBidderApp != null)
						bidderIdList.add(tdscBidderApp.getBidderId());
				}
			}
			List bidderYixiangList = tdscBidderAppService.queryBidderAppYixiangList(noticeId);
			for (int i = 0; bidderYixiangList != null && i < bidderYixiangList.size(); i++) {
				String bidderId = (String) bidderYixiangList.get(i);
				if (StringUtils.isNotEmpty(bidderId))
					bidderIdList.add(bidderId);
			}

			if (bidderIdList != null && bidderIdList.size() > 0)
				condition.setBidderIdList(bidderIdList);
			if (condition.getBidderName() != null && !"".equals(condition.getBidderName()))
				condition.setBidderName(StringUtil.GBKtoISO88591(condition.getBidderName()));

			// 如果是“受理部总受理”或者是系统管理员，则查询所有数据；
			if (buttonMap != null) {
				if (buttonMap.get(GlobalConstants.BUTTON_ID_JMSQZSL) != null || buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) != null) {
					bidderGlList = (List) tdscBidderAppService.queryBidderAppListByCondition(condition);
					// bidderGlList = (List)
					// tdscBidderAppService.findBidderList(condition);
				} else {
					// 否则就只查询本窗口受理的数据
					// 新加入的：要求根据appUserId显示各自受理的份数;
					condition.setAppUserId(appUserId);
					condition.setIfCommit("0");
					bidderGlList = (List) tdscBidderAppService.queryBidderAppListByCondition(condition);
					// bidderGlList = (List)
					// tdscBidderAppService.findBidderListByJMSQZSL(condition);
				}
			}
		}

		if (condition.getBidderName() != null && !"".equals(condition.getBidderName()))
			condition.setBidderName(StringUtil.ISO88591toGBK(condition.getBidderName()));
		request.setAttribute("bidderGlList", bidderGlList);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("condition", condition);
		return mapping.findForward("bidderGlList");
	}

	/**
	 * 根据条件获得一个公告中的所有竞买人列表 list_existJmrgl.jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryExistBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBidderCondition condition = new TdscBidderCondition();
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String appId = (String) tdscBidderForm.getAppId();
		// 判断是否是页面上的“返回”按钮的提交
		String buttonType = (String) request.getParameter("buttonType");
		if ("00".equals(buttonType)) {
			// 若是“返回”按钮，则只给condition 设置 appId
			condition.setAppId(appId);
		} else {
			bindObject(condition, form);
		}

		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		List bidderGlList = new ArrayList();
		if (appId != null && !"".equals(appId)) {
			// 根据appId查询该地块的noticeId
			TdscBaseQueryCondition viewCondition1 = new TdscBaseQueryCondition();
			TdscBaseQueryCondition viewCondition2 = new TdscBaseQueryCondition();
			List appIdList = new ArrayList();

			viewCondition1.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(viewCondition1);

			if (tdscBlockAppView.getNoticeId() != null && !"".equals(tdscBlockAppView.getNoticeId())) {
				viewCondition2.setNoticeId(tdscBlockAppView.getNoticeId());
				viewCondition2.setOrderKey("blockNoticeNo");
				viewCondition2.setOrderType("desc");

				condition.setNoticeId(tdscBlockAppView.getNoticeId());
				// 根据noticeId得到该公告中的所有地块列表 appIdList
				List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(viewCondition2);
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
						TdscBlockAppView tdscAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
						if (!appId.equals(tdscAppView.getAppId())) {
							appIdList.add(tdscAppView.getAppId());
						}
					}
				}
			}
			// 根据appIdList得到交易卡编号不重复的竞买人列表
			bidderGlList = (List) tdscBidderAppService.findBidderListByAppIdList(appIdList);
		}

		// int pageSize = ((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		// PageList PageList = PageUtil.getPageList(bidderGlList,
		// pageSize,currentPage);

		request.setAttribute("bidderGlList", bidderGlList);
		request.setAttribute("appId", appId);
		request.setAttribute("condition", condition);
		return mapping.findForward("existBidderPersonList");
	}

	public ActionForward queryBidderCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBidderCondition bidCondition = new TdscBidderCondition();
		bindObject(bidCondition, form);
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String appId = (String) tdscBidderForm.getAppId();
		// 根据appId查询与该条有关的信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		// PageList pageList =
		// (PageList)tdscBidderAppService.findPersonPageList(bidCondition);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		return mapping.findForward("bidderGlList");
	}

	/**
	 * 联合竞买 中添加一个竞买人的信息
	 * 
	 * @param form
	 * @param request
	 */
	public ActionForward addOneOfUnionBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// form 转化为tdscBidderPersonApp实体
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		bindObject(tdscBidderPersonApp, form);
		// form 转化为tdscBidderMaterial实体 数组
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		// 构造 提交材料的 List
		List tdscBidMatList = new ArrayList();
		// 判断是第几条
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// 添加相应的实体属性
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				// tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				// tdscBidderMaterial.setBidderId(tdscBidderPersonApp.getBidderId());
				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		// 保存手动添加的竞买申请材料
		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				// tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		// 返回申请人
		TdscBidderPersonApp tdscBidPerApp = tdscBidderAppService.addOneBidderPerson(tdscBidderPersonApp, tdscBidMatList);
		request.setAttribute("tdscBidderPersonApp", tdscBidPerApp);

		return mapping.findForward("bginfosqrjsp");
	}

	/**
	 * 根据bidderPersonId 查询联合竞买人之一的信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryOneOfUnionBidPer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面信息
		String jnfs = (String) request.getParameter("jnfs");
		String hasHead = (String) request.getParameter("hasHead");
		String bidderPersonId = (String) request.getParameter("bidderPersonId");
		String blockTypeValue = (String) request.getParameter("blockTypeValue");
		String transferMode = (String) request.getParameter("transferMode");
		// 根据bidderPersonId 查询一个竞买人的信息
		TdscBidderPersonApp rtnPerson = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonId);
		List perMatList = (List) tdscBidderAppService.queryOnePerMat(bidderPersonId);
		List otherMatList = (List) tdscBidderAppService.queryOtherMatList(bidderPersonId);
		request.setAttribute("otherMatList", otherMatList);
		// 判断这个人是不是联合竞买中的牵头人
		if ("1".equals(jnfs) && "1".equals(rtnPerson.getIsHead())) {
			hasHead = "no";
		}
		request.setAttribute("hasHead", hasHead);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("blockType", blockTypeValue);
		request.setAttribute("perMatList", perMatList);
		request.setAttribute("rtnPerson", rtnPerson);
		request.setAttribute("bzjJnfs", jnfs);
		return mapping.findForward("queryOneOfUnionBidPer");
	}

	/**
	 * 修改联合竞买人之一 保存
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUnionBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String opeType = (String) request.getParameter("opeType");
		// 获取页面参数
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		tdscBidderPersonApp = tdscBidderAppService.queryOneBiddPer(tdscBidderForm.getBidderPersonId());
		// 机审之后修改竞买人信息，需要保留保证金到账数额、时间、到账情况
		// tdscBidderForm 中 的字段值为 null 时，不会bind 给实体
		// tdscBidderForm.setBzjDzqk(null);
		// tdscBidderForm.setBzjDzse(null);
		// tdscBidderForm.setBzjDzsj(null);
		bindObject(tdscBidderPersonApp, tdscBidderForm);
		// 构造 提交材料的 List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// 添加相应的实体属性
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		// 保存手动添加的竞买申请材料
		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		request.setAttribute("tdscBidderPersonApp", tdscBidderAppService.modifyBidPerXX(tdscBidderPersonApp, tdscBidMatList));
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		request.setAttribute("opeType", opeType);
		return mapping.findForward("bginfosqrjsp");
	}

	/**
	 * 修改联合竞买人之一 保存 Ajax
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUnionBidderAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		String opeType = (String) request.getParameter("opeType");
		// 获取页面参数
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();

		// 转型
		// 机审之后修改竞买人信息，需要保留保证金到账数额、时间、到账情况
		// tdscBidderForm 中 的字段值为 null 时，不会bind 给实体
		tdscBidderForm.setBzjDzqk(null);
		tdscBidderForm.setBzjDzse(null);
		tdscBidderForm.setBzjDzsj(null);
		bindObject(tdscBidderPersonApp, tdscBidderForm);
		tdscBidderPersonApp.setBidderId(tdscBidderForm.getBidderId());
		// TdscBidderPersonApp delBidPerApp
		// =(TdscBidderPersonApp)tdscBidderAppService.queryOneBiddPer(tdscBidderForm.getBidderPersonId());
		// 构造 提交材料的 List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		if (materialTypes != null) {
			for (int i = 0; i < materialTypes.length; i++) {
				if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
					// 添加相应的实体属性
					TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
					tdscBidderMaterial.setMaterialBh(materialBhs[j]);
					tdscBidderMaterial.setMaterialType(materialTypes[i]);
					tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
					tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
					tdscBidMatList.add(tdscBidderMaterial);
					j++;
				}
			}
		}

		// 保存手动添加的竞买申请材料
		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		tdscBidderPersonApp = tdscBidderAppService.modifyBidPerXX(tdscBidderPersonApp, tdscBidMatList);
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		request.setAttribute("opeType", opeType);

		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// 返回给回调函数的参数
		String rowId = tdscBidderPersonApp.getBidderPersonId();
		pw.write(rowId);
		pw.close();

		return null;
	}

	/**
	 * 删除联合竞买中的一个人 Ajax
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteOneBidAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderPersonId = request.getParameter("bidderPersonId");
		TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonId);
		String isHead = (String) tdscBidderPersonApp.getIsHead();
		List perMatList = (List) tdscBidderAppService.queryMatList(bidderPersonId);
		if (tdscBidderPersonApp != null) {
			tdscBidderAppService.removeOneBidPer(tdscBidderPersonApp, perMatList);
		}

		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// 返回给回调函数的参数
		String retString = bidderPersonId + "," + isHead;
		pw.write(retString);
		pw.close();

		return null;

	}

	// 打印保证金缴纳通知单
	public ActionForward printbzj(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		String yktBh = (String) request.getParameter("yktBh");

		List retList = new ArrayList();
		List appIdList = new ArrayList();
		if (noticeId != null && !"".equals(noticeId) && yktBh != null && !"".equals(yktBh)) {
			// 根据公告查询所有地块appIdList，查询该公告中的所有用该交易卡编号的用户
			TdscBidderCondition bidderCondition = new TdscBidderCondition();
			bidderCondition.setNoticeId(noticeId);
			bidderCondition.setYktBh(yktBh);
			List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
			if (bidderAppList != null && bidderAppList.size() > 0) {
				for (int i = 0; i < bidderAppList.size(); i++) {
					TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
					if (tdscBidderApp != null) {
						String peasonName = tdscBidderAppService.getPersonNameByBidderId(tdscBidderApp.getBidderId());

						TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
						condition.setAppId(tdscBidderApp.getAppId());
						TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
						// 根据block_id查询地块编号
						String blockCode = tdscBidderAppService.getTdscBlockPartListInBlockCode(tdscBlockAppView.getBlockId());
						tdscBlockAppView.setUnitebBlockCode(StringUtils.trimToEmpty(blockCode));
						tdscBlockAppView.setAcceptPerson(StringUtils.trimToEmpty(peasonName));
						retList.add(tdscBlockAppView);
					}
				}
			}

			request.setAttribute("tdscBlockAppViewList", retList);
		}

		return mapping.findForward("printbzj");
	}

	/**
	 * 单独竞买--新增竞买人 TDSC_BIDDER_APP表 和 TDSC_BIDDER_PERSON_APP 表
	 * Tdsc_Bidder_Material表
	 */
	public ActionForward addBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// 获取页面参数
		String noticeId = tdscBidderForm.getNoticeId();
		String yktBh = tdscBidderForm.getYktBh();
		String type = request.getParameter("type"); // jmrgl就是转到管理页面，否则转到新增页面
		String ifprint = request.getParameter("ifprint");// 判断是否打印：”11“为打印（暂存+打印）
		String ifCommit = request.getParameter("ifCommit");
		String provideBm = (String) tdscBidderForm.getProvideBm();

		String bidderId = request.getParameter("bidderId");
		String bidderPersonId = StringUtils.trimToEmpty(request.getParameter("bidderPersonId"));

		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();

		if (StringUtils.isNotEmpty(bidderId)) {
			tdscBidderApp = this.tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
		}
		if (StringUtils.isNotEmpty(bidderPersonId)) {
			tdscBidderPersonApp = this.tdscBidderAppService.queryOneBiddPer(bidderPersonId);
		}

		bindObject(tdscBidderApp, form);
		bindObject(tdscBidderPersonApp, form);

		String appIds[] = tdscBidderForm.getAppIds();

		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();

		BigDecimal[] bzjDzse = tdscBidderForm.getBidderBzjDzse();
		Date bzjDzsj = new Date();
		// tdscBidderPersonApp.setBzjDzse(bzjDzse);
		tdscBidderPersonApp.setBzjDzsj(bzjDzsj);
		tdscBidderPersonApp.setBzjDzqk("1003");

		// 删除该竞买人已经提交的报名材料列表
		if (StringUtils.isNotEmpty(bidderId)) {
			this.tdscBidderAppService.delOneBidderMaterialByBidderId(bidderId);
		}

		// 构造 提交材料的 List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// 添加实体属性
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				// tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());

				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				// tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		// 获得区县信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// Integer regionID = new Integer(user.getRegionID());
		// 设置竞买申请的“受理用户”
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}

		// //保存竞买人及其材料列表，先判断在该公告中有无该交易卡号的竞买人，如有，全删
		// //List appIdList = new ArrayList();
		// if(noticeId!=null&&!"".equals(noticeId)&&yktBh!=null&&!"".equals(yktBh)){
		// //根据公告查询所有地块appIdList，查询该公告中的所有用该交易卡编号的用户
		// TdscBidderCondition bidderCondition = new TdscBidderCondition();
		// bidderCondition.setNoticeId(noticeId);
		// bidderCondition.setYktBh(yktBh);
		// List bidderAppList =
		// tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		//
		// if(bidderAppList!=null&&bidderAppList.size()>0){
		// for(int nn=0;nn<bidderAppList.size();nn++){
		// TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(nn);
		// if(bidderApp!=null)
		// tdscBidderAppService.delOneBidderByBidderId(bidderApp.getBidderId());
		// }
		// }
		// }
		// //删除结束

		// 增加新竞买人数据
		// String bidderId = "";
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		if (appIds != null && appIds.length > 0) {
			for (int m = 0; m < appIds.length; m++) {
				if (!"".equals(appIds[m])) {
					TdscBidderApp tempBidderApp = new TdscBidderApp();
					TdscBidderPersonApp tempBidderPersonApp = new TdscBidderPersonApp();
					BeanUtils.copyProperties(tempBidderApp, tdscBidderApp);
					BeanUtils.copyProperties(tempBidderPersonApp, tdscBidderPersonApp);
					tempBidderPersonApp.setBzjDzse(bzjDzse[m]);
					tempBidderApp.setAppId(appIds[m]);
					// 设置查询地块视图的条件
					condition.setAppId(appIds[m]);
					// 无锡增加
					tempBidderApp.setBzjztDzqk("1003"); // 设置竞买申请的到账情况
					// 增加已经下载资格证书
					tempBidderApp.setIfDownloadZgzs("1");

					// 查询视图
					tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
					if (tdscBlockAppView != null) {
						if (StringUtils.isNotEmpty(tdscBlockAppView.getUserId())) {
							// 设置经办人ID
							tempBidderApp.setUserId(tdscBlockAppView.getUserId());
						}
					}
					bidderId = tdscBidderAppService.addBidder(provideBm, tempBidderApp, tempBidderPersonApp, tdscBidMatList);
				}
			}
		}

		// if("11".equals(type)){
		// if(tdscBidderForm.getBidderId()!=null&&tdscBidderForm.getBidderId()!=""){
		// tdscBidderAppService.delOneBidderByBidderId(tdscBidderForm.getBidderId());
		// }
		TdscBidderForm tempForm = new TdscBidderForm();
		tempForm.setNoticeId(noticeId);
		tempForm.setBidderId(bidderId);
		tempForm.setYktBh(yktBh);
		tempForm.setType(type);

		if ("11".equals(ifprint) || "22".equals(ifprint) || "33".equals(ifprint)) {
			tempForm.setIfprint(ifprint);
			return queryOneBidder(mapping, tempForm, request, response);
		}

		// }
		// TdscBidderForm retForm = new TdscBidderForm();
		// retForm.setAppId(tdscBidderForm.getAppId());
		// return queryBidder(mapping, retForm, request, response);

		String forwardString = "bidderApp.do?method=query";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 根据bidderId查询 转到竞买人修改页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryOneBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String noticeId = tdscBidderForm.getNoticeId();
		String type = tdscBidderForm.getType();
		String ifprint = tdscBidderForm.getIfprint();
		String bidderId = request.getParameter("bidderId") + "";
		if (StringUtil.isEmpty(bidderId)) {
			bidderId = tdscBidderForm.getBidderId();
		}

		// 保存方式为暂存
		if ("11".equals(type)) {
			request.setAttribute("type", type);
		}
		// 判断是否要求打印
		if ("11".equals(ifprint) || "22".equals(ifprint) || "33".equals(ifprint)) {
			request.setAttribute("ifprint", ifprint);
		}
		// 查询竞买信息

		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		String appId = (String) tdscBidderApp.getAppId();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		// 查询公告中所有的地块列表
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		String transferMode = "";
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
			transferMode = tdscBlockAppView.getTransferMode();
		}

		// 查询该竞买人参与竞买的地块列表
		// 根据公告查询所有地块appIdList，查询该公告中的所有用该交易卡编号的用户
		TdscBidderCondition bidderCondition = new TdscBidderCondition();
		bidderCondition.setNoticeId(noticeId);
		if (StringUtils.isNotEmpty(tdscBidderApp.getYktBh())) {
			bidderCondition.setYktBh(tdscBidderApp.getYktBh());
		} else {
			bidderCondition.setNotYktBh("1");
		}
		List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			for (int i = 0; i < bidderAppList.size(); i++) {
				TdscBidderApp tempApp = (TdscBidderApp) bidderAppList.get(i);
				TdscBidderPersonApp temPersonApp = this.tdscBidderAppService.getTdscBidderPersonByBidderId(tempApp.getBidderId());
				tempApp.setAloneTdscBidderPersonApp(temPersonApp);
				request.setAttribute("tdscBidderPersonApp", temPersonApp);
			}
		}
		request.setAttribute("appId", appId);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("bidderAppList", bidderAppList);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("bidderId", bidderId);
		// 如果是单独竞买
		if ("1".equals(tdscBidderApp.getBidderType())) { // 查询 竞买人信息
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(tdscBidderForm.getBidderId());
			// 查询竞买人材料信息
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				List otherMatList = (List) tdscBidderAppService.queryOtherMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				request.setAttribute("perMatList", retMatList);
				request.setAttribute("otherMatList", otherMatList);
			}
			return mapping.findForward("danDuJingMai");
		} else {
			// 如果是联合竞买
			// 查询竞买人列表
			List retBidPerList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderForm.getBidderId());
			// 20080313add 判断是否有牵头人
			if (retBidPerList != null && retBidPerList.size() > 0) {
				String hasHead = "no";
				// 对每一个竞买人进行判断
				for (int i = 0; i < retBidPerList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) retBidPerList.get(i);
					if ("1".equals(tdscBidderPersonApp.getIsHead())) {
						hasHead = "yes";
						break;
					}
				}
				request.setAttribute("hasHead", hasHead);
			}

			request.setAttribute("retBidPerList", retBidPerList);
			return mapping.findForward("lianHejingMai");
		}
	}

	/**
	 * 根据bidderId查询 转到新增已存在竞买人页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddExistBidPer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String type = tdscBidderForm.getType();
		String ifprint = tdscBidderForm.getIfprint();

		String appId = request.getParameter("appId");
		// 保存方式为暂存
		if ("11".equals(type)) {
			request.setAttribute("type", type);
		}
		// 判断是否要求打印
		if ("11".equals(ifprint)) {
			request.setAttribute("ifprint", ifprint);
		}
		// 查询竞买信息
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(tdscBidderForm.getBidderId());

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 查询要增加竞买人的地块信息
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);

		request.setAttribute("appId", appId);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("bidderId", tdscBidderForm.getBidderId());
		// 如果是单独竞买
		if ("1".equals(tdscBidderApp.getBidderType())) { // 查询 竞买人信息
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(tdscBidderForm.getBidderId());
			// 查询竞买人材料信息
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				List otherMatList = (List) tdscBidderAppService.queryOtherMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				request.setAttribute("perMatList", retMatList);
				request.setAttribute("otherMatList", otherMatList);
			}
			return mapping.findForward("existddjmr");
		} else {
			// 如果是联合竞买
			// 查询竞买人列表
			List retBidPerList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderForm.getBidderId());
			// 20080313add 判断是否有牵头人
			if (retBidPerList != null && retBidPerList.size() > 0) {
				String hasHead = "no";
				// 对每一个竞买人进行判断
				for (int i = 0; i < retBidPerList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) retBidPerList.get(i);
					if ("1".equals(tdscBidderPersonApp.getIsHead())) {
						hasHead = "yes";
						break;
					}
				}
				request.setAttribute("hasHead", hasHead);
			}
			request.setAttribute("retBidPerList", retBidPerList);
			return mapping.findForward("existlhjmr");
		}

	}

	/**
	 * 打印竞买申请文件收件收据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = request.getParameter("bidderId");
		// 查询竞买信息
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		List appIdList = new ArrayList();
		// if (tdscBidderApp != null) {
		// // 根据公告查询所有地块appIdList，查询该公告中的所有用该交易卡编号的用户
		// TdscBidderCondition bidderCondition = new TdscBidderCondition();
		// bidderCondition.setNoticeId(tdscBidderApp.getNoticeId());
		// bidderCondition.setYktBh(tdscBidderApp.getYktBh());
		// List bidderAppList =
		// tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		// if (bidderAppList != null && bidderAppList.size() > 0) {
		// for (int m = 0; m < bidderAppList.size(); m++) {
		// TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);
		// appIdList.add(bidderApp.getAppId());
		// }
		// }
		// }
		String[] appIds = null;
		if (tdscBidderApp != null && StringUtils.isNotBlank(tdscBidderApp.getAppId())) {
			appIds = tdscBidderApp.getAppId().split(",");
		}
		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {
				appIdList.add(appIds[i]);
			}
		}

		String transferMode = "";
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppIdList(appIdList);
		// 查询地块 信息
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
			transferMode = tdscBlockAppView.getTransferMode();
		}

		// 打印时间
		Date retDate = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormator = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		String d = dateFormator.format(retDate);

		request.setAttribute("retDate", d);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		// 判断竞买类型 单独或者联合
		// 1.单独
		if ("1".equals(tdscBidderApp.getBidderType())) {
			// 查询竞买人信息
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(bidderId);
			// 查询竞买人材料信息
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				request.setAttribute("perMatList", retMatList);

				return mapping.findForward("printGyReceipt");

			} else {
				return mapping.findForward("nullListjsp");
			}
		}
		// 2.联合
		if ("2".equals(tdscBidderApp.getBidderType())) {
			// 获得竞买人的List
			List bidderList = (List) tdscBidderAppService.queryBidderPersonList(bidderId);
			// 构造返回List
			if (bidderList != null && bidderList.size() > 0) {
				List returnList = new ArrayList();
				String retString = "";
				int matListSize = 0;
				if (bidderList != null && bidderList.size() > 0) {
					for (int j = 0; j < bidderList.size(); j++) {
						List retList = new ArrayList();
						TdscBidderPersonApp bidPersonApp = (TdscBidderPersonApp) bidderList.get(j);
						if (j < bidderList.size() - 1) {
							retString += bidPersonApp.getBidderName() + "；";
						} else {
							retString += bidPersonApp.getBidderName();
						}

						// 查询竞买人提交的材料的信息
						List matList = (List) tdscBidderAppService.queryOnePerMat(bidPersonApp.getBidderPersonId());
						if (matList != null && matList.size() > 0) {
							for (int k = 0; k < matList.size(); k++) {
								TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) matList.get(k);
								matListSize += tdscBidderMaterial.getMaterialCount().intValue();
							}

						}

						retList.add(bidPersonApp);
						retList.add(matList);

						// 添加到返回的List中
						returnList.add(retList);
					}
				}
				request.setAttribute("returnList", returnList);
				request.setAttribute("retString", retString);
				request.setAttribute("matListSize", matListSize + "");

				return mapping.findForward("printGyLhjmReceipt");

			} else {
				return mapping.findForward("nullListjsp");
			}

		}

		return null;
	}

	/**
	 * 打印竞买资格确认书
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printJmzgqrs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = request.getParameter("bidderId");
		// 查询竞买信息
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		List appIdList = new ArrayList();

		// if (tdscBidderApp != null) {
		// // 根据公告查询所有地块appIdList，查询该公告中的所有用该交易卡编号的用户
		// TdscBidderCondition bidderCondition = new TdscBidderCondition();
		// bidderCondition.setNoticeId(tdscBidderApp.getNoticeId());
		// bidderCondition.setYktBh(tdscBidderApp.getYktBh());
		// List bidderAppList =
		// tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		// if (bidderAppList != null && bidderAppList.size() > 0) {
		// for (int m = 0; m < bidderAppList.size(); m++) {
		// TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);
		// appIdList.add(bidderApp.getAppId());
		// }
		// }
		// }
		String[] appIds = null;
		if (tdscBidderApp != null && StringUtils.isNotBlank(tdscBidderApp.getAppId())) {
			appIds = tdscBidderApp.getAppId().split(",");
		}
		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {
				appIdList.add(appIds[i]);
			}
		}
		String transferMode = "";
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppIdList(appIdList);
		String blockName = "";// 地块名称
		String planId = "";
		TdscBlockPlanTable blockPlan = null;

		// 查询地块 信息
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {

			tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
			planId = tdscBlockAppView.getPlanId();
			transferMode = tdscBlockAppView.getTransferMode();

		}

		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {

			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				blockName += tdscBlockAppView.getBlockName() + ",";
			}
			if (blockName != null) {
				request.setAttribute("blockName", blockName);
			}
		}
		if (!StringUtils.isEmpty(planId)) {
			blockPlan = tdscLocalTradeService.getTdscBlockPlanTable(planId);
			request.setAttribute("tdscBlockPlanTable", blockPlan);
		}

		// 得到竞买人竞买的地块名称

		// 打印时间
		Date retDate = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormator = new SimpleDateFormat("yyyy年M月d日");
		String d = dateFormator.format(retDate);

		request.setAttribute("printDate", d);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		// 判断竞买类型 单独或者联合
		// 1.单独
		if ("1".equals(tdscBidderApp.getBidderType())) {
			// 查询竞买人信息
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(bidderId);
			// 查询竞买人材料信息
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				// request.setAttribute("perMatList", retMatList);

				return mapping.findForward("printJmzgqrs");

			} else {
				return mapping.findForward("nullListjsp");
			}
		}
		return null;
	}

	/**
	 * 修改 单独竞买人的信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateDdjmBidApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得原有的信息
		String bidderId = (String) request.getParameter("bidderId");
		String type = request.getParameter("type"); // 保存类型：”11“为暂存
		String ifprint = request.getParameter("ifprint");// 判断是否打印：”11“为打印
		String ifCommit = request.getParameter("ifCommit");// 判断是否提交：”1“为提交
		List oneBidApp = (List) tdscBidderAppService.queryByBidderId(bidderId);
		TdscBidderApp tempBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		TdscBidderPersonApp tempBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(bidderId);
		TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
		// 添加新的信息
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// 获取页面参数
		// 机审之后修改竞买人信息，需要保留保证金到账数额、时间、到账情况
		// tdscBidderForm 中 的字段值为 null 时，不会bind 给实体
		tdscBidderForm.setBzjDzqk(null);
		tdscBidderForm.setBzjDzse(null);
		tdscBidderForm.setBzjDzsj(null);
		tdscBidderForm.setCertNo(null);
		tdscBidderForm.setReviewResult(null);
		tdscBidderForm.setReviewOpnn(null);
		bindObject(tempBidderApp, tdscBidderForm);
		// 判断是“提交”还是“保存”
		if ("1".equals(ifCommit)) {
			tempBidderApp.setIfCommit("1");
		} else {
			tempBidderApp.setIfCommit("0");
		}
		bindObject(tempBidderPersonApp, tdscBidderForm);
		bindObject(tdscBidderMaterial, tdscBidderForm);
		// 把原来的申请ID赋值给新的实体
		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		// 构造 提交材料的 List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// 添加实体属性
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialBh(materialBhs[j]);
				tdscBidMatal.setMaterialType(materialTypes[i]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(materialCounts[i]));
				tdscBidMatal.setBidderPersonId(tempBidderPersonApp.getBidderPersonId());
				tdscBidMatList.add(tdscBidMatal);
				j++;
			}
		}

		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				tdscBidMatal.setBidderPersonId(tempBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		tdscBidderAppService.updateBidder(oneBidApp, tempBidderApp, tempBidderPersonApp, tdscBidMatList);

		TdscBidderForm retForm = new TdscBidderForm();
		retForm.setAppId(tdscBidderForm.getAppId());
		if ("11".equals(type)) {
			retForm.setBidderId(bidderId);
			retForm.setType(type);
			if ("11".equals(ifprint)) {
				retForm.setIfprint(ifprint);
			}
			return queryOneBidder(mapping, retForm, request, response);

		}
		return queryBidder(mapping, retForm, request, response);
	}

	/**
	 * 补卡换卡 进入输入密码页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward changeCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得申请换卡人的 bidderId
		String bidderId = (String) request.getParameter("bidderId");
		request.setAttribute("bidderId", bidderId);
		// 跳转页面
		return mapping.findForward("changeCard");
	}

	/**
	 * 补卡换卡 校验输入的密码是否与原密码相同 若相同,返回输入新卡号页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkYktMm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取申请换卡人的bidderId
		String bidderId = (String) request.getParameter("bidderId");
		// 查询该换卡人的信息
		TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// 判断输入密码是否与原密码相同
		if (tdscBidderForm.getYktMm().equals(bidderApp.getYktMm())) {
			return mapping.findForward("changeCard1");
		}
		return mapping.findForward("yktMmError");
	}

	/**
	 * 补卡换卡 校验输入的密码是否与原密码相同 若相同,返回输入新卡号页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkYktMmAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取申请换卡人的bidderId
		String bidderId = (String) request.getParameter("bidderId");
		String yktMm = (String) request.getParameter("yktMm");
		// 查询该换卡人的信息
		TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// 判断输入密码是否与原密码相同
		String rtnStatus = null;
		if (yktMm.equals(bidderApp.getYktMm())) {
			rtnStatus = "2";
		} else {
			rtnStatus = "1";
		}
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// 返回给回调函数的参数
		pw.write(rtnStatus);
		pw.close();

		return null;
	}

	/**
	 * 补卡换卡，修改交易卡卡号、交易卡编号
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateYktBh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获得申请换卡的bidderId
		String bidderId = (String) request.getParameter("bidderId");
		String newYktBh = (String) request.getParameter("yktBh");
		String newYktXh = (String) request.getParameter("yktXh");
		String password = (String) request.getParameter("password");
		// 获得原实体属性
		TdscBidderApp oldBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);

		String rtnStatus = null;
		// 设置新的卡号与密码
		if (newYktBh != null && password != null) {
			oldBidderApp.setYktMm(password);
			tdscBidderAppService.modBidYktMm(oldBidderApp, newYktBh, newYktXh);
			rtnStatus = "1";
		} else {
			rtnStatus = "2";
		}
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// 返回给回调函数的参数
		pw.write(rtnStatus);
		pw.close();

		return null;
	}

	/**
	 * 校验 材料编号 是否已经办理过 “新增竞买人” 并判断 竞买方式
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkClBh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String provideBm = (String) request.getParameter("bianhao");

		String rtnStatus = null;
		// 根据provideBm 查询该申请材料
		List bmList = (List) tdscBidderAppService.getProByProvideBm(provideBm);
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pwBidtype = response.getWriter();
		// 判断 是否有这份申请
		if (bmList == null || bmList.size() == 0) {
			rtnStatus = "01";
		}
		// 判断该材料是否已经做过申请
		if (bmList != null && bmList.size() > 0) {
			TdscBidderProvide tdscBidderProvide = (TdscBidderProvide) bmList.get(0);

			String ifapp = (String) tdscBidderProvide.getIfApp(); // 1为“申请过”
			// 0为“未申请过”
			// 可以进行申请
			if (ifapp == null || "0".equals(ifapp)) {
				String appId = (String) tdscBidderProvide.getAppId();
				TdscAppNodeStat nodeStat = this.commonFlowService.findAppNodeStat(appId, FlowConstants.FLOW_NODE_BIDDER_APP);
				if (nodeStat != null && FlowConstants.STAT_ACTIVE.equals(nodeStat.getNodeStat())) {
					// 正常受理
					rtnStatus = "02";
					String bidtype = (String) tdscBidderProvide.getBidType();
					if (bidtype != null) {
						// 返回的材料编号
						PrintWriter pwProvideBm = response.getWriter();
						pwProvideBm.write(provideBm);
						// 返回 地块的appId
						if (appId != null) {
							PrintWriter pwAppId = response.getWriter();
							pwAppId.write(appId);
						}
						// 返回申请类型 单独或者联合
						pwBidtype.write(bidtype);
					}
				} else if (nodeStat != null && (FlowConstants.STAT_END.equals(nodeStat.getNodeStat()) || FlowConstants.STAT_TERMINATE.equals(nodeStat.getNodeStat()))) {
					// 提示受理结束
					rtnStatus = "88";
				} else {
					// 提示未开始受理
					rtnStatus = "99";
				}

			}
			// 不可以进行申请
			if ("1".equals(ifapp)) {
				rtnStatus = "03";
			}
		}
		// 返回给回调函数的参数
		PrintWriter pwRtnStatus = response.getWriter();
		pwRtnStatus.write(rtnStatus);
		pwRtnStatus.close();
		return null;
	}

	/**
	 * 校验一卡通编号或者卡号是否已经使用
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkJykbh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String yktXh = (String) request.getParameter("bianhao");
		String checkType = (String) request.getParameter("checkType");
		String actionType = (String) request.getParameter("actionType");
		String rtnStatus = "00";
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pwBidtype = response.getWriter();
		List bmList = new ArrayList();
		if ("1".equals(checkType)) { // 校验编号
			bmList = (List) tdscBidderAppService.checkIfUsedYktXh(yktXh);
		} else { // 校验卡号
			bmList = (List) tdscBidderAppService.checkIfUsedYktBh(yktXh);
		}

		// 修改页面的 校验一卡通编号或者卡号是否已经使用的方法 ：判断是否使用;是否被自己使用
		if ("0000".equals(actionType)) {
			String bidderId = (String) request.getParameter("bidderId");
			// List bmList = (List) tdscBidderAppService.checkIfUsed(yktXh);
			if (bmList == null || bmList.size() == 0) { // 没有被使用过
				rtnStatus = "0001";
			} else if (bmList.size() == 1) { // 被使用过,则校验是否是自己使用
				TdscBidderApp appIdYktXhList = new TdscBidderApp();
				if ("1".equals(checkType)) {
					appIdYktXhList = (TdscBidderApp) tdscBidderAppService.getBidderAppByBidderIdYktXh(bidderId, yktXh);
				} else {
					appIdYktXhList = (TdscBidderApp) tdscBidderAppService.getBidderAppByBidderIdYktBh(bidderId, yktXh);
				}

				if (appIdYktXhList == null) { // 被使用过,但不是自己使用的
					rtnStatus = "9999";
				} else {
					rtnStatus = "0001"; // 被使用过,是自己使用的
				}
			}
		}

		// 申请页面 校验一卡通编号是否已经使用的方法 ：只判断是否使用
		if ("1111".equals(actionType)) {
			if (bmList == null || bmList.size() == 0) { // 没有被使用过
				rtnStatus = "0001";
			}
			if (bmList.size() == 1) { // 被使用过
				rtnStatus = "9999";
			}
		}

		if (bmList.size() > 1) { // 警告：多人使用着这个 一卡通编号
			rtnStatus = "8888";
		}
		rtnStatus = rtnStatus + "," + checkType;
		pwBidtype.write(rtnStatus);
		pwBidtype.close();
		return null;
	}

	/**
	 * 校验 联合竞买--修改页面 “保存”时 是否录入申请人的信息
	 * 
	 * @param bidderId
	 * @param tdscBidderForm
	 * @return
	 */
	public ActionForward checkBidderPersonNum(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rtnStatus = "0000";
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// 获取页面参数
		String bidderId = (String) request.getParameter("bidderId");
		// 修改时 根据bidderId查询原有的 申请人信息是否已经都被删除
		List BidderPersonList = (List) tdscBidderAppService.queryBidderPersonList(bidderId);

		if (BidderPersonList != null && BidderPersonList.size() > 0) {
			rtnStatus = "1111";
		}
		pw.write(rtnStatus);
		pw.close();
		return null;
	}

	/**
	 * 查询资格审查结果列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryReviewResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		this.bindObject(condition, form);
		// 获取用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

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
		PageList appViewList = (PageList) tdscBidderAppService.queryAppViewList(condition);
		if (appViewList != null) {
			List list = (List) appViewList.getList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < appViewList.getList().size(); i++) {
					TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) list.get(i);
					if (tdscBlockAppView.getAppId() != null) {
						// 设置TempStr为通过appId获得的已经下载过资格证书的竞买人 的个数
						tdscBlockAppView.setTempStr((tdscBidderAppService.queryBidderListSrc(tdscBlockAppView.getAppId()).size() - tdscBidderAppService
								.queryBidderListDownloadZgzs(tdscBlockAppView.getAppId()).size())
								+ "");
						// 设置TempStr2通过appId 获得所有通过机审的竞买人的个数
						tdscBlockAppView.setTempStr2(tdscBidderAppService.queryBidderListSrc(tdscBlockAppView.getAppId()).size() + "");
					}
				}
			}
		}
		request.setAttribute("condition", condition);
		request.setAttribute("pageList", appViewList);
		return mapping.findForward("reviewResultList");
	}

	/**
	 * 资格审查结果--查询某一地块的竞买人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryReviewResultInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = (String) request.getParameter("appId");
		// 根据appId查询竞买申请信息List
		List bidderList = (List) tdscBidderAppService.queryBidderAppList(appId);
		if (bidderList != null && bidderList.size() > 0) {
			for (int i = 0; i < bidderList.size(); i++) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderList.get(i);
				// 查询每份申请中竞买人的信息LIST
				if (tdscBidderApp != null) {
					StringBuffer bidderNames = new StringBuffer();
					List personList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
					if (personList != null && personList.size() > 0) {
						for (int j = 0; j < personList.size(); j++) {
							TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) personList.get(j);
							bidderNames.append(tdscBidderPersonApp.getBidderName()).append("，");
						}
					}
					if (bidderNames != null) {
						tdscBidderApp.setMemo(bidderNames.toString().substring(0, bidderNames.toString().length() - 1));
					}
				}
			}
		}
		request.setAttribute("bidderList", bidderList);
		return mapping.findForward("reviewResultInfo");
	}

	// public ActionForward queryScOrgList(ActionMapping mapping, ActionForm
	// form, HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	// PageList pageList = new PageList();
	// String pageNo = request.getParameter("currentPage");
	// int pageSize = ((Integer)
	// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
	// int currentPage = 0;
	// if (pageNo != null && pageNo.trim().equals("") == false) {
	// currentPage = Integer.parseInt(pageNo);
	//
	// }
	// if (pageNo==null || Integer.parseInt(pageNo) < 1) {
	// currentPage = 1;
	// }
	// TdscBidderCondition condition = new TdscBidderCondition();
	// bindObject(condition, form);
	// String organizName = condition.getOrganizName();
	//
	// //String organizName = request.getParameter("organizName");
	// if(organizName==null){
	// organizName = "";
	// }
	//
	// PublicManager manager = PublicFactory.getPublicManager();
	// //manager.getOrganizationListByName(organizName, 1);
	// List organizList = manager.getOrganizationListByName(organizName,1);
	// if(organizList!=null&&organizList.size()!=0){
	// pageList.setList(organizList);
	// }
	// pageList = PageUtil.getPageList(organizList, pageSize, currentPage);
	// request.setAttribute("pageList", pageList);
	// request.setAttribute("condition", condition);
	// return mapping.findForward("bidderQueryScOrgList");
	// }

	// XXX add by xys start
	public ActionForward queryFlapperTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// 查询列表
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);
		// else
		// planCondition.setTransferMode("3104");

		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);

		return mapping.findForward("flapperResultInfo");
	}

	public ActionForward printFlapperTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List rptList = new ArrayList();
		List retList = new ArrayList();
		Map map = new HashMap();
		String planId = request.getParameter("planId") + "";

		TdscBaseQueryCondition tdscBaseQuerycondition = new TdscBaseQueryCondition();
		tdscBaseQuerycondition.setPlanId(planId);
		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(tdscBaseQuerycondition);

		String ss = "";

		if (appViewList != null && appViewList.size() > 0)
			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) appViewList.get(i);
				if (!"04".equals(appView.getTranResult())) {// “04”为终止交易,终止交地的地块不做处理
					if (!StringUtil.isEmpty(appView.getNoitceNo()))
						ss = appView.getNoitceNo();
					ss = ss.substring(0, 10) + Integer.parseInt(ss.substring(10, 12)) + "号";

					List bidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());

					if (bidderAppList != null && bidderAppList.size() > 0) {
						for (int m = 0; m < bidderAppList.size(); m++) {
							TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);

							TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());

							// // 如果未报价则不进入列表
							// if (!StringUtil.isEmpty(bidderApp.getYktBh()) &&
							// !"1".equals(bidderApp.getIsPurposePerson())) {
							// String thisBidderMaxPrice =
							// tdscLocalTradeService.getBidderMaxPrice(bidderApp.getYktBh(),
							// appView.getAppId());
							// if (thisBidderMaxPrice != null &&
							// !StringUtil.isEmpty(thisBidderMaxPrice) &&
							// !"0".equals(thisBidderMaxPrice)) {
							// TdscBidderForm bean = new TdscBidderForm();
							//
							// // bean.setNoticeId(noticeId);
							// bean.setAppId(bidderApp.getAppId());
							// bean.setBidderId(bidderPersonApp.getBidderId());
							// bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
							// bean.setBidderName(bidderPersonApp.getBidderName());
							// bean.setAcceptNo(bidderApp.getAcceptNo());
							// bean.setCertNo(bidderApp.getCertNo());
							// bean.setConNum(bidderApp.getConNum());
							// bean.setIsPurposePerson(bidderApp.getIsPurposePerson());
							// bean.setBidderLxdh(bidderPersonApp.getBidderLxdh());
							//
							// map.put(bidderPersonApp.getBidderName(), bean);
							//
							// }
							// }

							if (!StringUtil.isEmpty(bidderApp.getYktBh())) {
								TdscBidderForm bean = new TdscBidderForm();

								// bean.setNoticeId(noticeId);
								bean.setAppId(bidderApp.getAppId());
								bean.setBidderId(bidderPersonApp.getBidderId());
								bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
								bean.setBidderName(bidderPersonApp.getBidderName());
								bean.setAcceptNo(bidderApp.getAcceptNo());
								bean.setCertNo(bidderApp.getCertNo());
								bean.setConNum(bidderApp.getConNum());
								bean.setIsPurposePerson(bidderApp.getIsPurposePerson());
								bean.setBidderLxdh(bidderPersonApp.getBidderLxdh());

								map.put(bidderPersonApp.getBidderName(), bean);
							}

						}
					}
				}
			}

		if (map.size() != 0) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				// Object key = entry.getKey();
				Object value = entry.getValue();
				retList.add((TdscBidderForm) value);
			}

		}

		// TODO 按照号牌排序

		if (retList != null && retList.size() > 0) {
			for (int i = 0; i < retList.size(); i++) {

				TdscBidderForm bean = (TdscBidderForm) retList.get(i);

				if (!StringUtil.isEmpty(bean.getCertNo()))
					bean.setBidderName(bean.getCertNo()); // 资格确认书编号
				else
					bean.setBidderName("&nbsp;");

				String tmpConNum = "未选取号牌";
				if (!StringUtil.isEmpty(bean.getConNum()))
					tmpConNum = bean.getConNum();
				bean.setConNum(tmpConNum);// 号牌 未选号牌情况，空之
				// bean.setBidderLxdh(bean.getBidderLxdh());
				rptList.add(bean);
			}
		}

		request.setAttribute("rptTitle", ss + "竞买号牌登记表");
		request.setAttribute("rptList", rptList);
		return mapping.findForward("printFlapperTbl");

	}

	private boolean isEmpty(String str) {
		if (str == null)
			return true;
		if ("null".equals(str))
			return true;
		if ("".equals(str))
			return true;

		if (str.length() == 0)
			return true;
		return false;
	}

	public ActionForward queryApplyTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fromMenu = request.getParameter("fromMenu");
		request.setAttribute("fromMenu", fromMenu);

		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// 查询列表
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);

		if (!isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		// 现场竞价当日的挂牌截止（11：00）前一个小时 ， 即10:00才可以看到数据
		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		// 报名情况详细表
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		if ("2".equals(fromMenu)) {
			if (!"唐珣".equals(user.getDisplayName()) && !"tdsc".equals(user.getUserId()))
				if (queryList != null && queryList.size() > 0) {
					for (int i = 0; i < queryList.size();) {
						TdscBlockPlanTable blockPlan = (TdscBlockPlanTable) queryList.get(i);
						// 得到现场竞价日期时间
						String listEndDate = DateUtil.timestamp2String(blockPlan.getListEndDate(), "yyyyMMddhhmmss");
						// 20110429110000
						long gpEndTime = Long.parseLong(listEndDate);
						long compTime = gpEndTime - 10000;

						// 20110503145532
						String currDate = DateConvertor.getCurrentDateWithTimeZone();
						long currTime = Long.parseLong(currDate);
						if (currTime < compTime) {
							queryList.remove(i);
						} else {
							i++;
						}

					}
				}
		}

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);
		return mapping.findForward("applyResultInfo");

	}

	public ActionForward printApplyTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List rptList = new ArrayList();
		String planId = request.getParameter("planId") + "";
		String type = request.getParameter("type") + "";
		String forwordPage = "";

		String stitle = "";
		// 打印报名情况表
		if ("1".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // 只要当前交易的地块，终止的地块不要进入；
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);
					// 在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "号";

						if ("3104".equals(app.getTransferMode()))
							stitle += "挂牌";
						if ("3103".equals(app.getTransferMode()))
							stitle += "拍卖";
						if ("3107".equals(app.getTransferMode()))
							stitle += "招标";

						// 查询本地块竞买人数
						// List personList =
						// tdscBidderAppService.findPageBidderList(app.getAppId());
						// List personList =
						// tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						//
						// System.out.println(app.getBlockName() +
						// app.getBlockNoticeNo());
						//
						// int iCountPerson = 0;
						// if (personList != null && personList.size() > 0) {
						// for (int j = 0; j < personList.size(); j++) {
						// TdscBidderApp bidderApp = (TdscBidderApp)
						// personList.get(j);
						//
						// String thisBidderMaxPrice =
						// tdscLocalTradeService.getBidderMaxPrice(bidderApp.getYktBh(),
						// app.getAppId());
						// // String thisBidderMaxPrice ="0";
						// if ("1".equals(bidderApp.getIsPurposePerson()) &&
						// "0".equals(thisBidderMaxPrice)) {
						// iCountPerson++;
						// }
						// if (thisBidderMaxPrice != null &&
						// !StringUtil.isEmpty(thisBidderMaxPrice) &&
						// !"0".equals(thisBidderMaxPrice)) {
						// iCountPerson++;
						// }
						// }
						// }

						// 查询本地块竞买人数
						int iCountPerson = 0;
						List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						if (personList != null && personList.size() > 0) {
							iCountPerson = personList.size();
						}

						TdscBlockForm formBean = new TdscBlockForm();
						String s1 = "";
						if (!StringUtil.isEmpty(app.getBlockNoticeNo()))
							s1 = app.getBlockNoticeNo();

						formBean.setBlockNoticeNo(s1);// 地块编号
						formBean.setBlockName(app.getBlockName());// 地块名称
						formBean.setCountSalePerson(iCountPerson + "");// 竞买人数，含意向竞买人
						rptList.add(formBean);
					}
				}
				request.setAttribute("rptTitle", stitle + "出让地块报名情况");
				request.setAttribute("rptContext", "");
				// request.setAttribute("rptContext",
				// "无锡市区xxxx年国有建设用地使用权挂牌出让共推出n幅地块，报名截止x月x日x时，有" + "个意向竞买人报名参与" +
				// blockList.size() + "幅地块的竞买：");
			}

			forwordPage = "printApplyTbl";
		}

		// 打印报名情况表(详细)
		if ("2".equals(type)) {

			// （1）意向单位排第一位
			// （2）最高报价单位排第二位
			// （3）备注里去掉多余的逗号

			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			cond.setOrderKey("blockNoticeNo");
			// cond.setStatus("01"); // 只要当前交易的地块，终止的地块不要进入；
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// 在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "号";

						if ("3104".equals(app.getTransferMode()))
							stitle += "挂牌";
						if ("3103".equals(app.getTransferMode()))
							stitle += "拍卖";
						if ("3107".equals(app.getTransferMode()))
							stitle += "招标";

						// 查询本地块竞买人数
						// List personList =
						// tdscBidderAppService.findPageBidderList(app.getAppId());
						// List personList =
						// tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());

						// 此处已经包含了意向人

						// int iCountPerson = 0;
						// if (personList != null && personList.size() > 0)
						// iCountPerson = personList.size();

						TdscBlockForm formBean = new TdscBlockForm();

						String s1 = "&nbsp;";
						if (!StringUtil.isEmpty(app.getBlockNoticeNo()))
							s1 = app.getBlockNoticeNo();

						formBean.setBlockNoticeNo(s1);// 地块编号
						formBean.setBlockName(app.getBlockName());// 地块名称
						formBean.setTotalLandArea(app.getTotalLandArea()); // 土地面积（M2）
						formBean.setInitPrice(app.getInitPrice()); // 土地出让金起始价

						// 得到当前最高报价
						TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(app.getAppId());
						if (listingInfo != null && listingInfo.getCurrPrice() != null) {
							formBean.setCurrMaxPrice(listingInfo.getCurrPrice() + ""); // 当前最高报价
						} else {
							formBean.setCurrMaxPrice(app.getInitPrice() + "");
						}

						// if (!"1".equals(app.getIsPurposeBlock())) {
						// // 不是意向地块
						// List personList =
						// commonQueryService.getAllBiddersByAppId(app.getAppId());
						// if(personList!=null && personList.size()>0){
						// for (int j = 0; j < personList.size(); j++) {
						// TdscBidderForm bean = (TdscBidderForm)
						// personList.get(j);
						// System.out.println(j+ " ================
						// "+bean.getBidderName());
						// }
						// }
						//
						// formBean.setBidders(null);
						// // set bidder info end
						// rptList.add(formBean);
						// } else {
						List personList = commonQueryService.getAllBiddersByAppId(app.getAppId());
						// set bidder info start
						List bidders = new ArrayList();
						int m = 0;
						boolean hasMaxPriceUnit = false;
						if (personList != null && personList.size() > 0) {
							for (int j = 0; j < personList.size(); j++) {

								TdscBidderForm bean = (TdscBidderForm) personList.get(j);

								if (!StringUtils.isEmpty(bean.getBidderId())) {

									TdscBidderCondition bidderCond = new TdscBidderCondition();
									bidderCond.setBidderId(bean.getBidderId());
									TdscBidderView bidderView = tdscBidderViewService.queryBidderViewInfo(bidderCond);

									System.out.println(bidderView.getBidderName());

									if ("1".equals(bidderView.getIsPurposePerson()) && bidderView.getPurposeAppId().indexOf(app.getAppId()) != -1) {
										bean.setBidderName(bidderView.getBidderName());

										if (!isEmpty(bidderView.getConNum()))
											bean.setConNum(bidderView.getConNum());
										else
											bean.setConNum("未选牌号");

										String currMaxPrice = formBean.getCurrMaxPrice();
										String thisBidderMaxPrice = tdscLocalTradeService.getBidderMaxPrice(bidderView.getYktBh(), app.getAppId());
										if (currMaxPrice.equals(thisBidderMaxPrice))
											hasMaxPriceUnit = true;

										String tmp = "意向单位";
										if (hasMaxPriceUnit)
											tmp += "，最高报价单位";

										// if (personList.size() == 1 &&
										// !hasMaxPriceUnit)
										// tmp += "，最高报价单位";
										bean.setTranResult(tmp);
										bean.setMemo("1");

										bidders.add(bean);

									} else {
										bean.setBidderName(bidderView.getBidderName());

										if (!isEmpty(bidderView.getConNum()))
											bean.setConNum(bidderView.getConNum());
										else
											bean.setConNum("未选牌号");

										String currMaxPrice = formBean.getCurrMaxPrice();
										// 得到该单位的最高报价
										String thisBidderMaxPrice = tdscLocalTradeService.getBidderMaxPrice(bidderView.getYktBh(), app.getAppId());
										// String thisBidderMaxPrice ="0";
										if (thisBidderMaxPrice != null && !StringUtil.isEmpty(thisBidderMaxPrice) && !"0".equals(thisBidderMaxPrice)) {
											if (currMaxPrice.equals(thisBidderMaxPrice)) {
												hasMaxPriceUnit = true;

												bean.setTranResult("最高报价单位");
												bean.setMemo("2");
											} else {
												bean.setTranResult("&nbsp;");
											}
										} else {
											bean.setTranResult("未报价");
										}

										bidders.add(bean);

									}
								} else {
									//
									bidders.add(new TdscBidderForm());
								}
							}
						}

						bidders = seqBidders(bidders);

						formBean.setBidders(bidders);
						// set bidder info end
						rptList.add(formBean);
						// }
					}
				}
				request.setAttribute("rptTitle", stitle + "出让地块报名情况");
				forwordPage = "printApplyDetailTbl";
			}
		}

		// 打印报名人联系方式
		if ("3".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // 只要当前交易的地块，终止的地块不要进入；
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// 在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "号";
						rptList.addAll(commonQueryService.getAllBiddersByAppId(app.getAppId()));
					}
				}
			}
			request.setAttribute("rptTitle", stitle + "竞买人联系方式");
			forwordPage = "printLianXiRenTbl";
		}
		// 打印竞得人联系方式
		if ("4".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0)
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);
					stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "号";
					rptList.addAll(commonQueryService.getAllBiddersByAppId(app.getAppId()));
				}
			request.setAttribute("rptTitle", stitle + "竞得人联系方式");
			forwordPage = "printLianXiRenTbl";
		}

		// 打印开场词
		if ("5".equals(type)) {
			String stitle2 = null;
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // 只要当前交易的地块，终止的地块不要进入；
			cond.setOrderKey("blockNoticeNo");
			Set allBiddersSet = new HashSet();
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
			// String allBidders = "";

			String noYiXiangNoPersonBlock = "";
			String noYiXiangOnePersonBlock = "";
			String yiXiangNoPersonBlock = "";

			int iJingJia = 0;

			BigDecimal totalarea = new BigDecimal(0);
			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size();) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// 在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						i++;
						stitle = app.getNoitceNo();
						String stitle1 = stitle.substring(5, 9);// 年
						stitle2 = stitle.substring(10, 12);//

						request.setAttribute("stitle1", stitle1);
						request.setAttribute("stitle2", stitle2);

						totalarea = totalarea.add(app.getTotalLandArea());// 总面积

						TdscBlockPlanTable tdscblockplantable = tdscLocalTradeService.getTdscBlockPlanTable(app.getPlanId());

						String issue_begin = DateUtil.date2String(tdscblockplantable.getIssueStartDate(), "yyyy年MM月dd日HH:mm");
						String acc_app_end = DateUtil.date2String(tdscblockplantable.getAccAppEndDate(), "yyyy年MM月dd日HH:mm");

						request.setAttribute("issue_begin", filter0FromDateString(issue_begin));// 开始公告时间
						request.setAttribute("acc_app_end", filter0FromDateString(acc_app_end));// 受理截止时间

						// 查询本地块竞买人数
						List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						// set bidder info start

						// 无意向挂牌地块
						if ("0".equals(app.getIsPurposeBlock())) {
							if (personList.size() == 0) {
								noYiXiangNoPersonBlock += app.getBlockName() + "、";
								iJingJia++;
							} else if (personList.size() == 1) {
								noYiXiangOnePersonBlock += app.getBlockName() + "、";
								iJingJia++;
							}
						}

						if ("1".equals(app.getIsPurposeBlock())) {
							if (personList.size() == 1) {
								yiXiangNoPersonBlock += app.getBlockName() + "、";
								iJingJia++;
							}
						}

						// 获得所有竞买人
						// List bidders = new ArrayList();
						if (personList != null && personList.size() > 0) {
							for (int j = 0; j < personList.size(); j++) {
								TdscBidderApp bidder = (TdscBidderApp) personList.get(j);

								// List tmpLists =
								// tdscLocalTradeService.queryListingAppByYktXh(bidder.getYktXh());
								// if (tmpLists != null && tmpLists.size() > 0)
								// {

								// set bidder info end
								allBiddersSet.add(bidder.getConNum());
								// }
							}

						}
					} else {
						blockList.remove(i);
					}
				}

				request.setAttribute("blockcount", blockList);// 获取该公告下的所有地块个数，终止交易的地块未统计在内

				float f = Float.parseFloat(totalarea + "");
				String ss = Math.round(f) + "";
				request.setAttribute("totalarea", ss);

				String retQizhong = "";

				if (!StringUtil.isEmpty(noYiXiangOnePersonBlock)) {
					retQizhong += noYiXiangOnePersonBlock.substring(0, noYiXiangOnePersonBlock.length() - 1) + "因只有一家报名，直接由报名单位摘得；";

					// request.setAttribute("noYiXiangOnePersonBlockContext",
					// noYiXiangOnePersonBlockContext);
				}
				if (!StringUtil.isEmpty(noYiXiangNoPersonBlock)) {
					retQizhong += noYiXiangNoPersonBlock.substring(0, noYiXiangNoPersonBlock.length() - 1) + "因无人报名，由我局收回；";
					// request.setAttribute("noYiXiangNoPersonBlockContext",
					// noYiXiangNoPersonBlockContext);
				}
				if (!StringUtil.isEmpty(yiXiangNoPersonBlock)) {
					retQizhong += yiXiangNoPersonBlock.substring(0, yiXiangNoPersonBlock.length() - 1) + "因无其他单位报名，直接由意向单位摘得；";
					// request.setAttribute("yiXiangNoPersonBlockContext",
					// yiXiangNoPersonBlockContext);
				}

				if (!StringUtil.isEmpty(retQizhong))
					request.setAttribute("retQizhong", "（其中" + retQizhong + "）");

				int s = blockList.size();
				// String count = (s - iJingJia) + "幅";
				String count = (s) + "幅";
				String jjCount = (s - iJingJia) + "幅";// 进入现场竞价环节的地块数
				request.setAttribute("count", count);
				request.setAttribute("jjCount", jjCount);

			}
			request.setAttribute("allBidders", allBiddersSet);
			request.setAttribute("rptTitle", "开场词");
			forwordPage = "printKaiCCiTbl";
		}
		// 打印主持词
		if ("6".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // 只要当前交易的地块，终止的地块不要进入；
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			Set allBiddersSet = new HashSet();
			Set xcBiddersSet = new HashSet();
			// String allBidders = "";
			// 规定时间内未挂牌的单位
			Set noListingPersonSet = new HashSet();

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// 在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo();
						stitle = stitle.substring(10, 12);
						request.setAttribute("stitle", stitle);
						if ("3104".equals(app.getTransferMode()))
							stitle += "挂牌";
						if ("3103".equals(app.getTransferMode()))
							stitle += "拍卖";
						if ("3107".equals(app.getTransferMode()))
							stitle += "招标";

						List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						if (personList != null && personList.size() > 1) {
							// //personList 包含了意向，但无人挂牌的地跨，遇到此情况，continue;
							// if (personList != null && personList.size() > 1)
							// {
							// for (int j = 0; j < personList.size(); j++) {
							//
							// TdscBidderApp bidder = (TdscBidderApp)
							// personList.get(j);
							// List tmpLists =
							// tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(bidder.getYktXh(),
							// app.getAppId());
							//
							// if (tmpLists == null || tmpLists.size() == 0) {
							// break;
							// }
							// }
							// }

							request.setAttribute("personList", personList);

							TdscBlockForm formBean = new TdscBlockForm();

							String s1 = "&nbsp;";
							if (!StringUtil.isEmpty(app.getBlockNoticeNo()))
								s1 = app.getBlockNoticeNo();

							formBean.setBlockNoticeNo(s1);// 地块编号

							formBean.setBlockName(app.getBlockName());// 地块名称

							formBean.setTotalLandArea(app.getTotalLandArea());

							formBean.setInitPrice(app.getInitPrice()); // 土地出让金起始价

							formBean.setLandLocation(app.getLandLocation());// 土地位置

							formBean.setTotalBuildingArea(app.getTotalBuildingArea()); // 可供建筑用地

							TdscBlockPart blockPart = tdscLocalTradeService.getBlockInfoPart(app.getBlockId());

							TdscBlockInfoCondition blockCond = new TdscBlockInfoCondition();
							blockCond.setBlockId(app.getBlockId());
							List blockinfoList = tdscLocalTradeService.queryBlockInfoByCond(blockCond);
							TdscBlockInfo blockinfo = null;
							if (blockinfoList != null && blockinfoList.size() > 0)
								blockinfo = (TdscBlockInfo) blockinfoList.get(0);

							formBean.setBuildingHeight(blockinfo.getBuildingHeight());// 建筑限高

							formBean.setLandUseType(blockPart.getLandUseType());// 土地用途

							formBean.setGreeningRate(blockPart.getGreeningRate());// 绿地率

							formBean.setDensity(blockPart.getDensity());// 建筑密度

							String sign2 = signConvert(blockPart.getVolumeRateSign());
							if (StringUtils.isNotBlank(sign2) && "介于".equals(sign2)) {
								formBean.setVolumeRate(blockPart.getVolumeRate() + "<容积率<" + blockPart.getVolumeRate2() + blockPart.getVolumeRateMemo());// 容积率
							} else {
								formBean.setVolumeRate(sign2 + blockPart.getVolumeRate() + blockPart.getVolumeRateMemo());// 容积率
							}

							formBean.setSpotAddPriceRange(app.getSpotAddPriceRange());// 自助加价幅度

							String tmp = blockinfo.getCountUse();

							if ("1".equals(tmp))
								tmp = "住宅";
							if ("2".equals(tmp))
								tmp = "商业";
							if ("3".equals(tmp))
								tmp = "科研";
							if ("4".equals(tmp))
								tmp = "工业";
							if ("5".equals(tmp))
								tmp = "其他";

							formBean.setCountUse(tmp);

							// 得到当前最高报价
							TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(app.getAppId());
							// if (listingInfo != null &&
							// listingInfo.getCurrPrice() != null) {
							// formBean.setCurrMaxPrice(listingInfo.getCurrPrice()
							// + ""); // 当前最高报价
							// } else {
							//
							// }
							// 查询当前最高报价人号牌

							if (listingInfo != null && listingInfo.getCurrPrice() != null) {
								formBean.setCurrMaxPrice(listingInfo.getCurrPrice() + ""); // 当前最高报价
								TdscBidderCondition bidderCond = new TdscBidderCondition();
								bidderCond.setYktBh(listingInfo.getYktXh());
								List bidderViewList = tdscBidderViewService.queryBidderByNameKahao(bidderCond);
								TdscBidderView bidderView = null;
								if (bidderViewList != null && bidderViewList.size() > 0)
									bidderView = (TdscBidderView) bidderViewList.get(0);
								formBean.setCurrMaxPersonNum(bidderView.getConNum());// 当前最高报价人号牌
							} else {
								// if (bidderViewList != null &&
								// bidderViewList.size() > 0)
								// formBean.setCurrMaxPersonNum(bidderView.getConNum());//
								// 当前最高报价人号牌

								// TODO 此处有问题，如果无人只有一家单位报名，但无挂牌就会出错；
								// 程序暂时不会走到此步，因为出让文件中说明报名单位必须挂牌一次
								String appId = app.getAppId();
								formBean.setCurrMaxPrice(app.getInitPrice() + "");

								System.out.println("appid=" + appId + " initPrice= " + app.getInitPrice());
							}

							// 查询本地块竞买人数
							// 进入现场的竞买人
							String allBidderConNum = "";
							String xcBidderConNum = "";

							/*
							 * 读取所有竞买人号牌
							 */

							if (personList != null && personList.size() > 1) {
								for (int j = 0; j < personList.size(); j++) {
									TdscBidderApp bidder = (TdscBidderApp) personList.get(j);

									if (!StringUtil.isEmpty(bidder.getConNum())) {

										allBidderConNum += bidder.getConNum() + "号、";

										allBiddersSet.add(new Integer(bidder.getConNum()));

									}

									TdscBidderCondition bidderCond = new TdscBidderCondition();
									bidderCond.setBidderId(bidder.getBidderId());
									TdscBidderView bidderView = tdscBidderViewService.queryBidderViewInfo(bidderCond);
									// 意向人的号牌要进去
									if ("1".equals(bidderView.getIsPurposePerson()) && bidderView.getPurposeAppId().indexOf(app.getAppId()) != -1) {
										allBidderConNum += bidder.getConNum() + "号、";
										allBiddersSet.add(new Integer(bidderView.getConNum()));

									}

								}
							}
							/*
							 * 读取进入现场的号牌
							 */
							if (personList != null && personList.size() > 1) {
								for (int j = 0; j < personList.size(); j++) {

									TdscBidderApp bidder = (TdscBidderApp) personList.get(j);
									List tmpLists = tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(bidder.getYktBh(), app.getAppId());

									if (tmpLists != null && tmpLists.size() > 0) {

										if (!StringUtil.isEmpty(bidder.getConNum())) {
											xcBidderConNum += bidder.getConNum() + "号、";
											xcBiddersSet.add(new Integer(bidder.getConNum()));
										}
									} else {
										if (!"1".equals(bidder.getIsPurposePerson())) {
											noListingPersonSet.add(new Integer(bidder.getConNum()));

											// noListingPerson +=
											// bidder.getConNum() + "号、";
										}
									}
									TdscBidderCondition bidderCond = new TdscBidderCondition();
									bidderCond.setBidderId(bidder.getBidderId());
									TdscBidderView bidderView = tdscBidderViewService.queryBidderViewInfo(bidderCond);
									// 意向人的号牌要进去
									if ("1".equals(bidderView.getIsPurposePerson()) && bidderView.getPurposeAppId().indexOf(app.getAppId()) != -1) {
										xcBidderConNum += bidder.getConNum() + "号、";
										xcBiddersSet.add(new Integer(bidderView.getConNum()));

									}
									String[] currBlockPerson = xcBidderConNum.split("号、");
									String[] temp = sort(currBlockPerson, 0);
									String strIfGrantCrfa = "";
									for (int k = 0; k < temp.length; k++) {
										strIfGrantCrfa += temp[k] + "号、";
									}
									formBean.setIfGrantCrfa(strIfGrantCrfa.substring(0, strIfGrantCrfa.length() - 1));
								}
							}
							rptList.add(formBean);
						}
					}
				}
			}

			request.setAttribute("rptTitle", "主持词");
			forwordPage = "printZhuChiCiTbl";

			// allBiddersSet = sortConNum(allBiddersSet);
			Set ss = new TreeSet(allBiddersSet);

			request.setAttribute("allBiddersSize", ss.size() + "");// 所有竞买人，未挂牌/已挂牌

			String strXcBidders = mergeListingPerson(xcBiddersSet);
			request.setAttribute("xcBidders", strXcBidders);// 现场已经挂牌的单位

			String strNoListingPerson = mergeListingPerson(noListingPersonSet);
			request.setAttribute("noListing", strNoListingPerson);// 规定时间内未挂牌的单位
		}
		request.setAttribute("rptList", rptList);
		return mapping.findForward(forwordPage);
	}

	/**
	 * 由 set 组合成 字符串
	 * 
	 * @param noListingPersonSet
	 * @return
	 */
	private String mergeListingPerson(Set personSet) {
		String retStr = "";
		if (personSet != null && personSet.size() > 0) {
			// 先排序
			Set ss = new TreeSet(personSet);
			// 再加入 逗号 和 “号、”
			if (ss != null && ss.size() > 0) {
				Iterator it = ss.iterator();
				while (it.hasNext()) {
					// out.println(it.next());
					retStr += (it.next() + "号、");
				}
			}
			return retStr.substring(0, retStr.length() - 1);
		} else {
			return "";
		}
	}

	private String signConvert(String strValue) {
		String sign2 = "";
		if ("01".equals(strValue)) {
			sign2 = "＜";
		}
		if ("02".equals(strValue)) {
			sign2 = "＜=";
		}
		if ("03".equals(strValue)) {
			sign2 = "=";
		}
		if ("04".equals(strValue)) {
			sign2 = ">";
		}
		if ("05".equals(strValue)) {
			sign2 = ">=";
		}
		if ("06".equals(strValue)) {
			sign2 = "介于";
		}
		return sign2;
	}

	/**
	 * 对字符串数组进行排序
	 * 
	 * @param str
	 *            原始字符串数组
	 * @param flag
	 *            flag=0:顺序排序 flag=1:倒序排序
	 * @return 排序后的字符串数组
	 */
	public String[] sort(String[] str, int flag) {
		if (str == null || str.length == 0)
			throw new IllegalArgumentException();
		String temp = str[0];
		// 顺序排列 ,即从小到大
		if (flag == 0) {
			for (int i = 0; i < str.length - 1; i++) {
				for (int j = i + 1; j < str.length; j++) {
					if (str[i].compareTo(str[j]) > 0) {
						temp = str[i];
						str[i] = str[j];
						str[j] = temp;
					}
				}
			}
		}
		// else if(flag==1){//倒序排列
		// for(int i=0;i<str.length-1;i++){
		// for(int j=i+1;j<str.length;j++){
		// if(str[i].compareTo(str[j])<0){
		// temp=str[i];
		// str[i]=str[j];
		// str[j]=temp;
		// }
		// }
		// }
		// }
		return str;
	}

	// add by xys end
	/**
	 * 排序意向人，最高报价人
	 */
	private List seqBidders(List bidders) {
		List retList = new ArrayList();

		if (bidders != null && bidders.size() > 0) {
			for (int i = 0; i < bidders.size(); i++) {
				TdscBidderForm bean = (TdscBidderForm) bidders.get(i);
				if ("1".equals(bean.getMemo())) {
					retList.add(bean);
					break;
				}
			}
			for (int i = 0; i < bidders.size(); i++) {
				TdscBidderForm bean = (TdscBidderForm) bidders.get(i);
				if ("2".equals(bean.getMemo())) {
					retList.add(bean);
					break;
				}
			}
			for (int i = 0; i < bidders.size(); i++) {
				TdscBidderForm bean = (TdscBidderForm) bidders.get(i);
				if (!"1".equals(bean.getMemo()) && !"2".equals(bean.getMemo())) {
					retList.add(bean);
				}
			}
		}
		return retList;

	}

	private String filter0FromDateString(String strDate) {
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8, 10);
		String strHour = strDate.substring(11, 13);
		String strMinute = strDate.substring(14, 16);

		return strYear + "年" + Integer.parseInt(strMonth) + "月" + Integer.parseInt(strDay) + "日" + Integer.parseInt(strHour) + ":" + strMinute;
	}

	public ActionForward queryBzjdzqk(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// 查询列表
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);
		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);

		return mapping.findForward("bzjdzqkList");
	}

	public ActionForward printBzjdzqk(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);

		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		String stitle = "保证金到账情况汇总";

		if (appViewList != null && appViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) appViewList.get(0);
			if(tdscBlockAppView.getNoitceNo().indexOf("租")>-1){
				stitle = tdscBlockAppView.getNoitceNo().substring(0, 11) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(11, 12)) + "号" + stitle;
			}else{
				stitle = tdscBlockAppView.getNoitceNo().substring(0, 10) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(10, 12)) + "号" + stitle;
			}
			
		}

		List tdscReturnBailList = tdscReturnBailService.findTdscReturnBailListByPlanId(planId);
		List resList = new ArrayList();

		if (tdscReturnBailList != null && tdscReturnBailList.size() > 0) {
			for (int i = 0; i < tdscReturnBailList.size(); i++) {
				TdscReturnBail tdscReturnBail = (TdscReturnBail) tdscReturnBailList.get(i);
				TdscBidderForm tdscBidderForm = new TdscBidderForm();

				tdscBidderForm.setBidderName(tdscBidderAppService.getPersonNameByBidderId(tdscReturnBail.getBidderId()));// 竞买单位或自然人名称
				tdscBidderForm.setBlockNoticeNo(commonQueryService.getTdscBlockAppViewByBlockId(tdscReturnBail.getBlockId()).getBlockNoticeNo());// 地块编号
				tdscBidderForm.setBzjDzse(tdscReturnBail.getBidderBail());// 保证金数额
				tdscBidderForm.setBzjDzqk(tdscReturnBail.getBzjBank());// 缴纳银行
				resList.add(tdscBidderForm);
			}
		}else{
			List list = tdscScheduletableService.queryTranAppList(planId);
			if(list!=null){
				TdscBlockTranApp tdscBlockApp = (TdscBlockTranApp)list.get(0);
				//查找此公告内所有的竞买人
				List bidderAppList = tdscBidderAppService.findAppByNoticeId(tdscBlockApp.getNoticeId());
				for(int i=0;i<bidderAppList.size();i++){
					TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(i);
					TdscBlockTranApp tranApp = this.tdscLocalTradeService.getTdscBlockTranApp(bidderApp.getAppId());
					
					if("1".equals(bidderApp.getIfCommit())&&StringUtils.isNotBlank(bidderApp.getCertNo())){
						TdscBidderPersonApp personApp = tdscBidderAppService.queryBidPerson(bidderApp.getBidderId());
						TdscBidderForm tdscBidderForm = new TdscBidderForm();
						tdscBidderForm.setBidderName(personApp.getBidderName());// 竞买单位或自然人名称
						tdscBidderForm.setBlockNoticeNo(tranApp.getBlockNoticeNo());// 地块编号
						tdscBidderForm.setBzjDzse(personApp.getBzjDzse());// 保证金数额
						tdscBidderForm.setBzjDzqk(bidderApp.getBankId());// 缴纳银行
						resList.add(tdscBidderForm);
					}
				}
			}
		}
		
		List bankDicList = tdscBidderViewService.queryBankDicList();
		request.setAttribute("bankDicList", bankDicList);

		request.setAttribute("stitle", stitle);
		request.setAttribute("resList", resList);

		return mapping.findForward("bzjdzqk");
	}

	public ActionForward queryJyfwf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// 查询列表
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);
		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);

		return mapping.findForward("jyfwfList");
	}

	/**
	 * 打印交易服务费统计
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printJyfwftj(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		condition.setOrderKey("blockNoticeNo");

		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		String stitle = "交易服务费统计";
		List resList = new ArrayList();

		if (appViewList != null && appViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) appViewList.get(0);
			if (StringUtils.isNotBlank(tdscBlockAppView.getNoitceNo())) {
				if(tdscBlockAppView.getNoitceNo().indexOf("租")>-1){
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 11) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(11, 12)) + "号" + stitle;
				}else{
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 10) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(10, 12)) + "号" + stitle;
				}
			}

			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) appViewList.get(i);
				BigDecimal chargeLower = new BigDecimal(0);

				// 只有交易成功的地块，才应该统计其交易服务费，否则不应该统计其交易服务费
				if ("01".equals(app.getTranResult())) {
					List blockPartList = tdscBidderAppService.getTdscBlockPartList(app.getBlockId());
					if (blockPartList != null && blockPartList.size() > 0) {
						TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(0);
						BigDecimal serviceCharge = blockPart.getServiceCharge() == null ? new BigDecimal(0) : blockPart.getServiceCharge();// 土地交易服务费，单价
						chargeLower = (app.getTotalLandArea().multiply(serviceCharge)).setScale(0, BigDecimal.ROUND_HALF_UP);
					}
					app.setTempBigDecimal(chargeLower);// 用来临时存放土地交易服务费用
					resList.add(app);
				}
			}
		}

		request.setAttribute("stitle", stitle);
		request.setAttribute("resList", resList);

		return mapping.findForward("jyfwftj");
	}

	/**
	 * 打印地质灾害评估费统计
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printDzpgftj(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		condition.setOrderKey("blockNoticeNo");

		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		String stitle = "地质灾害评估费统计";
		List resList = new ArrayList();

		if (appViewList != null && appViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) appViewList.get(0);
			if (StringUtils.isNotBlank(tdscBlockAppView.getNoitceNo())) {
				if(tdscBlockAppView.getNoitceNo().indexOf("租")>-1){
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 11) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(11, 12)) + "号" + stitle;
				}else{
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 10) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(10, 12)) + "号" + stitle;
				}
			}

			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) appViewList.get(i);
				// 只有交易成功且有地质灾害评估费的地块，才应该统计其评估费，否则不应该统计其评估费
				if ("01".equals(app.getTranResult())) {
					TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
					tdscBlockInfo = tdscScheduletableService.getBlockInfoApp(app.getBlockId());
					if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0)) > 0) {// 填写了地质灾害评估费，并且大于0
						app.setTempBigDecimal(tdscBlockInfo.getGeologicalHazard());// 用来临时存放地质灾害评估费用
						app.setAcceptPerson(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_GEOLOGY_ASSESS_UINT, tdscBlockInfo.getGeologyAssessUint()));
						resList.add(app);
					}
				}
			}
		}

		request.setAttribute("stitle", stitle);
		request.setAttribute("resList", resList);

		return mapping.findForward("dzzhpgftj");
	}

	/**
	 * 校验竞买人是否是意向竞买人
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkJmrName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = (String) request.getParameter("bidderId");
		String bidderName = (String) request.getParameter("bidderName");
		String noticeId = (String) request.getParameter("noticeId");

		// 得到当前招拍挂活动的所有地块
		List tdscBlockAppViewList = (List) tdscBidderAppService.queryAppViewListByNoticeId(noticeId);
		List allAppIdList = new ArrayList();
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				allAppIdList.add(tdscBlockAppView.getAppId());
			}
		}

		String rtnStatus = "0";// 提示标识,0表示可以新增或者修改该竞买人
		List resList = new ArrayList();// 用来存放本次活动中，该名称的竞买人的相关信息

		List bidderViewList = tdscBidderViewService.queryBidderViewListByPersonName(bidderName);// 根据单位名称，得到数据库中该单位竞买所有的竞买信息
		if (bidderViewList != null && bidderViewList.size() > 0) {
			for (int i = 0; i < bidderViewList.size(); i++) {
				TdscBidderView tdscBidderView = (TdscBidderView) bidderViewList.get(i);
				String appIdStr = tdscBidderView.getAppId();
				if (appIdStr != null) {
					String[] appIds = appIdStr.split(",");
					for (int m = 0; m < appIds.length; m++) {
						if (allAppIdList.contains(appIds[m])) {
							resList.add(tdscBidderView.getBidderId());
							break;
						}
					}
				}
			}
		}

		if (StringUtils.isNotBlank(bidderId)) {// bidderId不为空时，表示修改，因为修改操作比较特殊和复杂，目前不对修改操作做校验，只对新增操作做校验
			// nothing
		} else {
			// 新增竞买人信息时，需要判断新增的竞买人名称是否已经在本次活动中存在，如果存在，则不能新增
			if (resList != null && resList.size() >= 1) {
				rtnStatus = "1";
			}
		}

		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pwBidtype = response.getWriter();
		// 返回给回调函数的参数
		pwBidtype.write(rtnStatus);
		pwBidtype.close();
		return null;
	}

	/**
	 * 服务费缴纳，查询已经结束出让的公告列表
	 */
	public ActionForward queryFinishList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		PageList pageList = new PageList();
		String pageNo = request.getParameter("currentPage");
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false) {
			currentPage = Integer.parseInt(pageNo);
		}
		if (pageNo == null || Integer.parseInt(pageNo) < 1) {
			currentPage = 1;
		}

		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
		bindObject(condition, form);
		condition.setIfResultPublish("1");// 成交公告已发布的
		condition.setOrderKey("resultPublishDate");
		condition.setOrderType("desc");

		List noticeList = new ArrayList();
		noticeList = tdscLocalTradeService.queryNoticeListByCondition(condition);

		pageList = PageUtil.getPageList(noticeList, pageSize, currentPage);
		request.setAttribute("tranceResultNoticeList", pageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("tranceResultNoticeList");
	}

	/**
	 * 服务费缴纳登记页面
	 */
	public ActionForward toRegisterCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (!StringUtil.isEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			condition.setTranResult("01");// 查询交易成功的地块（01）
			condition.setOrderKey("blockNoticeNo");
			// 通过通用接口查询土地视图
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("toRegisterCost");
	}

	/**
	 * 保存交易服务费
	 */
	public ActionForward savePaymentCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String[] appIds = tdscBidderForm.getAppIds();
		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscLocalTradeService.getTdscBlockTranApp(appIds[i]);
				tdscBlockTranApp.setPaymentCost(tdscBidderForm.getPaymentCost()[i]);
				tdscBlockTranApp.setPaymentDate(tdscBidderForm.getPaymentDate()[i]);
				tdscBlockTranApp.setPaymentMemo(tdscBidderForm.getPaymentMemo()[i]);
				tdscLocalTradeService.updateTdscBlockTranApp(tdscBlockTranApp);
			}
		}

		String forwardString = "bidderApp.do?method=toRegisterCost&noticeId=" + noticeId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * 查询第一个报名的竞买人需要挂牌的地块,将这些地块编号返回页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward selectListingBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = (String) request.getParameter("bidderId");
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBidderPersonApp bidderPersonApp = new TdscBidderPersonApp();

		if (StringUtils.isNotBlank(bidderId)) {
			bidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId.trim());
			bidderPersonApp = tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());
		}

		String appIds = bidderApp.getAppId();// 该人所购买的地块，其中包括他的意向地块
		String purposeAppIdStr = bidderPersonApp.getPurposeAppId() + "";// 该人的意向地块

		List appIdList = new ArrayList();
		List purposeAppIdList = new ArrayList();
		List resultAppIdList = new ArrayList();// 存放该人所买地块，他的意向地块不在其中
		String resultStr = "";// 需要挂牌的地块的地块编号和appId

		if (StringUtils.isNotBlank(appIds)) {
			appIdList = Arrays.asList(appIds.split(","));
		}

		// 如果该人是某些地块的意向人，则在其所买地块中，去掉意向地块，只处理非意向地块
		if (StringUtils.isNotBlank(purposeAppIdStr)) {
			purposeAppIdList = Arrays.asList(purposeAppIdStr.split(","));
			for (int i = 0; i < purposeAppIdList.size(); i++) {
				String purposeAppId = (String) purposeAppIdList.get(i);
				for (int m = 0; m < appIdList.size(); m++) {
					if (!purposeAppId.equals(appIdList.get(m))) {
						resultAppIdList.add(appIdList.get(m));
					}
				}
			}
		}

		// 在该人所购买的地块中，如果存在没有挂牌的，则拼接这些地块编号返回页面
		for (int i = 0; i < resultAppIdList.size(); i++) {
			String appId = (String) resultAppIdList.get(i);
			List listingAppList = tdscLocalTradeService.queryListingAppListByAppId(appId);
			if (listingAppList == null || (listingAppList != null && listingAppList.size() == 0)) {
				if (resultStr.length() > 0) {
					resultStr += "," + tdscLocalTradeService.getTdscBlockTranApp(appId).getBlockNoticeNo() + "," + appId;
				} else {
					resultStr += tdscLocalTradeService.getTdscBlockTranApp(appId).getBlockNoticeNo() + "," + appId;
				}
			}
		}

		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// 返回给回调函数的参数
		pw.write(resultStr);
		pw.close();

		return null;
	}

	/**
	 * 第一个报名人对地块进行挂牌
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toListing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appIds = request.getParameter("appIds") + "";
		String bidderId = request.getParameter("bidderId") + "";
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List appIdList = new ArrayList();// 该竞买人需要挂牌的地块
		if (StringUtils.isNotBlank(bidderId)) {
			bidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId.trim());
		}
		if (StringUtils.isNotBlank(appIds)) {
			appIdList = Arrays.asList(appIds.split(","));
		}

		String failBlockNoticeNo = "";// 想进行挂牌，但未成功挂牌的地块的地块编号,这些地块编号需要返回页面，显示给竞买人和窗口工作人员
		for (int i = 0; i < appIdList.size(); i++) {
			String appId = appIdList.get(i) + "";
			List listingAppList = tdscLocalTradeService.queryListingAppListByAppId(appId.trim());
			tdscBlockTranApp = tdscLocalTradeService.getTdscBlockTranApp(appId.trim());
			if (listingAppList == null || (listingAppList != null && listingAppList.size() == 0)) {// 需要挂牌的地块当前没有挂牌记录（无人对其挂牌），则进行挂牌报价
				TdscListingInfo tdscListingInfo = new TdscListingInfo();
				TdscListingApp tdscListingApp = new TdscListingApp();

				tdscListingInfo.setListCert(bidderApp.getCertNo());
				tdscListingInfo.setAppId(appId);
				tdscListingInfo.setListDate(new Timestamp(System.currentTimeMillis()));
				tdscListingInfo.setYktXh(bidderApp.getYktBh());// 此处写入该竞买人的CA证书号
				if (tdscBlockTranApp.getIsPurposeBlock() != null && "1".equals(tdscBlockTranApp.getIsPurposeBlock())) {// 有意向挂牌出让地块，在挂牌机上挂牌报价成功，第一次向tdsc_listing_info表插入记录时，其挂牌轮数被设为2，第1轮已由意向人挂牌报价
					tdscListingInfo.setCurrRound(new BigDecimal(2));
					tdscListingInfo.setCurrPrice(tdscBlockTranApp.getInitPrice().add(tdscBlockTranApp.getSpotAddPriceRange()));// 意向出让地块，第一轮挂牌价格必须为起始价加上加价幅度
					tdscListingApp.setListPrice(tdscBlockTranApp.getInitPrice().add(tdscBlockTranApp.getSpotAddPriceRange()));// 意向出让地块，第一轮挂牌价格必须为起始价加上加价幅度
				} else if (tdscBlockTranApp.getIsPurposeBlock() != null && "0".equals(tdscBlockTranApp.getIsPurposeBlock())) {// 无意向挂牌出让地块，在挂牌机上挂牌报价成功，第一次向tdsc_listing_info表插入记录时，其挂牌轮数被设为1
					tdscListingInfo.setCurrRound(new BigDecimal(1));
					tdscListingInfo.setCurrPrice(tdscBlockTranApp.getInitPrice());// 无意向出让地块，第一轮挂牌价格可以为起始价
					tdscListingApp.setListPrice(tdscBlockTranApp.getInitPrice());// 无意向出让地块，第一轮挂牌价格可以为起始价
				}
				tdscListingApp.setListCert(bidderApp.getCertNo());
				tdscListingApp.setPriceType("11");// 网上交易竞价类型 11:挂牌 22:限时竞价
				tdscListingApp.setAppId(appId);
				tdscListingApp.setListNo(bidderApp.getConNum());

				this.tdscLocalTradeService.saveListingInfoAndApp(tdscListingInfo, tdscListingApp);
			} else {
				if (failBlockNoticeNo.length() > 0) {
					failBlockNoticeNo += "," + tdscBlockTranApp.getBlockNoticeNo();
				} else {
					failBlockNoticeNo = tdscBlockTranApp.getBlockNoticeNo();
				}
			}
		}

		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// 返回给回调函数的参数
		pw.write(failBlockNoticeNo);
		pw.close();

		return null;
	}
}
