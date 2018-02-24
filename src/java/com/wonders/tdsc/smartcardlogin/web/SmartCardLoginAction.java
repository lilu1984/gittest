package com.wonders.tdsc.smartcardlogin.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.model.Pager;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.smartcardlogin.service.SmartCardLoginService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class SmartCardLoginAction extends BaseAction{
	
	private SmartCardLoginService smartCardLoginService;
	
	private CommonQueryService commonQueryService;
	
    private TdscBidderAppService tdscBidderAppService;
    
	public void setSmartCardLoginService(SmartCardLoginService smartCardLoginService) {
		this.smartCardLoginService = smartCardLoginService;
	}
	
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}
	/**
	 * ���Ŵ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		request.setAttribute("cardNo", request.getParameter("cardNo"));
		return mapping.findForward("showLogin");
	}

	/**
	 * ���Ƶ�½
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//�����û����ź�����
		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo,password);
		String checkMsg = "false";
		if(tdscBidderApp!=null){
			checkMsg="true";
		}
		request.setAttribute("checkMsg", checkMsg);
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		return mapping.findForward("showLogin");
	}
	
	/**
	 * ������������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkListingInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cardNo = request.getParameter("cardNo");
		request.setAttribute("cardNo", cardNo);
		String password = request.getParameter("password");
		TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo,password);
		//��ѯ�ؿ�Ľ���״̬
		String appId = tdscBidderApp.getAppId();
		TdscBlockTranApp tdscBlockTranApp =  this.smartCardLoginService.queryBlockTranAppInfoByAppId(appId);
		String blockId = tdscBlockTranApp.getBlockId();
		TdscBlockInfo tdscBlockInfo = this.smartCardLoginService.queryBlockInfoByBlockId(blockId);
		String status = tdscBlockInfo.getStatus();
		String isTran = "true";
		if(status.equals("02")||status.equals("03")||status.equals("04")){
			isTran = "false";//�����ѽ���
		}
		request.setAttribute("isTran", isTran);
		//�鿴�Ƿ���Χ"00"��û�����"01"ͨ��"02"ûͨ��
		String reviewResult = tdscBidderApp.getReviewResult();	
		request.setAttribute("reviewResult", reviewResult);
		//�鿴���ƿ�ʼʱ��ͽ���ʱ��
		String isStart = this.smartCardLoginService.checkStartDate();
		request.setAttribute("isStart", isStart);
		String isEnd = this.smartCardLoginService.checkEndDate();
		request.setAttribute("isEnd", isEnd);
		return mapping.findForward("checkListing");
	}
	
	/**
	 * ���Ŵ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showLoginToOp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		request.setAttribute("cardNo", request.getParameter("cardNo"));
		request.setAttribute("yktXh", request.getParameter("yktXh"));
		return mapping.findForward("indexoverpass");
	}
	/**
	 * У��ҳ�洫��Ŀ����Ƿ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward overpassLoginCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		String cardNo= request.getParameter("cardNo");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString="";
		String yktXh="";
		String count="";
		List tempList=(List)tdscBidderAppService.checkIfUsedYktBh(cardNo);
		if(tempList.size()==0){
			count="0";
		}
		if(tempList.size()==1){
			count="1";
			//��ý��׿��ı��
			TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppService.getBidderAppByYktBh(cardNo);
			if(tdscBidderApp!=null){
				 yktXh=tdscBidderApp.getYktXh()+",";
			}
		}
		if(tempList.size()>1){
			count="2";
			//��ý��׿��ı��
			List tdscBidderAppList = (List)tdscBidderAppService.getBidderAppListByYktBh(cardNo);
			if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
				TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(0);
				yktXh=tdscBidderApp.getYktXh()+",";
			}
		}
		
		retString=count+","+cardNo+","+yktXh;
		
/*		request.setAttribute("count",count);
		request.setAttribute("cardNo", cardNo);
		return mapping.findForward("overpass");*/
		pw.write(retString);
		pw.close();
		return null;
	}
	
	
	/**
	 * ��Χ�������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward overpassPasswordCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//�����û����ź�����
		String cardNo = request.getParameter("cardNo");
		request.setAttribute("cardNo", cardNo);
		String password = request.getParameter("password");
		request.setAttribute("password", password);
		TdscBidderApp tdscBidderApp = smartCardLoginService.checkSmartCard(cardNo,password);
		String checkMsg = "false";
		if(tdscBidderApp!=null){
			checkMsg="true";
		}
		request.setAttribute("checkMsg", checkMsg);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		return mapping.findForward("overpass");
	}
	
	/**
	 * ��Χ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward overpassCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = request.getParameter("bidderId");
		
		TdscBidderCondition condition = new TdscBidderCondition();
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		
		if(bidderId!=null&&!"".equals(bidderId)){
			condition.setBidderId(bidderId);
			//TdscBidderApp tdscBidderApp = smartCardLoginService..checkByAcceptNo(password);
			List tdscBidderAppList = smartCardLoginService.getBidderAppListByCondition(condition);
			if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
				tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(0);
				if(tdscBidderApp!=null)
					request.setAttribute("appId", tdscBidderApp.getAppId());
					request.setAttribute("acceptNo", tdscBidderApp.getAcceptNo());
			}
			request.setAttribute("bidderId", bidderId);
		}
		//�鿴�Ƿ���Χ
		if(tdscBidderApp.getReviewResult()!=null&&tdscBidderApp.getReviewResult().equals(GlobalConstants.DIC_ID_REVIEW_RESULT_YES)){
			return mapping.findForward("overpassSuccess");
		}else{
			return mapping.findForward("overpassFailed");
		}
	}
	
	/**����cardNo�ж���������ҳ�������һ��ͨ���ţ�1���õؿ���÷�ʽ�ǲ��ǹ���
	 *                                      2���õؿ��ǲ�����������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkCardNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String cardNo = request.getParameter("cardNo");
		String password = request.getParameter("password");
		String retResult="00";
		List cardNoList=(List)tdscBidderAppService.checkIfUsedYktBh(cardNo);
		//�ж����ݿ���û�иÿ��ţ�
		//û��ʱ��
		if(cardNoList==null||cardNoList.size()==0){
			retResult = "01";
		}
        if(cardNoList.size()==1){
			
			//���appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp)cardNoList.get(0);
			//�ж��Ƿ�����˻���
			if(tdscBidderApp.getReviewResult()==null){
				
				retResult = "88";
			}else{
				if(password!=null&&tdscBidderApp.getYktMm()!=null){
				   if(password.equals(tdscBidderApp.getYktMm())){
						String appId=(String)tdscBidderApp.getAppId();
						//����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
						TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
						tdscBaseQueryCondition.setAppId(appId);
						TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
						//���÷�ʽΪ�����ơ�ʱ
					    if(GlobalConstants.DIC_TRANSFER_LISTING==tdscBlockAppView.getTransferMode()){
					    	retResult = "99";
					    }else{
					    	retResult = "02";
					    }
					   
				   }else{
					        retResult = "77";
				   }
				}
			}
		}
		//�е���Ψһʱ����ѯ���ÿ���Ӧ�����еؿ�����������ĵؿ���Ϣ
		if(cardNoList.size()>1){
		   for(int i=0;i<cardNoList.size();i++){
			   TdscBidderApp tdscBidderApp = (TdscBidderApp)cardNoList.get(i); 
			   String appId=(String)tdscBidderApp.getAppId();
				//����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				tdscBaseQueryCondition.setAppId(appId);
				//TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
				//δ�ꡣ����
		   }
		   retResult="03";
		} 
        // ���ظ��ص������Ĳ���
        //String rowId = bidderPersonId;
		response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(retResult);
        pw.write(password);
        pw.close();

        return null;
	}
	
	/**
	 * ����cardNo�ж������޸�ҳ�������һ��ͨ�����Ƿ����   lz+  090724
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkCardNoAndPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = request.getParameter("bidderId");
		String cardNo = request.getParameter("cardNo");//һ��ͨ���
		String password = request.getParameter("password");
		String retResult="00";
		String yktBh = "";
		if(bidderId!=null&&!"".equals(bidderId)){
		TdscBidderApp bidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
		if(bidderApp!=null&&cardNo.equals(bidderApp.getYktXh()))
			yktBh = bidderApp.getYktBh();
		
		List cardNoList=(List)tdscBidderAppService.checkIfUsedYktBh(yktBh);
		//�ж����ݿ���û�иÿ��ţ�
		//û��ʱ��
		if(cardNoList==null||cardNoList.size()==0){
			retResult = "01";
		}
        if(cardNoList.size()==1){
			//���appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp)cardNoList.get(0);
			
				if(password!=null&&tdscBidderApp.getYktMm()!=null){
				   if(password.equals(tdscBidderApp.getYktMm())){
					   retResult = "02";//ͨ����֤
				   }else{
					   retResult = "77";//�������
				   }
				}
			}
		
		//�е���Ψһʱ����ѯ���ÿ���Ӧ�����еؿ�����������ĵؿ���Ϣ
		if(cardNoList.size()>1){
		   retResult="03";
		}
		
		}
        // ���ظ��ص������Ĳ���
		response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(retResult);
        pw.write(password);
        pw.close();

        return null;
	}
	
	/**
	 * ���ص���ʼҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toHuiTui(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
		return mapping.findForward("indexht");
	}
	/**
	 * ��ѯ�ֿ�����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String bidderId =(String)request.getParameter("bidderId");
		
		//����bidderId(���׿����ź�����)��ѯ�ֿ���
		if(bidderId!=null&&!"".equals(bidderId)){
			TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
			List bidlist = (List)tdscBidderAppService.queryBidderPersonList(bidderId);
			TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
			//��þ����˵�List
			if(tdscBidderApp!=null){
			   String appId = (String)tdscBidderApp.getAppId();
			   //����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
			   TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			   tdscBaseQueryCondition.setAppId(appId);
			   tdscBlockAppView = (TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);			   
			}
			
			request.setAttribute("bidlist", bidlist);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
			
			return mapping.findForward("infoindex");
		}
		
		return null;
		
	}
	
	/**
	 * ��ѯ�ֿ�����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String cardNo =(String)request.getParameter("cardNo");
		String password =(String)request.getParameter("password");
		//���� cardNo��password(���׿����ź�����)��ѯ�ֿ���
		List tdscBidderAppList = (List)smartCardLoginService.getBidderAppListByCard(cardNo,password);
		List  bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView=new TdscBlockAppView();
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		//��þ����˵�List
		if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
			tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(0);
			String bidderId = (String)tdscBidderApp.getBidderId();
			bidlist = (List)tdscBidderAppService.queryBidderPersonList(bidderId);
			String appId = (String)tdscBidderApp.getAppId();
			//����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
			TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
			tdscBaseQueryCondition.setAppId(appId);
			tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);			   
		}
		request.setAttribute("bidlist", bidlist);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		
		return mapping.findForward("infoindex");
	}
	
	/**
	 * ��ѯ�ֿ��˾��۵ؿ���Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showBlockList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String cardNo =(String)request.getParameter("cardNo");
		String password =(String)request.getParameter("password");
		//���� cardNo��password(���׿����ź�����)��ѯ�ֿ����б�
		//TdscBidderApp tdscBidderApp=(TdscBidderApp)smartCardLoginService.checkSmartCard(cardNo,password);
		List tdscBidderAppList = smartCardLoginService.getBidderAppListByCard(cardNo, password);
		List returnList = new ArrayList();
		if(tdscBidderAppList!=null&&tdscBidderAppList.size()>0){
			for(int i=0;i<tdscBidderAppList.size();i++){
				TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppList.get(i);
				if(tdscBidderApp!=null&&tdscBidderApp.getAppId()!=null){
					//����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ���ڽ����е�TdscBlockAppView
					TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
					tdscBaseQueryCondition.setAppId(tdscBidderApp.getAppId());
					tdscBaseQueryCondition.setStatus("01");//������
					List tdscBlockAppViewList = (List)commonQueryService.queryTdscBlockAppViewListWithoutNode(tdscBaseQueryCondition);
					if(tdscBlockAppViewList!=null&&tdscBlockAppViewList.size()==1){
						TdscBlockAppView tdscBlockAppView = (TdscBlockAppView)tdscBlockAppViewList.get(0);
						tdscBlockAppView.setBzBeizhu(tdscBidderApp.getBidderId());//��bidderId����BzBeizhu�ֶ���
						returnList.add(tdscBlockAppView);
					}
				}
			}
		}
		
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("password", password);
		request.setAttribute("tdscBlockAppViewList", returnList);
		
		return mapping.findForward("showBlockList");
	}
	
//*////////////////////////////////////////////////////*/	
	
	/**����cardNo�ж���������ҳ�������һ��ͨ���ţ�1���õؿ���÷�ʽ�ǲ��ǹ���
	 *                                      2���õؿ��ǲ�����������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkAcceptNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String acceptNo = request.getParameter("password");
		String retResult="00";
		List acceptNoList=(List)tdscBidderAppService.checkIfUsedAcceptNo(acceptNo);
		//�ж����ݿ���û�иÿ��ţ�
		//û��ʱ��
		if(acceptNoList==null||acceptNoList.size()==0){
			retResult = "01";
		}
        if(acceptNoList.size()==1){
			//���appId
			TdscBidderApp tdscBidderApp = (TdscBidderApp)acceptNoList.get(0);
			//�ж��Ƿ�����˻���
			if(tdscBidderApp.getReviewResult()==null){
				retResult = "88";
			}else{
				String appId=(String)tdscBidderApp.getAppId();
				//����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				tdscBaseQueryCondition.setAppId(appId);
				TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
				//���÷�ʽΪ�����ơ�ʱ
			    if(GlobalConstants.DIC_TRANSFER_LISTING==tdscBlockAppView.getTransferMode()){
			    	retResult = "99";
			    }else{
			    	retResult = "02";
			    }
			}
		}
		//�е���Ψһʱ����ѯ���ÿ���Ӧ�����еؿ�����������ĵؿ���Ϣ
		if(acceptNoList.size()>1){
		   for(int i=0;i<acceptNoList.size();i++){
			   TdscBidderApp tdscBidderApp = (TdscBidderApp)acceptNoList.get(i); 
			   String appId=(String)tdscBidderApp.getAppId();
				//����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
				TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
				tdscBaseQueryCondition.setAppId(appId);
				//TdscBlockAppView tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
				//δ�ꡣ����
		   }
		   retResult="03";
		} 
        // ���ظ��ص������Ĳ���
		response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(retResult);
        pw.write(acceptNo);
        pw.close();

        return null;
	}
	
	/**
	 * ��ѯ�ֿ�����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBidByAcceptNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String acceptNo =(String)request.getParameter("password");
		//����������acceptNo��ѯ�ֿ���
		TdscBidderApp tdscBidderApp=(TdscBidderApp)smartCardLoginService.checkByAcceptNo(acceptNo);
		List  bidlist = new ArrayList();
		TdscBlockAppView tdscBlockAppView=new TdscBlockAppView();
		//��þ����˵�List
		if(tdscBidderApp!=null){
		   String bidderId = (String)tdscBidderApp.getBidderId();
		   bidlist = (List)tdscBidderAppService.queryBidderPersonList(bidderId);
		   String appId = (String)tdscBidderApp.getAppId();
			//����appIdͨ��ͨ�ò�ѯ�������appId��Ӧ��TdscBlockAppView
		   TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		   tdscBaseQueryCondition.setAppId(appId);
		   tdscBlockAppView=(TdscBlockAppView)commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);			   
		}
		request.setAttribute("bidlist", bidlist);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		
		return mapping.findForward("infoindex");
	}
	
	
	/**
     * �������һ��ͨ�б�
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward queryYkt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ��ȡҳ�����
    	TdscBidderCondition condition = new TdscBidderCondition();
    	TdscBidderForm tdscBidderForm = (TdscBidderForm)form;
        bindObject(condition, tdscBidderForm);
    	//��ȡ�û���Ϣ
    	SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

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
        if(StringUtils.isNotEmpty(condition.getBidderName()) ){
        	List bidderIdList = tdscBidderAppService.getBidderIdListByBidderName(condition.getBidderName());
        	if(bidderIdList != null && bidderIdList.size() > 0){
        		condition.setBidderIdList(bidderIdList);
        		//���ݾ��������Ʋ�ѯʱ�������LIST�������ѯ�����򷵻ؿյ�PageList
        	}else{
                PageList pageList = new PageList();
                request.setAttribute("pageList", pageList);
                request.setAttribute("condition", condition);
                
                return mapping.findForward("bidderYktList");
        	}
        }
        // ����û���Ϣ
		//SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        //String appUserId=String.valueOf(user.getUserId());
        
        String currentPage = request.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = (String) request.getAttribute("currentPage");
        }

        int cPage = 0;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        
        /* ����ҳ������ */
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
        condition.setCurrentPage(cPage);
        
        // ��ѯ�б�
