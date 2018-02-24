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
	 * ��ѯ��ɽ��Ϣ
	 * 
	 * @param condition
	 * @return
	 */
	public List queryMiningTranAppList(KyqBaseQueryCondition condition) {
		List tranAppList = this.miningtranAppDao.findMiningList(condition);
		return tranAppList;
	}

	/**
	 * ɾ����ɽ��Ϣ
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
	 * �����ɽ��Ϣ
	 * 
	 * @param miningTranApp
	 * @param miningBidderApp
	 * @return
	 */
	public void saveMiningTran(MiningTranApp miningTranApp, MiningBidderApp miningBidderApp) {
		// �жϵ�id�Ƿ���ڣ������ڣ������ű�����������ڣ���Mining�;����˱�ĸ���
		if (miningTranApp != null && StringUtils.isNotBlank(miningTranApp.getTranAppId())) {
			miningtranAppDao.update(miningTranApp);
			miningBidderAppDao.update(miningBidderApp);
		} else {
			// 1. ���� JY_MINING_TRAN_APP
			MiningTranApp tApp = (MiningTranApp) miningtranAppDao.save(miningTranApp);
			// 2. ���� JY_MINING_BIDDER_APP
			miningBidderApp.setMiningId(tApp.getTranAppId());
			miningBidderApp.setPurposeAppId(tApp.getTranAppId());
			miningBidderAppDao.save(miningBidderApp);
		}
	}

	/**
	 * �޸Ŀ�ɽ��Ϣ
	 * 
	 * @param tranAppId
	 * @return
	 */
	public MiningTranApp getMiningTranAppByTranAppId(String tranAppId) {
		MiningTranApp miningTranApp = miningtranAppDao.getMiningTranAppByTranAppId(tranAppId);
		return miningTranApp;
	}

	/**
	 * ���ݹ���ID�޸Ŀ�ɽ��Ϣ
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryMiningTranAppListByNoticeId(String noticeId) {
		List list = miningtranAppDao.queryMiningTranAppListByNoticeId(noticeId);
		return list;
	}

	/**
	 * �޸Ľ�����Ϣ
	 * 
	 * @param tranAppId
	 * @return
	 */

	public MiningBidderApp getMiningBidderAppByMiningId(String tranAppId) {
		MiningBidderApp miningBidderApp = miningBidderAppDao.getMiningBidderAppByMiningId(tranAppId);
		return miningBidderApp;
	}

	/**
	 * ���ݾ�����ID�޸Ŀ�ɽ��Ϣ
	 * 
	 * @param bidderId
	 * @return
	 */
	public List queryMiningTranAppListByMiningId(String miningId) {
		List miningTranAppList = miningtranAppDao.queryMiningTranAppListByMiningId(miningId);
		return miningTranAppList;
	}

}
