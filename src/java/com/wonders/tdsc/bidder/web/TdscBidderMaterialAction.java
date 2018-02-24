package com.wonders.tdsc.bidder.web;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderMaterialService;
import com.wonders.tdsc.bo.TdscBidderProvide;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBusinessRecordCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;

public class TdscBidderMaterialAction extends BaseAction {

    private TdscBidderMaterialService tdscBidderMaterialService;

    public void setTdscBidderMaterialService(TdscBidderMaterialService tdscBidderMaterialService) {
        this.tdscBidderMaterialService = tdscBidderMaterialService;
    }

    /**
     * ����������þ��������б�
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
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ��ȡҳ�����
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        bindObject(condition, form);

        /** ����������еĽڵ���Ҫ���ýڵ��� */
        condition.setNodeId(FlowConstants.FLOW_NODE_FILE_GET);
        // ����ҳ������
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        condition.setOrderKey("blockNoticeNo");
        // ����û���Ϣ
        UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        // ��ѯ�б�
        request.setAttribute("pageList", tdscBidderMaterialService.findPageList(condition, user));
        request.setAttribute("condition", condition);

        return mapping.findForward("materialBlockList");
    }

    /**
     * ��iweboffice��ʾ��ӡҳ�����Ϣ
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward bidderMaterialDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        condition.setAppId(request.getParameter("appId"));
        String printType = request.getParameter("printType");
        TdscBlockAppView tdscBlockAppView = tdscBidderMaterialService.detailTdscTranAppById(condition);
        //��ӡ����������ɵ�Document�ļ���·��
        String mFilePath=request.getSession().getServletContext().getRealPath("")+"\\iweboffice\\Document\\";
        //�жϸ��ļ����Ƿ���ڣ����������½���
        File dirname = new File(mFilePath);
        if(!dirname.exists()){
        	dirname.mkdir();
        }
        long start = System.currentTimeMillis();
        String tempStr = tdscBlockAppView.getTempStr();
        //����������
        //tdscBidderMaterialService.creatBarImage(tempStr, mFilePath+"provide_"+start+".jpg");
        request.setAttribute("barName", "provide_"+start+".jpg");
        if ("1".equals(printType)){
            request.setAttribute("modeName", "");
            request.setAttribute("bidderType", "1");
            request.setAttribute("modeNameEn", "dandu.doc");
        }else{
            request.setAttribute("modeName", "");
            request.setAttribute("bidderType", "2");
            request.setAttribute("modeNameEn", "lianhe.doc");        	
        }
            
        request.setAttribute("tdscBlockAppView", tdscBlockAppView);
        return mapping.findForward("print_iweboffice");      
    }
    /**
     * ��web��ʾ��ӡҳ��
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward bidderMaterialDetailForWeb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(request.getParameter("appId"));
		String printType = request.getParameter("printType");
		TdscBlockAppView tdscBlockAppView = tdscBidderMaterialService.detailTdscTranAppById(condition);
		
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		if ("1".equals(printType)){
			request.setAttribute("bidderType", "1");
			return mapping.findForward("print_ddjm");
		}else{
			request.setAttribute("bidderType", "2");
			return mapping.findForward("print_lhjm");
		}   
	}  
    

    /**
     * ��ӡȷ��
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
    /*
     * public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception { //
     * ��ȡҳ����� TdscBidderCondition condition = new TdscBidderCondition(); bindObject(condition, form); // ����ҳ������ condition.setPageSize(((Integer)
     * DicPropertyUtil.getInstance().getPropertyValue( GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue()); // ��ѯ�б�
     * request.setAttribute("pageList", tdscBidderMaterialService.findPageList(condition)); request.setAttribute("condition", condition);
     * 
     * //ȡ����¼�û��ɣ� SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_NK_USER_INFO); String
     * uname=String.valueOf(user.getUserID());
     * 
     * tdscBidderMaterialService.updateTdscBusinessRecordById(request.getParameter("appId"),uname);
     * 
     * return mapping.findForward("materialBlockList"); }
     */
    /**
     * ���´���ҵ���¼���ؿ齻����Ϣ��
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward updateMaterialCount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        String appId=(String)request.getParameter("appId");
        String mFileName=(String) request.getParameter("mFileName");
        condition.setAppId(appId);
        //��ѯTdscBlockAppView
        TdscBlockAppView tdscBlockAppView = tdscBidderMaterialService.getTdscTranAppById(condition);
        // ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        //�жϸ��û��Ƿ�Ϊ������������
        List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
        Date nowDate = new Date();

        TdscBidderProvide tdscBidderProvide = new TdscBidderProvide();
        tdscBidderProvide.setAppId(tdscBlockAppView.getAppId());
        tdscBidderProvide.setUserId(user.getUserId());
        //���� ����ID
        String districtId = user.getRegionId();
        tdscBidderProvide.setRegionId(new Integer(districtId));
        tdscBidderProvide.setBidType(request.getParameter("bidderType"));
        tdscBidderProvide.setProvideBm(request.getParameter("provideBm"));
        tdscBidderProvide.setProvideDate(nowDate);
        String userIdNum="0";
        String countNum="0";
        if(buttonList!=null){
        	tdscBidderMaterialService.updateFfcl(tdscBidderProvide);
        	//����appId��userId��ѯ�ô�������ķ���-----�����ѯCondition
        	TdscBusinessRecordCondition busiRecCondition =new TdscBusinessRecordCondition ();
        	busiRecCondition.setAppId(appId);
        	busiRecCondition.setUserId(String.valueOf(user.getUserId()));
        	//���ز�ѯ���
        	userIdNum=tdscBidderMaterialService.queryNumByUserId(busiRecCondition);
        	countNum=tdscBidderMaterialService.queryNumByAppIdId(busiRecCondition);
        }


        String resultText=tdscBlockAppView.getAppId()+","+userIdNum+","+countNum;
        
        // ���������õ����
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        // ���ظ��ص������Ĳ���
        pw.write(resultText);
        pw.close();
        //ɾ�����δ�ӡ�����ɵ�������
        barImageDel(mFileName,request);
        return null;

    }
   
    /**
     * ɾ�����δ�ӡ�����ɵ�������(jspҳ����õķ���)
     * @param request
     * @param response
     * @throws Exception
     */
    public void barImageDel(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        //��ӡ����������ɵ�Document�ļ���·��
        String mFilePath=request.getSession().getServletContext().getRealPath("")+"\\iweboffice\\Document\\";
        String mFileName=(String) request.getParameter("mFileName");
        //����File
        File file = new File(mFilePath+mFileName);
        //ɾ��
        if (file.isFile()) {
        	try{
        		file.delete();
        	}catch(Exception e){
        		e.getMessage();
        	}
		}
    }
    /**
     * ɾ�����δ�ӡ�����ɵ������루Action�е��õķ�����
     * @param request
     * @param response
     * @throws Exception
     */
    public void barImageDel(String mFileName,HttpServletRequest request) throws Exception {
        //��ӡ����������ɵ�Document�ļ���·��
        String mFilePath=request.getSession().getServletContext().getRealPath("")+"\\iweboffice\\Document\\";
        //����File
        File file = new File(mFilePath+mFileName);
        //ɾ��
        if (file.isFile()) {
        	try{
        		file.delete();
        	}catch(Exception e){
        		e.getMessage();
        	}
		}
    }
}
