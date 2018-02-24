package com.wonders.tdsc.blockwork.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.id.service.IdSpringManager;
import com.wonders.tdsc.blockwork.dao.TdscBlockFileAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.dao.TdscNoticeAppDao;
import com.wonders.tdsc.bo.TdscAppFlow;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscBlockInfo;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscBlockTranAppCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.flowadapter.FlowConstants;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;

public class TdscNoticeService extends BaseSpringManagerImpl {

	private TdscNoticeAppDao tdscNoticeAppDao;

	private TdscBlockTranAppDao tdscBlockTranAppDao;

	private TdscBlockInfoDao tdscBlockInfoDao;

	private IdSpringManager idSpringManager;

	private CommonQueryService commonQueryService;

	private TdscFileService tdscFileService;

	private AppFlowService appFlowService;

	private TdscBlockFileAppDao tdscBlockFileAppDao;

	public void setTdscBlockFileAppDao(TdscBlockFileAppDao tdscBlockFileAppDao) {
		this.tdscBlockFileAppDao = tdscBlockFileAppDao;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setIdSpringManager(IdSpringManager idSpringManager) {
		this.idSpringManager = idSpringManager;
	}

	public void setTdscBlockInfoDao(TdscBlockInfoDao tdscBlockInfoDao) {
		this.tdscBlockInfoDao = tdscBlockInfoDao;
	}

	public void setTdscNoticeAppDao(TdscNoticeAppDao tdscNoticeAppDao) {
		this.tdscNoticeAppDao = tdscNoticeAppDao;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public PageList findPageList(TdscNoticeAppCondition condition) {

		return tdscNoticeAppDao.findPageList(condition);
	}

	/**
	 * 上传出让公告 保存noticeId,noticeNo,fileUrl到出让公告信息表 将上一页选中的地块交易信息表中的记录通过appId更新对应记录的noticeId,noticeNo
	 * 
	 * @param upLoadFile
	 * @param tdscNoticeApp
	 * @param appIds
	 */
	public void save(TdscNoticeApp tdscNoticeApp, String appIds[], String oldNoticeId, String oldNoticeNo) {
		// String filePath = null;
		// // 调用文件上传函数，传入upLoadFile
		// filePath = tdscNoticeAppDao.upLoadFile(upLoadFile, tdscNoticeApp.getNoticeNo());
		//
		// if (filePath == null) {
		// // 上传文件出错
		// System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// // return mapping.findForward("errorPage");
		// }

		// 如果之前已暂存过公告，则删除之前的记录，否则更新对应的记录

		TdscNoticeApp temp = null;
		if (!"".equals(oldNoticeId) && oldNoticeId != null)
			temp = (TdscNoticeApp) tdscNoticeAppDao.get(oldNoticeId);
		if (temp != null)
			tdscNoticeAppDao.delete(temp);

		// tdscNoticeApp.setFileUrl(filePath);
		// 更新noticeId,noticeNo,fileUrl到出让公告信息表
		tdscNoticeAppDao.save(tdscNoticeApp);

		TdscBlockTranApp tdscBlockTranApp = null;
		if (oldNoticeId != null) {
			// 将地块交易信息表中所有noticeNo和前台noticeNo相同的记录noticeNo=null
			List tdscBlockTranList = new ArrayList();
			tdscBlockTranList = tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(oldNoticeId, oldNoticeNo);
			if (tdscBlockTranList != null && tdscBlockTranList.size() > 0) {
				for (int i = 0; i < tdscBlockTranList.size(); i++) {
					tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranList.get(i);
					if (tdscBlockTranApp != null) {
						tdscBlockTranApp.setNoitceNo("null");
						tdscBlockTranAppDao.update(tdscBlockTranApp);
					}
				}
			}
		}

		// 通过appIds数组，更新地块交易信息表中对应的记录的noticeId,noticeNo
		for (int i = 0; i < appIds.length; i++) {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appIds[i]);
			if (tdscBlockTranApp != null) {
				tdscBlockTranApp.setNoticeId(tdscNoticeApp.getNoticeId());
				tdscBlockTranApp.setNoitceNo(tdscNoticeApp.getNoticeNo());
				tdscBlockTranAppDao.update(tdscBlockTranApp);
			}

		}

	}

	/**
	 * 根据noticeId，noitceNo查询TdscBlockTranApp列表
	 * 
	 * @param noticeId
	 * @param noitceNo
	 * @return
	 */
	public List findTdscBlockTranAppByNoticeNo(String noticeId, String noitceNo) {
		return tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(noticeId, noitceNo);
	}

	/**
	 * 上传出让公告 保存noticeId,noticeNo,fileUrl到出让公告信息表,如果保存的公告名称和暂存前的公告名称不同，则删除原公告，并插入新公告 将上一页选中的地块交易信息表中的记录通过appId更新对应记录的noticeId,noticeNo
	 * 同时将删除的地块信息对应的记录通过appId更新对应记录的noticeId=null,noticeNo=null
	 * 
	 * @param upLoadFile
	 * @param tdscNoticeApp
	 * @param appIds
	 */
	public void update(TdscNoticeApp tdscNoticeApp, String appIds[], String oldNoticeId, String oldNoticeNo) {
		// String filePath = null;
		// // 调用文件上传函数，传入upLoadFile
		// filePath = tdscNoticeAppDao.upLoadFile(upLoadFile, tdscNoticeApp.getNoticeNo());
		//
		// if (filePath == null) {
		// // 上传文件出错
		// System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// // return mapping.findForward("errorPage");
		// }
		//
		// // 如果之前已暂存过公告，则删除之前的记录，否则更新对应的记录

		TdscNoticeApp temp = null;
		if (!"".equals(oldNoticeId))
			temp = (TdscNoticeApp) tdscNoticeAppDao.get(oldNoticeId);
		if (temp != null)
			tdscNoticeAppDao.delete(temp);

		// tdscNoticeApp.setFileUrl(filePath);
		// 更新noticeId,noticeNo,fileUrl到出让公告信息表
		tdscNoticeAppDao.save(tdscNoticeApp);

		TdscBlockTranApp tdscBlockTranApp = null;
		if (oldNoticeId != null) {
			// 将地块交易信息表中所有noticeNo和前台noticeNo相同的记录noticeNo=null
			List tdscBlockTranList = new ArrayList();
			tdscBlockTranList = tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(oldNoticeId, oldNoticeNo);
			if (tdscBlockTranList != null && tdscBlockTranList.size() > 0) {
				for (int i = 0; i < tdscBlockTranList.size(); i++) {
					tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranList.get(i);
					if (tdscBlockTranApp != null) {
						tdscBlockTranApp.setNoitceNo("");
						tdscBlockTranAppDao.update(tdscBlockTranApp);
					}
				}
			}
		}

		/**
		 * 对appIds根据districtId排序，同时更新blockNoticeNo
		 */
		// 通过appId[]取出对应的districtId[]
		String districtId[] = new String[appIds.length];
		for (int i = 0; i < appIds.length; i++) {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appIds[i]);
			if (tdscBlockTranApp != null) {
				String blockId = tdscBlockTranApp.getBlockId();
				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(blockId);
				if (tdscBlockInfo != null) {
					districtId[i] = tdscBlockInfo.getDistrictId().toString();
				}
			}
		}

		// 对districtId[]进行排序(如果为编辑页面)
		if (districtId.length != 0) {
			for (int i = 0; i < districtId.length; i++) {
				for (int j = i; j < districtId.length; j++) {
					// 将最小的元素放到首位
					if (Integer.parseInt(districtId[i]) > Integer.parseInt(districtId[j])) {
						String districtIdtempValue = districtId[i];
						districtId[i] = districtId[j];
						districtId[j] = districtIdtempValue;

						// 将appIds[]中元素位置和districtId[]相同
						String appIdtempValue = appIds[i];
						appIds[i] = appIds[j];
						appIds[j] = appIdtempValue;
					}
				}
			}
		}

		/**
		 * END 对appIds根据districtId排序，同时更新blockNoticeNo
		 */

		// 通过appIds数组，更新地块交易信息表中对应的记录的noticeId,noticeNo
		for (int i = 0; i < appIds.length; i++) {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appIds[i]);
			if (tdscBlockTranApp != null) {
				tdscBlockTranApp.setNoticeId(tdscNoticeApp.getNoticeId());
				tdscBlockTranApp.setNoitceNo(tdscNoticeApp.getNoticeNo());
				String blockNoticeNo = tdscNoticeApp.getNoticeNo().toString() + "0" + String.valueOf(i + 1);
				tdscBlockTranApp.setBlockNoticeNo(blockNoticeNo);
				tdscBlockTranAppDao.update(tdscBlockTranApp);

				TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(tdscBlockTranApp.getBlockId());
				tdscBlockInfo.setBlockNoticeNo(blockNoticeNo);
				tdscBlockInfoDao.update(tdscBlockInfo);

			}

		}

	}

	public TdscNoticeApp findNoticeAppByNoticeId(String noticeId) {

		return (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
	}

	public List getAppIdsByNoticeId(String noticeId) {
		List appIds = new ArrayList();
		List tdscBlockTranList = new ArrayList();
		TdscBlockTranApp tdscBlockTranApp = null;
		tdscBlockTranList = tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(noticeId, "");
		for (int i = 0; i < tdscBlockTranList.size(); i++) {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranList.get(i);
			appIds.add(tdscBlockTranApp.getAppId());
		}
		return appIds;
	}

	public List findTdscBlockAppViewList(TdscBaseQueryCondition condition, String noticeNo) {
		return tdscNoticeAppDao.findTdscBlockAppViewList(condition, noticeNo);
	}

	public List findBlockAppViewList(TdscBaseQueryCondition condition, String noticeNo, String issueStartDate) {
		return tdscNoticeAppDao.findBlockAppViewList(condition, noticeNo, issueStartDate);
	}

	public void delCrgg(String noticeId) {
		tdscNoticeAppDao.delFile(noticeId + ".doc");
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		// String noticeNo = tdscNoticeApp.getNoticeNo();
		String fileName = tdscNoticeApp.getFileUrl();
		tdscNoticeAppDao.delete(tdscNoticeApp);
		List tdscBlockTranList = tdscBlockTranAppDao.findTranAppByNoticeId(noticeId);
		String planId = "";
		if (tdscBlockTranList != null && tdscBlockTranList.size() > 0) {
			for (int i = 0; i < tdscBlockTranList.size(); i++) {
				TdscBlockTranApp temp = (TdscBlockTranApp) tdscBlockTranList.get(i);
				temp.setNoticeId("");
				temp.setNoitceNo("");
				temp.setBlockNoticeNo("");
				String blockId = temp.getBlockId();
				planId = temp.getPlanId();
				tdscBlockTranAppDao.update(temp);
				if (!"".equals(blockId)) {
					TdscBlockInfo tdscBlockInfo = (TdscBlockInfo) tdscBlockInfoDao.get(blockId);
					if (tdscBlockInfo != null) {
						tdscBlockInfo.setBlockNoticeNo("");
						tdscBlockInfoDao.update(tdscBlockInfo);
					}
				}
			}
		}
		tdscNoticeAppDao.delFile(fileName);
		if (StringUtils.isNotEmpty(planId))
			tdscBlockFileAppDao.deleteById(planId);
	}

	/**
	 * 新版出让公告
	 */

	public String createNoticeNo() {

		// TODO
		// 获得公告号，规则：年份+3位的流水号
		Date nowdate = new Date();
		String year = String.valueOf(nowdate.getYear() + 1900);
		String tmpId = "000" + idSpringManager.getIncrementId("NOTICE_NO");
		tmpId = year + tmpId.substring(tmpId.length() - 3);

		return tmpId;
	}

	/**
	 * 通过appIds[]查询出TdscBlockAppView列表
	 * 
	 * @param appIds
	 * @return
	 */
	public List queryTdscBlockAppViewByappIds(String appIds[]) {
		List tdscblockAppViewList = new ArrayList();
		for (int i = 0; i < appIds.length; i++) {
			TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
			condition.setAppId(appIds[i]);
			TdscBlockAppView tdscBlockAppView = commonQueryService.getTdscBlockAppView(condition);
			tdscblockAppViewList.add(tdscBlockAppView);
		}

		return tdscblockAppViewList;
	}

	public void saveNotice(SysUser user, String appIds[], String noticeNo, String noticeName, String noticeStatus, String noticeId, String modeNameEn, String recordId) {

		// 通过noticeId删除TdscNoticeApp,TdscBlockTranApp中的原有记录

		if (!"".equals(noticeId) && noticeId != null) {
			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
			// 删除TdscNoticeApp中的记录
			tdscNoticeAppDao.delete(tdscNoticeApp);
			// 删除原来上传的文件
			tdscNoticeAppDao.delFile(noticeId + ".doc");

			// 通过noticeId查询TdscBlockTranApp中的记录
			List tdscBlockTranAppList = tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(noticeId, "");
			if (tdscBlockTranAppList != null && tdscBlockTranAppList.size() > 0) {
				for (int i = 0; i < tdscBlockTranAppList.size(); i++) {
					TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppList.get(i);
					tdscBlockTranApp.setNoticeId("");
					tdscBlockTranApp.setNoitceNo("");
					tdscBlockTranApp.setBlockNoticeNo("");
					tdscBlockTranAppDao.update(tdscBlockTranApp);
				}
			}
		}

		// 将noticeNo，noticeName保存到数据中
		TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
		tdscNoticeApp.setNoticeNo(noticeNo);
		tdscNoticeApp.setNoticeName(noticeName);
		tdscNoticeApp.setNoticeType("01");
		tdscNoticeApp.setNoticeStatus(noticeStatus);
		tdscNoticeApp.setIfReleased("0");
		tdscNoticeApp.setFileUrl(modeNameEn);
		tdscNoticeApp.setRecordId(recordId);
		tdscNoticeApp.setUserId(user.getUserId());
		tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.save(tdscNoticeApp);

		// 通过appIds数组，更新地块交易信息表中对应的记录的noticeId,noticeNo,blockNoticeNo
		for (int i = 0; i < appIds.length; i++) {
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appIds[i]);
			if (tdscBlockTranApp != null) {
				tdscBlockTranApp.setNoticeId(tdscNoticeApp.getNoticeId());
				tdscBlockTranApp.setNoitceNo(tdscNoticeApp.getNoticeNo());

				// 取得地块公告号
				String blockNoticeNo = noticeNo.substring(7, 11) + noticeNo.substring(12, 14);
				String xuhao = "00" + String.valueOf(i + 1);
				xuhao = xuhao.substring(xuhao.length() - 2);
				blockNoticeNo += xuhao;

				tdscBlockTranApp.setBlockNoticeNo(blockNoticeNo);
				tdscBlockTranAppDao.update(tdscBlockTranApp);
			}

		}

		// 如果是出让文件审批，则更新出让公告noticeid lz+
		if (noticeId != null && !"".equals(noticeId)) {
			TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
			TdscBlockFileApp tdscBlockFile = new TdscBlockFileApp();
			tdscBlockFileOld = tdscFileService.getBlockFileAppById(noticeId);
			if (tdscBlockFileOld != null && tdscBlockFileOld.getFileId() != null) {
				tdscBlockFileAppDao.delete(tdscBlockFileOld);
				// xin
				tdscBlockFile.setFileDate(new Date(System.currentTimeMillis()));
				tdscBlockFile.setFileId(tdscNoticeApp.getNoticeId());
				tdscBlockFile.setFilePerson(tdscBlockFileOld.getFilePerson());
				tdscBlockFile.setRecordId(tdscBlockFileOld.getRecordId());
				tdscBlockFileAppDao.save(tdscBlockFile);
			}
		}

		// 需要同时上传用户编缉后的word文件
		// tdscNoticeAppDao.upLoadFile(upLoadFile, tdscNoticeApp.getNoticeId());
	}

	/**
	 * 出让文件 暂存，回退 和第一步审核的提交 lz+ 2009.6.25
	 * 
	 * @param appIds
	 * @param noticeNo
	 * @param noticeName
	 * @param noticeId
	 * @param modeNameEn
	 * @param recordId
	 * @param user
	 * @param submitType
	 * @param textOpen
	 * @param tdscBlockPlanTable
	 * @param landLocation
	 */
	public void saveNoticeLz(String userId, String appIds[], String noticeNo, String noticeName, String noticeId, String planId, String modeNameEn, String recordId, SysUser user,
			String submitType, String textOpen, TdscBlockPlanTable tdscBlockPlanTable, String landLocation) {

		// 保存出让公告
		TdscBlockFileApp tdscBlockFileOld = null;
		if (!"".equals(noticeId)) {
			// tdscBlockFileOld = tdscFileService.getBlockFileAppById(noticeId);
			TdscNoticeApp tdscNoticeApp = this.getTdscNoticeAppByNoticeId(noticeId);
			if (tdscNoticeApp != null) {
				tdscBlockFileOld = tdscFileService.getBlockFileAppByRecordId(tdscNoticeApp.getRecordId());
			}

			if (tdscBlockFileOld != null) {
				tdscBlockFileOld.setFilePerson(userId);
				tdscBlockFileOld.setFileDate(new Date(System.currentTimeMillis()));
				// 说明已经保存过，update下
				tdscBlockFileAppDao.update(tdscBlockFileOld);
			} else {
				TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
				tdscBlockFileApp.setRecordId(recordId);
				tdscBlockFileApp.setFileId(noticeId);
				tdscBlockFileApp.setFileUrl(modeNameEn);// 将文件类型 保存在附件地址中
				tdscBlockFileApp.setFilePerson(userId);
				tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
				// 保存出让文件信息 TDSC_BLOCK_FILE_APP
				tdscBlockFileAppDao.save(tdscBlockFileApp);
			}
		}

		// 通过noticeId删除TdscNoticeApp,TdscBlockTranApp中的原有记录
		if (!"".equals(noticeId) && noticeId != null) {
			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
			if (tdscNoticeApp != null) {
				// 删除TdscNoticeApp中的记录
				tdscNoticeAppDao.delete(tdscNoticeApp);
				// 删除原来上传的文件
				tdscNoticeAppDao.delFile(noticeId + ".doc");
				// 通过noticeId查询TdscBlockTranApp中的记录
			}
		}

		// 将noticeNo，noticeName保存到数据中
		TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
		// tdscNoticeApp.setNoticeId(noticeId);
		tdscNoticeApp.setNoticeNo(noticeNo);
		if(StringUtils.isEmpty(noticeName)){
			noticeName = "无锡市国土资源局国有建设用地使用权挂牌出让公告";
		}
		tdscNoticeApp.setNoticeName(noticeName);
		tdscNoticeApp.setNoticeType("01");
		tdscNoticeApp.setIfReleased("0");
		tdscNoticeApp.setFileUrl(modeNameEn);
		tdscNoticeApp.setRecordId(recordId);
		tdscNoticeApp.setTradeNum(tdscBlockPlanTable.getTradeNum());
		tdscNoticeApp.setUniteBlockName(tdscBlockPlanTable.getBlockName());
		tdscNoticeApp.setTransferMode(tdscBlockPlanTable.getTransferMode());
		tdscNoticeApp.setLandLocation(landLocation);
		// 设置经办人ID
		if (StringUtils.isNotEmpty(userId)) {
			tdscNoticeApp.setUserId(StringUtils.trimToEmpty(userId));
		}
		// 11是制作保存，12是制作提交，21是审核保存，22是审核提交，23是审核回退
		tdscNoticeApp.setNoticeStatus("00");
		if ("11".equals(submitType))
			tdscNoticeApp.setNoticeStatus("00");
		if ("12".equals(submitType))
			tdscNoticeApp.setNoticeStatus("01");
		if ("21".equals(submitType))
			tdscNoticeApp.setNoticeStatus("01");
		if ("22".equals(submitType))
			tdscNoticeApp.setNoticeStatus("02");
		if ("23".equals(submitType))
			tdscNoticeApp.setNoticeStatus("00");

		tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.save(tdscNoticeApp);

		String tempStr = noticeNo.substring(1, 2);
		String blockNoticeNoPrefix = "";
		
		String max ="";
		if(noticeNo.indexOf("租") != -1){
			if ("经".equals(tempStr)) {
				blockNoticeNoPrefix = "锡国土经（租）" + noticeNo.substring(6, 10) + "-";
			} else if ("工".equals(tempStr)) {
				blockNoticeNoPrefix = "锡国土工（租）" + noticeNo.substring(6, 10) + "-";
			}
			max = this.tdscBlockTranAppDao.getMaxNoticeNoBlockNoticeNoPrefix("20",blockNoticeNoPrefix);
		}else{
			if ("经".equals(tempStr)) {
				blockNoticeNoPrefix = "锡国土（经）" + noticeNo.substring(5, 9) + "-";
			} else if ("工".equals(tempStr)) {
				blockNoticeNoPrefix = "锡国土（工）" + noticeNo.substring(5, 9) + "-";
			}
			max = this.tdscBlockTranAppDao.getMaxNoticeNoBlockNoticeNoPrefix("18",blockNoticeNoPrefix);
		}
		
		int maxValue = Integer.parseInt(max);

		// 通过planId数组，更新地块交易信息表中对应的记录的noticeId,noticeNo,blockNoticeNo
		TdscBlockTranAppCondition condition = new TdscBlockTranAppCondition();
		condition.setPlanId(planId);
		condition.setOrderKey("xuHao");
		List tdscBlockTranAppList = tdscBlockTranAppDao.findTranAppList(condition);
		if (tdscBlockTranAppList != null && tdscBlockTranAppList.size() > 0) {
			for (int i = 0; i < tdscBlockTranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppList.get(i);
				tdscBlockTranApp.setNoticeId(tdscNoticeApp.getNoticeId());
				tdscBlockTranApp.setNoitceNo(tdscNoticeApp.getNoticeNo());
				// 当submitType为12时，是制作提交出让公告，此时需要将地块编号更新保存到tdscBlockTranApp
				if ("12".equals(submitType)) {
					if(StringUtils.isEmpty(tdscBlockTranApp.getBlockNoticeNo())){
						max = String.valueOf((maxValue + 1));
						String blockNoticeNo = blockNoticeNoPrefix+ max;
						tdscBlockTranApp.setBlockNoticeNo(blockNoticeNo);
						maxValue++;
					}

				}
				tdscBlockTranAppDao.update(tdscBlockTranApp);
			}
		}
		// 所有地块流程推动

		List tdscBlockAppViewList = new ArrayList();// 所有地块信息表
		String statusId = "";
		String appId = "";

		if (noticeNo != null && !"".equals(noticeNo) && planId != null && !"".equals(planId)) {
			TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
			conditionBlock.setPlanId(planId);
			conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);
		}
		// 地块循环开始----------------------------
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				// 地块信息节点status作为出让文件status
				statusId = blockapp.getStatusId();
				appId = blockapp.getAppId();
				//
				TdscAppFlow appFlow = new TdscAppFlow();
				appFlow.setAppId(appId);
				appFlow.setTransferMode(tdscBlockPlanTable.getTransferMode());
				appFlow.setUser(user);
				appFlow.setTextOpen(textOpen);
				if (FlowConstants.FLOW_STATUS_FILE_MODIFY.equals(statusId) || FlowConstants.FLOW_STATUS_FILE_MAKE.equals(statusId)) {
					try {
						if ("11".equals(submitType)) {
							//this.appFlowService.tempSaveOpnn(appId, tdscBlockPlanTable.getTransferMode(), user);
							this.appFlowService.tempSaveOpnn(appFlow);
						} else if ("12".equals(submitType)) {
							// this.appFlowService.saveOpnn(appId, tdscBlockPlanTable.getTransferMode(), user);
							this.appFlowService.saveOpnn(appFlow);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//
				if (FlowConstants.FLOW_STATUS_FILE_VERIFY.equals(statusId)) {
					try {
						if ("21".equals(submitType)) {
							this.appFlowService.tempSaveOpnn(appFlow);
						} else if ("23".equals(submitType)) {// 回退
							appFlow.setResultId("030202");
							// appFlow.setTextOpen(textOpen);
							this.appFlowService.saveOpnn(appFlow);
						} else if ("12".equals(submitType) || "22".equals(submitType)) {// 审核提交
							this.appFlowService.saveOpnn(appFlow);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (FlowConstants.FLOW_STATUS_NOTICE_MAKE.equals(statusId)) {
					try {
						if ("21".equals(submitType)) {
							this.appFlowService.tempSaveOpnn(appFlow);
						} else if ("23".equals(submitType)) {// 回退
							appFlow.setResultId("030302");
							// appFlow.setTextOpen(textOpen);
							this.appFlowService.saveOpnn(appFlow);
						} else if ("12".equals(submitType) || "22".equals(submitType)) {// 审核提交
							this.appFlowService.saveOpnn(appFlow);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (FlowConstants.FLOW_STATUS_NOTICE_MODIFY.equals(statusId)) {
					try {
						if ("21".equals(submitType)) {
							this.appFlowService.tempSaveOpnn(appFlow);
						} else if ("23".equals(submitType)) {// 回退
							appFlow.setResultId("030202");
							// appFlow.setTextOpen(textOpen);
							this.appFlowService.saveOpnn(appFlow);
						} else if ("12".equals(submitType) || "22".equals(submitType)) {// 审核提交
							this.appFlowService.saveOpnn(appFlow);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}// 地块循环完毕----------------------------
		}
		// //如果是出让文件审批，则更新出让公告noticeid
		// if(noticeId!=null&&!"".equals(noticeId)){
		// TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
		// TdscBlockFileApp tdscBlockFile = new TdscBlockFileApp();
		// tdscBlockFileOld = tdscFileService.getBlockFileAppById(noticeId);
		// if(tdscBlockFileOld!=null&&tdscBlockFileOld.getFileId()!=null){
		// tdscBlockFileAppDao.delete(tdscBlockFileOld);
		// //xin
		// tdscBlockFile.setFileDate(new Date(System.currentTimeMillis()));
		// tdscBlockFile.setFileId(tdscNoticeApp.getNoticeId());
		// tdscBlockFile.setFilePerson(tdscBlockFileOld.getFilePerson());
		// tdscBlockFile.setRecordId(tdscBlockFileOld.getRecordId());
		// tdscBlockFileAppDao.save(tdscBlockFile);
		// }
		// }

	}

	public List getNewList(TdscBaseQueryCondition condition, String noticeId) {
		// 通过通用接口查出符合条件的记录
		List commonQueryList = commonQueryService.queryTdscBlockAppViewList(condition);
		/**
		 * 通过条件，查出已经暂存的记录
		 */
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		// 查询出appIds
		List zancunList = new ArrayList();
		TdscBaseQueryCondition tempCondition = new TdscBaseQueryCondition();
		tempCondition.setNoitceNo(tdscNoticeApp.getNoticeNo());
		// condition.setNoticeId(tdscNoticeApp.getNoticeId());
		tempCondition.setNodeId(condition.getNodeId());
		zancunList = commonQueryService.queryTdscBlockAppViewList(tempCondition);

		/**
		 * 组装list 将通用接口查出的记录和已经暂存的封装成一个list
		 */
		if (zancunList != null && zancunList.size() > 0) {
			for (int i = 0; i < zancunList.size(); i++) {
				TdscBlockAppView app = (TdscBlockAppView) zancunList.get(i);

				if (StringUtils.isNotEmpty(condition.getBlockType()) && StringUtils.isNotEmpty(condition.getTransferMode())) {
					if (condition.getBlockType().equals(app.getBlockType()) && condition.getTransferMode().equals(app.getTransferMode()))
						commonQueryList.add(app);
				} else if (StringUtils.isNotEmpty(condition.getBlockType())) {
					if (condition.getBlockType().equals(app.getBlockType()))
						commonQueryList.add(app);
				} else if (StringUtils.isNotEmpty(condition.getTransferMode())) {
					if (condition.getTransferMode().equals(app.getTransferMode()))
						commonQueryList.add(app);
				} else {
					commonQueryList.add(app);
				}
			}
		}

		commonQueryList = sortList(commonQueryList);

		return commonQueryList;
	}

	/**
	 * 对通用接口查出的土地信息列表进行排序
	 */
	public List sortTdscBlockAppViewList(TdscBaseQueryCondition condition) {

		List tempList = new ArrayList();
		tempList = commonQueryService.queryTdscBlockAppViewList(condition);

		tempList = sortList(tempList);

		return tempList;
	}

	/**
	 * 将得到的List按区县排序
	 * 
	 * @param sortList
	 * @return
	 */
	public List sortList(List sortList) {

		if (sortList != null && sortList.size() > 0) {
			for (int i = 0; i < sortList.size(); i++) {
				TdscBlockAppView appI = (TdscBlockAppView) sortList.get(i);
				for (int j = i; j < sortList.size(); j++) {
					TdscBlockAppView appJ = (TdscBlockAppView) sortList.get(j);
					if (appI.getDistrictId() == null || "".equals(appI.getDistrictId()))
						continue;
					if (appJ.getDistrictId() == null || "".equals(appJ.getDistrictId()))
						continue;
					if (appI.getDistrictId().intValue() > appJ.getDistrictId().intValue()) {
						sortList.set(i, appJ);
						sortList.set(j, appI);
						appI = (TdscBlockAppView) sortList.get(i);
					}
				}
			}
		}

		return sortList;
	}

	/**
	 * 通过noticeId查找TdscNoticeApp表中是否为空
	 * 
	 * @param noticeId
	 * @return tdscNoticeApp
	 */
	public TdscNoticeApp getTdscNoticeAppByNoticeId(String noticeId) {
		TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
		tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		return tdscNoticeApp;
	}

	public List findTdscNoticeAppListByNoticeNo(String noticeNo) {
		List tdscNoticeAppList = new ArrayList();
		tdscNoticeAppList = (List) tdscNoticeAppDao.findTdscNoticeAppListByNoticeNo(noticeNo);
		return tdscNoticeAppList;
	}

	/**
	 * 取出正确的公告号 20080514*
	 */
	public String selectNoticeNo() {
		List listNoticeNo = tdscNoticeAppDao.findNoticeNo();
		String noticeNo = "";
		String tempNotcieNo;
		// 取出断号
		if (listNoticeNo == null || listNoticeNo.size() < 1) {
			tempNotcieNo = "001";
		} else {
			List listGapNo = tidyGapNo(listNoticeNo);
			if (listGapNo != null && listGapNo.size() > 0) {
				tempNotcieNo = selectMinNo(listGapNo);
			} else {
				tempNotcieNo = selectMaxNo(listNoticeNo);
			}
		}
		// TODO
		// 获得公告号，规则：年份+2位的流水号
		Date nowdate = new Date();
		String year = String.valueOf(nowdate.getYear() + 1900);
		noticeNo = "000" + tempNotcieNo;
		noticeNo = year + noticeNo.substring(noticeNo.length() - 2);
		return noticeNo;
	}

	/**
	 * 整理TDSC_NOTICE_APP表中NOTICE_NO的列表 20080514*
	 */
	public List tidyGapNo(List listNoticeNo) {
		List listGapNo = new ArrayList();
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) listNoticeNo.get(0);
		String firstNo = tdscNoticeApp.getNoticeNo();
		int gapNum = 0;
		String str = firstNo.substring(11, 13);
		int firstNum = Integer.parseInt(str);
		if (listNoticeNo != null && listNoticeNo.size() > 0) {
			for (int i = 0; i < listNoticeNo.size(); i++) {
				TdscNoticeApp noticeApp = (TdscNoticeApp) listNoticeNo.get(i);
				String noticeNo = noticeApp.getNoticeNo();
				int tempNoticeNum = Integer.parseInt(noticeNo.substring(11, 13));
				if (tempNoticeNum - firstNum == 1) {
					firstNum = tempNoticeNum;
					continue;
				} else {
					int j = tempNoticeNum - firstNum;
					for (int k = 1; k < j; k++) {
						gapNum = firstNum + k;
						listGapNo.add(new Integer(gapNum).toString());
					}
					firstNum = tempNoticeNum;
				}
			}
		}
		return listGapNo;
	}

	/**
	 * 取出最大的公告号+1的值 20080515*
	 */
	public String selectMaxNo(List listNoticeNo) {
		String maxNo = "";
		int maxNum = 0;
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) listNoticeNo.get(0);
		String firstNo = tdscNoticeApp.getNoticeNo();
		int firstNum = Integer.parseInt(firstNo.substring(11, 13));
		if (listNoticeNo != null && listNoticeNo.size() > 0) {
			for (int i = 0; i < listNoticeNo.size(); i++) {
				TdscNoticeApp noticeApp = (TdscNoticeApp) listNoticeNo.get(i);
				String noticeNo = noticeApp.getNoticeNo();
				int tempNoticeNum = Integer.parseInt(noticeNo.substring(11, 13));
				if (firstNum <= tempNoticeNum) {
					maxNum = tempNoticeNum;
				} else {
					maxNum = firstNum;
				}
				firstNum = maxNum;
			}
			maxNo = new Integer(maxNum + 1).toString();
		}
		return maxNo;
	}

	/**
	 * 取出断号中最小的公告号的值 20080515*
	 */
	public String selectMinNo(List listGapNo) {
		String minNo = "";
		int minNum = 0;
		String firstNo = listGapNo.get(0).toString();
		int firstNum = Integer.parseInt(firstNo);
		if (listGapNo != null && listGapNo.size() > 0) {
			for (int i = 0; i < listGapNo.size(); i++) {
				String gapNo = listGapNo.get(i).toString();
				int tempNoticeNum = Integer.parseInt(gapNo);
				if (firstNum <= tempNoticeNum) {
					minNum = firstNum;
				} else {
					minNum = tempNoticeNum;
				}
				firstNum = minNum;
			}
			minNo = new Integer(minNum).toString();
		}
		return minNo;
	}

	/**
	 * 整理区县名称，个数
	 * 
	 * @param tdscblockAppViewList
	 * @return
	 */
	public List tidyDistrictList(List tdscblockAppViewList, TdscBlockAppView tdscBlockAppView) {

		List tempList = new ArrayList();
		TdscBlockAppView app = new TdscBlockAppView();
		TdscBlockAppView tempApp = new TdscBlockAppView();

		if (tdscblockAppViewList != null && tdscblockAppViewList.size() > 0) {
			for (int i = 0; i < tdscblockAppViewList.size(); i++) {
				app = (TdscBlockAppView) tdscblockAppViewList.get(i);
				tempList.add(app);
			}
			for (int i = 0; i < tempList.size(); i++) {
				app = (TdscBlockAppView) tempList.get(i);
				for (int j = i + 1; j < tempList.size(); j++) {
					tempApp = (TdscBlockAppView) tempList.get(j);
					if (tempApp.getDistrictId().equals(app.getDistrictId())) {
						tempList.remove(tempApp);
						j--;
					}
				}
			}
		}
		return tempList;
	}

	public void saveOpnnByViewList(List tdList, SysUser user) throws Exception {

		if (tdList != null && tdList.size() > 0) {
			for (int i = 0; i < tdList.size(); i++) {
				TdscBlockAppView temp = (TdscBlockAppView) tdList.get(i);
				// 保存每条土地的意见
				this.appFlowService.saveOpnn(temp.getAppId(), temp.getTransferMode(), user);
			}
		}
	}

	// 出让文件审核方法
	public void saveNoticeOpnnshenhe(String[] appIds, SysUser user, String recordId, String submitType, String noticeNo, String noticeName, String noticeStatus, String noticeId,
			String modeNameEn, String transferMode) throws Exception {

		this.saveNotice(user, appIds, noticeNo, noticeName, noticeStatus, noticeId, modeNameEn, recordId);

		for (int i = 0; i < appIds.length; i++) {
			TdscAppFlow appFlow = new TdscAppFlow();

			appFlow.setAppId(appIds[i]);
			// appFlow.setResultId(recordId);
			appFlow.setUser(user);
			appFlow.setTransferMode(transferMode);
			try {
				if ("1".equals(submitType)) {
					this.appFlowService.tempSaveOpnn(appFlow);
				} else if ("2".equals(submitType)) {
					this.appFlowService.saveOpnn(appFlow);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void callBack(String appId, TdscAppFlow appFlow) {
		try {
			TdscBlockFileApp tdscBlockFileApp = (TdscBlockFileApp) this.tdscBlockFileAppDao.get(appId);
			if (tdscBlockFileApp != null) {
				this.tdscBlockFileAppDao.delete(tdscBlockFileApp);
				this.appFlowService.saveOpnn(appFlow);
			}
		} catch (Exception e) {
		}
	}

	public String getCurrNoticeNoByNoticeNoPrefix(String startindex,String noticeNoPrefix) {

		String currNoticeNo = tdscNoticeAppDao.getCurrNoticeNoByNoticeNoPrefix(startindex,noticeNoPrefix);

		String nextNoticeNo = "000";
		if ("0".equals(currNoticeNo)) {
			nextNoticeNo += "01";
		} else {
			nextNoticeNo += Integer.parseInt(currNoticeNo) + 1 + "";
		}

		nextNoticeNo = nextNoticeNo.substring(nextNoticeNo.length() - 2, nextNoticeNo.length());

		return nextNoticeNo;
	}

	public List queryNoticeIdListPublish() {
		return tdscNoticeAppDao.queryNoticeIdListPublish();
	}
	public List queryNoticeIdListInTrade() {
		return tdscNoticeAppDao.queryNoticeIdListInTrade();
	}
	public TdscBlockFileApp getFileId(String appId) {
		// TODO Auto-generated method stub
		return tdscFileService.getBlockFileAppById(appId);
	}
}
