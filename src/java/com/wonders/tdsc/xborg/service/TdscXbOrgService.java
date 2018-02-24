package com.wonders.tdsc.xborg.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.util.ext.BeanUtils;
import com.wonders.tdsc.bo.TdscXbOrgApp;
import com.wonders.tdsc.bo.TdscXbOrgHistory;
import com.wonders.tdsc.bo.TdscXbOrgInfo;
import com.wonders.tdsc.bo.condition.TdscXbOrgAppCondition;
import com.wonders.tdsc.bo.condition.TdscXbOrgHistoryCondition;
import com.wonders.tdsc.bo.condition.TdscXbOrgInfoCondition;
import com.wonders.tdsc.xborg.dao.TdscXbOrgAppDao;
import com.wonders.tdsc.xborg.dao.TdscXbOrgHistoryDao;
import com.wonders.tdsc.xborg.dao.TdscXbOrgInfoDao;

public class TdscXbOrgService {

	private TdscXbOrgAppDao tdscXbOrgAppDao;

	private TdscXbOrgHistoryDao tdscXbOrgHistoryDao;

	private TdscXbOrgInfoDao tdscXbOrgInfoDao;

	public void setTdscXbOrgAppDao(TdscXbOrgAppDao tdscXbOrgAppDao) {
		this.tdscXbOrgAppDao = tdscXbOrgAppDao;
	}

	public void setTdscXbOrgHistoryDao(TdscXbOrgHistoryDao tdscXbOrgHistoryDao) {
		this.tdscXbOrgHistoryDao = tdscXbOrgHistoryDao;
	}

	public void setTdscXbOrgInfoDao(TdscXbOrgInfoDao tdscXbOrgInfoDao) {
		this.tdscXbOrgInfoDao = tdscXbOrgInfoDao;
	}

	/**
	 * 根据查询条件取得机构的分页信息列表
	 * 
	 * @param condition
	 * @return
	 */
	public PageList queryOrgAppPageListByCondition(TdscXbOrgAppCondition condition) {
		PageList pageList = this.tdscXbOrgAppDao.findOrgAppPageListByCondition(condition);
		return pageList;
	}

	/**
	 * 通过orgAppId取得对应的协办机构详细信息
	 * @param orgAppId
	 * @return
	 */
	public TdscXbOrgApp getOrgAppInfoById(String orgAppId) {
		if (StringUtils.isEmpty(orgAppId)) return null;
		TdscXbOrgApp tdscXbOrgApp = (TdscXbOrgApp) this.tdscXbOrgAppDao.get(orgAppId);
		return tdscXbOrgApp;
	}

	/**
	 * 更新协办机构详细信息
	 * @param tdscXbOrgApp
	 * @return
	 */
	public TdscXbOrgApp updateOrgApp(TdscXbOrgApp tdscXbOrgApp) {
		if (tdscXbOrgApp == null) return null;
		if (StringUtils.isEmpty(tdscXbOrgApp.getOrgAppId())) {
			TdscXbOrgAppCondition condition = new TdscXbOrgAppCondition();
			condition.setOrgName(tdscXbOrgApp.getOrgName());
			List list = this.tdscXbOrgAppDao.findOrgAppListByCondition(condition);
			if (list.size() > 0 ) throw new RuntimeException("该机构名称已经存在");
			tdscXbOrgApp.setOrgAppId(null);
		}
		tdscXbOrgApp = (TdscXbOrgApp) this.tdscXbOrgAppDao.saveOrUpdate(tdscXbOrgApp);
		return tdscXbOrgApp;
	}
	
	public Map findOrgAppIdNameMap() {
		List list = this.tdscXbOrgAppDao.findAll();
		Map map = new HashMap();
		for (int i = 0 ; list != null && i < list.size(); i++) {
			TdscXbOrgApp orgApp = (TdscXbOrgApp) list.get(i);
			map.put(orgApp.getOrgAppId(), orgApp.getOrgName());
		}
		return map;
	}

