package com.wonders.wsjy.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class GetFile  extends DispatchAction{
	
	private CommonQueryService commonQueryService;
	
	private TdscNoticeService tdscNoticeService;
	private TdscBlockInfoService tdscBlockInfoService;
	
	
	public TdscBlockInfoService getTdscBlockInfoService() {
		return tdscBlockInfoService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	/**
	 * 取得可以下载出让文件的地块列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String currPageStr = request.getParameter("currentPage");
		String noticeNo = request.getParameter("noticeNo");
		String blockNoticeNo = request.getParameter("blockNoticeNo");
		String blockName = request.getParameter("blockName");
		int currPage = 1;
		if (StringUtils.isNotEmpty(currPageStr)) {
			currPage = Integer.parseInt(currPageStr);
		}
		List noticeId = tdscNoticeService.queryNoticeIdListInTrade();
		PageList list = commonQueryService.queryTdscBlockAppInNoticeId(noticeId, currPage, noticeNo, blockNoticeNo, blockName);
		request.setAttribute("noticeList", list);
		return mapping.findForward("toFileDownList");
	}
	/**
	 * 打开地块对应的出让文件和附件列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fileList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String blockId = request.getParameter("blockId");
		if (StringUtils.isNotBlank(blockId)) {
			List fileList = tdscBlockInfoService.findFileRefByBusId(blockId, GlobalConstants.BLOCK_FILE);
			request.setAttribute("fileList", fileList);
		}
		request.setAttribute("appId", appId);
		return mapping.findForward("toBlockFileDownList");
	}
}
