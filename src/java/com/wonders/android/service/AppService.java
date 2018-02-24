package com.wonders.android.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;











import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderView;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;

import java.util.*;

import com.wonders.android.AppConsts;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.localtrade.dao.TdscListingAppDao;
import com.wonders.tdsc.localtrade.dao.TdscListingInfoDao;
import com.wonders.tdsc.publishinfo.dao.TdscNoticeAppDao;
import com.wonders.tdsc.tdscbase.dao.TdscBidderViewDao;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.bo.PersonInfo;
import com.wonders.wsjy.bo.TdscTradeView;
import com.wonders.wsjy.dao.PersonInfoDAO;
import com.wonders.wsjy.dao.TdscTradeViewDao;

import examples.newsgroups;

public class AppService {
	private TdscNoticeAppDao tdscPublishInfoNoticeAppDao;
	private TdscBidderAppDao tdscBidderAppDao;
	private TdscListingAppDao tdscListingAppDao;
	private TdscTradeViewDao tdscTradeViewDao;
	private TdscBidderPersonAppDao tdscBidderPersonAppDao;
	private TdscBidderViewDao tdscBidderViewDao;
	private PersonInfoDAO personInfoDao;
	private TdscBlockTranAppDao tdscBlockTranAppDao;
	private TdscBlockPlanTableDao tdscBlockPlanTableDao;
	private TdscListingInfoDao tdscListingInfoDao;

	public PersonInfoDAO getPersonInfoDao() {
		return personInfoDao;
	}

	public void setPersonInfoDao(PersonInfoDAO personInfoDao) {
		this.personInfoDao = personInfoDao;
	}

	public void setTdscPublishInfoNoticeAppDao(
			TdscNoticeAppDao tdscPublishInfoNoticeAppDao) {
		this.tdscPublishInfoNoticeAppDao = tdscPublishInfoNoticeAppDao;
	}

	public void setTdscListingAppDao(TdscListingAppDao tdscListingAppDao) {
		this.tdscListingAppDao = tdscListingAppDao;
	}

