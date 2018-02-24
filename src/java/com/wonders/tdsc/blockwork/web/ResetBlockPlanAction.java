package com.wonders.tdsc.blockwork.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.tdsc.blockwork.service.ResetBlockPlanService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class ResetBlockPlanAction extends BaseAction {

	private CommonQueryService		commonQueryService;

	private ResetBlockPlanService	resetBlockPlanService;

	public void setResetBlockPlanService(ResetBlockPlanService resetBlockPlanService) {
		this.resetBlockPlanService = resetBlockPlanService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	/**
	 * 显示重新制定方案
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toResetPlanFun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 根据planId 取得该招拍挂活动对应的地块信息
		String planId = request.getParameter("planId");
		request.setAttribute("planId", planId);
		// 根据 招拍挂 活动的主键取得 地块列表
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		List list = this.commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		// 判断该招拍挂活动是否处于 待制作出让文件 和待制作出让公告环节
		boolean isMakeFile = isMakeFileValid(list);
		if (isMakeFile) {
			// 如果处于 这两个状态 则可以重新安排招拍挂活动的地块信息
			// 1. 查找该招拍挂活动对应的地块信息
			List planBlockList = list;
			// 2. 查找处于制作进度安排表的地块列表信息
			ArrayList nodeList = new ArrayList();
			nodeList.add(FlowConstants.FLOW_NODE_SCHEDULE_PLAN);
			condition = new TdscBaseQueryCondition();
			condition.setNodeList(nodeList);
			List toMakePlanBlockList = this.commonQueryService.queryTdscBlockAppViewList(condition);
			List formatList = new ArrayList();
			for (int i = 0; toMakePlanBlockList != null && i < toMakePlanBlockList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) toMakePlanBlockList.get(i);
				if (StringUtils.isEmpty(tdscBlockAppView.getPlanId())) {
					formatList.add(tdscBlockAppView);
				}
			}
			// 返回页面
			request.setAttribute("makedBlockList", planBlockList);
			request.setAttribute("makingBlockList", formatList);
		} else {
			// 否则的话，通知制作人不可以制作
			request.setAttribute("message", "该招拍挂活动不处于调整阶段！");
		}
		return mapping.findForward("showPlanReset");
	}

	/**
	 * 从当前方案当中移除该地块
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeBlockFromPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		resetBlockPlanService.sendBack(tdscBlockAppView, user, "02", "0201", tdscBlockAppView.getPlanId());
		return new ActionForward("resetPlan.do?method=toResetPlanFun&planId=" + StringUtils.trimToEmpty(planId), true);
	}

	public ActionForward submitBlockPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String[] appIds = request.getParameterValues("appId");
		String planId = request.getParameter("planId");
		if (StringUtils.isNotEmpty(planId) && appIds != null)
			resetBlockPlanService.submitBlockPlan(appIds, planId, user);
		return new ActionForward("resetPlan.do?method=toResetPlanFun&planId=" + StringUtils.trimToEmpty(planId), true);
	}

	/**
	 * 根据planId 判断是否该招拍挂活动中的所有地块 处于 “待制作出让公告” 和 “待制作出让文件”环节
	 * 
	 * @param planId
	 *            招拍挂活动编号
	 * @return 是否
	 */
	private boolean isMakeFileValid(List list) {
		if (null == list || list.size() == 0)
			return false;
		// 判断是否都处于制作出让公告阶段
		boolean flag = true;
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBlockAppView appView = (TdscBlockAppView) list.get(i);
			// 取得节点编号
			String nodeId = appView.getStatusId();
			// 是否处于 制作文件 和 制作公告
			if (!FlowConstants.FLOW_STATUS_FILE_MAKE.equals(nodeId) && !FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(nodeId)
					&& !FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(nodeId) && !FlowConstants.FLOW_STATUS_NOTICE_MODIFY.equals(nodeId)) {
				flag = false;
				break;
			}
		}
		return flag;
	}
}
