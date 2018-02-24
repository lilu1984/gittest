package com.wonders.tdsc.bidder.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.bank.service.TdscBankAppService;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.bidder.web.form.TdscBidderForm;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.blockwork.web.form.TdscBlockForm;
import com.wonders.tdsc.blockwork.web.form.TdscDicBean;
import com.wonders.tdsc.bo.FileRef;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBankApp;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderMaterial;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderProvide;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscReturnBail;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.retbail.service.TdscReturnBailService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.tdscbase.service.TdscBidderViewService;

public class TdscBidderAppAction extends BaseAction {

	private TdscScheduletableService tdscScheduletableService;

	private CommonQueryService commonQueryService;

	private TdscBidderViewService tdscBidderViewService;

	private TdscBidderAppService tdscBidderAppService;

	private CommonFlowService commonFlowService;

	private TdscLocalTradeService tdscLocalTradeService;

	private TdscReturnBailService tdscReturnBailService;

	private TdscBankAppService tdscBankAppService;
	private TdscBlockInfoService tdscBlockInfoService;
	public void setTdscBankAppService(TdscBankAppService tdscBankAppService) {
		this.tdscBankAppService = tdscBankAppService;
	}

	public void setTdscBidderViewService(TdscBidderViewService tdscBidderViewService) {
		this.tdscBidderViewService = tdscBidderViewService;
	}

