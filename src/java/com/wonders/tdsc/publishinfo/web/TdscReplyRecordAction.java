package com.wonders.tdsc.publishinfo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscAnswerService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscReplyConfInfo;
import com.wonders.tdsc.bo.condition.TdscReplyConfInfoCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.publishinfo.service.TdscReplyRecordService;

public class TdscReplyRecordAction extends BaseAction {

    private TdscReplyRecordService tdscReplyRecordService;
    
    private TdscBlockInfoService tdscBlockInfoService;

    private CommonFlowService  commonFlowService;
    
    private TdscAnswerService tdscAnswerService;

    public void setTdscAnswerService(TdscAnswerService tdscAnswerService) {
        this.tdscAnswerService = tdscAnswerService;
    }

    public void setCommonFlowService(CommonFlowService commonFlowService) {
        this.commonFlowService = commonFlowService;
    }

    public void setTdscReplyRecordService(TdscReplyRecordService tdscReplyRecordService) {
        this.tdscReplyRecordService = tdscReplyRecordService;
    }

    public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
        this.tdscBlockInfoService = tdscBlockInfoService;
    }

    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获取页面参数
        TdscReplyConfInfoCondition condition = new TdscReplyConfInfoCondition();

        bindObject(condition, form);
        PageList pageList =new PageList();
        if (request.getParameter("currentPage") != null)
            condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        // 查询列表
        if(StringUtils.isNotEmpty(condition.getBlockName())){
             pageList = (PageList)tdscReplyRecordService.findPageListByCondition(condition);
        }else{
             pageList = (PageList)tdscReplyRecordService.findPageList(condition);  
        }

        request.setAttribute("pageList", pageList);
        request.setAttribute("condition", condition);

        return mapping.findForward("replyrecordList");
    }
    
    /**
     * 保存答疑记要
     */
    public ActionForward saveFabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取页面参数
        String appId = request.getParameter("appId");
        
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp)tdscBlockInfoService.findTdscBlockTranApp(appId);
        
        String transferMode = (String)tdscBlockTranApp.getTransferMode();
        //修改 实体的 是否发布的状态 并记录发布时间
        tdscReplyRecordService.modTdscReplyConfInfo(appId);
        
        // 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String userId = user.getUserId()+"";
        commonFlowService.postAppNode(appId,FlowConstants.FLOW_NODE_FAQ_RELEASE, transferMode, userId);
        
        return query(mapping,form,request,response);
    }
    
    public ActionForward fabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//      获取页面参数
        String appId = request.getParameter("appId");
        request.setAttribute("appId", appId);
        request.setAttribute("modeNameEn", "reply_summary.doc");
        String recordId = "";
//      通过appId来查询答疑会信息表的信息
        TdscReplyConfInfo tempReplyConfInfo = tdscAnswerService.findReplyConfInfo(appId);
        if(null != tempReplyConfInfo){
            request.setAttribute("tempReplyConfInfo", tempReplyConfInfo);
            recordId = tempReplyConfInfo.getRecordId();
            request.setAttribute("recordId", recordId);
        }
        return mapping.findForward("toFabu");
    }
    
    /**
     * 下载答疑信息文件到本地
     * 08.03.07
     */
    public ActionForward downLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//      获取页面参数
        String appId = request.getParameter("appId");
        request.setAttribute("appId", appId);
        request.setAttribute("modeNameEn", "reply_summary.doc");
        String recordId = "";
//      通过appId来查询答疑会信息表的信息
        TdscReplyConfInfo tempReplyConfInfo = tdscAnswerService.findReplyConfInfo(appId);
        if(null != tempReplyConfInfo){
            request.setAttribute("tempReplyConfInfo", tempReplyConfInfo);
            recordId = tempReplyConfInfo.getRecordId();
            request.setAttribute("recordId", recordId);
        }
        return mapping.findForward("toDownLoad");
    }
 
}
