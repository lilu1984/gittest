package com.wonders.tdsc.blockwork.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;

import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscAnswerService;
import com.wonders.tdsc.blockwork.web.form.AnswerInfoForm;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockScheduleTable;
import com.wonders.tdsc.bo.TdscFaqInfo;
import com.wonders.tdsc.bo.TdscReplyConfInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscAnswerAction extends BaseAction {

    private CommonQueryService commonQueryService;

    private TdscAnswerService tdscAnswerService;

    private AppFlowService appFlowService;

    public void setAppFlowService(AppFlowService appFlowService) {
        this.appFlowService = appFlowService;
    }

    public void setTdscAnswerService(TdscAnswerService tdscAnswerService) {
        this.tdscAnswerService = tdscAnswerService;
    }

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }

    /**
     * 查询待办业务列表（问题信息管理）
     */
    public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String blockName = request.getParameter("blockName");
        String blockType = request.getParameter("blockType");
        String transferMode = request.getParameter("transferMode");
        String districtId = request.getParameter("districtId");
        String currentPage = request.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = (String) request.getAttribute("currentPage");
        }

        int cPage = 0;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }

        List buttomList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
        
        Map buttomMap = new HashMap();
        for(int i = 0; i < buttomList.size(); i++){
        	String tree = (String)buttomList.get(i);
            buttomMap.put(tree,tree);
        }
                        
        ArrayList statusIdList = new ArrayList();
		List districtIdList = null;
        if (buttomMap.get(GlobalConstants.BUTTON_ID_QUESTION_WRITE)!=null){
            statusIdList.add(FlowConstants.FLOW_STATUS_QUESTION_WRITE);
        }
        if (buttomMap.get(GlobalConstants.BUTTON_ID_QUESTION_TRANSMIT)!=null){
            statusIdList.add(FlowConstants.FLOW_STATUS_QUESTION_TRANSMIT);
        }
        if (buttomMap.get(GlobalConstants.BUTTON_ID_QUESTION_CLASSIFY)!=null){
            statusIdList.add(FlowConstants.FLOW_STATUS_QUESTION_CLASSIFY);
            
            List qxList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
            if (qxList!=null && qxList.size()>0){
                districtIdList = qxList;
            }
        }
        if (statusIdList.size()==0){
            statusIdList = null;
        }
            
            
        String nodeId = "07";

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setNodeId(nodeId);
        condition.setCurrentPage(cPage);
        condition.setBlockName(blockName);
        condition.setBlockType(blockType);
        condition.setTransferMode(transferMode);
        condition.setDistrictId(districtId);
        condition.setStatusIDList(statusIdList);
        condition.setDistrictIdList(districtIdList);
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        condition.setUser(user);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewPageList(condition));// 按条件查询列表
        //System.out.println("queryAppList"+commonQueryService.queryTdscBlockAppViewPageList(condition).getList().size());
        request.setAttribute("queryAppCondition", condition);

        return mapping.findForward("list");
    }

    /**
     * 查询待办业务列表（答疑信息管理）
     */
    public ActionForward replyAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String blockName = request.getParameter("blockName");
        String blockType = request.getParameter("blockType");
        String transferMode = request.getParameter("transferMode");
        String districtId = request.getParameter("districtId");
        String currentPage = request.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = (String) request.getAttribute("currentPage");
        }

        int cPage = 0;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }

        String nodeId = "08";

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setNodeId(nodeId);
        condition.setCurrentPage(cPage);
        condition.setBlockName(blockName);
        condition.setBlockType(blockType);
        condition.setTransferMode(transferMode);
        condition.setDistrictId(districtId);
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        condition.setUser(user);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewPageList(condition));// 按条件查询列表
        request.setAttribute("queryAppCondition", condition);

        return mapping.findForward("replyList");
    }

    /**
     * 录入问题信息管理的信息
     * 
     * @param appId
     *            20071119*
     */
    public ActionForward toSaveAnswerInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获得页面参数appId
        String appId = request.getParameter("appId");
        String nodeId = request.getParameter("nodeId");
        String statusId = request.getParameter("statusId");
        // 获得查询条件
        if (null != appId) {
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            // 调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            // 新建地块交易信息表(TDSC_BLOCK_TRAN_APP)、地块基本信息表(TDSC_BLOCK_INFO)的对象
            if (commonInfo != null) {
                // 取得地块交易信息表、地块基本信息表的信息
                request.setAttribute("commonInfo", commonInfo);
            }
            // 定义答疑信息表的list
            List tdscFaqInfoList = new ArrayList();
            // 获得答疑信息表的list
            tdscFaqInfoList = this.tdscAnswerService.findTdscFaqInfoList(appId);
            if(tdscFaqInfoList.size() > 0){
                request.setAttribute("tdscFaqInfoList", tdscFaqInfoList);
            }
        }
        request.setAttribute("statusId", statusId);
        request.setAttribute("appId", appId);
        request.setAttribute("nodeId", nodeId);
        return mapping.findForward("saveInfo");
    }

    /**
     * 转发问题信息管理的信息
     * 
     * @param appId
     *            20071120*
     */
    public ActionForward toTransmitAnswerInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获得页面参数appId
        String appId = request.getParameter("appId");
        String nodeId = request.getParameter("nodeId");
        String statusId = request.getParameter("statusId");
        // 定义答疑信息表的list
        List tdscFaqInfoList = new ArrayList();
        // 获得查询条件
        if (null != appId) {
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            // 调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            // 新建地块交易信息表(TDSC_BLOCK_TRAN_APP)、地块基本信息表(TDSC_BLOCK_INFO)的对象
            if (commonInfo != null) {
                // 取得地块交易信息表、地块基本信息表的信息
                request.setAttribute("commonInfo", commonInfo);
            }
            // 获得答疑信息表的list
            tdscFaqInfoList = this.tdscAnswerService.findTdscFaqInfoList(appId);
        }
        request.setAttribute("appId", appId);
        request.setAttribute("statusId", statusId);
        request.setAttribute("tdscFaqInfoList", tdscFaqInfoList);
        return mapping.findForward("transmitInfo");
    }

    /**
     * 归类问题信息管理的信息
     * 
     * @param appId
     *            20071120*
     */
    public ActionForward toClassifyAnswerInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获得页面参数appId
        String appId = request.getParameter("appId");
        String nodeId = request.getParameter("nodeId");
        String statusId = request.getParameter("statusId");
        // 定义答疑信息表的list
        List tdscFaqInfoList = new ArrayList();
        // 获得查询条件
        if (null != appId) {
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            // 调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            // 新建地块交易信息表(TDSC_BLOCK_TRAN_APP)、地块基本信息表(TDSC_BLOCK_INFO)的对象
            if (commonInfo != null) {
                // 取得地块交易信息表、地块基本信息表的信息
                request.setAttribute("commonInfo", commonInfo);
            }
            // 获得答疑信息表的list
            tdscFaqInfoList = this.tdscAnswerService.findTdscFaqInfoList(appId);
            if(tdscFaqInfoList.size() > 0){
                int i = 0;
                TdscFaqInfo tdscFaqInfo = (TdscFaqInfo)tdscFaqInfoList.get(i);
                String answerUnit = (String)tdscFaqInfo.getAnswerUnit();
                if(null != answerUnit){
                    String isEmpty = "false";
                    request.setAttribute("isEmpty",isEmpty);
                }
            }
        }
        request.setAttribute("appId", appId);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("statusId", statusId);
        request.setAttribute("tdscFaqInfoList", tdscFaqInfoList);
        return mapping.findForward("classifyInfo");
    }  
    /**
     * 打印问题归类页面
     * @param appId
     * 20071219*
     */
    public ActionForward toPrintClassify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
