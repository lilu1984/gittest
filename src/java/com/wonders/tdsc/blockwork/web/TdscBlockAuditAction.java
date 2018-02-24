package com.wonders.tdsc.blockwork.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.web.form.TdscBlockForm;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockMaterial;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPjxxInfo;
import com.wonders.tdsc.bo.TdscBlockQqjcInfo;
import com.wonders.tdsc.bo.TdscBlockRemisemoneyDefray;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscBlockUsedInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscBlockMaterialCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.lob.bo.TdscEsClob;
import com.wonders.tdsc.presell.web.form.TdscBlockPresellForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBlockAuditAction extends BaseAction {
	// ͨ�ò�ѯservice
	private CommonQueryService		commonQueryService;
	// ���ػ�����Ϣservice
	private TdscBlockInfoService	tdscBlockInfoService;
	// ���̿���service
	private AppFlowService			appFlowService;

	// ����Ϊ������ �ʸ�֤����
	private TdscBidderAppService	tdscBidderAppService;

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	/**
	 * ��ѯ����ҵ���б�
	 */
	public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		// ���������ص����б�ҳ��
		// request.setAttribute("condition", sqCondition);
		this.bindObject(sqCondition, tdscBlockForm);
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

		String nodeId = "01";

		if (sqCondition.getBlockName() != null && !"".equals(sqCondition.getBlockName())) {
			sqCondition.setBlockName(StringUtil.GBKtoISO88591(sqCondition.getBlockName()));
		}
		if (sqCondition.getAuditedNum() != null && !"".equals(sqCondition.getAuditedNum())) {
			sqCondition.setAuditedNum(StringUtil.GBKtoISO88591(sqCondition.getAuditedNum()));
		}
		sqCondition.setNodeList(nodeList); // �����û�Ȩ�޲�ѯ�����б�
		sqCondition.setOrderKey("actionDateBlock");// �������ֶ�����
		sqCondition.setOrderType("desc");
		sqCondition.setNodeId(nodeId);
		sqCondition.setDistrictIdList(districtIdList);
		sqCondition.setCurrentPage(currentPage);
		sqCondition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		sqCondition.setUser(user);
		// ����ͨ�ò�ѯ ��ѯ�б���Ϣ
		List queryAppList = commonQueryService.queryTdscBlockAppViewListWithoutNode(sqCondition);

		request.setAttribute("nodeId", nodeId);
		// �����ؿ��ѯ
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		// �趨status��ѯ��δ���׼�������ֹ����Ϣ
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };

		// �����ؿ��ѯ����
		TdscBlockInfoCondition cbCondition = new TdscBlockInfoCondition();
		this.bindObject(cbCondition, tdscBlockForm);
		cbCondition.setStatus(status);

		// �ж��Ƿ��ǹ���Ա
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			cbCondition.setUserId(user.getUserId());
		}

		// cbCondition.setCurrentPage(currentPage);
		// cbCondition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
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

		// ����2����ѯ���List
		if (queryAppList != null && queryAppList.size() > 0) {
			for (int i = 0; i < queryAppList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) queryAppList.get(i);
				// ����ǡ����롱���ڵ����ݣ�ֻ��¼�����ݵ��û����Կ���
				if ("0101".equals(appView.getStatusId())) {
					// ������ǹ���Ա����ֻ�ܲ�ѯ���û����������
					if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
						if (user.getUserId().equals(appView.getUserId())) {
							cbBlockList.add(appView);
						}
					} else {
						cbBlockList.add(appView);
					}
				} else {
					cbBlockList.add(appView);
				}
			}
		}

		int pageSize = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue();
		PageList PageList = PageUtil.getPageList(cbBlockList, pageSize, currentPage);

		request.setAttribute("queryAppList", PageList);// �����б�
		return mapping.findForward("listAll");
	}

	/**
	 * �����ؿ�����
	 */
	public ActionForward queryBlockInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡ��ǰ�û�
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ��ȡҳ�����
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		// �趨status��ѯ��δ���׼�������ֹ����Ϣ
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };
		String type = request.getParameter("type");

		// ���ظ���������ҳ�� ��ѯ����
		TdscBlockInfoCondition condition = new TdscBlockInfoCondition();
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		if (StringUtils.isBlank(type))
			this.bindObject(condition, tdscBlockForm);
		condition.setStatus(status);
		// �жϸ��û��Ƿ��ǹ���Ա������ǹ���Ա���ѯȫ�����ݣ�
		// ����ֻ��ѯ��1�����û��ύ�����ݣ�2���û�Ϊ�յ����ݣ��û�Ϊ�������������1Ϊ��ʷ���ݣ�2Ϊ������ϵͳ���͵����ݣ�
		// ��ð�ťȨ���б�
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// ת��ΪbuttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// �ж��Ƿ��ǹ���Ա
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// ���ظ���������ҳ�� ���ػ�����Ϣ�б�
		PageList blockInfoList = tdscBlockInfoService.findPageList(condition);

		request.setAttribute("blockInfoList", blockInfoList);// ��ѯ������Ϣ
		if (StringUtils.isBlank(type))
			request.setAttribute("TdscBlockCondition", condition);// ���ز�ѯ����

		return mapping.findForward("blockInfoList");

	}

	/**
	 * ���봢���ؿ�
	 */
	public ActionForward toBlockInfoAndPart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockId = request.getParameter("blockId");
		if (StringUtils.isNotBlank(blockId)) {
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(blockId);
			request.setAttribute("blockInfo", tdscBlockInfo);// ��ѯ������Ϣ
			request.setAttribute("tdscBlockPartList", tdscBlockInfoService.getTdscBlockPartList(tdscBlockInfo.getBlockId()));
		}
		return mapping.findForward("blockInfo");

	}

	/**
	 * ɾ�������ؿ鼰���ӵؿ� lz+
	 */
	public ActionForward deleteBlockInfoAndPart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strAppId = request.getParameter("appId");
		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setAppId(strAppId);
		TdscBlockAppView view = (TdscBlockAppView) commonQueryService.getTdscBlockAppView(cond);
		tdscBlockInfoService.delBlockInfoByBlockId(strAppId, view.getBlockId());
		
		return new ActionForward("blockAudit.do?method=queryAppListWithNodeId", true);
	}

	/**
	 * �����ؿ鱣��
	 */
	public ActionForward saveBlockInfoAndPart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡ��ǰ�û�
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String blockId = request.getParameter("blockId");
		// 1.ʹ��ActionForm����ҳ���ύ��ֵ
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;

		// 2.�������ػ�����Ϣ����
		TdscBlockInfo tdscBlockInfo = null;
		if (StringUtils.isNotBlank(blockId))
			// �ݴ�����������Ϣ
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());
		else
			tdscBlockInfo = new TdscBlockInfo();
		this.bindObject(tdscBlockInfo, tdscBlockForm);

		// 4.����ӵؿ���Ϣ(checkbox�򹳵�)
		BigDecimal totalArea = new BigDecimal(0);
		BigDecimal totalBuildingArea = new BigDecimal(0);
		List blockPartList = new ArrayList();
		String[] blockCodes = request.getParameterValues("blockCodes");
		if (blockCodes != null && blockCodes.length > 0) {
			for (int j = 0; j < blockCodes.length; j++) {
				TdscBlockPart tdscBlockPart = new TdscBlockPart();
				// tdscBlockPart.setBlockCode(tdscBlockForm.getBlockCodes()[j]);
				if (tdscBlockForm.getBlockAreas()[j] != null && !"".equals(tdscBlockForm.getBlockAreas()[j])) {
					tdscBlockPart.setBlockArea(new BigDecimal(tdscBlockForm.getBlockAreas()[j]));
				} else {
					tdscBlockPart.setBlockArea(new BigDecimal(0));
				}
				tdscBlockPart.setLandUseType(tdscBlockForm.getLandUseTypes()[j]);
				if (tdscBlockForm.getPlanBuildingAreas()[j] != null && !"".equals(tdscBlockForm.getPlanBuildingAreas()[j])) {
					tdscBlockPart.setPlanBuildingArea(new BigDecimal(tdscBlockForm.getPlanBuildingAreas()[j]));
				} else {
					tdscBlockPart.setPlanBuildingArea(new BigDecimal(0));
				}
				if (tdscBlockForm.getVolumeRates()[j] != null && !"".equals(tdscBlockForm.getVolumeRates()[j])) {
					tdscBlockPart.setVolumeRate(tdscBlockForm.getVolumeRates()[j]);
				}
				if (tdscBlockForm.getDensitys()[j] != null && !"".equals(tdscBlockForm.getDensitys()[j])) {
					tdscBlockPart.setDensity(tdscBlockForm.getDensitys()[j]);
				}
				if (tdscBlockForm.getGreeningRates()[j] != null && !"".equals(tdscBlockForm.getGreeningRates()[j])) {
					tdscBlockPart.setGreeningRate(tdscBlockForm.getGreeningRates()[j]);
				}
				if (tdscBlockForm.getServiceCharge()[j] != null && !"".equals(tdscBlockForm.getServiceCharge()[j])) {
					tdscBlockPart.setServiceCharge(tdscBlockForm.getServiceCharge()[j]);
				}
				blockPartList.add(tdscBlockPart);
				// ������������ܽ������
				totalArea = totalArea.add(tdscBlockPart.getBlockArea() == null ? new BigDecimal(0) : tdscBlockPart.getBlockArea());
				totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea() == null ? new BigDecimal(0) : tdscBlockPart.getPlanBuildingArea());
			}
		}

		// ������������ܽ������
		tdscBlockInfo.setTotalBuildingArea(totalBuildingArea);
		tdscBlockInfo.setTotalLandArea(totalArea);
		tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		// �����û�ID��Ϣ
		tdscBlockInfo.setUserId(user.getUserId());

		tdscBlockInfoService.saveBlockInfoAndPart(tdscBlockInfo, blockPartList);

		return new ActionForward("blockAudit.do?method=queryBlockInfoList&type=1", true);

	}

	/**
	 * �½���������
	 */
	public ActionForward toCreateNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String blockId = request.getParameter("blockId");
		String auditedNum = request.getParameter("auditedNum");
		request.setAttribute("auditedNum", auditedNum);
		request.setAttribute("blockId", blockId);
		if (StringUtils.isNotEmpty(blockId)) {
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findTdscBlockInfo(blockId);
			List blockPartList = tdscBlockInfoService.getTdscBlockPartList(blockId);

			Map retMap = new HashMap();
			retMap.put("tdscBlockInfo", tdscBlockInfo);
			retMap.put("blockPartList", blockPartList);
			request.setAttribute("tdscBlockInfo", tdscBlockInfo);
			request.setAttribute("blockPartList", blockPartList);
			request.setAttribute("retMap", retMap);
		}
		if ("1".equals(request.getParameter("flag"))) {
			return mapping.findForward("toApply");
		}
		return mapping.findForward("createNew");

	}

	/**
	 * �½�����
	 */
	public ActionForward toCreateApply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ���ظ���������ҳ�� ��ѯ����
		String locString = request.getParameter("locString");
		String blockId = request.getParameter("blockId");
		// List tdscBlockInfoList=null;
		// ����JSON�ַ���������MAP
		Map xmlJobDataMap = (Map) tdscBlockInfoService.tidyMap(locString);
		// ����MAP�е� BLOCK_ID����ѯ��Ӧ�ĵؿ���Ϣ
		if (xmlJobDataMap != null) {
			String object_block_id = (String) xmlJobDataMap.get("BLOCK_ID");
			if (StringUtils.isNotEmpty(object_block_id)) {
				TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findTdscBlockInfo(object_block_id);
				// List blockPartList = tdscBlockInfoService.getTdscBlockPartList(object_block_id);

				Map retMap = new HashMap();
				retMap.put("tdscBlockInfo", tdscBlockInfo);
				// retMap.put("blockPartList", blockPartList);
				request.setAttribute("tdscBlockInfo", tdscBlockInfo);
				// request.setAttribute("blockPartList", blockPartList);
				request.setAttribute("retMap", retMap);
			}
		}
		// ����ԭ�е�blockId
		request.setAttribute("blockId", blockId);
		request.setAttribute("xmlJobDataMap", xmlJobDataMap);
		// request.setAttribute("tdscBlockInfoList",tdscBlockInfoList);// ��ѯ������Ϣ

		return mapping.findForward("createApply");
	}

	/**
	 * ���������б�
	 */
	public ActionForward toAddList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String statusId = request.getParameter("statusId");
		String nodeId = request.getParameter("nodeId");
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// �趨status��ѯ��δ���׼�������ֹ����Ϣ
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };

		// ���ظ���������ҳ�� ��ѯ����
		TdscBlockInfoCondition condition = new TdscBlockInfoCondition();
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		this.bindObject(condition, tdscBlockForm);
		condition.setStatus(status);
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// ���ظ���������ҳ�� ���ػ�����Ϣ�б�
		PageList blockInfoList = tdscBlockInfoService.findPageList(condition);

		request.setAttribute("blockInfoList", blockInfoList);// ��ѯ������Ϣ
		request.setAttribute("TdscBlockCondition", condition);// ���ز�ѯ����
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("statusId", statusId);
		return mapping.findForward("newList");
	}

	/**
	 * �����½�����ҳ��
	 */
	public ActionForward newRemise(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String blockId = request.getParameter("blockId");
		String blockType = request.getParameter("blockType");
		String nodeId = request.getParameter("nodeId");
		String statusId = request.getParameter("statusId");

		if (!FlowConstants.FLOW_STATUS_AUDIT_INIT.equals(statusId)) {
			return mapping.findForward("tdscError");
		}

		// ��ؿ��blockId��Ϊ�� ��ת����Ӧ�� ��ҵ���� ���� ��ҵ����ҳ��
		if (StringUtils.isNotEmpty(blockId)) {
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findTdscBlockInfo(blockId);
			List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);

			Map retMap = new HashMap();
			retMap.put("tdscBlockInfo", tdscBlockInfo);

			request.setAttribute("retMap", retMap);
			request.setAttribute("tdscBlockUsedInfoList", tdscBlockUsedInfoList);
			request.setAttribute("nodeId", nodeId);

			// ����blockType������ȷ����������Ӧ��jspҳ��
			if (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY == Integer.parseInt(blockType)) {
				return mapping.findForward("IndInfo");
			}
			return mapping.findForward("ComInfo");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * �ļ�����
	 */
	public ActionForward fileLink(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");

		if (StringUtils.isNotEmpty(id)) {
			TdscEsClob tdscEsClob = this.tdscBlockInfoService.findClobContent(id);
			if (null != tdscEsClob) {
				request.setAttribute("wordFile", tdscEsClob.getClobContent());
			}
		}

		return mapping.findForward("fileLink");
	}

	/**
	 * ��������(�˻��޸�,������)ҳ��
	 */
	public ActionForward toApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String blockId = request.getParameter("blockId");
		String appId = request.getParameter("appId");
		//�ж��Ƿ�������
		String zulin = request.getParameter("zulin");
		if(StringUtils.isNotBlank(zulin)){
			request.setAttribute("zulin", zulin);
		}
		List bankDicList = tdscBlockInfoService.queryBankDicList();

		// ��ѯ���ػ�����Ϣ
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(blockId);
		TdscBidderApp tdscBidderApp = this.tdscBlockInfoService.getTdscBidderAppByAppId(appId);
		TdscBidderPersonApp tdscBidderPersonApp = null;
		if (tdscBidderApp != null) {
			tdscBidderPersonApp = this.tdscBlockInfoService.getTdscBidderPersonAppByBidderId(tdscBidderApp.getBidderId());
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		// ��ѯ���ؽ�����Ϣ
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		TdscEsClob tdscEsClob = new TdscEsClob();
		if (StringUtils.isNotEmpty(appId)) {
			tdscBlockTranApp = tdscBlockInfoService.findTdscBlockTranApp(appId);
			// ����滮���Ҫ��
			// tdscBlockTranApp = tdscBlockInfoService.reTidyTranApp(tdscBlockTranApp);
			tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
		}
		// ��ѯ����ʹ����Ϣ�б�
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);
		// ��ѯǩ�ղ����б�
		List tdscBlockMaterialList = tdscBlockInfoService.findMaterialListByAppId(StringUtils.trimToEmpty(appId));
		// ��ǩ�ղ����б����Ϣ���浽Map��һ�� ����ҳ��checkbox�Ƿ�ѡ��
		Map tdscBlockMaterialMap = new HashMap();
		if (tdscBlockMaterialList != null) {
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();
			for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
				tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
				// �Բ�������Ϊ���� �洢������Ϣ
				tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
			}
		}
		// ��ѯ�ӵؿ���Ϣ
		List blockPartList = (List) tdscBlockInfoService.getTdscBlockPartList(blockId);
		List TdscBlockRemisemoneyDefrayList = (List) tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockId);
		// �鶯̬ǩ�ղ���
		if (StringUtils.isNotEmpty(appId)) {
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);
			request.setAttribute("tdscOtherBlockMaterialList", tdscOtherBlockMaterialList);
		}

		TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo) tdscBlockInfoService.getQqjcInfoByblockId(blockId);
		// ��ѯ��ʷ���
		Map opnninfo = new HashMap();
		if (StringUtils.isNotEmpty(appId)) {
			opnninfo = this.appFlowService.queryOpnnInfo(appId);
		}
		// �����ݴ�ҳ����Ҫ����Ϣ
		Map retMap = new HashMap();
		if (tdscEsClob != null) {
			// String setClobContent = (StringUtil.GBKtoISO88591(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
			// String setClobContent_1 = (StringUtil.ISO88591toGBK(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
		}
		// ����佨��Ϣ
		TdscBlockPjxxInfo tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfoByBlockId(blockId);

		retMap.put("tdscEsClob", tdscEsClob);
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);
		retMap.put("blockPartList", blockPartList);
		retMap.put("tdscBlockRemisemoneyDefrayList", TdscBlockRemisemoneyDefrayList);
		retMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);

		request.setAttribute("retMap", retMap);
		request.setAttribute("blockPartList", blockPartList);
		// request.setAttribute("tdscBlockRemisemoneyDefrayList", TdscBlockRemisemoneyDefrayList);
		request.setAttribute("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
		request.setAttribute("tdscBlockUsedInfoList", tdscBlockUsedInfoList);
		request.setAttribute("tdscBlockMaterialMap", tdscBlockMaterialMap);

		request.setAttribute("opnninfo", opnninfo);
		request.setAttribute("appId", appId);

		request.setAttribute("bankDicList", bankDicList);
		//�������༭����
		

		if (StringUtils.isNotBlank(blockId)) {
			List fileList = tdscBlockInfoService.findFileRefByBusId(blockId, GlobalConstants.BLOCK_FILE);
			request.setAttribute("fileList", fileList);
		}

		return mapping.findForward("ComInfo");

	}


	/**
	 * ȡ������
	 */
	public ActionForward cancelApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");

		// �趨currentPageΪ��0ҳ
		request.setAttribute("currentPage", "0");

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ɾ���ؿ���Ϣ
		if (appId != null) {
			// ����������Ϣ�����ؽ�����Ϣ��״̬
			this.tdscBlockInfoService.cancelApp(appId);

			// Ϊ�����趨��Ӧ��ֵ
			TdscAppFlow appFlow = new TdscAppFlow();
			appFlow.setAppId(appId);
			appFlow.setResultId(FlowConstants.FLOW_AUDIT_RESULT_CANCEL_APP);
			appFlow.setUser(user);

			this.appFlowService.saveOpnn(appFlow);
		}
		return this.queryAppListWithNodeId(mapping, null, request, response);
	}

	/**
	 * <<<<<<< .mine ======= �޸ĳ��õؿ鼰�ݴ����Ϣ
	 */
	public ActionForward modifyBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 1.ʹ��ActionForm����ҳ���ύ��ֵ
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		tdscBlockForm.setMarginEndDate(DateUtil.string2Timestamp(DateUtil.date2String(tdscBlockForm.getMarginEndDate(), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATETIME));

		// 2.�������ػ�����Ϣ����
		TdscBlockInfo tdscBlockInfo = null;
		if (tdscBlockForm != null && !tdscBlockForm.getBlockId().equals("")) {
			// �ݴ�����������Ϣ
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());
		} else {
			// �½�����������Ϣ
			tdscBlockInfo = new TdscBlockInfo();
		}
		tdscBlockForm.setAuditedNum("������[" + tdscBlockForm.getAuditedNum1() + "]" + tdscBlockForm.getAuditedNum2() + "��");
		this.bindObject(tdscBlockInfo, tdscBlockForm);
		if (tdscBlockInfo != null && tdscBlockInfo.getBlockId().equals("")) {
			tdscBlockInfo.setBlockId(null);
		}
		// 3.���ؽ��ױ� ����ҵ��ID
		TdscBlockTranApp tdscBlockTranApp = null;
		TdscEsClob tdscEsClob = new TdscEsClob();
		if ("".equals(tdscBlockForm.getAppId()) || tdscBlockForm.getAppId() == null) {
			tdscBlockTranApp = new TdscBlockTranApp();
			tdscBlockTranApp.setInitType("01");
			tdscBlockTranApp.setHasSelectBNotary("00");
			tdscBlockTranApp.setHasSelectCNotary("00");
			tdscBlockTranApp.setHasSelectCompere("00");
			tdscBlockTranApp.setHasSelectSpecialist("00");
			tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_INIT);
			tdscBlockTranApp.setIfPublish("0");
		} else {
			tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(tdscBlockForm.getAppId());
			// ���桰�滮���Ҫ�㡱��clob�ֶ�
			if (tdscBlockForm.getClobContent() != null && !"".equals(tdscBlockForm.getClobContent())) {
				tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
				if (tdscEsClob == null) {
					tdscEsClob = new TdscEsClob();
				}
			}
		}
		// 4.���Ϊ��������Ƴ��õؿ� ��������������Ϣ
		TdscBidderApp tdscBidderApp = null;
		TdscBidderPersonApp tdscBidderPersonApp = null;
		String transferMode = tdscBlockForm.getTransferMode();
		String isPurposeBlock = tdscBlockForm.getIsPurposeBlock();
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
			if (GlobalConstants.DIC_ID_REVIEW_RESULT_YES.equals(isPurposeBlock)) {
				String yixiangPersonName = tdscBlockForm.getYixiangPersonName();
				tdscBidderApp = new TdscBidderApp();
				// ��������
				tdscBidderApp.setBidderType("1");
				tdscBidderApp.setIsPurposePerson(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
				tdscBidderApp.setBidderId(tdscBlockForm.getBidderId());

				// ��ǰҵ��Աֻ�ܹ���ǰ�ľ�����
				tdscBidderApp.setUserId(user.getUserId());

				tdscBidderPersonApp = new TdscBidderPersonApp();
				tdscBidderPersonApp.setBidderPersonId(tdscBlockForm.getBidderPersonId());
				tdscBidderPersonApp.setBidderName(yixiangPersonName);
			} else if (GlobalConstants.DIC_ID_BREVIEW_RESULT_NO.equals(isPurposeBlock)) {
				tdscBlockForm.setIsPurposeBlock("0");
			}
		} else {
			tdscBlockForm.setIsPurposeBlock(null);
		}
		this.bindObject(tdscEsClob, tdscBlockForm);

		// ���tdscBlockTranApp֮���ٵ���set����pingguMethod����
		this.bindObject(tdscBlockTranApp, tdscBlockForm);
		String[] pingguMethods = request.getParameterValues("pingguMethod");
		String pingguMethod = "";
		if (pingguMethods != null && pingguMethods.length > 0) {
			for (int i = 0; i < pingguMethods.length; i++) {
				if (i == 0) {
					pingguMethod += pingguMethods[i];
				} else {
					pingguMethod += "," + pingguMethods[i];
				}
			}
		}
		tdscBlockTranApp.setPingguMethod(pingguMethod);

		// ȡ�õ׼�
		String upset = request.getParameter("dj");
		if (upset != null && upset.equals("")) {
			// ��������е׼��޸�Ϊ��
			tdscBlockTranApp.setUpset(null);
		}
		// 3.����Ѿ�ѡ���ǩ�ղ����б�(checkbox�򹳵�)
		List saveMateriallist = new ArrayList();
		String[] checkbox = request.getParameterValues("choose");

		if (checkbox != null) {
			for (int i = 0; i < checkbox.length; i++) {
				// �����ѡ��checkbox��λ�� �ǵڼ���
				int tempI = Integer.parseInt(checkbox[i]);

				TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();
				tdscBlockMaterial.setMaterialType(tdscBlockForm.getGy_materialType()[tempI]);
				tdscBlockMaterial.setMaterialName(tdscBlockForm.getGy_materialName()[tempI]);
				tdscBlockMaterial.setIssueUnit(tdscBlockForm.getGy_issueUnit()[tempI]);
				if (!"".equals(tdscBlockForm.getGy_materialNum()[tempI])) {
					tdscBlockMaterial.setMaterialNum(new BigDecimal(tdscBlockForm.getGy_materialNum()[tempI]));
				}
				tdscBlockMaterial.setMaterialNumb(tdscBlockForm.getGy_materialNumb()[tempI]);
				tdscBlockMaterial.setMemo(tdscBlockForm.getGy_memo()[tempI]);
				tdscBlockMaterial.setIssueUnitMemo(tdscBlockForm.getGy_issueUnitMemo()[tempI]);
				saveMateriallist.add(tdscBlockMaterial);
			}
		}

		// 4.����ӵؿ���Ϣ(checkbox�򹳵�)
		List blockPartList = new ArrayList();
		String[] blockCodes = request.getParameterValues("landUseTypes");
		if (blockCodes != null) {
			BigDecimal totalArea = new BigDecimal(0);
			BigDecimal totalBuildingArea = new BigDecimal(0);
			for (int j = 0; j < blockCodes.length; j++) {
				TdscBlockPart tdscBlockPart = new TdscBlockPart();
				// tdscBlockPart.setBlockCode(tdscBlockForm.getBlockCodes()[j]);
				if (tdscBlockForm.getBlockAreas()[j] != null && !"".equals(tdscBlockForm.getBlockAreas()[j])) {
					tdscBlockPart.setBlockArea(new BigDecimal(tdscBlockForm.getBlockAreas()[j]));
				} else {
					tdscBlockPart.setBlockArea(new BigDecimal(0));
				}
				tdscBlockPart.setLandUseType(tdscBlockForm.getLandUseTypes()[j]);
				if (tdscBlockForm.getPlanBuildingAreas()[j] != null && !"".equals(tdscBlockForm.getPlanBuildingAreas()[j])) {
					tdscBlockPart.setPlanBuildingArea(new BigDecimal(tdscBlockForm.getPlanBuildingAreas()[j]));
				} else {
					tdscBlockPart.setPlanBuildingArea(new BigDecimal(0));
				}
				if (tdscBlockForm.getVolumeRates()[j] != null && !"".equals(tdscBlockForm.getVolumeRates()[j])) {
					tdscBlockPart.setVolumeRate(tdscBlockForm.getVolumeRates()[j]);
				}
				if (tdscBlockForm.getDensitys()[j] != null && !"".equals(tdscBlockForm.getDensitys()[j])) {
					tdscBlockPart.setDensity(tdscBlockForm.getDensitys()[j]);
				}
				if (tdscBlockForm.getGreeningRates()[j] != null && !"".equals(tdscBlockForm.getGreeningRates()[j])) {
					tdscBlockPart.setGreeningRate(tdscBlockForm.getGreeningRates()[j]);
				}
				if (tdscBlockForm.getVolumeRateSigns()[j] != null && !"".equals(tdscBlockForm.getVolumeRateSigns()[j])) {
					tdscBlockPart.setVolumeRateSign(tdscBlockForm.getVolumeRateSigns()[j]);
				}
				if (tdscBlockForm.getDensitySigns()[j] != null && !"".equals(tdscBlockForm.getDensitySigns()[j])) {
					tdscBlockPart.setDensitySign(tdscBlockForm.getDensitySigns()[j]);
				}
				if (tdscBlockForm.getGreeningRateSigns()[j] != null && !"".equals(tdscBlockForm.getGreeningRateSigns()[j])) {
					tdscBlockPart.setGreeningRateSign(tdscBlockForm.getGreeningRateSigns()[j]);
				}
				if (tdscBlockForm.getServiceCharge()[j] != null && !"".equals(tdscBlockForm.getServiceCharge()[j])) {
					tdscBlockPart.setServiceCharge(tdscBlockForm.getServiceCharge()[j]);
				}

				blockPartList.add(tdscBlockPart);
				// ������������ܽ������
				totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
			}
			// ������������ܽ������
			tdscBlockInfo.setTotalBuildingArea(totalBuildingArea);
			tdscBlockInfo.setTotalLandArea(totalArea);
			tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		}

		// 5.��õؿ�ĳ��ý�֧����Ϣ
		List tdscBlockRemisemoneyDefrayList = new ArrayList();
		String[] payBatchs = request.getParameterValues("payBatchs");
		if (payBatchs != null && payBatchs.length > 0) {
			for (int j = 0; j < payBatchs.length; j++) {
				TdscBlockRemisemoneyDefray tdscBlockRemisemoneyDefray = new TdscBlockRemisemoneyDefray();
				if (tdscBlockForm.getPayBatchs()[j] != null && !"".equals(tdscBlockForm.getPayBatchs()[j])) {
					tdscBlockRemisemoneyDefray.setPayBatch(tdscBlockForm.getPayBatchs()[j]);
				}
				if (tdscBlockForm.getPayProportions()[j] != null && !"".equals(tdscBlockForm.getPayProportions()[j])) {
					tdscBlockRemisemoneyDefray.setPayProportion(tdscBlockForm.getPayProportions()[j]);
				}
				if (tdscBlockForm.getPayTimes()[j] != null && !"".equals(tdscBlockForm.getPayTimes()[j])) {
					tdscBlockRemisemoneyDefray.setPayTime(tdscBlockForm.getPayTimes()[j]);
				}
				tdscBlockRemisemoneyDefrayList.add(tdscBlockRemisemoneyDefray);
			}
		}

		TdscBlockQqjcInfo tdscBlockQqjcInfo = new TdscBlockQqjcInfo();
		this.bindObject(tdscBlockQqjcInfo, tdscBlockForm);

		// 6.�����佨��Ϣ
		// ��������ϢID��Ϊ�գ����ѯԭ��Ϣ
		TdscBlockPjxxInfo tdscBlockPjxxInfo = null;
		if (StringUtils.isNotEmpty(tdscBlockForm.getPjxxInfoId())) {
			tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfo(tdscBlockForm.getPjxxInfoId());
		} else {
			tdscBlockPjxxInfo = new TdscBlockPjxxInfo();
		}

		this.bindObject(tdscBlockPjxxInfo, tdscBlockForm);
		// �����佨����
		String pjlxs = "";
		if (tdscBlockForm.getPjlxs() != null && tdscBlockForm.getPjlxs().length > 0) {
			for (int i = 0; i < tdscBlockForm.getPjlxs().length; i++) {
				pjlxs = pjlxs + tdscBlockForm.getPjlxs()[i] + ",";
			}
		}
		tdscBlockPjxxInfo.setPjlxs(pjlxs);

		// saveRemise����������ʱ��ʹ��MAP��ֵ
		Map parameterMap = new HashMap();
		parameterMap.put("tdscBlockInfo", tdscBlockInfo);
		parameterMap.put("tdscBlockTranApp", tdscBlockTranApp);
		parameterMap.put("saveMateriallist", saveMateriallist);
		parameterMap.put("saveType", tdscBlockForm.getSaveType());
		parameterMap.put("blockPartList", blockPartList);
		parameterMap.put("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);
		parameterMap.put("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
		parameterMap.put("tdscEsClob", tdscEsClob);
		parameterMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);
		parameterMap.put("user", user);
		parameterMap.put("tdscBidderApp", tdscBidderApp);
		parameterMap.put("tdscBidderPersonApp", tdscBidderPersonApp);

		// Map retMap = tdscBlockInfoService.saveRemise(tdscBlockInfo, tdscBlockTranApp, saveMateriallist,tdscBlockForm.getSaveType(),user,blockPartList,
		// tdscBlockQqjcInfo,tdscEsClob);

		Map retMap = tdscBlockInfoService.saveRemise(parameterMap);

		// saveRemise����֮�󷵻�Map������tdscBlockTranApp��tdscBlockInfo
		tdscBlockTranApp = (TdscBlockTranApp) retMap.get("tdscBlockTranApp");
		// ��tdscBlockTranApp�л��appId
		String appId = tdscBlockTranApp.getAppId();
		// ��ѯ��ʷ���
		Map opnninfo = this.appFlowService.queryOpnnInfo(appId);
		// �ݴ�
		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			// ��ѯǩ�ղ����б�
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			Map tdscBlockMaterialMap = new HashMap();
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();

			// �����Ѿ�ѡ���ǩ�ղ����б�(checkbox�򹳵�) ���ڷ��ص�ҳ��ʱcheckbox��
			if (tdscBlockMaterialList != null) {
				for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
					tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
					tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
				}
			}

			// �鶯̬ǩ�ղ���
			tdscBlockMaterialCondition.setAppId(appId);
			if (tdscBlockForm.getBlockType() != null) {
				tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			}

			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			List bankDicList = tdscBlockInfoService.queryBankDicList();
			request.setAttribute("bankDicList", bankDicList);

			request.setAttribute("saveMessage", "����ɹ�");
			// ���淵�ص�ҳ���ֵ
			request.setAttribute("retMap", retMap);// return value
			request.setAttribute("appId", appId);
			request.setAttribute("currentPage", "0");
			request.setAttribute("opnninfo", opnninfo);
			request.setAttribute("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
			request.setAttribute("tdscBlockMaterialMap", tdscBlockMaterialMap);
			request.setAttribute("tdscOtherBlockMaterialList", tdscOtherBlockMaterialList);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
			request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);

			if ("2".equals(request.getParameter("updateBlockInfo"))) {
				return mapping.findForward("updateBlockInfo");
			}

			if (!"1".equals(request.getParameter("backUpdate"))) {
				return mapping.findForward("ComInfo");
			} else {
				if (tdscBlockForm.getBlockType() != null && (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY) == Integer.parseInt(tdscBlockForm.getBlockType())) {
					return mapping.findForward("IndBackUpdate");
				} else {
					return mapping.findForward("ComBackUpdate");
				}
			}
		} else if ("submitSave".equals(tdscBlockForm.getSaveType())) {

			return new ActionForward("blockAudit.do?method=queryAppListWithNodeId", true);
			// return this.queryAppListWithNodeId(mapping, null, request, response);
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * ������õؿ鼰�ݴ����Ϣ
	 */
	public ActionForward saveBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//���Ӹ���
		
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 1.ʹ��ActionForm����ҳ���ύ��ֵ
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		tdscBlockForm.setMarginEndDate(DateUtil.string2Timestamp(DateUtil.date2String(tdscBlockForm.getMarginEndDate(), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATETIME));

		// 2.�������ػ�����Ϣ����
		TdscBlockInfo tdscBlockInfo = null;
		if (tdscBlockForm != null && !tdscBlockForm.getBlockId().equals("")) {
			// �ݴ�����������Ϣ
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());
		} else {
			// �½�����������Ϣ
			tdscBlockInfo = new TdscBlockInfo();
		}
		tdscBlockInfo.setStatus(GlobalConstants.DIC_ID_STATUS_TRADING);

		tdscBlockForm.setAuditedNum("������[" + tdscBlockForm.getAuditedNum1() + "]" + tdscBlockForm.getAuditedNum2() + "��");
		this.bindObject(tdscBlockInfo, tdscBlockForm);
		tdscBlockInfo.setGeologicalHazard(tdscBlockForm.getGeologicalHazard());
		if (tdscBlockInfo != null && tdscBlockInfo.getBlockId().equals("")) {
			tdscBlockInfo.setBlockId(null);
		}
		// 3.���ؽ��ױ� ����ҵ��ID
		TdscBlockTranApp tdscBlockTranApp = null;
		TdscEsClob tdscEsClob = new TdscEsClob();
		if ("".equals(tdscBlockForm.getAppId()) || tdscBlockForm.getAppId() == null) {
			tdscBlockTranApp = new TdscBlockTranApp();
			tdscBlockTranApp.setInitType("01");
			tdscBlockTranApp.setHasSelectBNotary("00");
			tdscBlockTranApp.setHasSelectCNotary("00");
			tdscBlockTranApp.setHasSelectCompere("00");
			tdscBlockTranApp.setHasSelectSpecialist("00");
			tdscBlockTranApp.setTranResult(GlobalConstants.DIC_TRANSFER_RESULT_INIT);
			tdscBlockTranApp.setIfPublish("0");
		} else {
			tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(tdscBlockForm.getAppId());
			// ���桰�滮���Ҫ�㡱��clob�ֶ�
			if (tdscBlockForm.getClobContent() != null && !"".equals(tdscBlockForm.getClobContent())) {
				tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
				if (tdscEsClob == null) {
					tdscEsClob = new TdscEsClob();
				}
			}
		}
		// 4.���Ϊ��������Ƴ��õؿ� ��������������Ϣ
		TdscBidderApp tdscBidderApp = null;
		TdscBidderPersonApp tdscBidderPersonApp = null;
		String transferMode = tdscBlockForm.getTransferMode();
		String isPurposeBlock = tdscBlockForm.getIsPurposeBlock();
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
			if (GlobalConstants.DIC_ID_REVIEW_RESULT_YES.equals(isPurposeBlock)) {
				String yixiangPersonName = tdscBlockForm.getYixiangPersonName();
				String yixiangOrgNo = tdscBlockForm.getYixiangOrgNo();
				tdscBidderApp = new TdscBidderApp();
				// ��������,�ʸ�֤����ֱ��д��,Ψһ

//				String certNo = tdscBidderAppService.generateCertNo();
//				tdscBidderApp.setCertNo(certNo);
//				tdscBidderApp.setAcceptNo(certNo);
//				tdscBidderApp.setYktXh("jm"+certNo);//ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
//				tdscBidderApp.setYktBh("jm"+certNo);//ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��

				tdscBidderApp.setBidderType("1");
				tdscBidderApp.setIsPurposePerson(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
				tdscBidderApp.setBidderId(tdscBlockForm.getBidderId());

				// ��ǰҵ��Աֻ�ܹ���ǰ�ľ�����
				tdscBidderApp.setUserId(user.getUserId());

				tdscBidderPersonApp = new TdscBidderPersonApp();
				tdscBidderPersonApp.setBidderPersonId(tdscBlockForm.getBidderPersonId());
				tdscBidderPersonApp.setBidderName(yixiangPersonName);
				tdscBidderPersonApp.setOrgNo(yixiangOrgNo);
			} else if (GlobalConstants.DIC_ID_BREVIEW_RESULT_NO.equals(isPurposeBlock)) {
				tdscBlockForm.setIsPurposeBlock("0");
			}
		} else {
			tdscBlockForm.setIsPurposeBlock(null);
		}
		this.bindObject(tdscEsClob, tdscBlockForm);

		// ���tdscBlockTranApp֮���ٵ���set����pingguMethod����
		this.bindObject(tdscBlockTranApp, tdscBlockForm);
		String[] pingguMethods = request.getParameterValues("pingguMethod");
		String pingguMethod = "";
		if (pingguMethods != null && pingguMethods.length > 0) {
			for (int i = 0; i < pingguMethods.length; i++) {
				if (i == 0) {
					pingguMethod += pingguMethods[i];
				} else {
					pingguMethod += "," + pingguMethods[i];
				}
			}
		}
		tdscBlockTranApp.setPingguMethod(pingguMethod);

		// ȡ�õ׼�
		String upset = request.getParameter("dj");
		if (upset != null && upset.equals("")) {
			// ��������е׼��޸�Ϊ��
			tdscBlockTranApp.setUpset(null);
		}
		// 3.����Ѿ�ѡ���ǩ�ղ����б�(checkbox�򹳵�)
		List saveMateriallist = new ArrayList();
		String[] checkbox = request.getParameterValues("choose");

		if (checkbox != null) {
			for (int i = 0; i < checkbox.length; i++) {
				// �����ѡ��checkbox��λ�� �ǵڼ���
				int tempI = Integer.parseInt(checkbox[i]);

				TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();
				tdscBlockMaterial.setMaterialType(tdscBlockForm.getGy_materialType()[tempI]);
				tdscBlockMaterial.setMaterialName(tdscBlockForm.getGy_materialName()[tempI]);
				tdscBlockMaterial.setIssueUnit(tdscBlockForm.getGy_issueUnit()[tempI]);
				if (!"".equals(tdscBlockForm.getGy_materialNum()[tempI])) {
					tdscBlockMaterial.setMaterialNum(new BigDecimal(tdscBlockForm.getGy_materialNum()[tempI]));
				}
				tdscBlockMaterial.setMaterialNumb(tdscBlockForm.getGy_materialNumb()[tempI]);
				tdscBlockMaterial.setMemo(tdscBlockForm.getGy_memo()[tempI]);
				tdscBlockMaterial.setIssueUnitMemo(tdscBlockForm.getGy_issueUnitMemo()[tempI]);
				saveMateriallist.add(tdscBlockMaterial);
			}
		}

		// 4.����ӵؿ���Ϣ(checkbox�򹳵�)
		List blockPartList = new ArrayList();
		String[] blockCodes = request.getParameterValues("landUseTypes");
		if (blockCodes != null) {
			BigDecimal totalArea = new BigDecimal(0);
			BigDecimal totalBuildingArea = new BigDecimal(0);
			for (int j = 0; j < blockCodes.length; j++) {
				TdscBlockPart tdscBlockPart = new TdscBlockPart();
				// tdscBlockPart.setBlockCode(tdscBlockForm.getBlockCodes()[j]);
				if (tdscBlockForm.getBlockAreas()[j] != null && !"".equals(tdscBlockForm.getBlockAreas()[j])) {
					tdscBlockPart.setBlockArea(new BigDecimal(tdscBlockForm.getBlockAreas()[j]));
				} else {
					tdscBlockPart.setBlockArea(new BigDecimal(0));
				}
				tdscBlockPart.setLandUseType(tdscBlockForm.getLandUseTypes()[j]);
				if (tdscBlockForm.getPlanBuildingAreas()[j] != null && !"".equals(tdscBlockForm.getPlanBuildingAreas()[j])) {
					tdscBlockPart.setPlanBuildingArea(new BigDecimal(tdscBlockForm.getPlanBuildingAreas()[j]));
				} else {
					tdscBlockPart.setPlanBuildingArea(new BigDecimal(0));
				}
				if (tdscBlockForm.getTransferLifes()[j] != null && !"".equals(tdscBlockForm.getTransferLifes()[j])) {
					tdscBlockPart.setTransferLife(tdscBlockForm.getTransferLifes()[j]);
				}
				if (tdscBlockForm.getVolumeRates()[j] != null && !"".equals(tdscBlockForm.getVolumeRates()[j])) {
					tdscBlockPart.setVolumeRate(tdscBlockForm.getVolumeRates()[j]);
				}
				if (tdscBlockForm.getVolumeRates2()[j] != null && !"".equals(tdscBlockForm.getVolumeRates2()[j])) {
					tdscBlockPart.setVolumeRate2(tdscBlockForm.getVolumeRates2()[j]);
				}
				
				if (tdscBlockForm.getDensitys()[j] != null && !"".equals(tdscBlockForm.getDensitys()[j])) {
					tdscBlockPart.setDensity(tdscBlockForm.getDensitys()[j]);
				}
				if (tdscBlockForm.getDensitys1()[j] != null && !"".equals(tdscBlockForm.getDensitys1()[j])) {
					tdscBlockPart.setDensity1(tdscBlockForm.getDensitys1()[j]);
				}
				if (tdscBlockForm.getDensitys2()[j] != null && !"".equals(tdscBlockForm.getDensitys2()[j])) {
					tdscBlockPart.setDensity2(tdscBlockForm.getDensitys2()[j]);
				}
				if (tdscBlockForm.getGreeningRates()[j] != null && !"".equals(tdscBlockForm.getGreeningRates()[j])) {
					tdscBlockPart.setGreeningRate(tdscBlockForm.getGreeningRates()[j]);
				}
				if (tdscBlockForm.getGreeningRates1()[j] != null && !"".equals(tdscBlockForm.getGreeningRates1()[j])) {
					tdscBlockPart.setGreeningRate1(tdscBlockForm.getGreeningRates1()[j]);
				}
				if (tdscBlockForm.getGreeningRates2()[j] != null && !"".equals(tdscBlockForm.getGreeningRates2()[j])) {
					tdscBlockPart.setGreeningRate2(tdscBlockForm.getGreeningRates2()[j]);
				}
				if (tdscBlockForm.getVolumeRateSigns()[j] != null && !"".equals(tdscBlockForm.getVolumeRateSigns()[j])) {
					tdscBlockPart.setVolumeRateSign(tdscBlockForm.getVolumeRateSigns()[j]);
				}
				if (tdscBlockForm.getDensitySigns()[j] != null && !"".equals(tdscBlockForm.getDensitySigns()[j])) {
					tdscBlockPart.setDensitySign(tdscBlockForm.getDensitySigns()[j]);
				}
				if (tdscBlockForm.getGreeningRateSigns()[j] != null && !"".equals(tdscBlockForm.getGreeningRateSigns()[j])) {
					tdscBlockPart.setGreeningRateSign(tdscBlockForm.getGreeningRateSigns()[j]);
				}
				if (tdscBlockForm.getVolumeRateMemo() != null && !"".equals(tdscBlockForm.getVolumeRateMemo())) {
					tdscBlockPart.setVolumeRateMemo(tdscBlockForm.getVolumeRateMemo());
				}
				if (tdscBlockForm.getServiceCharge()[j] != null && !"".equals(tdscBlockForm.getServiceCharge()[j])) {
					tdscBlockPart.setServiceCharge(tdscBlockForm.getServiceCharge()[j]);
				}

				blockPartList.add(tdscBlockPart);
				// ������������ܽ������
				totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
			}
			// ������������ܽ������
			tdscBlockInfo.setTotalBuildingArea(totalBuildingArea);
			tdscBlockInfo.setTotalLandArea(totalArea);
			tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		}

		// 5.��õؿ�ĳ��ý�֧����Ϣ
		List tdscBlockRemisemoneyDefrayList = new ArrayList();
		String[] payBatchs = request.getParameterValues("payBatchs");
		if (payBatchs != null && payBatchs.length > 0) {
			for (int j = 0; j < payBatchs.length; j++) {
				TdscBlockRemisemoneyDefray tdscBlockRemisemoneyDefray = new TdscBlockRemisemoneyDefray();
				if (tdscBlockForm.getPayBatchs()[j] != null && !"".equals(tdscBlockForm.getPayBatchs()[j])) {
					tdscBlockRemisemoneyDefray.setPayBatch(tdscBlockForm.getPayBatchs()[j]);
				}
				if (tdscBlockForm.getPayProportions()[j] != null && !"".equals(tdscBlockForm.getPayProportions()[j])) {
					tdscBlockRemisemoneyDefray.setPayProportion(tdscBlockForm.getPayProportions()[j]);
				}
				if (tdscBlockForm.getPayTimes()[j] != null && !"".equals(tdscBlockForm.getPayTimes()[j])) {
					tdscBlockRemisemoneyDefray.setPayTime(tdscBlockForm.getPayTimes()[j]);
				}
				tdscBlockRemisemoneyDefrayList.add(tdscBlockRemisemoneyDefray);
			}
		}

		TdscBlockQqjcInfo tdscBlockQqjcInfo = new TdscBlockQqjcInfo();
		this.bindObject(tdscBlockQqjcInfo, tdscBlockForm);

		// 6.�����佨��Ϣ
		// ��������ϢID��Ϊ�գ����ѯԭ��Ϣ
		TdscBlockPjxxInfo tdscBlockPjxxInfo = null;
		if (StringUtils.isNotEmpty(tdscBlockForm.getPjxxInfoId())) {
			tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfo(tdscBlockForm.getPjxxInfoId());
		} else {
			tdscBlockPjxxInfo = new TdscBlockPjxxInfo();
		}

		this.bindObject(tdscBlockPjxxInfo, tdscBlockForm);
		// �����佨����
		String pjlxs = "";
		if (tdscBlockForm.getPjlxs() != null && tdscBlockForm.getPjlxs().length > 0) {
			for (int i = 0; i < tdscBlockForm.getPjlxs().length; i++) {
				pjlxs = pjlxs + tdscBlockForm.getPjlxs()[i] + ",";
			}
		}
		tdscBlockPjxxInfo.setPjlxs(pjlxs);

		// saveRemise����������ʱ��ʹ��MAP��ֵ
		Map parameterMap = new HashMap();
		parameterMap.put("tdscBlockInfo", tdscBlockInfo);
		parameterMap.put("tdscBlockTranApp", tdscBlockTranApp);
		parameterMap.put("saveMateriallist", saveMateriallist);
		parameterMap.put("saveType", tdscBlockForm.getSaveType());
		parameterMap.put("blockPartList", blockPartList);
		parameterMap.put("tdscBlockRemisemoneyDefrayList", tdscBlockRemisemoneyDefrayList);
		parameterMap.put("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
		parameterMap.put("tdscEsClob", tdscEsClob);
		parameterMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);
		parameterMap.put("user", user);
		parameterMap.put("tdscBidderApp", tdscBidderApp);
		parameterMap.put("tdscBidderPersonApp", tdscBidderPersonApp);

		// Map retMap = tdscBlockInfoService.saveRemise(tdscBlockInfo, tdscBlockTranApp, saveMateriallist,tdscBlockForm.getSaveType(),user,blockPartList,
		// tdscBlockQqjcInfo,tdscEsClob);

		Map retMap = tdscBlockInfoService.saveRemise(parameterMap);
		
		// saveRemise����֮�󷵻�Map������tdscBlockTranApp��tdscBlockInfo
		tdscBlockTranApp = (TdscBlockTranApp) retMap.get("tdscBlockTranApp");
		// ��tdscBlockTranApp�л��appId
		String appId = tdscBlockTranApp.getAppId();
		// ��ѯ��ʷ���
		Map opnninfo = this.appFlowService.queryOpnnInfo(appId);
		//���渽��
		String blockId = tdscBlockTranApp.getBlockId();
		//tdscBlockForm = (TdscBlockForm) form;
		String[] fileNameList = tdscBlockForm.getAccessoryName();
		MultipartRequestHandler multipartRequestHandler = form.getMultipartRequestHandler(); 
		// ȡ�������ϴ��ļ��Ķ��󼯺� 
		Hashtable elements = multipartRequestHandler.getFileElements(); 
		Object[] fileList = new Object[elements.size()];
		// ѭ������ÿһ���ļ� 
		for (java.util.Enumeration e = elements.keys(); e.hasMoreElements();){
			String key = (String) e.nextElement();
			int index;
			String strIndex = key.replaceAll("formFile", "");
			index = Integer.parseInt(strIndex);
			FormFile file = (FormFile)elements.get(key);
			fileList[index]=file;
		}

		tdscBlockInfoService.saveBlockFile(blockId, fileList, fileNameList);
		List fujianList = tdscBlockInfoService.findFileRefByBusId(blockId, GlobalConstants.BLOCK_FILE);
		request.setAttribute("fileList", fujianList);
		
		// �ݴ�
		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			// ��ѯǩ�ղ����б�
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			Map tdscBlockMaterialMap = new HashMap();
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();

			// �����Ѿ�ѡ���ǩ�ղ����б�(checkbox�򹳵�) ���ڷ��ص�ҳ��ʱcheckbox��
			if (tdscBlockMaterialList != null) {
				for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
					tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
					tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
				}
			}

			// �鶯̬ǩ�ղ���
			tdscBlockMaterialCondition.setAppId(appId);
			if (tdscBlockForm.getBlockType() != null) {
				tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			}

			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			List bankDicList = tdscBlockInfoService.queryBankDicList();
			request.setAttribute("bankDicList", bankDicList);

			request.setAttribute("saveMessage", "����ɹ�");
			// ���淵�ص�ҳ���ֵ
			request.setAttribute("retMap", retMap);// return value
			request.setAttribute("appId", appId);
			request.setAttribute("currentPage", "0");
			request.setAttribute("opnninfo", opnninfo);
			request.setAttribute("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
			request.setAttribute("tdscBlockMaterialMap", tdscBlockMaterialMap);
			request.setAttribute("tdscOtherBlockMaterialList", tdscOtherBlockMaterialList);
			request.setAttribute("tdscBidderApp", tdscBidderApp);
			request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);

			if ("2".equals(request.getParameter("updateBlockInfo"))) {
				return mapping.findForward("updateBlockInfo");
			}
			
			if ("tempSave".equals(request.getParameter("saveType"))) {
				return mapping.findForward("IndInfo");
			}
			
			if (!"1".equals(request.getParameter("backUpdate"))) {
				//return mapping.findForward("ComInfo");
				return mapping.findForward("updateBlockInfo");				
			} else {
				if (tdscBlockForm.getBlockType() != null && (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY) == Integer.parseInt(tdscBlockForm.getBlockType())) {
					return mapping.findForward("IndBackUpdate");
				} else {
					return mapping.findForward("ComBackUpdate");
				}
			}
		} else if ("submitSave".equals(tdscBlockForm.getSaveType())) {

			return new ActionForward("blockAudit.do?method=queryAppListWithNodeId", true);
			// return this.queryAppListWithNodeId(mapping, null, request, response);
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * ����ҳ��
	 */
	public ActionForward toAccept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String blockType = request.getParameter("blockType");
		String appId = request.getParameter("appId");
		String blockId = request.getParameter("blockId");
		String statusId = request.getParameter("statusId");

		if (!FlowConstants.FLOW_STATUS_AUDIT_ACCEPT.equals(statusId)) {
			return mapping.findForward("tdscError");
		}

		// ��ѯ��ʷ���
		Map opnnMap = new HashMap();
		try {
			opnnMap = appFlowService.queryOpnnInfo(appId);
			request.setAttribute("opnninfo", opnnMap);
		} catch (Exception e) {
			request.setAttribute("opnninfo", opnnMap);
			e.printStackTrace();
		}

		// ��ѯ���ػ�����Ϣ
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findTdscBlockInfo(blockId);

		// ��ѯ����ʹ�ñ���Ϣ
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);

		// ��ѯ���ؽ�����Ϣ
		TdscBlockTranApp tdscBlockTranApp = tdscBlockInfoService.getTdscBlockTranAppByAppId(appId);

		// ��ѯǩ�ղ��� ���������MAP ����ҳ�� ��ѡ�� ��
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(appId);
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

		Map retMap = new HashMap();
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);

		// ����ҳ����Ӧ��Ϣ
		request.setAttribute("retMap", retMap);
		request.setAttribute("appId", appId);
		request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);
		request.setAttribute("tdscBlockUsedInfoList", tdscBlockUsedInfoList);

		if (FlowConstants.FLOW_STATUS_AUDIT_ACCEPT.equals(statusId)) {
			if (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY == Integer.parseInt(blockType)) {
				return mapping.findForward("SlGy");
			}
			if (null != blockId && !"".equals(blockId)) {
				List blockPartList = tdscBlockInfoService.getTdscBlockPartList(blockId);
				request.setAttribute("blockPartList", blockPartList);
			}
			return mapping.findForward("SlSy");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * ����ҵ��ҵ
	 */
	public ActionForward acceptIndCom(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ʹ��ActionForm����ҳ���ύ��ֵ
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;

		// ��ѯ���ػ�����Ϣ
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());

		// ��ѯ���ؽ�����Ϣ
		TdscBlockTranApp tdscBlockTranApp = tdscBlockInfoService.findTdscBlockTranApp(tdscBlockForm.getAppId());

		// ��ѯ����ʹ����Ϣ
		TdscBlockUsedInfo tdscBlockUsedInfo = new TdscBlockUsedInfo();
		if (!"".equals(tdscBlockForm.getBlockUsedId())) {
			tdscBlockUsedInfo = tdscBlockInfoService.findTdscBlockUsedInfo(tdscBlockForm.getBlockUsedId());
		}

		// --------����Ӧ����ֵ---------------------------------------------------------------
		tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		this.bindObject(tdscBlockInfo, tdscBlockForm);
		this.bindObject(tdscBlockTranApp, tdscBlockForm);
		this.bindObject(tdscBlockUsedInfo, tdscBlockForm);

		// ��ѯǩ�ղ����б�
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(tdscBlockForm.getAppId());
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

		// Ϊ�����趨��Ӧ��ֵ
		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(tdscBlockForm.getAppId());
		appFlow.setResultId(request.getParameter("resultId"));
		appFlow.setTextOpen(request.getParameter("textOpen"));
		appFlow.setTransferMode(tdscBlockForm.getTransferMode());
		appFlow.setUser(user);

		// ���淵��ֵ ����MAP
		Map retMap = new HashMap();
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);

		tdscBlockInfoService.saveAccept(tdscBlockInfo, tdscBlockTranApp, tdscBlockUsedInfo, tdscBlockForm.getSaveType(), user);

		// ����ȱʡ��ҳΪ��0ҳ
		request.setAttribute("currentPage", "0");

		// �ݴ��뱣��
		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			try {
				request.setAttribute("saveMessage", "����ɹ�");
				// ��ѯ��ʷ���
				Map opnnMap;
				try {
					opnnMap = appFlowService.queryOpnnInfo(tdscBlockForm.getAppId());
					request.setAttribute("opnninfo", opnnMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("saveMessage", "����ʧ��");
			}

			// ������Ϣ
			request.setAttribute("retMap", retMap);
			request.setAttribute("appId", tdscBlockForm.getAppId());
			request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);
			request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);

			// ת����Ӧҳ��
			if (tdscBlockForm.getBlockType() != null && (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY) == Integer.parseInt(tdscBlockForm.getBlockType())) {
				return mapping.findForward("SlGy");
			} else {
				if (tdscBlockInfo.getBlockId() != null && !"".equals(tdscBlockInfo.getBlockId())) {
					List blockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockInfo.getBlockId());
					request.setAttribute("blockPartList", blockPartList);
				}
				return mapping.findForward("SlSy");
			}
		} else if ("submitSave".equals(tdscBlockForm.getSaveType())) {
			// ���Ϊ�ύ ҳ��ת���ѯ�б�
			return this.queryAppListWithNodeId(mapping, null, request, response);
		}

		return mapping.findForward("tdscError");
	}

	/**
	 * ���ҳ��
	 */
	public ActionForward toCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ղ���
		String appId = request.getParameter("appId");
		// String blockType = request.getParameter("blockType");
		String statusId = request.getParameter("statusId");

		// ����ͨ�ò�ѯ���� ��ѯ����Ӧ��ͼ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(condition);
		String blockId = commonInfo.getBlockId();

		// ��ѯ���ػ�����Ϣ
		TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);

		// ��ѯ����ʹ����Ϣ�б�
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);

		// ��ѯ���ؽ�����Ϣ
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		// ���滮���Ҫ�㡱�ֶΣ�clob
		TdscEsClob tdscEsClob = (TdscEsClob) tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
		// ��ѯǰ�ڼ����Ϣ
		TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo) tdscBlockInfoService.getQqjcInfoByblockId(blockId);
		// �佨��Ϣ
		TdscBlockPjxxInfo tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfoByBlockId(blockId);

		// ��ѯ��ʷ���
		Map opnnMap = new HashMap();
		try {
			opnnMap = appFlowService.queryOpnnInfo(appId);
			request.setAttribute("opnninfo", opnnMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ǩ�ղ����б�
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(appId);
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

		// ������Ϣ
		Map retMap = new HashMap();
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);
		retMap.put("tdscEsClob", tdscEsClob);
		retMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);

		List blockPartList = this.tdscBlockInfoService.getTdscBlockPartList(blockId);
		request.setAttribute("blockPartList", blockPartList);
		// ������Ϣ
		request.setAttribute("retMap", retMap);
		request.setAttribute("commonInfo", commonInfo);
		request.setAttribute("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
		request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);
		request.setAttribute("tdscBlockUsedInfoList", tdscBlockUsedInfoList);
		request.setAttribute("statusId", statusId);

		if (FlowConstants.FLOW_STATUS_AUDIT_ENDORSE.equals(statusId) || FlowConstants.FLOW_STATUS_AUDIT_INITTRY.equals(statusId)
				|| FlowConstants.FLOW_STATUS_AUDIT_VERIFY.equals(statusId) || FlowConstants.FLOW_STATUS_AUDIT_RECIEVE.equals(statusId)) {
			return mapping.findForward("ShSy");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * �������ҳ��
	 */
	public ActionForward saveCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// ���ղ���
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		String statusId = request.getParameter("statusId");

		// ����ͨ�ò�ѯ���� ��ѯ����Ӧ��ͼ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(tdscBlockForm.getAppId());
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(condition);
		String blockId = commonInfo.getBlockId();

		// workflow
		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(tdscBlockForm.getAppId());
		appFlow.setResultId(request.getParameter("resultId"));
		appFlow.setTextOpen(request.getParameter("textOpen"));
		appFlow.setTransferMode(tdscBlockForm.getTransferMode());
		appFlow.setUser(user);

		// -------���޸���Ϣ-------------------------///
		TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);
		tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		// û��LocalTradeType����
		// tdscBlockInfo.setLocalTradeType(tdscBlockForm.getLocalTradeType());
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(tdscBlockForm.getAppId());
		this.bindObject(tdscBlockTranApp, tdscBlockForm);

		// ���������Ϣ
		Map retMap = this.tdscBlockInfoService.saveCheck(tdscBlockInfo, tdscBlockTranApp);

		// �趨��ǰҳΪ0
		request.setAttribute("currentPage", "0");
		request.setAttribute("statusId", statusId);

		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			// ǩ�ղ���list
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(tdscBlockForm.getAppId());
			List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			try {
				this.appFlowService.tempSaveOpnn(appFlow);

				// ��ѯ��ʷ���
				Map opnnMap = new HashMap();

				try {
					opnnMap = appFlowService.queryOpnnInfo(tdscBlockForm.getAppId());
					request.setAttribute("opnninfo", opnnMap);
				} catch (Exception e) {
					e.printStackTrace();
				}

				request.setAttribute("retMap", retMap);
				request.setAttribute("commonInfo", commonInfo);
				request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);
				request.setAttribute("saveMessage", "����ɹ�");
			} catch (Exception e) {
				e.printStackTrace();
				request.removeAttribute("commonInfo");
				request.setAttribute("saveMessage", "����ʧ��");
			}

			List blockPartList = this.tdscBlockInfoService.getTdscBlockPartList(blockId);
			request.setAttribute("blockPartList", blockPartList);
			return mapping.findForward("ShSy");

		} else if ("submitSave".equals(tdscBlockForm.getSaveType())) {
			this.appFlowService.saveOpnn(appFlow);
			return this.queryAppListWithNodeId(mapping, null, request, response);
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * ��ӡ��ִ��
	 */
	public ActionForward printReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String appId = request.getParameter("appId");
		String blockType = request.getParameter("blockType");
		String blockId = request.getParameter("blockId");

		TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);
		String blockName = tdscBlockInfo.getBlockName();
		String blockLandId = tdscBlockInfo.getBlockLandId();
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(appId);
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

		// ���ص���ӡҳ��������Ϣ
		request.setAttribute("blockName", blockName);
		request.setAttribute("blockLandId", blockLandId);
		request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);

		if (blockType != null && (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY) == Integer.parseInt(blockType)) {
			return mapping.findForward("printGy");
		}
		return mapping.findForward("printSy");
	}

	/*
	 * �˷������ڱ���������������queryAppListWithNodeId()����ʱ �Ѳ���ֱ�Ӹ������Ӧ�Ĳ����Ĵ���
	 */
	public ActionForward queryAppListWithNodeIdClearParam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.queryAppListWithNodeId(mapping, null, request, response);
	}

	/**
	 * ���ӻ��޸��ӵؿ���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateoraddChildBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ղ���
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		TdscBlockPart blockPart = new TdscBlockPart();
		bindObject(blockPart, tdscBlockForm);
		String landUseType = request.getParameter("landUseTypeId");
		if (null != landUseType && !"".equals(landUseType))
			;
		blockPart.setLandUseType(landUseType);
		if (null != (blockPart.getPartId()) && !"".equals(blockPart.getPartId())) {
			blockPart = tdscBlockInfoService.updateTdscBlockPart(blockPart);
		} else {
			blockPart = tdscBlockInfoService.saveTdscBlockPart(blockPart);
		}
		request.setAttribute("blockPart", blockPart);

		return mapping.findForward("operateChildblock");
	}

	/**
	 * ��ʾ,��ת�޸��ӵؿ���Ϣҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward forwardorshowChildBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ղ���
		String partId = request.getParameter("partId");
		TdscBlockPart blockPart = tdscBlockInfoService.getTdscblockPart(partId);
		request.setAttribute("blockPart", blockPart);
		String param = request.getParameter("param");
		if ("update".equals(param)) {
			return mapping.findForward("updatechildBlock");
		}
		return mapping.findForward("showBlockPart");
	}

	/**
	 * ɾ���ӵؿ���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteChildBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ղ���
		String partId = request.getParameter("partId");
		TdscBlockPart tdscBlockPart = tdscBlockInfoService.getTdscblockPart(partId);
		tdscBlockInfoService.deleteTdscBlockPart(tdscBlockPart);
		// ���ûص������Ĳ���
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		pw.write(partId);
		pw.close();
		return null;
	}

	/**
	 * �ڵ�ǰ���׵ؿ�˵��� �޸ĵؿ������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String blockId = request.getParameter("blockId");
		String appId = request.getParameter("appId");
		// ��ѯ���ػ�����Ϣ
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(blockId);
		TdscBidderApp tdscBidderApp = this.tdscBlockInfoService.getTdscBidderAppByAppId(appId);
		TdscBidderPersonApp tdscBidderPersonApp = null;
		if (tdscBidderApp != null) {
			tdscBidderPersonApp = this.tdscBlockInfoService.getTdscBidderPersonAppByBidderId(tdscBidderApp.getBidderId());
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		// ��ѯ���ؽ�����Ϣ
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		TdscEsClob tdscEsClob = new TdscEsClob();
		if (StringUtils.isNotEmpty(appId)) {
			tdscBlockTranApp = tdscBlockInfoService.findTdscBlockTranApp(appId);
			// ����滮���Ҫ��
			// tdscBlockTranApp = tdscBlockInfoService.reTidyTranApp(tdscBlockTranApp);
			tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
		}
		// ��ѯ����ʹ����Ϣ�б�
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);
		// ��ѯǩ�ղ����б�
		List tdscBlockMaterialList = tdscBlockInfoService.findMaterialListByAppId(StringUtils.trimToEmpty(appId));
		// ��ǩ�ղ����б����Ϣ���浽Map��һ�� ����ҳ��checkbox�Ƿ�ѡ��
		Map tdscBlockMaterialMap = new HashMap();
		if (tdscBlockMaterialList != null) {
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();
			for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
				tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
				// �Բ�������Ϊ���� �洢������Ϣ
				tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
			}
		}
		// ��ѯ�ӵؿ���Ϣ
		List blockPartList = (List) tdscBlockInfoService.getTdscBlockPartList(blockId);
		List TdscBlockRemisemoneyDefrayList = (List) tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockId);
		// �鶯̬ǩ�ղ���
		if (StringUtils.isNotEmpty(appId)) {
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);
			request.setAttribute("tdscOtherBlockMaterialList", tdscOtherBlockMaterialList);
		}

		TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo) tdscBlockInfoService.getQqjcInfoByblockId(blockId);
		// ��ѯ��ʷ���
		Map opnninfo = new HashMap();
		if (StringUtils.isNotEmpty(appId)) {
			opnninfo = this.appFlowService.queryOpnnInfo(appId);
		}
		// �����ݴ�ҳ����Ҫ����Ϣ
		Map retMap = new HashMap();
		if (tdscEsClob != null) {
			// String setClobContent = (StringUtil.GBKtoISO88591(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
			// String setClobContent_1 = (StringUtil.ISO88591toGBK(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
		}
		// ����佨��Ϣ
		TdscBlockPjxxInfo tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfoByBlockId(blockId);

		List bankDicList = tdscBlockInfoService.queryBankDicList();
		request.setAttribute("bankDicList", bankDicList);

		retMap.put("tdscEsClob", tdscEsClob);
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);
		retMap.put("blockPartList", blockPartList);
		retMap.put("tdscBlockRemisemoneyDefrayList", TdscBlockRemisemoneyDefrayList);
		retMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);

		request.setAttribute("retMap", retMap);
		request.setAttribute("blockPartList", blockPartList);
		// request.setAttribute("tdscBlockRemisemoneyDefrayList", TdscBlockRemisemoneyDefrayList);
		request.setAttribute("tdscBlockQqjcInfo", tdscBlockQqjcInfo);
		request.setAttribute("tdscBlockUsedInfoList", tdscBlockUsedInfoList);
		request.setAttribute("tdscBlockMaterialMap", tdscBlockMaterialMap);

		request.setAttribute("opnninfo", opnninfo);
		request.setAttribute("appId", appId);
		if (StringUtils.isNotBlank(blockId)) {
			List fileList = tdscBlockInfoService.findFileRefByBusId(blockId, GlobalConstants.BLOCK_FILE);
			request.setAttribute("fileList", fileList);
		}
		return mapping.findForward("updateBlockInfo");
	}
	
	/**
	 * ɾ��Ԥ������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileId = request.getParameter("fileId");
		tdscBlockInfoService.delFileRefById(fileId);
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		pw.write("OK");
		pw.close();
		return null;
	}
}
