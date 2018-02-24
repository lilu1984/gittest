package com.wonders.tdsc.flowadapter.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.flowadapter.service.TestFlowService;

public class TestFlowAction extends BaseAction {
    private TestFlowService testFlowService;

    private AppFlowService appFlowService;

    private CommonFlowService commonFlowService;

    public void setTestFlowService(TestFlowService testFlowService) {
        this.testFlowService = testFlowService;
    }

    public void setAppFlowService(AppFlowService appFlowService) {
        this.appFlowService = appFlowService;
    }

    public void setCommonFlowService(CommonFlowService commonFlowService) {
        this.commonFlowService = commonFlowService;
    }
    
    /**
     * 查询土地列表
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
    public ActionForward queryAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        SysUser user = (SysUser) request.getSession().getAttribute("user");
        List buttomList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
    	Map buttomMap = new HashMap();
    	for ( int j = 0; j < buttomList.size() ; j++){
    		String id = (String)buttomList.get(j);
    		buttomMap.put(id, buttomList.get(j));
    	}
        if (buttomMap.get(GlobalConstants.BUTTON_ID_NEW_AUDIT)!=null){
            logger.debug("success has button");
        }
        
        String nodeId = "01";
        if (request.getParameter("nodeId") != null) {
            nodeId = request.getParameter("nodeId");
        } else if (request.getAttribute("nodeId") != null) {
            nodeId = (String) request.getAttribute("nodeId");
        }
        String pageType = "02";
        if (request.getParameter("pageType") != null) {
            pageType = (String) request.getParameter("pageType");
        } else if (request.getAttribute("pageType") != null) {
            pageType = (String) request.getAttribute("pageType");
        }
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("pageType", pageType);
        String blockname = request.getParameter("blockname");
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setNodeId(nodeId);
        condition.setUser(user);
        condition.setBlockName(blockname);
        PageList returnList = null;
        if ("01".equals(pageType)) {
            returnList = this.testFlowService.queryAppList(condition);
        } else if ("02".equals(pageType)) {
            returnList = this.testFlowService.queryAppFlowList(condition);
        }
        request.setAttribute("returnList", returnList);
        return mapping.findForward("appList");
    }

    /**
     * 到新增地块信息页面
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
    public ActionForward toBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String nodeId = request.getParameter("nodeId");
        String pageType = request.getParameter("pageType");
        String appId = request.getParameter("appId");
        String statusId = request.getParameter("statusId");

        TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
        TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();

        Map map = this.testFlowService.queryBlockAppInfo(appId);
        Map opnnMap = this.testFlowService.queryYijianInfo(appId);

        request.setAttribute("tdscBlockInfo", (TdscBlockInfo) map.get("tdscBlockInfo"));
        request.setAttribute("tdscBlockTranApp", (TdscBlockTranApp) map.get("tdscBlockTranApp"));
        request.setAttribute("opnninfo", opnnMap);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("pageType", pageType);

        if (FlowConstants.FLOW_STATUS_AUDIT_INIT.equals(statusId)) {
            return mapping.findForward("info");
        } else if (FlowConstants.FLOW_STATUS_AUDIT_MODIFY.equals(statusId)) {
            return mapping.findForward("info_modify");
        }

        return mapping.findForward("tdscError");
    }

    /**
     * 到新增地块信息页面
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
    public ActionForward toBlockOpnn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String nodeId = request.getParameter("nodeId");
        String pageType = request.getParameter("pageType");
        String appId = request.getParameter("appId");

        TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
        TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();

        Map map = this.testFlowService.queryBlockAppInfo(appId);
        Map opnnMap = this.testFlowService.queryYijianInfo(appId);

        request.setAttribute("tdscBlockInfo", (TdscBlockInfo) map.get("tdscBlockInfo"));
        request.setAttribute("tdscBlockTranApp", (TdscBlockTranApp) map.get("tdscBlockTranApp"));
        request.setAttribute("opnninfo", opnnMap);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("pageType", pageType);

        return mapping.findForward("opnn");
    }

    /**
     * 保存初始化意见
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
    public ActionForward saveBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String nodeId = request.getParameter("nodeId");
        String pageType = request.getParameter("pageType");
        String saveType = request.getParameter("saveType");
        SysUser user = (SysUser) request.getSession().getAttribute("user");

        TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
        tdscBlockInfo.setBlockId(request.getParameter("blockId"));
        tdscBlockInfo.setBlockName(request.getParameter("blockName"));
        tdscBlockInfo.setBlockType(request.getParameter("blockType"));

        TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
        tdscBlockTranApp.setAppId(request.getParameter("appId"));
        tdscBlockTranApp.setBlockId(request.getParameter("blockId"));
        tdscBlockTranApp.setTransferMode(request.getParameter("transferMode"));

        Map returnMap = this.testFlowService.saveBlockInfo(tdscBlockInfo, tdscBlockTranApp);
        tdscBlockInfo = (TdscBlockInfo) returnMap.get("tdscBlockInfo");
        tdscBlockTranApp = (TdscBlockTranApp) returnMap.get("tdscBlockTranApp");
        String appId = tdscBlockTranApp.getAppId();
        String transferMode = tdscBlockTranApp.getTransferMode();

        try {
            if ("tempSave".equals(saveType)) {
                this.appFlowService.tempSaveOpnn(appId, transferMode, user);
            } else if ("submitSave".equals(saveType)) {
                this.appFlowService.saveOpnn(appId, transferMode, user);
            }
            request.setAttribute("saveMessage", "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            this.testFlowService.deleteBlockInfo(appId);
            tdscBlockInfo = new TdscBlockInfo();
            tdscBlockTranApp = new TdscBlockTranApp();
            request.setAttribute("saveMessage", "保存失败");
        }

        request.setAttribute("tdscBlockInfo", tdscBlockInfo);
        request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("pageType", pageType);

        if ("tempSave".equals(saveType)) {
            return mapping.findForward("info");
        } else if ("submitSave".equals(saveType)) {
            return new ActionForward("/tdsc/testFlow.do?method=queryAppList");
        }

        return mapping.findForward("tdscError");
    }

    /**
     * 保存初始化意见
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
    public ActionForward cancelApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nodeId = request.getParameter("nodeId");
        String pageType = request.getParameter("pageType");
        String saveType = request.getParameter("saveType");
        SysUser user = (SysUser) request.getSession().getAttribute("user");

        String appId = request.getParameter("appId");

        this.testFlowService.deleteBlockInfo(appId);

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setNodeId(nodeId);
        condition.setUser(user);
        PageList returnList = null;
        if ("01".equals(pageType)) {
            returnList = this.testFlowService.queryAppList(condition);
        } else if ("02".equals(pageType)) {
            returnList = this.testFlowService.queryAppFlowList(condition);
        }
        request.setAttribute("returnList", returnList);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("pageType", pageType);

        return mapping.findForward("appList");
    }

    /**
     * 保存初始化意见
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
    public ActionForward saveOpnnInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String nodeId = request.getParameter("nodeId");
        String pageType = request.getParameter("pageType");
        String saveType = request.getParameter("saveType");
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        String appId = request.getParameter("appId");
        String transferMode = request.getParameter("transferMode");

        TdscAppFlow appFlow = new TdscAppFlow();
        appFlow.setAppId(appId);
        appFlow.setResultId(request.getParameter("resultId"));
        appFlow.setTextOpen(request.getParameter("textOpen"));
        appFlow.setTransferMode(transferMode);
        appFlow.setUser(user);

        Map map = this.testFlowService.queryBlockAppInfo(appId);
        TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) map.get("tdscBlockInfo");
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) map.get("tdscBlockTranApp");
        tdscBlockTranApp.setTransferMode(transferMode);
        Map returnMap = this.testFlowService.saveBlockInfo(tdscBlockInfo, tdscBlockTranApp);
        
        
        try {
            this.testFlowService.saveYijianInfo(appFlow, saveType);
            request.setAttribute("saveMessage", "意见保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("saveMessage", "保存失败");
        }

        Map opnninfo = this.testFlowService.queryYijianInfo(appId);

        request.setAttribute("tdscBlockInfo", (TdscBlockInfo) returnMap.get("tdscBlockInfo"));
        request.setAttribute("tdscBlockTranApp", (TdscBlockTranApp) returnMap.get("tdscBlockTranApp"));
        request.setAttribute("opnninfo", opnninfo);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("pageType", pageType);

        if ("tempSave".equals(saveType)) {
            return mapping.findForward("opnn");
        } else if ("submitSave".equals(saveType)) {
            return new ActionForward("/tdsc/testFlow.do?method=queryAppList");
        }

        return mapping.findForward("tdscError");
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
    public ActionForward toBlockFlowInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String appId = request.getParameter("appId");
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        //String userId = Integer.toString(user.getUserId());
        
        //WorkItem workItem= this.appFlowService.getAppFlowInfo(appId, userId);
        List appStatList = this.commonFlowService.getAppNodeStat(appId);

        //request.setAttribute("workItem", workItem);
        request.setAttribute("appStatList", appStatList);

        return mapping.findForward("flowInfo");
    }

}