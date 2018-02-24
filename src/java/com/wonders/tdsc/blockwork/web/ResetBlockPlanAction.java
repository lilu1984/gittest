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
	 * ��ʾ�����ƶ�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toResetPlanFun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// ����planId ȡ�ø����Ĺһ��Ӧ�ĵؿ���Ϣ
		String planId = request.getParameter("planId");
		request.setAttribute("planId", planId);
		// ���� ���Ĺ� �������ȡ�� �ؿ��б�
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		List list = this.commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		// �жϸ����Ĺһ�Ƿ��� �����������ļ� �ʹ��������ù��滷��
		boolean isMakeFile = isMakeFileValid(list);
		if (isMakeFile) {
			// ������� ������״̬ ��������°������Ĺһ�ĵؿ���Ϣ
			// 1. ���Ҹ����Ĺһ��Ӧ�ĵؿ���Ϣ
			List planBlockList = list;
			// 2. ���Ҵ����������Ȱ��ű�ĵؿ��б���Ϣ
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
			// ����ҳ��
			request.setAttribute("makedBlockList", planBlockList);
			request.setAttribute("makingBlockList", formatList);
		} else {
			// ����Ļ���֪ͨ�����˲���������
			request.setAttribute("message", "�����Ĺһ�����ڵ����׶Σ�");
		}
		return mapping.findForward("showPlanReset");
	}

	/**
	 * �ӵ�ǰ���������Ƴ��õؿ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeBlockFromPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����û���Ϣ
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
		// ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String[] appIds = request.getParameterValues("appId");
		String planId = request.getParameter("planId");
		if (StringUtils.isNotEmpty(planId) && appIds != null)
			resetBlockPlanService.submitBlockPlan(appIds, planId, user);
		return new ActionForward("resetPlan.do?method=toResetPlanFun&planId=" + StringUtils.trimToEmpty(planId), true);
	}

	/**
	 * ����planId �ж��Ƿ�����Ĺһ�е����еؿ� ���� �����������ù��桱 �� �������������ļ�������
	 * 
	 * @param planId
	 *            ���Ĺһ���
	 * @return �Ƿ�
	 */
	private boolean isMakeFileValid(List list) {
		if (null == list || list.size() == 0)
			return false;
		// �ж��Ƿ񶼴����������ù���׶�
		boolean flag = true;
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBlockAppView appView = (TdscBlockAppView) list.get(i);
			// ȡ�ýڵ���
			String nodeId = appView.getStatusId();
			// �Ƿ��� �����ļ� �� ��������
			if (!FlowConstants.FLOW_STATUS_FILE_MAKE.equals(nodeId) && !FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(nodeId)
					&& !FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(nodeId) && !FlowConstants.FLOW_STATUS_NOTICE_MODIFY.equals(nodeId)) {
				flag = false;
				break;
			}
		}
		return flag;
	}
}
