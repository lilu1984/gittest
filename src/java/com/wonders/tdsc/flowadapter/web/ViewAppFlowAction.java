package com.wonders.tdsc.flowadapter.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.tdsc.bo.TdscAppWorkflowInstanceRel;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscOpnnCondition;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class ViewAppFlowAction extends BaseAction {
    private AppFlowService appFlowService;

    private CommonFlowService commonFlowService;
    
    private CommonQueryService commonQueryService;

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }

    public void setAppFlowService(AppFlowService appFlowService) {
        this.appFlowService = appFlowService;
    }

    public void setCommonFlowService(CommonFlowService commonFlowService) {
        this.commonFlowService = commonFlowService;
    }

    /**
     * 到地块流程信息页面
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward toBlockFlowDiagram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String appId = request.getParameter("appId");

        TdscOpnnCondition condition=new TdscOpnnCondition();
        condition.setAppId(appId);
        
        TdscBaseQueryCondition baseCondition=new TdscBaseQueryCondition();
        baseCondition.setAppId(appId);
        
        TdscBlockAppView tdscBlockAppView=commonQueryService.getTdscBlockAppView(baseCondition);
        String transferMode=tdscBlockAppView.getTransferMode();
        String blockName=tdscBlockAppView.getBlockName();
        
        Map appStatMap = this.commonFlowService.getAppNodeStatMap(appId);
        List appOpnnList = this.appFlowService.queryAppOpnnList(condition);
        String auditInstanceId = "";
        String fileInstanceId = "";
        
        TdscAppWorkflowInstanceRel auditRel = this.commonFlowService.findWorkInstanceInfo(appId, FlowConstants.FLOW_BUSINESS_CODE_AUDIT);
        if (auditRel!=null && auditRel.getProcessInstanceId()!=null){
            auditInstanceId = auditRel.getProcessInstanceId();
        }
        TdscAppWorkflowInstanceRel fileRel = this.commonFlowService.findWorkInstanceInfo(appId, FlowConstants.FLOW_BUSINESS_CODE_FILE);
        if (fileRel!=null && fileRel.getProcessInstanceId()!=null){
            fileInstanceId = fileRel.getProcessInstanceId();
        }

        request.setAttribute("appStatMap", appStatMap);
        request.setAttribute("appOpnnList", appOpnnList);
        request.setAttribute("transferMode", transferMode);
        request.setAttribute("blockName", blockName);
        request.setAttribute("auditInstanceId", auditInstanceId);
        request.setAttribute("fileInstanceId", fileInstanceId);

        return mapping.findForward("flowInfo");
    }

}