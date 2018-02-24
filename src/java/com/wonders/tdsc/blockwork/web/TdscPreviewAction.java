package com.wonders.tdsc.blockwork.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscPreviewService;
import com.wonders.tdsc.blockwork.web.form.PreviewInfoForm;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockScheduleTable;
import com.wonders.tdsc.bo.TdscExplorInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscPreviewAction extends BaseAction {
    
    private CommonQueryService commonQueryService;
    
    private TdscPreviewService tdscPreviewService;
    
	private AppFlowService appFlowService;

    public void setTdscPreviewService(TdscPreviewService tdscPreviewService) {
        this.tdscPreviewService = tdscPreviewService;
    }

    public void setCommonQueryService(CommonQueryService commonQueryService) {
        this.commonQueryService = commonQueryService;
    }
    
	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}
    
    /**
	 * ��ѯ����ҵ���б�
	 */
	public ActionForward queryAppListWithNodeId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String blockName = request.getParameter("blockName");
		String blockType = request.getParameter("blockType");
		String transferMode = request.getParameter("transferMode");
        String districtId = request.getParameter("districtId");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		String nodeId = "06";

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(blockName);
		condition.setBlockType(blockType);
		condition.setTransferMode(transferMode);
        condition.setDistrictId(districtId);
		condition.setNodeId(nodeId);
		condition.setCurrentPage(cPage);
		condition
				.setPageSize(((Integer) DicPropertyUtil.getInstance()
						.getPropertyValue(
								GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER))
						.intValue());
		condition.setUser(user);
		request.setAttribute("nodeId", nodeId);
		
		//��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		//ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for ( int j = 0; j < buttonList.size() ; j++){
			String id = (String)buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		//�ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null){
			condition.setUserId(user.getUserId());
		}
		
		request.setAttribute("queryAppList", commonQueryService
				.queryTdscBlockAppViewPageList(condition));// ��������ѯ�б�
		request.setAttribute("queryAppCondition", condition);

		return mapping.findForward("list");
	}
    /**
     * ����ҵ����ģ���ֳ��������
     * 20071116*

    public ActionForward queryAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        return mapping.findForward("list");
    }
    */
    /**
     * ��ӡ�����ǩ����
     * @param appId
     * 20071113*
     */
    public ActionForward toPrintSign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
       
        //���ҳ�����appId
        String appId = request.getParameter("appId");
        if(appId != null){
            //��ò�ѯ����
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            //���ù�������ȡ�ù�����Ϣ
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            //�½��ؿ齻����Ϣ��(TDSC_BLOCK_TRAN_APP)���ؿ������Ϣ��(TDSC_BLOCK_INFO)�Ķ���
            if (commonInfo != null){
                //ȡ�õؿ齻����Ϣ���ؿ������Ϣ�����Ϣ
                request.setAttribute("commonInfo", commonInfo);
            }
        }
        return mapping.findForward("toPrint");
    }
    
    /**
     * ¼���ֳ������������Ϣ
     * @param appId
     * 20071114*
     */
    public ActionForward toPreviewInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        //���ҳ�����appId
        String appId = request.getParameter("appId");
        String recordId = "";
        //String nodeId = request.getParameter("nodeId");
        //String statusId = request.getParameter("statusId");
        if(null != appId){
            //��ò�ѯ����
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            //���ù�������ȡ�ù�����Ϣ
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            if (commonInfo != null){
                request.setAttribute("commonInfo", commonInfo);
            }
//          ͨ���ؿ�ID����ѯ������;��Ϣ
            TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            //ͨ��appId�õ���ͼTdscBlockAppView�е�blockId
            TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
            String blockId = tdscBlockAppView.getBlockId();
            //����blockId�õ��ؿ���;��Ϣ���еĹ滮��;�ͳ�������
            List tdscBlockUsedInfo = tdscPreviewService.queryTdscBlockUsedInfoList(blockId);
            request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);
