package com.wonders.wsjy.wxtrade;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import bjca.org.apache.log4j.Logger;

import com.wonders.engine.bo.TradeBlock;
import com.wonders.engine.bo.TradeNotice;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.database.DbSupport;
import com.wonders.engine.socket.ClientNode;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.wsjy.TradeConsts;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.service.TradeServer;

/**
 * 无锡网上交易数据服务.
 * 
 * @author sunxin
 * 
 */
public class WxDbService implements DbSupport {
	private Logger logger = Logger.getLogger(WxDbService.class);

	public boolean finishBlockTrade(TradeBlock tradeBlock, boolean isAll) {
		try {
			TradeServer tradeServer = getTradeServerBean();
			TdscBlockTranApp tdscBlockTranApp = tradeServer.getTdscBlockTranAppById(tradeBlock.getAppId());
			TdscBlockInfo tdscBlockInfo = tradeServer.getBlockInfoByAppId(tdscBlockTranApp.getBlockId());
			tdscBlockTranApp.setTradeStatus(TradeConsts.TRADE_OUTCOME);// 交易状态标识,未进入交易大厅
			tradeBlock.setBasePrice(tdscBlockTranApp.getDiJia());
			if ("0".equals(tradeBlock.getTradeResult())	|| !tradeBlock.isValidTopPrice()) {// 不成交
				if ("1".equals(tradeBlock.getTradeResult())) {
					tdscBlockTranApp.setTranResult("03");// "03"交易等待确认结果
					tdscBlockInfo.setStatus("03");
				} else {
					tdscBlockTranApp.setTranResult("02");// "02"交易不成功
					tdscBlockInfo.setStatus("02");
				}
				// 保存
				tradeServer.blockTradeFinish(isAll, null, tdscBlockInfo, tdscBlockTranApp);
			} else {
				// 成交地块
				TdscListingInfo tdscListingInfo = tradeServer.getTdscListingInfoByAppId(tradeBlock.getAppId());
				TdscBidderView tdscBidderView = tradeServer.getBidderViewByCertNo(tradeBlock.getTopClientNo());
				if (tdscListingInfo == null) {
					tdscListingInfo = new TdscListingInfo();
					tdscListingInfo.setAppId(tradeBlock.getAppId());
				}
				// 设置身份信息
				tdscListingInfo.setYktXh(tdscBidderView.getYktBh());
				tdscListingInfo.setListCert(tdscBidderView.getCertNo());
				tdscListingInfo.setResultCert(tdscBidderView.getCertNo());// 竞得人的资格证书号
				tdscListingInfo.setResultNo(tdscBidderView.getConNum());// 竞得号牌
				tdscListingInfo.setResultPrice(new BigDecimal(tradeBlock.getTopPrice()));// 成交价格，万元
				tdscListingInfo.setListResult("01");// "01"挂牌结束，转入现场竞价。为了保持数据完整，直接插入“01”即可
				tdscListingInfo.setListResultDate(new Timestamp(System.currentTimeMillis()));// 成交时间
				tdscListingInfo.setSceneResult("01");// "01"现场竞价成功。为了保持数据完整，直接插入“01”即可
				tdscListingInfo.setSceneResultDate(new Timestamp(System.currentTimeMillis()));// 竞得时间
				// 设置报价信息
				tdscListingInfo.setCurrPrice(new BigDecimal(tradeBlock.getTopPrice()));
				tdscListingInfo.setCurrRound(new BigDecimal((tradeBlock.getPriceNum() + 1)));
				tdscListingInfo.setListDate(new Timestamp(System.currentTimeMillis()));

				tdscBlockInfo.setTranResult("01");
				tdscBlockInfo.setResultDate(new Timestamp(System.currentTimeMillis()));// 竞得时间
				tdscBlockInfo.setResultPrice(new BigDecimal(tradeBlock.getTopPrice()));// 成交价格，万元
				tdscBlockInfo.setResultCert(tdscBidderView.getCertNo());// 竞得人的资格证书号
				tdscBlockInfo.setResultName(tdscBidderView.getBidderName());// 竞得人名称
				tdscBlockInfo.setStatus("02");// (00-未交易;01-交易中;02-交易结束)

				tdscBlockTranApp.setTotalPrice(new BigDecimal(tradeBlock.getTopPrice()));// 成交价格，万元
				tdscBlockTranApp.setTranResult("01");// 交易结果 00未交易 01 交易成功；02 // 交易失败（流标）；04 终止交易；
				tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));// 竞得时间
				tdscBlockTranApp.setResultPrice(new BigDecimal(tradeBlock.getTopPrice()));// 成交价格，万元
				tdscBlockTranApp.setResultCert(tdscBidderView.getConNum());// 竞得人的号牌
				tdscBlockTranApp.setResultName(tdscBidderView.getBidderName());// 竞得人名称
				if(tradeBlock.getPartakeConNumList()!=null){
					tdscBlockTranApp.setPartakeBidderConNum(tradeBlock.getStrPartakeConNum());
				}
				if ("2".equals(tradeBlock.getTradeResult())) {
					tdscBlockTranApp.setTradeStatus(TradeConsts.TRADE_FIN);// 交易状态标识,交易完成
				}
				if("5".equals(tradeBlock.getTradeResult())){
					tdscBlockInfo.setTranResult("05");
					tdscBlockInfo.setStatus("05");
					tdscBlockTranApp.setTradeStatus(TradeConsts.TRADE_YH);
					tdscBlockTranApp.setTranResult("05");
				}
				// 保存
				tradeServer.blockTradeFinish(isAll, tdscListingInfo, tdscBlockInfo, tdscBlockTranApp);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public Map initClient(String clientNo) {
		TradeServer tradeServer = getTradeServerBean();
		TdscBidderView tdscBidderView = tradeServer.getBidderViewByCertNo(clientNo);
		if (tdscBidderView != null) {
			Map resultMap = new HashMap();
			resultMap.put("noticeIds", tdscBidderView.getNoticeId());
			resultMap.put("conNum", tdscBidderView.getConNum());
			resultMap.put("yktBh", tdscBidderView.getYktBh());
			if ("1".equals(tdscBidderView.getIsAccptSms()) && StringUtils.isNotBlank(tdscBidderView.getPushSmsPhone())) {
				resultMap.put("mobile", tdscBidderView.getPushSmsPhone());
			}
			resultMap.put("bidderAppIds", tdscBidderView.getAppId());
			return resultMap;
		}
		return null;

	}

	public Map initClientModel2(String clientNo, Map hashMap) {
		Map map = new HashMap(); // noticeId, clientNode
		TradeServer tradeServer = getTradeServerBean();

		if (hashMap != null) {
			PersonInfo personinfo = tradeServer.getPersonInfo(clientNo);
			if (personinfo != null) {
				if ("1".equals(personinfo.getIsAccptSms()) && StringUtils.isNotBlank(personinfo.getPushSmsPhone())) {
					hashMap.put("mobile", personinfo.getPushSmsPhone());
				}
			}
		}

		List bidderAppList = tradeServer.getBidderViewListByUserId(clientNo);
		if (bidderAppList != null) {
			for (int i = 0; i < bidderAppList.size(); i++) {
				TdscBidderView bidderView = (TdscBidderView) bidderAppList.get(i);
				String noticeId = bidderView.getNoticeId();
				ClientNode clientNode = (ClientNode) map.get(noticeId);
				if (clientNode != null) {
					clientNode.addAppId(bidderView.getAppId());
					clientNode.addCertNo(bidderView.getAppId(), bidderView.getCertNo());
				} else {
					clientNode = new ClientNode();
					clientNode.setConNum(bidderView.getConNum());
					clientNode.setMobile(bidderView.getPushSmsPhone());
					clientNode.setNoticeIds(noticeId);
					clientNode.setYktBh(bidderView.getYktBh());
					clientNode.addAppId(bidderView.getAppId());
					clientNode.addCertNo(bidderView.getAppId(), bidderView.getCertNo());
					map.put(noticeId, clientNode);
				}
			}
			return map;
		}
		return null;

	}

	public boolean saveAuctionPrice(TradeBlock tradeBlock, TradePrice tradePrice) {
		try {
			TradeServer tradeServer = getTradeServerBean();
			// 更新TdscListingApp信息
			TdscListingApp tdscListingApp = new TdscListingApp();
			// 设置外键
			tdscListingApp.setListingId(tradeBlock.getListingId());
			tdscListingApp.setAppId(tradePrice.getAppId());
			// 报价信息
			tdscListingApp.setListPrice(new BigDecimal(tradePrice.getPrice()));
			tdscListingApp.setListDate(tradePrice.getPriceTime());
			// 身份标识信息
			tdscListingApp.setListCert(tradePrice.getClientNo());
			tdscListingApp.setListNo(tradePrice.getConNum());
			// 交易卡号
			tdscListingApp.setYktXh(tradePrice.getYktBh());
			// 竞价顺序
			tdscListingApp.setPriceNum(new BigDecimal(tradeBlock.getPriceNum()));
			tdscListingApp.setListingSer(new BigDecimal(tradeBlock.getPriceNum()));
			// 竞价阶段
			tdscListingApp.setPriceType(tradePrice.getPhase() + tradePrice.getPhase());
			// 设置listing主键
			TdscListingInfo tdscListingInfo = tradeServer.getTdscListingInfoByAppId(tradeBlock.getAppId());
			if (tdscListingInfo != null) {
				tdscListingApp.setListingId(tdscListingInfo.getListingId());
			}

			tradeServer.saveTdscListingApp(tdscListingApp);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean saveListingPrice(TradeBlock tradeBlock, TradePrice tradePrice) {
		try {
			TradeServer tradeServer = getTradeServerBean();
			// 更新TdscListingApp信息
			TdscListingApp tdscListingApp = new TdscListingApp();
			// 设置外键
			tdscListingApp.setListingId(tradeBlock.getListingId());
			tdscListingApp.setAppId(tradePrice.getAppId());
			// 报价信息
			tdscListingApp.setListPrice(new BigDecimal(tradePrice.getPrice()));
			tdscListingApp.setListDate(tradePrice.getPriceTime());
			// 身份标识信息
			tdscListingApp.setListCert(tradePrice.getClientNo());
			tdscListingApp.setListNo(tradePrice.getConNum());
			// 交易卡号
			tdscListingApp.setYktXh(tradePrice.getYktBh());
			// 竞价顺序
			tdscListingApp.setPriceNum(new BigDecimal((tradeBlock.getPriceNum() + 1)));
			tdscListingApp.setListingSer(new BigDecimal((tradeBlock.getPriceNum() + 1)));
			// 竞价阶段
			tdscListingApp.setPriceType(tradePrice.getPhase() + tradePrice.getPhase());
			// 更新TdscListingInfo信息
			TdscListingInfo tdscListingInfo = tradeServer.getTdscListingInfoByAppId(tradeBlock.getAppId());
			if (tdscListingInfo == null) {
				tdscListingInfo = new TdscListingInfo();
			}
			// 设置外键
			tdscListingInfo.setAppId(tradeBlock.getAppId());
			// 设置报价信息
			tdscListingInfo.setCurrPrice(new BigDecimal(tradePrice.getPrice()));
			tdscListingInfo.setCurrRound(new BigDecimal((tradeBlock.getPriceNum() + 1)));
			tdscListingInfo.setListDate(tradePrice.getPriceTime());
			// 设置身份信息
			tdscListingInfo.setYktXh(tradePrice.getYktBh());
			tdscListingInfo.setListCert(tradePrice.getClientNo());
			tdscListingInfo.setListNo(tradePrice.getConNum());
			// 设置listing主键
			tradeBlock.setListingId(tradeServer.saveTdscListingInfoAndApp(tdscListingInfo, tdscListingApp));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean startBlockTrade(TradeBlock tradeBlock) {
		updateBlockTranApp(tradeBlock, TradeConsts.TRADE_ING);
		return true;
	}

	public TradeBlock updateEngineBlockInfo(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		TdscBlockTranApp tdscBlockTranApp = tradeServer.getTdscBlockTranAppById(appId);
		return genTradeBlock(tdscBlockTranApp);
	}

	public LinkedHashMap updateEngineBlockInfo() {
		LinkedHashMap linkedHashMap = new LinkedHashMap();
		TradeServer tradeServer = getTradeServerBean();
		// 获取公告信息
		List tradePlanTableList = tradeServer.findTradeNotice();
		if (tradePlanTableList != null && tradePlanTableList.size() > 0) {
			for (int i = 0; i < tradePlanTableList.size(); i++) {
				TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tradePlanTableList.get(i);
				// 添加地块信息
				List tradeBlockList = tradeServer.findBlockTranAppList(tdscBlockPlanTable.getPlanId());
				if (tradeBlockList != null && tradeBlockList.size() > 0) {
					TdscBlockTranApp temptdscBlockTranApp = (TdscBlockTranApp) tradeBlockList.get(0);
					if (!isNoticePublic(temptdscBlockTranApp)) {
						continue;
					}
					for (int j = 0; j < tradeBlockList.size(); j++) {
						TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tradeBlockList.get(j);
						// 向交易地块堆栈添加地块信息
						// 2012-07-06 高东添加
						TdscBlockInfo tempTdscBlockInfo = tradeServer.getBlockInfoByAppId(tdscBlockTranApp.getBlockId());
						if (!"00".equals(tempTdscBlockInfo.getStatus())	&& !"04".equals(tdscBlockTranApp.getTranResult())&& !"01".equals(tdscBlockTranApp.getTranResult())) {
							// 向交易地块堆栈添加地块信息
							linkedHashMap.put(tdscBlockTranApp.getAppId(), genTradeBlock(tdscBlockTranApp));
						}
						// linkedHashMap.put(tdscBlockTranApp.getAppId(), genTradeBlock(tdscBlockTranApp));
					}
				}
			}
		}
		return linkedHashMap;
	}

	public List updateEngineNoticeInfo() {
		List result = new ArrayList();
		TradeServer tradeServer = getTradeServerBean();
		// 获取公告信息
		List tradePlanTableList = tradeServer.findTradeNotice();
		if (tradePlanTableList != null && tradePlanTableList.size() > 0) {
			for (int i = 0; i < tradePlanTableList.size(); i++) {
				TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tradePlanTableList.get(i);
				if (tdscBlockPlanTable.getOnLineEndDate() != null) {
					continue;
				}
				TradeNotice tradeNotice = new TradeNotice();
				// 设置公告开始和结束时间
				tradeNotice.setListEndDate(tdscBlockPlanTable.getListEndDate());
				tradeNotice.setListStartDate(tdscBlockPlanTable.getListStartDate());
				// 设置时间差
				tradeNotice.setSurplusTime(genDateDiff(tradeNotice.getListStartDate(), tradeNotice.getListEndDate()));
				// 设置planId
				tradeNotice.setPlanId(tdscBlockPlanTable.getPlanId());
				tradeNotice.setOnLineStatDate(tdscBlockPlanTable.getOnLineStatDate());
				tradeNotice.setOnLineEndDate(tdscBlockPlanTable.getOnLineEndDate());
				// 判断是否是网上交易
				if ("1".equals(tdscBlockPlanTable.getIfOnLine())) {
					tradeNotice.setWsjy(true);
				} else {
					tradeNotice.setWsjy(false);
				}
				// 添加地块信息
				List tradeBlockList = tradeServer.findBlockTranAppList(tdscBlockPlanTable.getPlanId());
				if (tradeBlockList != null && tradeBlockList.size() > 0) {
					TdscBlockTranApp temptdscBlockTranApp = (TdscBlockTranApp) tradeBlockList.get(0);
					if (!isNoticePublic(temptdscBlockTranApp)) {
						continue;
					} else {
						tradeNotice.setNoticeId(temptdscBlockTranApp.getNoticeId());
						tradeNotice.setNoticeNo(temptdscBlockTranApp.getNoitceNo());
						// 添加交易公告添加公告信息
						result.add(tradeNotice);
					}
				}
			}
		}

		return result;
	}

	public boolean startTrade(List tradeBlockList) {
		return false;
	}

	public boolean finishTrade(List tradeBlockList) {
		for (int i = 0; i < tradeBlockList.size(); i++) {
			TradeBlock tradeBlock = (TradeBlock) tradeBlockList.get(i);
			if (i == tradeBlockList.size() - 1) {
				finishBlockTrade(tradeBlock, true);
			} else {
				finishBlockTrade(tradeBlock, false);
			}
		}
		return true;
	}

	/**
	 * 推动流程
	 * 
	 * @param noticeId
	 *            公告主键
	 * @param isWsjy
	 *            是否是网上交易
	 */
	public void pushWf(String noticeId, boolean isWsjy) {
		TradeServer tradeServer = getTradeServerBean();
		if (isWsjy) {
			tradeServer.pushTo18(noticeId);
		} else {
			tradeServer.pushTo17(noticeId);
		}
	}

	/**
	 * 获取交易服务bean
	 * 
	 * @return
	 */
	public static TradeServer getTradeServerBean() {
		WebApplicationContext wac = AppContextUtil.getInstance().getAppContext();
		TradeServer tradeServer = (TradeServer) wac.getBean("tradeServer");
		return tradeServer;
	}

	/**
	 * 更新交易状态.
	 * 
	 * @param appId
	 *            地块申请主键
	 * @param tradeStatus
	 *            交易状态
	 */
	public void updateBlockTranApp(TradeBlock tradeBlock, String tradeStatus) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.saveBlockTranApp(tradeBlock.getAppId(), tradeStatus);
	}

	/**
	 * 公告是否已经发布.
	 * 
	 * @param engineNoticeStatck
	 *            公告堆栈
	 * @param tdscBlockPlanTable
	 *            公告执行表态表
	 * @return
	 */
	private boolean isNoticePublic(TdscBlockTranApp temptdscBlockTranApp) {
		TradeServer tradeServer = getTradeServerBean();
		if (StringUtils.isNotBlank(temptdscBlockTranApp.getNoticeId())) {
			TdscNoticeApp app = tradeServer.getBlockNoticeAppById(temptdscBlockTranApp.getNoticeId());
			if ("1".equals(app.getIfReleased())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从交易申请信息中提取交易引擎需要的地块信息.
	 * 
	 * @param tdscBlockTranApp
	 *            交易申请信息
	 * @return
	 */
	private TradeBlock genTradeBlock(TdscBlockTranApp tdscBlockTranApp) {
		
		TradeBlock tradeBlock = new TradeBlock();
		tradeBlock.setAppId(tdscBlockTranApp.getAppId());
		tradeBlock.setLimitTime(TradeConsts.TRADE_LIMIT_TIME);
		// 数据库初始化--保存时设置
		// tradeBlock.setListingId("9999");
		tradeBlock.setPriceAdd(tdscBlockTranApp.getSpotAddPriceRange().doubleValue());
		tradeBlock.setBlockNoticeNo(tdscBlockTranApp.getBlockNoticeNo());
		//设置限价信息
		if(tdscBlockTranApp.getMaxPrice()!=null){
			tradeBlock.setMaxPrice(tdscBlockTranApp.getMaxPrice().doubleValue());
		}
		// 判断意向出让
		if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
			tradeBlock.setAim(true);
		} else {
			tradeBlock.setAim(false);
		}
		tradeBlock.setNoticeId(tdscBlockTranApp.getNoticeId());
		// 初始化轮次----数据库初始化挂牌信息
		TdscListingApp tdscListingApp = getTopListingApp(tdscBlockTranApp.getAppId());
		if (tdscListingApp == null) {
			// 判断意向出让
			if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
				tradeBlock.setPriceNum(1);// 根据具体情况进行--调整
				// 查意向竞买人
				TradeServer tradeServer = getTradeServerBean();
				TdscBidderView tdscBidderView = tradeServer.getYixiangNameLikeAppId(tdscBlockTranApp.getAppId());
				if (tdscBidderView != null) {
					tradeBlock.setTopClientNo(tdscBidderView.getCertNo());
					tradeBlock.setTopConNum(tdscBidderView.getConNum());
				}
			} else {
				tradeBlock.setPriceNum(0);// 根据具体情况进行--调整
				tradeBlock.setTopClientNo("");
			}
			tradeBlock.setTopPrice(tdscBlockTranApp.getInitPrice().doubleValue());

		} else {
			tradeBlock.setPriceNum(tdscListingApp.getPriceNum().intValue());
			tradeBlock.setTopPrice(tdscListingApp.getListPrice().doubleValue());
			tradeBlock.setTopClientNo(tdscListingApp.getListCert());
			tradeBlock.setTopConNum(tdscListingApp.getListNo());
		}
		return tradeBlock;
	}

	/**
	 * 获取地块最新的报价信息.
	 * 
	 * @param appId
	 *            地块主键
	 * @return
	 */
	private TdscListingApp getTopListingApp(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		List tdscListingAppList = tradeServer.findListingAppList(appId, null, null);
		if (tdscListingAppList != null && tdscListingAppList.size() > 0) {
			return (TdscListingApp) tdscListingAppList.get(0);
		}
		return null;
	}

	/**
	 * 更新交易开始时间
	 * 
	 * @param planId
	 *            进度安排表时间
	 * @param isFinish
	 *            是否完成
	 */
	public void updateBlockPlanTableStatus(String planId, boolean isFinish) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.updateBlockPlanTableStatus(planId, isFinish);
	}

	/**
	 * 计算两个时间相差的秒数.
	 * 
	 * @param startdate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	private long genDateDiff(Timestamp startdate, Timestamp endDate) {
		if (startdate != null && endDate != null) {
			long diff = 0;
			// 如果已经开始倒计时
			if (startdate.getTime() < System.currentTimeMillis()) {
				diff = endDate.getTime() - System.currentTimeMillis();
			} else {// 未开始倒计时
				diff = endDate.getTime() - startdate.getTime();
			}
			return Math.round(diff / 1000);
		}
		return 0;
	}

	/**
	 * 获取竞买地块的人数.
	 * 
	 * @param appId
	 * @return
	 */
	public int getBidderPersonCount(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		List result = tradeServer.queryBidderAppListLikeAppId(appId);
		if (result != null) {
			return result.size();
		} else {
			return 0;
		}
	}

	/**
	 * 获取tranapp
	 * 
	 * @param appId
	 *            申请主键
	 * @return
	 */
	public int getguapaiPersonCount(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		// dscListingApp
		List tdscListingAppList = tradeServer.getListingAppListByAppId(appId, null);
		// 如果不为空
		if (tdscListingAppList != null && tdscListingAppList.size() > 0) {
			String certNo = null;
			for (int i = 0; i < tdscListingAppList.size(); i++) {
				TdscListingApp tdscListingApp = (TdscListingApp) tdscListingAppList.get(i);
				if (certNo == null) {
					certNo = tdscListingApp.getListCert();
				} else {
					if (!certNo.equals(tdscListingApp.getListCert())) {
						return 2;
					}
				}
			}
		} else {
			return 0;// 为空显示为0个人
		}
		// 如果都是一样的certno，则认为是一个人
		return 1;
	}

	/**
	 * 获取tranapp
	 * 
	 * @param appId
	 *            申请主键
	 * @return
	 */
	public TdscBlockTranApp getTranAppByAppId(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		return tradeServer.getTdscBlockTranAppById(appId);
	}

	/**
	 * 获取电话相关信息
	 * 
	 * @param cilentNo
	 *            资格编号
	 * @param appId
	 *            地块主键
	 * @return
	 */
	public Map getSmsInfoByAppId(String cilentNo, String appId) {
		TradeServer tradeServer = getTradeServerBean();
		List list = tradeServer.queryBiddersByAppId(appId);
		Map resultMap = new HashMap();
		if (list != null) {
			String mibiles = null;
			for (int i = 0; i < list.size(); i++) {
				TdscBidderView tdscBidderView = (TdscBidderView) list.get(i);

				if ("1".equals(tdscBidderView.getIsAccptSms()) && StringUtils.isNotBlank(tdscBidderView.getPushSmsPhone())) {

					// if(StringUtils.isNotBlank(tdscBidderView.getBidderLxdh())){
					if (cilentNo != null && cilentNo.equals(tdscBidderView.getCertNo())) {
						// 竞买资格编号竞买人的电话号码
						resultMap.put(cilentNo, tdscBidderView.getPushSmsPhone());
						// 号牌
						resultMap.put("conNum", tdscBidderView.getConNum());
					} else {
						// 串电话号码
						if (mibiles == null) {
							mibiles = tdscBidderView.getPushSmsPhone();
						} else {
							mibiles = mibiles + ","	+ tdscBidderView.getPushSmsPhone();
						}
					}
				}
			}
			if (mibiles != null) {
				// 存放除竞买资格编号竞买人外其他竞买人的电话号码，用逗号隔开.
				resultMap.put(appId, mibiles);
			}
		}
		return resultMap;
	}

	/**
	 * 通过noticeId取得对应的竞买人信息列表
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findBidderAppList(String noticeId) {
		TradeServer tradeServer = getTradeServerBean();
		List list = tradeServer.findTdscBidderAppListByNoticeId(noticeId);
		return list;
	}

	/**
	 * 通过blockid取得blockpart中的信息
	 * 
	 * @param blockId
	 * @return
	 */
	public TdscBlockPart getBlockPartByBlockId(String blockId) {
		TradeServer tradeServer = getTradeServerBean();
		TdscBlockPart tdscBlockPart = tradeServer.getBlockPartByBlockId(blockId);
		return tdscBlockPart;
	}

	/**
	 * 通过blockId取得对应的TdscblockInfo信息
	 * 
	 * @param blockId
	 * @return
	 */
	public TdscBlockInfo getBlockInfoByBlockId(String blockId) {
		TradeServer tradeServer = getTradeServerBean();
		return tradeServer.getBlockInfoByAppId(blockId);
	}

	/**
	 * 取得计划安排表
	 * 
	 * @param planId
	 * @return
	 */
	public TdscBlockPlanTable getBlockPlanByPlanId(String planId) {
		TradeServer tradeServer = getTradeServerBean();
		TdscBlockPlanTable blockPlanTable = tradeServer.getBlockPlanTableById(planId);
		return blockPlanTable;
	}

	/**
	 * 通过notice编号取得对应的信息
	 * 
	 * @param noticeId
	 * @return
	 */
	public TdscNoticeApp getNoticeAppByNoticeId(String noticeId) {
		TradeServer tradeServer = getTradeServerBean();
		TdscNoticeApp tdscNoticeApp = tradeServer.getBlockNoticeAppById(noticeId);
		return tdscNoticeApp;
	}

	/**
	 * 通过竞买资格证书编号取得对应的竞买人信息
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderView getBidderViewByCertNo(String certNo) {
		TradeServer tradeServer = getTradeServerBean();
		return tradeServer.getBidderViewByCertNo(certNo);
	}

	/**
	 * 通过地块编号取得该地块竞买人的信息列表
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderAppListLikeAppId(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		List list = tradeServer.queryBidderAppListLikeAppId(appId);
		return list;
	}

	/**
	 * 取得挂牌和竞价列表
	 * 
	 * @param appId
	 * @return
	 */
	public List queryListingAppList(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		List list = tradeServer.findListingAppList(appId, null, null);
		return list;
	}

	public TdscBidderView getYixiangNameLikeAppId(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		return tradeServer.getYixiangNameLikeAppId(appId);
	}

	/**
	 * 通过公告编号取得所有的地块列表
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findBlockAppList(String noticeId) {
		TradeServer tradeServer = getTradeServerBean();
		List list = tradeServer.getBlockInfoByNoticeId(noticeId);
		return list;
	}

	/**
	 * 判断该地块是否有限时竞价
	 * 
	 * @param appId
	 * @return
	 */
	public int getTradeListCount(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		// dscListingApp
		List tdscListingAppList = tradeServer.getListingAppListByAppId(appId, "22");
		if (tdscListingAppList == null)
			return 0;
		return tdscListingAppList.size();
	}

	/**
	 * 返回底价价格
	 */
	public BigDecimal getBlockBasePrice(TradeBlock tradeBlock) {
		// TODO: 查询该地块是否有低价，如果有，返回底价，如果没有返回空
		TradeServer tradeServer = getTradeServerBean();
		TdscBlockTranApp tdscBlockTranApp = tradeServer.getTdscBlockTranAppById(tradeBlock.getAppId());
		BigDecimal basePrice = tdscBlockTranApp.getDiJia();
		if (basePrice == null)
			basePrice = new BigDecimal("0");
		return basePrice;
	}

	/**
	 * 设置进入底价确认状态
	 */
	public void confirmBlock(TradeBlock tradeBlock) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.saveBlockTranApp(tradeBlock.getAppId(), "11");
	}

	/**
	 * 设置为等待状态
	 */
	public void confirmWaitBlock(TradeBlock tradeBlock) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.saveBlockTranApp(tradeBlock.getAppId(), "22");
	}

	/**
	 * 结束地块
	 */
	public void overBlock(TradeBlock tradeBlock, boolean flag) {
		TradeServer tradeServer = getTradeServerBean();
		BigDecimal basePrice = getBlockBasePrice(tradeBlock);
		tradeBlock.setBasePrice(basePrice);
		BigDecimal topPrice = new BigDecimal(tradeBlock.getTopPrice());
		if (topPrice != null && topPrice.compareTo(basePrice) >= 0 )
			tradeBlock.setTradeResult("2");
		else
			tradeBlock.setTradeResult("0");
		finishBlockTrade(tradeBlock, flag);
		tradeServer.saveBlockTranApp(tradeBlock.getAppId(), "33");
	}
	/**
	 * 是否第一次报价(true是，false否)
	 * @param appId
	 * @param conNum
	 * @return
	 */
	public boolean isFirstSubmitPrice(TradeBlock tradeBlock,TradePrice tradePrice){
		TradeServer tradeServer = getTradeServerBean();
		List list = tradeServer.findListingAppListByConNum(tradePrice.getAppId(),tradePrice.getConNum(),"11");
		if(list!=null&&list.size()>0){
			return false;
		}
		return true;
	}
}