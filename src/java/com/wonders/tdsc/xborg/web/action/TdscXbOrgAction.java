package com.wonders.tdsc.xborg.web.action;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscXbOrgApp;
import com.wonders.tdsc.bo.TdscXbOrgHistory;
import com.wonders.tdsc.bo.TdscXbOrgInfo;
import com.wonders.tdsc.bo.condition.TdscXbOrgAppCondition;
import com.wonders.tdsc.bo.condition.TdscXbOrgHistoryCondition;
import com.wonders.tdsc.bo.condition.TdscXbOrgInfoCondition;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.xborg.service.TdscXbOrgService;

public class TdscXbOrgAction extends BaseAction{

	private TdscXbOrgService tdscXbOrgService;
	
	private  TdscScheduletableService tdscScheduletableService;

	public void setTdscXbOrgService(TdscXbOrgService tdscXbOrgService) {
		this.tdscXbOrgService = tdscXbOrgService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}
	/**
	 * 查找机构分页列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryPageListOfOrgApp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		TdscXbOrgAppCondition condition = new TdscXbOrgAppCondition();
		String currentPage = request.getParameter("currentPage");
		bindObject(condition, form);
		if (StringUtils.isNotEmpty(currentPage)) {
			condition.setCurrentPage(Integer.parseInt(currentPage));
		}
		PageList pageList = this.tdscXbOrgService.queryOrgAppPageListByCondition(condition);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("orgAppList");
	}
	
	/**
	 * 根据orgApp的编号取得对应的信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getOrgAppInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String orgAppId = request.getParameter("orgAppId");
		String currentPage = request.getParameter("currentPage");
		TdscXbOrgApp tdscXbOrgApp = this.tdscXbOrgService.getOrgAppInfoById(orgAppId);
		request.setAttribute("tdscXbOrgApp", tdscXbOrgApp);
		request.setAttribute("currentPage", currentPage);
		return mapping.findForward("orgAppInfo");
	}
	
	/**
	 * 保存更新orgapp信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateOrgAppInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String currentPage = request.getParameter("currentPage");
		TdscXbOrgApp tdscXbOrgApp = new TdscXbOrgApp();
		bindObject(tdscXbOrgApp, form);
		try {	
			tdscXbOrgApp = this.tdscXbOrgService.updateOrgApp(tdscXbOrgApp);
        } catch (RuntimeException e) {
        	tdscXbOrgApp.setOrgName(null);
        	request.setAttribute("tdscXbOrgApp", tdscXbOrgApp);
        	request.setAttribute("errorMsg", e.getMessage());
			return mapping.findForward("orgAppInfo");
		}
		return new ActionForward("orgApp.do?method=getOrgAppInfo&orgAppId=" + tdscXbOrgApp.getOrgAppId() + "&currentPage=" + currentPage, true);
	}
	
	/**
	 * 删除
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delOrgAppInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String orgAppId = request.getParameter("orgAppId");
		String currentPage = request.getParameter("currentPage");
		this.tdscXbOrgService.delOrgAppInfoById(orgAppId);
		return new ActionForward("orgApp.do?method=queryPageListOfOrgApp&orgAppId=" + StringUtils.trimToEmpty(orgAppId) + "&currentPage=" + StringUtils.trimToEmpty(currentPage), true);
	}
	

	/**
	 * 查找机构分页列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryPageListOfOrgInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		TdscXbOrgInfoCondition condition = new TdscXbOrgInfoCondition();
		String currentPage = request.getParameter("currentPage");
		bindObject(condition, form);
		if (StringUtils.isNotEmpty(currentPage)) {
			condition.setCurrentPage(Integer.parseInt(currentPage));
		}
		PageList pageList = this.tdscXbOrgService.queryOrgInfoPageListByCondition(condition);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("orgInfoList");
	}
	
	/**
	 * 根据orgApp的编号取得对应的信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getOrgInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String orgInfoId = request.getParameter("orgInfoId");
		String currentPage = request.getParameter("currentPage");
		String msg = request.getParameter("msg");
		if (StringUtils.isNotEmpty(msg)) msg = "请关闭其他开启中协办机构批次!";
		TdscXbOrgInfo tdscXbOrgInfo = this.tdscXbOrgService.getOrgInfoById(orgInfoId);
		request.setAttribute("orgAppMap", this.tdscXbOrgService.findOrgAppIdNameMap());
		request.setAttribute("tdscXbOrgInfo", tdscXbOrgInfo);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("msg", msg);
		return mapping.findForward("orgInfo");
	}
	
	/**
	 * 保存更新orgapp信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateOrgInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String currentPage = request.getParameter("currentPage");
		TdscXbOrgInfo tdscXbOrgInfo = new TdscXbOrgInfo();
		bindObject(tdscXbOrgInfo, form);
		String msg = "";
		try {
			tdscXbOrgInfo = this.tdscXbOrgService.updateOrgInfo(tdscXbOrgInfo);
		} catch (RuntimeException e) {
			msg = "error";
		}
		return new ActionForward("orgApp.do?method=getOrgInfo&orgInfoId=" + tdscXbOrgInfo.getOrgInfoId() + "&currentPage=" + currentPage + "&msg=" + msg, true);
	}
	
	/**
	 * 删除
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delOrgInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String orgInfoId = request.getParameter("orgInfoId");
		String currentPage = request.getParameter("currentPage");
		this.tdscXbOrgService.delOrgInfoById(orgInfoId);
		return new ActionForward("orgApp.do?method=queryPageListOfOrgInfo&orgAppId=" + StringUtils.trimToEmpty(orgInfoId) + "&currentPage=" + StringUtils.trimToEmpty(currentPage), true);
	}
	
	/**
	 * 查找机构分页列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryPageListOfOrgHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		TdscXbOrgHistoryCondition condition = new TdscXbOrgHistoryCondition();
		String currentPage = request.getParameter("currentPage");
		bindObject(condition, form);
		if (StringUtils.isNotEmpty(currentPage)) {
			condition.setCurrentPage(Integer.parseInt(currentPage));
		}
		PageList pageList = this.tdscXbOrgService.queryOrgHistoryPageListByCondition(condition);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("orgHistoryList");
	}
	
	/**
	 * 根据orgApp的编号取得对应的信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getOrgHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String historyId = request.getParameter("historyId");
		String ifPrint = request.getParameter("ifPrint");
		String toPrint = request.getParameter("toPrint");
		String currentPage = request.getParameter("currentPage");
		TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getOrgHistoryById(historyId);
		TdscXbOrgInfo tdscXbOrgInfo = this.tdscXbOrgService.getOrgInfoById(tdscXbOrgHistory.getOrgInfoId());
		//获得地块交易信息表LIST
		if(tdscXbOrgHistory != null){
			if(StringUtils.isNotEmpty(tdscXbOrgHistory.getPlanId())){
				//获得实施方案信息
				if (tdscScheduletableService == null) System.out.println("tdscScheduletableService is null");
				if (tdscXbOrgHistory == null) System.out.println("tdscXbOrgHistory is null");
				System.out.println(tdscScheduletableService);
				System.out.println(tdscXbOrgHistory);
				TdscBlockPlanTable table = (TdscBlockPlanTable)tdscScheduletableService.findTdscBlockPlanTable(tdscXbOrgHistory.getPlanId());
				//获得地块交易信息LIST
				List tranAppList = (List)tdscScheduletableService.queryTranAppList(tdscXbOrgHistory.getPlanId());
				//招拍挂出让地块宗数
				String zongshu = tranAppList.size()+"";
				//招拍挂成交地块宗数
				int cj_zongshu = 0;
				//出让地块总的土地面积（㎡）
				BigDecimal blockArea = new BigDecimal("0");
				//成交面积（㎡）
				BigDecimal cj_blockArea = new BigDecimal("0");
				if(tranAppList != null && tranAppList.size() > 0){
					for(int i = 0; i<tranAppList.size(); i++){
						TdscBlockTranApp tranApp = (TdscBlockTranApp)tranAppList.get(i);
						if("02".equals(tranApp.getTranResult())){
							cj_zongshu ++;
						}
						TdscBlockInfo blockInfo = (TdscBlockInfo)tdscScheduletableService.getBlockInfoApp(StringUtils.trimToEmpty(tranApp.getBlockId()));
						if(blockInfo != null){
							List partList  = (List)tdscScheduletableService.getTdscBlockPartList(blockInfo.getBlockId());
							if(partList != null && partList.size() > 0){
								for(int p = 0; p<partList.size(); p++){
									TdscBlockPart part = (TdscBlockPart)partList.get(p);
									blockArea = blockArea.add(part.getBlockArea());
									if("02".equals(tranApp.getTranResult())){
										cj_blockArea = cj_blockArea.add(part.getBlockArea());
									}
								}
							}
						}
					}
				}
				request.setAttribute("zongshu", zongshu);
				request.setAttribute("cj_zongshu", cj_zongshu+"");
				request.setAttribute("blockArea", blockArea);
				request.setAttribute("cj_blockArea", cj_blockArea);
				request.setAttribute("table", table);
			}
		}
		
		request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
		request.setAttribute("tdscXbOrgInfo", tdscXbOrgInfo);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("ifPrint", ifPrint);
		if("1".equals(toPrint)){
			return mapping.findForward("printOrgHistoryInfo");
		}
		return mapping.findForward("orgHistoryInfo");
	}
	
	/**
	 * 保存更新orgapp信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateOrgHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String currentPage = request.getParameter("currentPage");
		String ifPrint = request.getParameter("ifPrint");
		TdscXbOrgHistory tdscXbOrgHistory = new TdscXbOrgHistory();
		bindObject(tdscXbOrgHistory, form);
		String ifGoon = tdscXbOrgHistory.getIfGoon();
		try {	
			tdscXbOrgHistory = this.tdscXbOrgService.updateOrgHistory(tdscXbOrgHistory);
        } catch (RuntimeException e) {
        	tdscXbOrgHistory.setOrgName(null);
        	request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
        	request.setAttribute("errorMsg", e.getMessage());
			return mapping.findForward("orgAppInfo");
		}
        if ("0".equals(StringUtils.trimToEmpty(ifGoon)))
        	return new ActionForward("orgApp.do?method=getOrgInfo&orgInfoId=" + tdscXbOrgHistory.getOrgInfoId(), true);
		return new ActionForward("orgApp.do?method=getOrgHistory&historyId=" + tdscXbOrgHistory.getHistoryId() + "&currentPage=" + currentPage + "&ifPrint=" + ifPrint, true);
		
	}
	
	/**
	 * 删除
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delOrgHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		String historyId = request.getParameter("historyId");
		String currentPage = request.getParameter("currentPage");
		this.tdscXbOrgService.delOrgHistoryById(historyId);
		return new ActionForward("orgApp.do?method=queryPageListOfOrgHistory&historyId=" + StringUtils.trimToEmpty(historyId) + "&currentPage=" + StringUtils.trimToEmpty(currentPage), true);
	}

}
