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
	 * �ϴ����ù��� ����noticeId,noticeNo,fileUrl�����ù�����Ϣ�� ����һҳѡ�еĵؿ齻����Ϣ���еļ�¼ͨ��appId���¶�Ӧ��¼��noticeId,noticeNo
	 * 
	 * @param upLoadFile
	 * @param tdscNoticeApp
	 * @param appIds
	 */
	public void save(TdscNoticeApp tdscNoticeApp, String appIds[], String oldNoticeId, String oldNoticeNo) {
		// String filePath = null;
		// // �����ļ��ϴ�����������upLoadFile
		// filePath = tdscNoticeAppDao.upLoadFile(upLoadFile, tdscNoticeApp.getNoticeNo());
		//
		// if (filePath == null) {
		// // �ϴ��ļ�����
		// System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// // return mapping.findForward("errorPage");
		// }

		// ���֮ǰ���ݴ�����棬��ɾ��֮ǰ�ļ�¼��������¶�Ӧ�ļ�¼

		TdscNoticeApp temp = null;
		if (!"".equals(oldNoticeId) && oldNoticeId != null)
			temp = (TdscNoticeApp) tdscNoticeAppDao.get(oldNoticeId);
		if (temp != null)
			tdscNoticeAppDao.delete(temp);

		// tdscNoticeApp.setFileUrl(filePath);
		// ����noticeId,noticeNo,fileUrl�����ù�����Ϣ��
		tdscNoticeAppDao.save(tdscNoticeApp);

		TdscBlockTranApp tdscBlockTranApp = null;
		if (oldNoticeId != null) {
			// ���ؿ齻����Ϣ��������noticeNo��ǰ̨noticeNo��ͬ�ļ�¼noticeNo=null
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

		// ͨ��appIds���飬���µؿ齻����Ϣ���ж�Ӧ�ļ�¼��noticeId,noticeNo
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
	 * ����noticeId��noitceNo��ѯTdscBlockTranApp�б�
	 * 
	 * @param noticeId
	 * @param noitceNo
	 * @return
	 */
	public List findTdscBlockTranAppByNoticeNo(String noticeId, String noitceNo) {
		return tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(noticeId, noitceNo);
	}

	/**
	 * �ϴ����ù��� ����noticeId,noticeNo,fileUrl�����ù�����Ϣ��,�������Ĺ������ƺ��ݴ�ǰ�Ĺ������Ʋ�ͬ����ɾ��ԭ���棬�������¹��� ����һҳѡ�еĵؿ齻����Ϣ���еļ�¼ͨ��appId���¶�Ӧ��¼��noticeId,noticeNo
	 * ͬʱ��ɾ���ĵؿ���Ϣ��Ӧ�ļ�¼ͨ��appId���¶�Ӧ��¼��noticeId=null,noticeNo=null
	 * 
	 * @param upLoadFile
	 * @param tdscNoticeApp
	 * @param appIds
	 */
	public void update(TdscNoticeApp tdscNoticeApp, String appIds[], String oldNoticeId, String oldNoticeNo) {
		// String filePath = null;
		// // �����ļ��ϴ�����������upLoadFile
		// filePath = tdscNoticeAppDao.upLoadFile(upLoadFile, tdscNoticeApp.getNoticeNo());
		//
		// if (filePath == null) {
		// // �ϴ��ļ�����
		// System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// // return mapping.findForward("errorPage");
		// }
		//
		// // ���֮ǰ���ݴ�����棬��ɾ��֮ǰ�ļ�¼��������¶�Ӧ�ļ�¼

		TdscNoticeApp temp = null;
		if (!"".equals(oldNoticeId))
			temp = (TdscNoticeApp) tdscNoticeAppDao.get(oldNoticeId);
		if (temp != null)
			tdscNoticeAppDao.delete(temp);

		// tdscNoticeApp.setFileUrl(filePath);
		// ����noticeId,noticeNo,fileUrl�����ù�����Ϣ��
		tdscNoticeAppDao.save(tdscNoticeApp);

		TdscBlockTranApp tdscBlockTranApp = null;
		if (oldNoticeId != null) {
			// ���ؿ齻����Ϣ��������noticeNo��ǰ̨noticeNo��ͬ�ļ�¼noticeNo=null
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
		 * ��appIds����districtId����ͬʱ����blockNoticeNo
		 */
		// ͨ��appId[]ȡ����Ӧ��districtId[]
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

		// ��districtId[]��������(���Ϊ�༭ҳ��)
		if (districtId.length != 0) {
			for (int i = 0; i < districtId.length; i++) {
				for (int j = i; j < districtId.length; j++) {
					// ����С��Ԫ�طŵ���λ
					if (Integer.parseInt(districtId[i]) > Integer.parseInt(districtId[j])) {
						String districtIdtempValue = districtId[i];
						districtId[i] = districtId[j];
						districtId[j] = districtIdtempValue;

						// ��appIds[]��Ԫ��λ�ú�districtId[]��ͬ
						String appIdtempValue = appIds[i];
						appIds[i] = appIds[j];
						appIds[j] = appIdtempValue;
					}
				}
			}
		}

		/**
		 * END ��appIds����districtId����ͬʱ����blockNoticeNo
		 */

		// ͨ��appIds���飬���µؿ齻����Ϣ���ж�Ӧ�ļ�¼��noticeId,noticeNo
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
	 * �°���ù���
	 */

	public String createNoticeNo() {

		// TODO
		// ��ù���ţ��������+3λ����ˮ��
		Date nowdate = new Date();
		String year = String.valueOf(nowdate.getYear() + 1900);
		String tmpId = "000" + idSpringManager.getIncrementId("NOTICE_NO");
		tmpId = year + tmpId.substring(tmpId.length() - 3);

		return tmpId;
	}

	/**
	 * ͨ��appIds[]��ѯ��TdscBlockAppView�б�
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

		// ͨ��noticeIdɾ��TdscNoticeApp,TdscBlockTranApp�е�ԭ�м�¼

		if (!"".equals(noticeId) && noticeId != null) {
			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
			// ɾ��TdscNoticeApp�еļ�¼
			tdscNoticeAppDao.delete(tdscNoticeApp);
			// ɾ��ԭ���ϴ����ļ�
			tdscNoticeAppDao.delFile(noticeId + ".doc");

			// ͨ��noticeId��ѯTdscBlockTranApp�еļ�¼
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

		// ��noticeNo��noticeName���浽������
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

		// ͨ��appIds���飬���µؿ齻����Ϣ���ж�Ӧ�ļ�¼��noticeId,noticeNo,blockNoticeNo
		for (int i = 0; i < appIds.length; i++) {
			TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppDao.get(appIds[i]);
			if (tdscBlockTranApp != null) {
				tdscBlockTranApp.setNoticeId(tdscNoticeApp.getNoticeId());
				tdscBlockTranApp.setNoitceNo(tdscNoticeApp.getNoticeNo());

				// ȡ�õؿ鹫���
				String blockNoticeNo = noticeNo.substring(7, 11) + noticeNo.substring(12, 14);
				String xuhao = "00" + String.valueOf(i + 1);
				xuhao = xuhao.substring(xuhao.length() - 2);
				blockNoticeNo += xuhao;

				tdscBlockTranApp.setBlockNoticeNo(blockNoticeNo);
				tdscBlockTranAppDao.update(tdscBlockTranApp);
			}

		}

		// ����ǳ����ļ�����������³��ù���noticeid lz+
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

		// ��Ҫͬʱ�ϴ��û��༩���word�ļ�
		// tdscNoticeAppDao.upLoadFile(upLoadFile, tdscNoticeApp.getNoticeId());
	}

	/**
	 * �����ļ� �ݴ棬���� �͵�һ����˵��ύ lz+ 2009.6.25
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

		// ������ù���
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
				// ˵���Ѿ��������update��
				tdscBlockFileAppDao.update(tdscBlockFileOld);
			} else {
				TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
				tdscBlockFileApp.setRecordId(recordId);
				tdscBlockFileApp.setFileId(noticeId);
				tdscBlockFileApp.setFileUrl(modeNameEn);// ���ļ����� �����ڸ�����ַ��
				tdscBlockFileApp.setFilePerson(userId);
				tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
				// ��������ļ���Ϣ TDSC_BLOCK_FILE_APP
				tdscBlockFileAppDao.save(tdscBlockFileApp);
			}
		}

		// ͨ��noticeIdɾ��TdscNoticeApp,TdscBlockTranApp�е�ԭ�м�¼
		if (!"".equals(noticeId) && noticeId != null) {
			TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
			if (tdscNoticeApp != null) {
				// ɾ��TdscNoticeApp�еļ�¼
				tdscNoticeAppDao.delete(tdscNoticeApp);
				// ɾ��ԭ���ϴ����ļ�
				tdscNoticeAppDao.delFile(noticeId + ".doc");
				// ͨ��noticeId��ѯTdscBlockTranApp�еļ�¼
			}
		}

		// ��noticeNo��noticeName���浽������
		TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
		// tdscNoticeApp.setNoticeId(noticeId);
		tdscNoticeApp.setNoticeNo(noticeNo);
		if(StringUtils.isEmpty(noticeName)){
			noticeName = "�����й�����Դ�ֹ��н����õ�ʹ��Ȩ���Ƴ��ù���";
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
		// ���þ�����ID
		if (StringUtils.isNotEmpty(userId)) {
			tdscNoticeApp.setUserId(StringUtils.trimToEmpty(userId));
		}
		// 11���������棬12�������ύ��21����˱��棬22������ύ��23����˻���
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
		if(noticeNo.indexOf("��") != -1){
			if ("��".equals(tempStr)) {
				blockNoticeNoPrefix = "�����������⣩" + noticeNo.substring(6, 10) + "-";
			} else if ("��".equals(tempStr)) {
				blockNoticeNoPrefix = "�����������⣩" + noticeNo.substring(6, 10) + "-";
			}
			max = this.tdscBlockTranAppDao.getMaxNoticeNoBlockNoticeNoPrefix("20",blockNoticeNoPrefix);
		}else{
			if ("��".equals(tempStr)) {
				blockNoticeNoPrefix = "������������" + noticeNo.substring(5, 9) + "-";
			} else if ("��".equals(tempStr)) {
				blockNoticeNoPrefix = "������������" + noticeNo.substring(5, 9) + "-";
			}
			max = this.tdscBlockTranAppDao.getMaxNoticeNoBlockNoticeNoPrefix("18",blockNoticeNoPrefix);
		}
		
		int maxValue = Integer.parseInt(max);

		// ͨ��planId���飬���µؿ齻����Ϣ���ж�Ӧ�ļ�¼��noticeId,noticeNo,blockNoticeNo
		TdscBlockTranAppCondition condition = new TdscBlockTranAppCondition();
		condition.setPlanId(planId);
		condition.setOrderKey("xuHao");
		List tdscBlockTranAppList = tdscBlockTranAppDao.findTranAppList(condition);
		if (tdscBlockTranAppList != null && tdscBlockTranAppList.size() > 0) {
			for (int i = 0; i < tdscBlockTranAppList.size(); i++) {
				TdscBlockTranApp tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppList.get(i);
				tdscBlockTranApp.setNoticeId(tdscNoticeApp.getNoticeId());
				tdscBlockTranApp.setNoitceNo(tdscNoticeApp.getNoticeNo());
				// ��submitTypeΪ12ʱ���������ύ���ù��棬��ʱ��Ҫ���ؿ��Ÿ��±��浽tdscBlockTranApp
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
		// ���еؿ������ƶ�

		List tdscBlockAppViewList = new ArrayList();// ���еؿ���Ϣ��
		String statusId = "";
		String appId = "";

		if (noticeNo != null && !"".equals(noticeNo) && planId != null && !"".equals(planId)) {
			TdscBaseQueryCondition conditionBlock = new TdscBaseQueryCondition();
			conditionBlock.setPlanId(planId);
			conditionBlock.setNodeId(FlowConstants.FLOW_NODE_FILE_MAKE);
			tdscBlockAppViewList = commonQueryService.queryTdscBlockAppViewList(conditionBlock);
		}
		// �ؿ�ѭ����ʼ----------------------------
		if (tdscBlockAppViewList != null && tdscBlockAppViewList.size() > 0) {
			for (int j = 0; j < tdscBlockAppViewList.size(); j++) {
				TdscBlockAppView blockapp = (TdscBlockAppView) tdscBlockAppViewList.get(j);
				// �ؿ���Ϣ�ڵ�status��Ϊ�����ļ�status
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
						} else if ("23".equals(submitType)) {// ����
							appFlow.setResultId("030202");
							// appFlow.setTextOpen(textOpen);
							this.appFlowService.saveOpnn(appFlow);
						} else if ("12".equals(submitType) || "22".equals(submitType)) {// ����ύ
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
						} else if ("23".equals(submitType)) {// ����
							appFlow.setResultId("030302");
							// appFlow.setTextOpen(textOpen);
							this.appFlowService.saveOpnn(appFlow);
						} else if ("12".equals(submitType) || "22".equals(submitType)) {// ����ύ
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
						} else if ("23".equals(submitType)) {// ����
							appFlow.setResultId("030202");
							// appFlow.setTextOpen(textOpen);
							this.appFlowService.saveOpnn(appFlow);
						} else if ("12".equals(submitType) || "22".equals(submitType)) {// ����ύ
							this.appFlowService.saveOpnn(appFlow);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}// �ؿ�ѭ�����----------------------------
		}
		// //����ǳ����ļ�����������³��ù���noticeid
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
		// ͨ��ͨ�ýӿڲ�����������ļ�¼
		List commonQueryList = commonQueryService.queryTdscBlockAppViewList(condition);
		/**
		 * ͨ������������Ѿ��ݴ�ļ�¼
		 */
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		// ��ѯ��appIds
		List zancunList = new ArrayList();
		TdscBaseQueryCondition tempCondition = new TdscBaseQueryCondition();
		tempCondition.setNoitceNo(tdscNoticeApp.getNoticeNo());
		// condition.setNoticeId(tdscNoticeApp.getNoticeId());
		tempCondition.setNodeId(condition.getNodeId());
		zancunList = commonQueryService.queryTdscBlockAppViewList(tempCondition);

		/**
		 * ��װlist ��ͨ�ýӿڲ���ļ�¼���Ѿ��ݴ�ķ�װ��һ��list
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
	 * ��ͨ�ýӿڲ����������Ϣ�б��������
	 */
	public List sortTdscBlockAppViewList(TdscBaseQueryCondition condition) {

		List tempList = new ArrayList();
		tempList = commonQueryService.queryTdscBlockAppViewList(condition);

		tempList = sortList(tempList);

		return tempList;
	}

	/**
	 * ���õ���List����������
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
	 * ͨ��noticeId����TdscNoticeApp�����Ƿ�Ϊ��
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
	 * ȡ����ȷ�Ĺ���� 20080514*
	 */
	public String selectNoticeNo() {
		List listNoticeNo = tdscNoticeAppDao.findNoticeNo();
		String noticeNo = "";
		String tempNotcieNo;
		// ȡ���Ϻ�
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
		// ��ù���ţ��������+2λ����ˮ��
		Date nowdate = new Date();
		String year = String.valueOf(nowdate.getYear() + 1900);
		noticeNo = "000" + tempNotcieNo;
		noticeNo = year + noticeNo.substring(noticeNo.length() - 2);
		return noticeNo;
	}

	/**
	 * ����TDSC_NOTICE_APP����NOTICE_NO���б� 20080514*
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
	 * ȡ�����Ĺ����+1��ֵ 20080515*
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
	 * ȡ���Ϻ�����С�Ĺ���ŵ�ֵ 20080515*
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
	 * �����������ƣ�����
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
				// ����ÿ�����ص����
				this.appFlowService.saveOpnn(temp.getAppId(), temp.getTransferMode(), user);
			}
		}
	}

	// �����ļ���˷���
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
