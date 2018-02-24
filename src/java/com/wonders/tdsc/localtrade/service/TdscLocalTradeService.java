package com.wonders.tdsc.localtrade.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.util.DateUtil;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.service.TdscBidderAppService;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscNoticeAppDao;
import com.wonders.tdsc.blockwork.service.TdscBlockInfoService;
import com.wonders.tdsc.bo.BlockAuctionInfo;
import com.wonders.tdsc.bo.BlockAuctionPrice;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscAuctionApp;
import com.wonders.tdsc.bo.TdscAuctionInfo;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.TdscTenderApp;
import com.wonders.tdsc.bo.TdscTenderInfo;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscBlockInfoCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscListingAppCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.localtrade.dao.TdscAuctionAppDao;
import com.wonders.tdsc.localtrade.dao.TdscAuctionInfoDao;
import com.wonders.tdsc.localtrade.dao.TdscListingAppDao;
import com.wonders.tdsc.localtrade.dao.TdscListingInfoDao;
import com.wonders.tdsc.localtrade.dao.TdscTenderAppDao;
import com.wonders.tdsc.localtrade.dao.TdscTenderInfoDao;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.tdsc.thirdpart.castor.util.CastorFactory;
import com.wonders.util.PropertiesUtil;

public class TdscLocalTradeService extends BaseSpringManagerImpl {

	private static Logger logger = Logger.getLogger(TdscLocalTradeService.class);

	private TdscBlockInfoDao tdscBlockInfoDao;

	private TdscBidderAppService tdscBidderAppService;

	private TdscListingInfoDao tdscListingInfoDao;

	private TdscListingAppDao tdscListingAppDao;

	private TdscAuctionInfoDao tdscAuctionInfoDao;

	private CommonQueryService commonQueryService;

	private TdscBlockInfoService tdscBlockInfoService;

	private TdscAuctionAppDao tdscAuctionAppDao;

	private TdscTenderInfoDao tdscTenderInfoDao;

	private TdscTenderAppDao tdscTenderAppDao;

	private TdscBlockPlanTableDao tdscBlockPlanTableDao;

	private TdscBidderPersonAppDao tdscBidderPersonAppDao;

	private TdscBlockTranAppDao tdscBlockTranAppDao;

	private AppFlowService appFlowService;

	private TdscNoticeAppDao tdscNoticeAppDao;

	private TdscBidderAppDao tdscBidderAppDao;

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscNoticeAppDao(TdscNoticeAppDao tdscNoticeAppDao) {
		this.tdscNoticeAppDao = tdscNoticeAppDao;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setTdscAuctionInfoDao(TdscAuctionInfoDao tdscAuctionInfoDao) {
		this.tdscAuctionInfoDao = tdscAuctionInfoDao;
	}

	public void setTdscBidderAppService(TdscBidderAppService tdscBidderAppService) {
		this.tdscBidderAppService = tdscBidderAppService;
	}

	public void setTdscListingInfoDao(TdscListingInfoDao tdscListingInfoDao) {
		this.tdscListingInfoDao = tdscListingInfoDao;
	}

	public void setTdscListingAppDao(TdscListingAppDao tdscListingAppDao) {
		this.tdscListingAppDao = tdscListingAppDao;
	}

	public void setTdscBlockInfoService(TdscBlockInfoService tdscBlockInfoService) {
		this.tdscBlockInfoService = tdscBlockInfoService;
	}

	public void setTdscAuctionAppDao(TdscAuctionAppDao tdscAuctionAppDao) {
		this.tdscAuctionAppDao = tdscAuctionAppDao;
	}

	public void setTdscTenderInfoDao(TdscTenderInfoDao tdscTenderInfoDao) {
		this.tdscTenderInfoDao = tdscTenderInfoDao;
	}

	public void setTdscTenderAppDao(TdscTenderAppDao tdscTenderAppDao) {
		this.tdscTenderAppDao = tdscTenderAppDao;
	}

	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}

	public void setTdscBidderPersonAppDao(TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
		this.tdscBlockInfoDao = tdscBlockInfoDao;
	}

	/**
	 * 查询所有竞买信息
	 * 
	 * @param appId
	 * @return
	 */
	public List queryTdscBidderAppList(String appId) {
		List tdscBidderAppList = this.tdscBidderAppService.queryBidderAppList(appId);
		return tdscBidderAppList;
	}

	/**
	 * 查询所有机审合格竞买信息
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListSrc(String appId) {
		List tdscBidderAppList = this.tdscBidderAppService.queryBidderListSrc(appId);
		return tdscBidderAppList;
	}

	/**
	 * 保存换取拍卖号牌的记录
	 * 
	 * @param vendueCardMap
	 */
	public void saveTakeCardBidder(Map vendueCardMap) {
		String certNo = (String) vendueCardMap.get("certNo");// 原 资格证书编号 ，现传
																// 一卡通编号 的值
		String cardNo = (String) vendueCardMap.get("cardNo");
		Timestamp takeCardTime = new Timestamp(System.currentTimeMillis());
		TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getBidderAppByYktBh(certNo);
		// TdscBidderApp tdscBidderApp =
		// (TdscBidderApp)queryBidderAppInfo(certNo);
		if (null != tdscBidderApp) {
			tdscBidderApp.setConNum(cardNo);
			tdscBidderApp.setConTime(takeCardTime);
			this.tdscBidderAppService.updateTdscBidderApp(tdscBidderApp);

			// if(tdscBidderApp.getCertNo() != null){
			// //TdscListingInfo tdscListingInfo
			// =(TdscListingInfo)tdscListingInfoDao.getTdscListingInfoByAppId(tdscBidderApp.getAppId());
			//
			// List listingAppList =
			// (List)tdscListingAppDao.queryByListCert(tdscBidderApp.getCertNo());
			//
			// if(listingAppList!=null && listingAppList.size()>0){
			// for(int i=0;i<listingAppList.size();i++){
			// TdscListingApp tdscListingApp =
			// (TdscListingApp)listingAppList.get(i);
			// tdscListingApp.setListNo(cardNo);
			// tdscListingAppDao.update(tdscListingApp);
			// }
			// }
			// }

		}

	}

