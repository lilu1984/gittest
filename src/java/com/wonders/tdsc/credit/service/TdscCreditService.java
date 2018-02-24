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
		// 黑名单列表
		PageList queryList = null;

		queryList = tdscCorpInfoDao
				.queryCreditListByCondition(tdscCreditCondition);

		return queryList;
	}

	/**
	 * 黑名单中增加信息
	 * 
	 * @param tdscCorpInfo
	 * @param tdscBidderCxApp
	 */
	public void saveCreditInfo(TdscCorpInfo tdscCorpInfo,
			TdscBidderCxApp tdscBidderCxApp) {
		// 保存企业及自然人基本信息

		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao
				.save(tdscCorpInfo);
		if (tdscCorp != null) {
			tdscBidderCxApp.setCorpId(tdscCorp.getCorpId());
			tdscBidderCxAppDao.save(tdscBidderCxApp);
		}
	}

	/**
	 * 根据corpId 获得tdscCorpInfo
	 * 
	 * @param corpId
	 * @return
	 */
	public TdscCorpInfo queryCreditInfoByCorpId(String corpId) {
		// 根据主键获得一个实例
		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao.get(corpId);
		return tdscCorp;
	}

	/**
	 * 根据corpId 获得tdscBidderCxApp
	 * 
	 * @param corpId
	 * @return
	 */
	public TdscBidderCxApp queryCxAppByCorpId(String corpId) {
		// 根据外键获得一个实例
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
	// 需要先根据主键附件查出是那条记录，再进行update么?
	public void updateCreditInfo(TdscCorpInfo tdscCorpInfo,
			TdscBidderCxApp tdscBidderCxApp) {
		//
		tdscCorpInfoDao.update(tdscCorpInfo);
		// 导入时此实例可能不存在
		tdscBidderCxAppDao.saveOrUpdate(tdscBidderCxApp);

	}

	/**
	 * delete 只需置位ifInBlockList,实质是update
	 * 
	 * @param corpId
	 */

	public void delCreditInfo(String corpId) {
		// 根据主键获得一个实例
		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao.get(corpId);
		// 置位ifInBlockList
		tdscCorp.setIfInBlockList("0");
		tdscCorp.setValidity("0");
		// 获得关联诚信表
		// tdscBidderCxApp tdscBidderCxApp =
		// (tdscBidderCxApp)tdscBidderCxAppDao.queryCxAppByCorpId(corpId);
		// 调用update方法
		tdscCorpInfoDao.update(tdscCorp);

		// tdscBidderCxAppDao.update(tdscBidderCxApp);

	}

	/**
	 * 根据查询条件查询不在黑名单中的预申请人列表
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
	 * 根据查询条件查询预申请人列表
	 * 
	 * @param condition
	 * @return
	 */

	public List queryCorpInfo(TdscCorpInfoCondition condition) {
		return tdscCorpInfoDao.queryAllCorpInfoList(condition);
	}

	/**
	 * 根据corpId 获得tdscCorpInfo
	 * 
	 * @param corpId
	 * @return
	 */
	public TdscCorpInfo lazyLoadAppByCorpId(String corpId) {
		// 根据主键获得一个实例
		TdscCorpInfo tdscCorp = (TdscCorpInfo) tdscCorpInfoDao.loadWithLazy(
				corpId, new String[] { "tdscBidderCxApp" });
		return tdscCorp;
	}
	/**
	 * 根据条件查询黑名单
	 * @param condition
	 * @return
	 */
	public List queryInBlockList(TdscCorpInfoCondition condition){
		return (List)tdscCorpInfoDao.queryInBlockList(condition);
	}

}
