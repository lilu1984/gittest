package com.wonders.tdsc.kyq.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.jdbc.BaseService;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.KyqNotice;
import com.wonders.tdsc.kyq.bo.MiningTranApp;
import com.wonders.tdsc.kyq.dao.KyqNoticeDao;
import com.wonders.tdsc.kyq.dao.MiningTranAppDao;

public class KyqNoticeService extends BaseService {
	private KyqNoticeDao		kyqNoticeDao;

	private MiningTranAppDao	miningtranAppDao;

	public void setKyqNoticeDao(KyqNoticeDao kyqNoticeDao) {
		this.kyqNoticeDao = kyqNoticeDao;
	}

	public void setMiningtranAppDao(MiningTranAppDao miningtranAppDao) {
		this.miningtranAppDao = miningtranAppDao;
	}
	
	/**
	 * ��ѯ������Ϣ
	 * 
	 * @param condition
	 * @return
	 */
	public List queryNoticeList(KyqBaseQueryCondition condition) {
		List noticeList = kyqNoticeDao.findNoticeList(condition);
		return noticeList;
	}

	/**
	 * ���ݹ���Id�õ�MiningTranApp��Ŀ�ɽ����
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryMiningTranAppListByNoticeId(String noticeId) {
		List list = miningtranAppDao.queryMiningTranAppListByNoticeId(noticeId);
		return list;
	}

	/**
	 * ���湫��Ϳ�ɽ������Ϣ
	 * 
	 * @param notice
	 *            ,miningTranApp
	 * @return
	 */
	public void saveNotice(KyqNotice notice, String[] tranAppIds) {

		MiningTranApp tmpApp = miningtranAppDao.getMiningTranAppByTranAppId(tranAppIds[0]);
		notice.setTransferMode(tmpApp.getTransferMode());
		if (notice != null && StringUtils.isNotBlank(notice.getNoticeId())) {
			// �޸�JY_NOTICE��
			kyqNoticeDao.saveOrUpdate(notice);
		} else {
			// 1. ���� JY_NOTICE ��
			notice.setStatus(KyqContent.KYQ_STATUS_MAKED_PLAN);
			notice.setIfReleased("0");
			notice.setIfResultPublish("0");
			KyqNotice n = (KyqNotice) kyqNoticeDao.save(notice);
			// 2. ���� JY_MINING_TRAN_APP ��
			for (int i = 0; i < tranAppIds.length; i++) {
				MiningTranApp miningTranApp = miningtranAppDao.getMiningTranAppByTranAppId(tranAppIds[i]);
				miningTranApp.setNoticeId(n.getNoticeId());
				miningTranApp.setXuHao(new Integer(i+1));

				miningtranAppDao.saveOrUpdate(miningTranApp);
			}
		}

	}

	/**
	 * �޸Ĺ�����Ϣ
	 * 
	 * @param noticeId
	 * @return
	 */
	public KyqNotice getNoticeByNoticeId(String noticeId) {
		KyqNotice notice = kyqNoticeDao.getNoticeByNoticeId(noticeId);
		return notice;
	}

	/**
	 * һ������ƴ�Ӷ�����ɽ��Ϣ
	 * 
	 * @param noticeId
	 * @return
	 */
	public String appenMiningNames(String noticeId) {
		String miningNames = "";
		List miningTranAppList = queryMiningTranAppListByNoticeId(noticeId);
		if (miningTranAppList != null && miningTranAppList.size() > 0) {
			for (int j = 0; j < miningTranAppList.size(); j++) {
				MiningTranApp miningTranApp = (MiningTranApp) miningTranAppList.get(j);
				if (StringUtils.isNotBlank(miningTranApp.getMiningName())) {
					if (j == 0) {
						miningNames += miningTranApp.getMiningName();
					} else {
						miningNames += "," + miningTranApp.getMiningName();
					}
				}
			}
		}

		return miningNames;
	}

	public List queryMakedNoticeList(KyqBaseQueryCondition condition) {
		List noticeList = kyqNoticeDao.queryMakedNoticeList(condition);
		return noticeList;
	}

	public List queryKyqAllPlanList() {
		
		List noticeList = kyqNoticeDao.queryKyqAllPlanList();
		return noticeList;
	}

	public void saveNoticeByObj(KyqNotice kyqNotice) {
		kyqNoticeDao.saveOrUpdate(kyqNotice);
		
	}
}