	/**
	 * 通过appId和号牌号码获得一个竞买信息
	 * 
	 * @param cardNo
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppidCardNo(String appId, String cardNo) {
		return this.tdscBidderAppService.getBidderAppByAppIdConNo(appId, cardNo);
	}

	/**
	 * 通过appId和一卡通序号获得一个竞买信息
	 * 
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppidYktXh(String appId, String yktXh) {
		return this.tdscBidderAppService.getBidderAppByAppIdYktXh(appId, yktXh);
	}

	/**
	 * 通过appId和资格证书编号获得一个竞买信息
	 * 
	 * @param appId
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppidCertNo(String appId, String certNo) {
		return this.tdscBidderAppService.getBidderAppByAppIdCertNo(appId, certNo);
	}

	/**
	 * 
	 * @param bidderId
	 * @return
	 */
	public List queryTdscBidderPersonAppList(String bidderId) {
		return this.tdscBidderAppService.queryBidderPersonList(bidderId);
	}

	/**
	 * 通过APPID找到一共有多少人参与了拍卖,即有多少人换领了号牌
	 * 
	 * @param appId
	 * @return
	 */
	public List queryJoinAuctionList(String appId) {
		return this.tdscBidderAppService.queryJoinAuctionList(appId);
	}

	/**
	 * 查出某一竞买人的挂牌记录
	 * 
	 * @param listingAppId
	 * @return
	 */
	public List queryListingAppInfo(String listingId, String listCert) {
		List listingAppList = this.tdscListingAppDao.findListingAppInfo(listingId, listCert);
		return listingAppList;
	}

	/**
	 * 分页列表
	 * 
	 * @param condition
	 * @return
	 */
	public PageList findPageList(TdscListingAppCondition condition) {
		return tdscListingAppDao.findPageList(condition);
	}

	public void saveTdscAuctionInfo(TdscAuctionInfo tdscAuctionInfo) {
		this.tdscAuctionInfoDao.save(tdscAuctionInfo);
	}

	/**
	 * 查询最高价格
	 * 
	 * @param listingId
	 * @return
	 */
	public BigDecimal queryTopPrice(String listingId) {
		BigDecimal topPrice = tdscListingAppDao.findTopPrice(listingId);
		return topPrice;
	}

	/**
	 * 保存最高价格到挂牌信息表
	 * 
	 * @param topPrice
	 */
	public void saveTopPrice(BigDecimal topPrice) {
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		tdscListingInfo.setResultPrice(topPrice);
		tdscListingInfoDao.save(tdscListingInfo);
	}

	/**
	 * 根据业务Id查出挂牌会Id
	 * 
	 * @param appId
	 * @return
	 */
	public String queryListingId(String appId) {
		String listingId = tdscListingInfoDao.findListingId(appId);
		return listingId;
	}

	public TdscListingInfo findListingIdByAppIdAndYktXh(String appId, String yktXh) {
		TdscListingInfo tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.findListingIdByAppIdAndYktXh(appId, yktXh);
		return tdscListingInfo;
	}

	/**
	 * 查出竞价证书号
	 * 
	 * @param listingId
	 * @return
	 */
	public String queryListCert(String listingId) {
		String listCert = this.tdscListingAppDao.findListCertByListingId(listingId);
		return listCert;
	}

	/**
	 * 查出挂牌信息表的主键listingAppId
	 * 
	 * @param listingId
	 * @param listCert
	 * @return
	 */
	public String queryListingAppId(String listingId, String listCert) {
		String listingAppId = this.tdscListingAppDao.findListingAppId(listingId, listCert);
		return listingAppId;
	}

	/**
	 * 查询当前挂牌次数
	 * 
	 * @param listingId
	 * @return
	 */
	public BigDecimal queryTimes(String listingId) {
		List timesList = (List) this.tdscListingAppDao.get(listingId);
		BigDecimal topTimes = new BigDecimal(timesList.size());
		return topTimes;
	}

	/**
	 * 
	 */
	public TdscAuctionInfo getTdscAuctionInfoByAppId(String appId) {
		TdscAuctionInfo tdscAuctionInfo = this.tdscAuctionInfoDao.getTdscAuctionInfoByAppId(appId);
		return tdscAuctionInfo;
	}

	/**
	 * 将交易信息保存进TDSC_BLOCK_TRAN_APP,TDSC_BLOCK_INFO表
	 */
	public void modifyVendueInfo(String appId) {
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.getTdscBlockTranAppByAppId(appId);
	}

	/**
	 * 更新挂牌信息表中的挂牌记录
	 * 
	 * @param listCert
	 * @param listingPrice
	 */
	public void modifyListingPrice(String listCert, String listingPrice) {
		// 根据证书编号取得挂牌会Id
		TdscListingApp tdscListingApp = new TdscListingApp();
		tdscListingApp.setListCert(listCert);
		tdscListingApp = (TdscListingApp) tdscListingAppDao.get(tdscListingApp);
		String listingId = tdscListingApp.getListingId();
		// 根据挂牌会Id取得对应地块挂牌信息
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		tdscListingInfo.setListingId(listingId);
		tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.get(tdscListingInfo);
		// 修改价格
		tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
		// 修改挂牌次数
		tdscListingInfo.setCurrRound(tdscListingApp.getPriceNum());
		// 修改时间
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		tdscListingInfo.setListDate(nowTime);
		// 完成一次挂牌信息的修改
		tdscListingInfoDao.save(tdscListingInfo);

	}

	/**
	 * 保存首次挂牌的挂牌信息
	 * 
	 * @param tdscListingInfo
	 */
	public TdscListingInfo saveListingInfo(TdscListingInfo tdscListingInfo) {
		TdscListingInfo rettdscListingInfo = (TdscListingInfo) tdscListingInfoDao.save(tdscListingInfo);
		return rettdscListingInfo;
	}

