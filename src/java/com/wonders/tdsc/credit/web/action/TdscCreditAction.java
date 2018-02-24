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
 * 诚信管理Action
 */
public class TdscCreditAction extends BaseAction{

	/**
	 * 诚信管理Service
	 */
    private TdscCreditService tdscCreditService;
	
	
	public TdscCreditService getTdscCreditService() {
		return tdscCreditService;
	}
	public void setTdscCreditService(TdscCreditService tdscCreditService) {
		this.tdscCreditService = tdscCreditService;
	}
	
	 /**
	  * 根据查询条件获得黑名单列表
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	
	 public ActionForward queryCreditListWithPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 //获得用户信息
		 /*SysUser user = (SysUser)request.getSession().getAttribute(GlobalConstants.SESSION_NK_USER_INFO);
		  * String userId = user.getUserID()+"";
		  */
		 //查询条件
		 TdscCorpInfoCondition condition = new TdscCorpInfoCondition();
		 //查询标识
		 String queryFlag =(String)request.getParameter("queryFlag");
		 
		 if(queryFlag == null) queryFlag=new String();
		 String currentPage = request.getParameter("currentPage");
		 if(currentPage != null){
		 condition.setCurrentPage(Integer.parseInt(currentPage));}
		 if(queryFlag.equals(GlobalConstants.DIC_VALUE_YESNO_YES)){
			 bindObject(condition,form);
		 }
		 /*	 condition.setUserId(userId);*/
		 //根据查询条件获得黑名单列表
		 PageList resultPageList = tdscCreditService.queryCreditList(condition);
		 List resultList = null;
		 Pager pager = null;
		 if(resultPageList != null){
			 resultList = resultPageList.getList();
			 pager = resultPageList.getPager();
		 }
		 
		 //属性设置
		 request.setAttribute("condition", condition);
		 request.setAttribute("resultList", resultList);
		 request.setAttribute("pager", pager);
 
