package com.wonders.tdsc.blockwork.web;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.blockwork.web.form.ScheduletableInfoForm;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNotaryInfo;
import com.wonders.tdsc.bo.TdscXbOrgApp;
import com.wonders.tdsc.bo.TdscXbOrgHistory;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscBlockTranAppCondition;
import com.wonders.tdsc.businessDay.X_BusinessDayService;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.randomselect.service.TdscSelectService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.tdscbase.service.ShortMessageService;
import com.wonders.tdsc.xborg.service.TdscXbOrgService;

public class TdscScheduletableAction extends BaseAction {

	private CommonQueryService			commonQueryService;

	private TdscScheduletableService	tdscScheduletableService;

	private AppFlowService				appFlowService;

	private TdscSelectService			tdscSelectService;

	private CommonFlowService			commonFlowService;

	private TdscXbOrgService			tdscXbOrgService;

	private ShortMessageService			smsService;

	private TdscNoticeService			tdscNoticeService;

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setTdscSelectService(TdscSelectService tdscSelectService) {
		this.tdscSelectService = tdscSelectService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setSmsService(ShortMessageService smsService) {
		this.smsService = smsService;
	}

	public void setTdscXbOrgService(TdscXbOrgService tdscXbOrgService) {
		this.tdscXbOrgService = tdscXbOrgService;
	}

	/**
	 * 删除进度安排表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delPlanTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		if (planId != null && !"".equals(planId)) {
			// 查询进度安排表
			TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
			planTableCondition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);
			// 查询地块交易表信息
			TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
			tranAppCondition.setPlanId(planId);
			List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
			tdscScheduletableService.delPlanTable(tdscBlockPlanTable, tranAppList);
		}
		// return queryAppListWithNodeId(mapping, form, request, response);

		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * 进度安排表中删除地块
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strAppId = request.getParameter("appId");
		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setAppId(strAppId);
		TdscBlockAppView view = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(cond);

		tdscScheduletableService.deleteBlockInfo(strAppId, view.getBlockId());

		return new ActionForward("scheduletable.do?method=queryBlockAppList", true);
	}

	/**
	 * 进入以进度安排表单位的查询列表页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面值
		String pageNo = request.getParameter("currentPage");
		String blockName = request.getParameter("blockName");
		String tradeNum = request.getParameter("tradeNum");
		String transferMode = request.getParameter("transferMode");
		String uniteBlockCode = request.getParameter("uniteBlockCode");
		String ifPCommit = request.getParameter("ifPCommit");
		String landLocation = request.getParameter("landLocation");

		// 获得Session中登录用户的按钮信息列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}

		// 设置查询条件
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		this.bindObject(planCondition, form);

		// 整理用户信息中的可查询的实施方案状态
		ArrayList statusFlowList = new ArrayList();
		// 判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			planCondition.setUserId(user.getUserId());
		}
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_MAKE) != null) {
			statusFlowList.add("00");
		}
		// 修改日期和场次，需要根据用户ID来查询数据
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_DATE_MODIFY) != null || buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_FIELD_MODIFY) != null) {
			statusFlowList.add("02");
		}
		// 审核人员查看的是全部待审核的数据，把查询条件中的UserId设置为空
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_SHENHE) != null) {
			statusFlowList.add("01");
			planCondition.setUserId(null);
		}

		if (request.getAttribute("forwardType") != null && "01".equals(request.getAttribute("forwardType"))) {
			planCondition.setBlockName("");
			planCondition.setTradeNum("");
			planCondition.setTransferMode("");
			planCondition.setUniteBlockCode("");
			planCondition.setLandLocation("");
			planCondition.setPlanId("");
		} else {
			planCondition.setBlockName(StringUtils.trimToEmpty(blockName));
			planCondition.setTradeNum(StringUtils.trimToEmpty(tradeNum));
			planCondition.setUniteBlockCode(StringUtils.trimToEmpty(uniteBlockCode));
			planCondition.setIfPCommit(StringUtils.trimToEmpty(ifPCommit));
			planCondition.setTransferMode(StringUtils.trimToEmpty(transferMode));
			planCondition.setStatusFlowList(statusFlowList);
			planCondition.setLandLocation(StringUtils.trimToEmpty(landLocation));
		}

		// 查询
		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		queryList = setFlagForDelete(queryList);
		// 构造分页
		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false) {
			currentPage = Integer.parseInt(pageNo);

		}
		if (pageNo == null || Integer.parseInt(pageNo) < 1) {
			currentPage = 1;
		}
		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("planCondition", planCondition);

		if (request.getParameter("type") != null && "chayue".equals(request.getParameter("type"))) {
			// 转到实施方案查阅页面
			return mapping.findForward("plantable_chayue");
		} else {
			// 转到实施方案拟定页面
			return mapping.findForward("plantablelist");
		}
	}

	/**
	 * 过滤出未做公告的所有计划 NODE_ID=03 && STATUS_ID=0302
	 * 
	 * @param queryList
	 *            TdscBlockPlanTable
	 * @return
	 */
	private List setFlagForDelete(List queryList) {
		if (queryList != null && queryList.size() > 0)
			for (int i = 0; i < queryList.size(); i++) {
				TdscBlockPlanTable plan = (TdscBlockPlanTable) queryList.get(i);

				TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
				cond.setPlanId(plan.getPlanId());
				List appViews = commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
				TdscBlockAppView appView = null;

				if (appViews != null && appViews.size() > 0)
					appView = (TdscBlockAppView) appViews.get(0);
				if(plan.getBzBeizhu()==null){
					if (appView != null) {
						if ("03".equals(appView.getNodeId()) && "0302".equals(appView.getStatusId()))
							plan.setBzBeizhu("CanDel");
						else
							plan.setBzBeizhu("NotDel");
					} else {
						plan.setBzBeizhu("NotDel");
					}
				}
			}
		return queryList;
	}

	/**
	 * 查询节点状态在制定进度安排表的地块
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
//		ArrayList nodeList = new ArrayList();
//		nodeList.add(FlowConstants.FLOW_NODE_SCHEDULE_PLAN);
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNodeId(FlowConstants.FLOW_NODE_SCHEDULE_PLAN);
//		condition.setNodeList(nodeList);
		// 判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		List queryList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		// 去除掉已经做过进度安排表的地块信息
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) queryList.get(i);
				if (tdscBlockAppView.getPlanId() != null) {
					queryList.remove(tdscBlockAppView);
					i--;
				}
			}
		}

		request.setAttribute("queryBlockist", queryList);

		return mapping.findForward("blocklist");
	}

	/**
	 * 制定进度安排表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward applyPlanTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		String[] appIds = infoForm.getAppIds();
		String statusFlow = request.getParameter("statusFlow");
		String ifOnLine = request.getParameter("ifOnLine");
		String transferMode = "";
		String iszulin = "";
		String blockQuality = "";
		List tdscblockAppViewList = new ArrayList();
		for (int i = 0; i < appIds.length; i++) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appIds[i]);
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			if (!"".equals(tdscBlockAppView.getTransferMode()) && tdscBlockAppView.getTransferMode() != null) {
				transferMode = tdscBlockAppView.getTransferMode();
				iszulin = tdscBlockAppView.getIsZulin();
			}
			if (StringUtils.isNotBlank(tdscBlockAppView.getBlockQuality())) {
				blockQuality = tdscBlockAppView.getBlockQuality();
			}

			tdscblockAppViewList.add(tdscBlockAppView);
		}
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("iszulin", iszulin);
		request.setAttribute("statusFlow", statusFlow);
		request.setAttribute("tdscBlockAppViewList", tdscblockAppViewList);
		// 获得协办机构
		TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
		request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);

		java.util.Date sysdate = new java.util.Date();
		java.text.SimpleDateFormat dformat = new java.text.SimpleDateFormat("yyyy");
		String year = (String) dformat.format(sysdate);

		String tradeNumPrefix = "";

		if ("3107".equals(transferMode)) {
			tradeNumPrefix += "WXZ";
		} else if ("3103".equals(transferMode)) {
			tradeNumPrefix += "WXP";
		} else if ("3104".equals(transferMode)) {
			tradeNumPrefix += "WXG";
		}
		String tradeNum ="";
		//租赁模式
		if("1".equals(iszulin)){
			if ("102".equals(blockQuality)) {
				tradeNumPrefix += "(经租)";
			} else if ("101".equals(blockQuality)) {
				tradeNumPrefix += "(工租)";
			}
			tradeNumPrefix += "-" + year;
			tradeNum = tdscScheduletableService.getNextTradeNum("16",tradeNumPrefix);
		}else{
			if ("102".equals(blockQuality)) {
				tradeNumPrefix += "(经)";
			} else if ("101".equals(blockQuality)) {
				tradeNumPrefix += "(工)";
			}
			tradeNumPrefix += "-" + year;
			tradeNum = tdscScheduletableService.getNextTradeNum("14",tradeNumPrefix);
			
		}


		request.setAttribute("tradeNum", tradeNum);
		request.setAttribute("ifOnLine", ifOnLine);

		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {

			return mapping.findForward("scheduletablePm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)) {
			return mapping.findForward("scheduletableZb");
		} else {
			return mapping.findForward("scheduletableGp");
		}
	}

	public ActionForward saveScheduletableInfo_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String transferMode = request.getParameter("transferMode");
		String saveType = request.getParameter("saveType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 获得页面上的tempSaveFlag,判断是否暂存
		String tempSaveFlag = request.getParameter("tempSaveFlag");
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// 将form中的值传入bo，对应出让地块进度安排表TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		// bindObject
		if (!"".equals(infoForm.getPlanId()) && infoForm.getPlanId() != null) {
			tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(infoForm.getPlanId());
		} else {
			infoForm.setPlanId(null);
		}
		bindObject(tdscBlockPlanTable, infoForm);
		bindObject(tdscBlockTranApp, infoForm);
		TdscBlockPlanTable retObj = (TdscBlockPlanTable) tdscScheduletableService.saveUnitePlanTable(tdscBlockPlanTable, infoForm, saveType, user);

		String[] appIds = infoForm.getAppIds();
		List tdscblockAppViewList = new ArrayList();
		for (int i = 0; i < appIds.length; i++) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appIds[i]);
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);

			tdscblockAppViewList.add(tdscBlockAppView);
		}

		// 根据ORG_APP_ID查询协办机构
		if (tdscBlockPlanTable != null) {
			String orgAppId = tdscBlockPlanTable.getOrgAppId();
			if (StringUtils.isNotEmpty(orgAppId)) {
				TdscXbOrgApp tdscXbOrgApp = (TdscXbOrgApp) tdscXbOrgService.getOrgAppInfoById(orgAppId);
				if (tdscXbOrgApp != null) {
					TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
					tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
					tdscXbOrgHistory.setOrgAppId(orgAppId);
					request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
				}
			}
		}
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tempSaveFlag", tempSaveFlag);
		request.setAttribute("tdscBlockAppViewList", tdscblockAppViewList);
		request.setAttribute("tdscBlockPlanTable", retObj);
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		request.setAttribute("saveMessage", "保存成功！");
		// 暂存返回info页面
		if ("tempSave".equals(saveType)) {
			// 根据出让方式选择进入那个业务：3107招标 3103拍卖 3104挂牌
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode) && null != transferMode) {
				return mapping.findForward("tempSaveZb");
			} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				return mapping.findForward("tempSavePm");
			} else {
				return mapping.findForward("tempSaveGp");
			}
		} else {// 提交则返回list页面

			return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
		}
	}

	/**
	 * 20090625保存实施方案
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveScheduletableInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String transferMode = request.getParameter("transferMode");
		String saveType = request.getParameter("saveType");
		String statusFlow = request.getParameter("statusFlow");
		String ifOnLine = request.getParameter("ifOnLine");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 获得页面上的tempSaveFlag,判断是否暂存
		String tempSaveFlag = request.getParameter("tempSaveFlag");
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// 将form中的值传入bo，对应出让地块进度安排表TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		// bindObject
		if (!"".equals(infoForm.getPlanId()) && infoForm.getPlanId() != null) {
			tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(infoForm.getPlanId());
		} else {
			infoForm.setPlanId(null);
		}

		bindObject(tdscBlockPlanTable, infoForm);
		tdscBlockPlanTable.setTradeNum(infoForm.getTradeNum_1() + "-" + infoForm.getTradeNum_2() + "-" + infoForm.getTradeNum_3());
		tdscBlockPlanTable.setIfOnLine(ifOnLine);//是否网上交易，1为是，0为现场交易
		bindObject(tdscBlockTranApp, infoForm);
		// 设置实施方案的流程节点
		tdscBlockPlanTable.setStatusFlow(statusFlow);
		// 设置操作用户信息
		String userId = "";
		String[] appIds = infoForm.getAppIds();
		List tdscblockAppViewList = new ArrayList();

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		for (int i = 0; i < appIds.length; i++) {
			if (StringUtils.isNotEmpty(appIds[i])) {
				// 设置查询条件
				condition.setAppId(appIds[i]);
				// 查询地块信息
				TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
				// 获得该地块的用户ID
				if (StringUtils.isNotEmpty(tdscBlockAppView.getUserId())) {
					userId = tdscBlockAppView.getUserId();
				}
				tdscblockAppViewList.add(tdscBlockAppView);
			}
		}
		// 由于一个
		if (StringUtils.isNotEmpty(userId)) {
			tdscBlockPlanTable.setUserId(userId);
		}
		
		//user_id 用于保存市局联系人
		String sjLinkMan = request.getParameter("sjLinkman");
		tdscBlockPlanTable.setUserId(sjLinkMan);
		
		TdscBlockPlanTable retObj = (TdscBlockPlanTable) tdscScheduletableService.saveUnitePlanTable(tdscBlockPlanTable, infoForm, saveType, user);

		// String[] tradeNums = retObj.getTradeNum().split("-");
		// if(tradeNums != null && tradeNums.length == 3){
		// request.setAttribute("tradeNum",tradeNums[2]);
		// }
		// 根据ORG_APP_ID查询协办机构
		// if(tdscBlockPlanTable != null){
		// String orgAppId = tdscBlockPlanTable.getOrgAppId();
		// if(StringUtils.isNotEmpty(orgAppId)){
		// TdscXbOrgApp tdscXbOrgApp = (TdscXbOrgApp)tdscXbOrgService.getOrgAppInfoById(orgAppId);
		// if(tdscXbOrgApp != null){
		// TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
		// tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
		// tdscXbOrgHistory.setOrgAppId(orgAppId);
		// request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
		// }
		// }
		// }
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tempSaveFlag", tempSaveFlag);
		request.setAttribute("statusFlow", statusFlow);
		request.setAttribute("tdscBlockAppViewList", tdscblockAppViewList);
		request.setAttribute("tdscBlockPlanTable", retObj);
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		request.setAttribute("saveMessage", "保存成功！");
		// 暂存返回info页面
		if ("tempSave".equals(saveType)) {
			// 根据出让方式选择进入那个业务：3107招标 3103拍卖 3104挂牌
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode) && null != transferMode) {
				return mapping.findForward("tempSaveZb");
			} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("forwardType", "01");
				return mapping.findForward("tempSavePm");
			} else {
				request.setAttribute("forwardType", "01");
				return mapping.findForward("tempSaveGp");
			}
		} else {// 提交则返回list页面
			request.setAttribute("forwardType", "01");
			return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
		}
	}

	/**
	 * 保存iweb实施方案的recordId
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveIwebPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		String recordId = request.getParameter("recordId");
		if (planId != null && !"".equals(planId)) {
			// 查询进度安排表
			TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
			planTableCondition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);
			if (recordId != null && !"".equals(recordId)) {
				tdscBlockPlanTable.setRecordId(recordId);
				tdscScheduletableService.updateBlockPlanTable(tdscBlockPlanTable);
			}
		}

		request.setAttribute("recordId", recordId);
		request.setAttribute("saveMessage", "保存成功！");

		return mapping.findForward("saveIwebPlan");
	}

	/**
	 * 查询待办业务列表
	 */
	public ActionForward queryAppListWithNodeId_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String blockNoticeNo = request.getParameter("blockNoticeNo");
		String currentPage = request.getParameter("currentPage");
		String blockName = request.getParameter("blockName");
		String transferMode = request.getParameter("transferMode");
		String blockType = request.getParameter("blockType");
		String districtId = request.getParameter("districtId");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		ArrayList nodeList = new ArrayList();
		// 获得Session中登录用户的按钮信息列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_MAKE) != null) {
			nodeList.add(FlowConstants.FLOW_NODE_SCHEDULE_PLAN);
		}
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_DATE_MODIFY) != null || buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_FIELD_MODIFY) != null) {
			nodeList.add(FlowConstants.FLOW_NODE_FILE_MAKE);
			nodeList.add(FlowConstants.FLOW_NODE_FILE_RELEASE);
			nodeList.add(FlowConstants.FLOW_NODE_PREVIEW);
			// nodeList.add(FlowConstants.FLOW_NODE_QUESTION_GATHER);
			// nodeList.add(FlowConstants.FLOW_NODE_ANSWER);
			nodeList.add(FlowConstants.FLOW_NODE_BID_OPENNING_APPROVAL);
			nodeList.add(FlowConstants.FLOW_NODE_BIDDER_APP);
			nodeList.add(FlowConstants.FLOW_NODE_AUCTION);
			nodeList.add(FlowConstants.FLOW_NODE_LISTING);
			nodeList.add(FlowConstants.FLOW_NODE_LISTING_SENCE);
		}

		// checked out by hjf to modify nodelist
		// field modify cn be done until transfer end

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		condition.setNodeList(nodeList);

		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
		condition.setBlockNoticeNo(blockNoticeNo);
		condition.setBlockName(blockName);
		condition.setTransferMode(transferMode);
		condition.setBlockType(blockType);
		condition.setDistrictId(districtId);
		condition.setPlusConditionTag(GlobalConstants.QUERY_WITHOUTPLUS_TAG);
		condition.setOrderKey("appDate");
		condition.setOrderType("desc");
		request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewPageList(condition));// 按条件查询列表
		request.setAttribute("queryAppCondition", condition);

		return mapping.findForward("list");
	}

	/**
	 * 进入业务工作模块进度安排表管理 20071129*
	 */
	public ActionForward queryAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return mapping.findForward("list");
	}

	/**
	 * 制定进度安排表管理信息 20071129*
	 */
	public ActionForward toScheduletableInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String statusId = request.getParameter("statusId");
		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 通过appId得到视图TdscBlockAppView中的blockId
			// 先给blockId的值为1，再取tdscBlockAppView中的blockId
			// TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(baseCondition);
			// String blockId = tdscBlockAppView.getBlockId();
			// 抽选功能

			// 根据blockId得到地块用途信息表中的规划用途和出让年限
			String blockId = "1";
			List tdscBlockUsedInfo = tdscScheduletableService.queryTdscBlockUsedInfoList(blockId);
			request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);
			// 通过appId来查询出让地块进度执行表中的‘接收要素材料起始时间(REC_MAT_START_DATE)’
			// TdscBlockScheduleTable tdscBlockScheduleTable = tdscScheduletableService.findScheduleInfo(appId);
			// request.setAttribute("tdscBlockScheduleTable", tdscBlockScheduleTable);
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// 判断TDSC_BLOCK_TRAN_APP表是否为空,取出MarginEndDate的值
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
		}
		request.setAttribute("appId", appId);
		// 获得查询条件
		TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		baseCondition.setAppId(appId);
		// 调用公共方法取得公共信息
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
		String mode = (String) commonInfo.getTransferMode();
		request.setAttribute("mode", mode);
		if (FlowConstants.FLOW_STATUS_SCHEDULETABLE_MAKE.equals(statusId)) {
			// 根据出让方式选择进入那个业务：DIC_TRANSFER_TENDER = "3107"招标 DIC_TRANSFER_AUCTION = "3103"拍卖 DIC_TRANSFER_LISTING = "3104"挂牌
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
				return mapping.findForward("scheduletableGp");
			}
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
				return mapping.findForward("scheduletablePm");
			}
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
				return mapping.findForward("scheduletableZb");
			}
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * 保存进度安排表管理信息-招标 拍卖 挂牌
	 * 
	 * @param appId
	 *            and mode 20071201*
	 */
	public ActionForward saveScheduletableInfo_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String mode = request.getParameter("mode");
		String saveType = request.getParameter("saveType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// String selectType = "01";

		// 获得页面上的tempSaveFlag,判断是否暂存
		String tempSaveFlag = request.getParameter("tempSaveFlag");
		// 调用ActionForm
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// 将form中的值传入bo，对应出让地块进度安排表TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		tdscBlockPlanTable.setAppId(appId);
		bindObject(tdscBlockPlanTable, infoForm);

		// 根据出让方式选择保存相应的值：3107招标 3103拍卖 3104挂牌
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			tdscBlockPlanTable.setListStartDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setListEndDate(DateUtil.string2Timestamp(request.getParameter("listEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidDate(DateUtil.string2Timestamp(request.getParameter("sceBidDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidLoc(request.getParameter("sceBidLoc"));
			tdscBlockPlanTable.setListLoc(request.getParameter("listLoc"));
		} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			tdscBlockPlanTable.setAuctionDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setAuctionLoc(request.getParameter("auctionLoc"));
		} else {
			tdscBlockPlanTable.setTenderStartDate(DateUtil.string2Timestamp(request.getParameter("tenderStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setTenderEndDate(DateUtil.string2Timestamp(request.getParameter("tenderEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningLoc(request.getParameter("openingLoc"));
			tdscBlockPlanTable.setBidEvaDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setBidEvaLoc(request.getParameter("bidEvaLoc"));
		}
		// 将“保证金到帐截止时间”保存到TDSC_BLOCK_TRAN_APP表中
		String marginEndDat = request.getParameter("marginEndDat");
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp.setMarginEndDate(DateUtil.string2Timestamp(marginEndDat, DateUtil.FORMAT_DATETIME));
		// 执行插入数据
		tdscScheduletableService.savePlanTableInfo(tdscBlockPlanTable, appId, mode, tdscBlockTranApp, saveType, user);
		request.setAttribute("saveMessage", "保存成功");
		// tempSaveFlag不为空时,跳到暂存页面
		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 通过appId得到视图TdscBlockAppView中的blockId
			// 先给blockId的值为1，再取tdscBlockAppView中的blockId
			// TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(baseCondition);
			// String blockId = tdscBlockAppView.getBlockId();
			String blockId = "1";
			// 根据blockId得到地块用途信息表中的规划用途和出让年限
			List tdscBlockUsedInfo = tdscScheduletableService.queryTdscBlockUsedInfoList(blockId);
			request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);

			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
			planCondition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(planCondition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// 判断TDSC_BLOCK_TRAN_APP表是否为空,取出MarginEndDate的值
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
		}
		request.setAttribute("appId", appId);
		request.setAttribute("mode", mode);
		request.setAttribute("tempSaveFlag", tempSaveFlag);

		if (null != tempSaveFlag) {
			// 根据出让方式选择进入那个业务：3107招标 3103拍卖 3104挂牌
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
				return mapping.findForward("tempSaveGp");
			}
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
				return mapping.findForward("tempSavePm");
			}
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
				return mapping.findForward("tempSaveZb");
			}
		}
		// if(null==tempSaveFlag){
		// // 抽选功能
		// //抽选公证处信息
		// TdscNotaryInfo tdscNotaryInfo = tdscSelectService.verticalRangeSelect();
		// request.setAttribute("tdscNotaryInfo", tdscNotaryInfo);
		// String notaryId = tdscNotaryInfo.getNotaryId();
		// // 根据出让方式保存公证处信息--挂牌
		// if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
		// Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		// // nowTime.setHours(07);
		// // nowTime.setMinutes(00);
		// // nowTime.setSeconds(00);
		// nowTime = tidy4AnyTimestamp(nowTime, 07, 0, 0);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// //根据回复截止时间
		// String replyTime = tdscSelectService.setReplyDeadLine();
		//
		//
		// // 更改或保存挂牌
		// TdscBlockSelectApp appTemp3 = new TdscBlockSelectApp();
		// appTemp3.setAppId(appId);
		// appTemp3.setSelectedId(notaryId);
		// appTemp3.setSelectType("01");
		// appTemp3.setActivityDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
		// appTemp3.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("listLoc")));
		// appTemp3.setUnlockTime(nowTime);
		// appTemp3.setSelectUser(user.toString());
		// appTemp3.setSelectDate(now);
		// appTemp3.setIsValid("01");
		// appTemp3.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp3.setNodeStat("00");
		// appTemp3.setNodeId("16");
		// appTemp3.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		//
		//
		// appTemp3.setBlockName(infoForm.getBlockName());
		// appTemp3.setMarginEndDate(tdscBlockTranApp.getMarginEndDate());
		// appTemp3.setListEndDate(tdscBlockPlanTable.getListEndDate());
		// appTemp3.setSceBidDate(tdscBlockPlanTable.getSceBidDate());
		// appTemp3.setSceBidLoc(tdscBlockPlanTable.getSceBidLoc());
		//
		// this.tdscSelectService.saveBlockSelectApp(appTemp3);
		// // A类公证处 进度安排表 提交后，需要向公证处发送段信通知，如果不是提交，则不能发送短信
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>发送消息的方法开始调用");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp3);
		// // logger.info("===========>发送消息的方法调用调用结束"+returnMsg);
		// // }
		// }
		//
		// // 根据出让方式保存公证处信息--招标
		// if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
		// // 根据回复截止时间
		// String replyTime = tdscSelectService.setReplyDeadLine();
		// Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		// // nowTime.setHours(07);
		// // nowTime.setMinutes(00);
		// // nowTime.setSeconds(00);
		// nowTime = tidy4AnyTimestamp(nowTime, 07, 0, 0);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		//
		//
		// // 更改或保存开标/定标会
		// TdscBlockSelectApp appTemp2 = new TdscBlockSelectApp();
		// appTemp2.setAppId(appId);
		// appTemp2.setSelectedId(notaryId);
		// appTemp2.setSelectType("01");
		// appTemp2.setActivityDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
		// appTemp2.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("openingLoc")));
		// appTemp2.setUnlockTime(nowTime);
		// appTemp2.setSelectUser(user.toString());
		// appTemp2.setSelectDate(now);
		// appTemp2.setIsValid("01");
		// appTemp2.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp2.setNodeStat("00");
		// appTemp2.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		// this.tdscSelectService.saveBlockSelectApp(appTemp2);
		//
		// // A类公证处 进度安排表 提交后，需要向公证处发送段信通知，如果不是提交，则不能发送短信
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>发送消息的方法开始调用");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp2);
		// // logger.info("===========>发送消息的方法调用调用结束"+returnMsg);
		// // }
		//
		// // 更改或保存评标会
		// TdscBlockSelectApp appTemp3 = new TdscBlockSelectApp();
		// appTemp3.setAppId(appId);
		// appTemp3.setSelectedId(notaryId);
		// appTemp3.setSelectType("01");
		// appTemp3.setActivityDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
		// appTemp3.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("bidEvaLoc")));
		// appTemp3.setUnlockTime(nowTime);
		// appTemp3.setSelectUser(user.toString());
		// appTemp3.setSelectDate(now);
		// appTemp3.setIsValid("01");
		// appTemp3.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp3.setNodeStat("00");
		// appTemp3.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		// this.tdscSelectService.saveBlockSelectApp(appTemp3);
		// // A类公证处 进度安排表 提交后，需要向公证处发送段信通知，如果不是提交，则不能发送短信
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>发送消息的方法开始调用");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp3);
		// // logger.info("===========>发送消息的方法调用调用结束"+returnMsg);
		// // }
		//
		// }
		// // 根据出让方式保存公证处信息--拍卖
		// if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
		// // 根据回复截止时间
		// String replyTime = tdscSelectService.setReplyDeadLine();
		// Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		// // nowTime.setHours(07);
		// // nowTime.setMinutes(00);
		// // nowTime.setSeconds(00);
		// nowTime = tidy4AnyTimestamp(nowTime, 07, 0, 0);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		//
		//
		// // 更改或保存拍卖会
		// TdscBlockSelectApp appTemp2 = new TdscBlockSelectApp();
		// appTemp2.setAppId(appId);
		// appTemp2.setSelectedId(notaryId);
		// appTemp2.setSelectType("01");
		// appTemp2.setActivityDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
		// appTemp2.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("auctionLoc")));
		// appTemp2.setUnlockTime(nowTime);
		// appTemp2.setSelectUser(user.toString());
		// appTemp2.setSelectDate(now);
		// appTemp2.setIsValid("01");
		// appTemp2.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp2.setNodeStat("00");
		// appTemp2.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		// this.tdscSelectService.saveBlockSelectApp(appTemp2);
		// // A类公证处 进度安排表 提交后，需要向公证处发送段信通知，如果不是提交，则不能发送短信
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>发送消息的方法开始调用");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp2);
		// // logger.info("===========>发送消息的方法调用调用结束"+returnMsg);
		// // }
		// }
		// }

		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * 制定进度安排表日期管理 20080104*
	 */
	public ActionForward toScheduletableDateModify_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		// String statusId = request.getParameter("statusId");
		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 抽选功能
			// 通过selectType为01和appID查询相应的公证处信息
			List blockSelectList = tdscSelectService.findBlockSelectList("01", appId);
			if (null != blockSelectList && blockSelectList.size() > 0) {
				TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) blockSelectList.get(0);
				TdscNotaryInfo tdscNotaryInfo = tdscSelectService.queryNotaryInfoBynotaryId(tdscBlockSelectApp.getSelectedId());
				request.setAttribute("tdscNotaryInfo", tdscNotaryInfo);
			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// 判断TDSC_BLOCK_TRAN_APP表是否为空,取出MarginEndDate的值
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
			// 针对每行检查对应的NODE_ID , NODE_STAT
			Map statMap = new HashMap();
			statMap = commonFlowService.getAppNodeStatMap(appId);
			if (statMap.size() > 0) {
				request.setAttribute("statMap", statMap);
			}
		}
		request.setAttribute("appId", appId);
		// 获得查询条件
		TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		baseCondition.setAppId(appId);
		// 调用公共方法取得公共信息
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
		String mode = (String) commonInfo.getTransferMode();
		request.setAttribute("mode", mode);
		// 根据出让方式选择进入那个业务：DIC_TRANSFER_TENDER = "3107"招标 DIC_TRANSFER_AUCTION = "3103"拍卖 DIC_TRANSFER_LISTING = "3104"挂牌
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyGp");
		}
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyPm");
		}
		if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyZb");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * 以公告为单位 制定进度安排表日期管理
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toScheduletableDateModify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
		planTableCondition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);

		String mode = tdscBlockPlanTable.getTransferMode();

		TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
		tranAppCondition.setPlanId(planId);

		List appIdList = new ArrayList();
		List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
		if (tranAppList != null && tranAppList.size() > 0) {
			for (int i = 0; i < tranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tranAppList.get(i);
				appIdList.add(tdscBlockTranApp.getAppId());
			}
		}
		// 查询地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List tdscBlockAppViewList = null;
		String appId = "";
		if (appIdList != null && appIdList.size() > 0) {
			condition.setAppIdList(appIdList);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
				appId = tdscBlockAppView.getAppId();
				if (tdscBlockAppView.getMarginEndDate() != null) {
					tdscBlockTranApp.setMarginEndDate(tdscBlockAppView.getMarginEndDate());
				}
				if (mode == null || "".equals(mode)) {
					mode = tdscBlockAppView.getTransferMode();
				}
			}
		}
		Map statMap = new HashMap();
		statMap = commonFlowService.getAppNodeStatMap(appId);
		if (statMap.size() > 0) {
			request.setAttribute("statMap", statMap);
		}
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("tranAppList", tranAppList);
		request.setAttribute("mode", mode);
		request.setAttribute("transferMode", mode);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyPm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyZb");
		} else {
			return mapping.findForward("dateModifyGp");
		}
	}

	/**
	 * 制定进度安排表场次管理 20080104*
	 */
	public ActionForward toScheduletableFieldModify_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		// String statusId = request.getParameter("statusId");
		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 抽选功能
			List blockSelectList = tdscSelectService.findBlockSelectList("01", appId);
			if (null != blockSelectList && blockSelectList.size() > 0) {
				TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) blockSelectList.get(0);
				TdscNotaryInfo tdscNotaryInfo = tdscSelectService.queryNotaryInfoBynotaryId(tdscBlockSelectApp.getSelectedId());
				request.setAttribute("tdscNotaryInfo", tdscNotaryInfo);

			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// 判断TDSC_BLOCK_TRAN_APP表是否为空,取出MarginEndDate的值
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
			// 针对每行检查对应的NODE_ID , NODE_STAT
			Map statMap = new HashMap();
			statMap = commonFlowService.getAppNodeStatMap(appId);
			if (statMap.size() > 0) {
				request.setAttribute("statMap", statMap);
				// TdscAppNodeStat tdscAppNodeStat4 = (TdscAppNodeStat)statMap.get(FlowConstants.FLOW_NODE_FILE_RELEASE);
				// String nodeStat4 = (String)tdscAppNodeStat4.getNodeStat();
			}
		}
		request.setAttribute("appId", appId);
		// 获得查询条件
		TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		baseCondition.setAppId(appId);
		// 调用公共方法取得公共信息
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
		String mode = (String) commonInfo.getTransferMode();
		request.setAttribute("mode", mode);
		// 根据出让方式选择进入那个业务：DIC_TRANSFER_TENDER = "3107"招标 DIC_TRANSFER_AUCTION = "3103"拍卖 DIC_TRANSFER_LISTING = "3104"挂牌
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyGp");
		}
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyPm");
		}
		if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyZb");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * 以进度安排表为单位 制定进度安排表场次管理
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toScheduletableFieldModify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
		planTableCondition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);

		String mode = tdscBlockPlanTable.getTransferMode();

		TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
		tranAppCondition.setPlanId(planId);

		List appIdList = new ArrayList();
		List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
		if (tranAppList != null && tranAppList.size() > 0) {
			for (int i = 0; i < tranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tranAppList.get(i);
				appIdList.add(tdscBlockTranApp.getAppId());
			}
		}
		// 查询地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List tdscBlockAppViewList = null;
		String appId = "";
		if (appIdList != null && appIdList.size() > 0) {
			condition.setAppIdList(appIdList);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
				appId = tdscBlockAppView.getAppId();
				if (tdscBlockAppView.getMarginEndDate() != null) {
					tdscBlockTranApp.setMarginEndDate(tdscBlockAppView.getMarginEndDate());
				}
				if (mode == null || "".equals(mode)) {
					mode = tdscBlockAppView.getTransferMode();
				}
			}
		}
		// 针对每行检查对应的NODE_ID , NODE_STAT
		Map statMap = new HashMap();
		statMap = commonFlowService.getAppNodeStatMap(appId);
		if (statMap.size() > 0) {
			request.setAttribute("statMap", statMap);
		}
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("tranAppList", tranAppList);
		request.setAttribute("mode", mode);
		request.setAttribute("transferMode", mode);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyPm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyZb");
		} else {
			return mapping.findForward("fieldModifyGp");
		}

	}

	/**
	 * 修改进度安排表日期-招标 拍卖 挂牌
	 * 
	 * @param appId
	 *            and mode 20080107*
	 */
	public ActionForward modifyScheduletableDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String mode = request.getParameter("mode");
		// String saveType = request.getParameter("saveType");
		// 调用ActionForm
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// 将form中的值传入bo，对应出让地块进度安排表TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		tdscBlockPlanTable.setAppId(appId);
		bindObject(tdscBlockPlanTable, infoForm);
		// 根据出让方式选择保存相应的值：3107招标 3103拍卖 3104挂牌
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			tdscBlockPlanTable.setListStartDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setListEndDate(DateUtil.string2Timestamp(request.getParameter("listEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidDate(DateUtil.string2Timestamp(request.getParameter("sceBidDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidLoc(request.getParameter("sceBidLoc"));
		} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			tdscBlockPlanTable.setAuctionDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setAuctionLoc(request.getParameter("auctionLoc"));
		} else {
			tdscBlockPlanTable.setTenderStartDate(DateUtil.string2Timestamp(request.getParameter("tenderStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setTenderEndDate(DateUtil.string2Timestamp(request.getParameter("tenderEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningLoc(request.getParameter("openingLoc"));
			tdscBlockPlanTable.setBidEvaDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setBidEvaLoc(request.getParameter("bidEvaLoc"));
		}
		// 将“保证金到帐截止时间”保存到TDSC_BLOCK_TRAN_APP表中
		String marginEndDat = request.getParameter("marginEndDat");
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp.setMarginEndDate(DateUtil.string2Timestamp(marginEndDat, DateUtil.FORMAT_DATETIME));
		// 执行插入数据
		tdscScheduletableService.modifyPlanTableInfo(tdscBlockPlanTable, appId, mode, tdscBlockTranApp);
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * 修改进度安排表场次-招标 拍卖 挂牌
	 * 
	 * @param appId
	 *            and mode 20080107*
	 */
	public ActionForward modifyScheduletableField(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String mode = request.getParameter("mode");
		// 调用ActionForm
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// 将form中的值传入bo，对应出让地块进度安排表TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		tdscBlockPlanTable.setAppId(appId);
		bindObject(tdscBlockPlanTable, infoForm);
		// 根据出让方式选择保存相应的值：3107招标 3103拍卖 3104挂牌
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			tdscBlockPlanTable.setListStartDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setListEndDate(DateUtil.string2Timestamp(request.getParameter("listEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidDate(DateUtil.string2Timestamp(request.getParameter("sceBidDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidLoc(request.getParameter("sceBidLoc"));
			tdscBlockPlanTable.setListLoc(request.getParameter("listLoc"));
		} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			tdscBlockPlanTable.setAuctionDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setAuctionLoc(request.getParameter("auctionLoc"));
		} else {
			tdscBlockPlanTable.setTenderStartDate(DateUtil.string2Timestamp(request.getParameter("tenderStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setTenderEndDate(DateUtil.string2Timestamp(request.getParameter("tenderEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningLoc(request.getParameter("openingLoc"));
			tdscBlockPlanTable.setBidEvaDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setBidEvaLoc(request.getParameter("bidEvaLoc"));
		}
		// 将“保证金到帐截止时间”保存到TDSC_BLOCK_TRAN_APP表中
		String marginEndDat = request.getParameter("marginEndDat");
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp.setMarginEndDate(DateUtil.string2Timestamp(marginEndDat, DateUtil.FORMAT_DATETIME));
		// 执行插入数据
		tdscScheduletableService.modifyPlanTableInfo(tdscBlockPlanTable, appId, mode, tdscBlockTranApp);
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * 修改进度安排表（多个地块）
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyPlantable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String saveType = request.getParameter("saveType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		String planId = infoForm.getPlanId();
		// 查询原进度安排表信息
		TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
		condition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(condition);

		this.bindObject(tdscBlockPlanTable, infoForm);

		String sjLinkMan = request.getParameter("sjLinkman");
		tdscBlockPlanTable.setUserId(sjLinkMan);

		List tranAppList = (List) tdscScheduletableService.queryTranAppList(planId);
		tdscScheduletableService.savePlanTable(tdscBlockPlanTable, tranAppList, infoForm.getMarginEndDate(), saveType, user);

		request.setAttribute("forwardType", "01");
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * 修改进度安排表 LIST页面中“制定进度安排”按钮功能
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyPlantableInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		String statusFlow = request.getParameter("statusFlow");
		TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
		planTableCondition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);

		String mode = tdscBlockPlanTable.getTransferMode();

		TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
		tranAppCondition.setPlanId(planId);

		List appIdList = new ArrayList();
		List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
		if (tranAppList != null && tranAppList.size() > 0) {
			for (int i = 0; i < tranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tranAppList.get(i);
				appIdList.add(tdscBlockTranApp.getAppId());
			}
		}
		// 查询地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List tdscBlockAppViewList = null;
		String appId = "";
		if (appIdList != null && appIdList.size() > 0) {
			condition.setAppIdList(appIdList);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
				appId = tdscBlockAppView.getAppId();
				if (tdscBlockAppView.getMarginEndDate() != null) {
					tdscBlockTranApp.setMarginEndDate(tdscBlockAppView.getMarginEndDate());
				}
			}
		}
		Map statMap = new HashMap();
		statMap = commonFlowService.getAppNodeStatMap(appId);
		if (statMap.size() > 0) {
			request.setAttribute("statMap", statMap);
		}
		// 根据ORG_APP_ID查询协办机构
		if (tdscBlockPlanTable != null) {
			String orgAppId = tdscBlockPlanTable.getOrgAppId();
			TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
			request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
			// if(StringUtils.isNotEmpty(orgAppId)){
			// TdscXbOrgApp tdscXbOrgApp = (TdscXbOrgApp)tdscXbOrgService.getOrgAppInfoById(orgAppId);
			// if(tdscXbOrgApp != null){
			// TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
			// tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
			// tdscXbOrgHistory.setOrgAppId(orgAppId);
			// request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
			// }
			// }
		}
		request.setAttribute("statusFlow", statusFlow);
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("tranAppList", tranAppList);
		request.setAttribute("mode", mode);
		request.setAttribute("transferMode", mode);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("tempSavePm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("tempSaveZb");
		} else {
			return mapping.findForward("tempSaveGp");
		}
	}

	/**
	 * 20090626实施方案流程状态修改
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward sendBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		String forwardType = request.getParameter("forwardType");
		// 实施方案流程状态修改
		tdscScheduletableService.plantableSendBack(planId);

		request.setAttribute("forwardType", forwardType);
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}
	
	//校验数据库内是否包含相同限时竞价时段的方案
	public ActionForward checkListEndDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String listEndDate = request.getParameter("listEndDate");
		String planId= request.getParameter("planId");
		String result="00";//验证通过
		if(!tdscScheduletableService.checkListingEndDate(listEndDate,planId)){
			result = "11";//验证不通过
		}
		pw.write(result);
		pw.close();
		return null;
	}
	public ActionForward checkTradeNum(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String tradeNum = request.getParameter("tradeNum");
		String planId = request.getParameter("planId");
		String saveType = request.getParameter("saveType");
		String retString = "11";//表示不可用
		
		String listEndDate = request.getParameter("listEndDate");
		
		if (tdscScheduletableService.checkTradeNumAjax(tradeNum, planId)) {
			retString = "00";// 表示不可用
		}
		if(!tdscScheduletableService.checkListingEndDate(listEndDate,null)){
			retString = "22";
		}
		retString += "_" + saveType;
		pw.write(retString);
		pw.close();
		return null;
	}

	/**
	 * 打印挂牌页面
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward toPrintGp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");

		if (null != planId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}

		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
		}
		return mapping.findForward("printGp");
	}

	/**
	 * 导出挂牌页面
	 * 
	 * @param appId
	 *            lz 20090514*
	 */
	public ActionForward todaochuGp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");
		if (null != planId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}

		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
		}

		return mapping.findForward("todaochuGp");
	}

	/**
	 * 打印拍卖页面
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward toPrintPm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		// String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");
		if (null != planId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("printPm");
	}

	/**
	 * 导出拍卖页面
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward todaochuPm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		// String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");
		if (null != planId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("todaochuPm");
	}

	/**
	 * 打印招标页面
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward toPrintZb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("printZb");
	}

	/**
	 * 导出招标页面
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward todaochuZb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得页面参数appId
		String appId = request.getParameter("appId");
		if (null != appId) {
			// 获得查询条件
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// 调用公共方法取得公共信息
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// 判断出让地块进度安排表是否为空
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("todaochuZb");
	}

	// private Timestamp tidy4AnyTimestamp(Timestamp timestamp,int hh,int mm,int ss){
	// if(timestamp!=null&&timestamp.toString().length()>10){
	// String dateStr = timestamp.toString().substring(0,10)+" "+hh+":"+mm+":"+ss;
	// timestamp=DateUtil.string2Timestamp(dateStr, DateUtil.FORMAT_DATETIME);
	// }
	// return timestamp;
	// }

	/**
	 * 业务工作模块--终止交易查询列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return list_dq.jsp
	 * @throws Exception
	 */
	public ActionForward queryDqAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int currentPage = 0;
		if (!StringUtils.isEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// 返回给页面查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		ScheduletableInfoForm scheduletableInfoForm = (ScheduletableInfoForm) form;

		this.bindObject(condition, scheduletableInfoForm);
		condition.setBlockName(StringUtil.GBKtoISO88591(condition.getBlockName()));
		condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(condition.getBlockNoticeNo()));
		
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// 获得用户登陆的区县信息
		List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
		if (quList != null && quList.size() == 1) {
			condition.setDistrictId(String.valueOf(quList.get(0)));
		}
		// 按当前状态查询

		// 入室审核当前状态为待出让
		if ("00".equals(scheduletableInfoForm.getBlockAuditStatus())) {
			condition.setNodeId(null);
		} else {
			List statusList = new ArrayList();
			statusList.add("01");
			statusList.add("00");
			condition.setStatusList(statusList);
			List statusIdList = new ArrayList();
			statusIdList.add("9001");
			statusIdList.add("9002");
			condition.setStatusIdList2(statusIdList);
		}

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
		// 查询
		PageList pageList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);
		if (pageList != null && pageList.getList() != null && pageList.getList().size() > 0) {

			List tempList = pageList.getList();
			List blockList = new ArrayList();
			// tempList便利出多个交易地块
			if (tempList != null && tempList.size() > 0) {
				for (int i = 0; i < tempList.size(); i++) {
					TdscBlockAppView commonInfo = (TdscBlockAppView) tempList.get(i);
					// 一个地块得到结点列表blokcNodeList
					if (null != commonInfo) {
						if (null != commonInfo.getAppId()) {
							List blokcNodeList = commonFlowService.getAppDiagramActiveNodeList(commonInfo.getAppId());
							// 结点列表blokcNodeList整合成string字符串
							String nodeName = "";
							if (blokcNodeList != null && blokcNodeList.size() > 0) {
								for (int j = 0; j < blokcNodeList.size(); j++) {
									TdscAppNodeStat appStat = (TdscAppNodeStat) blokcNodeList.get(j);
									nodeName += appStat.getNodeName() + "，";
								}
							}
							if (!"".equals(nodeName) && nodeName.length() > 0) {
								nodeName = nodeName.substring(0, nodeName.length() - 1);
							}
							// string字符串塞到地块对象commonInfo的nodeId中
							commonInfo.setNodeId(nodeName);
						}
					}
					// 将改造的地块对象塞入blockList中
					blockList.add(commonInfo);
				}
			}

			pageList.setList(blockList);
		}
		// 入室审核当前状态为待出让
		if (StringUtils.isNotEmpty(condition.getBlockAuditStatus())) {
			condition.setNodeId("01");
		}
		// 查询条件中的当前节点
		String transferMode = condition.getTransferMode();
		if (StringUtils.isNotEmpty(transferMode)) {
			List nodeIdList = new ArrayList();
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)) {
				nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("01");
			}
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("02");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("03");
			}
			request.setAttribute("nodeIdList", nodeIdList);// 按条件查询列表
		}

		condition.setBlockName(StringUtil.ISO88591toGBK(condition.getBlockName()));
		//condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(condition.getBlockNoticeNo()));
		request.setAttribute("queryAppList", pageList);// 按条件查询列表
		request.setAttribute("queryAppCondition", condition);
		return mapping.findForward("endTradeAppList");
	}

	/**
	 * 终止某地块的交易
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endTradeAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = (String) request.getParameter("appId");
		// 操作用户
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String textOpen = (String) request.getParameter("textOpen");
		if (textOpen != null && !"".equals(textOpen)) {
			// textOpen = new String(textOpen.getBytes("ISO-8859-1"),"GBK");
		}

		// 构造查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 查询结果
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		// 纪录操作意见
		tdscBlockAppView.setTempStr(textOpen);
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// 终止交易
		String returnString = "00";// 操作失败
		if (tdscBlockAppView != null) {
			boolean actionResult = tdscScheduletableService.endTradeByAppId(tdscBlockAppView, user);
			if (actionResult) {
				returnString = "01";// 操作成功
			}
		}
		pw.write(returnString);
		pw.close();
		return null;
	}

	public ActionForward endTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = (String) request.getParameter("appId");
		// 操作用户
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String textOpen = (String) request.getParameter("textOpen");
		if (textOpen != null && !"".equals(textOpen)) {
			// textOpen = new String(textOpen.getBytes("ISO-8859-1"),"GBK");
		}

		// 构造查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// 查询结果
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		// 纪录操作意见
		tdscBlockAppView.setTempStr(textOpen);
		// 终止交易
		tdscScheduletableService.endTradeByAppId(tdscBlockAppView, user);
		ScheduletableInfoForm scheduletableInfoForm = new ScheduletableInfoForm();
		return queryDqAppList(mapping, scheduletableInfoForm, request, response);
	}

	/**
	 * 跳转至验证地块公告号的页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBlockNoticeNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接受页面传递的业务ID
		String appId = request.getParameter("appId");
		// 构造查询条件
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		return mapping.findForward("checkBlockNoticeNo");
	}

	/**
	 * 数据库方式 查询某年某月工作于假期
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBusinessDay_DB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// 获得查询的年、月
		String tempYear = (String) infoForm.getConditionYear();
		String tempMonth = (String) infoForm.getConditionMonth();
		TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();

		// 如果为空，则查询当月的工作日与假期
		X_BusinessDayService bDayService = new X_BusinessDayService();
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM");
		String tempDate = (String) sdm.format(d);

		if (tempYear == null || "".equals(tempYear)) {
			tempYear = tempDate.split("-")[0];
		}
		if (tempMonth == null || "".equals(tempMonth)) {
			tempMonth = tempDate.split("-")[1];
		}
		// 查询
		Map dateMap = (Map) bDayService.queryDayInfoByMonth(tempYear, tempMonth);
		condition.setConditionYear(tempYear);
		condition.setConditionMonth(tempMonth);

		request.setAttribute("condition", condition);
		request.setAttribute("dateMap", dateMap);

		return mapping.findForward("businessDayMap");
	}

	/**
	 * 数据库方式 重新初始化工作日与假期安排，恢复到初始状态
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward creatDateAgain_DB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		X_BusinessDayService bDayService = new X_BusinessDayService();

		if (bDayService.reinitBusinessDay()) {
			request.setAttribute("saveMessage", "重置成功!");
		} else {
			request.setAttribute("saveMessage", "重置失败!");
		}
		return queryBusinessDay_DB(mapping, form, request, response);
	}

	/**
	 * 该方法可以连续查询给定日期后的几个工作日 dateString格式为：yyyy-mm-dd,2,3,10,8,*
	 * 
	 * dateString后可以跟多个数字，来查询多个日期；
	 * 
	 * 返回的日期为（a,b,c,d） 其中 a = 给定日期 + 2个工作日； b = a + 3个工作日； c = b + 10个工作日； d = c + 8个工作日
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryDateByMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// dateString格式为：yyyy-mm-dd,2,3,10,8
		String dateString = (String) request.getParameter("dateString");
		String[] tempShuZu = dateString.split(",");
		// 将内容设置到输出
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		int count = 1;
		String firstDate = (String) tempShuZu[0];
		String returnStr = "";
		X_BusinessDayService bDayService = new X_BusinessDayService();
		if (null != tempShuZu) {
			while (count < tempShuZu.length) {
				firstDate = (String) bDayService.queryDate_S(firstDate, tempShuZu[count]);
				count++;
				returnStr += firstDate + ",";
			}
		}
		System.out.println("日期字符串===>" + returnStr);
		pw.write(returnStr);
		pw.close();
		return null;
	}

	public ActionForward modifyBusinessDay_DB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接页面整理的工作日、假期的详细安排
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;

		String businessStr = (String) infoForm.getBusinessStr();
		String holidayStr = (String) infoForm.getHolidayStr();
		// 构造工具类
		X_BusinessDayService bDayService = new X_BusinessDayService();
		// 整理工作日、假期数组
		String[] businessDay = businessStr.split(",");
		String[] holidayDay = holidayStr.split(",");
		// 修改工作日、假期安排
		bDayService.updateDateToHoliday(holidayDay);
		bDayService.updateDateToWorkDay(businessDay);
		return queryBusinessDay_DB(mapping, form, request, response);
	}

}
