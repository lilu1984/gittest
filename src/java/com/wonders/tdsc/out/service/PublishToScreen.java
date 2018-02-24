package com.wonders.tdsc.out.service;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.jdbc.dbmanager.ConnectionManager;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.blockwork.service.TdscFileSaleInfoService;
import com.wonders.tdsc.bo.BaseInfo;
import com.wonders.tdsc.bo.BlockBaseInfo;
import com.wonders.tdsc.bo.BlockTransInfo;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TransInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.common.util.FileUtils;
import com.wonders.tdsc.localtrade.service.TdscLocalTradeService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.thirdpart.castor.util.CastorFactory;
import com.wonders.util.PropertiesUtil;

public class PublishToScreen {

	// private Connection conn = ConnectionManager.getInstance().getConnection();
	private static final String	dapingPath	= PropertiesUtil.getInstance().getProperty("daping_path");

	/**
	 * 1¥��ǰ���Ʊ���,���ƽ���ǰʱ�������ļ������ƽ�����������
	 */
	public void publishDqZgBj() {
		try {
			CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext().getBean("commonQueryService");
			TdscLocalTradeService tdscLocalTradeService = (TdscLocalTradeService) AppContextUtil.getInstance().getAppContext().getBean("tdscLocalTradeService");

			List planIdList = (List) getTdscBlockPlanTableListForCurrPrice();
			// һ��planId��Ӧһ�����Ĺҳ��û(��һ������)������һ����߱��۵�xml�ļ�
			// ������ƽ�����Ҫ�����ɸ��ļ�

			if (planIdList != null && planIdList.size() > 0) {

				for (int i = 0; i < planIdList.size(); i++) {
					String fileName = "";//���ɵ��ļ����ƣ�һ��plan��Ӧһ���ļ����ƣ����ɹ���ΪX_yyyymmddhhmmss0000000000
					String tradeNum = "0000000";//����������Ĺұ�ŵ���ݺ���ˮ�ţ������ñ�������ļ�������ʼĬ��Ϊ0000000(4λ��ݼ�3λ��ˮ��)
					String ydxz = "000";//�õ�����,�ֹ�ҵ�;�Ӫ������,����Ϊ3λ����
					String planId = (String) planIdList.get(i);
					TdscBlockPlanTable planInfo = tdscLocalTradeService.getTdscBlockPlanTable(planId);
					
					if(planInfo != null && planInfo.getTradeNum() !=null && planInfo.getTradeNum().split("-").length == 3){//ȡ���Ĺұ�������ݺ���ˮ��
						tradeNum = planInfo.getTradeNum().split("-")[1] + planInfo.getTradeNum().split("-")[2];
					}
							
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					condition.setPlanId(planId);
					condition.setStatus("01");
					condition.setOrderKey("xuHao");

					// һ�����Ĺһһ��planId��һ��planId��Ӧ���tdscBlockAppView����tdscBlockAppVie wList
					List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListByPlanId(condition);

					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
						TdscBlockAppView appTmp = (TdscBlockAppView) tdscBlockAppViewList.get(0);

						StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
						sb.append("<root>");
						sb.append("<blockNoticeNo>").append(appTmp.getNoitceNo()).append("</blockNoticeNo>");

						for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
							TdscBlockAppView app = (TdscBlockAppView) tdscBlockAppViewList.get(m);
							//��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
							if ("00".equals(app.getStatus()) || "04".equals(app.getTranResult())) {//�����δ���׵ĵؿ�����ǽ�����ֹ�ĵؿ飬���������沿��
								continue;
							}
							
							if(StringUtils.isNotBlank(app.getBlockQuality())){
								ydxz = app.getBlockQuality();
							}
							
							if ("3107".equals(app.getTransferMode())) {
								fileName = "Z_" + DateUtil.date2String(app.getOpeningDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
							} else if ("3103".equals(app.getTransferMode())) {
								fileName = "P_" + DateUtil.date2String(app.getActionDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
							} else if ("3104".equals(app.getTransferMode())) {
								fileName = "G_" + DateUtil.date2String(app.getSceBidDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
							} else {
								fileName = "0_19000101000000" + ydxz + tradeNum;
							}
							String currRound = "";
							String currPrice = "";
							String listDate = "";
							TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(app.getAppId());
							if (listingInfo != null && listingInfo.getCurrRound() != null) {
								currRound = listingInfo.getCurrRound() + ""; // �����ִ�
							} else {
								currRound = "";
							}
							if (listingInfo != null && listingInfo.getCurrPrice() != null) {
								currPrice = listingInfo.getCurrPrice() + ""; // ��ǰ��߱���
							} else {
								currPrice = app.getInitPrice() + "";
							}
							if (listingInfo != null && listingInfo.getListDate() != null) {
								listDate = DateUtil.date2String(listingInfo.getListDate(), "yyyy-MM-dd HH:mm:ss") + ""; //
							} else {
								if("1".equals(app.getIsPurposeBlock())){
									listDate = DateUtil.date2String(planInfo.getListStartDate(), "yyyy-MM-dd HH:mm:ss") + ""; 
								}else{
									listDate = "";									
								}
							}

							String blockName = app.getBlockName() + "";
							// System.out.println(blockName);
							int s = blockName.lastIndexOf("�ؿ�");
							if (s != -1)
								blockName = blockName.substring(0, s);
							sb.append("<context>");
							sb.append("<seqNo>").append(m + 1).append("</seqNo>");
							// �ؿ���
							sb.append("<blockNo>").append(app.getBlockNoticeNo()).append("</blockNo>");
							// �ؿ�����
							sb.append("<blockName>").append(blockName).append("</blockName>");
							// ��ǰ�۸�
							sb.append("<currPrice>").append(currPrice).append("</currPrice>");
							// ����ʱ��
							sb.append("<listDate>").append(listDate).append("</listDate>");
							// �ִ�
							sb.append("<currRound>").append(currRound).append("</currRound>");

							sb.append("</context>");

							// /////////////////////////�ַ������ݱ��浽�ļ�///////////////////////////////

						}

						sb.append("</root>");

						String filePath = dapingPath + "dqbj\\";
						fileName += ".xml";
						FileUtils.saveStringToFile(sb.toString(), filePath, fileName);
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * 2¥�������ұ���߱���, ����ÿ�������Ӧ�ؿ飬��߱��۵�xml �ļ������� /zgbj Ŀ¼������ʱ�����������һ����߱���
	 */
	public void publishZgbj() {
		try {
			CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext().getBean("commonQueryService");
			TdscLocalTradeService tdscLocalTradeService = (TdscLocalTradeService) AppContextUtil.getInstance().getAppContext().getBean("tdscLocalTradeService");
			TdscBidderAppService tdscBidderAppService = (TdscBidderAppService) AppContextUtil.getInstance().getAppContext().getBean("tdscBidderAppService");

			List planIdList = (List) getTdscBlockPlanTableListForMaxPrice();
			// һ��planId��Ӧһ�����Ĺҳ��û(��һ������)������һ����߱��۵�xml�ļ�
			// TODO �������ʱ������н��棬����ʱ��ڵ�����xml�ļ���ɾ��ԭ�е��ļ�
			// ���ƽ�ֹ���ֳ�����ǰ���ɴ��ļ�
			// ���� Plan_id�õ�plan

			if (planIdList != null && planIdList.size() > 0) {

				for (int i = 0; i < planIdList.size(); i++) {
					String fileName = "";
					String tradeNum = "0000000";//����������Ĺұ�ŵ���ݺ���ˮ�ţ������ñ�������ļ�������ʼĬ��Ϊ0000000(4λ��ݼ�3λ��ˮ��)
					String ydxz = "000";//�õ�����,�ֹ�ҵ�;�Ӫ������,����Ϊ3λ����
					String planId = (String) planIdList.get(i);
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					//condition.setStatus("01");
					condition.setOrderKey("xuHao");
					condition.setPlanId(planId);
					
					TdscBlockPlanTable planInfo = tdscLocalTradeService.getTdscBlockPlanTable(planId);
					
					if(planInfo != null && planInfo.getTradeNum() !=null && planInfo.getTradeNum().split("-").length == 3){//ȡ���Ĺұ�������ݺ���ˮ��
						tradeNum = planInfo.getTradeNum().split("-")[1] + planInfo.getTradeNum().split("-")[2];
					}

					// һ�����Ĺһһ��planId��һ��planId��Ӧ���tdscBlockAppView����tdscBlockAppViewList
					List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListByPlanId(condition);

					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
						TdscBlockAppView appTmp = (TdscBlockAppView) tdscBlockAppViewList.get(0);
						
						//������ù�����ֻ����һ��أ�ͬʱ�����ǽ�����ֹ�ؿ飬��Ӧ���������ɱ�xml����������ˣ��򲻿���Һ��������Ļ����
						StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
						sb.append("<root>");
						sb.append("<blockNoticeNo>").append(appTmp.getNoitceNo()).append("</blockNoticeNo>");

						for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
							TdscBlockAppView app = (TdscBlockAppView) tdscBlockAppViewList.get(m);
							
							//��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
							if ("00".equals(app.getStatus()) || "04".equals(app.getTranResult())) {
								continue;
							}
							
							if(StringUtils.isNotBlank(app.getBlockQuality())){
								ydxz = app.getBlockQuality();
							}
							
							if ("3107".equals(app.getTransferMode())) {
								fileName = "Z_" + DateUtil.date2String(app.getOpeningDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
							} else if ("3103".equals(app.getTransferMode())) {
								fileName = "P_" + DateUtil.date2String(app.getActionDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
							} else if ("3104".equals(app.getTransferMode())) {
								fileName = "G_" + DateUtil.date2String(app.getSceBidDate(), "yyyyMMddHHmmss") + ydxz + tradeNum;
							} else {
								fileName = "0_19000101000000" + ydxz + tradeNum;
							}
							String currPrice = "";
							// ��ȡ��ǰ��߱���
							TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(app.getAppId());
							if (listingInfo != null && listingInfo.getCurrPrice() != null) {
								currPrice = listingInfo.getCurrPrice() + ""; // ��ǰ��߱���
							} else {
								currPrice = app.getInitPrice() + "";
							}

							sb.append("<context>");
							sb.append("<seqNo>").append(m + 1).append("</seqNo>");
							sb.append("<blockName>").append(app.getBlockName()).append("</blockName>");

							// �������
							sb.append("<blockArea>").append(app.getTotalLandArea()).append("</blockArea>");
							sb.append("<initPrice>").append(app.getInitPrice()).append("</initPrice>");
							// ��߱���
							sb.append("<maxPrice>").append(currPrice).append("</maxPrice>");

							sb.append("</context>");

							// /////////////////////////�ַ������ݱ��浽�ļ�///////////////////////////////

						}

						sb.append("</root>");

						String filePath = dapingPath + "zgbj\\";
						fileName += ".xml";
						FileUtils.saveStringToFile(sb.toString(), filePath, fileName);
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 2¥�������ұ���߱���, ����ÿ�������Ӧ�ؿ飬��߱��۵�xml �ļ������� /zgbj Ŀ¼������ʱ�����������һ����߱���
	 * 
	 * @return
	 */
	private List getTdscBlockPlanTableListForMaxPrice() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		// ��ѯ����planIdList
		Statement st = null;
		ResultSet rs = null;
		List retList = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer("");
			// ��ѯ���Ĺһ��planId����ѯ�����ǣ����ù����Ѿ������˵�,ϵͳ��ǰʱ����ڵ��ڹ��ƽ�ֹʱ�䣬��С�ڵ����ֳ�����ʱ�䡣����planIdList��
			sql.append("select distinct a.plan_id as planId from tdsc_block_plan_table a,tdsc_block_tran_app b,tdsc_notice_app c ");
			sql.append("where sysdate >= a.list_end_date ");
			sql.append("and sysdate <= a.sce_bid_date ");
			sql.append("and a.plan_id = b.plan_id ");
			sql.append("and b.notice_id = c.notice_id ");
			sql.append("and c.if_released = '1' ");

			st = conn.createStatement();
			rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				String tmp = rs.getString("planId");
				retList.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retList;
	}

	// ��ǰ����ʵʱ����
	private List getTdscBlockPlanTableListForCurrPrice() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		// ��ѯ����planIdList
		Statement st = null;
		ResultSet rs = null;
		List retList = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer("");
			// ��ѯ���Ĺһ��planId����ѯ�����ǣ����ù����Ѿ������˵ģ�ϵͳ��ǰʱ��С���ڹ��ƽ�ֹʱ�䣬�Ҵ��ڵ��ڹ��ƿ�ʼʱ�䡣
			// ���ƽ������������ļ�
			// ����planIdList��

			sql.append("select distinct a.plan_id as planId from tdsc_block_plan_table a,tdsc_block_tran_app b,tdsc_notice_app c ");
			sql.append("where 1=1 ");
			sql.append("and a.list_end_date >= sysdate ");
			sql.append("and sysdate >= a.list_start_date ");
			sql.append("and a.plan_id = b.plan_id ");
			sql.append("and b.notice_id = c.notice_id ");
			sql.append("and c.if_released = '1'");

			st = conn.createStatement();
			rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				String tmp = rs.getString("planId");
				retList.add(tmp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retList;
	}

	
	//�������ɴ����ļ�������daping/cjqk�ļ����µ�xml�ļ�,cjqklistû��ʹ����
	public void publishIt() {

		try {
			String blockBaseInfoMapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "blockBaseInfoMap.xml";
			String transInfoMapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "transInfoMap.xml";

			TdscBidderAppService tdscBidderAppService = (TdscBidderAppService) AppContextUtil.getInstance().getAppContext().getBean("tdscBidderAppService");
			TdscBlockInfoService tdscBlockInfoService = (TdscBlockInfoService) AppContextUtil.getInstance().getAppContext().getBean("tdscBlockInfoService");
			CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext().getBean("commonQueryService");
			TdscLocalTradeService tdscLocalTradeService = (TdscLocalTradeService) AppContextUtil.getInstance().getAppContext().getBean("tdscLocalTradeService");
			TdscFileSaleInfoService tdscFileSaleInfoService = (TdscFileSaleInfoService) AppContextUtil.getInstance().getAppContext().getBean("tdscFileSaleInfoService");

			List planIdList = (List) getTdscBlockPlanTableList();

			// һ��planId��Ӧһ�����Ĺҳ��û������һ��xml�ļ�
			if (planIdList != null && planIdList.size() > 0) {
				for (int i = 0; i < planIdList.size(); i++) {
					//��ȡ�ļ���Чʱ�䣬��ȷ���룬�������xml�ļ������ƣ�ϵͳ��ǰ���ڴ�����Чʱ���е�����ʱ�����ļ��ƶ�������Ŀ¼�£�
					String endTime = "";
					
					String tradeNum = "0000000";//����������Ĺұ�ŵ���ݺ���ˮ�ţ������ñ�������ļ�������ʼĬ��Ϊ0000000(4λ��ݼ�3λ��ˮ��)
					String ydxz = "000";//�õ�����,�ֹ�ҵ�;�Ӫ������,����Ϊ3λ����
					String blockBaseInfoClassName = ""; // blockBaseInfoMap.xml��Ӧbean�����ɵ�xml�ļ�����
					String blockBaseInfoXmlSP = "";// Ŀ��xml���ļ�·�����ļ���
					String cjqkPath = "";// cjqkĿ¼��·��

					String transInfoClassName = ""; // transInfoMap.xml��Ӧbean�����ɵ�xml�ļ�����
					String transInfoXmlSP = "";// Ŀ��xml���ļ�·�����ļ���
					String cjqkListPath = "";// cjqklistĿ¼��·��

					List tdscBlockAppViewList = new ArrayList();

					List baseInfoList = new ArrayList();
					List transInfoList = new ArrayList();

					String title = "";// blockBaseInfo��NoticeNo���ԣ�������ű��⣬������ɹ���Ϊ�����ù����+�����н����õ�ʹ��Ȩ��+���÷�ʽ+�����������

					String planId = (String) planIdList.get(i);
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					condition.setStatus("01");
					condition.setPlanId(planId);
					condition.setOrderKey("xuHao");
					
					TdscBlockPlanTable planInfo = tdscLocalTradeService.getTdscBlockPlanTable(planId);
					
					if(planInfo != null && planInfo.getTradeNum() !=null && planInfo.getTradeNum().split("-").length == 3){//ȡ���Ĺұ�������ݺ���ˮ��
						tradeNum = planInfo.getTradeNum().split("-")[1] + planInfo.getTradeNum().split("-")[2];
					}

					BlockBaseInfo blockBaseInfo = new BlockBaseInfo();
					BlockTransInfo blockTransInfo = new BlockTransInfo();

					int totalICountPerson = 0;// һ�����Ĺһ���ܹ����ļ�����
					int totalCountPerson = 0;// һ�����Ĺһ���ܾ�������

					// һ�����Ĺһһ��planId��һ��planId��Ӧ���tdscBlockAppView����tdscBlockAppViewList
					tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListByPlanId(condition);

					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
						for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
							TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(m);

							//��״̬��status��Ϊ�����л��߽��׽����ĵؿ��У����˵�������ֹ�ĵؿ�
							if ("00".equals(tdscBlockAppView.getStatus()) || "04".equals(tdscBlockAppView.getTranResult())) {//�����δ���׵ĵؿ�����ǽ�����ֹ�ĵؿ飬���������沿��
								continue;
							}
							
							// һ��planId��Ӧһ��blockBaseInfo��һ��blockBaseInfo����һ�����ù����(һ������)�����baseInfo,��baseInfoList��
							// blockBaseInfo.setNoticeNo(tdscBlockAppView.getNoitceNo());//���ù���ţ�һ��blockBaseInfo����һ�����ù����
							
							if(StringUtils.isNotBlank(tdscBlockAppView.getBlockQuality())){
								ydxz = tdscBlockAppView.getBlockQuality();
							}
							
							title = tdscBlockAppView.getNoitceNo();

							BaseInfo baseInfo = new BaseInfo();
							if (tdscBlockAppView.getBlockNoticeNo() != null && !"".equals(tdscBlockAppView.getBlockNoticeNo().trim())) {
								baseInfo.setBlockNoticeNo(tdscBlockAppView.getBlockNoticeNo());// �ؿ���
							} else {
								baseInfo.setBlockNoticeNo("");// �ؿ���
							}

							if (tdscBlockAppView.getBlockName() != null && !"".equals(tdscBlockAppView.getBlockName().trim())) {
								baseInfo.setLandName(tdscBlockAppView.getBlockName());// �ؿ�����
							} else {
								baseInfo.setLandName("");// �ؿ�����
							}

							if (tdscBlockAppView.getLandLocation() != null && !"".equals(tdscBlockAppView.getLandLocation().trim())) {
								baseInfo.setLandLocation(tdscBlockAppView.getLandLocation());// ����λ��
							} else {
								baseInfo.setLandLocation("");// ����λ��
							}

							if (tdscBlockAppView.getSellYear() != null && !"".equals(tdscBlockAppView.getSellYear().trim())) {
								baseInfo.setSellYear(tdscBlockAppView.getSellYear());// ��������
							} else {
								baseInfo.setSellYear("");// ��������
							}

							if (tdscBlockAppView.getTotalLandArea() != null && !"".equals(tdscBlockAppView.getTotalLandArea().toString())
									&& tdscBlockAppView.getTotalLandArea().intValue() > 0) {
								baseInfo.setTotalLandArea(tdscBlockAppView.getTotalLandArea() + "ƽ����");// �������
							} else {
								baseInfo.setTotalLandArea("");// �������
							}

							if (tdscBlockAppView.getInitPrice() != null && !"".equals(tdscBlockAppView.getInitPrice().toString()) && tdscBlockAppView.getInitPrice().intValue() > 0) {
								baseInfo.setInitPrice(tdscBlockAppView.getInitPrice() + "��Ԫ");// ��ʼ��
							} else {
								baseInfo.setInitPrice("");// ��ʼ��
							}
							if (tdscBlockAppView.getMarginAmount() != null && !"".equals(tdscBlockAppView.getMarginAmount()) && tdscBlockAppView.getMarginAmount().intValue() > 0) {
								baseInfo.setMarginAmount(tdscBlockAppView.getMarginAmount() + "��Ԫ");// ��֤��
							} else {
								baseInfo.setMarginAmount("");// ��֤��
							}
							// ���÷�ʽ
							if ("3107".equals(tdscBlockAppView.getTransferMode())) {
								baseInfo.setTransferMode("�б�");
								title += "���н����õ�ʹ��Ȩ�б�������";
								blockBaseInfoClassName = "Z_";
								transInfoClassName = "Z_";
								endTime = DateUtil.date2String(tdscBlockAppView.getOpeningDate(), "yyyyMMddHHmmss");
							} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
								baseInfo.setTransferMode("����");
								title += "���н����õ�ʹ��Ȩ�����������";
								blockBaseInfoClassName = "P_";
								transInfoClassName = "P_";
								endTime = DateUtil.date2String(tdscBlockAppView.getActionDate(), "yyyyMMddHHmmss");
							} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
								if ("1".equals(tdscBlockAppView.getIsPurposeBlock())) {
									baseInfo.setTransferMode("���������");
								} else {
									baseInfo.setTransferMode("���������");
								}
								title += "���н����õ�ʹ��Ȩ���Ƴ������";
								blockBaseInfoClassName = "G_";
								transInfoClassName = "G_";
								endTime = DateUtil.date2String(tdscBlockAppView.getSceBidDate(), "yyyyMMddHHmmss");
							} else {
								baseInfo.setTransferMode("");
								title = "";
								blockBaseInfoClassName = "0_";
								transInfoClassName = "0_";
								endTime = "19000101000000";// 1900��01��01��00ʱ00��00��
							}
							List blockPartlist = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());
							if (blockPartlist != null && blockPartlist.size() > 0) {
								// Ŀǰ����һ���ؿ�ֻ��һ��blockPart�����������blockPartlist.size()Ϊ1
								for (int n = 0; n < blockPartlist.size(); n++) {
									TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartlist.get(n);
									if (tdscBlockPart != null && tdscBlockPart.getLandUseType() != null && !"".equals(tdscBlockPart.getLandUseType().trim())) {
										baseInfo.setLandUseType(tdscBlockPart.getLandUseType());// ������;
									} else {
										baseInfo.setLandUseType("");// ������;
									}
								}
							}
							blockBaseInfo.setNoticeNo(title);
							baseInfoList.add(baseInfo);

							TransInfo transInfo = new TransInfo();
							if (tdscBlockAppView.getBlockNoticeNo() != null && !"".equals(tdscBlockAppView.getBlockNoticeNo().trim())) {
								transInfo.setBlockNoticeNo(tdscBlockAppView.getBlockNoticeNo());// �ؿ���
							} else {
								transInfo.setBlockNoticeNo("");// �ؿ���
							}
							// ���÷�ʽ
							if ("3107".equals(tdscBlockAppView.getTransferMode())) {
								transInfo.setTransferMode("�б�");
							} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
								transInfo.setTransferMode("����");
							} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
								if ("1".equals(tdscBlockAppView.getIsPurposeBlock())) {
									transInfo.setTransferMode("���������");
								} else {
									transInfo.setTransferMode("���������");
								}
							} else {
								transInfo.setTransferMode("");
							}

							TdscListingInfo tdscListingInfo = (TdscListingInfo) tdscLocalTradeService.getTdscListingInfoByAppId(tdscBlockAppView.getAppId());

							if (tdscListingInfo != null && tdscListingInfo.getCurrPrice() != null) {
								transInfo.setCurrPrice(tdscListingInfo.getCurrPrice().toString());// ��ǰ����
							} else {
								transInfo.setCurrPrice("");// ��ǰ����
							}

							if (tdscListingInfo != null && tdscListingInfo.getListDate() != null) {
								transInfo.setListDate(DateUtil.date2String(tdscListingInfo.getListDate(), "yyyy-MM-dd HH:mm"));// ����ʱ��
							} else {
								transInfo.setListDate("");// ����ʱ��
							}

							List personList = tdscFileSaleInfoService.countSalePerson(tdscBlockAppView.getNoticeId(), tdscBlockAppView.getAppId());
							if (personList != null) {
								transInfo.setICountPerson(personList.size() + "");// �����ļ�����
								totalICountPerson += personList.size();// һ�����Ĺһ���ܹ����ļ�����
							} else {
								transInfo.setICountPerson("0");// �����ļ�����
								totalICountPerson += 0;// һ�����Ĺһ���ܹ����ļ�����
							}

							List countList = (List) tdscBidderAppService.queryBidderAppListLikeAppId(tdscBlockAppView.getAppId());
							if (countList != null) {
								transInfo.setCountPerson(countList.size() + "");// ��������
								totalCountPerson += countList.size();// һ�����Ĺһ���ܾ�������
							} else {
								transInfo.setCountPerson("0");// ��������
								totalCountPerson += 0;// һ�����Ĺһ���ܾ�������
							}

							blockTransInfo.setNoticeNo(title);
							transInfoList.add(transInfo);

							title = "";// һ��planIdֻ��Ҫһ��title��ÿ����һ��planId��title�󣬽�title���Ϊ����
						}
						blockBaseInfo.setList(baseInfoList);// һ��blockBaseInfo����һ��baseInfoList

						blockTransInfo.setTotalICountPerson(totalICountPerson + "");
						blockTransInfo.setTotalCountPerson(totalCountPerson + "");
						blockTransInfo.setList(transInfoList);// һ��blockTransInfo����һ��transInfoList
					}

					blockBaseInfoClassName += endTime;
					cjqkPath = dapingPath + "cjqk\\";
					blockBaseInfoXmlSP = cjqkPath + blockBaseInfoClassName + ydxz + tradeNum + ".xml";// ���blockBaseInfoMap.xml��Ӧbean�����ɵ�xml�ļ�·�����ļ���

					transInfoClassName += endTime;
					cjqkListPath = dapingPath + "cjqklist\\";
					transInfoXmlSP = cjqkListPath + transInfoClassName + ydxz + tradeNum + ".xml";// ���transInfoMap.xml��Ӧbean�����ɵ�xml�ļ�·�����ļ���

					File dirCjqk = new File(cjqkPath);
					if (!dirCjqk.exists()) {
						dirCjqk.mkdir();
					}

					File dirCjqkList = new File(cjqkListPath);
					if (!dirCjqkList.exists()) {
						dirCjqkList.mkdir();
					}

					CastorFactory.marshalBeanOverWriteXml(blockBaseInfo, blockBaseInfoMapPath, blockBaseInfoXmlSP);
					// CastorFactory.marshalBeanOverWriteXml(blockTransInfo, transInfoMapPath, transInfoXmlSP);
				}
			}
		} catch (Exception e) {
			// logger.error(e);
		}
	}

	private List getTdscBlockPlanTableList() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		// ��ѯ����planIdList
		Statement st = null;
		ResultSet rs = null;
		List retList = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer("");
			// ��ѯ���Ĺһ��planId����ѯ�����ǣ�ϵͳ��ǰʱ���������������͹��ƽ�ֹʱ��֮�䣬�ҳ��ù����Ѿ������˵ġ�����planIdList��
			sql.append("select distinct a.plan_id as planId from tdsc_block_plan_table a,tdsc_block_tran_app b,tdsc_notice_app c ");
			sql.append("where sysdate between a.acc_app_stat_date and a.list_end_date ");
			sql.append("and a.plan_id = b.plan_id ");
			sql.append("and b.notice_id = c.notice_id ");
			sql.append("and c.if_released = '1'");

			st = conn.createStatement();
			rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				String tmp = rs.getString("planId");
				retList.add(tmp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(st != null){
					st.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retList;
	}
}
