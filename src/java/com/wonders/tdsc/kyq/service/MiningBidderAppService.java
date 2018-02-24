package com.wonders.tdsc.kyq.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.jdbc.BaseService;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscListingApp;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.KyqNotice;
import com.wonders.tdsc.kyq.bo.MiningBidderApp;
import com.wonders.tdsc.kyq.bo.MiningTranApp;
import com.wonders.tdsc.kyq.dao.KyqNoticeDao;
import com.wonders.tdsc.kyq.dao.MiningBidderAppDao;
import com.wonders.tdsc.kyq.dao.MiningTranAppDao;
import com.wonders.tdsc.localtrade.dao.TdscListingAppDao;
import com.wonders.tdsc.localtrade.dao.TdscListingInfoDao;

public class MiningBidderAppService extends BaseService {
	
	private MiningBidderAppDao	miningBidderAppDao;
	
	private IdSpringManager idSpringManager;
	
	private KyqNoticeDao kyqNoticeDao;
	
	private MiningTranAppDao miningtranAppDao;	
	
	private TdscListingInfoDao tdscListingInfoDao;
	
	private TdscListingAppDao tdscListingAppDao;

	public void setMiningBidderAppDao(MiningBidderAppDao miningBidderAppDao) {
		this.miningBidderAppDao = miningBidderAppDao;
	}

	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}
	
	public void setKyqNoticeDao(KyqNoticeDao kyqNoticeDao) {
		this.kyqNoticeDao = kyqNoticeDao;
	}
	
	public void setMiningtranAppDao(MiningTranAppDao miningtranAppDao) {
		this.miningtranAppDao = miningtranAppDao;
	}
	
	public void setTdscListingAppDao(TdscListingAppDao tdscListingAppDao) {
		this.tdscListingAppDao = tdscListingAppDao;
	}

	public void setTdscListingInfoDao(TdscListingInfoDao tdscListingInfoDao) {
		this.tdscListingInfoDao = tdscListingInfoDao;
	}

	/**
	 * ��ѯ��������Ϣ
	 * 
	 * @param condition
	 * @return
	 */
	public List queryMiningBidderAppList(KyqBaseQueryCondition condition) {
		List miningBidderList = this.miningBidderAppDao.findBidderList(condition);
		return miningBidderList;
	}

	public List queryminingBidderAppListByBidderId(String bidderId) {
		List miningBidderAppList = this.miningBidderAppDao.queryMiningBidderAppListByBidderId(bidderId);
		return miningBidderAppList;
	}

	/**
	 * ���澺������Ϣ
	 * 
	 * @param miningBidderApp
	 * @return
	 */
	public MiningBidderApp saveBidderApp(MiningBidderApp miningBidderApp, String[] tranAppIds) {
		String miningIds = "";
		if(tranAppIds!=null && tranAppIds.length>0){
			for(int i=0; i<tranAppIds.length; i++){
				if(i==0){
					miningIds = tranAppIds[i];
				}else{
					miningIds += "," + tranAppIds[i];
				}
			}
		}
		if (miningBidderApp != null && StringUtils.isNotBlank(miningBidderApp.getBidderId())) {
			// �޸�JY_MINING_BIDDER_APP��ѡ�еĵؿ� app_id �ö��ŷָ���д��bidder���mining_id
			miningBidderApp.setMiningId(miningIds);
			if(StringUtils.isEmpty(miningBidderApp.getCertNo())){
				miningBidderApp.setCertNo(genAppNum("K", idSpringManager));//�����ʸ�֤���ţ���K����ʾ��ҵȨ
			}
			return (MiningBidderApp)miningBidderAppDao.saveOrUpdate(miningBidderApp);			 
		} else {
			// ����JY_MINING_BIDDER_APP��ѡ�еĵؿ� app_id �ö��ŷָ���д��bidder���mining_id
			miningBidderApp.setMiningId(miningIds);
			miningBidderApp.setCertNo(genAppNum("K", idSpringManager));//�����ʸ�֤���ţ���K����ʾ��ҵȨ
			return (MiningBidderApp)miningBidderAppDao.save(miningBidderApp);
		}
	}

	public void delByBidderId(String bidderId) {
		miningBidderAppDao.deleteById(bidderId);
	}
	
	public List queryMiningBidderAppListByMiningId(String miningId){
		return miningBidderAppDao.queryMiningBidderAppListByMiningId(miningId);
	}
	
	/**
	 * ����ҵ����ˮ��
	 * 
	 * @param busType
	 *            ҵ������
	 * @param incrementIdManager
	 *            ˳���������
	 * @return ���ɺõ�ҵ����ˮ��(ҵ������+���+˳���)
	 */
	public String genAppNum(String busType, IdSpringManager idSpringManager) {
		// ȡ�õ�ǰ���
		String curYear = DateUtil.date2String(new Date(), "yyyy");
		// ��ȡ�õı�����ó���Ч�ַ���
		String longBookId = ("00000" + idSpringManager.getIncrementId(busType + curYear));
		// ȡ����Ч�ַ���
		String bookId = longBookId.substring(longBookId.length() - 5, longBookId.length());
		// �������ɺõ������(ҵ���+���+˳���)
		return busType + curYear + bookId;
	}
	
	//�����˱���ʱ������ؿ�����һ�ֹ��Ƽ�¼�����Ƽ۸�Ϊ��ʼ�ۣ�ʱ��Ϊ������ʼʱ��
	public void insertListingAppOfPurposePerson(MiningBidderApp miningBidderApp, String purposeAppId) {
		
		String[] appIds = purposeAppId.split(",");// �þ����˵�����ؿ�ID
		for (int i=0; null!=appIds && i < appIds.length; i++) {
			MiningTranApp miningTranApp= miningtranAppDao.getMiningTranAppByTranAppId(appIds[i]);
			
			KyqNotice kyqNotice = kyqNoticeDao.getNoticeByNoticeId(miningBidderApp.getNoticeId()); 
			
			TdscListingInfo tdscListingInfo = null;			
			tdscListingInfo = tdscListingInfoDao.getTdscListingInfoByAppId(appIds[i]);// ���Ҹõؿ��Ƿ���Ʊ��۹�			
			if (tdscListingInfo == null) {
				tdscListingInfo = new TdscListingInfo();
				tdscListingInfo.setAppId(appIds[i]);
				tdscListingInfo.setCurrRound(new BigDecimal(1));
				tdscListingInfo.setListDate(kyqNotice.getNoticePublishDate());
				tdscListingInfo.setYktXh(miningBidderApp.getCertNo());
				tdscListingInfo.setListCert(miningBidderApp.getCertNo());
				tdscListingInfo.setListNo(miningBidderApp.getHaoPai());
				tdscListingInfo.setCurrPrice(miningTranApp.getInitPrice());
				tdscListingInfo = (TdscListingInfo) tdscListingInfoDao.save(tdscListingInfo);
			}

			TdscListingApp tdscListingApp = tdscListingAppDao.getListingAppOfPurposePerson(appIds[i], miningBidderApp.getCertNo());
			
			if (tdscListingApp == null) {// ������û�ж���������������ؿ���й��ƣ��������Ƽ�¼�����Ƽ۸�Ϊ��ʼ�ۣ�����ʱ��Ϊ������ʼʱ��
				TdscListingApp listingApp = new TdscListingApp();
				listingApp.setListingId(tdscListingInfo.getListingId());
				listingApp.setListingSer(new BigDecimal(1));
				listingApp.setPriceType("11");
				listingApp.setPriceNum(new BigDecimal(1));
				listingApp.setListCert(miningBidderApp.getCertNo());
				listingApp.setListNo(miningBidderApp.getHaoPai());
				listingApp.setListDate(kyqNotice.getNoticePublishDate());
				listingApp.setYktXh(miningBidderApp.getCertNo());
				listingApp.setAppId(appIds[i]);
				listingApp.setListPrice(miningTranApp.getInitPrice());

				tdscListingAppDao.save(listingApp);
			}
		}
	}
}
