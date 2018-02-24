package com.wonders.tdsc.publishinfo.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.sun.jndi.toolkit.url.UrlUtil;
import com.wonders.common.authority.SysUser;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscSendEmailInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.FileUtils;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.out.service.ExpDataService;
import com.wonders.tdsc.publishinfo.service.TdscNoticePublishService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.util.PropertiesUtil;

public class TdscNoticePublishAction extends BaseAction {

	private TdscNoticePublishService tdscNoticePublishService;

	private TdscFileService tdscFileService;

	private AppFlowService appFlowService;

	private TdscNoticeService tdscNoticeService;

	private CommonQueryService commonQueryService;

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscNoticePublishService(TdscNoticePublishService tdscNoticePublishService) {
		this.tdscNoticePublishService = tdscNoticePublishService;
	}

	public ActionForward queryNoticeList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();

		bindObject(condition, form);
		condition.setNoticeStatus("02");
		condition.setIfResultPublish("0");// 交易结果未公示的记录，未成交的数据

		if (!StringUtils.isEmpty(request.getParameter("currentPage")))
			condition.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 获得按钮权限列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		// 转化为buttonMap
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}
		// 判断是否是管理员，若不是管理员则只能查询该用户提交的地块信息
		if (buttonMap.get(GlobalConstants.BUTTON_ID_TDSC_ADMIN) == null) {
			condition.setUserId(user.getUserId());
		}
		// 设置页面行数
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		// 查询列表
		PageList pageList = tdscNoticePublishService.findPageList(condition);
		List list = new ArrayList();

		if (pageList != null) {
			// 增加页面需要的字段 from tdscBlockAppView----strat
			list = pageList.getList();
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {

				TdscNoticeApp app = (TdscNoticeApp) list.get(i);

				TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
				conditionBlock.setNoticeId(app.getNoticeId());

				// 所有地块信息表
				List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListWithoutNode(conditionBlock);
				String blockName = "";
				if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
					for (int j = 0; j < tdscBlockAppViewList.size(); j++) {

						TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);

						blockName += blockapp.getBlockName() + ",";
						app.setTranManTel(blockapp.getTradeNum());// 将"招拍挂编号"存在此字段中
						app.setTranManAddr(blockapp.getTransferMode());// 将"出让方式"存在此字段中
					}
					blockName = blockName.substring(0, blockName.length() - 1);
					app.setTranManName(blockName);// 将地块名称存在此字段中
				}

			}
		}
		// -----end

		request.setAttribute("pageList", pageList);
		request.setAttribute("condition", condition);

		return mapping.findForward("noticeList");
	}

	public ActionForward fabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		request.setAttribute("modeNameEn", "notice_mode.doc");
		String recordId = "";

		// 判断TdscNoticeApp表中的recordId值是否为空
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
		return mapping.findForward("toFabu");
	}

	/**
	 * 保存出让公告在本地 08.03.07
	 */
	// public ActionForward saveFabu_bak(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response) throws
	// Exception {
	// 获取页面参数
	// String noticeId = request.getParameter("noticeId");
	// 获得用户信息
	// SysUser user = (SysUser)
	// request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
	// if (!"".equals(noticeId)) {
	// tdscNoticePublishService.updateAndSaveOpnn(noticeId,user);
	// }
	// return mapping.findForward("success");
	// }
	/**
	 * 保存出让公告在本地 09.06.13
	 */
	public ActionForward saveFabu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		String RecordID = request.getParameter("RecordID");
		//再发布的标识
		String flag = request.getParameter("flag");
		
		String noticeNo = "";
		if (null != noticeId) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
			}
		}

		// 获取发送列表
		List tdscSendEmailList = new ArrayList();
		if (request.getParameterValues("acceptOgr") != null && request.getParameterValues("acceptAddress") != null) {
			String[] acceptOgr = request.getParameterValues("acceptOgr");
			String[] acceptAddress = request.getParameterValues("acceptAddress");

			if (acceptOgr != null && acceptOgr.length > 0 && acceptAddress != null && acceptAddress.length > 0) {
				for (int i = 0; i < acceptOgr.length; i++) {
					TdscSendEmailInfo tdscSendEmailInfo = new TdscSendEmailInfo();
					tdscSendEmailInfo.setIfSend("1");
					tdscSendEmailInfo.setNoticeId(noticeId);
					tdscSendEmailInfo.setNoticeType("1");
					tdscSendEmailInfo.setAcceptOrg(acceptOgr[i]);
					tdscSendEmailInfo.setAcceptAddress(acceptAddress[i]);
					tdscSendEmailInfo.setRecordId(RecordID);
					tdscSendEmailInfo.setNoticeNo(noticeNo);

					tdscSendEmailList.add(tdscSendEmailInfo);
				}
			}
		}

		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		if (!"".equals(noticeId)) {
			if("1".equals(flag)){
				tdscNoticePublishService.updateAndSaveOpnn(noticeId, RecordID, user, tdscSendEmailList,false);
			}else{
				tdscNoticePublishService.updateAndSaveOpnn(noticeId, RecordID, user, tdscSendEmailList,true);
			}
		}
		request.setAttribute("saveMessage", "保存成功!");

		return mapping.findForward("success");
	}

	/**
	 * 保存出让公告 09.06.13
	 */
	public ActionForward saveNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		String RecordID = request.getParameter("RecordID");
		String getmodeNameEn = request.getParameter("fileName");
		String type = (String) request.getAttribute("type");
		String createNoticeNo1 = request.getParameter("createNoticeNo1");
		String createNoticeNo2 = request.getParameter("createNoticeNo2");
		String planId= request.getParameter("planId");
		String noticeNo = "";
		if (null != noticeId && !"".equals(noticeId)) {
			TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
			tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				noticeNo = tdscNoticeApp.getNoticeNo();
			}
		}

		// 获取发送列表
		List tdscSendEmailList = new ArrayList();
		if (request.getParameterValues("acceptOgr") != null && request.getParameterValues("acceptAddress") != null) {
			String[] acceptOgr = request.getParameterValues("acceptOgr");
			String[] acceptAddress = request.getParameterValues("acceptAddress");

			if (acceptOgr != null && acceptOgr.length > 0 && acceptAddress != null && acceptAddress.length > 0) {
				for (int i = 0; i < acceptOgr.length; i++) {
					TdscSendEmailInfo tdscSendEmailInfo = new TdscSendEmailInfo();
					tdscSendEmailInfo.setIfSend("0");
					tdscSendEmailInfo.setNoticeId(noticeId);
					tdscSendEmailInfo.setNoticeType("1");
					tdscSendEmailInfo.setAcceptOrg(acceptOgr[i]);
					tdscSendEmailInfo.setAcceptAddress(acceptAddress[i]);
					tdscSendEmailInfo.setRecordId(RecordID);
					tdscSendEmailInfo.setNoticeNo(noticeNo);

					tdscSendEmailList.add(tdscSendEmailInfo);
				}
			}
		}
		// 获得用户信息
		SysUser user = (SysUser) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

		// 跟新或保存公告和邮件发送列表
		if (!"".equals(noticeId)) {
			tdscFileService.saveAndUpdate(noticeId, user.getUserId(), RecordID, getmodeNameEn, tdscSendEmailList);
		}
		request.setAttribute("saveMessage", "保存成功!");
		request.setAttribute("saveFlag", "success");
		request.setAttribute("recordId", RecordID);
		request.setAttribute("modeNameEn", getmodeNameEn);
		request.setAttribute("type", type);

		// return mapping.findForward("FirstSave");
		return new ActionForward("file.do?method=printNotice&noticeId=" + noticeId + "&printType=notice&planId=" + planId + "&createNoticeNo1=" + createNoticeNo1
				+ "&createNoticeNo2=" + createNoticeNo2 + "&getnoticeNo=true", true);
	}

	/**
	 * 下载出让公告文件到本地 08.03.07
	 */
	public ActionForward downLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		String fileType = request.getParameter("fileType");
		request.setAttribute("noticeId", noticeId);
		String recordId = "";
		// 判断TdscNoticeApp表中的recordId值是否为空
		if (null != noticeId) {
			if ("crgg".equals(fileType)) {
				// 出让公告
				// TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
				// tdscBlockFileApp =
				// tdscFileService.getBlockFileAppById(noticeId);
				TdscNoticeApp tdscNoticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(noticeId);
				if (tdscNoticeApp != null) {
					recordId = tdscNoticeApp.getRecordId();
					if (recordId != null) {
						request.setAttribute("recordId", recordId);
					}
				}
			} else {
				// 出让文件
				TdscNoticeApp tdscNoticeApp = tdscNoticePublishService.queryNoticeAppByNoticeId(noticeId);
				if (tdscNoticeApp != null) {
					recordId = tdscNoticeApp.getRecordId();
					if (recordId != null) {
						request.setAttribute("recordId", recordId);
					}
				}
			}
		}
		request.setAttribute("type", "downLoad");
		return mapping.findForward("toDownLoad");
	}

	/**
	 * 20080620新增：地块出让公告 在信息发布模块的”修改”功能 smw
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward xiugai(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		request.setAttribute("noticeId", noticeId);
		// request.setAttribute("modeNameEn", "notice_mode.doc");
		String recordId = "";
		// 判断TdscNoticeApp表中的recordId值是否为空
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
		request.setAttribute("type", "xiugai");
		return mapping.findForward("modNotice");
	}

	/**
	 * 转到公告发布页面，取得保存的公告 08.03.07
	 */
	public ActionForward toSaveNotice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取页面参数
		String noticeId = request.getParameter("noticeId");
		String type = request.getParameter("type");

		TdscBlockTranApp tdscBlockTranApp = null;
		List list = this.tdscNoticeService.findTdscBlockTranAppByNoticeNo(noticeId, null);
		if (list != null && list.size() > 0) {
			tdscBlockTranApp = (TdscBlockTranApp) list.get(0);
		}

		String recordId = "";
		String modeNameEn = "";
		String noticeNo = "";

		// 判断TdscNoticeApp表中的recordId值是否为空
		if (null != noticeId) {
			TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
			tdscBlockFileApp = tdscFileService.getBlockFileAppById(tdscBlockTranApp.getPlanId());
			if (tdscBlockFileApp != null) {
				recordId = tdscBlockFileApp.getRecordId();
				modeNameEn = tdscBlockFileApp.getFileUrl();

				request.setAttribute("recordId", recordId);
				request.setAttribute("modeNameEn", modeNameEn);
			}

			// 如果有，则获取并保存发送列表
			List tdscSendEmailInfoList = new ArrayList();
			if (request.getParameterValues("acceptOgr") != null && request.getParameterValues("acceptAddress") != null) {
				String[] acceptOgr = request.getParameterValues("acceptOgr");
				String[] acceptAddress = request.getParameterValues("acceptAddress");

				if (null != noticeId && !"".equals(noticeId)) {
					TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
					tdscNoticeApp = tdscNoticeService.findNoticeAppByNoticeId(noticeId);
					if (tdscNoticeApp != null) {
						noticeNo = tdscNoticeApp.getNoticeNo();
					}
				}

				if (acceptOgr != null && acceptOgr.length > 0 && acceptAddress != null && acceptAddress.length > 0) {
					for (int i = 0; i < acceptOgr.length; i++) {
						TdscSendEmailInfo tdscSendEmailInfo = new TdscSendEmailInfo();
						tdscSendEmailInfo.setIfSend("0");
						tdscSendEmailInfo.setNoticeId(noticeId);
						tdscSendEmailInfo.setNoticeType("1");
						tdscSendEmailInfo.setAcceptOrg(acceptOgr[i]);
						tdscSendEmailInfo.setAcceptAddress(acceptAddress[i]);
						tdscSendEmailInfo.setRecordId(recordId);
						tdscSendEmailInfo.setNoticeNo(noticeNo);

						tdscSendEmailInfoList.add(tdscSendEmailInfo);
					}
				}
				tdscNoticePublishService.saveSendEmailList(tdscSendEmailInfoList, noticeId, "1");
			}

			// 查询有无已保存的邮件发送列表 出让公告
			List tdscSendEmailList = new ArrayList();
			tdscSendEmailList = tdscNoticePublishService.querySendEmailListByType(noticeId, "1");

			request.setAttribute("tdscSendEmailList", tdscSendEmailList);
			request.setAttribute("noticeId", noticeId);
		}
		request.setAttribute("type", type);

		TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		tdscBaseQueryCondition.setNoticeId(noticeId);
		List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListByPlanId(tdscBaseQueryCondition);

		String transferMode = "";
		String endTime = "";
		String tradeNum = "0000000";// 用来存放招拍挂编号的年份和流水号，并将该变量组成文件名，初始默认为0000000(4位年份加3位流水号)
		String ydxz = "000";// 用地性质,分工业和经营性两种,代码为3位数字

		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
				TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(m);

				if ("04".equals(tdscBlockAppView.getTranResult())) {// 如果是交易终止的地块，则跳过後面部份
					continue;
				}

				if (StringUtils.isNotBlank(tdscBlockAppView.getTransferMode())) {
					if ("3107".equals(tdscBlockAppView.getTransferMode())) {
						transferMode = "Z_";
					} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
						transferMode = "P_";
					} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
						transferMode = "G_";
					}
				}
				endTime = DateUtil.date2String(tdscBlockAppView.getIssueEndDate(), "yyyyMMddHHmmss");

				if (StringUtils.isNotBlank(tdscBlockAppView.getBlockQuality())) {
					ydxz = tdscBlockAppView.getBlockQuality();
				}

				if (StringUtils.isNotBlank(tdscBlockAppView.getTradeNum()) && tdscBlockAppView.getTradeNum().split("-").length == 3) {// 取招拍挂编号里的年份和流水号
					tradeNum = tdscBlockAppView.getTradeNum().split("-")[1] + tdscBlockAppView.getTradeNum().split("-")[2];
				}
			}
		}
		request.setAttribute("fileName", transferMode + endTime + ydxz + tradeNum + ".html");

		if (type != null & "xiugai".equals(type))// 转到出让公告修改页面
			return mapping.findForward("noticeEdit");
		else
			// 转到出让公告发布页面
			return mapping.findForward("publishNoticeSave");
	}
	/**
	 * 导出上报数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward expData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String noticeId = request.getParameter("noticeId");
		String noticeNo = request.getParameter("noticeNo");
		UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		ExpDataService expData = new ExpDataService(noticeId, noticeNo,user.getLogonName());
		String xml = "";
		String fileName = noticeNo+".xml";
		fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		fileName = new String(fileName.getBytes("UTF-8"), "GBK"); 
		xml = expData.expData();
		response.setContentType("application/octet-stream;charset=gbk");
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setContentLength((int) xml.getBytes().length); 
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		byte[] bytes = xml.getBytes() ; 
		ByteArrayInputStream inputstream=new ByteArrayInputStream(bytes);
		byte abyte0[] = new byte[1024]; 
		int k = 0;
        while ((k=inputstream.read(abyte0, 0, 1024))>-1) {
        	toClient.write(abyte0, 0, k);
        } 
		toClient.flush();
		toClient.close();
		return null;
	}
}
