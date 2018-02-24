package com.wonders.tdsc.kyq.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.MiningBidderApp;
import com.wonders.tdsc.kyq.bo.MiningTranApp;
import com.wonders.tdsc.kyq.form.MiningTranAppForm;
import com.wonders.tdsc.kyq.service.MiningTranAppService;

public class MiningTranAppAction extends BaseAction {
	private MiningTranAppService	miningTranAppService;

	public void setMiningTranAppService(MiningTranAppService miningTranAppService) {
		this.miningTranAppService = miningTranAppService;
	}

	public ActionForward queryMiningList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();
		condition.setTranStatus("0");
		String tmpName = request.getParameter("miningName");
		String flag = request.getParameter("flag");
		if (StringUtils.isNotBlank(flag) && "back".equals(flag)) {
			List miningList = miningTranAppService.queryMiningTranAppList(condition);
			request.setAttribute("condition", condition);
			request.setAttribute("miningList", miningList);
			return mapping.findForward("miningList");
		}
		if (StringUtils.isNotBlank(tmpName)) {
			condition.setMiningName(StringUtil.GBKtoISO88591(tmpName));
		}
		List miningList = miningTranAppService.queryMiningTranAppList(condition);
		request.setAttribute("condition", condition);
		request.setAttribute("miningList", miningList);
		return mapping.findForward("miningList");
	}

	public ActionForward toMining(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String tranAppId = request.getParameter("tranAppId");

		MiningTranApp miningTranApp = miningTranAppService.getMiningTranAppByTranAppId(tranAppId);

		MiningBidderApp miningBidderApp = miningTranAppService.getMiningBidderAppByMiningId(tranAppId);

		request.setAttribute("miningTranApp", miningTranApp);

		request.setAttribute("miningBidderApp", miningBidderApp);

		return mapping.findForward("toMining");
	}

	public ActionForward saveMining(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		MiningTranApp miningTranApp = new MiningTranApp();

		MiningBidderApp miningBidderApp = new MiningBidderApp();

		MiningTranAppForm tranAppForm = (MiningTranAppForm) form;

		this.bindObject(miningTranApp, tranAppForm);

		this.bindObject(miningBidderApp, tranAppForm);

		miningTranAppService.saveMiningTran(miningTranApp, miningBidderApp);

		return new ActionForward("miningTranApp.do?method=queryMiningList", true);
	}

	public ActionForward deleteMining(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tranAppId = request.getParameter("tranAppId");

		miningTranAppService.deleteMiningTran(tranAppId);

		return new ActionForward("miningTranApp.do?method=queryMiningList", true);
	}
}