	public void setTdscTradeViewDao(TdscTradeViewDao tdscTradeViewDao) {
		this.tdscTradeViewDao = tdscTradeViewDao;
	}

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscBidderPersonAppDao(
			TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public void setTdscBidderViewDao(TdscBidderViewDao tdscBidderViewDao) {
		this.tdscBidderViewDao = tdscBidderViewDao;
	}



	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}


	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}


	public void setTdscListingInfoDao(TdscListingInfoDao tdscListingInfoDao) {
		this.tdscListingInfoDao = tdscListingInfoDao;
	}


	public PageList getAssignNoticePageList(TdscNoticeAppCondition condition){
		PageList tdscNoticeAppPageList=tdscPublishInfoNoticeAppDao.getAssignNoticePageList(condition);
		return tdscNoticeAppPageList;
	}

	/**
	 * 查询观摩公告列表
	 * 
	 * @return
	 */
	public List queryMonitorPlanList() {
		List list = tdscTradeViewDao.queryMonitorPlanList();
		ArrayList result = new ArrayList();
		Map<String, TdscTradeView> results=new HashMap<String, TdscTradeView>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				TdscTradeView v = (TdscTradeView) list.get(i);
				v.setTradeStatus(getCurrBlockTranStatus(v));
				String noticeId = v.getNoticeId();
				if (results.containsKey(noticeId)) {
					int tradeStatus=Integer.parseInt(v.getTradeStatus());
					TdscTradeView ttv=results.get(noticeId);
					if (tradeStatus<Integer.parseInt(ttv.getTradeStatus())) {
						ttv.setTradeStatus(tradeStatus+"");
					}
					continue;
				}
				
				results.put(noticeId, v);
				result.add(v);			
			}
		}
		return result;
	}

	/**
	 * 查询观摩地块列表
	 * 
	 * @param planId
	 * @return
	 */
	public List queryMonitorBlockList(String planId) {
		return tdscTradeViewDao.getTdscTradeViewAppByPlanId(planId);
	}

	/**
	 * 依据appId查找tdscTradeView数据
	 * 
	 * @param appId
	 * @return
	 */
	public TdscTradeView getTdscTradeView(String appId) {
		return tdscTradeViewDao.getTdscTradeViewAppById(appId);
	}

	/**
	 * 根据地块的appId查找当前用户的交易信息
	 * 
	 * @param appId
	 *            地块ID
	 * @param flag
	 *            是否校验当前竞买人是否可以报价(可以报价完成资格审核并领取了号牌)
	 * @return
	 */
	public TdscBidderApp getCurrUserTradeByAppId(HttpServletRequest request,
			String appId, boolean flag) {
		String userId = this.getUserInfo(request);
		if (StringUtils.isNotEmpty(userId)) {
			TdscBidderCondition condition = new TdscBidderCondition();
			condition.setAppId(appId);
			condition.setUserId(userId);
			List list = this.tdscBidderAppDao
					.findBidderAppListByCondition(condition);
			if (list.size() > 0) {
				TdscBidderApp bidderApp = (TdscBidderApp) list.get(0);
				if (flag) {
					if (StringUtils.isNotEmpty(bidderApp.getCertNo())
							&& StringUtils.isNotEmpty(bidderApp.getConNum())) {
						return bidderApp;
					} else {
						return null;
					}
				}
				return bidderApp;
			}
		}
		return null;

	}

	/**
	 * 获取地块的竞买人申请人数(已取得竞买资格的)
	 * 
	 * @param appId
	 * @return
	 */
	public int getBlockBidderNum(String appId) {
		List list = this.tdscBidderAppDao.findHaveCertNoBidderList(appId);
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
	}

	/**
	 * 获取当前用户ID
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return true 通过 false 不通过
	 */
	public String getUserInfo(HttpServletRequest request) {
		String certNo = (String) request.getSession().getAttribute("certNo");
		return certNo;
	}

	/**
	 * 获取当前地块的报价信息
	 * 
	 * @param appId
	 * @return
	 */
	public List findListingApp(String appId) {
		List list = this.tdscListingAppDao.queryListingAppListByAppId(appId);
		return list;
	}

	/**
	 * 获取当前地块的交易状态
	 * 
	 * @param appId
	 * @return
	 */
	public String getCurrBlockTranStatus(TdscTradeView tradeView) {
		Date nowDate = new Date();
		if (!"00".equals(tradeView.getTranResult())) {
			// 交易结束的地块
			return AppConsts.TRAN_STATUS_END;
		} else if (nowDate.after(tradeView.getAccAppStatDate())
				&& nowDate.before(tradeView.getListStartDate())) {
			// 处于受理还未开始挂牌阶段
			return AppConsts.TRAN_STATUS_ACC;
		} else if (nowDate.after(tradeView.getListStartDate())
				&& nowDate.before(tradeView.getListEndDate())) {
			// 处于挂牌期间
			return AppConsts.TRAN_STATUS_LISTING;
		} else if (tradeView.getTradeStatus() != null
				&& nowDate.after(tradeView.getSceBidDate())
				&& tradeView.getOnLineStatDate() != null
				&& tradeView.getOnLineEndDate() == null) {
			// 处于竞价期
			if ("2".equals(tradeView.getTradeStatus())) {
				// tradeStatus==2时处于等待期
				return AppConsts.TRAN_STATUS_WAITING;
			}
			return AppConsts.TRAN_STATUS_AUCTION;
		}
		return null;
	}

	public TdscBidderApp getPurposeBidderAppByAppId(String appId) {
		return tdscBidderAppDao.getPurposeBidderAppByAppId(appId);
	}

	/**
	 * 通过bidderId查找对应的personapp
	 * 
	 * @param bidderId
	 * @return
	 */
	public TdscBidderPersonApp getBidderPersonAppByBidderId(String bidderId) {
		TdscBidderPersonApp bidderPersonApp = tdscBidderPersonAppDao
				.getOneBidderByBidderId(bidderId);
		return bidderPersonApp;
	}

	/**
	 * 保存竞买人信息
	 * 
	 * @param tdscBidderApp
	 * @param bidderPersonApp
	 */
	public void persistenBidder(TdscBidderApp tdscBidderApp,
			TdscBidderPersonApp bidderPersonApp) {
		tdscBidderAppDao.saveOrUpdate(tdscBidderApp);
		bidderPersonApp.setBidderId(tdscBidderApp.getBidderId());
		tdscBidderPersonAppDao.saveOrUpdate(bidderPersonApp);
	}

	/**
	 * 获得出让公告列表
	 * 
	 * @param condition
	 * @return
	 */
	public PageList getAssignNoticesPageList(TdscNoticeAppCondition condition) {
		// 获得公告列表所指向的固定路径
		String assign_notice_url = PropertiesUtil.getInstance().getProperty(
				"assign_notice_url");
		// 获得出让公告列表：查询出让公告列表：ifReleased='1' ;noticeDate倒叙
		PageList tdscNoticeAppPageList = tdscPublishInfoNoticeAppDao
				.getAssignNoticePageList(condition);
		List tdscNoticeAppList = tdscNoticeAppPageList.getList();
		// 循环出让公告列表，将noticeDate转换为String类型，为页面和Js取值，新增加两个虚字段stringDate url
		for (int i = 0; i < tdscNoticeAppList.size(); i++) {
			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppList
					.get(i);
			Timestamp noticeDate = tdscNoticeApp.getNoticeDate();
			String date = "";
			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
			date = dfm.format(noticeDate);
			tdscNoticeApp.setStringDate(date);
			// 获取公告id，并拼接公告访问路径
			String recordId = tdscNoticeApp.getRecordId();
			String url = assign_notice_url + recordId + ".html";
			tdscNoticeApp.setUrl(url);
		}
		return tdscNoticeAppPageList;
	}

	/**
	 * 获得成交公告列表
	 * 
	 * @param condition
	 * @return
	 */
	public PageList getTradeNoticePageList(TdscNoticeAppCondition condition) {
		// 获得公告列表所指向的固定路径
		String assign_notice_url = PropertiesUtil.getInstance().getProperty(
				"assign_notice_url");
		// 获取成交公告列表：ifResultPublish='1' resultPublishDate倒叙
		PageList tdscNoticeAppPageList = tdscPublishInfoNoticeAppDao
				.getTradeNoticePageList(condition);
		List tdscNoticeAppList = tdscNoticeAppPageList.getList();

		// 循环出让公告列表，将noticeDate转换为String类型，为页面和Js取值，新增加两个虚字段stringDate url
		for (int i = 0; i < tdscNoticeAppList.size(); i++) {
			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppList
					.get(i);
			Timestamp resultPublishDate = tdscNoticeApp.getResultPublishDate();
			String date = "";
			if (resultPublishDate != null) {
				DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
				date = dfm.format(resultPublishDate);
				tdscNoticeApp.setStringDate(date);
			}
			// 获取公告id，并拼接公告访问路径
			String resulrRecordId = tdscNoticeApp.getResultRecordId();
			String url = assign_notice_url + resulrRecordId + ".html";
			tdscNoticeApp.setUrl(url);
		}
		return tdscNoticeAppPageList;
	}

	public List queryBiddersWithConNum(String appId) {
		return this.tdscBidderAppDao.queryBidderAppListLikeAppId(appId);
	}

	/**
	 * 根据userId获得申购列表
	 * 
	 * @param userId
	 * @return
	 */
	public List queryBidderOrdersList(String userId) {
		List list = this.tdscBidderViewDao.queryBidderOrdersList(userId);
		// 将保证金到账情况以文字形式显示：00 足额到账 ；01 逾期足额 ；其余 未足额到账
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				TdscBidderView tdscBidderView = (TdscBidderView) list.get(i);
				String gzj_dzqk = tdscBidderView.getBzjDzqk();
				if ("00".equals(gzj_dzqk)) {
					tdscBidderView.setBzjDzqk("足额到账");
				} else if ("01".equals(gzj_dzqk)) {
					tdscBidderView.setBzjDzqk("逾期足额");
				} else {
					tdscBidderView.setBzjDzqk("未足额到账");
				}
				/*
				 * if("02".equals(gzj_dzqk)){
				 * tdscBidderView.setBzjDzqk("逾期未足额"); } if(gzj_dzqk==null){
				 * tdscBidderView.setBzjDzqk("未足额到账"); }
				 */
			}
		}
		return list;
	}

	/**
	 * 我的交易
	 * 
	 * @param userId
	 * @return
	 */
	public List queryMyTradeList(String userId) {
		return tdscBidderViewDao.getBidderViewListByUserId(userId);
	}

	public TdscBidderView getMyTrade(String appId, String bidderId) {
		return tdscBidderViewDao.getMyTrade(appId, bidderId);
	}

	/**
	 * 保存CA里的竞买人信息
	 * 
	 * @param certNo
	 * @param keyType
	 */
	public void saveWxPersonInfo(HttpServletRequest request) {
		String keyType = "";
		String jmrName = "";
		String jmrCode = "";
		String certNo = "";
		if (request.getSession().getAttribute("certNo") != null) {
			certNo = (String) request.getSession().getAttribute("certNo");
		}
		if (request.getSession().getAttribute("keyType") != null) {
			keyType = (String) request.getSession().getAttribute("keyType");
		}
		if (request.getSession().getAttribute("jmrName") != null) {
			jmrName = (String) request.getSession().getAttribute("jmrName");
		}
		if (request.getSession().getAttribute("jmrCode") != null) {
			jmrCode = (String) request.getSession().getAttribute("jmrCode");
		}
		/**
		 * 取得KEY信息后对比数据库若不存在数据则进行插入数据工作，若存在则不处理
		 */
		PersonInfo personInfo = this.getPersonInfo(certNo);
		if (personInfo == null) {
			personInfo = new PersonInfo();
			personInfo.setBidderId(certNo);
			// 判断keyType是否为法人若为法人则值为12,否则为...
			if (keyType.startsWith("ent")) {
				personInfo.setBidderProperty("12");
			}

			if (keyType.startsWith("user")) {
				personInfo.setBidderProperty("11");
			}
			// 假如竞买人名称和竞买人身份认证唯一标识没有则将资格证书编号给他.
			// (由于正式使用后CA信息内肯定含有这些信息，所以此逻辑可在测试时候使用,单也不影响正式环境的运行。)
			if (StringUtils.isEmpty(jmrName)) {
				jmrName = certNo;
			}
			if (StringUtils.isEmpty(jmrCode)) {
				jmrCode = certNo;
			}
			personInfo.setBidderName(jmrName);
			personInfo.setOrgNo(jmrCode);
			personInfo.setCreateDate(new Date());
			personInfo.setLastUpdateDate(new Date());
			this.getPersonInfoDao().save(personInfo);
		}
	}

	/**
	 * 取得竞买人信息(CA)
	 * 
	 * @param bidderId
	 * @return
	 */
	public PersonInfo getPersonInfo(String bidderId) {
		if (StringUtils.isEmpty(bidderId))
			return null;
		return this.personInfoDao.getPersonInfo(bidderId);
	}


	public List queryBidderAppListForme(String userId, String noticeId) {
		return this.tdscBidderAppDao.queryBidderAppListForme(userId, noticeId);
	}


	public void saveSelectedCon(String bidderId, String haopai, String certNo) {
		TdscBidderApp bidderApp = (TdscBidderApp) tdscBidderAppDao.get(bidderId);
		bidderApp.setConNum(haopai);
		bidderApp.setConTime(new Timestamp(System.currentTimeMillis()));
		if(StringUtils.isNotBlank(certNo)){
			bidderApp.setCertNo(certNo);
		}
		this.saveSameNoticeHaoPai(bidderApp.getNoticeId(), bidderApp.getUserId(), haopai);
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


	public List findTdscBidderAppListByNoticeId(String noticeId) {
		List list = this.tdscBidderAppDao.findBidderAppByAppId(noticeId);
		return list;
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
}
