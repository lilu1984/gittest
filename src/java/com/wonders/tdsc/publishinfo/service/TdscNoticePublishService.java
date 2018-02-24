package com.wonders.tdsc.publishinfo.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.util.DateUtil;
import com.wonders.tdsc.blockwork.dao.TdscBlockPlanTableDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockTranAppDao;
import com.wonders.tdsc.blockwork.service.TdscFileService;
import com.wonders.tdsc.bo.TdscBlockAppView;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.TdscBlockPlanTable;
import com.wonders.tdsc.bo.TdscBlockTranApp;
import com.wonders.tdsc.bo.TdscNoticeApp;
import com.wonders.tdsc.bo.condition.TdscBaseQueryCondition;
import com.wonders.tdsc.bo.condition.TdscNoticeAppCondition;
import com.wonders.tdsc.common.util.DateConvertor;
import com.wonders.tdsc.flowadapter.service.AppFlowService;
import com.wonders.tdsc.publishinfo.dao.TdscNoticeAppDao;
import com.wonders.tdsc.publishinfo.dao.TdscSendEmailInfoDao;
import com.wonders.tdsc.tdscbase.service.CommonQueryService;
import com.wonders.util.PropertiesUtil;

public class TdscNoticePublishService extends BaseSpringManagerImpl {

	private TdscNoticeAppDao tdscNoticeAppDao;

	private CommonQueryService commonQueryService;

	private AppFlowService appFlowService;

	private TdscFileService tdscFileService;

	private TdscBlockTranAppDao tdscBlockTranAppDao;

	private TdscSendEmailInfoDao tdscSendEmailInfoDao;

	private TdscBlockPlanTableDao tdscBlockPlanTableDao;

	public void setTdscBlockPlanTableDao(TdscBlockPlanTableDao tdscBlockPlanTableDao) {
		this.tdscBlockPlanTableDao = tdscBlockPlanTableDao;
	}

	public void setTdscSendEmailInfoDao(TdscSendEmailInfoDao tdscSendEmailInfoDao) {
		this.tdscSendEmailInfoDao = tdscSendEmailInfoDao;
	}

	public void setTdscFileService(TdscFileService tdscFileService) {
		this.tdscFileService = tdscFileService;
	}

	public void setAppFlowService(AppFlowService appFlowService) {
		this.appFlowService = appFlowService;
	}

	public void setTdscBlockTranAppDao(TdscBlockTranAppDao tdscBlockTranAppDao) {
		this.tdscBlockTranAppDao = tdscBlockTranAppDao;
	}

