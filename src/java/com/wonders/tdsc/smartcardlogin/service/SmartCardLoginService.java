package com.wonders.tdsc.smartcardlogin.service;

import java.util.List;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.bidder.dao.TdscBidderAppDao;
import com.wonders.tdsc.bidder.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;

import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.condition.TdscBidderCondition;

public class SmartCardLoginService extends BaseSpringManagerImpl{

	private TdscBidderAppDao tdscBidderAppDao;
	
	private TdscBlockInfoDao tdscBlockInfoDao;
	
	private TdscBlockTranAppDao tdscBlockTranAppDao;
	
	private TdscBlockPlanTableDao tdscBlockPlanTableDao;
	
	public void setTdscBidderAppDao(TdscBidderAppDao tdscBidderAppDao) {
		this.tdscBidderAppDao = tdscBidderAppDao;
	}

	public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
		this.tdscBlockInfoDao = tdscBlockInfoDao;
	}
	
	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}
	
	
	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}
	
	public TdscBidderApp checkSmartCard(String cardNumber,String password){
		TdscBidderApp tdscBidderApp = this.tdscBidderAppDao.getBidderAppByCardNumberPassword(cardNumber,password);
		return tdscBidderApp;
	}
	
	/**
	 * 根据card信息（密码可空）查询tdscBidderAppList ，一卡对应多个地块
	 * @param cardNumber
	 * @param password
	 * @return
	 */
	public List getBidderAppListByCard(String cardNumber,String password){
		List tdscBidderAppList = this.tdscBidderAppDao.getBidderAppListByCard(cardNumber,password);
		return tdscBidderAppList;
	}
	
	/**
	 * 根据条件 查询tdscBidderApp
	 * @param cardNumber
	 * @param password
	 * @return
	 */
	public List getBidderAppListByCondition(TdscBidderCondition condition){
		List tdscBidderAppList = this.tdscBidderAppDao.findBidderAppListByCondition(condition);
		if(tdscBidderAppList!=null && tdscBidderAppList.size()>0){
			return tdscBidderAppList;
		}
		return null;
	}
	
	public TdscBidderApp checkByAcceptNo(String acceptNo){
		List temp = this.tdscBidderAppDao.getProByAcceptNo(acceptNo);
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		if(temp!=null && temp.size()>0){
			tdscBidderApp = (TdscBidderApp)temp.get(0);
		}
		return tdscBidderApp;
	}

	
	public TdscBidderApp checkByCertNo(String certNo){
		List temp = this.tdscBidderAppDao.getProByCertNo(certNo);
		TdscBidderApp tdscBidderApp = new TdscBidderApp();
		if(temp!=null && temp.size()>0){
			tdscBidderApp = (TdscBidderApp)temp.get(0);
		}
		return tdscBidderApp;
	}
	/**
	 * 取得一个地块的交易信息
	 * @param appId
	 * @return
	 */
	public TdscBlockTranApp queryBlockTranAppInfoByAppId(String appId){
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp)this.tdscBlockTranAppDao.get(appId);
		return tdscBlockTranApp;
	}
	
	/**
	 * 取得一个地块的全部信息
	 * @param blockId
	 * @return
	 */
	public TdscBlockInfo queryBlockInfoByBlockId(String blockId){
		TdscBlockInfo tdscBlockInfo = (TdscBlockInfo)this.tdscBlockInfoDao.get(blockId);
		return tdscBlockInfo;
	}
	
	/**
	 * 检验是否已到挂牌时间
	 * @return
	 */
	public String checkStartDate(){
		String isStart = this.tdscBlockPlanTableDao.isStartDate();
		return isStart;
	}
	
	/**
	 * 检验挂牌是否已结束
	 * @return
	 */
	public String checkEndDate(){
		String isEnd = this.tdscBlockPlanTableDao.isEndDate();
		return isEnd;
	}
	
	/**
	 * 检验是否已过审查时间
	 * @return
	 */
	public String CheckOverpass(){
		String checkDate = this.tdscBidderAppDao.isCheckOverpass();
		return checkDate;
	}
}
