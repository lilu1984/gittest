package com.wonders.tdsc.blockwork.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.web.form.TdscNoticeForm;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscNoticeAction extends BaseAction {

    private TdscNoticeService tdscNoticeService;

    private CommonQueryService commonQueryService;

    private TdscBlockInfoService tdscBlockInfoService;

    private AppFlowService appFlowService;

    public void setAppFlowService(AppFlowService appFlowService) {
        this.appFlowService = appFlowService;
    }

    public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
        this.tdscNoticeService = tdscNoticeService;
    }

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }

    public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
        this.tdscBlockInfoService = tdscBlockInfoService;
    }

    /**
     * 查询出所有已提交的公告列表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取页面参数
        TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
        // 如果从出记公告制做页面返回，则不bindObject查询条件，这样可以显示出所有的记录
        String forwardType = request.getParameter("forwardType");
        if (!"1".equals(request.getAttribute("forwardType")))
            bindObject(condition, form);
        if ("1".equals(forwardType)) {
            condition.setNoticeNo("");
            condition.setNoticeName("");
        }

        if (request.getParameter("currentPage") != null)
            condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

        // 设置公告状态为暂存
        condition.setNoticeStatus("00");

        /** 如果是流程中的节点需要设置节点编号 */
        // condition.setNodeId(GlobalConstants.FLOW_NODE_FILE_GET);
        // 设置页面行数
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        // 查询列表
        request.setAttribute("pageList", tdscNoticeService.findPageList(condition));
        request.setAttribute("condition", condition);

        request.setAttribute("oldNoticeNo", "");
        return mapping.findForward("tdscNoticeAppList");
    }

    // 查询出所有可制做出让公告的土地信息列表
    public ActionForward queryGgList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获取页面参数
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        bindObject(condition, form);

        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
        condition.setBlockName(request.getParameter("blockName"));
        condition.setTransferMode(request.getParameter("transferMode"));
        condition.setStatusId("0303");
        condition.setNoitceNo("null");

        /** 如果是流程中的节点需要设置节点编号 */
        // condition.setNodeId(GlobalConstants.FLOW_NODE_FILE_GET);
        // 设置页面行数
        // condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        String getOldNoticeNo = (String) request.getParameter("oldNoticeNo");
        if (getOldNoticeNo != null && !"null".equals(getOldNoticeNo)) {
            // 查询出noticeNo="null"和已经暂存的土地信息
            List list = tdscNoticeService.findTdscBlockAppViewList(condition, getOldNoticeNo);
            request.setAttribute("list", list);
        } else {
            // 查询列表
            List newList = commonQueryService.queryTdscBlockAppViewList(condition);
            request.setAttribute("list", newList);
        }

        /*
         * //取出暂存时已存入列表中的土地信息 String getOldNoticeNo=(String) request.getParameter("oldNoticeNo"); int flag=0; if(getOldNoticeNo!=null){
         * condition.setNoitceNo(getOldNoticeNo); List oldList=commonQueryService.queryTdscBlockAppViewList(condition);
         * if(oldList!=null&&oldList.size()>0){ for(int i=0;i<oldList.size();i++){ TdscBlockAppView tempOldList=(TdscBlockAppView) oldList.get(i);
         * for(int j=0;j<newList.size();j++){ TdscBlockAppView tempNewList=(TdscBlockAppView) newList.get(j);
         * if(tempOldList.getNoitceNo()==tempNewList.getNoitceNo()){ flag=1; break; } } if(flag==0) newList.add(oldList.get(i)); flag=0; } } }
         * 
         */

        if ("1".equals(request.getParameter("type"))) {
            TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
            String appIds[] = tdscNoticeForm.getAppIds();
            List appIdList = new ArrayList();
            if (appIds != null) {
                for (int i = 0; i < appIds.length; i++) {
                    appIdList.add(appIds[i]);
                }
            }
            request.setAttribute("parentAppList", appIdList);
        }

        request.setAttribute("condition", condition);

        return mapping.findForward("tdscNoticeAppList_new");
    }

    /**
     * 查询土地信息列表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward queryTdxx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;

        String appIds[] = tdscNoticeForm.getAppIds();

        // 查询列表
        // PageList newList = tdscBlockInfoService.findPageListByAppIds(appIds);
        // request.setAttribute("pageList", newList);

        List list = new ArrayList();
        TdscBlockAppView tdscBlockAppView = null;

        if (appIds.length != 0) {
            for (int i = 0; i < appIds.length; i++) {
                if (appIds[i] == null || "".equals(appIds[i]))
                    continue;
                else {
                    TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
                    condition.setAppId(appIds[i]);
                    tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
                    if (tdscBlockAppView != null)
                        list.add(tdscBlockAppView);

                }
            }
        }

        request.setAttribute("tdscBlockAppViewList", list);

        return mapping.findForward("zzgg");
    }

    /**
     * 提交出让公告
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

     
        // 传入文件名
        TdscNoticeForm fileForm = (TdscNoticeForm) form;
//        FormFile upLoadFile = (FormFile) fileForm.getFileName();

        String appIds[] = fileForm.getAppIds();

        String noticeStatus = request.getParameter("noticeStatus");
        // 获取页面参数
        TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
        tdscNoticeApp.setNoticeNo(request.getParameter("noticeNo"));
        tdscNoticeApp.setNoticeName(request.getParameter("noticeName"));
        tdscNoticeApp.setNoticeType("01");
        tdscNoticeApp.setNoticeStatus(noticeStatus);
    

        // 取得上一次暂存的noticeId,noticeNo
        String getOldNoticeNo = (String) request.getParameter("oldNoticeNo");
        String getOldNoticeId = (String) request.getParameter("oldNoticeId");

        tdscNoticeService.save(tdscNoticeApp, appIds, getOldNoticeId, getOldNoticeNo);

        // 制做完成出让公告，forwardType=1，查询页面可以不bindObject查询条件
        request.setAttribute("forwardType", "1");

        // 保存暂存前的noticeNo
        request.setAttribute("oldNoticeNo", "");

        // 保存暂存前的noticeId
        request.setAttribute("oldNoticeId", "");

        return mapping.findForward("successSave");
    }

    /**
     * 中间跳转，用于将制做出让公告时，将父页面已选中的土地信息传到子页面，子页面就不显示该记录
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward transferAppIds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("transferAppIds");
    }

    /**
     * 暂存出让公告
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward zanCunCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 传入文件名
        TdscNoticeForm fileForm = (TdscNoticeForm) form;
//        FormFile upLoadFile = (FormFile) fileForm.getFileName();

        String appIds[] = fileForm.getAppIds();

        // 获取页面参数
        TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
        tdscNoticeApp.setNoticeNo(request.getParameter("noticeNo"));
        tdscNoticeApp.setNoticeName(request.getParameter("noticeName"));
        tdscNoticeApp.setNoticeType("01");
        tdscNoticeApp.setNoticeStatus("00");

        // 制做完成出让公告，forwardType=1，查询页面可以不bindObject查询条件
        request.setAttribute("forwardType", "1");

        String getOldNoticeNo = (String) request.getParameter("oldNoticeNo");
        String getOldNoticeId = (String) request.getParameter("oldNoticeId");

        //
        tdscNoticeService.update(tdscNoticeApp, appIds, getOldNoticeId, getOldNoticeNo);

        // 通过页面传来的appIds取出土地信息列表对象
        List list = new ArrayList();
        TdscBlockAppView tdscBlockAppView = null;

        if (appIds.length != 0) {
            for (int i = 0; i < appIds.length; i++) {
                if (appIds[i] == null || "".equals(appIds[i]))
                    continue;
                else {
                    TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
                    condition.setAppId(appIds[i]);
                    tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
                    if (tdscBlockAppView != null)
                        list.add(tdscBlockAppView);

                }
            }
        }

        // 返回土地信息列表
        request.setAttribute("tdscBlockAppViewList", list);

        // 保存暂存前的noticeNo
        request.setAttribute("oldNoticeNo", tdscNoticeApp.getNoticeNo());

        // 保存暂存前的noticeId
        request.setAttribute("oldNoticeId", tdscNoticeApp.getNoticeId());

        // 将页面输入的值保存
        request.setAttribute("tdscNoticeApp", tdscNoticeApp);

        // 返回到本页
        return mapping.findForward("bianji");
    }

    /**
     * 编辑出让公告
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward bianJiCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 通过noticeId查询出出让公告信息表一对应的一条记录TdscNoticeApp
        // 再查询地块交易信息表中noticeId和出让公告信息表noticeId相同的记录TdscBlockTranApp
        // 将以上信息返回给页面

        // 返回输入框中的信息
        String noticeId = request.getParameter("noticeId");
        TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
        request.setAttribute("tdscNoticeApp", tdscNoticeApp);

        // 查询出appIds
        List tdscBlockAppViewList = new ArrayList();
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setNoitceNo(tdscNoticeApp.getNoticeNo());
        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_GET);
        tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);

        /*
         * List appIdList = new ArrayList(); appIdList = tdscNoticeService.getAppIdsByNoticeId(noticeId); // 查询列表 // PageList newList =
         * tdscBlockInfoService.findPageListByAppIds(appIds); // request.setAttribute("pageList", newList);
         * 
         * List list = new ArrayList(); TdscBlockAppView tdscBlockAppView = null;
         * 
         * if (appIdList != null && appIdList.size()>0) { for (int i = 0; i < appIdList.size(); i++) { if (appIdList.get(i) == null ||
         * "".equals(appIdList.get(i))) continue; else { TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
         * condition.setAppId(String.valueOf(appIdList.get(i))); tdscBlockAppView = (TdscBlockAppView)
         * commonQueryService.getTdscBlockAppView(condition); if (tdscBlockAppView != null) list.add(tdscBlockAppView); } } }
         */
        request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);

        // 保存暂存前的noticeNo
        request.setAttribute("oldNoticeNo", tdscNoticeApp.getNoticeNo());

        // 保存暂存前的noticeId
        request.setAttribute("oldNoticeId", tdscNoticeApp.getNoticeId());

        return mapping.findForward("bianji");
    }

    /**
     * 删除出让公告
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward delCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String oldNoticeId = request.getParameter("oldNoticeId");

        tdscNoticeService.delCrgg(oldNoticeId);

        // 制做完成出让公告，forwardType=1，查询页面可以不bindObject查询条件
        request.setAttribute("forwardType", "1");

        // 保存暂存前的noticeNo
        request.setAttribute("oldNoticeNo", "");

        // 保存暂存前的noticeId
        request.setAttribute("oldNoticeId", "");

        return mapping.findForward("successSave");
    }

    /**
     * 新版出让公告
     */

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward querynewGgList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 获取页面参数
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        // 如果从提交公告页面进入，则不bindObject查询条件，这样可以显示出所有的记录
        String issueStartDate = "";
        if (!"1".equals(request.getParameter("forwardType"))) {
            bindObject(condition, form);
            condition.setTransferMode(request.getParameter("transferMode"));
            issueStartDate = request.getParameter("issueStartDate");
        }

        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
        condition.setStatusId(FlowConstants.FLOW_STATUS_NOTICE_MAKE);
        condition.setNoitceNo("null");

        String noticeId = request.getParameter("noticeId");
        request.setAttribute("noticeId", noticeId);

        String oldNoticeNo = request.getParameter("oldNoticeNo");
        if (!"".equals(oldNoticeNo) && oldNoticeNo != null) {
            oldNoticeNo = "沪告字[" + oldNoticeNo.substring(0, 4) + "]第" + oldNoticeNo.substring(4) + "号";
            request.setAttribute("oldNoticeNo", oldNoticeNo);
        }

        List list = new ArrayList();
        if (!"".equals(oldNoticeNo) && oldNoticeNo != null && !"".equals(noticeId))
            // list = tdscNoticeService.findBlockAppViewList(condition, oldNoticeNo,issueStartDate);
            list = tdscNoticeService.getNewList(condition, noticeId);
        else
            list = tdscNoticeService.sortTdscBlockAppViewList(condition);
        request.setAttribute("pageList", list);
        request.setAttribute("condition", condition);

        // 如果第一次进入页面则产生公告号，以后每次查询不再产生新的公告号
        if ("1".equals(request.getParameter("ifDisplay"))) {
            request.setAttribute("ifDisplay", "1");
            String temp = request.getParameter("oldNoticeNo");
            request.setAttribute("createNoticeNo", temp);
        } else {
            String temp = (String) tdscNoticeService.selectNoticeNo();
            request.setAttribute("createNoticeNo", temp);
        }

        return mapping.findForward("listNewGg");
    }

    /**
     * 生成公告模版
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward createNoticeModel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获取页面参数
        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        String noticeName = tdscNoticeForm.getNoticeName();
        String noticeNo = tdscNoticeForm.getNoticeNo();
        String noticeId = tdscNoticeForm.getNoticeId();

        // 查询列表
        List list = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
        request.setAttribute("pageList", list);

        request.setAttribute("noticeName", noticeName);
        request.setAttribute("noticeNo", noticeNo);
        request.setAttribute("noticeId", noticeId);

        return mapping.findForward("noticeModel");
    }

    // 需要同时上传用户编缉后的word文件
    public ActionForward addNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 获取页面参数
        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        String noticeName = tdscNoticeForm.getNoticeName();
        String noticeNo = tdscNoticeForm.getNoticeNo();
        String recordId = request.getParameter("RecordID");
        String modeNameEn = request.getParameter("modeNameEn");

        String noticeStatus = request.getParameter("noticeStatus");

        String noticeId = request.getParameter("noticeId");

        TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
        
        // 保存前需要通过noticeId删除原有的记录
        //tdscNoticeService.saveNotice(appIds ,noticeNo, noticeName, noticeStatus, noticeId, modeNameEn, recordId);

        // 如果是提交公告，则需要保存每条土地信息的处理意见
        if ("01".equals(noticeStatus)) {
            // 获得用户信息
    		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

            TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
            // 将appId[]封装成appIdList
            List appIdList = new ArrayList();
            for (int i = 0; i < appIds.length; i++) {
                appIdList.add(appIds[i]);
            }
            condition.setAppIdList(appIdList);
            // 通过appIdList查出土地List
            List tdList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
            tdscNoticeService.saveOpnnByViewList(tdList, user);
        }

        request.setAttribute("forwardType", "1");

        return mapping.findForward("successSave");
    }

    public ActionForward editNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        if (appIds != null && appIds.length > 0) {
            List tempAppIdsList = new ArrayList();
            for (int i = 0; i < appIds.length; i++) {
                tempAppIdsList.add(appIds[i]);
            }
            condition.setAppIdList(tempAppIdsList);
            List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

            request.setAttribute("pageList", tdscBlockAppViewList);

            String noticeNo = tdscNoticeForm.getNoticeNo();
            noticeNo = noticeNo.substring(4, 8) + noticeNo.substring(10, 13);
            request.setAttribute("createNoticeNo", noticeNo);
            request.setAttribute("condition", null);
            return mapping.findForward("editNotice");
        }

        // 通过noticeId查询出出让公告信息表一对应的一条记录TdscNoticeApp
        // 再查询地块交易信息表中noticeId和出让公告信息表noticeId相同的记录TdscBlockTranApp
        // 将以上信息返回给页面

        // 返回输入框中的信息
        String noticeId = request.getParameter("noticeId");
        TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
        request.setAttribute("tdscNoticeApp", tdscNoticeApp);

        // 查询出appIds
        List tdscBlockAppViewList = new ArrayList();

        condition.setNoitceNo(tdscNoticeApp.getNoticeNo());
        // condition.setNoticeId(tdscNoticeApp.getNoticeId());
        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);

        tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);
        // tdscNoticeService.findTdscBlockAppViewList(condition, getOldNoticeNo);

        request.setAttribute("pageList", tdscBlockAppViewList);

        String noticeNo = tdscNoticeApp.getNoticeNo();
        noticeNo = noticeNo.substring(7, 11) + noticeNo.substring(12, 15);
        request.setAttribute("createNoticeNo", noticeNo);
        request.setAttribute("condition", condition);

        return mapping.findForward("editNotice");
    }

    /**
     * 根据通用接口获得土地信息
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
    /**
     * 查询待办业务列表
     */
    public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String currentPage = request.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = (String) request.getAttribute("currentPage");
        }

        int cPage = 0;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }

        String nodeId = "03";

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setNodeId(nodeId);
        condition.setCurrentPage(cPage);
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        condition.setUser(user);
        request.setAttribute("nodeId", nodeId);
        request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewFlowPageList(condition));// 按条件查询列表
        request.setAttribute("queryAppCondition", condition);

        return mapping.findForward("list");
    }

    /**
     * 第三版新增功能,在第二版出让公告的基础上增加了出让公告预生成模版下载，公告上传功能
     */

    /**
     * 进入公告模版上传下载页面
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward entryUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获取页面参数
        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        String noticeName = tdscNoticeForm.getNoticeName();
        String noticeNo = tdscNoticeForm.getNoticeNo();
        String noticeId = tdscNoticeForm.getNoticeId();
//        String recordId = "";
        TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
        List appIdList = new ArrayList();
        if (appIds.length > 0) {
            for (int i = 0; i < appIds.length; i++) {
                appIdList.add(appIds[i]);
            }
        }
        
//        //判断TdscNoticeApp表中的recordId值是否为空
//        if(null != noticeId){
//            TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
//            tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
//            if(tdscNoticeApp != null){
//                recordId = tdscNoticeApp.getRecordId();
//                if(recordId != null){
//                    request.setAttribute("recordId", recordId);
//                }
//            }
//        }
//      查询列表
        List list = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
        
        
        String transferMode = "";
        String blockType = "";
        if(list!=null&&list.size()>0){
        	for(int i=0;i<list.size();i++){
        		tdscBlockAppView = (TdscBlockAppView)list.get(0);
        		transferMode = tdscBlockAppView.getTransferMode();
        		blockType = tdscBlockAppView.getBlockType();
        		break;
        	}
        }
        //招标
        if ("3107".equals(transferMode)) {
            // 工业性用地
            if ("101".equals(blockType)) {
                request.setAttribute("modeNameEn", "gyxydzb_notice_mode.doc");
            } else {// 经营性用地
                request.setAttribute("modeNameEn", "jyxydzb_notice_mode.doc");
            }
        }

        // 拍卖
        if ("3103".equals(transferMode)) {
            // 工业性用地
            if ("101".equals(blockType)) {
                request.setAttribute("modeNameEn", "gyxydpm_notice_mode.doc");
            } else {// 经营性用地
                request.setAttribute("modeNameEn", "jyxydpm_notice_mode.doc");
            }
        }

        // 挂牌
        if ("3104".equals(transferMode)) {
            // 工业性用地
            if ("101".equals(blockType)) {
                request.setAttribute("modeNameEn", "gyxydgp_notice_mode.doc");
            } else {// 经营性用地
                request.setAttribute("modeNameEn", "jyxydgp_notice_mode.doc");
            }
        }
        
        request.setAttribute("pageList", list);
        
        List districtList = tdscNoticeService.tidyDistrictList(list, tdscBlockAppView);

        request.setAttribute("districtList", districtList);
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        request.setAttribute("appIdList", appIdList);
        request.setAttribute("noticeName", noticeName);
        request.setAttribute("noticeNo", noticeNo);
        request.setAttribute("noticeId", noticeId);
        //request.setAttribute("appIds", appIds);
        //request.setAttribute("modeNameEn", "notice_mode.doc");

        return mapping.findForward("upLoadNoticePage");

    }

    /**
     * 下载公告模版
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward downLoadNoticeModel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 获取页面参数
        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        String noticeName = tdscNoticeForm.getNoticeName();
        String noticeNo = tdscNoticeForm.getNoticeNo();
        String noticeId = tdscNoticeForm.getNoticeId();

        // 查询列表
        List list = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
        request.setAttribute("pageList", list);

        request.setAttribute("noticeName", noticeName);
        request.setAttribute("noticeNo", noticeNo);
        request.setAttribute("noticeId", noticeId);

        return mapping.findForward("downLoadNoticeModel");
    }

    /**
     * 对已上传的出让公告进行编辑
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward editUploadNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String noticeId = request.getParameter("noticeId");
        TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
        String noticeNameCN = "";
        String noticeFileName = "";
        String noticeName = "";
        String noticeNo = "";

        if (!"".equals(noticeId)) {
            tdscNoticeApp = (TdscNoticeApp) tdscNoticeService.findNoticeAppByNoticeId(noticeId);
        }
        if (tdscNoticeApp != null) {
            noticeNameCN = tdscNoticeApp.getFileUrl();
            noticeFileName = tdscNoticeApp.getNoticeId();
            noticeFileName += ".doc";
            noticeName = tdscNoticeApp.getNoticeName();
            noticeNo = tdscNoticeApp.getNoticeNo();
        }

        request.setAttribute("noticeNameCN", noticeNameCN);
        request.setAttribute("noticeFileName", noticeFileName);
        request.setAttribute("noticeId", noticeId);
        request.setAttribute("noticeNo", noticeNo);
        request.setAttribute("noticeName", noticeName);
        /**
         * 通过notice取得noticeNo,noticeName,fileurl
         */
        List appIdsList = tdscNoticeService.getAppIdsByNoticeId(noticeId);
        request.setAttribute("appIdsList", appIdsList);

        return mapping.findForward("editUpLoadNotice");
    }
    
    /**
     * 进入模板修改上传页面
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward toUploadWord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
    	String type = request.getParameter("type");
    	
    	if("download".equals(type)){
    		return mapping.findForward("toWordList");
    	}
    	
    	return mapping.findForward("toUploadWord");
    }
    
    /**
     *进入模板修改上传的备份页面
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward toUploadWordBak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    	String currPath = (String)request.getParameter("currPath");
    	
    	request.setAttribute("currPath", currPath);
    	return mapping.findForward("toUploadWordBak");
	}
    /**
     * 对已上传的出让公告进行编辑
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward uploadWord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // 要下載的文件名      
        String fileName=request.getParameter("fileName");   
        
        // 文件真实地址
        String filePath = request.getParameter("filePath");   
        
        String filePathName = filePath+ File.separator +fileName;   
        //filePathName=new String(filePathName.getBytes("iso-8859-1"),"GBK");
        
        // 设置内容格式
        response.setContentType("text/x-msdownload");
        
        // 设置文件头信息
        response.setHeader("Content-Disposition","attachment; filename=" + fileName + "");
        
        OutputStream os = response.getOutputStream();
        InputStream is = new FileInputStream(filePathName);
        byte[] buffer = new byte[1024];
        int i = -1;
        while ((i = is.read(buffer)) != -1) {
            os.write(buffer, 0, i);
        }
        os.flush();
        os.close();
        is.close();
        
        return null;
    }

}
