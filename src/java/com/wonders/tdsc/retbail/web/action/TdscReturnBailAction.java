package com.wonders.tdsc.retbail.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscReturnBail;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.retbail.service.TdscReturnBailService;
import com.wonders.tdsc.retbail.web.form.TdscReturnBailForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.tdscbase.service.TdscBidderViewService;

public class TdscReturnBailAction extends BaseAction {

	private TdscReturnBailService	tdscReturnBailService;

	private CommonQueryService		commonQueryService;

	private TdscBidderViewService	tdscBidderViewService;

	private TdscBidderAppService	tdscBidderAppService;

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setTdscBidderViewService(TdscBidderViewService tdscBidderViewService) {
		this.tdscBidderViewService = tdscBidderViewService;
	}

	public void setTdscReturnBailService(TdscReturnBailService tdscReturnBailService) {
		this.tdscReturnBailService = tdscReturnBailService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public ActionForward returnBail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		TdscReturnBailForm retForm = (TdscReturnBailForm) form;

		tdscReturnBailService.saveTdscReturnBail(retForm, user);

		List retList = new ArrayList();

		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setBlockName(retForm.getBlockName());
		cond.setBlockNoticeNo(retForm.getBlockNoticeNo());
		request.setAttribute("formCond", retForm);
		retList = queryBjzBeanList(retList, cond);
		request.setAttribute("returnResult", retList);

		return mapping.findForward("toReturnBailListPage");
	}

	public ActionForward queryReturnBailList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List retList = new ArrayList();

		TdscReturnBailForm tdscBailForm = (TdscReturnBailForm) form;

		if (StringUtils.isEmpty(tdscBailForm.getBlockName()) && StringUtils.isEmpty(tdscBailForm.getBlockNoticeNo())) {
			return mapping.findForward("toReturnBailListPage");
		}

		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setBlockName(tdscBailForm.getBlockName());
		cond.setBlockNoticeNo(tdscBailForm.getBlockNoticeNo());
		request.setAttribute("formCond", tdscBailForm);

		retList = queryBjzBeanList(retList, cond);

		request.setAttribute("returnResult", retList);

		return mapping.findForward("toReturnBailListPage");
	}

	private List queryBjzBeanList(List retList, TdscBaseQueryCondition cond) {
		List blockList = commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);

		if (blockList != null && blockList.size() > 0) {
			for (int i = 0; i < blockList.size(); i++) {
				TdscBlockAppView blockApp = (TdscBlockAppView) blockList.get(i);
				// String planId = blockApp.getPlanId();
				// ʹ�� appId��ѯ������صľ�����
				List bidders = tdscBidderViewService.queryBiddersByAppId(blockApp.getAppId());
				if (bidders != null && bidders.size() > 0) {
					for (int j = 0; j < bidders.size(); j++) {
						TdscBidderView bidder = (TdscBidderView) bidders.get(j);

						TdscReturnBailForm retBean = new TdscReturnBailForm();
						retBean.setPlanId(blockApp.getPlanId());
						retBean.setBlockId(blockApp.getBlockId());
						retBean.setAppId(blockApp.getAppId());
						retBean.setNoticeNo(blockApp.getNoitceNo());
						retBean.setBlockName(blockApp.getBlockName());

						retBean.setBidderId(bidder.getBidderId());
						retBean.setBidderName(bidder.getBidderName());

						// ����Ǿ����ˣ������˻���֤��
						// ���� plan_id bidder_name���ұ�TDSC_BLOCK_TRAN_APP, ����м�¼�����ʾ�����Ǿ����ˣ�������
						TdscBlockTranApp tranApp = tdscBidderAppService.getTdscBlockTranAppById(blockApp.getAppId());
						if (tranApp != null && !StringUtils.isEmpty(tranApp.getResultName())) {

							retBean.setBidderBail(tranApp.getMarginAmount());

							String tranBidderName = tranApp.getResultName().trim();

							if (tranBidderName.equals(bidder.getBidderName())) {
								// �����Ǿ�����
								retBean.setCanReturn("false");
							} else {
								retBean.setCanReturn("true");
							}
						}

						// ��ѯ��TDSC_RETURN_BAIL������ bidder_id��������ڣ����ʾ�Ѿ��˹���֤��
						TdscReturnBail retBail = tdscReturnBailService.getTdscReturnBailByAppIdBidderId(blockApp.getAppId(), bidder.getBidderId());
						if (retBail != null) {
							if ("01".equals(retBail.getIfReturn())) {
								retBean.setRetStatus("���˻�");
							} else {
								if ("false".equals(retBean.getCanReturn())) {
									retBean.setRetStatus("���õ�λ");
								} else {
									retBean.setRetStatus("δ�˻�");
								}
							}
							retBean.setBailId(retBail.getBailId());
						}
						retList.add(retBean);
					}
				}

			}
		}
		return retList;
	}
}
