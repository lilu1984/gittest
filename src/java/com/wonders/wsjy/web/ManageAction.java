package com.wonders.wsjy.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.engine.bo.TradeNotice;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.service.TradeServer;

public class ManageAction extends BaseAction {

	private TradeServer tradeServer;

	public TradeServer getTradeServer() {
		return tradeServer;
	}

	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	/**
	 * ����������ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward topauselist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List tradePlanTableList = tradeServer.findTradeNotice();
		List noticeList = new ArrayList();
		if (tradePlanTableList != null && tradePlanTableList.size() > 0) {
			for (int i = 0; i < tradePlanTableList.size(); i++) {
				TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tradePlanTableList.get(i);
				TradeNotice tradeNotice = new TradeNotice();
				if(System.currentTimeMillis()<tdscBlockPlanTable.getListStartDate().getTime()){
					tradeNotice.setSurplusTime(-1);
				}else if(System.currentTimeMillis()>tdscBlockPlanTable.getListEndDate().getTime()){
					tradeNotice.setSurplusTime(1);
				}else{ 
					tradeNotice.setSurplusTime(0);
				}
				// ����planId
				tradeNotice.setPlanId(tdscBlockPlanTable.getPlanId());
				// ��ӵؿ���Ϣ
				List tradeBlockList = tradeServer.findBlockTranAppList(tdscBlockPlanTable.getPlanId());
				if (tradeBlockList != null && tradeBlockList.size() > 0) {
					TdscBlockTranApp temptdscBlockTranApp = (TdscBlockTranApp) tradeBlockList.get(0);
					if (!isNoticePublic(temptdscBlockTranApp)) {
						break;
					} else {
						tradeNotice.setNoticeId(temptdscBlockTranApp.getNoticeId());
						tradeNotice.setNoticeNo(temptdscBlockTranApp.getNoitceNo());
						// ��ӽ��׹�����ӹ�����Ϣ
						noticeList.add(tradeNotice);
					}
				}
			}
		}

		//���÷�����Ϣ
		request.setAttribute("noticeList", noticeList);
		return mapping.findForward("pauselist");
	}
	
	private boolean isNoticePublic(TdscBlockTranApp temptdscBlockTranApp) {
		if (StringUtils.isNotBlank(temptdscBlockTranApp.getNoticeId())) {
			TdscNoticeApp app = tradeServer.getBlockNoticeAppById(temptdscBlockTranApp.getNoticeId());
			if ("1".equals(app.getIfReleased())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ����ؿ����б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward topauseblock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		if(!filterParam(appId)){//�жϲ����Ƿ�Ϸ�,���Ϸ�ת����½ҳ��
			System.out.println("appId�������Ϸ���value="+appId);
			return mapping.findForward("tdscCaLogin");
		}
		// ȡ�ù�����Ϣ
		List lisingInfoList = this.tradeServer.getListingAppListByAppId(appId, null);
		//��ȡ���������Ϣ
		TdscTradeView tdscTradeView = this.tradeServer.getTdscTradeViewAppById(appId);

		//���÷�����Ϣ
		request.setAttribute("lisingInfoList", lisingInfoList);
		request.setAttribute("tdscTradeView", tdscTradeView);
		return mapping.findForward("pauseblock");
	}
}
