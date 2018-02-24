package com.wonders.tdsc.publishinfo.web;

import java.math.BigDecimal;
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
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscSendEmailInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.FileUtils;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.out.service.Export4PubPlatService;
import com.wonders.tdsc.publishinfo.dao.TdscNoticeAppDao;
import com.wonders.tdsc.publishinfo.dao.TdscSendEmailInfoDao;
import com.wonders.tdsc.publishinfo.service.TdscNoticePublishService;
import com.wonders.tdsc.publishinfo.web.form.RecordAndResultForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.util.PropertiesUtil;

public class TdscTransResultAction extends BaseAction {

	private static final String			dapingPath	= PropertiesUtil.getInstance().getProperty("daping_path");

	private CommonQueryService			commonQueryService;

	private TdscBlockInfoService		tdscBlockInfoService;

	private AppFlowService				appFlowService;

	private TdscNoticePublishService	tdscNoticePublishService;

	private TdscNoticeAppDao			tdscNoticeAppDao;

	private TdscSendEmailInfoDao		tdscSendEmailInfoDao;
	
	private Export4PubPlatService		export4PubPlatService;

	public void setTdscSendEmailInfoDao(TdscSendEmailInfoDao tdscSendEmailInfoDao) {
		this.tdscSendEmailInfoDao = tdscSendEmailInfoDao;
	}

	public void setTdscNoticeAppDao(TdscNoticeAppDao tdscNoticeAppDao) {
		this.tdscNoticeAppDao = tdscNoticeAppDao;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscNoticePublishService(TdscNoticePublishService tdscNoticePublishService) {
		this.tdscNoticePublishService = tdscNoticePublishService;
	}

	public void setExport4PubPlatService(Export4PubPlatService export4PubPlatService) {
		this.export4PubPlatService = export4PubPlatService;
	}

	public ActionForward printResultNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");

		String noticeNo = "";
		String transferMode = "";
		List tdscBlockAppViewList = null;

		if (noticeId != null && !"".equals(noticeId)) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);
			condition.setOrderKey("blockNoticeNo");

			tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			tdscBlockAppViewList = filterCanceledBlock(tdscBlockAppViewList);

			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
				transferMode = tdscNoticeApp.getTransferMode();
			}
		}

		request.setAttribute("noticeNo", noticeNo);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);

		return mapping.findForward("printResultNotice");
	}

	private List filterCanceledBlock(List appViewList) {
		List retList = new ArrayList();
		if (appViewList != null && appViewList.size() > 0) {
			for (int i = 0; i < appViewList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) appViewList.get(i);
				if (!"04".equals(app.getTranResult())) {
					retList.add(app);
				}
			}
		}
		return retList;
	}

	/**
	 * ��ѯ�����Ѿ������Ĺ��棬Ϊ�˴�ӡ��������ϵ��ʽ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBidderedPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String strTradeStatus = request.getParameter("tradeStatus") + "";

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

		if (!StringUtils.isEmpty(strTradeStatus) && "1".equals(strTradeStatus)) {
			request.setAttribute("tradeStatus", strTradeStatus);
			condition.setIfResultPublish("1");
		} else {
			condition.setIfResultPublish("0");// ���׽��δ��ʾ�ļ�¼��δ�ɽ�������
		}
		// ��ѯ����List
		List noticeList = new ArrayList();
		noticeList = tdscNoticePublishService.queryNoticeListByCondition(condition);
		// ����condition����List���ϳ�PageLsit����
		List rebuildPageList = new ArrayList();
		if (noticeList != null && noticeList.size() > 0) {
			rebuildPageList = tdscNoticePublishService.rebuildPageList(noticeList);
		}

		// ����ҳ����Ҫ���ֶ� from tdscBlockAppView----strat
		if (rebuildPageList != null && rebuildPageList.size() > 0) {
			for (int i = 0; i < rebuildPageList.size(); i++) {

				TdscNoticeApp app = (TdscNoticeApp) rebuildPageList.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				conditionBlock.setNoticeId(app.getNoticeId());
				// conditionBlock.setTranResult("01");//ֻ��ѯ���׳ɹ��ؿ�

				// ���еؿ���Ϣ��
				// List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
				// String blockName = "";
				// if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				// for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				//
				// TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				//
				// blockName += blockapp.getBlockName() + ",";
				// app.setTranManTel(blockapp.getTradeNum());// ��"���Ĺұ��"���ڴ��ֶ���
				// app.setTranManAddr(blockapp.getTransferMode());// ��"���÷�ʽ"���ڴ��ֶ���
				// }
				// blockName = blockName.substring(0, blockName.length() - 1);
				// app.setTranManName(blockName);// ���ؿ����ƴ��ڴ��ֶ���
				// }

				app.setTranManTel(app.getTradeNum());// ��"���Ĺұ��"���ڴ��ֶ���
				app.setTranManAddr(app.getTransferMode());// ��"���÷�ʽ"���ڴ��ֶ���

			}
		}
		// -----end

		if (rebuildPageList != null && rebuildPageList.size() != 0) {
			pageList.setList(rebuildPageList);
		}
		pageList = PageUtil.getPageList(rebuildPageList, pageSize, currentPage);
		request.setAttribute("tranceResultNoticeList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("toEndBlockList");
	}

	// public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	// throws Exception {
	//
	// TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
	// bindObject(condition, form);
	//
	// PageList pageList =new PageList();
	// if (request.getParameter("currentPage") != null)
	// condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));
	// //����"�����ʾ"�ڵ�
	// condition.setNodeId(FlowConstants.FLOW_NODE_RESULT_SHOW);
	// // ����ҳ������
	// condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
	//
	// condition.setEndlessTag(GlobalConstants.QUERY_ENDLESS_TAG);
	//
	// pageList = (PageList)commonQueryService.queryTdscBlockAppViewPageList(condition);
	//
	// request.setAttribute("pageList", pageList);
	// request.setAttribute("condition", condition);
	//
	// return mapping.findForward("transResultList");
	// }

	/**
	 * ���׽����ʾ-��ʾ�����б�
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ���ܲ�ѯ����
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
		// ��ѯ����List
		List noticeList = new ArrayList();
		if(condition.getIfResultPublish()==null)
		{
			condition.setIfResultPublish("0");// ���׽��δ��ʾ�ļ�¼��δ�ɽ�������
		}
		noticeList = tdscNoticePublishService.queryNoticeListByCondition(condition);
		// ����condition����List���ϳ�PageLsit����
		List rebuildPageList = new ArrayList();
		if (noticeList != null && noticeList.size() > 0) {
			rebuildPageList = tdscNoticePublishService.rebuildPageList(noticeList);
		}

		// ����ҳ����Ҫ���ֶ� from tdscBlockAppView----strat
		if (rebuildPageList != null && rebuildPageList.size() > 0) {
			for (int i = 0; i < rebuildPageList.size(); i++) {

				TdscNoticeApp app = (TdscNoticeApp) rebuildPageList.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				conditionBlock.setNoticeId(app.getNoticeId());
				// conditionBlock.setTranResult("01");//ֻ��ѯ���׳ɹ��ؿ�

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

		if (rebuildPageList != null && rebuildPageList.size() != 0) {
			pageList.setList(rebuildPageList);
		}
		pageList = PageUtil.getPageList(rebuildPageList, pageSize, currentPage);
		request.setAttribute("tranceResultNoticeList", pageList);
		request.setAttribute("condition", condition);
		return mapping.findForward("tranceResultNoticeList");
	}

	/**
	 * ����������ѯ�ؿ���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showTdscViewListByNoticeId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����ҳ�洫�������
		String noticeId = request.getParameter("noticeId");
		// �����ѯ����
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		bindObject(condition, form);
		condition.setNoticeId(noticeId);
		condition.setTranResult("01");
		// ��ѯ�ؿ���Ϣ�б�
		List pageList = new ArrayList();
		pageList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		// ��ѯ�ù������Ϣ������ַ�������
		String noticeTitle = tdscNoticePublishService.initRelustNoticeTitle(noticeId);

		request.setAttribute("noticeTitle", noticeTitle);
		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("transeResltTdscView");
	}

	/**
	 * ��ʾĳһ�ؿ����ϸ��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showTdscBlockAppViewByAppId(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		// TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		// condition.setAppId(appId);
		// TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		// tdscBlockAppView = (TdscBlockAppView)commonQueryService.getTdscBlockAppView(condition);
		// //�滮��;�����ӵؿ�
		// tdscBlockAppView=tdscBlockInfoService.tidyTdscBlockAppViewByBlockId(tdscBlockAppView);

		request.setAttribute("tdscBlockAppView", tdscBlockInfoService.tidyTdscBlockAppViewByBlockId(appId));
		return mapping.findForward("showTdscBlockAppViewByAppId");
	}

	public ActionForward fabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ��ȡҳ�����
		String appId = request.getParameter("appId");

		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockInfoService.findTdscBlockTranApp(appId);
		if (tdscBlockTranApp != null) {
			String transferMode = (String) tdscBlockTranApp.getTransferMode();
			// ����û���Ϣ
			SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
			// �޸� ʵ��� �Ƿ񷢲���״̬ ����¼����ʱ��
			tdscBlockInfoService.modIfPublish(appId, transferMode, user);
		}

		return query(mapping, form, request, response);
	}

	/**
	 * ���ù��淢��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward publishNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����û���Ϣ
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String noticeId = request.getParameter("noticeId");
		String recordId = request.getParameter("recordId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		List tdscBlockTranAppList = new ArrayList();
		tdscBlockTranAppList = (List) tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, "");

		String noticeNo = "";
		if (null != noticeId) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
			}
		}

		// ��ȡ�����б�
		List tdscSendEmailList = new ArrayList();
		if (request.getParameterValues("acceptOgr") != null && request.getParameterValues("acceptAddress") != null) {
			String[] acceptOgr = request.getParameterValues("acceptOgr");
			String[] acceptAddress = request.getParameterValues("acceptAddress");

			if (acceptOgr != null && acceptOgr.length > 0 && acceptAddress != null && acceptAddress.length > 0) {
				for (int i = 0; i < acceptOgr.length; i++) {
					TdscSendEmailInfo tdscSendEmailInfo = new TdscSendEmailInfo();
					tdscSendEmailInfo.setIfSend("1");
					tdscSendEmailInfo.setNoticeId(noticeId);
					tdscSendEmailInfo.setNoticeType("2");
					tdscSendEmailInfo.setAcceptOrg(acceptOgr[i]);
					tdscSendEmailInfo.setAcceptAddress(acceptAddress[i]);
					tdscSendEmailInfo.setRecordId(recordId);
					tdscSendEmailInfo.setNoticeNo(noticeNo);

					tdscSendEmailList.add(tdscSendEmailInfo);
				}
			}
		}

		if (tdscBlockTranAppList != null && tdscBlockTranAppList.size() > 0) {
			tdscNoticePublishService.publishNotice(tdscBlockTranAppList, tdscSendEmailList, user, noticeId, recordId);
		}
		//����ʡƽ̨
		try {
			export4PubPlatService.makeVm(noticeId, 1);
		} catch (Exception e) {
			logger.error("�ɽ���Ϊ��Ϣ����noticeId="+noticeId+"\n"+e.getMessage());
		}
		List apps = this.tdscBlockInfoService.findTdscBlockTranAppByNoticeNo(noticeId, null);
		for(int b = 0; b < apps.size(); b++){
			TdscBlockTranApp app = (TdscBlockTranApp) apps.get(b);
			try {
				export4PubPlatService.makeVm(app.getAppId(), 2);
			} catch (Exception e) {
				logger.error("�ɽ��ڵ���Ϣ����appId="+app.getAppId()+"\n"+e.getMessage());
			}
		}

		RecordAndResultForm recordAndResultForm = new RecordAndResultForm();
		return query(mapping, recordAndResultForm, request, response);
	}

	/**
	 * �ɽ������ݴ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward zancun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����û���Ϣ
		String noticeId = request.getParameter("noticeId");
		String recordId = request.getParameter("recordId");

		tdscNoticePublishService.zancunNotice(noticeId, recordId);

		request.setAttribute("saveMessage", "����ɹ���");

		return query(mapping, form, request, response);
	}

	/**
	 * �ɽ����漰�ʼ��б��ݴ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fbZanCun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ����û���Ϣ
		String noticeId = request.getParameter("noticeId");
		String recordId = request.getParameter("recordId");

		String noticeNo = "";
		if (null != noticeId) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
			}
		}

		// ��ȡ�����б�
		List tdscSendEmailList = new ArrayList();
		if (request.getParameterValues("acceptOgr") != null && request.getParameterValues("acceptAddress") != null) {
			String[] acceptOgr = request.getParameterValues("acceptOgr");
			String[] acceptAddress = request.getParameterValues("acceptAddress");

			if (acceptOgr != null && acceptOgr.length > 0 && acceptAddress != null && acceptAddress.length > 0) {
				for (int i = 0; i < acceptOgr.length; i++) {
					TdscSendEmailInfo tdscSendEmailInfo = new TdscSendEmailInfo();
					tdscSendEmailInfo.setIfSend("0");
					tdscSendEmailInfo.setNoticeId(noticeId);
					tdscSendEmailInfo.setNoticeType("2");
					tdscSendEmailInfo.setAcceptOrg(acceptOgr[i]);
					tdscSendEmailInfo.setAcceptAddress(acceptAddress[i]);
					tdscSendEmailInfo.setRecordId(recordId);
					tdscSendEmailInfo.setNoticeNo(noticeNo);

					tdscSendEmailList.add(tdscSendEmailInfo);
				}
			}
		}

		tdscNoticePublishService.zancunNoticeAndEmail(noticeId, recordId, tdscSendEmailList);

		request.setAttribute("saveMessage", "����ɹ���");

		RecordAndResultForm recordAndResultForm = new RecordAndResultForm();
		return query(mapping, recordAndResultForm, request, response);
	}

	/**
	 * ���ù���iweb��ʾ���޸�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward iwebResultNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String noticeId = request.getParameter("noticeId");
		String type = request.getParameter("type");

		String noticeTitle = "";
		String fileName = "";

		request.setAttribute("noticeId", noticeId);
		request.setAttribute("type", type);

		if (noticeId != null && !"".equals(noticeId)) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setNoticeId(noticeId);
			// condition.setTranResult("01");
			// condition.setTranResult("01");
			condition.setOrderKey("xuHao");
			List tdscBlockAppViewList = new ArrayList();
			List tdscBlockPartList = new ArrayList();
			List returnList = new ArrayList();

			String transferModeShort = "";
			// ��ȡxml�ļ���Чʱ�䣬��ȷ���룬�������xml�ļ������ƣ�ϵͳ��ǰ���ڴ�����Чʱ���е�����ʱ�����ļ��ƶ�������Ŀ¼�£���ʼ��Ϊ1900��01��01��00ʱ00��00��
			String endTime = "19000101000000";
			
			String tradeNum = "0000000";//����������Ĺұ�ŵ���ݺ���ˮ�ţ������ñ�������ļ�������ʼĬ��Ϊ0000000(4λ��ݼ�3λ��ˮ��)
	        String ydxz = "000";//�õ�����,�ֹ�ҵ�;�Ӫ������,����Ϊ3λ����

			tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
			if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
				for (int i = 0; i < tdscBlockAppViewList.size();) {
					TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);

					// ���˵��ɽ���������Աҵ����ֹ�ĵؿ�
					if ("04".equals(tdscBlockAppView.getTranResult())) {
						tdscBlockAppViewList.remove(i);
						continue;
					} else {
						i++;
					}
					
					if(StringUtils.isNotBlank(tdscBlockAppView.getBlockQuality())){
						ydxz = tdscBlockAppView.getBlockQuality();
					}
					
					if(StringUtils.isNotBlank(tdscBlockAppView.getTradeNum()) && tdscBlockAppView.getTradeNum().split("-").length == 3){//ȡ���Ĺұ�������ݺ���ˮ��
						tradeNum = tdscBlockAppView.getTradeNum().split("-")[1] + tdscBlockAppView.getTradeNum().split("-")[2];
					}

					String tmp = tdscBlockAppView.getNoitceNo();

					if (StringUtils.isNotBlank(tdscBlockAppView.getTransferMode())) {
						if ("3107".equals(tdscBlockAppView.getTransferMode())) {// �б�
							transferModeShort = "Z_";
						} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {// ����
							transferModeShort = "P_";
						} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {// ����
							transferModeShort = "G_";
						}
					}
					String tranMode = "";
					if ("3107".equals(tdscBlockAppView.getTransferMode())) {
						fileName = "Z_" + DateUtil.date2String(tdscBlockAppView.getOpeningDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
						tranMode = "�б�";
					} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
						fileName = "P_" + DateUtil.date2String(tdscBlockAppView.getActionDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
						tranMode = "����";
					} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
						fileName = "G_" + DateUtil.date2String(tdscBlockAppView.getSceBidDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
						tranMode = "����";
					} else {
						fileName = "0_19000101000000" + ydxz + tradeNum;
					}

					// noticeTitle = tmp.substring(0, 10) + Integer.parseInt(tmp.substring(10, 12)) + "�Ź��н����õ�ʹ��Ȩ" + tranMode + "���óɽ�����";
					noticeTitle = "���н����õ�ʹ��Ȩ" + tranMode + "���óɽ�����";

					endTime = DateUtil.date2String(tdscBlockAppView.getResultShowDate(), "yyyyMMddHHmmss");

					String blockId = tdscBlockAppView.getBlockId();
					String transferMode = tdscBlockAppView.getTransferMode();
					String tdyt = "";
					BigDecimal blockArea = new BigDecimal(0.00);

					// ��ѯ�ӵؿ�
					tdscBlockPartList = (List) tdscBlockInfoService.getTdscBlockPartList(blockId);
					if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
						for (int j = 0; j < tdscBlockPartList.size(); j++) {
							TdscBlockPart tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);

							tdscBlockPart.setRangeEast(StringUtils.trimToEmpty(tdscBlockAppView.getLandLocation()));// ����λ��
							if (!StringUtils.isEmpty(tdscBlockAppView.getResultName()))
								tdscBlockPart.setRangeSouth(tdscBlockAppView.getResultName());// ������
							else
								tdscBlockPart.setRangeSouth("���й�����Դ���ջ�");

							tdscBlockPart.setRangeWest(tdscBlockAppView.getBlockNoticeNo().substring(tdscBlockAppView.getBlockNoticeNo().length() - 1,
									tdscBlockAppView.getBlockNoticeNo().length()));
							tdscBlockPart.setTotalLandArea(tdscBlockAppView.getInitPrice());// ������м�
							if (tdscBlockAppView.getResultPrice() != null)
								tdscBlockPart.setPlanBuildingArea(tdscBlockAppView.getResultPrice());// �ɽ����ص���
							else
								tdscBlockPart.setPlanBuildingArea(new BigDecimal("0"));

							tdscBlockPart.setBlockName(tdscBlockAppView.getBlockName());// �ؿ���
							tdscBlockPart.setBlockCode(tdscBlockAppView.getBlockNoticeNo());// �ؿ���

							returnList.add(tdscBlockPart);
						}
					}

					request.setAttribute("transferMode", transferMode);
					request.setAttribute("tdscBlockAppView", tdscBlockAppView);
				}
			}

			// �ɽ���������ʹ��xml�ļ�չʾ������
			// add by xys
			if("cjggfabu".equals(type)){
				makeXmlFileToCjgg(fileName, noticeTitle, returnList);
			}
			
			request.setAttribute("fileName", transferModeShort + endTime + ydxz + tradeNum + ".html");// ƴ�ɽ�����html�ļ����ļ���

			// ��ѯ�����ѱ�����ʼ������б� �ɽ�����2
			List tdscSendEmailList = new ArrayList();
			tdscSendEmailList = tdscSendEmailInfoDao.querySendEmailListByType(noticeId, "2");
			request.setAttribute("tdscSendEmailList", tdscSendEmailList);

			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
			String resultRecordId = StringUtils.trimToEmpty(tdscNoticeApp.getResultRecordId());

			request.setAttribute("noticeNo", StringUtils.trimToEmpty(tdscNoticeApp.getNoticeNo()));
			request.setAttribute("recordId", resultRecordId);
			request.setAttribute("modeNameEn", "cjqkgg.doc");
			request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);
			request.setAttribute("tdscBlockPartList", returnList);

		}

		return mapping.findForward("iwebResultNotice");
	}

	/**
	 * �ɽ�����ʹ�� xml �ļ�չʾ������ ���� xml �ļ��� CJGG Ŀ¼
	 * 
	 * @param noticeTitle
	 * @param returnList
	 */
	private void makeXmlFileToCjgg(String fileName, String noticeTitle, List contextList) {

		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		sb.append("<root>");
		sb.append("<noticeTitle>").append(noticeTitle).append("</noticeTitle>");
		if (contextList != null && contextList.size() > 0) {
			for (int i = 0; i < contextList.size(); i++) {
				TdscBlockPart info = (TdscBlockPart) contextList.get(i);
				sb.append("<context>");
				sb.append("<blockNo>").append(info.getBlockCode()).append("</blockNo>");
				sb.append("<blockName>").append(info.getBlockName()).append("</blockName>");
				sb.append("<blockLocation>").append(info.getRangeEast()).append("</blockLocation>");
				sb.append("<bidderPerson>").append(info.getRangeSouth()).append("</bidderPerson>");
				if (info.getPlanBuildingArea().intValue() == 0)
					sb.append("<cjPrice>").append("").append("</cjPrice>");
				else
					sb.append("<cjPrice>").append(info.getPlanBuildingArea()).append("</cjPrice>");
				sb.append("</context>");
			}
		}
		sb.append("</root>");

		String filePath = dapingPath + "cjgg\\";
		fileName += ".xml";
		FileUtils.saveStringToFile(sb.toString(), filePath, fileName);

	}

	/**
	 * ���ù��淢��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tradeResultList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");

		String tradeNum = "";
		String transferMode = "";
		String startDate = "";// ���ƣ��ֳ�����ʱ�䣻������������ʱ�䣻
		BigDecimal allBlockPrice = new BigDecimal(0.00);// ���еؿ��ܼ�
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setNoticeId(noticeId);
		condition.setOrderKey("xuHao");
		// ���еؿ���Ϣ��
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int i = 0; i < tdscBlockAppViewList.size(); i++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(i);
				tradeNum = tdscBlockAppView.getTradeNum();
				transferMode = tdscBlockAppView.getTransferMode();
				if ("3104".equals(transferMode))
					startDate = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getSceBidDate(), "yyyy��MM��dd��"));
				else
					startDate = StringUtils.trimToEmpty(DateUtil.timestamp2String(tdscBlockAppView.getAuctionDate(), "yyyy��MM��dd��"));

				// ���ҳ������
				String unionBlockCode = tdscBlockInfoService.tidyBlockCodeByBlockId(tdscBlockAppView.getBlockId());
				tdscBlockAppView.setUnitebBlockCode(unionBlockCode);// �ؿ��ż���
				String unionLandUseType = tdscBlockInfoService.tidyLandUseTypeByBlockId(tdscBlockAppView.getBlockId());
				tdscBlockAppView.setBlockLandId(unionLandUseType);// ������;����

				List tdscBlockPartList = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());
				BigDecimal totalLandArea = new BigDecimal(0.00);// ���������
				BigDecimal totalBuildingArea = new BigDecimal(0.00);// �ܹ滮�������
				if (tdscBlockPartList != null && tdscBlockPartList.size() > 0) {
					for (int j = 0; j < tdscBlockPartList.size(); j++) {
						TdscBlockPart tdscBlockPart = (TdscBlockPart) tdscBlockPartList.get(j);
						if (tdscBlockPart.getBlockArea() != null && tdscBlockPart.getBlockArea().compareTo(new BigDecimal("0.00")) == 1)
							totalLandArea = totalLandArea.add(tdscBlockPart.getBlockArea());
						if (tdscBlockPart.getPlanBuildingArea() != null && tdscBlockPart.getPlanBuildingArea().compareTo(new BigDecimal("0.00")) == 1)
							totalBuildingArea = totalBuildingArea.add(tdscBlockPart.getPlanBuildingArea());
					}
				}
				tdscBlockAppView.setTotalLandArea(totalLandArea);// �������
				tdscBlockAppView.setTotalBuildingArea(totalBuildingArea);// �滮�������

				if (tdscBlockAppView.getTotalPrice() != null && tdscBlockAppView.getTotalPrice().compareTo(new BigDecimal("0.00")) == 1) {
					tdscBlockAppView.setTotalPrice(tdscBlockAppView.getTotalPrice().movePointLeft(4));
					allBlockPrice = allBlockPrice.add(tdscBlockAppView.getTotalPrice());
				}
			}
		}

		request.setAttribute("allBlockPrice", NumberUtil.numberDisplay(allBlockPrice, 1));
		request.setAttribute("tradeNum", tradeNum);
		request.setAttribute("transferMode", transferMode);
		request.setAttribute("startDate", startDate);
		request.setAttribute("tdscBlockAppViewList", tdscBlockAppViewList);

		return mapping.findForward("tradeResultList");
	}

}
