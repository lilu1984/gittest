package com.wonders.tdsc.stat.web.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.bo.UserInfo;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.DateUtil;
import com.wonders.esframework.util.PageUtil;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscFileSaleInfoService;
import com.wonders.tdsc.blockwork.service.TdscNoticeService;
import com.wonders.tdsc.blockwork.service.TdscScheduletableService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.stat.service.TdscTradeStatisticsService;
import com.wonders.tdsc.stat.web.form.TdscTradeStatisticsForm;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.tdscbase.service.TdscBidderViewService;

public class TdscTradeStatisticsAction extends BaseAction {

	private TdscTradeStatisticsService	tdscTradeStatService;

	private TdscScheduletableService	tdscScheduletableService;

	private CommonQueryService			commonQueryService;

	private TdscBidderViewService		tdscBidderViewService;

	private TdscBlockInfoService		tdscBlockInfoService;

	private TdscNoticeService			tdscNoticeService;

	private TdscFileSaleInfoService		tdscFileSaleInfoService;

	private TdscLocalTradeService		tdscLocalTradeService;
	private TdscBidderAppService		tdscBidderAppService;

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscBidderViewService(TdscBidderViewService tdscBidderViewService) {
		this.tdscBidderViewService = tdscBidderViewService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscTradeStatService(TdscTradeStatisticsService tdscTradeStatService) {
		this.tdscTradeStatService = tdscTradeStatService;
	}

	public void setTdscScheduletableService(TdscScheduletableService tdscScheduletableService) {
		this.tdscScheduletableService = tdscScheduletableService;
	}

	public void setTdscNoticeService(TdscNoticeService tdscNoticeService) {
		this.tdscNoticeService = tdscNoticeService;
	}

	public CommonQueryService getCommonQueryService() {
		return commonQueryService;
	}

	public TdscBidderViewService getTdscBidderViewService() {
		return tdscBidderViewService;
	}

	public TdscBlockInfoService getTdscBlockInfoService() {
		return tdscBlockInfoService;
	}

	public TdscScheduletableService getTdscScheduletableService() {
		return tdscScheduletableService;
	}

	public TdscTradeStatisticsService getTdscTradeStatService() {
		return tdscTradeStatService;
	}

	public TdscFileSaleInfoService getTdscFileSaleInfoService() {
		return tdscFileSaleInfoService;
	}

	public void setTdscFileSaleInfoService(TdscFileSaleInfoService tdscFileSaleInfoService) {
		this.tdscFileSaleInfoService = tdscFileSaleInfoService;
	}

	public TdscNoticeService getTdscNoticeService() {
		return tdscNoticeService;
	}

	public TdscLocalTradeService getTdscLocalTradeService() {
		return tdscLocalTradeService;
	}

	public void setTdscLocalTradeService(TdscLocalTradeService tdscLocalTradeService) {
		this.tdscLocalTradeService = tdscLocalTradeService;
	}

	public TdscBidderAppService getTdscBidderAppService() {
		return tdscBidderAppService;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}


	public ActionForward toShowHistoryTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String userId = user.getUserId();

		// 获得Session中登录用户的按钮信息列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}

		// 页面参数值：招拍挂编号
		String fromMenu = request.getParameter("fromMenu");
		String pageNo = request.getParameter("currentPage");
		String tradeNum = request.getParameter("tradeNum");
		String transferMode = request.getParameter("transferMode");
		List queryList = null;
		TdscBlockPlanTableCondition planCond = new TdscBlockPlanTableCondition();
		this.bindObject(planCond, form);
		// status 00 is history records
		planCond.setStatus("00");

		if (!StringUtils.isEmpty(fromMenu) && "NO".equals(fromMenu)) {
			if (!StringUtils.isEmpty(tradeNum))
				planCond.setTradeNum(tradeNum);
			if (!StringUtils.isEmpty(transferMode))
				planCond.setTransferMode(transferMode);

		}
		queryList = (List) tdscScheduletableService.queryPlanTableList(planCond);
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				TdscBlockPlanTable planInfo = (TdscBlockPlanTable) queryList.get(i);

				List tranApps = tdscScheduletableService.queryTranAppList(planInfo.getPlanId());
				if (tranApps != null && tranApps.size() > 0) {
					TdscBlockTranApp tranApp = (TdscBlockTranApp) tranApps.get(0);

					planInfo.setPreNoticeNo(tranApp.getNoticeId());// 该实施方案的
					// NoticeId

				}
			}
		}

		// 构造分页
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
		request.setAttribute("planCondition", planCond);
		return mapping.findForward("toShowHistoryTradePage");
	}

	// 历史地块实施方案信息
	public ActionForward viewHistoryPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");
		TdscBlockPlanTable planInfo = tdscScheduletableService.findPlanTableByPlanId(planId);
		request.setAttribute("planTable", planInfo);

		List tmpList = tdscScheduletableService.queryTranAppList(planId);
		if (tmpList != null && tmpList.size() > 0) {
			TdscBlockTranApp tranApp = (TdscBlockTranApp) tmpList.get(0);
			request.setAttribute("marginEndDate", DateUtil.date2String(tranApp.getMarginEndDate(), DateUtil.FORMAT_DATETIME));
		}

		return mapping.findForward("detailHistoryPlanPage");
	}

	public ActionForward detailHistoryTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");
		// 根据 planId得到所有详细信息
		// 地块信息(list)，每个地块对应的出让文件
		List retList = null;
		List blockIds = tdscScheduletableService.queryBlockIdListByPlanId(planId);
		if (blockIds != null && blockIds.size() > 0) {
			retList = new ArrayList();
			for (int i = 0; i < blockIds.size(); i++) {
				String blockId = blockIds.get(i) + "";
				Map tmpMap = new HashMap();
				TdscBlockAppView appView = commonQueryService.getTdscBlockAppViewByBlockId(blockId);
				tmpMap.put("TdscBlockAppView", appView);
				tmpMap.put("TdscBlockInfo", tdscScheduletableService.getBlockInfoApp(blockId));
				if ("1".equals(appView.getIsPurposeBlock())) {
					TdscBidderView bidderApp = tdscBidderViewService.getYixiangNameLikeAppId(appView.getAppId());
					tmpMap.put("pursposeName", bidderApp.getBidderName());
				}
				tmpMap.put("TdscBlockRemisemoneyDefrayList", tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockId));
				tmpMap.put("TdscBlockInfoPart", tdscScheduletableService.getTdscBlockPartList(blockId).get(0));
				tmpMap.put("TdscBlockTranApp", tdscScheduletableService.findBlockTranAppInfo(appView.getAppId()));
				retList.add(tmpMap);
			}
		}
		request.setAttribute("retList", retList);

		return mapping.findForward("detailHistoryTradePage");
	}

	// 查询当前交易
	public ActionForward toShowCurrentTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
		String userId = user.getUserId();
		List noticeApps = null;
		// 获得Session中登录用户的按钮信息列表
		List buttonList = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_BUTTON_MAP);
		Map buttonMap = new HashMap();
		for (int j = 0; j < buttonList.size(); j++) {
			String id = (String) buttonList.get(j);
			buttonMap.put(id, buttonList.get(j));
		}

		// 页面参数值：招拍挂编号
		String fromMenu = request.getParameter("fromMenu");
		String pageNo = request.getParameter("currentPage");
		String tradeNum = request.getParameter("tradeNum");
		String transferMode = request.getParameter("transferMode");
		List queryList = null;
		TdscBlockPlanTableCondition planCond = new TdscBlockPlanTableCondition();
		this.bindObject(planCond, form);
		// status 01 is current records
		planCond.setStatus("01");

		if (!StringUtils.isEmpty(fromMenu) && "NO".equals(fromMenu)) {
			if (!StringUtils.isEmpty(tradeNum))
				planCond.setTradeNum(tradeNum);
			if (!StringUtils.isEmpty(transferMode))
				planCond.setTransferMode(transferMode);

		}
		queryList = (List) tdscScheduletableService.queryPlanTableList(planCond);
		if (queryList != null && queryList.size() > 0) {
			for (int i = 0; i < queryList.size(); i++) {
				TdscBlockPlanTable planInfo = (TdscBlockPlanTable) queryList.get(i);
				List tranApps = tdscScheduletableService.queryTranAppList(planInfo.getPlanId());
				if (tranApps != null && tranApps.size() > 0) {
					TdscBlockTranApp tranApp = (TdscBlockTranApp) tranApps.get(0);
					planInfo.setPreNoticeNo(tranApp.getNoticeId());// 该实施方案的
					// NoticeId
					TdscNoticeAppCondition cond = new TdscNoticeAppCondition();
					cond.setNoticeId(tranApp.getNoticeId());
					PageList pageList = tdscNoticeService.findPageList(cond);
					if (pageList != null) {
						noticeApps = pageList.getList();
						TdscNoticeApp noticeApp = (TdscNoticeApp) noticeApps.get(0);
						planInfo.setListLoc(noticeApp.getIfReleased());// 公告是否已经发布
					}
				}
			}
		}

		// 构造分页
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
		request.setAttribute("planCondition", planCond);

		return mapping.findForward("toShowCurrentTradePage");
	}

	/*
	 * planId下，所有地块
	 */
	public ActionForward detailCurrentTrade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String planId = request.getParameter("planId");
		// 根据 planId得到所有详细信息
		// 地块信息(list)，每个地块对应的出让文件
		List retList = null;
		List blockIds = tdscScheduletableService.queryBlockIdListByPlanId(planId);
		if (blockIds != null && blockIds.size() > 0) {
			retList = new ArrayList();
			for (int i = 0; i < blockIds.size(); i++) {
				String blockId = blockIds.get(i) + "";
				Map tmpMap = new HashMap();
				TdscBlockAppView appView = commonQueryService.getTdscBlockAppViewByBlockId(blockId);
				tmpMap.put("TdscBlockAppView", appView);
				tmpMap.put("TdscBlockInfo", tdscScheduletableService.getBlockInfoApp(blockId));
				if ("1".equals(appView.getIsPurposeBlock())) {
					TdscBidderView bidderApp = tdscBidderViewService.getYixiangNameLikeAppId(appView.getAppId());
					tmpMap.put("pursposeName", bidderApp.getBidderName());
				}
				tmpMap.put("TdscBlockRemisemoneyDefrayList", tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(blockId));
				tmpMap.put("TdscBlockInfoPart", tdscScheduletableService.getTdscBlockPartList(blockId).get(0));
				tmpMap.put("TdscBlockTranApp", tdscScheduletableService.findBlockTranAppInfo(appView.getAppId()));
				retList.add(tmpMap);
			}
		}
		request.setAttribute("retList", retList);
		return mapping.findForward("detailCurrentTradePage");
	}

	// 当前地块详细信息
	public ActionForward viewCurrentPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String planId = request.getParameter("planId");
		TdscBlockPlanTable planInfo = tdscScheduletableService.findPlanTableByPlanId(planId);
		request.setAttribute("planTable", planInfo);

		List tmpList = tdscScheduletableService.queryTranAppList(planId);
		if (tmpList != null && tmpList.size() > 0) {
			TdscBlockTranApp tranApp = (TdscBlockTranApp) tmpList.get(0);
			request.setAttribute("marginEndDate", DateUtil.date2String(tranApp.getMarginEndDate(), DateUtil.FORMAT_DATETIME));
		}

		return mapping.findForward("detailCurrentPlanPage");
	}

	
	/*
	 * 检查出让活动中是否存在溢价率大于50%的异常地块
	 */
	public ActionForward checkYiChang(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 对于105个重点城市成交的总价最高地块、溢价率超过50%的地块均应填报此表
		// 溢价率 = 当前价-起始价 /起始价 *100%

		// TDSC_BLOCK_TRAN_APP
		List expBlockList = new ArrayList();
		String planId = request.getParameter("planId");
		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setPlanId(planId);
		List appViews = commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
		if (appViews != null && appViews.size() > 0)
			for (int i = 0; i < appViews.size(); i++) {

				TdscBlockAppView appInfo = (TdscBlockAppView) appViews.get(i);
				TdscBlockTranApp tranApp = tdscLocalTradeService.getTdscBlockTranApp(appInfo.getAppId());

				BigDecimal initPrice = tranApp.getInitPrice();// 起始价
				BigDecimal currPrice = tranApp.getResultPrice();// 成交价格
				if ("01".equals(tranApp.getTranResult()) && isExpceptionPrice(initPrice, currPrice)) {

					TdscBlockPart blockPart = tdscLocalTradeService.getBlockInfoPart(appInfo.getBlockId());
					TdscNoticeApp noticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(tranApp.getNoticeId());
					List auctionApps = tdscLocalTradeService.getTdscAuctionAppByAppId(appInfo.getAppId());

					// TDSC_AUCTION_APP
					// 该地块竞价轮数
					String auctionCounts = "1";
					if (auctionApps != null && auctionApps.size() > 0)
						auctionCounts = auctionApps.size() + "";
					expBlockList.add(tdscTradeStatService.getExceptionInfo(appInfo, blockPart, tranApp, noticeApp, auctionCounts));
				}

			}

		 //将内容设置到输出
		 response.setContentType("text/xml; charset=UTF-8");
		 response.setHeader("Cache-Control", "no-cache");
		 PrintWriter pw = response.getWriter();
		 // 返回给回调函数的参数
		
		if (expBlockList != null && expBlockList.size() > 0) {
			 pw.write("1," + planId);//存在溢价率大于50%的地块
			 pw.close();
		}else{
			 pw.write("0," + planId);//不存在溢价率大于50%的地块
			 pw.close();
		}
		
		return null;
	}
	
	/*
	 * 异常统计表
	 */
	public ActionForward printYiChangExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 对于105个重点城市成交的总价最高地块、溢价率超过50%的地块均应填报此表
		// 溢价率 = 当前价-起始价 /起始价 *100%

		// TDSC_BLOCK_TRAN_APP
		List expBlockList = new ArrayList();
		String planId = request.getParameter("planId");
		TdscBaseQueryCondition cond = new TdscBaseQueryCondition();
		cond.setPlanId(planId);
		List appViews = commonQueryService.queryTdscBlockAppViewListByPlanId(cond);
		if (appViews != null && appViews.size() > 0)
			for (int i = 0; i < appViews.size(); i++) {

				TdscBlockAppView appInfo = (TdscBlockAppView) appViews.get(i);
				TdscBlockTranApp tranApp = tdscLocalTradeService.getTdscBlockTranApp(appInfo.getAppId());

				BigDecimal initPrice = tranApp.getInitPrice();// 起始价
				BigDecimal currPrice = tranApp.getResultPrice();// 成交价格
				if ("01".equals(tranApp.getTranResult()) && isExpceptionPrice(initPrice, currPrice)) {

					TdscBlockPart blockPart = tdscLocalTradeService.getBlockInfoPart(appInfo.getBlockId());
					TdscNoticeApp noticeApp = tdscNoticeService.getTdscNoticeAppByNoticeId(tranApp.getNoticeId());
					List auctionApps = tdscLocalTradeService.getTdscAuctionAppByAppId(appInfo.getAppId());

					// TDSC_AUCTION_APP
					// 该地块竞价轮数
					String auctionCounts = "1";
					if (auctionApps != null && auctionApps.size() > 0)
						auctionCounts = auctionApps.size() + "";
					expBlockList.add(tdscTradeStatService.getExceptionInfo(appInfo, blockPart, tranApp, noticeApp, auctionCounts));
				}

			}

		if (expBlockList != null && expBlockList.size() > 0) {
			System.out.println(expBlockList);
			// TODO 写入excel
		}

		String path = request.getSession().getServletContext().getRealPath("/");
		if (path.endsWith("/")) path += File.separator;
		path +=  "iweboffice" + File.separator + "Template" + File.separator + "Exception_Info.xls";
		// 生成 excel 并提示下载
		String outFileName = new String("异常表".getBytes(),"utf-8");
		response.reset();
		response.setContentType("application/x-msdownload");
		response.addHeader("Content-Disposition","attachment;filename=ccc.xls");
		
		OutputStream output = response.getOutputStream();
		//WritableWorkbook book = null;
		HSSFWorkbook book = null;
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 新建excel文件
		try {
			book = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (book != null) {
			// 循环生成sheet
			int i = 0;
			for (i = 0 ; expBlockList != null && i < expBlockList.size(); i++) {
				
				TdscTradeStatisticsForm bean = (TdscTradeStatisticsForm) expBlockList.get(i);
				book.cloneSheet(i);
				book.setSheetName(i, "sheet" + (i+1));
				HSSFSheet sheet = book.getSheetAt(i);
				
				// 填报时间
				HSSFRow row = sheet.getRow(2);
				HSSFCell cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString("填报时间：" +bean.getReportDate()));
				
				// 宗地名称
				row = sheet.getRow(3);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getBlockName()));
				
				// 受让人
				row = sheet.getRow(3);
				cell = row.getCell(8);
				cell.setCellValue(new HSSFRichTextString(bean.getBidderName()));
				
				// 具体位置
				row = sheet.getRow(4);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getBlockLocation()));
				
				// 面积
				row = sheet.getRow(8);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getBlockArea()));
				
				// 建筑面积
				row = sheet.getRow(9);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getBuildArea()));
				
				// 房屋用途
				row = sheet.getRow(9);
				cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString(bean.getBuildUsed()));
				
				// 绿地率
				row = sheet.getRow(10);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getGreenRate()));
				
				// 容积率
				row = sheet.getRow(10);
				cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString(bean.getVolumeRate()));
				
				// 建筑密度
				row = sheet.getRow(11);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getDensityRate()));
				
				// 招拍挂公告编号
				row = sheet.getRow(12);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getNoticeNo()));
				
				// 公告时间
				row = sheet.getRow(12);
				cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString(bean.getNoticeDate()));
				
				// 竞价轮次
				row = sheet.getRow(12);
				cell = row.getCell(9);
				cell.setCellValue(new HSSFRichTextString(bean.getAuctionCount()));
				
