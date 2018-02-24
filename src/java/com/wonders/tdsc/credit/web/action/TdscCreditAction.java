package com.wonders.tdsc.credit.web.action;


import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.model.Pager;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.tdsc.bo.TdscBidderCxApp;
import com.wonders.tdsc.bo.TdscCorpInfo;
import com.wonders.tdsc.bo.condition.TdscCorpInfoCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.credit.service.TdscCreditService;

/**
 * ���Ź���Action
 */
public class TdscCreditAction extends BaseAction{

	/**
	 * ���Ź���Service
	 */
    private TdscCreditService tdscCreditService;
	
	
	public TdscCreditService getTdscCreditService() {
		return tdscCreditService;
	}
	public void setTdscCreditService(TdscCreditService tdscCreditService) {
		this.tdscCreditService = tdscCreditService;
	}
	
	 /**
	  * ���ݲ�ѯ������ú������б�
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	
	 public ActionForward queryCreditListWithPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 //����û���Ϣ
		 /*SysUser user = (SysUser)request.getSession().getAttribute(GlobalConstants.SESSION_NK_USER_INFO);
		  * String userId = user.getUserID()+"";
		  */
		 //��ѯ����
		 TdscCorpInfoCondition condition = new TdscCorpInfoCondition();
		 //��ѯ��ʶ
		 String queryFlag =(String)request.getParameter("queryFlag");
		 
		 if(queryFlag == null) queryFlag=new String();
		 String currentPage = request.getParameter("currentPage");
		 if(currentPage != null){
		 condition.setCurrentPage(Integer.parseInt(currentPage));}
		 if(queryFlag.equals(GlobalConstants.DIC_VALUE_YESNO_YES)){
			 bindObject(condition,form);
		 }
		 /*	 condition.setUserId(userId);*/
		 //���ݲ�ѯ������ú������б�
		 PageList resultPageList = tdscCreditService.queryCreditList(condition);
		 List resultList = null;
		 Pager pager = null;
		 if(resultPageList != null){
			 resultList = resultPageList.getList();
			 pager = resultPageList.getPager();
		 }
		 
		 //��������
		 request.setAttribute("condition", condition);
		 request.setAttribute("resultList", resultList);
		 request.setAttribute("pager", pager);
 
