package com.wonders.tdsc.publishinfo.web;

import java.math.BigDecimal;
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
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscSendEmailInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.FileUtils;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.out.service.Export4PubPlatService;
import com.wonders.tdsc.publishinfo.dao.TdscNoticeAppDao;
import com.wonders.tdsc.publishinfo.dao.TdscSendEmailInfoDao;
import com.wonders.tdsc.publishinfo.service.TdscNoticePublishService;
import com.wonders.tdsc.publishinfo.web.form.RecordAndResultForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.util.PropertiesUtil;

public class TdscTransResultAction extends BaseAction {

	private static final String			dapingPath	= PropertiesUtil.getInstance().getProperty("daping_path");

	private CommonQueryService			commonQueryService;

	private TdscBlockInfoService		tdscBlockInfoService;

	private AppFlowService				appFlowService;

	private TdscNoticePublishService	tdscNoticePublishService;

	private TdscNoticeAppDao			tdscNoticeAppDao;

	private TdscSendEmailInfoDao		tdscSendEmailInfoDao;
	
	private Export4PubPlatService		export4PubPlatService;

	public void setTdscSendEmailInfoDao(TdscSendEmailInfoDao tdscSendEmailInfoDao) {
		this.tdscSendEmailInfoDao = tdscSendEmailInfoDao;
	}

	public void setTdscNoticeAppDao(TdscNoticeAppDao tdscNoticeAppDao) {
		this.tdscNoticeAppDao = tdscNoticeAppDao;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscNoticePublishService(TdscNoticePublishService tdscNoticePublishService) {
		this.tdscNoticePublishService = tdscNoticePublishService;
	}

	public void setExport4PubPlatService(Export4PubPlatService export4PubPlatService) {
		this.export4PubPlatService = export4PubPlatService;
	}

	public ActionForward printResultNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");

		String noticeNo = "";
		String transferMode = "";
		List tdscBlockAppViewList = null;

		if (noticeId != null && !"".equals(noticeId)) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);
			condition.setOrderKey("blockNoticeNo");

			tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			tdscBlockAppViewList = filterCanceledBlock(tdscBlockAppViewList);

			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
				transferMode = tdscNoticeApp.getTransferMode();
			}
		}

		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);

		return mapping.findForward("printResultNotice");
	}

	private List filterCanceledBlock(List appViewList) {
		List retList = new ArrayList();
		if (appViewList != null && appViewList.size() > 0) {
			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) appViewList.get(i);
				if (!"04".equals(app.getTranResult())) {
					retList.add(app);
				}
			}
		}
		return retList;
	}

	/**
	 * 查询所有已经结束的公告，为了打印竞得人联系方式表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBidderedPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strTradeStatus = request.getParameter("tradeStatus") + "";

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

		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			condition.setIfResultPublish("1");
		} else {
			condition.setIfResultPublish("0");// 交易结果未公示的记录，未成交的数据
		}
		// 查询公告List
		List noticeList = new ArrayList();
		noticeList = tdscNoticePublishService.queryNoticeListByCondition(condition);
		// 根据condition，把List整合成PageLsit返回
		List rebuildPageList = new ArrayList();
		if (noticeList != null && noticeList.size() > 0) {
			rebuildPageList = tdscNoticePublishService.rebuildPageList(noticeList);
		}

		// 增加页面需要的字段 from tdscBlockAppView----strat
		if (rebuildPageList != null && rebuildPageList.size() > 0) {
			for (int i = 0; i < rebuildPageList.size(); i++) {

				TdscNoticeApp app = (TdscNoticeApp) rebuildPageList.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				conditionBlock.setNoticeId(app.getNoticeId());
				// conditionBlock.setTranResult("01");//只查询交易成功地块

				// 所有地块信息表
				// List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
				// String blockName = "";
				// if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				// for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				//
				// TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				//
				// blockName += blockapp.getBlockName() + ",";
				// app.setTranManTel(blockapp.getTradeNum());// 将"招拍挂编号"存在此字段中
				// app.setTranManAddr(blockapp.getTransferMode());// 将"出让方式"存在此字段中
				// }
				// blockName = blockName.substring(0, blockName.length() - 1);
				// app.setTranManName(blockName);// 将地块名称存在此字段中
				// }

				app.setTranManTel(app.getTradeNum());// 将"招拍挂编号"存在此字段中
				app.setTranManAddr(app.getTransferMode());// 将"出让方式"存在此字段中

			}
		}
		// -----end

		if (rebuildPageList != null && rebuildPageList.size() != 0) {
			pageList.setList(rebuildPageList);
		}
		pageList = PageUtil.getPageList(rebuildPageList, pageSize, currentPage);
		request.setAttribute("tranceResultNoticeList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("toEndBlockList");
	}

	// public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	//
	// TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
	// bindObject(condition, form);
	//
	// PageList pageList =new PageList();
	// if (request.getParameter("currentPage") != null)
	// condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));
	// //加入"结果公示"节点
	// condition.setNodeId(FlowConstants.FLOW_NODE_RESULT_SHOW);
	// // 设置页面行数
	// condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
	//
	// condition.setEndlessTag(GlobalConstants.QUERY_ENDLESS_TAG);
	//
	// pageList = (PageList)commonQueryService.queryTdscBlockAppViewPageList(condition);
	//
	// request.setAttribute("pageList", pageList);
	// request.setAttribute("condition", condition);
	//
	// return mapping.findForward("transResultList");
	// }

	/**
	 * 交易结果公示-显示公告列表
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接受查询条件
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
		// 查询公告List
		List noticeList = new ArrayList();
		if(condition.getIfResultPublish()==null)
		{
			condition.setIfResultPublish("0");// 交易结果未公示的记录，未成交的数据
		}
		noticeList = tdscNoticePublishService.queryNoticeListByCondition(condition);
		// 根据condition，把List整合成PageLsit返回
		List rebuildPageList = new ArrayList();
		if (noticeList != null && noticeList.size() > 0) {
			rebuildPageList = tdscNoticePublishService.rebuildPageList(noticeList);
		}

		// 增加页面需要的字段 from tdscBlockAppView----strat
		if (rebuildPageList != null && rebuildPageList.size() > 0) {
			for (int i = 0; i < rebuildPageList.size(); i++) {

				TdscNoticeApp app = (TdscNoticeApp) rebuildPageList.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				conditionBlock.setNoticeId(app.getNoticeId());
				// conditionBlock.setTranResult("01");//只查询交易成功地块

				// 所有地块信息表
				List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
				String blockName = "";
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int j = 0; j < tdscBlockAppViewList.size(); j++) {

						TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);

						blockName += blockapp.getBlockName() + ",";
						app.setTranManTel(blockapp.getTradeNum());// 将"招拍挂编号"存在此字段中
						app.setTranManAddr(blockapp.getTransferMode());// 将"出让方式"存在此字段中
					}
					blockName = blockName.substring(0, blockName.length() - 1);
					app.setTranManName(blockName);// 将地块名称存在此字段中
				}

			}
		}
		// -----end

		if (rebuildPageList != null && rebuildPageList.size() != 0) {
			pageList.setList(rebuildPageList);
		}
		pageList = PageUtil.getPageList(rebuildPageList, pageSize, currentPage);
		request.setAttribute("tranceResultNoticeList", pageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("tranceResultNoticeList");
	}

	/**
	 * 根据条件查询地块信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showTdscViewListByNoticeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接受页面传输的数据
		String noticeId = request.getParameter("noticeId");
		// 构造查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		bindObject(condition, form);
		condition.setNoticeId(noticeId);
		condition.setTranResult("01");
		// 查询地块信息列表
		List pageList = new ArrayList();
		pageList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		// 查询该公告的信息，组合字符串返回
		String noticeTitle = tdscNoticePublishService.initRelustNoticeTitle(noticeId);

		request.setAttribute("noticeTitle", noticeTitle);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("transeResltTdscView");
	}

	/**
	 * 显示某一地块的详细信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showTdscBlockAppViewByAppId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		// condition.setAppId(appId);
		// TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// tdscBlockAppView = (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);
		// //规划用途包括子地块
		// tdscBlockAppView=tdscBlockInfoService.tidyTdscBlockAppViewByBlockId(tdscBlockAppView);

		request.setAttribute("tdscBlockAppView", tdscBlockInfoService.tidyTdscBlockAppViewByBlockId(appId));
		return mapping.findForward("showTdscBlockAppViewByAppId");
	}

	public ActionForward fabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String appId = request.getParameter("appId");

		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockInfoService.findTdscBlockTranApp(appId);
		if (tdscBlockTranApp != null) {
			String transferMode = (String) tdscBlockTranApp.getTransferMode();
			// 获得用户信息
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			// 修改 实体的 是否发布的状态 并记录发布时间
			tdscBlockInfoService.modIfPublish(appId, transferMode, user);
		}

		return query(mapping, form, request, response);
	}

	/**
	 * 出让公告发布
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward publishNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String noticeId = request.getParameter("noticeId");
		String recordId = request.getParameter("recordId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		List tdscBlockTranAppList = new ArrayList();
		tdscBlockTranAppList = (List) tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, "");

		String noticeNo = "";
		if (null != noticeId) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
			}
		}

		// 获取发送列表
		List tdscSendEmailList = new ArrayList();
		if (request.getParameterValues("acceptOgr") != null && request.getParameterValues("acceptAddress") != null) {
			String[] acceptOgr = request.getParameterValues("acceptOgr");
			String[] acceptAddress = request.getParameterValues("acceptAddress");

			if (acceptOgr != null && acceptOgr.length > 0 && acceptAddress != null && acceptAddress.length > 0) {
				for (int i = 0; i < acceptOgr.length; i++) {
					TdscSendEmailInfo tdscSendEmailInfo = new TdscSendEmailInfo();
					tdscSendEmailInfo.setIfSend("1");
					tdscSendEmailInfo.setNoticeId(noticeId);
					tdscSendEmailInfo.setNoticeType("2");
					tdscSendEmailInfo.setAcceptOrg(acceptOgr[i]);
					tdscSendEmailInfo.setAcceptAddress(acceptAddress[i]);
					tdscSendEmailInfo.setRecordId(recordId);
					tdscSendEmailInfo.setNoticeNo(noticeNo);

					tdscSendEmailList.add(tdscSendEmailInfo);
				}
			}
		}

		if (tdscBlockTranAppList != null && tdscBlockTranAppList.size() > 0) {
			tdscNoticePublishService.publishNotice(tdscBlockTranAppList, tdscSendEmailList, user, noticeId, recordId);
		}
		//发给省平台
		try {
			export4PubPlatService.makeVm(noticeId, 1);
		} catch (Exception e) {
			logger.error("成交行为信息报错！noticeId="+noticeId+"\n"+e.getMessage());
		}
		List apps = this.tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, null);
		for(int b = 0; b < apps.size(); b++){
			TdscBlockTranApp app = (TdscBlockTranApp) apps.get(b);
			try {
				export4PubPlatService.makeVm(app.getAppId(), 2);
			} catch (Exception e) {
				logger.error("成交宗地信息报错！appId="+app.getAppId()+"\n"+e.getMessage());
			}
		}

		RecordAndResultForm recordAndResultForm = new RecordAndResultForm();
		return query(mapping, recordAndResultForm, request, response);
	}

	/**
	 * 成交公告暂存
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward zancun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得用户信息
		String noticeId = request.getParameter("noticeId");
		String recordId = request.getParameter("recordId");

		tdscNoticePublishService.zancunNotice(noticeId, recordId);

		request.setAttribute("saveMessage", "保存成功！");

		return query(mapping, form, request, response);
	}

	/**
	 * 成交公告及邮件列表暂存
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fbZanCun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得用户信息
		String noticeId = request.getParameter("noticeId");
		String recordId = request.getParameter("recordId");

		String noticeNo = "";
		if (null != noticeId) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
			}
		}

		// 获取发送列表
		List tdscSendEmailList = new ArrayList();
		if (request.getParameterValues("acceptOgr") != null && request.getParameterValues("acceptAddress") != null) {
			String[] acceptOgr = request.getParameterValues("acceptOgr");
			String[] acceptAddress = request.getParameterValues("acceptAddress");

			if (acceptOgr != null && acceptOgr.length > 0 && acceptAddress != null && acceptAddress.length > 0) {
				for (int i = 0; i < acceptOgr.length; i++) {
					TdscSendEmailInfo tdscSendEmailInfo = new TdscSendEmailInfo();
					tdscSendEmailInfo.setIfSend("0");
					tdscSendEmailInfo.setNoticeId(noticeId);
					tdscSendEmailInfo.setNoticeType("2");
					tdscSendEmailInfo.setAcceptOrg(acceptOgr[i]);
					tdscSendEmailInfo.setAcceptAddress(acceptAddress[i]);
					tdscSendEmailInfo.setRecordId(recordId);
					tdscSendEmailInfo.setNoticeNo(noticeNo);

					tdscSendEmailList.add(tdscSendEmailInfo);
				}
			}
		}

		tdscNoticePublishService.zancunNoticeAndEmail(noticeId, recordId, tdscSendEmailList);

		request.setAttribute("saveMessage", "保存成功！");

		RecordAndResultForm recordAndResultForm = new RecordAndResultForm();
		return query(mapping, recordAndResultForm, request, response);
	}

	/**
	 * 出让公告iweb显示，修改
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward iwebResultNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");
		String type = request.getParameter("type");

		String noticeTitle = "";
		String fileName = "";

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("type", type);

		if (noticeId != null && !"".equals(noticeId)) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);
			// condition.setTranResult("01");
			// condition.setTranResult("01");
			condition.setOrderKey("xuHao");
			List tdscBlockAppViewList = new ArrayList();
			List tdscBlockPartList = new ArrayList();
			List returnList = new ArrayList();

			String transferModeShort = "";
			// 获取xml文件有效时间，精确到秒，用来组成xml文件的名称（系统当前日期大于有效时间中的日期时，将文件移动到备份目录下）初始化为1900年01月01日00时00分00秒
			String endTime = "19000101000000";
			
			String tradeNum = "0000000";//用来存放招拍挂编号的年份和流水号，并将该变量组成文件名，初始默认为0000000(4位年份加3位流水号)
	        String ydxz = "000";//用地性质,分工业和经营性两种,代码为3位数字

			tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				for (int i = 0; i < tdscBlockAppViewList.size();) {
					TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);

					// 过滤掉由交易中心人员业务终止的地块
					if ("04".equals(tdscBlockAppView.getTranResult())) {
						tdscBlockAppViewList.remove(i);
						continue;
					} else {
						i++;
					}
					
					if(StringUtils.isNotBlank(tdscBlockAppView.getBlockQuality())){
						ydxz = tdscBlockAppView.getBlockQuality();
					}
					
					if(StringUtils.isNotBlank(tdscBlockAppView.getTradeNum()) && tdscBlockAppView.getTradeNum().split("-").length == 3){//取招拍挂编号里的年份和流水号
						tradeNum = tdscBlockAppView.getTradeNum().split("-")[1] + tdscBlockAppView.getTradeNum().split("-")[2];
					}

					String tmp = tdscBlockAppView.getNoitceNo();

					if (StringUtils.isNotBlank(tdscBlockAppView.getTransferMode())) {
						if ("3107".equals(tdscBlockAppView.getTransferMode())) {// 招标
							transferModeShort = "Z_";
						} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {// 拍卖
							transferModeShort = "P_";
						} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {// 挂牌
							transferModeShort = "G_";
						}
					}
					String tranMode = "";
					if ("3107".equals(tdscBlockAppView.getTransferMode())) {
						fileName = "Z_" + DateUtil.date2String(tdscBlockAppView.getOpeningDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
						tranMode = "招标";
					} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
						fileName = "P_" + DateUtil.date2String(tdscBlockAppView.getActionDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
						tranMode = "拍卖";
					} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
						fileName = "G_" + DateUtil.date2String(tdscBlockAppView.getSceBidDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
						tranMode = "挂牌";
					} else {
						fileName = "0_19000101000000" + ydxz + tradeNum;
					}

					// noticeTitle = tmp.substring(0, 10) + Integer.parseInt(tmp.substring(10, 12)) + "号国有建设用地使用权" + tranMode + "出让成交公告";
					noticeTitle = "国有建设用地使用权" + tranMode + "出让成交公告";

					endTime = DateUtil.date2String(tdscBlockAppView.getResultShowDate(), "yyyyMMddHHmmss");

					String blockId = tdscBlockAppView.getBlockId();
					String transferMode = tdscBlockAppView.getTransferMode();
					String tdyt = "";
					BigDecimal blockArea = new BigDecimal(0.00);

					// 查询子地块
					tdscBlockPartList = (List) tdscBlockInfoService.getTdscBlockPartList(blockId);
					if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
						for (int j = 0; j < tdscBlockPartList.size(); j++) {
							TdscBlockPart tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);

							tdscBlockPart.setRangeEast(StringUtils.trimToEmpty(tdscBlockAppView.getLandLocation()));// 土地位置
							if (!StringUtils.isEmpty(tdscBlockAppView.getResultName()))
								tdscBlockPart.setRangeSouth(tdscBlockAppView.getResultName());// 竞得人
							else
								tdscBlockPart.setRangeSouth("由市国土资源局收回");

							tdscBlockPart.setRangeWest(tdscBlockAppView.getBlockNoticeNo().substring(tdscBlockAppView.getBlockNoticeNo().length() - 1,
									tdscBlockAppView.getBlockNoticeNo().length()));
							tdscBlockPart.setTotalLandArea(tdscBlockAppView.getInitPrice());// 拍卖起叫价
							if (tdscBlockAppView.getResultPrice() != null)
								tdscBlockPart.setPlanBuildingArea(tdscBlockAppView.getResultPrice());// 成交土地单价
							else
								tdscBlockPart.setPlanBuildingArea(new BigDecimal("0"));

							tdscBlockPart.setBlockName(tdscBlockAppView.getBlockName());// 地块名
							tdscBlockPart.setBlockCode(tdscBlockAppView.getBlockNoticeNo());// 地块编号

							returnList.add(tdscBlockPart);
						}
					}

					request.setAttribute("transferMode", transferMode);
					request.setAttribute("tdscBlockAppView", tdscBlockAppView);
				}
			}

			// 成交公告现在使用xml文件展示到大屏
			// add by xys
			if("cjggfabu".equals(type)){
				makeXmlFileToCjgg(fileName, noticeTitle, returnList);
			}
			
			request.setAttribute("fileName", transferModeShort + endTime + ydxz + tradeNum + ".html");// 拼成交公告html文件的文件名

			// 查询有无已保存的邮件发送列表 成交公告2
			List tdscSendEmailList = new ArrayList();
			tdscSendEmailList = tdscSendEmailInfoDao.querySendEmailListByType(noticeId, "2");
			request.setAttribute("tdscSendEmailList", tdscSendEmailList);

			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			String resultRecordId = StringUtils.trimToEmpty(tdscNoticeApp.getResultRecordId());

			request.setAttribute("noticeNo", StringUtils.trimToEmpty(tdscNoticeApp.getNoticeNo()));
			request.setAttribute("recordId", resultRecordId);
			request.setAttribute("modeNameEn", "cjqkgg.doc");
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
			request.setAttribute("tdscBlockPartList", returnList);

		}

		return mapping.findForward("iwebResultNotice");
	}

	/**
	 * 成交公告使用 xml 文件展示到大屏 生成 xml 文件到 CJGG 目录
	 * 
	 * @param noticeTitle
	 * @param returnList
	 */
	private void makeXmlFileToCjgg(String fileName, String noticeTitle, List contextList) {

		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		sb.append("<root>");
		sb.append("<noticeTitle>").append(noticeTitle).append("</noticeTitle>");
		if (contextList != null && contextList.size() > 0) {
			for (int i = 0; i < contextList.size(); i++) {
				TdscBlockPart info = (TdscBlockPart) contextList.get(i);
				sb.append("<context>");
				sb.append("<blockNo>").append(info.getBlockCode()).append("</blockNo>");
				sb.append("<blockName>").append(info.getBlockName()).append("</blockName>");
				sb.append("<blockLocation>").append(info.getRangeEast()).append("</blockLocation>");
				sb.append("<bidderPerson>").append(info.getRangeSouth()).append("</bidderPerson>");
				if (info.getPlanBuildingArea().intValue() == 0)
					sb.append("<cjPrice>").append("").append("</cjPrice>");
				else
					sb.append("<cjPrice>").append(info.getPlanBuildingArea()).append("</cjPrice>");
				sb.append("</context>");
			}
		}
		sb.append("</root>");

		String filePath = dapingPath + "cjgg\\";
		fileName += ".xml";
		FileUtils.saveStringToFile(sb.toString(), filePath, fileName);

	}

	/**
	 * 出让公告发布
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tradeResultList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");

		String tradeNum = "";
		String transferMode = "";
		String startDate = "";// 挂牌：现场竞价时间；拍卖：拍卖会时间；
		BigDecimal allBlockPrice = new BigDecimal(0.00);// 所有地块总价
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		condition.setOrderKey("xuHao");
		// 所有地块信息表
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				tradeNum = tdscBlockAppView.getTradeNum();
				transferMode = tdscBlockAppView.getTransferMode();
				if ("3104".equals(transferMode))
					startDate = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getSceBidDate(), "yyyy年MM月dd日"));
				else
					startDate = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getAuctionDate(), "yyyy年MM月dd日"));

				// 填充页面数据
				String unionBlockCode = tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId());
				tdscBlockAppView.setUnitebBlockCode(unionBlockCode);// 地块编号集合
				String unionLandUseType = tdscBlockInfoService.tidyLandUseTypeByBlockId(tdscBlockAppView.getBlockId());
				tdscBlockAppView.setBlockLandId(unionLandUseType);// 土地用途集合

				List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());
				BigDecimal totalLandArea = new BigDecimal(0.00);// 总土地面积
				BigDecimal totalBuildingArea = new BigDecimal(0.00);// 总规划建筑面积
				if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
					for (int j = 0; j < tdscBlockPartList.size(); j++) {
						TdscBlockPart tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
						if (tdscBlockPart.getBlockArea() != null && tdscBlockPart.getBlockArea().compareTo(new BigDecimal("0.00")) == 1)
							totalLandArea = totalLandArea.add(tdscBlockPart.getBlockArea());
						if (tdscBlockPart.getPlanBuildingArea() != null && tdscBlockPart.getPlanBuildingArea().compareTo(new BigDecimal("0.00")) == 1)
							totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
					}
				}
				tdscBlockAppView.setTotalLandArea(totalLandArea);// 土地面积
				tdscBlockAppView.setTotalBuildingArea(totalBuildingArea);// 规划建筑面积

				if (tdscBlockAppView.getTotalPrice() != null && tdscBlockAppView.getTotalPrice().compareTo(new BigDecimal("0.00")) == 1) {
					tdscBlockAppView.setTotalPrice(tdscBlockAppView.getTotalPrice().movePointLeft(4));
					allBlockPrice = allBlockPrice.add(tdscBlockAppView.getTotalPrice());
				}
			}
		}

		request.setAttribute("allBlockPrice", NumberUtil.numberDisplay(allBlockPrice, 1));
		request.setAttribute("tradeNum", tradeNum);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("startDate", startDate);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);

		return mapping.findForward("tradeResultList");
	}

}