	public TdscBlockInfoService getTdscBlockInfoService() {
		return tdscBlockInfoService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscLocalTradeService(TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setTdscReturnBailService(TdscReturnBailService tdscReturnBailService) {
		this.tdscReturnBailService = tdscReturnBailService;
	}

	private static String OPERATOR_ADD = "ADD";

	private static String OPERATOR_MOD = "MODIFY";

	public ActionForward printSalesConfirmation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fromMenu = request.getParameter("fromMenu");

		String type = request.getParameter("type");

		String strTradeStatus = request.getParameter("tradeStatus") + "";

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}
		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setBlockName(StringUtil.GBKtoISO88591(request.getParameter("blockName")));
		condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(request.getParameter("blockNoticeNo")));
		condition.setTransferMode(request.getParameter("transferMode"));
		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
		// ȷ�����URL,�������Ϊ"1",������ϢΪ"2",����¼��Ϊ"3",���¼��Ϊ"4"
		condition.setEnterWay(request.getParameter("enterWay"));
		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}

		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {// ��ʷ����
			request.setAttribute("tradeStatus", strTradeStatus);
			condition.setStatus("02");// (00-δ����;01-������;02-���׽���)
			condition.setTranResult("01");// ���׽�� 00δ���� 01 ���׳ɹ���02 ����ʧ�ܣ����꣩��04
											// ��ֹ���ף�
		} else {// ��ǰ����
			condition.setStatus("01");// (00-δ����;01-������;02-���׽���)
		}

		condition.setOrderKey("blockNoticeNo");
		// condition.setOrderType("desc");
		// 2011-03-21 ��Ҫ�ѹ��˵������ݼ��뵽list ����ʾ����
		// ����ؿ�û���˾���ĵؿ�
		PageList tdscBlockAppViewPageList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);

		if (tdscBlockAppViewPageList != null) {
			List tempList = tdscBlockAppViewPageList.getList();
			if (null != tempList && tempList.size() > 0) {
				HashMap appIdMap = new HashMap();
				for (int i = 0; i < tempList.size(); i++) {
					TdscBlockAppView commonInfo = (TdscBlockAppView) tempList.get(i);
					if (null != commonInfo.getAppId() && !"".equals(commonInfo.getAppId())) {
						TdscBlockInfo tdscBlockInfo = tdscScheduletableService.getBlockInfoApp(commonInfo.getBlockId());
						if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0)) > 0) {
							appIdMap.put(commonInfo.getAppId(), "1");
						}
						TdscListingInfo tdscListingInfo = this.tdscLocalTradeService.getTdscListingInfoByAppId(commonInfo.getAppId());
						if (tdscListingInfo != null) {
							commonInfo.setResultCert(tdscListingInfo.getListCert());
							commonInfo.setResultDate(tdscListingInfo.getListDate());
						}
						//�������Ͼ����������ʾ
						TdscBidderApp bidderApp = tdscBidderAppService.getBidderAppByCertNo(commonInfo.getResultCert());
						if(bidderApp!=null){
							List jmsqFileList = tdscBlockInfoService.findFileRefByBusId(bidderApp.getBidderId(), GlobalConstants.JMSQ_FILE);
							List lhjmFileList = tdscBlockInfoService.findFileRefByBusId(bidderApp.getBidderId(), GlobalConstants.LHJM_FILE);
							if(jmsqFileList!=null&&jmsqFileList.size()>0){
								FileRef jmsqFile = (FileRef)jmsqFileList.get(0);
								commonInfo.setJmsqFileId(jmsqFile.getFileId()+"."+jmsqFile.getFileType());
							}
							if(lhjmFileList!=null&&lhjmFileList.size()>0){
								FileRef lhjmFile = (FileRef)lhjmFileList.get(0);
								commonInfo.setLhjmFileId(lhjmFile.getFileId()+"."+lhjmFile.getFileType());
							}
						}
					}
				}
				request.setAttribute("appIdMap", appIdMap);
			}
		}
		
		request.setAttribute("tdscBlockAppViewPageList", tdscBlockAppViewPageList);
		request.setAttribute("condition", condition);
		request.setAttribute("type", type);

		return mapping.findForward("toPrintSalesConfirmationPage");
	}

	/**
	 * ��ӡ���о�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printAllBidderPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");

		String stitle = "";
		List retList = new ArrayList();
		// ���ݹ����ѯ���еؿ�
		// ���ݵؿ飬��ѯ���о�����
		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setNoticeId(noticeId);
		cond.setOrderKey("blockNoticeNo");
		List tmpList = commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);
		if (tmpList != null && tmpList.size() > 0) {
			for (int i = 0; i < tmpList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) tmpList.get(i);

				stitle = appView.getNoitceNo().substring(0, 10) + Integer.parseInt(appView.getNoitceNo().substring(10, 12)) + "��";
				TdscBlockTranApp tranApp = tdscLocalTradeService.getTdscBlockTranApp(appView.getAppId());
				if (tranApp != null && !StringUtil.isEmpty(tranApp.getResultName())) {
					TdscBidderView tdscBidderView = tdscBidderViewService.getBidderViewByPersonName(tranApp.getResultName());

					TdscBidderForm bean = new TdscBidderForm();

					// appView.getBlockNoticeNo();// �ؿ���
					// appView.getBlockName();// �ؿ�����
					// tranApp.getResultName(); // ���õ�λ����
					//
					// tdscBidderView.getLinkManName(); //��ϵ��
					// tdscBidderView.getBidderLxdh();//��ϵ�绰

					bean.setBlockNoticeNo(appView.getBlockNoticeNo());
					bean.setBlockName(appView.getBlockName());
					bean.setBidderName(tranApp.getResultName());
					bean.setLinkManName(tdscBidderView.getLinkManName());
					bean.setBidderLxdh(tdscBidderView.getBidderLxdh());

					retList.add(bean);
				}
			}
		}

		request.setAttribute("rptTitle", stitle + "���õ�λ��ϵ���嵥");
		request.setAttribute("retList", retList);

		return mapping.findForward("printBidderedPersonList");

	}

	/**
	 * ��ӡ�����ʸ�ȷ���� ���� ����λ���ơ����Ų�ѯ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintBidderZGZS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderName = request.getParameter("bidderName");
		String kahao = request.getParameter("kahao");// оƬ��
		String fromMenu = request.getParameter("fromMenu");
		if (!"Y".equals(fromMenu) && (!isEmpty(bidderName) || !isEmpty(kahao))) {
			String pageNo = request.getParameter("currentPage");

			// ��ѯ�б�
			TdscBidderCondition bidderCond = new TdscBidderCondition();
			if (!isEmpty(bidderName))
				bidderCond.setBidderName(StringUtil.GBKtoISO88591(bidderName));
			if (!isEmpty(kahao))
				bidderCond.setYktBh(kahao);

			List retList = new ArrayList();

			List queryList = (List) tdscBidderViewService.queryBidderByNameKahao(bidderCond);
			if (queryList != null && queryList.size() > 0) {
				for (int i = 0; i < queryList.size(); i++) {
					TdscBidderView bidderView = (TdscBidderView) queryList.get(i);
					if (!StringUtil.isEmpty(bidderView.getNoticeId())) {
						System.out.println("NoticeId is : " + bidderView.getNoticeId());
						try {
							// �˴����noticeId �Ҳ�����¼�������ڷ������������ݣ�ʹ�ø÷���ʹ�����������ȥ

							String noticeNo = tdscBidderAppService.getNoticeNoByNoticeId(bidderView.getNoticeId());
							bidderView.setNoticeId(noticeNo);

							retList.add(bidderView);
						} catch (Exception e) {
							continue;
						}
					} else {
						continue;
					}
				}
			}

			PageList pageList = new PageList();
			int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
			int currentPage = 0;
			if (pageNo != null && pageNo.trim().equals("") == false)
				currentPage = Integer.parseInt(pageNo);

			if (pageNo == null || Integer.parseInt(pageNo) < 1)
				currentPage = 1;

			pageList = PageUtil.getPageList(retList, pageSize, currentPage);

			request.setAttribute("queryList", retList);
			request.setAttribute("pageList", pageList);
			if (!isEmpty(bidderName))
				bidderCond.setBidderName(bidderName);
			request.setAttribute("condition", bidderCond);
		}
		return mapping.findForward("toPrintBidderZGZS");

	}

	public ActionForward saveSelectHaoPai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strBidderId = (String) request.getParameter("bidderId");
		String strConNum = (String) request.getParameter("conNum");

		tdscBidderAppService.saveSelectHaoPai(strBidderId, strConNum);

		// ����������ˣ����䱨��ѡ����ƺ���Ҫ����������ؿ�����һ�ֹ��Ƽ�¼�����Ƽ۸�Ϊ��ʼ�ۣ�ʱ��Ϊ������ʼʱ��
		TdscBidderApp tdscBidderApp = tdscBidderAppService.queryBidderAppInfo(strBidderId);
		if ("1".equals(tdscBidderApp.getIsPurposePerson())) {
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(strBidderId);
			String purposeAppId = tdscBidderPersonApp.getPurposeAppId();
			tdscLocalTradeService.insertListingAppOfPurposePerson(tdscBidderApp, purposeAppId);
		}

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
		String bidderId = (String) request.getParameter("bidderId");

		List list = this.tdscBidderAppService.findAppByNoticeId(noticeId);
		Set set = new HashSet();
		for (int i = 0; null != list && i < list.size(); i++) {
			TdscBidderApp app = (TdscBidderApp) list.get(i);
			set.add(app.getConNum());
		}
		request.setAttribute("contaisNum", set);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("bidderId", bidderId);

		return mapping.findForward("bidderSelectNumPage");
	}

	/**
	 * ��ӡ�����ʸ�ȷ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toBidderCredentials(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderName = (String) request.getParameter("bidderName");
		// String bidderCardNum = (String)
		// request.getParameter("bidderCardNum");

		List retList = tdscBidderAppService.queryBidderCredList(bidderName);

		request.setAttribute("BidderCredList", retList);
		return mapping.findForward("toBidderCredentialsPage");

	}

	/**
	 * ɾ��������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");
		String bidderId = (String) request.getParameter("bidderId");
		String bidderPersonId = (String) request.getParameter("bidderPersonId");
		// ����IDɾ�������� TDSC_BIDDER_APP /TDSC_BIDDER_PERSON_APP
		tdscBidderAppService.delBidderInfo(bidderId, bidderPersonId);

		StringBuffer sb = new StringBuffer("bidderApp.do?method=toListBidders");
		sb.append("&noticeId=").append(noticeId);
		ActionForward f = new ActionForward(sb.toString(), true);
		return f;

	}

	/**
	 * ��ת�� ����������ҳ�� by xys 2011-02-22
	 */
	public ActionForward toAddBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");

		List bankDicList = tdscBidderViewService.queryBankDicList();
		if (bankDicList != null && bankDicList.size() > 0) {
			for (int i = 0; i < bankDicList.size();) {
				TdscDicBean tdscDicBean = (TdscDicBean) bankDicList.get(i);
				if ("0".equals(tdscDicBean.getIsValidity())) {
					bankDicList.remove(i);
				} else {
					i++;
				}
			}
		}
		request.setAttribute("bankDicList", bankDicList);

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (!StringUtil.isEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			condition.setStatus("01");
			condition.setOrderKey("xuHao");
			// ͨ��ͨ�ýӿڲ�ѯ������ͼ
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		request.setAttribute("noticeId", noticeId);

		request.setAttribute("opt", OPERATOR_ADD);

		return mapping.findForward("toAddBidPerPage");
	}

	/**
	 * ת���޸ľ����˵�ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toModBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");
		// String appId = (String) request.getParameter("appId");
		String bidderId = (String) request.getParameter("bidderId");
		String bidderPersonId = (String) request.getParameter("bidderPersonId");
		String isPurposePerson = (String) request.getParameter("isPurposePerson");
		String strSaveOk = (String) request.getParameter("saveOk");

		request.setAttribute("saveOk", strSaveOk);

		String ifPrint = (String) request.getParameter("ifPrint");
		if (!StringUtil.isEmpty(ifPrint) && "Y".equals(ifPrint))
			request.setAttribute("ifPrint", "Y");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (!StringUtil.isEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			//condition.setStatus("01");// ����ѯ�����еĵؿ飬������ֹ�ĵؿ�
			condition.setOrderKey("xuHao");
			// ͨ��ͨ�ýӿڲ�ѯ������ͼ
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		TdscBidderPersonApp bidderPersonApp = null;
		List tdscBidderMaterial = null;

		// 1. ��ѯ���е� appIds �� ��֤��
		// 2. ��ѯ�ļ��б�������ļ��б�
		String appIds = "";
		// String bzjDzse = "";
		TdscBidderApp bidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
		appIds = bidderApp.getAppId();
		bidderPersonApp = tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());
		tdscBidderMaterial = tdscBidderAppService.queryOnePerMat(bidderPersonApp.getBidderPersonId());

		// 3. ��ѯ��������Ϣ, �� bidderApp ��
		// 4. ��ѯת������Ϣ, �� bidderApp ��
		// 5. ��ѯ��������Ϣ, �� bidderPersonApp ��
		// 6. ��ѯ���׿���Ϣ, �� bidderApp ��

		request.setAttribute("bidderApp", bidderApp);
		request.setAttribute("bidderPersonApp", bidderPersonApp);
		request.setAttribute("tdscBidderMaterial", tdscBidderMaterial);
		request.setAttribute("appIds", appIds);
		// request.setAttribute("bzjDzse", bzjDzse);

		// ���˻���֤����Ϣ���У���ȡ��֤��������Ϣ��
		List bzjInfoList = tdscBidderAppService.queryTdscReturnBailList(bidderId);
		request.setAttribute("bzjInfoList", bzjInfoList);

		// ��֤�������ֵ�
		List bankDicList = tdscBidderViewService.queryBankDicList();
		if (bankDicList != null && bankDicList.size() > 0) {
			for (int i = 0; i < bankDicList.size();) {
				TdscDicBean tdscDicBean = (TdscDicBean) bankDicList.get(i);
				if ("0".equals(tdscDicBean.getIsValidity())) {
					bankDicList.remove(i);
				} else {
					i++;
				}
			}
		}
		request.setAttribute("bankDicList", bankDicList);

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("isPurposePerson", isPurposePerson);

		request.setAttribute("opt", OPERATOR_MOD);

		if (StringUtils.isNotBlank(bidderApp.getConNum())) {
			request.setAttribute("saveFlag", "SUCCESS");
		}

		// ��ѯ�þ����˵ı�֤��������
		List bankAppList = tdscBankAppService.queryTdscBankAppListByBidderId(bidderId);
		request.setAttribute("bankAppList", bankAppList);

		return mapping.findForward("toAddBidPerPage");
	}

	private void mergeYiXiangRen(String noticeId) {

		// 1. ɾ��һ������
		// 2. �ϲ���ɾ�����ݵ� app_id
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		condition.setNoticeId(noticeId);
		condition.setOrderKey("blockNoticeNo");
		// ͨ��ͨ�ýӿڲ�ѯ������ͼ
		List appViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		List tdscBidderPersonAppList = new ArrayList();

		if (appViewList != null && appViewList.size() > 0) {
			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) appViewList.get(i);
				if ("1".equals(appView.getIsPurposeBlock())) {// ��������ؿ���д���
					// û��������������ˣ�ֻ�� appId �� IsPurposeBlock
					TdscBidderApp tdscBidderApp = tdscBidderAppService.getYixiangPerson(appView.getAppId());
					if (tdscBidderApp != null) {
						TdscBidderPersonApp bidderPersonApp = tdscBidderAppService.getTdscBidderPersonByBidderId(tdscBidderApp.getBidderId());
						tdscBidderPersonAppList.add(bidderPersonApp);
					}
				}
			}
		}

		// ��tdscBidderPersonAppList��������/��������������
		if (tdscBidderPersonAppList != null && tdscBidderPersonAppList.size() > 2) {
			Collections.sort(tdscBidderPersonAppList, new Comparator() {
				public int compare(Object o1, Object o2) {
					TdscBidderPersonApp bidderPersonApp1 = (TdscBidderPersonApp) o1;
					TdscBidderPersonApp bidderPersonApp2 = (TdscBidderPersonApp) o2;
					return bidderPersonApp1.getBidderName().compareTo(bidderPersonApp2.getBidderName());
				}
			});
		}

		String yixiangAppIds = "";
		String bidderId = "";
		String bidderPersonId = "";
		String bidderName = "";

		if (tdscBidderPersonAppList != null && tdscBidderPersonAppList.size() > 2) {
			for (int i = 0; i < tdscBidderPersonAppList.size(); i++) {
				TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderPersonAppList.get(i);
				if (bidderName.equals(bidderPersonApp.getBidderName())) {
					bidderId += bidderPersonApp.getBidderId() + ",";
					bidderPersonId += bidderPersonApp.getBidderPersonId() + ",";
					yixiangAppIds += bidderPersonApp.getPurposeAppId() + ",";

					if (!StringUtil.isEmpty(bidderId) && !StringUtil.isEmpty(bidderPersonId)) {
						// ɾ������ TDSC_BIDDER_APP , TDSC_BIDDER_PERSON_APP
						String[] tmpBidderIds = bidderId.split(",");
						String[] tmpBidderPersonIds = bidderPersonId.split(",");
						for (int m = 0; m < tmpBidderIds.length - 1; m++) {
							tdscBidderAppService.delBidderInfo(tmpBidderIds[m], tmpBidderPersonIds[m]);
						}
						tdscBidderAppService.modifyPurposePersonAppIds(tmpBidderIds[tmpBidderIds.length - 1], tmpBidderPersonIds[tmpBidderPersonIds.length - 1], yixiangAppIds);
					}
				} else {
					bidderName = bidderPersonApp.getBidderName();
					bidderId = bidderPersonApp.getBidderId() + ",";
					bidderPersonId = bidderPersonApp.getBidderPersonId() + ",";
					yixiangAppIds = bidderPersonApp.getPurposeAppId() + ",";
				}
			}
		}
	}

	/**
	 * ��ѯ���ι����µ����о�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	public ActionForward toListBidders(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		String noticeId = (String) request.getParameter("noticeId");

		// ��Ҫ����ϲ�������������ͬ�����ݣ�
		mergeYiXiangRen(noticeId);

		List retList = new ArrayList();
		Map map = new HashMap();
		if (!StringUtil.isEmpty(noticeId)) {
			String bidderName = "";

			// ��ѯ���ι������еľ�����/������
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);

			List appViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (appViewList != null && appViewList.size() > 0)
				for (int i = 0; i < appViewList.size(); i++) {
					TdscBlockAppView appView = (TdscBlockAppView) appViewList.get(i);

					List bidderAppList = null;
					// if ("tdsc".equals(user.getUserId()) ||
					// "���".equals(user.getDisplayName()) ||
					// "�ƫ�".equals(user.getDisplayName()))
					// if ("tdsc".equals(user.getUserId()))
					// bidderAppList =
					// tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());
					// else {
					// bidderAppList =
					// tdscBidderAppService.queryBidderAppListLikeAppIdAndUserId(appView.getAppId(),
					// user.getUserId());
					// // ��Ҫ�������������˵ĵ�λ
					// List tempList =
					// tdscBidderAppService.queryBidderAppYixiangList(noticeId);
					// // bidderAppList.addAll();
					// System.out.println(tempList);
					// }

					bidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());

					if (bidderAppList != null && bidderAppList.size() > 0)
						for (int m = 0; m < bidderAppList.size(); m++) {
							TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);

							if ("1".equals(bidderApp.getIsPurposePerson())) {
								TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());

								TdscBidderForm bean = new TdscBidderForm();

								bean.setNoticeId(noticeId);
								bean.setAppId(bidderApp.getAppId());
								System.out.println("bidderApp.getBidderId()=" + bidderApp.getBidderId());
								bean.setBidderId(bidderApp.getBidderId());
								bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
								bean.setBidderName(bidderPersonApp.getBidderName());
								bean.setAcceptNo(bidderApp.getAcceptNo());
								bean.setCertNo(bidderApp.getCertNo());

								bean.setIsPurposePerson(bidderApp.getIsPurposePerson());

								map.put(bidderPersonApp.getBidderName(), bean);

							} else {
								if (StringUtils.trimToEmpty(bidderApp.getUserId()).equals(user.getUserId()) || "tdsc".equals(user.getUserId())) {
									TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());

									TdscBidderForm bean = new TdscBidderForm();

									bean.setNoticeId(noticeId);
									bean.setAppId(bidderApp.getAppId());
									System.out.println("bidderApp.getBidderId()=" + bidderApp.getBidderId());
									bean.setBidderId(bidderApp.getBidderId());
									bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
									bean.setBidderName(bidderPersonApp.getBidderName());
									bean.setAcceptNo(bidderApp.getAcceptNo());
									bean.setCertNo(bidderApp.getCertNo());

									bean.setIsPurposePerson(bidderApp.getIsPurposePerson());

									map.put(bidderPersonApp.getBidderName(), bean);
								}
							}

						}

				}

		}

		if (map.size() != 0) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				// Object key = entry.getKey();
				Object value = entry.getValue();
				retList.add((TdscBidderForm) value);
			}
			request.setAttribute("allBidderList", retList);
		}

		return mapping.findForward("toListBiddersPage");
	}

	/**
	 * �����޸ľ�����/����������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveJmrInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String operatorType = request.getParameter("opt") + "";

		String noticeId = request.getParameter("noticeId") + "";
		// List bidderIds = new ArrayList();

		// ���������Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		// ���� �ύ���ϵ� List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// ���ʵ������
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				// tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());

				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				// tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		String appIds[] = tdscBidderForm.getAppIds();

		// ��֤������
		String[] bzjDzqk = tdscBidderForm.getBzjDzqks();
		bzjDzqk = trimBank(bzjDzqk);
		// save into TDSC_RETURN_BAIL
		List tdscReturnBailList = new ArrayList();

		// �ʸ�֤���� Ψһ
		if (OPERATOR_ADD.equals(operatorType)) {
//			String certNo = tdscBidderAppService.generateCertNo();
//			tdscBidderForm.setCertNo(certNo);
//			tdscBidderForm.setAcceptNo(certNo);
//
//			tdscBidderForm.setYktXh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
//			tdscBidderForm.setYktBh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��
		}

		String bidderId = "";
		boolean isPurposePerson = false;

		String strAppId = "";

		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {

				String appId = appIds[i];

				String bzjBank = bzjDzqk[i];

				strAppId += appIds[i] + ",";

				TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
				condition.setAppId(appId);
				TdscBlockAppView appView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);

				TdscReturnBail returnBail = new TdscReturnBail();
				returnBail.setPlanId(appView.getPlanId());
				returnBail.setAppId(appId);
				returnBail.setBlockId(appView.getBlockId());
				// returnBail.setBidderId(bidderId);
				returnBail.setBidderBail(appView.getMarginAmount());
				returnBail.setBzjBank(bzjBank);
				returnBail.setIfReturn("00");// δ�˻�

				tdscReturnBailList.add(returnBail);

				// �ؿ��Ƿ���������
				if ("1".equals(appView.getIsPurposeBlock())) {
					isPurposePerson = true;
				} else {
					// TODO ���������˵ĵؿ飬�����ĵ�һ�������˼���һ�ֹ��ƣ���д��� TDSC_LISTING_APP and
					// TDSC_LISTING_INFO
					// �жϸõؿ��Ƿ��Ѿ��м�¼�ڱ���

					// 1. save TDSC_LISTING_INFO (listing_id=+1, app_id,
					// curr_price=�ÿ����صĳ�ʼ�۸�, curr_round=1, list_date=now date,
					// ykt_xh=����, list_cert=�ʸ�ȷ������)
					// 2. save TDSC_LISTING_APP (list_app_id=+1, listing_id,
					// listing_ser=1, price_type=1, list_cert=, list_price=,
					// list_date=, ytk_xh=, app_id)
					// String yktXh = tdscBidderForm.getYktXh();
					// BigDecimal initPrice = appView.getInitPrice();

					// tdscLocalTradeService.makeFirstPersonIsInitPrice(appId,
					// yktXh, certNo, initPrice);
				}
			}
		}

		String bankAppIdStr = tdscBidderForm.getBankAppIds();
		String[] bankAppIds = null;
		if (StringUtils.isNotBlank(bankAppIdStr)) {
			bankAppIds = bankAppIdStr.split(",");
		}

		if (OPERATOR_ADD.equals(operatorType)) {
			bidderId = tdscBidderAppService.saveJmrInfo(strAppId, false, tdscBidderForm, tdscBidMatList, user);
			// ��֤����Ϣ����
			tdscBidderAppService.saveReturnBzjInfo(bidderId, tdscReturnBailList);

			if (bankAppIds != null && bankAppIds.length > 0) {
				for (int i = 0; i < bankAppIds.length; i++) {
					TdscBankApp tdscBankApp = tdscBankAppService.getTdscBankAppById(bankAppIds[i]);
					tdscBankApp.setStatus("1");// ״̬��1Ϊ�ѹ���
					tdscBankApp.setBidderId(bidderId);
					tdscBankApp.setNoticeId(tdscBidderForm.getNoticeId());
					tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankApp);
				}
			}
		}
		if (OPERATOR_MOD.equals(operatorType)) {
			TdscBidderApp tdscBidderApp = tdscBidderAppService.queryBidderAppInfo(tdscBidderForm.getBidderId());
			if ("1".equals(tdscBidderApp.getIsPurposePerson()))
				isPurposePerson = true;
			else
				isPurposePerson = false;

			String purposeAppId = "";
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(tdscBidderForm.getBidderId());
			purposeAppId = tdscBidderPersonApp.getPurposeAppId();
			tdscBidderForm.setPurposeAppId(purposeAppId);
			tdscBidderForm.setYktXh(tdscBidderApp.getYktXh());// ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
			tdscBidderForm.setYktBh(tdscBidderApp.getYktBh());// ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��

			bidderId = tdscBidderAppService.updateJmrInfo(strAppId, isPurposePerson, tdscBidderForm, tdscBidMatList, user);
			// ��֤����Ϣ����
			tdscBidderAppService.saveReturnBzjInfo(bidderId, tdscReturnBailList);

			// ��������þ����˵ı�֤���¼��Ϣ����������
			List tdscBankAppOldList = tdscBankAppService.queryTdscBankAppListByBidderId(bidderId);
			if (tdscBankAppOldList != null && tdscBankAppOldList.size() > 0) {
				for (int i = 0; i < tdscBankAppOldList.size(); i++) {
					TdscBankApp tdscBankAppOld = (TdscBankApp) tdscBankAppOldList.get(i);
					tdscBankAppOld.setStatus("0");// ״̬��0Ϊδ����
					tdscBankAppOld.setBidderId("");// �����þ����˵Ĺ�����ϵ
					tdscBankAppOld.setNoticeId("");
					tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankAppOld);
				}
			}
			for (int i = 0; null != bankAppIds && i < bankAppIds.length; i++) {
				TdscBankApp tdscBankApp = tdscBankAppService.getTdscBankAppById(bankAppIds[i]);
				tdscBankApp.setStatus("1");// ״̬��1Ϊ�ѹ���
				tdscBankApp.setBidderId(bidderId);
				tdscBankApp.setNoticeId(tdscBidderForm.getNoticeId());
				tdscBankAppService.saveOrUpdateTdscBankApp(tdscBankApp);
			}
		}

		request.setAttribute("opt", OPERATOR_MOD);
		// request.setAttribute("ifPrint", "Y");

		// return mapping.findForward("saveJmrInfoOK");
		TdscBidderPersonApp bidderPersonApp = tdscBidderAppService.queryBidPerson(bidderId);

		StringBuffer sb = new StringBuffer("bidderApp.do?method=toModBidder");
		sb.append("&noticeId=").append(noticeId);
		sb.append("&bidderId=").append(bidderId);
		sb.append("&bidderPersonId=").append(bidderPersonApp.getBidderPersonId());
		sb.append("&isPurposePerson=").append(isPurposePerson);
		sb.append("&ifPrint=Y");
		sb.append("&saveOk=SUCCESS");
		ActionForward f = new ActionForward(sb.toString(), true);
		return f;
	}

	private String[] trimBank(String[] bzjBank) {
		String tmp = "";
		if (bzjBank != null && bzjBank.length > 0) {
			for (int i = 0; i < bzjBank.length; i++) {
				if (!StringUtils.isEmpty(bzjBank[i])) {
					tmp += bzjBank[i] + ",";
				}
			}
		}
		return tmp.split(",");
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

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscNoticeAppCondition noticeCondition = new TdscNoticeAppCondition();
		List appIdList = new ArrayList();
		List noticeIdList = new ArrayList();
		List retList = new ArrayList();

		// ����������еĽڵ���Ҫ���ýڵ��� RECORD_BIDDER_APPLY
		condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
		condition.setOrderKey("blockNoticeNo");
		/* ����ҳ������ */
		// condition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// PageList pageList = (PageList)
		// commonQueryService.queryTdscBlockAppViewPageList(condition);
		// ��ȡ�û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			// condition.setUserId(user.getUserId());
		}
		// ����������ѯ������״̬�ĵؿ�appId�б�
		List tdscblockAppViewList = (List) commonQueryService.queryTdscBlockAppViewList(condition);
		if (tdscblockAppViewList != null && tdscblockAppViewList.size() > 0) {
			for (int i = 0; i < tdscblockAppViewList.size(); i++) {
				TdscBlockAppView tdscblockAppView = (TdscBlockAppView) tdscblockAppViewList.get(i);
				if (tdscblockAppView != null)
					appIdList.add(tdscblockAppView.getAppId());
			}
			noticeIdList = tdscBidderAppService.findNoticeIdListByAppIdList(appIdList);
			if (noticeIdList != null && noticeIdList.size() > 0) {
				bindObject(noticeCondition, form);
				if (!StringUtil.isEmpty(noticeCondition.getUniteBlockName()))
					noticeCondition.setUniteBlockName(StringUtil.GBKtoISO88591(noticeCondition.getUniteBlockName()));
				if (!StringUtil.isEmpty(noticeCondition.getTradeNum()))
					noticeCondition.setTradeNum(StringUtil.GBKtoISO88591(noticeCondition.getTradeNum()));
				if (!StringUtil.isEmpty(noticeCondition.getTransferMode()))
					noticeCondition.setTransferMode(StringUtil.GBKtoISO88591(noticeCondition.getTransferMode()));

				noticeCondition.setNoticeIdList(noticeIdList);
				retList = tdscBidderAppService.findNoticeListByCondition(noticeCondition);

				// ��ѯ�ù��������еؿ��Ƿ�������������ˣ��������һ���ؿ���ڣ���UI �������� ����ʾ ���������ˡ���
				// ���û���κ�һ���ؿ���ڣ�ֻ��ʾ��������
				if (retList != null && retList.size() > 0)
					for (int i = 0; i < retList.size(); i++) {
						TdscNoticeApp noticeApp = (TdscNoticeApp) retList.get(i);
						TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
						cond.setNoticeId(noticeApp.getNoticeId());
						List appList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(cond);
						if (appList != null && appList.size() > 0)
							for (int j = 0; j < appList.size(); j++) {
								TdscBlockAppView appView = (TdscBlockAppView) appList.get(j);
								if ("1".equals(appView.getIsPurposeBlock())) {
									noticeApp.setLinkManTel("displayManager"); // ��ʾ�˵ؿ��������ˣ��˹��������ʾ���������ˡ�
									// ��ť
									break;
								}

								// ��ѯ���о������б�appId ��Ҫ like
								List tmpList = tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());

								if (tmpList != null && tmpList.size() > 0) {
									noticeApp.setLinkManTel("displayManager"); // ��ʾ�˵ؿ��о����ˣ��˹��������ʾ���������ˡ�
									// ��ť
									break;
								}
							}
					}
			}
		}

		// ƴװ��ҳ��Ϣ
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage")))
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		PageList retPageList = PageUtil.getPageList(retList, pageSize, currentPage);

		request.setAttribute("condition", noticeCondition);
		request.setAttribute("pageList", retPageList);

		return mapping.findForward("jmrNoticeList");
	}

	// deleted by xys 2011-02-22
	// public ActionForward query(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) throws
	// Exception {
	// // ��ȡҳ�����
	// TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
	// bindObject(condition, form);
	// // ����û���Ϣ
	// SysUser user = (SysUser)
	// request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
	// String appUserId = String.valueOf(user.getUserId());
	// // �жϸ��û��Ƿ�Ϊ������������
	// List buttonList = (List)
	// request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
	// Map buttonMap = new HashMap();
	// for (int j = 0; j < buttonList.size(); j++) {
	// String id = (String) buttonList.get(j);
	// buttonMap.put(id, buttonList.get(j));
	// }
	// // �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
	// if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
	// condition.setUserId(user.getUserId());
	// }
	// // ����������еĽڵ���Ҫ���ýڵ��� RECORD_BIDDER_APPLY
	// condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
	// /* ����ҳ������ */
	// condition.setPageSize(((Integer)
	// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
	// condition.setOrderKey("blockNoticeNo");
	//
	// // ��ѯ�б�
	// PageList pageList = (PageList)
	// commonQueryService.queryTdscBlockAppViewPageList(condition);
	// request.setAttribute("pageList", pageList);
	// request.setAttribute("condition", condition);
	// // ��ʾÿ���ؿ�ľ����������
	// if (pageList != null) {
	// List appList = (List) pageList.getList();
	// if (appList != null && appList.size() > 0) {
	// List retCountList = new ArrayList();
	// for (int i = 0; i < appList.size(); i++) {
	// TdscBlockAppView app = (TdscBlockAppView) appList.get(i);
	//
	// List countList = new ArrayList();
	// // ����ǡ����������������ѯ�������ݣ������ֻ��ѯ���������������
	// if (buttonMap != null) {
	// if (buttonMap.get(GlobalConstants.BUTTON_ID_JMSQZSL) != null) {
	// countList = (List)
	// tdscBidderAppService.findPageBidderList(app.getAppId());
	// } else {
	// countList = (List)
	// tdscBidderAppService.findPageBidderListByUserId(app.getAppId(),
	// appUserId);
	// }
	// }
	// retCountList.add(countList);
	// }
	// request.setAttribute("retCountList", retCountList);
	// }
	// }
	//
	// return mapping.findForward("bidderAppList");
	// }

	/**
	 * ����������þ������ص�λ�б�
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
	public ActionForward daochudwInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		bindObject(condition, form);

		String appId = (String) request.getParameter("appId");
		String blockNoticeNo = (String) request.getParameter("blockNoticeNo");
		// String blockNoticeNo_like = blockNoticeNo.substring(3);

		// ȡ�����ƹұ�źͳ��÷�ʽ
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		if (appId != null && !"".equals(appId)) {
			condition.setAppId(appId);
			tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		}

		List countList = new ArrayList();
		List personList = new ArrayList();

		TdscBidderCondition Listcondition = new TdscBidderCondition();
		Listcondition.setNoticeId(tdscBlockAppView.getNoticeId());
		// Listcondition.setAcceptNo(blockNoticeNo_like);
		countList = (List) tdscBidderAppService.findBidderListByJMSQZSL(Listcondition);

		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		// List countList2 = (List)
		// tdscBidderAppService.findPageBidderList(appId);
		if (countList != null && countList.size() > 0) {
			for (int i = 0; i < countList.size(); i++) {
				TdscBidderApp app = (TdscBidderApp) countList.get(i);
				String bidderId = app.getBidderId();
				String app_id = app.getAppId();
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBidderAppService.findTdscBlockTranApp(app_id);
				blockNoticeNo = tdscBlockTranApp.getBlockNoticeNo();
				app.setReviewResult(blockNoticeNo);

				String peasonName = "";
				personList = (List) tdscBidderAppService.queryBidderPersonList(bidderId);

				if (personList != null & personList.size() > 0) {
					for (int j = 0; j < personList.size(); j++) {
						tdscBidderPersonApp = (TdscBidderPersonApp) personList.get(j);
						// ��������
						if (j == 0)
							peasonName = tdscBidderPersonApp.getBidderName();

						// ���Ͼ���
						if (j > 0) {
							peasonName += "��" + tdscBidderPersonApp.getBidderName();
						}
					}
				}
				app.setReviewOpnn(peasonName);
			}
		}

		request.setAttribute("tradeNum", tdscBlockAppView.getTradeNum());
		request.setAttribute("transferMode", tdscBlockAppView.getTransferMode());
		request.setAttribute("countList", countList);

		return mapping.findForward("daochudwInfo");
	}

	/**
	 * ��ѯ�����ڰ�ĳ��ù���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAllNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String bidderType = (String) request.getParameter("bidderType");
		String type = (String) request.getParameter("type");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		TdscNoticeAppCondition noticeCondition = new TdscNoticeAppCondition();
		List appIdList = new ArrayList();
		List noticeIdList = new ArrayList();
		List retList = new ArrayList();

		// ����������еĽڵ���Ҫ���ýڵ��� RECORD_BIDDER_APPLY
		condition.setNodeId(FlowConstants.FLOW_NODE_BIDDER_APP);
		condition.setOrderKey("blockNoticeNo");
		/* ����ҳ������ */
		// condition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// PageList pageList = (PageList)
		// commonQueryService.queryTdscBlockAppViewPageList(condition);
		// ��ȡ�û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		// ����������ѯ������״̬�ĵؿ�appId�б�
		List tdscblockAppViewList = (List) commonQueryService.queryTdscBlockAppViewList(condition);
		if (tdscblockAppViewList != null && tdscblockAppViewList.size() > 0) {
			for (int i = 0; i < tdscblockAppViewList.size(); i++) {
				TdscBlockAppView tdscblockAppView = (TdscBlockAppView) tdscblockAppViewList.get(i);
				if (tdscblockAppView != null)
					appIdList.add(tdscblockAppView.getAppId());
			}

			noticeIdList = tdscBidderAppService.findNoticeIdListByAppIdList(appIdList);
			if (noticeIdList != null && noticeIdList.size() > 0) {
				bindObject(noticeCondition, form);

				if (noticeCondition.getUniteBlockName() != null && !"".equals(noticeCondition.getUniteBlockName()))
					noticeCondition.setUniteBlockName(StringUtil.GBKtoISO88591(noticeCondition.getUniteBlockName()));
				if (noticeCondition.getTradeNum() != null && !"".equals(noticeCondition.getTradeNum())) {
					noticeCondition.setTradeNum(StringUtil.GBKtoISO88591(noticeCondition.getTradeNum()));
				}

				noticeCondition.setNoticeIdList(noticeIdList);
				retList = tdscBidderAppService.findNoticeListByCondition(noticeCondition);
			}
		}

		// ƴװ��ҳ��Ϣ
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();

		PageList retPageList = PageUtil.getPageList(retList, pageSize, currentPage);

		if (noticeCondition.getUniteBlockName() != null && !"".equals(noticeCondition.getUniteBlockName())) {
			noticeCondition.setUniteBlockName(StringUtil.ISO88591toGBK(noticeCondition.getUniteBlockName()));
		}
		if (noticeCondition.getTradeNum() != null && !"".equals(noticeCondition.getTradeNum())) {
			noticeCondition.setTradeNum(StringUtil.ISO88591toGBK(noticeCondition.getTradeNum()));
		}
		request.setAttribute("condition", noticeCondition);
		request.setAttribute("bidderType", bidderType);
		request.setAttribute("type", type);
		request.setAttribute("pageList", retPageList);

		return mapping.findForward("jmrNoticeList");
	}

	/**
	 * ��ת�� ����������ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddBidPer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeId = (String) request.getParameter("noticeId");
		String bidType = (String) request.getParameter("bidderType");
		String bidderId = (String) request.getParameter("bidderId");
		if (StringUtils.isNotEmpty(bidderId)) {
			TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
			TdscBidderPersonApp tdscBidderPersonApp = this.tdscBidderAppService.getTdscBidderPersonByBidderId(bidderId);
			request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);

		}
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (noticeId != null) {
			condition.setNoticeId(noticeId);
			condition.setOrderKey("blockNoticeNo");
			// ͨ��ͨ�ýӿڲ�ѯ������ͼ
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		request.setAttribute("bidType", bidType);
		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("toAddBidPerPage");
	}

	/**
	 * **��ת�� ���Ͼ���--����������ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addUnionBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String jnfs = (String) request.getParameter("jnfs");
		String hasHead = (String) request.getParameter("hasHead");
		String transferMode = (String) request.getParameter("transferMode");
		String[] appIds = tdscBidderForm.getAppIds();
		// ����appId��ѯ������йص���Ϣ
		if (appIds != null && appIds.length > 0) {
			TdscBaseQueryCondition tdscBaseQuerycondition = new TdscBaseQueryCondition();
			tdscBaseQuerycondition.setAppId(appIds[0]);
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(tdscBaseQuerycondition);
			request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		}// ���÷��ص�blockType transferMode bzjJnfs
		request.setAttribute("bzjJnfs", jnfs);
		request.setAttribute("hasHead", hasHead);
		request.setAttribute("transferMode", transferMode);

		return mapping.findForward("addUnionBidderPage");
	}

	/**
	 * ���Ͼ���---��Ӷ��������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addUnBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String noticeId = tdscBidderForm.getNoticeId();
		String yktBh = tdscBidderForm.getYktBh();
		String ifCommit = request.getParameter("ifCommit");
		String type = request.getParameter("type"); // jmrgl����ת������ҳ�棬����ת������ҳ��
		String ifprint = request.getParameter("ifprint");// �ж��Ƿ��ӡ����11��Ϊ��ӡ�ռ��վݣ���bzj��Ϊ��ӡ��֤���ݴ�+��ӡ��;
		String provideBm = (String) tdscBidderForm.getProvideBm();

		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		bindObject(tdscBidderApp, form);

		// ����������������ϵľ�����
		String appIds[] = tdscBidderForm.getAppIds();
		String bidderPersonIds[] = tdscBidderForm.getBidderPersonIds();
		List bidPerList = new ArrayList();
		List bidMatList = new ArrayList();
		if (bidderPersonIds != null) {
			if (bidderPersonIds.length > 0) {
				for (int i = 0; i < bidderPersonIds.length; i++) {
					TdscBidderPersonApp bidPerApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonIds[i]);
					if (bidPerApp != null) {
						bidPerList.add(bidPerApp);
						List perMatList = tdscBidderAppService.queryMatList(bidPerApp.getBidderPersonId());
						if (perMatList != null && perMatList.size() > 0) {
							for (int j = 0; j < perMatList.size(); j++) {
								TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) perMatList.get(j);
								if (tdscBidderMaterial != null)
									bidMatList.add(tdscBidderMaterial);
							}
						}
					}
				}
			}
		}

		// ���澺���˼�������б����ж��ڸù��������޸ý��׿��ŵľ����ˣ����У�ȫɾ
		List appIdList = new ArrayList();
		if (noticeId != null && !"".equals(noticeId) && yktBh != null && !"".equals(yktBh)) {
			// ���ݹ����ѯ���еؿ�appIdList����ѯ�ù����е������øý��׿���ŵ��û�
			TdscBidderCondition bidderCondition = new TdscBidderCondition();
			bidderCondition.setNoticeId(noticeId);
			bidderCondition.setYktBh(yktBh);
			List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);

			if (bidderAppList != null && bidderAppList.size() > 0) {
				for (int nn = 0; nn < bidderAppList.size(); nn++) {
					TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(nn);
					if (bidderApp != null)
						tdscBidderAppService.delOneBidderByBidderId(bidderApp.getBidderId());
				}
			}
		}
		// ɾ������

		// ���������Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// ��������ڵ�UserID
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));
		// �ж��Ƿ��ύ:1Ϊ�ύ��0Ϊ���ύ��
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}
		String bidderId = "";
		if (appIds != null && appIds.length > 0) {
			for (int m = 0; m < appIds.length; m++) {
				if (!"".equals(appIds[m])) {
					tdscBidderApp.setAppId(appIds[m]);
					// copyԭ���Ͼ������������´���
					bidderId = tdscBidderAppService.addUnionBidder(provideBm, tdscBidderApp, bidPerList, bidMatList);
				}
			}
			// ɾ��ԭ���Ͼ�������Ϣ
			if (bidPerList != null && bidPerList.size() > 0)
				tdscBidderAppService.delLJBidderPer(bidPerList);
		}

		// TdscBidderForm retForm = new TdscBidderForm();
		// retForm.setAppId(tdscBidderForm.getAppId());
		// if("11".equals(type)){
		TdscBidderForm tempForm = new TdscBidderForm();
		tempForm.setBidderId(bidderId);
		tempForm.setNoticeId(noticeId);
		if ("11".equals(ifprint) || "22".equals(ifprint)) {
			tempForm.setIfprint(ifprint);
			return queryOneBidder(mapping, tempForm, request, response);
		}

		// }
		// return queryBidder(mapping, retForm, request, response);

		String forwardString = "bidderApp.do?method=query";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * ���Ͼ���---��Ӷ���Ѵ��ڵľ�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addExistUnBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String type = request.getParameter("type");
		String ifprint = request.getParameter("ifprint");
		String ifCommit = request.getParameter("ifCommit");
		// String provideBm = (String) tdscBidderForm.getProvideBm();
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		bindObject(tdscBidderApp, form);
		// ��������ľ�����
		String bidderPersonIds[] = tdscBidderForm.getBidderPersonIds();
		List bidPerList = new ArrayList();
		if (bidderPersonIds != null) {
			if (bidderPersonIds.length > 0) {
				for (int i = 0; i < bidderPersonIds.length; i++) {
					TdscBidderPersonApp bidPerApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonIds[i]);
					bidPerList.add(bidPerApp);
				}
			}
		}

		// ���������Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// Integer regionID = new Integer(user.getRegionID());
		// ��������ڵ�UserID
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));
		// �ж��Ƿ��ύ:1Ϊ�ύ��0Ϊ���ύ��
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}
		// �����ξ����������
		// String acceptNo =
		// tdscBidderAppService.createAcceptNo(tdscBidderForm.getAppId());

		// String
		// bidderId=tdscBidderAppService.addExistUnionBidder(provideBm,tdscBidderApp,
		// bidPerList);
		String bidderId = tdscBidderAppService.addExistUnionBidder(tdscBidderApp, bidPerList);
		TdscBidderForm retForm = new TdscBidderForm();
		retForm.setAppId(tdscBidderForm.getAppId());
		if ("11".equals(type)) {
			TdscBidderForm tempForm = new TdscBidderForm();
			tempForm.setBidderId(bidderId);
			tempForm.setType(type);
			if ("11".equals(ifprint)) {
				tempForm.setIfprint(ifprint);
			}
			return queryOneBidder(mapping, tempForm, request, response);
		}
		return queryBidder(mapping, retForm, request, response);
	}

	/**
	 * �����˹���--�޸Ķ��������--����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUnBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// ��ȡҳ�����
		String type = request.getParameter("type");
		String ifprint = request.getParameter("ifprint");
		String ifCommit = request.getParameter("ifCommit");
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		bindObject(tdscBidderApp, form);
		// ��������ڵ�userID
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));

		String bidderId = (String) tdscBidderForm.getBidderId();
		tdscBidderApp.setBidderId(bidderId);
		// �ж��Ƿ��ύ:1Ϊ�ύ��0Ϊ���ύ��
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}
		// ��ѯԭ����������
		TdscBidderApp oldBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// ���ԭ���ı�֤�����嵽�����,������ ,�ʸ�֤���� �����µ�ʵ�� ;
		// TdscBidderApp bidderApp = (TdscBidderApp)
		// tdscBidderAppService.queryBidderAppInfo(tdscBidderApp.getBidderId());
		if (oldBidderApp != null) {
			if (oldBidderApp.getBzjztDzqk() != null && oldBidderApp.getBzjztDzqk() != "") {
				tdscBidderApp.setBzjztDzqk(oldBidderApp.getBzjztDzqk());
			}
			if (oldBidderApp.getReviewResult() != null && oldBidderApp.getReviewResult() != "") {
				tdscBidderApp.setReviewResult(oldBidderApp.getReviewResult());
			}
			if (oldBidderApp.getCertNo() != null && oldBidderApp.getCertNo() != "") {
				tdscBidderApp.setCertNo(oldBidderApp.getCertNo());
			}
			// ��ԭ���������Ÿ����µ�ʵ��
			if (oldBidderApp.getAcceptNo() != null) {
				tdscBidderApp.setAcceptNo(oldBidderApp.getAcceptNo());
			}
		}
		// ����µľ�����
		String bidderPersonIds[] = tdscBidderForm.getBidderPersonIds();
		// ��ѯԭTdscBidderAppʵ������
		TdscBidderApp delBidApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		List bidPerList = new ArrayList();
		// �޸�ʱû�м����µ����Ͼ����� ֻ�޸�TdscBidderApp
		if (bidderPersonIds == null) {
			if (delBidApp.getAcceptDate() != null) {
				tdscBidderApp.setAcceptDate(delBidApp.getAcceptDate());
			} else {
				tdscBidderApp.setAcceptDate(new Timestamp(System.currentTimeMillis()));
			}
			tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);
			TdscBidderForm retForm = new TdscBidderForm();
			retForm.setAppId(tdscBidderForm.getAppId());
			if ("11".equals(type)) {
				TdscBidderForm tempForm = new TdscBidderForm();
				tempForm.setBidderId(bidderId);
				tempForm.setType(type);
				if ("11".equals(ifprint)) {
					tempForm.setIfprint(ifprint);
				}
				return queryOneBidder(mapping, tempForm, request, response);
			}
			return queryBidder(mapping, retForm, request, response);
		}

		// �޸�ʱ�������µ����Ͼ�����
		if (bidderPersonIds.length > 0) {
			for (int i = 0; i < bidderPersonIds.length; i++) {
				TdscBidderPersonApp bidPerApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonIds[i]);
				bidPerList.add(bidPerApp);
			}
		}

		tdscBidderAppService.updateUnionBidder(tdscBidderApp, bidPerList);
		TdscBidderForm retForm = new TdscBidderForm();
		retForm.setAppId(tdscBidderForm.getAppId());
		if ("11".equals(type)) {
			TdscBidderForm tempForm = new TdscBidderForm();
			tempForm.setBidderId(bidderId);
			tempForm.setType(type);
			if ("11".equals(ifprint)) {
				tempForm.setIfprint(ifprint);
			}
			return queryOneBidder(mapping, tempForm, request, response);
		}
		return queryBidder(mapping, retForm, request, response);
	}

	/**
	 * ���� bidderIdɾ�� ������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delOneBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ��Ĳ���
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String bidderId = (String) request.getParameter("bidderId");

		if (bidderId != null) {
			TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
			// ���澺���˼�������б����ж��ڸù��������޸ý��׿��ŵľ����ˣ����У�ȫɾ\
			if (tdscBidderApp != null) {
				TdscBidderCondition bidderCondition = new TdscBidderCondition();
				bidderCondition.setNoticeId(tdscBidderApp.getNoticeId());
				bidderCondition.setYktBh(tdscBidderApp.getYktBh());
				List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);

				if (bidderAppList != null && bidderAppList.size() > 0) {
					for (int nn = 0; nn < bidderAppList.size(); nn++) {
						TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(nn);
						if (bidderApp != null)
							tdscBidderAppService.delOneBidderByBidderId(bidderApp.getBidderId());
					}
				}
			}
			// ɾ������
		}

		// // ����bidderId��ѯ������������Ϣ
		// List bidderList = (List)
		// tdscBidderAppService.queryByBidderId(bidderId);
		// tdscBidderAppService.delOneBidder(bidderList);

		return queryBidder(mapping, tdscBidderForm, request, response);
	}

	/**
	 * ������������������б�
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
	public ActionForward querySqr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBidderCondition condition = new TdscBidderCondition();
		ActionForm actionForm = (ActionForm) request.getSession().getAttribute("lmrList");

		if (actionForm != null)
			bindObject(condition, form);

		// ����ҳ������
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// ��ѯ�б�
		if (actionForm == null)
			request.setAttribute("pageList", null);
		else
			request.setAttribute("pageList", tdscBidderAppService.findPersonPageList(condition));
		request.setAttribute("condition", condition);
		return mapping.findForward("bidderPersonAppList");
	}

	public ActionForward removeSqr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		tdscBidderAppService.removeById(request.getParameter("bidderPersonId"));
		return querySqr(mapping, form, request, response);
	}

	/**
	 * ����������þ������б� list_jmrgl.jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeId = (String) request.getParameter("noticeId");
		TdscBidderCondition condition = new TdscBidderCondition();
		List bidderGlList = new ArrayList();

		if (noticeId != null && !"".equals(noticeId)) {
			// ����û���Ϣ
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			String appUserId = String.valueOf(user.getUserId());

			// �жϸ��û��Ƿ�Ϊ������������
			List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
			Map buttonMap = new HashMap();
			for (int j = 0; j < buttonList.size(); j++) {
				String id = (String) buttonList.get(j);
				buttonMap.put(id, buttonList.get(j));
			}

			// �ж��Ƿ���ҳ���ϵġ����ء���ť���ύ
			String buttonType = (String) request.getParameter("buttonType");
			if ("00".equals(buttonType)) {
				// ���ǡ����ء���ť����ֻ��condition ���� appId
				// condition.setAppId(appId);
			} else {
				bindObject(condition, form);
			}

			condition.setNoticeId(noticeId);

			// ���� �����������˸����������ƹ���Ϣ��
			this.tdscBidderAppService.updateBidderNoticeId(noticeId);

			List bidderIdList = new ArrayList();
			// ����noticeIdȡ������yktbh����ͬ�ľ�������Ϣ�б�
			List bidderAppList = (List) tdscBidderAppService.queryBidderAppListByNoticeId(noticeId);
			if (bidderAppList != null && bidderAppList.size() > 0) {
				for (int i = 0; i < bidderAppList.size(); i++) {
					TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
					if (tdscBidderApp != null)
						bidderIdList.add(tdscBidderApp.getBidderId());
				}
			}
			List bidderYixiangList = tdscBidderAppService.queryBidderAppYixiangList(noticeId);
			for (int i = 0; bidderYixiangList != null && i < bidderYixiangList.size(); i++) {
				String bidderId = (String) bidderYixiangList.get(i);
				if (StringUtils.isNotEmpty(bidderId))
					bidderIdList.add(bidderId);
			}

			if (bidderIdList != null && bidderIdList.size() > 0)
				condition.setBidderIdList(bidderIdList);
			if (condition.getBidderName() != null && !"".equals(condition.getBidderName()))
				condition.setBidderName(StringUtil.GBKtoISO88591(condition.getBidderName()));

			// ����ǡ�����������������ϵͳ����Ա�����ѯ�������ݣ�
			if (buttonMap != null) {
				if (buttonMap.get(GlobalConstants.BUTTON_ID_JMSQZSL) != null || buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) != null) {
					bidderGlList = (List) tdscBidderAppService.queryBidderAppListByCondition(condition);
					// bidderGlList = (List)
					// tdscBidderAppService.findBidderList(condition);
				} else {
					// �����ֻ��ѯ���������������
					// �¼���ģ�Ҫ�����appUserId��ʾ��������ķ���;
					condition.setAppUserId(appUserId);
					condition.setIfCommit("0");
					bidderGlList = (List) tdscBidderAppService.queryBidderAppListByCondition(condition);
					// bidderGlList = (List)
					// tdscBidderAppService.findBidderListByJMSQZSL(condition);
				}
			}
		}

		if (condition.getBidderName() != null && !"".equals(condition.getBidderName()))
			condition.setBidderName(StringUtil.ISO88591toGBK(condition.getBidderName()));
		request.setAttribute("bidderGlList", bidderGlList);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("condition", condition);
		return mapping.findForward("bidderGlList");
	}

	/**
	 * �����������һ�������е����о������б� list_existJmrgl.jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryExistBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBidderCondition condition = new TdscBidderCondition();
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String appId = (String) tdscBidderForm.getAppId();
		// �ж��Ƿ���ҳ���ϵġ����ء���ť���ύ
		String buttonType = (String) request.getParameter("buttonType");
		if ("00".equals(buttonType)) {
			// ���ǡ����ء���ť����ֻ��condition ���� appId
			condition.setAppId(appId);
		} else {
			bindObject(condition, form);
		}

		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		List bidderGlList = new ArrayList();
		if (appId != null && !"".equals(appId)) {
			// ����appId��ѯ�õؿ��noticeId
			TdscBaseQueryCondition viewCondition1 = new TdscBaseQueryCondition();
			TdscBaseQueryCondition viewCondition2 = new TdscBaseQueryCondition();
			List appIdList = new ArrayList();

			viewCondition1.setAppId(appId);
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(viewCondition1);

			if (tdscBlockAppView.getNoticeId() != null && !"".equals(tdscBlockAppView.getNoticeId())) {
				viewCondition2.setNoticeId(tdscBlockAppView.getNoticeId());
				viewCondition2.setOrderKey("blockNoticeNo");
				viewCondition2.setOrderType("desc");

				condition.setNoticeId(tdscBlockAppView.getNoticeId());
				// ����noticeId�õ��ù����е����еؿ��б� appIdList
				List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(viewCondition2);
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
						TdscBlockAppView tdscAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
						if (!appId.equals(tdscAppView.getAppId())) {
							appIdList.add(tdscAppView.getAppId());
						}
					}
				}
			}
			// ����appIdList�õ����׿���Ų��ظ��ľ������б�
			bidderGlList = (List) tdscBidderAppService.findBidderListByAppIdList(appIdList);
		}

		// int pageSize = ((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		// PageList PageList = PageUtil.getPageList(bidderGlList,
		// pageSize,currentPage);

		request.setAttribute("bidderGlList", bidderGlList);
		request.setAttribute("appId", appId);
		request.setAttribute("condition", condition);
		return mapping.findForward("existBidderPersonList");
	}

	public ActionForward queryBidderCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBidderCondition bidCondition = new TdscBidderCondition();
		bindObject(bidCondition, form);
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String appId = (String) tdscBidderForm.getAppId();
		// ����appId��ѯ������йص���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		// PageList pageList =
		// (PageList)tdscBidderAppService.findPersonPageList(bidCondition);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appId", appId);
		return mapping.findForward("bidderGlList");
	}

	/**
	 * ���Ͼ��� �����һ�������˵���Ϣ
	 * 
	 * @param form
	 * @param request
	 */
	public ActionForward addOneOfUnionBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// form ת��ΪtdscBidderPersonAppʵ��
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		bindObject(tdscBidderPersonApp, form);
		// form ת��ΪtdscBidderMaterialʵ�� ����
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;

		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		// ���� �ύ���ϵ� List
		List tdscBidMatList = new ArrayList();
		// �ж��ǵڼ���
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// �����Ӧ��ʵ������
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				// tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				// tdscBidderMaterial.setBidderId(tdscBidderPersonApp.getBidderId());
				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		// �����ֶ���ӵľ����������
		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				// tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		// ����������
		TdscBidderPersonApp tdscBidPerApp = tdscBidderAppService.addOneBidderPerson(tdscBidderPersonApp, tdscBidMatList);
		request.setAttribute("tdscBidderPersonApp", tdscBidPerApp);

		return mapping.findForward("bginfosqrjsp");
	}

	/**
	 * ����bidderPersonId ��ѯ���Ͼ�����֮һ����Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryOneOfUnionBidPer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ����Ϣ
		String jnfs = (String) request.getParameter("jnfs");
		String hasHead = (String) request.getParameter("hasHead");
		String bidderPersonId = (String) request.getParameter("bidderPersonId");
		String blockTypeValue = (String) request.getParameter("blockTypeValue");
		String transferMode = (String) request.getParameter("transferMode");
		// ����bidderPersonId ��ѯһ�������˵���Ϣ
		TdscBidderPersonApp rtnPerson = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonId);
		List perMatList = (List) tdscBidderAppService.queryOnePerMat(bidderPersonId);
		List otherMatList = (List) tdscBidderAppService.queryOtherMatList(bidderPersonId);
		request.setAttribute("otherMatList", otherMatList);
		// �ж�������ǲ������Ͼ����е�ǣͷ��
		if ("1".equals(jnfs) && "1".equals(rtnPerson.getIsHead())) {
			hasHead = "no";
		}
		request.setAttribute("hasHead", hasHead);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("blockType", blockTypeValue);
		request.setAttribute("perMatList", perMatList);
		request.setAttribute("rtnPerson", rtnPerson);
		request.setAttribute("bzjJnfs", jnfs);
		return mapping.findForward("queryOneOfUnionBidPer");
	}

	/**
	 * �޸����Ͼ�����֮һ ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUnionBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String opeType = (String) request.getParameter("opeType");
		// ��ȡҳ�����
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		tdscBidderPersonApp = tdscBidderAppService.queryOneBiddPer(tdscBidderForm.getBidderPersonId());
		// ����֮���޸ľ�������Ϣ����Ҫ������֤�������ʱ�䡢�������
		// tdscBidderForm �� ���ֶ�ֵΪ null ʱ������bind ��ʵ��
		// tdscBidderForm.setBzjDzqk(null);
		// tdscBidderForm.setBzjDzse(null);
		// tdscBidderForm.setBzjDzsj(null);
		bindObject(tdscBidderPersonApp, tdscBidderForm);
		// ���� �ύ���ϵ� List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// �����Ӧ��ʵ������
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		// �����ֶ���ӵľ����������
		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		request.setAttribute("tdscBidderPersonApp", tdscBidderAppService.modifyBidPerXX(tdscBidderPersonApp, tdscBidMatList));
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		request.setAttribute("opeType", opeType);
		return mapping.findForward("bginfosqrjsp");
	}

	/**
	 * �޸����Ͼ�����֮һ ���� Ajax
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUnionBidderAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();
		String opeType = (String) request.getParameter("opeType");
		// ��ȡҳ�����
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();

		// ת��
		// ����֮���޸ľ�������Ϣ����Ҫ������֤�������ʱ�䡢�������
		// tdscBidderForm �� ���ֶ�ֵΪ null ʱ������bind ��ʵ��
		tdscBidderForm.setBzjDzqk(null);
		tdscBidderForm.setBzjDzse(null);
		tdscBidderForm.setBzjDzsj(null);
		bindObject(tdscBidderPersonApp, tdscBidderForm);
		tdscBidderPersonApp.setBidderId(tdscBidderForm.getBidderId());
		// TdscBidderPersonApp delBidPerApp
		// =(TdscBidderPersonApp)tdscBidderAppService.queryOneBiddPer(tdscBidderForm.getBidderPersonId());
		// ���� �ύ���ϵ� List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		if (materialTypes != null) {
			for (int i = 0; i < materialTypes.length; i++) {
				if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
					// �����Ӧ��ʵ������
					TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
					tdscBidderMaterial.setMaterialBh(materialBhs[j]);
					tdscBidderMaterial.setMaterialType(materialTypes[i]);
					tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
					tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
					tdscBidMatList.add(tdscBidderMaterial);
					j++;
				}
			}
		}

		// �����ֶ���ӵľ����������
		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		tdscBidderPersonApp = tdscBidderAppService.modifyBidPerXX(tdscBidderPersonApp, tdscBidMatList);
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		request.setAttribute("opeType", opeType);

		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// ���ظ��ص������Ĳ���
		String rowId = tdscBidderPersonApp.getBidderPersonId();
		pw.write(rowId);
		pw.close();

		return null;
	}

	/**
	 * ɾ�����Ͼ����е�һ���� Ajax
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteOneBidAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderPersonId = request.getParameter("bidderPersonId");
		TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryOneBiddPer(bidderPersonId);
		String isHead = (String) tdscBidderPersonApp.getIsHead();
		List perMatList = (List) tdscBidderAppService.queryMatList(bidderPersonId);
		if (tdscBidderPersonApp != null) {
			tdscBidderAppService.removeOneBidPer(tdscBidderPersonApp, perMatList);
		}

		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// ���ظ��ص������Ĳ���
		String retString = bidderPersonId + "," + isHead;
		pw.write(retString);
		pw.close();

		return null;

	}

	// ��ӡ��֤�����֪ͨ��
	public ActionForward printbzj(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeId = request.getParameter("noticeId");
		String yktBh = (String) request.getParameter("yktBh");

		List retList = new ArrayList();
		List appIdList = new ArrayList();
		if (noticeId != null && !"".equals(noticeId) && yktBh != null && !"".equals(yktBh)) {
			// ���ݹ����ѯ���еؿ�appIdList����ѯ�ù����е������øý��׿���ŵ��û�
			TdscBidderCondition bidderCondition = new TdscBidderCondition();
			bidderCondition.setNoticeId(noticeId);
			bidderCondition.setYktBh(yktBh);
			List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
			if (bidderAppList != null && bidderAppList.size() > 0) {
				for (int i = 0; i < bidderAppList.size(); i++) {
					TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderAppList.get(i);
					if (tdscBidderApp != null) {
						String peasonName = tdscBidderAppService.getPersonNameByBidderId(tdscBidderApp.getBidderId());

						TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
						condition.setAppId(tdscBidderApp.getAppId());
						TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
						// ����block_id��ѯ�ؿ���
						String blockCode = tdscBidderAppService.getTdscBlockPartListInBlockCode(tdscBlockAppView.getBlockId());
						tdscBlockAppView.setUnitebBlockCode(StringUtils.trimToEmpty(blockCode));
						tdscBlockAppView.setAcceptPerson(StringUtils.trimToEmpty(peasonName));
						retList.add(tdscBlockAppView);
					}
				}
			}

			request.setAttribute("tdscBlockAppViewList", retList);
		}

		return mapping.findForward("printbzj");
	}

	/**
	 * ��������--���������� TDSC_BIDDER_APP�� �� TDSC_BIDDER_PERSON_APP ��
	 * Tdsc_Bidder_Material��
	 */
	public ActionForward addBidderApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// ��ȡҳ�����
		String noticeId = tdscBidderForm.getNoticeId();
		String yktBh = tdscBidderForm.getYktBh();
		String type = request.getParameter("type"); // jmrgl����ת������ҳ�棬����ת������ҳ��
		String ifprint = request.getParameter("ifprint");// �ж��Ƿ��ӡ����11��Ϊ��ӡ���ݴ�+��ӡ��
		String ifCommit = request.getParameter("ifCommit");
		String provideBm = (String) tdscBidderForm.getProvideBm();

		String bidderId = request.getParameter("bidderId");
		String bidderPersonId = StringUtils.trimToEmpty(request.getParameter("bidderPersonId"));

		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		TdscBidderPersonApp tdscBidderPersonApp = new TdscBidderPersonApp();

		if (StringUtils.isNotEmpty(bidderId)) {
			tdscBidderApp = this.tdscBidderAppService.getTdscBidderAppByBidderId(bidderId);
		}
		if (StringUtils.isNotEmpty(bidderPersonId)) {
			tdscBidderPersonApp = this.tdscBidderAppService.queryOneBiddPer(bidderPersonId);
		}

		bindObject(tdscBidderApp, form);
		bindObject(tdscBidderPersonApp, form);

		String appIds[] = tdscBidderForm.getAppIds();

		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();

		BigDecimal[] bzjDzse = tdscBidderForm.getBidderBzjDzse();
		Date bzjDzsj = new Date();
		// tdscBidderPersonApp.setBzjDzse(bzjDzse);
		tdscBidderPersonApp.setBzjDzsj(bzjDzsj);
		tdscBidderPersonApp.setBzjDzqk("1003");

		// ɾ���þ������Ѿ��ύ�ı��������б�
		if (StringUtils.isNotEmpty(bidderId)) {
			this.tdscBidderAppService.delOneBidderMaterialByBidderId(bidderId);
		}

		// ���� �ύ���ϵ� List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// ���ʵ������
				TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
				tdscBidderMaterial.setMaterialBh(materialBhs[j]);
				tdscBidderMaterial.setMaterialType(materialTypes[i]);
				tdscBidderMaterial.setMaterialCount(Integer.valueOf(materialCounts[i]));
				// tdscBidderMaterial.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());

				tdscBidMatList.add(tdscBidderMaterial);
				j++;
			}
		}

		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				// tdscBidMatal.setBidderPersonId(tdscBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		// ���������Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// Integer regionID = new Integer(user.getRegionID());
		// ���þ�������ġ������û���
		tdscBidderApp.setAppUserId(String.valueOf(user.getUserId()));
		if ("1".equals(ifCommit)) {
			tdscBidderApp.setIfCommit("1");
		} else {
			tdscBidderApp.setIfCommit("0");
		}

		// //���澺���˼�������б����ж��ڸù��������޸ý��׿��ŵľ����ˣ����У�ȫɾ
		// //List appIdList = new ArrayList();
		// if(noticeId!=null&&!"".equals(noticeId)&&yktBh!=null&&!"".equals(yktBh)){
		// //���ݹ����ѯ���еؿ�appIdList����ѯ�ù����е������øý��׿���ŵ��û�
		// TdscBidderCondition bidderCondition = new TdscBidderCondition();
		// bidderCondition.setNoticeId(noticeId);
		// bidderCondition.setYktBh(yktBh);
		// List bidderAppList =
		// tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		//
		// if(bidderAppList!=null&&bidderAppList.size()>0){
		// for(int nn=0;nn<bidderAppList.size();nn++){
		// TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(nn);
		// if(bidderApp!=null)
		// tdscBidderAppService.delOneBidderByBidderId(bidderApp.getBidderId());
		// }
		// }
		// }
		// //ɾ������

		// �����¾���������
		// String bidderId = "";
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		if (appIds != null && appIds.length > 0) {
			for (int m = 0; m < appIds.length; m++) {
				if (!"".equals(appIds[m])) {
					TdscBidderApp tempBidderApp = new TdscBidderApp();
					TdscBidderPersonApp tempBidderPersonApp = new TdscBidderPersonApp();
					BeanUtils.copyProperties(tempBidderApp, tdscBidderApp);
					BeanUtils.copyProperties(tempBidderPersonApp, tdscBidderPersonApp);
					tempBidderPersonApp.setBzjDzse(bzjDzse[m]);
					tempBidderApp.setAppId(appIds[m]);
					// ���ò�ѯ�ؿ���ͼ������
					condition.setAppId(appIds[m]);
					// ��������
					tempBidderApp.setBzjztDzqk("1003"); // ���þ�������ĵ������
					// �����Ѿ������ʸ�֤��
					tempBidderApp.setIfDownloadZgzs("1");

					// ��ѯ��ͼ
					tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
					if (tdscBlockAppView != null) {
						if (StringUtils.isNotEmpty(tdscBlockAppView.getUserId())) {
							// ���þ�����ID
							tempBidderApp.setUserId(tdscBlockAppView.getUserId());
						}
					}
					bidderId = tdscBidderAppService.addBidder(provideBm, tempBidderApp, tempBidderPersonApp, tdscBidMatList);
				}
			}
		}

		// if("11".equals(type)){
		// if(tdscBidderForm.getBidderId()!=null&&tdscBidderForm.getBidderId()!=""){
		// tdscBidderAppService.delOneBidderByBidderId(tdscBidderForm.getBidderId());
		// }
		TdscBidderForm tempForm = new TdscBidderForm();
		tempForm.setNoticeId(noticeId);
		tempForm.setBidderId(bidderId);
		tempForm.setYktBh(yktBh);
		tempForm.setType(type);

		if ("11".equals(ifprint) || "22".equals(ifprint) || "33".equals(ifprint)) {
			tempForm.setIfprint(ifprint);
			return queryOneBidder(mapping, tempForm, request, response);
		}

		// }
		// TdscBidderForm retForm = new TdscBidderForm();
		// retForm.setAppId(tdscBidderForm.getAppId());
		// return queryBidder(mapping, retForm, request, response);

		String forwardString = "bidderApp.do?method=query";
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * ����bidderId��ѯ ת���������޸�ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryOneBidder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String noticeId = tdscBidderForm.getNoticeId();
		String type = tdscBidderForm.getType();
		String ifprint = tdscBidderForm.getIfprint();
		String bidderId = request.getParameter("bidderId") + "";
		if (StringUtil.isEmpty(bidderId)) {
			bidderId = tdscBidderForm.getBidderId();
		}

		// ���淽ʽΪ�ݴ�
		if ("11".equals(type)) {
			request.setAttribute("type", type);
		}
		// �ж��Ƿ�Ҫ���ӡ
		if ("11".equals(ifprint) || "22".equals(ifprint) || "33".equals(ifprint)) {
			request.setAttribute("ifprint", ifprint);
		}
		// ��ѯ������Ϣ

		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		String appId = (String) tdscBidderApp.getAppId();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		// ��ѯ���������еĵؿ��б�
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		String transferMode = "";
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
			transferMode = tdscBlockAppView.getTransferMode();
		}

		// ��ѯ�þ����˲��뾺��ĵؿ��б�
		// ���ݹ����ѯ���еؿ�appIdList����ѯ�ù����е������øý��׿���ŵ��û�
		TdscBidderCondition bidderCondition = new TdscBidderCondition();
		bidderCondition.setNoticeId(noticeId);
		if (StringUtils.isNotEmpty(tdscBidderApp.getYktBh())) {
			bidderCondition.setYktBh(tdscBidderApp.getYktBh());
		} else {
			bidderCondition.setNotYktBh("1");
		}
		List bidderAppList = tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		if (bidderAppList != null && bidderAppList.size() > 0) {
			for (int i = 0; i < bidderAppList.size(); i++) {
				TdscBidderApp tempApp = (TdscBidderApp) bidderAppList.get(i);
				TdscBidderPersonApp temPersonApp = this.tdscBidderAppService.getTdscBidderPersonByBidderId(tempApp.getBidderId());
				tempApp.setAloneTdscBidderPersonApp(temPersonApp);
				request.setAttribute("tdscBidderPersonApp", temPersonApp);
			}
		}
		request.setAttribute("appId", appId);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("bidderAppList", bidderAppList);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("bidderId", bidderId);
		// ����ǵ�������
		if ("1".equals(tdscBidderApp.getBidderType())) { // ��ѯ ��������Ϣ
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(tdscBidderForm.getBidderId());
			// ��ѯ�����˲�����Ϣ
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				List otherMatList = (List) tdscBidderAppService.queryOtherMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				request.setAttribute("perMatList", retMatList);
				request.setAttribute("otherMatList", otherMatList);
			}
			return mapping.findForward("danDuJingMai");
		} else {
			// ��������Ͼ���
			// ��ѯ�������б�
			List retBidPerList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderForm.getBidderId());
			// 20080313add �ж��Ƿ���ǣͷ��
			if (retBidPerList != null && retBidPerList.size() > 0) {
				String hasHead = "no";
				// ��ÿһ�������˽����ж�
				for (int i = 0; i < retBidPerList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) retBidPerList.get(i);
					if ("1".equals(tdscBidderPersonApp.getIsHead())) {
						hasHead = "yes";
						break;
					}
				}
				request.setAttribute("hasHead", hasHead);
			}

			request.setAttribute("retBidPerList", retBidPerList);
			return mapping.findForward("lianHejingMai");
		}
	}

	/**
	 * ����bidderId��ѯ ת�������Ѵ��ھ�����ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAddExistBidPer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String type = tdscBidderForm.getType();
		String ifprint = tdscBidderForm.getIfprint();

		String appId = request.getParameter("appId");
		// ���淽ʽΪ�ݴ�
		if ("11".equals(type)) {
			request.setAttribute("type", type);
		}
		// �ж��Ƿ�Ҫ���ӡ
		if ("11".equals(ifprint)) {
			request.setAttribute("ifprint", ifprint);
		}
		// ��ѯ������Ϣ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(tdscBidderForm.getBidderId());

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ��ѯҪ���Ӿ����˵ĵؿ���Ϣ
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);

		request.setAttribute("appId", appId);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("bidderId", tdscBidderForm.getBidderId());
		// ����ǵ�������
		if ("1".equals(tdscBidderApp.getBidderType())) { // ��ѯ ��������Ϣ
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(tdscBidderForm.getBidderId());
			// ��ѯ�����˲�����Ϣ
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				List otherMatList = (List) tdscBidderAppService.queryOtherMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				request.setAttribute("perMatList", retMatList);
				request.setAttribute("otherMatList", otherMatList);
			}
			return mapping.findForward("existddjmr");
		} else {
			// ��������Ͼ���
			// ��ѯ�������б�
			List retBidPerList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderForm.getBidderId());
			// 20080313add �ж��Ƿ���ǣͷ��
			if (retBidPerList != null && retBidPerList.size() > 0) {
				String hasHead = "no";
				// ��ÿһ�������˽����ж�
				for (int i = 0; i < retBidPerList.size(); i++) {
					TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) retBidPerList.get(i);
					if ("1".equals(tdscBidderPersonApp.getIsHead())) {
						hasHead = "yes";
						break;
					}
				}
				request.setAttribute("hasHead", hasHead);
			}
			request.setAttribute("retBidPerList", retBidPerList);
			return mapping.findForward("existlhjmr");
		}

	}

	/**
	 * ��ӡ���������ļ��ռ��վ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = request.getParameter("bidderId");
		// ��ѯ������Ϣ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		List appIdList = new ArrayList();
		// if (tdscBidderApp != null) {
		// // ���ݹ����ѯ���еؿ�appIdList����ѯ�ù����е������øý��׿���ŵ��û�
		// TdscBidderCondition bidderCondition = new TdscBidderCondition();
		// bidderCondition.setNoticeId(tdscBidderApp.getNoticeId());
		// bidderCondition.setYktBh(tdscBidderApp.getYktBh());
		// List bidderAppList =
		// tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		// if (bidderAppList != null && bidderAppList.size() > 0) {
		// for (int m = 0; m < bidderAppList.size(); m++) {
		// TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);
		// appIdList.add(bidderApp.getAppId());
		// }
		// }
		// }
		String[] appIds = null;
		if (tdscBidderApp != null && StringUtils.isNotBlank(tdscBidderApp.getAppId())) {
			appIds = tdscBidderApp.getAppId().split(",");
		}
		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {
				appIdList.add(appIds[i]);
			}
		}

		String transferMode = "";
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppIdList(appIdList);
		// ��ѯ�ؿ� ��Ϣ
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
			transferMode = tdscBlockAppView.getTransferMode();
		}

		// ��ӡʱ��
		Date retDate = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormator = new SimpleDateFormat("yyyy��MM��dd��HHʱmm��");
		String d = dateFormator.format(retDate);

		request.setAttribute("retDate", d);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		// �жϾ������� ������������
		// 1.����
		if ("1".equals(tdscBidderApp.getBidderType())) {
			// ��ѯ��������Ϣ
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(bidderId);
			// ��ѯ�����˲�����Ϣ
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				request.setAttribute("perMatList", retMatList);

				return mapping.findForward("printGyReceipt");

			} else {
				return mapping.findForward("nullListjsp");
			}
		}
		// 2.����
		if ("2".equals(tdscBidderApp.getBidderType())) {
			// ��þ����˵�List
			List bidderList = (List) tdscBidderAppService.queryBidderPersonList(bidderId);
			// ���췵��List
			if (bidderList != null && bidderList.size() > 0) {
				List returnList = new ArrayList();
				String retString = "";
				int matListSize = 0;
				if (bidderList != null && bidderList.size() > 0) {
					for (int j = 0; j < bidderList.size(); j++) {
						List retList = new ArrayList();
						TdscBidderPersonApp bidPersonApp = (TdscBidderPersonApp) bidderList.get(j);
						if (j < bidderList.size() - 1) {
							retString += bidPersonApp.getBidderName() + "��";
						} else {
							retString += bidPersonApp.getBidderName();
						}

						// ��ѯ�������ύ�Ĳ��ϵ���Ϣ
						List matList = (List) tdscBidderAppService.queryOnePerMat(bidPersonApp.getBidderPersonId());
						if (matList != null && matList.size() > 0) {
							for (int k = 0; k < matList.size(); k++) {
								TdscBidderMaterial tdscBidderMaterial = (TdscBidderMaterial) matList.get(k);
								matListSize += tdscBidderMaterial.getMaterialCount().intValue();
							}

						}

						retList.add(bidPersonApp);
						retList.add(matList);

						// ��ӵ����ص�List��
						returnList.add(retList);
					}
				}
				request.setAttribute("returnList", returnList);
				request.setAttribute("retString", retString);
				request.setAttribute("matListSize", matListSize + "");

				return mapping.findForward("printGyLhjmReceipt");

			} else {
				return mapping.findForward("nullListjsp");
			}

		}

		return null;
	}

	/**
	 * ��ӡ�����ʸ�ȷ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printJmzgqrs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bidderId = request.getParameter("bidderId");
		// ��ѯ������Ϣ
		TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		List appIdList = new ArrayList();

		// if (tdscBidderApp != null) {
		// // ���ݹ����ѯ���еؿ�appIdList����ѯ�ù����е������øý��׿���ŵ��û�
		// TdscBidderCondition bidderCondition = new TdscBidderCondition();
		// bidderCondition.setNoticeId(tdscBidderApp.getNoticeId());
		// bidderCondition.setYktBh(tdscBidderApp.getYktBh());
		// List bidderAppList =
		// tdscBidderAppService.queryBidderAppListByCondition(bidderCondition);
		// if (bidderAppList != null && bidderAppList.size() > 0) {
		// for (int m = 0; m < bidderAppList.size(); m++) {
		// TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);
		// appIdList.add(bidderApp.getAppId());
		// }
		// }
		// }
		String[] appIds = null;
		if (tdscBidderApp != null && StringUtils.isNotBlank(tdscBidderApp.getAppId())) {
			appIds = tdscBidderApp.getAppId().split(",");
		}
		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {
				appIdList.add(appIds[i]);
			}
		}
		String transferMode = "";
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppIdList(appIdList);
		String blockName = "";// �ؿ�����
		String planId = "";
		TdscBlockPlanTable blockPlan = null;

		// ��ѯ�ؿ� ��Ϣ
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {

			tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
			planId = tdscBlockAppView.getPlanId();
			transferMode = tdscBlockAppView.getTransferMode();

		}

		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {

			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				blockName += tdscBlockAppView.getBlockName() + ",";
			}
			if (blockName != null) {
				request.setAttribute("blockName", blockName);
			}
		}
		if (!StringUtils.isEmpty(planId)) {
			blockPlan = tdscLocalTradeService.getTdscBlockPlanTable(planId);
			request.setAttribute("tdscBlockPlanTable", blockPlan);
		}

		// �õ������˾���ĵؿ�����

		// ��ӡʱ��
		Date retDate = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormator = new SimpleDateFormat("yyyy��M��d��");
		String d = dateFormator.format(retDate);

		request.setAttribute("printDate", d);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		// �жϾ������� ������������
		// 1.����
		if ("1".equals(tdscBidderApp.getBidderType())) {
			// ��ѯ��������Ϣ
			TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(bidderId);
			// ��ѯ�����˲�����Ϣ
			if (tdscBidderPersonApp != null) {
				List retMatList = (List) tdscBidderAppService.queryMatList(tdscBidderPersonApp.getBidderPersonId());
				request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
				// request.setAttribute("perMatList", retMatList);

				return mapping.findForward("printJmzgqrs");

			} else {
				return mapping.findForward("nullListjsp");
			}
		}
		return null;
	}

	/**
	 * �޸� ���������˵���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateDdjmBidApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ԭ�е���Ϣ
		String bidderId = (String) request.getParameter("bidderId");
		String type = request.getParameter("type"); // �������ͣ���11��Ϊ�ݴ�
		String ifprint = request.getParameter("ifprint");// �ж��Ƿ��ӡ����11��Ϊ��ӡ
		String ifCommit = request.getParameter("ifCommit");// �ж��Ƿ��ύ����1��Ϊ�ύ
		List oneBidApp = (List) tdscBidderAppService.queryByBidderId(bidderId);
		TdscBidderApp tempBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		TdscBidderPersonApp tempBidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.queryBidPerson(bidderId);
		TdscBidderMaterial tdscBidderMaterial = new TdscBidderMaterial();
		// ����µ���Ϣ
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// ��ȡҳ�����
		// ����֮���޸ľ�������Ϣ����Ҫ������֤�������ʱ�䡢�������
		// tdscBidderForm �� ���ֶ�ֵΪ null ʱ������bind ��ʵ��
		tdscBidderForm.setBzjDzqk(null);
		tdscBidderForm.setBzjDzse(null);
		tdscBidderForm.setBzjDzsj(null);
		tdscBidderForm.setCertNo(null);
		tdscBidderForm.setReviewResult(null);
		tdscBidderForm.setReviewOpnn(null);
		bindObject(tempBidderApp, tdscBidderForm);
		// �ж��ǡ��ύ�����ǡ����桱
		if ("1".equals(ifCommit)) {
			tempBidderApp.setIfCommit("1");
		} else {
			tempBidderApp.setIfCommit("0");
		}
		bindObject(tempBidderPersonApp, tdscBidderForm);
		bindObject(tdscBidderMaterial, tdscBidderForm);
		// ��ԭ��������ID��ֵ���µ�ʵ��
		String materialBhs[] = tdscBidderForm.getMaterialBhs();
		String materialTypes[] = tdscBidderForm.getMaterialTypes();
		String materialCounts[] = tdscBidderForm.getMaterialCounts();

		String otherMaterialBhs[] = tdscBidderForm.getOtherMaterialBhs();
		String otherMaterialTypes[] = tdscBidderForm.getOtherMaterialTypes();
		String otherMaterialCounts[] = tdscBidderForm.getOtherMaterialCounts();
		String memo[] = tdscBidderForm.getMaterialNames();
		// ���� �ύ���ϵ� List
		List tdscBidMatList = new ArrayList();
		int j = 0;
		for (int i = 0; i < materialTypes.length; i++) {
			if (!"".equals(materialTypes[i]) && j < materialBhs.length) {
				// ���ʵ������
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialBh(materialBhs[j]);
				tdscBidMatal.setMaterialType(materialTypes[i]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(materialCounts[i]));
				tdscBidMatal.setBidderPersonId(tempBidderPersonApp.getBidderPersonId());
				tdscBidMatList.add(tdscBidMatal);
				j++;
			}
		}

		if (null != otherMaterialBhs && null != otherMaterialTypes && null != otherMaterialCounts && null != memo) {
			for (int k = 0; k < memo.length; k++) {
				TdscBidderMaterial tdscBidMatal = new TdscBidderMaterial();
				tdscBidMatal.setMaterialCode(otherMaterialBhs[k]);
				tdscBidMatal.setMaterialType(otherMaterialTypes[k]);
				tdscBidMatal.setMaterialCount(Integer.valueOf(otherMaterialCounts[k]));
				tdscBidMatal.setBidderPersonId(tempBidderPersonApp.getBidderPersonId());
				tdscBidMatal.setMemo(memo[k]);
				tdscBidMatList.add(tdscBidMatal);
			}
		}

		tdscBidderAppService.updateBidder(oneBidApp, tempBidderApp, tempBidderPersonApp, tdscBidMatList);

		TdscBidderForm retForm = new TdscBidderForm();
		retForm.setAppId(tdscBidderForm.getAppId());
		if ("11".equals(type)) {
			retForm.setBidderId(bidderId);
			retForm.setType(type);
			if ("11".equals(ifprint)) {
				retForm.setIfprint(ifprint);
			}
			return queryOneBidder(mapping, retForm, request, response);

		}
		return queryBidder(mapping, retForm, request, response);
	}

	/**
	 * �������� ������������ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward changeCard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ������뻻���˵� bidderId
		String bidderId = (String) request.getParameter("bidderId");
		request.setAttribute("bidderId", bidderId);
		// ��תҳ��
		return mapping.findForward("changeCard");
	}

	/**
	 * �������� У������������Ƿ���ԭ������ͬ ����ͬ,���������¿���ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkYktMm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡ���뻻���˵�bidderId
		String bidderId = (String) request.getParameter("bidderId");
		// ��ѯ�û����˵���Ϣ
		TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// �ж����������Ƿ���ԭ������ͬ
		if (tdscBidderForm.getYktMm().equals(bidderApp.getYktMm())) {
			return mapping.findForward("changeCard1");
		}
		return mapping.findForward("yktMmError");
	}

	/**
	 * �������� У������������Ƿ���ԭ������ͬ ����ͬ,���������¿���ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkYktMmAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡ���뻻���˵�bidderId
		String bidderId = (String) request.getParameter("bidderId");
		String yktMm = (String) request.getParameter("yktMm");
		// ��ѯ�û����˵���Ϣ
		TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);
		// �ж����������Ƿ���ԭ������ͬ
		String rtnStatus = null;
		if (yktMm.equals(bidderApp.getYktMm())) {
			rtnStatus = "2";
		} else {
			rtnStatus = "1";
		}
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// ���ظ��ص������Ĳ���
		pw.write(rtnStatus);
		pw.close();

		return null;
	}

	/**
	 * �����������޸Ľ��׿����š����׿����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateYktBh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ������뻻����bidderId
		String bidderId = (String) request.getParameter("bidderId");
		String newYktBh = (String) request.getParameter("yktBh");
		String newYktXh = (String) request.getParameter("yktXh");
		String password = (String) request.getParameter("password");
		// ���ԭʵ������
		TdscBidderApp oldBidderApp = (TdscBidderApp) tdscBidderAppService.queryBidderAppInfo(bidderId);

		String rtnStatus = null;
		// �����µĿ���������
		if (newYktBh != null && password != null) {
			oldBidderApp.setYktMm(password);
			tdscBidderAppService.modBidYktMm(oldBidderApp, newYktBh, newYktXh);
			rtnStatus = "1";
		} else {
			rtnStatus = "2";
		}
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// ���ظ��ص������Ĳ���
		pw.write(rtnStatus);
		pw.close();

		return null;
	}

	/**
	 * У�� ���ϱ�� �Ƿ��Ѿ������ �����������ˡ� ���ж� ����ʽ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkClBh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String provideBm = (String) request.getParameter("bianhao");

		String rtnStatus = null;
		// ����provideBm ��ѯ���������
		List bmList = (List) tdscBidderAppService.getProByProvideBm(provideBm);
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pwBidtype = response.getWriter();
		// �ж� �Ƿ����������
		if (bmList == null || bmList.size() == 0) {
			rtnStatus = "01";
		}
		// �жϸò����Ƿ��Ѿ���������
		if (bmList != null && bmList.size() > 0) {
			TdscBidderProvide tdscBidderProvide = (TdscBidderProvide) bmList.get(0);

			String ifapp = (String) tdscBidderProvide.getIfApp(); // 1Ϊ���������
			// 0Ϊ��δ�������
			// ���Խ�������
			if (ifapp == null || "0".equals(ifapp)) {
				String appId = (String) tdscBidderProvide.getAppId();
				TdscAppNodeStat nodeStat = this.commonFlowService.findAppNodeStat(appId, FlowConstants.FLOW_NODE_BIDDER_APP);
				if (nodeStat != null && FlowConstants.STAT_ACTIVE.equals(nodeStat.getNodeStat())) {
					// ��������
					rtnStatus = "02";
					String bidtype = (String) tdscBidderProvide.getBidType();
					if (bidtype != null) {
						// ���صĲ��ϱ��
						PrintWriter pwProvideBm = response.getWriter();
						pwProvideBm.write(provideBm);
						// ���� �ؿ��appId
						if (appId != null) {
							PrintWriter pwAppId = response.getWriter();
							pwAppId.write(appId);
						}
						// ������������ ������������
						pwBidtype.write(bidtype);
					}
				} else if (nodeStat != null && (FlowConstants.STAT_END.equals(nodeStat.getNodeStat()) || FlowConstants.STAT_TERMINATE.equals(nodeStat.getNodeStat()))) {
					// ��ʾ�������
					rtnStatus = "88";
				} else {
					// ��ʾδ��ʼ����
					rtnStatus = "99";
				}

			}
			// �����Խ�������
			if ("1".equals(ifapp)) {
				rtnStatus = "03";
			}
		}
		// ���ظ��ص������Ĳ���
		PrintWriter pwRtnStatus = response.getWriter();
		pwRtnStatus.write(rtnStatus);
		pwRtnStatus.close();
		return null;
	}

	/**
	 * У��һ��ͨ��Ż��߿����Ƿ��Ѿ�ʹ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkJykbh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String yktXh = (String) request.getParameter("bianhao");
		String checkType = (String) request.getParameter("checkType");
		String actionType = (String) request.getParameter("actionType");
		String rtnStatus = "00";
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pwBidtype = response.getWriter();
		List bmList = new ArrayList();
		if ("1".equals(checkType)) { // У����
			bmList = (List) tdscBidderAppService.checkIfUsedYktXh(yktXh);
		} else { // У�鿨��
			bmList = (List) tdscBidderAppService.checkIfUsedYktBh(yktXh);
		}

		// �޸�ҳ��� У��һ��ͨ��Ż��߿����Ƿ��Ѿ�ʹ�õķ��� ���ж��Ƿ�ʹ��;�Ƿ��Լ�ʹ��
		if ("0000".equals(actionType)) {
			String bidderId = (String) request.getParameter("bidderId");
			// List bmList = (List) tdscBidderAppService.checkIfUsed(yktXh);
			if (bmList == null || bmList.size() == 0) { // û�б�ʹ�ù�
				rtnStatus = "0001";
			} else if (bmList.size() == 1) { // ��ʹ�ù�,��У���Ƿ����Լ�ʹ��
				TdscBidderApp appIdYktXhList = new TdscBidderApp();
				if ("1".equals(checkType)) {
					appIdYktXhList = (TdscBidderApp) tdscBidderAppService.getBidderAppByBidderIdYktXh(bidderId, yktXh);
				} else {
					appIdYktXhList = (TdscBidderApp) tdscBidderAppService.getBidderAppByBidderIdYktBh(bidderId, yktXh);
				}

				if (appIdYktXhList == null) { // ��ʹ�ù�,�������Լ�ʹ�õ�
					rtnStatus = "9999";
				} else {
					rtnStatus = "0001"; // ��ʹ�ù�,���Լ�ʹ�õ�
				}
			}
		}

		// ����ҳ�� У��һ��ͨ����Ƿ��Ѿ�ʹ�õķ��� ��ֻ�ж��Ƿ�ʹ��
		if ("1111".equals(actionType)) {
			if (bmList == null || bmList.size() == 0) { // û�б�ʹ�ù�
				rtnStatus = "0001";
			}
			if (bmList.size() == 1) { // ��ʹ�ù�
				rtnStatus = "9999";
			}
		}

		if (bmList.size() > 1) { // ���棺����ʹ������� һ��ͨ���
			rtnStatus = "8888";
		}
		rtnStatus = rtnStatus + "," + checkType;
		pwBidtype.write(rtnStatus);
		pwBidtype.close();
		return null;
	}

	/**
	 * У�� ���Ͼ���--�޸�ҳ�� �����桱ʱ �Ƿ�¼�������˵���Ϣ
	 * 
	 * @param bidderId
	 * @param tdscBidderForm
	 * @return
	 */
	public ActionForward checkBidderPersonNum(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rtnStatus = "0000";
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		// ��ȡҳ�����
		String bidderId = (String) request.getParameter("bidderId");
		// �޸�ʱ ����bidderId��ѯԭ�е� ��������Ϣ�Ƿ��Ѿ�����ɾ��
		List BidderPersonList = (List) tdscBidderAppService.queryBidderPersonList(bidderId);

		if (BidderPersonList != null && BidderPersonList.size() > 0) {
			rtnStatus = "1111";
		}
		pw.write(rtnStatus);
		pw.close();
		return null;
	}

	/**
	 * ��ѯ�ʸ�������б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryReviewResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		this.bindObject(condition, form);
		// ��ȡ�û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		PageList appViewList = (PageList) tdscBidderAppService.queryAppViewList(condition);
		if (appViewList != null) {
			List list = (List) appViewList.getList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < appViewList.getList().size(); i++) {
					TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) list.get(i);
					if (tdscBlockAppView.getAppId() != null) {
						// ����TempStrΪͨ��appId��õ��Ѿ����ع��ʸ�֤��ľ����� �ĸ���
						tdscBlockAppView.setTempStr((tdscBidderAppService.queryBidderListSrc(tdscBlockAppView.getAppId()).size() - tdscBidderAppService
								.queryBidderListDownloadZgzs(tdscBlockAppView.getAppId()).size())
								+ "");
						// ����TempStr2ͨ��appId �������ͨ������ľ����˵ĸ���
						tdscBlockAppView.setTempStr2(tdscBidderAppService.queryBidderListSrc(tdscBlockAppView.getAppId()).size() + "");
					}
				}
			}
		}
		request.setAttribute("condition", condition);
		request.setAttribute("pageList", appViewList);
		return mapping.findForward("reviewResultList");
	}

	/**
	 * �ʸ������--��ѯĳһ�ؿ�ľ�������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryReviewResultInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = (String) request.getParameter("appId");
		// ����appId��ѯ����������ϢList
		List bidderList = (List) tdscBidderAppService.queryBidderAppList(appId);
		if (bidderList != null && bidderList.size() > 0) {
			for (int i = 0; i < bidderList.size(); i++) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) bidderList.get(i);
				// ��ѯÿ�������о����˵���ϢLIST
				if (tdscBidderApp != null) {
					StringBuffer bidderNames = new StringBuffer();
					List personList = (List) tdscBidderAppService.queryBidderPersonList(tdscBidderApp.getBidderId());
					if (personList != null && personList.size() > 0) {
						for (int j = 0; j < personList.size(); j++) {
							TdscBidderPersonApp tdscBidderPersonApp = (TdscBidderPersonApp) personList.get(j);
							bidderNames.append(tdscBidderPersonApp.getBidderName()).append("��");
						}
					}
					if (bidderNames != null) {
						tdscBidderApp.setMemo(bidderNames.toString().substring(0, bidderNames.toString().length() - 1));
					}
				}
			}
		}
		request.setAttribute("bidderList", bidderList);
		return mapping.findForward("reviewResultInfo");
	}

	// public ActionForward queryScOrgList(ActionMapping mapping, ActionForm
	// form, HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	// PageList pageList = new PageList();
	// String pageNo = request.getParameter("currentPage");
	// int pageSize = ((Integer)
	// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
	// int currentPage = 0;
	// if (pageNo != null && pageNo.trim().equals("") == false) {
	// currentPage = Integer.parseInt(pageNo);
	//
	// }
	// if (pageNo==null || Integer.parseInt(pageNo) < 1) {
	// currentPage = 1;
	// }
	// TdscBidderCondition condition = new TdscBidderCondition();
	// bindObject(condition, form);
	// String organizName = condition.getOrganizName();
	//
	// //String organizName = request.getParameter("organizName");
	// if(organizName==null){
	// organizName = "";
	// }
	//
	// PublicManager manager = PublicFactory.getPublicManager();
	// //manager.getOrganizationListByName(organizName, 1);
	// List organizList = manager.getOrganizationListByName(organizName,1);
	// if(organizList!=null&&organizList.size()!=0){
	// pageList.setList(organizList);
	// }
	// pageList = PageUtil.getPageList(organizList, pageSize, currentPage);
	// request.setAttribute("pageList", pageList);
	// request.setAttribute("condition", condition);
	// return mapping.findForward("bidderQueryScOrgList");
	// }

	// XXX add by xys start
	public ActionForward queryFlapperTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// ��ѯ�б�
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);
		// else
		// planCondition.setTransferMode("3104");

		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);

		return mapping.findForward("flapperResultInfo");
	}

	public ActionForward printFlapperTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List rptList = new ArrayList();
		List retList = new ArrayList();
		Map map = new HashMap();
		String planId = request.getParameter("planId") + "";

		TdscBaseQueryCondition tdscBaseQuerycondition = new TdscBaseQueryCondition();
		tdscBaseQuerycondition.setPlanId(planId);
		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(tdscBaseQuerycondition);

		String ss = "";

		if (appViewList != null && appViewList.size() > 0)
			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) appViewList.get(i);
				if (!"04".equals(appView.getTranResult())) {// ��04��Ϊ��ֹ����,��ֹ���صĵؿ鲻������
					if (!StringUtil.isEmpty(appView.getNoitceNo()))
						ss = appView.getNoitceNo();
					ss = ss.substring(0, 10) + Integer.parseInt(ss.substring(10, 12)) + "��";

					List bidderAppList = tdscBidderAppService.queryBidderAppListLikeAppId(appView.getAppId());

					if (bidderAppList != null && bidderAppList.size() > 0) {
						for (int m = 0; m < bidderAppList.size(); m++) {
							TdscBidderApp bidderApp = (TdscBidderApp) bidderAppList.get(m);

							TdscBidderPersonApp bidderPersonApp = (TdscBidderPersonApp) tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());

							// // ���δ�����򲻽����б�
							// if (!StringUtil.isEmpty(bidderApp.getYktBh()) &&
							// !"1".equals(bidderApp.getIsPurposePerson())) {
							// String thisBidderMaxPrice =
							// tdscLocalTradeService.getBidderMaxPrice(bidderApp.getYktBh(),
							// appView.getAppId());
							// if (thisBidderMaxPrice != null &&
							// !StringUtil.isEmpty(thisBidderMaxPrice) &&
							// !"0".equals(thisBidderMaxPrice)) {
							// TdscBidderForm bean = new TdscBidderForm();
							//
							// // bean.setNoticeId(noticeId);
							// bean.setAppId(bidderApp.getAppId());
							// bean.setBidderId(bidderPersonApp.getBidderId());
							// bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
							// bean.setBidderName(bidderPersonApp.getBidderName());
							// bean.setAcceptNo(bidderApp.getAcceptNo());
							// bean.setCertNo(bidderApp.getCertNo());
							// bean.setConNum(bidderApp.getConNum());
							// bean.setIsPurposePerson(bidderApp.getIsPurposePerson());
							// bean.setBidderLxdh(bidderPersonApp.getBidderLxdh());
							//
							// map.put(bidderPersonApp.getBidderName(), bean);
							//
							// }
							// }

							if (!StringUtil.isEmpty(bidderApp.getYktBh())) {
								TdscBidderForm bean = new TdscBidderForm();

								// bean.setNoticeId(noticeId);
								bean.setAppId(bidderApp.getAppId());
								bean.setBidderId(bidderPersonApp.getBidderId());
								bean.setBidderPersonId(bidderPersonApp.getBidderPersonId());
								bean.setBidderName(bidderPersonApp.getBidderName());
								bean.setAcceptNo(bidderApp.getAcceptNo());
								bean.setCertNo(bidderApp.getCertNo());
								bean.setConNum(bidderApp.getConNum());
								bean.setIsPurposePerson(bidderApp.getIsPurposePerson());
								bean.setBidderLxdh(bidderPersonApp.getBidderLxdh());

								map.put(bidderPersonApp.getBidderName(), bean);
							}

						}
					}
				}
			}

		if (map.size() != 0) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry entry = (Entry) it.next();
				// Object key = entry.getKey();
				Object value = entry.getValue();
				retList.add((TdscBidderForm) value);
			}

		}

		// TODO ���պ�������

		if (retList != null && retList.size() > 0) {
			for (int i = 0; i < retList.size(); i++) {

				TdscBidderForm bean = (TdscBidderForm) retList.get(i);

				if (!StringUtil.isEmpty(bean.getCertNo()))
					bean.setBidderName(bean.getCertNo()); // �ʸ�ȷ������
				else
					bean.setBidderName("&nbsp;");

				String tmpConNum = "δѡȡ����";
				if (!StringUtil.isEmpty(bean.getConNum()))
					tmpConNum = bean.getConNum();
				bean.setConNum(tmpConNum);// ���� δѡ�����������֮
				// bean.setBidderLxdh(bean.getBidderLxdh());
				rptList.add(bean);
			}
		}

		request.setAttribute("rptTitle", ss + "������ƵǼǱ�");
		request.setAttribute("rptList", rptList);
		return mapping.findForward("printFlapperTbl");

	}

	private boolean isEmpty(String str) {
		if (str == null)
			return true;
		if ("null".equals(str))
			return true;
		if ("".equals(str))
			return true;

		if (str.length() == 0)
			return true;
		return false;
	}

	public ActionForward queryApplyTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String fromMenu = request.getParameter("fromMenu");
		request.setAttribute("fromMenu", fromMenu);

		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// ��ѯ�б�
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);

		if (!isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		// �ֳ����۵��յĹ��ƽ�ֹ��11��00��ǰһ��Сʱ �� ��10:00�ſ��Կ�������
		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		// ���������ϸ��
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		if ("2".equals(fromMenu)) {
			if (!"�ƫ�".equals(user.getDisplayName()) && !"tdsc".equals(user.getUserId()))
				if (queryList != null && queryList.size() > 0) {
					for (int i = 0; i < queryList.size();) {
						TdscBlockPlanTable blockPlan = (TdscBlockPlanTable) queryList.get(i);
						// �õ��ֳ���������ʱ��
						String listEndDate = DateUtil.timestamp2String(blockPlan.getListEndDate(), "yyyyMMddhhmmss");
						// 20110429110000
						long gpEndTime = Long.parseLong(listEndDate);
						long compTime = gpEndTime - 10000;

						// 20110503145532
						String currDate = DateConvertor.getCurrentDateWithTimeZone();
						long currTime = Long.parseLong(currDate);
						if (currTime < compTime) {
							queryList.remove(i);
						} else {
							i++;
						}

					}
				}
		}

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);
		return mapping.findForward("applyResultInfo");

	}

	public ActionForward printApplyTbl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List rptList = new ArrayList();
		String planId = request.getParameter("planId") + "";
		String type = request.getParameter("type") + "";
		String forwordPage = "";

		String stitle = "";
		// ��ӡ���������
		if ("1".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // ֻҪ��ǰ���׵ĵؿ飬��ֹ�ĵؿ鲻Ҫ���룻
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);
					// ��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "��";

						if ("3104".equals(app.getTransferMode()))
							stitle += "����";
						if ("3103".equals(app.getTransferMode()))
							stitle += "����";
						if ("3107".equals(app.getTransferMode()))
							stitle += "�б�";

						// ��ѯ���ؿ龺������
						// List personList =
						// tdscBidderAppService.findPageBidderList(app.getAppId());
						// List personList =
						// tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						//
						// System.out.println(app.getBlockName() +
						// app.getBlockNoticeNo());
						//
						// int iCountPerson = 0;
						// if (personList != null && personList.size() > 0) {
						// for (int j = 0; j < personList.size(); j++) {
						// TdscBidderApp bidderApp = (TdscBidderApp)
						// personList.get(j);
						//
						// String thisBidderMaxPrice =
						// tdscLocalTradeService.getBidderMaxPrice(bidderApp.getYktBh(),
						// app.getAppId());
						// // String thisBidderMaxPrice ="0";
						// if ("1".equals(bidderApp.getIsPurposePerson()) &&
						// "0".equals(thisBidderMaxPrice)) {
						// iCountPerson++;
						// }
						// if (thisBidderMaxPrice != null &&
						// !StringUtil.isEmpty(thisBidderMaxPrice) &&
						// !"0".equals(thisBidderMaxPrice)) {
						// iCountPerson++;
						// }
						// }
						// }

						// ��ѯ���ؿ龺������
						int iCountPerson = 0;
						List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						if (personList != null && personList.size() > 0) {
							iCountPerson = personList.size();
						}

						TdscBlockForm formBean = new TdscBlockForm();
						String s1 = "";
						if (!StringUtil.isEmpty(app.getBlockNoticeNo()))
							s1 = app.getBlockNoticeNo();

						formBean.setBlockNoticeNo(s1);// �ؿ���
						formBean.setBlockName(app.getBlockName());// �ؿ�����
						formBean.setCountSalePerson(iCountPerson + "");// ��������������������
						rptList.add(formBean);
					}
				}
				request.setAttribute("rptTitle", stitle + "���õؿ鱨�����");
				request.setAttribute("rptContext", "");
				// request.setAttribute("rptContext",
				// "��������xxxx����н����õ�ʹ��Ȩ���Ƴ��ù��Ƴ�n���ؿ飬������ֹx��x��xʱ����" + "���������˱�������" +
				// blockList.size() + "���ؿ�ľ���");
			}

			forwordPage = "printApplyTbl";
		}

		// ��ӡ���������(��ϸ)
		if ("2".equals(type)) {

			// ��1������λ�ŵ�һλ
			// ��2����߱��۵�λ�ŵڶ�λ
			// ��3����ע��ȥ������Ķ���

			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			cond.setOrderKey("blockNoticeNo");
			// cond.setStatus("01"); // ֻҪ��ǰ���׵ĵؿ飬��ֹ�ĵؿ鲻Ҫ���룻
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// ��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "��";

						if ("3104".equals(app.getTransferMode()))
							stitle += "����";
						if ("3103".equals(app.getTransferMode()))
							stitle += "����";
						if ("3107".equals(app.getTransferMode()))
							stitle += "�б�";

						// ��ѯ���ؿ龺������
						// List personList =
						// tdscBidderAppService.findPageBidderList(app.getAppId());
						// List personList =
						// tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());

						// �˴��Ѿ�������������

						// int iCountPerson = 0;
						// if (personList != null && personList.size() > 0)
						// iCountPerson = personList.size();

						TdscBlockForm formBean = new TdscBlockForm();

						String s1 = "&nbsp;";
						if (!StringUtil.isEmpty(app.getBlockNoticeNo()))
							s1 = app.getBlockNoticeNo();

						formBean.setBlockNoticeNo(s1);// �ؿ���
						formBean.setBlockName(app.getBlockName());// �ؿ�����
						formBean.setTotalLandArea(app.getTotalLandArea()); // ���������M2��
						formBean.setInitPrice(app.getInitPrice()); // ���س��ý���ʼ��

						// �õ���ǰ��߱���
						TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(app.getAppId());
						if (listingInfo != null && listingInfo.getCurrPrice() != null) {
							formBean.setCurrMaxPrice(listingInfo.getCurrPrice() + ""); // ��ǰ��߱���
						} else {
							formBean.setCurrMaxPrice(app.getInitPrice() + "");
						}

						// if (!"1".equals(app.getIsPurposeBlock())) {
						// // ��������ؿ�
						// List personList =
						// commonQueryService.getAllBiddersByAppId(app.getAppId());
						// if(personList!=null && personList.size()>0){
						// for (int j = 0; j < personList.size(); j++) {
						// TdscBidderForm bean = (TdscBidderForm)
						// personList.get(j);
						// System.out.println(j+ " ================
						// "+bean.getBidderName());
						// }
						// }
						//
						// formBean.setBidders(null);
						// // set bidder info end
						// rptList.add(formBean);
						// } else {
						List personList = commonQueryService.getAllBiddersByAppId(app.getAppId());
						// set bidder info start
						List bidders = new ArrayList();
						int m = 0;
						boolean hasMaxPriceUnit = false;
						if (personList != null && personList.size() > 0) {
							for (int j = 0; j < personList.size(); j++) {

								TdscBidderForm bean = (TdscBidderForm) personList.get(j);

								if (!StringUtils.isEmpty(bean.getBidderId())) {

									TdscBidderCondition bidderCond = new TdscBidderCondition();
									bidderCond.setBidderId(bean.getBidderId());
									TdscBidderView bidderView = tdscBidderViewService.queryBidderViewInfo(bidderCond);

									System.out.println(bidderView.getBidderName());

									if ("1".equals(bidderView.getIsPurposePerson()) && bidderView.getPurposeAppId().indexOf(app.getAppId()) != -1) {
										bean.setBidderName(bidderView.getBidderName());

										if (!isEmpty(bidderView.getConNum()))
											bean.setConNum(bidderView.getConNum());
										else
											bean.setConNum("δѡ�ƺ�");

										String currMaxPrice = formBean.getCurrMaxPrice();
										String thisBidderMaxPrice = tdscLocalTradeService.getBidderMaxPrice(bidderView.getYktBh(), app.getAppId());
										if (currMaxPrice.equals(thisBidderMaxPrice))
											hasMaxPriceUnit = true;

										String tmp = "����λ";
										if (hasMaxPriceUnit)
											tmp += "����߱��۵�λ";

										// if (personList.size() == 1 &&
										// !hasMaxPriceUnit)
										// tmp += "����߱��۵�λ";
										bean.setTranResult(tmp);
										bean.setMemo("1");

										bidders.add(bean);

									} else {
										bean.setBidderName(bidderView.getBidderName());

										if (!isEmpty(bidderView.getConNum()))
											bean.setConNum(bidderView.getConNum());
										else
											bean.setConNum("δѡ�ƺ�");

										String currMaxPrice = formBean.getCurrMaxPrice();
										// �õ��õ�λ����߱���
										String thisBidderMaxPrice = tdscLocalTradeService.getBidderMaxPrice(bidderView.getYktBh(), app.getAppId());
										// String thisBidderMaxPrice ="0";
										if (thisBidderMaxPrice != null && !StringUtil.isEmpty(thisBidderMaxPrice) && !"0".equals(thisBidderMaxPrice)) {
											if (currMaxPrice.equals(thisBidderMaxPrice)) {
												hasMaxPriceUnit = true;

												bean.setTranResult("��߱��۵�λ");
												bean.setMemo("2");
											} else {
												bean.setTranResult("&nbsp;");
											}
										} else {
											bean.setTranResult("δ����");
										}

										bidders.add(bean);

									}
								} else {
									//
									bidders.add(new TdscBidderForm());
								}
							}
						}

						bidders = seqBidders(bidders);

						formBean.setBidders(bidders);
						// set bidder info end
						rptList.add(formBean);
						// }
					}
				}
				request.setAttribute("rptTitle", stitle + "���õؿ鱨�����");
				forwordPage = "printApplyDetailTbl";
			}
		}

		// ��ӡ��������ϵ��ʽ
		if ("3".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // ֻҪ��ǰ���׵ĵؿ飬��ֹ�ĵؿ鲻Ҫ���룻
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// ��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "��";
						rptList.addAll(commonQueryService.getAllBiddersByAppId(app.getAppId()));
					}
				}
			}
			request.setAttribute("rptTitle", stitle + "��������ϵ��ʽ");
			forwordPage = "printLianXiRenTbl";
		}
		// ��ӡ��������ϵ��ʽ
		if ("4".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			if (blockList != null && blockList.size() > 0)
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);
					stitle = app.getNoitceNo().substring(0, 10) + Integer.parseInt(app.getNoitceNo().substring(10, 12)) + "��";
					rptList.addAll(commonQueryService.getAllBiddersByAppId(app.getAppId()));
				}
			request.setAttribute("rptTitle", stitle + "��������ϵ��ʽ");
			forwordPage = "printLianXiRenTbl";
		}

		// ��ӡ������
		if ("5".equals(type)) {
			String stitle2 = null;
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // ֻҪ��ǰ���׵ĵؿ飬��ֹ�ĵؿ鲻Ҫ���룻
			cond.setOrderKey("blockNoticeNo");
			Set allBiddersSet = new HashSet();
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
			// String allBidders = "";

			String noYiXiangNoPersonBlock = "";
			String noYiXiangOnePersonBlock = "";
			String yiXiangNoPersonBlock = "";

			int iJingJia = 0;

			BigDecimal totalarea = new BigDecimal(0);
			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size();) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// ��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						i++;
						stitle = app.getNoitceNo();
						String stitle1 = stitle.substring(5, 9);// ��
						stitle2 = stitle.substring(10, 12);//

						request.setAttribute("stitle1", stitle1);
						request.setAttribute("stitle2", stitle2);

						totalarea = totalarea.add(app.getTotalLandArea());// �����

						TdscBlockPlanTable tdscblockplantable = tdscLocalTradeService.getTdscBlockPlanTable(app.getPlanId());

						String issue_begin = DateUtil.date2String(tdscblockplantable.getIssueStartDate(), "yyyy��MM��dd��HH:mm");
						String acc_app_end = DateUtil.date2String(tdscblockplantable.getAccAppEndDate(), "yyyy��MM��dd��HH:mm");

						request.setAttribute("issue_begin", filter0FromDateString(issue_begin));// ��ʼ����ʱ��
						request.setAttribute("acc_app_end", filter0FromDateString(acc_app_end));// �����ֹʱ��

						// ��ѯ���ؿ龺������
						List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						// set bidder info start

						// ��������Ƶؿ�
						if ("0".equals(app.getIsPurposeBlock())) {
							if (personList.size() == 0) {
								noYiXiangNoPersonBlock += app.getBlockName() + "��";
								iJingJia++;
							} else if (personList.size() == 1) {
								noYiXiangOnePersonBlock += app.getBlockName() + "��";
								iJingJia++;
							}
						}

						if ("1".equals(app.getIsPurposeBlock())) {
							if (personList.size() == 1) {
								yiXiangNoPersonBlock += app.getBlockName() + "��";
								iJingJia++;
							}
						}

						// ������о�����
						// List bidders = new ArrayList();
						if (personList != null && personList.size() > 0) {
							for (int j = 0; j < personList.size(); j++) {
								TdscBidderApp bidder = (TdscBidderApp) personList.get(j);

								// List tmpLists =
								// tdscLocalTradeService.queryListingAppByYktXh(bidder.getYktXh());
								// if (tmpLists != null && tmpLists.size() > 0)
								// {

								// set bidder info end
								allBiddersSet.add(bidder.getConNum());
								// }
							}

						}
					} else {
						blockList.remove(i);
					}
				}

				request.setAttribute("blockcount", blockList);// ��ȡ�ù����µ����еؿ��������ֹ���׵ĵؿ�δͳ������

				float f = Float.parseFloat(totalarea + "");
				String ss = Math.round(f) + "";
				request.setAttribute("totalarea", ss);

				String retQizhong = "";

				if (!StringUtil.isEmpty(noYiXiangOnePersonBlock)) {
					retQizhong += noYiXiangOnePersonBlock.substring(0, noYiXiangOnePersonBlock.length() - 1) + "��ֻ��һ�ұ�����ֱ���ɱ�����λժ�ã�";

					// request.setAttribute("noYiXiangOnePersonBlockContext",
					// noYiXiangOnePersonBlockContext);
				}
				if (!StringUtil.isEmpty(noYiXiangNoPersonBlock)) {
					retQizhong += noYiXiangNoPersonBlock.substring(0, noYiXiangNoPersonBlock.length() - 1) + "�����˱��������Ҿ��ջأ�";
					// request.setAttribute("noYiXiangNoPersonBlockContext",
					// noYiXiangNoPersonBlockContext);
				}
				if (!StringUtil.isEmpty(yiXiangNoPersonBlock)) {
					retQizhong += yiXiangNoPersonBlock.substring(0, yiXiangNoPersonBlock.length() - 1) + "����������λ������ֱ��������λժ�ã�";
					// request.setAttribute("yiXiangNoPersonBlockContext",
					// yiXiangNoPersonBlockContext);
				}

				if (!StringUtil.isEmpty(retQizhong))
					request.setAttribute("retQizhong", "������" + retQizhong + "��");

				int s = blockList.size();
				// String count = (s - iJingJia) + "��";
				String count = (s) + "��";
				String jjCount = (s - iJingJia) + "��";// �����ֳ����ۻ��ڵĵؿ���
				request.setAttribute("count", count);
				request.setAttribute("jjCount", jjCount);

			}
			request.setAttribute("allBidders", allBiddersSet);
			request.setAttribute("rptTitle", "������");
			forwordPage = "printKaiCCiTbl";
		}
		// ��ӡ���ִ�
		if ("6".equals(type)) {
			TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
			cond.setPlanId(planId);
			// cond.setStatus("01"); // ֻҪ��ǰ���׵ĵؿ飬��ֹ�ĵؿ鲻Ҫ���룻
			cond.setOrderKey("blockNoticeNo");
			List blockList = this.commonQueryService.queryTdscBlockAppViewListByPlanId(cond);

			Set allBiddersSet = new HashSet();
			Set xcBiddersSet = new HashSet();
			// String allBidders = "";
			// �涨ʱ����δ���Ƶĵ�λ
			Set noListingPersonSet = new HashSet();

			if (blockList != null && blockList.size() > 0) {
				for (int i = 0; i < blockList.size(); i++) {
					TdscBlockAppView app = (TdscBlockAppView) blockList.get(i);

					// ��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
					if (!"00".equals(app.getStatus()) && !"04".equals(app.getTranResult())) {
						stitle = app.getNoitceNo();
						stitle = stitle.substring(10, 12);
						request.setAttribute("stitle", stitle);
						if ("3104".equals(app.getTransferMode()))
							stitle += "����";
						if ("3103".equals(app.getTransferMode()))
							stitle += "����";
						if ("3107".equals(app.getTransferMode()))
							stitle += "�б�";

						List personList = tdscBidderAppService.queryBidderAppListLikeAppId(app.getAppId());
						if (personList != null && personList.size() > 1) {
							// //personList ���������򣬵����˹��Ƶĵؿ磬�����������continue;
							// if (personList != null && personList.size() > 1)
							// {
							// for (int j = 0; j < personList.size(); j++) {
							//
							// TdscBidderApp bidder = (TdscBidderApp)
							// personList.get(j);
							// List tmpLists =
							// tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(bidder.getYktXh(),
							// app.getAppId());
							//
							// if (tmpLists == null || tmpLists.size() == 0) {
							// break;
							// }
							// }
							// }

							request.setAttribute("personList", personList);

							TdscBlockForm formBean = new TdscBlockForm();

							String s1 = "&nbsp;";
							if (!StringUtil.isEmpty(app.getBlockNoticeNo()))
								s1 = app.getBlockNoticeNo();

							formBean.setBlockNoticeNo(s1);// �ؿ���

							formBean.setBlockName(app.getBlockName());// �ؿ�����

							formBean.setTotalLandArea(app.getTotalLandArea());

							formBean.setInitPrice(app.getInitPrice()); // ���س��ý���ʼ��

							formBean.setLandLocation(app.getLandLocation());// ����λ��

							formBean.setTotalBuildingArea(app.getTotalBuildingArea()); // �ɹ������õ�

							TdscBlockPart blockPart = tdscLocalTradeService.getBlockInfoPart(app.getBlockId());

							TdscBlockInfoCondition blockCond = new TdscBlockInfoCondition();
							blockCond.setBlockId(app.getBlockId());
							List blockinfoList = tdscLocalTradeService.queryBlockInfoByCond(blockCond);
							TdscBlockInfo blockinfo = null;
							if (blockinfoList != null && blockinfoList.size() > 0)
								blockinfo = (TdscBlockInfo) blockinfoList.get(0);

							formBean.setBuildingHeight(blockinfo.getBuildingHeight());// �����޸�

							formBean.setLandUseType(blockPart.getLandUseType());// ������;

							formBean.setGreeningRate(blockPart.getGreeningRate());// �̵���

							formBean.setDensity(blockPart.getDensity());// �����ܶ�

							String sign2 = signConvert(blockPart.getVolumeRateSign());
							if (StringUtils.isNotBlank(sign2) && "����".equals(sign2)) {
								formBean.setVolumeRate(blockPart.getVolumeRate() + "<�ݻ���<" + blockPart.getVolumeRate2() + blockPart.getVolumeRateMemo());// �ݻ���
							} else {
								formBean.setVolumeRate(sign2 + blockPart.getVolumeRate() + blockPart.getVolumeRateMemo());// �ݻ���
							}

							formBean.setSpotAddPriceRange(app.getSpotAddPriceRange());// �����Ӽ۷���

							String tmp = blockinfo.getCountUse();

							if ("1".equals(tmp))
								tmp = "סլ";
							if ("2".equals(tmp))
								tmp = "��ҵ";
							if ("3".equals(tmp))
								tmp = "����";
							if ("4".equals(tmp))
								tmp = "��ҵ";
							if ("5".equals(tmp))
								tmp = "����";

							formBean.setCountUse(tmp);

							// �õ���ǰ��߱���
							TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(app.getAppId());
							// if (listingInfo != null &&
							// listingInfo.getCurrPrice() != null) {
							// formBean.setCurrMaxPrice(listingInfo.getCurrPrice()
							// + ""); // ��ǰ��߱���
							// } else {
							//
							// }
							// ��ѯ��ǰ��߱����˺���

							if (listingInfo != null && listingInfo.getCurrPrice() != null) {
								formBean.setCurrMaxPrice(listingInfo.getCurrPrice() + ""); // ��ǰ��߱���
								TdscBidderCondition bidderCond = new TdscBidderCondition();
								bidderCond.setYktBh(listingInfo.getYktXh());
								List bidderViewList = tdscBidderViewService.queryBidderByNameKahao(bidderCond);
								TdscBidderView bidderView = null;
								if (bidderViewList != null && bidderViewList.size() > 0)
									bidderView = (TdscBidderView) bidderViewList.get(0);
								formBean.setCurrMaxPersonNum(bidderView.getConNum());// ��ǰ��߱����˺���
							} else {
								// if (bidderViewList != null &&
								// bidderViewList.size() > 0)
								// formBean.setCurrMaxPersonNum(bidderView.getConNum());//
								// ��ǰ��߱����˺���

								// TODO �˴������⣬�������ֻ��һ�ҵ�λ���������޹��ƾͻ����
								// ������ʱ�����ߵ��˲�����Ϊ�����ļ���˵��������λ�������һ��
								String appId = app.getAppId();
								formBean.setCurrMaxPrice(app.getInitPrice() + "");

								System.out.println("appid=" + appId + " initPrice= " + app.getInitPrice());
							}

							// ��ѯ���ؿ龺������
							// �����ֳ��ľ�����
							String allBidderConNum = "";
							String xcBidderConNum = "";

							/*
							 * ��ȡ���о����˺���
							 */

							if (personList != null && personList.size() > 1) {
								for (int j = 0; j < personList.size(); j++) {
									TdscBidderApp bidder = (TdscBidderApp) personList.get(j);

									if (!StringUtil.isEmpty(bidder.getConNum())) {

										allBidderConNum += bidder.getConNum() + "�š�";

										allBiddersSet.add(new Integer(bidder.getConNum()));

									}

									TdscBidderCondition bidderCond = new TdscBidderCondition();
									bidderCond.setBidderId(bidder.getBidderId());
									TdscBidderView bidderView = tdscBidderViewService.queryBidderViewInfo(bidderCond);
									// �����˵ĺ���Ҫ��ȥ
									if ("1".equals(bidderView.getIsPurposePerson()) && bidderView.getPurposeAppId().indexOf(app.getAppId()) != -1) {
										allBidderConNum += bidder.getConNum() + "�š�";
										allBiddersSet.add(new Integer(bidderView.getConNum()));

									}

								}
							}
							/*
							 * ��ȡ�����ֳ��ĺ���
							 */
							if (personList != null && personList.size() > 1) {
								for (int j = 0; j < personList.size(); j++) {

									TdscBidderApp bidder = (TdscBidderApp) personList.get(j);
									List tmpLists = tdscLocalTradeService.queryTdscListingAppListByYKTXHAndAppId(bidder.getYktBh(), app.getAppId());

									if (tmpLists != null && tmpLists.size() > 0) {

										if (!StringUtil.isEmpty(bidder.getConNum())) {
											xcBidderConNum += bidder.getConNum() + "�š�";
											xcBiddersSet.add(new Integer(bidder.getConNum()));
										}
									} else {
										if (!"1".equals(bidder.getIsPurposePerson())) {
											noListingPersonSet.add(new Integer(bidder.getConNum()));

											// noListingPerson +=
											// bidder.getConNum() + "�š�";
										}
									}
									TdscBidderCondition bidderCond = new TdscBidderCondition();
									bidderCond.setBidderId(bidder.getBidderId());
									TdscBidderView bidderView = tdscBidderViewService.queryBidderViewInfo(bidderCond);
									// �����˵ĺ���Ҫ��ȥ
									if ("1".equals(bidderView.getIsPurposePerson()) && bidderView.getPurposeAppId().indexOf(app.getAppId()) != -1) {
										xcBidderConNum += bidder.getConNum() + "�š�";
										xcBiddersSet.add(new Integer(bidderView.getConNum()));

									}
									String[] currBlockPerson = xcBidderConNum.split("�š�");
									String[] temp = sort(currBlockPerson, 0);
									String strIfGrantCrfa = "";
									for (int k = 0; k < temp.length; k++) {
										strIfGrantCrfa += temp[k] + "�š�";
									}
									formBean.setIfGrantCrfa(strIfGrantCrfa.substring(0, strIfGrantCrfa.length() - 1));
								}
							}
							rptList.add(formBean);
						}
					}
				}
			}

			request.setAttribute("rptTitle", "���ִ�");
			forwordPage = "printZhuChiCiTbl";

			// allBiddersSet = sortConNum(allBiddersSet);
			Set ss = new TreeSet(allBiddersSet);

			request.setAttribute("allBiddersSize", ss.size() + "");// ���о����ˣ�δ����/�ѹ���

			String strXcBidders = mergeListingPerson(xcBiddersSet);
			request.setAttribute("xcBidders", strXcBidders);// �ֳ��Ѿ����Ƶĵ�λ

			String strNoListingPerson = mergeListingPerson(noListingPersonSet);
			request.setAttribute("noListing", strNoListingPerson);// �涨ʱ����δ���Ƶĵ�λ
		}
		request.setAttribute("rptList", rptList);
		return mapping.findForward(forwordPage);
	}

	/**
	 * �� set ��ϳ� �ַ���
	 * 
	 * @param noListingPersonSet
	 * @return
	 */
	private String mergeListingPerson(Set personSet) {
		String retStr = "";
		if (personSet != null && personSet.size() > 0) {
			// ������
			Set ss = new TreeSet(personSet);
			// �ټ��� ���� �� ���š���
			if (ss != null && ss.size() > 0) {
				Iterator it = ss.iterator();
				while (it.hasNext()) {
					// out.println(it.next());
					retStr += (it.next() + "�š�");
				}
			}
			return retStr.substring(0, retStr.length() - 1);
		} else {
			return "";
		}
	}

	private String signConvert(String strValue) {
		String sign2 = "";
		if ("01".equals(strValue)) {
			sign2 = "��";
		}
		if ("02".equals(strValue)) {
			sign2 = "��=";
		}
		if ("03".equals(strValue)) {
			sign2 = "=";
		}
		if ("04".equals(strValue)) {
			sign2 = ">";
		}
		if ("05".equals(strValue)) {
			sign2 = ">=";
		}
		if ("06".equals(strValue)) {
			sign2 = "����";
		}
		return sign2;
	}

	/**
	 * ���ַ��������������
	 * 
	 * @param str
	 *            ԭʼ�ַ�������
	 * @param flag
	 *            flag=0:˳������ flag=1:��������
	 * @return �������ַ�������
	 */
	public String[] sort(String[] str, int flag) {
		if (str == null || str.length == 0)
			throw new IllegalArgumentException();
		String temp = str[0];
		// ˳������ ,����С����
		if (flag == 0) {
			for (int i = 0; i < str.length - 1; i++) {
				for (int j = i + 1; j < str.length; j++) {
					if (str[i].compareTo(str[j]) > 0) {
						temp = str[i];
						str[i] = str[j];
						str[j] = temp;
					}
				}
			}
		}
		// else if(flag==1){//��������
		// for(int i=0;i<str.length-1;i++){
		// for(int j=i+1;j<str.length;j++){
		// if(str[i].compareTo(str[j])<0){
		// temp=str[i];
		// str[i]=str[j];
		// str[j]=temp;
		// }
		// }
		// }
		// }
		return str;
	}

	// add by xys end
	/**
	 * ���������ˣ���߱�����
	 */
	private List seqBidders(List bidders) {
		List retList = new ArrayList();

		if (bidders != null && bidders.size() > 0) {
			for (int i = 0; i < bidders.size(); i++) {
				TdscBidderForm bean = (TdscBidderForm) bidders.get(i);
				if ("1".equals(bean.getMemo())) {
					retList.add(bean);
					break;
				}
			}
			for (int i = 0; i < bidders.size(); i++) {
				TdscBidderForm bean = (TdscBidderForm) bidders.get(i);
				if ("2".equals(bean.getMemo())) {
					retList.add(bean);
					break;
				}
			}
			for (int i = 0; i < bidders.size(); i++) {
				TdscBidderForm bean = (TdscBidderForm) bidders.get(i);
				if (!"1".equals(bean.getMemo()) && !"2".equals(bean.getMemo())) {
					retList.add(bean);
				}
			}
		}
		return retList;

	}

	private String filter0FromDateString(String strDate) {
		String strYear = strDate.substring(0, 4);
		String strMonth = strDate.substring(5, 7);
		String strDay = strDate.substring(8, 10);
		String strHour = strDate.substring(11, 13);
		String strMinute = strDate.substring(14, 16);

		return strYear + "��" + Integer.parseInt(strMonth) + "��" + Integer.parseInt(strDay) + "��" + Integer.parseInt(strHour) + ":" + strMinute;
	}

	public ActionForward queryBzjdzqk(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// ��ѯ�б�
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);
		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);

		return mapping.findForward("bzjdzqkList");
	}

	public ActionForward printBzjdzqk(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);

		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		String stitle = "��֤�����������";

		if (appViewList != null && appViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) appViewList.get(0);
			if(tdscBlockAppView.getNoitceNo().indexOf("��")>-1){
				stitle = tdscBlockAppView.getNoitceNo().substring(0, 11) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(11, 12)) + "��" + stitle;
			}else{
				stitle = tdscBlockAppView.getNoitceNo().substring(0, 10) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(10, 12)) + "��" + stitle;
			}
			
		}

		List tdscReturnBailList = tdscReturnBailService.findTdscReturnBailListByPlanId(planId);
		List resList = new ArrayList();

		if (tdscReturnBailList != null && tdscReturnBailList.size() > 0) {
			for (int i = 0; i < tdscReturnBailList.size(); i++) {
				TdscReturnBail tdscReturnBail = (TdscReturnBail) tdscReturnBailList.get(i);
				TdscBidderForm tdscBidderForm = new TdscBidderForm();

				tdscBidderForm.setBidderName(tdscBidderAppService.getPersonNameByBidderId(tdscReturnBail.getBidderId()));// ����λ����Ȼ������
				tdscBidderForm.setBlockNoticeNo(commonQueryService.getTdscBlockAppViewByBlockId(tdscReturnBail.getBlockId()).getBlockNoticeNo());// �ؿ���
				tdscBidderForm.setBzjDzse(tdscReturnBail.getBidderBail());// ��֤������
				tdscBidderForm.setBzjDzqk(tdscReturnBail.getBzjBank());// ��������
				resList.add(tdscBidderForm);
			}
		}else{
			List list = tdscScheduletableService.queryTranAppList(planId);
			if(list!=null){
				TdscBlockTranApp tdscBlockApp = (TdscBlockTranApp)list.get(0);
				//���Ҵ˹��������еľ�����
				List bidderAppList = tdscBidderAppService.findAppByNoticeId(tdscBlockApp.getNoticeId());
				for(int i=0;i<bidderAppList.size();i++){
					TdscBidderApp bidderApp = (TdscBidderApp)bidderAppList.get(i);
					TdscBlockTranApp tranApp = this.tdscLocalTradeService.getTdscBlockTranApp(bidderApp.getAppId());
					
					if("1".equals(bidderApp.getIfCommit())&&StringUtils.isNotBlank(bidderApp.getCertNo())){
						TdscBidderPersonApp personApp = tdscBidderAppService.queryBidPerson(bidderApp.getBidderId());
						TdscBidderForm tdscBidderForm = new TdscBidderForm();
						tdscBidderForm.setBidderName(personApp.getBidderName());// ����λ����Ȼ������
						tdscBidderForm.setBlockNoticeNo(tranApp.getBlockNoticeNo());// �ؿ���
						tdscBidderForm.setBzjDzse(personApp.getBzjDzse());// ��֤������
						tdscBidderForm.setBzjDzqk(bidderApp.getBankId());// ��������
						resList.add(tdscBidderForm);
					}
				}
			}
		}
		
		List bankDicList = tdscBidderViewService.queryBankDicList();
		request.setAttribute("bankDicList", bankDicList);

		request.setAttribute("stitle", stitle);
		request.setAttribute("resList", resList);

		return mapping.findForward("bzjdzqk");
	}

	public ActionForward queryJyfwf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pageNo = request.getParameter("currentPage");
		String strTradeNum = request.getParameter("tradeNum") + "";
		String strTransferMode = request.getParameter("transferMode") + "";
		String strTradeStatus = request.getParameter("tradeStatus") + "";

		// ��ѯ�б�
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		if (!isEmpty(strTradeNum))
			planCondition.setTradeNum(strTradeNum);
		if (!isEmpty(strTransferMode))
			planCondition.setTransferMode(strTransferMode);
		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			planCondition.setStatus("00");
		}

		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false)
			currentPage = Integer.parseInt(pageNo);

		if (pageNo == null || Integer.parseInt(pageNo) < 1)
			currentPage = 1;

		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", planCondition);

		return mapping.findForward("jyfwfList");
	}

	/**
	 * ��ӡ���׷����ͳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printJyfwftj(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		condition.setOrderKey("blockNoticeNo");

		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		String stitle = "���׷����ͳ��";
		List resList = new ArrayList();

		if (appViewList != null && appViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) appViewList.get(0);
			if (StringUtils.isNotBlank(tdscBlockAppView.getNoitceNo())) {
				if(tdscBlockAppView.getNoitceNo().indexOf("��")>-1){
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 11) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(11, 12)) + "��" + stitle;
				}else{
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 10) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(10, 12)) + "��" + stitle;
				}
			}

			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) appViewList.get(i);
				BigDecimal chargeLower = new BigDecimal(0);

				// ֻ�н��׳ɹ��ĵؿ飬��Ӧ��ͳ���佻�׷���ѣ�����Ӧ��ͳ���佻�׷����
				if ("01".equals(app.getTranResult())) {
					List blockPartList = tdscBidderAppService.getTdscBlockPartList(app.getBlockId());
					if (blockPartList != null && blockPartList.size() > 0) {
						TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(0);
						BigDecimal serviceCharge = blockPart.getServiceCharge() == null ? new BigDecimal(0) : blockPart.getServiceCharge();// ���ؽ��׷���ѣ�����
						chargeLower = (app.getTotalLandArea().multiply(serviceCharge)).setScale(0, BigDecimal.ROUND_HALF_UP);
					}
					app.setTempBigDecimal(chargeLower);// ������ʱ������ؽ��׷������
					resList.add(app);
				}
			}
		}

		request.setAttribute("stitle", stitle);
		request.setAttribute("resList", resList);

		return mapping.findForward("jyfwftj");
	}

	/**
	 * ��ӡ�����ֺ�������ͳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printDzpgftj(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		condition.setOrderKey("blockNoticeNo");

		List appViewList = commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		String stitle = "�����ֺ�������ͳ��";
		List resList = new ArrayList();

		if (appViewList != null && appViewList.size() > 0) {
			TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) appViewList.get(0);
			if (StringUtils.isNotBlank(tdscBlockAppView.getNoitceNo())) {
				if(tdscBlockAppView.getNoitceNo().indexOf("��")>-1){
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 11) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(11, 12)) + "��" + stitle;
				}else{
					stitle = tdscBlockAppView.getNoitceNo().substring(0, 10) + Integer.parseInt(tdscBlockAppView.getNoitceNo().substring(10, 12)) + "��" + stitle;
				}
			}

			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) appViewList.get(i);
				// ֻ�н��׳ɹ����е����ֺ������ѵĵؿ飬��Ӧ��ͳ���������ѣ�����Ӧ��ͳ����������
				if ("01".equals(app.getTranResult())) {
					TdscBlockInfo tdscBlockInfo = new TdscBlockInfo();
					tdscBlockInfo = tdscScheduletableService.getBlockInfoApp(app.getBlockId());
					if (tdscBlockInfo != null && tdscBlockInfo.getGeologicalHazard() != null && tdscBlockInfo.getGeologicalHazard().compareTo(new BigDecimal(0)) > 0) {// ��д�˵����ֺ������ѣ����Ҵ���0
						app.setTempBigDecimal(tdscBlockInfo.getGeologicalHazard());// ������ʱ��ŵ����ֺ���������
						app.setAcceptPerson(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_GEOLOGY_ASSESS_UINT, tdscBlockInfo.getGeologyAssessUint()));
						resList.add(app);
					}
				}
			}
		}

		request.setAttribute("stitle", stitle);
		request.setAttribute("resList", resList);

		return mapping.findForward("dzzhpgftj");
	}

	/**
	 * У�龺�����Ƿ�����������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkJmrName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = (String) request.getParameter("bidderId");
		String bidderName = (String) request.getParameter("bidderName");
		String noticeId = (String) request.getParameter("noticeId");

		// �õ���ǰ���Ĺһ�����еؿ�
		List tdscBlockAppViewList = (List) tdscBidderAppService.queryAppViewListByNoticeId(noticeId);
		List allAppIdList = new ArrayList();
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				allAppIdList.add(tdscBlockAppView.getAppId());
			}
		}

		String rtnStatus = "0";// ��ʾ��ʶ,0��ʾ�������������޸ĸþ�����
		List resList = new ArrayList();// ������ű��λ�У������Ƶľ����˵������Ϣ

		List bidderViewList = tdscBidderViewService.queryBidderViewListByPersonName(bidderName);// ���ݵ�λ���ƣ��õ����ݿ��иõ�λ�������еľ�����Ϣ
		if (bidderViewList != null && bidderViewList.size() > 0) {
			for (int i = 0; i < bidderViewList.size(); i++) {
				TdscBidderView tdscBidderView = (TdscBidderView) bidderViewList.get(i);
				String appIdStr = tdscBidderView.getAppId();
				if (appIdStr != null) {
					String[] appIds = appIdStr.split(",");
					for (int m = 0; m < appIds.length; m++) {
						if (allAppIdList.contains(appIds[m])) {
							resList.add(tdscBidderView.getBidderId());
							break;
						}
					}
				}
			}
		}

		if (StringUtils.isNotBlank(bidderId)) {// bidderId��Ϊ��ʱ����ʾ�޸ģ���Ϊ�޸Ĳ����Ƚ�����͸��ӣ�Ŀǰ�����޸Ĳ�����У�飬ֻ������������У��
			// nothing
		} else {
			// ������������Ϣʱ����Ҫ�ж������ľ����������Ƿ��Ѿ��ڱ��λ�д��ڣ�������ڣ���������
			if (resList != null && resList.size() >= 1) {
				rtnStatus = "1";
			}
		}

		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pwBidtype = response.getWriter();
		// ���ظ��ص������Ĳ���
		pwBidtype.write(rtnStatus);
		pwBidtype.close();
		return null;
	}

	/**
	 * ����ѽ��ɣ���ѯ�Ѿ��������õĹ����б�
	 */
	public ActionForward queryFinishList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		PageList pageList = new PageList();
		String pageNo = request.getParameter("currentPage");
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false) {
			currentPage = Integer.parseInt(pageNo);
		}
		if (pageNo == null || Integer.parseInt(pageNo) < 1) {
			currentPage = 1;
		}

		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
		bindObject(condition, form);
		condition.setIfResultPublish("1");// �ɽ������ѷ�����
		condition.setOrderKey("resultPublishDate");
		condition.setOrderType("desc");

		List noticeList = new ArrayList();
		noticeList = tdscLocalTradeService.queryNoticeListByCondition(condition);

		pageList = PageUtil.getPageList(noticeList, pageSize, currentPage);
		request.setAttribute("tranceResultNoticeList", pageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("tranceResultNoticeList");
	}

	/**
	 * ����ѽ��ɵǼ�ҳ��
	 */
	public ActionForward toRegisterCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		if (!StringUtil.isEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			condition.setTranResult("01");// ��ѯ���׳ɹ��ĵؿ飨01��
			condition.setOrderKey("blockNoticeNo");
			// ͨ��ͨ�ýӿڲ�ѯ������ͼ
			List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		}

		request.setAttribute("noticeId", noticeId);

		return mapping.findForward("toRegisterCost");
	}

	/**
	 * ���潻�׷����
	 */
	public ActionForward savePaymentCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = (String) request.getParameter("noticeId");

		TdscBidderForm tdscBidderForm = (TdscBidderForm) form;
		String[] appIds = tdscBidderForm.getAppIds();
		if (appIds != null && appIds.length > 0) {
			for (int i = 0; i < appIds.length; i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscLocalTradeService.getTdscBlockTranApp(appIds[i]);
				tdscBlockTranApp.setPaymentCost(tdscBidderForm.getPaymentCost()[i]);
				tdscBlockTranApp.setPaymentDate(tdscBidderForm.getPaymentDate()[i]);
				tdscBlockTranApp.setPaymentMemo(tdscBidderForm.getPaymentMemo()[i]);
				tdscLocalTradeService.updateTdscBlockTranApp(tdscBlockTranApp);
			}
		}

		String forwardString = "bidderApp.do?method=toRegisterCost&noticeId=" + noticeId;
		ActionForward f = new ActionForward(forwardString, true);
		return f;
	}

	/**
	 * ��ѯ��һ�������ľ�������Ҫ���Ƶĵؿ�,����Щ�ؿ��ŷ���ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward selectListingBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bidderId = (String) request.getParameter("bidderId");
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBidderPersonApp bidderPersonApp = new TdscBidderPersonApp();

		if (StringUtils.isNotBlank(bidderId)) {
			bidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId.trim());
			bidderPersonApp = tdscBidderAppService.getTdscBidderPersonByBidderId(bidderApp.getBidderId());
		}

		String appIds = bidderApp.getAppId();// ����������ĵؿ飬���а�����������ؿ�
		String purposeAppIdStr = bidderPersonApp.getPurposeAppId() + "";// ���˵�����ؿ�

		List appIdList = new ArrayList();
		List purposeAppIdList = new ArrayList();
		List resultAppIdList = new ArrayList();// ��Ÿ�������ؿ飬��������ؿ鲻������
		String resultStr = "";// ��Ҫ���Ƶĵؿ�ĵؿ��ź�appId

		if (StringUtils.isNotBlank(appIds)) {
			appIdList = Arrays.asList(appIds.split(","));
		}

		// ���������ĳЩ�ؿ�������ˣ�����������ؿ��У�ȥ������ؿ飬ֻ���������ؿ�
		if (StringUtils.isNotBlank(purposeAppIdStr)) {
			purposeAppIdList = Arrays.asList(purposeAppIdStr.split(","));
			for (int i = 0; i < purposeAppIdList.size(); i++) {
				String purposeAppId = (String) purposeAppIdList.get(i);
				for (int m = 0; m < appIdList.size(); m++) {
					if (!purposeAppId.equals(appIdList.get(m))) {
						resultAppIdList.add(appIdList.get(m));
					}
				}
			}
		}

		// �ڸ���������ĵؿ��У��������û�й��Ƶģ���ƴ����Щ�ؿ��ŷ���ҳ��
		for (int i = 0; i < resultAppIdList.size(); i++) {
			String appId = (String) resultAppIdList.get(i);
			List listingAppList = tdscLocalTradeService.queryListingAppListByAppId(appId);
			if (listingAppList == null || (listingAppList != null && listingAppList.size() == 0)) {
				if (resultStr.length() > 0) {
					resultStr += "," + tdscLocalTradeService.getTdscBlockTranApp(appId).getBlockNoticeNo() + "," + appId;
				} else {
					resultStr += tdscLocalTradeService.getTdscBlockTranApp(appId).getBlockNoticeNo() + "," + appId;
				}
			}
		}

		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// ���ظ��ص������Ĳ���
		pw.write(resultStr);
		pw.close();

		return null;
	}

	/**
	 * ��һ�������˶Եؿ���й���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toListing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appIds = request.getParameter("appIds") + "";
		String bidderId = request.getParameter("bidderId") + "";
		TdscBidderApp bidderApp = new TdscBidderApp();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List appIdList = new ArrayList();// �þ�������Ҫ���Ƶĵؿ�
		if (StringUtils.isNotBlank(bidderId)) {
			bidderApp = tdscBidderAppService.getTdscBidderAppByBidderId(bidderId.trim());
		}
		if (StringUtils.isNotBlank(appIds)) {
			appIdList = Arrays.asList(appIds.split(","));
		}

		String failBlockNoticeNo = "";// ����й��ƣ���δ�ɹ����Ƶĵؿ�ĵؿ���,��Щ�ؿ�����Ҫ����ҳ�棬��ʾ�������˺ʹ��ڹ�����Ա
		for (int i = 0; i < appIdList.size(); i++) {
			String appId = appIdList.get(i) + "";
			List listingAppList = tdscLocalTradeService.queryListingAppListByAppId(appId.trim());
			tdscBlockTranApp = tdscLocalTradeService.getTdscBlockTranApp(appId.trim());
			if (listingAppList == null || (listingAppList != null && listingAppList.size() == 0)) {// ��Ҫ���Ƶĵؿ鵱ǰû�й��Ƽ�¼�����˶�����ƣ�������й��Ʊ���
				TdscListingInfo tdscListingInfo = new TdscListingInfo();
				TdscListingApp tdscListingApp = new TdscListingApp();

				tdscListingInfo.setListCert(bidderApp.getCertNo());
				tdscListingInfo.setAppId(appId);
				tdscListingInfo.setListDate(new Timestamp(System.currentTimeMillis()));
				tdscListingInfo.setYktXh(bidderApp.getYktBh());// �˴�д��þ����˵�CA֤���
				if (tdscBlockTranApp.getIsPurposeBlock() != null && "1".equals(tdscBlockTranApp.getIsPurposeBlock())) {// ��������Ƴ��õؿ飬�ڹ��ƻ��Ϲ��Ʊ��۳ɹ�����һ����tdsc_listing_info������¼ʱ���������������Ϊ2����1�����������˹��Ʊ���
					tdscListingInfo.setCurrRound(new BigDecimal(2));
					tdscListingInfo.setCurrPrice(tdscBlockTranApp.getInitPrice().add(tdscBlockTranApp.getSpotAddPriceRange()));// ������õؿ飬��һ�ֹ��Ƽ۸����Ϊ��ʼ�ۼ��ϼӼ۷���
					tdscListingApp.setListPrice(tdscBlockTranApp.getInitPrice().add(tdscBlockTranApp.getSpotAddPriceRange()));// ������õؿ飬��һ�ֹ��Ƽ۸����Ϊ��ʼ�ۼ��ϼӼ۷���
				} else if (tdscBlockTranApp.getIsPurposeBlock() != null && "0".equals(tdscBlockTranApp.getIsPurposeBlock())) {// ��������Ƴ��õؿ飬�ڹ��ƻ��Ϲ��Ʊ��۳ɹ�����һ����tdsc_listing_info������¼ʱ���������������Ϊ1
					tdscListingInfo.setCurrRound(new BigDecimal(1));
					tdscListingInfo.setCurrPrice(tdscBlockTranApp.getInitPrice());// ��������õؿ飬��һ�ֹ��Ƽ۸����Ϊ��ʼ��
					tdscListingApp.setListPrice(tdscBlockTranApp.getInitPrice());// ��������õؿ飬��һ�ֹ��Ƽ۸����Ϊ��ʼ��
				}
				tdscListingApp.setListCert(bidderApp.getCertNo());
				tdscListingApp.setPriceType("11");// ���Ͻ��׾������� 11:���� 22:��ʱ����
				tdscListingApp.setAppId(appId);
				tdscListingApp.setListNo(bidderApp.getConNum());

				this.tdscLocalTradeService.saveListingInfoAndApp(tdscListingInfo, tdscListingApp);
			} else {
				if (failBlockNoticeNo.length() > 0) {
					failBlockNoticeNo += "," + tdscBlockTranApp.getBlockNoticeNo();
				} else {
					failBlockNoticeNo = tdscBlockTranApp.getBlockNoticeNo();
				}
			}
		}

		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// ���ظ��ص������Ĳ���
		pw.write(failBlockNoticeNo);
		pw.close();

		return null;
	}
}
