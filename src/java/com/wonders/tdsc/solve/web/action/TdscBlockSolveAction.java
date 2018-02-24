package com.wonders.tdsc.solve.web.action;

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
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockSolve;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.solve.service.TdscBlockSolveService;
import com.wonders.tdsc.solve.web.form.TdscBlockSolveForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBlockSolveAction extends BaseAction {
	private TdscBlockSolveService tdscBlockSolveService;
	private CommonQueryService commonQueryService;
	private AppFlowService appFlowService;
	private CommonFlowService commonFlowService;

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setTdscBlockSolveService(TdscBlockSolveService tdscBlockSolveService) {
		this.tdscBlockSolveService = tdscBlockSolveService;
	}

	public ActionForward queryTradeBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TdscBlockSolve tdscBlockSolve = new TdscBlockSolve();
		bindObject(tdscBlockSolve, form);

		int currentPage = 0;
		if (!StringUtils.isEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// ���ظ�ҳ���ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		TdscBlockSolveForm tdscBlockForm = (TdscBlockSolveForm) form;

		this.bindObject(condition, tdscBlockForm);
		condition.setBlockName(StringUtil.GBKtoISO88591(tdscBlockForm.getBlockName()));
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		condition.setOrderType("desc");
		condition.setOrderKey("appId");

		// ����û���½��������Ϣ
		List quList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
		if (quList != null && quList.size() == 1) {
			condition.setDistrictId(String.valueOf(quList.get(0)));
		}
		// //����ǰ״̬��ѯ
		//
		// //������˵�ǰ״̬Ϊ������
		if ("00".equals(tdscBlockForm.getBlockAuditStatus())) {
			condition.setNodeId(null);
		} else {
			List statusList = new ArrayList();
			statusList.add("01");
			statusList.add("00");
			condition.setStatusList(statusList);

			List statusIdList = new ArrayList();
			statusIdList.add("1601");
			statusIdList.add("1301");
			statusIdList.add("1801");
			condition.setStatusIdList(statusIdList);

			List statusId2List = new ArrayList();
			statusId2List.add("9001");
			statusId2List.add("9002");
			condition.setStatusIdList2(statusId2List);
		}

		//
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
		PageList pageList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);
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

		request.setAttribute("queryAppList", pageList);// ��������ѯ�б�

		condition.setBlockName(StringUtil.ISO88591toGBK(condition.getBlockName()));
		request.setAttribute("queryAppCondition", condition);

		return mapping.findForward("solveListNotices");
	}

	public ActionForward addSolve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String userId = user.getUserId();
		TdscBlockSolve tdscBlockSolve = new TdscBlockSolve();
		bindObject(tdscBlockSolve, form);
		tdscBlockSolveService.saveIt(tdscBlockSolve, userId);
		tdscBlockSolveService.saveFileToDaPing(tdscBlockSolve);
		return mapping.findForward("saveSloveOK");
	}

	public ActionForward gotoAddSolve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strBlockId = request.getParameter("blockId");
		String strBlockNo = request.getParameter("blockNo");
		String strBlockName = request.getParameter("blockName");
		String strPlanId = request.getParameter("planId");

		request.setAttribute("blockId", strBlockId);
		request.setAttribute("blockNo", strBlockNo);
		request.setAttribute("blockName", strBlockName);
		request.setAttribute("planId", strPlanId);

		return mapping.findForward("gotoAddSolve");
	}

	public ActionForward showSolveHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strBlockId = request.getParameter("blockId");

		// ������Ǹ�ҵ��Ա�����, ������ʾ; ���ڴ���Ϊȫ���г�.
		UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String userId = user.getUserId();

		List history = tdscBlockSolveService.showSolveHistory(strBlockId, userId);

		request.setAttribute("solveHistory", history);
		// request.setAttribute("blockNo", StringUtil.ISO88591toGBK(strBlockNo));
		// request.setAttribute("blockName", StringUtil.ISO88591toGBK(strBlockName));

		return mapping.findForward("showSolveHistory");
	}

}
