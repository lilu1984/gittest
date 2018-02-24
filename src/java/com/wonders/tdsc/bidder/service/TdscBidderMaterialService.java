package com.wonders.tdsc.bidder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.wonders.common.authority.bo.UserInfo;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderPersonAppDao;
import com.wonders.tdsc.bidder.dao.TdscBidderProvideDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bidder.dao.TdscBusinessRecordDao;
import com.wonders.tdsc.bo.TdscBidderPersonApp;
import com.wonders.tdsc.bo.TdscBidderProvide;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscBusinessRecord;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;
import com.wonders.tdsc.bo.condition.TdscBusinessRecordCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscBidderMaterialService extends BaseSpringManagerImpl {

	private TdscBlockTranAppDao tdscBlockTranAppDao;

	private TdscBidderAppDao tdscBidderAppDao;

	private TdscBidderPersonAppDao tdscBidderPersonAppDao;

	private TdscBusinessRecordDao tdscBusinessRecordDao;

	private CommonQueryService commonQueryService;

	private TdscBidderProvideDao tdscBidderProvideDao;

	private IdSpringManager idSpringManager;

	public void setTdscBusinessRecordDao(TdscBusinessRecordDao tdscBusinessRecordDao) {
		this.tdscBusinessRecordDao = tdscBusinessRecordDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscBidderPersonAppDao(TdscBidderPersonAppDao tdscBidderPersonAppDao) {
		this.tdscBidderPersonAppDao = tdscBidderPersonAppDao;
	}

	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}

	/**
	 * �����������һҳ���û��б�
	 * 
	 * @param condition
	 *            ��ѯ��������
	 * @return PageList����
	 */
	public PageList findPageList(TdscBaseQueryCondition condition, UserInfo user) {
		// TODO
		// 1����commonQueryService��ô���������б���Ϣ��
		// 2�������б��е�������Ϣ����øÿ�������ĳһ���ڷ���������ϵ�������(busi_type,busi_id,userId)
		// 3���������õ������б��е����ض����У�
		PageList pageList = commonQueryService.queryTdscBlockAppViewPageList(condition);

		if (pageList == null)
			return null;

		List list = pageList.getList();
		List tempList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			TdscBlockAppView view = (TdscBlockAppView) list.get(i);

			// ���ò�ѯ����
			TdscBusinessRecordCondition recordCondition = new TdscBusinessRecordCondition();
			// recordCondition.setBusiType(GlobalConstants.RECORD_BIDDER_MATERIALS);
			recordCondition.setAppId(view.getAppId());
			recordCondition.setUserId(user.getUserId());

			// ��ô��ڼ�¼����
			// TdscBusinessRecord record =
			// tdscBusinessRecordDao.getTdscBusinessRecordByCondition(recordCondition);
			// ���÷�������
			List numList = (List) tdscBidderProvideDao.getTdscBidderProvideByCondition(recordCondition);
			List numListCount = (List) tdscBidderProvideDao.getTdscBidderProvideByAppId(recordCondition);
			if (numList != null && numList.size() > 0) {
				view.setTempStr(numList.size() + "");
			} else {
				view.setTempStr("0");
			}
			if (numListCount != null && numListCount.size() > 0) {
				view.setTempStr2(numListCount.size() + "");
			} else {
				view.setTempStr2("0");
			}
			tempList.add(view);
		}
		pageList.setList(tempList);
		return pageList;
	}

	/*
	 * public PageList findBidderPageList(TdscBidderCondition condition) { return tdscBlockTranAppDao.findBidderPageList(condition); }
	 */

	public PageList findPageFundList(TdscBidderCondition condition) {
		return tdscBidderPersonAppDao.findPageList(condition);
	}

	/**
	 * ͨ��appId,userId���µؿ齻����Ϣ��,����ҵ���¼��
	 * 
	 * @param appId
	 * @param userId
	 */
	public void updateTdscBusinessRecordById(String appId, String userId) {
		// ͨ��appId���µؿ齻����Ϣ����������Ϸ�����
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
		if (tdscBlockTranApp.getAppSupNum() == null)
			tdscBlockTranApp.setAppSupNum(new BigDecimal(1));
		else
			tdscBlockTranApp.setAppSupNum(new BigDecimal(tdscBlockTranApp.getAppSupNum().intValue() + 1));
		this.tdscBlockTranAppDao.update(tdscBlockTranApp);

		// ͨ��appId,username���´���ҵ���¼���з��ŷ���
		TdscBusinessRecord tdscBusinessRecord = tdscBusinessRecordDao.getTdscBusinessRecordByRecordId(appId, userId);
		if (tdscBusinessRecord != null) {
			tdscBusinessRecord.setProvideCount(new Integer(tdscBusinessRecord.getProvideCount().intValue() + 1));
			// tdscBusinessRecord.setRecordId(appId);
			// tdscBusinessRecord.setUserId(userId);
			this.tdscBusinessRecordDao.update(tdscBusinessRecord);
		} else {
			tdscBusinessRecord = new TdscBusinessRecord();
			// recordIdΪ�������Զ����ɣ��޷�д��ָ����ֵ
			tdscBusinessRecord.setRecordId(appId);
			tdscBusinessRecord.setProvideCount(new Integer(1));
			tdscBusinessRecord.setUserId(userId);

			this.tdscBusinessRecordDao.save(tdscBusinessRecord);
		}

	}

	/**
	 * ͨ�����������þ�������Ϣ�б�
	 * 
	 * @param bzjDzqk
	 * @return
	 */
	public TdscBidderPersonApp getTdscBidderPersonAppById(String id) {
		return (TdscBidderPersonApp) tdscBidderPersonAppDao.get(id);
	}

	public List queryBidderPresonAppList(String bidderId) {
		List list = tdscBidderPersonAppDao.findTdscBidderPersonDzqkList(bidderId);
		return list;
	}

	/*    *//**
	 * �޸ľ����˵ĵ������
	 * 
	 */
	/*
	 * public void modifyPersonDzqk(List personAppList) { if (personAppList != null && personAppList.size() > 0) for (int i = 0; i < personAppList.size(); i++) {
	 * TdscBidderPersonApp tmpPersonApp = (TdscBidderPersonApp) personAppList.get(i);
	 * 
	 * TdscBidderPersonApp personApp = (TdscBidderPersonApp) tdscBidderPersonAppDao.get(tmpPersonApp.getAppPersonId());
	 * 
	 * personApp.setBzjDzqk(tmpPersonApp.getBzjDzqk());
	 * 
	 * tdscBidderPersonAppDao.update(personApp); } }
	 */

	/**
	 * ͨ��id��ʾ�ؿ齻����Ϣ�еĴ�ӡ����
	 * 
	 * @param id
	 */
	public TdscBlockAppView getTdscTranAppById(TdscBaseQueryCondition condition) {
		return commonQueryService.getTdscBlockAppView(condition);
	}

	/**
	 * ͨ��id��ʾ�ؿ齻����Ϣ�еĴ�ӡ����
	 * 
	 * @param id
	 */
	public TdscBlockAppView detailTdscTranAppById(TdscBaseQueryCondition condition) {
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);

		// TODO
		// ���������ϱ�ţ����򣺡�1��+9λ����ˮ��
		String tmpId = "000000000" + idSpringManager.getIncrementId(GlobalConstants.INCREMENT_ID_BIDDER_MATER);
		tmpId = "1" + tmpId.substring(tmpId.length() - 9);
		tdscBlockAppView.setTempStr(tmpId);

		return tdscBlockAppView;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	/**
	 * �����������´���ҵ���¼���еķ��ŷ���
	 * 
	 * @param condition
	 *            ��ѯ��������
	 * 
	 */
	/*
	 * public void updateBusinessRecord(TdscBusinessRecordCondition condition) { // ��ô��ڼ�¼���� TdscBusinessRecord record =
	 * tdscBusinessRecordDao.getTdscBusinessRecordByCondition(condition);
	 * 
	 * if (record == null) { record = new TdscBusinessRecord(); record.setProvideCount(new Integer(1)); tdscBusinessRecordDao.save(record); } else { Integer provideCount =
	 * record.getProvideCount();
	 * 
	 * if (provideCount == null || provideCount.intValue() == 0) record.setProvideCount(new Integer(1)); else record.setProvideCount(new Integer(record.getProvideCount().intValue()
	 * + 1)); tdscBusinessRecordDao.update(record); } }
	 */

	/**
	 * �����������´���ҵ���¼���еķ��ŷ���
	 * 
	 * @param condition
	 *            ��ѯ��������
	 * 
	 */
	/*
	 * public void updateBlockTranApp(String appId) { // ��ô��ڼ�¼���� TdscBlockTranApp blockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
	 * 
	 * if (blockTranApp == null) { blockTranApp = new TdscBlockTranApp(); blockTranApp.setAppSupNum(new BigDecimal(1)); tdscBlockTranAppDao.save(blockTranApp); } else { //
	 * ȡ��������Ϸ����� BigDecimal appSupNum = blockTranApp.getAppSupNum();
	 * 
	 * if (appSupNum == null || appSupNum.intValue() == 0) blockTranApp.setAppSupNum(new BigDecimal(1)); else blockTranApp.setAppSupNum(new
	 * BigDecimal(blockTranApp.getAppSupNum().intValue() + 1));
	 * 
	 * tdscBlockTranAppDao.update(blockTranApp); } }
	 */

	// ���´���ҵ���¼��,�ؿ齻����Ϣ���еķ��ŷ���
	public String updateFfcl(TdscBidderProvide tdscBidderProvide) {
		String retVal = tdscBlockTranAppDao.updateAppSupNum(tdscBidderProvide.getAppId());
		// ��TdscBidderPrivide���в���һ����¼
		tdscBidderProvideDao.save(tdscBidderProvide);

		return retVal;
	}

	public void setTdscBidderProvideDao(TdscBidderProvideDao tdscBidderProvideDao) {
		this.tdscBidderProvideDao = tdscBidderProvideDao;
	}

	/**
	 * ����appId��userId��ѯĳ�����ڷ��ŵķ���
	 * 
	 * @param condition
	 * @return
	 */
	public String queryNumByUserId(TdscBusinessRecordCondition condition) {
		List tempList = (List) tdscBidderProvideDao.getTdscBidderProvideByCondition(condition);
		String retNum = "0";
		if (tempList != null && tempList.size() != 0) {
			retNum = String.valueOf(tempList.size());
		}
		return retNum;
	}

	public String queryNumByAppIdId(TdscBusinessRecordCondition condition) {
		List tempList = (List) tdscBidderProvideDao.getTdscBidderProvideByAppId(condition);
		String retNum = "0";
		if (tempList != null && tempList.size() != 0) {
			retNum = String.valueOf(tempList.size());
		}
		return retNum;
	}

	/**
	 * ��֤���Сдת�� ������ת�������ĵĴ�д����ֵ
	 * 
	 * @param moneyValue
	 * @return
	 */
	public String convertToCapitalMoney(double moneyValue) {
		double money = moneyValue; // ��ֹ��������������������
		String Result = "";
		String capitalLetter = "��Ҽ��������½��ƾ�";
		String moneytaryUnit = "�ֽ�Բʰ��Ǫ��ʰ��Ǫ��ʰ��Ǫ��ʰ��Ǫ��ʰ��Ǫ";
		String tempCapital, tempUnit;

		int integer; // Ǯ����������
		// int point; // Ǯ��С������
		int tempValue; // Ǯ��ÿһλ��ֵ
		integer = (int) money;
		// point = (int)(100 * (money - (float)integer));

		if (integer == 0)
			Result = "��";
		/*
		 * �����������ֲ��� 1. ����ȡ��ÿһλ�ϵ�ֵ 2. ת���ɴ�д 3. ȷ�����ҵ�λ
		 */
		for (int i = 1; integer > 0; i++) {
			tempValue = (integer % 10);
			tempCapital = capitalLetter.substring(tempValue, tempValue + 1);
			tempUnit = moneytaryUnit.substring(i + 1, i + 2);
			Result = tempCapital + tempUnit + Result;
			integer = integer / 10;
		}
		/*
		 * ����С�����ֲ���
		 */
		/*
		 * tempValue = (point / 10); for (int i = 1; i > -1; i--) { tempCapital = capitalLetter.substring(tempValue, tempValue + 1); tempUnit = moneytaryUnit.substring(i, i + 1);
		 * Result = Result + tempCapital + tempUnit; tempValue = point % 10; }
		 */
		if (!"".equals(Result)) {
			Result = Result.substring(0, Result.length() - 1) + "��";
		}
		return Result;
	}

	

}
