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

	// ������;��Ϣ���Ӧ��DAO
	private TdscBlockUsedInfoDao		tdscBlockUsedInfoDao;

	// ���õؿ����ִ�б��Ӧ��DAO
	private TdscBlockScheduleTableDao	tdscBlockScheduleTableDao;

	// ���õؿ���Ȱ��ű��Ӧ��DAO
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
	 * ͨ���ؿ�ID����ѯ������;��Ϣ��list
	 * 
	 * @param blockId
	 * @return tdscBlockUsedInfoList 20071121*
	 */
	public List queryTdscBlockUsedInfoList(String blockId) {
		// ����blockId�õ��ؿ���;��Ϣ���еĹ滮��;�ͳ�������
		List tdscBlockUsedInfoList = tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockId);
		return tdscBlockUsedInfoList;
	}

	/**
	 * ͨ��appId����ѯ���õؿ����ִ�б����Ϣ
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
	 * ¼����õؿ���Ȱ��ű���Ϣ
	 * 
	 * @param ����tdscBlockPlanTable
	 *            �ؿ齻����Ϣ���appId ���÷�ʽmode 20071201*
	 */
	public void savePlanTableInfo(TdscBlockPlanTable tdscBlockPlanTable, String appId, String mode, TdscBlockTranApp tdscBlockTranApp, String saveType, SysUser user) {

		// ����appId��ѯ���õؿ���Ȱ��ű��ֵ�Ƿ�Ϊ�գ���Ϊ�ո������ݣ����򱣴�
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
			// ���ݳ��÷�ʽѡ�������Ӧ��ֵ��3107�б� 3103���� 3104����
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
		// ����appId��ѯTDSC_BLOCK_TRAN_APP���ֵ�Ƿ�Ϊ�գ���Ϊ�ո������ݣ����򱣴�
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
	 * 20090626����ʵʩ����
	 * 
	 * @param tdscBlockPlanTable
	 * @param infoForm
	 * @param saveType
	 * @param user
	 * @return
	 */
	public TdscBlockPlanTable saveUnitePlanTable(TdscBlockPlanTable tdscBlockPlanTable, ScheduletableInfoForm infoForm, String saveType, SysUser user) {
		// ���ý��Ȱ��ű����Ч״̬
		tdscBlockPlanTable.setStatus("01");
		// �������Ĺұ��
		StringBuffer tradeNum = new StringBuffer("");
		tradeNum = tradeNum.append(StringUtils.trimToEmpty(infoForm.getTradeNum_1())).append("-").append(StringUtils.trimToEmpty(infoForm.getTradeNum_2())).append("-")
				.append(StringUtils.trimToEmpty(infoForm.getTradeNum_3()));
		tdscBlockPlanTable.setTradeNum(tradeNum.toString());
		// ���ò���ʱ��
		tdscBlockPlanTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
		TdscBlockPlanTable obj = (TdscBlockPlanTable) tdscBlockPlanTableDao.saveOrUpdate(tdscBlockPlanTable);
		// ���appId
		String appIds[] = infoForm.getAppIds();
		StringBuffer blockNames = new StringBuffer("");
		StringBuffer partBlockCode = new StringBuffer("");
		StringBuffer landLocation = new StringBuffer("");
		// �ж��Ƿ񱣴�Э�������Ϣ��������һ��Э�������Ϊ��ѡ��Э�������
		// boolean updateOrgHistory = false;

		for (int i = 0; i < appIds.length; i++) {
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appIds[i]);
			// ���ý�����Ϣ���еĽ��Ȱ��ű�ID���Դ˹������ű�
			tdscBlockTranApp.setPlanId(obj.getPlanId());
			// ���ñ�֤���ֹ����
			tdscBlockTranApp.setMarginEndDate(infoForm.getMarginEndDate());
			// Э�����
			if (infoForm.getBzXbjg() != null && !"".equals(infoForm.getBzXbjg())) {
				tdscBlockTranApp.setBzXbjg(infoForm.getBzXbjg());
			}
			// ����Ӷ��
			if (infoForm.getBzPmyj() != null && !"".equals(infoForm.getBzPmyj())) {
				tdscBlockTranApp.setBzPmyj(infoForm.getBzPmyj());
			}
			// ����������
			if (infoForm.getBzGpsxf() != null && !"".equals(infoForm.getBzGpsxf())) {
				tdscBlockTranApp.setBzGpsxf(infoForm.getBzGpsxf());
			}
			// ��ע
			if (infoForm.getBzBeizhu() != null && !"".equals(infoForm.getBzBeizhu())) {
				tdscBlockTranApp.setBzBeizhu(infoForm.getBzBeizhu());
			}
			tdscBlockTranAppDao.saveOrUpdate(tdscBlockTranApp);
			// ƴ�Ӹý��Ȱ������������ĵؿ������
			if (tdscBlockTranApp.getBlockId() != null) {
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(tdscBlockTranApp.getBlockId());
				// ƴ�ӵؿ�����
				if (i < appIds.length - 1) {
					blockNames.append(tdscBlockInfo.getBlockName()).append(",");
				} else {
					blockNames.append(tdscBlockInfo.getBlockName());
				}
				// ƴ�ӵؿ�λ��
				if (tdscBlockInfo.getLandLocation() != null && !"".equals(tdscBlockInfo.getLandLocation())) {
					if (i < appIds.length - 1) {
						landLocation.append(tdscBlockInfo.getLandLocation()).append(",");
					} else {
						landLocation.append(tdscBlockInfo.getLandLocation());
					}
				}
				// ƴ�ӵؿ���
				List blockPartList = (List) tdscBlockPartDao.getTdscBlockPartList(tdscBlockTranApp.getBlockId());
				if (blockPartList != null && blockPartList.size() > 0) {
					for (int j = 0; j < blockPartList.size(); j++) {
						TdscBlockPart tdscBlockPart = (TdscBlockPart) blockPartList.get(j);
						partBlockCode.append(tdscBlockPart.getBlockCode()).append(",");
					}
				}
			}
			// �ƶ�����
			try {
				if ("tempSave".equals(saveType)) {
					tdscBlockPlanTable.setIfPCommit("1");
					this.appFlowService.tempSaveOpnn(tdscBlockTranApp.getAppId(), tdscBlockTranApp.getTransferMode(), user);
				}
				// ����ύ��ʱ���ƶ�����
				if ("01".equals(tdscBlockPlanTable.getStatusFlow())) {
					if ("submitSave".equals(saveType)) {
						this.appFlowService.saveOpnn(tdscBlockTranApp.getAppId(), tdscBlockTranApp.getTransferMode(), user);
						// �ж��Ƿ񱣴�Э�������Ϣ��������һ��Э�������Ϊ��ѡ��Э�������
						// updateOrgHistory = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ���ý��Ȱ��ű��еؿ����ƣ�Ϊ����ؿ�ĵؿ������ۼ�
		tdscBlockPlanTable.setBlockName(blockNames.toString());
		tdscBlockPlanTable.setLandLocation(landLocation.toString());
		// ���õؿ���
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
		// ����ύ�ƶ�����ʱ������Э�������Ϣ��������һ��Э�������Ϊ��ѡ��Э�������
		// if(updateOrgHistory){
		// tdscXbOrgService.updateTdscXbOrgHistory(infoForm.getOrgAppId(), infoForm.getOrgInfoId(), tradeNum.toString(), retObj.getPlanId());
		// }

		return retObj;
	}

	/**
	 * ¼����õؿ���Ȱ��ű�Ļ�������Ϣ
	 * 
	 * @param ����tdscBlockPlanTable
	 *            �ؿ齻����Ϣ���appId ���÷�ʽmode 20080311*
	 */
	public void saveMeetingNo(TdscBlockPlanTable planTableMeetingNo, String appId, String mode) {
		// ����appId��ѯ���õؿ���Ȱ��ű��ֵ�Ƿ�Ϊ�գ���Ϊ�ո������ݣ����򱣴�
		TdscBlockPlanTable planTable = tdscBlockPlanTableDao.findPlanTableInfo(appId);
		if (null != planTable) {
			// ���ݳ��÷�ʽѡ�������Ӧ��ֵ��3107�б� 3103���� 3104����
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
	 * ¼����õؿ���Ȱ��ű���Ϣ
	 * 
	 * @param ����tdscBlockPlanTable
	 *            �ؿ齻����Ϣ���appId ���÷�ʽmode 20071201*
	 */
	public void modifyPlanTableInfo(TdscBlockPlanTable tdscBlockPlanTable, String appId, String mode, TdscBlockTranApp tdscBlockTranApp) {

		// ����appId��ѯ���õؿ���Ȱ��ű��ֵ�Ƿ�Ϊ�գ���Ϊ�ո����ֶ�Status
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
				// ���ݳ��÷�ʽѡ�������Ӧ��ֵ��3107�б� 3103���� 3104����
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
				// ��¼�Ƿ���Ž�ϵͳ���͹���Ϣ�ı�ʶ
				if ("1".equals(planTable.getIfSendGuard())) {
					tdscBlockPlanTable.setIfSendGuard("1");
				}
			}
			tdscBlockPlanTable.setStatus(GlobalConstants.PLAN_TABLE_ACTIVE);
			tdscBlockPlanTable.setLastActionDate(new Timestamp(System.currentTimeMillis()));
			tdscBlockPlanTableDao.save(tdscBlockPlanTable);
		}
		// ����appId��ѯTDSC_BLOCK_TRAN_APP���ֵ�Ƿ�Ϊ�գ���Ϊ�ո������ݣ����򱣴�
		TdscBlockTranApp tempTranApp = tdscBlockTranAppDao.findTdscBlockTranAppInfo(appId);
		if (tempTranApp != null) {
			tempTranApp.setMarginEndDate(tdscBlockTranApp.getMarginEndDate());
			tdscBlockTranAppDao.update(tempTranApp);
		} else {
			tdscBlockTranAppDao.save(tdscBlockTranApp);
		}
	}

	/**
	 * ͨ��appId����ѯ���õؿ���Ȱ��ű���Ϣ
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
	 * ͨ��TdscBlockPlanTableCondition(appId)����ѯ���õؿ���Ȱ��ű���Ϣ
	 * 
	 * @param appId
	 * @return TdscBlockPlanTable 20071214*
	 */
	public TdscBlockPlanTable findBlockPlanTableInfo(TdscBlockPlanTableCondition condition) {
		TdscBlockPlanTable tdscBlockPlanTable = tdscBlockPlanTableDao.findBlockPlanTableInfo(condition);
		return tdscBlockPlanTable;
	}

	/**
	 * ͨ��TdscBlockPlanTableCondition(planId)����ѯ���г��õؿ���Ȱ��ű���Ϣ
	 * 
	 * @param appId
	 * @return TdscBlockPlanTableList 20090601*
	 */
	public List findBlockPlanTableInfoList(TdscBlockPlanTableCondition condition) {
		return tdscBlockPlanTableDao.findBlockPlanTableList(condition);
	}

	/**
	 * ͨ��appId����ѯ���õؿ���Ȱ��ű���Ϣ
	 * 
	 * @param appId
	 * @return TdscBlockTranApp 20080505*
	 */
	public TdscBlockTranApp findBlockTranAppInfo(String appId) {
		TdscBlockTranApp tdscBlockTranApp = tdscBlockTranAppDao.findTdscBlockTranApp(appId);
		return tdscBlockTranApp;
	}

	/**
	 * ������ֹ������ĳ���ؿ��ҵ��
	 * 
	 * @param appId
	 */
	public boolean endTradeByAppId(TdscBlockAppView tdscBlockAppView, SysUser user) {
		boolean ifActionSuccess = false;
		String appId = (String) tdscBlockAppView.getAppId();
		Timestamp actionDate = new Timestamp(System.currentTimeMillis());
		// ����ҵ���
		TdscApp tdscApp = (TdscApp) tdscAppDao.get(appId);
		tdscApp.setNodeId("90");
		tdscApp.setNodeDate(actionDate);
		tdscApp.setStatusId(FlowConstants.FLOW_STATUS_END_TRADE);
		tdscApp.setStatusDate(actionDate);
		tdscAppDao.update(tdscApp);
		// �ؿ齻����Ϣ��
		TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appId);
		// �ؿ������Ϣ��
		TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(tdscBlockTranApp.getBlockId());
		// ��TDSC_ES_DIC_CONFIG��18234�ֵ���"04=������ֹ"
		tdscBlockTranApp.setTranResult("04");
		tdscBlockInfo.setTranResult("04");
		tdscBlockInfo.setStatus("02");
		
		tdscBlockTranAppDao.update(tdscBlockTranApp);
		tdscBlockInfoDao.update(tdscBlockInfo);
		// �޸�����ҵ��ڵ�״̬��
		TdscFlowCondition nodeCondition = new TdscFlowCondition();
		nodeCondition.setAppId(appId);
		// ��ѯ�õؿ���TDSC_APP_NODE_STAT�����ڰ�ڵ��б�
		List tdscAppNodeStatList = (List) tdscAppNodeStatDao.findActiveNodeList(nodeCondition);
		// �ر������ڰ�ڵ�
		if (tdscAppNodeStatList.size() > 0 && tdscAppNodeStatList != null) {
			TdscAppNodeStat appNodeStat = new TdscAppNodeStat();
			for (int j = 0; j < tdscAppNodeStatList.size(); j++) {
				appNodeStat = (TdscAppNodeStat) tdscAppNodeStatList.get(j);
				if (!FlowConstants.STAT_END.equals(appNodeStat.getNodeStat())) {
					appNodeStat.setNodeStat(FlowConstants.STAT_END);
					appNodeStat.setEndDate(new Date(System.currentTimeMillis()));
					tdscAppNodeStatDao.update(appNodeStat); // ���ڵ�״̬����Ϊ�ѹرղ���¼�ر�ʱ��
				}
			}
		}
		// ������ʷҵ���
		TdscOpnnCondition tdscOpnnCondition = new TdscOpnnCondition();
		tdscOpnnCondition.setAppId(appId);
		List tdscOpnnList = (List) tdscOpnnDao.findOpnnList(tdscOpnnCondition);
		if (tdscOpnnList != null && tdscOpnnList.size() > 0) {
			TdscOpnn tdscOpnn = new TdscOpnn();
			// ������IS_OPT��Ϊ01;
			for (int i = 0; i < tdscOpnnList.size(); i++) {
				tdscOpnn = (TdscOpnn) tdscOpnnList.get(i);
				if (!AppFlowService.IS_OPT_TRUE.equals(tdscOpnn.getIsOpt())) {
					tdscOpnn.setIsOpt(AppFlowService.IS_OPT_TRUE);
					if (i == tdscOpnnList.size() - 1) {
						String userOrgan = user.getRegionId();
						String userId = user.getUserId();
						String userName = user.getDisplayName();
						tdscOpnn.setActionId("9004");
						tdscOpnn.setResultName("������ֹ");
						tdscOpnn.setActionOrgan(userOrgan);
						tdscOpnn.setActionUserId(userId);
						tdscOpnn.setActionUser(userName);
						tdscOpnn.setActionDate(actionDate);
						// ��¼�������
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
	 * ��ѯ���Ȱ��ű�
	 * 
	 * @return
	 */
	public List queryUnitePlanTable() {
		return tdscBlockPlanTableDao.queryUnitePlanTable();
	}

	/**
	 * ����������ѯ������Ϣ��
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
	 * ɾ�����Ȱ��ű���ɾ��������Ϣ���е�PLAN_ID
	 * 
	 * @param tdscBlockPlanTable
	 * @param tranAppList
	 */
	public void delPlanTable(TdscBlockPlanTable tdscBlockPlanTable, List tranAppList) {
		// TDSC_APP NODE_ID and STATUS_ID ҲҪ����
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

				// ɾ�����
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

				String flowId = "01"; // ����
				String nodeId02 = "02"; // �ƶ���������
				String nodeId03 = "03"; // �������ù�����ļ�

				// ��Ҫ��״̬���˻�ȥ
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
	 * ������Ȱ��ű����޸���Ӧ�ؿ�ı�֤���ֹʱ��
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
	 * ����tdscBlockPlanTableʵʩ������Ϣ
	 */
	public void updateBlockPlanTable(TdscBlockPlanTable tdscBlockPlanTable) {
		tdscBlockPlanTableDao.update(tdscBlockPlanTable);
	}

	/**
	 * ����planId���ʵʩ������Ϣ
	 * 
	 * @param planId
	 * @return
	 */
	public TdscBlockPlanTable findTdscBlockPlanTable(String planId) {
		TdscBlockPlanTable tdscBlockPlanTable = (TdscBlockPlanTable) tdscBlockPlanTableDao.get(planId);
		return tdscBlockPlanTable;
	}

	/**
	 * ����blockId��ѯ�ӵؿ���Ϣ
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
	//��ѯ���еؿ���Ϣ
	public List queryTdscBlockInfoList(TdscBlockPlanTableCondition condition) {

		return tdscBlockInfoDao.queryTdscBlockInfoList(condition);
	}
	//�������Ƶõ��ؿ���Ϣ
	public List findPlanTableByBlockName(String blockName) {
		return tdscBlockPlanTableDao.findPlanTableInfoByName(blockName);
	}
}
