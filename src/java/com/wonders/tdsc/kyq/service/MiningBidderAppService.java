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
	 * 查询竞买人信息
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
	 * 保存竞买人信息
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
			// 修改JY_MINING_BIDDER_APP表，选中的地块 app_id 用逗号分隔，写入bidder表的mining_id
			miningBidderApp.setMiningId(miningIds);
			if(StringUtils.isEmpty(miningBidderApp.getCertNo())){
				miningBidderApp.setCertNo(genAppNum("K", idSpringManager));//生成资格证书编号，“K”表示矿业权
			}
			return (MiningBidderApp)miningBidderAppDao.saveOrUpdate(miningBidderApp);			 
		} else {
			// 新增JY_MINING_BIDDER_APP表，选中的地块 app_id 用逗号分隔，写入bidder表的mining_id
			miningBidderApp.setMiningId(miningIds);
			miningBidderApp.setCertNo(genAppNum("K", idSpringManager));//生成资格证书编号，“K”表示矿业权
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
	 * 生成业务流水号
	 * 
	 * @param busType
	 *            业务类型
	 * @param incrementIdManager
	 *            顺序号生成器
	 * @return 生成好的业务流水号(业务类型+年份+顺序号)
	 */
	public String genAppNum(String busType, IdSpringManager idSpringManager) {
		// 取得当前年份
		String curYear = DateUtil.date2String(new Date(), "yyyy");
		// 将取得的编号设置成有效字符串
		String longBookId = ("00000" + idSpringManager.getIncrementId(busType + curYear));
		// 取得有效字符串
		String bookId = longBookId.substring(longBookId.length() - 5, longBookId.length());
		// 返回生成好的申请号(业务号+年份+顺序号)
		return busType + curYear + bookId;
	}
	
	//意向人报名时对意向地块插入第一轮挂牌记录，挂牌价格为起始价，时间为公告起始时间
	public void insertListingAppOfPurposePerson(MiningBidderApp miningBidderApp, String purposeAppId) {
		
		String[] appIds = purposeAppId.split(",");// 该竞买人的意向地块ID
		for (int i=0; null!=appIds && i < appIds.length; i++) {
			MiningTranApp miningTranApp= miningtranAppDao.getMiningTranAppByTranAppId(appIds[i]);
			
			KyqNotice kyqNotice = kyqNoticeDao.getNoticeByNoticeId(miningBidderApp.getNoticeId()); 
			
			TdscListingInfo tdscListingInfo = null;			
			tdscListingInfo = tdscListingInfoDao.getTdscListingInfoByAppId(appIds[i]);// 查找该地块是否挂牌报价过			
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
			
			if (tdscListingApp == null) {// 意向人没有对他购买的这个意向地块进行挂牌，则插入挂牌记录，挂牌价格为起始价，挂牌时间为公告起始时间
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
