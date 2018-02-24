package com.wonders.tdsc.credit.service;

import java.util.List;

import com.wonders.esframework.common.model.PageList;
import com.wonders.jdbc.BaseService;
import com.wonders.tdsc.bo.TdscBidderCxApp;
import com.wonders.tdsc.bo.TdscCorpInfo;
import com.wonders.tdsc.bo.condition.TdscCorpInfoCondition;
import com.wonders.tdsc.credit.dao.TdscBidderCxAppDao;
import com.wonders.tdsc.credit.dao.TdscCorpInfoDao;

public class TdscCreditService extends BaseService {

	private TdscCorpInfoDao tdscCorpInfoDao;

	private TdscBidderCxAppDao tdscBidderCxAppDao;

	public void setTdscBidderCxAppDao(TdscBidderCxAppDao tdscBidderCxAppDao) {
		this.tdscBidderCxAppDao = tdscBidderCxAppDao;
	}

	public void setTdscCorpInfoDao(TdscCorpInfoDao tdscCorpInfoDao) {
		this.tdscCorpInfoDao = tdscCorpInfoDao;
	}

	public PageList queryCreditList(TdscCorpInfoCondition tdscCreditCondition) {
		// �������б�
		PageList queryList = null;

		queryList = tdscCorpInfoDao
				.queryCreditListByCondition(tdscCreditCondition);

		return queryList;
	}

	/**
	 * ��������������Ϣ
	 * 
	 * @param tdscCorpInfo
	 * @param tdscBidderCxApp
	 */
	public void saveCreditInfo(TdscCorpInfo tdscCorpInfo,
			TdscBidderCxApp tdscBidderCxApp) {
		// ������ҵ����Ȼ�˻�����Ϣ

		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao
				.save(tdscCorpInfo);
		if (tdscCorp != null) {
			tdscBidderCxApp.setCorpId(tdscCorp.getCorpId());
			tdscBidderCxAppDao.save(tdscBidderCxApp);
		}
	}

	/**
	 * ����corpId ���tdscCorpInfo
	 * 
	 * @param corpId
	 * @return
	 */
	public TdscCorpInfo queryCreditInfoByCorpId(String corpId) {
		// �����������һ��ʵ��
		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao.get(corpId);
		return tdscCorp;
	}

	/**
	 * ����corpId ���tdscBidderCxApp
	 * 
	 * @param corpId
	 * @return
	 */
	public TdscBidderCxApp queryCxAppByCorpId(String corpId) {
		// ����������һ��ʵ��
		TdscBidderCxApp tdscBidderCxApp = (TdscBidderCxApp) tdscBidderCxAppDao
				.queryCxAppByCorpId(corpId);
		return tdscBidderCxApp;
	}

	/**
	 * update
	 * 
	 * @param tdscCorpInfo
	 * @param tdscBidderCxApp
	 */
	// ��Ҫ�ȸ����������������������¼���ٽ���updateô?
	public void updateCreditInfo(TdscCorpInfo tdscCorpInfo,
			TdscBidderCxApp tdscBidderCxApp) {
		//
		tdscCorpInfoDao.update(tdscCorpInfo);
		// ����ʱ��ʵ�����ܲ�����
		tdscBidderCxAppDao.saveOrUpdate(tdscBidderCxApp);

	}

	/**
	 * delete ֻ����λifInBlockList,ʵ����update
	 * 
	 * @param corpId
	 */

	public void delCreditInfo(String corpId) {
		// �����������һ��ʵ��
		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao.get(corpId);
		// ��λifInBlockList
		tdscCorp.setIfInBlockList("0");
		tdscCorp.setValidity("0");
		// ��ù������ű�
		// tdscBidderCxApp tdscBidderCxApp =
		// (tdscBidderCxApp)tdscBidderCxAppDao.queryCxAppByCorpId(corpId);
		// ����update����
		tdscCorpInfoDao.update(tdscCorp);

		// tdscBidderCxAppDao.update(tdscBidderCxApp);

	}

	/**
	 * ���ݲ�ѯ������ѯ���ں������е�Ԥ�������б�
	 * 
	 * @param condition
	 * @return
	 */

	public List queryCorpInfoListByCondition(TdscCorpInfoCondition condition) {
		return tdscCorpInfoDao.queryCorpInfoListByCondition(condition);
	}

	public void saveToCredit(TdscCorpInfo tdscCorpInfo) {
		tdscCorpInfoDao.update(tdscCorpInfo);
	}

	public void saveTdscBidderCxApp(TdscBidderCxApp tdscBidderCxApp) {
		tdscBidderCxAppDao.save(tdscBidderCxApp);
	}

	/**
	 * ���ݲ�ѯ������ѯԤ�������б�
	 * 
	 * @param condition
	 * @return
	 */

	public List queryCorpInfo(TdscCorpInfoCondition condition) {
		return tdscCorpInfoDao.queryAllCorpInfoList(condition);
	}

	/**
	 * ����corpId ���tdscCorpInfo
	 * 
	 * @param corpId
	 * @return
	 */
	public TdscCorpInfo lazyLoadAppByCorpId(String corpId) {
		// �����������һ��ʵ��
		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao.loadWithLazy(
				corpId, new String[] { "tdscBidderCxApp" });
		return tdscCorp;
	}
	/**
	 * ����������ѯ������
	 * @param condition
	 * @return
	 */
	public List queryInBlockList(TdscCorpInfoCondition condition){
		return (List)tdscCorpInfoDao.queryInBlockList(condition);
	}

}
