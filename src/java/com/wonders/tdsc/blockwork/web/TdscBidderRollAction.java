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
	// ͨ�ò�ѯservice
	private CommonQueryService		commonQueryService;
	// ���ػ�����Ϣservice
	private TdscBlockInfoService	tdscBlockInfoService;
	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	
	/**
	 * ҡ�ŵؿ��б�
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
		condition.setTranResult("05");//��ҡ�ŵĵؿ�
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
		//����ֹ���д�ĳɽ��۸�		
		BigDecimal resultPrice=new BigDecimal(request.getParameter("resultPrice"));
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView app = commonQueryService.getTdscBlockAppView(condition);
		logger.info("-------��ʼ����ҡ�Ž��--------");
		try {
			TdscBlockTranApp tranApp = tdscBlockInfoService.getTdscBlockTranAppByAppId(appId);
			logger.info("------��ȡtranApp------");
			TdscBlockInfo blockInfo = tdscBlockInfoService.getTdscBlockInfoByBlockId(app.getBlockId());
			logger.info("------��ȡblockInfo------");
			TdscBidderApp bidderApp = tdscBlockInfoService.getTdscBidderAppByAppIdAndConNum(appId, conNum);
			logger.info("------��ȡbidderApp------");
			TdscBidderPersonApp bidderPerson = tdscBlockInfoService.getTdscBidderPersonAppByBidderId(bidderApp.getBidderId());
			logger.info("------��ȡbidderPerson------");
			TdscListingInfo listingInfo = tdscBlockInfoService.findListingInfo(appId);
			logger.info("------��ȡlistingInfo------");
			//���׽���
			logger.info("-------setListingInfo--------");
			listingInfo.setYktXh(bidderApp.getYktXh());
			listingInfo.setListCert(bidderApp.getCertNo());
			listingInfo.setResultCert(bidderApp.getCertNo());
			listingInfo.setResultNo(conNum);
			//listingInfo.setResultPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));
			listingInfo.setResultPrice(resultPrice);
			listingInfo.setListResult("01");
			listingInfo.setListResultDate(new Timestamp(System.currentTimeMillis()));// �ɽ�ʱ��
			listingInfo.setSceneResult("01");// "01"�ֳ����۳ɹ���Ϊ�˱�������������ֱ�Ӳ��롰01������
			listingInfo.setSceneResultDate(new Timestamp(System.currentTimeMillis()));// ����ʱ��
			// ���ñ�����Ϣ
			//listingInfo.setCurrPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));
			listingInfo.setCurrPrice(resultPrice);
			listingInfo.setCurrRound(listingInfo.getCurrRound().add(new BigDecimal(1)));
			listingInfo.setListDate(new Timestamp(System.currentTimeMillis()));
			logger.info("--------setBlockInfo---------");
			blockInfo.setTranResult("01");
			blockInfo.setResultDate(new Timestamp(System.currentTimeMillis()));// ����ʱ��
			//blockInfo.setResultPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));// �ɽ��۸���Ԫ
			blockInfo.setResultPrice(resultPrice);// �ɽ��۸���Ԫ
			blockInfo.setResultCert(bidderApp.getCertNo());// �����˵��ʸ�֤���
			blockInfo.setResultName(bidderPerson.getBidderName());// ����������
			blockInfo.setStatus("02");// (00-δ����;01-������;02-���׽���)
			logger.info("---------setTranApp----------");
			//tranApp.setTotalPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));// �ɽ��۸���Ԫ
			tranApp.setTotalPrice(resultPrice);
			tranApp.setTranResult("01");// ���׽�� 00δ���� 01 ���׳ɹ���02 // ����ʧ�ܣ����꣩��04 ��ֹ���ף�
			tranApp.setResultDate(new Timestamp(System.currentTimeMillis()));// ����ʱ��
			//tranApp.setResultPrice(tranApp.getMaxPrice().add(tranApp.getSpotAddPriceRange()));// �ɽ��۸���Ԫ
			tranApp.setResultPrice(resultPrice);// �ɽ��۸���Ԫ
			tranApp.setResultCert(conNum);// �����˵ĺ���
			tranApp.setResultName(bidderPerson.getBidderName());// ����������
			logger.info("---------��ʼ�������ʵ��---------");
			tdscBlockInfoService.updateTdscBlockTranApp(tranApp);
			logger.info("---------tranApp����ɹ�-----------");
			tdscBlockInfoService.updateTdscBlockInfo(blockInfo);
			logger.info("---------blockInfo����ɹ�-----------");
			tdscBlockInfoService.updateTdscListingInfo(listingInfo);
			logger.info("---------listingInfo����ɹ�-----------");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		//return mapping.findForward("bidderRollInfo");
		//�ص��б�ҳ��
//		String blockName = request.getParameter("blockName");
//		String blockNoticeNo = request.getParameter("blockNoticeNo");
//		TdscBaseQueryCondition condition1 = new TdscBaseQueryCondition();
//		condition1.setTranResult("05");//��ҡ�ŵĵؿ�
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