        return mapping.findForward("toFindCreditList");
     }
	 

	 /**
	  * 跳转到添加或编辑诚信库页面
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward toaddOrEditCreditInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
		 //获取flag=1 编辑； flag=0 新加
		 String flag= (String)request.getParameter("flag");
		 
		 if(flag.equals("1")){
		 //获取企业及自然人基本信息ID
		 String corpId = (String)request.getParameter("corpId");	 
		 //查询条件
	//	 YsqsCorpInfoCondition condition = new YsqsCorpInfoCondition();
	//	 condition.setCorpId(corpId);
		 //根据条件查询到一条信息
		 TdscCorpInfo tdscCorpInfo = new TdscCorpInfo();
		 //YsqsBidderCxApp ysqsBidderCxApp = new YsqsBidderCxApp();
		 
		 tdscCorpInfo = tdscCreditService.lazyLoadAppByCorpId(corpId);
		 //ysqsBidderCxApp = ysqsCreditService.queryCxAppByCorpId(corpId);
		 
		 request.setAttribute("tdscCorpInfo", tdscCorpInfo);
		 //request.setAttribute("ysqsBidderCxApp", ysqsBidderCxApp );
		 		 
		 }
		 //设置属性
		 request.setAttribute("flag",flag);
		 //生成同步令牌,防止重复提交
		 saveToken(request); 
 
        return mapping.findForward("toAddOrEditCreditInfo");
     }

	 /**
	  * 保存添加或编辑后的诚信库信息方法 
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward baocunCreditInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
	     //获得用户信息
		 SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
         String userId = user.getUserId();
		 //查询条件
		 String flag = (String)request.getParameter("flag");
		 
		 TdscCorpInfo tdscCorpInfo = new TdscCorpInfo();
		 
		 TdscBidderCxApp tdscBidderCxApp = new TdscBidderCxApp();
		 //获取页面中 违规信息
		 
		 
		 //添加
		 if(flag.equals("0")){
		     //检查企业或自然人信息表中是否已存在此记录
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
             //若已存在，返回原页面，并带回已存在标识
             if(tdscCorpInfoList.size()>0){
                 checkExist =GlobalConstants.DIC_VALUE_YESNO_YES;
                 request.setAttribute("checkExist", checkExist);
                 return mapping.findForward("toAddOrEditCreditInfo");
                 
             }
		     //验证同步令牌
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
			 //设置信息来源
			 tdscCorpInfo.setAddType("1");
			 //设置在黑名单标志
			 tdscCorpInfo.setIfInBlockList("1");
			 //设置有效位标志
			 tdscCorpInfo.setValidity("1");
			 //记录操作人员
	         tdscBidderCxApp.setAucitonUserId(userId);
	         //记录操作时间
	         Date date = new Date();
	         tdscBidderCxApp.setAuctionDate(date);
	         //若违规时间未记录，默认为当前操作时间
             if (violateDate == null){
                 tdscBidderCxApp.setViolateDate(date);
             }
			 tdscCreditService.saveCreditInfo(tdscCorpInfo,tdscBidderCxApp);
		     }
		     else{System.out.println("重复提交");      
		     return mapping.findForward("Error");
		     }
		 }
		 //编辑
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
			 
			 //记录操作人员
	         //ysqsBidderCxApp.setAucitonUserId(userId);
	         //记录操作时间
	         //Date date = new Date();
	         //ysqsBidderCxApp.setAuctionDate(date);
	         //若违规时间未记录，默认为当前操作时间
             //if (violateDate == null){
                 //ysqsBidderCxApp.setViolateDate(date);
             //}
			 tdscCreditService.saveToCredit(tdscCorpInfo);
		 }

 
        return new ActionForward("credit.do?method=queryCreditListWithPage",true);
     }
 

	 /**
	  * 删除诚信库记录方法
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
		 
		 tdscCreditService.delCreditInfo(corpId);//只需置位ifInBlockList 
	
        
       // request.setAttribute("resultList", resultList);
 
		 return new ActionForward("credit.do?method=queryCreditListWithPage",true);
     }

	 /**
	  * 从数据库导入预申请人至黑名单 转到导入页面 view_import.jsp;
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
	  * 显示数据库中预申请人列表（不在黑名单中的）
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 
	 public ActionForward toListImport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
	     //查询条件
		 TdscCorpInfoCondition condition = new TdscCorpInfoCondition();
		 bindObject(condition,form);
		 //根据查询条件查询未在黑名单中的预申请人列表
		 List corpInfoList = (List)tdscCreditService.queryCorpInfoListByCondition(condition);
		 
		 request.setAttribute("corpInfoList", corpInfoList);
 
		 return mapping.findForward("showImportList");
     }
	 
	 /**
	  * 显示选中数据库中预申请人列表的一条记录的详细信息
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
	  * “从数据库导入”功能中保存信息
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 public ActionForward saveToCredit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception {
	     //获得用户信息
         SysUser user = (SysUser)request.getSession().getAttribute(GlobalConstants.SESSION_NK_USER_INFO);
         String userId = user.getUserId();
         //获得预申请人主键ID
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
         
         //使 是否在黑名单标志 为1
         tdscCorpInfo.setIfInBlockList("1");
         //记录操作人员
         tdscBidderCxApp.setAucitonUserId(userId);
         //记录操作时间
         Date date = new Date();
         //ysqsBidderCxApp.setAuctionDate(date);
         //若违规时间未记录，默认为当前操作时间
         if (tdscBidderCxApp.getViolateDate() == null){
             tdscBidderCxApp.setViolateDate(date);
         }
         
         tdscCreditService.updateCreditInfo(tdscCorpInfo, tdscBidderCxApp);
         
         request.setAttribute("success", GlobalConstants.DIC_VALUE_YESNO_YES);
         //刷新整个页面
         return mapping.findForward("toImportCredit");
     }
	 
	 /**
	  * 显示选中数据库中预申请人列表的一条记录的详细信息
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
	  * 显示选中数据库中预申请人列表的一条记录的详细信息
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
		 request.setAttribute("message", "保存成功");
		 return mapping.findForward("toAddCreditInfo");
     }
	 
	 /**
	  * 竞买人诚信验证
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
		 //根据竞买人命名称查询是否在黑名单中
		 List creditList = (List)tdscCreditService.queryInBlockList(condition);
		 //在黑名单中，返回 “1”
		 if(creditList != null && creditList.size() > 0){
			 ifPass = "1";
		 }
		 
		 // 将内容设置到输出
		 response.setContentType("text/xml; charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 PrintWriter pw = response.getWriter();
		 // 返回给回调函数的参数
		
		 pw.write(ifPass);
		 pw.close();

		 return null;
     }
	 
	 
	 
	 
	 
	 
	 
	 
}
