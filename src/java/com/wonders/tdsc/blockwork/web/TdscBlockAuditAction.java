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
	// 通用查询service
	private CommonQueryService		commonQueryService;
	// 土地基本信息service
	private TdscBlockInfoService	tdscBlockInfoService;
	// 流程控制service
	private AppFlowService			appFlowService;

	// 仅仅为了生成 资格证书编号
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
	 * 查询待办业务列表
	 */
	public ActionForward queryAppListWithNodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前用户
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// 出让地块申请查询条件
		TdscBaseQueryCondition sqCondition = new TdscBaseQueryCondition();
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		// 将条件返回到主列表页面
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
		sqCondition.setNodeList(nodeList); // 根据用户权限查询待办列表
		sqCondition.setOrderKey("actionDateBlock");// 按所需字段排序
		sqCondition.setOrderType("desc");
		sqCondition.setNodeId(nodeId);
		sqCondition.setDistrictIdList(districtIdList);
		sqCondition.setCurrentPage(currentPage);
		sqCondition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		sqCondition.setUser(user);
		// 调用通用查询 查询列表信息
		List queryAppList = commonQueryService.queryTdscBlockAppViewListWithoutNode(sqCondition);

		request.setAttribute("nodeId", nodeId);
		// 储备地块查询
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		// 设定status查询出未交易及交易中止的信息
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };

		// 储备地块查询条件
		TdscBlockInfoCondition cbCondition = new TdscBlockInfoCondition();
		this.bindObject(cbCondition, tdscBlockForm);
		cbCondition.setStatus(status);

		// 判断是否是管理员
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			cbCondition.setUserId(user.getUserId());
		}

		// cbCondition.setCurrentPage(currentPage);
		// cbCondition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
		// 返回给入市申请页面 土地基本信息列表
		List cbBlockList = (List) tdscBlockInfoService.queryAppViewList(cbCondition);
		// 将条件返回到主列表页面
		// this.bindObject(sqCondition, tdscBlockForm);

		if (sqCondition.getBlockName() != null && !"".equals(sqCondition.getBlockName())) {
			sqCondition.setBlockName(StringUtil.ISO88591toGBK(sqCondition.getBlockName()));
		}
		if (sqCondition.getAuditedNum() != null && !"".equals(sqCondition.getAuditedNum())) {
			sqCondition.setAuditedNum(StringUtil.ISO88591toGBK(sqCondition.getAuditedNum()));
		}
		request.setAttribute("condition", sqCondition);

		// 整合2个查询结果List
		if (queryAppList != null && queryAppList.size() > 0) {
			for (int i = 0; i < queryAppList.size(); i++) {
				TdscBlockAppView appView = (TdscBlockAppView) queryAppList.get(i);
				// 如果是“申请”环节的数据，只有录入数据的用户可以看到
				if ("0101".equals(appView.getStatusId())) {
					// 如果不是管理员，则只能查询该用户保存的数据
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

		request.setAttribute("queryAppList", PageList);// 返回列表
		return mapping.findForward("listAll");
	}

	/**
	 * 储备地块整理
	 */
	public ActionForward queryBlockInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前用户
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 获取页面参数
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		// 设定status查询出未交易及交易中止的信息
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };
		String type = request.getParameter("type");

		// 返回给入市申请页面 查询条件
		TdscBlockInfoCondition condition = new TdscBlockInfoCondition();
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		if (StringUtils.isBlank(type))
			this.bindObject(condition, tdscBlockForm);
		condition.setStatus(status);
		// 判断该用户是否是管理员：如果是管理员则查询全部数据；
		// 否则只查询：1、该用户提交的数据；2、用户为空的数据（用户为空有两种情况：1为历史数据；2为从其他系统传送的数据）
		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// 判断是否是管理员
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// 返回给入市申请页面 土地基本信息列表
		PageList blockInfoList = tdscBlockInfoService.findPageList(condition);

		request.setAttribute("blockInfoList", blockInfoList);// 查询土地信息
		if (StringUtils.isBlank(type))
			request.setAttribute("TdscBlockCondition", condition);// 返回查询条件

		return mapping.findForward("blockInfoList");

	}

	/**
	 * 进入储备地块
	 */
	public ActionForward toBlockInfoAndPart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockId = request.getParameter("blockId");
		if (StringUtils.isNotBlank(blockId)) {
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(blockId);
			request.setAttribute("blockInfo", tdscBlockInfo);// 查询土地信息
			request.setAttribute("tdscBlockPartList", tdscBlockInfoService.getTdscBlockPartList(tdscBlockInfo.getBlockId()));
		}
		return mapping.findForward("blockInfo");

	}

	/**
	 * 删除储备地块及其子地块 lz+
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
	 * 储备地块保存
	 */
	public ActionForward saveBlockInfoAndPart(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前用户
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String blockId = request.getParameter("blockId");
		// 1.使用ActionForm接受页面提交的值
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;

		// 2.创建土地基本信息对象
		TdscBlockInfo tdscBlockInfo = null;
		if (StringUtils.isNotBlank(blockId))
			// 暂存入市申请信息
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());
		else
			tdscBlockInfo = new TdscBlockInfo();
		this.bindObject(tdscBlockInfo, tdscBlockForm);

		// 4.获得子地块信息(checkbox打钩的)
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
				// 计算总面积和总建筑面积
				totalArea = totalArea.add(tdscBlockPart.getBlockArea() == null ? new BigDecimal(0) : tdscBlockPart.getBlockArea());
				totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea() == null ? new BigDecimal(0) : tdscBlockPart.getPlanBuildingArea());
			}
		}

		// 设置总面积和总建筑面积
		tdscBlockInfo.setTotalBuildingArea(totalBuildingArea);
		tdscBlockInfo.setTotalLandArea(totalArea);
		tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		// 设置用户ID信息
		tdscBlockInfo.setUserId(user.getUserId());

		tdscBlockInfoService.saveBlockInfoAndPart(tdscBlockInfo, blockPartList);

		return new ActionForward("blockAudit.do?method=queryBlockInfoList&type=1", true);

	}

	/**
	 * 新建入市申请
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
	 * 新建申请
	 */
	public ActionForward toCreateApply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 返回给入市申请页面 查询条件
		String locString = request.getParameter("locString");
		String blockId = request.getParameter("blockId");
		// List tdscBlockInfoList=null;
		// 整理JSON字符串，返回MAP
		Map xmlJobDataMap = (Map) tdscBlockInfoService.tidyMap(locString);
		// 根据MAP中的 BLOCK_ID，查询相应的地块信息
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
		// 返回原有的blockId
		request.setAttribute("blockId", blockId);
		request.setAttribute("xmlJobDataMap", xmlJobDataMap);
		// request.setAttribute("tdscBlockInfoList",tdscBlockInfoList);// 查询土地信息

		return mapping.findForward("createApply");
	}

	/**
	 * 入市申请列表
	 */
	public ActionForward toAddList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String statusId = request.getParameter("statusId");
		String nodeId = request.getParameter("nodeId");
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}

		// 设定status查询出未交易及交易中止的信息
		String[] status = { GlobalConstants.DIC_ID_STATUS_NOTTRADE };

		// 返回给入市申请页面 查询条件
		TdscBlockInfoCondition condition = new TdscBlockInfoCondition();
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		this.bindObject(condition, tdscBlockForm);
		condition.setStatus(status);
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// 返回给入市申请页面 土地基本信息列表
		PageList blockInfoList = tdscBlockInfoService.findPageList(condition);

		request.setAttribute("blockInfoList", blockInfoList);// 查询土地信息
		request.setAttribute("TdscBlockCondition", condition);// 返回查询条件
		request.setAttribute("nodeId", nodeId);
		request.setAttribute("statusId", statusId);
		return mapping.findForward("newList");
	}

	/**
	 * 进入新建出让页面
	 */
	public ActionForward newRemise(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String blockId = request.getParameter("blockId");
		String blockType = request.getParameter("blockType");
		String nodeId = request.getParameter("nodeId");
		String statusId = request.getParameter("statusId");

		if (!FlowConstants.FLOW_STATUS_AUDIT_INIT.equals(statusId)) {
			return mapping.findForward("tdscError");
		}

		// 如地块的blockId不为空 跳转到相应的 工业出让 或者 商业出让页面
		if (StringUtils.isNotEmpty(blockId)) {
			TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findTdscBlockInfo(blockId);
			List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);

			Map retMap = new HashMap();
			retMap.put("tdscBlockInfo", tdscBlockInfo);

			request.setAttribute("retMap", retMap);
			request.setAttribute("tdscBlockUsedInfoList", tdscBlockUsedInfoList);
			request.setAttribute("nodeId", nodeId);

			// 根据blockType的类型确定申请所对应的jsp页面
			if (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY == Integer.parseInt(blockType)) {
				return mapping.findForward("IndInfo");
			}
			return mapping.findForward("ComInfo");
		}
		return mapping.findForward("tdscError");
	}

	/**
	 * 文件链接
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
	 * 进入申请(退回修改,待申请)页面
	 */
	public ActionForward toApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String blockId = request.getParameter("blockId");
		String appId = request.getParameter("appId");
		//判断是否是租赁
		String zulin = request.getParameter("zulin");
		if(StringUtils.isNotBlank(zulin)){
			request.setAttribute("zulin", zulin);
		}
		List bankDicList = tdscBlockInfoService.queryBankDicList();

		// 查询土地基本信息
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(blockId);
		TdscBidderApp tdscBidderApp = this.tdscBlockInfoService.getTdscBidderAppByAppId(appId);
		TdscBidderPersonApp tdscBidderPersonApp = null;
		if (tdscBidderApp != null) {
			tdscBidderPersonApp = this.tdscBlockInfoService.getTdscBidderPersonAppByBidderId(tdscBidderApp.getBidderId());
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		// 查询土地交易信息
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		TdscEsClob tdscEsClob = new TdscEsClob();
		if (StringUtils.isNotEmpty(appId)) {
			tdscBlockTranApp = tdscBlockInfoService.findTdscBlockTranApp(appId);
			// 整理规划设计要点
			// tdscBlockTranApp = tdscBlockInfoService.reTidyTranApp(tdscBlockTranApp);
			tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
		}
		// 查询土地使用信息列表
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);
		// 查询签收材料列表
		List tdscBlockMaterialList = tdscBlockInfoService.findMaterialListByAppId(StringUtils.trimToEmpty(appId));
		// 将签收材料列表的信息保存到Map中一份 用于页面checkbox是否选择
		Map tdscBlockMaterialMap = new HashMap();
		if (tdscBlockMaterialList != null) {
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();
			for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
				tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
				// 以材料名字为主键 存储材料信息
				tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
			}
		}
		// 查询子地块信息
		List blockPartList = (List) tdscBlockInfoService.getTdscBlockPartList(blockId);
		List TdscBlockRemisemoneyDefrayList = (List) tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockId);
		// 查动态签收材料
		if (StringUtils.isNotEmpty(appId)) {
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);
			request.setAttribute("tdscOtherBlockMaterialList", tdscOtherBlockMaterialList);
		}

		TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo) tdscBlockInfoService.getQqjcInfoByblockId(blockId);
		// 查询历史意见
		Map opnninfo = new HashMap();
		if (StringUtils.isNotEmpty(appId)) {
			opnninfo = this.appFlowService.queryOpnnInfo(appId);
		}
		// 保存暂存页面需要的信息
		Map retMap = new HashMap();
		if (tdscEsClob != null) {
			// String setClobContent = (StringUtil.GBKtoISO88591(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
			// String setClobContent_1 = (StringUtil.ISO88591toGBK(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
		}
		// 获得配建信息
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
		//新增、编辑附件
		

		if (StringUtils.isNotBlank(blockId)) {
			List fileList = tdscBlockInfoService.findFileRefByBusId(blockId, GlobalConstants.BLOCK_FILE);
			request.setAttribute("fileList", fileList);
		}

		return mapping.findForward("ComInfo");

	}


	/**
	 * 取消申请
	 */
	public ActionForward cancelApp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");

		// 设定currentPage为第0页
		request.setAttribute("currentPage", "0");

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 删除地块信息
		if (appId != null) {
			// 更新土地信息和土地交易信息的状态
			this.tdscBlockInfoService.cancelApp(appId);

			// 为流程设定相应的值
			TdscAppFlow appFlow = new TdscAppFlow();
			appFlow.setAppId(appId);
			appFlow.setResultId(FlowConstants.FLOW_AUDIT_RESULT_CANCEL_APP);
			appFlow.setUser(user);

			this.appFlowService.saveOpnn(appFlow);
		}
		return this.queryAppListWithNodeId(mapping, null, request, response);
	}

	/**
	 * <<<<<<< .mine ======= 修改出让地块及暂存的信息
	 */
	public ActionForward modifyBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 1.使用ActionForm接受页面提交的值
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		tdscBlockForm.setMarginEndDate(DateUtil.string2Timestamp(DateUtil.date2String(tdscBlockForm.getMarginEndDate(), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATETIME));

		// 2.创建土地基本信息对象
		TdscBlockInfo tdscBlockInfo = null;
		if (tdscBlockForm != null && !tdscBlockForm.getBlockId().equals("")) {
			// 暂存入市申请信息
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());
		} else {
			// 新建入市申请信息
			tdscBlockInfo = new TdscBlockInfo();
		}
		tdscBlockForm.setAuditedNum("锡政字[" + tdscBlockForm.getAuditedNum1() + "]" + tdscBlockForm.getAuditedNum2() + "号");
		this.bindObject(tdscBlockInfo, tdscBlockForm);
		if (tdscBlockInfo != null && tdscBlockInfo.getBlockId().equals("")) {
			tdscBlockInfo.setBlockId(null);
		}
		// 3.土地交易表 生成业务ID
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
			// 保存“规划设计要点”，clob字段
			if (tdscBlockForm.getClobContent() != null && !"".equals(tdscBlockForm.getClobContent())) {
				tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
				if (tdscEsClob == null) {
					tdscEsClob = new TdscEsClob();
				}
			}
		}
		// 4.如果为有意向挂牌出让地块 则增加意向人信息
		TdscBidderApp tdscBidderApp = null;
		TdscBidderPersonApp tdscBidderPersonApp = null;
		String transferMode = tdscBlockForm.getTransferMode();
		String isPurposeBlock = tdscBlockForm.getIsPurposeBlock();
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
			if (GlobalConstants.DIC_ID_REVIEW_RESULT_YES.equals(isPurposeBlock)) {
				String yixiangPersonName = tdscBlockForm.getYixiangPersonName();
				tdscBidderApp = new TdscBidderApp();
				// 是意向人
				tdscBidderApp.setBidderType("1");
				tdscBidderApp.setIsPurposePerson(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
				tdscBidderApp.setBidderId(tdscBlockForm.getBidderId());

				// 当前业务员只能管理当前的竞买人
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

		// 填充tdscBlockTranApp之后，再单独set它的pingguMethod属性
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

		// 取得底价
		String upset = request.getParameter("dj");
		if (upset != null && upset.equals("")) {
			// 如果不含有底价修改为空
			tdscBlockTranApp.setUpset(null);
		}
		// 3.获得已经选择的签收材料列表(checkbox打钩的)
		List saveMateriallist = new ArrayList();
		String[] checkbox = request.getParameterValues("choose");

		if (checkbox != null) {
			for (int i = 0; i < checkbox.length; i++) {
				// 获得所选择checkbox的位置 是第几个
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

		// 4.获得子地块信息(checkbox打钩的)
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
				// 计算总面积和总建筑面积
				totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
			}
			// 设置总面积和总建筑面积
			tdscBlockInfo.setTotalBuildingArea(totalBuildingArea);
			tdscBlockInfo.setTotalLandArea(totalArea);
			tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		}

		// 5.获得地块的出让金支付信息
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

		// 6.保存配建信息
		// 如果配件信息ID不为空，则查询原信息
		TdscBlockPjxxInfo tdscBlockPjxxInfo = null;
		if (StringUtils.isNotEmpty(tdscBlockForm.getPjxxInfoId())) {
			tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfo(tdscBlockForm.getPjxxInfoId());
		} else {
			tdscBlockPjxxInfo = new TdscBlockPjxxInfo();
		}

		this.bindObject(tdscBlockPjxxInfo, tdscBlockForm);
		// 整理配建类型
		String pjlxs = "";
		if (tdscBlockForm.getPjlxs() != null && tdscBlockForm.getPjlxs().length > 0) {
			for (int i = 0; i < tdscBlockForm.getPjlxs().length; i++) {
				pjlxs = pjlxs + tdscBlockForm.getPjlxs()[i] + ",";
			}
		}
		tdscBlockPjxxInfo.setPjlxs(pjlxs);

		// saveRemise方法传参数时，使用MAP传值
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

		// saveRemise保存之后返回Map包含了tdscBlockTranApp和tdscBlockInfo
		tdscBlockTranApp = (TdscBlockTranApp) retMap.get("tdscBlockTranApp");
		// 从tdscBlockTranApp中获得appId
		String appId = tdscBlockTranApp.getAppId();
		// 查询历史意见
		Map opnninfo = this.appFlowService.queryOpnnInfo(appId);
		// 暂存
		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			// 查询签收材料列表
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			Map tdscBlockMaterialMap = new HashMap();
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();

			// 保存已经选择的签收材料列表(checkbox打钩的) 用于返回到页面时checkbox打钩
			if (tdscBlockMaterialList != null) {
				for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
					tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
					tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
				}
			}

			// 查动态签收材料
			tdscBlockMaterialCondition.setAppId(appId);
			if (tdscBlockForm.getBlockType() != null) {
				tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			}

			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			List bankDicList = tdscBlockInfoService.queryBankDicList();
			request.setAttribute("bankDicList", bankDicList);

			request.setAttribute("saveMessage", "保存成功");
			// 保存返回到页面的值
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
	 * 保存出让地块及暂存的信息
	 */
	public ActionForward saveBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//增加附件
		
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		// 1.使用ActionForm接受页面提交的值
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		tdscBlockForm.setMarginEndDate(DateUtil.string2Timestamp(DateUtil.date2String(tdscBlockForm.getMarginEndDate(), DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATETIME));

		// 2.创建土地基本信息对象
		TdscBlockInfo tdscBlockInfo = null;
		if (tdscBlockForm != null && !tdscBlockForm.getBlockId().equals("")) {
			// 暂存入市申请信息
			tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());
		} else {
			// 新建入市申请信息
			tdscBlockInfo = new TdscBlockInfo();
		}
		tdscBlockInfo.setStatus(GlobalConstants.DIC_ID_STATUS_TRADING);

		tdscBlockForm.setAuditedNum("锡政字[" + tdscBlockForm.getAuditedNum1() + "]" + tdscBlockForm.getAuditedNum2() + "号");
		this.bindObject(tdscBlockInfo, tdscBlockForm);
		tdscBlockInfo.setGeologicalHazard(tdscBlockForm.getGeologicalHazard());
		if (tdscBlockInfo != null && tdscBlockInfo.getBlockId().equals("")) {
			tdscBlockInfo.setBlockId(null);
		}
		// 3.土地交易表 生成业务ID
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
			// 保存“规划设计要点”，clob字段
			if (tdscBlockForm.getClobContent() != null && !"".equals(tdscBlockForm.getClobContent())) {
				tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
				if (tdscEsClob == null) {
					tdscEsClob = new TdscEsClob();
				}
			}
		}
		// 4.如果为有意向挂牌出让地块 则增加意向人信息
		TdscBidderApp tdscBidderApp = null;
		TdscBidderPersonApp tdscBidderPersonApp = null;
		String transferMode = tdscBlockForm.getTransferMode();
		String isPurposeBlock = tdscBlockForm.getIsPurposeBlock();
		if (GlobalConstants.DIC_TRANSFER_LISTING.equals(transferMode)) {
			if (GlobalConstants.DIC_ID_REVIEW_RESULT_YES.equals(isPurposeBlock)) {
				String yixiangPersonName = tdscBlockForm.getYixiangPersonName();
				String yixiangOrgNo = tdscBlockForm.getYixiangOrgNo();
				tdscBidderApp = new TdscBidderApp();
				// 是意向人,资格证书编号直接写入,唯一

//				String certNo = tdscBidderAppService.generateCertNo();
//				tdscBidderApp.setCertNo(certNo);
//				tdscBidderApp.setAcceptNo(certNo);
//				tdscBidderApp.setYktXh("jm"+certNo);//取消交易卡，用资格证书编号代替交易卡编号
//				tdscBidderApp.setYktBh("jm"+certNo);//取消交易卡，用资格证书编号代替交易卡芯片号

				tdscBidderApp.setBidderType("1");
				tdscBidderApp.setIsPurposePerson(GlobalConstants.DIC_ID_REVIEW_RESULT_YES);
				tdscBidderApp.setBidderId(tdscBlockForm.getBidderId());

				// 当前业务员只能管理当前的竞买人
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

		// 填充tdscBlockTranApp之后，再单独set它的pingguMethod属性
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

		// 取得底价
		String upset = request.getParameter("dj");
		if (upset != null && upset.equals("")) {
			// 如果不含有底价修改为空
			tdscBlockTranApp.setUpset(null);
		}
		// 3.获得已经选择的签收材料列表(checkbox打钩的)
		List saveMateriallist = new ArrayList();
		String[] checkbox = request.getParameterValues("choose");

		if (checkbox != null) {
			for (int i = 0; i < checkbox.length; i++) {
				// 获得所选择checkbox的位置 是第几个
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

		// 4.获得子地块信息(checkbox打钩的)
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
				// 计算总面积和总建筑面积
				totalArea = totalArea.add(tdscBlockPart.getBlockArea());
				totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
			}
			// 设置总面积和总建筑面积
			tdscBlockInfo.setTotalBuildingArea(totalBuildingArea);
			tdscBlockInfo.setTotalLandArea(totalArea);
			tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		}

		// 5.获得地块的出让金支付信息
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

		// 6.保存配建信息
		// 如果配件信息ID不为空，则查询原信息
		TdscBlockPjxxInfo tdscBlockPjxxInfo = null;
		if (StringUtils.isNotEmpty(tdscBlockForm.getPjxxInfoId())) {
			tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfo(tdscBlockForm.getPjxxInfoId());
		} else {
			tdscBlockPjxxInfo = new TdscBlockPjxxInfo();
		}

		this.bindObject(tdscBlockPjxxInfo, tdscBlockForm);
		// 整理配建类型
		String pjlxs = "";
		if (tdscBlockForm.getPjlxs() != null && tdscBlockForm.getPjlxs().length > 0) {
			for (int i = 0; i < tdscBlockForm.getPjlxs().length; i++) {
				pjlxs = pjlxs + tdscBlockForm.getPjlxs()[i] + ",";
			}
		}
		tdscBlockPjxxInfo.setPjlxs(pjlxs);

		// saveRemise方法传参数时，使用MAP传值
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
		
		// saveRemise保存之后返回Map包含了tdscBlockTranApp和tdscBlockInfo
		tdscBlockTranApp = (TdscBlockTranApp) retMap.get("tdscBlockTranApp");
		// 从tdscBlockTranApp中获得appId
		String appId = tdscBlockTranApp.getAppId();
		// 查询历史意见
		Map opnninfo = this.appFlowService.queryOpnnInfo(appId);
		//保存附件
		String blockId = tdscBlockTranApp.getBlockId();
		//tdscBlockForm = (TdscBlockForm) form;
		String[] fileNameList = tdscBlockForm.getAccessoryName();
		MultipartRequestHandler multipartRequestHandler = form.getMultipartRequestHandler(); 
		// 取得所有上传文件的对象集合 
		Hashtable elements = multipartRequestHandler.getFileElements(); 
		Object[] fileList = new Object[elements.size()];
		// 循环遍历每一个文件 
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
		
		// 暂存
		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			// 查询签收材料列表
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			Map tdscBlockMaterialMap = new HashMap();
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();

			// 保存已经选择的签收材料列表(checkbox打钩的) 用于返回到页面时checkbox打钩
			if (tdscBlockMaterialList != null) {
				for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
					tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
					tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
				}
			}

			// 查动态签收材料
			tdscBlockMaterialCondition.setAppId(appId);
			if (tdscBlockForm.getBlockType() != null) {
				tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			}

			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			List bankDicList = tdscBlockInfoService.queryBankDicList();
			request.setAttribute("bankDicList", bankDicList);

			request.setAttribute("saveMessage", "保存成功");
			// 保存返回到页面的值
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
	 * 受理页面
	 */
	public ActionForward toAccept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String blockType = request.getParameter("blockType");
		String appId = request.getParameter("appId");
		String blockId = request.getParameter("blockId");
		String statusId = request.getParameter("statusId");

		if (!FlowConstants.FLOW_STATUS_AUDIT_ACCEPT.equals(statusId)) {
			return mapping.findForward("tdscError");
		}

		// 查询历史意见
		Map opnnMap = new HashMap();
		try {
			opnnMap = appFlowService.queryOpnnInfo(appId);
			request.setAttribute("opnninfo", opnnMap);
		} catch (Exception e) {
			request.setAttribute("opnninfo", opnnMap);
			e.printStackTrace();
		}

		// 查询土地基本信息
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findTdscBlockInfo(blockId);

		// 查询土地使用表信息
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);

		// 查询土地交易信息
		TdscBlockTranApp tdscBlockTranApp = tdscBlockInfoService.getTdscBlockTranAppByAppId(appId);

		// 查询签收材料 并将其放入MAP 用于页面 复选框 打钩
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(appId);
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

		Map retMap = new HashMap();
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);

		// 保存页面相应信息
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
	 * 受理工业商业
	 */
	public ActionForward acceptIndCom(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 使用ActionForm接受页面提交的值
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;

		// 查询土地基本信息
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(tdscBlockForm.getBlockId());

		// 查询土地交易信息
		TdscBlockTranApp tdscBlockTranApp = tdscBlockInfoService.findTdscBlockTranApp(tdscBlockForm.getAppId());

		// 查询土地使用信息
		TdscBlockUsedInfo tdscBlockUsedInfo = new TdscBlockUsedInfo();
		if (!"".equals(tdscBlockForm.getBlockUsedId())) {
			tdscBlockUsedInfo = tdscBlockInfoService.findTdscBlockUsedInfo(tdscBlockForm.getBlockUsedId());
		}

		// --------给相应对象赋值---------------------------------------------------------------
		tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		this.bindObject(tdscBlockInfo, tdscBlockForm);
		this.bindObject(tdscBlockTranApp, tdscBlockForm);
		this.bindObject(tdscBlockUsedInfo, tdscBlockForm);

		// 查询签收材料列表
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(tdscBlockForm.getAppId());
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

		// 为流程设定相应的值
		TdscAppFlow appFlow = new TdscAppFlow();
		appFlow.setAppId(tdscBlockForm.getAppId());
		appFlow.setResultId(request.getParameter("resultId"));
		appFlow.setTextOpen(request.getParameter("textOpen"));
		appFlow.setTransferMode(tdscBlockForm.getTransferMode());
		appFlow.setUser(user);

		// 保存返回值 存入MAP
		Map retMap = new HashMap();
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);

		tdscBlockInfoService.saveAccept(tdscBlockInfo, tdscBlockTranApp, tdscBlockUsedInfo, tdscBlockForm.getSaveType(), user);

		// 设置缺省分页为第0页
		request.setAttribute("currentPage", "0");

		// 暂存与保存
		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			try {
				request.setAttribute("saveMessage", "保存成功");
				// 查询历史意见
				Map opnnMap;
				try {
					opnnMap = appFlowService.queryOpnnInfo(tdscBlockForm.getAppId());
					request.setAttribute("opnninfo", opnnMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("saveMessage", "保存失败");
			}

			// 保存信息
			request.setAttribute("retMap", retMap);
			request.setAttribute("appId", tdscBlockForm.getAppId());
			request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);
			request.setAttribute("tdscBlockUsedInfo", tdscBlockUsedInfo);

			// 转向相应页面
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
			// 如果为提交 页面转向查询列表
			return this.queryAppListWithNodeId(mapping, null, request, response);
		}

		return mapping.findForward("tdscError");
	}

	/**
	 * 审核页面
	 */
	public ActionForward toCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接收参数
		String appId = request.getParameter("appId");
		// String blockType = request.getParameter("blockType");
		String statusId = request.getParameter("statusId");

		// 借用通用查询方法 查询出相应视图数据
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView commonInfo = commonQueryService.getTdscBlockAppView(condition);
		String blockId = commonInfo.getBlockId();

		// 查询土地基本信息
		TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);

		// 查询土地使用信息列表
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);

		// 查询土地交易信息
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(appId);
		// “规划设计要点”字段，clob
		TdscEsClob tdscEsClob = (TdscEsClob) tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
		// 查询前期监察信息
		TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo) tdscBlockInfoService.getQqjcInfoByblockId(blockId);
		// 配建信息
		TdscBlockPjxxInfo tdscBlockPjxxInfo = (TdscBlockPjxxInfo) tdscBlockInfoService.getTdscBlockPjxxInfoByBlockId(blockId);

		// 查询历史意见
		Map opnnMap = new HashMap();
		try {
			opnnMap = appFlowService.queryOpnnInfo(appId);
			request.setAttribute("opnninfo", opnnMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 签收材料列表
		TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
		tdscBlockMaterialCondition.setAppId(appId);
		List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

		// 保存信息
		Map retMap = new HashMap();
		retMap.put("tdscBlockInfo", tdscBlockInfo);
		retMap.put("tdscBlockTranApp", tdscBlockTranApp);
		retMap.put("tdscEsClob", tdscEsClob);
		retMap.put("tdscBlockPjxxInfo", tdscBlockPjxxInfo);

		List blockPartList = this.tdscBlockInfoService.getTdscBlockPartList(blockId);
		request.setAttribute("blockPartList", blockPartList);
		// 保存信息
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
	 * 处理审核页面
	 */
	public ActionForward saveCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 接收参数
		TdscBlockForm tdscBlockForm = (TdscBlockForm) form;
		String statusId = request.getParameter("statusId");

		// 借用通用查询方法 查询出相应视图数据
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

		// -------可修改信息-------------------------///
		TdscBlockInfo tdscBlockInfo = this.tdscBlockInfoService.findBlockInfo(blockId);
		tdscBlockInfo.setActionDateBlock(new Timestamp(System.currentTimeMillis()));
		// 没有LocalTradeType属性
		// tdscBlockInfo.setLocalTradeType(tdscBlockForm.getLocalTradeType());
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.findTdscBlockTranApp(tdscBlockForm.getAppId());
		this.bindObject(tdscBlockTranApp, tdscBlockForm);

		// 保存审核信息
		Map retMap = this.tdscBlockInfoService.saveCheck(tdscBlockInfo, tdscBlockTranApp);

		// 设定当前页为0
		request.setAttribute("currentPage", "0");
		request.setAttribute("statusId", statusId);

		if ("tempSave".equals(tdscBlockForm.getSaveType())) {
			// 签收材料list
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(tdscBlockForm.getAppId());
			List tdscBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);

			try {
				this.appFlowService.tempSaveOpnn(appFlow);

				// 查询历史意见
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
				request.setAttribute("saveMessage", "保存成功");
			} catch (Exception e) {
				e.printStackTrace();
				request.removeAttribute("commonInfo");
				request.setAttribute("saveMessage", "保存失败");
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
	 * 打印回执单
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

		// 返回到打印页面所需信息
		request.setAttribute("blockName", blockName);
		request.setAttribute("blockLandId", blockLandId);
		request.setAttribute("tdscBlockMaterialList", tdscBlockMaterialList);

		if (blockType != null && (GlobalConstants.DIC_BLOCK_TYPE_INDUSTRY) == Integer.parseInt(blockType)) {
			return mapping.findForward("printGy");
		}
		return mapping.findForward("printSy");
	}

	/*
	 * 此方法用于避免主调函数调用queryAppListWithNodeId()方法时 把参数直接赋给其对应的参数的错误
	 */
	public ActionForward queryAppListWithNodeIdClearParam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.queryAppListWithNodeId(mapping, null, request, response);
	}

	/**
	 * 增加或修改子地块信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateoraddChildBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接收参数
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
	 * 显示,跳转修改子地块信息页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward forwardorshowChildBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接收参数
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
	 * 删除子地块信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteChildBlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 接收参数
		String partId = request.getParameter("partId");
		TdscBlockPart tdscBlockPart = tdscBlockInfoService.getTdscblockPart(partId);
		tdscBlockInfoService.deleteTdscBlockPart(tdscBlockPart);
		// 设置回调函数的参数
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		pw.write(partId);
		pw.close();
		return null;
	}

	/**
	 * 在当前交易地块菜单中 修改地块基本信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateBlockInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String blockId = request.getParameter("blockId");
		String appId = request.getParameter("appId");
		// 查询土地基本信息
		TdscBlockInfo tdscBlockInfo = tdscBlockInfoService.findBlockInfo(blockId);
		TdscBidderApp tdscBidderApp = this.tdscBlockInfoService.getTdscBidderAppByAppId(appId);
		TdscBidderPersonApp tdscBidderPersonApp = null;
		if (tdscBidderApp != null) {
			tdscBidderPersonApp = this.tdscBlockInfoService.getTdscBidderPersonAppByBidderId(tdscBidderApp.getBidderId());
		}
		request.setAttribute("tdscBidderApp", tdscBidderApp);
		request.setAttribute("tdscBidderPersonApp", tdscBidderPersonApp);
		// 查询土地交易信息
		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		TdscEsClob tdscEsClob = new TdscEsClob();
		if (StringUtils.isNotEmpty(appId)) {
			tdscBlockTranApp = tdscBlockInfoService.findTdscBlockTranApp(appId);
			// 整理规划设计要点
			// tdscBlockTranApp = tdscBlockInfoService.reTidyTranApp(tdscBlockTranApp);
			tdscEsClob = tdscBlockInfoService.getTdscEsClob(tdscBlockTranApp.getBzGhsjyd());
		}
		// 查询土地使用信息列表
		List tdscBlockUsedInfoList = tdscBlockInfoService.queryTdscBlockUsedInfoListByBlockId(blockId);
		// 查询签收材料列表
		List tdscBlockMaterialList = tdscBlockInfoService.findMaterialListByAppId(StringUtils.trimToEmpty(appId));
		// 将签收材料列表的信息保存到Map中一份 用于页面checkbox是否选择
		Map tdscBlockMaterialMap = new HashMap();
		if (tdscBlockMaterialList != null) {
			TdscBlockMaterial tdscBlockMaterial = new TdscBlockMaterial();
			for (int i = 0; i < tdscBlockMaterialList.size(); i++) {
				tdscBlockMaterial = (TdscBlockMaterial) tdscBlockMaterialList.get(i);
				// 以材料名字为主键 存储材料信息
				tdscBlockMaterialMap.put(tdscBlockMaterial.getMaterialType(), tdscBlockMaterial);
			}
		}
		// 查询子地块信息
		List blockPartList = (List) tdscBlockInfoService.getTdscBlockPartList(blockId);
		List TdscBlockRemisemoneyDefrayList = (List) tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockId);
		// 查动态签收材料
		if (StringUtils.isNotEmpty(appId)) {
			TdscBlockMaterialCondition tdscBlockMaterialCondition = new TdscBlockMaterialCondition();
			tdscBlockMaterialCondition.setAppId(appId);
			tdscBlockMaterialCondition.setMaterialNum(new BigDecimal(10));
			List tdscOtherBlockMaterialList = tdscBlockInfoService.getTdscBlockMaterialList(tdscBlockMaterialCondition);
			request.setAttribute("tdscOtherBlockMaterialList", tdscOtherBlockMaterialList);
		}

		TdscBlockQqjcInfo tdscBlockQqjcInfo = (TdscBlockQqjcInfo) tdscBlockInfoService.getQqjcInfoByblockId(blockId);
		// 查询历史意见
		Map opnninfo = new HashMap();
		if (StringUtils.isNotEmpty(appId)) {
			opnninfo = this.appFlowService.queryOpnnInfo(appId);
		}
		// 保存暂存页面需要的信息
		Map retMap = new HashMap();
		if (tdscEsClob != null) {
			// String setClobContent = (StringUtil.GBKtoISO88591(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
			// String setClobContent_1 = (StringUtil.ISO88591toGBK(tdscEsClob.getClobContent()==null?"":tdscEsClob.getClobContent()));
		}
		// 获得配建信息
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
	 * 删除预公告信息
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
