package com.wonders.tdsc.blockwork.web;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBidderRollAction extends BaseAction {
	// 通用查询service
	private CommonQueryService		commonQueryService;
	// 土地基本信息service
	private TdscBlockInfoService	tdscBlockInfoService;
	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	
	/**
	 * 摇号地块列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String blockName = request.getParameter("blockName");
		String blockNoticeNo = request.getParameter("blockNoticeNo");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setTranResult("05");//待摇号的地块
		condition.setBlockName(blockName);
		condition.setBlockNoticeNo(blockNoticeNo);
		int currentPage = 0;
		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		condition.setCurrentPage(currentPage);
		condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

		PageList blockList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition);
		
		request.setAttribute("blockList", blockList);
		request.setAttribute("condition", condition);
		return mapping.findForward("bidderRollList");
	}
	
	
	public ActionForward toResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String appId = request.getParameter("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView app = commonQueryService.getTdscBlockAppView(condition);
		request.setAttribute("app", app);
		return mapping.findForward("bidderRollInfo");
	}
	
	public ActionForward toSaveResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String appId = request.getParameter("appId");
		String conNum = request.getParameter("conNum");
		//获得手工填写的成交价格		
		BigDecimal resultPrice=new BigDecimal(request.getParameter("resultPrice"));
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView app = commonQueryService.getTdscBlockAppView(condition);
		logger.info("-------开始保存摇号结果--------");
		try {
			TdscBlockTranApp tranApp = tdscBlockInfoService.getTdscBlockTranAppByAppId(appId);
			logger.info("------获取tranApp------");
			TdscBlockInfo blockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(app.getBlockId());
			logger.info("------获取blockInfo------");
			TdscBidderApp bidderApp = tdscBlockInfoService.getTdscBidderAppByAppIdAndConNum(appId, conNum);
			logger.info("------获取bidderApp------");
			TdscBidderPersonApp bidderPerson = tdscBlockInfoService.getTdscBidderPersonAppByBidderId(bidderApp.getBidderId());
			logger.info("------获取bidderPerson------");
			TdscListingInfo listingInfo = tdscBlockInfoService.findListingInfo(appId);
			logger.info("------获取listingInfo------");
			//交易结束
			logger.info("-------setListingInfo--------");
			listingInfo.setYktXh(bidderApp.getYktXh());
			listingInfo.setListCert(bidderApp.getCertNo());
			listingInfo.setResultCert(bidderApp.getCertNo());
			listingInfo.setResultNo(conNum);
			//listingInfo.setResultPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));
			listingInfo.setResultPrice(resultPrice);
			listingInfo.setListResult("01");
			listingInfo.setListResultDate(new Timestamp(System.currentTimeMillis()));// 成交时间
			listingInfo.setSceneResult("01");// "01"现场竞价成功。为了保持数据完整，直接插入“01”即可
			listingInfo.setSceneResultDate(new Timestamp(System.currentTimeMillis()));// 竞得时间
			// 设置报价信息
			//listingInfo.setCurrPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));
			listingInfo.setCurrPrice(resultPrice);
			listingInfo.setCurrRound(listingInfo.getCurrRound().add(new BigDecimal(1)));
			listingInfo.setListDate(new Timestamp(System.currentTimeMillis()));
			logger.info("--------setBlockInfo---------");
			blockInfo.setTranResult("01");
			blockInfo.setResultDate(new Timestamp(System.currentTimeMillis()));// 竞得时间
			//blockInfo.setResultPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));// 成交价格，万元
			blockInfo.setResultPrice(resultPrice);// 成交价格，万元
			blockInfo.setResultCert(bidderApp.getCertNo());// 竞得人的资格证书号
			blockInfo.setResultName(bidderPerson.getBidderName());// 竞得人名称
			blockInfo.setStatus("02");// (00-未交易;01-交易中;02-交易结束)
			logger.info("---------setTranApp----------");
			//tranApp.setTotalPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));// 成交价格，万元
			tranApp.setTotalPrice(resultPrice);
			tranApp.setTranResult("01");// 交易结果 00未交易 01 交易成功；02 // 交易失败（流标）；04 终止交易；
			tranApp.setResultDate(new Timestamp(System.currentTimeMillis()));// 竞得时间
			//tranApp.setResultPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));// 成交价格，万元
			tranApp.setResultPrice(resultPrice);// 成交价格，万元
			tranApp.setResultCert(conNum);// 竞得人的号牌
			tranApp.setResultName(bidderPerson.getBidderName());// 竞得人名称
			logger.info("---------开始保存各个实体---------");
			tdscBlockInfoService.updateTdscBlockTranApp(tranApp);
			logger.info("---------tranApp保存成功-----------");
			tdscBlockInfoService.updateTdscBlockInfo(blockInfo);
			logger.info("---------blockInfo保存成功-----------");
			tdscBlockInfoService.updateTdscListingInfo(listingInfo);
			logger.info("---------listingInfo保存成功-----------");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		//return mapping.findForward("bidderRollInfo");
		//回到列表页面
//		String blockName = request.getParameter("blockName");
//		String blockNoticeNo = request.getParameter("blockNoticeNo");
//		TdscBaseQueryCondition condition1 = new TdscBaseQueryCondition();
//		condition1.setTranResult("05");//待摇号的地块
//		condition1.setBlockName(blockName);
//		condition1.setBlockNoticeNo(blockNoticeNo);
//		int currentPage = 0;
//		if (StringUtils.isNotEmpty(request.getParameter("currentPage"))) {
//			currentPage = Integer.parseInt(request.getParameter("currentPage"));
//		}
//		condition1.setCurrentPage(currentPage);
//		condition1.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());
//
//		PageList blockList = commonQueryService.queryTdscBlockAppViewPageListWithoutNode(condition1);
//		
//		request.setAttribute("blockList", blockList);
//		request.setAttribute("condition", condition1);
//		return mapping.findForward("bidderRollList");
		return new ActionForward("bidderRoll.do?method=list", true);
	}
}
