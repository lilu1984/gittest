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
     * ��ѯ���������ύ�Ĺ����б�
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ��ȡҳ�����
        TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
        // ����ӳ��ǹ�������ҳ�淵�أ���bindObject��ѯ����������������ʾ�����еļ�¼
        String forwardType = request.getParameter("forwardType");
        if (!"1".equals(request.getAttribute("forwardType")))
            bindObject(condition, form);
        if ("1".equals(forwardType)) {
            condition.setNoticeNo("");
            condition.setNoticeName("");
        }

        if (request.getParameter("currentPage") != null)
            condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

        // ���ù���״̬Ϊ�ݴ�
        condition.setNoticeStatus("00");

        /** ����������еĽڵ���Ҫ���ýڵ��� */
        // condition.setNodeId(GlobalConstants.FLOW_NODE_FILE_GET);
        // ����ҳ������
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        // ��ѯ�б�
        request.setAttribute("pageList", tdscNoticeService.findPageList(condition));
        request.setAttribute("condition", condition);

        request.setAttribute("oldNoticeNo", "");
        return mapping.findForward("tdscNoticeAppList");
    }

    // ��ѯ�����п��������ù����������Ϣ�б�
    public ActionForward queryGgList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // ��ȡҳ�����
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        bindObject(condition, form);

        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
        condition.setBlockName(request.getParameter("blockName"));
        condition.setTransferMode(request.getParameter("transferMode"));
        condition.setStatusId("0303");
        condition.setNoitceNo("null");

        /** ����������еĽڵ���Ҫ���ýڵ��� */
        // condition.setNodeId(GlobalConstants.FLOW_NODE_FILE_GET);
        // ����ҳ������
        // condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        String getOldNoticeNo = (String) request.getParameter("oldNoticeNo");
        if (getOldNoticeNo != null && !"null".equals(getOldNoticeNo)) {
            // ��ѯ��noticeNo="null"���Ѿ��ݴ��������Ϣ
            List list = tdscNoticeService.findTdscBlockAppViewList(condition, getOldNoticeNo);
            request.setAttribute("list", list);
        } else {
            // ��ѯ�б�
            List newList = commonQueryService.queryTdscBlockAppViewList(condition);
            request.setAttribute("list", newList);
        }

        /*
         * //ȡ���ݴ�ʱ�Ѵ����б��е�������Ϣ String getOldNoticeNo=(String) request.getParameter("oldNoticeNo"); int flag=0; if(getOldNoticeNo!=null){
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
     * ��ѯ������Ϣ�б�
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

        // ��ѯ�б�
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
     * �ύ���ù���
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

     
        // �����ļ���
        TdscNoticeForm fileForm = (TdscNoticeForm) form;
//        FormFile upLoadFile = (FormFile) fileForm.getFileName();

        String appIds[] = fileForm.getAppIds();

        String noticeStatus = request.getParameter("noticeStatus");
        // ��ȡҳ�����
        TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
        tdscNoticeApp.setNoticeNo(request.getParameter("noticeNo"));
        tdscNoticeApp.setNoticeName(request.getParameter("noticeName"));
        tdscNoticeApp.setNoticeType("01");
        tdscNoticeApp.setNoticeStatus(noticeStatus);
    

        // ȡ����һ���ݴ��noticeId,noticeNo
        String getOldNoticeNo = (String) request.getParameter("oldNoticeNo");
        String getOldNoticeId = (String) request.getParameter("oldNoticeId");

        tdscNoticeService.save(tdscNoticeApp, appIds, getOldNoticeId, getOldNoticeNo);

        // ������ɳ��ù��棬forwardType=1����ѯҳ����Բ�bindObject��ѯ����
        request.setAttribute("forwardType", "1");

        // �����ݴ�ǰ��noticeNo
        request.setAttribute("oldNoticeNo", "");

        // �����ݴ�ǰ��noticeId
        request.setAttribute("oldNoticeId", "");

        return mapping.findForward("successSave");
    }

    /**
     * �м���ת�����ڽ��������ù���ʱ������ҳ����ѡ�е�������Ϣ������ҳ�棬��ҳ��Ͳ���ʾ�ü�¼
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
     * �ݴ���ù���
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

        // �����ļ���
        TdscNoticeForm fileForm = (TdscNoticeForm) form;
//        FormFile upLoadFile = (FormFile) fileForm.getFileName();

        String appIds[] = fileForm.getAppIds();

        // ��ȡҳ�����
        TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
        tdscNoticeApp.setNoticeNo(request.getParameter("noticeNo"));
        tdscNoticeApp.setNoticeName(request.getParameter("noticeName"));
        tdscNoticeApp.setNoticeType("01");
        tdscNoticeApp.setNoticeStatus("00");

        // ������ɳ��ù��棬forwardType=1����ѯҳ����Բ�bindObject��ѯ����
        request.setAttribute("forwardType", "1");

        String getOldNoticeNo = (String) request.getParameter("oldNoticeNo");
        String getOldNoticeId = (String) request.getParameter("oldNoticeId");

        //
        tdscNoticeService.update(tdscNoticeApp, appIds, getOldNoticeId, getOldNoticeNo);

        // ͨ��ҳ�洫����appIdsȡ��������Ϣ�б����
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

        // ����������Ϣ�б�
        request.setAttribute("tdscBlockAppViewList", list);

        // �����ݴ�ǰ��noticeNo
        request.setAttribute("oldNoticeNo", tdscNoticeApp.getNoticeNo());

        // �����ݴ�ǰ��noticeId
        request.setAttribute("oldNoticeId", tdscNoticeApp.getNoticeId());

        // ��ҳ�������ֵ����
        request.setAttribute("tdscNoticeApp", tdscNoticeApp);

        // ���ص���ҳ
        return mapping.findForward("bianji");
    }

    /**
     * �༭���ù���
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
        // ͨ��noticeId��ѯ�����ù�����Ϣ��һ��Ӧ��һ����¼TdscNoticeApp
        // �ٲ�ѯ�ؿ齻����Ϣ����noticeId�ͳ��ù�����Ϣ��noticeId��ͬ�ļ�¼TdscBlockTranApp
        // ��������Ϣ���ظ�ҳ��

        // ����������е���Ϣ
        String noticeId = request.getParameter("noticeId");
        TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
        request.setAttribute("tdscNoticeApp", tdscNoticeApp);

        // ��ѯ��appIds
        List tdscBlockAppViewList = new ArrayList();
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setNoitceNo(tdscNoticeApp.getNoticeNo());
        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_GET);
        tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);

        /*
         * List appIdList = new ArrayList(); appIdList = tdscNoticeService.getAppIdsByNoticeId(noticeId); // ��ѯ�б� // PageList newList =
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

        // �����ݴ�ǰ��noticeNo
        request.setAttribute("oldNoticeNo", tdscNoticeApp.getNoticeNo());

        // �����ݴ�ǰ��noticeId
        request.setAttribute("oldNoticeId", tdscNoticeApp.getNoticeId());

        return mapping.findForward("bianji");
    }

    /**
     * ɾ�����ù���
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

        // ������ɳ��ù��棬forwardType=1����ѯҳ����Բ�bindObject��ѯ����
        request.setAttribute("forwardType", "1");

        // �����ݴ�ǰ��noticeNo
        request.setAttribute("oldNoticeNo", "");

        // �����ݴ�ǰ��noticeId
        request.setAttribute("oldNoticeId", "");

        return mapping.findForward("successSave");
    }

    /**
     * �°���ù���
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
        // ��ȡҳ�����
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        // ������ύ����ҳ����룬��bindObject��ѯ����������������ʾ�����еļ�¼
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
            oldNoticeNo = "������[" + oldNoticeNo.substring(0, 4) + "]��" + oldNoticeNo.substring(4) + "��";
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

        // �����һ�ν���ҳ�����������ţ��Ժ�ÿ�β�ѯ���ٲ����µĹ����
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
     * ���ɹ���ģ��
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

        // ��ȡҳ�����
        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        String noticeName = tdscNoticeForm.getNoticeName();
        String noticeNo = tdscNoticeForm.getNoticeNo();
        String noticeId = tdscNoticeForm.getNoticeId();

        // ��ѯ�б�
        List list = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
        request.setAttribute("pageList", list);

        request.setAttribute("noticeName", noticeName);
        request.setAttribute("noticeNo", noticeNo);
        request.setAttribute("noticeId", noticeId);

        return mapping.findForward("noticeModel");
    }

    // ��Ҫͬʱ�ϴ��û��༩���word�ļ�
    public ActionForward addNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // ��ȡҳ�����
        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        String noticeName = tdscNoticeForm.getNoticeName();
        String noticeNo = tdscNoticeForm.getNoticeNo();
        String recordId = request.getParameter("RecordID");
        String modeNameEn = request.getParameter("modeNameEn");

        String noticeStatus = request.getParameter("noticeStatus");

        String noticeId = request.getParameter("noticeId");

        TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
        
        // ����ǰ��Ҫͨ��noticeIdɾ��ԭ�еļ�¼
        //tdscNoticeService.saveNotice(appIds ,noticeNo, noticeName, noticeStatus, noticeId, modeNameEn, recordId);

        // ������ύ���棬����Ҫ����ÿ��������Ϣ�Ĵ������
        if ("01".equals(noticeStatus)) {
            // ����û���Ϣ
    		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

            TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
            // ��appId[]��װ��appIdList
            List appIdList = new ArrayList();
            for (int i = 0; i < appIds.length; i++) {
                appIdList.add(appIds[i]);
            }
            condition.setAppIdList(appIdList);
            // ͨ��appIdList�������List
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

        // ͨ��noticeId��ѯ�����ù�����Ϣ��һ��Ӧ��һ����¼TdscNoticeApp
        // �ٲ�ѯ�ؿ齻����Ϣ����noticeId�ͳ��ù�����Ϣ��noticeId��ͬ�ļ�¼TdscBlockTranApp
        // ��������Ϣ���ظ�ҳ��

        // ����������е���Ϣ
        String noticeId = request.getParameter("noticeId");
        TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
        request.setAttribute("tdscNoticeApp", tdscNoticeApp);

        // ��ѯ��appIds
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
     * ����ͨ�ýӿڻ��������Ϣ
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
     * ��ѯ����ҵ���б�
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
        request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewFlowPageList(condition));// ��������ѯ�б�
        request.setAttribute("queryAppCondition", condition);

        return mapping.findForward("list");
    }

    /**
     * ��������������,�ڵڶ�����ù���Ļ����������˳��ù���Ԥ����ģ�����أ������ϴ�����
     */

    /**
     * ���빫��ģ���ϴ�����ҳ��
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

        // ��ȡҳ�����
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
        
//        //�ж�TdscNoticeApp���е�recordIdֵ�Ƿ�Ϊ��
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
//      ��ѯ�б�
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
        //�б�
        if ("3107".equals(transferMode)) {
            // ��ҵ���õ�
            if ("101".equals(blockType)) {
                request.setAttribute("modeNameEn", "gyxydzb_notice_mode.doc");
            } else {// ��Ӫ���õ�
                request.setAttribute("modeNameEn", "jyxydzb_notice_mode.doc");
            }
        }

        // ����
        if ("3103".equals(transferMode)) {
            // ��ҵ���õ�
            if ("101".equals(blockType)) {
                request.setAttribute("modeNameEn", "gyxydpm_notice_mode.doc");
            } else {// ��Ӫ���õ�
                request.setAttribute("modeNameEn", "jyxydpm_notice_mode.doc");
            }
        }

        // ����
        if ("3104".equals(transferMode)) {
            // ��ҵ���õ�
            if ("101".equals(blockType)) {
                request.setAttribute("modeNameEn", "gyxydgp_notice_mode.doc");
            } else {// ��Ӫ���õ�
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
     * ���ع���ģ��
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

        // ��ȡҳ�����
        TdscNoticeForm tdscNoticeForm = (TdscNoticeForm) form;
        String appIds[] = tdscNoticeForm.getAppIds();
        String noticeName = tdscNoticeForm.getNoticeName();
        String noticeNo = tdscNoticeForm.getNoticeNo();
        String noticeId = tdscNoticeForm.getNoticeId();

        // ��ѯ�б�
        List list = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
        request.setAttribute("pageList", list);

        request.setAttribute("noticeName", noticeName);
        request.setAttribute("noticeNo", noticeNo);
        request.setAttribute("noticeId", noticeId);

        return mapping.findForward("downLoadNoticeModel");
    }

    /**
     * �����ϴ��ĳ��ù�����б༭
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
         * ͨ��noticeȡ��noticeNo,noticeName,fileurl
         */
        List appIdsList = tdscNoticeService.getAppIdsByNoticeId(noticeId);
        request.setAttribute("appIdsList", appIdsList);

        return mapping.findForward("editUpLoadNotice");
    }
    
    /**
     * ����ģ���޸��ϴ�ҳ��
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
     *����ģ���޸��ϴ��ı���ҳ��
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
     * �����ϴ��ĳ��ù�����б༭
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
        // Ҫ���d���ļ���      
        String fileName=request.getParameter("fileName");   
        
        // �ļ���ʵ��ַ
        String filePath = request.getParameter("filePath");   
        
        String filePathName = filePath+ File.separator +fileName;   
        //filePathName=new String(filePathName.getBytes("iso-8859-1"),"GBK");
        
        // �������ݸ�ʽ
        response.setContentType("text/x-msdownload");
        
        // �����ļ�ͷ��Ϣ
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