	/**
	 * 保存首次挂牌的挂牌信息
	 * 
	 * @param tdscListingInfo
	 */
	public void endListAndSaveOpnn(TdscListingInfo tdscListingInfo, TdscAppFlow tdscAppFlow) {
		tdscListingInfoDao.save(tdscListingInfo);

		try {
			appFlowService.saveOpnn(tdscAppFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存首次挂牌的挂牌信息 TdscListingInfo TdscListingInfo两张表
	 * 
	 * @param tdscListingInfo
	 * @param tdscListingApp
	 * @return
	 */
	public TdscListingInfo saveListingInfoAndApp(TdscListingInfo tdscListingInfo, TdscListingApp tdscListingApp) {
		TdscListingInfo rettdscListingInfo = (TdscListingInfo) tdscListingInfoDao.save(tdscListingInfo);

		tdscListingApp.setListingId(rettdscListingInfo.getListingId());
		tdscListingApp.setListDate(rettdscListingInfo.getListDate());
		tdscListingApp.setYktXh(rettdscListingInfo.getYktXh());
		tdscListingApp.setListingSer(tdscListingInfo.getCurrRound());
		tdscListingApp.setPriceNum(tdscListingInfo.getCurrRound());
		tdscListingAppDao.save(tdscListingApp);

		return rettdscListingInfo;
	}

	/**
	 * 更新挂牌会信息表
	 * 
	 * @param tdscListingInfo
	 */
	public void modifyListingInfo(TdscListingInfo tdscListingInfo) {
		if ("".equals(tdscListingInfo.getListingId()) || tdscListingInfo.getListingId() == null)
			tdscListingInfoDao.save(tdscListingInfo);
		else
			tdscListingInfoDao.update(tdscListingInfo);
	}

	/**
	 * 更新挂牌会信息表 TdscListingInfo TdscListingInfo两张表
	 * 
	 * @param tdscListingInfo
	 * @param tdscListingApp
	 */
	public void modifyListingInfoAndApp(TdscListingInfo tdscListingInfo, TdscListingApp tdscListingApp) {
		tdscListingInfoDao.update(tdscListingInfo);
		tdscListingApp.setListingId(tdscListingInfo.getListingId());
		tdscListingApp.setListDate(tdscListingInfo.getListDate());
		tdscListingApp.setYktXh(tdscListingInfo.getYktXh());
		tdscListingApp.setListingSer(tdscListingInfo.getCurrRound());
		tdscListingAppDao.save(tdscListingApp);
	}

	/**
	 * 查找挂牌会Id对应的挂牌信息
	 * 
	 * @param listingId
	 * @return
	 */
	public TdscListingInfo getListingInfo(String listingId) {
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.get(listingId);
		return tdscListingInfo;
	}

	/**
	 * 查找招标会Id对应的招标信息
	 * 
	 * @param tenderId
	 * @return
	 */
	public TdscTenderInfo getTdscTenderInfo(String tenderId) {
		TdscTenderInfo tdscTenderInfo = new TdscTenderInfo();
		tdscTenderInfo = (TdscTenderInfo) this.tdscTenderInfoDao.get(tenderId);
		return tdscTenderInfo;
	}

	/**
	 * 在Listing_App表中增加一次挂牌记录
	 * 
	 * @param tdscListingApp
	 */
	public void saveToListingApp(TdscListingApp tdscListingApp) {
		tdscListingAppDao.save(tdscListingApp);
	}

	/**
	 * 根据APPID查找TDSCLISTINGINFO对象
	 */
	public TdscListingInfo getTdscListingInfoByAppId(String appId) {
		return this.tdscListingInfoDao.getTdscListingInfoByAppId(appId);
	}

	/**
	 * 通过listingId 查找tdscListingApp的列表
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppListByListingId(String listingId) {
		return this.tdscListingAppDao.findTdscListingAppListByListingId(listingId);
	}

	/**
	 * 通过listingId和priceType查找tdscListingApp的列表
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppListByListingId(String listingId, String priceType) {
		return this.tdscListingAppDao.findTdscListingAppListByListingId(listingId, priceType);
	}

	/**
	 * 通过listingId 查找tdscListingApp表中所有现场竞价的列表
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppLocalSenseList(String listingId) {
		return this.tdscListingAppDao.findTdscListingAppLocalSenseList(listingId);
	}

	/**
	 * 通过listingId 查找tdscListingApp表中所有 过程录入流程 中保存的数据
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppInGCLR(String listingId) {
		return this.tdscListingAppDao.queryTdscListingAppInGCLR(listingId);
	}

	public List saveTradeProcess(Map vendueAppMap) {
		// 保存方法采用全部删除,再将页面提交的表单全部保存

		String appId = (String) vendueAppMap.get("appId");
		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppId(appId);
		TdscBlockAppView tdscBlockAppView = this.commonQueryService.getTdscBlockAppView(condition);
		String[] serial = (String[]) vendueAppMap.get("serial");
		String[] cardNo = (String[]) vendueAppMap.get("cardNo");
		String[] price = (String[]) vendueAppMap.get("price");
		if (tdscBlockAppView.getTransferMode().equals("3103")) {
			TdscAuctionInfo tdscAuctionInfo = this.getTdscAuctionInfoByAppId(appId);
			if (tdscAuctionInfo == null) {
				tdscAuctionInfo = new TdscAuctionInfo();
				tdscAuctionInfo.setAppId(appId);
				tdscAuctionInfo.setAuctionDate(new Date(System.currentTimeMillis()));
				tdscAuctionInfo = (TdscAuctionInfo) tdscAuctionInfoDao.saveOrUpdate(tdscAuctionInfo);
			}
			// 找出所有的tdscAuctionApp,删除
			List tdscAuctionAppOldList = this.tdscAuctionAppDao.findTdscAuctionAppListByAppId(appId);
			for (int j = 0; j < tdscAuctionAppOldList.size(); j++) {
				TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) tdscAuctionAppOldList.get(j);
				this.tdscAuctionAppDao.delete(tdscAuctionApp);
			}
			// 删除完毕,开始添加
			List tdscAuctionAppList = new ArrayList();
			if (serial != null && serial.length > 0) {
				for (int i = 0; i < serial.length; i++) {
					TdscAuctionApp tdscAuctionApp = new TdscAuctionApp();
					tdscAuctionApp.setAuctionId(tdscAuctionInfo.getAuctionId());
					if (StringUtils.isNumeric(serial[i]))
						tdscAuctionApp.setAuctionSer(new BigDecimal(serial[i]));
					tdscAuctionApp.setConNum(cardNo[i]);
					if (StringUtils.isNumeric(price[i]))
						tdscAuctionApp.setAddPrice(new BigDecimal(price[i]));
					tdscAuctionAppList.add(tdscAuctionAppDao.save(tdscAuctionApp));
				}
			}
			return tdscAuctionAppList;
		} else {
			TdscListingInfo tdscListingInfo = this.getTdscListingInfoByAppId(appId);
			if (tdscListingInfo == null) {
				tdscListingInfo = new TdscListingInfo();
				tdscListingInfo.setAppId(appId);
				tdscListingInfo.setListDate(new Timestamp(System.currentTimeMillis()));
				tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.saveOrUpdate(tdscListingInfo);
			}
			// 找出所有tdscLisingApp,删除
			List tdscListingAppOldList = this.tdscListingAppDao.findTdscListingAppLocalSenseList(tdscListingInfo.getListingId());
			for (int j = 0; j < tdscListingAppOldList.size(); j++) {
				TdscListingApp tdscListingApp = (TdscListingApp) tdscListingAppOldList.get(j);
				this.tdscListingAppDao.delete(tdscListingApp);
			}
			List tdscListingAppList = new ArrayList();
			if (serial != null && serial.length > 0) {
				for (int i = 0; i < serial.length; i++) {
					TdscListingApp tdscListingApp = new TdscListingApp();
					tdscListingApp.setListingId(tdscListingInfo.getListingId());
					if (StringUtils.isNumeric(serial[i]))
						tdscListingApp.setPriceNum(new BigDecimal(serial[i]));
					if (tdscBlockAppView.getLocalTradeType().equals("2")) {
						tdscListingApp.setListNo(cardNo[i]);
						tdscListingApp.setPriceType("2");
					} else if (tdscBlockAppView.getLocalTradeType().equals("1")) {
						tdscListingApp.setListCert(cardNo[i]);
						tdscListingApp.setPriceType("1");
					}
					if (StringUtils.isNumeric(price[i]))
						tdscListingApp.setListPrice(new BigDecimal(price[i]));
					tdscListingAppList.add(tdscListingAppDao.save(tdscListingApp));
				}
			}
			return tdscListingAppList;
		}
	}

	/**
	 * 根据appId显示自助挂牌过程的价格
	 * 
	 * @param appId
	 * @return
	 */
	public List queryTdscAuctionAppList(String appId) {
		return this.tdscAuctionAppDao.findTdscAuctionAppListByAppId(appId);
	}

	public List queryListingRecord(String appId) {
		List ListingRecord = this.tdscListingInfoDao.findListingInfo(appId);
		return ListingRecord;
	}

	/**
	 * 根据资格编号得到记录
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp queryBidderAppInfo(String certNo) {
		TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getBidderAppByCertNo(certNo);
		return tdscBidderApp;
	}

	/**
	 * 根据一卡通序号得到记录
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp queryBidderAppInfoByYktXh(String yktXh) {
		TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getBidderAppByYktXh(yktXh);
		return tdscBidderApp;
	}

	/**
	 * 取得未竟得人列表
	 * 
	 * @param resultCert
	 * @param resultName
	 * @param tdscBidderAppList
	 * @return
	 */
	public List getBidderFailedList(String resultCert, String resultName, List tdscBidderAppList) {
		List bidderFailedList = new ArrayList();
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			for (int i = 0; i < tdscBidderAppList.size(); i++) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
				if (!resultCert.equals(tdscBidderApp.getCertNo())) {
					bidderFailedList.add(tdscBidderApp);
				}
			}
		}
		return bidderFailedList;
	}

	/**
	 * 通过bidderIdList查询TdscBidderPersonApp 每个bidderId可能对应多个bidderName，
	 * 当联合竟买时需将多个竟买人组装成一个字符串(以＂，＂分隔)，将其存入bidderNameList
	 * 
	 * 当单独竟买时需将bidderName存入bidderNameList
	 * 
	 * @param bidderIdList
	 * @return
	 */
	public List getBidderNameListByBidderId(List bidderIdList) {
		List bidderNameList = new ArrayList();
		if (bidderIdList != null && bidderIdList.size() > 0) {
			for (int i = 0; i < bidderIdList.size(); i++) {
				String bidderId = (String) bidderIdList.get(i);
				// 通过bidderId取得TdscBidderPersonApp对象List
				List tempList = tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
				String mutilBidderName = "";
				if (tempList != null && tempList.size() > 0) {
					for (int j = 0; j < tempList.size(); j++) {
						// 将bidderName组装成一个字符串(以＂，＂分隔)
						TdscBidderPersonApp tempTdscBidderPersonApp = (TdscBidderPersonApp) tempList.get(j);
						mutilBidderName += tempTdscBidderPersonApp.getBidderName();
						mutilBidderName += ",";
					}
					mutilBidderName = mutilBidderName.substring(0, mutilBidderName.length() - 1);
					bidderNameList.add(mutilBidderName);
				}

			}
		}

		return bidderNameList;
	}

	/**
	 * 查询评标编号
	 * 
	 * @param appId
	 * @return
	 */
	public List queryTenderNoList(String appId) {
		List tenderInfoList = this.tdscBidderAppService.queryTenderNoList(appId);
		return tenderInfoList;
	}

	/**
	 * 根据评标编号查询资格证书编号
	 * 
	 * @param tenderNo
	 * @return
	 */
	public String queryCertNoByTenderNo(String tenderNo) {
		String certNo = this.tdscBidderAppService.queryCertNoByTenderNo(tenderNo);
		return certNo;
	}

	/**
	 * 保存招标成功的结果
	 * 
	 * @param tdscTenderInfo
	 */
	public void saveTenderInfo(TdscTenderInfo tdscTenderInfo) {
		this.tdscTenderInfoDao.save(tdscTenderInfo);
	}

	/**
	 * 取得招标过程表中的全部信息
	 * 
	 * @return
	 */
	public List queryTenderAppList() {
		List tenderList = this.tdscTenderAppDao.findTenderAppList();
		return tenderList;
	}

	/**
	 * 根据AppId取得招标会Id
	 * 
	 * @param appId
	 * @return
	 */
	public String queryTenderIdByAppId(String appId) {
		String tenderId = this.tdscTenderInfoDao.findTenderIdByAppId(appId);
		return tenderId;
	}

	/**
	 * 根据appId查询进度安排表
	 * 
	 * @param condition
	 * @return
	 */
	public TdscBlockPlanTable queryBlockPlanTable(TdscBlockPlanTableCondition condition) {
		TdscBlockPlanTable tdscBlockPlanTable = this.tdscBlockPlanTableDao.findBlockPlanTableInfo(condition);
		return tdscBlockPlanTable;
	}

	/**
	 * 保存招标过程
	 * 
	 * @param tdscTenderApp
	 */
	public void saveTenderAppInfo(TdscTenderApp tdscTenderApp) {
		this.tdscTenderAppDao.save(tdscTenderApp);
	}

	/**
	 * 保存修改后的招标记录
	 * 
	 * @param tdscTenderApp
	 */
	public void modifyTenderAppRecord(TdscTenderApp tdscTenderApp) {
		this.tdscTenderAppDao.update(tdscTenderApp);
	}

	/**
	 * 查出在tdsc_tender_app表中certNo对应的记录
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscTenderApp queryTenderAppListByCertNo(String certNo) {
		TdscTenderApp tdscTenderApp = this.tdscTenderAppDao.findTenderAppListByCertNo(certNo);
		return tdscTenderApp;
	}

	/**
	 * 删除一条投标过程表中的记录
	 * 
	 * @param tdscTenderApp
	 */
	public void removeTenderAppRecord(TdscTenderApp tdscTenderApp) {
		this.tdscTenderAppDao.delete(tdscTenderApp);
	}

	/**
	 * 保存价格最高者得的招标过程
	 * 
	 * @param tenderAppMap
	 * @return
	 */
	public List saveInviteProcess(Map tenderAppMap) {
		String appId = (String) tenderAppMap.get("appId");
		String inviteType = (String) tenderAppMap.get("inviteType");
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// 当前时间
		String[] serial = (String[]) tenderAppMap.get("serialPrice");
		String[] certNo = (String[]) tenderAppMap.get("certNoPrice");
		String[] tenderNo = (String[]) tenderAppMap.get("tenderNoPrice");
		String[] price = (String[]) tenderAppMap.get("invitePrice");

		String tenderId = this.tdscTenderInfoDao.findTenderIdByAppId(appId);
		TdscTenderInfo tdscTenderInfo = (TdscTenderInfo) this.tdscTenderInfoDao.get(tenderId);

		// 找出所有的tdscTenderApp,删除
		List tdscTenderAppOldList = this.tdscTenderAppDao.findTenderAppListByTenderId(tenderId);
		if (tdscTenderAppOldList != null && tdscTenderAppOldList.size() > 0) {
			for (int j = 0; j < tdscTenderAppOldList.size(); j++) {
				TdscTenderApp tdscTenderApp = (TdscTenderApp) tdscTenderAppOldList.get(j);
				this.tdscTenderAppDao.delete(tdscTenderApp);
			}
		}
		// 删除完毕,开始添加
		List tdscTenderAppList = new ArrayList();
		for (int i = 0; i < serial.length; i++) {
			TdscTenderApp tdscTenderApp = new TdscTenderApp();
			tdscTenderApp.setTenderId(tenderId);
			tdscTenderApp.setTenderType(inviteType);
			tdscTenderApp.setTenderDate(nowTime);
			tdscTenderApp.setTenderSer(new BigDecimal(serial[i]));
			tdscTenderApp.setTenderCert(certNo[i]);
			tdscTenderApp.setTenderNo(tenderNo[i]);
			tdscTenderApp.setTenderPrice(new BigDecimal(price[i]));
			tdscTenderAppList.add(tdscTenderAppDao.save(tdscTenderApp));
		}
		return tdscTenderAppList;
	}

	/**
	 * 根据tenderId查询招标过程的list
	 * 
	 * @param tenderId
	 * @return
	 */
	public List queryTenderAppListByTenderId(String tenderId) {
		List list = this.tdscTenderAppDao.findTenderAppListByTenderId(tenderId);
		return list;
	}

	/**
	 * 根据ListingId查出表中最高的挂牌价格
	 * 
	 * @param ListingId
	 * @return
	 */
	public String getMaxPrice(String ListingId) {
		List listingAppList = new ArrayList();
		BigDecimal maxPrice = null;
		listingAppList = tdscListingAppDao.findTdscListingAppListByListingId(ListingId);
		if (listingAppList != null && listingAppList.size() > 0) {
			maxPrice = ((TdscListingApp) listingAppList.get(0)).getListPrice();
			for (int i = 1; i < listingAppList.size(); i++) {
				BigDecimal temp = ((TdscListingApp) listingAppList.get(i)).getListPrice();
				if (temp.intValue() > maxPrice.intValue()) {
					maxPrice = temp;
				}
			}
		}
		if (maxPrice == null)
			return "";
		else
			return maxPrice.toString();
	}

	public List queryListingAppByYktXh(String yktXh) {
		List listingAppList = tdscListingAppDao.queryListingAppByYktXh(yktXh);
		List tempList = new ArrayList();
		if (listingAppList != null && listingAppList.size() > 0) {
			for (int i = 0; i < listingAppList.size(); i++) {
				TdscListingApp tdscListingApp = (TdscListingApp) listingAppList.get(i);
				listingAppList.remove(tdscListingApp);
				i--;
				for (int j = 0; j < listingAppList.size(); j++) {
					TdscListingApp compListApp = (TdscListingApp) listingAppList.get(j);
					if (compListApp.getListPrice().equals(tdscListingApp.getListPrice())) {
						listingAppList.remove(j);
					}
				}
				tempList.add(tdscListingApp);
			}
		}
		return tempList;
	}

	/**
	 * 取得未竟得人列表
	 * 
	 * @param resultCert
	 * @param resultName
	 * @param tdscBidderAppList
	 * @return
	 */
	public List getBidderFailedListZB(String resultNo, List tdscBidderAppList) {

		if (null == resultNo) {
			return tdscBidderAppList;
		}

		List bidderFailedList = new ArrayList();
		if (tdscBidderAppList != null && tdscBidderAppList.size() > 0) {
			for (int i = 0; i < tdscBidderAppList.size(); i++) {
				TdscBidderApp tdscBidderApp = (TdscBidderApp) tdscBidderAppList.get(i);
				if (resultNo.equals(tdscBidderApp.getYktXh())) {
					continue;
				} else
					bidderFailedList.add(tdscBidderApp);

			}
		}
		return bidderFailedList;
	}

	/**
	 * 保存招标交易结果
	 * 
	 * @param tdscTenderInfo
	 * @param tdscBlockTranApp
	 */
	public void updateTenderResult(TdscTenderInfo tdscTenderInfo, TdscBlockTranApp tdscBlockTranApp, TdscBlockInfo tdscBlockInfo) {
		tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
		if (null != tdscTenderInfo) {
			tdscTenderInfoDao.saveOrUpdate(tdscTenderInfo);
		}
	}

	/**
	 * 保存拍卖交易结果
	 * 
	 * @param tdscBlockTranApp
	 * @param tdscBlockInfo
	 * @param tdscAuctionInfo
	 */

	public void updateVendueTranResult(TdscBlockTranApp tdscBlockTranApp, TdscBlockInfo tdscBlockInfo, TdscAuctionInfo tdscAuctionInfo) {
		if (null != tdscBlockTranApp) {
			this.tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
		}
		this.tdscBlockInfoDao.saveOrUpdate(tdscBlockInfo);
		if (null != tdscAuctionInfo) {
			this.tdscAuctionInfoDao.saveOrUpdate(tdscAuctionInfo);
		}

	}

	public List queryByListCertAndAppId(String listCert, String appId) {
		return (List) tdscListingAppDao.queryByListCert(listCert, appId);
	}

	/**
	 * 查询出让文件（公告）已经发布，但公告交易结果尚未发布的公告信息
	 * 
	 * @return
	 */
	public List queryNoticeIdListInTrade() {
		return (List) tdscNoticeAppDao.queryNoticeIdListInTrade();
	}

	/**
	 * 根据noticeId查询TdscBlockAppView列表
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryAppViewListByNoticeId(String noticeId) {
		return (List) tdscNoticeAppDao.queryAppViewListByNoticeId(noticeId);
	}

	/**
	 * 通过地块信息表获得参加拍卖会所有换领的号牌列表（机审通过）
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryCertNolistByNoticeId(String noticeId) {
		// 根据公告号查询地块ID（appIdList）
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// 根据appIdList查询机审合格的竞买人信息LIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryCertNolistByAppId(appIdList);
		}
		return null;
	}

	/**
	 * 根据公告ID查询竞买人信息（机审通过）
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListByNoticeId(String noticeId) {
		// 根据公告号查询地块ID（appIdList）
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// 根据appIdList查询机审合格的竞买人信息LIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListByAppId(appIdList);
		}
		return null;
	}

	/**
	 * 根据公告ID查询竞买人信息（机审通过）
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListWithNoCertNo(String noticeId) {
		// 根据公告号查询地块ID（appIdList）
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// 根据appIdList查询机审合格的竞买人信息LIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListWithNoCertNo(appIdList);
		}
		return null;
	}

	/**
	 * 取得该号牌号在公告中所有对应的竞买人信息
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListByCertNo(String noticeId, String certNo) {
		// 根据公告号查询地块ID（appIdList）
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// 根据appIdList查询机审合格的竞买人信息LIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListByCertNo(appIdList, certNo);
		}
		return null;
	}

	/**
	 * 取得一卡通号在公告中所有对应的竞买人信息
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListByYktBh(String noticeId, String certNo) {
		// 根据公告号查询地块ID（appIdList）
		List tdscBidderAppList = new ArrayList();
		if (noticeId != null && !"".equals(noticeId) && certNo != null && !"".equals(certNo)) {
			TdscBidderCondition condition = new TdscBidderCondition();
			condition.setNoticeId(noticeId);
			condition.setYktBh(certNo);
			tdscBidderAppList = (List) tdscBidderAppDao.findBidderAppListByCondition(condition);
		}
		return tdscBidderAppList;
	}

	/**
	 * 取得该公告中所有不同交易卡对应的竞买人信息
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListWithDiffYkt(String noticeId) {
		// 根据公告号查询地块ID（appIdList）
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// 根据appIdList查询机审合格的竞买人信息LIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListWithDiffYkt(appIdList);
		}
		return null;
	}

	public TdscNoticeApp queryNoticeListByNoticeId(String noticeId) {
		return (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
	}

	/**
	 * 结束挂牌保存流程
	 * 
	 * @param tempList
	 * @param user
	 */
	public void saveOpnnJSHP(List tempList, SysUser user) {
		if (tempList != null && tempList.size() > 0) {
			for (int i = 0; i < tempList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) tempList.get(i);
				if (app != null) {
					// 挂牌
					if (app.getTransferMode() != null && (GlobalConstants.DIC_TRANSFER_LISTING).equals(app.getTransferMode())) {
						if (app.getNodeId() != null && "17".equals(app.getNodeId())) {// 只处理在换领号牌节点的数据
							try {
								this.appFlowService.saveOpnn(app.getAppId(), app.getTransferMode(), user);// 参数依次为appId、出让方式和用户信息
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					// 拍卖
					else if (app.getTransferMode() != null && (GlobalConstants.DIC_TRANSFER_AUCTION).equals(app.getTransferMode())) {
						if (app.getNodeId() != null && "15".equals(app.getNodeId())) {// 只处理在换领号牌节点的数据
							try {
								this.appFlowService.saveOpnn(app.getAppId(), app.getTransferMode(), user);// 参数依次为appId、出让方式和用户信息
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 根据TdscBaseQueryCondition查询noticeapList lz+ 090710
	 * 
	 * @param condition
	 * @return
	 */
	public List queryNoticeListByCondition(TdscNoticeAppCondition condition) {
		return (List) tdscNoticeAppDao.findNoticeAppList(condition);
	}

	/**
	 * 保存现场竞价过程信息
	 * 
	 * @param tdscAuctionAppList
	 */
	public void saveTdscAuctionAppList(List tdscAuctionAppList) {
		if (tdscAuctionAppList != null && tdscAuctionAppList.size() > 0) {
			for (int i = 0; i < tdscAuctionAppList.size(); i++) {
				TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) tdscAuctionAppList.get(i);
				tdscAuctionAppDao.save(tdscAuctionApp);
			}
		}
	}

	public List findAppListByAppId(String appId) {
		return (List) tdscAuctionAppDao.findAppListByAppId(appId);
	}

	public List findAppListByAppIddesc(String appId) {
		return (List) tdscAuctionAppDao.findAppListByAppIddesc(appId);
	}

	public List findAppListByAppIdDesc(String appId) {
		return (List) tdscAuctionAppDao.findAppListByAppIdDesc(appId);
	}

	public void tidyAppListByAppId(String appId, List tdscAuctionAppList) {
		// 根据appId查询原有数据
		List appList = (List) tdscAuctionAppDao.findAppListByAppId(appId);
		// 删除原有数据
		if (appList != null && appList.size() > 0) {
			for (int i = 0; i < appList.size(); i++) {
				TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) appList.get(i);
				tdscAuctionAppDao.delete(tdscAuctionApp);
			}
		}
		// 新增现场竞价信息
		saveTdscAuctionAppList(tdscAuctionAppList);
	}

	public void produceXcjjXml(String appId) {

		// 以下是利用CastorFactory根据bean和mapxml生成需要向大屏发布的目标xml文件
		TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		tdscBaseQueryCondition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
		List list = findAppListByAppIddesc(appId);

		String blockAuctionInfoMapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "xcjjMap.xml";// 源xml文件的路径
		String transferModeShort = "";// 出让方式的简称，用来组成xml文件的名称
		String endTime = "";// xml文件的有效时间，精确到秒，用来组成xml文件的名称（系统当前日期大于有效时间中的日期时，将文件移动到备份目录下）
		String blockAuctionInfoClassName = "";// 该变量存放xcjjMap.xml对应bean所生成的目标xml文件的名称
		String dapingPath = PropertiesUtil.getInstance().getProperty("daping_path");// 大屏发布目录，该目录下有若干子目录，每个子目录对应一个信息发布要点
		String xcjjPath = "";// 大屏发布目录下现场竞价子目录(xcjj)的路径
		String blockAuctionInfoXmlSP = "";// 该变量存放xcjjMap.xml对应bean所生成的目标xml文件路径及文件名
		if ("3107".equals(tdscBlockAppView.getTransferMode())) {
			transferModeShort = "Z";
			// 因为存在同一天举行多个公告的现场竞价会，所以时间格式为yyyyMMdd比较好处理
			endTime = DateUtil.date2String(tdscBlockAppView.getOpeningDate(), "yyyyMMdd");
		} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
			transferModeShort = "P";
			// 因为存在同一天举行多个公告的现场竞价会，所以时间格式为yyyyMMdd比较好处理
			endTime = DateUtil.date2String(tdscBlockAppView.getActionDate(), "yyyyMMdd");
		} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
			transferModeShort = "G";
			// 因为存在同一天举行多个公告的现场竞价会，所以时间格式为yyyyMMdd比较好处理
			endTime = DateUtil.date2String(tdscBlockAppView.getSceBidDate(), "yyyyMMdd");
		}

		if (StringUtils.isBlank(transferModeShort)) {
			transferModeShort = "0";
		}

		if (StringUtils.isBlank(endTime)) {
			endTime = "19000101";// 1900年01月01日
		}

		BlockAuctionInfo blockAuctionInfo = new BlockAuctionInfo();
		int currentNum = 0;// 轮次
		String haoPai = "";// 报价号牌
		BigDecimal currentPrice = new BigDecimal(0);// 当前报价
		BigDecimal totalBuildingArea = new BigDecimal(0);// 总建筑面积
		BigDecimal initPrice = tdscBlockAppView.getInitPrice();// 起始价
		BigDecimal totalArea = new BigDecimal(0);// 总土地面积
		List blockAuctionPriceList = new ArrayList();

		blockAuctionInfo.setBlockNoticeNo(tdscBlockAppView.getBlockNoticeNo());// 地块编号

		if (tdscBlockAppView.getTotalBuildingArea() != null) {
			totalBuildingArea = tdscBlockAppView.getTotalBuildingArea();
		}

		if (tdscBlockAppView.getTotalLandArea() != null) {
			totalArea = tdscBlockAppView.getTotalLandArea();
		}

		for (int i = 0; null != list && i < list.size(); i++) {
			BlockAuctionPrice blockAuctionPrice = new BlockAuctionPrice();
			TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) list.get(i);

			if (tdscAuctionApp.getAddPrice() != null) {

				// 轮次
				currentNum = list.size() - i;
				blockAuctionPrice.setCurrentNum(currentNum + "");

				// 报价号牌
				haoPai = tdscAuctionApp.getHaoPai();
				blockAuctionPrice.setHaoPai(haoPai);

				// 当前总价
				currentPrice = tdscAuctionApp.getAddPrice();
				blockAuctionPrice.setCurrentPrice(currentPrice.toString().trim());

				// 楼面地价为当前总价除以地块规划建筑面积
				if (totalBuildingArea.intValue() > 0) {
					if ("102".equals(tdscBlockAppView.getBlockQuality())) {// 经营性用地，使用楼面地价（元/平方米），楼面地价=报价÷规划建筑面积
						blockAuctionPrice.setUnitPrice(currentPrice.multiply(new BigDecimal(10000)).divide(totalBuildingArea, 2, BigDecimal.ROUND_HALF_UP).toString());
					} else {// 工业用地，使用亩均价（万元/亩），亩均价=报价÷土地面积×666.67
						blockAuctionPrice.setUnitPrice(currentPrice.multiply(new BigDecimal(666.67)).divide(totalArea, 2, BigDecimal.ROUND_HALF_UP).toString());
					}
				}

				if (initPrice != null && initPrice.intValue() > 0) {
					BigDecimal premium = currentPrice.subtract(initPrice);// 溢价
					blockAuctionPrice.setPremiumRate(premium.multiply(new BigDecimal(100)).divide(initPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
				}
			}
			blockAuctionPriceList.add(blockAuctionPrice);
		}

		blockAuctionInfo.setList(blockAuctionPriceList);

		blockAuctionInfoClassName = transferModeShort + "_" + endTime;
		xcjjPath = dapingPath + "xcjj\\";
		blockAuctionInfoXmlSP = xcjjPath + blockAuctionInfoClassName + ".xml";// 存放xcjjMap.xml对应bean所生成的目标xml文件路径及文件名

		File dirXcjj = new File(xcjjPath);
		if (!dirXcjj.exists()) {
			dirXcjj.mkdir();
		}

		CastorFactory.marshalBeanOverWriteXml(blockAuctionInfo, blockAuctionInfoMapPath, blockAuctionInfoXmlSP);

	}

	public List queryTdscListingAppListByYKTXHAndAppId(String yktXh, String appId) {
		return (List) tdscListingAppDao.queryTdscListingAppListByYKTXHAndAppId(yktXh, appId);
	}

	public TdscBlockTranApp getTdscBlockTranApp(String appId) {

		return (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
	}

	public void updateTdscBlockTranApp(TdscBlockTranApp tdscBlockTranApp) {

		tdscBlockTranAppDao.update(tdscBlockTranApp);
	}

	/**
	 * 用交易卡号和 appid 获得其最高报价
	 * 
	 * @param kahao
	 * @param appId
	 * @return
	 */
	public String getBidderMaxPrice(String kahao, String appId) {
		List tmpList = tdscListingAppDao.queryTdscListingAppListByYKTXHAndAppId(kahao, appId);
		BigDecimal tmpBd = new BigDecimal("0");
		if (tmpList != null && tmpList.size() > 0) {
			for (int i = 0; i < tmpList.size(); i++) {
				TdscListingApp tdscListingApp = (TdscListingApp) tmpList.get(i);
				if (tmpBd.compareTo(tdscListingApp.getListPrice()) < 0)
					tmpBd = tdscListingApp.getListPrice();
			}
		}
		return tmpBd + "";
	}

	public TdscBlockPart getBlockInfoPart(String blockId) {
		return tdscBlockInfoService.getBlockInfoPart(blockId);
	}

	public List queryBlockInfoByCond(TdscBlockInfoCondition blockCond) {
		return tdscBlockInfoDao.queryBlockInfoList(blockCond);
	}

	public List findBlockPlanTableList(TdscBlockPlanTableCondition condition) {
		return tdscBlockPlanTableDao.findBlockPlanTableList(condition);
	}

	/**
	 * 无意向竞买人的地块，新增的第一个竞买人即第一轮挂牌，判断该地块是否已经有记录在表中, 需写入表 TDSC_LISTING_APP and
	 * TDSC_LISTING_INFO
	 * 
	 * @param appId
	 * @param yktXh
	 * @param certNo
	 * @param initPrice
	 * @return
	 */
	public boolean makeFirstPersonIsInitPrice(String appId, String yktXh, String certNo, BigDecimal initPrice) {
		// 1. save TDSC_LISTING_INFO (listing_id=+1, app_id,
		// curr_price=该块土地的初始价格, curr_round=1, list_date=now date, ykt_xh=卡号,
		// list_cert=资格确认书编号)
		// 2. save TDSC_LISTING_APP (list_app_id=+1, listing_id, listing_ser=1,
		// price_type=1, list_cert=, list_price=, list_date=, ytk_xh=, app_id)
		List infoList = tdscListingInfoDao.findListingInfo(appId);
		if (infoList != null && infoList.size() > 0) {
			// 该地块已经存在挂牌记录，不必加入
			return false;
		} else {

			TdscListingInfo listingInfo = new TdscListingInfo();
			listingInfo.setAppId(appId);
			listingInfo.setCurrPrice(initPrice);
			listingInfo.setCurrRound(new BigDecimal("1"));
			listingInfo.setListDate(new Timestamp(System.currentTimeMillis()));
			listingInfo.setYktXh(yktXh);
			listingInfo.setListCert(certNo);

			TdscListingInfo newListingInfo = (TdscListingInfo) tdscListingInfoDao.save(listingInfo);

			TdscListingApp tdscListingApp = new TdscListingApp();
			tdscListingApp.setListingId(newListingInfo.getListingId());
			tdscListingApp.setListingSer(new BigDecimal("1"));
			tdscListingApp.setPriceType("1");
			tdscListingApp.setListCert(certNo);
			tdscListingApp.setListPrice(initPrice);
			tdscListingApp.setListDate(new Timestamp(System.currentTimeMillis()));
			tdscListingApp.setYktXh(yktXh);
			tdscListingApp.setAppId(appId);

			tdscListingAppDao.save(tdscListingApp);

		}
		return true;
	}

	public TdscBlockPlanTable getTdscBlockPlanTable(String planId) {
		return (TdscBlockPlanTable) tdscBlockPlanTableDao.get(planId);
	}

	public List getTdscAuctionAppByAppId(String appId) {
		List tdscAuctionApp = this.tdscAuctionInfoDao.getTdscAuctionAppByAppId(appId);
		return tdscAuctionApp;
	}

	public List queryListingAppListByAppId(String appId) {
		return this.tdscListingAppDao.queryListingAppListByAppId(appId);
	}

	// 意向人报名时对意向地块插入第一轮挂牌记录，挂牌价格为起始价，时间为公告起始时间
	public void insertListingAppOfPurposePerson(TdscBidderApp tdscBidderApp, String purposeAppId) {
		
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
				tdscListingInfo.setYktXh(tdscBidderApp.getYktXh());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListNo(tdscBidderApp.getConNum());
				tdscListingInfo.setCurrPrice(tdscBlockTranApp.getInitPrice());
				tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.save(tdscListingInfo);
			}

			TdscListingApp tdscListingApp = tdscListingAppDao.getListingAppOfPurposePerson(appIds[i], tdscBidderApp.getCertNo());
			
			if (tdscListingApp == null) {// 意向人没有对他购买的这个意向地块进行挂牌，则插入挂牌记录，挂牌价格为起始价，挂牌时间为公告起始时间
				TdscListingApp listingApp = new TdscListingApp();
				listingApp.setListingId(tdscListingInfo.getListingId());
				listingApp.setListingSer(new BigDecimal(1));
				listingApp.setPriceType("11");
				listingApp.setPriceNum(new BigDecimal(1));
				listingApp.setListCert(tdscBidderApp.getCertNo());
				listingApp.setListNo(tdscBidderApp.getConNum());
				listingApp.setListDate(tdscBlockPlanTable.getIssueStartDate());
				listingApp.setYktXh(tdscBidderApp.getYktXh());
				listingApp.setAppId(appIds[i]);
				listingApp.setListPrice(tdscBlockTranApp.getInitPrice());

				tdscListingAppDao.save(listingApp);
			}
		}
	}
	
	public List getListingAppListByAppId(String appId,String certNo, String priceType) {
		return this.tdscListingAppDao.getListingAppListByAppId(appId, certNo, priceType);
	}

}