	/**
	 * 根据orgAppId删除对应的协办机构信息
	 * @param orgAppId
	 */
	public void delOrgAppInfoById(String orgAppId) {
		if (StringUtils.isEmpty(orgAppId)) return;
		this.tdscXbOrgAppDao.deleteById(orgAppId);
	}

	/**
	 * 通过查询条件取得对应的协办机构批次分页信息
	 * @param condition
	 * @return
	 */
	public PageList queryOrgInfoPageListByCondition(TdscXbOrgInfoCondition condition) {
		PageList pageList = this.tdscXbOrgInfoDao.findOrgAppPageListByCondition(condition);
		return pageList;
	}

	public TdscXbOrgInfo getOrgInfoById(String orgInfoId) {
		if (StringUtils.isEmpty(orgInfoId)) return null;
		TdscXbOrgInfo tdscXbOrgInfo = (TdscXbOrgInfo) this.tdscXbOrgInfoDao.get(orgInfoId);
		return tdscXbOrgInfo;
	}

	public TdscXbOrgInfo updateOrgInfo(TdscXbOrgInfo tdscXbOrgInfo) throws IllegalAccessException, InvocationTargetException {
		if (tdscXbOrgInfo == null) return null;
		if (StringUtils.isEmpty(tdscXbOrgInfo.getOrgInfoId())) {
			tdscXbOrgInfo.setOrgInfoId(null);
			tdscXbOrgInfo.setAcitonDate(new Date());
			tdscXbOrgInfo.setNextNo(new Integer(0));
			if(tdscXbOrgInfo.getOrgIds() != null && tdscXbOrgInfo.getOrgIds().size() > 0){
				tdscXbOrgInfo.setNextOrgId((String)tdscXbOrgInfo.getOrgIds().get(0));
			}
			tdscXbOrgInfo = (TdscXbOrgInfo) this.tdscXbOrgInfoDao.saveOrUpdate(tdscXbOrgInfo);
		} else {
			if ("1".equals(tdscXbOrgInfo.getStatus())) { // 如果是开启的话
				TdscXbOrgInfoCondition condition = new TdscXbOrgInfoCondition();
				condition.setStatus("1");
				TdscXbOrgInfo orgInfo = this.tdscXbOrgInfoDao.getOrgInfoByCondition(condition);
				if (orgInfo != null && !orgInfo.getOrgInfoId().equals(tdscXbOrgInfo.getOrgInfoId())) throw new RuntimeException("请关闭其他开启中协办机构批次!");
			}
			TdscXbOrgInfo dbTdscXbOrgInfo = (TdscXbOrgInfo)this.tdscXbOrgInfoDao.get(tdscXbOrgInfo.getOrgInfoId());
			dbTdscXbOrgInfo.setAcitonDate(new Date());
			BeanUtils.copyProperties(dbTdscXbOrgInfo, tdscXbOrgInfo);
			if(dbTdscXbOrgInfo.getOrgIds() != null && dbTdscXbOrgInfo.getOrgIds().size() > 0){
				dbTdscXbOrgInfo.setNextOrgId((String)dbTdscXbOrgInfo.getOrgIds().get(0));
			}
			if("2".equals(dbTdscXbOrgInfo.getStatus()) && dbTdscXbOrgInfo.getNextNo().intValue() == 0) {
				dbTdscXbOrgInfo.setStatus("8");
				dbTdscXbOrgInfo.setNextNo(new Integer(0));
			} else if ("2".equals(dbTdscXbOrgInfo.getStatus()) && dbTdscXbOrgInfo.getNextNo().intValue() > 0) {
				dbTdscXbOrgInfo.setStatus("8");
			}
			tdscXbOrgInfo = dbTdscXbOrgInfo;
			//tdscXbOrgInfoDao.saveOrUpdate(dbTdscXbOrgInfo);
		}
		return tdscXbOrgInfo;
	}

	public void delOrgInfoById(String orgInfoId) {
		if (StringUtils.isEmpty(orgInfoId)) return;
		this.tdscXbOrgInfoDao.deleteById(orgInfoId);
	}

