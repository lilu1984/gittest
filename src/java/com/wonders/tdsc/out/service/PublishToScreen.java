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
	 * 1楼当前挂牌报价,挂牌结束前时刻生成文件；挂牌结束后不再生成
	 */
	public void publishDqZgBj() {
		try {
			CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext().getBean("commonQueryService");
			TdscLocalTradeService tdscLocalTradeService = (TdscLocalTradeService) AppContextUtil.getInstance().getAppContext().getBean("tdscLocalTradeService");

			List planIdList = (List) getTdscBlockPlanTableListForCurrPrice();
			// 一个planId对应一个招拍挂出让活动(即一个公告)，生成一个最高报价的xml文件
			// 如果挂牌结束不要再生成该文件

			if (planIdList != null && planIdList.size() > 0) {

				for (int i = 0; i < planIdList.size(); i++) {
					String fileName = "";//生成的文件名称，一个plan对应一个文件名称，生成规则为X_yyyymmddhhmmss0000000000
					String tradeNum = "0000000";//用来存放招拍挂编号的年份和流水号，并将该变量组成文件名，初始默认为0000000(4位年份加3位流水号)
					String ydxz = "000";//用地性质,分工业和经营性两种,代码为3位数字
					String planId = (String) planIdList.get(i);
					TdscBlockPlanTable planInfo = tdscLocalTradeService.getTdscBlockPlanTable(planId);
					
					if(planInfo != null && planInfo.getTradeNum() !=null && planInfo.getTradeNum().split("-").length == 3){//取招拍挂编号里的年份和流水号
						tradeNum = planInfo.getTradeNum().split("-")[1] + planInfo.getTradeNum().split("-")[2];
					}
							
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					condition.setPlanId(planId);
					condition.setStatus("01");
					condition.setOrderKey("xuHao");

					// 一个招拍挂活动一个planId，一个planId对应多个tdscBlockAppView，即tdscBlockAppVie wList
					List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListByPlanId(condition);

					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
						TdscBlockAppView appTmp = (TdscBlockAppView) tdscBlockAppViewList.get(0);

						StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
						sb.append("<root>");
						sb.append("<blockNoticeNo>").append(appTmp.getNoitceNo()).append("</blockNoticeNo>");

						for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
							TdscBlockAppView app = (TdscBlockAppView) tdscBlockAppViewList.get(m);
							//在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
							if ("00".equals(app.getStatus()) || "04".equals(app.getTranResult())) {//如果是未交易的地块或者是交易终止的地块，则跳过後面部份
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
								currRound = listingInfo.getCurrRound() + ""; // 报价轮次
							} else {
								currRound = "";
							}
							if (listingInfo != null && listingInfo.getCurrPrice() != null) {
								currPrice = listingInfo.getCurrPrice() + ""; // 当前最高报价
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
							int s = blockName.lastIndexOf("地块");
							if (s != -1)
								blockName = blockName.substring(0, s);
							sb.append("<context>");
							sb.append("<seqNo>").append(m + 1).append("</seqNo>");
							// 地块编号
							sb.append("<blockNo>").append(app.getBlockNoticeNo()).append("</blockNo>");
							// 地块名称
							sb.append("<blockName>").append(blockName).append("</blockName>");
							// 当前价格
							sb.append("<currPrice>").append(currPrice).append("</currPrice>");
							// 报价时间
							sb.append("<listDate>").append(listDate).append("</listDate>");
							// 轮次
							sb.append("<currRound>").append(currRound).append("</currRound>");

							sb.append("</context>");

							// /////////////////////////字符串内容保存到文件///////////////////////////////

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
	 * 2楼大厅最右边最高报价, 生成每个公告对应地块，最高报价的xml 文件，放入 /zgbj 目录。挂牌时间结束后即生成一次最高报价
	 */
	public void publishZgbj() {
		try {
			CommonQueryService commonQueryService = (CommonQueryService) AppContextUtil.getInstance().getAppContext().getBean("commonQueryService");
			TdscLocalTradeService tdscLocalTradeService = (TdscLocalTradeService) AppContextUtil.getInstance().getAppContext().getBean("tdscLocalTradeService");
			TdscBidderAppService tdscBidderAppService = (TdscBidderAppService) AppContextUtil.getInstance().getAppContext().getBean("tdscBidderAppService");

			List planIdList = (List) getTdscBlockPlanTableListForMaxPrice();
			// 一个planId对应一个招拍挂出让活动(即一个公告)，生成一个最高报价的xml文件
			// TODO 多个公告时间可能有交叉，根据时间节点生成xml文件，删除原有的文件
			// 挂牌截止后现场竞价前生成此文件
			// 根据 Plan_id得到plan

			if (planIdList != null && planIdList.size() > 0) {

				for (int i = 0; i < planIdList.size(); i++) {
					String fileName = "";
					String tradeNum = "0000000";//用来存放招拍挂编号的年份和流水号，并将该变量组成文件名，初始默认为0000000(4位年份加3位流水号)
					String ydxz = "000";//用地性质,分工业和经营性两种,代码为3位数字
					String planId = (String) planIdList.get(i);
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					//condition.setStatus("01");
					condition.setOrderKey("xuHao");
					condition.setPlanId(planId);
					
					TdscBlockPlanTable planInfo = tdscLocalTradeService.getTdscBlockPlanTable(planId);
					
					if(planInfo != null && planInfo.getTradeNum() !=null && planInfo.getTradeNum().split("-").length == 3){//取招拍挂编号里的年份和流水号
						tradeNum = planInfo.getTradeNum().split("-")[1] + planInfo.getTradeNum().split("-")[2];
					}

					// 一个招拍挂活动一个planId，一个planId对应多个tdscBlockAppView，即tdscBlockAppViewList
					List tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListByPlanId(condition);

					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
						TdscBlockAppView appTmp = (TdscBlockAppView) tdscBlockAppViewList.get(0);
						
						//如果出让公告中只出让一块地，同时这块地是交易终止地块，则应该无需生成本xml，如果生成了，则不开启液晶电视屏幕即是
						StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
						sb.append("<root>");
						sb.append("<blockNoticeNo>").append(appTmp.getNoitceNo()).append("</blockNoticeNo>");

						for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
							TdscBlockAppView app = (TdscBlockAppView) tdscBlockAppViewList.get(m);
							
							//在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
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
							// 获取当前最高报价
							TdscListingInfo listingInfo = tdscLocalTradeService.getTdscListingInfoByAppId(app.getAppId());
							if (listingInfo != null && listingInfo.getCurrPrice() != null) {
								currPrice = listingInfo.getCurrPrice() + ""; // 当前最高报价
							} else {
								currPrice = app.getInitPrice() + "";
							}

							sb.append("<context>");
							sb.append("<seqNo>").append(m + 1).append("</seqNo>");
							sb.append("<blockName>").append(app.getBlockName()).append("</blockName>");

							// 土地面积
							sb.append("<blockArea>").append(app.getTotalLandArea()).append("</blockArea>");
							sb.append("<initPrice>").append(app.getInitPrice()).append("</initPrice>");
							// 最高报价
							sb.append("<maxPrice>").append(currPrice).append("</maxPrice>");

							sb.append("</context>");

							// /////////////////////////字符串内容保存到文件///////////////////////////////

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
	 * 2楼大厅最右边最高报价, 生成每个公告对应地块，最高报价的xml 文件，放入 /zgbj 目录。挂牌时间结束后即生成一次最高报价
	 * 
	 * @return
	 */
	private List getTdscBlockPlanTableListForMaxPrice() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		// 查询返回planIdList
		Statement st = null;
		ResultSet rs = null;
		List retList = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer("");
			// 查询招拍挂活动的planId，查询条件是：出让公告已经发布了的,系统当前时间大于等于挂牌截止时间，且小于等于现场竞价时间。返回planIdList。
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

	// 当前挂牌实时报价
	private List getTdscBlockPlanTableListForCurrPrice() {
		Connection conn = ConnectionManager.getInstance().getConnection();
		// 查询返回planIdList
		Statement st = null;
		ResultSet rs = null;
		List retList = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer("");
			// 查询招拍挂活动的planId，查询条件是：出让公告已经发布了的，系统当前时间小等于挂牌截止时间，且大于等于挂牌开始时间。
			// 挂牌结束后不再生成文件
			// 返回planIdList。

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

	
	//用来生成大屏文件，生成daping/cjqk文件夹下的xml文件,cjqklist没有使用了
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

			// 一个planId对应一个招拍挂出让活动，生成一个xml文件
			if (planIdList != null && planIdList.size() > 0) {
				for (int i = 0; i < planIdList.size(); i++) {
					//获取文件有效时间，精确到秒，用来组成xml文件的名称（系统当前日期大于有效时间中的日期时，将文件移动到备份目录下）
					String endTime = "";
					
					String tradeNum = "0000000";//用来存放招拍挂编号的年份和流水号，并将该变量组成文件名，初始默认为0000000(4位年份加3位流水号)
					String ydxz = "000";//用地性质,分工业和经营性两种,代码为3位数字
					String blockBaseInfoClassName = ""; // blockBaseInfoMap.xml对应bean所生成的xml文件名称
					String blockBaseInfoXmlSP = "";// 目标xml的文件路径及文件名
					String cjqkPath = "";// cjqk目录的路径

					String transInfoClassName = ""; // transInfoMap.xml对应bean所生成的xml文件名称
					String transInfoXmlSP = "";// 目标xml的文件路径及文件名
					String cjqkListPath = "";// cjqklist目录的路径

					List tdscBlockAppViewList = new ArrayList();

					List baseInfoList = new ArrayList();
					List transInfoList = new ArrayList();

					String title = "";// blockBaseInfo的NoticeNo属性，用来存放标题，标题组成规则为：出让公告号+“国有建设用地使用权”+出让方式+“出让情况”

					String planId = (String) planIdList.get(i);
					TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
					condition.setStatus("01");
					condition.setPlanId(planId);
					condition.setOrderKey("xuHao");
					
					TdscBlockPlanTable planInfo = tdscLocalTradeService.getTdscBlockPlanTable(planId);
					
					if(planInfo != null && planInfo.getTradeNum() !=null && planInfo.getTradeNum().split("-").length == 3){//取招拍挂编号里的年份和流水号
						tradeNum = planInfo.getTradeNum().split("-")[1] + planInfo.getTradeNum().split("-")[2];
					}

					BlockBaseInfo blockBaseInfo = new BlockBaseInfo();
					BlockTransInfo blockTransInfo = new BlockTransInfo();

					int totalICountPerson = 0;// 一个招拍挂活动的总购买文件人数
					int totalCountPerson = 0;// 一个招拍挂活动的总竞买人数

					// 一个招拍挂活动一个planId，一个planId对应多个tdscBlockAppView，即tdscBlockAppViewList
					tdscBlockAppViewList = (List) commonQueryService.queryTdscBlockAppViewListByPlanId(condition);

					if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
						for (int m = 0; m < tdscBlockAppViewList.size(); m++) {
							TdscBlockAppView tdscBlockAppView = (TdscBlockAppView) tdscBlockAppViewList.get(m);

							//在状态（status）为交易中或者交易结束的地块中，过滤掉交易终止的地块
							if ("00".equals(tdscBlockAppView.getStatus()) || "04".equals(tdscBlockAppView.getTranResult())) {//如果是未交易的地块或者是交易终止的地块，则跳过後面部份
								continue;
							}
							
							// 一个planId对应一个blockBaseInfo，一个blockBaseInfo包含一个出让公告号(一个标题)，多个baseInfo,即baseInfoList。
							// blockBaseInfo.setNoticeNo(tdscBlockAppView.getNoitceNo());//出让公告号，一个blockBaseInfo包含一个出让公告号
							
							if(StringUtils.isNotBlank(tdscBlockAppView.getBlockQuality())){
								ydxz = tdscBlockAppView.getBlockQuality();
							}
							
							title = tdscBlockAppView.getNoitceNo();

							BaseInfo baseInfo = new BaseInfo();
							if (tdscBlockAppView.getBlockNoticeNo() != null && !"".equals(tdscBlockAppView.getBlockNoticeNo().trim())) {
								baseInfo.setBlockNoticeNo(tdscBlockAppView.getBlockNoticeNo());// 地块编号
							} else {
								baseInfo.setBlockNoticeNo("");// 地块编号
							}

							if (tdscBlockAppView.getBlockName() != null && !"".equals(tdscBlockAppView.getBlockName().trim())) {
								baseInfo.setLandName(tdscBlockAppView.getBlockName());// 地块名称
							} else {
								baseInfo.setLandName("");// 地块名称
							}

							if (tdscBlockAppView.getLandLocation() != null && !"".equals(tdscBlockAppView.getLandLocation().trim())) {
								baseInfo.setLandLocation(tdscBlockAppView.getLandLocation());// 土地位置
							} else {
								baseInfo.setLandLocation("");// 土地位置
							}

							if (tdscBlockAppView.getSellYear() != null && !"".equals(tdscBlockAppView.getSellYear().trim())) {
								baseInfo.setSellYear(tdscBlockAppView.getSellYear());// 出让年期
							} else {
								baseInfo.setSellYear("");// 出让年期
							}

							if (tdscBlockAppView.getTotalLandArea() != null && !"".equals(tdscBlockAppView.getTotalLandArea().toString())
									&& tdscBlockAppView.getTotalLandArea().intValue() > 0) {
								baseInfo.setTotalLandArea(tdscBlockAppView.getTotalLandArea() + "平方米");// 土地面积
							} else {
								baseInfo.setTotalLandArea("");// 土地面积
							}

							if (tdscBlockAppView.getInitPrice() != null && !"".equals(tdscBlockAppView.getInitPrice().toString()) && tdscBlockAppView.getInitPrice().intValue() > 0) {
								baseInfo.setInitPrice(tdscBlockAppView.getInitPrice() + "万元");// 起始价
							} else {
								baseInfo.setInitPrice("");// 起始价
							}
							if (tdscBlockAppView.getMarginAmount() != null && !"".equals(tdscBlockAppView.getMarginAmount()) && tdscBlockAppView.getMarginAmount().intValue() > 0) {
								baseInfo.setMarginAmount(tdscBlockAppView.getMarginAmount() + "万元");// 保证金
							} else {
								baseInfo.setMarginAmount("");// 保证金
							}
							// 出让方式
							if ("3107".equals(tdscBlockAppView.getTransferMode())) {
								baseInfo.setTransferMode("招标");
								title += "国有建设用地使用权招标出让情况";
								blockBaseInfoClassName = "Z_";
								transInfoClassName = "Z_";
								endTime = DateUtil.date2String(tdscBlockAppView.getOpeningDate(), "yyyyMMddHHmmss");
							} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
								baseInfo.setTransferMode("拍卖");
								title += "国有建设用地使用权拍卖出让情况";
								blockBaseInfoClassName = "P_";
								transInfoClassName = "P_";
								endTime = DateUtil.date2String(tdscBlockAppView.getActionDate(), "yyyyMMddHHmmss");
							} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
								if ("1".equals(tdscBlockAppView.getIsPurposeBlock())) {
									baseInfo.setTransferMode("有意向挂牌");
								} else {
									baseInfo.setTransferMode("无意向挂牌");
								}
								title += "国有建设用地使用权挂牌出让情况";
								blockBaseInfoClassName = "G_";
								transInfoClassName = "G_";
								endTime = DateUtil.date2String(tdscBlockAppView.getSceBidDate(), "yyyyMMddHHmmss");
							} else {
								baseInfo.setTransferMode("");
								title = "";
								blockBaseInfoClassName = "0_";
								transInfoClassName = "0_";
								endTime = "19000101000000";// 1900年01月01日00时00分00秒
							}
							List blockPartlist = tdscBlockInfoService.getTdscBlockPartList(tdscBlockAppView.getBlockId());
							if (blockPartlist != null && blockPartlist.size() > 0) {
								// 目前无锡一个地块只有一个blockPart，所以这里的blockPartlist.size()为1
								for (int n = 0; n < blockPartlist.size(); n++) {
									TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartlist.get(n);
									if (tdscBlockPart != null && tdscBlockPart.getLandUseType() != null && !"".equals(tdscBlockPart.getLandUseType().trim())) {
										baseInfo.setLandUseType(tdscBlockPart.getLandUseType());// 土地用途
									} else {
										baseInfo.setLandUseType("");// 土地用途
									}
								}
							}
							blockBaseInfo.setNoticeNo(title);
							baseInfoList.add(baseInfo);

							TransInfo transInfo = new TransInfo();
							if (tdscBlockAppView.getBlockNoticeNo() != null && !"".equals(tdscBlockAppView.getBlockNoticeNo().trim())) {
								transInfo.setBlockNoticeNo(tdscBlockAppView.getBlockNoticeNo());// 地块编号
							} else {
								transInfo.setBlockNoticeNo("");// 地块编号
							}
							// 出让方式
							if ("3107".equals(tdscBlockAppView.getTransferMode())) {
								transInfo.setTransferMode("招标");
							} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
								transInfo.setTransferMode("拍卖");
							} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
								if ("1".equals(tdscBlockAppView.getIsPurposeBlock())) {
									transInfo.setTransferMode("有意向挂牌");
								} else {
									transInfo.setTransferMode("无意向挂牌");
								}
							} else {
								transInfo.setTransferMode("");
							}

							TdscListingInfo tdscListingInfo = (TdscListingInfo) tdscLocalTradeService.getTdscListingInfoByAppId(tdscBlockAppView.getAppId());

							if (tdscListingInfo != null && tdscListingInfo.getCurrPrice() != null) {
								transInfo.setCurrPrice(tdscListingInfo.getCurrPrice().toString());// 当前报价
							} else {
								transInfo.setCurrPrice("");// 当前报价
							}

							if (tdscListingInfo != null && tdscListingInfo.getListDate() != null) {
								transInfo.setListDate(DateUtil.date2String(tdscListingInfo.getListDate(), "yyyy-MM-dd HH:mm"));// 报价时间
							} else {
								transInfo.setListDate("");// 报价时间
							}

							List personList = tdscFileSaleInfoService.countSalePerson(tdscBlockAppView.getNoticeId(), tdscBlockAppView.getAppId());
							if (personList != null) {
								transInfo.setICountPerson(personList.size() + "");// 购买文件人数
								totalICountPerson += personList.size();// 一个招拍挂活动的总购买文件人数
							} else {
								transInfo.setICountPerson("0");// 购买文件人数
								totalICountPerson += 0;// 一个招拍挂活动的总购买文件人数
							}

							List countList = (List) tdscBidderAppService.queryBidderAppListLikeAppId(tdscBlockAppView.getAppId());
							if (countList != null) {
								transInfo.setCountPerson(countList.size() + "");// 竞买人数
								totalCountPerson += countList.size();// 一个招拍挂活动的总竞买人数
							} else {
								transInfo.setCountPerson("0");// 竞买人数
								totalCountPerson += 0;// 一个招拍挂活动的总竞买人数
							}

							blockTransInfo.setNoticeNo(title);
							transInfoList.add(transInfo);

							title = "";// 一个planId只需要一个title，每设置一个planId的title后，将title清空为“”
						}
						blockBaseInfo.setList(baseInfoList);// 一个blockBaseInfo包含一个baseInfoList

						blockTransInfo.setTotalICountPerson(totalICountPerson + "");
						blockTransInfo.setTotalCountPerson(totalCountPerson + "");
						blockTransInfo.setList(transInfoList);// 一个blockTransInfo包含一个transInfoList
					}

					blockBaseInfoClassName += endTime;
					cjqkPath = dapingPath + "cjqk\\";
					blockBaseInfoXmlSP = cjqkPath + blockBaseInfoClassName + ydxz + tradeNum + ".xml";// 存放blockBaseInfoMap.xml对应bean所生成的xml文件路径及文件名

					transInfoClassName += endTime;
					cjqkListPath = dapingPath + "cjqklist\\";
					transInfoXmlSP = cjqkListPath + transInfoClassName + ydxz + tradeNum + ".xml";// 存放transInfoMap.xml对应bean所生成的xml文件路径及文件名

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
		// 查询返回planIdList
		Statement st = null;
		ResultSet rs = null;
		List retList = new ArrayList();
		try {
			StringBuffer sql = new StringBuffer("");
			// 查询招拍挂活动的planId，查询条件是：系统当前时间介于受理竞买申请和挂牌截止时间之间，且出让公告已经发布了的。返回planIdList。
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
