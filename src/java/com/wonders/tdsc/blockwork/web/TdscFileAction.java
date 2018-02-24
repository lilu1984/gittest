package com.wonders.tdsc.blockwork.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.blockwork.web.form.TdscDicBean;
import com.wonders.tdsc.blockwork.web.form.TdscFileForm;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.kyq.bo.KyqNotice;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.common.util.MoneyUtils;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.kyq.service.KyqContent;
import com.wonders.tdsc.kyq.service.KyqNoticeService;
import com.wonders.tdsc.lob.bo.TdscEsClob;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscFileAction extends BaseAction {

	private CommonQueryService commonQueryService;

	private TdscFileService tdscFileService;

	private TdscBlockInfoService tdscBlockInfoService;

	private AppFlowService appFlowService;

	private TdscScheduletableService tdscScheduletableService;

	private TdscNoticeService tdscNoticeService;

	private TdscBidderAppService tdscBidderAppService;

	private IdSpringManager idSpringManager;
	
	
	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	/**
	 * ����ͨ�ýӿڻ��������Ϣ
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
	/**
	 * ��ѯ����ҵ���б�
	 */
	public ActionForward queryAppListWithNodeId_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		/*
		 * String blockName = request.getParameter("blockName"); String blockType = request.getParameter("blockType"); String transferMode = request.getParameter("transferMode");
		 */
		bindObject(condition, form);
		String currentPage = request.getParameter("currentPage");
		/* String districtId = request.getParameter("districtId"); */
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		String nodeId = "03";
		// ��ȡ�û����Ȩ��
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);
		/*
		 * condition.setBlockName(blockName); condition.setBlockType(blockType); condition.settransferMode(transferMode);
		 */

		condition.setNodeList(nodeList); // �����û�Ȩ�޲�ѯ�����б�
		condition.setNodeId(nodeId);
		condition.setCurrentPage(cPage);
		/* condition.setDistrictId(districtId); */
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewFlowPageList(condition));// ��������ѯ�б�
		request.setAttribute("queryAppCondition", condition);

		return mapping.findForward("list");
		// return mapping.findForward("listnew");
	}

	// ��ѯ�����п����������ļ���������Ϣ�б�
	public ActionForward newCRWJ(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		bindObject(condition, form);

		// String issueStartDate = "";
		if (!"1".equals(request.getParameter("forwardType"))) {
			bindObject(condition, form);
			condition.setTransferMode(request.getParameter("transferMode"));
			// issueStartDate = request.getParameter("issueStartDate");
		}

		String currentPage = request.getParameter("currentPage");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		String nodeId = "03";
		condition.setNoitceNo("null");

		String noticeId = request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);

		String noticeNo = request.getParameter("noticeNo");
		if (!"".equals(noticeNo) && noticeNo != null) {
			noticeNo = "������[" + noticeNo.substring(0, 4) + "]��" + noticeNo.substring(4) + "��";
			request.setAttribute("noticeNo", noticeNo);
		}

		// ��ȡ�û����Ȩ��
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);
		/*
		 * condition.setBlockName(blockName); condition.setBlockType(blockType); condition.settransferMode(transferMode);
		 */

		condition.setNodeList(nodeList); // �����û�Ȩ�޲�ѯ�����б�
		condition.setNodeId(nodeId);
		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
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
		// PageList pageList =
		// commonQueryService.queryTdscBlockAppViewFlowPageList(condition);

		List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewFlowList(condition);
		// if(pageList!=null){
		// tdscBlockAppViewList = pageList.getList();
		// }
		List tdscBlockPlanTableList = new ArrayList();
		List planIdList = new ArrayList();

		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				String planId = tdscBlockAppView.getPlanId();

				if (planId != null && !"".equals(planId) && !planIdList.contains(planId)) {
					planIdList.add(planId);
					TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);
					if (tdscBlockPlanTable != null)
						tdscBlockPlanTableList.add(tdscBlockPlanTable);
				}
			}
		}
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("pageList", tdscBlockPlanTableList);
		request.setAttribute("tdscBlockPlanTableList", tdscBlockPlanTableList);// ��������ѯ�б�
		request.setAttribute("condition", condition);

		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		if (tdscBlockPlanTableList != null && tdscBlockPlanTableList.size() > 0) {
			int size = tdscBlockPlanTableList.size();
			tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableList.get(size - 1);
		}

		java.util.Date sysdate = new java.util.Date();
		java.text.SimpleDateFormat dformat = new java.text.SimpleDateFormat("yyyy");
		String year = (String) dformat.format(sysdate);

		String noticeNoPrefix = "";
		String tradeNum = tdscBlockPlanTable.getTradeNum();
		if(StringUtils.isNotBlank(tradeNum) && tradeNum.indexOf("��") != -1){
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "��" + tradeNum.substring(4, 6) + "����[" + year + "]";
			} else {
				noticeNoPrefix = "���������[" + year + "]";
			}
		}else{
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "��" + tradeNum.substring(4, 5) + "����[" + year + "]";
			} else {
				noticeNoPrefix = "��������[" + year + "]";
			}
		}

		String blockQuality = "102";
		if ("��".equals(noticeNoPrefix.substring(1, 2))) {
			blockQuality = "101";
		} else if ("��".equals(noticeNoPrefix.substring(1, 2))) {
			blockQuality = "102";
		}
		request.setAttribute("blockQuality", blockQuality);

		// �����һ�ν���ҳ�����������ţ��Ժ�ÿ�β�ѯ���ٲ����µĹ����
		if ("1".equals(request.getParameter("ifDisplay"))) {
			request.setAttribute("ifDisplay", "1");
			String temp = request.getParameter("noticeNo");
			request.setAttribute("createNoticeNo", temp);
		} else {
			// String temp = (String) tdscNoticeService.selectNoticeNo();
			String temp ="";
			if(StringUtils.isNotBlank(tradeNum) && tradeNum.indexOf("��") != -1){
				temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("17",noticeNoPrefix);
			}else{
				temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("15",noticeNoPrefix);
			}
			request.setAttribute("createNoticeNo", noticeNoPrefix + temp + "��");
		}

		return mapping.findForward("listNewCrwj");
	}

	/**
	 * ��ѯ�����������ļ��б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAppFileListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ��ȡ��ǰ�û�
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// ���õؿ������ѯ����
		TdscBaseQueryCondition sqCondition = new TdscBaseQueryCondition();
		// TdscBlockForm tdscBlockForm = (TdscBlockForm)form;
		// ���������ص����б�ҳ��
		// request.setAttribute("condition", sqCondition);
		this.bindObject(sqCondition, form);
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		List districtIdList = null;
		List qxList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
		if (qxList != null && qxList.size() > 0) {
			districtIdList = qxList;
		}
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);

		// String nodeId = "03";

		if (sqCondition.getBlockName() != null && !"".equals(sqCondition.getBlockName())) {
			sqCondition.setBlockName(StringUtil.GBKtoISO88591(sqCondition.getBlockName()));
		}
		if (sqCondition.getAuditedNum() != null && !"".equals(sqCondition.getAuditedNum())) {
			sqCondition.setAuditedNum(StringUtil.GBKtoISO88591(sqCondition.getAuditedNum()));
		}
		// sqCondition.setNodeList(nodeList); // �����û�Ȩ�޲�ѯ�����б�

		// sqCondition.setOrderKey("actionDateBlock");//�������ֶ�����
		// sqCondition.setOrderType("desc");
		// sqCondition.setNodeId(nodeId);
		sqCondition.setDistrictIdList(districtIdList);
		// sqCondition.setCurrentPage(currentPage);
		// sqCondition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		sqCondition.setUser(user);
		sqCondition.setOrderKey("tradeNum asc, a.blockName");

		// ��ѯ���� �������ͨ�� �������
		// ���ƹ���󼴿ɱ����ļ�,������ɱ��ƹ����Ҳ������ļ�
		List statusIdList = new ArrayList();
		statusIdList.add("0303");
		// statusIdList.add("0305");
		statusIdList.add("0401");
		statusIdList.add("0601");
		statusIdList.add("0701");
		statusIdList.add("0702");
		statusIdList.add("0703");
		statusIdList.add("0801");
		statusIdList.add("1101");
		statusIdList.add("1201");
		statusIdList.add("1301");
		statusIdList.add("1401");
		statusIdList.add("1501");
		statusIdList.add("1502");
		statusIdList.add("1601");
		// statusIdList.add("0601");
		sqCondition.setStatusIdList(statusIdList);

		// ����ͨ�ò�ѯ ��ѯ�б���Ϣ
		List queryAppList = commonQueryService.queryTdscBlockAppViewListWithoutNode(sqCondition);

		// request.setAttribute("nodeId", nodeId);
		// �����ؿ��ѯ
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		// �趨status��ѯ��δ���׼�������ֹ����Ϣ
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };

		// �����ؿ��ѯ����
		TdscBlockInfoCondition cbCondition = new TdscBlockInfoCondition();
		this.bindObject(cbCondition, form);
		cbCondition.setStatus(status);

		// �ж��Ƿ��ǹ���Ա
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			cbCondition.setUserId(user.getUserId());
		}

		// cbCondition.setCurrentPage(currentPage);
		// cbCondition.setPageSize(((Integer)
		// DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// ���ظ���������ҳ�� ���ػ�����Ϣ�б�
		List cbBlockList = (List) tdscBlockInfoService.queryAppViewList(cbCondition);
		// ���������ص����б�ҳ��
		// this.bindObject(sqCondition, tdscBlockForm);

		if (sqCondition.getBlockName() != null && !"".equals(sqCondition.getBlockName())) {
			sqCondition.setBlockName(StringUtil.ISO88591toGBK(sqCondition.getBlockName()));
		}
		if (sqCondition.getAuditedNum() != null && !"".equals(sqCondition.getAuditedNum())) {
			sqCondition.setAuditedNum(StringUtil.ISO88591toGBK(sqCondition.getAuditedNum()));
		}
		request.setAttribute("condition", sqCondition);

		Map blockNoticeMap = new HashMap();
		
		// ����2����ѯ���List
		if (queryAppList != null && queryAppList.size() > 0) {
			for (int i = 0; i < queryAppList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) queryAppList.get(i);

				// ����������ʱ�� ǰ

				String getFileEndDate = DateUtil.date2String(appView.getAccAppEndDate(), "yyyyMMddHHmmss");
				String nowTime = DateConvertor.getCurrentDateWithTimeZone();

				Long f = new Long(getFileEndDate);
				Long n = new Long(nowTime);
				if (f.longValue() >= n.longValue()) {
					TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appView.getAppId());
					if (temp != null)
						appView.setBidEvaLoc("makedDoc"); // ������� ����ʾ�鿴

					cbBlockList.add(appView);
					TdscNoticeApp tdscNoticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(appView.getNoticeId());
					String ifReleased = null;
					if(tdscNoticeApp != null){
						ifReleased = tdscNoticeApp.getIfReleased();
					}
					//���õؿ��ID�͸õؿ����ڳ��ù����Ƿ񷢲�set��map��
					if(ifReleased != null){
						blockNoticeMap.put(appView.getBlockId(),tdscNoticeService.getTdscNoticeAppByNoticeId(appView.getNoticeId()).getIfReleased());//���õؿ��ID�͸õؿ����ڳ��ù����Ƿ񷢲�set��map��						
					}else{
						blockNoticeMap.put(appView.getBlockId(),"0");
					}
				}

			}
		}

		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		PageList PageList = PageUtil.getPageList(cbBlockList, pageSize, currentPage);

		request.setAttribute("queryAppList", PageList);// �����б�
		request.setAttribute("blockNoticeMap", blockNoticeMap);

		return mapping.findForward("fileList");
	}

	public ActionForward viewDoc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		if (!StringUtil.isEmpty(appId)) {

			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// ��ѯ������Ϣ��(��֤���ֹʱ��)
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.findBlockTranAppInfo(appId);
			// ��ѯ���Ȱ��ű�
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(tdscBlockTranApp.getPlanId());
			request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);

			String strMethod = "";
			if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
				strMethod = "������";
			} else {
				strMethod = "������";
			}

			String transferMode = commonInfo.getTransferMode();

			String dicValue = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_BLOCK_TRANSFER).get(transferMode) + "";
			strMethod += "����" + dicValue;

			request.setAttribute("purposeInfo", strMethod);// �����򹫿�����,�ȵ�.

			TdscBlockInfo blockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(commonInfo.getBlockId());

			List tdscBlockRemisemoneyDefrayList = tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockInfo.getBlockId());

			request.setAttribute("blockInfo", blockInfo);
			request.setAttribute("commonInfo", commonInfo);
			request.setAttribute("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);

			// Get TDSC_BLOCK_PART information by block_id
			List blockPartList = tdscBlockInfoService.getTdscBlockPartList(commonInfo.getBlockId());
			if (blockPartList != null) {
				String partNums = "";
				// String partArea = "";
				BigDecimal partArea = new BigDecimal("0.0");

				for (int i = 0; i < blockPartList.size(); i++) {
					TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(i);
					partNums += blockPart.getBlockCode() + ", ";
					partArea = partArea.add(blockPart.getBlockArea());
				}

				request.setAttribute("partNums", partNums);// �ؿ���
				request.setAttribute("partArea", partArea);// �ؿ����

			}

			String bankName = "";
			String bankAccount = "";
			List bankDicList = tdscBlockInfoService.queryBankDicList();
			if (StringUtils.isNotBlank(commonInfo.getMarginBank())) {
				if (bankDicList.size() > 0) {
					for (int i = 0; i < bankDicList.size(); i++) {
						TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
						if (commonInfo.getMarginBank().equals(dicBank.getDicCode())) {
							bankName = dicBank.getDicDescribe();
							bankAccount = dicBank.getDicValue();
						}
					}
				}
			}
			request.setAttribute("bankName", bankName);
			request.setAttribute("bankAccount", bankAccount);

			// DicDataUtil.getInstance().getDicItemName(arg0, arg1)//�ֵ����

			// �����ļ�ȡ������,�������ļ���������
			String d1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "yyyy��M��d��");
			String t1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "HH:mm");
			String d2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "yyyy��M��d��");
			String t2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "HH");
			String t3 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "mm");
			String swDate = d1 + "��" + d2 + ("00".equals(t3) ? t2 + "ʱ" : t2 + "ʱ" + t3 + "��");
			request.setAttribute("shouwenDate", swDate);

			// �ύ��������
			d1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "yyyy��M��d��");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "yyyy��M��d��");
			t2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "HH");
			t3 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "mm");
			String tjDate = d1 + "9ʱ��" + d2 + ("00".equals(t3) ? t2 + "ʱ" : t2 + "ʱ" + t3 + "��");
			request.setAttribute("tijiaoDate", tjDate);

			// ������ʼ����
			d1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(), "yyyy��M��d��");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(), "yyyy��M��d��");
			// t2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(), "hh:mm");
			String ggDate = d1 + "��" + d2;
			request.setAttribute("gonggaoDate", ggDate);
			request.setAttribute("gonggaokaishiDate", d1);

			// �ֳ���������
			d1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "yyyy��M��d��");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "HHʱmm��");
			String jjDate = d1 + "" + t1;
			request.setAttribute("jingjiaDate", jjDate);
			request.setAttribute("chengjiaoDate", d1);

			// ������������
			d1 = DateUtil.date2String(tdscBlockPlanTable.getListStartDate(), "yyyy��M��d��");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "yyyy��M��d��");
			String h1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "HH");
			String s1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "mm");
			String gpDate = d1 + "9ʱ00����" + d2 + h1 + "ʱ" + s1 + "��";
			String gpjzDate = d2 + h1 + "ʱ" + s1 + "��";
			request.setAttribute("guapaiDate", gpDate);
			request.setAttribute("guapaijiezhiDate", gpjzDate);

			// ϵͳ��ǰ����
			Calendar calendar = Calendar.getInstance();
			String sysDate = DateUtil.date2String(calendar.getTime(), "yyyy��M��d��");
			request.setAttribute("sysDate", sysDate);

			String blockType = "";
			blockType = commonInfo.getBlockType();
			// String transferMode = "";
			transferMode = commonInfo.getTransferMode();
			// �б�
			if ("3107".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õ��б�ģ��.doc");
					request.setAttribute("modeNameEn", "gyxydzb.doc");
				} else {// ��Ӫ���õ�
					request.setAttribute("modeName", "��Ӫ���õ��б�ģ��.doc");
					request.setAttribute("modeNameEn", "jyxydzb.doc");
				}
			}

			// ����
			if ("3103".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õ�����ģ��.doc");
					request.setAttribute("modeNameEn", "2008gyxydpm.doc");
				} else {// ��Ӫ���õ�
					request.setAttribute("modeName", "��Ӫ���õ�����ģ��.doc");
					request.setAttribute("modeNameEn", "2008jyxydpm.doc");
				}
			}

			// ����
			if ("3104".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õع���ģ��.doc");
					request.setAttribute("modeNameEn", "2008gyxydgp.doc");
				} else {// ��Ӫ���õ�
					// LocalTradeType���ڡ�1��ʱ���ǡ��ֳ����汨�ۡ�����ʽ������"�����ļ�__סլ������ļ�.doc"
					if ("1".equals(commonInfo.getLocalTradeType())) {
						request.setAttribute("modeNameEn", "2008jyxydgp_xcsmbj.doc");
						// LocalTradeType���ڡ�2��ʱ���ǡ����ƾ��ۡ�����ʽ������"�����ļ�__��סլ������ļ�.doc"
					} else if ("2".equals(commonInfo.getLocalTradeType())) {
						request.setAttribute("modeNameEn", "2008jyxydgp_jpjj.doc");
					}
					request.setAttribute("modeName", "��Ӫ���õع���ģ��.doc");
				}
				// ��������ֻ��һ��ģ��
				request.setAttribute("modeNameEn", "wxfile.doc");
			}

			// ���ͨ��appId������TdscBlockFileApp���в鵽��¼����˵���ó����ļ��Ѿ��ݴ��
			TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
			if (temp != null) {
				request.setAttribute("ifZancun", "1");
				request.setAttribute("fileName", temp.getFileUrl());
				request.setAttribute("oldAppId", appId);
				request.setAttribute("recordId", temp.getRecordId());
			}

			// String fileName = tdscFileService.getFileName(appId);
			// String fileUrl = appId + ".doc";
			// request.setAttribute("fileName", fileName);
			// request.setAttribute("fileUrl", fileUrl);

		}
		request.setAttribute("view", "true");

		return mapping.findForward("viewFileInfo");

		// return mapping.findForward("previewFileInfo");
	}

	/**
	 * �����ļ�������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward makeDoc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String nodeId = request.getParameter("nodeId");
		String statusId = request.getParameter("statusId");
		// String view = request.getParameter("view");
		request.setAttribute("view", "false");
		String recordId = "";

		String retPage = "";
		String ifOnLine = "0";//���׷�ʽ��0Ϊ�ֳ���1Ϊ��ʱ

		if (appId != null) {

			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// ��ѯ������Ϣ��(��֤���ֹʱ��)
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.findBlockTranAppInfo(appId);
			// ��ѯ���Ȱ��ű�
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(tdscBlockTranApp.getPlanId());
			ifOnLine = tdscBlockPlanTable.getIfOnLine();//���׷�ʽ��0Ϊ�ֳ���1Ϊ��ʱ
			request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);

			String strMethod = "";
			if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
				strMethod = "������";
			} else {
				strMethod = "������";
			}

			String transferMode = commonInfo.getTransferMode();

			String dicValue = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_BLOCK_TRANSFER).get(transferMode) + "";
			strMethod += "����" + dicValue;

			request.setAttribute("purposeInfo", strMethod);// �����򹫿�����,�ȵ�.

			TdscBlockInfo blockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(commonInfo.getBlockId());

			List tdscBlockRemisemoneyDefrayList = tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockInfo.getBlockId());

			request.setAttribute("blockInfo", blockInfo);
			request.setAttribute("commonInfo", commonInfo);
			request.setAttribute("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);

			// Get TDSC_BLOCK_PART information by block_id
			List blockPartList = tdscBlockInfoService.getTdscBlockPartList(commonInfo.getBlockId());
			if (blockPartList != null) {
				String partNums = "";
				// String partArea = "";
				BigDecimal partArea = new BigDecimal("0.0");

				for (int i = 0; i < blockPartList.size(); i++) {
					TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(i);
					partNums += blockPart.getBlockCode() + ", ";
					partArea = partArea.add(blockPart.getBlockArea());
				}

				request.setAttribute("partNums", partNums);// �ؿ���
				request.setAttribute("partArea", partArea);// �ؿ����

				TdscBlockPart blockPart = (TdscBlockPart) blockPartList.get(0);
				request.setAttribute("tdscBlockPart", blockPart);// �ؿ���

				BigDecimal totalArea = blockInfo.getTotalLandArea();
				BigDecimal serviceCharge = blockPart.getServiceCharge();
				BigDecimal chargeLower = (totalArea.multiply(serviceCharge)).setScale(0, BigDecimal.ROUND_HALF_UP);

				String chargeUpper = MoneyUtils.NumToRMBStrWithJiao(Double.parseDouble(chargeLower + ""));
				request.setAttribute("chargeLower", chargeLower + "");
				request.setAttribute("chargeUpper", chargeUpper + "");
				
				if(blockInfo.getGeologicalHazard()!=null && blockInfo.getGeologicalHazard().compareTo(new BigDecimal(0))>0){//��д�˵����ֺ������ѣ����Ҵ���0
					String dzzhpgfUpper = MoneyUtils.NumToRMBStrWithJiao(Double.parseDouble(blockInfo.getGeologicalHazard() + ""));//�����ֺ�������
					request.setAttribute("dzzhpgfUpper", dzzhpgfUpper + "");
					
					List bankDicList = tdscBlockInfoService.queryGeologyAssessUintDicList();
					if(StringUtils.isNotBlank(blockInfo.getGeologyAssessUint())){
						if (bankDicList != null && bankDicList.size() > 0) {
							for (int i = 0; i < bankDicList.size(); i++) {
								TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
								if (blockInfo.getGeologyAssessUint().equals(dicBank.getDicCode())) {
									request.setAttribute("geologyAssessUint", dicBank.getDicValue());//����
									request.setAttribute("geologyAssessUintBankName", dicBank.getDicDescribe().split(",")[0]);//DIC_DESCRIBE�����ö��ŷָ��������ƺ������˺�,������ϵ�绰������
									request.setAttribute("geologyAssessUintBankNum", dicBank.getDicDescribe().split(",")[1]);//DIC_DESCRIBE�����ö��ŷָ��������ƺ������˺�,������ϵ�绰������
									request.setAttribute("geologyAssessUintFinTel", dicBank.getDicDescribe().split(",")[2]);//DIC_DESCRIBE�����ö��ŷָ��������ƺ������˺�,������ϵ�绰������
									request.setAttribute("geologyAssessUintFox", dicBank.getDicDescribe().split(",")[3]);//DIC_DESCRIBE�����ö��ŷָ��������ƺ������˺�,������ϵ�绰������
								}
							}
						}
					}
				}
			}

			String bankName = "";
			String bankAccount = "";
			String finTel = "";
			String fox = "";
			List bankDicList = tdscBlockInfoService.queryBankDicList();
			if (StringUtils.isNotBlank(commonInfo.getMarginBank())) {
				if (bankDicList.size() > 0) {
					for (int i = 0; i < bankDicList.size(); i++) {
						TdscDicBean dicBank = (TdscDicBean) bankDicList.get(i);
						if (commonInfo.getMarginBank().equals(dicBank.getDicCode())) {
							bankName = dicBank.getDicDescribe();
							bankAccount = dicBank.getDicValue();
							finTel = dicBank.getDicValue();
							fox = dicBank.getDicValue();
						}
					}
				}
			}
			request.setAttribute("bankName", bankName);
			request.setAttribute("bankAccount", bankAccount);
			request.setAttribute("finTel", finTel);
			request.setAttribute("fox", fox);

			// DicDataUtil.getInstance().getDicItemName(arg0, arg1)//�ֵ����

			// �����ļ�ȡ������,�������ļ���������
			String d1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "yyyy��M��d��");
			String t1 = DateUtil.date2String(tdscBlockPlanTable.getGetFileStartDate(), "HH:mm");
			String d2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "yyyy��M��d��");
			String t2 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "HH");
			String t3 = DateUtil.date2String(tdscBlockPlanTable.getGetFileEndDate(), "mm");
			String swDate = d1 + "9ʱ��" + d2 + ("00".equals(t3) ? t2 + "ʱ" : t2 + "ʱ" + t3 + "��");
			request.setAttribute("shouwenDate", swDate);

			// �ύ��������
			d1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "yyyy��M��d��");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "yyyy��M��d��");
			t2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "HH");
			t3 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "mm");
			String tjDate = d1 + "9ʱ��" + d2 + ("00".equals(t3) ? t2 + "ʱ" : t2 + "ʱ" + t3 + "��");
			request.setAttribute("tijiaoDate", tjDate);
			
			//����������
			d1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "yyyy��M��d��");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(), "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "yyyy��M��d��");
			t2 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "HH");
			t3 = DateUtil.date2String(tdscBlockPlanTable.getAccAppEndDate(), "mm");
			String bmDate = d1 + "��" + d2 + ("00".equals(t3) ? t2 + "ʱ" : t2 + "ʱ" + t3 + "��");
			request.setAttribute("bmDate", bmDate);

			// ������ʼ����
			d1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(), "yyyy��M��d��");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getIssueStartDate(),
			// "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(), "yyyy��M��d��");
			// t2 = DateUtil.date2String(tdscBlockPlanTable.getIssueEndDate(),
			// "hh:mm");
			String ggDate = d1 + "��" + d2;
			request.setAttribute("gonggaoDate", ggDate);
			request.setAttribute("gonggaokaishiDate", d1);

			// �ֳ���������
			d1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "yyyy��M��d��");
			t1 = DateUtil.date2String(tdscBlockPlanTable.getSceBidDate(), "HHʱmm��");
			String jjDate = d1 + "" + t1;
			request.setAttribute("jingjiaDate", jjDate);
			request.setAttribute("chengjiaoDate", d1);

			// ������������
			d1 = DateUtil.date2String(tdscBlockPlanTable.getListStartDate(), "yyyy��M��d��");
			// t1 = DateUtil.date2String(tdscBlockPlanTable.getAccAppStatDate(),
			// "hh:mm");
			d2 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "yyyy��M��d��");
			String h1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "HH");
			String s1 = DateUtil.date2String(tdscBlockPlanTable.getListEndDate(), "mm");
			String gpDate = d1 + "9ʱ00����" + d2 + h1 + "ʱ" + s1 + "��";
			String gpjzDate = d2 + h1 + "ʱ" + s1 + "��";
			request.setAttribute("guapaiDate", gpDate);
			request.setAttribute("guapaijiezhiDate", gpjzDate);

			// ϵͳ��ǰ����
			Calendar calendar = Calendar.getInstance();
			String sysDate = DateUtil.date2String(calendar.getTime(), "yyyy��M��d��");

			String tempSysDate = DateConvertor.formatStr(DateUtil.date2String(calendar.getTime(), "yyyy-MM-dd"));
			String yearAndMonth = DateConvertor.getYearStr(tempSysDate) + "��" + DateConvertor.getMonthStr(tempSysDate) + "��";

			request.setAttribute("sysDate", sysDate);
			request.setAttribute("yearAndMonth", yearAndMonth);
			
			//����־����ƺ͵�ַ
			String districtId = blockInfo.getDistrictId().toString();
			Map map = getDistrictMap();
			ArrayList al = (ArrayList)map.get(districtId);
			request.setAttribute("dkfj", al.get(0));
			request.setAttribute("dkfjdz", al.get(1));
			
			String blockType = "";
			blockType = commonInfo.getBlockQuality();
			// String transferMode = "";
			transferMode = commonInfo.getTransferMode();
			// �б�
			if ("3107".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õ��б�ģ��.doc");
					request.setAttribute("modeNameEn", "gyxydzb.doc");
				} else {// ��Ӫ���õ�
					request.setAttribute("modeName", "��Ӫ���õ��б�ģ��.doc");
					request.setAttribute("modeNameEn", "jyxydzb.doc");
				}
			}

			// ����
			if ("3103".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õ�����ģ��.doc");
					request.setAttribute("modeNameEn", "2008gyxydpm.doc");
				} else {// ��Ӫ���õ�
					request.setAttribute("modeName", "��Ӫ���õ�����ģ��.doc");
					request.setAttribute("modeNameEn", "2008jyxydpm.doc");
				}
			}

			// ����
			if ("3104".equals(transferMode)) {
				if ("101".equals(blockType)) {
					// ��ҵ���õ�
					retPage = "makeGpIndustryFile";
					if("1".equals(commonInfo.getIsZulin())){
						request.setAttribute("modeName", "��ҵ���õ�(����)����ģ����ʱ��.doc");
						request.setAttribute("modeNameEn", "gp_industry_time_zl.doc");
					}else{
						request.setAttribute("modeName", "��ҵ���õع���ģ����ʱ��.doc");
						request.setAttribute("modeNameEn", "gp_industry_time.doc");
					}
				} else {
					// ��Ӫ���õ�
					retPage = "makeGpOperatingFile";
					if("1".equals(ifOnLine)){
						if("1".equals(commonInfo.getIsZulin())){
							request.setAttribute("modeName", "��Ӫ���õ�(����)����ģ����ʱ��.doc");
							request.setAttribute("modeNameEn", "gp_operating_time_zl.doc");
						}else{
							request.setAttribute("modeName", "��Ӫ���õع���ģ����ʱ��.doc");
							request.setAttribute("modeNameEn", "gp_operating_time.doc");
							
						}
					}else{
						request.setAttribute("modeName", "��Ӫ���õع���ģ��.doc");
						request.setAttribute("modeNameEn", "gp_operating.doc");
					}
				}
			}

			// �������Ϊ�������ù���
			if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {

			}// �������Ϊ�ݴ���ù���,��˳��ù���
			else {
				String fileName = tdscFileService.getFileName(appId);
				String fileUrl = appId + ".doc";
				request.setAttribute("fileName", fileName);
				request.setAttribute("fileUrl", fileUrl);
			}
		}

		// ���ͨ��appId������TdscBlockFileApp���в鵽��¼����˵���ó����ļ��Ѿ��ݴ��
		TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
		// TdscBlockFileApp temp = null;
		if (temp != null) {
			request.setAttribute("ifZancun", "1");
			request.setAttribute("fileName", temp.getFileUrl());
			request.setAttribute("oldAppId", appId);
			request.setAttribute("recordId", temp.getRecordId());
		} else {
			request.setAttribute("recordId", recordId);
		}

		Map returnMap = new HashMap();
		if (appId != null) {
			returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
		}
		request.setAttribute("opnninfo", returnMap);
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("appId", appId);
		request.setAttribute("statusId", statusId);

		// if (temp != null)
		// return mapping.findForward("viewFileInfo");

		return mapping.findForward(retPage);

		// ����˳����ļ�
		// if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId) || FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
		// if (temp != null)
		// return mapping.findForward("xiugai");
		// else {
		// // ����
		//
		// }
		// } else {
		// return mapping.findForward("viewFileInfo");
		// }
	}
	/**
	 * ��ȡ�־����ݼ��ϣ��־����ƺ͵�ַ��
	 * @param districtId
	 * @return
	 */
	private Map getDistrictMap(){
		HashMap map = new HashMap();
		ArrayList al = null;
		al = new ArrayList();
		al.add("��ɽ�־�");
		al.add("��������ɽ����ͤ��·2��");
		map.put("1", al);
		al = new ArrayList();
		al.add("��ɽ�־�");
		al.add("�����л�ɽ���Ļ�·16��");
		map.put("2", al);
		al = new ArrayList();
		al.add("�����־�");
		al.add("�����б���������·78��");
		map.put("3", al);
		al = new ArrayList();
		al.add("�����־�");
		al.add("������������ǰ·11��");
		map.put("4", al);
		al = new ArrayList();
		al.add("�ϳ��־�");
		al.add("�������Ļ�·199��");
		map.put("5", al);
		al = new ArrayList();
		al.add("�����־�");
		al.add("�����б��������·����԰һ��26��");
		map.put("6", al);
		al = new ArrayList();
		al.add("�簲�־�");
		al.add("�����г簲������·311�ų��Ĵ���9¥");
		map.put("7", al);
		al = new ArrayList();
		al.add("��Ϫ�־�");
		al.add("�������Ļ�·199��");
		map.put("8", al);
		return map;
	}

	
	
	/**
	 * �����ļ���ѯ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
		// TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		// ����ӳ��ǹ�������ҳ�淵�أ���bindObject��ѯ����������������ʾ�����еļ�¼
		String forwardType = request.getParameter("forwardType");

		String noticeNo = "";
		if (request.getParameter("noticeNo") != null && !"".equals(request.getParameter("noticeNo"))) {
			noticeNo = request.getParameter("noticeNo");
			noticeNo = StringUtil.GBKtoISO88591(noticeNo);
		}

		String noticeName = "";
		if (request.getParameter("noticeName") != null && !"".equals(request.getParameter("noticeName"))) {
			noticeName = request.getParameter("noticeName");
			noticeName = StringUtil.GBKtoISO88591(noticeName);
		}

		// String noticeId ="";
		// if(request.getParameter("noticeId") !=
		// null&&!"".equals(request.getParameter("noticeId")))
		// noticeId = request.getParameter("noticeId");

		if (!"1".equals(request.getAttribute("forwardType"))) {
			bindObject(condition, form);
			condition.setNoticeNo(noticeNo);
			condition.setNoticeName(noticeName);
		}
		if ("1".equals(forwardType)) {
			condition.setNoticeNo("");
			condition.setNoticeName("");
		}

		if (request.getParameter("currentPage") != null)
			condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

		// ����״̬ �ų�״̬Ϊ02�Ĺ���
		condition.setNoticeStatus("02");
		// ����ҳ������
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		if (condition.getUniteBlockName() != null && !"".equals(condition.getUniteBlockName()))
			condition.setUniteBlockName(StringUtil.GBKtoISO88591(condition.getUniteBlockName()));

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

		List noticeStatusList = new ArrayList();
		// ���Session�е�¼�û��Ĺ�����Ȩ����Ϣ�б�
		List nodeList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_WORK_FLOW);

		Map flowMap = new HashMap();
		for (int j = 0; j < nodeList.size(); j++) {
			String id_flow = (String) nodeList.get(j);
			flowMap.put(id_flow, nodeList.get(j));
		}
		// ���ò�ѯ�������������ݵ�Ȩ��
		if (flowMap.get(GlobalConstants.FLOW_ID_MAKE_FILE) != null) {
			noticeStatusList.add("00");
		}
		// ���ò�ѯ������ˡ����ݵ�Ȩ��
		if (flowMap.get(GlobalConstants.FLOW_ID_CHECK_FILE) != null) {
			noticeStatusList.add("01");
			// ����С���ˡ�Ȩ�ޣ����ѯ��������
			condition.setUserId(null);
		}
		condition.setNoticeStatusList(noticeStatusList);
		// �����ѯ�б�
		PageList NoticePageList = tdscNoticeService.findPageList(condition);

		// ����ؿ��ѯ�б� lz+ ���ؿ�nodeid��statusidȡ����Ϊ �����ļ���״̬
		List tdscBlockAppViewList = new ArrayList();
		List appList = null;
		if (NoticePageList != null) {
			appList = NoticePageList.getList();
		}

		if (appList != null && appList.size() > 0) {
			for (int i = 0; i < appList.size(); i++) {
				TdscNoticeApp app = (TdscNoticeApp) appList.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				// conditionBlock.setNoitceNo(app.getNoticeNo());
				conditionBlock.setNoticeId(app.getNoticeId());
				conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);

				// ���еؿ���Ϣ��
				tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);
				String noticeStatusId = "";
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int j = 0; j < tdscBlockAppViewList.size(); j++) {

						TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);

						// �ؿ���Ϣ�ڵ�status��Ϊ�����ļ�status
						noticeStatusId = blockapp.getStatusId();

						app.setNoticeStatusId(noticeStatusId);
						app.setPlanId(blockapp.getPlanId());

						// �õ�ÿ���ؿ鵱ǰ��״̬���������һ���ؿ���δ���Ƴ����ļ�����������˳��ù��桱��������ʾ��Ϣ
						// ����ļ�û�б�����ɣ�������ֵΪ false��ʹ�� RecordId �ֶδ��棻
						if (GlobalConstants.FLOW_ID_MAKE_FILE.equals(noticeStatusId)) {
							app.setRecordId("false");
							break;
						} else
							app.setRecordId("true");
					}
				}
				appList.set(i, app);
			}
		}
		// lz end
		request.setAttribute("NoticePageList", NoticePageList);

		if ("1".equals(forwardType) || "1".equals(request.getAttribute("forwardType"))) {
			condition.setNoticeNo("");
			condition.setNoticeName("");
			condition.setTradeNum("");
			condition.setTransferMode("");
			condition.setUniteBlockName("");
		} else {
			condition.setNoticeName(StringUtil.ISO88591toGBK(noticeName));
			condition.setNoticeNo(StringUtil.ISO88591toGBK(noticeNo));
			condition.setUniteBlockName(StringUtil.ISO88591toGBK(condition.getUniteBlockName()));
		}
		request.setAttribute("condition", condition);

		request.setAttribute("oldNoticeNo", "");

		return mapping.findForward("listnew");
	}

	public ActionForward queryChaYueList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeName = request.getParameter("noticeName");// ��ѯ��������

		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();

		bindObject(condition, form);
		if (condition.getLandLocation() != null && !"".equals(condition.getLandLocation())) {
			condition.setLandLocation(StringUtil.GBKtoISO88591(condition.getLandLocation()));
		}
		if (condition.getUniteBlockName() != null && !"".equals(condition.getUniteBlockName())) {
			condition.setUniteBlockName(StringUtil.GBKtoISO88591(condition.getUniteBlockName()));
		}
		if (condition.getTradeNum() != null && !"".equals(condition.getTradeNum())) {
			condition.setTradeNum(StringUtil.GBKtoISO88591(condition.getTradeNum()));
		}

		// condition.setIfReleased("1");
		condition.setNoticeName(StringUtils.trimToEmpty(noticeName));

		if (!StringUtils.isEmpty(request.getParameter("currentPage")))
			condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));

		// ����ҳ������
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// ��ѯ�б�
		PageList pageList = tdscNoticeService.findPageList(condition);

		if (pageList != null && pageList.getList().size() > 0) {
			// ����ҳ����Ҫ���ֶ� from tdscBlockAppView----strat
			List list = pageList.getList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					TdscNoticeApp app = (TdscNoticeApp) list.get(i);

					TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
					conditionBlock.setNoticeId(app.getNoticeId());

					// ���еؿ���Ϣ��
					List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
					String blockName = "";
					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
						for (int j = 0; j < tdscBlockAppViewList.size(); j++) {

							TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);

							blockName += blockapp.getBlockName() + ",";
							app.setTranManTel(blockapp.getTradeNum());// ��"���Ĺұ��"���ڴ��ֶ���
							app.setTranManAddr(blockapp.getTransferMode());// ��"���÷�ʽ"���ڴ��ֶ���
						}
						blockName = blockName.substring(0, blockName.length() - 1);
						app.setTranManName(blockName);// ���ؿ����ƴ��ڴ��ֶ���
					}

				}
			}
			// -----end
		}

		request.setAttribute("pageList", pageList);

		if (condition.getLandLocation() != null && !"".equals(condition.getLandLocation())) {
			condition.setLandLocation(StringUtil.ISO88591toGBK(condition.getLandLocation()));
		}
		if (condition.getUniteBlockName() != null && !"".equals(condition.getUniteBlockName())) {
			condition.setUniteBlockName(StringUtil.ISO88591toGBK(condition.getUniteBlockName()));
		}
		if (condition.getTradeNum() != null && !"".equals(condition.getTradeNum())) {
			condition.setTradeNum(StringUtil.ISO88591toGBK(condition.getTradeNum()));
		}
		request.setAttribute("condition", condition);

		// ת�������ļ������б�ҳ��
		return mapping.findForward("listnew_chayue");
	}

	// �ѷ����ĳ����ļ� ����
	public ActionForward fileCY(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeId = request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		String recordId = "";
		// �ж�TdscFileApp���е�recordIdֵ�Ƿ�Ϊ��
		if (null != noticeId) {
			TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
			tdscBlockFileApp = tdscFileService.getBlockFileAppById(noticeId);
			if (tdscBlockFileApp != null) {
				recordId = tdscBlockFileApp.getRecordId();
				if (recordId != null) {
					request.setAttribute("recordId", recordId);
				}
			}
		}
		request.setAttribute("type", "chayue");
		return mapping.findForward("listnew_notice_chayue");
	}

	// �����ļ� ����ύ
	public ActionForward toBlockPaiXu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ��ȡҳ�����
		TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		String noticeName = tdscNoticeForm.getNoticeName();
		String noticeNo = tdscNoticeForm.getNoticeNo();

		String createNoticeNo1 = (String) request.getParameter("createNoticeNo1");
		String createNoticeNo2 = (String) request.getParameter("createNoticeNo2");

		String planId = tdscNoticeForm.getPlanId();

		// planId ��Ӧ�ĵؿ��б�
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		List list = new ArrayList();
		if (planId != null && !"".equals(planId)) {
			condition.setPlanId(planId);
			list = commonQueryService.queryTdscBlockAppViewListWithoutNodeOrderByDistrictAndName(condition);
		}

		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		if (list != null && list.size() > 0) {
			tdscBlockAppView = (TdscBlockAppView) list.get(0);
		}

		if("1".equals(tdscBlockAppView.getIsZulin())){
			if ("102".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "���������" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";
			} else if ("101".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "���������" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";
			}
		}else{
			if ("102".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "��������" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";
			} else if ("101".equals(tdscBlockAppView.getBlockQuality())) {
				noticeNo = "��������" + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";
			}
		}
		

		request.setAttribute("noticeName", noticeName);
		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("createNoticeNo1", createNoticeNo1);
		request.setAttribute("createNoticeNo2", createNoticeNo2);
		request.setAttribute("planId", planId);
		request.setAttribute("tdscBlockAppViewList", list);

		return mapping.findForward("toBlockPaiXu");
	}

	/**
	 * ���빫��ģ���ϴ�����ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward entryUploadPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ��ȡҳ�����
		TdscFileForm tdscNoticeForm = (TdscFileForm) form;

		String createNoticeNoYear = "";
		String createNoticeNohao = "";

		String noticeName = tdscNoticeForm.getNoticeName();

		String createNoticeNo1 = request.getParameter("createNoticeNo1");
		String createNoticeNo2 = request.getParameter("createNoticeNo2");
		String noticeNo = tdscNoticeForm.getNoticeNo();
		String noticeId = tdscNoticeForm.getNoticeId();

		createNoticeNoYear = createNoticeNo1;
		createNoticeNohao = createNoticeNo2;

		String[] blockSelect = null;
		if (request.getParameterValues("blockSelect") != null) {
			blockSelect = request.getParameterValues("blockSelect");
		}

		// ����Ŵ���ؿ齻�ױ�
		if (blockSelect != null) {
			for (int i = 0; i < blockSelect.length; i++) {
				String blockId = blockSelect[i];
				if (blockId != null && !"".equals(blockId)) {
					TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);
					if (tdscBlockTranApp != null) {
						tdscBlockTranApp.setXuHao(String.valueOf(i + 1));
						tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
					}
				}
			}
		}

		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		// planId ��Ӧ�ĵؿ��б�
		List list = new ArrayList();
		if (tdscNoticeForm.getPlanId() != null && !"".equals(tdscNoticeForm.getPlanId())) {
			condition.setPlanId(tdscNoticeForm.getPlanId());
			condition.setOrderKey("xuHao");
			list = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		}

		List appIdList = new ArrayList();
		String appId = "";
		String transferMode = "";
		String blockId = "";
		String statusId = "";
		List blockidList = new ArrayList();// Ϊȡ�����еؿ�������ӵؿ�Ĳ���list
		List returnPartForOneList = new ArrayList();

		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List blockidListForOne = new ArrayList();// Ϊȡ�������ؿ��ӵؿ�Ĳ���list

				tdscBlockAppView = (TdscBlockAppView) list.get(i);
				appId = tdscBlockAppView.getAppId();
				if (StringUtils.isEmpty(noticeId)) {
					noticeId = tdscBlockAppView.getPlanId();
					request.setAttribute("planId", noticeId);
					System.out.println("noticeId===>" + noticeId);
				}
				transferMode = tdscBlockAppView.getTransferMode();
				statusId = tdscBlockAppView.getStatusId();
				blockId = tdscBlockAppView.getBlockId();

				appIdList.add(appId);
				blockidList.add(blockId);
				blockidListForOne.add(blockId);
				List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

				String blockCode = "";// �ؿ���
				String tdcrnx = "";// ���еؿ�������;
				String tdcrnxAll = "";// ���еؿ�������;+��������
				String volumeRate = "";// �滮�ݻ���
				String density = "";// �滮�����ܶ�
				String greeningRate = "";// �̵���
				BigDecimal blockArea = new BigDecimal(0);// �ؿ����

				for (int j = 0; j < partListForOne.size(); j++) {
					TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);

					// ���ظ��ĵؿ��ź͵ؿ����
					blockCode = tdscBidderAppService.getTdscBlockPartListInBlockCode(tdscBlockAppView.getBlockId());
					blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);
					// �滮�ݻ��ʣ��滮�����ܶȣ��̵���
					if (tdscBlockPartForOne.getVolumeRate() != null && !"".equals(tdscBlockPartForOne.getVolumeRate()))
						volumeRate += tdscBlockPartForOne.getBlockCode() + "�ݻ���" + tdscBlockPartForOne.getVolumeRate() + ";  ";
					if (tdscBlockPartForOne.getDensity() != null && !"".equals(tdscBlockPartForOne.getDensity()))
						density += tdscBlockPartForOne.getBlockCode() + "�滮�����ܶ�" + tdscBlockPartForOne.getDensity() + ";  ";
					if (tdscBlockPartForOne.getGreeningRate() != null && !"".equals(tdscBlockPartForOne.getGreeningRate()))
						greeningRate += tdscBlockPartForOne.getBlockCode() + "�̵���" + tdscBlockPartForOne.getGreeningRate() + ";  ";

					// �������еؿ�������;
					tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
					tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);

					tdscBlockAppView.setTotalLandArea(blockArea);
					tdscBlockAppView.setUnitebBlockCode(blockCode);

					// ȡ���������ʱ�¼�����ֶ� ����tranapp��
					blockId = tdscBlockPartForOne.getBlockId();
					TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);
					// �滮���Ҫ�㣬clob�ֶ�
					TdscEsClob tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
					if (tdscEsClob != null) {
						tdscBlockAppView.setBzGhsjyd(replaceRN(tdscEsClob.getClobContent()));
					}
					tdscBlockAppView.setBzBeizhu(replaceRN(tdscBlockTranApp.getBzBeizhu()));
					tdscBlockAppView.setBzCrjkjnyq(replaceRN(tdscBlockTranApp.getBzCrjkjnyq()));
					tdscBlockAppView.setBzDgjjgsj(replaceRN(tdscBlockTranApp.getBzDgjjgsj()));
					tdscBlockAppView.setBzDkqtsm(replaceRN(tdscBlockTranApp.getBzDkqtsm()));
					tdscBlockAppView.setBzGpsxf(replaceRN(tdscBlockTranApp.getBzGpsxf()));
					tdscBlockAppView.setBzJmzgjyq(replaceRN(tdscBlockTranApp.getBzJmzgjyq()));
					tdscBlockAppView.setBzPmyj(replaceRN(tdscBlockTranApp.getBzPmyj()));
					tdscBlockAppView.setBzSzpttj(replaceRN(tdscBlockTranApp.getBzSzpttj()));
					tdscBlockAppView.setBzTdcrfj(replaceRN(tdscBlockTranApp.getBzTdcrfj()));
					tdscBlockAppView.setBzTdjftj(replaceRN(tdscBlockTranApp.getBzTdjftj()));
					tdscBlockAppView.setBzXbjg(replaceRN(tdscBlockTranApp.getBzXbjg()));
				}

				// �������еؿ�������; ����RangeSouth�У����س������޴���RangeWest
				tdscBlockAppView.setRangeSouth(tdcrnx);
				tdscBlockAppView.setRangeWest(tdcrnxAll);

				tdscBlockAppView.setVolumeRate(volumeRate);
				tdscBlockAppView.setDensity(density);
				tdscBlockAppView.setGreeningRate(greeningRate);
				if (tdscBlockAppView.getMarginAmount() != null) {
					// ��λ����Ԫ
					BigDecimal marginAmount = tdscBlockAppView.getMarginAmount().multiply(new BigDecimal(10000));
					tdscBlockAppView.setMarginAmount(marginAmount);
				}

				returnPartForOneList.add(tdscBlockAppView);
			}
		}

		// Ϊ�����ļ����������Ϣ����ָ����ʽ����
		List returnPartList = new ArrayList();
		if (blockidList != null && blockidList.size() > 0) {
			returnPartList = tdscBlockInfoService.makeDataForFile(blockidList);
		}
		request.setAttribute("PartList", returnPartList);

		if ("3103".equals(transferMode)) {// ����
			request.setAttribute("modeNameEn", "pm_crwj_mode.doc");
		}
		if ("3104".equals(transferMode)) {// ����
			request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
		}

		request.setAttribute("pageList", returnPartForOneList);// ��ؿ���Ϣ��

		List districtList = tdscNoticeService.tidyDistrictList(list, tdscBlockAppView);
		request.setAttribute("districtList", districtList);
		request.setAttribute("tdscBlockAppView", tdscBlockAppView);
		request.setAttribute("appIdList", appIdList);
		request.setAttribute("noticeName", noticeName);
		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("createNoticeNo", createNoticeNoYear + createNoticeNohao);
		request.setAttribute("createNoticeNoYear", createNoticeNoYear);
		request.setAttribute("createNoticeNohao", createNoticeNohao);

		String fowardpage = "";
		if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId)) {
			fowardpage = "upLoadfileshenhe";
		} else {
			fowardpage = "upLoadfilePage";
		}
		return mapping.findForward(fowardpage);
	}

	// �����ļ� ����ύ
	public ActionForward addNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ��ȡҳ�����
		TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		String appIds[] = tdscNoticeForm.getAppIds();
		String noticeName = tdscNoticeForm.getNoticeName();
		String noticeNo = tdscNoticeForm.getNoticeNo();
		String recordId = request.getParameter("RecordID");
		String modeNameEn = request.getParameter("modeNameEn");

		String noticeStatus = request.getParameter("noticeStatus");
		String noticeId = request.getParameter("noticeId");
		String submitType = request.getParameter("submitType");

		// ȡ�ó����ļ���Ϣ
		// TdscNoticeApp noticeApp = new TdscNoticeApp();
		// noticeApp =
		// tdscNoticeService.findNoticeAppByNoticeId(tdscNoticeForm.getNoticeId());

		List list = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// ���÷�ʽ
		String transferMode = "";
		// ��ȡ�����ݵľ�����ID
		String userId = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				tdscBlockAppView = (TdscBlockAppView) list.get(0);
				transferMode = tdscBlockAppView.getTransferMode();
				if (StringUtils.isNotEmpty(tdscBlockAppView.getUserId())) {
					userId = tdscBlockAppView.getUserId();
				}
			}
		}

		// ������ύ���棬����Ҫ����ÿ��������Ϣ�Ĵ������
		if ("01".equals(noticeStatus)) {
			// ����û���Ϣ
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			if (user != null)
				user.setUserId(userId);
			// ���
			tdscNoticeService.saveNoticeOpnnshenhe(appIds, user, recordId, submitType, noticeNo, noticeName, noticeStatus, noticeId, modeNameEn, transferMode);
		}

		request.setAttribute("forwardType", "1");

		return mapping.findForward("successSave");
	}

	// �����ļ� �������޸�
	public ActionForward editNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		// ��������ļ���Ϣ
		TdscNoticeApp noticeApp = new TdscNoticeApp();
		noticeApp = tdscNoticeService.findNoticeAppByNoticeId(tdscNoticeForm.getNoticeId());
		request.setAttribute("noticeApp", noticeApp);

		String appIds[] = tdscNoticeForm.getAppIds();
		if (appIds != null && appIds.length > 0) {
			List tempAppIdsList = new ArrayList();
			for (int i = 0; i < appIds.length; i++) {
				tempAppIdsList.add(appIds[i]);
			}
			condition.setAppIdList(tempAppIdsList);
			List tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

			request.setAttribute("pageList", tdscBlockAppViewList);

			String noticeNo = tdscNoticeForm.getNoticeNo();
			noticeNo = noticeNo.substring(4, 8) + noticeNo.substring(10, 13);
			request.setAttribute("createNoticeNo", noticeNo);
			request.setAttribute("condition", null);
			return mapping.findForward("editNotice");
		}

		// ͨ��noticeId��ѯ�����ù�����Ϣ��һ��Ӧ��һ����¼TdscNoticeApp
		// �ٲ�ѯ�ؿ齻����Ϣ����noticeId�ͳ��ù�����Ϣ��noticeId��ͬ�ļ�¼TdscBlockTranApp
		// ��������Ϣ���ظ�ҳ��

		// ����������е���Ϣ
		String noticeId = request.getParameter("noticeId");
		TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		request.setAttribute("tdscNoticeApp", tdscNoticeApp);

		// ��ѯ��appIds
		List tdscBlockAppViewList = new ArrayList();

		condition.setNoitceNo(tdscNoticeApp.getNoticeNo());
		condition.setNoticeId(tdscNoticeApp.getNoticeId());
		condition.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);

		tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);
		// tdscNoticeService.findTdscBlockAppViewList(condition,
		// getOldNoticeNo);

		request.setAttribute("pageList", tdscBlockAppViewList);

		String createNoticeNo = "";
		createNoticeNo = StringUtils.trimToEmpty(noticeApp.getNoticeNo());
		if (createNoticeNo != null && createNoticeNo.length() > 14)
			createNoticeNo = createNoticeNo.substring(7, 11) + createNoticeNo.substring(12, 14);

		request.setAttribute("createNoticeNo", createNoticeNo);
		request.setAttribute("condition", condition);

		return mapping.findForward("editNotice");
	}

	/**
	 * �༭���ù���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward bianJiCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ͨ��noticeId��ѯ�����ù�����Ϣ��һ��Ӧ��һ����¼TdscNoticeApp
		// �ٲ�ѯ�ؿ齻����Ϣ����noticeId�ͳ��ù�����Ϣ��noticeId��ͬ�ļ�¼TdscBlockTranApp
		// ��������Ϣ���ظ�ҳ��

		// ����������е���Ϣ
		String noticeId = request.getParameter("noticeId");
		TdscNoticeApp tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		request.setAttribute("tdscNoticeApp", tdscNoticeApp);

		// ��ѯ��appIds
		List tdscBlockAppViewList = new ArrayList();
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoitceNo(tdscNoticeApp.getNoticeNo());
		condition.setNodeId(FlowConstants.FLOW_NODE_FILE_GET);
		tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(condition);

		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);

		// �����ݴ�ǰ��noticeNo
		request.setAttribute("oldNoticeNo", tdscNoticeApp.getNoticeNo());

		// �����ݴ�ǰ��noticeId
		request.setAttribute("oldNoticeId", tdscNoticeApp.getNoticeId());

		return mapping.findForward("bianji");
	}

	/**
	 * ɾ�������ļ�
	 * 
	 */
	public ActionForward delCrgg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String oldNoticeId = request.getParameter("oldNoticeId");
		List tranAppList = this.tdscNoticeService.findTdscBlockTranAppByNoticeNo(oldNoticeId, null);
		if(tranAppList!=null&&tranAppList.size()>0){
			TdscBlockTranApp tranApp= (TdscBlockTranApp)tranAppList.get(0);
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(tranApp.getPlanId());
			tdscBlockPlanTable.setBzBeizhu("CanDel");
			this.tdscScheduletableService.savePlanTableInfo(tdscBlockPlanTable);
		}

		tdscNoticeService.delCrgg(oldNoticeId);
		
		// ������ɳ��ù��棬forwardType=1����ѯҳ����Բ�bindObject��ѯ����
		request.setAttribute("forwardType", "1");

		// �����ݴ�ǰ��noticeNo
		request.setAttribute("oldNoticeNo", "");

		// �����ݴ�ǰ��noticeId
		request.setAttribute("oldNoticeId", "");

		return mapping.findForward("successSave");
	}

	/**
	 * 20080620�������ؿ�����ļ���˵���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward noticeshenhe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeName = request.getParameter("noticeName");
		String noticeId = request.getParameter("noticeId");
		String type = "";
		if (request.getParameter("type") != null && !"".equals(request.getParameter("type"))) {
			type = request.getParameter("type");
		}
		// ��������ļ���Ϣ
		// TdscNoticeApp noticeApp = new TdscNoticeApp();
		// noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		String noticeNo = request.getParameter("noticeNo");
		String createNoticeNo = "";

		// ���еؿ���Ϣ���ѯbynoticeNo��noticeId
		TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
		conditionBlock.setNoitceNo(noticeNo);
		conditionBlock.setNoticeId(noticeId);
		conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
		List tdscBlockAppViewList = new ArrayList();
		tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);

		List appIdList = new ArrayList();
		String appId = "";
		String planId = "";
		// String planId = "";

		// �ؿ�ѭ����ʼ------------
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				appId = blockapp.getAppId();
				planId = blockapp.getPlanId();
				appIdList.add(appId);
				// planId = blockapp.getPlanId();
				// ȡ�����
				Map returnMap = new HashMap();
				if (appId != null) {
					returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
					if (returnMap != null) {
						List opnnList = (List) returnMap.get("opnnList");
						if (opnnList != null && opnnList.size() > 0) {
							for (int p = 0; p < opnnList.size(); p++) {
								TdscOpnn tdscOpnn = (TdscOpnn) opnnList.get(p);
								if ("0103".equals(tdscOpnn.getActionId())) {
									opnnList.remove(tdscOpnn);
								}
							}
						}
						returnMap.put("opnnList", opnnList);
					}
				}
				request.setAttribute("opnninfo", returnMap);
			}
		}

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("noticeName", noticeName);
		request.setAttribute("appIdList", appIdList);
		request.setAttribute("pageList", tdscBlockAppViewList);
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("createNoticeNo", createNoticeNo);
		request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
		request.setAttribute("planId", planId);
		if (noticeNo != null && noticeNo.length() > 7)
			createNoticeNo = noticeNo.substring(7, 11) + noticeNo.substring(12, 14);

		String recordId = "";
		// �ж�TdscNoticeApp���е�recordIdֵ�Ƿ�Ϊ��
		if (null != noticeId) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				recordId = tdscNoticeApp.getRecordId();
				if (recordId != null) {
					request.setAttribute("recordId", recordId);
				}
			}
		}

		request.setAttribute("type", type);
		if (type != null && "chayue".equals(type))
			// ת�������ļ�����ҳ��
			return mapping.findForward("listnew_notice_chayue");
		else
			// ת�������ļ����ҳ��
			return mapping.findForward("noticeshenhe");
	}

	/**
	 * ��˳����ļ������� 2010-06-04 ���� by SMW
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward noticeShenHe_100604(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeId = request.getParameter("noticeId");
		// ��ѯ�ù����е����еؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		if (StringUtils.isNotEmpty(noticeId)) {
			condition.setNoticeId(noticeId);
			condition.setOrderKey("blockNoticeNo");
			// ����noticeId��ѯ
			List blockViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			request.setAttribute("blockViewList", blockViewList);

			// ��������ļ������ù�����Ϣ
			TdscNoticeApp noticeApp = (TdscNoticeApp) tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (StringUtils.isNotEmpty(noticeId)) {
				List blockTranList = this.tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, null);
				request.setAttribute("blockTranList", blockTranList);
			}
			TdscBlockFileApp fileApp = (TdscBlockFileApp) tdscFileService.getBlockFileAppById(noticeId);

			request.setAttribute("noticeApp", noticeApp);
			request.setAttribute("fileApp", fileApp);

			// �ؿ�ѭ����ʼ����ȡ�������
			String appId = "";
			if (blockViewList != null && blockViewList.size() > 0) {
				TdscBlockAppView blockapp = (TdscBlockAppView) blockViewList.get(0);
				request.setAttribute("planId", blockapp.getPlanId());
				request.setAttribute("statusId", blockapp.getStatusId());
				appId = blockapp.getAppId();
				// ȡ�����
				Map returnMap = new HashMap();
				if (appId != null) {
					returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);

					if (returnMap != null) {
						List tempList = (List) returnMap.get("opnnList");

						if (tempList != null && tempList.size() > 0) {
							for (int k = 0; k < tempList.size(); k++) {
								TdscOpnn tdscOpnn = (TdscOpnn) tempList.get(k);
								if ("0103".equals(tdscOpnn.getActionId())) {
									tempList.remove(tdscOpnn);
								}
							}
						}
						returnMap.put("opnnList", tempList);

					}
				}
				request.setAttribute("opnninfo", returnMap);
			}
		}

		return mapping.findForward("noticeShenHe_100604");
	}

	/**
	 * ��˳����ļ����ڣ��鿴�����ļ��ͳ��ù�����Ϣ ��ѯ�����ļ����߳��ù����recordId
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showWordFileById(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String noticeId = request.getParameter("noticeId");
		// ��ѯ�ļ����ͣ�1Ϊ�����ļ���2Ϊ���ù���
		String type = request.getParameter("type");
		String recordId = "";
		// �ж�TdscNoticeApp���е�recordIdֵ�Ƿ�Ϊ��
		if (StringUtils.isNotEmpty(noticeId)) {
			if ("1".equals(type)) {
				// �����ļ�
				TdscNoticeApp tdscNoticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(noticeId);
				if (tdscNoticeApp != null) {
					recordId = tdscNoticeApp.getRecordId();
					if (recordId != null) {
						request.setAttribute("recordId", recordId);
					}
				}
			} else {
				// ���ù���
				TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
				TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
				List list = tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, null);
				if (list != null && list.size() > 0) {
					tdscBlockTranApp = (TdscBlockTranApp) list.get(0);
				}
				tdscBlockFileApp = tdscFileService.getBlockFileAppById(tdscBlockTranApp.getPlanId());
				if (tdscBlockFileApp != null) {
					recordId = tdscBlockFileApp.getRecordId();
					if (recordId != null) {
						request.setAttribute("recordId", recordId);
					}
				}
			}
		}

		request.setAttribute("type", type);
		return mapping.findForward("showWordFile");
	}

	/**
	 * �����ļ���������������޸�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPreviewInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String nodeId = request.getParameter("nodeId");
		String statusId = request.getParameter("statusId");
		String recordId = "";

		if (appId != null) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// ��ѯ������Ϣ��(��֤���ֹʱ��)
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.findBlockTranAppInfo(appId);
			// ��ѯ���Ȱ��ű�
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableInfo(appId);

			request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
			request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);

			String blockType = "";
			blockType = commonInfo.getBlockType();
			String transferMode = "";
			transferMode = commonInfo.getTransferMode();
			// �б�
			if ("3107".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õ��б�ģ��.doc");
					request.setAttribute("modeNameEn", "gyxydzb.doc");
				} else {// ��Ӫ���õ�
					request.setAttribute("modeName", "��Ӫ���õ��б�ģ��.doc");
					request.setAttribute("modeNameEn", "jyxydzb.doc");
				}
			}

			// ����
			if ("3103".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õ�����ģ��.doc");
					request.setAttribute("modeNameEn", "2008gyxydpm.doc");
				} else {// ��Ӫ���õ�
					request.setAttribute("modeName", "��Ӫ���õ�����ģ��.doc");
					request.setAttribute("modeNameEn", "2008jyxydpm.doc");
				}
			}

			// ����
			if ("3104".equals(transferMode)) {
				// ��ҵ���õ�
				if ("101".equals(blockType)) {
					request.setAttribute("modeName", "��ҵ���õع���ģ��.doc");
					request.setAttribute("modeNameEn", "2008gyxydgp.doc");
				} else {// ��Ӫ���õ�
					request.setAttribute("modeName", "��Ӫ���õع���ģ��.doc");
					request.setAttribute("modeNameEn", "2008jyxydgp.doc");
				}
			}

			// �������Ϊ�������ù���
			if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {

			}// �������Ϊ�ݴ���ù���,��˳��ù���
			else {
				String fileName = tdscFileService.getFileName(appId);
				String fileUrl = appId + ".doc";
				request.setAttribute("fileName", fileName);
				request.setAttribute("fileUrl", fileUrl);
			}

		}
		// ���ͨ��appId������TdscBlockFileApp���в鵽��¼����˵���ó����ļ��Ѿ��ݴ��
		TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
		if (temp != null) {
			request.setAttribute("ifZancun", "1");
			request.setAttribute("fileName", temp.getFileUrl());
			request.setAttribute("oldAppId", appId);
			request.setAttribute("recordId", temp.getRecordId());
		}

		Map returnMap = new HashMap();
		if (appId != null) {
			returnMap = this.appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
		}
		request.setAttribute("opnninfo", returnMap);
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("appId", appId);
		request.setAttribute("statusId", statusId);
		// ����˳����ļ�
		if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId))
			return mapping.findForward("shenhe");
		// �˻����Ƴ����ļ�
		else if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
			return mapping.findForward("tuihuixiugai");
			// �����������ļ�
		} else if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
			if (temp != null)
				return mapping.findForward("xiugai");
			else {
				// ����
				request.setAttribute("recordId", recordId);
				return mapping.findForward("previewFileInfo");
			}
		}
		return null;
	}

	/**
	 * �����ļ���������������޸�-2009.5
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPreviewInfoNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// // ���ҳ�����appId
		// String nodeId = request.getParameter("nodeId");
		// String statusId = request.getParameter("statusId");
		// String noticeNo = request.getParameter("noticeNo");
		// String noticeId = request.getParameter("noticeId");
		// String modeNameEn = request.getParameter("modeNameEn");
		// String recordId = "";
		// String appId = "";
		//
		// //ȡ�ó����ļ���Ϣ
		// //TdscNoticeApp noticeApp = new TdscNoticeApp();
		// //noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		//
		// TdscFileForm tdscNoticeForm = (TdscFileForm) form;
		// String appIds[] = tdscNoticeForm.getAppIds();
		// String noticeName = tdscNoticeForm.getNoticeName();
		// String noticeStatus = request.getParameter("noticeStatus");
		//
		//
		// // ����ǰ��Ҫͨ��noticeIdɾ��ԭ�еļ�¼
		// //tdscNoticeService.saveNotice(appIds, noticeNo, noticeName,
		// noticeStatus, noticeId, modeNameEn, recordId);
		//
		//
		// if (appId != null) {
		// // ��ò�ѯ����
		// TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		// baseCondition.setAppId(appId);
		// // ���ù�������ȡ�ù�����Ϣ
		// TdscBlockAppView commonInfo =
		// commonQueryService.getTdscBlockAppView(baseCondition);
		// if (commonInfo != null) {
		// request.setAttribute("commonInfo", commonInfo);
		// }
		// //��ѯ������Ϣ��(��֤���ֹʱ��)
		// TdscBlockTranApp tdscBlockTranApp =
		// (TdscBlockTranApp)tdscScheduletableService.findBlockTranAppInfo(appId);
		// //��ѯ���Ȱ��ű�
		// TdscBlockPlanTable tdscBlockPlanTable =
		// (TdscBlockPlanTable)tdscScheduletableService.findPlanTableInfo(appId);
		//
		//
		// request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		// request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		//
		// //String blockType = commonInfo.getBlockType();
		// String transferMode = "";
		// transferMode = commonInfo.getTransferMode();
		//
		// // ����
		// if ("3103".equals(transferMode)) {
		// request.setAttribute("modeName", "����ģ��.doc");
		// request.setAttribute("modeNameEn", "pm_crwj_mode.doc");
		// }
		//
		// // ����
		// if ("3104".equals(transferMode)) {
		// request.setAttribute("modeName", "����ģ��.doc");
		// request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
		// }
		//
		// // �������Ϊ�������ù���
		// if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
		//
		// }// �������Ϊ�ݴ���ù���,��˳��ù���
		// else {
		// String fileName = tdscFileService.getFileName(appId);
		// String fileUrl = appId + ".doc";
		// request.setAttribute("fileName", fileName);
		// request.setAttribute("fileUrl", fileUrl);
		// }
		//
		// }
		// // ���ͨ��appId������TdscBlockFileApp���в鵽��¼����˵���ó����ļ��Ѿ��ݴ��
		// TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
		// if (temp != null) {
		// request.setAttribute("ifZancun", "1");
		// request.setAttribute("fileName", temp.getFileUrl());
		// request.setAttribute("oldAppId", appId);
		// request.setAttribute("recordId",temp.getRecordId());
		// }
		//
		// Map returnMap = new HashMap();
		// if (appId != null) {
		// returnMap = this.appFlowService.queryOpnnInfo(appId,
		// FlowConstants.FLOW_NODE_FILE_MAKE);
		// }
		// request.setAttribute("opnninfo", returnMap);
		// request.setAttribute("nodeId", nodeId);
		// request.setAttribute("appId", appId);
		// request.setAttribute("statusId", statusId);
		// //����˳����ļ�
		// if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId))
		// return mapping.findForward("shenhe");
		// //�˻����Ƴ����ļ�
		// else if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
		// return mapping.findForward("tuihuixiugai");
		// //�����������ļ�
		// } else if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
		// if (temp != null)
		// return mapping.findForward("xiugai");
		// else{
		// //����
		// request.setAttribute("recordId", recordId);
		// return mapping.findForward("previewFileInfo");
		// }
		// }
		return null;
	}

	/**
	 * ��ѯ�����б�
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
	public ActionForward queryAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return mapping.findForward("list");
	}

	/**
	 * �ϴ��ļ���ͬʱд������ļ���Ϣ��
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
	// public ActionForward upLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	//
	// String statusId = request.getParameter("statusId");
	// String appId = request.getParameter("appId");
	// String modeNameEn = request.getParameter("modeNameEn");
	// String recordId = request.getParameter("RecordID");
	// // ����û���Ϣ
	// SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
	//
	// TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
	// // ȡ��ϵͳ��ǰʱ��
	// Date date = new Date();
	//
	// // ���ͨ��appId������TdscBlockFileApp���в鵽��¼����˵���ó����ļ��Ѿ��ݴ��
	// TdscBlockFileApp temp = tdscFileService.getBlockFileAppById(appId);
	// if (temp != null && !FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId) && !"2".equals(request.getParameter("submitType"))) {
	// temp.setFilePerson(String.valueOf(user.getUserId()));
	// temp.setFileUrl(modeNameEn);
	// request.setAttribute("ifZancun", "1");
	// temp.setFileDate(date);
	// temp.setFileId(appId);
	// temp.setRecordId(recordId);
	// tdscFileService.update(temp);
	// request.setAttribute("fileName", modeNameEn);
	// request.setAttribute("oldAppId", appId);
	// request.setAttribute("recordId", temp.getRecordId());
	// } else {
	// tdscBlockFileApp.setFilePerson(String.valueOf(user.getUserId()));
	// tdscBlockFileApp.setFileDate(date);
	// tdscBlockFileApp.setFileUrl(modeNameEn);
	// tdscBlockFileApp.setFileId(appId);
	// tdscBlockFileApp.setRecordId(recordId);
	// request.setAttribute("recordId", recordId);
	//
	// // �����ļ��ϴ���ϢTDSC_BLOCK_FILE_APP
	// if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
	// tdscFileService.update(tdscBlockFileApp);
	// } else if (FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
	// if (temp != null)
	// tdscFileService.update(tdscBlockFileApp);
	// else
	// tdscFileService.save(tdscBlockFileApp);
	// request.setAttribute("fileName", modeNameEn);
	// request.setAttribute("oldAppId", appId);
	// }
	// }
	//
	//
	//
	// // ��ò�ѯ����
	// TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
	// baseCondition.setAppId(appId);
	// // ���ù�������ȡ�ù�����Ϣ
	// TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
	// request.setAttribute("commonInfo", commonInfo);
	// String blockType = "";
	// blockType = commonInfo.getBlockType();
	// String transferMode = "";
	// transferMode = commonInfo.getTransferMode();
	// // �б�
	// if ("3107".equals(transferMode)) {
	// // ��ҵ���õ�
	// if ("101".equals(blockType)) {
	// request.setAttribute("modeName", "��ҵ���õ��б�ģ��.doc");
	// request.setAttribute("modeNameEn", "gyxydzb.doc");
	// } else {// ��Ӫ���õ�
	// request.setAttribute("modeName", "��Ӫ���õ��б�ģ��.doc");
	// request.setAttribute("modeNameEn", "jyxydzb.doc");
	// }
	// }
	//
	// // ����
	// if ("3103".equals(transferMode)) {
	// // ��ҵ���õ�
	// if ("101".equals(blockType)) {
	// request.setAttribute("modeName", "��ҵ���õ�����ģ��.doc");
	// request.setAttribute("modeNameEn", "gyxydpm.doc");
	// } else {// ��Ӫ���õ�
	// request.setAttribute("modeName", "��Ӫ���õ�����ģ��.doc");
	// request.setAttribute("modeNameEn", "jyxydpm.doc");
	// }
	// }
	//
	// // ����
	// if ("3104".equals(transferMode)) {
	// // ��ҵ���õ�
	// if ("101".equals(blockType)) {
	// request.setAttribute("modeName", "��ҵ���õع���ģ��.doc");
	// request.setAttribute("modeNameEn", "gyxydgp.doc");
	// } else {// ��Ӫ���õ�
	// request.setAttribute("modeName", "��Ӫ���õع���ģ��.doc");
	// request.setAttribute("modeNameEn", "jyxydgp.doc");
	// }
	// }
	//
	// request.setAttribute("currentPage", "0");
	// if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId) || FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
	// try {
	// if ("1".equals(request.getParameter("submitType"))) {
	// this.appFlowService.tempSaveOpnn(appId, transferMode, user);
	// } else if ("2".equals(request.getParameter("submitType"))) {
	// this.appFlowService.saveOpnn(appId, transferMode, user);
	// }
	// request.setAttribute("saveMessage", "����ɹ�");
	// } catch (Exception e) {
	// e.printStackTrace();
	// request.setAttribute("saveMessage", "����ʧ��");
	// }
	// }
	//
	// if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId)) {
	// TdscAppFlow appFlow = new TdscAppFlow();
	// appFlow.setAppId(appId);
	// appFlow.setResultId(request.getParameter("resultId"));
	// appFlow.setTextOpen(request.getParameter("textOpen"));
	// appFlow.setTransferMode(transferMode);
	// appFlow.setUser(user);
	// try {
	// if ("1".equals(request.getParameter("submitType"))) {
	// this.appFlowService.tempSaveOpnn(appFlow);
	// // ��ѯ��ʷ���
	// Map opnnMap = appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
	// request.setAttribute("opnninfo", opnnMap);
	// } else if ("2".equals(request.getParameter("submitType"))) {
	// this.appFlowService.saveOpnn(appFlow);
	// }
	// request.setAttribute("oldAppId", appId);
	// request.setAttribute("saveMessage", "����ɹ�");
	// } catch (Exception e) {
	// e.printStackTrace();
	// request.setAttribute("saveMessage", "����ʧ��");
	// }
	// request.setAttribute("fileName", temp.getFileUrl());
	// if ("1".equals(request.getParameter("submitType")))
	// return mapping.findForward("shenhe");
	// }
	// // ���ص��б�ҳ�棬���ݴ棬��Ϊ�ύ
	// if ("1".equals(request.getParameter("submitType")) && FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId)) {
	// Map opnnMap = appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);
	// request.setAttribute("opnninfo", opnnMap);
	// return mapping.findForward("tuihuixiugai");
	// } else if ("1".equals(request.getParameter("submitType")))
	// return mapping.findForward("xiugai");
	// else
	// return new ActionForward("file.do?method=queryAppFileListWithNodeId", true);
	// }

	public ActionForward upLoadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// String statusId = request.getParameter("statusId");
		String appId = request.getParameter("appId") + "";
		String modeNameEn = request.getParameter("modeNameEn");
		String recordId = request.getParameter("RecordID");

		// ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ȡ��ϵͳ��ǰʱ��
		Date date = new Date();

		// ���ͨ��appId������TdscBlockFileApp���в鵽��¼����˵���ó����ļ��Ѿ��ݴ��
		TdscBlockFileApp tdscBlockFileOld = null;
		tdscBlockFileOld = tdscFileService.getBlockFileAppById(appId);

		if (tdscBlockFileOld != null) {
			tdscBlockFileOld.setFilePerson(user.getUserId());
			tdscBlockFileOld.setFileDate(new Date(System.currentTimeMillis()));
			// ˵���Ѿ��������update��
			tdscFileService.update(tdscBlockFileOld);
		} else {
			TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
			tdscBlockFileApp.setRecordId(recordId);
			tdscBlockFileApp.setFileId(appId);
			tdscBlockFileApp.setFileUrl(modeNameEn);// ���ļ����� �����ڸ�����ַ��
			tdscBlockFileApp.setFilePerson(user.getUserId());
			tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
			// ��������ļ���Ϣ TDSC_BLOCK_FILE_APP
			tdscFileService.save(tdscBlockFileApp);
		}

		// tdscBlockFileApp.setFilePerson(String.valueOf(user.getUserId()));
		// tdscBlockFileApp.setFileDate(date);
		// tdscBlockFileApp.setFileUrl(modeNameEn);
		// tdscBlockFileApp.setFileId(appId);
		// tdscBlockFileApp.setRecordId(recordId);
		request.setAttribute("recordId", recordId);

		// tdscFileService.save(tdscBlockFileApp);

		request.setAttribute("saveMessage", "����ɹ�");

		return new ActionForward("file.do?method=queryAppFileListWithNodeId", true);
	}

	/**
	 * �ϴ��ļ���ͬʱд������ļ���Ϣ��
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
	public ActionForward upLoadFileNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscFileForm tdscNoticeForm = (TdscFileForm) form;

		String appIds[] = tdscNoticeForm.getAppIds();
		String noticeId = request.getParameter("noticeId");
		String getmodeNameEn = request.getParameter("fileName");
		String noticeNo = request.getParameter("noticeNo");
		String noticeName = request.getParameter("noticeName");
		// String statusId = request.getParameter("statusId");
		String modeNameEn = request.getParameter("modeNameEn");
		String recordId = request.getParameter("RecordID");
		String submitType = request.getParameter("submitType");// 11���������棬12�������ύ��21����˱��棬22������ύ��23����˻���
		String textOpen = request.getParameter("textOpen");// �������

		request.setAttribute("fileName", modeNameEn);
		request.setAttribute("recordId", recordId);

		String appId = "";
		String transferMode = "";
		String planId = request.getParameter("planId");
		planId = StringUtils.trimToEmpty(planId);
		String landLocation = "";

		// TdscNoticeApp noticeApp = new TdscNoticeApp();
		// if(noticeId!=null&&!"".equals(noticeId))
		// noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);

		// ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ���еؿ���Ϣ��
		List tdscBlockAppViewList = new ArrayList();
		// ���þ�����ID
		String userId = "";
		if (noticeNo != null && !"".equals(noticeNo) && noticeId != null && !"".equals(noticeId)) {
			TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
			conditionBlock.setPlanId(planId);
			conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);
		} else {
			tdscBlockAppViewList = tdscNoticeService.queryTdscBlockAppViewByappIds(appIds);
		}
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				if (StringUtils.isNotEmpty(blockapp.getUserId())) {
					userId = blockapp.getUserId();
				}
				// �ؿ���Ϣ�ڵ�status��Ϊ�����ļ�status
				// statusId = blockapp.getStatusId();
				appId = blockapp.getAppId();
				transferMode = blockapp.getTransferMode();
				planId = blockapp.getPlanId();
				if (blockapp.getLandLocation() != null && !"".equals(blockapp.getLandLocation()))
					landLocation += blockapp.getLandLocation() + ",";

				// ����
				if ("3103".equals(transferMode)) {
					request.setAttribute("modeName", "�ൺ��������ģ��.doc");
					request.setAttribute("modeNameEn", "pm_crwj_mode.doc");
				}
				// ����
				if ("3104".equals(transferMode)) {
					request.setAttribute("modeName", "�ൺ���ع���ģ��.doc");
					request.setAttribute("modeNameEn", "gp_crwj_mode.doc");
				}

				// ��ѯ��ʷ���
				Map opnnMap = appFlowService.queryOpnnInfo(appId, FlowConstants.FLOW_NODE_FILE_MAKE);

				request.setAttribute("commonInfo", blockapp);
				request.setAttribute("opnninfo", opnnMap);
				request.setAttribute("oldAppId", appId);
			}
		}

		if (landLocation != null && !"".equals(landLocation))
			landLocation = landLocation.substring(0, landLocation.length() - 1);

		try {
			TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
			if (planId != null && !"".equals(planId)){
				tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);
			}
			// tdscFileService.saveAndUpdate(noticeId, user.getUserId(),
			// recordId, getmodeNameEn, null);
			tdscNoticeService.saveNoticeLz(userId, appIds, noticeNo, noticeName, noticeId, planId, modeNameEn, recordId, user, submitType, textOpen, tdscBlockPlanTable,
					landLocation);
			request.setAttribute("saveMessage", "����ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("saveMessage", "����ʧ��");
		}

		request.setAttribute("currentPage", "0");
		request.setAttribute("forwardType", "1");

		return mapping.findForward("gobackList");
	}

	/**
	 * �õ�ģ����ϸ��Ϣ ����ģ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modelDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String blockType = "";
		String transferMode = "";

		if (appId != null) {
			// ��ò�ѯ����
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(condition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
				blockType = commonInfo.getBlockType();
				transferMode = commonInfo.getTransferMode();
			}
		}
		// �б�
		if ("3107".equals(transferMode)) {
			// ��ҵ���õ�
			if ("101".equals(blockType)) {
				return mapping.findForward("gyZB");
			} else {// ��Ӫ���õ�
				return mapping.findForward("jyxZB");
			}
		}

		// ����
		if ("3103".equals(transferMode)) {
			// ��ҵ���õ�
			if ("101".equals(blockType)) {
				return mapping.findForward("gyPM");
			} else {// ��Ӫ���õ�
				return mapping.findForward("jyxPM");
			}
		}

		// ����
		if ("3104".equals(transferMode)) {
			// ��ҵ���õ�
			if ("101".equals(blockType)) {
				return mapping.findForward("gyGP");
			} else {// ��Ӫ���õ�
				return mapping.findForward("jyxGP");
			}
		}
		return null;
	}

	/**
	 * ��ӡ������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// noticeId ��ʵ��ΪplanId���ڽ����������桱��ǰ���롰���������ļ���һ��ʱ�޸ģ�2010-5-3 smw����
		String noticeId = (String) request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		String printType = (String) request.getParameter("printType");
		String type = (String) request.getParameter("type");
		String planId = request.getParameter("planId");
		request.setAttribute("noticeName", request.getParameter("noticeName"));

		String createNoticeNo1 = request.getParameter("createNoticeNo1");
		String createNoticeNo2 = request.getParameter("createNoticeNo2");
		String getNoticeNo = request.getParameter("getnoticeNo");
		request.setAttribute("createNoticeNo1", createNoticeNo1);
		request.setAttribute("createNoticeNo2", createNoticeNo2);
		TdscBlockPlanTable blockPlanTable = this.tdscScheduletableService.findPlanTableByPlanId(planId);
		request.setAttribute("blockPlanTable", blockPlanTable);
		
		String marginStartDate ="";
		String marginEndDate ="";
		if(blockPlanTable.getAccAppStatDate()!=null){
			marginStartDate = DateUtil.date2String(blockPlanTable.getAccAppStatDate(),"yyyy��M��d�� HHʱmm��");
		}
		String strBlockQuality = "";

		String noticeNo = "";
		// ȡ�ó����ļ���Ϣ
		TdscNoticeApp noticeApp = new TdscNoticeApp();

		noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		if (noticeApp != null && !"".equals(noticeApp.getNoticeNo())) {
			noticeNo = noticeApp.getNoticeNo();
		} else {
			// TdscFileForm tdscFileForm = (TdscFileForm)form;
			String noticeNum = request.getParameter("noticeNum");
			if (StringUtils.isNotEmpty(noticeNum))
				noticeNo = "�����ʷ�����[" + noticeNum + "��";
			else
				noticeNo = request.getParameter("noticeNo");
		}

		request.setAttribute("noticeNo", noticeNo);

		// Ĭ�ϳ��÷�ʽΪ����
		String transferMode = GlobalConstants.DIC_TRANSFER_AUCTION;

		String strNoPurContext = "";

		if (noticeId != null && !"".equals(noticeId)) {
			// ����noticeId��ѯblockIdList(noticeId ��ʵ��ΪplanId)
			// planId =
			// StringUtils.isEmpty(planId)?noticeId:StringUtils.trimToEmpty(planId);
			// List blockIdList =
			// (List)tdscScheduletableService.queryBlockIdListByPlanId(planId);

			String[] blockSelect = null;
			List blockIdList = new ArrayList();
			if (request.getParameterValues("blockSelect") != null) {
				blockSelect = request.getParameterValues("blockSelect");
			}

			if (blockSelect == null) {
				// blockIdList =
				// (List)tdscScheduletableService.queryBlockIdListByPlanId(planId);
				blockIdList = (List) tdscScheduletableService.queryBlockIdListByNoticeId(noticeId);
			} else {
				blockIdList = Arrays.asList(blockSelect);
			}

			// Ϊ�����ļ����������Ϣ����ָ����ʽ����
			List tdscBlockPartList = new ArrayList();
			if (blockIdList != null && blockIdList.size() > 0) {
				tdscBlockPartList = tdscBlockInfoService.makeDataForFile(blockIdList);
			}
			request.setAttribute("tdscBlockPartList", tdscBlockPartList);

			// �������ʸ�Ҫ��
			TdscBaseQueryCondition viewcondition = new TdscBaseQueryCondition();
			List blockTranAppList = new ArrayList();
			List appIdsList = new ArrayList();
			String blockQuality = "";
			String appId = "";
			for (int i = 0; i < blockIdList.size(); i++) {
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp((String) blockIdList.get(i));
				if("4".equals(tdscBlockInfo.getDistrictId().toString())){//��������
					request.setAttribute("regCapital", StringUtils.trimToEmpty(tdscBlockInfo.getRegCapital()));//������ҵ�ؿ���ҵע���ʱ�
				}
				strBlockQuality = tdscBlockInfo.getBlockQuality();
				if ("101".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "��";
				} else if ("102".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "��";
				}
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo((String) blockIdList.get(i));
				if (tdscBlockInfo != null && tdscBlockTranApp != null) {
					tdscBlockTranApp.setXuHao(String.valueOf(i + 1));
					tdscBlockInfoService.updateTdscBlockTranApp(tdscBlockTranApp);
					// ���ؿ����� ��ʱ������ SpecialPromise �ֶ���
					tdscBlockTranApp.setSpecialPromise(tdscBlockInfo.getBlockName() + "");
					transferMode = tdscBlockTranApp.getTransferMode();
					appId = tdscBlockTranApp.getAppId();
					appIdsList.add(appId);
				}
				blockTranAppList.add(tdscBlockTranApp);
			}

			if (appIdsList != null && appIdsList.size() > 0) {

				request.setAttribute("appIdList", appIdsList);
				// lz+ ����part�ؿ���Ϣ
				viewcondition.setAppIdList(appIdsList);
				// viewcondition.setOrderKey("blockNoticeNo");
				viewcondition.setOrderKey("xuHao");

				List list = commonQueryService.queryTdscBlockAppViewListWithoutNode(viewcondition);

				String blockId = "";
				List returnPartForOneList = new ArrayList();
				TdscBlockAppView BlockAppView = new TdscBlockAppView();

				// String max = this.tdscBlockInfoService.getMaxNoticeNo();
				String tempStr = noticeNo.substring(1, 2);
				String blockNoticeNoPrefix = "";
				String max ="";
				if(noticeNo.indexOf("��") != -1){
					if ("��".equals(tempStr)) {
						blockNoticeNoPrefix = "�����������⣩" + noticeNo.substring(6, 10) + "-";
					} else if ("��".equals(tempStr)) {
						blockNoticeNoPrefix = "�����������⣩" + noticeNo.substring(6, 10) + "-";
					}
					max = this.tdscBlockInfoService.getMaxNoticeNoBlockNoticeNoPrefix("20",blockNoticeNoPrefix);
				}else{
					if ("��".equals(tempStr)) {
						blockNoticeNoPrefix = "������������" + noticeNo.substring(5, 9) + "-";
					} else if ("��".equals(tempStr)) {
						blockNoticeNoPrefix = "������������" + noticeNo.substring(5, 9) + "-";
					}
					max = this.tdscBlockInfoService.getMaxNoticeNoBlockNoticeNoPrefix("18",blockNoticeNoPrefix);
				}
				

				
				int maxValue = Integer.parseInt(max);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						List blockidListForOne = new ArrayList();// Ϊȡ�������ؿ��ӵؿ�Ĳ���list
						BlockAppView = (TdscBlockAppView) list.get(i);
						
						if(BlockAppView.getMarginEndDate()!=null){
							marginEndDate =DateUtil.timestamp2String( BlockAppView.getMarginEndDate(),"yyyy��M��d�� HHʱmm��");
						}
						
						blockId = BlockAppView.getBlockId();
						TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);
						blockidListForOne.add(blockId);
						List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

						String blockCode = "";
						String tdcrnx = "";
						String tdcrnxAll = "";
						BigDecimal blockArea = new BigDecimal(0.00);
						StringBuffer landUseTypes = new StringBuffer();
						StringBuffer transferLifes = new StringBuffer();
						StringBuffer volumeRates = new StringBuffer();
						StringBuffer densitys = new StringBuffer();
						StringBuffer greeningRates = new StringBuffer();

						for (int j = 0; j < partListForOne.size(); j++) {
							TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);
							landUseTypes.append(tdscBlockPartForOne.getLandUseType());
							transferLifes.append(tdscBlockPartForOne.getTransferLife());
							
							//����ݻ����ǽ��ڣ���ƴ�ӳ� a���ݻ��ʣ�b������ƴ�ӳɷ���+a
//							if(tdscBlockPartForOne.getVolumeRateSign() != null && "06".equals(tdscBlockPartForOne.getVolumeRateSign())){
//								volumeRates.append(tdscBlockPartForOne.getVolumeRate()).append("��").append("�ݻ���").append("��").append(tdscBlockPartForOne.getVolumeRate2()).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRateMemo()));
//							}else if(tdscBlockPartForOne.getVolumeRateSign() != null){
//								volumeRates.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,tdscBlockPartForOne.getVolumeRateSign())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRate())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRateMemo()));
//							}else{
//								volumeRates.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRate())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getVolumeRateMemo()));
//							}
							volumeRates.append(tdscBlockPartForOne.getVolumeRateMemo());
							
							//����滮�����ܶ��ǽ��ڣ���ƴ�ӳ� a%�������ܶȣ�b%������ƴ�ӳɷ���+a%
//							if(tdscBlockPartForOne.getDensitySign() != null && "06".equals(tdscBlockPartForOne.getDensitySign())){
//								densitys.append(tdscBlockPartForOne.getDensity()).append("%��").append("�����ܶ�").append("��").append(tdscBlockPartForOne.getDensity2()).append("%");
//							}else if(tdscBlockPartForOne.getDensitySign() != null){
//								densitys.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,tdscBlockPartForOne.getDensitySign())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getDensity())).append("%");
//							}else{
//								densitys.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getDensity())).append("%");
//							}
							densitys.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getDensity()));
							//����ݻ����ǽ��ڣ���ƴ�ӳ� a%���ݻ��ʣ�b%������ƴ�ӳɷ���+a%
//							if(tdscBlockPartForOne.getGreeningRateSign() != null && "06".equals(tdscBlockPartForOne.getGreeningRateSign())){
//								greeningRates.append(tdscBlockPartForOne.getGreeningRate()).append("%��").append("�̵���").append("��").append(tdscBlockPartForOne.getGreeningRate2()).append("%");
//							}else if(tdscBlockPartForOne.getGreeningRateSign() != null){
//								greeningRates.append(DicDataUtil.getInstance().getDicItemName(GlobalConstants.DIC_MATHEMATICS_SIGN,tdscBlockPartForOne.getGreeningRateSign())).append(StringUtils.trimToEmpty(tdscBlockPartForOne.getGreeningRate())).append("%");
//							}else{
//								greeningRates.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getGreeningRate())).append("%");
//							}
							greeningRates.append(StringUtils.trimToEmpty(tdscBlockPartForOne.getGreeningRate()));

							if (j + 1 != partListForOne.size()) {
								landUseTypes.append(",");
								transferLifes.append(",");
								volumeRates.append(",");
								densitys.append(",");
								greeningRates.append(",");
							}
							if (j == 0) {
								blockCode = tdscBlockPartForOne.getBlockCode();
							}
							if (j > 0) {
								blockCode += "��" + tdscBlockPartForOne.getBlockCode();
							}
							blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);

							// �������еؿ�������;
							tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
							tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);

							BlockAppView.setTotalLandArea(blockArea);
							BlockAppView.setUnitebBlockCode(blockCode);

							// ȡ���������ʱ�¼�����ֶ� ����tranapp��
							blockId = tdscBlockPartForOne.getBlockId();
							TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);

							BlockAppView.setBzBeizhu(tdscBlockTranApp.getBzBeizhu());
							BlockAppView.setBzCrjkjnyq(tdscBlockTranApp.getBzCrjkjnyq());
							BlockAppView.setBzDgjjgsj(tdscBlockTranApp.getBzDgjjgsj());
							BlockAppView.setBzDkqtsm(tdscBlockTranApp.getBzDkqtsm());
							BlockAppView.setBzGhsjyd(tdscBlockTranApp.getBzGhsjyd());
							BlockAppView.setBzGpsxf(tdscBlockTranApp.getBzGpsxf());
							BlockAppView.setBzJmzgjyq(tdscBlockTranApp.getBzJmzgjyq());
							BlockAppView.setBzPmyj(tdscBlockTranApp.getBzPmyj());
							BlockAppView.setBzSzpttj(tdscBlockTranApp.getBzSzpttj());
							BlockAppView.setBzTdcrfj(tdscBlockTranApp.getBzTdcrfj());
							BlockAppView.setBzTdjftj(tdscBlockTranApp.getBzTdjftj());
							BlockAppView.setBzXbjg(tdscBlockTranApp.getBzXbjg());
							BlockAppView.setIsPurposeBlock(tdscBlockTranApp.getIsPurposeBlock());
							// BlockAppView.setBzXbjg(replaceRN(tdscBlockTranApp.getBzXbjg()));
						}

						max = String.valueOf((maxValue + 1));
						String blockNoticeNo = blockNoticeNoPrefix + max;
						maxValue++;

						if ("0".equals(BlockAppView.getIsPurposeBlock())) {
							strNoPurContext += blockNoticeNo + "��";
						}

						BlockAppView.setBlockNoticeNo(blockNoticeNo);
						BlockAppView.setLandUseTypes(landUseTypes.toString());
						BlockAppView.setTransferLifes(transferLifes.toString());
						BlockAppView.setVolumeRates(volumeRates.toString());
						BlockAppView.setDensitys(densitys.toString());
						BlockAppView.setGreeningRates(greeningRates.toString());
						BlockAppView.setBuildingHeight(tdscBlockInfo.getBuildingHeight());
						BlockAppView.setSellYear(tdscBlockInfo.getSellYear());
						BlockAppView.setOpeningMeetingNo(tdscBlockInfo.getBlockInvestAmount());//Ͷ��ǿ��
						// �������еؿ�������; ����RangeSouth�У����س������޴���RangeWest
						BlockAppView.setRangeSouth(tdcrnx);
						BlockAppView.setRangeWest(tdcrnxAll);
						returnPartForOneList.add(BlockAppView);
						request.setAttribute("tdscBlockAppView", BlockAppView);
					}
				}
				request.setAttribute("pageList", returnPartForOneList);// ��ؿ���Ϣ��
				// lz end
			}
			request.setAttribute("marginStartDate", marginStartDate);
			request.setAttribute("marginEndDate", marginEndDate);
			if ("true".equals(getNoticeNo)) {
				if(noticeNo.indexOf("��") != -1){
					getNoticeNo = "������" + blockQuality + "����[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";
				}else{
					getNoticeNo = "������" + blockQuality + "����[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";
				}
				request.setAttribute("noticeNo", getNoticeNo);
			}

			if (!"".equals(appId)) {
				// ��ѯ��ͼ����ù�������
				TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
				condition.setAppId(appId);
				// TdscBlockAppView tdscBlockAppView =
				// (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);

			}
			request.setAttribute("blockTranAppList", blockTranAppList);
		}

		request.setAttribute("transferMode", transferMode);

		if (!StringUtil.isEmpty(strNoPurContext)) {
			strNoPurContext = strNoPurContext.substring(0, strNoPurContext.length() - 1);
			strNoPurContext += "�ؿ�Ϊ�����򹫿����Ƴ��ã�";

			request.setAttribute("noPurContext", strNoPurContext);

		} else {
			request.setAttribute("noPurContext", "");
		}

		// �鿴��ǰ���޳��ù��汣��
		TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
		if (!"".equals(noticeId)) {
			// tdscBlockFileOld = tdscFileService.getBlockFileAppById(noticeId);
			TdscNoticeApp tdscNoticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				tdscBlockFileOld = tdscFileService.getBlockFileAppByRecordId(tdscNoticeApp.getRecordId());
			}
		}

		// �ж��Ƿ��Ƿ�������
		if ("fabu".equals(type)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
			}
			request.setAttribute("type", type);
			request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
			return mapping.findForward("printSaveNotice");
		}

		if ("notice".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");

					// request.setAttribute("blockPlanTable", arg1)
					return mapping.findForward("printSaveNotice");
				} else {
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
					return mapping.findForward("printNotice_word");
					// return mapping.findForward("printNotice_pm");
				}
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {

				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					// ����ǹ�ҵ�õ�
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());

					if ("101".equals(strBlockQuality)) {
						// ��ҵ�õأ���Ҫ�� Template �м����ļ� notice_gp_industry.doc
						// �޸����� struts.xml
						if(noticeNo.indexOf("��") != -1){
							request.setAttribute("modeNameEn", "notice_gp_industry_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "notice_gp_industry.doc");
						}
						return mapping.findForward("printSaveNoticeGpIndustry");
					} else {
						// ��Ӫ���õ�
						if(noticeNo.indexOf("��") != -1){
							request.setAttribute("modeNameEn", "printNotice_gp_word_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
						}
						return mapping.findForward("printSaveNotice");
					}

				} else {
					if ("101".equals(strBlockQuality)) {
						// ��ҵ�õأ���Ҫ�� Template �м����ļ� notice_gp_industry.doc
						// �޸����� struts.xml
						if(noticeNo.indexOf("��") != -1){
							request.setAttribute("modeNameEn", "notice_gp_industry_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "notice_gp_industry.doc");
						}
						
						return mapping.findForward("printNotice_industry");
					} else {
						// ��Ӫ���õ�
						if(noticeNo.indexOf("��") != -1){
							request.setAttribute("modeNameEn", "printNotice_gp_word_zl.doc");
						}else{
							request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
						}
						return mapping.findForward("printNotice_word");
						// return mapping.findForward("printNotice_gp");
					}
				}
			}

		} else if ("plan".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_pm_word.doc");
				// return mapping.findForward("printPlan_pm");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_gp_word.doc");
				// return mapping.findForward("printPlan_gp");
			}
			return mapping.findForward("printPlan_word");
		}
		return null;
	}

	/**
	 * ��ӡ������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward printNotice_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// noticeId ��ʵ��ΪplanId���ڽ����������桱��ǰ���롰���������ļ���һ��ʱ�޸ģ�2010-5-3 smw����
		String noticeId = (String) request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		String printType = (String) request.getParameter("printType");
		String type = (String) request.getParameter("type");
		String planId = request.getParameter("planId");
		request.setAttribute("noticeName", request.getParameter("noticeName"));

		String createNoticeNo1 = request.getParameter("createNoticeNo1");
		String createNoticeNo2 = request.getParameter("createNoticeNo2");
		String getNoticeNo = request.getParameter("getnoticeNo");
		request.setAttribute("createNoticeNo1", createNoticeNo1);
		request.setAttribute("createNoticeNo2", createNoticeNo2);

		String noticeNo = "";
		// ȡ�ó����ļ���Ϣ
		TdscNoticeApp noticeApp = new TdscNoticeApp();

		noticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
		if (noticeApp != null && !"".equals(noticeApp.getNoticeNo())) {
			noticeNo = noticeApp.getNoticeNo();
		} else {
			// TdscFileForm tdscFileForm = (TdscFileForm)form;
			String noticeNum = request.getParameter("noticeNum");
			if (StringUtils.isNotEmpty(noticeNum))
				noticeNo = "�����ʷ�����[" + noticeNum + "��";
			else
				noticeNo = request.getParameter("noticeNo");
		}
		// Ĭ�ϳ��÷�ʽΪ����
		String transferMode = GlobalConstants.DIC_TRANSFER_AUCTION;
		if (noticeId != null && !"".equals(noticeId)) {
			// ����noticeId��ѯblockIdList(noticeId ��ʵ��ΪplanId)
			planId = StringUtils.isEmpty(planId) ? noticeId : StringUtils.trimToEmpty(planId);
			List blockIdList = (List) tdscScheduletableService.queryBlockIdListByPlanId(planId);

			// Ϊ�����ļ����������Ϣ����ָ����ʽ����
			List tdscBlockPartList = new ArrayList();
			if (blockIdList != null && blockIdList.size() > 0) {
				tdscBlockPartList = tdscBlockInfoService.makeDataForFile(blockIdList);
			}
			request.setAttribute("tdscBlockPartList", tdscBlockPartList);

			// �������ʸ�Ҫ��
			TdscBaseQueryCondition viewcondition = new TdscBaseQueryCondition();
			List blockTranAppList = new ArrayList();
			List appIdsList = new ArrayList();
			String blockQuality = "";
			String appId = "";
			for (int i = 0; i < blockIdList.size(); i++) {
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp((String) blockIdList.get(i));
				if ("101".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "��";
				} else if ("102".equals(tdscBlockInfo.getBlockQuality())) {
					blockQuality = "��";
				}
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo((String) blockIdList.get(i));
				if (tdscBlockInfo != null && tdscBlockTranApp != null) {
					// ���ؿ����� ��ʱ������ SpecialPromise �ֶ���
					tdscBlockTranApp.setSpecialPromise(tdscBlockInfo.getBlockName() + "");
					transferMode = tdscBlockTranApp.getTransferMode();
					appId = tdscBlockTranApp.getAppId();
					appIdsList.add(appId);
				}
				blockTranAppList.add(tdscBlockTranApp);
			}

			if (appIdsList != null && appIdsList.size() > 0) {

				request.setAttribute("appIdList", appIdsList);
				// lz+ ����part�ؿ���Ϣ
				viewcondition.setAppIdList(appIdsList);
				viewcondition.setOrderKey("xuHao");
				List list = commonQueryService.queryTdscBlockAppViewListWithoutNode(viewcondition);

				String blockId = "";
				List returnPartForOneList = new ArrayList();
				TdscBlockAppView BlockAppView = new TdscBlockAppView();

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						List blockidListForOne = new ArrayList();// Ϊȡ�������ؿ��ӵؿ�Ĳ���list
						BlockAppView = (TdscBlockAppView) list.get(i);

						blockId = BlockAppView.getBlockId();
						blockidListForOne.add(blockId);
						List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

						String blockCode = "";
						String tdcrnx = "";
						String tdcrnxAll = "";
						BigDecimal blockArea = new BigDecimal(0.00);
						for (int j = 0; j < partListForOne.size(); j++) {
							TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);
							if (j == 0) {
								blockCode = tdscBlockPartForOne.getBlockCode();
							}
							if (j > 0) {
								blockCode += "��" + tdscBlockPartForOne.getBlockCode();
							}
							blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);

							// �������еؿ�������;
							tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
							tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);

							BlockAppView.setTotalLandArea(blockArea);
							BlockAppView.setUnitebBlockCode(blockCode);

							// ȡ���������ʱ�¼�����ֶ� ����tranapp��
							blockId = tdscBlockPartForOne.getBlockId();
							TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);

							BlockAppView.setBzBeizhu(tdscBlockTranApp.getBzBeizhu());
							BlockAppView.setBzCrjkjnyq(tdscBlockTranApp.getBzCrjkjnyq());
							BlockAppView.setBzDgjjgsj(tdscBlockTranApp.getBzDgjjgsj());
							BlockAppView.setBzDkqtsm(tdscBlockTranApp.getBzDkqtsm());
							BlockAppView.setBzGhsjyd(tdscBlockTranApp.getBzGhsjyd());
							BlockAppView.setBzGpsxf(tdscBlockTranApp.getBzGpsxf());
							BlockAppView.setBzJmzgjyq(tdscBlockTranApp.getBzJmzgjyq());
							BlockAppView.setBzPmyj(tdscBlockTranApp.getBzPmyj());
							BlockAppView.setBzSzpttj(tdscBlockTranApp.getBzSzpttj());
							BlockAppView.setBzTdcrfj(tdscBlockTranApp.getBzTdcrfj());
							BlockAppView.setBzTdjftj(tdscBlockTranApp.getBzTdjftj());
							BlockAppView.setBzXbjg(tdscBlockTranApp.getBzXbjg());
							// BlockAppView.setBzXbjg(replaceRN(tdscBlockTranApp.getBzXbjg()));
						}

						// �������еؿ�������; ����RangeSouth�У����س������޴���RangeWest
						BlockAppView.setRangeSouth(tdcrnx);
						BlockAppView.setRangeWest(tdcrnxAll);
						returnPartForOneList.add(BlockAppView);
						request.setAttribute("tdscBlockAppView", BlockAppView);
					}
				}
				request.setAttribute("pageList", returnPartForOneList);// ��ؿ���Ϣ��
				// lz end
			}

			if ("true".equals(getNoticeNo)) {
				getNoticeNo = "������" + blockQuality + "��[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";

			}

			if (!"".equals(appId)) {
				// ��ѯ��ͼ����ù�������
				TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
				condition.setAppId(appId);
				// TdscBlockAppView tdscBlockAppView =
				// (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);

			}
			request.setAttribute("blockTranAppList", blockTranAppList);
		}

		request.setAttribute("transferMode", transferMode);

		// �鿴��ǰ���޳��ù��汣��
		TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
		if (!"".equals(noticeId)) {
			tdscBlockFileOld = tdscFileService.getBlockFileAppById(noticeId);
		}

		// �ж��Ƿ��Ƿ�������
		if ("fabu".equals(type)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
			}
			request.setAttribute("type", type);
			request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
			return mapping.findForward("printSaveNotice");
		}

		if ("notice".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");

					return mapping.findForward("printSaveNotice");
				} else {
					request.setAttribute("modeNameEn", "printNotice_pm_word.doc");
					return mapping.findForward("printNotice_word");
					// return mapping.findForward("printNotice_pm");
				}
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				if (tdscBlockFileOld != null && tdscBlockFileOld.getRecordId() != null) {
					request.setAttribute("recordId", tdscBlockFileOld.getRecordId());
					request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
					return mapping.findForward("printSaveNotice");
				} else {
					request.setAttribute("modeNameEn", "printNotice_gp_word.doc");
					return mapping.findForward("printNotice_word");
					// return mapping.findForward("printNotice_gp");
				}
			}

		} else if ("plan".equals(printType)) {
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_pm_word.doc");
				// return mapping.findForward("printPlan_pm");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				request.setAttribute("modeNameEn", "printPlan_gp_word.doc");
				// return mapping.findForward("printPlan_gp");
			}
			return mapping.findForward("printPlan_word");
		}
		return null;
	}

	/**
	 * ��ӡ�������������ƶ�ʵʩ������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toPrintPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = (String) request.getParameter("planId");
		String type = (String) request.getParameter("type");

		request.setAttribute("type", type);

		String noticeNo = "";
		String transferMode = "";
		String blockId = "";
		String recordId = "";
		List blockIdList = new ArrayList();

		if (planId != null && !"".equals(planId)) {

			// ȡ��ʵʩ������Ϣ��ȡrecordId
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);
			if (tdscBlockPlanTable != null && tdscBlockPlanTable.getRecordId() != null) {
				recordId = tdscBlockPlanTable.getRecordId();
			}

			TdscBlockAppView BlockAppViewByPlanId = new TdscBlockAppView();
			// ȡ�����еؿ���Ϣ
			TdscBaseQueryCondition planIdCondition = new TdscBaseQueryCondition();
			planIdCondition.setPlanId(planId);
			List viewListByPlanId = commonQueryService.queryTdscBlockAppViewListByPlanId(planIdCondition);

			// ��ѯblockIdList
			if (viewListByPlanId != null && viewListByPlanId.size() > 0) {
				for (int m = 0; m < viewListByPlanId.size(); m++) {
					BlockAppViewByPlanId = (TdscBlockAppView) viewListByPlanId.get(m);
					transferMode = BlockAppViewByPlanId.getTransferMode();
					blockId = BlockAppViewByPlanId.getBlockId();
					blockIdList.add(blockId);
				}
			}

			if (blockIdList != null && blockIdList.size() > 0) {
				// ��ѯ�ӵؿ���Ϣ
				List tdscBlockPartList = (List) tdscFileService.getBlockPartByBlockIdList(blockIdList);
				BigDecimal blockAreas = new BigDecimal(0);
				if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
					for (int i = 0; i < tdscBlockPartList.size(); i++) {
						TdscBlockPart tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(i);
						if (tdscBlockPart.getBlockArea() != null) {
							blockAreas = blockAreas.add(tdscBlockPart.getBlockArea());
						}
						// ȡ�����
						String blockId2 = tdscBlockPart.getBlockId();
						String xuhao = "";
						for (int m = 0; m < blockIdList.size(); m++) {
							if (blockId2.equals(blockIdList.get(m))) {
								xuhao = "00" + String.valueOf(m + 1);
								xuhao = xuhao.substring(xuhao.length() - 2);
							}
						}
						// ��Ŵ���BlockName��
						tdscBlockPart.setBlockName(xuhao);

						TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp(blockId2);

						TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId2);
						// �ݻ���,�ܶȣ��̻�,�ؿ�λ�ñ�����RangeEast��,�ؿ鹫��ű�����RangeNorth��
						// tdscBlockPart.setVolumeRate(StringUtils.trimToEmpty(tdscBlockPart.getVolumeRate()));
						// tdscBlockPart.setDensity(StringUtils.trimToEmpty(tdscBlockPart.getDensity()));
						// tdscBlockPart.setGreeningRate(StringUtils.trimToEmpty(tdscBlockPart.getGreeningRate()));
						tdscBlockPart.setRangeEast(StringUtils.trimToEmpty(tdscBlockInfo.getLandLocation()));
						tdscBlockPart.setRangeNorth(StringUtils.trimToEmpty(tdscBlockInfo.getBlockNoticeNo()));

						// ��֤�����ļ�
						tdscBlockPart.setMemo(NumberUtil.numberDisplay(tdscBlockTranApp.getMarginAmount(), 2));
						tdscBlockPart.setBlockDetailedUsed(NumberUtil.numberDisplay(tdscBlockTranApp.getInitPrice(), 2));
					}
				}
				request.setAttribute("blockAreas", blockAreas + "");
				request.setAttribute("tdscBlockPartList", tdscBlockPartList);
				// �������ʸ�Ҫ��
				TdscBaseQueryCondition viewcondition = new TdscBaseQueryCondition();
				List blockTranAppList = new ArrayList();
				List appIdsList = new ArrayList();
				String appId = "";
				for (int i = 0; i < blockIdList.size(); i++) {
					TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscScheduletableService.getBlockInfoApp((String) blockIdList.get(i));
					TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo((String) blockIdList.get(i));
					if (tdscBlockInfo != null && tdscBlockTranApp != null) {
						// ���ؿ����� ��ʱ������ SpecialPromise �ֶ���
						tdscBlockTranApp.setSpecialPromise(tdscBlockInfo.getBlockName() + "");
						transferMode = tdscBlockTranApp.getTransferMode();
						appId = tdscBlockTranApp.getAppId();
						appIdsList.add(appId);
					}
					blockTranAppList.add(tdscBlockTranApp);
				}

				// lz+ ����part�ؿ���Ϣ
				viewcondition.setAppIdList(appIdsList);
				List list = commonQueryService.queryTdscBlockAppViewListWithoutNode(viewcondition);

				List returnPartForOneList = new ArrayList();
				TdscBlockAppView BlockAppView = new TdscBlockAppView();

				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						List blockidListForOne = new ArrayList();// Ϊȡ�������ؿ��ӵؿ�Ĳ���list
						BlockAppView = (TdscBlockAppView) list.get(i);

						blockId = BlockAppView.getBlockId();
						blockidListForOne.add(blockId);
						List partListForOne = tdscFileService.getBlockPartByBlockIdList(blockidListForOne);

						String blockCode = "";
						String tdcrnx = "";
						String tdcrnxAll = "";
						BigDecimal blockArea = new BigDecimal(0.00);
						BigDecimal mu = new BigDecimal(0.00);
						for (int j = 0; j < partListForOne.size(); j++) {
							TdscBlockPart tdscBlockPartForOne = (TdscBlockPart) partListForOne.get(j);
							if (j == 0) {
								blockCode = tdscBlockPartForOne.getBlockCode();
							}
							if (j > 0) {
								blockCode += "��" + tdscBlockPartForOne.getBlockCode();
							}
							blockArea = tdscBlockPartForOne.getBlockArea().add(blockArea);

							tdcrnx = tdscBlockInfoService.tidyLandUseTypeByBlockId(blockId);
							tdcrnxAll = tdscBlockInfoService.tidyUseTypeAndTransferLife(blockId);
							BlockAppView.setTotalLandArea(blockArea);
							BlockAppView.setUnitebBlockCode(blockCode);

							// ȡ���������ʱ�¼�����ֶ� ����tranapp��
							blockId = tdscBlockPartForOne.getBlockId();
							TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscScheduletableService.getBlockTranAppInfo(blockId);

							BlockAppView.setBzBeizhu(tdscBlockTranApp.getBzBeizhu());
							BlockAppView.setBzCrjkjnyq(tdscBlockTranApp.getBzCrjkjnyq());
							BlockAppView.setBzDgjjgsj(tdscBlockTranApp.getBzDgjjgsj());
							BlockAppView.setBzDkqtsm(tdscBlockTranApp.getBzDkqtsm());
							BlockAppView.setBzGhsjyd(tdscBlockTranApp.getBzGhsjyd());
							BlockAppView.setBzGpsxf(tdscBlockTranApp.getBzGpsxf());
							BlockAppView.setBzJmzgjyq(tdscBlockTranApp.getBzJmzgjyq());
							BlockAppView.setBzPmyj(tdscBlockTranApp.getBzPmyj());
							BlockAppView.setBzSzpttj(tdscBlockTranApp.getBzSzpttj());
							BlockAppView.setBzTdcrfj(tdscBlockTranApp.getBzTdcrfj());
							BlockAppView.setBzTdjftj(tdscBlockTranApp.getBzTdjftj());
							BlockAppView.setBzXbjg(tdscBlockTranApp.getBzXbjg());
						}

						// �������еؿ�������; ����RangeSouth�У����س������޴���RangeWest
						mu = blockArea.multiply(new BigDecimal(0.0015));
						mu = mu.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
						BlockAppView.setMu(mu);// Ķ
						BlockAppView.setRangeSouth(tdcrnx);
						BlockAppView.setRangeWest(tdcrnxAll);

						returnPartForOneList.add(BlockAppView);
						request.setAttribute("tdscBlockAppView", BlockAppView);
					}
				}

				request.setAttribute("pageList", returnPartForOneList);// ��ؿ���Ϣ��
				// lz end
				if (!"".equals(appId)) {
					// ��ѯ��ͼ����ù�������
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					condition.setAppId(appId);
					// TdscBlockAppView tdscBlockAppView =
					// (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);

				}
				request.setAttribute("blockTranAppList", blockTranAppList);
			}
		}

		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("recordId", recordId);

		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
			request.setAttribute("modeNameEn", "printPlan_pm_word.doc");
		}
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
			request.setAttribute("modeNameEn", "printPlan_gp_word.doc");
		}
		return mapping.findForward("printPlan_word");
	}

	public static String replaceRN(String s) {
		while (s != null && (s.indexOf("\r\n") != -1)) {
			s = s.substring(0, s.indexOf("\r\n")) + "<br>" + s.substring(s.indexOf("\r\n") + 2);
		}
		return s;
	}

	/**
	 * �����ù����Ƿ��Ѵ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward ifSaveFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = (String) request.getParameter("noticeId");
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		String returnStr = "00";

		// �鿴��ǰ���޳��ù��汣��
		TdscNoticeApp tdscNoticeApp = null;
		if (!"".equals(noticeId)) {
			tdscNoticeApp = this.tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null && tdscNoticeApp.getRecordId() != null)
				returnStr = "01";
		}

		pw.write(returnStr);
		pw.close();
		return null;
	}

	/**
	 * ���noticeNo�Ƿ��Ѵ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkNoticeNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// String noticeNo1 = (String) request.getParameter("noticeNo");
		String createNoticeNo1 = (String) request.getParameter("createNoticeNo1");
		String createNoticeNo2 = (String) request.getParameter("createNoticeNo2");
		String blockNoticeTile = (String) request.getParameter("blockNoticeTile");

		String noticeNo = blockNoticeTile + "[" + createNoticeNo1 + "]" + createNoticeNo2 + "��";

		request.setAttribute("noticeNo", noticeNo);
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		String returnStr = "00";

		// �鿴��ǰ���޳��ù��汣��
		List tdscNoticeAppList = new ArrayList();
		if (!"".equals(noticeNo)) {
			tdscNoticeAppList = tdscNoticeService.findTdscNoticeAppListByNoticeNo(StringUtil.GBKtoISO88591(noticeNo));
			if (tdscNoticeAppList != null && tdscNoticeAppList.size() > 0)
				returnStr = "01";// ���ڸù����
		}

		pw.write(returnStr);
		pw.close();
		return null;
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
	public ActionForward checkFileEditComp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setPlanId(planId);
		List list = this.commonQueryService.queryTdscBlockAppViewListByPlanId(condition);
		int size = 0;
		int editCount = 0;
		if (list != null) {
			size = list.size();
			for (int i = 0; i < list.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) list.get(i);
				if ("0302".equals(tdscBlockAppView.getNextAction())) {
					editCount++;
				}
			}
		}
		// ���������õ����

		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		if (size == editCount) {
			pw.write("1");
		} else {
			pw.write("0");
		}
		pw.close();
		return null;
	}

	/**
	 * �˻�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward callback(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String transferMode = tdscBlockAppView.getTransferMode();
		// ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(appId);
		appFlow.setTransferMode(transferMode);
		appFlow.setUser(user);
		appFlow.setTextOpen("�����������������ļ�");
		appFlow.setResultId("030202");
		// ����֮��ɾ�� �����ļ�
		this.tdscNoticeService.callBack(appId, appFlow);
		return new ActionForward("file.do?method=queryAppFileListWithNodeId", true);
	}

	public ActionForward generateNextNoticeNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");

		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(planId);

		String noticeNoName = "";
		if ("3107".equals(tdscBlockPlanTable.getTransferMode())) {
			noticeNoName = "�����й�����Դ�ֹ��н����õ�ʹ��Ȩ�б���ù���";
		} else if ("3103".equals(tdscBlockPlanTable.getTransferMode())) {
			noticeNoName = "�����й�����Դ�ֹ��н����õ�ʹ��Ȩ�������ù���";
		} else if ("3104".equals(tdscBlockPlanTable.getTransferMode())) {
			//�ж��Ƿ������޹���
			if(StringUtils.isNotBlank(tdscBlockPlanTable.getTradeNum()) && tdscBlockPlanTable.getTradeNum().indexOf("��") != -1){
				noticeNoName = "�����й�����Դ�ֹ��н����õ�ʹ��Ȩ�������޹���";
			}else{
				noticeNoName = "�����й�����Դ�ֹ��н����õ�ʹ��Ȩ���Ƴ��ù���";
			}
		}

		java.util.Date sysdate = new java.util.Date();
		java.text.SimpleDateFormat dformat = new java.text.SimpleDateFormat("yyyy");
		String year = (String) dformat.format(sysdate);

		String noticeNoPrefix = "";
		String noticeNo1 = "";
		String temp ="";
		String tradeNum = tdscBlockPlanTable.getTradeNum();
		
		
		if(StringUtils.isNotBlank(tradeNum) && tradeNum.indexOf("��") != -1){
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "��" + tradeNum.substring(4, 5) + "�����[" + year + "]";
				noticeNo1 = "��" + tradeNum.substring(4, 5) + "�����";
			} else {
				noticeNoPrefix = "���������[" + year + "]";
				noticeNo1 = "���������";
			}
			temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("17",noticeNoPrefix);
		}else{
			if (StringUtils.isNotBlank(tradeNum)) {
				noticeNoPrefix = "��" + tradeNum.substring(4, 5) + "����[" + year + "]";
				noticeNo1 = "��" + tradeNum.substring(4, 5) + "����";
			} else {
				noticeNoPrefix = "��������[" + year + "]";
				noticeNo1 = "��������";
			}
			temp = (String) tdscNoticeService.getCurrNoticeNoByNoticeNoPrefix("15",noticeNoPrefix);
		}


		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();

		pw.write(noticeNoName + "," + noticeNo1 + "," + year + "," + Integer.parseInt(temp));
		pw.close();

		return null;
	}	
}
