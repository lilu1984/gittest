package com.wonders.tdsc.bidder.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.service.TdscReturnBzjService;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;

public class TdscReturnBzjAction extends BaseAction {

	private TdscReturnBzjService	tdscReturnBzjService;

	public void setTdscReturnBzjService(TdscReturnBzjService tdscReturnBzjService) {
		this.tdscReturnBzjService = tdscReturnBzjService;
	}

	/**
	 * ����������þ��������б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ǰҳ
		String pageNo = request.getParameter("currentPage");
		// ȡ���Ѿ������ķ���
		List planList = this.tdscReturnBzjService.queryPlanOfEnd();
		// ���û�з��ؿ�
		if (planList == null || planList.size() == 0)
			return mapping.findForward("fundBlockList");
		// ����У����Ҷ�Ӧ�ķ����б�
		List planTableList = this.tdscReturnBzjService.queryBlockPlanTableByPlanIdList(planList);
		// �����ҳ
		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false) {
			currentPage = Integer.parseInt(pageNo);
		}
		if (pageNo == null || Integer.parseInt(pageNo) < 1) {
			currentPage = 1;
		}
		pageList = PageUtil.getPageList(planTableList, pageSize, currentPage);
		request.setAttribute("pageList", pageList);
		return mapping.findForward("fundBlockList");
	}

	public ActionForward returnBzjByPlanId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		request.setAttribute("planId", planId);
		// 1. ����planId ȡ��noticeId
		String noticeId = this.tdscReturnBzjService.queryNoticeIdByPlanId(planId);
		request.setAttribute("noticeId", noticeId);
		// 2. ����noticeId ȡ��bidderApp�б�
		Map map = this.tdscReturnBzjService.queryReturnBzjList(noticeId);
		request.setAttribute("list", map);
		return mapping.findForward("resultList");
	}

	public ActionForward clickReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String yktBh = request.getParameter("yktBh");
		String noticeId = request.getParameter("noticeId");
		String planId = request.getParameter("planId");
		this.tdscReturnBzjService.updateBidderListConvertByYktNoticeId(yktBh, noticeId);
		return new ActionForward("returnBzj.do?method=returnBzjByPlanId&planId=" + StringUtils.trimToEmpty(planId), true);
	}
}
