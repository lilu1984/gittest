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
 * 申购
 * @author Gordon
 *
 */
public class SubscribeAction extends BaseAction {
	//是否为测试环境，若不为1则为测试环境
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
	 * 获取用户信息
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
	 * 持久化用户对象
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
		//竞买申请附件
		if(personForm.getJmrFile()!=null){
			FormFile jmrFile = personForm.getJmrFile();
			String fileType = jmrFile.getFileName().substring(jmrFile.getFileName().lastIndexOf(".")+1);
			FileRef jmsqFile = subscribeService.uploadFile(personInfo.getBidderId(),jmrFile.getFileData() , jmrFile.getFileName(), fileType,GlobalConstants.JMSQ_FILE);
			request.setAttribute("jmsqFile", jmsqFile);
		}
		//联合竞买申请附件
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
	 * 验证竞买人信息是否完整
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
	 * 取得可申购的公告信息列表
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
		// 如果是流程中的节点需要设置节点编号 RECORD_BIDDER_APPLY
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
	 * 根据公告取得对应的地块列表
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
			Map blockAndListingAppMap = new LinkedHashMap();//存放出让地块ID与最高挂牌记录对象的映射关系
			Map blockAndListingCountMap = new LinkedHashMap();//存放出让地块ID与当前挂牌轮次的映射关系
			Map blockAndBidderCountMap = new LinkedHashMap();//存放出让地块ID与当前挂牌轮次的映射关系
			for(int i=0; i<tradeBlockList.size(); ){
				TdscTradeView tdscTradeView = (TdscTradeView)tradeBlockList.get(i);
				if("04".equals(tdscTradeView.getTranResult())){//04表示交易终止
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
	 * 选中某个地块后 显示申请书
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
	 * 验证是否申购过该地块
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
//	 * 我要申购
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
	 * 进入选择竞买方式页面
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
		//初始化数据
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
	 * 验证是否申购过该地块
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
	 * 根据后缀获取导出文件类型.
	 * 
	 * @param suffix
	 *            后缀
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
	 * 保存竞买方式,以及联合竞买上传的文件
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
		//竞买类型(1:单独竞买 2:联合竞买)
		String bidderType = personForm.getBidderType();
		//竞买申请附件
		if(personForm.getJmrFile() != null){
			FormFile jmrFile = personForm.getJmrFile();
			if (GlobalConstants.BIDDER_TYPE_UNION.equals(personForm.getBidderType())) {
				String fileType = jmrFile.getFileName().substring(jmrFile.getFileName().lastIndexOf(".")+1);
				FileRef fileRef = subscribeService.uploadFile(null,jmrFile.getFileData() , jmrFile.getFileName(), fileType,GlobalConstants.JMSQ_FILE);
				request.getSession().setAttribute("jmrFileId", fileRef.getFileId());
			}
		}
		//联合竞买申请附件
		if(personForm.getJmrFile2() != null){
			FormFile jmrFile2 = personForm.getJmrFile2();
			if (GlobalConstants.BIDDER_TYPE_UNION.equals(personForm.getBidderType())) {
				String fileType = jmrFile2.getFileName().substring(jmrFile2.getFileName().lastIndexOf(".")+1);
				FileRef fileRef = subscribeService.uploadFile(null,jmrFile2.getFileData() , jmrFile2.getFileName(), fileType,GlobalConstants.LHJM_FILE);
				request.getSession().setAttribute("jmrFileId2", fileRef.getFileId());
			}
		}
		
		request.getSession().setAttribute("bidderType", bidderType);
		
		//跳转到银行列表页面
		return new ActionForward("subscribe.do?method=bankList&appId=" + appId, true);
	}
	
	/**
	 * 显示银行列表
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
		//申请书页面参数传递
		String uploadFileId = request.getParameter("uploadFileId");
		String bidderType = request.getParameter("bidderType");
		String isCreateComp = request.getParameter("isCreateComp");
		request.setAttribute("uploadFileId", uploadFileId);
		request.setAttribute("bidderType", bidderType);
		request.setAttribute("isCreateComp", isCreateComp);
		return mapping.findForward("subscribe-bank-list");
	}
	/**
	 * 登录网上银行付款页面
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
	 * 进入代付款页面
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
	 * 获取子帐号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward buy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//银行ID为99时为其他银行，由系统生成虚拟子账号!
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
				// 如果为意向地块
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
			// 取得用户信息
			//BeanUtils.copyProperties(bidderApp, personInfo);
			initBidderApp(bidderApp, personInfo);
			initBidderPersonApp(tdscBidderPersonApp, personInfo);
			//在选择竞买类型时预生成的bidderId
//			String bidderId = (String)request.getSession().getAttribute("bidderId");
			//bidderApp.setBidderId(bidderId);
			bidderApp.setUserId(personId);
			bidderApp.setAppId(tdscTradeView.getAppId());
			bidderApp.setNoticeId(tdscTradeView.getNoticeId());
			bidderApp.setAcceptDate(nowTime);
			bidderApp.setBankId(bankId);
			//竞买方式
			bidderApp.setUploadFileId((String)request.getParameter("uploadFileId"));
			bidderApp.setBidderType((String)request.getParameter("bidderType"));
			bidderApp.setIsCreateComp((String)request.getParameter("isCreateComp"));
			if("1".equals(isTest)||"2".equals(isTest)||"99".equals(bankId)){
				//本地使用
				String number = getBankNumber();
				bidderApp.setBankNumber(number);
			}
			bidderApp.setSgDate(nowTime);
			String sqNumber = subscribeService.createSqNumber();
			bidderApp.setSqNumber(sqNumber);
			// 这部分需要在和银行对账之后才能获取到
//			String certNo = subscribeService.generateCertNo();
//			bidderApp.setCertNo(certNo);
//			bidderApp.setAcceptNo(certNo);
//			bidderApp.setYktXh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡编号
//			bidderApp.setYktBh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡芯片号
			if(org.apache.struts.util.TokenProcessor.getInstance().isTokenValid(request,true)){
				tradeServer.persistenBidder(bidderApp, tdscBidderPersonApp);
			}else{
				//重复提交让界面提示消息并返回
				request.setAttribute("refresh", "true");
			}
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(bidderApp.getAppId());
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			
			if("0".equals(isTest)&&!"99".equals(bankId)){
				//正式环境使用,保存银行返回的子账号。
				BankService service = new BankService();
				service.sendClientG00003(bankId, bidderApp.getBidderId());	
			}
			TdscBidderApp newBidder = null;
			if(StringUtils.isNotBlank(bidderApp.getBidderId())){
				newBidder = (TdscBidderApp)subscribeService.getTdscBidderAppDao().get(bidderApp.getBidderId());
			}else{
				newBidder = bidderApp;
				
			}
			
			//如果是联合竞买,则关联上传的文件
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
//		bidderApp.setBidderType("1"); //竞买类型(1:单独竞买 2:联合竞买)
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
	 * 获取子帐号
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
	 * 查看我的申购列表
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
	 * 删除
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
		
		//检测同一块地是否有相同号牌已被保存,若再数据库已存在相同号牌则在页面做出提示
		if(bidderView!=null&&tradeServer.checkSameHaoPaiByAppId(bidderView.getAppId(),strConNum)){
			return new ActionForward("subscribe.do?method=selectHaoPai&saveFlag=ERROR&bidderId="+strBidderId+"&noticeId="+noticeId, true);
		}
		tradeServer.saveSelectHaoPai(strBidderId, strConNum,null);
		CoreService.reloadClientPipe(bidderView.getUserId());
		request.setAttribute("saveFlag", "SUCCESS");
		return mapping.findForward("bidderSelectNumPage");
	}

	/**
	 * 选择号牌
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
	 * 查看申购信息
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
		// 取得竞买信息
		TdscBidderView tdscBidderView = tradeServer.getBidderAppById(bidderId);
		// 取得地块信息
		TdscTradeView tdscTradeView = tradeServer.getTdscTradeViewAppById(tdscBidderView.getAppId());
		// 地块信息
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(tdscBidderView.getAppId());
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("tdscBidderView", tdscBidderView);
		request.setAttribute("tdscTradeView", tdscTradeView);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		return mapping.findForward("subscribe-info");
	}
	
	/**
	 * 查看地块详细信息
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
		// 根据 planId得到所有详细信息
		// 地块信息(list)，每个地块对应的出让文件
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
	 * 取得用户的资格证书编号 2011110025
	 * 
	 * @param request
	 * @return
	 */
	private String getClientNo(HttpServletRequest request) {
		String clientNo = (String) request.getSession().getAttribute("certNo");
		return clientNo;
	}

	
	/**
	 * 查找我的有意向地块列表
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
	 * 打印竞价通知书
	 * 返回页面 subcribe-jj-notice.jsp
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
		//根据bidderId获取竞买人名称bidderName
		TdscBidderPersonApp tdscBidderPersonApp = tradeServer.getBidderPersonAppByBidderId(bidderId) ;
		String bidderName = tdscBidderPersonApp.getBidderName();
		TdscBlockTranApp  tdscBlockTranApp =  tradeServer.getTdscBlockTranAppById(appId);
		String planId = tdscBlockTranApp.getPlanId();
		String blockNoticeNo = tdscBlockTranApp.getBlockNoticeNo();
		TdscBlockPlanTable tdscBlockPlanTable =tradeServer.getBlockPlanTableById(planId);
		String listEndDate =  DateUtil.date2String(tdscBlockPlanTable.getListEndDate(),"yyyy年MM月dd日HH时mm分");
		request.setAttribute("bidderName", bidderName);
		request.setAttribute("blockNoticeNo", blockNoticeNo);
		request.setAttribute("listEndDate", listEndDate);
		return mapping.findForward("subscribe-jj-notice");

	}
	
	/**
	 * 打开我的保证金
	 * 返回页面 subcribe-margin-amount.jsp
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
		//获得wsjyBankApp对象的集合
		List list = subscribeService.findPayList(inacct);
		request.setAttribute("list", list);
		
		
		return mapping.findForward("subscribe-margin-amount");

	}
	/**
	 * 关于竞得后地块后拟成立新公司的申请
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
	 *  打印竞买申请书
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
	 * 删除联合竞买附件
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

