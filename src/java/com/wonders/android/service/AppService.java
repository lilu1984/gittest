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
	 * ��ѯ��Ħ�����б�
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
	 * ��ѯ��Ħ�ؿ��б�
	 * 
	 * @param planId
	 * @return
	 */
	public List queryMonitorBlockList(String planId) {
		return tdscTradeViewDao.getTdscTradeViewAppByPlanId(planId);
	}

	/**
	 * ����appId����tdscTradeView����
	 * 
	 * @param appId
	 * @return
	 */
	public TdscTradeView getTdscTradeView(String appId) {
		return tdscTradeViewDao.getTdscTradeViewAppById(appId);
	}

	/**
	 * ���ݵؿ��appId���ҵ�ǰ�û��Ľ�����Ϣ
	 * 
	 * @param appId
	 *            �ؿ�ID
	 * @param flag
	 *            �Ƿ�У�鵱ǰ�������Ƿ���Ա���(���Ա�������ʸ���˲���ȡ�˺���)
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
	 * ��ȡ�ؿ�ľ�������������(��ȡ�þ����ʸ��)
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
	 * ��ȡ��ǰ�û�ID
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return true ͨ�� false ��ͨ��
	 */
	public String getUserInfo(HttpServletRequest request) {
		String certNo = (String) request.getSession().getAttribute("certNo");
		return certNo;
	}

	/**
	 * ��ȡ��ǰ�ؿ�ı�����Ϣ
	 * 
	 * @param appId
	 * @return
	 */
	public List findListingApp(String appId) {
		List list = this.tdscListingAppDao.queryListingAppListByAppId(appId);
		return list;
	}

	/**
	 * ��ȡ��ǰ�ؿ�Ľ���״̬
	 * 
	 * @param appId
	 * @return
	 */
	public String getCurrBlockTranStatus(TdscTradeView tradeView) {
		Date nowDate = new Date();
		if (!"00".equals(tradeView.getTranResult())) {
			// ���׽����ĵؿ�
			return AppConsts.TRAN_STATUS_END;
		} else if (nowDate.after(tradeView.getAccAppStatDate())
				&& nowDate.before(tradeView.getListStartDate())) {
			// ��������δ��ʼ���ƽ׶�
			return AppConsts.TRAN_STATUS_ACC;
		} else if (nowDate.after(tradeView.getListStartDate())
				&& nowDate.before(tradeView.getListEndDate())) {
			// ���ڹ����ڼ�
			return AppConsts.TRAN_STATUS_LISTING;
		} else if (tradeView.getTradeStatus() != null
				&& nowDate.after(tradeView.getSceBidDate())
				&& tradeView.getOnLineStatDate() != null
				&& tradeView.getOnLineEndDate() == null) {
			// ���ھ�����
			if ("2".equals(tradeView.getTradeStatus())) {
				// tradeStatus==2ʱ���ڵȴ���
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
	 * ͨ��bidderId���Ҷ�Ӧ��personapp
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
	 * ���澺������Ϣ
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
	 * ��ó��ù����б�
	 * 
	 * @param condition
	 * @return
	 */
	public PageList getAssignNoticesPageList(TdscNoticeAppCondition condition) {
		// ��ù����б���ָ��Ĺ̶�·��
		String assign_notice_url = PropertiesUtil.getInstance().getProperty(
				"assign_notice_url");
		// ��ó��ù����б���ѯ���ù����б�ifReleased='1' ;noticeDate����
		PageList tdscNoticeAppPageList = tdscPublishInfoNoticeAppDao
				.getAssignNoticePageList(condition);
		List tdscNoticeAppList = tdscNoticeAppPageList.getList();
		// ѭ�����ù����б���noticeDateת��ΪString���ͣ�Ϊҳ���Jsȡֵ���������������ֶ�stringDate url
		for (int i = 0; i < tdscNoticeAppList.size(); i++) {
			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppList
					.get(i);
			Timestamp noticeDate = tdscNoticeApp.getNoticeDate();
			String date = "";
			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
			date = dfm.format(noticeDate);
			tdscNoticeApp.setStringDate(date);
			// ��ȡ����id����ƴ�ӹ������·��
			String recordId = tdscNoticeApp.getRecordId();
			String url = assign_notice_url + recordId + ".html";
			tdscNoticeApp.setUrl(url);
		}
		return tdscNoticeAppPageList;
	}

	/**
	 * ��óɽ������б�
	 * 
	 * @param condition
	 * @return
	 */
	public PageList getTradeNoticePageList(TdscNoticeAppCondition condition) {
		// ��ù����б���ָ��Ĺ̶�·��
		String assign_notice_url = PropertiesUtil.getInstance().getProperty(
				"assign_notice_url");
		// ��ȡ�ɽ������б�ifResultPublish='1' resultPublishDate����
		PageList tdscNoticeAppPageList = tdscPublishInfoNoticeAppDao
				.getTradeNoticePageList(condition);
		List tdscNoticeAppList = tdscNoticeAppPageList.getList();

		// ѭ�����ù����б���noticeDateת��ΪString���ͣ�Ϊҳ���Jsȡֵ���������������ֶ�stringDate url
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
			// ��ȡ����id����ƴ�ӹ������·��
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
	 * ����userId����깺�б�
	 * 
	 * @param userId
	 * @return
	 */
	public List queryBidderOrdersList(String userId) {
		List list = this.tdscBidderViewDao.queryBidderOrdersList(userId);
		// ����֤���������������ʽ��ʾ��00 ���� ��01 ������� ������ δ����
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				TdscBidderView tdscBidderView = (TdscBidderView) list.get(i);
				String gzj_dzqk = tdscBidderView.getBzjDzqk();
				if ("00".equals(gzj_dzqk)) {
					tdscBidderView.setBzjDzqk("����");
				} else if ("01".equals(gzj_dzqk)) {
					tdscBidderView.setBzjDzqk("�������");
				} else {
					tdscBidderView.setBzjDzqk("δ����");
				}
				/*
				 * if("02".equals(gzj_dzqk)){
				 * tdscBidderView.setBzjDzqk("����δ���"); } if(gzj_dzqk==null){
				 * tdscBidderView.setBzjDzqk("δ����"); }
				 */
			}
		}
		return list;
	}

	/**
	 * �ҵĽ���
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
	 * ����CA��ľ�������Ϣ
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
		 * ȡ��KEY��Ϣ��Ա����ݿ�����������������в������ݹ������������򲻴���
		 */
		PersonInfo personInfo = this.getPersonInfo(certNo);
		if (personInfo == null) {
			personInfo = new PersonInfo();
			personInfo.setBidderId(certNo);
			// �ж�keyType�Ƿ�Ϊ������Ϊ������ֵΪ12,����Ϊ...
			if (keyType.startsWith("ent")) {
				personInfo.setBidderProperty("12");
			}

			if (keyType.startsWith("user")) {
				personInfo.setBidderProperty("11");
			}
			// ���羺�������ƺ;����������֤Ψһ��ʶû�����ʸ�֤���Ÿ���.
			// (������ʽʹ�ú�CA��Ϣ�ڿ϶�������Щ��Ϣ�����Դ��߼����ڲ���ʱ��ʹ��,��Ҳ��Ӱ����ʽ���������С�)
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
	 * ȡ�þ�������Ϣ(CA)
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


	public List findTdscBidderAppListByNoticeId(String noticeId) {
		List list = this.tdscBidderAppDao.findBidderAppByAppId(noticeId);
		return list;
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
}