//				// 成交确认书编号
//				row = sheet.getRow(3);
//				cell = row.getCell(8);
//				cell.setCellValue(new HSSFRichTextString("受让人"));
				
				// 成交确认书签订时间
				row = sheet.getRow(13);
				cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString(bean.getTranTime()));
				
				// 交易底价总额
				row = sheet.getRow(14);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getInitPrice()));
				
				// 地面低价
				row = sheet.getRow(14);
				cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString(bean.getiBlockPrice()));
				
				// 地面楼价
				row = sheet.getRow(14);
				cell = row.getCell(9);
				cell.setCellValue(new HSSFRichTextString(bean.getiBuildPrice()));
				
				// 起始价总额
				row = sheet.getRow(15);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getInitPrice()));
				
				// 地面低价
				row = sheet.getRow(15);
				cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString(bean.getiBlockPrice()));
				
				// 露面低价
				row = sheet.getRow(15);
				cell = row.getCell(9);
				cell.setCellValue(new HSSFRichTextString(bean.getiBuildPrice()));
				
				// 成交价总额
				row = sheet.getRow(16);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getTranPrice()));
				
				// 地面地价
				row = sheet.getRow(16);
				cell = row.getCell(4);
				cell.setCellValue(new HSSFRichTextString(bean.gettBlockPrice()));
				
				// 楼面地价
				row = sheet.getRow(16);
				cell = row.getCell(9);
				cell.setCellValue(new HSSFRichTextString(bean.gettBuildPrice()));
				
				// 具体交易方式
				row = sheet.getRow(17);
				cell = row.getCell(2);
				cell.setCellValue(new HSSFRichTextString(bean.getTranMode()));
				
			}
			book.removeSheetAt(i);
			book.write(output);
		}
		
		try {
			
			os.flush();
			os.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		// WritableWorkbook book = null;
		// WritableSheet sheet = null;
		// OutputStream os = null;
		// // 根据 planId得到所有详细信息
		// // 地块信息(list)，每个地块对应的出让文件
		// List blockIds = tdscScheduletableService.queryBlockIdListByPlanId(planId);
		// if (blockIds != null && blockIds.size() > 0) {
		// for (int i = 0; i < blockIds.size(); i++) {
		// String blockId = blockIds.get(i) + "";
		// TdscBlockAppView appView = commonQueryService.getTdscBlockAppViewByBlockId(blockId);
		//
		// String outPutName = appView.getNoitceNo() + "_Exception_Info" + ".xls";
		// outPutName = new String(outPutName.getBytes("GBK"), "ISO-8859-1");
		// request.setCharacterEncoding("GBK");
		// response.reset();
		// response.setContentType("application/x-msdownload");
		// response.addHeader("Content-Disposition", "attachment;filename=" + outPutName);
		// // 取得模板路径 开始
		// String path = request.getSession().getServletContext().getRealPath("/");
		// if (path.endsWith("/"))
		// path += File.separator;
		// String fileName = path + "iweboffice\\Template\\Exception_Info.xls";
		// // 取得模板路径 结束
		// // 创建输出excel文件 开始
		// try {
		// os = new BufferedOutputStream(response.getOutputStream());
		// } catch (IOException e2) {
		// e2.printStackTrace();
		// }
		// try {
		// // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
		// try {
		// // 根据模板复制一个新的excel文件
		// book = Workbook.createWorkbook(os, Workbook.getWorkbook(new File(fileName)));
		//
		// } catch (BiffException e) {
		// e.printStackTrace();
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// // 创建excel 文件结束
		// if (book != null) {
		// // 创建一个可写入的工作表
		// // Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
		// sheet = book.getSheet(0);
		//
		// // 写入数据 Label(i,j) i表示第几列(从0开始) j表示第几行(从0开始)
		// try {
		// WritableCellFormat rwcfF = new WritableCellFormat();
		// rwcfF.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
		// // 设置右边框线为实线
		// rwcfF.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
		// // 设置顶部框线为实线
		// rwcfF.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM);
		// // 地块名称
		// Label label1 = new Label(4, 3, StringUtils.trimToEmpty(appView.getBlockName()), rwcfF);
		// sheet.addCell(label1);
		//
		// } catch (RowsExceededException e1) {
		// e1.printStackTrace();
		// } catch (WriteException e1) {
		// e1.printStackTrace();
		// }
		// try {
		// // 从内存中写入文件中
		// book.write();
		// // 关闭资源，释放内存
		// book.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (WriteException e) {
		// e.printStackTrace();
		// }
		// }
		// try {
		// // 关闭
		// os.flush();
		// os.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// }
		// }
		//
		return null;

	}

	/**
	 * 溢价率是否超过50%
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	private boolean isExpceptionPrice(BigDecimal t1, BigDecimal t2) {
		// 溢价率 = 当前价-起始价 /起始价 *100%
		// 溢价率大于50%，返回true;
		BigDecimal t3 = t2.subtract(t1);
		BigDecimal t4 = t3.divide(t1, 2, BigDecimal.ROUND_UP);
		BigDecimal ret = t4.multiply(new BigDecimal("100"));
		if (ret.compareTo(new BigDecimal("50")) < 0)
			return false;
		return true;

	}
	
	/**
	 * 统计表查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toShowStatisticalFind(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pageNo = request.getParameter("currentPage");
		List queryList = null;
		TdscBlockPlanTableCondition planCond = new TdscBlockPlanTableCondition();
		this.bindObject(planCond, form);
		// status 00 is history records
//		planCond.setStatus("00");

		queryList = (List) tdscScheduletableService.queryTdscBlockInfoList(planCond);
		
		// 构造分页
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
		request.setAttribute("planCondition", planCond);
		return mapping.findForward("toShowStaatisticsPage");
	}
	
	/*
	 * 地块信息导出为excel
	 */
	public ActionForward impBlockInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 地块信息(list)，每个地块对应的出让文件
		TdscBlockPlanTableCondition planCond = new TdscBlockPlanTableCondition();
		this.bindObject(planCond, form);

		List resulteList = new ArrayList();
		List blockIds  = (List) tdscScheduletableService.queryTdscBlockInfoList(planCond);

		// 地块信息(list)，每个地块对应的出让文件
		if (blockIds != null && blockIds.size() > 0) {
			for (int i = 0; i < blockIds.size(); i++) {
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) blockIds.get(i);
				Map tmpMap = new HashMap();
				//根据 地块名称得到公告时间
				TdscBlockAppView tdscBlockAppView= commonQueryService.getTdscBlockAppViewByBlockId(tdscBlockInfo.getBlockId());
				TdscBlockPlanTable planInfo = tdscScheduletableService.findPlanTableByPlanId(tdscBlockAppView.getPlanId());
				
				tmpMap.put("TdscBlockAppView", tdscBlockAppView);
				tmpMap.put("TdscBlockInfo", tdscBlockInfo);
				if ("1".equals(tdscBlockAppView.getIsPurposeBlock())) {
					TdscBidderView bidderApp = tdscBidderViewService.getYixiangNameLikeAppId(tdscBlockAppView.getAppId());
					tmpMap.put("pursposeName", bidderApp.getBidderName());
				}
				tmpMap.put("TdscBlockRemisemoneyDefrayList", tdscBlockInfoService.getTdscBlockRemisemoneyDefrayList(tdscBlockInfo.getBlockId()));
				tmpMap.put("TdscBlockInfoPart", tdscScheduletableService.getTdscBlockPartList(tdscBlockInfo.getBlockId()).get(0));
				tmpMap.put("TdscBlockTranApp", tdscScheduletableService.findBlockTranAppInfo(tdscBlockAppView.getAppId()));
				tmpMap.put("TdscListingInfo", tdscLocalTradeService.getTdscListingInfoByAppId(tdscBlockAppView.getAppId()));
				tmpMap.put("planInfo", planInfo);
				// 统计文件购买人数
				List buyfilepersonList = tdscFileSaleInfoService.countSalePerson(tdscBlockAppView.getNoticeId(), tdscBlockAppView.getAppId());
				if (buyfilepersonList != null && buyfilepersonList.size() > 0)
					tmpMap.put("CountPerson", buyfilepersonList.size() + "");

				// 统计报名人数和单位名称
				List personList = tdscBidderAppService.queryBidderAppListLikeAppId(tdscBlockAppView.getAppId());

				int iCountPerson = 0;
				if (personList != null && personList.size() > 0) {
					for (int j = 0; j < personList.size(); j++) {
						TdscBidderApp bidderApp = (TdscBidderApp) personList.get(j);

						String thisBidderMaxPrice = tdscLocalTradeService.getBidderMaxPrice(bidderApp.getYktXh(), tdscBlockAppView.getAppId());
						// String thisBidderMaxPrice ="0";
						if ("1".equals(bidderApp.getIsPurposePerson()) && "0".equals(thisBidderMaxPrice)) {
							iCountPerson++;
						}
						if (thisBidderMaxPrice != null && !StringUtil.isEmpty(thisBidderMaxPrice) && !"0".equals(thisBidderMaxPrice)) {
							iCountPerson++;
						}

					}
				}
				tmpMap.put("allbidercount", iCountPerson + "");
				List allbiddersList = commonQueryService.getAllBiddersByAppId(tdscBlockAppView.getAppId());
				tmpMap.put("allbidersList", allbiddersList);
				//
				List tdscAuctionApp = tdscLocalTradeService.getTdscAuctionAppByAppId(tdscBlockAppView.getAppId());
				if (tdscAuctionApp != null && tdscAuctionApp.size() > 0)
					tmpMap.put("xcjjRound", tdscAuctionApp.size() + "");
				resulteList.add(tmpMap);
			}
		}
		request.setAttribute("resulteList", resulteList);

		return mapping.findForward("detailHistoryStaticsPage");
	}
}
