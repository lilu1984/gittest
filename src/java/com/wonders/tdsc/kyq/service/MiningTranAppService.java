package com.wonders.tdsc.kyq.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.jdbc.BaseService;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.MiningBidderApp;
import com.wonders.tdsc.kyq.bo.MiningTranApp;
import com.wonders.tdsc.kyq.dao.MiningBidderAppDao;
import com.wonders.tdsc.kyq.dao.MiningTranAppDao;

public class MiningTranAppService extends BaseService {
	private MiningTranAppDao	miningtranAppDao;

	private MiningBidderAppDao	miningBidderAppDao;

	public void setMiningBidderAppDao(MiningBidderAppDao miningBidderAppDao) {
		this.miningBidderAppDao = miningBidderAppDao;
	}

	public void setMiningtranAppDao(MiningTranAppDao miningtranAppDao) {
		this.miningtranAppDao = miningtranAppDao;
	}

	/**
	 * 查询矿山信息
	 * 
	 * @param condition
	 * @return
	 */
	public List queryMiningTranAppList(KyqBaseQueryCondition condition) {
		List tranAppList = this.miningtranAppDao.findMiningList(condition);
		return tranAppList;
	}

	/**
	 * 删除矿山信息
	 * 
	 * @param tranAppId
	 * @return
	 */
	public void deleteMiningTran(String tranAppId) {
		MiningTranApp miningTranApp = miningtranAppDao.getMiningTranAppByTranAppId(tranAppId);

		String miningId = miningTranApp.getTranAppId();

		MiningBidderApp miningBidderApp = miningBidderAppDao.getMiningBidderAppByMiningId(miningId);

		miningtranAppDao.delete(miningTranApp);

		miningBidderAppDao.delete(miningBidderApp);

	}

	/**
	 * 保存矿山信息
	 * 
	 * @param miningTranApp
	 * @param miningBidderApp
	 * @return
	 */
	public void saveMiningTran(MiningTranApp miningTranApp, MiningBidderApp miningBidderApp) {
		// 判断的id是否存在，不存在，做两张表的新增；存在，做Mining和竞买人表的更新
		if (miningTranApp != null && StringUtils.isNotBlank(miningTranApp.getTranAppId())) {
			miningtranAppDao.update(miningTranApp);
			miningBidderAppDao.update(miningBidderApp);
		} else {
			// 1. 新增 JY_MINING_TRAN_APP
			MiningTranApp tApp = (MiningTranApp) miningtranAppDao.save(miningTranApp);
			// 2. 新增 JY_MINING_BIDDER_APP
			miningBidderApp.setMiningId(tApp.getTranAppId());
			miningBidderApp.setPurposeAppId(tApp.getTranAppId());
			miningBidderAppDao.save(miningBidderApp);
		}
	}

	/**
	 * 修改矿山信息
	 * 
	 * @param tranAppId
	 * @return
	 */
	public MiningTranApp getMiningTranAppByTranAppId(String tranAppId) {
		MiningTranApp miningTranApp = miningtranAppDao.getMiningTranAppByTranAppId(tranAppId);
		return miningTranApp;
	}

	/**
	 * 根据公告ID修改矿山信息
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryMiningTranAppListByNoticeId(String noticeId) {
		List list = miningtranAppDao.queryMiningTranAppListByNoticeId(noticeId);
		return list;
	}

	/**
	 * 修改交易信息
	 * 
	 * @param tranAppId
	 * @return
	 */

	public MiningBidderApp getMiningBidderAppByMiningId(String tranAppId) {
		MiningBidderApp miningBidderApp = miningBidderAppDao.getMiningBidderAppByMiningId(tranAppId);
		return miningBidderApp;
	}

	/**
	 * 根据竞买人ID修改矿山信息
	 * 
	 * @param bidderId
	 * @return
	 */
	public List queryMiningTranAppListByMiningId(String miningId) {
		List miningTranAppList = miningtranAppDao.queryMiningTranAppListByMiningId(miningId);
		return miningTranAppList;
	}

}