        return mapping.findForward("toFindCreditList");
     }
	 

	 /**
	  * ��ת����ӻ�༭���ſ�ҳ��
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward toaddOrEditCreditInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 //��ȡflag=1 �༭�� flag=0 �¼�
		 String flag= (String)request.getParameter("flag");
		 
		 if(flag.equals("1")){
		 //��ȡ��ҵ����Ȼ�˻�����ϢID
		 String corpId = (String)request.getParameter("corpId");	 
		 //��ѯ����
	//	 YsqsCorpInfoCondition condition = new YsqsCorpInfoCondition();
	//	 condition.setCorpId(corpId);
		 //����������ѯ��һ����Ϣ
		 TdscCorpInfo tdscCorpInfo = new TdscCorpInfo();
		 //YsqsBidderCxApp ysqsBidderCxApp = new YsqsBidderCxApp();
		 
		 tdscCorpInfo = tdscCreditService.lazyLoadAppByCorpId(corpId);
		 //ysqsBidderCxApp = ysqsCreditService.queryCxAppByCorpId(corpId);
		 
		 request.setAttribute("tdscCorpInfo", tdscCorpInfo);
		 //request.setAttribute("ysqsBidderCxApp", ysqsBidderCxApp );
		 		 
		 }
		 //��������
		 request.setAttribute("flag",flag);
		 //����ͬ������,��ֹ�ظ��ύ
		 saveToken(request); 
 
        return mapping.findForward("toAddOrEditCreditInfo");
     }

	 /**
	  * ������ӻ�༭��ĳ��ſ���Ϣ���� 
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward baocunCreditInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
	     //����û���Ϣ
		 SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
         String userId = user.getUserId();
		 //��ѯ����
		 String flag = (String)request.getParameter("flag");
		 
		 TdscCorpInfo tdscCorpInfo = new TdscCorpInfo();
		 
		 TdscBidderCxApp tdscBidderCxApp = new TdscBidderCxApp();
		 //��ȡҳ���� Υ����Ϣ
		 
		 
		 //���
		 if(flag.equals("0")){
		     //�����ҵ����Ȼ����Ϣ�����Ƿ��Ѵ��ڴ˼�¼
             String checkExist =GlobalConstants.DIC_VALUE_YESNO_NO;
             TdscCorpInfoCondition condition2 = new TdscCorpInfoCondition();
             condition2.setBidderName(request.getParameter("bidderName"));
             condition2.setValidity("1");
//             if(request.getParameter("bidderProperty").equals("1")){
//                 //condition2.setBidderName(request.getParameter("bidderName"));
//                 condition2.setBidderZjlx(request.getParameter("bidderZjlx"));
//                 condition2.setBidderZjhm(request.getParameter("bidderZjhm"));
//             }
//             else{
//                 //condition2.setBidderName(request.getParameter("bidderName"));
//                 condition2.setCorpFrZjlx(request.getParameter("corpFrZjlx"));
//                 condition2.setCorpFrZjhm(request.getParameter("corpFrZjhm"));
//             }
             List tdscCorpInfoList = (List)tdscCreditService.queryCorpInfo(condition2);
             //���Ѵ��ڣ�����ԭҳ�棬�������Ѵ��ڱ�ʶ
             if(tdscCorpInfoList.size()>0){
                 checkExist =GlobalConstants.DIC_VALUE_YESNO_YES;
                 request.setAttribute("checkExist", checkExist);
                 return mapping.findForward("toAddOrEditCreditInfo");
                 
             }
		     //��֤ͬ������
		     if (isTokenValid(request, true)) {  
		         
			 bindObject(tdscCorpInfo, form);
		//	 bindObject(ysqsBidderCxApp,form);
			 Date violateDate = DateUtil.string2Date(request.getParameter("violateDate"), DateUtil.FORMAT_DATE);
	         tdscBidderCxApp.setViolateRuleType(request.getParameter("violateRuleType"));
	         tdscBidderCxApp.setViolateBlockName(request.getParameter("violateBlockName"));
	         tdscBidderCxApp.setViolateDate(violateDate);
	         tdscBidderCxApp.setMemo(request.getParameter("memo"));
	         
	         Date endDate = DateUtil.string2Date(request.getParameter("endDate"), DateUtil.FORMAT_DATE);
	         tdscCorpInfo.setEndDate(endDate);
			 //������Ϣ��Դ
			 tdscCorpInfo.setAddType("1");
			 //�����ں�������־
			 tdscCorpInfo.setIfInBlockList("1");
			 //������Чλ��־
			 tdscCorpInfo.setValidity("1");
			 //��¼������Ա
	         tdscBidderCxApp.setAucitonUserId(userId);
	         //��¼����ʱ��
	         Date date = new Date();
	         tdscBidderCxApp.setAuctionDate(date);
	         //��Υ��ʱ��δ��¼��Ĭ��Ϊ��ǰ����ʱ��
             if (violateDate == null){
                 tdscBidderCxApp.setViolateDate(date);
             }
			 tdscCreditService.saveCreditInfo(tdscCorpInfo,tdscBidderCxApp);
		     }
		     else{System.out.println("�ظ��ύ");      
		     return mapping.findForward("Error");
		     }
		 }
		 //�༭
		 else{
			 String corpId = (String)request.getParameter("corpId");
			 tdscCorpInfo = tdscCreditService.queryCreditInfoByCorpId(corpId);
			 //ysqsBidderCxApp = ysqsCreditService.queryCxAppByCorpId(corpId);
			 //if(ysqsBidderCxApp == null) ysqsBidderCxApp = new YsqsBidderCxApp();
			 
			 bindObject(tdscCorpInfo, form);
		//	 bindObject(ysqsBidderCxApp,form);
			 Date violateDate = DateUtil.string2Date(request.getParameter("violateDate"), DateUtil.FORMAT_DATE);
			 Date endDate = DateUtil.string2Date(request.getParameter("endDate"), DateUtil.FORMAT_DATE);
	         tdscCorpInfo.setEndDate(endDate);
	         //ysqsBidderCxApp.setViolateRuleType(request.getParameter("violateRuleType"));
	         //ysqsBidderCxApp.setViolateBlockName(request.getParameter("violateBlockName"));
	         //ysqsBidderCxApp.setViolateDate(violateDate);
	         //ysqsBidderCxApp.setMemo(request.getParameter("memo"));
			 
			 //��¼������Ա
	         //ysqsBidderCxApp.setAucitonUserId(userId);
	         //��¼����ʱ��
	         //Date date = new Date();
	         //ysqsBidderCxApp.setAuctionDate(date);
	         //��Υ��ʱ��δ��¼��Ĭ��Ϊ��ǰ����ʱ��
             //if (violateDate == null){
                 //ysqsBidderCxApp.setViolateDate(date);
             //}
			 tdscCreditService.saveToCredit(tdscCorpInfo);
		 }

 
        return new ActionForward("credit.do?method=queryCreditListWithPage",true);
     }
 

	 /**
	  * ɾ�����ſ��¼����
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward delCreditInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 
		 String corpId = (String)request.getParameter("corpId");  
		 
		 tdscCreditService.delCreditInfo(corpId);//ֻ����λifInBlockList 
	
        
       // request.setAttribute("resultList", resultList);
 
		 return new ActionForward("credit.do?method=queryCreditListWithPage",true);
     }

	 /**
	  * �����ݿ⵼��Ԥ�������������� ת������ҳ�� view_import.jsp;
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward toImportCredit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 
		 
 
		 return mapping.findForward("toImportCredit");
     }
	 
	 /**
	  * ��ʾ���ݿ���Ԥ�������б����ں������еģ�
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 
	 public ActionForward toListImport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
	     //��ѯ����
		 TdscCorpInfoCondition condition = new TdscCorpInfoCondition();
		 bindObject(condition,form);
		 //���ݲ�ѯ������ѯδ�ں������е�Ԥ�������б�
		 List corpInfoList = (List)tdscCreditService.queryCorpInfoListByCondition(condition);
		 
		 request.setAttribute("corpInfoList", corpInfoList);
 
		 return mapping.findForward("showImportList");
     }
	 
	 /**
	  * ��ʾѡ�����ݿ���Ԥ�������б��һ����¼����ϸ��Ϣ
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward toInfoImport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 String corpId = (String)request.getParameter("corpId");
		 
		 TdscCorpInfo tdscCorpInfo = (TdscCorpInfo)tdscCreditService.queryCreditInfoByCorpId(corpId);
		 
		 
		 request.setAttribute("tdscCorpInfo", tdscCorpInfo);
		 
 
		 return mapping.findForward("showImportInfo");
     }
	 
	 
	 /**
	  * �������ݿ⵼�롱�����б�����Ϣ
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward saveToCredit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
	     //����û���Ϣ
         SysUser user = (SysUser)request.getSession().getAttribute(GlobalConstants.SESSION_NK_USER_INFO);
         String userId = user.getUserId();
         //���Ԥ����������ID
         String corpId = (String)request.getParameter("corpId");
         
         TdscCorpInfo tdscCorpInfo = (TdscCorpInfo)tdscCreditService.queryCreditInfoByCorpId(corpId);
         //YsqsBidderCxApp ysqsBidderCxApp = (YsqsBidderCxApp)ysqsCreditService.queryCxAppByCorpId(corpId);
         //if(ysqsBidderCxApp == null) ysqsBidderCxApp = new YsqsBidderCxApp();
         TdscBidderCxApp tdscBidderCxApp = new TdscBidderCxApp();
         bindObject(tdscCorpInfo,form);
         bindObject(tdscBidderCxApp,form);
         
//         Date violateDate = DateUtil.string2Date(request.getParameter("violateDate"), DateUtil.FORMAT_DATE);
//         ysqsBidderCxApp.setViolateRuleType(request.getParameter("violateRuleType"));
//         ysqsBidderCxApp.setViolateBlockName(request.getParameter("violateBlockName"));
//         ysqsBidderCxApp.setViolateDate(violateDate);
//         ysqsBidderCxApp.setMemo(request.getParameter("memo"));
         
         //ʹ �Ƿ��ں�������־ Ϊ1
         tdscCorpInfo.setIfInBlockList("1");
         //��¼������Ա
         tdscBidderCxApp.setAucitonUserId(userId);
         //��¼����ʱ��
         Date date = new Date();
         //ysqsBidderCxApp.setAuctionDate(date);
         //��Υ��ʱ��δ��¼��Ĭ��Ϊ��ǰ����ʱ��
         if (tdscBidderCxApp.getViolateDate() == null){
             tdscBidderCxApp.setViolateDate(date);
         }
         
         tdscCreditService.updateCreditInfo(tdscCorpInfo, tdscBidderCxApp);
         
         request.setAttribute("success", GlobalConstants.DIC_VALUE_YESNO_YES);
         //ˢ������ҳ��
         return mapping.findForward("toImportCredit");
     }
	 
	 /**
	  * ��ʾѡ�����ݿ���Ԥ�������б��һ����¼����ϸ��Ϣ
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward toAddCreditInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 String corpId = (String)request.getParameter("corpId");
		 request.setAttribute("corpId", corpId);
		 return mapping.findForward("toAddCreditInfo");
     }
	 /**
	  * ��ʾѡ�����ݿ���Ԥ�������б��һ����¼����ϸ��Ϣ
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward addCreditInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 TdscBidderCxApp tdscBidderCxApp = new TdscBidderCxApp();
		 bindObject(tdscBidderCxApp, form);
		 tdscCreditService.saveTdscBidderCxApp(tdscBidderCxApp);
		 request.setAttribute("message", "����ɹ�");
		 return mapping.findForward("toAddCreditInfo");
     }
	 
	 /**
	  * �����˳�����֤
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward checkBidderCredit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 String ifPass = "0";
		 String bidderName = (String)request.getParameter("bidderName");
		 
		 TdscCorpInfoCondition condition = new TdscCorpInfoCondition();
		 condition.setBidderName(bidderName);
		 //���ݾ����������Ʋ�ѯ�Ƿ��ں�������
		 List creditList = (List)tdscCreditService.queryInBlockList(condition);
		 //�ں������У����� ��1��
		 if(creditList != null && creditList.size() > 0){
			 ifPass = "1";
		 }
		 
		 // ���������õ����
		 response.setContentType("text/xml; charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 PrintWriter pw = response.getWriter();
		 // ���ظ��ص������Ĳ���
		
		 pw.write(ifPass);
		 pw.close();

		 return null;
     }
	 
	 
	 
	 
	 
	 
	 
	 
}
