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
	//推动流程
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
	 * 根据用户的资格证书编号取得竞买信息列表
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
	 * 根据用户的资格证书编号取得竞买信息列表
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
	 * 通过appId取得对应该地块信息最高报价，以及
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
	 * 取得报价列表 按照倒序排列
	 * 
	 * @param appId 地块编号
	 * @param certNo 竞买人编号
	 * @param priceType 竞价类型 1 挂牌 2 限时竞价
	 * @return
	 */
	public List findListingAppList(String appId, String certNo, String priceType) {
		List listingAppList = this.tdscListingAppDao.getListingAppListByAppId( appId, certNo, priceType);
		return listingAppList;
	}
	
	
	/**
	 * 通过公告编号去的对应的地块信息列表，并且按照排序方式从小到大排列
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
	 * 根据appId取得该地块对应的挂牌信息
	 * @param appId
	 * @return
	 */
	public List getListingAppListByAppId(String appId, String priceType) {
		if (StringUtils.isEmpty(appId)) return null;
		List list = this.tdscListingAppDao.getListingAppListByAppId(appId,null, priceType);
		return list;
	}

	/**
	 * 取得公告信息
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
	 * 设置地块交易状态
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
	 * 查询公告发布后，交易结束前地块.
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
	  * 交易完成,进行数据处理
	  * @param noticeId 公告主键
	  */
	 public void tradeFinish(String noticeId) {
		 System.out.println("交易完成后的数据处理");
	 }
	 
	/**
	 * 查询申请地块信息.
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findBlockTranAppList(String planId) {
		return tdscBlockTranAppDao.findAppListByPlanId(planId);
	}
	
	/**
	 * 获取最新报价信息.
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
	 * 一个地块交易结束.
	 * @param isAll 是否是最后一块地
	 * @param tdscListingInfo 交易记录表
	 * @param tdscBlockInfo 地块表
	 * @param tdscBlockTranApp 地块申请表
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
			//设置交易结束标识
			TdscBlockPlanTable tdscBlockPlanTable = getBlockPlanTableById(tdscBlockTranApp.getPlanId());
			tdscBlockPlanTable.setOnLineEndDate(new Timestamp(System.currentTimeMillis()));
			//如果开始为空，设置开始时间
			if(tdscBlockPlanTable.getOnLineStatDate() == null){
				tdscBlockPlanTable.setOnLineStatDate(new Timestamp(System.currentTimeMillis()));
			}
			tdscBlockPlanTableDao.update(tdscBlockPlanTable);
			//如果结束，推流程
			pushTo18(tdscBlockTranApp.getNoticeId());
		}
	}
	
	/**
	 * 将流程推到17节点
	 * @param noticeId 公告主键
	 */
	public void pushTo17(String noticeId){
		//推动TdscApp
		pushTdscApp(noticeId,"17","1702");
	   //处理TdscAppNodeStat
		pushTdscAppNodeStat(noticeId,"17");
		//处理TdscAppNodeStat
		pushTdscOpnn(noticeId,"1702");
	}
	
	/**
	 * 查询申购appId的地块.
	 * @param appId 地块主键
	 * @return
	 */
	public List queryBiddersByAppId(String appId) {
		return this.tdscBidderViewDao.queryBiddersByAppId(appId);
	}
	/**
	 * 将流程推到18节点
	 * @param noticeId 公告主键
	 */
	public void pushTo18(String noticeId){
		//推动TdscApp
		pushTdscApp(noticeId,"18","1801");
	   //处理TdscAppNodeStat
		pushTdscAppNodeStat(noticeId,"18");
		//处理TdscAppNodeStat
		pushTdscOpnn(noticeId,"1801");
	}
	
	private void pushTdscApp(String noticeId,String nodeId,String statusId){
		//推动TdscApp
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
				tdscOpnn.setActionUser("管理员");
				tdscOpnn.setActionUserId("tdsc");
				tdscOpnn.setAccDate(new Timestamp(System.currentTimeMillis()));
				tdscOpnn.setAccDate(tdscOpnn.getAccDate());
				tdscOpnn.setResultId("160102");
				tdscOpnn.setResultName("挂牌结束，换领号牌");
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
				// 竞价中 查询竞价最高价
				topPrice = this.tdscListingAppDao.findTopPrice(tdscBlockTranApp.getAppId(), null, "22");
				if (topPrice == null)
					topPrice = this.tdscListingAppDao.findTopPrice(tdscBlockTranApp.getAppId(), null, "11");
			} else if ("2".endsWith(tdscBlockTranApp.getTradeStatus())){
				// 等待中的 查询挂牌最高价
				topPrice = this.tdscListingAppDao.findTopPrice(tdscBlockTranApp.getAppId(), null, "11");
			} else if ("3".endsWith(tdscBlockTranApp.getTradeStatus())){
				// 结束的
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
	 * 通过地块公告编号取得竞买人信息列表
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findTdscBidderAppListByNoticeId(String noticeId) {
		List list = this.tdscBidderAppDao.findBidderAppByAppId(noticeId);
		return list;
	}
	
	/**
	 * 竞买人在本公告中的号牌
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
	 * 取得所有的以成交和流拍的地块
	 * @param noticeId
	 * @return
	 */
	public List queryBlockViewListTheEnd(String noticeId) {
		return this.tdscTradeViewDao.queryBlockViewListTheEnd(noticeId);
	}
	
	/**
	 * 保存号牌
	 * @param strBidderId
	 * @param strConNum
	 */
	public TdscBidderApp persistentBidderApp(TdscBidderApp tdscBidderApp) {
		return (TdscBidderApp)tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
	}
	
	/**
	 * 保存号牌
	 * @param strBidderId
	 * @param strConNum
	 */
	public void persistenBidder(TdscBidderApp tdscBidderApp, TdscBidderPersonApp bidderPersonApp) {
		tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
		bidderPersonApp.setBidderId(tdscBidderApp.getBidderId());
		tdscBidderPersonAppDao.saveOrUpdate(bidderPersonApp);
	}
	/**
	 * 保存号牌
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
		//	bidderApp.setYktXh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡编号
		//	bidderApp.setYktBh("jm" + certNo);// 取消交易卡，用资格证书编号代替交易卡芯片号
		//}
		//tdscBidderAppDao.saveOrUpdate(bidderApp);
		this.saveSameNoticeHaoPai(bidderApp.getNoticeId(), bidderApp.getUserId(), strConNum);
		// 如果是意向人，在其报名选择号牌后，需要对他的意向地块插入第一轮挂牌记录，挂牌价格为起始价，时间为公告起始时间
		TdscBidderView tdscBidderView = getBidderAppById(strBidderId);
		//if ("1".equals(bidderApp.getIsPurposePerson())) {
		//	String purposeAppId = tdscBidderView.getPurposeAppId();
		//	insertListingAppOfPurposePerson(bidderApp, purposeAppId);
		//}
	}
	/**
	 * 保存同一个人相同公告内所有已缴纳保证金的申购地块的号牌
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
	 * 检测同一块地是否存在相同的号牌
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
	 * 获取资格证书编号
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
	 * 根据竞买申请编号取得对应的竞买信息
	 * @param bidderId
	 * @return
	 */
	public TdscBidderView getBidderAppById(String bidderId) {
		return (TdscBidderView)tdscBidderViewDao.getBidderView(bidderId);
	}
	
	/**
	 * 对意向人进行首次挂牌
	 * @param tdscBidderView
	 * @param purposeAppId
	 */
	public void insertListingAppOfPurposePerson(TdscBidderApp tdscBidderView, String purposeAppId) {
		String[] appIds = purposeAppId.split(",");// 该竞买人的意向地块ID
		for (int i=0; null!=appIds && i < appIds.length; i++) {
			TdscBlockTranApp tdscBlockTranApp= tdscBlockTranAppDao.findTdscBlockTranAppInfo(appIds[i]);
			
			TdscBlockPlanTableCondition tdscBlockPlanTableCondition = new TdscBlockPlanTableCondition();
			tdscBlockPlanTableCondition.setPlanId(tdscBlockTranApp.getPlanId());
			TdscBlockPlanTable tdscBlockPlanTable = tdscBlockPlanTableDao.findBlockPlanTableInfo(tdscBlockPlanTableCondition);
			
			TdscListingInfo tdscListingInfo = null;			
			tdscListingInfo = tdscListingInfoDao.getTdscListingInfoByAppId(appIds[i]);// 查找该地块是否挂牌报价过			
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
			
			if (tdscListingApp == null) {// 意向人没有对他购买的这个意向地块进行挂牌，则插入挂牌记录，挂牌价格为起始价，挂牌时间为公告起始时间
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
	 * 根据blockId查询子地块信息
	 * 
	 * @param blockId
	 * @return
	 */
	public List getTdscBlockPartList(String blockId) {
		return (List) tdscBlockPartDao.getTdscBlockPartList(blockId);
	}
	
	/**
	 * 通过bidderId查找对应的personapp
	 * @param bidderId
	 * @return
	 */
	public TdscBidderPersonApp getBidderPersonAppByBidderId(String bidderId) {
		TdscBidderPersonApp bidderPersonApp = tdscBidderPersonAppDao.getOneBidderByBidderId(bidderId);
		return bidderPersonApp;
	}
	
	/**
	 * 我申购的所有公告的所有地块
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
	 * 根据身份认证唯一标识筛选我的有意向地块且时间在竞买受理开始时间之前(公告发布后----挂牌开始时间之前)。
	 * @param blockViewList
	 * @param userId 用户ID
	 * @return
	 */
	public List fixMyYiXiangList(String userId){
		
		
		PersonInfo personInfo = personInfoDAO.getPersonInfo(userId); 
		//查找所有处于竞买受理申请节点的地块数据
		List list = this.tdscBlockTranAppDao.findYxBlockListByOrgNo(personInfo.getOrgNo());
		return list;
	}
	/**
	 * 取得竞买人信息
	 * @param bidderId
	 * @return
	 */
	public PersonInfo getPersonInfo(String bidderId) {
		if (StringUtils.isEmpty(bidderId)) return null;
		return personInfoDAO.getPersonInfo(bidderId);
	}

	/**
	 * 查找这个公告中含有底价确认阶段的地块列表
	 * 
	 * @param noticeId
	 * @return
	 */
	public List findConfirmBlockList(String noticeId) {
		List blockList = tdscTradeViewDao.queryConfirmBlockList(noticeId);
		return blockList;
	}
	/**
	 * 依据号牌与报价类型查找地块的报价信息
	 * @param appId
	 * @param conNum
	 * @param priceType
	 * @return
	 */
	public List findListingAppListByConNum(String appId,String conNum, String priceType){
		return this.tdscListingAppDao.findListingAppListByConNum(appId, conNum, priceType);
	}
}
