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
        // ��ȡҳ�����
        TdscReplyConfInfoCondition condition = new TdscReplyConfInfoCondition();

        bindObject(condition, form);
        PageList pageList =new PageList();
        if (request.getParameter("currentPage") != null)
            condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

        // ����ҳ������
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        // ��ѯ�б�
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
     * ������ɼ�Ҫ
     */
    public ActionForward saveFabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ��ȡҳ�����
        String appId = request.getParameter("appId");
        
        TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp)tdscBlockInfoService.findTdscBlockTranApp(appId);
        
        String transferMode = (String)tdscBlockTranApp.getTransferMode();
        //�޸� ʵ��� �Ƿ񷢲���״̬ ����¼����ʱ��
        tdscReplyRecordService.modTdscReplyConfInfo(appId);
        
        // ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String userId = user.getUserId()+"";
        commonFlowService.postAppNode(appId,FlowConstants.FLOW_NODE_FAQ_RELEASE, transferMode, userId);
        
        return query(mapping,form,request,response);
    }
    
    public ActionForward fabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//      ��ȡҳ�����
        String appId = request.getParameter("appId");
        request.setAttribute("appId", appId);
        request.setAttribute("modeNameEn", "reply_summary.doc");
        String recordId = "";
//      ͨ��appId����ѯ���ɻ���Ϣ�����Ϣ
        TdscReplyConfInfo tempReplyConfInfo = tdscAnswerService.findReplyConfInfo(appId);
        if(null != tempReplyConfInfo){
            request.setAttribute("tempReplyConfInfo", tempReplyConfInfo);
            recordId = tempReplyConfInfo.getRecordId();
            request.setAttribute("recordId", recordId);
        }
        return mapping.findForward("toFabu");
    }
    
    /**
     * ���ش�����Ϣ�ļ�������
     * 08.03.07
     */
    public ActionForward downLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//      ��ȡҳ�����
        String appId = request.getParameter("appId");
        request.setAttribute("appId", appId);
        request.setAttribute("modeNameEn", "reply_summary.doc");
        String recordId = "";
//      ͨ��appId����ѯ���ɻ���Ϣ�����Ϣ
        TdscReplyConfInfo tempReplyConfInfo = tdscAnswerService.findReplyConfInfo(appId);
        if(null != tempReplyConfInfo){
            request.setAttribute("tempReplyConfInfo", tempReplyConfInfo);
            recordId = tempReplyConfInfo.getRecordId();
            request.setAttribute("recordId", recordId);
        }
        return mapping.findForward("toDownLoad");
    }
 
}
