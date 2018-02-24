package com.wonders.tdsc.blockwork.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;

public class TradeFinishAction  extends BaseAction{
	// 土地基本信息service
	private TdscBlockInfoService	tdscBlockInfoService;
	
	private TdscBidderAppService	tdscBidderAppService;
	
	
	public TdscBlockInfoService getTdscBlockInfoService() {
		return tdscBlockInfoService;
	}


	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}


	public TdscBidderAppService getTdscBidderAppService() {
		return tdscBidderAppService;
	}


	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}


	/**
	 * 交易结束后的地块列表
	 */
	public ActionForward toTradeFinishList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取tdscBlockTranApp对象的集合
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();

		PageList blockList= this.tdscBlockInfoService.findTradeEndList(pageSize,currentPage);
		
		request.setAttribute("blockList", blockList);
		return mapping.findForward("finishList");
	}
	/**
	 * 竞买人列表
	 */
	public ActionForward toBidderList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		List bidderList = this.tdscBidderAppService.findHaveCertNoBidderList(appId);
		request.setAttribute("bidderList", bidderList);
		return mapping.findForward("bidderList");
	}
}
