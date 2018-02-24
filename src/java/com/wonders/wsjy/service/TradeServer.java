package com.wonders.wsjy.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.esframework.util.DateUtil;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.blockwork.dao.TdscNoticeAppDao;
import com.wonders.tdsc.bo.TdscApp;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.flowadapter.dao.TdscAppDao;
import com.wonders.tdsc.flowadapter.dao.TdscAppNodeStatDao;
import com.wonders.tdsc.flowadapter.dao.TdscOpnnDao;
import com.wonders.tdsc.localtrade.dao.TdscListingAppDao;
import com.wonders.tdsc.localtrade.dao.TdscListingInfoDao;
import com.wonders.tdsc.tdscbase.dao.TdscBidderViewDao;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.dao.PersonInfoDAO;
import com.wonders.wsjy.dao.TdscTradeViewDao;

public class TradeServer {

	private TdscBlockTranAppDao tdscBlockTranAppDao;
	
	private TdscBidderAppDao tdscBidderAppDao;
	
	private TdscBidderPersonAppDao tdscBidderPersonAppDao;
	
	private TdscListingAppDao tdscListingAppDao;
	
	private TdscListingInfoDao tdscListingInfoDao;
	
	private TdscNoticeAppDao tdscNoticeAppDao;
	
	private TdscBlockPlanTableDao tdscBlockPlanTableDao;
	
	private TdscBidderViewDao tdscBidderViewDao;
	
	private TdscBlockInfoDao tdscBlockInfoDao;
	//�ƶ�����
	private TdscAppDao tdscAppDao;
	
	private TdscOpnnDao tdscOpnnDao;
	
	private TdscAppNodeStatDao tdscAppNodeStatDao;
	
	private TdscTradeViewDao tdscTradeViewDao;
	
	private TdscBlockPartDao tdscBlockPartDao;
	
