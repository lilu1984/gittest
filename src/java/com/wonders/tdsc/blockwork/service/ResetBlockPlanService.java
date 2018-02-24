package com.wonders.tdsc.blockwork.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.util.NumberUtil;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.bo.TdscApp;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.bo.condition.TdscOpnnCondition;
import com.wonders.tdsc.flowadapter.dao.TdscAppDao;
import com.wonders.tdsc.flowadapter.dao.TdscAppNodeStatDao;
import com.wonders.tdsc.flowadapter.dao.TdscOpnnDao;

public class ResetBlockPlanService extends BaseSpringManagerImpl {

	private TdscAppDao tdscAppDao;

	private TdscAppNodeStatDao tdscAppNodeStatDao;

	private TdscOpnnDao tdscOpnnDao;
	
	private TdscBlockTranAppDao tdscBlockTranAppDao;
	
	private TdscBlockInfoDao tdscBlockInfoDao;
	
	private TdscBlockPlanTableDao tdscBlockPlanTableDao;
	
	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}

	public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
		this.tdscBlockInfoDao = tdscBlockInfoDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscOpnnDao(TdscOpnnDao tdscOpnnDao) {
		this.tdscOpnnDao = tdscOpnnDao;
	}

	public void setTdscAppNodeStatDao(TdscAppNodeStatDao tdscAppNodeStatDao) {
		this.tdscAppNodeStatDao = tdscAppNodeStatDao;
	}

	public void setTdscAppDao(TdscAppDao tdscAppDao) {
		this.tdscAppDao = tdscAppDao;
	}

	public void sendBack(TdscBlockAppView appView, SysUser user, String nodeId, String statusId, String planId) throws Exception {
// TdscAppFlow appFlow = new TdscAppFlow();
// appFlow.setAppId(appView.getAppId());
// appFlow.setTransferMode(appView.getTransferMode());
// appFlow.setUser(user);
// appFlow.setTextOpen("");
// appFlow.setResultId("020101");
// //appFlow.setTextOpen(textOpen);
// this.appFlowService.saveOpnn(appFlow);
		String appId = appView.getAppId();
		// 1.重新设置 tdscapp
		TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
		tdscApp.setNodeId(nodeId); // 02
		tdscApp.setNodeDate(new Timestamp(System.currentTimeMillis()));
		tdscApp.setStatusId(statusId); // 0201
		tdscApp.setStatusDate(new Timestamp(System.currentTimeMillis()));
		// 2.重新设置 tdscappnodestat
		TdscFlowCondition condition = new TdscFlowCondition();
		condition.setAppId(appId);
		// 取得当前的节点
		List list = this.tdscAppNodeStatDao.findActiveNodeList(condition);
		if (list != null && list.size() > 0) {
			TdscAppNodeStat nowTdscAppNodeStat = (TdscAppNodeStat)list.get(0);
			nowTdscAppNodeStat.setNodeStat("00");
			nowTdscAppNodeStat.setStartDate(null);
		}
		// 取得上一点
		condition.setNodeId(nodeId);
		List newList = this.tdscAppNodeStatDao.findNodeList(condition);
		if (newList != null && newList.size() > 0) {
			TdscAppNodeStat tdscAppNodeStat = (TdscAppNodeStat) newList.get(0);
			tdscAppNodeStat.setNodeStat("01");
			tdscAppNodeStat.setEndDate(null);
		}
		// 3.重新设置 tdscopnn
		TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
		opnnCondition.setIfOpt("00");
		opnnCondition.setAppId(appId);
		TdscOpnn tdscOpnn = (TdscOpnn) this.tdscOpnnDao.findOpnnInfo(opnnCondition);
		int actionNum = Integer.parseInt(NumberUtil.numberDisplay(tdscOpnn.getActionNum(),0));
		tdscOpnn.setActionOrgan(user.getRegionId());
		tdscOpnn.setActionUser(user.getDisplayName());
		tdscOpnn.setActionUserId(user.getUserId());
		tdscOpnn.setActionDate(new Timestamp(System.currentTimeMillis()));
		tdscOpnn.setResultId(appView.getNodeId() + "0101");
		tdscOpnn.setResultName("回退");
		tdscOpnn.setTextOpen("重新拟定方案");
		tdscOpnn.setIsOpt("01");
		// 新增
		TdscOpnn newTdscOpnn = new TdscOpnn();
		newTdscOpnn.setAppId(appId);
		actionNum += 1;
		newTdscOpnn.setActionNum(new BigDecimal(actionNum));
		newTdscOpnn.setActionId("0201");
		newTdscOpnn.setFirstDate(new Timestamp(System.currentTimeMillis()));
		newTdscOpnn.setAccDate(new Timestamp(System.currentTimeMillis()));
		newTdscOpnn.setIsOpt("00");
		this.tdscOpnnDao.saveOrUpdate(newTdscOpnn);
		
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) this.tdscBlockTranAppDao.get(appId);
		tdscBlockTranApp.setPlanId(null);
		
		List blocklist = this.tdscBlockTranAppDao.findTranAppList(planId);
		if (blocklist != null) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0 ; i < blocklist.size(); i++) {
				TdscBlockTranApp tranApp = (TdscBlockTranApp) blocklist.get(i);
				TdscBlockInfo blockInfo = (TdscBlockInfo) this.tdscBlockInfoDao.get(tranApp.getBlockId());
				sb.append(blockInfo.getBlockName());
				if (i + 1 != blocklist.size()) {
					sb.append(",");
				}
			}
			TdscBlockPlanTable blockPlanTable = (TdscBlockPlanTable)this.tdscBlockPlanTableDao.get(planId);
			blockPlanTable.setBlockName(sb.toString());
		}
	}

	public void submitBlockPlan(String[] appIds, String planId, SysUser user) {
		for (int i = 0 ; i < appIds.length; i++) {
			String appId = appIds[i];
			// 设置实施方案编号
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) this.tdscBlockTranAppDao.get(appId);
			tdscBlockTranApp.setPlanId(planId);
			
			// 1.重新设置 tdscapp
			TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
			tdscApp.setNodeId("03"); // 03
			tdscApp.setNodeDate(new Timestamp(System.currentTimeMillis()));
			tdscApp.setStatusId("0301"); // 0201
			tdscApp.setStatusDate(new Timestamp(System.currentTimeMillis()));
			// 2.重新设置 tdscappnodestat
			TdscFlowCondition condition = new TdscFlowCondition();
			condition.setAppId(appId);
			// 取得当前的节点
			List list = this.tdscAppNodeStatDao.findActiveNodeList(condition);
			if (list != null && list.size() > 0) {
				TdscAppNodeStat nowTdscAppNodeStat = (TdscAppNodeStat)list.get(0);
				nowTdscAppNodeStat.setNodeStat("02");
				nowTdscAppNodeStat.setStartDate(new Timestamp(System.currentTimeMillis()));
			}
			// 取得下一点
			condition.setNodeId("03");
			List newList = this.tdscAppNodeStatDao.findNodeList(condition);
			if (newList != null && newList.size() > 0) {
				TdscAppNodeStat tdscAppNodeStat = (TdscAppNodeStat) newList.get(0);
				tdscAppNodeStat.setNodeStat("01");
				tdscAppNodeStat.setStartDate(new Timestamp(System.currentTimeMillis()));
				tdscAppNodeStat.setEndDate(null);
			}
			// 3.重新设置 tdscopnn
			TdscOpnnCondition opnnCondition = new TdscOpnnCondition();
			opnnCondition.setIfOpt("00");
			opnnCondition.setAppId(appId);
			TdscOpnn tdscOpnn = (TdscOpnn) this.tdscOpnnDao.findOpnnInfo(opnnCondition);
			int actionNum = Integer.parseInt(NumberUtil.numberDisplay(tdscOpnn.getActionNum(),0));
			tdscOpnn.setActionOrgan(user.getRegionId());
			tdscOpnn.setActionUser(user.getDisplayName());
			tdscOpnn.setActionUserId(user.getUserId());
			tdscOpnn.setActionDate(new Timestamp(System.currentTimeMillis()));
			tdscOpnn.setResultId(tdscApp.getNodeId() + "01");
			tdscOpnn.setResultName("制订完成");
			tdscOpnn.setTextOpen("");
			tdscOpnn.setIsOpt("01");
			// 新增
			TdscOpnn newTdscOpnn = new TdscOpnn();
			newTdscOpnn.setAppId(appId);
			actionNum += 1;
			newTdscOpnn.setActionNum(new BigDecimal(actionNum));
			newTdscOpnn.setActionId("0301");
			newTdscOpnn.setFirstDate(new Timestamp(System.currentTimeMillis()));
			newTdscOpnn.setAccDate(new Timestamp(System.currentTimeMillis()));
			newTdscOpnn.setIsOpt("00");
			this.tdscOpnnDao.saveOrUpdate(newTdscOpnn);
			
			List blocklist = this.tdscBlockTranAppDao.findTranAppList(planId);
			if (blocklist != null) {
				StringBuffer sb = new StringBuffer();
				for (int t = 0 ; t < blocklist.size(); t++) {
					TdscBlockTranApp tranApp = (TdscBlockTranApp) blocklist.get(t);
					TdscBlockInfo blockInfo = (TdscBlockInfo) this.tdscBlockInfoDao.get(tranApp.getBlockId());
					sb.append(blockInfo.getBlockName());
					if (t + 1 != blocklist.size()) {
						sb.append(",");
					}
				}
				TdscBlockPlanTable blockPlanTable = (TdscBlockPlanTable)this.tdscBlockPlanTableDao.get(planId);
				blockPlanTable.setBlockName(sb.toString());
			}
		}
	}
}
