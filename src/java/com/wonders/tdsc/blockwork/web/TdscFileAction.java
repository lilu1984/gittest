package com.wonders.tdsc.blockwork.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.blockwork.web.form.TdscDicBean;
import com.wonders.tdsc.blockwork.web.form.TdscFileForm;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.kyq.bo.KyqNotice;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.common.util.MoneyUtils;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.kyq.service.KyqContent;
import com.wonders.tdsc.kyq.service.KyqNoticeService;
import com.wonders.tdsc.lob.bo.TdscEsClob;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscFileAction extends BaseAction {

	private CommonQueryService commonQueryService;

	private TdscFileService tdscFileService;

	private TdscBlockInfoService tdscBlockInfoService;

	private AppFlowService appFlowService;

	private TdscScheduletableService tdscScheduletableService;

	private TdscNoticeService tdscNoticeService;

	private TdscBidderAppService tdscBidderAppService;

	private IdSpringManager idSpringManager;
	
	
	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	/**
	 * 根据通用接口获得土地信息
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
	/**
	 * 查询待办业务列表
	 */
	public ActionForward queryAppListWithNodeId_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		/*
		 * String blockName = request.getParameter("blockName"); String blockType = request.getParameter("blockType"); String transferMode = request.getParameter("transferMode");
		 */
		bindObject(condition, form);
		String currentPage = request.getParameter("currentPage");
		/* String districtId = request.getParameter("districtId"); */
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		String nodeId = "03";
		// 获取用户结点权限
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);
		/*
		 * condition.setBlockName(blockName); condition.setBlockType(blockType); condition.settransferMode(transferMode);
		 */

		condition.setNodeList(nodeList); // 根据用户权限查询待办列表
		condition.setNodeId(nodeId);
		condition.setCurrentPage(cPage);
		/* condition.setDistrictId(districtId); */
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewFlowPageList(condition));// 按条件查询列表
		request.setAttribute("queryAppCondition", condition);

		return mapping.findForward("list");
		// return mapping.findForward("listnew");
	}

	// 查询出所有可制做出让文件的土地信息列表
	public ActionForward newCRWJ(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		bindObject(condition, form);

		// String issueStartDate = "";
		if (!"1".equals(request.getParameter("forwardType"))) {
			bindObject(condition, form);
			condition.setTransferMode(request.getParameter("transferMode"));
			// issueStartDate = request.getParameter("issueStartDate");
		}

		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		String nodeId = "03";
		condition.setNoitceNo("null");

		String noticeId = request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);

		String noticeNo = request.getParameter("noticeNo");
		if (!"".equals(noticeNo) && noticeNo != null) {
			noticeNo = "沪告字[" + noticeNo.substring(0, 4) + "]第" + noticeNo.substring(4) + "号";
			request.setAttribute("noticeNo", noticeNo);
		}

		// 获取用户结点权限
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);
		/*
		 * condition.setBlockName(blockName); condition.setBlockType(blockType); condition.settransferMode(transferMode);
		 */

		condition.setNodeList(nodeList); // 根据用户权限查询待办列表
		condition.setNodeId(nodeId);
		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
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
		// PageList pageList =
		// commonQueryService.queryTdscBlockAppViewFlowPageList(condition);

		List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewFlowList(condition);
		// if(pageList!=null){
		// tdscBlockAppViewList = pageList.getList();
		// }
		List tdscBlockPlanTableList = new ArrayList();
		List planIdList = new ArrayList();

		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				String planId = tdscBlockAppView.getPlanId();

				if (planId != null && !"".equals(planId) && !planIdList.contains(planId)) {
					planIdList.add(planId);
					TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);
					if (tdscBlockPlanTable != null)
						tdscBlockPlanTableList.add(tdscBlockPlanTable);
				}
			}
		}
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("pageList", tdscBlockPlanTableList);
		request.setAttribute("tdscBlockPlanTableList", tdscBlockPlanTableList);// 按条件查询列表
		request.setAttribute("condition", condition);

		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		if (tdscBlockPlanTableList != null && tdscBlockPlanTableList.size() > 0) {
			int size = tdscBlockPlanTableList.size();
			tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableList.get(size - 1);
		}

		java.util.Date sysdate = new java.util.Date();
		java.text.SimpleDateFormat dformat = new java.text.SimpleDateFormat("yyyy");
		String year = (String) dformat.format(sysdate);

		String noticeNoPrefix = "";
		String tradeNum = tdscBlockPlanTable.getTradeNum();
		if(StringUtils.isNotBlank(tradeNum) && tradeNum.indexOf("租") != -1){
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "锡" + tradeNum.substring(4, 6) + "告字[" + year + "]";
			} else {
				noticeNoPrefix = "锡经租告字[" + year + "]";
			}
		}else{
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "锡" + tradeNum.substring(4, 5) + "告字[" + year + "]";
			} else {
				noticeNoPrefix = "锡经告字[" + year + "]";
			}
		}

		String blockQuality = "102";
		if ("工".equals(noticeNoPrefix.substring(1, 2))) {
			blockQuality = "101";
		} else if ("经".equals(noticeNoPrefix.substring(1, 2))) {
			blockQuality = "102";
		}
		request.setAttribute("blockQuality", blockQuality);

		// 如果第一次进入页面则产生公告号，以后每次查询不再产生新的公告号
		if ("1".equals(request.getParameter("ifDisplay"))) {
			request.setAttribute("ifDisplay", "1");
			String temp = request.getParameter("noticeNo");
			request.setAttribute("createNoticeNo", temp);
		} else {
			// String temp = (String) tdscNoticeService.selectNoticeNo();
			String temp ="";
			if(StringUtils.isNotBlank(tradeNum) && tradeNum.indexOf("租") != -1){
				temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("17",noticeNoPrefix);
			}else{
				temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("15",noticeNoPrefix);
			}
			request.setAttribute("createNoticeNo", noticeNoPrefix + temp + "号");
		}

		return mapping.findForward("listNewCrwj");
	}

	/**
	 * 查询待制作出让文件列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAppFileListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取当前用户
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// 出让地块申请查询条件
		TdscBaseQueryCondition sqCondition = new TdscBaseQueryCondition();
		// TdscBlockForm tdscBlockForm = (TdscBlockForm)form;
		// 将条件返回到主列表页面
		// request.setAttribute("condition", sqCondition);
		this.bindObject(sqCondition, form);
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		List districtIdList = null;
		List qxList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
		if (qxList != null && qxList.size() > 0) {
			districtIdList = qxList;
		}
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);

		// String nodeId = "03";

		if (sqCondition.getBlockName() != null && !"".equals(sqCondition.getBlockName())) {
			sqCondition.setBlockName(StringUtil.GBKtoISO88591(sqCondition.getBlockName()));
		}
		if (sqCondition.getAuditedNum() != null && !"".equals(sqCondition.getAuditedNum())) {
			sqCondition.setAuditedNum(StringUtil.GBKtoISO88591(sqCondition.getAuditedNum()));
		}
		// sqCondition.setNodeList(nodeList); // 根据用户权限查询待办列表

		// sqCondition.setOrderKey("actionDateBlock");//按所需字段排序
		// sqCondition.setOrderType("desc");
		// sqCondition.setNodeId(nodeId);
		sqCondition.setDistrictIdList(districtIdList);
		// sqCondition.setCurrentPage(currentPage);
		// sqCondition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		sqCondition.setUser(user);
		sqCondition.setOrderKey("tradeNum asc, a.blockName");

		// 查询所有 公告审核通过 后的数据
		// 编制公告后即可编制文件,回退完成编制公告后也需编制文件
		List statusIdList = new ArrayList();
		statusIdList.add("0303");
		// statusIdList.add("0305");
		statusIdList.add("0401");
		statusIdList.add("0601");
		statusIdList.add("0701");
		statusIdList.add("0702");
		statusIdList.add("0703");
		statusIdList.add("0801");
		statusIdList.add("1101");
		statusIdList.add("1201");
		statusIdList.add("1301");
		statusIdList.add("1401");
		statusIdList.add("1501");
		statusIdList.add("1502");
		statusIdList.add("1601");
		// statusIdList.add("0601");
		sqCondition.setStatusIdList(statusIdList);

		// 调用通用查询 查询列表信息
		List queryAppList = commonQueryService.queryTdscBlockAppViewListWithoutNode(sqCondition);

		// request.setAttribute("nodeId", nodeId);
		// 储备地块查询
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		// 设定status查询出未交易及交易中止的信息
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };

		// 储备地块查询条件
		TdscBlockInfoCondition cbCondition = new TdscBlockInfoCondition();
		this.bindObject(cbCondition, form);
		cbCondition.setStatus(status);

		// 判断是否是管理员
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			cbCondition.setUserId(user.getUserId());
		}

		// cbCondition.setCurrentPage(currentPage);
		// cbCondition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// 返回给入市申请页面 土地基本信息列表
		List cbBlockList = (List) tdscBlockInfoService.queryAppViewList(cbCondition);
		// 将条件返回到主列表页面
		// this.bindObject(sqCondition, tdscBlockForm);

		if (sqCondition.getBlockName() != null && !"".equals(sqCondition.getBlockName())) {
			sqCondition.setBlockName(StringUtil.ISO88591toGBK(sqCondition.getBlockName()));
		}
		if (sqCondition.getAuditedNum() != null && !"".equals(sqCondition.getAuditedNum())) {
			sqCondition.setAuditedNum(StringUtil.ISO88591toGBK(sqCondition.getAuditedNum()));
		}
		request.setAttribute("condition", sqCondition);

		Map blockNoticeMap = new HashMap();
		
		// 整合2个查询结果List
		if (queryAppList != null && queryAppList.size() > 0) {
			for (int i = 0; i < queryAppList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) queryAppList.get(i);

				// 受理竞买申请时间 前

				String getFileEndDate = DateUtil.date2String(appView.getAccAppEndDate(), "yyyyMMddHHmmss");
				String nowTime = DateConvertor.getCurrentDateWithTimeZone();

				Long f = new Long(getFileEndDate);
				Long n = new Long(nowTime);
				if (f.longValue() >= n.longValue()) {
					TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appView.getAppId());
					if (temp != null)
						appView.setBidEvaLoc("makedDoc"); // 如果不空 则显示查看

					cbBlockList.add(appView);
					TdscNoticeApp tdscNoticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(appView.getNoticeId());
					String ifReleased = null;
					if(tdscNoticeApp != null){
						ifReleased = tdscNoticeApp.getIfReleased();
					}
					//将该地块的ID和该地块所在出让公告是否发布set到map中
					if(ifReleased != null){
						blockNoticeMap.put(appView.getBlockId(),tdscNoticeService.getTdscNoticeAppByNoticeId(appView.getNoticeId()).getIfReleased());//将该地块的ID和该地块所在出让公告是否发布set到map中						
					}else{
						blockNoticeMap.put(appView.getBlockId(),"0");
					}
				}

			}
		}

		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		PageList PageList = PageUtil.getPageList(cbBlockList, pageSize, currentPage);

		request.setAttribute("queryAppList", PageList);// 返回列表
		request.setAttribute("blockNoticeMap", blockNoticeMap);

		return mapping.findForward("fileList");
	}

	public ActionForward viewDoc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		if (!StringUtil.isEmpty(appId)) {

			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 查询交易信息表(保证金截止时间)
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.findBlockTranAppInfo(appId);
			// 查询进度安排表
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(tdscBlockTranApp.getPlanId());
			request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);

			String strMethod = "";
			if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
				strMethod = "有意向";
			} else {
				strMethod = "无意向";
			}

			String transferMode = commonInfo.getTransferMode();

			String dicValue = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_BLOCK_TRANSFER).get(transferMode) + "";
			strMethod += "公开" + dicValue;

			request.setAttribute("purposeInfo", strMethod);// 有意向公开挂牌,等等.

			TdscBlockInfo blockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(commonInfo.getBlockId());

			List tdscBlockRemisemoneyDefrayList = tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockInfo.getBlockId());

			request.setAttribute("blockInfo", blockInfo);
			request.setAttribute("commonInfo", commonInfo);
			request.setAttribute("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);

			// Get TDSC_BLOCK_PART information by block_id
			List blockPartList = tdscBlockInfoService.getTdscBlockPartList(commonInfo.getBlockId());
			if (blockPartList != null) {
				String partNums = "";
				// String partArea = "";
				BigDecimal partArea = new BigDecimal("0.0");

				for (int i = 0; i < blockPartList.size(); i++) {
					TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(i);
					partNums += blockPart.getBlockCode() + ", ";
					partArea = partArea.add(blockPart.getBlockArea());
				}

				request.setAttribute("partNums", partNums);// 地块编号
				request.setAttribute("partArea", partArea);// 地块面积

			}

			String bankName = "";
			String bankAccount = "";
			List bankDicList = tdscBlockInfoService.queryBankDicList();
			if (StringUtils.isNotBlank(commonInfo.getMarginBank())) {
				if (bankDicList.size() > 0) {
					for (int i = 0; i < bankDicList.size(); i++) {
						TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
						if (commonInfo.getMarginBank().equals(dicBank.getDicCode())) {
							bankName = dicBank.getDicDescribe();
							bankAccount = dicBank.getDicValue();
						}
					}
				}
			}
			request.setAttribute("bankName", bankName);
			request.setAttribute("bankAccount", bankAccount);

			// DicDataUtil.getInstance().getDicItemName(arg0, arg1)//字典相关

			// 挂牌文件取得日期,即出让文件发售日期
			String d1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "yyyy年M月d日");
			String t1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "HH:mm");
			String d2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "yyyy年M月d日");
			String t2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "HH");
			String t3 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "mm");
			String swDate = d1 + "至" + d2 + ("00".equals(t3) ? t2 + "时" : t2 + "时" + t3 + "分");
			request.setAttribute("shouwenDate", swDate);

			// 提交申请日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "yyyy年M月d日");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "yyyy年M月d日");
			t2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "HH");
			t3 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "mm");
			String tjDate = d1 + "9时至" + d2 + ("00".equals(t3) ? t2 + "时" : t2 + "时" + t3 + "分");
			request.setAttribute("tijiaoDate", tjDate);

			// 公告起始日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(), "yyyy年M月d日");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(), "yyyy年M月d日");
			// t2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(), "hh:mm");
			String ggDate = d1 + "至" + d2;
			request.setAttribute("gonggaoDate", ggDate);
			request.setAttribute("gonggaokaishiDate", d1);

			// 现场竞价日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "yyyy年M月d日");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "HH时mm分");
			String jjDate = d1 + "" + t1;
			request.setAttribute("jingjiaDate", jjDate);
			request.setAttribute("chengjiaoDate", d1);

			// 自助挂牌日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getListStartDate(), "yyyy年M月d日");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "yyyy年M月d日");
			String h1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "HH");
			String s1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "mm");
			String gpDate = d1 + "9时00分至" + d2 + h1 + "时" + s1 + "分";
			String gpjzDate = d2 + h1 + "时" + s1 + "分";
			request.setAttribute("guapaiDate", gpDate);
			request.setAttribute("guapaijiezhiDate", gpjzDate);

			// 系统当前日期
			Calendar calendar = Calendar.getInstance();
			String sysDate = DateUtil.date2String(calendar.getTime(), "yyyy年M月d日");
			request.setAttribute("sysDate", sysDate);

			String blockType = "";
			blockType = commonInfo.getBlockType();
			// String transferMode = "";
			transferMode = commonInfo.getTransferMode();
			// 招标
			if ("3107".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地招标模版.doc");
					request.setAttribute("modeNameEn", "gyxydzb.doc");
				} else {// 经营性用地
					request.setAttribute("modeName", "经营性用地招标模版.doc");
					request.setAttribute("modeNameEn", "jyxydzb.doc");
				}
			}

			// 拍卖
			if ("3103".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地拍卖模版.doc");
					request.setAttribute("modeNameEn", "2008gyxydpm.doc");
				} else {// 经营性用地
					request.setAttribute("modeName", "经营性用地拍卖模版.doc");
					request.setAttribute("modeNameEn", "2008jyxydpm.doc");
				}
			}

			// 挂牌
			if ("3104".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地挂牌模版.doc");
					request.setAttribute("modeNameEn", "2008gyxydgp.doc");
				} else {// 经营性用地
					// LocalTradeType等于“1”时，是“现场书面报价”的形式，采用"出让文件__住宅版挂牌文件.doc"
					if ("1".equals(commonInfo.getLocalTradeType())) {
						request.setAttribute("modeNameEn", "2008jyxydgp_xcsmbj.doc");
						// LocalTradeType等于“2”时，是“举牌竞价”的形式，采用"出让文件__非住宅版挂牌文件.doc"
					} else if ("2".equals(commonInfo.getLocalTradeType())) {
						request.setAttribute("modeNameEn", "2008jyxydgp_jpjj.doc");
					}
					request.setAttribute("modeName", "经营性用地挂牌模版.doc");
				}
				// 现在无锡只有一种模板
				request.setAttribute("modeNameEn", "wxfile.doc");
			}

			// 如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
			TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
			if (temp != null) {
				request.setAttribute("ifZancun", "1");
				request.setAttribute("fileName", temp.getFileUrl());
				request.setAttribute("oldAppId", appId);
				request.setAttribute("recordId", temp.getRecordId());
			}

			// String fileName = tdscFileService.getFileName(appId);
			// String fileUrl = appId + ".doc";
			// request.setAttribute("fileName", fileName);
			// request.setAttribute("fileUrl", fileUrl);

		}
		request.setAttribute("view", "true");

		return mapping.findForward("viewFileInfo");

		// return mapping.findForward("previewFileInfo");
	}

	/**
	 * 出让文件的制作
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward makeDoc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String nodeId = request.getParameter("nodeId");
		String statusId = request.getParameter("statusId");
		// String view = request.getParameter("view");
		request.setAttribute("view", "false");
		String recordId = "";

		String retPage = "";
		String ifOnLine = "0";//交易方式，0为现场，1为限时

		if (appId != null) {

			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 查询交易信息表(保证金截止时间)
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.findBlockTranAppInfo(appId);
			// 查询进度安排表
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(tdscBlockTranApp.getPlanId());
			ifOnLine = tdscBlockPlanTable.getIfOnLine();//交易方式，0为现场，1为限时
			request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);

			String strMethod = "";
			if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
				strMethod = "有意向";
			} else {
				strMethod = "无意向";
			}

			String transferMode = commonInfo.getTransferMode();

			String dicValue = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_BLOCK_TRANSFER).get(transferMode) + "";
			strMethod += "公开" + dicValue;

			request.setAttribute("purposeInfo", strMethod);// 有意向公开挂牌,等等.

			TdscBlockInfo blockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(commonInfo.getBlockId());

			List tdscBlockRemisemoneyDefrayList = tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockInfo.getBlockId());

			request.setAttribute("blockInfo", blockInfo);
			request.setAttribute("commonInfo", commonInfo);
			request.setAttribute("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);

			// Get TDSC_BLOCK_PART information by block_id
			List blockPartList = tdscBlockInfoService.getTdscBlockPartList(commonInfo.getBlockId());
			if (blockPartList != null) {
				String partNums = "";
				// String partArea = "";
				BigDecimal partArea = new BigDecimal("0.0");

				for (int i = 0; i < blockPartList.size(); i++) {
					TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(i);
					partNums += blockPart.getBlockCode() + ", ";
					partArea = partArea.add(blockPart.getBlockArea());
				}

				request.setAttribute("partNums", partNums);// 地块编号
				request.setAttribute("partArea", partArea);// 地块面积

				TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(0);
				request.setAttribute("tdscBlockPart", blockPart);// 地块编号

				BigDecimal totalArea = blockInfo.getTotalLandArea();
				BigDecimal serviceCharge = blockPart.getServiceCharge();
				BigDecimal chargeLower = (totalArea.multiply(serviceCharge)).setScale(0, BigDecimal.ROUND_HALF_UP);

				String chargeUpper = MoneyUtils.NumToRMBStrWithJiao(Double.parseDouble(chargeLower + ""));
				request.setAttribute("chargeLower", chargeLower + "");
				request.setAttribute("chargeUpper", chargeUpper + "");
				
				if(blockInfo.getGeologicalHazard()!=null && blockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//填写了地质灾害评估费，并且大于0
					String dzzhpgfUpper = MoneyUtils.NumToRMBStrWithJiao(Double.parseDouble(blockInfo.getGeologicalHazard() + ""));//地质灾害评估费
					request.setAttribute("dzzhpgfUpper", dzzhpgfUpper + "");
					
					List bankDicList = tdscBlockInfoService.queryGeologyAssessUintDicList();
					if(StringUtils.isNotBlank(blockInfo.getGeologyAssessUint())){
						if (bankDicList != null && bankDicList.size() > 0) {
							for (int i = 0; i < bankDicList.size(); i++) {
								TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
								if (blockInfo.getGeologyAssessUint().equals(dicBank.getDicCode())) {
									request.setAttribute("geologyAssessUint", dicBank.getDicValue());//户名
									request.setAttribute("geologyAssessUintBankName", dicBank.getDicDescribe().split(",")[0]);//DIC_DESCRIBE里面用逗号分割银行名称和银行账号,财务联系电话，传真
									request.setAttribute("geologyAssessUintBankNum", dicBank.getDicDescribe().split(",")[1]);//DIC_DESCRIBE里面用逗号分割银行名称和银行账号,财务联系电话，传真
									request.setAttribute("geologyAssessUintFinTel", dicBank.getDicDescribe().split(",")[2]);//DIC_DESCRIBE里面用逗号分割银行名称和银行账号,财务联系电话，传真
									request.setAttribute("geologyAssessUintFox", dicBank.getDicDescribe().split(",")[3]);//DIC_DESCRIBE里面用逗号分割银行名称和银行账号,财务联系电话，传真
								}
							}
						}
					}
				}
			}

			String bankName = "";
			String bankAccount = "";
			String finTel = "";
			String fox = "";
			List bankDicList = tdscBlockInfoService.queryBankDicList();
			if (StringUtils.isNotBlank(commonInfo.getMarginBank())) {
				if (bankDicList.size() > 0) {
					for (int i = 0; i < bankDicList.size(); i++) {
						TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
						if (commonInfo.getMarginBank().equals(dicBank.getDicCode())) {
							bankName = dicBank.getDicDescribe();
							bankAccount = dicBank.getDicValue();
							finTel = dicBank.getDicValue();
							fox = dicBank.getDicValue();
						}
					}
				}
			}
			request.setAttribute("bankName", bankName);
			request.setAttribute("bankAccount", bankAccount);
			request.setAttribute("finTel", finTel);
			request.setAttribute("fox", fox);

			// DicDataUtil.getInstance().getDicItemName(arg0, arg1)//字典相关

			// 挂牌文件取得日期,即出让文件发售日期
			String d1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "yyyy年M月d日");
			String t1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "HH:mm");
			String d2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "yyyy年M月d日");
			String t2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "HH");
			String t3 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "mm");
			String swDate = d1 + "9时至" + d2 + ("00".equals(t3) ? t2 + "时" : t2 + "时" + t3 + "分");
			request.setAttribute("shouwenDate", swDate);

			// 提交申请日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "yyyy年M月d日");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "yyyy年M月d日");
			t2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "HH");
			t3 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "mm");
			String tjDate = d1 + "9时至" + d2 + ("00".equals(t3) ? t2 + "时" : t2 + "时" + t3 + "分");
			request.setAttribute("tijiaoDate", tjDate);
			
			//竞买报名日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "yyyy年M月d日");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "yyyy年M月d日");
			t2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "HH");
			t3 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "mm");
			String bmDate = d1 + "至" + d2 + ("00".equals(t3) ? t2 + "时" : t2 + "时" + t3 + "分");
			request.setAttribute("bmDate", bmDate);

			// 公告起始日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(), "yyyy年M月d日");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(),
			// "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(), "yyyy年M月d日");
			// t2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(),
			// "hh:mm");
			String ggDate = d1 + "至" + d2;
			request.setAttribute("gonggaoDate", ggDate);
			request.setAttribute("gonggaokaishiDate", d1);

			// 现场竞价日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "yyyy年M月d日");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "HH时mm分");
			String jjDate = d1 + "" + t1;
			request.setAttribute("jingjiaDate", jjDate);
			request.setAttribute("chengjiaoDate", d1);

			// 自助挂牌日期
			d1 = DateUtil.date2String(tdscBlockPlanTable.getListStartDate(), "yyyy年M月d日");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(),
			// "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "yyyy年M月d日");
			String h1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "HH");
			String s1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "mm");
			String gpDate = d1 + "9时00分至" + d2 + h1 + "时" + s1 + "分";
			String gpjzDate = d2 + h1 + "时" + s1 + "分";
			request.setAttribute("guapaiDate", gpDate);
			request.setAttribute("guapaijiezhiDate", gpjzDate);

			// 系统当前日期
			Calendar calendar = Calendar.getInstance();
			String sysDate = DateUtil.date2String(calendar.getTime(), "yyyy年M月d日");

			String tempSysDate = DateConvertor.formatStr(DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd"));
			String yearAndMonth = DateConvertor.getYearStr(tempSysDate) + "年" + DateConvertor.getMonthStr(tempSysDate) + "月";

			request.setAttribute("sysDate", sysDate);
			request.setAttribute("yearAndMonth", yearAndMonth);
			
			//处理分局名称和地址
			String districtId = blockInfo.getDistrictId().toString();
			Map map = getDistrictMap();
			ArrayList al = (ArrayList)map.get(districtId);
			request.setAttribute("dkfj", al.get(0));
			request.setAttribute("dkfjdz", al.get(1));
			
			String blockType = "";
			blockType = commonInfo.getBlockQuality();
			// String transferMode = "";
			transferMode = commonInfo.getTransferMode();
			// 招标
			if ("3107".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地招标模版.doc");
					request.setAttribute("modeNameEn", "gyxydzb.doc");
				} else {// 经营性用地
					request.setAttribute("modeName", "经营性用地招标模版.doc");
					request.setAttribute("modeNameEn", "jyxydzb.doc");
				}
			}

			// 拍卖
			if ("3103".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地拍卖模版.doc");
					request.setAttribute("modeNameEn", "2008gyxydpm.doc");
				} else {// 经营性用地
					request.setAttribute("modeName", "经营性用地拍卖模版.doc");
					request.setAttribute("modeNameEn", "2008jyxydpm.doc");
				}
			}

			// 挂牌
			if ("3104".equals(transferMode)) {
				if ("101".equals(blockType)) {
					// 工业性用地
					retPage = "makeGpIndustryFile";
					if("1".equals(commonInfo.getIsZulin())){
						request.setAttribute("modeName", "工业性用地(租赁)挂牌模版限时版.doc");
						request.setAttribute("modeNameEn", "gp_industry_time_zl.doc");
					}else{
						request.setAttribute("modeName", "工业性用地挂牌模版限时版.doc");
						request.setAttribute("modeNameEn", "gp_industry_time.doc");
					}
				} else {
					// 经营性用地
					retPage = "makeGpOperatingFile";
					if("1".equals(ifOnLine)){
						if("1".equals(commonInfo.getIsZulin())){
							request.setAttribute("modeName", "经营性用地(租赁)挂牌模版限时版.doc");
							request.setAttribute("modeNameEn", "gp_operating_time_zl.doc");
						}else{
							request.setAttribute("modeName", "经营性用地挂牌模版限时版.doc");
							request.setAttribute("modeNameEn", "gp_operating_time.doc");
							
						}
					}else{
						request.setAttribute("modeName", "经营性用地挂牌模版.doc");
						request.setAttribute("modeNameEn", "gp_operating.doc");
					}
				}
			}

			// 如果类型为制做出让公告
			if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {

			}// 如果类型为暂存出让公告,审核出让公告
			else {
				String fileName = tdscFileService.getFileName(appId);
				String fileUrl = appId + ".doc";
				request.setAttribute("fileName", fileName);
				request.setAttribute("fileUrl", fileUrl);
			}
		}

		// 如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
		TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
		// TdscBlockFileApp temp = null;
		if (temp != null) {
			request.setAttribute("ifZancun", "1");
			request.setAttribute("fileName", temp.getFileUrl());
			request.setAttribute("oldAppId", appId);
			request.setAttribute("recordId", temp.getRecordId());
		} else {
			request.setAttribute("recordId", recordId);
		}

		Map returnMap = new HashMap();
		if (appId != null) {
			returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
		}
		request.setAttribute("opnninfo", returnMap);
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("appId", appId);
		request.setAttribute("statusId", statusId);

		// if (temp != null)
		// return mapping.findForward("viewFileInfo");

		return mapping.findForward(retPage);

		// 待审核出让文件
		// if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId) || FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
		// if (temp != null)
		// return mapping.findForward("xiugai");
		// else {
		// // 制作
		//
		// }
		// } else {
		// return mapping.findForward("viewFileInfo");
		// }
	}
	/**
	 * 获取分局数据集合（分局名称和地址）
	 * @param districtId
	 * @return
	 */
	private Map getDistrictMap(){
		HashMap map = new HashMap();
		ArrayList al = null;
		al = new ArrayList();
		al.add("锡山分局");
		al.add("无锡市锡山区东亭南路2号");
		map.put("1", al);
		al = new ArrayList();
		al.add("惠山分局");
		al.add("无锡市惠山区文惠路16号");
		map.put("2", al);
		al = new ArrayList();
		al.add("滨湖分局");
		al.add("无锡市滨湖区青莲路78号");
		map.put("3", al);
		al = new ArrayList();
		al.add("新区分局");
		al.add("无锡市新区坊前路11号");
		map.put("4", al);
		al = new ArrayList();
		al.add("南长分局");
		al.add("无锡市文华路199号");
		map.put("5", al);
		al = new ArrayList();
		al.add("北塘分局");
		al.add("无锡市北塘区凤宾路民丰家园一区26号");
		map.put("6", al);
		al = new ArrayList();
		al.add("崇安分局");
		al.add("无锡市崇安区人民东路311号崇文大厦9楼");
		map.put("7", al);
		al = new ArrayList();
		al.add("梁溪分局");
		al.add("无锡市文华路199号");
		map.put("8", al);
		return map;
	}

	
	
	/**
	 * 出让文件查询方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
		// TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		// 如果从出记公告制做页面返回，则不bindObject查询条件，这样可以显示出所有的记录
		String forwardType = request.getParameter("forwardType");

		String noticeNo = "";
		if (request.getParameter("noticeNo") != null && !"".equals(request.getParameter("noticeNo"))) {
			noticeNo = request.getParameter("noticeNo");
			noticeNo = StringUtil.GBKtoISO88591(noticeNo);
		}

		String noticeName = "";
		if (request.getParameter("noticeName") != null && !"".equals(request.getParameter("noticeName"))) {
			noticeName = request.getParameter("noticeName");
			noticeName = StringUtil.GBKtoISO88591(noticeName);
		}

		// String noticeId ="";
		// if(request.getParameter("noticeId") !=
		// null&&!"".equals(request.getParameter("noticeId")))
		// noticeId = request.getParameter("noticeId");

		if (!"1".equals(request.getAttribute("forwardType"))) {
			bindObject(condition, form);
			condition.setNoticeNo(noticeNo);
			condition.setNoticeName(noticeName);
		}
		if ("1".equals(forwardType)) {
			condition.setNoticeNo("");
			condition.setNoticeName("");
		}

		if (request.getParameter("currentPage") != null)
			condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

		// 公告状态 排除状态为02的公告
		condition.setNoticeStatus("02");
		// 设置页面行数
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		if (condition.getUniteBlockName() != null && !"".equals(condition.getUniteBlockName()))
			condition.setUniteBlockName(StringUtil.GBKtoISO88591(condition.getUniteBlockName()));

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

		List noticeStatusList = new ArrayList();
		// 获得Session中登录用户的工作流权限信息列表
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);

		Map flowMap = new HashMap();
		for (int j = 0; j < nodeList.size(); j++) {
			String id_flow = (String) nodeList.get(j);
			flowMap.put(id_flow, nodeList.get(j));
		}
		// 设置查询待“制作”数据的权限
		if (flowMap.get(GlobalConstants.FLOW_ID_MAKE_FILE) != null) {
			noticeStatusList.add("00");
		}
		// 设置查询待“审核”数据的权限
		if (flowMap.get(GlobalConstants.FLOW_ID_CHECK_FILE) != null) {
			noticeStatusList.add("01");
			// 如果有“审核”权限，则查询所有数据
			condition.setUserId(null);
		}
		condition.setNoticeStatusList(noticeStatusList);
		// 公告查询列表
		PageList NoticePageList = tdscNoticeService.findPageList(condition);

		// 公告地块查询列表 lz+ 将地块nodeid和statusid取出作为 出让文件的状态
		List tdscBlockAppViewList = new ArrayList();
		List appList = null;
		if (NoticePageList != null) {
			appList = NoticePageList.getList();
		}

		if (appList != null && appList.size() > 0) {
			for (int i = 0; i < appList.size(); i++) {
				TdscNoticeApp app = (TdscNoticeApp) appList.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				// conditionBlock.setNoitceNo(app.getNoticeNo());
				conditionBlock.setNoticeId(app.getNoticeId());
				conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);

				// 所有地块信息表
				tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);
				String noticeStatusId = "";
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int j = 0; j < tdscBlockAppViewList.size(); j++) {

						TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);

						// 地块信息节点status作为出让文件status
						noticeStatusId = blockapp.getStatusId();

						app.setNoticeStatusId(noticeStatusId);
						app.setPlanId(blockapp.getPlanId());

						// 得到每个地块当前的状态，如果有任一个地块尚未编制出让文件，则点击“审核出让公告”，给出提示信息
						// 如果文件没有编制完成，则设置值为 false，使用 RecordId 字段储存；
						if (GlobalConstants.FLOW_ID_MAKE_FILE.equals(noticeStatusId)) {
							app.setRecordId("false");
							break;
						} else
							app.setRecordId("true");
					}
				}
				appList.set(i, app);
			}
		}
		// lz end
		request.setAttribute("NoticePageList", NoticePageList);

		if ("1".equals(forwardType) || "1".equals(request.getAttribute("forwardType"))) {
			condition.setNoticeNo("");
			condition.setNoticeName("");
			condition.setTradeNum("");
			condition.setTransferMode("");
			condition.setUniteBlockName("");
		} else {
			condition.setNoticeName(StringUtil.ISO88591toGBK(noticeName));
			condition.setNoticeNo(StringUtil.ISO88591toGBK(noticeNo));
			condition.setUniteBlockName(StringUtil.ISO88591toGBK(condition.getUniteBlockName()));
		}
		request.setAttribute("condition", condition);

		request.setAttribute("oldNoticeNo", "");

		return mapping.findForward("listnew");
	}

	public ActionForward queryChaYueList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeName = request.getParameter("noticeName");// 查询公告名称

		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();

		bindObject(condition, form);
		if (condition.getLandLocation() != null && !"".equals(condition.getLandLocation())) {
			condition.setLandLocation(StringUtil.GBKtoISO88591(condition.getLandLocation()));
		}
		if (condition.getUniteBlockName() != null && !"".equals(condition.getUniteBlockName())) {
			condition.setUniteBlockName(StringUtil.GBKtoISO88591(condition.getUniteBlockName()));
		}
		if (condition.getTradeNum() != null && !"".equals(condition.getTradeNum())) {
			condition.setTradeNum(StringUtil.GBKtoISO88591(condition.getTradeNum()));
		}

		// condition.setIfReleased("1");
		condition.setNoticeName(StringUtils.trimToEmpty(noticeName));

		if (!StringUtils.isEmpty(request.getParameter("currentPage")))
			condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

		// 设置页面行数
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// 查询列表
		PageList pageList = tdscNoticeService.findPageList(condition);

		if (pageList != null && pageList.getList().size() > 0) {
			// 增加页面需要的字段 from tdscBlockAppView----strat
			List list = pageList.getList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					TdscNoticeApp app = (TdscNoticeApp) list.get(i);

					TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
					conditionBlock.setNoticeId(app.getNoticeId());

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
		}

		request.setAttribute("pageList", pageList);

		if (condition.getLandLocation() != null && !"".equals(condition.getLandLocation())) {
			condition.setLandLocation(StringUtil.ISO88591toGBK(condition.getLandLocation()));
		}
		if (condition.getUniteBlockName() != null && !"".equals(condition.getUniteBlockName())) {
			condition.setUniteBlockName(StringUtil.ISO88591toGBK(condition.getUniteBlockName()));
		}
		if (condition.getTradeNum() != null && !"".equals(condition.getTradeNum())) {
			condition.setTradeNum(StringUtil.ISO88591toGBK(condition.getTradeNum()));
		}
		request.setAttribute("condition", condition);

		// 转到出让文件查阅列表页面
		return mapping.findForward("listnew_chayue");
	}

	// 已发布的出让文件 查阅
	public ActionForward fileCY(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		String recordId = "";
		// 判断TdscFileApp表中的recordId值是否为空
		if (null != noticeId) {
			TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
			tdscBlockFileApp = tdscFileService.getBlockFileAppById(noticeId);
			if (tdscBlockFileApp != null) {
				recordId = tdscBlockFileApp.getRecordId();
				if (recordId != null) {
					request.setAttribute("recordId", recordId);
				}
			}
		}
		request.setAttribute("type", "chayue");
		return mapping.findForward("listnew_notice_chayue");
	}

	// 出让文件 审核提交
	public ActionForward toBlockPaiXu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取页面参数
		TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		String noticeName = tdscNoticeForm.getNoticeName();
		String noticeNo = tdscNoticeForm.getNoticeNo();

		String createNoticeNo1 = (String) request.getParameter("createNoticeNo1");
		String createNoticeNo2 = (String) request.getParameter("createNoticeNo2");

		String planId = tdscNoticeForm.getPlanId();

		// planId 对应的地块列表
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		List list = new ArrayList();
		if (planId != null && !"".equals(planId)) {
			condition.setPlanId(planId);
			list = commonQueryService.queryTdscBlockAppViewListWithoutNodeOrderByDistrictAndName(condition);
		}

		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		if (list != null && list.size() > 0) {
			tdscBlockAppView = (TdscBlockAppView) list.get(0);
		}

		if("1".equals(tdscBlockAppView.getIsZulin())){
			if ("102".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "锡经租告字" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";
			} else if ("101".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "锡工租告字" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";
			}
		}else{
			if ("102".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "锡经告字" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";
			} else if ("101".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "锡工告字" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";
			}
		}
		

		request.setAttribute("noticeName", noticeName);
		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("createNoticeNo1", createNoticeNo1);
		request.setAttribute("createNoticeNo2", createNoticeNo2);
		request.setAttribute("planId", planId);
		request.setAttribute("tdscBlockAppViewList", list);

		return mapping.findForward("toBlockPaiXu");
	}

	/**
	 * 进入公告模版上传下载页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward entryUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取页面参数
		TdscFileForm tdscNoticeForm = (TdscFileForm) form;

		String createNoticeNoYear = "";
		String createNoticeNohao = "";

		String noticeName = tdscNoticeForm.getNoticeName();

		String createNoticeNo1 = request.getParameter("createNoticeNo1");
		String createNoticeNo2 = request.getParameter("createNoticeNo2");
		String noticeNo = tdscNoticeForm.getNoticeNo();
		String noticeId = tdscNoticeForm.getNoticeId();

		createNoticeNoYear = createNoticeNo1;
		createNoticeNohao = createNoticeNo2;

		String[] blockSelect = null;
		if (request.getParameterValues("blockSelect") != null) {
			blockSelect = request.getParameterValues("blockSelect");
		}

		// 将序号存入地块交易表
		if (blockSelect != null) {
			for (int i = 0; i < blockSelect.length; i++) {
				String blockId = blockSelect[i];
				if (blockId != null && !"".equals(blockId)) {
					TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);
					if (tdscBlockTranApp != null) {
						tdscBlockTranApp.setXuHao(String.valueOf(i + 1));
						tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
					}
				}
			}
		}

		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		// planId 对应的地块列表
		List list = new ArrayList();
		if (tdscNoticeForm.getPlanId() != null && !"".equals(tdscNoticeForm.getPlanId())) {
			condition.setPlanId(tdscNoticeForm.getPlanId());
			condition.setOrderKey("xuHao");
			list = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		}

		List appIdList = new ArrayList();
		String appId = "";
		String transferMode = "";
		String blockId = "";
		String statusId = "";
		List blockidList = new ArrayList();// 为取出所有地块的所有子地块的参数list
		List returnPartForOneList = new ArrayList();

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List blockidListForOne = new ArrayList();// 为取出单个地块子地块的参数list

				tdscBlockAppView = (TdscBlockAppView) list.get(i);
				appId = tdscBlockAppView.getAppId();
				if (StringUtils.isEmpty(noticeId)) {
					noticeId = tdscBlockAppView.getPlanId();
					request.setAttribute("planId", noticeId);
					System.out.println("noticeId===>" + noticeId);
				}
				transferMode = tdscBlockAppView.getTransferMode();
				statusId = tdscBlockAppView.getStatusId();
				blockId = tdscBlockAppView.getBlockId();

				appIdList.add(appId);
				blockidList.add(blockId);
				blockidListForOne.add(blockId);
				List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

				String blockCode = "";// 地块编号
				String tdcrnx = "";// 所有地块土地用途
				String tdcrnxAll = "";// 所有地块土地用途+出让年限
				String volumeRate = "";// 规划容积率
				String density = "";// 规划建筑密度
				String greeningRate = "";// 绿地率
				BigDecimal blockArea = new BigDecimal(0);// 地块面积

				for (int j = 0; j < partListForOne.size(); j++) {
					TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);

					// 不重复的地块编号和地块面积
					blockCode = tdscBidderAppService.getTdscBlockPartListInBlockCode(tdscBlockAppView.getBlockId());
					blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);
					// 规划容积率，规划建筑密度，绿地率
					if (tdscBlockPartForOne.getVolumeRate() != null && !"".equals(tdscBlockPartForOne.getVolumeRate()))
						volumeRate += tdscBlockPartForOne.getBlockCode() + "容积率" + tdscBlockPartForOne.getVolumeRate() + ";  ";
					if (tdscBlockPartForOne.getDensity() != null && !"".equals(tdscBlockPartForOne.getDensity()))
						density += tdscBlockPartForOne.getBlockCode() + "规划建筑密度" + tdscBlockPartForOne.getDensity() + ";  ";
					if (tdscBlockPartForOne.getGreeningRate() != null && !"".equals(tdscBlockPartForOne.getGreeningRate()))
						greeningRate += tdscBlockPartForOne.getBlockCode() + "绿地率" + tdscBlockPartForOne.getGreeningRate() + ";  ";

					// 拍卖所有地块土地用途
					tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
					tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);

					tdscBlockAppView.setTotalLandArea(blockArea);
					tdscBlockAppView.setUnitebBlockCode(blockCode);

					// 取得入市审核时新加入的字段 存在tranapp中
					blockId = tdscBlockPartForOne.getBlockId();
					TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);
					// 规划设计要点，clob字段
					TdscEsClob tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
					if (tdscEsClob != null) {
						tdscBlockAppView.setBzGhsjyd(replaceRN(tdscEsClob.getClobContent()));
					}
					tdscBlockAppView.setBzBeizhu(replaceRN(tdscBlockTranApp.getBzBeizhu()));
					tdscBlockAppView.setBzCrjkjnyq(replaceRN(tdscBlockTranApp.getBzCrjkjnyq()));
					tdscBlockAppView.setBzDgjjgsj(replaceRN(tdscBlockTranApp.getBzDgjjgsj()));
					tdscBlockAppView.setBzDkqtsm(replaceRN(tdscBlockTranApp.getBzDkqtsm()));
					tdscBlockAppView.setBzGpsxf(replaceRN(tdscBlockTranApp.getBzGpsxf()));
					tdscBlockAppView.setBzJmzgjyq(replaceRN(tdscBlockTranApp.getBzJmzgjyq()));
					tdscBlockAppView.setBzPmyj(replaceRN(tdscBlockTranApp.getBzPmyj()));
					tdscBlockAppView.setBzSzpttj(replaceRN(tdscBlockTranApp.getBzSzpttj()));
					tdscBlockAppView.setBzTdcrfj(replaceRN(tdscBlockTranApp.getBzTdcrfj()));
					tdscBlockAppView.setBzTdjftj(replaceRN(tdscBlockTranApp.getBzTdjftj()));
					tdscBlockAppView.setBzXbjg(replaceRN(tdscBlockTranApp.getBzXbjg()));
				}

				// 拍卖所有地块土地用途 存在RangeSouth中，土地出让年限存在RangeWest
				tdscBlockAppView.setRangeSouth(tdcrnx);
				tdscBlockAppView.setRangeWest(tdcrnxAll);

				tdscBlockAppView.setVolumeRate(volumeRate);
				tdscBlockAppView.setDensity(density);
				tdscBlockAppView.setGreeningRate(greeningRate);
				if (tdscBlockAppView.getMarginAmount() != null) {
					// 单位是万元
					BigDecimal marginAmount = tdscBlockAppView.getMarginAmount().multiply(new BigDecimal(10000));
					tdscBlockAppView.setMarginAmount(marginAmount);
				}

				returnPartForOneList.add(tdscBlockAppView);
			}
		}

		// 为出让文件最上面的信息表构造指定格式数据
		List returnPartList = new ArrayList();
		if (blockidList != null && blockidList.size() > 0) {
			returnPartList = tdscBlockInfoService.makeDataForFile(blockidList);
		}
		request.setAttribute("PartList", returnPartList);

		if ("3103".equals(transferMode)) {// 拍卖
			request.setAttribute("modeNameEn", "pm_crwj_mode.doc");
		}
		if ("3104".equals(transferMode)) {// 挂牌
			request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
		}

		request.setAttribute("pageList", returnPartForOneList);// 大地块信息表

		List districtList = tdscNoticeService.tidyDistrictList(list, tdscBlockAppView);
		request.setAttribute("districtList", districtList);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appIdList", appIdList);
		request.setAttribute("noticeName", noticeName);
		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("createNoticeNo", createNoticeNoYear + createNoticeNohao);
		request.setAttribute("createNoticeNoYear", createNoticeNoYear);
		request.setAttribute("createNoticeNohao", createNoticeNohao);

		String fowardpage = "";
		if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId)) {
			fowardpage = "upLoadfileshenhe";
		} else {
			fowardpage = "upLoadfilePage";
		}
		return mapping.findForward(fowardpage);
	}

	// 出让文件 审核提交
	public ActionForward addNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取页面参数
		TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		String appIds[] = tdscNoticeForm.getAppIds();
		String noticeName = tdscNoticeForm.getNoticeName();
		String noticeNo = tdscNoticeForm.getNoticeNo();
		String recordId = request.getParameter("RecordID");
		String modeNameEn = request.getParameter("modeNameEn");

		String noticeStatus = request.getParameter("noticeStatus");
		String noticeId = request.getParameter("noticeId");
		String submitType = request.getParameter("submitType");

		// 取得出让文件信息
		// TdscNoticeApp noticeApp = new TdscNoticeApp();
		// noticeApp =
		// tdscNoticeService.findNoticeAppByNoticeId(tdscNoticeForm.getNoticeId());

		List list = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// 出让方式
		String transferMode = "";
		// 获取该数据的经办人ID
		String userId = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				tdscBlockAppView = (TdscBlockAppView) list.get(0);
				transferMode = tdscBlockAppView.getTransferMode();
				if (StringUtils.isNotEmpty(tdscBlockAppView.getUserId())) {
					userId = tdscBlockAppView.getUserId();
				}
			}
		}

		// 如果是提交公告，则需要保存每条土地信息的处理意见
		if ("01".equals(noticeStatus)) {
			// 获得用户信息
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			if (user != null)
				user.setUserId(userId);
			// 审核
			tdscNoticeService.saveNoticeOpnnshenhe(appIds, user, recordId, submitType, noticeNo, noticeName, noticeStatus, noticeId, modeNameEn, transferMode);
		}

		request.setAttribute("forwardType", "1");

		return mapping.findForward("successSave");
	}

	// 出让文件 制作和修改
	public ActionForward editNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		// 传入出让文件信息
		TdscNoticeApp noticeApp = new TdscNoticeApp();
		noticeApp = tdscNoticeService.findNoticeAppByNoticeId(tdscNoticeForm.getNoticeId());
		request.setAttribute("noticeApp", noticeApp);

		String appIds[] = tdscNoticeForm.getAppIds();
		if (appIds != null && appIds.length > 0) {
			List tempAppIdsList = new ArrayList();
			for (int i = 0; i < appIds.length; i++) {
				tempAppIdsList.add(appIds[i]);
			}
			condition.setAppIdList(tempAppIdsList);
			List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

			request.setAttribute("pageList", tdscBlockAppViewList);

			String noticeNo = tdscNoticeForm.getNoticeNo();
			noticeNo = noticeNo.substring(4, 8) + noticeNo.substring(10, 13);
			request.setAttribute("createNoticeNo", noticeNo);
			request.setAttribute("condition", null);
			return mapping.findForward("editNotice");
		}

		// 通过noticeId查询出出让公告信息表一对应的一条记录TdscNoticeApp
		// 再查询地块交易信息表中noticeId和出让公告信息表noticeId相同的记录TdscBlockTranApp
		// 将以上信息返回给页面

		// 返回输入框中的信息
		String noticeId = request.getParameter("noticeId");
		TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		request.setAttribute("tdscNoticeApp", tdscNoticeApp);

		// 查询出appIds
		List tdscBlockAppViewList = new ArrayList();

		condition.setNoitceNo(tdscNoticeApp.getNoticeNo());
		condition.setNoticeId(tdscNoticeApp.getNoticeId());
		condition.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);

		tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);
		// tdscNoticeService.findTdscBlockAppViewList(condition,
		// getOldNoticeNo);

		request.setAttribute("pageList", tdscBlockAppViewList);

		String createNoticeNo = "";
		createNoticeNo = StringUtils.trimToEmpty(noticeApp.getNoticeNo());
		if (createNoticeNo != null && createNoticeNo.length() > 14)
			createNoticeNo = createNoticeNo.substring(7, 11) + createNoticeNo.substring(12, 14);

		request.setAttribute("createNoticeNo", createNoticeNo);
		request.setAttribute("condition", condition);

		return mapping.findForward("editNotice");
	}

	/**
	 * 编辑出让公告
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward bianJiCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 通过noticeId查询出出让公告信息表一对应的一条记录TdscNoticeApp
		// 再查询地块交易信息表中noticeId和出让公告信息表noticeId相同的记录TdscBlockTranApp
		// 将以上信息返回给页面

		// 返回输入框中的信息
		String noticeId = request.getParameter("noticeId");
		TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		request.setAttribute("tdscNoticeApp", tdscNoticeApp);

		// 查询出appIds
		List tdscBlockAppViewList = new ArrayList();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoitceNo(tdscNoticeApp.getNoticeNo());
		condition.setNodeId(FlowConstants.FLOW_NODE_FILE_GET);
		tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);

		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);

		// 保存暂存前的noticeNo
		request.setAttribute("oldNoticeNo", tdscNoticeApp.getNoticeNo());

		// 保存暂存前的noticeId
		request.setAttribute("oldNoticeId", tdscNoticeApp.getNoticeId());

		return mapping.findForward("bianji");
	}

	/**
	 * 删除出让文件
	 * 
	 */
	public ActionForward delCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String oldNoticeId = request.getParameter("oldNoticeId");
		List tranAppList = this.tdscNoticeService.findTdscBlockTranAppByNoticeNo(oldNoticeId, null);
		if(tranAppList!=null&&tranAppList.size()>0){
			TdscBlockTranApp tranApp= (TdscBlockTranApp)tranAppList.get(0);
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(tranApp.getPlanId());
			tdscBlockPlanTable.setBzBeizhu("CanDel");
			this.tdscScheduletableService.savePlanTableInfo(tdscBlockPlanTable);
		}

		tdscNoticeService.delCrgg(oldNoticeId);
		
		// 制做完成出让公告，forwardType=1，查询页面可以不bindObject查询条件
		request.setAttribute("forwardType", "1");

		// 保存暂存前的noticeNo
		request.setAttribute("oldNoticeNo", "");

		// 保存暂存前的noticeId
		request.setAttribute("oldNoticeId", "");

		return mapping.findForward("successSave");
	}

	/**
	 * 20080620新增：地块出让文件审核调出
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward noticeshenhe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeName = request.getParameter("noticeName");
		String noticeId = request.getParameter("noticeId");
		String type = "";
		if (request.getParameter("type") != null && !"".equals(request.getParameter("type"))) {
			type = request.getParameter("type");
		}
		// 传入出让文件信息
		// TdscNoticeApp noticeApp = new TdscNoticeApp();
		// noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		String noticeNo = request.getParameter("noticeNo");
		String createNoticeNo = "";

		// 所有地块信息表查询bynoticeNo，noticeId
		TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
		conditionBlock.setNoitceNo(noticeNo);
		conditionBlock.setNoticeId(noticeId);
		conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
		List tdscBlockAppViewList = new ArrayList();
		tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);

		List appIdList = new ArrayList();
		String appId = "";
		String planId = "";
		// String planId = "";

		// 地块循环开始------------
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				appId = blockapp.getAppId();
				planId = blockapp.getPlanId();
				appIdList.add(appId);
				// planId = blockapp.getPlanId();
				// 取出意见
				Map returnMap = new HashMap();
				if (appId != null) {
					returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
					if (returnMap != null) {
						List opnnList = (List) returnMap.get("opnnList");
						if (opnnList != null && opnnList.size() > 0) {
							for (int p = 0; p < opnnList.size(); p++) {
								TdscOpnn tdscOpnn = (TdscOpnn) opnnList.get(p);
								if ("0103".equals(tdscOpnn.getActionId())) {
									opnnList.remove(tdscOpnn);
								}
							}
						}
						returnMap.put("opnnList", opnnList);
					}
				}
				request.setAttribute("opnninfo", returnMap);
			}
		}

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("noticeName", noticeName);
		request.setAttribute("appIdList", appIdList);
		request.setAttribute("pageList", tdscBlockAppViewList);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("createNoticeNo", createNoticeNo);
		request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
		request.setAttribute("planId", planId);
		if (noticeNo != null && noticeNo.length() > 7)
			createNoticeNo = noticeNo.substring(7, 11) + noticeNo.substring(12, 14);

		String recordId = "";
		// 判断TdscNoticeApp表中的recordId值是否为空
		if (null != noticeId) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				recordId = tdscNoticeApp.getRecordId();
				if (recordId != null) {
					request.setAttribute("recordId", recordId);
				}
			}
		}

		request.setAttribute("type", type);
		if (type != null && "chayue".equals(type))
			// 转到出让文件查阅页面
			return mapping.findForward("listnew_notice_chayue");
		else
			// 转到出让文件审核页面
			return mapping.findForward("noticeshenhe");
	}

	/**
	 * 审核出让文件及公告 2010-06-04 新增 by SMW
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward noticeShenHe_100604(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		// 查询该公告中的所有地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		if (StringUtils.isNotEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			condition.setOrderKey("blockNoticeNo");
			// 根据noticeId查询
			List blockViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("blockViewList", blockViewList);

			// 传入出让文件及出让公告信息
			TdscNoticeApp noticeApp = (TdscNoticeApp) tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (StringUtils.isNotEmpty(noticeId)) {
				List blockTranList = this.tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, null);
				request.setAttribute("blockTranList", blockTranList);
			}
			TdscBlockFileApp fileApp = (TdscBlockFileApp) tdscFileService.getBlockFileAppById(noticeId);

			request.setAttribute("noticeApp", noticeApp);
			request.setAttribute("fileApp", fileApp);

			// 地块循环开始，获取操作意见
			String appId = "";
			if (blockViewList != null && blockViewList.size() > 0) {
				TdscBlockAppView blockapp = (TdscBlockAppView) blockViewList.get(0);
				request.setAttribute("planId", blockapp.getPlanId());
				request.setAttribute("statusId", blockapp.getStatusId());
				appId = blockapp.getAppId();
				// 取出意见
				Map returnMap = new HashMap();
				if (appId != null) {
					returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);

					if (returnMap != null) {
						List tempList = (List) returnMap.get("opnnList");

						if (tempList != null && tempList.size() > 0) {
							for (int k = 0; k < tempList.size(); k++) {
								TdscOpnn tdscOpnn = (TdscOpnn) tempList.get(k);
								if ("0103".equals(tdscOpnn.getActionId())) {
									tempList.remove(tdscOpnn);
								}
							}
						}
						returnMap.put("opnnList", tempList);

					}
				}
				request.setAttribute("opnninfo", returnMap);
			}
		}

		return mapping.findForward("noticeShenHe_100604");
	}

	/**
	 * 审核出让文件环节，查看出让文件和出让公告信息 查询出让文件或者出让公告的recordId
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showWordFileById(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		// 查询文件类型：1为出让文件；2为出让公告
		String type = request.getParameter("type");
		String recordId = "";
		// 判断TdscNoticeApp表中的recordId值是否为空
		if (StringUtils.isNotEmpty(noticeId)) {
			if ("1".equals(type)) {
				// 出让文件
				TdscNoticeApp tdscNoticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(noticeId);
				if (tdscNoticeApp != null) {
					recordId = tdscNoticeApp.getRecordId();
					if (recordId != null) {
						request.setAttribute("recordId", recordId);
					}
				}
			} else {
				// 出让公告
				TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
				TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
				List list = tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, null);
				if (list != null && list.size() > 0) {
					tdscBlockTranApp = (TdscBlockTranApp) list.get(0);
				}
				tdscBlockFileApp = tdscFileService.getBlockFileAppById(tdscBlockTranApp.getPlanId());
				if (tdscBlockFileApp != null) {
					recordId = tdscBlockFileApp.getRecordId();
					if (recordId != null) {
						request.setAttribute("recordId", recordId);
					}
				}
			}
		}

		request.setAttribute("type", type);
		return mapping.findForward("showWordFile");
	}

	/**
	 * 出让文件的制作，审核与修改
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPreviewInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String nodeId = request.getParameter("nodeId");
		String statusId = request.getParameter("statusId");
		String recordId = "";

		if (appId != null) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 查询交易信息表(保证金截止时间)
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.findBlockTranAppInfo(appId);
			// 查询进度安排表
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableInfo(appId);

			request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);

			String blockType = "";
			blockType = commonInfo.getBlockType();
			String transferMode = "";
			transferMode = commonInfo.getTransferMode();
			// 招标
			if ("3107".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地招标模版.doc");
					request.setAttribute("modeNameEn", "gyxydzb.doc");
				} else {// 经营性用地
					request.setAttribute("modeName", "经营性用地招标模版.doc");
					request.setAttribute("modeNameEn", "jyxydzb.doc");
				}
			}

			// 拍卖
			if ("3103".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地拍卖模版.doc");
					request.setAttribute("modeNameEn", "2008gyxydpm.doc");
				} else {// 经营性用地
					request.setAttribute("modeName", "经营性用地拍卖模版.doc");
					request.setAttribute("modeNameEn", "2008jyxydpm.doc");
				}
			}

			// 挂牌
			if ("3104".equals(transferMode)) {
				// 工业性用地
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "工业性用地挂牌模版.doc");
					request.setAttribute("modeNameEn", "2008gyxydgp.doc");
				} else {// 经营性用地
					request.setAttribute("modeName", "经营性用地挂牌模版.doc");
					request.setAttribute("modeNameEn", "2008jyxydgp.doc");
				}
			}

			// 如果类型为制做出让公告
			if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {

			}// 如果类型为暂存出让公告,审核出让公告
			else {
				String fileName = tdscFileService.getFileName(appId);
				String fileUrl = appId + ".doc";
				request.setAttribute("fileName", fileName);
				request.setAttribute("fileUrl", fileUrl);
			}

		}
		// 如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
		TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
		if (temp != null) {
			request.setAttribute("ifZancun", "1");
			request.setAttribute("fileName", temp.getFileUrl());
			request.setAttribute("oldAppId", appId);
			request.setAttribute("recordId", temp.getRecordId());
		}

		Map returnMap = new HashMap();
		if (appId != null) {
			returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
		}
		request.setAttribute("opnninfo", returnMap);
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("appId", appId);
		request.setAttribute("statusId", statusId);
		// 待审核出让文件
		if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId))
			return mapping.findForward("shenhe");
		// 退回重制出让文件
		else if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
			return mapping.findForward("tuihuixiugai");
			// 待制作出让文件
		} else if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
			if (temp != null)
				return mapping.findForward("xiugai");
			else {
				// 制作
				request.setAttribute("recordId", recordId);
				return mapping.findForward("previewFileInfo");
			}
		}
		return null;
	}

	/**
	 * 出让文件的制作，审核与修改-2009.5
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPreviewInfoNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// // 获得页面参数appId
		// String nodeId = request.getParameter("nodeId");
		// String statusId = request.getParameter("statusId");
		// String noticeNo = request.getParameter("noticeNo");
		// String noticeId = request.getParameter("noticeId");
		// String modeNameEn = request.getParameter("modeNameEn");
		// String recordId = "";
		// String appId = "";
		//
		// //取得出让文件信息
		// //TdscNoticeApp noticeApp = new TdscNoticeApp();
		// //noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		//
		// TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		// String appIds[] = tdscNoticeForm.getAppIds();
		// String noticeName = tdscNoticeForm.getNoticeName();
		// String noticeStatus = request.getParameter("noticeStatus");
		//
		//
		// // 保存前需要通过noticeId删除原有的记录
		// //tdscNoticeService.saveNotice(appIds, noticeNo, noticeName,
		// noticeStatus, noticeId, modeNameEn, recordId);
		//
		//
		// if (appId != null) {
		// // 获得查询条件
		// TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		// baseCondition.setAppId(appId);
		// // 调用公共方法取得公共信息
		// TdscBlockAppView commonInfo =
		// commonQueryService.getTdscBlockAppView(baseCondition);
		// if (commonInfo != null) {
		// request.setAttribute("commonInfo", commonInfo);
		// }
		// //查询交易信息表(保证金截止时间)
		// TdscBlockTranApp tdscBlockTranApp =
		// (TdscBlockTranApp)tdscScheduletableService.findBlockTranAppInfo(appId);
		// //查询进度安排表
		// TdscBlockPlanTable tdscBlockPlanTable =
		// (TdscBlockPlanTable)tdscScheduletableService.findPlanTableInfo(appId);
		//
		//
		// request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		// request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		//
		// //String blockType = commonInfo.getBlockType();
		// String transferMode = "";
		// transferMode = commonInfo.getTransferMode();
		//
		// // 拍卖
		// if ("3103".equals(transferMode)) {
		// request.setAttribute("modeName", "拍卖模版.doc");
		// request.setAttribute("modeNameEn", "pm_crwj_mode.doc");
		// }
		//
		// // 挂牌
		// if ("3104".equals(transferMode)) {
		// request.setAttribute("modeName", "挂牌模版.doc");
		// request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
		// }
		//
		// // 如果类型为制做出让公告
		// if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
		//
		// }// 如果类型为暂存出让公告,审核出让公告
		// else {
		// String fileName = tdscFileService.getFileName(appId);
		// String fileUrl = appId + ".doc";
		// request.setAttribute("fileName", fileName);
		// request.setAttribute("fileUrl", fileUrl);
		// }
		//
		// }
		// // 如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
		// TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
		// if (temp != null) {
		// request.setAttribute("ifZancun", "1");
		// request.setAttribute("fileName", temp.getFileUrl());
		// request.setAttribute("oldAppId", appId);
		// request.setAttribute("recordId",temp.getRecordId());
		// }
		//
		// Map returnMap = new HashMap();
		// if (appId != null) {
		// returnMap = this.appFlowService.queryOpnnInfo(appId,
		// FlowConstants.FLOW_NODE_FILE_MAKE);
		// }
		// request.setAttribute("opnninfo", returnMap);
		// request.setAttribute("nodeId", nodeId);
		// request.setAttribute("appId", appId);
		// request.setAttribute("statusId", statusId);
		// //待审核出让文件
		// if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId))
		// return mapping.findForward("shenhe");
		// //退回重制出让文件
		// else if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
		// return mapping.findForward("tuihuixiugai");
		// //待制作出让文件
		// } else if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
		// if (temp != null)
		// return mapping.findForward("xiugai");
		// else{
		// //制作
		// request.setAttribute("recordId", recordId);
		// return mapping.findForward("previewFileInfo");
		// }
		// }
		return null;
	}

	/**
	 * 查询土地列表
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
	public ActionForward queryAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return mapping.findForward("list");
	}

	/**
	 * 上传文件，同时写入出让文件信息表
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
	// public ActionForward upLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	//
	// String statusId = request.getParameter("statusId");
	// String appId = request.getParameter("appId");
	// String modeNameEn = request.getParameter("modeNameEn");
	// String recordId = request.getParameter("RecordID");
	// // 获得用户信息
	// SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
	//
	// TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
	// // 取得系统当前时间
	// Date date = new Date();
	//
	// // 如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
	// TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
	// if (temp != null && !FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId) && !"2".equals(request.getParameter("submitType"))) {
	// temp.setFilePerson(String.valueOf(user.getUserId()));
	// temp.setFileUrl(modeNameEn);
	// request.setAttribute("ifZancun", "1");
	// temp.setFileDate(date);
	// temp.setFileId(appId);
	// temp.setRecordId(recordId);
	// tdscFileService.update(temp);
	// request.setAttribute("fileName", modeNameEn);
	// request.setAttribute("oldAppId", appId);
	// request.setAttribute("recordId", temp.getRecordId());
	// } else {
	// tdscBlockFileApp.setFilePerson(String.valueOf(user.getUserId()));
	// tdscBlockFileApp.setFileDate(date);
	// tdscBlockFileApp.setFileUrl(modeNameEn);
	// tdscBlockFileApp.setFileId(appId);
	// tdscBlockFileApp.setRecordId(recordId);
	// request.setAttribute("recordId", recordId);
	//
	// // 保存文件上传信息TDSC_BLOCK_FILE_APP
	// if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
	// tdscFileService.update(tdscBlockFileApp);
	// } else if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
	// if (temp != null)
	// tdscFileService.update(tdscBlockFileApp);
	// else
	// tdscFileService.save(tdscBlockFileApp);
	// request.setAttribute("fileName", modeNameEn);
	// request.setAttribute("oldAppId", appId);
	// }
	// }
	//
	//
	//
	// // 获得查询条件
	// TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
	// baseCondition.setAppId(appId);
	// // 调用公共方法取得公共信息
	// TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
	// request.setAttribute("commonInfo", commonInfo);
	// String blockType = "";
	// blockType = commonInfo.getBlockType();
	// String transferMode = "";
	// transferMode = commonInfo.getTransferMode();
	// // 招标
	// if ("3107".equals(transferMode)) {
	// // 工业性用地
	// if ("101".equals(blockType)) {
	// request.setAttribute("modeName", "工业性用地招标模版.doc");
	// request.setAttribute("modeNameEn", "gyxydzb.doc");
	// } else {// 经营性用地
	// request.setAttribute("modeName", "经营性用地招标模版.doc");
	// request.setAttribute("modeNameEn", "jyxydzb.doc");
	// }
	// }
	//
	// // 拍卖
	// if ("3103".equals(transferMode)) {
	// // 工业性用地
	// if ("101".equals(blockType)) {
	// request.setAttribute("modeName", "工业性用地拍卖模版.doc");
	// request.setAttribute("modeNameEn", "gyxydpm.doc");
	// } else {// 经营性用地
	// request.setAttribute("modeName", "经营性用地拍卖模版.doc");
	// request.setAttribute("modeNameEn", "jyxydpm.doc");
	// }
	// }
	//
	// // 挂牌
	// if ("3104".equals(transferMode)) {
	// // 工业性用地
	// if ("101".equals(blockType)) {
	// request.setAttribute("modeName", "工业性用地挂牌模版.doc");
	// request.setAttribute("modeNameEn", "gyxydgp.doc");
	// } else {// 经营性用地
	// request.setAttribute("modeName", "经营性用地挂牌模版.doc");
	// request.setAttribute("modeNameEn", "jyxydgp.doc");
	// }
	// }
	//
	// request.setAttribute("currentPage", "0");
	// if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId) || FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
	// try {
	// if ("1".equals(request.getParameter("submitType"))) {
	// this.appFlowService.tempSaveOpnn(appId, transferMode, user);
	// } else if ("2".equals(request.getParameter("submitType"))) {
	// this.appFlowService.saveOpnn(appId, transferMode, user);
	// }
	// request.setAttribute("saveMessage", "保存成功");
	// } catch (Exception e) {
	// e.printStackTrace();
	// request.setAttribute("saveMessage", "保存失败");
	// }
	// }
	//
	// if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId)) {
	// TdscAppFlow appFlow = new TdscAppFlow();
	// appFlow.setAppId(appId);
	// appFlow.setResultId(request.getParameter("resultId"));
	// appFlow.setTextOpen(request.getParameter("textOpen"));
	// appFlow.setTransferMode(transferMode);
	// appFlow.setUser(user);
	// try {
	// if ("1".equals(request.getParameter("submitType"))) {
	// this.appFlowService.tempSaveOpnn(appFlow);
	// // 查询历史意见
	// Map opnnMap = appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
	// request.setAttribute("opnninfo", opnnMap);
	// } else if ("2".equals(request.getParameter("submitType"))) {
	// this.appFlowService.saveOpnn(appFlow);
	// }
	// request.setAttribute("oldAppId", appId);
	// request.setAttribute("saveMessage", "保存成功");
	// } catch (Exception e) {
	// e.printStackTrace();
	// request.setAttribute("saveMessage", "保存失败");
	// }
	// request.setAttribute("fileName", temp.getFileUrl());
	// if ("1".equals(request.getParameter("submitType")))
	// return mapping.findForward("shenhe");
	// }
	// // 返回到列表页面，１暂存，２为提交
	// if ("1".equals(request.getParameter("submitType")) && FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
	// Map opnnMap = appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
	// request.setAttribute("opnninfo", opnnMap);
	// return mapping.findForward("tuihuixiugai");
	// } else if ("1".equals(request.getParameter("submitType")))
	// return mapping.findForward("xiugai");
	// else
	// return new ActionForward("file.do?method=queryAppFileListWithNodeId", true);
	// }

	public ActionForward upLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// String statusId = request.getParameter("statusId");
		String appId = request.getParameter("appId") + "";
		String modeNameEn = request.getParameter("modeNameEn");
		String recordId = request.getParameter("RecordID");

		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 取得系统当前时间
		Date date = new Date();

		// 如果通过appId可以在TdscBlockFileApp表中查到记录，则说明该出让文件已经暂存过
		TdscBlockFileApp tdscBlockFileOld = null;
		tdscBlockFileOld = tdscFileService.getBlockFileAppById(appId);

		if (tdscBlockFileOld != null) {
			tdscBlockFileOld.setFilePerson(user.getUserId());
			tdscBlockFileOld.setFileDate(new Date(System.currentTimeMillis()));
			// 说明已经保存过，update下
			tdscFileService.update(tdscBlockFileOld);
		} else {
			TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
			tdscBlockFileApp.setRecordId(recordId);
			tdscBlockFileApp.setFileId(appId);
			tdscBlockFileApp.setFileUrl(modeNameEn);// 将文件类型 保存在附件地址中
			tdscBlockFileApp.setFilePerson(user.getUserId());
			tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
			// 保存出让文件信息 TDSC_BLOCK_FILE_APP
			tdscFileService.save(tdscBlockFileApp);
		}

		// tdscBlockFileApp.setFilePerson(String.valueOf(user.getUserId()));
		// tdscBlockFileApp.setFileDate(date);
		// tdscBlockFileApp.setFileUrl(modeNameEn);
		// tdscBlockFileApp.setFileId(appId);
		// tdscBlockFileApp.setRecordId(recordId);
		request.setAttribute("recordId", recordId);

		// tdscFileService.save(tdscBlockFileApp);

		request.setAttribute("saveMessage", "保存成功");

		return new ActionForward("file.do?method=queryAppFileListWithNodeId", true);
	}

	/**
	 * 上传文件，同时写入出让文件信息表
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
	public ActionForward upLoadFileNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscFileForm tdscNoticeForm = (TdscFileForm) form;

		String appIds[] = tdscNoticeForm.getAppIds();
		String noticeId = request.getParameter("noticeId");
		String getmodeNameEn = request.getParameter("fileName");
		String noticeNo = request.getParameter("noticeNo");
		String noticeName = request.getParameter("noticeName");
		// String statusId = request.getParameter("statusId");
		String modeNameEn = request.getParameter("modeNameEn");
		String recordId = request.getParameter("RecordID");
		String submitType = request.getParameter("submitType");// 11是制作保存，12是制作提交，21是审核保存，22是审核提交，23是审核回退
		String textOpen = request.getParameter("textOpen");// 回退意见

		request.setAttribute("fileName", modeNameEn);
		request.setAttribute("recordId", recordId);

		String appId = "";
		String transferMode = "";
		String planId = request.getParameter("planId");
		planId = StringUtils.trimToEmpty(planId);
		String landLocation = "";

		// TdscNoticeApp noticeApp = new TdscNoticeApp();
		// if(noticeId!=null&&!"".equals(noticeId))
		// noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);

		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 所有地块信息表
		List tdscBlockAppViewList = new ArrayList();
		// 设置经办人ID
		String userId = "";
		if (noticeNo != null && !"".equals(noticeNo) && noticeId != null && !"".equals(noticeId)) {
			TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
			conditionBlock.setPlanId(planId);
			conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);
		} else {
			tdscBlockAppViewList = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
		}
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				if (StringUtils.isNotEmpty(blockapp.getUserId())) {
					userId = blockapp.getUserId();
				}
				// 地块信息节点status作为出让文件status
				// statusId = blockapp.getStatusId();
				appId = blockapp.getAppId();
				transferMode = blockapp.getTransferMode();
				planId = blockapp.getPlanId();
				if (blockapp.getLandLocation() != null && !"".equals(blockapp.getLandLocation()))
					landLocation += blockapp.getLandLocation() + ",";

				// 拍卖
				if ("3103".equals(transferMode)) {
					request.setAttribute("modeName", "青岛土地拍卖模版.doc");
					request.setAttribute("modeNameEn", "pm_crwj_mode.doc");
				}
				// 挂牌
				if ("3104".equals(transferMode)) {
					request.setAttribute("modeName", "青岛土地挂牌模版.doc");
					request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
				}

				// 查询历史意见
				Map opnnMap = appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);

				request.setAttribute("commonInfo", blockapp);
				request.setAttribute("opnninfo", opnnMap);
				request.setAttribute("oldAppId", appId);
			}
		}

		if (landLocation != null && !"".equals(landLocation))
			landLocation = landLocation.substring(0, landLocation.length() - 1);

		try {
			TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
			if (planId != null && !"".equals(planId)){
				tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);
			}
			// tdscFileService.saveAndUpdate(noticeId, user.getUserId(),
			// recordId, getmodeNameEn, null);
			tdscNoticeService.saveNoticeLz(userId, appIds, noticeNo, noticeName, noticeId, planId, modeNameEn, recordId, user, submitType, textOpen, tdscBlockPlanTable,
					landLocation);
			request.setAttribute("saveMessage", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("saveMessage", "保存失败");
		}

		request.setAttribute("currentPage", "0");
		request.setAttribute("forwardType", "1");

		return mapping.findForward("gobackList");
	}

	/**
	 * 用地模版详细信息 用于模版下载
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modelDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String blockType = "";
		String transferMode = "";

		if (appId != null) {
			// 获得查询条件
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(condition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
				blockType = commonInfo.getBlockType();
				transferMode = commonInfo.getTransferMode();
			}
		}
		// 招标
		if ("3107".equals(transferMode)) {
			// 工业性用地
			if ("101".equals(blockType)) {
				return mapping.findForward("gyZB");
			} else {// 经营性用地
				return mapping.findForward("jyxZB");
			}
		}

		// 拍卖
		if ("3103".equals(transferMode)) {
			// 工业性用地
			if ("101".equals(blockType)) {
				return mapping.findForward("gyPM");
			} else {// 经营性用地
				return mapping.findForward("jyxPM");
			}
		}

		// 挂牌
		if ("3104".equals(transferMode)) {
			// 工业性用地
			if ("101".equals(blockType)) {
				return mapping.findForward("gyGP");
			} else {// 经营性用地
				return mapping.findForward("jyxGP");
			}
		}
		return null;
	}

	/**
	 * 打印公告信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// noticeId ，实质为planId，在将“制作公告”提前至与“制作出让文件”一起时修改（2010-5-3 smw）。
		String noticeId = (String) request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		String printType = (String) request.getParameter("printType");
		String type = (String) request.getParameter("type");
		String planId = request.getParameter("planId");
		request.setAttribute("noticeName", request.getParameter("noticeName"));

		String createNoticeNo1 = request.getParameter("createNoticeNo1");
		String createNoticeNo2 = request.getParameter("createNoticeNo2");
		String getNoticeNo = request.getParameter("getnoticeNo");
		request.setAttribute("createNoticeNo1", createNoticeNo1);
		request.setAttribute("createNoticeNo2", createNoticeNo2);
		TdscBlockPlanTable blockPlanTable = this.tdscScheduletableService.findPlanTableByPlanId(planId);
		request.setAttribute("blockPlanTable", blockPlanTable);
		
		String marginStartDate ="";
		String marginEndDate ="";
		if(blockPlanTable.getAccAppStatDate()!=null){
			marginStartDate = DateUtil.date2String(blockPlanTable.getAccAppStatDate(),"yyyy年M月d日 HH时mm分");
		}
		String strBlockQuality = "";

		String noticeNo = "";
		// 取得出让文件信息
		TdscNoticeApp noticeApp = new TdscNoticeApp();

		noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		if (noticeApp != null && !"".equals(noticeApp.getNoticeNo())) {
			noticeNo = noticeApp.getNoticeNo();
		} else {
			// TdscFileForm tdscFileForm = (TdscFileForm)form;
			String noticeNum = request.getParameter("noticeNum");
			if (StringUtils.isNotEmpty(noticeNum))
				noticeNo = "青土资房告字[" + noticeNum + "号";
			else
				noticeNo = request.getParameter("noticeNo");
		}

		request.setAttribute("noticeNo", noticeNo);

		// 默认出让方式为拍卖
		String transferMode = GlobalConstants.DIC_TRANSFER_AUCTION;

		String strNoPurContext = "";

		if (noticeId != null && !"".equals(noticeId)) {
			// 根据noticeId查询blockIdList(noticeId ，实质为planId)
			// planId =
			// StringUtils.isEmpty(planId)?noticeId:StringUtils.trimToEmpty(planId);
			// List blockIdList =
			// (List)tdscScheduletableService.queryBlockIdListByPlanId(planId);

			String[] blockSelect = null;
			List blockIdList = new ArrayList();
			if (request.getParameterValues("blockSelect") != null) {
				blockSelect = request.getParameterValues("blockSelect");
			}

			if (blockSelect == null) {
				// blockIdList =
				// (List)tdscScheduletableService.queryBlockIdListByPlanId(planId);
				blockIdList = (List) tdscScheduletableService.queryBlockIdListByNoticeId(noticeId);
			} else {
				blockIdList = Arrays.asList(blockSelect);
			}

			// 为出让文件最上面的信息表构造指定格式数据
			List tdscBlockPartList = new ArrayList();
			if (blockIdList != null && blockIdList.size() > 0) {
				tdscBlockPartList = tdscBlockInfoService.makeDataForFile(blockIdList);
			}
			request.setAttribute("tdscBlockPartList", tdscBlockPartList);

			// 整理竞买资格要求
			TdscBaseQueryCondition viewcondition = new TdscBaseQueryCondition();
			List blockTranAppList = new ArrayList();
			List appIdsList = new ArrayList();
			String blockQuality = "";
			String appId = "";
			for (int i = 0; i < blockIdList.size(); i++) {
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp((String) blockIdList.get(i));
				if("4".equals(tdscBlockInfo.getDistrictId().toString())){//无锡新区
					request.setAttribute("regCapital", StringUtils.trimToEmpty(tdscBlockInfo.getRegCapital()));//新区工业地块企业注册资本
				}
				strBlockQuality = tdscBlockInfo.getBlockQuality();
				if ("101".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "工";
				} else if ("102".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "经";
				}
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo((String) blockIdList.get(i));
				if (tdscBlockInfo != null && tdscBlockTranApp != null) {
					tdscBlockTranApp.setXuHao(String.valueOf(i + 1));
					tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
					// 将地块名称 暂时设置在 SpecialPromise 字段里
					tdscBlockTranApp.setSpecialPromise(tdscBlockInfo.getBlockName() + "");
					transferMode = tdscBlockTranApp.getTransferMode();
					appId = tdscBlockTranApp.getAppId();
					appIdsList.add(appId);
				}
				blockTranAppList.add(tdscBlockTranApp);
			}

			if (appIdsList != null && appIdsList.size() > 0) {

				request.setAttribute("appIdList", appIdsList);
				// lz+ 所有part地块信息
				viewcondition.setAppIdList(appIdsList);
				// viewcondition.setOrderKey("blockNoticeNo");
				viewcondition.setOrderKey("xuHao");

				List list = commonQueryService.queryTdscBlockAppViewListWithoutNode(viewcondition);

				String blockId = "";
				List returnPartForOneList = new ArrayList();
				TdscBlockAppView BlockAppView = new TdscBlockAppView();

				// String max = this.tdscBlockInfoService.getMaxNoticeNo();
				String tempStr = noticeNo.substring(1, 2);
				String blockNoticeNoPrefix = "";
				String max ="";
				if(noticeNo.indexOf("租") != -1){
					if ("经".equals(tempStr)) {
						blockNoticeNoPrefix = "锡国土经（租）" + noticeNo.substring(6, 10) + "-";
					} else if ("工".equals(tempStr)) {
						blockNoticeNoPrefix = "锡国土工（租）" + noticeNo.substring(6, 10) + "-";
					}
					max = this.tdscBlockInfoService.getMaxNoticeNoBlockNoticeNoPrefix("20",blockNoticeNoPrefix);
				}else{
					if ("经".equals(tempStr)) {
						blockNoticeNoPrefix = "锡国土（经）" + noticeNo.substring(5, 9) + "-";
					} else if ("工".equals(tempStr)) {
						blockNoticeNoPrefix = "锡国土（工）" + noticeNo.substring(5, 9) + "-";
					}
					max = this.tdscBlockInfoService.getMaxNoticeNoBlockNoticeNoPrefix("18",blockNoticeNoPrefix);
				}
				

				
				int maxValue = Integer.parseInt(max);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						List blockidListForOne = new ArrayList();// 为取出单个地块子地块的参数list
						BlockAppView = (TdscBlockAppView) list.get(i);
						
						if(BlockAppView.getMarginEndDate()!=null){
							marginEndDate =DateUtil.timestamp2String( BlockAppView.getMarginEndDate(),"yyyy年M月d日 HH时mm分");
						}
						
						blockId = BlockAppView.getBlockId();
						TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);
						blockidListForOne.add(blockId);
						List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

						String blockCode = "";
						String tdcrnx = "";
						String tdcrnxAll = "";
						BigDecimal blockArea = new BigDecimal(0.00);
						StringBuffer landUseTypes = new StringBuffer();
						StringBuffer transferLifes = new StringBuffer();
						StringBuffer volumeRates = new StringBuffer();
						StringBuffer densitys = new StringBuffer();
						StringBuffer greeningRates = new StringBuffer();

						for (int j = 0; j < partListForOne.size(); j++) {
							TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);
							landUseTypes.append(tdscBlockPartForOne.getLandUseType());
							transferLifes.append(tdscBlockPartForOne.getTransferLife());
							
							//如果容积率是介于，则拼接成 a＜容积率＜b，否则拼接成符号+a
//							if(tdscBlockPartForOne.getVolumeRateSign() != null && "06".equals(tdscBlockPartForOne.getVolumeRateSign())){
//								volumeRates.append(tdscBlockPartForOne.getVolumeRate()).append("＜").append("容积率").append("＜").append(tdscBlockPartForOne.getVolumeRate2()).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRateMemo()));
//							}else if(tdscBlockPartForOne.getVolumeRateSign() != null){
//								volumeRates.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,tdscBlockPartForOne.getVolumeRateSign())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRate())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRateMemo()));
//							}else{
//								volumeRates.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRate())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRateMemo()));
//							}
							volumeRates.append(tdscBlockPartForOne.getVolumeRateMemo());
							
							//如果规划建筑密度是介于，则拼接成 a%＜建筑密度＜b%，否则拼接成符号+a%
//							if(tdscBlockPartForOne.getDensitySign() != null && "06".equals(tdscBlockPartForOne.getDensitySign())){
//								densitys.append(tdscBlockPartForOne.getDensity()).append("%＜").append("建筑密度").append("＜").append(tdscBlockPartForOne.getDensity2()).append("%");
//							}else if(tdscBlockPartForOne.getDensitySign() != null){
//								densitys.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,tdscBlockPartForOne.getDensitySign())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getDensity())).append("%");
//							}else{
//								densitys.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getDensity())).append("%");
//							}
							densitys.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getDensity()));
							//如果容积率是介于，则拼接成 a%＜容积率＜b%，否则拼接成符号+a%
//							if(tdscBlockPartForOne.getGreeningRateSign() != null && "06".equals(tdscBlockPartForOne.getGreeningRateSign())){
//								greeningRates.append(tdscBlockPartForOne.getGreeningRate()).append("%＜").append("绿地率").append("＜").append(tdscBlockPartForOne.getGreeningRate2()).append("%");
//							}else if(tdscBlockPartForOne.getGreeningRateSign() != null){
//								greeningRates.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,tdscBlockPartForOne.getGreeningRateSign())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getGreeningRate())).append("%");
//							}else{
//								greeningRates.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getGreeningRate())).append("%");
//							}
							greeningRates.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getGreeningRate()));

							if (j + 1 != partListForOne.size()) {
								landUseTypes.append(",");
								transferLifes.append(",");
								volumeRates.append(",");
								densitys.append(",");
								greeningRates.append(",");
							}
							if (j == 0) {
								blockCode = tdscBlockPartForOne.getBlockCode();
							}
							if (j > 0) {
								blockCode += "、" + tdscBlockPartForOne.getBlockCode();
							}
							blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);

							// 拍卖所有地块土地用途
							tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
							tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);

							BlockAppView.setTotalLandArea(blockArea);
							BlockAppView.setUnitebBlockCode(blockCode);

							// 取得入市审核时新加入的字段 存在tranapp中
							blockId = tdscBlockPartForOne.getBlockId();
							TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);

							BlockAppView.setBzBeizhu(tdscBlockTranApp.getBzBeizhu());
							BlockAppView.setBzCrjkjnyq(tdscBlockTranApp.getBzCrjkjnyq());
							BlockAppView.setBzDgjjgsj(tdscBlockTranApp.getBzDgjjgsj());
							BlockAppView.setBzDkqtsm(tdscBlockTranApp.getBzDkqtsm());
							BlockAppView.setBzGhsjyd(tdscBlockTranApp.getBzGhsjyd());
							BlockAppView.setBzGpsxf(tdscBlockTranApp.getBzGpsxf());
							BlockAppView.setBzJmzgjyq(tdscBlockTranApp.getBzJmzgjyq());
							BlockAppView.setBzPmyj(tdscBlockTranApp.getBzPmyj());
							BlockAppView.setBzSzpttj(tdscBlockTranApp.getBzSzpttj());
							BlockAppView.setBzTdcrfj(tdscBlockTranApp.getBzTdcrfj());
							BlockAppView.setBzTdjftj(tdscBlockTranApp.getBzTdjftj());
							BlockAppView.setBzXbjg(tdscBlockTranApp.getBzXbjg());
							BlockAppView.setIsPurposeBlock(tdscBlockTranApp.getIsPurposeBlock());
							// BlockAppView.setBzXbjg(replaceRN(tdscBlockTranApp.getBzXbjg()));
						}

						max = String.valueOf((maxValue + 1));
						String blockNoticeNo = blockNoticeNoPrefix + max;
						maxValue++;

						if ("0".equals(BlockAppView.getIsPurposeBlock())) {
							strNoPurContext += blockNoticeNo + "、";
						}

						BlockAppView.setBlockNoticeNo(blockNoticeNo);
						BlockAppView.setLandUseTypes(landUseTypes.toString());
						BlockAppView.setTransferLifes(transferLifes.toString());
						BlockAppView.setVolumeRates(volumeRates.toString());
						BlockAppView.setDensitys(densitys.toString());
						BlockAppView.setGreeningRates(greeningRates.toString());
						BlockAppView.setBuildingHeight(tdscBlockInfo.getBuildingHeight());
						BlockAppView.setSellYear(tdscBlockInfo.getSellYear());
						BlockAppView.setOpeningMeetingNo(tdscBlockInfo.getBlockInvestAmount());//投资强度
						// 拍卖所有地块土地用途 存在RangeSouth中，土地出让年限存在RangeWest
						BlockAppView.setRangeSouth(tdcrnx);
						BlockAppView.setRangeWest(tdcrnxAll);
						returnPartForOneList.add(BlockAppView);
						request.setAttribute("tdscBlockAppView", BlockAppView);
					}
				}
				request.setAttribute("pageList", returnPartForOneList);// 大地块信息表
				// lz end
			}
			request.setAttribute("marginStartDate", marginStartDate);
			request.setAttribute("marginEndDate", marginEndDate);
			if ("true".equals(getNoticeNo)) {
				if(noticeNo.indexOf("租") != -1){
					getNoticeNo = "锡国土" + blockQuality + "租字[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";
				}else{
					getNoticeNo = "锡国土" + blockQuality + "租字[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";
				}
				request.setAttribute("noticeNo", getNoticeNo);
			}

			if (!"".equals(appId)) {
				// 查询视图，获得工作方案
				TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
				condition.setAppId(appId);
				// TdscBlockAppView tdscBlockAppView =
				// (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);

			}
			request.setAttribute("blockTranAppList", blockTranAppList);
		}

		request.setAttribute("transferMode", transferMode);

		if (!StringUtil.isEmpty(strNoPurContext)) {
			strNoPurContext = strNoPurContext.substring(0, strNoPurContext.length() - 1);
			strNoPurContext += "地块为无意向公开挂牌出让，";

			request.setAttribute("noPurContext", strNoPurContext);

		} else {
			request.setAttribute("noPurContext", "");
		}

		// 查看以前有无出让公告保存
		TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
		if (!"".equals(noticeId)) {
			// tdscBlockFileOld = tdscFileService.getBlockFileAppById(noticeId);
			TdscNoticeApp tdscNoticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				tdscBlockFileOld = tdscFileService.getBlockFileAppByRecordId(tdscNoticeApp.getRecordId());
			}
		}

		// 判断是否是发布公告
		if ("fabu".equals(type)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
			}
			request.setAttribute("type", type);
			request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
			return mapping.findForward("printSaveNotice");
		}

		if ("notice".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");

					// request.setAttribute("blockPlanTable", arg1)
					return mapping.findForward("printSaveNotice");
				} else {
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
					return mapping.findForward("printNotice_word");
					// return mapping.findForward("printNotice_pm");
				}
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {

				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					// 如果是工业用地
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());

					if ("101".equals(strBlockQuality)) {
						// 工业用地，需要在 Template 中加入文件 notice_gp_industry.doc
						// 修改配置 struts.xml
						if(noticeNo.indexOf("租") != -1){
							request.setAttribute("modeNameEn", "notice_gp_industry_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "notice_gp_industry.doc");
						}
						return mapping.findForward("printSaveNoticeGpIndustry");
					} else {
						// 经营性用地
						if(noticeNo.indexOf("租") != -1){
							request.setAttribute("modeNameEn", "printNotice_gp_word_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
						}
						return mapping.findForward("printSaveNotice");
					}

				} else {
					if ("101".equals(strBlockQuality)) {
						// 工业用地，需要在 Template 中加入文件 notice_gp_industry.doc
						// 修改配置 struts.xml
						if(noticeNo.indexOf("租") != -1){
							request.setAttribute("modeNameEn", "notice_gp_industry_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "notice_gp_industry.doc");
						}
						
						return mapping.findForward("printNotice_industry");
					} else {
						// 经营性用地
						if(noticeNo.indexOf("租") != -1){
							request.setAttribute("modeNameEn", "printNotice_gp_word_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
						}
						return mapping.findForward("printNotice_word");
						// return mapping.findForward("printNotice_gp");
					}
				}
			}

		} else if ("plan".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_pm_word.doc");
				// return mapping.findForward("printPlan_pm");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_gp_word.doc");
				// return mapping.findForward("printPlan_gp");
			}
			return mapping.findForward("printPlan_word");
		}
		return null;
	}

	/**
	 * 打印公告信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printNotice_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// noticeId ，实质为planId，在将“制作公告”提前至与“制作出让文件”一起时修改（2010-5-3 smw）。
		String noticeId = (String) request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		String printType = (String) request.getParameter("printType");
		String type = (String) request.getParameter("type");
		String planId = request.getParameter("planId");
		request.setAttribute("noticeName", request.getParameter("noticeName"));

		String createNoticeNo1 = request.getParameter("createNoticeNo1");
		String createNoticeNo2 = request.getParameter("createNoticeNo2");
		String getNoticeNo = request.getParameter("getnoticeNo");
		request.setAttribute("createNoticeNo1", createNoticeNo1);
		request.setAttribute("createNoticeNo2", createNoticeNo2);

		String noticeNo = "";
		// 取得出让文件信息
		TdscNoticeApp noticeApp = new TdscNoticeApp();

		noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		if (noticeApp != null && !"".equals(noticeApp.getNoticeNo())) {
			noticeNo = noticeApp.getNoticeNo();
		} else {
			// TdscFileForm tdscFileForm = (TdscFileForm)form;
			String noticeNum = request.getParameter("noticeNum");
			if (StringUtils.isNotEmpty(noticeNum))
				noticeNo = "青土资房告字[" + noticeNum + "号";
			else
				noticeNo = request.getParameter("noticeNo");
		}
		// 默认出让方式为拍卖
		String transferMode = GlobalConstants.DIC_TRANSFER_AUCTION;
		if (noticeId != null && !"".equals(noticeId)) {
			// 根据noticeId查询blockIdList(noticeId ，实质为planId)
			planId = StringUtils.isEmpty(planId) ? noticeId : StringUtils.trimToEmpty(planId);
			List blockIdList = (List) tdscScheduletableService.queryBlockIdListByPlanId(planId);

			// 为出让文件最上面的信息表构造指定格式数据
			List tdscBlockPartList = new ArrayList();
			if (blockIdList != null && blockIdList.size() > 0) {
				tdscBlockPartList = tdscBlockInfoService.makeDataForFile(blockIdList);
			}
			request.setAttribute("tdscBlockPartList", tdscBlockPartList);

			// 整理竞买资格要求
			TdscBaseQueryCondition viewcondition = new TdscBaseQueryCondition();
			List blockTranAppList = new ArrayList();
			List appIdsList = new ArrayList();
			String blockQuality = "";
			String appId = "";
			for (int i = 0; i < blockIdList.size(); i++) {
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp((String) blockIdList.get(i));
				if ("101".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "工";
				} else if ("102".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "经";
				}
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo((String) blockIdList.get(i));
				if (tdscBlockInfo != null && tdscBlockTranApp != null) {
					// 将地块名称 暂时设置在 SpecialPromise 字段里
					tdscBlockTranApp.setSpecialPromise(tdscBlockInfo.getBlockName() + "");
					transferMode = tdscBlockTranApp.getTransferMode();
					appId = tdscBlockTranApp.getAppId();
					appIdsList.add(appId);
				}
				blockTranAppList.add(tdscBlockTranApp);
			}

			if (appIdsList != null && appIdsList.size() > 0) {

				request.setAttribute("appIdList", appIdsList);
				// lz+ 所有part地块信息
				viewcondition.setAppIdList(appIdsList);
				viewcondition.setOrderKey("xuHao");
				List list = commonQueryService.queryTdscBlockAppViewListWithoutNode(viewcondition);

				String blockId = "";
				List returnPartForOneList = new ArrayList();
				TdscBlockAppView BlockAppView = new TdscBlockAppView();

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						List blockidListForOne = new ArrayList();// 为取出单个地块子地块的参数list
						BlockAppView = (TdscBlockAppView) list.get(i);

						blockId = BlockAppView.getBlockId();
						blockidListForOne.add(blockId);
						List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

						String blockCode = "";
						String tdcrnx = "";
						String tdcrnxAll = "";
						BigDecimal blockArea = new BigDecimal(0.00);
						for (int j = 0; j < partListForOne.size(); j++) {
							TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);
							if (j == 0) {
								blockCode = tdscBlockPartForOne.getBlockCode();
							}
							if (j > 0) {
								blockCode += "、" + tdscBlockPartForOne.getBlockCode();
							}
							blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);

							// 拍卖所有地块土地用途
							tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
							tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);

							BlockAppView.setTotalLandArea(blockArea);
							BlockAppView.setUnitebBlockCode(blockCode);

							// 取得入市审核时新加入的字段 存在tranapp中
							blockId = tdscBlockPartForOne.getBlockId();
							TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);

							BlockAppView.setBzBeizhu(tdscBlockTranApp.getBzBeizhu());
							BlockAppView.setBzCrjkjnyq(tdscBlockTranApp.getBzCrjkjnyq());
							BlockAppView.setBzDgjjgsj(tdscBlockTranApp.getBzDgjjgsj());
							BlockAppView.setBzDkqtsm(tdscBlockTranApp.getBzDkqtsm());
							BlockAppView.setBzGhsjyd(tdscBlockTranApp.getBzGhsjyd());
							BlockAppView.setBzGpsxf(tdscBlockTranApp.getBzGpsxf());
							BlockAppView.setBzJmzgjyq(tdscBlockTranApp.getBzJmzgjyq());
							BlockAppView.setBzPmyj(tdscBlockTranApp.getBzPmyj());
							BlockAppView.setBzSzpttj(tdscBlockTranApp.getBzSzpttj());
							BlockAppView.setBzTdcrfj(tdscBlockTranApp.getBzTdcrfj());
							BlockAppView.setBzTdjftj(tdscBlockTranApp.getBzTdjftj());
							BlockAppView.setBzXbjg(tdscBlockTranApp.getBzXbjg());
							// BlockAppView.setBzXbjg(replaceRN(tdscBlockTranApp.getBzXbjg()));
						}

						// 拍卖所有地块土地用途 存在RangeSouth中，土地出让年限存在RangeWest
						BlockAppView.setRangeSouth(tdcrnx);
						BlockAppView.setRangeWest(tdcrnxAll);
						returnPartForOneList.add(BlockAppView);
						request.setAttribute("tdscBlockAppView", BlockAppView);
					}
				}
				request.setAttribute("pageList", returnPartForOneList);// 大地块信息表
				// lz end
			}

			if ("true".equals(getNoticeNo)) {
				getNoticeNo = "锡国土" + blockQuality + "字[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";

			}

			if (!"".equals(appId)) {
				// 查询视图，获得工作方案
				TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
				condition.setAppId(appId);
				// TdscBlockAppView tdscBlockAppView =
				// (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);

			}
			request.setAttribute("blockTranAppList", blockTranAppList);
		}

		request.setAttribute("transferMode", transferMode);

		// 查看以前有无出让公告保存
		TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
		if (!"".equals(noticeId)) {
			tdscBlockFileOld = tdscFileService.getBlockFileAppById(noticeId);
		}

		// 判断是否是发布公告
		if ("fabu".equals(type)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
			}
			request.setAttribute("type", type);
			request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
			return mapping.findForward("printSaveNotice");
		}

		if ("notice".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");

					return mapping.findForward("printSaveNotice");
				} else {
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
					return mapping.findForward("printNotice_word");
					// return mapping.findForward("printNotice_pm");
				}
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
					request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
					return mapping.findForward("printSaveNotice");
				} else {
					request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
					return mapping.findForward("printNotice_word");
					// return mapping.findForward("printNotice_gp");
				}
			}

		} else if ("plan".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_pm_word.doc");
				// return mapping.findForward("printPlan_pm");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_gp_word.doc");
				// return mapping.findForward("printPlan_gp");
			}
			return mapping.findForward("printPlan_word");
		}
		return null;
	}

	/**
	 * 打印工作方案——制定实施方案中
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = (String) request.getParameter("planId");
		String type = (String) request.getParameter("type");

		request.setAttribute("type", type);

		String noticeNo = "";
		String transferMode = "";
		String blockId = "";
		String recordId = "";
		List blockIdList = new ArrayList();

		if (planId != null && !"".equals(planId)) {

			// 取得实施方案信息，取recordId
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);
			if (tdscBlockPlanTable != null && tdscBlockPlanTable.getRecordId() != null) {
				recordId = tdscBlockPlanTable.getRecordId();
			}

			TdscBlockAppView BlockAppViewByPlanId = new TdscBlockAppView();
			// 取得所有地块信息
			TdscBaseQueryCondition planIdCondition = new TdscBaseQueryCondition();
			planIdCondition.setPlanId(planId);
			List viewListByPlanId = commonQueryService.queryTdscBlockAppViewListByPlanId(planIdCondition);

			// 查询blockIdList
			if (viewListByPlanId != null && viewListByPlanId.size() > 0) {
				for (int m = 0; m < viewListByPlanId.size(); m++) {
					BlockAppViewByPlanId = (TdscBlockAppView) viewListByPlanId.get(m);
					transferMode = BlockAppViewByPlanId.getTransferMode();
					blockId = BlockAppViewByPlanId.getBlockId();
					blockIdList.add(blockId);
				}
			}

			if (blockIdList != null && blockIdList.size() > 0) {
				// 查询子地块信息
				List tdscBlockPartList = (List) tdscFileService.getBlockPartByBlockIdList(blockIdList);
				BigDecimal blockAreas = new BigDecimal(0);
				if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
					for (int i = 0; i < tdscBlockPartList.size(); i++) {
						TdscBlockPart tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
						if (tdscBlockPart.getBlockArea() != null) {
							blockAreas = blockAreas.add(tdscBlockPart.getBlockArea());
						}
						// 取得序号
						String blockId2 = tdscBlockPart.getBlockId();
						String xuhao = "";
						for (int m = 0; m < blockIdList.size(); m++) {
							if (blockId2.equals(blockIdList.get(m))) {
								xuhao = "00" + String.valueOf(m + 1);
								xuhao = xuhao.substring(xuhao.length() - 2);
							}
						}
						// 序号存在BlockName中
						tdscBlockPart.setBlockName(xuhao);

						TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp(blockId2);

						TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId2);
						// 容积率,密度，绿化,地块位置保存在RangeEast中,地块公告号保存在RangeNorth中
						// tdscBlockPart.setVolumeRate(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()));
						// tdscBlockPart.setDensity(StringUtils.trimToEmpty(tdscBlockPart.getDensity()));
						// tdscBlockPart.setGreeningRate(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()));
						tdscBlockPart.setRangeEast(StringUtils.trimToEmpty(tdscBlockInfo.getLandLocation()));
						tdscBlockPart.setRangeNorth(StringUtils.trimToEmpty(tdscBlockInfo.getBlockNoticeNo()));

						// 保证金，起拍价
						tdscBlockPart.setMemo(NumberUtil.numberDisplay(tdscBlockTranApp.getMarginAmount(), 2));
						tdscBlockPart.setBlockDetailedUsed(NumberUtil.numberDisplay(tdscBlockTranApp.getInitPrice(), 2));
					}
				}
				request.setAttribute("blockAreas", blockAreas + "");
				request.setAttribute("tdscBlockPartList", tdscBlockPartList);
				// 整理竞买资格要求
				TdscBaseQueryCondition viewcondition = new TdscBaseQueryCondition();
				List blockTranAppList = new ArrayList();
				List appIdsList = new ArrayList();
				String appId = "";
				for (int i = 0; i < blockIdList.size(); i++) {
					TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp((String) blockIdList.get(i));
					TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo((String) blockIdList.get(i));
					if (tdscBlockInfo != null && tdscBlockTranApp != null) {
						// 将地块名称 暂时设置在 SpecialPromise 字段里
						tdscBlockTranApp.setSpecialPromise(tdscBlockInfo.getBlockName() + "");
						transferMode = tdscBlockTranApp.getTransferMode();
						appId = tdscBlockTranApp.getAppId();
						appIdsList.add(appId);
					}
					blockTranAppList.add(tdscBlockTranApp);
				}

				// lz+ 所有part地块信息
				viewcondition.setAppIdList(appIdsList);
				List list = commonQueryService.queryTdscBlockAppViewListWithoutNode(viewcondition);

				List returnPartForOneList = new ArrayList();
				TdscBlockAppView BlockAppView = new TdscBlockAppView();

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						List blockidListForOne = new ArrayList();// 为取出单个地块子地块的参数list
						BlockAppView = (TdscBlockAppView) list.get(i);

						blockId = BlockAppView.getBlockId();
						blockidListForOne.add(blockId);
						List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

						String blockCode = "";
						String tdcrnx = "";
						String tdcrnxAll = "";
						BigDecimal blockArea = new BigDecimal(0.00);
						BigDecimal mu = new BigDecimal(0.00);
						for (int j = 0; j < partListForOne.size(); j++) {
							TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);
							if (j == 0) {
								blockCode = tdscBlockPartForOne.getBlockCode();
							}
							if (j > 0) {
								blockCode += "、" + tdscBlockPartForOne.getBlockCode();
							}
							blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);

							tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
							tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);
							BlockAppView.setTotalLandArea(blockArea);
							BlockAppView.setUnitebBlockCode(blockCode);

							// 取得入市审核时新加入的字段 存在tranapp中
							blockId = tdscBlockPartForOne.getBlockId();
							TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);

							BlockAppView.setBzBeizhu(tdscBlockTranApp.getBzBeizhu());
							BlockAppView.setBzCrjkjnyq(tdscBlockTranApp.getBzCrjkjnyq());
							BlockAppView.setBzDgjjgsj(tdscBlockTranApp.getBzDgjjgsj());
							BlockAppView.setBzDkqtsm(tdscBlockTranApp.getBzDkqtsm());
							BlockAppView.setBzGhsjyd(tdscBlockTranApp.getBzGhsjyd());
							BlockAppView.setBzGpsxf(tdscBlockTranApp.getBzGpsxf());
							BlockAppView.setBzJmzgjyq(tdscBlockTranApp.getBzJmzgjyq());
							BlockAppView.setBzPmyj(tdscBlockTranApp.getBzPmyj());
							BlockAppView.setBzSzpttj(tdscBlockTranApp.getBzSzpttj());
							BlockAppView.setBzTdcrfj(tdscBlockTranApp.getBzTdcrfj());
							BlockAppView.setBzTdjftj(tdscBlockTranApp.getBzTdjftj());
							BlockAppView.setBzXbjg(tdscBlockTranApp.getBzXbjg());
						}

						// 拍卖所有地块土地用途 存在RangeSouth中，土地出让年限存在RangeWest
						mu = blockArea.multiply(new BigDecimal(0.0015));
						mu = mu.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
						BlockAppView.setMu(mu);// 亩
						BlockAppView.setRangeSouth(tdcrnx);
						BlockAppView.setRangeWest(tdcrnxAll);

						returnPartForOneList.add(BlockAppView);
						request.setAttribute("tdscBlockAppView", BlockAppView);
					}
				}

				request.setAttribute("pageList", returnPartForOneList);// 大地块信息表
				// lz end
				if (!"".equals(appId)) {
					// 查询视图，获得工作方案
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					condition.setAppId(appId);
					// TdscBlockAppView tdscBlockAppView =
					// (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);

				}
				request.setAttribute("blockTranAppList", blockTranAppList);
			}
		}

		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("recordId", recordId);

		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
			request.setAttribute("modeNameEn", "printPlan_pm_word.doc");
		}
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
			request.setAttribute("modeNameEn", "printPlan_gp_word.doc");
		}
		return mapping.findForward("printPlan_word");
	}

	public static String replaceRN(String s) {
		while (s != null && (s.indexOf("\r\n") != -1)) {
			s = s.substring(0, s.indexOf("\r\n")) + "<br>" + s.substring(s.indexOf("\r\n") + 2);
		}
		return s;
	}

	/**
	 * 检查出让公告是否已存在
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward ifSaveFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = (String) request.getParameter("noticeId");
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		String returnStr = "00";

		// 查看以前有无出让公告保存
		TdscNoticeApp tdscNoticeApp = null;
		if (!"".equals(noticeId)) {
			tdscNoticeApp = this.tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null && tdscNoticeApp.getRecordId() != null)
				returnStr = "01";
		}

		pw.write(returnStr);
		pw.close();
		return null;
	}

	/**
	 * 检查noticeNo是否已存在
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkNoticeNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// String noticeNo1 = (String) request.getParameter("noticeNo");
		String createNoticeNo1 = (String) request.getParameter("createNoticeNo1");
		String createNoticeNo2 = (String) request.getParameter("createNoticeNo2");
		String blockNoticeTile = (String) request.getParameter("blockNoticeTile");

		String noticeNo = blockNoticeTile + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "号";

		request.setAttribute("noticeNo", noticeNo);
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		String returnStr = "00";

		// 查看以前有无出让公告保存
		List tdscNoticeAppList = new ArrayList();
		if (!"".equals(noticeNo)) {
			tdscNoticeAppList = tdscNoticeService.findTdscNoticeAppListByNoticeNo(StringUtil.GBKtoISO88591(noticeNo));
			if (tdscNoticeAppList != null && tdscNoticeAppList.size() > 0)
				returnStr = "01";// 存在该公告号
		}

		pw.write(returnStr);
		pw.close();
		return null;
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
	public ActionForward checkFileEditComp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		List list = this.commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		int size = 0;
		int editCount = 0;
		if (list != null) {
			size = list.size();
			for (int i = 0; i < list.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) list.get(i);
				if ("0302".equals(tdscBlockAppView.getNextAction())) {
					editCount++;
				}
			}
		}
		// 将内容设置到输出

		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		if (size == editCount) {
			pw.write("1");
		} else {
			pw.write("0");
		}
		pw.close();
		return null;
	}

	/**
	 * 退回
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward callback(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String transferMode = tdscBlockAppView.getTransferMode();
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(appId);
		appFlow.setTransferMode(transferMode);
		appFlow.setUser(user);
		appFlow.setTextOpen("回退重新制作出让文件");
		appFlow.setResultId("030202");
		// 回退之后删除 出让文件
		this.tdscNoticeService.callBack(appId, appFlow);
		return new ActionForward("file.do?method=queryAppFileListWithNodeId", true);
	}

	public ActionForward generateNextNoticeNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);

		String noticeNoName = "";
		if ("3107".equals(tdscBlockPlanTable.getTransferMode())) {
			noticeNoName = "无锡市国土资源局国有建设用地使用权招标出让公告";
		} else if ("3103".equals(tdscBlockPlanTable.getTransferMode())) {
			noticeNoName = "无锡市国土资源局国有建设用地使用权拍卖出让公告";
		} else if ("3104".equals(tdscBlockPlanTable.getTransferMode())) {
			//判断是否是租赁公告
			if(StringUtils.isNotBlank(tdscBlockPlanTable.getTradeNum()) && tdscBlockPlanTable.getTradeNum().indexOf("租") != -1){
				noticeNoName = "无锡市国土资源局国有建设用地使用权挂牌租赁公告";
			}else{
				noticeNoName = "无锡市国土资源局国有建设用地使用权挂牌出让公告";
			}
		}

		java.util.Date sysdate = new java.util.Date();
		java.text.SimpleDateFormat dformat = new java.text.SimpleDateFormat("yyyy");
		String year = (String) dformat.format(sysdate);

		String noticeNoPrefix = "";
		String noticeNo1 = "";
		String temp ="";
		String tradeNum = tdscBlockPlanTable.getTradeNum();
		
		
		if(StringUtils.isNotBlank(tradeNum) && tradeNum.indexOf("租") != -1){
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "锡" + tradeNum.substring(4, 5) + "租告字[" + year + "]";
				noticeNo1 = "锡" + tradeNum.substring(4, 5) + "租告字";
			} else {
				noticeNoPrefix = "锡经租告字[" + year + "]";
				noticeNo1 = "锡经租告字";
			}
			temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("17",noticeNoPrefix);
		}else{
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "锡" + tradeNum.substring(4, 5) + "告字[" + year + "]";
				noticeNo1 = "锡" + tradeNum.substring(4, 5) + "告字";
			} else {
				noticeNoPrefix = "锡经告字[" + year + "]";
				noticeNo1 = "锡经告字";
			}
			temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("15",noticeNoPrefix);
		}


		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		pw.write(noticeNoName + "," + noticeNo1 + "," + year + "," + Integer.parseInt(temp));
		pw.close();

		return null;
	}	
}
