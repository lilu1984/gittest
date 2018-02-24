package com.wonders.tdsc.blockwork.web;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.blockwork.web.form.ScheduletableInfoForm;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockSelectApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNotaryInfo;
import com.wonders.tdsc.bo.TdscXbOrgApp;
import com.wonders.tdsc.bo.TdscXbOrgHistory;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscBlockTranAppCondition;
import com.wonders.tdsc.businessDay.X_BusinessDayService;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.randomselect.service.TdscSelectService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.tdscbase.service.ShortMessageService;
import com.wonders.tdsc.xborg.service.TdscXbOrgService;

public class TdscScheduletableAction extends BaseAction {

	private CommonQueryService			commonQueryService;

	private TdscScheduletableService	tdscScheduletableService;

	private AppFlowService				appFlowService;

	private TdscSelectService			tdscSelectService;

	private CommonFlowService			commonFlowService;

	private TdscXbOrgService			tdscXbOrgService;

	private ShortMessageService			smsService;

	private TdscNoticeService			tdscNoticeService;

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setTdscSelectService(TdscSelectService tdscSelectService) {
		this.tdscSelectService = tdscSelectService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setSmsService(ShortMessageService smsService) {
		this.smsService = smsService;
	}

	public void setTdscXbOrgService(TdscXbOrgService tdscXbOrgService) {
		this.tdscXbOrgService = tdscXbOrgService;
	}

	/**
	 * ɾ�����Ȱ��ű�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delPlanTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		if (planId != null && !"".equals(planId)) {
			// ��ѯ���Ȱ��ű�
			TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
			planTableCondition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);
			// ��ѯ�ؿ齻�ױ���Ϣ
			TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
			tranAppCondition.setPlanId(planId);
			List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
			tdscScheduletableService.delPlanTable(tdscBlockPlanTable, tranAppList);
		}
		// return queryAppListWithNodeId(mapping, form, request, response);

		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * ���Ȱ��ű���ɾ���ؿ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strAppId = request.getParameter("appId");
		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setAppId(strAppId);
		TdscBlockAppView view = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(cond);

		tdscScheduletableService.deleteBlockInfo(strAppId, view.getBlockId());

		return new ActionForward("scheduletable.do?method=queryBlockAppList", true);
	}

	/**
	 * �����Խ��Ȱ��ű�λ�Ĳ�ѯ�б�ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ��ֵ
		String pageNo = request.getParameter("currentPage");
		String blockName = request.getParameter("blockName");
		String tradeNum = request.getParameter("tradeNum");
		String transferMode = request.getParameter("transferMode");
		String uniteBlockCode = request.getParameter("uniteBlockCode");
		String ifPCommit = request.getParameter("ifPCommit");
		String landLocation = request.getParameter("landLocation");

		// ���Session�е�¼�û��İ�ť��Ϣ�б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}

		// ���ò�ѯ����
		TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
		this.bindObject(planCondition, form);

		// �����û���Ϣ�еĿɲ�ѯ��ʵʩ����״̬
		ArrayList statusFlowList = new ArrayList();
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			planCondition.setUserId(user.getUserId());
		}
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_MAKE) != null) {
			statusFlowList.add("00");
		}
		// �޸����ںͳ��Σ���Ҫ�����û�ID����ѯ����
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_DATE_MODIFY) != null || buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_FIELD_MODIFY) != null) {
			statusFlowList.add("02");
		}
		// �����Ա�鿴����ȫ������˵����ݣ��Ѳ�ѯ�����е�UserId����Ϊ��
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_SHENHE) != null) {
			statusFlowList.add("01");
			planCondition.setUserId(null);
		}

		if (request.getAttribute("forwardType") != null && "01".equals(request.getAttribute("forwardType"))) {
			planCondition.setBlockName("");
			planCondition.setTradeNum("");
			planCondition.setTransferMode("");
			planCondition.setUniteBlockCode("");
			planCondition.setLandLocation("");
			planCondition.setPlanId("");
		} else {
			planCondition.setBlockName(StringUtils.trimToEmpty(blockName));
			planCondition.setTradeNum(StringUtils.trimToEmpty(tradeNum));
			planCondition.setUniteBlockCode(StringUtils.trimToEmpty(uniteBlockCode));
			planCondition.setIfPCommit(StringUtils.trimToEmpty(ifPCommit));
			planCondition.setTransferMode(StringUtils.trimToEmpty(transferMode));
			planCondition.setStatusFlowList(statusFlowList);
			planCondition.setLandLocation(StringUtils.trimToEmpty(landLocation));
		}

		// ��ѯ
		List queryList = (List) tdscScheduletableService.queryPlanTableList(planCondition);

		queryList = setFlagForDelete(queryList);
		// �����ҳ
		PageList pageList = new PageList();
		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		int currentPage = 0;
		if (pageNo != null && pageNo.trim().equals("") == false) {
			currentPage = Integer.parseInt(pageNo);

		}
		if (pageNo == null || Integer.parseInt(pageNo) < 1) {
			currentPage = 1;
		}
		pageList = PageUtil.getPageList(queryList, pageSize, currentPage);

		request.setAttribute("queryList", queryList);
		request.setAttribute("pageList", pageList);
		request.setAttribute("planCondition", planCondition);

		if (request.getParameter("type") != null && "chayue".equals(request.getParameter("type"))) {
			// ת��ʵʩ��������ҳ��
			return mapping.findForward("plantable_chayue");
		} else {
			// ת��ʵʩ�����ⶨҳ��
			return mapping.findForward("plantablelist");
		}
	}

	/**
	 * ���˳�δ����������мƻ� NODE_ID=03 && STATUS_ID=0302
	 * 
	 * @param queryList
	 *            TdscBlockPlanTable
	 * @return
	 */
	private List setFlagForDelete(List queryList) {
		if (queryList != null && queryList.size() > 0)
			for (int i = 0; i < queryList.size(); i++) {
				TdscBlockPlanTable plan = (TdscBlockPlanTable) queryList.get(i);

				TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
				cond.setPlanId(plan.getPlanId());
				List appViews = commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
				TdscBlockAppView appView = null;

				if (appViews != null && appViews.size() > 0)
					appView = (TdscBlockAppView) appViews.get(0);
				if(plan.getBzBeizhu()==null){
					if (appView != null) {
						if ("03".equals(appView.getNodeId()) && "0302".equals(appView.getStatusId()))
							plan.setBzBeizhu("CanDel");
						else
							plan.setBzBeizhu("NotDel");
					} else {
						plan.setBzBeizhu("NotDel");
					}
				}
			}
		return queryList;
	}

	/**
	 * ��ѯ�ڵ�״̬���ƶ����Ȱ��ű�ĵؿ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBlockAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
//		ArrayList nodeList = new ArrayList();
//		nodeList.add(FlowConstants.FLOW_NODE_SCHEDULE_PLAN);
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNodeId(FlowConstants.FLOW_NODE_SCHEDULE_PLAN);
//		condition.setNodeList(nodeList);
		// �ж��Ƿ��ǹ���Ա�������ǹ���Ա��ֻ�ܲ�ѯ���û��ύ�ĵؿ���Ϣ
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		List queryList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		// ȥ�����Ѿ��������Ȱ��ű�ĵؿ���Ϣ
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) queryList.get(i);
				if (tdscBlockAppView.getPlanId() != null) {
					queryList.remove(tdscBlockAppView);
					i--;
				}
			}
		}

		request.setAttribute("queryBlockist", queryList);

		return mapping.findForward("blocklist");
	}

	/**
	 * �ƶ����Ȱ��ű�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward applyPlanTable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		String[] appIds = infoForm.getAppIds();
		String statusFlow = request.getParameter("statusFlow");
		String ifOnLine = request.getParameter("ifOnLine");
		String transferMode = "";
		String iszulin = "";
		String blockQuality = "";
		List tdscblockAppViewList = new ArrayList();
		for (int i = 0; i < appIds.length; i++) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appIds[i]);
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			if (!"".equals(tdscBlockAppView.getTransferMode()) && tdscBlockAppView.getTransferMode() != null) {
				transferMode = tdscBlockAppView.getTransferMode();
				iszulin = tdscBlockAppView.getIsZulin();
			}
			if (StringUtils.isNotBlank(tdscBlockAppView.getBlockQuality())) {
				blockQuality = tdscBlockAppView.getBlockQuality();
			}

			tdscblockAppViewList.add(tdscBlockAppView);
		}
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("iszulin", iszulin);
		request.setAttribute("statusFlow", statusFlow);
		request.setAttribute("tdscBlockAppViewList", tdscblockAppViewList);
		// ���Э�����
		TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
		request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);

		java.util.Date sysdate = new java.util.Date();
		java.text.SimpleDateFormat dformat = new java.text.SimpleDateFormat("yyyy");
		String year = (String) dformat.format(sysdate);

		String tradeNumPrefix = "";

		if ("3107".equals(transferMode)) {
			tradeNumPrefix += "WXZ";
		} else if ("3103".equals(transferMode)) {
			tradeNumPrefix += "WXP";
		} else if ("3104".equals(transferMode)) {
			tradeNumPrefix += "WXG";
		}
		String tradeNum ="";
		//����ģʽ
		if("1".equals(iszulin)){
			if ("102".equals(blockQuality)) {
				tradeNumPrefix += "(����)";
			} else if ("101".equals(blockQuality)) {
				tradeNumPrefix += "(����)";
			}
			tradeNumPrefix += "-" + year;
			tradeNum = tdscScheduletableService.getNextTradeNum("16",tradeNumPrefix);
		}else{
			if ("102".equals(blockQuality)) {
				tradeNumPrefix += "(��)";
			} else if ("101".equals(blockQuality)) {
				tradeNumPrefix += "(��)";
			}
			tradeNumPrefix += "-" + year;
			tradeNum = tdscScheduletableService.getNextTradeNum("14",tradeNumPrefix);
			
		}


		request.setAttribute("tradeNum", tradeNum);
		request.setAttribute("ifOnLine", ifOnLine);

		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {

			return mapping.findForward("scheduletablePm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)) {
			return mapping.findForward("scheduletableZb");
		} else {
			return mapping.findForward("scheduletableGp");
		}
	}

	public ActionForward saveScheduletableInfo_bak(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String transferMode = request.getParameter("transferMode");
		String saveType = request.getParameter("saveType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// ���ҳ���ϵ�tempSaveFlag,�ж��Ƿ��ݴ�
		String tempSaveFlag = request.getParameter("tempSaveFlag");
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// ��form�е�ֵ����bo����Ӧ���õؿ���Ȱ��ű�TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		// bindObject
		if (!"".equals(infoForm.getPlanId()) && infoForm.getPlanId() != null) {
			tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(infoForm.getPlanId());
		} else {
			infoForm.setPlanId(null);
		}
		bindObject(tdscBlockPlanTable, infoForm);
		bindObject(tdscBlockTranApp, infoForm);
		TdscBlockPlanTable retObj = (TdscBlockPlanTable) tdscScheduletableService.saveUnitePlanTable(tdscBlockPlanTable, infoForm, saveType, user);

		String[] appIds = infoForm.getAppIds();
		List tdscblockAppViewList = new ArrayList();
		for (int i = 0; i < appIds.length; i++) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appIds[i]);
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);

			tdscblockAppViewList.add(tdscBlockAppView);
		}

		// ����ORG_APP_ID��ѯЭ�����
		if (tdscBlockPlanTable != null) {
			String orgAppId = tdscBlockPlanTable.getOrgAppId();
			if (StringUtils.isNotEmpty(orgAppId)) {
				TdscXbOrgApp tdscXbOrgApp = (TdscXbOrgApp) tdscXbOrgService.getOrgAppInfoById(orgAppId);
				if (tdscXbOrgApp != null) {
					TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
					tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
					tdscXbOrgHistory.setOrgAppId(orgAppId);
					request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
				}
			}
		}
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tempSaveFlag", tempSaveFlag);
		request.setAttribute("tdscBlockAppViewList", tdscblockAppViewList);
		request.setAttribute("tdscBlockPlanTable", retObj);
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		request.setAttribute("saveMessage", "����ɹ���");
		// �ݴ淵��infoҳ��
		if ("tempSave".equals(saveType)) {
			// ���ݳ��÷�ʽѡ������Ǹ�ҵ��3107�б� 3103���� 3104����
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode) && null != transferMode) {
				return mapping.findForward("tempSaveZb");
			} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				return mapping.findForward("tempSavePm");
			} else {
				return mapping.findForward("tempSaveGp");
			}
		} else {// �ύ�򷵻�listҳ��

			return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
		}
	}

	/**
	 * 20090625����ʵʩ����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveScheduletableInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String transferMode = request.getParameter("transferMode");
		String saveType = request.getParameter("saveType");
		String statusFlow = request.getParameter("statusFlow");
		String ifOnLine = request.getParameter("ifOnLine");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// ���ҳ���ϵ�tempSaveFlag,�ж��Ƿ��ݴ�
		String tempSaveFlag = request.getParameter("tempSaveFlag");
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// ��form�е�ֵ����bo����Ӧ���õؿ���Ȱ��ű�TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		// bindObject
		if (!"".equals(infoForm.getPlanId()) && infoForm.getPlanId() != null) {
			tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findPlanTableByPlanId(infoForm.getPlanId());
		} else {
			infoForm.setPlanId(null);
		}

		bindObject(tdscBlockPlanTable, infoForm);
		tdscBlockPlanTable.setTradeNum(infoForm.getTradeNum_1() + "-" + infoForm.getTradeNum_2() + "-" + infoForm.getTradeNum_3());
		tdscBlockPlanTable.setIfOnLine(ifOnLine);//�Ƿ����Ͻ��ף�1Ϊ�ǣ�0Ϊ�ֳ�����
		bindObject(tdscBlockTranApp, infoForm);
		// ����ʵʩ���������̽ڵ�
		tdscBlockPlanTable.setStatusFlow(statusFlow);
		// ���ò����û���Ϣ
		String userId = "";
		String[] appIds = infoForm.getAppIds();
		List tdscblockAppViewList = new ArrayList();

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		for (int i = 0; i < appIds.length; i++) {
			if (StringUtils.isNotEmpty(appIds[i])) {
				// ���ò�ѯ����
				condition.setAppId(appIds[i]);
				// ��ѯ�ؿ���Ϣ
				TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
				// ��øõؿ���û�ID
				if (StringUtils.isNotEmpty(tdscBlockAppView.getUserId())) {
					userId = tdscBlockAppView.getUserId();
				}
				tdscblockAppViewList.add(tdscBlockAppView);
			}
		}
		// ����һ��
		if (StringUtils.isNotEmpty(userId)) {
			tdscBlockPlanTable.setUserId(userId);
		}
		
		//user_id ���ڱ����о���ϵ��
		String sjLinkMan = request.getParameter("sjLinkman");
		tdscBlockPlanTable.setUserId(sjLinkMan);
		
		TdscBlockPlanTable retObj = (TdscBlockPlanTable) tdscScheduletableService.saveUnitePlanTable(tdscBlockPlanTable, infoForm, saveType, user);

		// String[] tradeNums = retObj.getTradeNum().split("-");
		// if(tradeNums != null && tradeNums.length == 3){
		// request.setAttribute("tradeNum",tradeNums[2]);
		// }
		// ����ORG_APP_ID��ѯЭ�����
		// if(tdscBlockPlanTable != null){
		// String orgAppId = tdscBlockPlanTable.getOrgAppId();
		// if(StringUtils.isNotEmpty(orgAppId)){
		// TdscXbOrgApp tdscXbOrgApp = (TdscXbOrgApp)tdscXbOrgService.getOrgAppInfoById(orgAppId);
		// if(tdscXbOrgApp != null){
		// TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
		// tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
		// tdscXbOrgHistory.setOrgAppId(orgAppId);
		// request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
		// }
		// }
		// }
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tempSaveFlag", tempSaveFlag);
		request.setAttribute("statusFlow", statusFlow);
		request.setAttribute("tdscBlockAppViewList", tdscblockAppViewList);
		request.setAttribute("tdscBlockPlanTable", retObj);
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		request.setAttribute("saveMessage", "����ɹ���");
		// �ݴ淵��infoҳ��
		if ("tempSave".equals(saveType)) {
			// ���ݳ��÷�ʽѡ������Ǹ�ҵ��3107�б� 3103���� 3104����
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode) && null != transferMode) {
				return mapping.findForward("tempSaveZb");
			} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				request.setAttribute("forwardType", "01");
				return mapping.findForward("tempSavePm");
			} else {
				request.setAttribute("forwardType", "01");
				return mapping.findForward("tempSaveGp");
			}
		} else {// �ύ�򷵻�listҳ��
			request.setAttribute("forwardType", "01");
			return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
		}
	}

	/**
	 * ����iwebʵʩ������recordId
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveIwebPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		String recordId = request.getParameter("recordId");
		if (planId != null && !"".equals(planId)) {
			// ��ѯ���Ȱ��ű�
			TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
			planTableCondition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);
			if (recordId != null && !"".equals(recordId)) {
				tdscBlockPlanTable.setRecordId(recordId);
				tdscScheduletableService.updateBlockPlanTable(tdscBlockPlanTable);
			}
		}

		request.setAttribute("recordId", recordId);
		request.setAttribute("saveMessage", "����ɹ���");

		return mapping.findForward("saveIwebPlan");
	}

	/**
	 * ��ѯ����ҵ���б�
	 */
	public ActionForward queryAppListWithNodeId_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String blockNoticeNo = request.getParameter("blockNoticeNo");
		String currentPage = request.getParameter("currentPage");
		String blockName = request.getParameter("blockName");
		String transferMode = request.getParameter("transferMode");
		String blockType = request.getParameter("blockType");
		String districtId = request.getParameter("districtId");
		if (currentPage == null) {
			currentPage = (String) request.getAttribute("currentPage");
		}

		int cPage = 0;
		if (currentPage != null) {
			cPage = Integer.parseInt(currentPage);
		}

		ArrayList nodeList = new ArrayList();
		// ���Session�е�¼�û��İ�ť��Ϣ�б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_MAKE) != null) {
			nodeList.add(FlowConstants.FLOW_NODE_SCHEDULE_PLAN);
		}
		if (buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_DATE_MODIFY) != null || buttonMap.get(GlobalConstants.BUTTON_ID_SCHEDULE_FIELD_MODIFY) != null) {
			nodeList.add(FlowConstants.FLOW_NODE_FILE_MAKE);
			nodeList.add(FlowConstants.FLOW_NODE_FILE_RELEASE);
			nodeList.add(FlowConstants.FLOW_NODE_PREVIEW);
			// nodeList.add(FlowConstants.FLOW_NODE_QUESTION_GATHER);
			// nodeList.add(FlowConstants.FLOW_NODE_ANSWER);
			nodeList.add(FlowConstants.FLOW_NODE_BID_OPENNING_APPROVAL);
			nodeList.add(FlowConstants.FLOW_NODE_BIDDER_APP);
			nodeList.add(FlowConstants.FLOW_NODE_AUCTION);
			nodeList.add(FlowConstants.FLOW_NODE_LISTING);
			nodeList.add(FlowConstants.FLOW_NODE_LISTING_SENCE);
		}

		// checked out by hjf to modify nodelist
		// field modify cn be done until transfer end

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();

		condition.setNodeList(nodeList);

		condition.setCurrentPage(cPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setUser(user);
		condition.setBlockNoticeNo(blockNoticeNo);
		condition.setBlockName(blockName);
		condition.setTransferMode(transferMode);
		condition.setBlockType(blockType);
		condition.setDistrictId(districtId);
		condition.setPlusConditionTag(GlobalConstants.QUERY_WITHOUTPLUS_TAG);
		condition.setOrderKey("appDate");
		condition.setOrderType("desc");
		request.setAttribute("queryAppList", commonQueryService.queryTdscBlockAppViewPageList(condition));// ��������ѯ�б�
		request.setAttribute("queryAppCondition", condition);

		return mapping.findForward("list");
	}

	/**
	 * ����ҵ����ģ����Ȱ��ű���� 20071129*
	 */
	public ActionForward queryAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return mapping.findForward("list");
	}

	/**
	 * �ƶ����Ȱ��ű������Ϣ 20071129*
	 */
	public ActionForward toScheduletableInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String statusId = request.getParameter("statusId");
		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// ͨ��appId�õ���ͼTdscBlockAppView�е�blockId
			// �ȸ�blockId��ֵΪ1����ȡtdscBlockAppView�е�blockId
			// TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(baseCondition);
			// String blockId = tdscBlockAppView.getBlockId();
			// ��ѡ����

			// ����blockId�õ��ؿ���;��Ϣ���еĹ滮��;�ͳ�������
			String blockId = "1";
			List tdscBlockUsedInfo = tdscScheduletableService.queryTdscBlockUsedInfoList(blockId);
			request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);
			// ͨ��appId����ѯ���õؿ����ִ�б��еġ�����Ҫ�ز�����ʼʱ��(REC_MAT_START_DATE)��
			// TdscBlockScheduleTable tdscBlockScheduleTable = tdscScheduletableService.findScheduleInfo(appId);
			// request.setAttribute("tdscBlockScheduleTable", tdscBlockScheduleTable);
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// �ж�TDSC_BLOCK_TRAN_APP���Ƿ�Ϊ��,ȡ��MarginEndDate��ֵ
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
		}
		request.setAttribute("appId", appId);
		// ��ò�ѯ����
		TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		baseCondition.setAppId(appId);
		// ���ù�������ȡ�ù�����Ϣ
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
		String mode = (String) commonInfo.getTransferMode();
		request.setAttribute("mode", mode);
		if (FlowConstants.FLOW_STATUS_SCHEDULETABLE_MAKE.equals(statusId)) {
			// ���ݳ��÷�ʽѡ������Ǹ�ҵ��DIC_TRANSFER_TENDER = "3107"�б� DIC_TRANSFER_AUCTION = "3103"���� DIC_TRANSFER_LISTING = "3104"����
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
				return mapping.findForward("scheduletableGp");
			}
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
				return mapping.findForward("scheduletablePm");
			}
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
				return mapping.findForward("scheduletableZb");
			}
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * ������Ȱ��ű������Ϣ-�б� ���� ����
	 * 
	 * @param appId
	 *            and mode 20071201*
	 */
	public ActionForward saveScheduletableInfo_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String mode = request.getParameter("mode");
		String saveType = request.getParameter("saveType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// String selectType = "01";

		// ���ҳ���ϵ�tempSaveFlag,�ж��Ƿ��ݴ�
		String tempSaveFlag = request.getParameter("tempSaveFlag");
		// ����ActionForm
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// ��form�е�ֵ����bo����Ӧ���õؿ���Ȱ��ű�TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		tdscBlockPlanTable.setAppId(appId);
		bindObject(tdscBlockPlanTable, infoForm);

		// ���ݳ��÷�ʽѡ�񱣴���Ӧ��ֵ��3107�б� 3103���� 3104����
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			tdscBlockPlanTable.setListStartDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setListEndDate(DateUtil.string2Timestamp(request.getParameter("listEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidDate(DateUtil.string2Timestamp(request.getParameter("sceBidDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidLoc(request.getParameter("sceBidLoc"));
			tdscBlockPlanTable.setListLoc(request.getParameter("listLoc"));
		} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			tdscBlockPlanTable.setAuctionDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setAuctionLoc(request.getParameter("auctionLoc"));
		} else {
			tdscBlockPlanTable.setTenderStartDate(DateUtil.string2Timestamp(request.getParameter("tenderStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setTenderEndDate(DateUtil.string2Timestamp(request.getParameter("tenderEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningLoc(request.getParameter("openingLoc"));
			tdscBlockPlanTable.setBidEvaDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setBidEvaLoc(request.getParameter("bidEvaLoc"));
		}
		// ������֤���ʽ�ֹʱ�䡱���浽TDSC_BLOCK_TRAN_APP����
		String marginEndDat = request.getParameter("marginEndDat");
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp.setMarginEndDate(DateUtil.string2Timestamp(marginEndDat, DateUtil.FORMAT_DATETIME));
		// ִ�в�������
		tdscScheduletableService.savePlanTableInfo(tdscBlockPlanTable, appId, mode, tdscBlockTranApp, saveType, user);
		request.setAttribute("saveMessage", "����ɹ�");
		// tempSaveFlag��Ϊ��ʱ,�����ݴ�ҳ��
		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// ͨ��appId�õ���ͼTdscBlockAppView�е�blockId
			// �ȸ�blockId��ֵΪ1����ȡtdscBlockAppView�е�blockId
			// TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(baseCondition);
			// String blockId = tdscBlockAppView.getBlockId();
			String blockId = "1";
			// ����blockId�õ��ؿ���;��Ϣ���еĹ滮��;�ͳ�������
			List tdscBlockUsedInfo = tdscScheduletableService.queryTdscBlockUsedInfoList(blockId);
			request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);

			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition planCondition = new TdscBlockPlanTableCondition();
			planCondition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(planCondition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// �ж�TDSC_BLOCK_TRAN_APP���Ƿ�Ϊ��,ȡ��MarginEndDate��ֵ
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
		}
		request.setAttribute("appId", appId);
		request.setAttribute("mode", mode);
		request.setAttribute("tempSaveFlag", tempSaveFlag);

		if (null != tempSaveFlag) {
			// ���ݳ��÷�ʽѡ������Ǹ�ҵ��3107�б� 3103���� 3104����
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
				return mapping.findForward("tempSaveGp");
			}
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
				return mapping.findForward("tempSavePm");
			}
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
				return mapping.findForward("tempSaveZb");
			}
		}
		// if(null==tempSaveFlag){
		// // ��ѡ����
		// //��ѡ��֤����Ϣ
		// TdscNotaryInfo tdscNotaryInfo = tdscSelectService.verticalRangeSelect();
		// request.setAttribute("tdscNotaryInfo", tdscNotaryInfo);
		// String notaryId = tdscNotaryInfo.getNotaryId();
		// // ���ݳ��÷�ʽ���湫֤����Ϣ--����
		// if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
		// Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		// // nowTime.setHours(07);
		// // nowTime.setMinutes(00);
		// // nowTime.setSeconds(00);
		// nowTime = tidy4AnyTimestamp(nowTime, 07, 0, 0);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// //���ݻظ���ֹʱ��
		// String replyTime = tdscSelectService.setReplyDeadLine();
		//
		//
		// // ���Ļ򱣴����
		// TdscBlockSelectApp appTemp3 = new TdscBlockSelectApp();
		// appTemp3.setAppId(appId);
		// appTemp3.setSelectedId(notaryId);
		// appTemp3.setSelectType("01");
		// appTemp3.setActivityDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
		// appTemp3.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("listLoc")));
		// appTemp3.setUnlockTime(nowTime);
		// appTemp3.setSelectUser(user.toString());
		// appTemp3.setSelectDate(now);
		// appTemp3.setIsValid("01");
		// appTemp3.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp3.setNodeStat("00");
		// appTemp3.setNodeId("16");
		// appTemp3.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		//
		//
		// appTemp3.setBlockName(infoForm.getBlockName());
		// appTemp3.setMarginEndDate(tdscBlockTranApp.getMarginEndDate());
		// appTemp3.setListEndDate(tdscBlockPlanTable.getListEndDate());
		// appTemp3.setSceBidDate(tdscBlockPlanTable.getSceBidDate());
		// appTemp3.setSceBidLoc(tdscBlockPlanTable.getSceBidLoc());
		//
		// this.tdscSelectService.saveBlockSelectApp(appTemp3);
		// // A�๫֤�� ���Ȱ��ű� �ύ����Ҫ��֤�����Ͷ���֪ͨ����������ύ�����ܷ��Ͷ���
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>������Ϣ�ķ�����ʼ����");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp3);
		// // logger.info("===========>������Ϣ�ķ������õ��ý���"+returnMsg);
		// // }
		// }
		//
		// // ���ݳ��÷�ʽ���湫֤����Ϣ--�б�
		// if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
		// // ���ݻظ���ֹʱ��
		// String replyTime = tdscSelectService.setReplyDeadLine();
		// Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		// // nowTime.setHours(07);
		// // nowTime.setMinutes(00);
		// // nowTime.setSeconds(00);
		// nowTime = tidy4AnyTimestamp(nowTime, 07, 0, 0);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		//
		//
		// // ���Ļ򱣴濪��/�����
		// TdscBlockSelectApp appTemp2 = new TdscBlockSelectApp();
		// appTemp2.setAppId(appId);
		// appTemp2.setSelectedId(notaryId);
		// appTemp2.setSelectType("01");
		// appTemp2.setActivityDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
		// appTemp2.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("openingLoc")));
		// appTemp2.setUnlockTime(nowTime);
		// appTemp2.setSelectUser(user.toString());
		// appTemp2.setSelectDate(now);
		// appTemp2.setIsValid("01");
		// appTemp2.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp2.setNodeStat("00");
		// appTemp2.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		// this.tdscSelectService.saveBlockSelectApp(appTemp2);
		//
		// // A�๫֤�� ���Ȱ��ű� �ύ����Ҫ��֤�����Ͷ���֪ͨ����������ύ�����ܷ��Ͷ���
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>������Ϣ�ķ�����ʼ����");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp2);
		// // logger.info("===========>������Ϣ�ķ������õ��ý���"+returnMsg);
		// // }
		//
		// // ���Ļ򱣴������
		// TdscBlockSelectApp appTemp3 = new TdscBlockSelectApp();
		// appTemp3.setAppId(appId);
		// appTemp3.setSelectedId(notaryId);
		// appTemp3.setSelectType("01");
		// appTemp3.setActivityDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
		// appTemp3.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("bidEvaLoc")));
		// appTemp3.setUnlockTime(nowTime);
		// appTemp3.setSelectUser(user.toString());
		// appTemp3.setSelectDate(now);
		// appTemp3.setIsValid("01");
		// appTemp3.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp3.setNodeStat("00");
		// appTemp3.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		// this.tdscSelectService.saveBlockSelectApp(appTemp3);
		// // A�๫֤�� ���Ȱ��ű� �ύ����Ҫ��֤�����Ͷ���֪ͨ����������ύ�����ܷ��Ͷ���
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>������Ϣ�ķ�����ʼ����");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp3);
		// // logger.info("===========>������Ϣ�ķ������õ��ý���"+returnMsg);
		// // }
		//
		// }
		// // ���ݳ��÷�ʽ���湫֤����Ϣ--����
		// if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
		// // ���ݻظ���ֹʱ��
		// String replyTime = tdscSelectService.setReplyDeadLine();
		// Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		// // nowTime.setHours(07);
		// // nowTime.setMinutes(00);
		// // nowTime.setSeconds(00);
		// nowTime = tidy4AnyTimestamp(nowTime, 07, 0, 0);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		//
		//
		// // ���Ļ򱣴�������
		// TdscBlockSelectApp appTemp2 = new TdscBlockSelectApp();
		// appTemp2.setAppId(appId);
		// appTemp2.setSelectedId(notaryId);
		// appTemp2.setSelectType("01");
		// appTemp2.setActivityDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
		// appTemp2.setActivityLoc((String)DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_HOUSE_GP).get(request.getParameter("auctionLoc")));
		// appTemp2.setUnlockTime(nowTime);
		// appTemp2.setSelectUser(user.toString());
		// appTemp2.setSelectDate(now);
		// appTemp2.setIsValid("01");
		// appTemp2.setSelectNum(tdscSelectService.getOrderNo());
		// appTemp2.setNodeStat("00");
		// appTemp2.setReplyDeadline(DateUtil.string2Timestamp(replyTime,DateUtil.FORMAT_DATETIME));
		// this.tdscSelectService.saveBlockSelectApp(appTemp2);
		// // A�๫֤�� ���Ȱ��ű� �ύ����Ҫ��֤�����Ͷ���֪ͨ����������ύ�����ܷ��Ͷ���
		// // if (tdscNotaryInfo.getNotaryContactMobile()!= null) {
		// // logger.info("===========>������Ϣ�ķ�����ʼ����");
		// // String returnMsg = smsService.sendNotaryMessage(tdscNotaryInfo, tdscBlockPlanTable,appTemp2);
		// // logger.info("===========>������Ϣ�ķ������õ��ý���"+returnMsg);
		// // }
		// }
		// }

		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * �ƶ����Ȱ��ű����ڹ��� 20080104*
	 */
	public ActionForward toScheduletableDateModify_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		// String statusId = request.getParameter("statusId");
		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// ��ѡ����
			// ͨ��selectTypeΪ01��appID��ѯ��Ӧ�Ĺ�֤����Ϣ
			List blockSelectList = tdscSelectService.findBlockSelectList("01", appId);
			if (null != blockSelectList && blockSelectList.size() > 0) {
				TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) blockSelectList.get(0);
				TdscNotaryInfo tdscNotaryInfo = tdscSelectService.queryNotaryInfoBynotaryId(tdscBlockSelectApp.getSelectedId());
				request.setAttribute("tdscNotaryInfo", tdscNotaryInfo);
			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// �ж�TDSC_BLOCK_TRAN_APP���Ƿ�Ϊ��,ȡ��MarginEndDate��ֵ
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
			// ���ÿ�м���Ӧ��NODE_ID , NODE_STAT
			Map statMap = new HashMap();
			statMap = commonFlowService.getAppNodeStatMap(appId);
			if (statMap.size() > 0) {
				request.setAttribute("statMap", statMap);
			}
		}
		request.setAttribute("appId", appId);
		// ��ò�ѯ����
		TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		baseCondition.setAppId(appId);
		// ���ù�������ȡ�ù�����Ϣ
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
		String mode = (String) commonInfo.getTransferMode();
		request.setAttribute("mode", mode);
		// ���ݳ��÷�ʽѡ������Ǹ�ҵ��DIC_TRANSFER_TENDER = "3107"�б� DIC_TRANSFER_AUCTION = "3103"���� DIC_TRANSFER_LISTING = "3104"����
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyGp");
		}
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyPm");
		}
		if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyZb");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * �Թ���Ϊ��λ �ƶ����Ȱ��ű����ڹ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toScheduletableDateModify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
		planTableCondition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);

		String mode = tdscBlockPlanTable.getTransferMode();

		TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
		tranAppCondition.setPlanId(planId);

		List appIdList = new ArrayList();
		List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
		if (tranAppList != null && tranAppList.size() > 0) {
			for (int i = 0; i < tranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tranAppList.get(i);
				appIdList.add(tdscBlockTranApp.getAppId());
			}
		}
		// ��ѯ�ؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List tdscBlockAppViewList = null;
		String appId = "";
		if (appIdList != null && appIdList.size() > 0) {
			condition.setAppIdList(appIdList);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
				appId = tdscBlockAppView.getAppId();
				if (tdscBlockAppView.getMarginEndDate() != null) {
					tdscBlockTranApp.setMarginEndDate(tdscBlockAppView.getMarginEndDate());
				}
				if (mode == null || "".equals(mode)) {
					mode = tdscBlockAppView.getTransferMode();
				}
			}
		}
		Map statMap = new HashMap();
		statMap = commonFlowService.getAppNodeStatMap(appId);
		if (statMap.size() > 0) {
			request.setAttribute("statMap", statMap);
		}
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("tranAppList", tranAppList);
		request.setAttribute("mode", mode);
		request.setAttribute("transferMode", mode);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyPm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("dateModifyZb");
		} else {
			return mapping.findForward("dateModifyGp");
		}
	}

	/**
	 * �ƶ����Ȱ��ű��ι��� 20080104*
	 */
	public ActionForward toScheduletableFieldModify_old(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		// String statusId = request.getParameter("statusId");
		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// ��ѡ����
			List blockSelectList = tdscSelectService.findBlockSelectList("01", appId);
			if (null != blockSelectList && blockSelectList.size() > 0) {
				TdscBlockSelectApp tdscBlockSelectApp = (TdscBlockSelectApp) blockSelectList.get(0);
				TdscNotaryInfo tdscNotaryInfo = tdscSelectService.queryNotaryInfoBynotaryId(tdscBlockSelectApp.getSelectedId());
				request.setAttribute("tdscNotaryInfo", tdscNotaryInfo);

			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tempBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tempBlockPlanTable) {
				request.setAttribute("tempBlockPlanTable", tempBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
			// �ж�TDSC_BLOCK_TRAN_APP���Ƿ�Ϊ��,ȡ��MarginEndDate��ֵ
			TdscBlockTranApp tempBlockTranApp = tdscScheduletableService.findBlockTranAppInfo(appId);
			if (tempBlockTranApp != null) {
				request.setAttribute("tempBlockTranApp", tempBlockTranApp);
			}
			// ���ÿ�м���Ӧ��NODE_ID , NODE_STAT
			Map statMap = new HashMap();
			statMap = commonFlowService.getAppNodeStatMap(appId);
			if (statMap.size() > 0) {
				request.setAttribute("statMap", statMap);
				// TdscAppNodeStat tdscAppNodeStat4 = (TdscAppNodeStat)statMap.get(FlowConstants.FLOW_NODE_FILE_RELEASE);
				// String nodeStat4 = (String)tdscAppNodeStat4.getNodeStat();
			}
		}
		request.setAttribute("appId", appId);
		// ��ò�ѯ����
		TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
		baseCondition.setAppId(appId);
		// ���ù�������ȡ�ù�����Ϣ
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
		String mode = (String) commonInfo.getTransferMode();
		request.setAttribute("mode", mode);
		// ���ݳ��÷�ʽѡ������Ǹ�ҵ��DIC_TRANSFER_TENDER = "3107"�б� DIC_TRANSFER_AUCTION = "3103"���� DIC_TRANSFER_LISTING = "3104"����
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyGp");
		}
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyPm");
		}
		if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyZb");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * �Խ��Ȱ��ű�Ϊ��λ �ƶ����Ȱ��ű��ι���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toScheduletableFieldModify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
		planTableCondition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);

		String mode = tdscBlockPlanTable.getTransferMode();

		TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
		tranAppCondition.setPlanId(planId);

		List appIdList = new ArrayList();
		List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
		if (tranAppList != null && tranAppList.size() > 0) {
			for (int i = 0; i < tranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tranAppList.get(i);
				appIdList.add(tdscBlockTranApp.getAppId());
			}
		}
		// ��ѯ�ؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List tdscBlockAppViewList = null;
		String appId = "";
		if (appIdList != null && appIdList.size() > 0) {
			condition.setAppIdList(appIdList);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
				appId = tdscBlockAppView.getAppId();
				if (tdscBlockAppView.getMarginEndDate() != null) {
					tdscBlockTranApp.setMarginEndDate(tdscBlockAppView.getMarginEndDate());
				}
				if (mode == null || "".equals(mode)) {
					mode = tdscBlockAppView.getTransferMode();
				}
			}
		}
		// ���ÿ�м���Ӧ��NODE_ID , NODE_STAT
		Map statMap = new HashMap();
		statMap = commonFlowService.getAppNodeStatMap(appId);
		if (statMap.size() > 0) {
			request.setAttribute("statMap", statMap);
		}
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("tranAppList", tranAppList);
		request.setAttribute("mode", mode);
		request.setAttribute("transferMode", mode);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyPm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("fieldModifyZb");
		} else {
			return mapping.findForward("fieldModifyGp");
		}

	}

	/**
	 * �޸Ľ��Ȱ��ű�����-�б� ���� ����
	 * 
	 * @param appId
	 *            and mode 20080107*
	 */
	public ActionForward modifyScheduletableDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String mode = request.getParameter("mode");
		// String saveType = request.getParameter("saveType");
		// ����ActionForm
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// ��form�е�ֵ����bo����Ӧ���õؿ���Ȱ��ű�TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		tdscBlockPlanTable.setAppId(appId);
		bindObject(tdscBlockPlanTable, infoForm);
		// ���ݳ��÷�ʽѡ�񱣴���Ӧ��ֵ��3107�б� 3103���� 3104����
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			tdscBlockPlanTable.setListStartDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setListEndDate(DateUtil.string2Timestamp(request.getParameter("listEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidDate(DateUtil.string2Timestamp(request.getParameter("sceBidDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidLoc(request.getParameter("sceBidLoc"));
		} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			tdscBlockPlanTable.setAuctionDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setAuctionLoc(request.getParameter("auctionLoc"));
		} else {
			tdscBlockPlanTable.setTenderStartDate(DateUtil.string2Timestamp(request.getParameter("tenderStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setTenderEndDate(DateUtil.string2Timestamp(request.getParameter("tenderEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningLoc(request.getParameter("openingLoc"));
			tdscBlockPlanTable.setBidEvaDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setBidEvaLoc(request.getParameter("bidEvaLoc"));
		}
		// ������֤���ʽ�ֹʱ�䡱���浽TDSC_BLOCK_TRAN_APP����
		String marginEndDat = request.getParameter("marginEndDat");
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp.setMarginEndDate(DateUtil.string2Timestamp(marginEndDat, DateUtil.FORMAT_DATETIME));
		// ִ�в�������
		tdscScheduletableService.modifyPlanTableInfo(tdscBlockPlanTable, appId, mode, tdscBlockTranApp);
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * �޸Ľ��Ȱ��ű���-�б� ���� ����
	 * 
	 * @param appId
	 *            and mode 20080107*
	 */
	public ActionForward modifyScheduletableField(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String mode = request.getParameter("mode");
		// ����ActionForm
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// ��form�е�ֵ����bo����Ӧ���õؿ���Ȱ��ű�TDSC_BLOCK_PLAN_TABLE
		TdscBlockPlanTable tdscBlockPlanTable = new TdscBlockPlanTable();
		tdscBlockPlanTable.setAppId(appId);
		bindObject(tdscBlockPlanTable, infoForm);
		// ���ݳ��÷�ʽѡ�񱣴���Ӧ��ֵ��3107�б� 3103���� 3104����
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
			tdscBlockPlanTable.setListStartDate(DateUtil.string2Timestamp(request.getParameter("listStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setListEndDate(DateUtil.string2Timestamp(request.getParameter("listEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidDate(DateUtil.string2Timestamp(request.getParameter("sceBidDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setSceBidLoc(request.getParameter("sceBidLoc"));
			tdscBlockPlanTable.setListLoc(request.getParameter("listLoc"));
		} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			tdscBlockPlanTable.setAuctionDate(DateUtil.string2Timestamp(request.getParameter("auctionDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setAuctionLoc(request.getParameter("auctionLoc"));
		} else {
			tdscBlockPlanTable.setTenderStartDate(DateUtil.string2Timestamp(request.getParameter("tenderStartDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setTenderEndDate(DateUtil.string2Timestamp(request.getParameter("tenderEndDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningDate(DateUtil.string2Timestamp(request.getParameter("openingDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setOpeningLoc(request.getParameter("openingLoc"));
			tdscBlockPlanTable.setBidEvaDate(DateUtil.string2Timestamp(request.getParameter("bidEvaDate"), DateUtil.FORMAT_DATETIME));
			tdscBlockPlanTable.setBidEvaLoc(request.getParameter("bidEvaLoc"));
		}
		// ������֤���ʽ�ֹʱ�䡱���浽TDSC_BLOCK_TRAN_APP����
		String marginEndDat = request.getParameter("marginEndDat");
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		tdscBlockTranApp.setMarginEndDate(DateUtil.string2Timestamp(marginEndDat, DateUtil.FORMAT_DATETIME));
		// ִ�в�������
		tdscScheduletableService.modifyPlanTableInfo(tdscBlockPlanTable, appId, mode, tdscBlockTranApp);
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * �޸Ľ��Ȱ��ű�����ؿ飩
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyPlantable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String saveType = request.getParameter("saveType");
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		String planId = infoForm.getPlanId();
		// ��ѯԭ���Ȱ��ű���Ϣ
		TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
		condition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(condition);

		this.bindObject(tdscBlockPlanTable, infoForm);

		String sjLinkMan = request.getParameter("sjLinkman");
		tdscBlockPlanTable.setUserId(sjLinkMan);

		List tranAppList = (List) tdscScheduletableService.queryTranAppList(planId);
		tdscScheduletableService.savePlanTable(tdscBlockPlanTable, tranAppList, infoForm.getMarginEndDate(), saveType, user);

		request.setAttribute("forwardType", "01");
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * �޸Ľ��Ȱ��ű� LISTҳ���С��ƶ����Ȱ��š���ť����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyPlantableInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		String statusFlow = request.getParameter("statusFlow");
		TdscBlockPlanTableCondition planTableCondition = new TdscBlockPlanTableCondition();
		planTableCondition.setPlanId(planId);
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscScheduletableService.findBlockPlanTableInfo(planTableCondition);

		String mode = tdscBlockPlanTable.getTransferMode();

		TdscBlockTranAppCondition tranAppCondition = new TdscBlockTranAppCondition();
		tranAppCondition.setPlanId(planId);

		List appIdList = new ArrayList();
		List tranAppList = (List) tdscScheduletableService.queryTranAppList(tranAppCondition);
		if (tranAppList != null && tranAppList.size() > 0) {
			for (int i = 0; i < tranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tranAppList.get(i);
				appIdList.add(tdscBlockTranApp.getAppId());
			}
		}
		// ��ѯ�ؿ���Ϣ
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		List tdscBlockAppViewList = null;
		String appId = "";
		if (appIdList != null && appIdList.size() > 0) {
			condition.setAppIdList(appIdList);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(0);
				appId = tdscBlockAppView.getAppId();
				if (tdscBlockAppView.getMarginEndDate() != null) {
					tdscBlockTranApp.setMarginEndDate(tdscBlockAppView.getMarginEndDate());
				}
			}
		}
		Map statMap = new HashMap();
		statMap = commonFlowService.getAppNodeStatMap(appId);
		if (statMap.size() > 0) {
			request.setAttribute("statMap", statMap);
		}
		// ����ORG_APP_ID��ѯЭ�����
		if (tdscBlockPlanTable != null) {
			String orgAppId = tdscBlockPlanTable.getOrgAppId();
			TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
			request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
			// if(StringUtils.isNotEmpty(orgAppId)){
			// TdscXbOrgApp tdscXbOrgApp = (TdscXbOrgApp)tdscXbOrgService.getOrgAppInfoById(orgAppId);
			// if(tdscXbOrgApp != null){
			// TdscXbOrgHistory tdscXbOrgHistory = this.tdscXbOrgService.getNowOrgInfo2OrgHistory();
			// tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
			// tdscXbOrgHistory.setOrgAppId(orgAppId);
			// request.setAttribute("tdscXbOrgHistory", tdscXbOrgHistory);
			// }
			// }
		}
		request.setAttribute("statusFlow", statusFlow);
		request.setAttribute("tdscBlockTranApp", tdscBlockTranApp);
		request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
		request.setAttribute("tranAppList", tranAppList);
		request.setAttribute("mode", mode);
		request.setAttribute("transferMode", mode);
		String isEmpty = "false";
		request.setAttribute("isEmpty", isEmpty);
		if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
			return mapping.findForward("tempSavePm");
		} else if (GlobalConstants.DIC_TRANSFER_TENDER.equals(mode) && null != mode) {
			return mapping.findForward("tempSaveZb");
		} else {
			return mapping.findForward("tempSaveGp");
		}
	}

	/**
	 * 20090626ʵʩ��������״̬�޸�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward sendBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		String forwardType = request.getParameter("forwardType");
		// ʵʩ��������״̬�޸�
		tdscScheduletableService.plantableSendBack(planId);

		request.setAttribute("forwardType", forwardType);
		return new ActionForward("scheduletable.do?method=queryAppListWithNodeId", true);
	}
	
	//У�����ݿ����Ƿ������ͬ��ʱ����ʱ�εķ���
	public ActionForward checkListEndDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String listEndDate = request.getParameter("listEndDate");
		String planId= request.getParameter("planId");
		String result="00";//��֤ͨ��
		if(!tdscScheduletableService.checkListingEndDate(listEndDate,planId)){
			result = "11";//��֤��ͨ��
		}
		pw.write(result);
		pw.close();
		return null;
	}
	public ActionForward checkTradeNum(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		String tradeNum = request.getParameter("tradeNum");
		String planId = request.getParameter("planId");
		String saveType = request.getParameter("saveType");
		String retString = "11";//��ʾ������
		
		String listEndDate = request.getParameter("listEndDate");
		
		if (tdscScheduletableService.checkTradeNumAjax(tradeNum, planId)) {
			retString = "00";// ��ʾ������
		}
		if(!tdscScheduletableService.checkListingEndDate(listEndDate,null)){
			retString = "22";
		}
		retString += "_" + saveType;
		pw.write(retString);
		pw.close();
		return null;
	}

	/**
	 * ��ӡ����ҳ��
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward toPrintGp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");

		if (null != planId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}

		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
		}
		return mapping.findForward("printGp");
	}

	/**
	 * ��������ҳ��
	 * 
	 * @param appId
	 *            lz 20090514*
	 */
	public ActionForward todaochuGp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");
		if (null != planId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}

		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
		}

		return mapping.findForward("todaochuGp");
	}

	/**
	 * ��ӡ����ҳ��
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward toPrintPm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		// String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");
		if (null != planId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("printPm");
	}

	/**
	 * ��������ҳ��
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward todaochuPm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		// String appId = request.getParameter("appId");
		String planId = request.getParameter("planId");
		if (null != planId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setPlanId(planId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setPlanId(planId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("todaochuPm");
	}

	/**
	 * ��ӡ�б�ҳ��
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward toPrintZb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("printZb");
	}

	/**
	 * �����б�ҳ��
	 * 
	 * @param appId
	 *            20071217*
	 */
	public ActionForward todaochuZb(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ҳ�����appId
		String appId = request.getParameter("appId");
		if (null != appId) {
			// ��ò�ѯ����
			TdscBaseQueryCondition baseCondition = new TdscBaseQueryCondition();
			baseCondition.setAppId(appId);
			// ���ù�������ȡ�ù�����Ϣ
			TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(baseCondition);
			if (commonInfo != null) {
				request.setAttribute("commonInfo", commonInfo);
			}
			// �жϳ��õؿ���Ȱ��ű��Ƿ�Ϊ��
			TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
			condition.setAppId(appId);
			TdscBlockPlanTable tdscBlockPlanTable = tdscScheduletableService.findBlockPlanTableInfo(condition);
			if (null != tdscBlockPlanTable) {
				request.setAttribute("tdscBlockPlanTable", tdscBlockPlanTable);
				String isEmpty = "false";
				request.setAttribute("isEmpty", isEmpty);
			}
		}
		return mapping.findForward("todaochuZb");
	}

	// private Timestamp tidy4AnyTimestamp(Timestamp timestamp,int hh,int mm,int ss){
	// if(timestamp!=null&&timestamp.toString().length()>10){
	// String dateStr = timestamp.toString().substring(0,10)+" "+hh+":"+mm+":"+ss;
	// timestamp=DateUtil.string2Timestamp(dateStr, DateUtil.FORMAT_DATETIME);
	// }
	// return timestamp;
	// }

	/**
	 * ҵ����ģ��--��ֹ���ײ�ѯ�б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return list_dq.jsp
	 * @throws Exception
	 */
	public ActionForward queryDqAppList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		int currentPage = 0;
		if (!StringUtils.isEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// ���ظ�ҳ���ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		ScheduletableInfoForm scheduletableInfoForm = (ScheduletableInfoForm) form;

		this.bindObject(condition, scheduletableInfoForm);
		condition.setBlockName(StringUtil.GBKtoISO88591(condition.getBlockName()));
		condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(condition.getBlockNoticeNo()));
		
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// ����û���½��������Ϣ
		List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
		if (quList != null && quList.size() == 1) {
			condition.setDistrictId(String.valueOf(quList.get(0)));
		}
		// ����ǰ״̬��ѯ

		// ������˵�ǰ״̬Ϊ������
		if ("00".equals(scheduletableInfoForm.getBlockAuditStatus())) {
			condition.setNodeId(null);
		} else {
			List statusList = new ArrayList();
			statusList.add("01");
			statusList.add("00");
			condition.setStatusList(statusList);
			List statusIdList = new ArrayList();
			statusIdList.add("9001");
			statusIdList.add("9002");
			condition.setStatusIdList2(statusIdList);
		}

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
		// ��ѯ
		PageList pageList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);
		if (pageList != null && pageList.getList() != null && pageList.getList().size() > 0) {

			List tempList = pageList.getList();
			List blockList = new ArrayList();
			// tempList������������׵ؿ�
			if (tempList != null && tempList.size() > 0) {
				for (int i = 0; i < tempList.size(); i++) {
					TdscBlockAppView commonInfo = (TdscBlockAppView) tempList.get(i);
					// һ���ؿ�õ�����б�blokcNodeList
					if (null != commonInfo) {
						if (null != commonInfo.getAppId()) {
							List blokcNodeList = commonFlowService.getAppDiagramActiveNodeList(commonInfo.getAppId());
							// ����б�blokcNodeList���ϳ�string�ַ���
							String nodeName = "";
							if (blokcNodeList != null && blokcNodeList.size() > 0) {
								for (int j = 0; j < blokcNodeList.size(); j++) {
									TdscAppNodeStat appStat = (TdscAppNodeStat) blokcNodeList.get(j);
									nodeName += appStat.getNodeName() + "��";
								}
							}
							if (!"".equals(nodeName) && nodeName.length() > 0) {
								nodeName = nodeName.substring(0, nodeName.length() - 1);
							}
							// string�ַ��������ؿ����commonInfo��nodeId��
							commonInfo.setNodeId(nodeName);
						}
					}
					// ������ĵؿ��������blockList��
					blockList.add(commonInfo);
				}
			}

			pageList.setList(blockList);
		}
		// ������˵�ǰ״̬Ϊ������
		if (StringUtils.isNotEmpty(condition.getBlockAuditStatus())) {
			condition.setNodeId("01");
		}
		// ��ѯ�����еĵ�ǰ�ڵ�
		String transferMode = condition.getTransferMode();
		if (StringUtils.isNotEmpty(transferMode)) {
			List nodeIdList = new ArrayList();
			if (GlobalConstants.DIC_TRANSFER_TENDER.equals(transferMode)) {
				nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("01");
			}
			if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(transferMode)) {
				nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("02");
			}
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
				nodeIdList = this.appFlowService.queryNodeListWithBlockTransfer("03");
			}
			request.setAttribute("nodeIdList", nodeIdList);// ��������ѯ�б�
		}

		condition.setBlockName(StringUtil.ISO88591toGBK(condition.getBlockName()));
		//condition.setBlockNoticeNo(StringUtil.GBKtoISO88591(condition.getBlockNoticeNo()));
		request.setAttribute("queryAppList", pageList);// ��������ѯ�б�
		request.setAttribute("queryAppCondition", condition);
		return mapping.findForward("endTradeAppList");
	}

	/**
	 * ��ֹĳ�ؿ�Ľ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward endTradeAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = (String) request.getParameter("appId");
		// �����û�
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String textOpen = (String) request.getParameter("textOpen");
		if (textOpen != null && !"".equals(textOpen)) {
			// textOpen = new String(textOpen.getBytes("ISO-8859-1"),"GBK");
		}

		// �����ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ��ѯ���
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		// ��¼�������
		tdscBlockAppView.setTempStr(textOpen);
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		// ��ֹ����
		String returnString = "00";// ����ʧ��
		if (tdscBlockAppView != null) {
			boolean actionResult = tdscScheduletableService.endTradeByAppId(tdscBlockAppView, user);
			if (actionResult) {
				returnString = "01";// �����ɹ�
			}
		}
		pw.write(returnString);
		pw.close();
		return null;
	}

	public ActionForward endTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = (String) request.getParameter("appId");
		// �����û�
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String textOpen = (String) request.getParameter("textOpen");
		if (textOpen != null && !"".equals(textOpen)) {
			// textOpen = new String(textOpen.getBytes("ISO-8859-1"),"GBK");
		}

		// �����ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		// ��ѯ���
		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);
		// ��¼�������
		tdscBlockAppView.setTempStr(textOpen);
		// ��ֹ����
		tdscScheduletableService.endTradeByAppId(tdscBlockAppView, user);
		ScheduletableInfoForm scheduletableInfoForm = new ScheduletableInfoForm();
		return queryDqAppList(mapping, scheduletableInfoForm, request, response);
	}

	/**
	 * ��ת����֤�ؿ鹫��ŵ�ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkBlockNoticeNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����ҳ�洫�ݵ�ҵ��ID
		String appId = request.getParameter("appId");
		// �����ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(condition);

		request.setAttribute("tdscBlockAppView", tdscBlockAppView);

		return mapping.findForward("checkBlockNoticeNo");
	}

	/**
	 * ���ݿⷽʽ ��ѯĳ��ĳ�¹����ڼ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBusinessDay_DB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;
		// ��ò�ѯ���ꡢ��
		String tempYear = (String) infoForm.getConditionYear();
		String tempMonth = (String) infoForm.getConditionMonth();
		TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();

		// ���Ϊ�գ����ѯ���µĹ����������
		X_BusinessDayService bDayService = new X_BusinessDayService();
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM");
		String tempDate = (String) sdm.format(d);

		if (tempYear == null || "".equals(tempYear)) {
			tempYear = tempDate.split("-")[0];
		}
		if (tempMonth == null || "".equals(tempMonth)) {
			tempMonth = tempDate.split("-")[1];
		}
		// ��ѯ
		Map dateMap = (Map) bDayService.queryDayInfoByMonth(tempYear, tempMonth);
		condition.setConditionYear(tempYear);
		condition.setConditionMonth(tempMonth);

		request.setAttribute("condition", condition);
		request.setAttribute("dateMap", dateMap);

		return mapping.findForward("businessDayMap");
	}

	/**
	 * ���ݿⷽʽ ���³�ʼ������������ڰ��ţ��ָ�����ʼ״̬
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward creatDateAgain_DB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		X_BusinessDayService bDayService = new X_BusinessDayService();

		if (bDayService.reinitBusinessDay()) {
			request.setAttribute("saveMessage", "���óɹ�!");
		} else {
			request.setAttribute("saveMessage", "����ʧ��!");
		}
		return queryBusinessDay_DB(mapping, form, request, response);
	}

	/**
	 * �÷�������������ѯ�������ں�ļ��������� dateString��ʽΪ��yyyy-mm-dd,2,3,10,8,*
	 * 
	 * dateString����Ը�������֣�����ѯ������ڣ�
	 * 
	 * ���ص�����Ϊ��a,b,c,d�� ���� a = �������� + 2�������գ� b = a + 3�������գ� c = b + 10�������գ� d = c + 8��������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryDateByMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// dateString��ʽΪ��yyyy-mm-dd,2,3,10,8
		String dateString = (String) request.getParameter("dateString");
		String[] tempShuZu = dateString.split(",");
		// ���������õ����
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		int count = 1;
		String firstDate = (String) tempShuZu[0];
		String returnStr = "";
		X_BusinessDayService bDayService = new X_BusinessDayService();
		if (null != tempShuZu) {
			while (count < tempShuZu.length) {
				firstDate = (String) bDayService.queryDate_S(firstDate, tempShuZu[count]);
				count++;
				returnStr += firstDate + ",";
			}
		}
		System.out.println("�����ַ���===>" + returnStr);
		pw.write(returnStr);
		pw.close();
		return null;
	}

	public ActionForward modifyBusinessDay_DB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ҳ������Ĺ����ա����ڵ���ϸ����
		ScheduletableInfoForm infoForm = (ScheduletableInfoForm) form;

		String businessStr = (String) infoForm.getBusinessStr();
		String holidayStr = (String) infoForm.getHolidayStr();
		// ���칤����
		X_BusinessDayService bDayService = new X_BusinessDayService();
		// �������ա���������
		String[] businessDay = businessStr.split(",");
		String[] holidayDay = holidayStr.split(",");
		// �޸Ĺ����ա����ڰ���
		bDayService.updateDateToHoliday(holidayDay);
		bDayService.updateDateToWorkDay(businessDay);
		return queryBusinessDay_DB(mapping, form, request, response);
	}

}
