package com.wonders.tdsc.blockwork.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockScheduleTableDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockUsedInfoDao;
import com.wonders.tdsc.blockwork.web.form.ScheduletableInfoForm;
import com.wonders.tdsc.bo.TdscApp;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPart;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockScheduleTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscBlockPlanTableCondition;
import com.wonders.tdsc.bo.condition.TdscBlockTranAppCondition;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.bo.condition.TdscOpnnCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.dao.TdscAppDao;
import com.wonders.tdsc.flowadapter.dao.TdscAppNodeStatDao;
import com.wonders.tdsc.flowadapter.dao.TdscOpnnDao;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.flowadapter.service.CommonFlowService;
import com.wonders.tdsc.xborg.service.TdscXbOrgService;

public class TdscScheduletableService extends BaseSpringManagerImpl {

	// 土地用途信息表对应的DAO
	private TdscBlockUsedInfoDao		tdscBlockUsedInfoDao;

	// 出让地块进度执行表对应的DAO
	private TdscBlockScheduleTableDao	tdscBlockScheduleTableDao;

	// 出让地块进度安排表对应的DAO
	private TdscBlockPlanTableDao		tdscBlockPlanTableDao;

	private TdscBlockTranAppDao			tdscBlockTranAppDao;

	private TdscBlockInfoDao			tdscBlockInfoDao;

	private TdscAppDao					tdscAppDao;

	private TdscAppNodeStatDao			tdscAppNodeStatDao;

	private TdscOpnnDao					tdscOpnnDao;

	private CommonFlowService			commonFlowService;

	private AppFlowService				appFlowService;

	private TdscBlockPartDao			tdscBlockPartDao;

	private TdscXbOrgService			tdscXbOrgService;

	public void setTdscXbOrgService(TdscXbOrgService tdscXbOrgService) {
		this.tdscXbOrgService = tdscXbOrgService;
	}

	public void setTdscBlockPartDao(TdscBlockPartDao tdscBlockPartDao) {
		this.tdscBlockPartDao = tdscBlockPartDao;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setCommonFlowService(CommonFlowService commonFlowService) {
		this.commonFlowService = commonFlowService;
	}

	public void setTdscAppNodeStatDao(TdscAppNodeStatDao tdscAppNodeStatDao) {
		this.tdscAppNodeStatDao = tdscAppNodeStatDao;
	}

	public void setTdscAppDao(TdscAppDao tdscAppDao) {
		this.tdscAppDao = tdscAppDao;
	}

	public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
		this.tdscBlockInfoDao = tdscBlockInfoDao;
	}

	public void setTdscOpnnDao(TdscOpnnDao tdscOpnnDao) {
		this.tdscOpnnDao = tdscOpnnDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}

	public void setTdscBlockUsedInfoDao(TdscBlockUsedInfoDao tdscBlockUsedInfoDao) {
		this.tdscBlockUsedInfoDao = tdscBlockUsedInfoDao;
	}

	public void setTdscBlockScheduleTableDao(TdscBlockScheduleTableDao tdscBlockScheduleTableDao) {
		this.tdscBlockScheduleTableDao = tdscBlockScheduleTableDao;
	}