	public PageList queryOrgHistoryPageListByCondition(TdscXbOrgHistoryCondition condition) {
		PageList pageList = this.tdscXbOrgHistoryDao.findOrgHistoryPageListByCondition(condition);
		return pageList;
	}

	public TdscXbOrgHistory getOrgHistoryById(String orgHistoryId) {
		if (StringUtils.isEmpty(orgHistoryId)) return null;
		TdscXbOrgHistory tdscXbOrgHistory = (TdscXbOrgHistory) this.tdscXbOrgHistoryDao.get(orgHistoryId);
		return tdscXbOrgHistory;
	}

	public TdscXbOrgHistory updateOrgHistory(TdscXbOrgHistory tdscXbOrgHistory) {
		if (tdscXbOrgHistory == null) return null;
		tidyNextOrg(tdscXbOrgHistory);
		if (StringUtils.isEmpty(tdscXbOrgHistory.getHistoryId())) {
			tdscXbOrgHistory.setHistoryId(null);
			tdscXbOrgHistory = (TdscXbOrgHistory) this.tdscXbOrgHistoryDao.saveOrUpdate(tdscXbOrgHistory);
			return tdscXbOrgHistory;
		} else {
			TdscXbOrgHistory getTdscXbOrgHistory = this.getOrgHistoryById(tdscXbOrgHistory.getHistoryId());
			BeanUtils.copyProperties(getTdscXbOrgHistory, tdscXbOrgHistory);
			return getTdscXbOrgHistory;
		}
	}

	public void delOrgHistoryById(String historyId) {
		if (StringUtils.isEmpty(historyId)) return;
		this.tdscXbOrgHistoryDao.deleteById(historyId);
	}

	
	/**
	 * 取得当前的批次以及当前的协办机构名称
	 * @return
	 */
	public TdscXbOrgHistory getNowOrgInfo2OrgHistory() {
		TdscXbOrgInfoCondition condition = new TdscXbOrgInfoCondition();
		condition.setStatus("1");
		TdscXbOrgInfo tdscXbOrgInfo = this.tdscXbOrgInfoDao.getOrgInfoByCondition(condition);
		if (tdscXbOrgInfo == null) return null;
		TdscXbOrgHistory tdscXbOrgHistory = new TdscXbOrgHistory();
		tdscXbOrgHistory.setOrgInfoId(tdscXbOrgInfo.getOrgInfoId());
		//String[] orgAppIds  = StringUtils.trimToEmpty(tdscXbOrgInfo.getCurrentOrgId()).split(",");
		//Integer index = tdscXbOrgInfo.getNextNo();
		//if(orgAppIds.length > 0){
			//String orgAppId = orgAppIds[index.intValue()%orgAppIds.length];
		String orgAppId = tdscXbOrgInfo.getNextOrgId();
		tdscXbOrgHistory.setOrgAppId(orgAppId);
		TdscXbOrgApp tdscXbOrgApp = this.getOrgAppInfoById(orgAppId);
		tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
		//}
		return tdscXbOrgHistory;
	}
	
