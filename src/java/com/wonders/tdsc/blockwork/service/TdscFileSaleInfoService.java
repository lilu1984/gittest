package com.wonders.tdsc.blockwork.service;

import java.sql.Timestamp;
import java.util.List;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscFileSaleInfoDao;
import com.wonders.tdsc.blockwork.web.form.TdscFileSaleInfoForm;
import com.wonders.tdsc.bo.TdscFileSaleInfo;
import com.wonders.tdsc.bo.condition.TdscFileSaleCondition;

public class TdscFileSaleInfoService extends BaseSpringManagerImpl {

	private TdscFileSaleInfoDao tdscFileSaleInfoDao;

	public void setTdscFileSaleInfoDao(TdscFileSaleInfoDao tdscFileSaleInfoDao) {
		this.tdscFileSaleInfoDao = tdscFileSaleInfoDao;
	}

	public List findPageList(TdscFileSaleCondition condition) {
		return tdscFileSaleInfoDao.findPageList(condition);
	}

	/**
	 * 使用 distinct 查询所有买文件的公司 by noticeId
	 */
	public List queryAllCompanyList(String noticeId) {
		return tdscFileSaleInfoDao.queryAllCompanyList(noticeId);
	}

	// 统计买文件人
	public List countSalePerson(String noticeId, String appId) {
		TdscFileSaleCondition condition = new TdscFileSaleCondition();
		condition.setNoticeId(noticeId);
		condition.setAppId(appId);
		// 查询原有数据
		return tdscFileSaleInfoDao.findPageList(condition);
	}

	public void saveFileSaleList(TdscFileSaleInfoForm tdscFileSaleInfoForm, SysUser user, String flag) {
		if (!"".equals(tdscFileSaleInfoForm.getNoticeId()) && tdscFileSaleInfoForm.getNoticeId() != null) {
			String noticeId = (String) tdscFileSaleInfoForm.getNoticeId();
			// 执行删除
			if (!"ADD".equals(flag)) {
				String[] saleIds = tdscFileSaleInfoForm.getBidderLxdz().split(",");
				for (int i = 0; i < saleIds.length; i++) {
					tdscFileSaleInfoDao.deleteById(saleIds[i]);
				}
				// String[] appIds = tdscFileSaleInfoForm.getBlock_choose();
				// 删除数据 根据 notice_id 和 apppId
				// for (int i = 0; i < appIds.length; i++) {
				// deleteOldFileSaleList(noticeId, appIds[i], tdscFileSaleInfoForm.getBidderLxdh());
				// }
			}

			// 保存数据
			insertFileSaleList(tdscFileSaleInfoForm, user);
		}
	}

