package com.wonders.tdsc.cardlogin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class PrintCompetenceAction extends BaseAction {
	private CommonQueryService commonQueryService;
	
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	/**
	 * 打印竞买资格证书/未入围资格证书
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintCompetenceSuccess(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		String appId = request.getParameter("appId");
		String forwardType=request.getParameter("forwardType");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		if("1".equals(forwardType)) return mapping.findForward("toPrintCompetenceSuccess");
		else return mapping.findForward("toPrintCompetenceFailed");
	}
	

}