	/**
	 * 保存接口
	 * @param orgAppId
	 * @param orgInfoId
	 * @param tradeNum
	 * @param planId
	 */
	public void updateTdscXbOrgHistory(String orgAppId, String orgInfoId, String tradeNum, String planId) {
		TdscXbOrgHistory tdscXbOrgHistory = new TdscXbOrgHistory();
		TdscXbOrgApp tdscXbOrgApp = this.getOrgAppInfoById(orgAppId);
		TdscXbOrgInfo tdscXbOrgInfo = this.getOrgInfoById(orgInfoId);
		tdscXbOrgHistory.setOrgAppId(orgAppId);
		tdscXbOrgHistory.setOrgInfoId(orgInfoId);
		tdscXbOrgHistory.setAcitonDate(new Date());
		tdscXbOrgHistory.setTradeNum(tradeNum);
		tdscXbOrgHistory.setPlanId(planId);
		tdscXbOrgHistory.setOrgName(tdscXbOrgApp.getOrgName());
		tdscXbOrgHistory.setOrgZhucr(tdscXbOrgApp.getOrgZhucr());
		if (StringUtils.isEmpty(tdscXbOrgHistory.getHistoryId())) tdscXbOrgHistory.setHistoryId(null);
		this.tdscXbOrgHistoryDao.saveOrUpdate(tdscXbOrgHistory);
//		int length = StringUtils.trimToEmpty(tdscXbOrgInfo.getCurrentOrgId()).split(",").length;
//		int no = tdscXbOrgInfo.getNextNo().intValue();
//		if (no == length) tdscXbOrgInfo.setNextNo(new Integer(0));
//		else tdscXbOrgInfo.setNextNo(new Integer(no + 1));
//		
//		List orgIds = (List)tdscXbOrgInfo.getOrgIds();
//		if(orgIds != null && orgIds.size() > 0){
//			for(int i = 0; i < orgIds.size(); i++){
//				String orgId = (String)orgIds.get(i);
//				if(orgId.equals(orgAppId)){
//					if(i < orgIds.size() -1){
//						tdscXbOrgInfo.setNextOrgId((String)orgIds.get(i+1));
//					}else{
//						tdscXbOrgInfo.setNextOrgId((String)orgIds.get(0));
//					}
//				}
//			}
//		}
		// add by gao start
		String setNextOrgId = getNextOrgId(tdscXbOrgInfo.getCurrentOrgId(), tdscXbOrgInfo.getNextOrgId());
		tdscXbOrgInfo.setNextOrgId(setNextOrgId);
		// add by gao end
		tdscXbOrgInfoDao.saveOrUpdate(tdscXbOrgInfo);
	}
	
	/**
	 * 通过当前编号，以及所有编号计算出下一个编号
	 * @param ids
	 * @param nextId
	 * @return
	 */
	private String getNextOrgId(String ids, String nextId) {
		if (StringUtils.isEmpty(ids)) return "";
		String[] strs = ids.split(",");
		int nowIndex = 0;
		for (int i = 0 ; i < strs.length; i++) {
			// 如果当前编号登录下一个编号，则取出当前编号的索引位置。
			if (StringUtils.trimToEmpty(nextId).equals(strs[i])) {
				nowIndex = i;
				break;
			}
		}
		if (nowIndex + 1 == strs.length) return strs[0];
		else return strs[nowIndex + 1];
	}
	
	
	public void tidyNextOrg(TdscXbOrgHistory getTdscXbOrgHistory){
		if("0".equals(getTdscXbOrgHistory.getIfGoon())){
			//获得协办机构选定信息
			TdscXbOrgInfo tdscXbOrgInfo = (TdscXbOrgInfo)tdscXbOrgInfoDao.get(getTdscXbOrgHistory.getOrgInfoId());
			
			String delId = getTdscXbOrgHistory.getOrgAppId();
			String currentOrgId = tdscXbOrgInfo.getCurrentOrgId();
			String nextOrgId = tdscXbOrgInfo.getNextOrgId();
			
			if (delId.equals(nextOrgId)) {
				String getNextId = getNextOrgId(currentOrgId, nextOrgId); // 先算出下一个编号
				currentOrgId = replaceCurrentOrgId(currentOrgId, delId); // 删除当前的编号
				tdscXbOrgInfo.setCurrentOrgId(currentOrgId);
				tdscXbOrgInfo.setNextOrgId(getNextId);
			} else {
				currentOrgId = replaceCurrentOrgId(currentOrgId, delId);// 先删除编号
				tdscXbOrgInfo.setCurrentOrgId(currentOrgId); 
			}
			
		}
	}
	
	public String replaceCurrentOrgId(String currentId, String replaceStr) {
		if (StringUtils.isEmpty(currentId) || StringUtils.isEmpty(replaceStr)) return "";
		String[] strs = currentId.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			if (!replaceStr.equals(strs[i])){
				sb.append(strs[i]);
				if ( i + 1 != strs.length) sb.append(",");
			}
		}
		return sb.toString();
	}

}