	private IdSpringManager idSpringManager;
	private PersonInfoDAO personInfoDAO;
	private CommonQueryService commonQueryService;
	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setPersonInfoDAO(PersonInfoDAO personInfoDAO) {
		this.personInfoDAO = personInfoDAO;
	}

	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}
	
	public void setTdscBidderPersonAppDao(
			TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}
	public void setTdscBlockPartDao(TdscBlockPartDao tdscBlockPartDao) {
		this.tdscBlockPartDao = tdscBlockPartDao;
	}
	public TdscTradeView getTdscTradeViewAppById(String appId) {
		return tdscTradeViewDao.getTdscTradeViewAppById(appId);
	}
	public List findGuapaiBlockByAppIds(String appIds) {
		return tdscTradeViewDao.findGuapaiBlockByAppIds(appIds);
	}
	public List getTdscTradeViewAppByNoticeId(String noticeId) {
		return tdscTradeViewDao.getTdscTradeViewAppByNoticeId(noticeId);
	}
	public void setTdscTradeViewDao(TdscTradeViewDao tdscTradeViewDao) {
		this.tdscTradeViewDao = tdscTradeViewDao;
	}

	public void setTdscAppDao(TdscAppDao tdscAppDao) {
		this.tdscAppDao = tdscAppDao;
	}

	public void setTdscAppNodeStatDao(TdscAppNodeStatDao tdscAppNodeStatDao) {
		this.tdscAppNodeStatDao = tdscAppNodeStatDao;
	}

	public void setTdscOpnnDao(TdscOpnnDao tdscOpnnDao) {
		this.tdscOpnnDao = tdscOpnnDao;
	}

	public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
		this.tdscBlockInfoDao = tdscBlockInfoDao;
	}

	public void setTdscBidderViewDao(TdscBidderViewDao tdscBidderViewDao) {
		this.tdscBidderViewDao = tdscBidderViewDao;
	}

	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}
	
	public void setTdscListingAppDao(TdscListingAppDao tdscListingAppDao) {
		this.tdscListingAppDao = tdscListingAppDao;
	}

	public void setTdscListingInfoDao(TdscListingInfoDao tdscListingInfoDao) {
		this.tdscListingInfoDao = tdscListingInfoDao;
	}
	
	public void setTdscNoticeAppDao(TdscNoticeAppDao tdscNoticeAppDao) {
		this.tdscNoticeAppDao = tdscNoticeAppDao;
	}

	/**
	 * �����û����ʸ�֤����ȡ�þ�����Ϣ�б�
	 * 
	 * @param certNoList
	 * @return
	 */
	public List getBidderAppListByCertNo(List certNoList) {
		if (certNoList == null || certNoList.size() < 1) return null;
		TdscBidderCondition condition = new TdscBidderCondition();
		condition.setCertNoList(certNoList);
		List bidderList = this.tdscBidderAppDao.queryBidderAppListByCondition(condition);
		return bidderList;
	}
	
	/**
	 * �����û����ʸ�֤����ȡ�þ�����Ϣ�б�
	 * 
	 * @param certNoList
	 * @return
	 */
	public List getBidderAppListByUserId(String userId) {
		List bidderList = this.tdscBidderAppDao.queryBidderAppListByUserId(userId);
		return bidderList;
	}
	
	public BigDecimal findMyGuapaiPrice(String appId,List certNoList){
	 return this.tdscListingAppDao.findTopPrice(appId, certNoList,"11");
	}
	
	/**
	 * ͨ��appIdȡ�ö�Ӧ�õؿ���Ϣ��߱��ۣ��Լ�
	 * 
	 * @param appIds
	 * @return
	 */
	public Map getBlockMaxListingPrice(List appIds, List certNoList, String priceType) {
		if (appIds != null) {
			Map map = new HashMap();
			for (int i = 0 ; i < appIds.size(); i++) {
				List list2 = new ArrayList();
				String appId = (String)appIds.get(i);
				List listingAppList = findListingAppList( appId, null, priceType);
				BigDecimal myMaxValue = this.tdscListingAppDao.findTopPrice(appId, certNoList,priceType);
				if (myMaxValue == null)
					myMaxValue = new BigDecimal("0");
				BigDecimal maxPrice = null;
				BigDecimal round = new BigDecimal("0");
				
				if (listingAppList != null && listingAppList.size() > 0){
					TdscListingApp listingApp = (TdscListingApp)listingAppList.get(0);
					maxPrice = listingApp.getListPrice();
					round = listingApp.getPriceNum() == null?new BigDecimal("0"):listingApp.getPriceNum();
				}
				
				list2.add(maxPrice);
				list2.add(myMaxValue);
				list2.add(round);
				map.put(appId, list2);
			}
			return map;
		}
		return null;
	}
	
	/**
	 * ȡ�ñ����б� ���յ�������
	 * 
	 * @param appId �ؿ���
	 * @param certNo �����˱��
	 * @param priceType �������� 1 ���� 2 ��ʱ����
	 * @return
	 */
	public List findListingAppList(String appId, String certNo, String priceType) {
		List listingAppList = this.tdscListingAppDao.getListingAppListByAppId( appId, certNo, priceType);
		return listingAppList;
	}
	
	
	/**
	 * ͨ��������ȥ�Ķ�Ӧ�ĵؿ���Ϣ�б����Ұ�������ʽ��С��������
	 * 
	 * @param noticeId
	 * @return
	 */
	public List getBlockInfoByNoticeId(String noticeId) {
		List list = this.tdscBlockTranAppDao.findAppListByNoticeId(noticeId);
		return list;
	}
	
	public List getBlockInfoByNoticeId(String noticeId,boolean status) {
		List list = this.tdscBlockTranAppDao.findAppListByNoticeId(noticeId, status);
		return list;
	}
	
	/**
	 * ����appIdȡ�øõؿ��Ӧ�Ĺ�����Ϣ
	 * @param appId
	 * @return
	 */
	public List getListingAppListByAppId(String appId, String priceType) {
		if (StringUtils.isEmpty(appId)) return null;
		List list = this.tdscListingAppDao.getListingAppListByAppId(appId,null, priceType);
		return list;
	}

	/**
	 * ȡ�ù�����Ϣ
	 * @param noticeId
	 * @return
	 */
	public TdscNoticeApp getBlockNoticeAppById(String noticeId) {
		TdscNoticeApp app = (TdscNoticeApp)this.tdscNoticeAppDao.get(noticeId);
		return app;
	}
	
	public TdscListingInfo saveTdscListingInfo(TdscListingInfo tdscListingInfo) {
		return (TdscListingInfo)this.tdscListingInfoDao.saveOrUpdate(tdscListingInfo);
	}
	
	public String saveTdscListingInfoAndApp(TdscListingInfo tdscListingInfo,TdscListingApp tdscListingApp) {
		tdscListingInfo = (TdscListingInfo) this.tdscListingInfoDao.saveOrUpdate(tdscListingInfo);
		tdscListingApp.setListingId(tdscListingInfo.getListingId());
		this.tdscListingAppDao.saveOrUpdate(tdscListingApp);
		return tdscListingInfo.getListingId();
	}
	
	public TdscListingApp saveTdscListingApp(TdscListingApp tdscListingApp) {
		return (TdscListingApp)this.tdscListingAppDao.saveOrUpdate(tdscListingApp);
	}


	public List findNoticeAppListByNoticeIdList(List noticeIds) {
		
		TdscNoticeAppCondition condition = new TdscNoticeAppCondition();
		condition.setNoticeIdList(noticeIds);
		return this.tdscNoticeAppDao.findNoticeAppList(condition);
	}

	/**
	 * ���õؿ齻��״̬
	 * 
	 * @param noticeId
	 * @return
	 */
	public void saveBlockTranApp(String appId,String tradeStatus) {
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) this.tdscBlockTranAppDao.get(appId);
		tdscBlockTranApp.setTradeStatus(tradeStatus);
		this.tdscBlockTranAppDao.update(tdscBlockTranApp);
	}

	/**
	 * ��ѯ���淢���󣬽��׽���ǰ�ؿ�.
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findTradeNotice() {
		return tdscBlockPlanTableDao.findListStatusList();
	}
	
	public List findTradeNoticeNotStart() {
		return tdscBlockPlanTableDao.findTradeNoticeNotStart();
	}
	
	
	public List queryBidderAppListLikeAppId(String appId) {
		return tdscBidderAppDao.queryBidderAppListLikeAppId(appId);
	}

	public TdscBlockPlanTable getBlockPlanTableById(String planId) {
		return (TdscBlockPlanTable) tdscBlockPlanTableDao.get(planId);
	}
	
	public void updateBlockPlanTableStatus(String planId,boolean isFinish) {
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableDao.get(planId);
		tdscBlockPlanTable.setOnLineStatDate(new Timestamp(System.currentTimeMillis()));
		if(isFinish){
			tdscBlockPlanTable.setOnLineEndDate(new Timestamp(System.currentTimeMillis()));
		}
		tdscBlockPlanTableDao.update(tdscBlockPlanTable);
	}

	


	 /**
	  * �������,�������ݴ���
	  * @param noticeId ��������
	  */
	 public void tradeFinish(String noticeId) {
		 System.out.println("������ɺ�����ݴ���");
	 }
	 
	/**
	 * ��ѯ����ؿ���Ϣ.
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findBlockTranAppList(String planId) {
		return tdscBlockTranAppDao.findAppListByPlanId(planId);
	}
	
	/**
	 * ��ȡ���±�����Ϣ.
	 * @param appId
	 * @return
	 */
	public TdscListingInfo getTdscListingInfoByAppId(String appId) {
		 return tdscListingInfoDao.getTdscListingInfoByAppId(appId);
	}
	
	public TdscBidderApp getBidderAppByCertNo(String certNo) {
		return tdscBidderAppDao.getBidderAppByCertNo(certNo);
	}
	

	public TdscBidderView getYixiangNameLikeAppId(String appId) {
		return tdscBidderViewDao.getYixiangNameLikeAppId(appId);
	}
	
	public TdscBidderView getBidderViewByCertNo(String certNo) {
		return tdscBidderViewDao.getBidderViewByCertNo(certNo);
	}
	
	public List getBidderViewListByUserId(String userId) {
		return tdscBidderViewDao.getBidderViewListByUserId(userId);
	}
	
	public TdscBlockTranApp getTdscBlockTranAppById(String appId) {
		return (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
	}
	
	public TdscBlockInfo getBlockInfoByAppId(String blockId) {
		return this.tdscBlockInfoDao.findBlockInfo(blockId);
	}
	/**
	 * һ���ؿ齻�׽���.
	 * @param isAll �Ƿ������һ���
	 * @param tdscListingInfo ���׼�¼��
	 * @param tdscBlockInfo �ؿ��
	 * @param tdscBlockTranApp �ؿ������
	 */
	public  void  blockTradeFinish(boolean isAll,TdscListingInfo tdscListingInfo,TdscBlockInfo tdscBlockInfo,TdscBlockTranApp tdscBlockTranApp) {
		if(tdscListingInfo !=null){
			tdscListingInfoDao.saveOrUpdate(tdscListingInfo);
		}
		if(tdscBlockInfo !=null){
			tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		}
		if(tdscBlockTranApp !=null){
			tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
		}
		if(isAll){
			//���ý��׽�����ʶ
			TdscBlockPlanTable tdscBlockPlanTable = getBlockPlanTableById(tdscBlockTranApp.getPlanId());
			tdscBlockPlanTable.setOnLineEndDate(new Timestamp(System.currentTimeMillis()));
			//�����ʼΪ�գ����ÿ�ʼʱ��
			if(tdscBlockPlanTable.getOnLineStatDate() == null){
				tdscBlockPlanTable.setOnLineStatDate(new Timestamp(System.currentTimeMillis()));
			}
			tdscBlockPlanTableDao.update(tdscBlockPlanTable);
			//���������������
			pushTo18(tdscBlockTranApp.getNoticeId());
		}
	}
	
	/**
	 * �������Ƶ�17�ڵ�
	 * @param noticeId ��������
	 */
	public void pushTo17(String noticeId){
		//�ƶ�TdscApp
		pushTdscApp(noticeId,"17","1702");
	   //����TdscAppNodeStat
		pushTdscAppNodeStat(noticeId,"17");
		//����TdscAppNodeStat
		pushTdscOpnn(noticeId,"1702");
	}
	
	/**
	 * ��ѯ�깺appId�ĵؿ�.
	 * @param appId �ؿ�����
	 * @return
	 */
	public List queryBiddersByAppId(String appId) {
		return this.tdscBidderViewDao.queryBiddersByAppId(appId);
	}
	/**
	 * �������Ƶ�18�ڵ�
	 * @param noticeId ��������
	 */
	public void pushTo18(String noticeId){
		//�ƶ�TdscApp
		pushTdscApp(noticeId,"18","1801");
	   //����TdscAppNodeStat
		pushTdscAppNodeStat(noticeId,"18");
		//����TdscAppNodeStat
		pushTdscOpnn(noticeId,"1801");
	}
	
	private void pushTdscApp(String noticeId,String nodeId,String statusId){
		//�ƶ�TdscApp
		List appList = tdscAppDao.findAppListByNoticeId(noticeId);
		if(appList !=null){
			for(int i=0;i<appList.size();i++){
				TdscApp tdscApp = (TdscApp) appList.get(i);
				tdscApp.setNodeId(nodeId);
				tdscApp.setStatusId(statusId);
				tdscApp.setNodeDate(new Timestamp(System.currentTimeMillis()));
				tdscApp.setStatusDate(tdscApp.getNodeDate());
				tdscAppDao.update(tdscApp);
			}
		}
	}
	
	private void pushTdscOpnn(String noticeId,String actionId){
		List opnnList = tdscOpnnDao.findOpnnListByNoticeId(noticeId);
		if(opnnList !=null){
			for(int i=0;i<opnnList.size();i++){
				TdscOpnn tdscOpnn = (TdscOpnn) opnnList.get(i);
				tdscOpnn.setActionOrgan("1");
				tdscOpnn.setActionUser("����Ա");
				tdscOpnn.setActionUserId("tdsc");
				tdscOpnn.setAccDate(new Timestamp(System.currentTimeMillis()));
				tdscOpnn.setAccDate(tdscOpnn.getAccDate());
				tdscOpnn.setResultId("160102");
				tdscOpnn.setResultName("���ƽ������������");
				tdscOpnn.setIsOpt("01");
				tdscOpnnDao.update(tdscOpnn);
				
				TdscOpnn nextTdscOpnn = new  TdscOpnn();
				nextTdscOpnn.setAppId(tdscOpnn.getAppId());
				nextTdscOpnn.setIsOpt("00");
				nextTdscOpnn.setActionNum(new BigDecimal(tdscOpnn.getActionNum().intValue()+1));
				nextTdscOpnn.setActionId(actionId);
				nextTdscOpnn.setFirstDate(new Timestamp(System.currentTimeMillis()));
				nextTdscOpnn.setAccDate(nextTdscOpnn.getFirstDate());
				tdscOpnnDao.save(nextTdscOpnn);
			}
		}
	}
	
	private void pushTdscAppNodeStat(String noticeId,String nodeId){
		List appNodeInfoList = tdscAppNodeStatDao.findAppNodeInfoListByNoticeId(noticeId);
		if(appNodeInfoList !=null){
			for(int i=0;i<appNodeInfoList.size();i++){
				TdscAppNodeStat tdscAppNodeStat = (TdscAppNodeStat) appNodeInfoList.get(i);
				if("18".equals(tdscAppNodeStat.getNodeId()) && !"17".equals(nodeId)){
					tdscAppNodeStat.setNodeStat("01");
					tdscAppNodeStat.setStartDate(new Date());
				}else if("17".equals(tdscAppNodeStat.getNodeId())){
					if("17".equals(nodeId)){
						tdscAppNodeStat.setNodeStat("01");
					}else{
						tdscAppNodeStat.setNodeStat("02");
					}
					tdscAppNodeStat.setStartDate(new Date());
					tdscAppNodeStat.setEndDate(new Date());
				}else if("16".equals(tdscAppNodeStat.getNodeId())){
					tdscAppNodeStat.setNodeStat("02");
					tdscAppNodeStat.setEndDate(new Date());
				}

				tdscAppNodeStatDao.update(tdscAppNodeStat);
			}
		}
	}

	public List findPlanTableListInTrade() {
		return tdscBlockPlanTableDao.findPlanTableListInTrade();
	}

	public Map getMaxPriceMap(List curreBlockId) {
		Map result = new HashMap();
		for (int i =0 ; curreBlockId!= null && i < curreBlockId.size();i++) {
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp)curreBlockId.get(i);
			BigDecimal topPrice = null;
			if ("1".endsWith(tdscBlockTranApp.getTradeStatus())){
				// ������ ��ѯ������߼�
				topPrice = this.tdscListingAppDao.findTopPrice(tdscBlockTranApp.getAppId(), null, "22");
				if (topPrice == null)
					topPrice = this.tdscListingAppDao.findTopPrice(tdscBlockTranApp.getAppId(), null, "11");
			} else if ("2".endsWith(tdscBlockTranApp.getTradeStatus())){
				// �ȴ��е� ��ѯ������߼�
				topPrice = this.tdscListingAppDao.findTopPrice(tdscBlockTranApp.getAppId(), null, "11");
			} else if ("3".endsWith(tdscBlockTranApp.getTradeStatus())){
				// ������
				topPrice = tdscBlockTranApp.getResultPrice();
			} 
			result.put(tdscBlockTranApp.getAppId(), topPrice);
		}
		return result;
	}
	
	public TdscBlockPart getBlockPartByBlockId(String blockId) {
		List list = this.tdscBlockPartDao.getTdscBlockPartList(blockId);
		if (list != null && list.size() > 0) {
			return (TdscBlockPart) list.get(0);
		}
		return null;
	}
	
	/**
	 * ͨ���ؿ鹫����ȡ�þ�������Ϣ�б�
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findTdscBidderAppListByNoticeId(String noticeId) {
		List list = this.tdscBidderAppDao.findBidderAppByAppId(noticeId);
		return list;
	}
	
	/**
	 * �������ڱ������еĺ���
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListForme(String userId, String noticeId) {
		List list = this.tdscBidderAppDao.queryBidderAppListForme(userId, noticeId);
		return list;
	}
		
	public List findPlanTableListInPublicTrading() {
		return tdscBlockPlanTableDao.findPlanTableListInPublicTrading();
	}
	
	public List getTdscTradeViewAppByPlanId(String planId) {
		return tdscTradeViewDao.getTdscTradeViewAppByPlanId(planId);
	}
	
	public List findBidderAppListByNoticeId(String noticeId) {
		return tdscBidderAppDao.queryBidderAppListByNoticeId(noticeId);
	}	
	
	/**
	 * ȡ�����е��Գɽ������ĵĵؿ�
	 * @param noticeId
	 * @return
	 */
	public List queryBlockViewListTheEnd(String noticeId) {
		return this.tdscTradeViewDao.queryBlockViewListTheEnd(noticeId);
	}
	
	/**
	 * �������
	 * @param strBidderId
	 * @param strConNum
	 */
	public TdscBidderApp persistentBidderApp(TdscBidderApp tdscBidderApp) {
		return (TdscBidderApp)tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
	}
	
	/**
	 * �������
	 * @param strBidderId
	 * @param strConNum
	 */
	public void persistenBidder(TdscBidderApp tdscBidderApp, TdscBidderPersonApp bidderPersonApp) {
		tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
		bidderPersonApp.setBidderId(tdscBidderApp.getBidderId());
		tdscBidderPersonAppDao.saveOrUpdate(bidderPersonApp);
	}
	/**
	 * �������
	 * @param strBidderId
	 * @param strConNum
	 */
	public void saveSelectHaoPai(String strBidderId, String strConNum,String certNo){
		TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppDao.get(strBidderId);
		bidderApp.setConNum(strConNum);
		bidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
		if(StringUtils.isNotBlank(certNo)){
			bidderApp.setCertNo(certNo);
		}
		//if (StringUtils.isNotEmpty(certNo)) {
		//	bidderApp.setCertNo(certNo);
		//	bidderApp.setAcceptNo(certNo);
		//	bidderApp.setYktXh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿����
		//	bidderApp.setYktBh("jm" + certNo);// ȡ�����׿������ʸ�֤���Ŵ��潻�׿�оƬ��
		//}
		//tdscBidderAppDao.saveOrUpdate(bidderApp);
		this.saveSameNoticeHaoPai(bidderApp.getNoticeId(), bidderApp.getUserId(), strConNum);
		// ����������ˣ����䱨��ѡ����ƺ���Ҫ����������ؿ�����һ�ֹ��Ƽ�¼�����Ƽ۸�Ϊ��ʼ�ۣ�ʱ��Ϊ������ʼʱ��
		TdscBidderView tdscBidderView = getBidderAppById(strBidderId);
		//if ("1".equals(bidderApp.getIsPurposePerson())) {
		//	String purposeAppId = tdscBidderView.getPurposeAppId();
		//	insertListingAppOfPurposePerson(bidderApp, purposeAppId);
		//}
	}
	/**
	 * ����ͬһ������ͬ�����������ѽ��ɱ�֤����깺�ؿ�ĺ���
	 * @param noticeId
	 * @param userId
	 * @param certNo
	 */
	public void saveSameNoticeHaoPai(String noticeId,String userId,String strConNum){
		List list = this.tdscBidderAppDao.findSameNoticeBidderListByUserId(noticeId, userId);
		for(int i=0;i<list.size();i++){
			TdscBidderApp bidderApp = (TdscBidderApp)list.get(i);
			if(StringUtils.isNotBlank(bidderApp.getCertNo())){
				bidderApp.setConNum(strConNum);
				bidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
				if ("1".equals(bidderApp.getIsPurposePerson())) {
					String purposeAppId = bidderApp.getAppId();
					insertListingAppOfPurposePerson(bidderApp, purposeAppId);
				}
				tdscBidderAppDao.saveOrUpdate(bidderApp);
			}
		}
	}
	/**
	 * ���ͬһ����Ƿ������ͬ�ĺ���
	 * 
	 * @param appId
	 * @return
	 */
	public boolean checkSameHaoPaiByAppId(String appId,String conNum){
		List list = tdscBidderAppDao.findBidderAppListByAppId(appId);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				TdscBidderApp bidderApp = (TdscBidderApp)list.get(i);
				if(StringUtils.isNotBlank(bidderApp.getConNum())&&bidderApp.getConNum().equals(conNum)){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * ��ȡ�ʸ�֤����
	 * @return
	 */
	public String generateCertNo() {
		String nowMonth = DateUtil.date2String(new java.util.Date(), "yyyyMM");
		Long certNo = idSpringManager.getIncrementId("CertNo" + nowMonth);
		return nowMonth + StringUtils.leftPad(certNo + "", 4, '0');
	}
	
	
	public TdscBidderApp getPurposeBidderAppByAppId(String appId) {
		return tdscBidderAppDao.getPurposeBidderAppByAppId(appId);
	}
	
	/**
	 * ���ݾ���������ȡ�ö�Ӧ�ľ�����Ϣ
	 * @param bidderId
	 * @return
	 */
	public TdscBidderView getBidderAppById(String bidderId) {
		return (TdscBidderView)tdscBidderViewDao.getBidderView(bidderId);
	}
	
	/**
	 * �������˽����״ι���
	 * @param tdscBidderView
	 * @param purposeAppId
	 */
	public void insertListingAppOfPurposePerson(TdscBidderApp tdscBidderView, String purposeAppId) {
		String[] appIds = purposeAppId.split(",");// �þ����˵�����ؿ�ID
		for (int i=0; null!=appIds && i < appIds.length; i++) {
			TdscBlockTranApp tdscBlockTranApp= tdscBlockTranAppDao.findTdscBlockTranAppInfo(appIds[i]);
			
			TdscBlockPlanTableCondition tdscBlockPlanTableCondition = new TdscBlockPlanTableCondition();
			tdscBlockPlanTableCondition.setPlanId(tdscBlockTranApp.getPlanId());
			TdscBlockPlanTable tdscBlockPlanTable = tdscBlockPlanTableDao.findBlockPlanTableInfo(tdscBlockPlanTableCondition);
			
			TdscListingInfo tdscListingInfo = null;			
			tdscListingInfo = tdscListingInfoDao.getTdscListingInfoByAppId(appIds[i]);// ���Ҹõؿ��Ƿ���Ʊ��۹�			
			if (tdscListingInfo == null) {
				tdscListingInfo = new TdscListingInfo();
				tdscListingInfo.setAppId(appIds[i]);
				tdscListingInfo.setCurrRound(new BigDecimal(1));
				tdscListingInfo.setListDate(tdscBlockPlanTable.getIssueStartDate());
				tdscListingInfo.setYktXh(tdscBidderView.getYktXh());
				tdscListingInfo.setListCert(tdscBidderView.getCertNo());
				tdscListingInfo.setListNo(tdscBidderView.getConNum());
				tdscListingInfo.setCurrPrice(tdscBlockTranApp.getInitPrice());
				tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.save(tdscListingInfo);
			}

			TdscListingApp tdscListingApp = tdscListingAppDao.getListingAppOfPurposePerson(appIds[i], tdscBidderView.getCertNo());
			
			if (tdscListingApp == null) {// ������û�ж���������������ؿ���й��ƣ��������Ƽ�¼�����Ƽ۸�Ϊ��ʼ�ۣ�����ʱ��Ϊ������ʼʱ��
				TdscListingApp listingApp = new TdscListingApp();
				listingApp.setListingId(tdscListingInfo.getListingId());
				listingApp.setListingSer(new BigDecimal(1));
				listingApp.setPriceType("11");
				listingApp.setPriceNum(new BigDecimal(1));
				listingApp.setListCert(tdscBidderView.getCertNo());
				listingApp.setListNo(tdscBidderView.getConNum());
				listingApp.setListDate(tdscBlockPlanTable.getIssueStartDate());
				listingApp.setYktXh(tdscBidderView.getYktXh());
				listingApp.setAppId(appIds[i]);
				listingApp.setListPrice(tdscBlockTranApp.getInitPrice());

				tdscListingAppDao.save(listingApp);
			}
		}
	}
	
	public List queryBlockIdListByPlanId(String planId) {
		return tdscBlockTranAppDao.queryBlockIdListByPlanId(planId);
	}

	/**
	 * ����blockId��ѯ�ӵؿ���Ϣ
	 * 
	 * @param blockId
	 * @return
	 */
	public List getTdscBlockPartList(String blockId) {
		return (List) tdscBlockPartDao.getTdscBlockPartList(blockId);
	}
	
	/**
	 * ͨ��bidderId���Ҷ�Ӧ��personapp
	 * @param bidderId
	 * @return
	 */
	public TdscBidderPersonApp getBidderPersonAppByBidderId(String bidderId) {
		TdscBidderPersonApp bidderPersonApp = tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
		return bidderPersonApp;
	}
	
	/**
	 * ���깺�����й�������еؿ�
	 * 
	 * @param planIdList
	 * @param appIds
	 * @return
	 */
	public List queryMyTradeNoticeBlock(List planIdList, List appIds) {
		if (planIdList == null && appIds == null) return null;
		if (planIdList != null && planIdList.size() < 1) return null;
		if (appIds != null && appIds.size() < 1) return null;
		List result = tdscTradeViewDao.queryMyTradeNoticeBlock(planIdList, appIds);
		return result;
	}
	
	/**
	 * 
	 * @param userId
	 * @param noticeId
	 * @return
	 */
	public List getCertNoList(String userId, String noticeId) {
		List list = tdscBidderAppDao.findCertNoList(userId, noticeId);
		return list;
	}
	public TdscBidderView getTdscBidderViewByAppId(String appId, String userId) {
		List list = tdscBidderViewDao.getBidderViewByAppId(appId, userId);
		if (list != null && list.size() > 0) {
			return (TdscBidderView)list.get(0);
		}
		return null;
	}

	public List findValidBidderList(String appId) {
		return tdscBidderAppDao.findValidBidderList(appId);
	}
	
	/**
	 * ���������֤Ψһ��ʶɸѡ�ҵ�������ؿ���ʱ���ھ�������ʼʱ��֮ǰ(���淢����----���ƿ�ʼʱ��֮ǰ)��
	 * @param blockViewList
	 * @param userId �û�ID
	 * @return
	 */
	public List fixMyYiXiangList(String userId){
		
		
		PersonInfo personInfo = personInfoDAO.getPersonInfo(userId); 
		//�������д��ھ�����������ڵ�ĵؿ�����
		List list = this.tdscBlockTranAppDao.findYxBlockListByOrgNo(personInfo.getOrgNo());
		return list;
	}
	/**
	 * ȡ�þ�������Ϣ
	 * @param bidderId
	 * @return
	 */
	public PersonInfo getPersonInfo(String bidderId) {
		if (StringUtils.isEmpty(bidderId)) return null;
		return personInfoDAO.getPersonInfo(bidderId);
	}

	/**
	 * ������������к��е׼�ȷ�Ͻ׶εĵؿ��б�
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findConfirmBlockList(String noticeId) {
		List blockList = tdscTradeViewDao.queryConfirmBlockList(noticeId);
		return blockList;
	}
	/**
	 * ���ݺ����뱨�����Ͳ��ҵؿ�ı�����Ϣ
	 * @param appId
	 * @param conNum
	 * @param priceType
	 * @return
	 */
	public List findListingAppListByConNum(String appId,String conNum, String priceType){
		return this.tdscListingAppDao.findListingAppListByConNum(appId, conNum, priceType);
	}
}
