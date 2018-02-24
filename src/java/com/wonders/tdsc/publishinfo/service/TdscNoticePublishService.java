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
		//flag为是否推流程
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
				// 保存每条土地的意见
				try {
					this.appFlowService.saveOpnn(temp.getAppId(), temp.getTransferMode(), user);
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
		}

		// 保存出让文件信息 TDSC_BLOCK_FILE_APP
		TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
		tdscBlockFileApp.setRecordId(recordId);
		tdscBlockFileApp.setFileId(noticeId);
		tdscBlockFileApp.setFilePerson(user.getUserId());
		tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
		tdscFileService.update(tdscBlockFileApp);

		// 保存邮件发送信息
		if (noticeId != null && tdscSendEmailList != null && tdscSendEmailList.size() > 0) {
			tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailList, noticeId, "1");
		}

		String targetURL = null;// TODO 指定URL
		String targetPath = "";// TODO 指定上传文件的路径

		targetPath = PropertiesUtil.getInstance().getProperty("html.save.path") + recordId + ".htm";
		targetURL = PropertiesUtil.getInstance().getProperty("httpclient_uploadfile_url");
		this.uploadFileToWxmh(targetPath, targetURL);
	}

	private void uploadFileToWxmh(String targetPath, String targetURL) {
		File targetFile = new File(targetPath);
		logger.info(">>>>>>>>>>传递文件开始>>>>>>>>>>>>http地址:"+targetURL+"###文件路径:"+targetPath);
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
				System.out.println("上传成功");
			} else {
				System.out.println("上传失败");
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
		// 通过appIdList查出土地List
		List tdList = commonQueryService.queryTdscBlockAppViewListWithoutNode(condition);

		return tdList;

	}

	/**
	 * 查询公告列表
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
	 * 根据noticeId查询TdscNoticeApp
	 * 
	 * @param noticeId
	 * @return
	 */
	public TdscNoticeApp queryNoticeAppByNoticeId(String noticeId) {
		return (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
	}

	/**
	 * 根据出让公告号组合出让结果公示标题的头
	 * 
	 * @param noticeId
	 * @return
	 */
	public String initRelustNoticeTitle(String noticeId) {
		// 根据noticeId查询该出让公告
		TdscNoticeApp tdscNoticeApp = (TdscNoticeApp) tdscNoticeAppDao.get(noticeId);
		StringBuffer retString = new StringBuffer();
		if (tdscNoticeApp != null) {
			String noticeNo = tdscNoticeApp.getNoticeNo();
			// 组合出让结果公示标题的头
			if (noticeNo != null && noticeNo.length() > 13) {
				// 示例：2008第12号
				retString = new StringBuffer(noticeNo.substring(7, 11)).append("第");
				// 3位流水号，去掉开头的零
				int tempNum = Integer.parseInt(noticeNo.substring(12, 15)) % 1000;
				retString.append(tempNum).append("号");
			}
		}
		return retString.toString();
	}

	/**
	 * 根据condition，把List整合成PageLsit返回
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
				// 设置查询的条件
				tdscBaseQueryCondition.setNoticeId(tdscNoticeApp.getNoticeId());
				// 根据NoticeId查询呢地块列表
				blockList = commonQueryService.queryTdscBlockAppViewListWithoutNode(tdscBaseQueryCondition);

				if (blockList != null && blockList.size() > 0) {
					for (int j = 0; j < blockList.size(); j++) {
						canBeShow = true;
						// 判断该公告中的所有地块节点是否都已经到了 结果公示发布。
						tdscBlockAppView = (TdscBlockAppView) blockList.get(j);
						if (Integer.parseInt(tdscBlockAppView.getNodeId()) < 18) {
							canBeShow = false;
							break;
						}
					}

				}
			}
			// 符合条件则加入到retList中
			if (canBeShow == true) {
				retList.add(tdscNoticeApp);
			}
		}
		return retList;
	}

	/**
	 * 根据出让公告发布地块的结果公示
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
			// 设置发布状态
			tdscBlockTranApp.setIfPublish("1");
			// 记录发布时间
			Date publishDate = new Date(System.currentTimeMillis());
			tdscBlockTranApp.setPublishDate(publishDate);
			tdscBlockTranAppDao.update(tdscBlockTranApp);
			if (tdscBlockTranApp.getTransferMode() != null && !"".equals(tdscBlockTranApp.getTransferMode())) {
				appFlowService.saveOpnn(tdscBlockTranApp.getAppId(), tdscBlockTranApp.getTransferMode(), user);
			}

			if (i == 0) {
				// 仅做一次
				// 使 TdscBlockPlanTable的 status = "00" 为了查询统计使用
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
		String targetURL = null;// TODO 指定URL
		String targetPath = "";// TODO 指定上传文件的路径

		targetPath = PropertiesUtil.getInstance().getProperty("html.save.path") + recordId + ".htm";
		targetURL = PropertiesUtil.getInstance().getProperty("httpclient_uploadfile_url");
		this.uploadFileToWxmh(targetPath, targetURL);
		// 保存邮件发送信息
		// if (noticeId != null && tdscSendEmailList != null &&
		// tdscSendEmailList.size() > 0) {
		// tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailList, noticeId,
		// "2");
		// }

		return null;
	}

	/**
	 * 暂存 出让公告发布地块的结果公示
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
	 * 暂存 出让公告发布地块的结果公示及邮件列表
	 * 
	 * @param tdscBlockTranAppList
	 * @param user
	 * @param noticeId
	 * @return
	 * @throws Exception
	 */
	public String zancunNoticeAndEmail(String noticeId, String recordId, List tdscSendEmailList) throws Exception {
		// 保存结果公示信息
		if (noticeId != null && !"".equals(noticeId)) {
			this.zancunNotice(noticeId, recordId);

			// 保存邮件发送信息
			if (tdscSendEmailList != null && tdscSendEmailList.size() > 0) {
				tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailList, noticeId, "2");
			}
		}

		return null;
	}

	/**
	 * 出让文件邮件保存
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
	 * 出让文件邮件查询
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