//      获得页面参数appId
        String appId = request.getParameter("appId");
        if(null != appId){
//          获得查询条件
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            //调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            if (commonInfo != null){
                request.setAttribute("commonInfo", commonInfo);
            }
//          定义答疑信息表的list
            List infoList = new ArrayList();
            // 获得答疑信息表的list
            infoList = this.tdscAnswerService.findTdscFaqInfoList(appId);
            if(infoList.size() > 0){
                request.setAttribute("tdscFaqInfoList", infoList);
            }
        }
        return mapping.findForward("printClassify");
    }

    /**
     * 录入答疑信息管理的信息
     * 
     * @param appId
     *            20071121*
     */
    public ActionForward toReplyInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获得页面参数appId
        String appId = request.getParameter("appId");
        String nodeId = request.getParameter("nodeId");
        String statusId = request.getParameter("statusId");
        String recordId = "";

        // 获得查询条件
        if (null != appId) {
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            // 调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            // 新建地块交易信息表(TDSC_BLOCK_TRAN_APP)、地块基本信息表(TDSC_BLOCK_INFO)的对象
            if (commonInfo != null) {
                // 取得地块交易信息表、地块基本信息表的信息、出让地块进度执行表信息
                request.setAttribute("commonInfo", commonInfo);
            }
            // 通过appId来查询出让地块进度执行表的信息
            TdscBlockScheduleTable tempBlockScheduleTable = tdscAnswerService.findScheduleInfo(appId);
            if(null != tempBlockScheduleTable){
                request.setAttribute("tempBlockScheduleTable", tempBlockScheduleTable);
            }
            // 通过appId来查询答疑会信息表的信息
            TdscReplyConfInfo tempReplyConfInfo = tdscAnswerService.findReplyConfInfo(appId);
            if(null != tempReplyConfInfo){
                request.setAttribute("tempReplyConfInfo", tempReplyConfInfo);
                recordId = tempReplyConfInfo.getRecordId();
            }
//          定义答疑信息表的list
            List tdscFaqInfoList = new ArrayList();
            // 获得答疑信息表的list
            tdscFaqInfoList = this.tdscAnswerService.findTdscFaqInfoList(appId);
            request.setAttribute("tdscFaqInfoList", tdscFaqInfoList);
        }
        request.setAttribute("recordId", recordId);
        request.setAttribute("modeNameEn", "reply_summary.doc");
        request.setAttribute("appId", appId);
        return mapping.findForward("replyInfo");
    }

    /**
     * 下载答疑会信息表的信息
     * 
     * @param appId
     *            20071124*
     */
    public ActionForward toDownLoadReply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获得页面参数appId
        String appId = request.getParameter("appId");
        // 获得查询条件
        if (null != appId) {
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            // 调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            // 新建地块交易信息表(TDSC_BLOCK_TRAN_APP)、地块基本信息表(TDSC_BLOCK_INFO)的对象
            if (commonInfo != null) {
                // 取得地块交易信息表、地块基本信息表的信息、出让地块进度执行表信息
                request.setAttribute("commonInfo", commonInfo);
            }
        }
        // 通过appId来查询出让地块进度执行表的信息
        TdscBlockScheduleTable tdscBlockScheduleTable = tdscAnswerService.findScheduleInfo(appId);
        request.setAttribute("tdscBlockScheduleTable", tdscBlockScheduleTable);
        // 通过appId来查询答疑会信息表的信息
        TdscReplyConfInfo tdscReplyConfInfo = tdscAnswerService.findReplyConfInfo(appId);
        request.setAttribute("tdscReplyConfInfo", tdscReplyConfInfo);
        request.setAttribute("appId", appId);
        return mapping.findForward("downLoadReply");
    }

    /**
     * 答疑纪要模版下载
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward modelDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获得页面参数appId
        String appId = request.getParameter("appId");
        if (null != appId) {
            // 获得查询条件
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            // 调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            // 新建地块交易信息表(TDSC_BLOCK_TRAN_APP)、地块基本信息表(TDSC_BLOCK_INFO)的对象
            if (commonInfo != null) {
                // 取得地块交易信息表、地块基本信息表的信息、出让地块进度执行表信息
                request.setAttribute("commonInfo", commonInfo);
            }
            // 定义答疑信息表的list
            List tdscFaqInfoList = new ArrayList();
            // 获得答疑信息表的list
            tdscFaqInfoList = this.tdscAnswerService.findTdscFaqInfoList(appId);
            request.setAttribute("tdscFaqInfoList", tdscFaqInfoList);
        }
        return mapping.findForward("replyMode");
    }

}