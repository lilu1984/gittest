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
 * �������Ͻ������ݷ���.
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
			tdscBlockTranApp.setTradeStatus(TradeConsts.TRADE_OUTCOME);// ����״̬��ʶ,δ���뽻�״���
			tradeBlock.setBasePrice(tdscBlockTranApp.getDiJia());
			if ("0".equals(tradeBlock.getTradeResult())	|| !tradeBlock.isValidTopPrice()) {// ���ɽ�
				if ("1".equals(tradeBlock.getTradeResult())) {
					tdscBlockTranApp.setTranResult("03");// "03"���׵ȴ�ȷ�Ͻ��
					tdscBlockInfo.setStatus("03");
				} else {
					tdscBlockTranApp.setTranResult("02");// "02"���ײ��ɹ�
					tdscBlockInfo.setStatus("02");
				}
				// ����
				tradeServer.blockTradeFinish(isAll, null, tdscBlockInfo, tdscBlockTranApp);
			} else {
				// �ɽ��ؿ�
				TdscListingInfo tdscListingInfo = tradeServer.getTdscListingInfoByAppId(tradeBlock.getAppId());
				TdscBidderView tdscBidderView = tradeServer.getBidderViewByCertNo(tradeBlock.getTopClientNo());
				if (tdscListingInfo == null) {
					tdscListingInfo = new TdscListingInfo();
					tdscListingInfo.setAppId(tradeBlock.getAppId());
				}
				// ���������Ϣ
				tdscListingInfo.setYktXh(tdscBidderView.getYktBh());
				tdscListingInfo.setListCert(tdscBidderView.getCertNo());
				tdscListingInfo.setResultCert(tdscBidderView.getCertNo());// �����˵��ʸ�֤���
				tdscListingInfo.setResultNo(tdscBidderView.getConNum());// ���ú���
				tdscListingInfo.setResultPrice(new BigDecimal(tradeBlock.getTopPrice()));// �ɽ��۸���Ԫ
				tdscListingInfo.setListResult("01");// "01"���ƽ�����ת���ֳ����ۡ�Ϊ�˱�������������ֱ�Ӳ��롰01������
				tdscListingInfo.setListResultDate(new Timestamp(System.currentTimeMillis()));// �ɽ�ʱ��
				tdscListingInfo.setSceneResult("01");// "01"�ֳ����۳ɹ���Ϊ�˱�������������ֱ�Ӳ��롰01������
				tdscListingInfo.setSceneResultDate(new Timestamp(System.currentTimeMillis()));// ����ʱ��
				// ���ñ�����Ϣ
				tdscListingInfo.setCurrPrice(new BigDecimal(tradeBlock.getTopPrice()));
				tdscListingInfo.setCurrRound(new BigDecimal((tradeBlock.getPriceNum() + 1)));
				tdscListingInfo.setListDate(new Timestamp(System.currentTimeMillis()));

				tdscBlockInfo.setTranResult("01");
				tdscBlockInfo.setResultDate(new Timestamp(System.currentTimeMillis()));// ����ʱ��
				tdscBlockInfo.setResultPrice(new BigDecimal(tradeBlock.getTopPrice()));// �ɽ��۸���Ԫ
				tdscBlockInfo.setResultCert(tdscBidderView.getCertNo());// �����˵��ʸ�֤���
				tdscBlockInfo.setResultName(tdscBidderView.getBidderName());// ����������
				tdscBlockInfo.setStatus("02");// (00-δ����;01-������;02-���׽���)

				tdscBlockTranApp.setTotalPrice(new BigDecimal(tradeBlock.getTopPrice()));// �ɽ��۸���Ԫ
				tdscBlockTranApp.setTranResult("01");// ���׽�� 00δ���� 01 ���׳ɹ���02 // ����ʧ�ܣ����꣩��04 ��ֹ���ף�
				tdscBlockTranApp.setResultDate(new Timestamp(System.currentTimeMillis()));// ����ʱ��
				tdscBlockTranApp.setResultPrice(new BigDecimal(tradeBlock.getTopPrice()));// �ɽ��۸���Ԫ
				tdscBlockTranApp.setResultCert(tdscBidderView.getConNum());// �����˵ĺ���
				tdscBlockTranApp.setResultName(tdscBidderView.getBidderName());// ����������
				if(tradeBlock.getPartakeConNumList()!=null){
					tdscBlockTranApp.setPartakeBidderConNum(tradeBlock.getStrPartakeConNum());
				}
				if ("2".equals(tradeBlock.getTradeResult())) {
					tdscBlockTranApp.setTradeStatus(TradeConsts.TRADE_FIN);// ����״̬��ʶ,�������
				}
				if("5".equals(tradeBlock.getTradeResult())){
					tdscBlockInfo.setTranResult("05");
					tdscBlockInfo.setStatus("05");
					tdscBlockTranApp.setTradeStatus(TradeConsts.TRADE_YH);
					tdscBlockTranApp.setTranResult("05");
				}
				// ����
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
			// ����TdscListingApp��Ϣ
			TdscListingApp tdscListingApp = new TdscListingApp();
			// �������
			tdscListingApp.setListingId(tradeBlock.getListingId());
			tdscListingApp.setAppId(tradePrice.getAppId());
			// ������Ϣ
			tdscListingApp.setListPrice(new BigDecimal(tradePrice.getPrice()));
			tdscListingApp.setListDate(tradePrice.getPriceTime());
			// ��ݱ�ʶ��Ϣ
			tdscListingApp.setListCert(tradePrice.getClientNo());
			tdscListingApp.setListNo(tradePrice.getConNum());
			// ���׿���
			tdscListingApp.setYktXh(tradePrice.getYktBh());
			// ����˳��
			tdscListingApp.setPriceNum(new BigDecimal(tradeBlock.getPriceNum()));
			tdscListingApp.setListingSer(new BigDecimal(tradeBlock.getPriceNum()));
			// ���۽׶�
			tdscListingApp.setPriceType(tradePrice.getPhase() + tradePrice.getPhase());
			// ����listing����
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
			// ����TdscListingApp��Ϣ
			TdscListingApp tdscListingApp = new TdscListingApp();
			// �������
			tdscListingApp.setListingId(tradeBlock.getListingId());
			tdscListingApp.setAppId(tradePrice.getAppId());
			// ������Ϣ
			tdscListingApp.setListPrice(new BigDecimal(tradePrice.getPrice()));
			tdscListingApp.setListDate(tradePrice.getPriceTime());
			// ��ݱ�ʶ��Ϣ
			tdscListingApp.setListCert(tradePrice.getClientNo());
			tdscListingApp.setListNo(tradePrice.getConNum());
			// ���׿���
			tdscListingApp.setYktXh(tradePrice.getYktBh());
			// ����˳��
			tdscListingApp.setPriceNum(new BigDecimal((tradeBlock.getPriceNum() + 1)));
			tdscListingApp.setListingSer(new BigDecimal((tradeBlock.getPriceNum() + 1)));
			// ���۽׶�
			tdscListingApp.setPriceType(tradePrice.getPhase() + tradePrice.getPhase());
			// ����TdscListingInfo��Ϣ
			TdscListingInfo tdscListingInfo = tradeServer.getTdscListingInfoByAppId(tradeBlock.getAppId());
			if (tdscListingInfo == null) {
				tdscListingInfo = new TdscListingInfo();
			}
			// �������
			tdscListingInfo.setAppId(tradeBlock.getAppId());
			// ���ñ�����Ϣ
			tdscListingInfo.setCurrPrice(new BigDecimal(tradePrice.getPrice()));
			tdscListingInfo.setCurrRound(new BigDecimal((tradeBlock.getPriceNum() + 1)));
			tdscListingInfo.setListDate(tradePrice.getPriceTime());
			// ���������Ϣ
			tdscListingInfo.setYktXh(tradePrice.getYktBh());
			tdscListingInfo.setListCert(tradePrice.getClientNo());
			tdscListingInfo.setListNo(tradePrice.getConNum());
			// ����listing����
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
		// ��ȡ������Ϣ
		List tradePlanTableList = tradeServer.findTradeNotice();
		if (tradePlanTableList != null && tradePlanTableList.size() > 0) {
			for (int i = 0; i < tradePlanTableList.size(); i++) {
				TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tradePlanTableList.get(i);
				// ��ӵؿ���Ϣ
				List tradeBlockList = tradeServer.findBlockTranAppList(tdscBlockPlanTable.getPlanId());
				if (tradeBlockList != null && tradeBlockList.size() > 0) {
					TdscBlockTranApp temptdscBlockTranApp = (TdscBlockTranApp) tradeBlockList.get(0);
					if (!isNoticePublic(temptdscBlockTranApp)) {
						continue;
					}
					for (int j = 0; j < tradeBlockList.size(); j++) {
						TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tradeBlockList.get(j);
						// ���׵ؿ��ջ��ӵؿ���Ϣ
						// 2012-07-06 �߶����
						TdscBlockInfo tempTdscBlockInfo = tradeServer.getBlockInfoByAppId(tdscBlockTranApp.getBlockId());
						if (!"00".equals(tempTdscBlockInfo.getStatus())	&& !"04".equals(tdscBlockTranApp.getTranResult())&& !"01".equals(tdscBlockTranApp.getTranResult())) {
							// ���׵ؿ��ջ��ӵؿ���Ϣ
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
		// ��ȡ������Ϣ
		List tradePlanTableList = tradeServer.findTradeNotice();
		if (tradePlanTableList != null && tradePlanTableList.size() > 0) {
			for (int i = 0; i < tradePlanTableList.size(); i++) {
				TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tradePlanTableList.get(i);
				if (tdscBlockPlanTable.getOnLineEndDate() != null) {
					continue;
				}
				TradeNotice tradeNotice = new TradeNotice();
				// ���ù��濪ʼ�ͽ���ʱ��
				tradeNotice.setListEndDate(tdscBlockPlanTable.getListEndDate());
				tradeNotice.setListStartDate(tdscBlockPlanTable.getListStartDate());
				// ����ʱ���
				tradeNotice.setSurplusTime(genDateDiff(tradeNotice.getListStartDate(), tradeNotice.getListEndDate()));
				// ����planId
				tradeNotice.setPlanId(tdscBlockPlanTable.getPlanId());
				tradeNotice.setOnLineStatDate(tdscBlockPlanTable.getOnLineStatDate());
				tradeNotice.setOnLineEndDate(tdscBlockPlanTable.getOnLineEndDate());
				// �ж��Ƿ������Ͻ���
				if ("1".equals(tdscBlockPlanTable.getIfOnLine())) {
					tradeNotice.setWsjy(true);
				} else {
					tradeNotice.setWsjy(false);
				}
				// ��ӵؿ���Ϣ
				List tradeBlockList = tradeServer.findBlockTranAppList(tdscBlockPlanTable.getPlanId());
				if (tradeBlockList != null && tradeBlockList.size() > 0) {
					TdscBlockTranApp temptdscBlockTranApp = (TdscBlockTranApp) tradeBlockList.get(0);
					if (!isNoticePublic(temptdscBlockTranApp)) {
						continue;
					} else {
						tradeNotice.setNoticeId(temptdscBlockTranApp.getNoticeId());
						tradeNotice.setNoticeNo(temptdscBlockTranApp.getNoitceNo());
						// ��ӽ��׹�����ӹ�����Ϣ
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
	 * �ƶ�����
	 * 
	 * @param noticeId
	 *            ��������
	 * @param isWsjy
	 *            �Ƿ������Ͻ���
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
	 * ��ȡ���׷���bean
	 * 
	 * @return
	 */
	public static TradeServer getTradeServerBean() {
		WebApplicationContext wac = AppContextUtil.getInstance().getAppContext();
		TradeServer tradeServer = (TradeServer) wac.getBean("tradeServer");
		return tradeServer;
	}

	/**
	 * ���½���״̬.
	 * 
	 * @param appId
	 *            �ؿ���������
	 * @param tradeStatus
	 *            ����״̬
	 */
	public void updateBlockTranApp(TradeBlock tradeBlock, String tradeStatus) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.saveBlockTranApp(tradeBlock.getAppId(), tradeStatus);
	}

	/**
	 * �����Ƿ��Ѿ�����.
	 * 
	 * @param engineNoticeStatck
	 *            �����ջ
	 * @param tdscBlockPlanTable
	 *            ����ִ�б�̬��
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
	 * �ӽ���������Ϣ����ȡ����������Ҫ�ĵؿ���Ϣ.
	 * 
	 * @param tdscBlockTranApp
	 *            ����������Ϣ
	 * @return
	 */
	private TradeBlock genTradeBlock(TdscBlockTranApp tdscBlockTranApp) {
		
		TradeBlock tradeBlock = new TradeBlock();
		tradeBlock.setAppId(tdscBlockTranApp.getAppId());
		tradeBlock.setLimitTime(TradeConsts.TRADE_LIMIT_TIME);
		// ���ݿ��ʼ��--����ʱ����
		// tradeBlock.setListingId("9999");
		tradeBlock.setPriceAdd(tdscBlockTranApp.getSpotAddPriceRange().doubleValue());
		tradeBlock.setBlockNoticeNo(tdscBlockTranApp.getBlockNoticeNo());
		//�����޼���Ϣ
		if(tdscBlockTranApp.getMaxPrice()!=null){
			tradeBlock.setMaxPrice(tdscBlockTranApp.getMaxPrice().doubleValue());
		}
		// �ж��������
		if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
			tradeBlock.setAim(true);
		} else {
			tradeBlock.setAim(false);
		}
		tradeBlock.setNoticeId(tdscBlockTranApp.getNoticeId());
		// ��ʼ���ִ�----���ݿ��ʼ��������Ϣ
		TdscListingApp tdscListingApp = getTopListingApp(tdscBlockTranApp.getAppId());
		if (tdscListingApp == null) {
			// �ж��������
			if ("1".equals(tdscBlockTranApp.getIsPurposeBlock())) {
				tradeBlock.setPriceNum(1);// ���ݾ����������--����
				// ����������
				TradeServer tradeServer = getTradeServerBean();
				TdscBidderView tdscBidderView = tradeServer.getYixiangNameLikeAppId(tdscBlockTranApp.getAppId());
				if (tdscBidderView != null) {
					tradeBlock.setTopClientNo(tdscBidderView.getCertNo());
					tradeBlock.setTopConNum(tdscBidderView.getConNum());
				}
			} else {
				tradeBlock.setPriceNum(0);// ���ݾ����������--����
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
	 * ��ȡ�ؿ����µı�����Ϣ.
	 * 
	 * @param appId
	 *            �ؿ�����
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
	 * ���½��׿�ʼʱ��
	 * 
	 * @param planId
	 *            ���Ȱ��ű�ʱ��
	 * @param isFinish
	 *            �Ƿ����
	 */
	public void updateBlockPlanTableStatus(String planId, boolean isFinish) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.updateBlockPlanTableStatus(planId, isFinish);
	}

	/**
	 * ��������ʱ����������.
	 * 
	 * @param startdate
	 *            ��ʼʱ��
	 * @param endDate
	 *            ����ʱ��
	 * @return
	 */
	private long genDateDiff(Timestamp startdate, Timestamp endDate) {
		if (startdate != null && endDate != null) {
			long diff = 0;
			// ����Ѿ���ʼ����ʱ
			if (startdate.getTime() < System.currentTimeMillis()) {
				diff = endDate.getTime() - System.currentTimeMillis();
			} else {// δ��ʼ����ʱ
				diff = endDate.getTime() - startdate.getTime();
			}
			return Math.round(diff / 1000);
		}
		return 0;
	}

	/**
	 * ��ȡ����ؿ������.
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
	 * ��ȡtranapp
	 * 
	 * @param appId
	 *            ��������
	 * @return
	 */
	public int getguapaiPersonCount(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		// dscListingApp
		List tdscListingAppList = tradeServer.getListingAppListByAppId(appId, null);
		// �����Ϊ��
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
			return 0;// Ϊ����ʾΪ0����
		}
		// �������һ����certno������Ϊ��һ����
		return 1;
	}

	/**
	 * ��ȡtranapp
	 * 
	 * @param appId
	 *            ��������
	 * @return
	 */
	public TdscBlockTranApp getTranAppByAppId(String appId) {
		TradeServer tradeServer = getTradeServerBean();
		return tradeServer.getTdscBlockTranAppById(appId);
	}

	/**
	 * ��ȡ�绰�����Ϣ
	 * 
	 * @param cilentNo
	 *            �ʸ���
	 * @param appId
	 *            �ؿ�����
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
						// �����ʸ��ž����˵ĵ绰����
						resultMap.put(cilentNo, tdscBidderView.getPushSmsPhone());
						// ����
						resultMap.put("conNum", tdscBidderView.getConNum());
					} else {
						// ���绰����
						if (mibiles == null) {
							mibiles = tdscBidderView.getPushSmsPhone();
						} else {
							mibiles = mibiles + ","	+ tdscBidderView.getPushSmsPhone();
						}
					}
				}
			}
			if (mibiles != null) {
				// ��ų������ʸ��ž����������������˵ĵ绰���룬�ö��Ÿ���.
				resultMap.put(appId, mibiles);
			}
		}
		return resultMap;
	}

	/**
	 * ͨ��noticeIdȡ�ö�Ӧ�ľ�������Ϣ�б�
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
	 * ͨ��blockidȡ��blockpart�е���Ϣ
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
	 * ͨ��blockIdȡ�ö�Ӧ��TdscblockInfo��Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public TdscBlockInfo getBlockInfoByBlockId(String blockId) {
		TradeServer tradeServer = getTradeServerBean();
		return tradeServer.getBlockInfoByAppId(blockId);
	}

	/**
	 * ȡ�üƻ����ű�
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
	 * ͨ��notice���ȡ�ö�Ӧ����Ϣ
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
	 * ͨ�������ʸ�֤����ȡ�ö�Ӧ�ľ�������Ϣ
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderView getBidderViewByCertNo(String certNo) {
		TradeServer tradeServer = getTradeServerBean();
		return tradeServer.getBidderViewByCertNo(certNo);
	}

	/**
	 * ͨ���ؿ���ȡ�øõؿ龺���˵���Ϣ�б�
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
	 * ȡ�ù��ƺ;����б�
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
	 * ͨ��������ȡ�����еĵؿ��б�
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
	 * �жϸõؿ��Ƿ�����ʱ����
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
	 * ���ص׼ۼ۸�
	 */
	public BigDecimal getBlockBasePrice(TradeBlock tradeBlock) {
		// TODO: ��ѯ�õؿ��Ƿ��еͼۣ�����У����ص׼ۣ����û�з��ؿ�
		TradeServer tradeServer = getTradeServerBean();
		TdscBlockTranApp tdscBlockTranApp = tradeServer.getTdscBlockTranAppById(tradeBlock.getAppId());
		BigDecimal basePrice = tdscBlockTranApp.getDiJia();
		if (basePrice == null)
			basePrice = new BigDecimal("0");
		return basePrice;
	}

	/**
	 * ���ý���׼�ȷ��״̬
	 */
	public void confirmBlock(TradeBlock tradeBlock) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.saveBlockTranApp(tradeBlock.getAppId(), "11");
	}

	/**
	 * ����Ϊ�ȴ�״̬
	 */
	public void confirmWaitBlock(TradeBlock tradeBlock) {
		TradeServer tradeServer = getTradeServerBean();
		tradeServer.saveBlockTranApp(tradeBlock.getAppId(), "22");
	}

	/**
	 * �����ؿ�
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
	 * �Ƿ��һ�α���(true�ǣ�false��)
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