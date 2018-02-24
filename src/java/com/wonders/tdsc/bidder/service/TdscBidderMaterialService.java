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
	 * 根据条件获得一页的用户列表
	 * 
	 * @param condition
	 *            查询条件对象
	 * @return PageList对象
	 */
	public PageList findPageList(TdscBaseQueryCondition condition, UserInfo user) {
		// TODO
		// 1、从commonQueryService获得待办的土地列表信息；
		// 2、根据列表中的土地信息，获得该块土地在某一窗口发放申请材料的数量；(busi_type,busi_id,userId)
		// 3、重新设置到土地列表中的土地对象中；
		PageList pageList = commonQueryService.queryTdscBlockAppViewPageList(condition);

		if (pageList == null)
			return null;

		List list = pageList.getList();
		List tempList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			TdscBlockAppView view = (TdscBlockAppView) list.get(i);

			// 设置查询条件
			TdscBusinessRecordCondition recordCondition = new TdscBusinessRecordCondition();
			// recordCondition.setBusiType(GlobalConstants.RECORD_BIDDER_MATERIALS);
			recordCondition.setAppId(view.getAppId());
			recordCondition.setUserId(user.getUserId());

			// 获得窗口记录对象
			// TdscBusinessRecord record =
			// tdscBusinessRecordDao.getTdscBusinessRecordByCondition(recordCondition);
			// 设置发放数量
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
	 * 通过appId,userId更新地块交易信息表,窗口业务记录表
	 * 
	 * @param appId
	 * @param userId
	 */
	public void updateTdscBusinessRecordById(String appId, String userId) {
		// 通过appId更新地块交易信息表中申请材料发放数
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
		if (tdscBlockTranApp.getAppSupNum() == null)
			tdscBlockTranApp.setAppSupNum(new BigDecimal(1));
		else
			tdscBlockTranApp.setAppSupNum(new BigDecimal(tdscBlockTranApp.getAppSupNum().intValue() + 1));
		this.tdscBlockTranAppDao.update(tdscBlockTranApp);

		// 通过appId,username更新窗口业务记录表中发放分数
		TdscBusinessRecord tdscBusinessRecord = tdscBusinessRecordDao.getTdscBusinessRecordByRecordId(appId, userId);
		if (tdscBusinessRecord != null) {
			tdscBusinessRecord.setProvideCount(new Integer(tdscBusinessRecord.getProvideCount().intValue() + 1));
			// tdscBusinessRecord.setRecordId(appId);
			// tdscBusinessRecord.setUserId(userId);
			this.tdscBusinessRecordDao.update(tdscBusinessRecord);
		} else {
			tdscBusinessRecord = new TdscBusinessRecord();
			// recordId为主键，自动生成，无法写入指定的值
			tdscBusinessRecord.setRecordId(appId);
			tdscBusinessRecord.setProvideCount(new Integer(1));
			tdscBusinessRecord.setUserId(userId);

			this.tdscBusinessRecordDao.save(tdscBusinessRecord);
		}

	}

	/**
	 * 通过到账情况获得竞买人信息列表
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
	 * 修改竞买人的到账情况
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
	 * 通过id显示地块交易信息中的打印内容
	 * 
	 * @param id
	 */
	public TdscBlockAppView getTdscTranAppById(TdscBaseQueryCondition condition) {
		return commonQueryService.getTdscBlockAppView(condition);
	}

	/**
	 * 通过id显示地块交易信息中的打印内容
	 * 
	 * @param id
	 */
	public TdscBlockAppView detailTdscTranAppById(TdscBaseQueryCondition condition) {
		TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);

		// TODO
		// 获得申请材料编号，规则：“1”+9位的流水号
		String tmpId = "000000000" + idSpringManager.getIncrementId(GlobalConstants.INCREMENT_ID_BIDDER_MATER);
		tmpId = "1" + tmpId.substring(tmpId.length() - 9);
		tdscBlockAppView.setTempStr(tmpId);

		return tdscBlockAppView;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	/**
	 * 根据条件更新窗口业务记录表中的发放分数
	 * 
	 * @param condition
	 *            查询条件对象
	 * 
	 */
	/*
	 * public void updateBusinessRecord(TdscBusinessRecordCondition condition) { // 获得窗口记录对象 TdscBusinessRecord record =
	 * tdscBusinessRecordDao.getTdscBusinessRecordByCondition(condition);
	 * 
	 * if (record == null) { record = new TdscBusinessRecord(); record.setProvideCount(new Integer(1)); tdscBusinessRecordDao.save(record); } else { Integer provideCount =
	 * record.getProvideCount();
	 * 
	 * if (provideCount == null || provideCount.intValue() == 0) record.setProvideCount(new Integer(1)); else record.setProvideCount(new Integer(record.getProvideCount().intValue()
	 * + 1)); tdscBusinessRecordDao.update(record); } }
	 */

	/**
	 * 根据条件更新窗口业务记录表中的发放分数
	 * 
	 * @param condition
	 *            查询条件对象
	 * 
	 */
	/*
	 * public void updateBlockTranApp(String appId) { // 获得窗口记录对象 TdscBlockTranApp blockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
	 * 
	 * if (blockTranApp == null) { blockTranApp = new TdscBlockTranApp(); blockTranApp.setAppSupNum(new BigDecimal(1)); tdscBlockTranAppDao.save(blockTranApp); } else { //
	 * 取得申请材料发放数 BigDecimal appSupNum = blockTranApp.getAppSupNum();
	 * 
	 * if (appSupNum == null || appSupNum.intValue() == 0) blockTranApp.setAppSupNum(new BigDecimal(1)); else blockTranApp.setAppSupNum(new
	 * BigDecimal(blockTranApp.getAppSupNum().intValue() + 1));
	 * 
	 * tdscBlockTranAppDao.update(blockTranApp); } }
	 */

	// 更新窗口业务记录表,地块交易信息表中的发放份数
	public String updateFfcl(TdscBidderProvide tdscBidderProvide) {
		String retVal = tdscBlockTranAppDao.updateAppSupNum(tdscBidderProvide.getAppId());
		// 将TdscBidderPrivide表中插入一条记录
		tdscBidderProvideDao.save(tdscBidderProvide);

		return retVal;
	}

	public void setTdscBidderProvideDao(TdscBidderProvideDao tdscBidderProvideDao) {
		this.tdscBidderProvideDao = tdscBidderProvideDao;
	}

	/**
	 * 根据appId和userId查询某个窗口发放的份数
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
	 * 保证金大小写转换 将数字转换成中文的大写货币值
	 * 
	 * @param moneyValue
	 * @return
	 */
	public String convertToCapitalMoney(double moneyValue) {
		double money = moneyValue; // 防止浮点数四舍五入造成误差
		String Result = "";
		String capitalLetter = "零壹贰叁肆伍陆柒捌玖";
		String moneytaryUnit = "分角圆拾佰仟万拾佰仟亿拾佰仟万拾佰仟亿拾佰仟";
		String tempCapital, tempUnit;

		int integer; // 钱的整数部分
		// int point; // 钱的小数部分
		int tempValue; // 钱的每一位的值
		integer = (int) money;
		// point = (int)(100 * (money - (float)integer));

		if (integer == 0)
			Result = "零";
		/*
		 * 货币整数部分操作 1. 依次取得每一位上的值 2. 转换成大写 3. 确定货币单位
		 */
		for (int i = 1; integer > 0; i++) {
			tempValue = (integer % 10);
			tempCapital = capitalLetter.substring(tempValue, tempValue + 1);
			tempUnit = moneytaryUnit.substring(i + 1, i + 2);
			Result = tempCapital + tempUnit + Result;
			integer = integer / 10;
		}
		/*
		 * 货币小数部分操作
		 */
		/*
		 * tempValue = (point / 10); for (int i = 1; i > -1; i--) { tempCapital = capitalLetter.substring(tempValue, tempValue + 1); tempUnit = moneytaryUnit.substring(i, i + 1);
		 * Result = Result + tempCapital + tempUnit; tempValue = point % 10; }
		 */
		if (!"".equals(Result)) {
			Result = Result.substring(0, Result.length() - 1) + "万";
		}
		return Result;
	}

	

}
