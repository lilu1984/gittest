package com.wonders.tdsc.blockwork.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscBlockPresell;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.presell.web.form.TdscBlockPresellForm;
import com.wonders.wsjy.wxtrade.WxDbService;

public class BlockMarkupAction extends BaseAction{
	// 土地基本信息service
	private TdscBlockInfoService	tdscBlockInfoService;

	public TdscBlockInfoService getTdscBlockInfoService() {
		return tdscBlockInfoService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}
	
	/**
	 * 在挂牌截至时间之前的所有地块
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward resultList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List list = this.tdscBlockInfoService.findMarkupDiJiaList();
		List selectDijiaList = this.tdscBlockInfoService.findSelectingDijiaList();
		request.setAttribute("list", list);
		request.setAttribute("selectDijiaList", selectDijiaList);
		return mapping.findForward("block-dijia-list");
	}
	/**
	 * 编辑底价
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward editDiJia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBlockTranApp tranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		TdscListingInfo listingInfo = this. tdscBlockInfoService.findListingInfo(appId);
		
		if(tranApp==null){
			tranApp = new TdscBlockTranApp();
		}
		request.setAttribute("listingInfo", listingInfo);
		request.setAttribute("tranApp", tranApp);
		return mapping.findForward("block-dijia-edit");
	}
	/**
	 * save底价
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveDiJia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String dijia = request.getParameter("dijia");
		TdscBlockTranApp tranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		if(tranApp!=null){
			tranApp.setDiJia(new BigDecimal(dijia));
			request.setAttribute("saveFlag", "true");
			this.tdscBlockInfoService.saveTdscBlockTranApp(tranApp);
		}else{
			request.setAttribute("saveFlag", "false");
		}
		
		request.setAttribute("tranApp", tranApp);
		return mapping.findForward("block-dijia-edit");
	}
	/**
	 * 回收地块
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward huishou(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBlockTranApp tranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		if(tranApp!=null){
			if("03".equals(tranApp.getTranResult())){
				WxDbService wxDbService = new WxDbService();
				TradeBlock tradeBlock = new TradeBlock();
				tradeBlock.setAppId(appId);
				tradeBlock.setTradeResult("0");
				wxDbService.finishBlockTrade(tradeBlock, false);
			}
		}
		return new ActionForward("makeup.do?method=resultList",true);
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBlockInfoCondition condition = new TdscBlockInfoCondition();
		// 获取页面参数
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		String blockName = request.getParameter("blockName");
		if(StringUtils.isNotEmpty(blockName)){
			condition.setBlockName(blockName);
		}
		condition.setCurrentPage(currentPage);
		condition.setPageSize(10);
		PageList pageList = this.tdscBlockInfoService.findPageList(condition);
		request.setAttribute("pageList", pageList);
		return mapping.findForward("block-list");
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockId = request.getParameter("blockId");
		String zhouBian = "";
		
		if (StringUtils.isNotBlank(blockId)) {
			TdscBlockTranApp tranApp = tdscBlockInfoService.getBlockTranAppByBlockId(blockId);
			List fileList = tdscBlockInfoService.findFileRefByBusId(blockId, GlobalConstants.BLOCK_FILE);
			if(tranApp!=null){
				zhouBian = tranApp.getTextMemo();
			}
			request.setAttribute("fileList", fileList);
		}
		request.setAttribute("blockId", blockId);
		request.setAttribute("zhoubian", zhouBian);

		return mapping.findForward("block-edit");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockId = request.getParameter("blockId");
		String zhoubian = request.getParameter("zhoubian");
		TdscBlockPresellForm tdscBlockPresellForm = (TdscBlockPresellForm) form;
		String[] fileNameList = tdscBlockPresellForm.getFileName();
		MultipartRequestHandler multipartRequestHandler = form.getMultipartRequestHandler(); 
		// 取得所有上传文件的对象集合 
		Hashtable elements = multipartRequestHandler.getFileElements(); 
		Object[] fileList = new Object[elements.size()];
		// 循环遍历每一个文件 
		for (java.util.Enumeration e = elements.keys(); e.hasMoreElements();){
			String key = (String) e.nextElement();
			int index;
			String strIndex = key.replaceAll("formFileList", "");
			index = Integer.parseInt(strIndex);
			FormFile file = (FormFile)elements.get(key);
			fileList[index]=file;
		}
		tdscBlockInfoService.saveMakeupData(blockId, zhoubian, fileList, fileNameList);
		return new ActionForward("makeup.do?method=edit&blockId=" + blockId, true);
	}
	/**
	 * 删除文件信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileId = request.getParameter("fileId");
		String blockId = request.getParameter("blockId");
		tdscBlockInfoService.delFileRefById(fileId);
		return new ActionForward("makeup.do?method=edit&blockId=" + blockId, true);
	}
	
}