	public void setTdscNoticeAppDao(TdscNoticeAppDao tdscNoticeAppDao) {
		this.tdscNoticeAppDao = tdscNoticeAppDao;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public PageList findPageList(TdscNoticeAppCondition condition) {
		return tdscNoticeAppDao.findPageList(condition);
	}

	public TdscNoticeApp getNoticeApp(String noticeId) {
		return (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
	}

	public void updateAndSaveOpnn(String noticeId, String recordId, SysUser user, List tdscSendEmailList,boolean flag) {
		//flagΪ�Ƿ�������
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);

		if (tdscNoticeApp != null) {
			tdscNoticeApp.setIfReleased("1");
			tdscNoticeApp.setNoticeDate(new Timestamp(System.currentTimeMillis()));
			tdscNoticeAppDao.update(tdscNoticeApp);
		}
		List tdList = this.getTdYj(noticeId, tdscNoticeApp.getNoticeNo());

		if (flag && tdList != null && tdList.size() > 0) {
			for (int i = 0; i < tdList.size(); i++) {
				TdscBlockAppView temp = (TdscBlockAppView) tdList.get(i);
				// ����ÿ�����ص����
				try {
					this.appFlowService.saveOpnn(temp.getAppId(), temp.getTransferMode(), user);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}

		// ��������ļ���Ϣ TDSC_BLOCK_FILE_APP
		TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
		tdscBlockFileApp.setRecordId(recordId);
		tdscBlockFileApp.setFileId(noticeId);
		tdscBlockFileApp.setFilePerson(user.getUserId());
		tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
		tdscFileService.update(tdscBlockFileApp);

		// �����ʼ�������Ϣ
		if (noticeId != null && tdscSendEmailList != null && tdscSendEmailList.size() > 0) {
			tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailList, noticeId, "1");
		}

		String targetURL = null;// TODO ָ��URL
		String targetPath = "";// TODO ָ���ϴ��ļ���·��

		targetPath = PropertiesUtil.getInstance().getProperty("html.save.path") + recordId + ".htm";
		targetURL = PropertiesUtil.getInstance().getProperty("httpclient_uploadfile_url");
		this.uploadFileToWxmh(targetPath, targetURL);
	}

	private void uploadFileToWxmh(String targetPath, String targetURL) {
		File targetFile = new File(targetPath);
		logger.info(">>>>>>>>>>�����ļ���ʼ>>>>>>>>>>>>http��ַ:"+targetURL+"###�ļ�·��:"+targetPath);
		PostMethod filePost = new PostMethod(targetURL);
		// filePost.setRequestHeader("Content-type", "multipart/form-data");
		try {
			Part[] parts = { new FilePart(targetFile.getName(), targetFile) };
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			HttpClient client = new HttpClient();
			//client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				System.out.println("�ϴ��ɹ�");
			} else {
				System.out.println("�ϴ�ʧ��");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			filePost.releaseConnection();
		}
	}

	public List getTdYj(String noticeId, String noitceNo) {
		List appIdList = new ArrayList();

		List tempList = tdscBlockTranAppDao.findTdscBlockTranAppByNoticeNo(noticeId, noitceNo);

		if (tempList != null && tempList.size() > 0) {
			for (int i = 0; i < tempList.size(); i++) {
				TdscBlockTranApp app = (TdscBlockTranApp) tempList.get(i);
				appIdList.add(app.getAppId());
			}
		}

		TdscBaseQueryCondition condition = new TdscBaseQueryCondition();
		condition.setAppIdList(appIdList);
		// ͨ��appIdList�������List
		List tdList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

		return tdList;

	}

	/**
	 * ��ѯ�����б�
	 * 
	 * @return
	 */
	public List queryNoticeList() {
		return tdscNoticeAppDao.queryNoticeList();
	}

	public List queryNoticeListByCondition(TdscNoticeAppCondition condition) {
		return tdscNoticeAppDao.queryNoticeListByCondition(condition);
	}

	/**
	 * ����noticeId��ѯTdscNoticeApp
	 * 
	 * @param noticeId
	 * @return
	 */
	public TdscNoticeApp queryNoticeAppByNoticeId(String noticeId) {
		return (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
	}

	/**
	 * ���ݳ��ù������ϳ��ý����ʾ�����ͷ
	 * 
	 * @param noticeId
	 * @return
	 */
	public String initRelustNoticeTitle(String noticeId) {
		// ����noticeId��ѯ�ó��ù���
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		StringBuffer retString = new StringBuffer();
		if (tdscNoticeApp != null) {
			String noticeNo = tdscNoticeApp.getNoticeNo();
			// ��ϳ��ý����ʾ�����ͷ
			if (noticeNo != null && noticeNo.length() > 13) {
				// ʾ����2008��12��
				retString = new StringBuffer(noticeNo.substring(7, 11)).append("��");
				// 3λ��ˮ�ţ�ȥ����ͷ����
				int tempNum = Integer.parseInt(noticeNo.substring(12, 15)) % 1000;
				retString.append(tempNum).append("��");
			}
		}
		return retString.toString();
	}

	/**
	 * ����condition����List���ϳ�PageLsit����
	 * 
	 * @param list
	 * @param condition
	 * @return
	 */
	public List rebuildPageList(List list) {

		List blockList = new ArrayList();
		List retList = new ArrayList();
		TdscNoticeApp tdscNoticeApp = new TdscNoticeApp();
		TdscBaseQueryCondition tdscBaseQueryCondition = new TdscBaseQueryCondition();
		TdscBlockAppView tdscBlockAppView = new TdscBlockAppView();
		boolean canBeShow = true;
		for (int i = 0; i < list.size(); i++) {
			tdscNoticeApp = (TdscNoticeApp) list.get(i);
			if (tdscNoticeApp.getNoticeId() != null) {
				// ���ò�ѯ������
				tdscBaseQueryCondition.setNoticeId(tdscNoticeApp.getNoticeId());
				// ����NoticeId��ѯ�صؿ��б�
				blockList = commonQueryService.queryTdscBlockAppViewListWithoutNode(tdscBaseQueryCondition);

				if (blockList != null && blockList.size() > 0) {
					for (int j = 0; j < blockList.size(); j++) {
						canBeShow = true;
						// �жϸù����е����еؿ�ڵ��Ƿ��Ѿ����� �����ʾ������
						tdscBlockAppView = (TdscBlockAppView) blockList.get(j);
						if (Integer.parseInt(tdscBlockAppView.getNodeId()) < 18) {
							canBeShow = false;
							break;
						}
					}

				}
			}
			// ������������뵽retList��
			if (canBeShow == true) {
				retList.add(tdscNoticeApp);
			}
		}
		return retList;
	}

	/**
	 * ���ݳ��ù��淢���ؿ�Ľ����ʾ
	 * 
	 * @param tdscBlockTranAppList
	 * @param user
	 * @param noticeId
	 * @return
	 * @throws Exception
	 */
	public String publishNotice(List tdscBlockTranAppList, List tdscSendEmailList, SysUser user, String noticeId, String recordId) throws Exception {

		TdscBlockTranApp tdscBlockTranApp = new TdscBlockTranApp();
		for (int i = 0; i < tdscBlockTranAppList.size(); i++) {
			tdscBlockTranApp = (TdscBlockTranApp) tdscBlockTranAppList.get(i);
			// ���÷���״̬
			tdscBlockTranApp.setIfPublish("1");
			// ��¼����ʱ��
			Date publishDate = new Date(System.currentTimeMillis());
			tdscBlockTranApp.setPublishDate(publishDate);
			tdscBlockTranAppDao.update(tdscBlockTranApp);
			if (tdscBlockTranApp.getTransferMode() != null && !"".equals(tdscBlockTranApp.getTransferMode())) {
				appFlowService.saveOpnn(tdscBlockTranApp.getAppId(), tdscBlockTranApp.getTransferMode(), user);
			}

			if (i == 0) {
				// ����һ��
				// ʹ TdscBlockPlanTable�� status = "00" Ϊ�˲�ѯͳ��ʹ��
				TdscBlockPlanTable plan = (TdscBlockPlanTable) tdscBlockPlanTableDao.get(tdscBlockTranApp.getPlanId());

				plan.setStatus("00");
				plan.setLastActionDate(DateUtil.string2Timestamp(DateConvertor.getCurrentDateWithTimeZone(), "yyyyMMddhhmmss"));

				tdscBlockPlanTableDao.saveOrUpdate(plan);
			}
		}
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		if (tdscNoticeApp != null) {
			tdscNoticeApp.setIfResultPublish("1");
			tdscNoticeApp.setResultRecordId(recordId);
			String nowTime = DateConvertor.getCurrentDateWithTimeZone();
			tdscNoticeApp.setResultPublishDate(DateUtil.string2Timestamp(nowTime, "yyyyMMddhhmmss"));
			tdscNoticeAppDao.update(tdscNoticeApp);
		}
		String targetURL = null;// TODO ָ��URL
		String targetPath = "";// TODO ָ���ϴ��ļ���·��

		targetPath = PropertiesUtil.getInstance().getProperty("html.save.path") + recordId + ".htm";
		targetURL = PropertiesUtil.getInstance().getProperty("httpclient_uploadfile_url");
		this.uploadFileToWxmh(targetPath, targetURL);
		// �����ʼ�������Ϣ
		// if (noticeId != null && tdscSendEmailList != null &&
		// tdscSendEmailList.size() > 0) {
		// tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailList, noticeId,
		// "2");
		// }

		return null;
	}

	/**
	 * �ݴ� ���ù��淢���ؿ�Ľ����ʾ
	 * 
	 * @param tdscBlockTranAppList
	 * @param user
	 * @param noticeId
	 * @return
	 * @throws Exception
	 */
	public String zancunNotice(String noticeId, String recordId) throws Exception {

		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		tdscNoticeApp.setResultRecordId(recordId);
		tdscNoticeApp.setResultPublishDate(new Timestamp(System.currentTimeMillis()));
		tdscNoticeAppDao.update(tdscNoticeApp);
		return null;
	}

	/**
	 * �ݴ� ���ù��淢���ؿ�Ľ����ʾ���ʼ��б�
	 * 
	 * @param tdscBlockTranAppList
	 * @param user
	 * @param noticeId
	 * @return
	 * @throws Exception
	 */
	public String zancunNoticeAndEmail(String noticeId, String recordId, List tdscSendEmailList) throws Exception {
		// ��������ʾ��Ϣ
		if (noticeId != null && !"".equals(noticeId)) {
			this.zancunNotice(noticeId, recordId);

			// �����ʼ�������Ϣ
			if (tdscSendEmailList != null && tdscSendEmailList.size() > 0) {
				tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailList, noticeId, "2");
			}
		}

		return null;
	}

	/**
	 * �����ļ��ʼ�����
	 * 
	 * @param tdscSendEmailInfoList
	 * @param noticeId
	 * @param noticeType
	 */
	public void saveSendEmailList(List tdscSendEmailInfoList, String noticeId, String noticeType) {
		// TODO Auto-generated method stub
		if (noticeId != null && !"".equals(noticeId)) {
			tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailInfoList, noticeId, noticeType);
		}
	}

	/**
	 * �����ļ��ʼ���ѯ
	 * 
	 * @param noticeId
	 * @param noticeType
	 * @return
	 */
	public List querySendEmailListByType(String noticeId, String noticeType) {
		// TODO Auto-generated method stub
		List SendEmailList = new ArrayList();
		if (noticeId != null && !"".equals(noticeId)) {
			SendEmailList = (List) tdscSendEmailInfoDao.querySendEmailListByType(noticeId, noticeType);
		}
		return SendEmailList;
	}

}