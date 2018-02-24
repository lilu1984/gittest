package com.wonders.tdsc.kyq.action;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.wonders.tdsc.kyq.bo.KyqNotice;
import com.wonders.tdsc.kyq.bo.MiningBidderApp;
import com.wonders.tdsc.kyq.bo.MiningTranApp;
import com.wonders.tdsc.kyq.form.MiningTranAppForm;
import com.wonders.tdsc.kyq.service.KyqContent;
import com.wonders.tdsc.kyq.service.KyqNoticeService;
import com.wonders.tdsc.kyq.service.MiningBidderAppService;
import com.wonders.tdsc.kyq.service.MiningTranAppService;

public class MiningBidderAppAction extends BaseAction {
	private MiningBidderAppService miningBidderAppService;

	private MiningTranAppService miningTranAppService;

	private KyqNoticeService kyqNoticeService;

	public void setKyqNoticeService(KyqNoticeService kyqNoticeService) {
		this.kyqNoticeService = kyqNoticeService;
	}

	public void setMiningTranAppService(MiningTranAppService miningTranAppService) {
		this.miningTranAppService = miningTranAppService;
	}

	public MiningBidderAppService getMiningBidderAppService() {
		return miningBidderAppService;
	}

	public void setMiningBidderAppService(MiningBidderAppService miningBidderAppService) {
		this.miningBidderAppService = miningBidderAppService;
	}

	public ActionForward queryMiningBidderAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		KyqBaseQueryCondition condition = new KyqBaseQueryCondition();

		MiningTranAppForm miningTranAppForm = (MiningTranAppForm) form;

		this.bindObject(condition, miningTranAppForm);
		List tranStatusList = new ArrayList();
		tranStatusList.add(KyqContent.KYQ_STATUS_PUBLISHED_NOTICE);
		condition.setTranStatusList(tranStatusList);

		if (StringUtils.isNotBlank(condition.getNoticeNumber())) {
			condition.setNoticeNumber(StringUtil.GBKtoISO88591(condition.getNoticeNumber()));
		}

		List noticeList = kyqNoticeService.queryNoticeList(condition);

		HashMap map = new HashMap();
		if (noticeList != null && noticeList.size() > 0) {
			for (int i = 0; i < noticeList.size(); i++) {
				KyqNotice notice = (KyqNotice) noticeList.get(i);
				String miningNames = kyqNoticeService.appenMiningNames(notice.getNoticeId());
				map.put(notice.getNoticeId(), miningNames);
			}
		}

		request.setAttribute("condition", condition);
		request.setAttribute("map", map);
		request.setAttribute("noticeList", noticeList);

		return mapping.findForward("miningBidderAppList");
	}

	public ActionForward toBidderList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		List miningTranAppList = miningTranAppService.queryMiningTranAppListByNoticeId(noticeId);
		List miningBidderAppList = new ArrayList();
		if (miningTranAppList != null && miningTranAppList.size() > 0) {
			for (int i = 0; i < miningTranAppList.size(); i++) {
				MiningTranApp miningTranApp = (MiningTranApp) miningTranAppList.get(i);
				List tempList = miningBidderAppService.queryMiningBidderAppListByMiningId(miningTranApp.getTranAppId());
				if (tempList != null && tempList.size() > 0) {
					miningBidderAppList.addAll(tempList);
				}
			}
		}

		request.setAttribute("bidderList", miningBidderAppList);
		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("tobidderlist");
	}

	public ActionForward toBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = request.getParameter("bidderId");
		String noticeId = request.getParameter("noticeId");

		List bidderList = miningBidderAppService.queryminingBidderAppListByBidderId(bidderId);
		if (bidderList != null && bidderList.size() > 0) {
			MiningBidderApp miningBidderApp = (MiningBidderApp) bidderList.get(0);
			request.setAttribute("miningBidderApp", miningBidderApp);
		}

		List miningList = miningTranAppService.queryMiningTranAppListByNoticeId(noticeId);

		request.setAttribute("miningList", miningList);
		request.setAttribute("bidderList", bidderList);
		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("tobidder");

	}

	public ActionForward saveBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MiningBidderApp miningBidderApp = new MiningBidderApp();
		MiningTranAppForm tranAppForm = (MiningTranAppForm) form;
		this.bindObject(miningBidderApp, tranAppForm);

		String[] tranAppIds = request.getParameterValues("appIds");

		miningBidderApp = miningBidderAppService.saveBidderApp(miningBidderApp, tranAppIds);
		if(StringUtils.isNotBlank(miningBidderApp.getPurposeAppId())){
			miningBidderAppService.insertListingAppOfPurposePerson(miningBidderApp, miningBidderApp.getPurposeAppId());
		}

		return new ActionForward("miningBidderApp.do?method=queryMiningBidderAppList", true);
	}

	// ¸ù¾ÝbidderIdÉ¾³ý
	public ActionForward delBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = request.getParameter("bidderId");
		miningBidderAppService.delByBidderId(bidderId);
		return new ActionForward("miningBidderApp.do?method=toBidderList", true);
	}
}