	/**
	 * 通过地块ID来查询土地用途信息的list
	 * 
	 * @param blockId
	 * @return tdscBlockUsedInfoList 20071121*
	 */
	public List queryTdscBlockUsedInfoList(String blockId) {
		// 根据blockId得到地块用途信息表中的规划用途和出让年限
		List tdscBlockUsedInfoList = tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockId);
		return tdscBlockUsedInfoList;
	}

	/**
	 * 通过appId来查询出让地块进度执行表的信息
	 * 
	 * @param appId
	 * @return tdscBlockScheduleTable 20071124*
	 */
	public TdscBlockScheduleTable findScheduleInfo(String appId) {
		TdscBlockScheduleTable tdscBlockScheduleTable = tdscBlockScheduleTableDao.findScheduleInfo(appId);
		return tdscBlockScheduleTable;
	}
	
	public void savePlanTableInfo(TdscBlockPlanTable tdscBlockPlanTable){
		this.tdscBlockPlanTableDao.saveOrUpdate(tdscBlockPlanTable);
	}
	/**
	 * 录入出让地块进度安排表信息
	 * 
	 * @param 对象tdscBlockPlanTable
	 *            地块交易信息表的appId 出让方式mode 20071201*
	 */
	public void savePlanTableInfo(TdscBlockPlanTable tdscBlockPlanTable, String appId, String mode, TdscBlockTranApp tdscBlockTranApp, String saveType, SysUser user) {

		// 根据appId查询出让地块进度安排表的值是否为空，不为空更新数据，否则保存
		TdscBlockPlanTable planTable = tdscBlockPlanTableDao.findPlanTableInfo(appId);
		if (null != planTable) {
			// planTable.setRecMatStartDate(tdscBlockPlanTable.getRecMatStartDate());
			// planTable.setRecMatEndDate(tdscBlockPlanTable.getRecMatEndDate());
			// planTable.setNoticeStartDate(tdscBlockPlanTable.getNoticeStartDate());
			// planTable.setNoticeEndDate(tdscBlockPlanTable.getNoticeEndDate());
			planTable.setIssueStartDate(tdscBlockPlanTable.getIssueStartDate());
			planTable.setIssueEndDate(tdscBlockPlanTable.getIssueEndDate());
			planTable.setGetFileStartDate(tdscBlockPlanTable.getGetFileStartDate());
			planTable.setGetFileEndDate(tdscBlockPlanTable.getGetFileEndDate());
			planTable.setInspDate(tdscBlockPlanTable.getInspDate());
			planTable.setInspLoc(tdscBlockPlanTable.getInspLoc());
			planTable.setGatherDate(tdscBlockPlanTable.getGatherDate());
			planTable.setAnswerDate(tdscBlockPlanTable.getAnswerDate());
			planTable.setAnswerLoc(tdscBlockPlanTable.getAnswerLoc());
			planTable.setRelFaqDate(tdscBlockPlanTable.getRelFaqDate());
			planTable.setAccAppStatDate(tdscBlockPlanTable.getAccAppStatDate());
			planTable.setAccAppEndDate(tdscBlockPlanTable.getAccAppEndDate());
			planTable.setResultShowDate(tdscBlockPlanTable.getResultShowDate());
			// 根据出让方式选择更新相应的值：3107招标 3103拍卖 3104挂牌
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
				planTable.setListStartDate(tdscBlockPlanTable.getListStartDate());
				planTable.setListEndDate(tdscBlockPlanTable.getListEndDate());
				planTable.setSceBidDate(tdscBlockPlanTable.getSceBidDate());
				planTable.setSceBidLoc(tdscBlockPlanTable.getSceBidLoc());
			} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
				planTable.setAuctionDate(tdscBlockPlanTable.getAuctionDate());
				planTable.setAuctionLoc(tdscBlockPlanTable.getAuctionLoc());
			} else {
				planTable.setTenderStartDate(tdscBlockPlanTable.getTenderStartDate());
				planTable.setTenderEndDate(tdscBlockPlanTable.getTenderEndDate());
				planTable.setOpeningDate(tdscBlockPlanTable.getOpeningDate());
				planTable.setOpeningLoc(tdscBlockPlanTable.getOpeningLoc());
				planTable.setBidEvaDate(tdscBlockPlanTable.getBidEvaDate());
				planTable.setBidEvaLoc(tdscBlockPlanTable.getBidEvaLoc());
			}
			planTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
			tdscBlockPlanTableDao.update(planTable);
		} else {
			tdscBlockPlanTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
			tdscBlockPlanTable.setStatus(GlobalConstants.PLAN_TABLE_ACTIVE);
			tdscBlockPlanTableDao.save(tdscBlockPlanTable);
		}
		// 根据appId查询TDSC_BLOCK_TRAN_APP表的值是否为空，不为空更新数据，否则保存
		TdscBlockTranApp tempTranApp = tdscBlockTranAppDao.findTdscBlockTranAppInfo(appId);
		if (tempTranApp != null) {
			tempTranApp.setMarginEndDate(tdscBlockTranApp.getMarginEndDate());
			tdscBlockTranAppDao.update(tempTranApp);
		} else {
			tdscBlockTranAppDao.save(tdscBlockTranApp);
		}

		try {
			if ("tempSave".equals(saveType)) {
				this.appFlowService.tempSaveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
			} else if ("submitSave".equals(saveType)) {
				this.appFlowService.saveOpnn(appId, tdscBlockTranApp.getTransferMode(), user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 20090626保存实施方案
	 * 
	 * @param tdscBlockPlanTable
	 * @param infoForm
	 * @param saveType
	 * @param user
	 * @return
	 */
	public TdscBlockPlanTable saveUnitePlanTable(TdscBlockPlanTable tdscBlockPlanTable, ScheduletableInfoForm infoForm, String saveType, SysUser user) {
		// 设置进度安排表的有效状态
		tdscBlockPlanTable.setStatus("01");
		// 设置招拍挂编号
		StringBuffer tradeNum = new StringBuffer("");
		tradeNum = tradeNum.append(StringUtils.trimToEmpty(infoForm.getTradeNum_1())).append("-").append(StringUtils.trimToEmpty(infoForm.getTradeNum_2())).append("-")
				.append(StringUtils.trimToEmpty(infoForm.getTradeNum_3()));
		tdscBlockPlanTable.setTradeNum(tradeNum.toString());
		// 设置操作时间
		tdscBlockPlanTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
		TdscBlockPlanTable obj = (TdscBlockPlanTable) tdscBlockPlanTableDao.saveOrUpdate(tdscBlockPlanTable);
		// 获得appId
		String appIds[] = infoForm.getAppIds();
		StringBuffer blockNames = new StringBuffer("");
		StringBuffer partBlockCode = new StringBuffer("");
		StringBuffer landLocation = new StringBuffer("");
		// 判断是否保存协办机构信息，并将下一个协办机构置为待选的协办机构；
		// boolean updateOrgHistory = false;

		for (int i = 0; i < appIds.length; i++) {
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appIds[i]);
			// 设置交易信息表中的进度安排表ID，以此关联两张表
			tdscBlockTranApp.setPlanId(obj.getPlanId());
			// 设置保证金截止日期
			tdscBlockTranApp.setMarginEndDate(infoForm.getMarginEndDate());
			// 协办机构
			if (infoForm.getBzXbjg() != null && !"".equals(infoForm.getBzXbjg())) {
				tdscBlockTranApp.setBzXbjg(infoForm.getBzXbjg());
			}
			// 拍卖佣金
			if (infoForm.getBzPmyj() != null && !"".equals(infoForm.getBzPmyj())) {
				tdscBlockTranApp.setBzPmyj(infoForm.getBzPmyj());
			}
			// 挂牌手续费
			if (infoForm.getBzGpsxf() != null && !"".equals(infoForm.getBzGpsxf())) {
				tdscBlockTranApp.setBzGpsxf(infoForm.getBzGpsxf());
			}
			// 备注
			if (infoForm.getBzBeizhu() != null && !"".equals(infoForm.getBzBeizhu())) {
				tdscBlockTranApp.setBzBeizhu(infoForm.getBzBeizhu());
			}
			tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
			// 拼接该进度安排中所包含的地块的名称
			if (tdscBlockTranApp.getBlockId() != null) {
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(tdscBlockTranApp.getBlockId());
				// 拼接地块名称
				if (i < appIds.length - 1) {
					blockNames.append(tdscBlockInfo.getBlockName()).append(",");
				} else {
					blockNames.append(tdscBlockInfo.getBlockName());
				}
				// 拼接地块位置
				if (tdscBlockInfo.getLandLocation() != null && !"".equals(tdscBlockInfo.getLandLocation())) {
					if (i < appIds.length - 1) {
						landLocation.append(tdscBlockInfo.getLandLocation()).append(",");
					} else {
						landLocation.append(tdscBlockInfo.getLandLocation());
					}
				}
				// 拼接地块编号
				List blockPartList = (List) tdscBlockPartDao.getTdscBlockPartList(tdscBlockTranApp.getBlockId());
				if (blockPartList != null && blockPartList.size() > 0) {
					for (int j = 0; j < blockPartList.size(); j++) {
						TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(j);
						partBlockCode.append(tdscBlockPart.getBlockCode()).append(",");
					}
				}
			}
			// 推动流程
			try {
				if ("tempSave".equals(saveType)) {
					tdscBlockPlanTable.setIfPCommit("1");
					this.appFlowService.tempSaveOpnn(tdscBlockTranApp.getAppId(), tdscBlockTranApp.getTransferMode(), user);
				}
				// 审核提交的时候推动流程
				if ("01".equals(tdscBlockPlanTable.getStatusFlow())) {
					if ("submitSave".equals(saveType)) {
						this.appFlowService.saveOpnn(tdscBlockTranApp.getAppId(), tdscBlockTranApp.getTransferMode(), user);
						// 判断是否保存协办机构信息，并将下一个协办机构置为待选的协办机构；
						// updateOrgHistory = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设置进度安排表中地块名称，为多个地块的地块名称累加
		tdscBlockPlanTable.setBlockName(blockNames.toString());
		tdscBlockPlanTable.setLandLocation(landLocation.toString());
		// 设置地块编号
		if (partBlockCode.toString() != null && !"".equals(partBlockCode.toString())) {
			tdscBlockPlanTable.setUniteBlockCode(partBlockCode.toString().substring(0, partBlockCode.toString().length() - 1));
		}
		if ("submitSave".equals(saveType)) {
			if ("01".equals(tdscBlockPlanTable.getStatusFlow())) {
				tdscBlockPlanTable.setStatusFlow("02");
			}
			if ("00".equals(tdscBlockPlanTable.getStatusFlow())) {
				tdscBlockPlanTable.setStatusFlow("01");
			}
		}
		TdscBlockPlanTable retObj = (TdscBlockPlanTable) tdscBlockPlanTableDao.saveOrUpdate(tdscBlockPlanTable);
		// 审核提交推动流程时，保存协办机构信息，并将下一个协办机构置为待选的协办机构；
		// if(updateOrgHistory){
		// tdscXbOrgService.updateTdscXbOrgHistory(infoForm.getOrgAppId(), infoForm.getOrgInfoId(), tradeNum.toString(), retObj.getPlanId());
		// }

		return retObj;
	}

	/**
	 * 录入出让地块进度安排表的会议编号信息
	 * 
	 * @param 对象tdscBlockPlanTable
	 *            地块交易信息表的appId 出让方式mode 20080311*
	 */
	public void saveMeetingNo(TdscBlockPlanTable planTableMeetingNo, String appId, String mode) {
		// 根据appId查询出让地块进度安排表的值是否为空，不为空更新数据，否则保存
		TdscBlockPlanTable planTable = tdscBlockPlanTableDao.findPlanTableInfo(appId);
		if (null != planTable) {
			// 根据出让方式选择更新相应的值：3107招标 3103拍卖 3104挂牌
			if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
				planTable.setListMeetingNo(planTableMeetingNo.getListMeetingNo());
				planTable.setSceBidMeetingNo(planTableMeetingNo.getSceBidMeetingNo());
			} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
				planTable.setAuctionMeetingNo(planTableMeetingNo.getAuctionMeetingNo());
			} else {
				planTable.setTenderMeetingNo(planTableMeetingNo.getTenderMeetingNo());
				planTable.setOpeningMeetingNo(planTableMeetingNo.getOpeningMeetingNo());
				planTable.setBidEvaMeetingNo(planTableMeetingNo.getBidEvaMeetingNo());
			}
			planTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
			tdscBlockPlanTableDao.update(planTable);
		} else {
			planTableMeetingNo.setLastActionDate(new Timestamp(System.currentTimeMillis()));
			tdscBlockPlanTableDao.save(planTableMeetingNo);
		}
	}

	/**
	 * 录入出让地块进度安排表信息
	 * 
	 * @param 对象tdscBlockPlanTable
	 *            地块交易信息表的appId 出让方式mode 20071201*
	 */
	public void modifyPlanTableInfo(TdscBlockPlanTable tdscBlockPlanTable, String appId, String mode, TdscBlockTranApp tdscBlockTranApp) {

		// 根据appId查询出让地块进度安排表的值是否为空，不为空更新字段Status
		TdscBlockPlanTableCondition condition = new TdscBlockPlanTableCondition();
		condition.setAppId(appId);
		condition.setStatus(GlobalConstants.PLAN_TABLE_ACTIVE);
		TdscBlockPlanTable planTable = tdscBlockPlanTableDao.findBlockPlanTableInfo(condition);
		if (null != planTable) {
			planTable.setStatus(GlobalConstants.PLAN_TABLE_HISTORY);
			planTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
			tdscBlockPlanTableDao.update(planTable);
		}
		if (null != tdscBlockPlanTable) {
			if (null != planTable) {
				// 根据出让方式选择更新相应的值：3107招标 3103拍卖 3104挂牌
				if (GlobalConstants.DIC_TRANSFER_LISTING.equals(mode) && null != mode) {
					if (planTable.getListMeetingNo() != null) {
						tdscBlockPlanTable.setListMeetingNo(planTable.getListMeetingNo());
					}
					if (planTable.getSceBidMeetingNo() != null) {
						tdscBlockPlanTable.setSceBidMeetingNo(planTable.getSceBidMeetingNo());
					}
				} else if (GlobalConstants.DIC_TRANSFER_AUCTION.equals(mode) && null != mode) {
					if (planTable.getAuctionMeetingNo() != null) {
						tdscBlockPlanTable.setAuctionMeetingNo(planTable.getAuctionMeetingNo());
					}
				} else {
					if (planTable.getTenderMeetingNo() != null) {
						tdscBlockPlanTable.setTenderMeetingNo(planTable.getTenderMeetingNo());
					}
					if (planTable.getOpeningMeetingNo() != null) {
						tdscBlockPlanTable.setOpeningMeetingNo(planTable.getOpeningMeetingNo());
					}
					if (planTable.getBidEvaMeetingNo() != null) {
						tdscBlockPlanTable.setBidEvaMeetingNo(planTable.getBidEvaMeetingNo());
					}
				}
				// 纪录是否给门禁系统发送过信息的标识
				if ("1".equals(planTable.getIfSendGuard())) {
					tdscBlockPlanTable.setIfSendGuard("1");
				}
			}
			tdscBlockPlanTable.setStatus(GlobalConstants.PLAN_TABLE_ACTIVE);
			tdscBlockPlanTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
			tdscBlockPlanTableDao.save(tdscBlockPlanTable);
		}
		// 根据appId查询TDSC_BLOCK_TRAN_APP表的值是否为空，不为空更新数据，否则保存
		TdscBlockTranApp tempTranApp = tdscBlockTranAppDao.findTdscBlockTranAppInfo(appId);
		if (tempTranApp != null) {
			tempTranApp.setMarginEndDate(tdscBlockTranApp.getMarginEndDate());
			tdscBlockTranAppDao.update(tempTranApp);
		} else {
			tdscBlockTranAppDao.save(tdscBlockTranApp);
		}
	}

	/**
	 * 通过appId来查询出让地块进度安排表信息
	 * 
	 * @param appId
	 * @return TdscBlockPlanTable 20071203*
	 */
	public TdscBlockPlanTable findPlanTableInfo(String appId) {
		TdscBlockPlanTable tdscBlockPlanTable = tdscBlockPlanTableDao.findPlanTableInfo(appId);
		return tdscBlockPlanTable;
	}

	public TdscBlockPlanTable findPlanTableByPlanId(String planId) {
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableDao.get(planId);
		return tdscBlockPlanTable;
	}

	/**
	 * 通过TdscBlockPlanTableCondition(appId)来查询出让地块进度安排表信息
	 * 
	 * @param appId
	 * @return TdscBlockPlanTable 20071214*
	 */
	public TdscBlockPlanTable findBlockPlanTableInfo(TdscBlockPlanTableCondition condition) {
		TdscBlockPlanTable tdscBlockPlanTable = tdscBlockPlanTableDao.findBlockPlanTableInfo(condition);
		return tdscBlockPlanTable;
	}

	/**
	 * 通过TdscBlockPlanTableCondition(planId)来查询所有出让地块进度安排表信息
	 * 
	 * @param appId
	 * @return TdscBlockPlanTableList 20090601*
	 */
	public List findBlockPlanTableInfoList(TdscBlockPlanTableCondition condition) {
		return tdscBlockPlanTableDao.findBlockPlanTableList(condition);
	}

	/**
	 * 通过appId来查询出让地块进度安排表信息
	 * 
	 * @param appId
	 * @return TdscBlockTranApp 20080505*
	 */
	public TdscBlockTranApp findBlockTranAppInfo(String appId) {
		TdscBlockTranApp tdscBlockTranApp = tdscBlockTranAppDao.findTdscBlockTranApp(appId);
		return tdscBlockTranApp;
	}

	/**
	 * 交易终止，结束某个地块的业务
	 * 
	 * @param appId
	 */
	public boolean endTradeByAppId(TdscBlockAppView tdscBlockAppView, SysUser user) {
		boolean ifActionSuccess = false;
		String appId = (String) tdscBlockAppView.getAppId();
		Timestamp actionDate = new Timestamp(System.currentTimeMillis());
		// 申请业务表
		TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
		tdscApp.setNodeId("90");
		tdscApp.setNodeDate(actionDate);
		tdscApp.setStatusId(FlowConstants.FLOW_STATUS_END_TRADE);
		tdscApp.setStatusDate(actionDate);
		tdscAppDao.update(tdscApp);
		// 地块交易信息表
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
		// 地块基本信息表
		TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(tdscBlockTranApp.getBlockId());
		// 在TDSC_ES_DIC_CONFIG的18234字典中"04=交易终止"
		tdscBlockTranApp.setTranResult("04");
		tdscBlockInfo.setTranResult("04");
		tdscBlockInfo.setStatus("02");
		
		tdscBlockTranAppDao.update(tdscBlockTranApp);
		tdscBlockInfoDao.update(tdscBlockInfo);
		// 修改申请业务节点状态表
		TdscFlowCondition nodeCondition = new TdscFlowCondition();
		nodeCondition.setAppId(appId);
		// 查询该地块在TDSC_APP_NODE_STAT表中在办节点列表
		List tdscAppNodeStatList = (List) tdscAppNodeStatDao.findActiveNodeList(nodeCondition);
		// 关闭所有在办节点
		if (tdscAppNodeStatList.size() > 0 && tdscAppNodeStatList != null) {
			TdscAppNodeStat appNodeStat = new TdscAppNodeStat();
			for (int j = 0; j < tdscAppNodeStatList.size(); j++) {
				appNodeStat = (TdscAppNodeStat) tdscAppNodeStatList.get(j);
				if (!FlowConstants.STAT_END.equals(appNodeStat.getNodeStat())) {
					appNodeStat.setNodeStat(FlowConstants.STAT_END);
					appNodeStat.setEndDate(new Date(System.currentTimeMillis()));
					tdscAppNodeStatDao.update(appNodeStat); // 将节点状态更新为已关闭并记录关闭时间
				}
			}
		}
		// 申请历史业务表
		TdscOpnnCondition tdscOpnnCondition = new TdscOpnnCondition();
		tdscOpnnCondition.setAppId(appId);
		List tdscOpnnList = (List) tdscOpnnDao.findOpnnList(tdscOpnnCondition);
		if (tdscOpnnList != null && tdscOpnnList.size() > 0) {
			TdscOpnn tdscOpnn = new TdscOpnn();
			// 将所有IS_OPT设为01;
			for (int i = 0; i < tdscOpnnList.size(); i++) {
				tdscOpnn = (TdscOpnn) tdscOpnnList.get(i);
				if (!AppFlowService.IS_OPT_TRUE.equals(tdscOpnn.getIsOpt())) {
					tdscOpnn.setIsOpt(AppFlowService.IS_OPT_TRUE);
					if (i == tdscOpnnList.size() - 1) {
						String userOrgan = user.getRegionId();
						String userId = user.getUserId();
						String userName = user.getDisplayName();
						tdscOpnn.setActionId("9004");
						tdscOpnn.setResultName("交易终止");
						tdscOpnn.setActionOrgan(userOrgan);
						tdscOpnn.setActionUserId(userId);
						tdscOpnn.setActionUser(userName);
						tdscOpnn.setActionDate(actionDate);
						// 纪录操作意见
						tdscOpnn.setTextOpen(tdscBlockAppView.getTempStr());
					}
					tdscOpnnDao.update(tdscOpnn);
				}
			}
		}
		ifActionSuccess = true;

		return ifActionSuccess;
	}

	/**
	 * 查询进度安排表
	 * 
	 * @return
	 */
	public List queryUnitePlanTable() {
		return tdscBlockPlanTableDao.queryUnitePlanTable();
	}

	/**
	 * 根据条件查询交易信息表
	 * 
	 * @param condition
	 * @return
	 */
	public List queryTranAppList(TdscBlockTranAppCondition condition) {

		return tdscBlockTranAppDao.findTranAppList(condition);
	}

	public List queryTranAppList(String planId) {

		return tdscBlockTranAppDao.findTranAppList(planId);
	}

	public List queryPlanTableList(TdscBlockPlanTableCondition condition) {

		return tdscBlockPlanTableDao.findBlockPlanTableList(condition);
	}

	/**
	 * 删除进度安排表，并删除交易信息表中的PLAN_ID
	 * 
	 * @param tdscBlockPlanTable
	 * @param tranAppList
	 */
	public void delPlanTable(TdscBlockPlanTable tdscBlockPlanTable, List tranAppList) {
		// TDSC_APP NODE_ID and STATUS_ID 也要重置
		if (tdscBlockPlanTable != null) {
			tdscBlockPlanTableDao.delete(tdscBlockPlanTable);
		}

		if (tranAppList != null && tranAppList.size() > 0) {
			for (int i = 0; i < tranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tranAppList.get(i);

				String appId = tdscBlockTranApp.getAppId();

				tdscBlockTranApp.setPlanId("");
				tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
				// tdscBlockTranAppDao.deleteById(appId);

				TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
				tdscApp.setNodeId("02");
				tdscApp.setStatusId("0201");
				tdscAppDao.saveOrUpdate(tdscApp);

				// 删除意见
				TdscOpnnCondition opnnCond = new TdscOpnnCondition();
				opnnCond.setAppId(appId);
				List opnnList = tdscOpnnDao.findOpnnList(opnnCond);
				if (opnnList != null && opnnList.size() > 0)
					for (int m = 0; m < opnnList.size(); m++) {
						TdscOpnn tdscOpnn = (TdscOpnn) opnnList.get(m);
						if ("0302".equals(tdscOpnn.getActionId())) {
							// delete it
							tdscOpnnDao.deleteById(tdscOpnn.getTdscOpnnId());
						}
						if ("0201".equals(tdscOpnn.getActionId())) {
							Timestamp nowDate = new Timestamp(System.currentTimeMillis());

							// update it
							tdscOpnnDao.deleteById(tdscOpnn.getTdscOpnnId());

							TdscOpnn tdscOpnnTmp = new TdscOpnn();
							tdscOpnnTmp.setAppId(appId);
							tdscOpnnTmp.setActionNum(new BigDecimal("2"));
							tdscOpnnTmp.setActionId("0201");
							tdscOpnnTmp.setIsOpt("00");
							tdscOpnnTmp.setFirstDate(nowDate);
							tdscOpnnTmp.setAccDate(nowDate);

							tdscOpnnDao.save(tdscOpnnTmp);

						}
					}

				String flowId = "01"; // 挂牌
				String nodeId02 = "02"; // 制定工作方案
				String nodeId03 = "03"; // 制作出让公告和文件

				// 需要把状态回退回去
				// node_id=03 flow_id=01 app_id=? update node_stat=00 start_date=null end_date=null sub_flow_id=null
				// node_id=02 flow_id=01 app_id=? update node_stat=01 end_date=null

				TdscAppNodeStat appStat02 = (TdscAppNodeStat) tdscAppNodeStatDao.findAppNodeStatInfo(flowId, nodeId02, appId);
				appStat02.setNodeStat("01");
				appStat02.setEndDate(null);
				tdscAppNodeStatDao.saveOrUpdate(appStat02);

				TdscAppNodeStat appStat03 = (TdscAppNodeStat) tdscAppNodeStatDao.findAppNodeStatInfo(flowId, nodeId03, appId);

				appStat03.setNodeStat("00");
				appStat03.setStartDate(null);
				appStat03.setEndDate(null);
				appStat03.setSubFlowId(null);
				tdscAppNodeStatDao.saveOrUpdate(appStat03);

			}
		}
	}

	/**
	 * 保存进度安排表，并修改相应地块的保证金截止时间
	 */
	public void savePlanTable(TdscBlockPlanTable tdscBlockPlanTable, List tranAppList, Timestamp getMarginEndDate, String saveType, SysUser user) {
		if ("submitSave".equals(saveType)) {
			tdscBlockPlanTable.setIfPCommit("1");
		}
		tdscBlockPlanTableDao.saveOrUpdate(tdscBlockPlanTable);
		if (getMarginEndDate != null) {
			if (tranAppList != null && tranAppList.size() > 0) {
				for (int i = 0; i < tranAppList.size(); i++) {
					TdscBlockTranApp app = (TdscBlockTranApp) tranAppList.get(i);
					app.setMarginEndDate(getMarginEndDate);
					tdscBlockTranAppDao.update(app);

					try {
						if ("tempSave".equals(saveType)) {
							this.appFlowService.tempSaveOpnn(app.getAppId(), app.getTransferMode(), user);
						} else if ("submitSave".equals(saveType)) {
							this.appFlowService.saveOpnn(app.getAppId(), app.getTransferMode(), user);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	public TdscBlockInfo getBlockInfoApp(String blockId) {
		TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(blockId);
		return tdscBlockInfo;
	}

	public TdscBlockTranApp getBlockTranAppInfo(String blcokId) {
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.getTdscBlockTranAppInfo(blcokId);
		return tdscBlockTranApp;
	}

	public List queryBlockIdListByNoticeId(String noticeId) {
		return tdscBlockTranAppDao.queryBlockIdListByNoticeId(noticeId);
	}

	public List queryBlockIdListByPlanId(String planId) {
		return tdscBlockTranAppDao.queryBlockIdListByPlanId(planId);
	}

	public void plantableSendBack(String planId) {
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableDao.get(planId);
		if (tdscBlockPlanTable != null) {
			tdscBlockPlanTable.setStatusFlow("00");
			tdscBlockPlanTableDao.saveOrUpdate(tdscBlockPlanTable);
		}
	}

	public boolean checkTradeNumAjax(String tradeNum, String planId) {
		return tdscBlockPlanTableDao.checkTradeNumAjax(tradeNum, planId);
	}
	public boolean checkListingEndDate(String listingEndDate,String planId){
		return tdscBlockPlanTableDao.checkListingEndDate(listingEndDate,planId);
	}
	/**
	 * 跟新tdscBlockPlanTable实施方案信息
	 */
	public void updateBlockPlanTable(TdscBlockPlanTable tdscBlockPlanTable) {
		tdscBlockPlanTableDao.update(tdscBlockPlanTable);
	}

	/**
	 * 根据planId获得实施方案信息
	 * 
	 * @param planId
	 * @return
	 */
	public TdscBlockPlanTable findTdscBlockPlanTable(String planId) {
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableDao.get(planId);
		return tdscBlockPlanTable;
	}

	/**
	 * 根据blockId查询子地块信息
	 * 
	 * @param blockId
	 * @return
	 */
	public List getTdscBlockPartList(String blockId) {
		return (List) tdscBlockPartDao.getTdscBlockPartList(blockId);
	}

	public String getNextTradeNum(String startindex,String tradeNumPrefix) {
		String currTradeNum = tdscBlockPlanTableDao.getCurrTradeNum(startindex,tradeNumPrefix);
		int maxValue = Integer.parseInt(currTradeNum);
		maxValue++;
		String nextTradeNum = "000" + maxValue;
		nextTradeNum = nextTradeNum.substring(nextTradeNum.length() - 3, nextTradeNum.length());

		return nextTradeNum;
	}

	public boolean deleteBlockInfo(String strAppId, String blockId) {
		// tdsc_block_info blockid
		// tdsc_app appid
		// tdsc_block_tran_app appid
		// tdsc_opnn appid

		tdscBlockInfoDao.deleteById(blockId);
		tdscAppDao.deleteById(strAppId);
		tdscBlockTranAppDao.deleteById(strAppId);
		// tdscOpnnDao.deleteByAppId(strAppId);

		return false;
	}
	//查询所有地块信息
	public List queryTdscBlockInfoList(TdscBlockPlanTableCondition condition) {

		return tdscBlockInfoDao.queryTdscBlockInfoList(condition);
	}
	//根据名称得到地块信息
	public List findPlanTableByBlockName(String blockName) {
		return tdscBlockPlanTableDao.findPlanTableInfoByName(blockName);
	}
}