	/**
	 * 根据noticeId删除原有数据
	 * 
	 * @param list
	 */
	public void deleteOldFileSaleList(String noticeId, String appId, String lxdh) {
		// 将原有数据“是否有效”设置为无效 0；记录设置的时间和操作用户
		TdscFileSaleCondition condition = new TdscFileSaleCondition();
		condition.setAppId(appId);
		condition.setNoticeId(noticeId);
		condition.setBidderLxdh(lxdh);
		// 查询原有数据
		List list = (List) tdscFileSaleInfoDao.findPageList(condition);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				TdscFileSaleInfo tdscFileSaleInfo = (TdscFileSaleInfo) list.get(i);
				System.out.println("delete FILE_SALE_ID = " + tdscFileSaleInfo.getFileSaleId());
				tdscFileSaleInfoDao.deleteById(tdscFileSaleInfo.getFileSaleId());
			}
		}
	}

	/**
	 * 插入新的录入数据
	 * 
	 * @param tdscFileSaleInfoForm
	 * @param user
	 */
	public void insertFileSaleList(TdscFileSaleInfoForm tdscFileSaleInfoForm, SysUser user) {
		if (tdscFileSaleInfoForm.getNoticeId() != null && !"".equals(tdscFileSaleInfoForm.getNoticeId())) {
			String noticeId = (String) tdscFileSaleInfoForm.getNoticeId();
			Timestamp tempTime = new Timestamp(System.currentTimeMillis());
			if (tdscFileSaleInfoForm.getBidderNames() != null) {
				String[] bidderNames = tdscFileSaleInfoForm.getBidderNames();
				String[] appIds = tdscFileSaleInfoForm.getBlock_choose();
				if (appIds != null) {
					for (int j = 0; j < appIds.length; j++) {
						String appId = appIds[j];
						if (bidderNames.length > 0) {
							for (int i = 0; i < bidderNames.length; i++) {
								TdscFileSaleInfo tdscFileSaleInfo = new TdscFileSaleInfo();
								tdscFileSaleInfo.setNoticeId(noticeId);
								tdscFileSaleInfo.setActionUser(user.getUserId());
								tdscFileSaleInfo.setActionDate(tempTime);
								tdscFileSaleInfo.setIfValidity("1");
								tdscFileSaleInfo.setBidderName(bidderNames[i]);
								tdscFileSaleInfo.setAppId(appId);
								// 联系地址
								// if(tdscFileSaleInfoForm.getBidderLxdzs().length>0 && tdscFileSaleInfoForm.getBidderLxdzs()[i]!=null){
								// tdscFileSaleInfo.setBidderLxdz(tdscFileSaleInfoForm.getBidderLxdzs()[i]);
								// }
								// 购买人
								if (tdscFileSaleInfoForm.getResultNames().length > 0 && tdscFileSaleInfoForm.getResultNames()[i] != null) {
									tdscFileSaleInfo.setResultName(tdscFileSaleInfoForm.getResultNames()[i]);
								}
								// 联系电话
								if (tdscFileSaleInfoForm.getBidderLxdhs().length > 0 && tdscFileSaleInfoForm.getBidderLxdhs()[i] != null) {
									tdscFileSaleInfo.setBidderLxdh(tdscFileSaleInfoForm.getBidderLxdhs()[i]);
								}
								// if(tdscFileSaleInfoForm.getBidderYzbms().length>0 && tdscFileSaleInfoForm.getBidderYzbms()[i]!=null){
								// tdscFileSaleInfo.setBidderYzbm(tdscFileSaleInfoForm.getBidderYzbms()[i]);
								// }
								tdscFileSaleInfoDao.save(tdscFileSaleInfo);
							}
						}
					}
				}
			}
		}
	}

	public String queryAppIdsByBidderName(String noticeId, String bidderName) {

		String ret = "";
		List infoList = (List) tdscFileSaleInfoDao.queryAllAppId(noticeId, bidderName);

		if (infoList != null && infoList.size() > 0) {
			for (int i = 0; i < infoList.size(); i++) {
				TdscFileSaleInfo info = (TdscFileSaleInfo) infoList.get(i);
				ret += info.getAppId() + ",";
			}
		}

		return ret;
	}

	public TdscFileSaleInfo getFileSaleInfo(String noticeId, String personName) {
		TdscFileSaleInfo retInfo = new TdscFileSaleInfo();
		List infoList = (List) tdscFileSaleInfoDao.queryAllAppId(noticeId, personName);
		if (infoList != null && infoList.size() > 0)
			retInfo = (TdscFileSaleInfo) infoList.get(0);

		String appIds = queryAppIdsByBidderName(noticeId, personName);
		retInfo.setAppId(appIds);

		String fileSaleIds = queryFileSaleIdsByBidderName(noticeId, personName);
		retInfo.setBidderLxdz(fileSaleIds);// 所有符合条件的 sale id 存入此字段

		return retInfo;
	}

	public void deleteFileSaleByNoticeAndUnitName(String noticeId, String unitName) {
		List infoList = (List) tdscFileSaleInfoDao.queryAllAppId(noticeId, unitName);
		if (infoList != null && infoList.size() > 0) {
			for (int i = 0; i < infoList.size(); i++) {
				TdscFileSaleInfo info = (TdscFileSaleInfo) infoList.get(i);
				tdscFileSaleInfoDao.deleteById(info.getFileSaleId());
			}
		}

	}

	public String queryFileSaleIdsByBidderName(String noticeId, String bidderName) {

		String ret = "";
		List infoList = (List) tdscFileSaleInfoDao.queryAllAppId(noticeId, bidderName);

		if (infoList != null && infoList.size() > 0) {
			for (int i = 0; i < infoList.size(); i++) {
				TdscFileSaleInfo info = (TdscFileSaleInfo) infoList.get(i);
				ret += info.getFileSaleId() + ",";
			}
		}

		return ret;
	}

}
