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
	 * 查询待办业务列表
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
		
		//获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		//转化为buttonMap
		Map buttonMap = new HashMap();
		for ( int j = 0; j < buttonList.size() ; j++){
			String id = (String)buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		//判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null){
			condition.setUserId(user.getUserId());
		}
		
		request.setAttribute("queryAppList", commonQueryService
				.queryTdscBlockAppViewPageList(condition));// 按条件查询列表
		request.setAttribute("queryAppCondition", condition);

		return mapping.findForward("list");
	}
    /**
     * 进入业务工作模块现场勘察管理
     * 20071116*

    public ActionForward queryAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        return mapping.findForward("list");
    }
    */
    /**
     * 打印勘查会签到表
     * @param appId
     * 20071113*
     */
    public ActionForward toPrintSign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
       
        //获得页面参数appId
        String appId = request.getParameter("appId");
        if(appId != null){
            //获得查询条件
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            //调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            //新建地块交易信息表(TDSC_BLOCK_TRAN_APP)、地块基本信息表(TDSC_BLOCK_INFO)的对象
            if (commonInfo != null){
                //取得地块交易信息表、地块基本信息表的信息
                request.setAttribute("commonInfo", commonInfo);
            }
        }
        return mapping.findForward("toPrint");
    }
    
    /**
     * 录入现场勘查会土地信息
     * @param appId
     * 20071114*
     */
    public ActionForward toPreviewInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        //获得页面参数appId
        String appId = request.getParameter("appId");
        String recordId = "";
        //String nodeId = request.getParameter("nodeId");
        //String statusId = request.getParameter("statusId");
        if(null != appId){
            //获得查询条件
            TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            //调用公共方法取得公共信息
            TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
            if (commonInfo != null){
                request.setAttribute("commonInfo", commonInfo);
            }
//          通过地块ID来查询土地用途信息
            TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
            baseCondition.setAppId(appId);
            //通过appId得到视图TdscBlockAppView中的blockId
            TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
            String blockId = tdscBlockAppView.getBlockId();
            //根据blockId得到地块用途信息表中的规划用途和出让年限
            List tdscBlockUsedInfo = tdscPreviewService.queryTdscBlockUsedInfoList(blockId);
            request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);
//          通过appId来查询出让地块进度执行表的信息
            TdscBlockScheduleTable tempBlockScheduleTable = tdscPreviewService.findScheduleInfo(appId);
            if(null != tempBlockScheduleTable){
                request.setAttribute("tempBlockScheduleTable", tempBlockScheduleTable);
            }
            //通过appId来查询勘查会情况表的信息
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
     * 保存现场勘查会土地信息
     * @param appId
     * 20071114*
     */
    public ActionForward savePreviewInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
       
        //获得页面上的appId,saveType
        String appId = request.getParameter("appId");
        String saveType = request.getParameter("saveType");
        String transferMode = request.getParameter("transferMode");
        SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        String recordId = request.getParameter("RecordID");
        String blockNoticeNo = request.getParameter("blockNoticeNo");
        request.setAttribute("recordId", recordId);
        //获得页面上的tempSaveFlag,判断是否暂存
        String tempSaveFlag = request.getParameter("tempSaveFlag");
        //调用ActionForm
        PreviewInfoForm infoForm = (PreviewInfoForm) form;
        //将form中的值传入bo，对应出让地块进度执行表
        TdscBlockScheduleTable tdscBlockScheduleTable = new TdscBlockScheduleTable();
        tdscBlockScheduleTable.setAppId(appId);
        
        bindObject(tdscBlockScheduleTable, infoForm);
        tdscBlockScheduleTable.setInspDate(infoForm.getInspDate());
        //将form中的值传入bo，对应勘查会情况表
        TdscExplorInfo tdscExplorInfo = new TdscExplorInfo();
        tdscExplorInfo.setExplorId(appId);
        bindObject(tdscExplorInfo, infoForm);
        
        //文件上传start
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
				file = infoForm.getFile();//取得上传的文件
				InputStream stream = file.getInputStream();//把文件读入
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
		//文件上传end
		
        //执行插入数据
		tdscExplorInfo.setFileName(blockNoticeNo+"_"+time+"_"+fileName);
        tdscPreviewService.savePreviewInfo(tdscBlockScheduleTable, tdscExplorInfo, appId,saveType,transferMode,user);

        request.setAttribute("saveMessage", "保存成功");
 
        //tempSaveFlag不为空时,跳到暂存页面
        if(null != tempSaveFlag){
            //通过appId来查询出让地块进度执行表的信息
            TdscBlockScheduleTable tempBlockScheduleTable = tdscPreviewService.findScheduleInfo(appId);
            request.setAttribute("tempBlockScheduleTable", tempBlockScheduleTable);
            //通过appId来查询勘查会情况表的信息
            TdscExplorInfo tempExplorInfo = tdscPreviewService.findExplorInfo(appId);
            request.setAttribute("tempExplorInfo", tempExplorInfo);
            request.setAttribute("appId", appId);
            if(null != appId){
                //获得查询条件
                TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
                baseCondition.setAppId(appId);
                //调用公共方法取得公共信息
                TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);

                if (commonInfo != null){
                    request.setAttribute("commonInfo", commonInfo);
                }

            //通过appId得到视图TdscBlockAppView中的blockId
            String blockId = commonInfo.getBlockId();
            //根据blockId得到地块用途信息表中的规划用途和出让年限
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
     * 录入答疑纪要
     * @param appId
     */
    public ActionForward luruDyjy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获得页面参数appId
        String appId = request.getParameter("appId");
        String recordId = request.getParameter("RecordId");
        if(recordId == null || "".equals(recordId)){
            //通过appId来查询勘查会情况表的信息
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
