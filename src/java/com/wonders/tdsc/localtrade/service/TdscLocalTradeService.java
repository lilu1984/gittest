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
	 * ��ѯ���о�����Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public List queryTdscBidderAppList(String appId) {
		List tdscBidderAppList = this.tdscBidderAppService.queryBidderAppList(appId);
		return tdscBidderAppList;
	}

	/**
	 * ��ѯ���л���ϸ�����Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public List queryBidderListSrc(String appId) {
		List tdscBidderAppList = this.tdscBidderAppService.queryBidderListSrc(appId);
		return tdscBidderAppList;
	}

	/**
	 * ���滻ȡ�������Ƶļ�¼
	 * 
	 * @param vendueCardMap
	 */
	public void saveTakeCardBidder(Map vendueCardMap) {
		String certNo = (String) vendueCardMap.get("certNo");// ԭ �ʸ�֤���� ���ִ�
																// һ��ͨ��� ��ֵ
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
	 * ͨ��appId�ͺ��ƺ�����һ��������Ϣ
	 * 
	 * @param cardNo
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppidCardNo(String appId, String cardNo) {
		return this.tdscBidderAppService.getBidderAppByAppIdConNo(appId, cardNo);
	}

	/**
	 * ͨ��appId��һ��ͨ��Ż��һ��������Ϣ
	 * 
	 * @param appId
	 * @param yktXh
	 * @return
	 */
	public TdscBidderApp getTdscBidderAppByAppidYktXh(String appId, String yktXh) {
		return this.tdscBidderAppService.getBidderAppByAppIdYktXh(appId, yktXh);
	}

	/**
	 * ͨ��appId���ʸ�֤���Ż��һ��������Ϣ
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
	 * ͨ��APPID�ҵ�һ���ж����˲���������,���ж����˻����˺���
	 * 
	 * @param appId
	 * @return
	 */
	public List queryJoinAuctionList(String appId) {
		return this.tdscBidderAppService.queryJoinAuctionList(appId);
	}

	/**
	 * ���ĳһ�����˵Ĺ��Ƽ�¼
	 * 
	 * @param listingAppId
	 * @return
	 */
	public List queryListingAppInfo(String listingId, String listCert) {
		List listingAppList = this.tdscListingAppDao.findListingAppInfo(listingId, listCert);
		return listingAppList;
	}

	/**
	 * ��ҳ�б�
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
	 * ��ѯ��߼۸�
	 * 
	 * @param listingId
	 * @return
	 */
	public BigDecimal queryTopPrice(String listingId) {
		BigDecimal topPrice = tdscListingAppDao.findTopPrice(listingId);
		return topPrice;
	}

	/**
	 * ������߼۸񵽹�����Ϣ��
	 * 
	 * @param topPrice
	 */
	public void saveTopPrice(BigDecimal topPrice) {
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		tdscListingInfo.setResultPrice(topPrice);
		tdscListingInfoDao.save(tdscListingInfo);
	}

	/**
	 * ����ҵ��Id������ƻ�Id
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
	 * �������֤���
	 * 
	 * @param listingId
	 * @return
	 */
	public String queryListCert(String listingId) {
		String listCert = this.tdscListingAppDao.findListCertByListingId(listingId);
		return listCert;
	}

	/**
	 * ���������Ϣ�������listingAppId
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
	 * ��ѯ��ǰ���ƴ���
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
	 * ��������Ϣ�����TDSC_BLOCK_TRAN_APP,TDSC_BLOCK_INFO��
	 */
	public void modifyVendueInfo(String appId) {
		TdscBlockTranApp tdscBlockTranApp = this.tdscBlockInfoService.getTdscBlockTranAppByAppId(appId);
	}

	/**
	 * ���¹�����Ϣ���еĹ��Ƽ�¼
	 * 
	 * @param listCert
	 * @param listingPrice
	 */
	public void modifyListingPrice(String listCert, String listingPrice) {
		// ����֤����ȡ�ù��ƻ�Id
		TdscListingApp tdscListingApp = new TdscListingApp();
		tdscListingApp.setListCert(listCert);
		tdscListingApp = (TdscListingApp) tdscListingAppDao.get(tdscListingApp);
		String listingId = tdscListingApp.getListingId();
		// ���ݹ��ƻ�Idȡ�ö�Ӧ�ؿ������Ϣ
		TdscListingInfo tdscListingInfo = new TdscListingInfo();
		tdscListingInfo.setListingId(listingId);
		tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.get(tdscListingInfo);
		// �޸ļ۸�
		tdscListingInfo.setCurrPrice(new BigDecimal(listingPrice));
		// �޸Ĺ��ƴ���
		tdscListingInfo.setCurrRound(tdscListingApp.getPriceNum());
		// �޸�ʱ��
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		tdscListingInfo.setListDate(nowTime);
		// ���һ�ι�����Ϣ���޸�
		tdscListingInfoDao.save(tdscListingInfo);

	}

	/**
	 * �����״ι��ƵĹ�����Ϣ
	 * 
	 * @param tdscListingInfo
	 */
	public TdscListingInfo saveListingInfo(TdscListingInfo tdscListingInfo) {
		TdscListingInfo rettdscListingInfo = (TdscListingInfo) tdscListingInfoDao.save(tdscListingInfo);
		return rettdscListingInfo;
	}

	/**
	 * �����״ι��ƵĹ�����Ϣ
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
	 * �����״ι��ƵĹ�����Ϣ TdscListingInfo TdscListingInfo���ű�
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
	 * ���¹��ƻ���Ϣ��
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
	 * ���¹��ƻ���Ϣ�� TdscListingInfo TdscListingInfo���ű�
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
	 * ���ҹ��ƻ�Id��Ӧ�Ĺ�����Ϣ
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
	 * �����б��Id��Ӧ���б���Ϣ
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
	 * ��Listing_App��������һ�ι��Ƽ�¼
	 * 
	 * @param tdscListingApp
	 */
	public void saveToListingApp(TdscListingApp tdscListingApp) {
		tdscListingAppDao.save(tdscListingApp);
	}

	/**
	 * ����APPID����TDSCLISTINGINFO����
	 */
	public TdscListingInfo getTdscListingInfoByAppId(String appId) {
		return this.tdscListingInfoDao.getTdscListingInfoByAppId(appId);
	}

	/**
	 * ͨ��listingId ����tdscListingApp���б�
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppListByListingId(String listingId) {
		return this.tdscListingAppDao.findTdscListingAppListByListingId(listingId);
	}

	/**
	 * ͨ��listingId��priceType����tdscListingApp���б�
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppListByListingId(String listingId, String priceType) {
		return this.tdscListingAppDao.findTdscListingAppListByListingId(listingId, priceType);
	}

	/**
	 * ͨ��listingId ����tdscListingApp���������ֳ����۵��б�
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppLocalSenseList(String listingId) {
		return this.tdscListingAppDao.findTdscListingAppLocalSenseList(listingId);
	}

	/**
	 * ͨ��listingId ����tdscListingApp�������� ����¼������ �б��������
	 * 
	 * @param listingId
	 * @return
	 */
	public List queryTdscListingAppInGCLR(String listingId) {
		return this.tdscListingAppDao.queryTdscListingAppInGCLR(listingId);
	}

	public List saveTradeProcess(Map vendueAppMap) {
		// ���淽������ȫ��ɾ��,�ٽ�ҳ���ύ�ı�ȫ������

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
			// �ҳ����е�tdscAuctionApp,ɾ��
			List tdscAuctionAppOldList = this.tdscAuctionAppDao.findTdscAuctionAppListByAppId(appId);
			for (int j = 0; j < tdscAuctionAppOldList.size(); j++) {
				TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) tdscAuctionAppOldList.get(j);
				this.tdscAuctionAppDao.delete(tdscAuctionApp);
			}
			// ɾ�����,��ʼ���
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
			// �ҳ�����tdscLisingApp,ɾ��
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
	 * ����appId��ʾ�������ƹ��̵ļ۸�
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
	 * �����ʸ��ŵõ���¼
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp queryBidderAppInfo(String certNo) {
		TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getBidderAppByCertNo(certNo);
		return tdscBidderApp;
	}

	/**
	 * ����һ��ͨ��ŵõ���¼
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscBidderApp queryBidderAppInfoByYktXh(String yktXh) {
		TdscBidderApp tdscBidderApp = this.tdscBidderAppService.getBidderAppByYktXh(yktXh);
		return tdscBidderApp;
	}

	/**
	 * ȡ��δ�������б�
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
	 * ͨ��bidderIdList��ѯTdscBidderPersonApp ÿ��bidderId���ܶ�Ӧ���bidderName��
	 * �����Ͼ���ʱ�轫�����������װ��һ���ַ���(�ԣ������ָ�)���������bidderNameList
	 * 
	 * ����������ʱ�轫bidderName����bidderNameList
	 * 
	 * @param bidderIdList
	 * @return
	 */
	public List getBidderNameListByBidderId(List bidderIdList) {
		List bidderNameList = new ArrayList();
		if (bidderIdList != null && bidderIdList.size() > 0) {
			for (int i = 0; i < bidderIdList.size(); i++) {
				String bidderId = (String) bidderIdList.get(i);
				// ͨ��bidderIdȡ��TdscBidderPersonApp����List
				List tempList = tdscBidderPersonAppDao.findTdscBidderPersonAppList(bidderId);
				String mutilBidderName = "";
				if (tempList != null && tempList.size() > 0) {
					for (int j = 0; j < tempList.size(); j++) {
						// ��bidderName��װ��һ���ַ���(�ԣ������ָ�)
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
	 * ��ѯ������
	 * 
	 * @param appId
	 * @return
	 */
	public List queryTenderNoList(String appId) {
		List tenderInfoList = this.tdscBidderAppService.queryTenderNoList(appId);
		return tenderInfoList;
	}

	/**
	 * ���������Ų�ѯ�ʸ�֤����
	 * 
	 * @param tenderNo
	 * @return
	 */
	public String queryCertNoByTenderNo(String tenderNo) {
		String certNo = this.tdscBidderAppService.queryCertNoByTenderNo(tenderNo);
		return certNo;
	}

	/**
	 * �����б�ɹ��Ľ��
	 * 
	 * @param tdscTenderInfo
	 */
	public void saveTenderInfo(TdscTenderInfo tdscTenderInfo) {
		this.tdscTenderInfoDao.save(tdscTenderInfo);
	}

	/**
	 * ȡ���б���̱��е�ȫ����Ϣ
	 * 
	 * @return
	 */
	public List queryTenderAppList() {
		List tenderList = this.tdscTenderAppDao.findTenderAppList();
		return tenderList;
	}

	/**
	 * ����AppIdȡ���б��Id
	 * 
	 * @param appId
	 * @return
	 */
	public String queryTenderIdByAppId(String appId) {
		String tenderId = this.tdscTenderInfoDao.findTenderIdByAppId(appId);
		return tenderId;
	}

	/**
	 * ����appId��ѯ���Ȱ��ű�
	 * 
	 * @param condition
	 * @return
	 */
	public TdscBlockPlanTable queryBlockPlanTable(TdscBlockPlanTableCondition condition) {
		TdscBlockPlanTable tdscBlockPlanTable = this.tdscBlockPlanTableDao.findBlockPlanTableInfo(condition);
		return tdscBlockPlanTable;
	}

	/**
	 * �����б����
	 * 
	 * @param tdscTenderApp
	 */
	public void saveTenderAppInfo(TdscTenderApp tdscTenderApp) {
		this.tdscTenderAppDao.save(tdscTenderApp);
	}

	/**
	 * �����޸ĺ���б��¼
	 * 
	 * @param tdscTenderApp
	 */
	public void modifyTenderAppRecord(TdscTenderApp tdscTenderApp) {
		this.tdscTenderAppDao.update(tdscTenderApp);
	}

	/**
	 * �����tdsc_tender_app����certNo��Ӧ�ļ�¼
	 * 
	 * @param certNo
	 * @return
	 */
	public TdscTenderApp queryTenderAppListByCertNo(String certNo) {
		TdscTenderApp tdscTenderApp = this.tdscTenderAppDao.findTenderAppListByCertNo(certNo);
		return tdscTenderApp;
	}

	/**
	 * ɾ��һ��Ͷ����̱��еļ�¼
	 * 
	 * @param tdscTenderApp
	 */
	public void removeTenderAppRecord(TdscTenderApp tdscTenderApp) {
		this.tdscTenderAppDao.delete(tdscTenderApp);
	}

	/**
	 * ����۸�����ߵõ��б����
	 * 
	 * @param tenderAppMap
	 * @return
	 */
	public List saveInviteProcess(Map tenderAppMap) {
		String appId = (String) tenderAppMap.get("appId");
		String inviteType = (String) tenderAppMap.get("inviteType");
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());// ��ǰʱ��
		String[] serial = (String[]) tenderAppMap.get("serialPrice");
		String[] certNo = (String[]) tenderAppMap.get("certNoPrice");
		String[] tenderNo = (String[]) tenderAppMap.get("tenderNoPrice");
		String[] price = (String[]) tenderAppMap.get("invitePrice");

		String tenderId = this.tdscTenderInfoDao.findTenderIdByAppId(appId);
		TdscTenderInfo tdscTenderInfo = (TdscTenderInfo) this.tdscTenderInfoDao.get(tenderId);

		// �ҳ����е�tdscTenderApp,ɾ��
		List tdscTenderAppOldList = this.tdscTenderAppDao.findTenderAppListByTenderId(tenderId);
		if (tdscTenderAppOldList != null && tdscTenderAppOldList.size() > 0) {
			for (int j = 0; j < tdscTenderAppOldList.size(); j++) {
				TdscTenderApp tdscTenderApp = (TdscTenderApp) tdscTenderAppOldList.get(j);
				this.tdscTenderAppDao.delete(tdscTenderApp);
			}
		}
		// ɾ�����,��ʼ���
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
	 * ����tenderId��ѯ�б���̵�list
	 * 
	 * @param tenderId
	 * @return
	 */
	public List queryTenderAppListByTenderId(String tenderId) {
		List list = this.tdscTenderAppDao.findTenderAppListByTenderId(tenderId);
		return list;
	}

	/**
	 * ����ListingId���������ߵĹ��Ƽ۸�
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
	 * ȡ��δ�������б�
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
	 * �����б꽻�׽��
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
	 * �����������׽��
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
	 * ��ѯ�����ļ������棩�Ѿ������������潻�׽����δ�����Ĺ�����Ϣ
	 * 
	 * @return
	 */
	public List queryNoticeIdListInTrade() {
		return (List) tdscNoticeAppDao.queryNoticeIdListInTrade();
	}

	/**
	 * ����noticeId��ѯTdscBlockAppView�б�
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryAppViewListByNoticeId(String noticeId) {
		return (List) tdscNoticeAppDao.queryAppViewListByNoticeId(noticeId);
	}

	/**
	 * ͨ���ؿ���Ϣ���òμ����������л���ĺ����б�����ͨ����
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryCertNolistByNoticeId(String noticeId) {
		// ���ݹ���Ų�ѯ�ؿ�ID��appIdList��
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// ����appIdList��ѯ����ϸ�ľ�������ϢLIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryCertNolistByAppId(appIdList);
		}
		return null;
	}

	/**
	 * ���ݹ���ID��ѯ��������Ϣ������ͨ����
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListByNoticeId(String noticeId) {
		// ���ݹ���Ų�ѯ�ؿ�ID��appIdList��
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// ����appIdList��ѯ����ϸ�ľ�������ϢLIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListByAppId(appIdList);
		}
		return null;
	}

	/**
	 * ���ݹ���ID��ѯ��������Ϣ������ͨ����
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListWithNoCertNo(String noticeId) {
		// ���ݹ���Ų�ѯ�ؿ�ID��appIdList��
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// ����appIdList��ѯ����ϸ�ľ�������ϢLIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListWithNoCertNo(appIdList);
		}
		return null;
	}

	/**
	 * ȡ�øú��ƺ��ڹ��������ж�Ӧ�ľ�������Ϣ
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListByCertNo(String noticeId, String certNo) {
		// ���ݹ���Ų�ѯ�ؿ�ID��appIdList��
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// ����appIdList��ѯ����ϸ�ľ�������ϢLIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListByCertNo(appIdList, certNo);
		}
		return null;
	}

	/**
	 * ȡ��һ��ͨ���ڹ��������ж�Ӧ�ľ�������Ϣ
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListByYktBh(String noticeId, String certNo) {
		// ���ݹ���Ų�ѯ�ؿ�ID��appIdList��
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
	 * ȡ�øù��������в�ͬ���׿���Ӧ�ľ�������Ϣ
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryBidderAppListWithDiffYkt(String noticeId) {
		// ���ݹ���Ų�ѯ�ؿ�ID��appIdList��
		List appIdList = (List) tdscBlockTranAppDao.queryAppIdListByNoticeId(noticeId);
		// ����appIdList��ѯ����ϸ�ľ�������ϢLIST
		if (appIdList != null && appIdList.size() > 0) {
			return (List) tdscBidderAppDao.queryBidderAppListWithDiffYkt(appIdList);
		}
		return null;
	}

	public TdscNoticeApp queryNoticeListByNoticeId(String noticeId) {
		return (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
	}

	/**
	 * �������Ʊ�������
	 * 
	 * @param tempList
	 * @param user
	 */
	public void saveOpnnJSHP(List tempList, SysUser user) {
		if (tempList != null && tempList.size() > 0) {
			for (int i = 0; i < tempList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) tempList.get(i);
				if (app != null) {
					// ����
					if (app.getTransferMode() != null && (GlobalConstants.DIC_TRANSFER_LISTING).equals(app.getTransferMode())) {
						if (app.getNodeId() != null && "17".equals(app.getNodeId())) {// ֻ�����ڻ�����ƽڵ������
							try {
								this.appFlowService.saveOpnn(app.getAppId(), app.getTransferMode(), user);// ��������ΪappId�����÷�ʽ���û���Ϣ
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					// ����
					else if (app.getTransferMode() != null && (GlobalConstants.DIC_TRANSFER_AUCTION).equals(app.getTransferMode())) {
						if (app.getNodeId() != null && "15".equals(app.getNodeId())) {// ֻ�����ڻ�����ƽڵ������
							try {
								this.appFlowService.saveOpnn(app.getAppId(), app.getTransferMode(), user);// ��������ΪappId�����÷�ʽ���û���Ϣ
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
	 * ����TdscBaseQueryCondition��ѯnoticeapList lz+ 090710
	 * 
	 * @param condition
	 * @return
	 */
	public List queryNoticeListByCondition(TdscNoticeAppCondition condition) {
		return (List) tdscNoticeAppDao.findNoticeAppList(condition);
	}

	/**
	 * �����ֳ����۹�����Ϣ
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
		// ����appId��ѯԭ������
		List appList = (List) tdscAuctionAppDao.findAppListByAppId(appId);
		// ɾ��ԭ������
		if (appList != null && appList.size() > 0) {
			for (int i = 0; i < appList.size(); i++) {
				TdscAuctionApp tdscAuctionApp = (TdscAuctionApp) appList.get(i);
				tdscAuctionAppDao.delete(tdscAuctionApp);
			}
		}
		// �����ֳ�������Ϣ
		saveTdscAuctionAppList(tdscAuctionAppList);
	}

	public void produceXcjjXml(String appId) {

		// ����������CastorFactory����bean��mapxml������Ҫ�����������Ŀ��xml�ļ�
		TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		tdscBaseQueryCondition.setAppId(appId);

		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(tdscBaseQueryCondition);
		List list = findAppListByAppIddesc(appId);

		String blockAuctionInfoMapPath = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "xcjjMap.xml";// Դxml�ļ���·��
		String transferModeShort = "";// ���÷�ʽ�ļ�ƣ��������xml�ļ�������
		String endTime = "";// xml�ļ�����Чʱ�䣬��ȷ���룬�������xml�ļ������ƣ�ϵͳ��ǰ���ڴ�����Чʱ���е�����ʱ�����ļ��ƶ�������Ŀ¼�£�
		String blockAuctionInfoClassName = "";// �ñ������xcjjMap.xml��Ӧbean�����ɵ�Ŀ��xml�ļ�������
		String dapingPath = PropertiesUtil.getInstance().getProperty("daping_path");// ��������Ŀ¼����Ŀ¼����������Ŀ¼��ÿ����Ŀ¼��Ӧһ����Ϣ����Ҫ��
		String xcjjPath = "";// ��������Ŀ¼���ֳ�������Ŀ¼(xcjj)��·��
		String blockAuctionInfoXmlSP = "";// �ñ������xcjjMap.xml��Ӧbean�����ɵ�Ŀ��xml�ļ�·�����ļ���
		if ("3107".equals(tdscBlockAppView.getTransferMode())) {
			transferModeShort = "Z";
			// ��Ϊ����ͬһ����ж��������ֳ����ۻᣬ����ʱ���ʽΪyyyyMMdd�ȽϺô���
			endTime = DateUtil.date2String(tdscBlockAppView.getOpeningDate(), "yyyyMMdd");
		} else if ("3103".equals(tdscBlockAppView.getTransferMode())) {
			transferModeShort = "P";
			// ��Ϊ����ͬһ����ж��������ֳ����ۻᣬ����ʱ���ʽΪyyyyMMdd�ȽϺô���
			endTime = DateUtil.date2String(tdscBlockAppView.getActionDate(), "yyyyMMdd");
		} else if ("3104".equals(tdscBlockAppView.getTransferMode())) {
			transferModeShort = "G";
			// ��Ϊ����ͬһ����ж��������ֳ����ۻᣬ����ʱ���ʽΪyyyyMMdd�ȽϺô���
			endTime = DateUtil.date2String(tdscBlockAppView.getSceBidDate(), "yyyyMMdd");
		}

		if (StringUtils.isBlank(transferModeShort)) {
			transferModeShort = "0";
		}

		if (StringUtils.isBlank(endTime)) {
			endTime = "19000101";// 1900��01��01��
		}

		BlockAuctionInfo blockAuctionInfo = new BlockAuctionInfo();
		int currentNum = 0;// �ִ�
		String haoPai = "";// ���ۺ���
		BigDecimal currentPrice = new BigDecimal(0);// ��ǰ����
		BigDecimal totalBuildingArea = new BigDecimal(0);// �ܽ������
		BigDecimal initPrice = tdscBlockAppView.getInitPrice();// ��ʼ��
		BigDecimal totalArea = new BigDecimal(0);// ���������
		List blockAuctionPriceList = new ArrayList();

		blockAuctionInfo.setBlockNoticeNo(tdscBlockAppView.getBlockNoticeNo());// �ؿ���

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

				// �ִ�
				currentNum = list.size() - i;
				blockAuctionPrice.setCurrentNum(currentNum + "");

				// ���ۺ���
				haoPai = tdscAuctionApp.getHaoPai();
				blockAuctionPrice.setHaoPai(haoPai);

				// ��ǰ�ܼ�
				currentPrice = tdscAuctionApp.getAddPrice();
				blockAuctionPrice.setCurrentPrice(currentPrice.toString().trim());

				// ¥��ؼ�Ϊ��ǰ�ܼ۳��Եؿ�滮�������
				if (totalBuildingArea.intValue() > 0) {
					if ("102".equals(tdscBlockAppView.getBlockQuality())) {// ��Ӫ���õأ�ʹ��¥��ؼۣ�Ԫ/ƽ���ף���¥��ؼ�=���ۡ¹滮�������
						blockAuctionPrice.setUnitPrice(currentPrice.multiply(new BigDecimal(10000)).divide(totalBuildingArea, 2, BigDecimal.ROUND_HALF_UP).toString());
					} else {// ��ҵ�õأ�ʹ��Ķ���ۣ���Ԫ/Ķ����Ķ����=���ۡ����������666.67
						blockAuctionPrice.setUnitPrice(currentPrice.multiply(new BigDecimal(666.67)).divide(totalArea, 2, BigDecimal.ROUND_HALF_UP).toString());
					}
				}

				if (initPrice != null && initPrice.intValue() > 0) {
					BigDecimal premium = currentPrice.subtract(initPrice);// ���
					blockAuctionPrice.setPremiumRate(premium.multiply(new BigDecimal(100)).divide(initPrice, 2, BigDecimal.ROUND_HALF_UP).toString());
				}
			}
			blockAuctionPriceList.add(blockAuctionPrice);
		}

		blockAuctionInfo.setList(blockAuctionPriceList);

		blockAuctionInfoClassName = transferModeShort + "_" + endTime;
		xcjjPath = dapingPath + "xcjj\\";
		blockAuctionInfoXmlSP = xcjjPath + blockAuctionInfoClassName + ".xml";// ���xcjjMap.xml��Ӧbean�����ɵ�Ŀ��xml�ļ�·�����ļ���

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
	 * �ý��׿��ź� appid �������߱���
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
	 * ���������˵ĵؿ飬�����ĵ�һ�������˼���һ�ֹ��ƣ��жϸõؿ��Ƿ��Ѿ��м�¼�ڱ���, ��д��� TDSC_LISTING_APP and
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
		// curr_price=�ÿ����صĳ�ʼ�۸�, curr_round=1, list_date=now date, ykt_xh=����,
		// list_cert=�ʸ�ȷ������)
		// 2. save TDSC_LISTING_APP (list_app_id=+1, listing_id, listing_ser=1,
		// price_type=1, list_cert=, list_price=, list_date=, ytk_xh=, app_id)
		List infoList = tdscListingInfoDao.findListingInfo(appId);
		if (infoList != null && infoList.size() > 0) {
			// �õؿ��Ѿ����ڹ��Ƽ�¼�����ؼ���
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

	// �����˱���ʱ������ؿ�����һ�ֹ��Ƽ�¼�����Ƽ۸�Ϊ��ʼ�ۣ�ʱ��Ϊ������ʼʱ��
	public void insertListingAppOfPurposePerson(TdscBidderApp tdscBidderApp, String purposeAppId) {
		
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
				tdscListingInfo.setYktXh(tdscBidderApp.getYktXh());
				tdscListingInfo.setListCert(tdscBidderApp.getCertNo());
				tdscListingInfo.setListNo(tdscBidderApp.getConNum());
				tdscListingInfo.setCurrPrice(tdscBlockTranApp.getInitPrice());
				tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.save(tdscListingInfo);
			}

			TdscListingApp tdscListingApp = tdscListingAppDao.getListingAppOfPurposePerson(appIds[i], tdscBidderApp.getCertNo());
			
			if (tdscListingApp == null) {// ������û�ж���������������ؿ���й��ƣ��������Ƽ�¼�����Ƽ۸�Ϊ��ʼ�ۣ�����ʱ��Ϊ������ʼʱ��
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
