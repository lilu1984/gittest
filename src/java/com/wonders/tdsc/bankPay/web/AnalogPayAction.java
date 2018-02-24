package com.wonders.tdsc.bankPay.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.wsjy.service.SubscribeService;
import com.wonders.wsjy.service.TradeServer;


public class AnalogPayAction extends BaseAction{
	private SubscribeService subscribeService;
	private TradeServer tradeServer;

	public TradeServer getTradeServer() {
		return tradeServer;
	}

	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	public SubscribeService getSubscribeService() {
		return subscribeService;
	}

	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}
	/**
	 * 查看地块列表
	 * 返回页面 block_list.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return block_list.jsp
	 * @throws Exception
	 */
	public ActionForward viewBlockList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取tdscBlockTranApp对象的集合
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();

		PageList blockList= subscribeService.findBlockList(pageSize,currentPage);
		
		request.setAttribute("blockList", blockList);
		return mapping.findForward("block_list");

	}
	/**
	 * 查看竞买人列表
	 * 返回页面 bidder_list.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return bidder_list.jsp
	 * @throws Exception
	 */
	public ActionForward viewBidderList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取tdscBidderView对象的集合
		String appId =request.getParameter("appId");
		List list = subscribeService.findBidderList(appId);
		request.setAttribute("list", list);
		return mapping.findForward("bidder_list");

	}

}