//          ͨ��appId����ѯ���õؿ����ִ�б����Ϣ
            TdscBlockScheduleTable tempBlockScheduleTable = tdscPreviewService.findScheduleInfo(appId);
            if(null != tempBlockScheduleTable){
                request.setAttribute("tempBlockScheduleTable", tempBlockScheduleTable);
            }
            //ͨ��appId����ѯ�������������Ϣ
            TdscExplorInfo tempExplorInfo = tdscPreviewService.findExplorInfo(appId);
            if(null != tempExplorInfo){
            	recordId = tempExplorInfo.getRecordId();
                request.setAttribute("tempExplorInfo", tempExplorInfo);
            }

        }
        request.setAttribute("appId", appId);
        request.setAttribute("recordId", recordId);
        request.setAttribute("modeNameEn", "reply_summary.doc");
        return mapping.findForward("previewInfo");
    }
    
    /**
     * �����ֳ������������Ϣ
     * @param appId
     * 20071114*
     */
    public ActionForward savePreviewInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
       
        //���ҳ���ϵ�appId,saveType
        String appId = request.getParameter("appId");
        String saveType = request.getParameter("saveType");
        String transferMode = request.getParameter("transferMode");
        SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String recordId = request.getParameter("RecordID");
        String blockNoticeNo = request.getParameter("blockNoticeNo");
        request.setAttribute("recordId", recordId);
        //���ҳ���ϵ�tempSaveFlag,�ж��Ƿ��ݴ�
        String tempSaveFlag = request.getParameter("tempSaveFlag");
        //����ActionForm
        PreviewInfoForm infoForm = (PreviewInfoForm) form;
        //��form�е�ֵ����bo����Ӧ���õؿ����ִ�б�
        TdscBlockScheduleTable tdscBlockScheduleTable = new TdscBlockScheduleTable();
        tdscBlockScheduleTable.setAppId(appId);
        
        bindObject(tdscBlockScheduleTable, infoForm);
        tdscBlockScheduleTable.setInspDate(infoForm.getInspDate());
        //��form�е�ֵ����bo����Ӧ����������
        TdscExplorInfo tdscExplorInfo = new TdscExplorInfo();
        tdscExplorInfo.setExplorId(appId);
        bindObject(tdscExplorInfo, infoForm);
        
        //�ļ��ϴ�start
        String appRealPath = request.getSession().getServletContext().getRealPath("/");
        String fileName = null;
		String filePath = null;
		String time = null;
		Date dd = new Date();
		time = StringUtils.trimToEmpty(DateUtil.date2String(dd, DateUtil.FORMAT_DATE).replaceAll("-", ""));
		time += StringUtils.trimToEmpty(DateUtil.date2String(dd, DateUtil.FORMAT_TIME).replaceAll(":", ""));
		
		
		FormFile file = null;
		if(infoForm.getFile()!=null){
			try {
				file = infoForm.getFile();//ȡ���ϴ����ļ�
				InputStream stream = file.getInputStream();//���ļ�����
				String uplodFilePathName = file.getFileName();
	            fileName = uplodFilePathName.substring(uplodFilePathName.lastIndexOf(File.separator) + 1);
				String endStyle =fileName.substring(uplodFilePathName.lastIndexOf("."));
	            //filePath = appRealPath+"dk"+File.separator+"upload"+File.separator+"tempFile"+File.separator+System.currentTimeMillis()+endStyle;
				filePath = appRealPath+"tdsc"+File.separator+"uploadfile"+File.separator+"previewfile"+File.separator+blockNoticeNo+"_"+time+"_"+fileName;

	            File fileOnServer = new File(filePath);
				OutputStream bos = new FileOutputStream(fileOnServer);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
			} catch (Exception e) {
	            e.printStackTrace();
	        }
	    } else {
	        System.out.println("the enctype must be multipart/form-data");
	    }
		//�ļ��ϴ�end
		
        //ִ�в�������
		tdscExplorInfo.setFileName(blockNoticeNo+"_"+time+"_"+fileName);
        tdscPreviewService.savePreviewInfo(tdscBlockScheduleTable, tdscExplorInfo, appId,saveType,transferMode,user);

        request.setAttribute("saveMessage", "����ɹ�");
 
        //tempSaveFlag��Ϊ��ʱ,�����ݴ�ҳ��
        if(null != tempSaveFlag){
            //ͨ��appId����ѯ���õؿ����ִ�б����Ϣ
            TdscBlockScheduleTable tempBlockScheduleTable = tdscPreviewService.findScheduleInfo(appId);
            request.setAttribute("tempBlockScheduleTable", tempBlockScheduleTable);
            //ͨ��appId����ѯ�������������Ϣ
            TdscExplorInfo tempExplorInfo = tdscPreviewService.findExplorInfo(appId);
            request.setAttribute("tempExplorInfo", tempExplorInfo);
            request.setAttribute("appId", appId);
            if(null != appId){
                //��ò�ѯ����
                TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
                baseCondition.setAppId(appId);
                //���ù�������ȡ�ù�����Ϣ
                TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);

                if (commonInfo != null){
                    request.setAttribute("commonInfo", commonInfo);
                }

            //ͨ��appId�õ���ͼTdscBlockAppView�е�blockId
            String blockId = commonInfo.getBlockId();
            //����blockId�õ��ؿ���;��Ϣ���еĹ滮��;�ͳ�������
            List tdscBlockUsedInfo = tdscPreviewService.queryTdscBlockUsedInfoList(blockId);
            request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);
            }
        }
        if(null != tempSaveFlag){
            return mapping.findForward("toTemp");
        }
        return new ActionForward("/tdsc/preview.do?method=queryAppListWithNodeId");
    }
    /**
     * ¼����ɼ�Ҫ
     * @param appId
     */
    public ActionForward luruDyjy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //���ҳ�����appId
        String appId = request.getParameter("appId");
        String recordId = request.getParameter("RecordId");
        if(recordId == null || "".equals(recordId)){
            //ͨ��appId����ѯ�������������Ϣ
            TdscExplorInfo tempExplorInfo = tdscPreviewService.findExplorInfo(appId);
            if(null != tempExplorInfo){
            	recordId = tempExplorInfo.getRecordId();
                request.setAttribute("tempExplorInfo", tempExplorInfo);
            }
        }
        request.setAttribute("appId", appId);
        request.setAttribute("recordId", recordId);
        request.setAttribute("modeNameEn", "reply_summary.doc");
        return mapping.findForward("editReply");
    }
}
