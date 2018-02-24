package com.wonders.wsjy.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.wonders.engine.CoreService;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscCorpInfo;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.bank.BankService;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.service.SubscribeService;
import com.wonders.wsjy.service.TradeServer;
import com.wonders.wsjy.web.form.PersonForm;
/**
 * �깺
 * @author Gordon
 *
 */
public class SubscribeAction extends BaseAction {
	//�Ƿ�Ϊ���Ի���������Ϊ1��Ϊ���Ի���
	private String isTest = PropertiesUtil.getInstance().getProperty("is_test");
	private CommonQueryService commonQueryService;
	
	private SubscribeService subscribeService;
	
	private TradeServer tradeServer;
	
	public void setTradeServer(TradeServer tradeServer) {
		this.tradeServer = tradeServer;
	}

	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	/**
	 * ��ȡ�û���Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getPersonInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = (String) request.getSession().getAttribute("certNo");
		String url = request.getParameter("url");
		PersonInfo personinfo = subscribeService.getPersonInfo(bidderId);
		//FileRef jmsqFile = subscribeService.findFileRefByBusId(bidderId,GlobalConstants.JMSQ_FILE);
		//FileRef lhjmFile = subscribeService.findFileRefByBusId(bidderId,GlobalConstants.LHJM_FILE);
		request.setAttribute("personInfo", personinfo);
		//request.setAttribute("lhjmFile", lhjmFile);
		//request.setAttribute("jmsqFile", jmsqFile);
		request.setAttribute("url", url);
		if("0".equals(isTest)){
			return mapping.findForward("person_info");
		}else{
			return mapping.findForward("t_person_info");
		}
	}
	
	/**
	 * �־û��û�����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward persistentPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = (String) request.getSession().getAttribute("certNo");
		PersonForm personForm = (PersonForm)form;
		PersonInfo personInfo = new PersonInfo();
		bindObject(personInfo, personForm);
		personInfo.setBidderId(bidderId);
		if (null == personInfo.getCreateDate()) {
			personInfo.setCreateDate(new Date());
			personInfo.setLastUpdateDate(new Date());
		} else {
			personInfo.setLastUpdateDate(new Date());
		}
		personInfo = subscribeService.persistentInfo(personInfo);
		/*
		//�������븽��
		if(personForm.getJmrFile()!=null){
			FormFile jmrFile = personForm.getJmrFile();
			String fileType = jmrFile.getFileName().substring(jmrFile.getFileName().lastIndexOf(".")+1);
			FileRef jmsqFile = subscribeService.uploadFile(personInfo.getBidderId(),jmrFile.getFileData() , jmrFile.getFileName(), fileType,GlobalConstants.JMSQ_FILE);
			request.setAttribute("jmsqFile", jmsqFile);
		}
		//���Ͼ������븽��
		if(personForm.getJmrFile2()!=null){
			FormFile jmrFile2 = personForm.getJmrFile2();
			String fileType = jmrFile2.getFileName().substring(jmrFile2.getFileName().lastIndexOf(".")+1);
			FileRef lhjmFile = subscribeService.uploadFile(personInfo.getBidderId(),jmrFile2.getFileData() , jmrFile2.getFileName(), fileType,GlobalConstants.LHJM_FILE);
			request.setAttribute("lhjmFile", lhjmFile);
		}
		*/
		request.getSession().setAttribute("bidderName", personInfo.getBidderName());
		String url = request.getParameter("url");
		if (StringUtils.isNotEmpty(url)) {
			request.setAttribute("redirect", url);
			return mapping.findForward("person_info");
		}
		return new ActionForward("subscribe.do?method=getPersonInfo&msg=true",true);
	}
	
	/**
	 * ��֤��������Ϣ�Ƿ�����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward validatePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = (String) request.getSession().getAttribute("certNo");
		PersonInfo personinfo = subscribeService.getPersonInfo(bidderId);
		response.setContentType("text/html; charset=GBK");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String retString = "0";
		if ( null != personinfo && StringUtils.isNotBlank(personinfo.getBidderName())) {
			retString = "1";
		}
		pw.write(retString);
		pw.close();
		return null;
	}
	
	/**
	 * ȡ�ÿ��깺�Ĺ�����Ϣ�б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		// ����������еĽڵ���Ҫ���ýڵ��� RECORD_BIDDER_APPLY
		condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
		condition.setOrderKey("noitceNo");
		List tdscblockAppViewList = (List) commonQueryService.queryTdscBlockAppViewList(condition);
		Set noticeSet = new HashSet();
		if (tdscblockAppViewList != null) {
			for (int i = 0 ; i < tdscblockAppViewList.size() ; ) {
				TdscBlockAppView appView = (TdscBlockAppView)tdscblockAppViewList.get(i);
				String noticeId = appView.getNoticeId();
				if (noticeSet.contains(noticeId)) {
					tdscblockAppViewList.remove(i);
					continue;
				}
				noticeSet.add(noticeId);
				i++;
			}
			request.setAttribute("tdscblockAppViewList", tdscblockAppViewList);
		}
		return mapping.findForward("subscribe-notice-list");
	}
	
	/**
	 * ���ݹ���ȡ�ö�Ӧ�ĵؿ��б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		//List tdscBlockList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		List tradeBlockList = tradeServer.getTdscTradeViewAppByNoticeId(noticeId);
		
		if(tradeBlockList!=null && tradeBlockList.size()>0){
			Map blockAndListingAppMap = new LinkedHashMap();//��ų��õؿ�ID����߹��Ƽ�¼�����ӳ���ϵ
			Map blockAndListingCountMap = new LinkedHashMap();//��ų��õؿ�ID�뵱ǰ�����ִε�ӳ���ϵ
			Map blockAndBidderCountMap = new LinkedHashMap();//��ų��õؿ�ID�뵱ǰ�����ִε�ӳ���ϵ
			for(int i=0; i<tradeBlockList.size(); ){
				TdscTradeView tdscTradeView = (TdscTradeView)tradeBlockList.get(i);
				if("04".equals(tdscTradeView.getTranResult())){//04��ʾ������ֹ
					tradeBlockList.remove(i);
				}else{
					List listingAppList = tradeServer.findListingAppList(tdscTradeView.getAppId(), null, "11");
					List bidderAppList = tradeServer.findValidBidderList(tdscTradeView.getAppId());
					
					if(listingAppList!=null && listingAppList.size()>0){
						TdscListingApp tdscListingApp = (TdscListingApp)listingAppList.get(0);
						blockAndListingAppMap.put(tdscTradeView.getAppId(), tdscListingApp);
						blockAndListingCountMap.put(tdscTradeView.getAppId(), listingAppList.size()+"");
					}
					
					if(bidderAppList!=null && bidderAppList.size()>0){
						blockAndBidderCountMap.put(tdscTradeView.getAppId(), bidderAppList.size()+"");
					}
					i++;
				}
			}
			request.setAttribute("tradeBlockList", tradeBlockList);
			request.setAttribute("blockAndListingAppMap", blockAndListingAppMap);
			request.setAttribute("blockAndListingCountMap", blockAndListingCountMap);
			request.setAttribute("blockAndBidderCountMap", blockAndBidderCountMap);
		}
		return mapping.findForward("subscribe-block-list");
	}
	
	/**
	 * ѡ��ĳ���ؿ�� ��ʾ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("blockInfo", tdscBlockAppView);
		String certNo = (String) request.getSession().getAttribute("certNo");
		PersonInfo personInfo = subscribeService.getPersonInfo(certNo);
		request.setAttribute("personInfo", personInfo);
		return mapping.findForward("subscribe-block-info");
	}
	
	/**
	 * ��֤�Ƿ��깺���õؿ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward validate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String certNo = (String) request.getSession().getAttribute("certNo");
		String retString = "0";
		TdscBidderApp getInfo = subscribeService.getSubscribeInfo(certNo, appId);
		PersonInfo person = this.subscribeService.getPersonInfo(certNo);
		if(person != null){
			TdscCorpInfo corp = this.subscribeService.getCorpByName(person.getBidderName());
			if(corp != null)
				retString = "2";
		}
		response.setContentType("text/html; charset=GBK");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		if ( null != getInfo) {
			retString = "1";
		}
		pw.write(retString);
		pw.close();
		return null;
	}
	
//	/**
//	 * ��Ҫ�깺
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	public ActionForward addMySubscribe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String appId = request.getParameter("appId");
//		String certNo = (String) request.getSession().getAttribute("certNo");
//		return new ActionForward("subscribe.do?method=bankList&bidderId=" + StringUtils.trimToEmpty(getInfo.getBidderId()), true);
//	}
	
	/**
	 * ����ѡ����ʽҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward chooseBidderType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		if (StringUtils.isNotEmpty(appId)) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		}
		//��ʼ������
		String uploadFileId = request.getParameter("uploadFileId");
		String bidderType = request.getParameter("bidderType");
		String isCreateComp = request.getParameter("isCreateComp");
		if(StringUtils.isNotBlank(uploadFileId)){
			List fileList = subscribeService.findFileRefList(uploadFileId,null);
			request.setAttribute("fileList", fileList);
			request.setAttribute("uploadFileId", uploadFileId);
		}
		if(StringUtils.isNotBlank(bidderType)){
			request.setAttribute("bidderType", bidderType);
		}
		if(StringUtils.isNotBlank(isCreateComp)){
			request.setAttribute("isCreateComp", isCreateComp);
		}
		return mapping.findForward("subscribe-bidder-type");
	}
	

	
	
	/**
	 * ��֤�Ƿ��깺���õؿ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward downAppFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileId = request.getParameter("fileId");
		String busId = request.getParameter("busId");
		String catalog = request.getParameter("catalog");
		FileRef fileRef = null;
		if(StringUtils.isNotBlank(fileId)){
			fileRef = subscribeService.getFileRefById(fileId);
		}else if(StringUtils.isNotBlank(busId)){

			fileRef = subscribeService.findFileRefByBusId(busId, catalog);
		}
		
		File file = new File(fileRef.getFilePath());
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		if (file.exists()) {
			try{
				InputStream fis = new BufferedInputStream(new FileInputStream(file));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				response.reset();
				String outPutName = new String(fileRef.getFileName().getBytes("GBK"), "ISO-8859-1");
				response.addHeader("Content-disposition", "attachment;filename=" + outPutName);
				response.addHeader("Content-Length","" + file.length());
				response.setContentType(genContentType(fileRef.getFileType())+"; charset=GBK");
			    OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			    toClient.write(buffer);
			    toClient.flush();
			    toClient.close();
			} catch(Exception ex) {
		    	throw ex;
		    } finally {
		        if (bis != null)
		            bis.close();
		          if (bos != null)
		            bos.close();
		    }
		      
		} 
		return null;
	}

	/**
	 * ���ݺ�׺��ȡ�����ļ�����.
	 * 
	 * @param suffix
	 *            ��׺
	 * @return
	 */
	public String genContentType(String suffix) {
		if (StringUtils.isNotBlank(suffix)) {
			suffix = suffix.toLowerCase();
		}
		if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
			return "application/x-msdownload";
		} else if ("doc".equals(suffix) || "docx".equals(suffix)) {
			return "application/msword";
		} else if ("jpeg".equals(suffix)) {
			return "image/jpeg";
		} else if ("png".equals(suffix)) {
			return "image/png";
		} else if ("pdf".equals(suffix)) {
			return "application/pdf";
		} else if ("txt".equals(suffix)) {
			return "text/plain";
		} else if ("pdf".equals(suffix)) {
			return "application/pdf";
		}
		return null;
	}

	
	/**
	 * ���澺��ʽ,�Լ����Ͼ����ϴ����ļ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveBidderType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		PersonForm personForm = (PersonForm) form;
		//��������(1:�������� 2:���Ͼ���)
		String bidderType = personForm.getBidderType();
		//�������븽��
		if(personForm.getJmrFile() != null){
			FormFile jmrFile = personForm.getJmrFile();
			if (GlobalConstants.BIDDER_TYPE_UNION.equals(personForm.getBidderType())) {
				String fileType = jmrFile.getFileName().substring(jmrFile.getFileName().lastIndexOf(".")+1);
				FileRef fileRef = subscribeService.uploadFile(null,jmrFile.getFileData() , jmrFile.getFileName(), fileType,GlobalConstants.JMSQ_FILE);
				request.getSession().setAttribute("jmrFileId", fileRef.getFileId());
			}
		}
		//���Ͼ������븽��
		if(personForm.getJmrFile2() != null){
			FormFile jmrFile2 = personForm.getJmrFile2();
			if (GlobalConstants.BIDDER_TYPE_UNION.equals(personForm.getBidderType())) {
				String fileType = jmrFile2.getFileName().substring(jmrFile2.getFileName().lastIndexOf(".")+1);
				FileRef fileRef = subscribeService.uploadFile(null,jmrFile2.getFileData() , jmrFile2.getFileName(), fileType,GlobalConstants.LHJM_FILE);
				request.getSession().setAttribute("jmrFileId2", fileRef.getFileId());
			}
		}
		
		request.getSession().setAttribute("bidderType", bidderType);
		
		//��ת�������б�ҳ��
		return new ActionForward("subscribe.do?method=bankList&appId=" + appId, true);
	}
	
	/**
	 * ��ʾ�����б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward bankList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String certNo = (String) request.getSession().getAttribute("certNo");
		if (StringUtils.isNotEmpty(appId)) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		}
		if (StringUtils.isNotEmpty(certNo)) {
			PersonInfo personInfo = subscribeService.getPersonInfo(certNo);
			request.setAttribute("personInfo", personInfo);
		}
		//������ҳ���������
		String uploadFileId = request.getParameter("uploadFileId");
		String bidderType = request.getParameter("bidderType");
		String isCreateComp = request.getParameter("isCreateComp");
		request.setAttribute("uploadFileId", uploadFileId);
		request.setAttribute("bidderType", bidderType);
		request.setAttribute("isCreateComp", isCreateComp);
		return mapping.findForward("subscribe-bank-list");
	}
	/**
	 * ��¼�������и���ҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bankId = request.getParameter("selectedBankId");
		String bidderId = request.getParameter("bidderId");
		
		request.setAttribute("bankId", bankId);
		request.setAttribute("bidderId", bidderId);
		return mapping.findForward("redirect-to-bank");
	}
	
	/**
	 * ���������ҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward goBuy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bankId = request.getParameter("bankId");
		String bidderId = request.getParameter("bidderId");
		TdscBidderApp bidderApp = subscribeService.getSubscribeInfo(bidderId);
		if (bidderApp != null) {
			bidderApp.setBankId(bankId);
			tradeServer.persistentBidderApp(bidderApp);
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(bidderApp.getAppId());
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
			request.setAttribute("bidderApp", bidderApp);
		}
		request.setAttribute("bankId", bankId);
		request.setAttribute("bidderId", bidderId);
		return mapping.findForward("subscribe-go-buy");
	}
	
	
	/**
	 * ��ȡ���ʺ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward buy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//����IDΪ99ʱΪ�������У���ϵͳ�����������˺�!
		String bankId = request.getParameter("selectedBankId");
		String personId = request.getParameter("personId");
		String appId = request.getParameter("appId");
		String bankName = request.getParameter("selectedBankName");
		PersonInfo personInfo = subscribeService.getPersonInfo(personId);
		// TdscBidderApp bidderApp = subscribeService.getSubscribeInfo(bidderId);


		if (personInfo != null) {
			TdscTradeView tdscTradeView = tradeServer.getTdscTradeViewAppById(appId);
			TdscBidderApp bidderApp = null;
			TdscBidderPersonApp tdscBidderPersonApp = null;
			if ("1".equals(tdscTradeView.getIsPurposeBlock())) {
				// ���Ϊ����ؿ�
				bidderApp = tradeServer.getPurposeBidderAppByAppId(appId);
				if (bidderApp != null) {
					tdscBidderPersonApp = tradeServer.getBidderPersonAppByBidderId(bidderApp.getBidderId());
					if (tdscBidderPersonApp != null) {
						if (!StringUtils.trimToEmpty(personInfo.getOrgNo()).equals(tdscBidderPersonApp.getOrgNo())) {
							bidderApp = null;
							tdscBidderPersonApp = null;
						} else {
							bidderApp.setIsPurposePerson("1");
						}
					}
				}
			} 
			if (bidderApp == null){
				bidderApp = new TdscBidderApp();
				bidderApp.setIsPurposePerson("0");
			}
			if (tdscBidderPersonApp == null) {
				tdscBidderPersonApp = new TdscBidderPersonApp();
			}
			Timestamp nowTime = new Timestamp(System.currentTimeMillis());
			//TdscBidderApp bidderApp = new TdscBidderApp();
			// ȡ���û���Ϣ
			//BeanUtils.copyProperties(bidderApp, personInfo);
			initBidderApp(bidderApp, personInfo);
			initBidderPersonApp(tdscBidderPersonApp, personInfo);
			//��ѡ��������ʱԤ���ɵ�bidderId
//			String bidderId = (String)request.getSession().getAttribute("bidderId");
			//bidderApp.setBidderId(bidderId);
			bidderApp.setUserId(personId);
			bidderApp.setAppId(tdscTradeView.getAppId());
			bidderApp.setNoticeId(tdscTradeView.getNoticeId());
			bidderApp.setAcceptDate(nowTime);
			bidderApp.setBankId(bankId);
			//����ʽ
			bidderApp.setUploadFileId((String)request.getParameter("uploadFileId"));
			bidderApp.setBidderType((String)request.getParameter("bidderType"));
			bidderApp.setIsCreateComp((String)request.getParameter("isCreateComp"));
			if("1".equals(isTest)||"2".equals(isTest)||"99".equals(bankId)){
				//����ʹ��
				String number = getBankNumber();
				bidderApp.setBankNumber(number);
			}
			bidderApp.setSgDate(nowTime);
			String sqNumber = subscribeService.createSqNumber();
			bidderApp.setSqNumber(sqNumber);
			// �ⲿ����Ҫ�ں����ж���֮����ܻ�ȡ��
//			String certNo = subscribeService.generateCertNo();
//			bidderApp.setCertNo(certNo);
//			bidderApp.setAcceptNo(certNo);
//			bidderApp.setYktXh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
//			bidderApp.setYktBh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��
			if(org.apache.struts.util.TokenProcessor.getInstance().isTokenValid(request,true)){
				tradeServer.persistenBidder(bidderApp, tdscBidderPersonApp);
			}else{
				//�ظ��ύ�ý�����ʾ��Ϣ������
				request.setAttribute("refresh", "true");
			}
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(bidderApp.getAppId());
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			
			if("0".equals(isTest)&&!"99".equals(bankId)){
				//��ʽ����ʹ��,�������з��ص����˺š�
				BankService service = new BankService();
				service.sendClientG00003(bankId, bidderApp.getBidderId());	
			}
			TdscBidderApp newBidder = null;
			if(StringUtils.isNotBlank(bidderApp.getBidderId())){
				newBidder = (TdscBidderApp)subscribeService.getTdscBidderAppDao().get(bidderApp.getBidderId());
			}else{
				newBidder = bidderApp;
				
			}
			
			//��������Ͼ���,������ϴ����ļ�
			//if (GlobalConstants.BIDDER_TYPE_UNION.equals(newBidder.getBidderType())) {
			//	String jmrFileId = (String)request.getSession().getAttribute("jmrFileId");
			///	String jmrFileId2 = (String)request.getSession().getAttribute("jmrFileId2");
			//	subscribeService.bindBidderAppWithFileRef(newBidder.getBidderId(), jmrFileId, jmrFileId2);
			//}
			
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
			request.setAttribute("bidderApp", newBidder);
			request.setAttribute("bankId", bankId);
			request.setAttribute("bankName", bankName);
			request.setAttribute("bidderId", bidderApp.getBidderId());
		}
		return mapping.findForward("subscribe-buy-frame");
	}
	
	private void initBidderApp(TdscBidderApp bidderApp, PersonInfo personInfo) {
		bidderApp.setIsAccptSms(personInfo.getIsAccptSms());
		bidderApp.setPushSmsPhone(personInfo.getPushSmsPhone());
//		bidderApp.setBidderType("1"); //��������(1:�������� 2:���Ͼ���)
		bidderApp.setIfDownloadZgzs("1");
		bidderApp.setIfzwt("0");
	}
	
	private void initBidderPersonApp(TdscBidderPersonApp bidderPersonApp, PersonInfo personInfo) {
		bidderPersonApp.setBidderLxdh(personInfo.getBidderLxdh());
		bidderPersonApp.setBidderLxdz(personInfo.getBidderLxdz());
		
		bidderPersonApp.setBidderName(personInfo.getBidderName());
		bidderPersonApp.setBidderProperty(personInfo.getBidderProperty());
		bidderPersonApp.setBidderYzbm(personInfo.getBidderYzbm());
		bidderPersonApp.setBidderZjhm(personInfo.getBidderZjhm());
		
		bidderPersonApp.setBidderZjlx(personInfo.getBidderZjlx());
		bidderPersonApp.setCorpFddbrZjhm(personInfo.getCorpFddbrZjhm());
		bidderPersonApp.setCorpFddbrZjlx(personInfo.getCorpFddbrZjlx());
		bidderPersonApp.setCorpFr(personInfo.getCorpFr());
		bidderPersonApp.setCorpFrZjhm(personInfo.getCorpFrZjhm());
		bidderPersonApp.setCorpFrZjlx(personInfo.getCorpFrZjlx());
		bidderPersonApp.setLinkManName(personInfo.getLinkManName());
		bidderPersonApp.setOrgNo(personInfo.getOrgNo());
		
		bidderPersonApp.setBidderProperty(personInfo.getBidderProperty());
	}

	/**
	 * ��ȡ���ʺ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward seebuy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = request.getParameter("bidderId");
		TdscBidderApp bidderApp = subscribeService.getSubscribeInfo(bidderId);
		if (bidderApp != null) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(bidderApp.getAppId());
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
			request.setAttribute("bidderApp", bidderApp);
			request.setAttribute("bankId", bidderApp.getBankId());
		}
		request.setAttribute("bidderId", bidderId);
		
		return mapping.findForward("subscribe-buy");
	}
	

	private String getBankNumber() {
		long time = System.currentTimeMillis();
		long random = (long)(Math.random() * 1000000000);
		String number = time + "" + random;
		number = number.substring(0, 16);
		return number;
	}

	/**
	 * �鿴�ҵ��깺�б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward mySubscribe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String certNo = (String) request.getSession().getAttribute("certNo");
		System.out.println("certNo--->" + certNo);
		List list = subscribeService.getMySubscribeList(certNo);
		request.setAttribute("tdscBidderApp", list);
		request.setAttribute("flag", request.getParameter("flag"));
		return mapping.findForward("subscribe-of-me");
	}
	
	/**
	 * ɾ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delSubscribe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = request.getParameter("bidderId");
		subscribeService.delSubscribe(bidderId);
		return new ActionForward("trans.do?method=queryMyTransList", true);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveSelectHaoPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strBidderId = (String) request.getParameter("bidderId");
		String strConNum = (String) request.getParameter("conNum");
		String noticeId = (String) request.getParameter("noticeId");
		//String certNo = subscribeService.generateCertNo();
		TdscBidderView bidderView = tradeServer.getBidderAppById(strBidderId);
		
		//���ͬһ����Ƿ�����ͬ�����ѱ�����,�������ݿ��Ѵ�����ͬ��������ҳ��������ʾ
		if(bidderView!=null&&tradeServer.checkSameHaoPaiByAppId(bidderView.getAppId(),strConNum)){
			return new ActionForward("subscribe.do?method=selectHaoPai&saveFlag=ERROR&bidderId="+strBidderId+"&noticeId="+noticeId, true);
		}
		tradeServer.saveSelectHaoPai(strBidderId, strConNum,null);
		CoreService.reloadClientPipe(bidderView.getUserId());
		request.setAttribute("saveFlag", "SUCCESS");
		return mapping.findForward("bidderSelectNumPage");
	}

	/**
	 * ѡ�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward selectHaoPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");
		String bidderId = request.getParameter("bidderId");
		
		String userId = getClientNo(request);
		List myBidderList = this.tradeServer.queryBidderAppListForme(userId, noticeId);
		if (myBidderList != null && myBidderList.size() > 0 ) {
			TdscBidderApp bidderApp = (TdscBidderApp)myBidderList.get(0);
			String haopai = bidderApp.getConNum();
			String certNo = bidderApp.getCertNo();
			tradeServer.saveSelectHaoPai(bidderId, haopai,certNo);

			return new ActionForward("subscribe.do?method=mySubscribe&flag=ok", true);
		}
		List list = this.tradeServer.findTdscBidderAppListByNoticeId(noticeId);
		Set set = new HashSet();
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) list.get(i);
			set.add(app.getConNum());
		}
		request.setAttribute("containsNum", set);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("bidderId", bidderId);

		return mapping.findForward("bidderSelectNumPage");
	}
	
	/**
	 * �鿴�깺��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward viewSubscribe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = (String) request.getParameter("bidderId");
		// ȡ�þ�����Ϣ
		TdscBidderView tdscBidderView = tradeServer.getBidderAppById(bidderId);
		// ȡ�õؿ���Ϣ
		TdscTradeView tdscTradeView = tradeServer.getTdscTradeViewAppById(tdscBidderView.getAppId());
		// �ؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(tdscBidderView.getAppId());
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBidderView", tdscBidderView);
		request.setAttribute("tdscTradeView", tdscTradeView);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("subscribe-info");
	}
	
	/**
	 * �鿴�ؿ���ϸ��Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward detailCurrentTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		request.setAttribute("appId", appId);
		// ���� planId�õ�������ϸ��Ϣ
		// �ؿ���Ϣ(list)��ÿ���ؿ��Ӧ�ĳ����ļ�
		List retList = null;
		if (StringUtils.isNotEmpty(appId)) {
			retList = new ArrayList();
			Map tmpMap = new HashMap();
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appId);
			TdscBlockAppView appView = commonQueryService.getTdscBlockAppView(condition);
			if (StringUtils.trimToEmpty(appId).equals(appView.getAppId())) {
				tmpMap.put("TdscBlockAppView", appView);
				tmpMap.put("TdscBlockInfo", tradeServer.getBlockInfoByAppId(appView.getBlockId()));
				if ("1".equals(appView.getIsPurposeBlock())) {
					TdscBidderView bidderApp = tradeServer.getYixiangNameLikeAppId(appView.getAppId());
					if (bidderApp != null)
						tmpMap.put("pursposeName", bidderApp.getBidderName());
				}
				//tmpMap.put("TdscBlockRemisemoneyDefrayList", tradeServer.getTdscBlockRemisemoneyDefrayList(blockId));
				tmpMap.put("TdscBlockInfoPart", tradeServer.getTdscBlockPartList(appView.getBlockId()).get(0));
				tmpMap.put("TdscBlockTranApp", tradeServer.getTdscBlockTranAppById(appView.getAppId()));
				retList.add(tmpMap);
			}
		}
		request.setAttribute("retList", retList);
		return mapping.findForward("detailCurrentTradePage");
	}

	
	/**
	 * 
	 * ȡ���û����ʸ�֤���� 2011110025
	 * 
	 * @param request
	 * @return
	 */
	private String getClientNo(HttpServletRequest request) {
		String clientNo = (String) request.getSession().getAttribute("certNo");
		return clientNo;
	}

	
	/**
	 * �����ҵ�������ؿ��б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward myYiXiangList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = (String)request.getSession().getAttribute("certNo");
		ArrayList tradeBlockList = new ArrayList();
		List list = tradeServer.fixMyYiXiangList(userId);
		for(int i=0;i<list.size();i++){
			TdscBlockTranApp view = (TdscBlockTranApp)list.get(i);
			TdscTradeView tradeView = tradeServer.getTdscTradeViewAppById(view.getAppId());
			tradeBlockList.add(tradeView);
		}
		request.setAttribute("tradeBlockList", tradeBlockList);
		return mapping.findForward("subscribe-yx-block-list");
	}


	/**
	 * ��ӡ����֪ͨ��
	 * ����ҳ�� subcribe-jj-notice.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return subcribe-jj-notice.jsp
	 * @throws Exception
	 */
	public ActionForward printJJNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String bidderId = request.getParameter("bidderId");
		//����bidderId��ȡ����������bidderName
		TdscBidderPersonApp tdscBidderPersonApp = tradeServer.getBidderPersonAppByBidderId(bidderId) ;
		String bidderName = tdscBidderPersonApp.getBidderName();
		TdscBlockTranApp  tdscBlockTranApp =  tradeServer.getTdscBlockTranAppById(appId);
		String planId = tdscBlockTranApp.getPlanId();
		String blockNoticeNo = tdscBlockTranApp.getBlockNoticeNo();
		TdscBlockPlanTable tdscBlockPlanTable =tradeServer.getBlockPlanTableById(planId);
		String listEndDate =  DateUtil.date2String(tdscBlockPlanTable.getListEndDate(),"yyyy��MM��dd��HHʱmm��");
		request.setAttribute("bidderName", bidderName);
		request.setAttribute("blockNoticeNo", blockNoticeNo);
		request.setAttribute("listEndDate", listEndDate);
		return mapping.findForward("subscribe-jj-notice");

	}
	
	/**
	 * ���ҵı�֤��
	 * ����ҳ�� subcribe-margin-amount.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return subcribe-margin-amount.jsp
	 * @throws Exception
	 */
	public ActionForward openMarginAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//String result = request.getParameter("result");
		String inacct = request.getParameter("inacct");
		//���wsjyBankApp����ļ���
		List list = subscribeService.findPayList(inacct);
		request.setAttribute("list", list);
		
		
		return mapping.findForward("subscribe-margin-amount");

	}
	/**
	 * ���ھ��ú�ؿ��������¹�˾������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward viewNewCompany(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("blockInfo", tdscBlockAppView);
		String certNo = (String) request.getSession().getAttribute("certNo");
		PersonInfo personInfo = subscribeService.getPersonInfo(certNo);
		request.setAttribute("personInfo", personInfo);
		return mapping.findForward("newcompany");
	}
	/**
	 *  ��ӡ����������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("blockInfo", tdscBlockAppView);
		String certNo = (String) request.getSession().getAttribute("certNo");
		PersonInfo personInfo = subscribeService.getPersonInfo(certNo);
		request.setAttribute("personInfo", personInfo);
		return mapping.findForward("subscribe-jingmaishu");
	}
	/**
	 * ɾ�����Ͼ��򸽼�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileId = request.getParameter("fileId");
		String retString = "1";
		try{
			FileRef fileRef = subscribeService.getFileRefById(fileId);
			File file = new File(fileRef.getFilePath());
			if(file.exists()){
				file.delete();
			}
			subscribeService.delFileRefById(fileId);
		}catch (Exception e) {
			retString = "0";
		}
		response.setContentType("text/html; charset=GBK");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		pw.write(retString);
		pw.close();
		return null;
	}
}	

