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
	 * ʹ�� distinct ��ѯ�������ļ��Ĺ�˾ by noticeId
	 */
	public List queryAllCompanyList(String noticeId) {
		return tdscFileSaleInfoDao.queryAllCompanyList(noticeId);
	}

	// ͳ�����ļ���
	public List countSalePerson(String noticeId, String appId) {
		TdscFileSaleCondition condition = new TdscFileSaleCondition();
		condition.setNoticeId(noticeId);
		condition.setAppId(appId);
		// ��ѯԭ������
		return tdscFileSaleInfoDao.findPageList(condition);
	}

	public void saveFileSaleList(TdscFileSaleInfoForm tdscFileSaleInfoForm, SysUser user, String flag) {
		if (!"".equals(tdscFileSaleInfoForm.getNoticeId()) && tdscFileSaleInfoForm.getNoticeId() != null) {
			String noticeId = (String) tdscFileSaleInfoForm.getNoticeId();
			// ִ��ɾ��
			if (!"ADD".equals(flag)) {
				String[] saleIds = tdscFileSaleInfoForm.getBidderLxdz().split(",");
				for (int i = 0; i < saleIds.length; i++) {
					tdscFileSaleInfoDao.deleteById(saleIds[i]);
				}
				// String[] appIds = tdscFileSaleInfoForm.getBlock_choose();
				// ɾ������ ���� notice_id �� apppId
				// for (int i = 0; i < appIds.length; i++) {
				// deleteOldFileSaleList(noticeId, appIds[i], tdscFileSaleInfoForm.getBidderLxdh());
				// }
			}

			// ��������
			insertFileSaleList(tdscFileSaleInfoForm, user);
		}
	}

	/**
	 * ����noticeIdɾ��ԭ������
	 * 
	 * @param list
	 */
	public void deleteOldFileSaleList(String noticeId, String appId, String lxdh) {
		// ��ԭ�����ݡ��Ƿ���Ч������Ϊ��Ч 0����¼���õ�ʱ��Ͳ����û�
		TdscFileSaleCondition condition = new TdscFileSaleCondition();
		condition.setAppId(appId);
		condition.setNoticeId(noticeId);
		condition.setBidderLxdh(lxdh);
		// ��ѯԭ������
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
	 * �����µ�¼������
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
								// ��ϵ��ַ
								// if(tdscFileSaleInfoForm.getBidderLxdzs().length>0 && tdscFileSaleInfoForm.getBidderLxdzs()[i]!=null){
								// tdscFileSaleInfo.setBidderLxdz(tdscFileSaleInfoForm.getBidderLxdzs()[i]);
								// }
								// ������
								if (tdscFileSaleInfoForm.getResultNames().length > 0 && tdscFileSaleInfoForm.getResultNames()[i] != null) {
									tdscFileSaleInfo.setResultName(tdscFileSaleInfoForm.getResultNames()[i]);
								}
								// ��ϵ�绰
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
		retInfo.setBidderLxdz(fileSaleIds);// ���з��������� sale id ������ֶ�

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