//        PageList pageList = (PageList) tdscBidderAppService.findAllYktPageList(condition);
//        if(pageList != null){
//        	List bidderList = (List)pageList.getList();
//        	if(bidderList != null && bidderList.size() > 0){
//        		for(int i = 0; i< bidderList.size(); i++){
//        			TdscBidderApp tdscBidderApp = (TdscBidderApp)bidderList.get(i);
//        			TdscBidderPersonApp tempApp = (TdscBidderPersonApp) tdscBidderAppService.tidyBidderByBidderId(tdscBidderApp.getBidderId());
//        			if(StringUtils.isNotEmpty(tempApp.getMemo())){
//        				tdscBidderApp.setMemo(tempApp.getMemo());
//        			}
//        		}
//        	}
//        }
        List bidderIdList = tdscBidderAppService.tidyBidderAppList(condition);
        List currentAppList = new ArrayList();
        
        int listSize = 0;
        if(bidderIdList != null && bidderIdList.size() > 0){
        	listSize =  bidderIdList.size();
        }
        PageList pageList = new PageList();
        Pager _pager=new Pager(listSize, condition.getPageSize(), condition.getCurrentPage());
        pageList.setPager(_pager);
        
		for(int h = 0; h < _pager.getPageSize(); h++){
			if(bidderIdList != null && bidderIdList.size() > 0){
				if(_pager.getStartRow() + h >= bidderIdList.size())break;
				currentAppList.add(bidderIdList.get(_pager.getStartRow()+h));//��ǰҳӦ����ʾ���ݼӵ���ǰ��ʾ�б������ҳ��
			}
		}
        
        pageList.setList(currentAppList);

        
        request.setAttribute("pageList", pageList);
        request.setAttribute("condition", condition);
        return mapping.findForward("bidderYktList");
    }
	
    /**
     * �������б��еġ����ؽ��׿����š���������ҳ�棬��ʾ�ֿ�����Ϣ
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward toViewDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	String bidderId = (String)request.getParameter("bidderId");
    	//������Ϣ
        TdscBidderApp tdscBidderApp=(TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
        if(tdscBidderApp != null){
        	if(StringUtils.isNotEmpty(tdscBidderApp.getAppId())){
        		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
        		condition.setAppId(tdscBidderApp.getAppId());
        		TdscBlockAppView appView = (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);
        		request.setAttribute("appView", appView);
        	}
        	
        }
        //�������б�
        List tdscBidderPersonAppList=tdscBidderAppService.queryBidderPersonList(bidderId);
        //���÷���ֵ
        
        request.setAttribute("tdscBidderPersonAppList", tdscBidderPersonAppList);
        request.setAttribute("tdscBidderApp", tdscBidderApp);
        return mapping.findForward("bidderAppDetails");
    }
   
	/**
	 * 
	 */
    public ActionForward chooseType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
    	String bidderId =(String)request.getParameter("bidderId");
		String type =(String)request.getParameter("type");
		
		if(bidderId!=null){
			TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
			if(tdscBidderApp!=null&&!"".equals(tdscBidderApp.getYktXh())){
				request.setAttribute("cardNo", tdscBidderApp.getYktXh());
				request.setAttribute("tdscBidderApp", tdscBidderApp);
			}
			request.setAttribute("bidderId", bidderId);
		}
		
		if(type!=null){
			if("xiugai".equals(type))
				return mapping.findForward("toOldPassword");
			else if("chongzhi".equals(type))
				return mapping.findForward("toNewPassword");
			else
				return mapping.findForward("toNewYkt");
		}
		
		return null;
	}
    
    /**
	 * �����޸Ľ������������ҳ��
	 */
	public ActionForward toOldPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String cardNo =(String)request.getParameter("cardNo");
		
		if(cardNo!=null&&!"".equals(cardNo))
			request.setAttribute("cardNo", cardNo);
		
		return mapping.findForward("toOldPassword");
	}
	
	/**
	 * �����޸Ľ�������������ҳ��
	 */
	public ActionForward toNewPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String cardNo =(String)request.getParameter("cardNo");
		String bidderId =(String)request.getParameter("bidderId");
		
		if(cardNo!=null&&!"".equals(cardNo))
			request.setAttribute("cardNo", cardNo);
		if(bidderId!=null&&!"".equals(bidderId))
			request.setAttribute("bidderId", bidderId);
		
		return mapping.findForward("toNewPassword");
	}
	
	
	/**
	 * ����2��ȷ��
	 */
	public ActionForward checkPasswordTwice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String newPassword =(String)request.getParameter("newPassword");
		String cardNo =(String)request.getParameter("cardNo");
		String bidderId =(String)request.getParameter("bidderId");
		
		//�ж����ݿ���û�иÿ���
		//List cardNoList=(List)tdscBidderAppService.checkIfUsedYktBh(cardNo);
		
		if(newPassword!=null&&!"".equals(newPassword))
			request.setAttribute("newPassword", newPassword);
		if(cardNo!=null&&!"".equals(cardNo))
			request.setAttribute("cardNo", cardNo);
		if(bidderId!=null&&!"".equals(bidderId))
			request.setAttribute("bidderId", bidderId);
		
		return mapping.findForward("toNewPassword2");
	}
	
	
	
	/**
	 * ����2��ȷ�ϣ����������޸�
	 */
	public ActionForward changePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String newPassword2 = (String)request.getParameter("newPassword2");
		String bidderId =(String)request.getParameter("bidderId");
		
		TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
			
		if(tdscBidderApp!=null&&tdscBidderApp.getYktBh()!=null&&tdscBidderApp.getYktMm()!=null){
			tdscBidderApp.setYktMm(newPassword2);
			tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
		}
		
		request.setAttribute("saveMessage", "����ɹ�!");
		return mapping.findForward("toNewPassword2");
	}
	
	
	
	/**
	 * һ��ͨ��ʧ���¿�¼��
	 */
	public ActionForward changeYktAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {	
        //����ҳ�洫���ֵ
		String bidderId =(String)request.getParameter("bidderId");
		String yktXh =(String)request.getParameter("yktXh");
		String yktBh =(String)request.getParameter("yktBh");
		String yktMm =(String)request.getParameter("yktMm");
		
		if(bidderId!=null&&!"".equals(bidderId)){
			TdscBidderApp tdscBidderApp = (TdscBidderApp)tdscBidderAppService.queryBidderAppInfo(bidderId);
				
			if(tdscBidderApp!=null&&tdscBidderApp.getYktBh()!=null&&tdscBidderApp.getYktMm()!=null){
				tdscBidderApp.setYktBh(yktBh);
				tdscBidderApp.setYktXh(yktXh);
				tdscBidderApp.setYktMm(yktMm);
				tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
			}
		}
		
		request.setAttribute("saveMessage", "����ɹ�!");
		return queryYkt(mapping, form, request, response);
	}
}