package com.wonders.tdsc.blockwork.service;

import java.util.Date;
import java.util.List;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockFileAppDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockPartDao;
import com.wonders.tdsc.blockwork.dao.TdscFileSaleInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscPublishRangeInfoDao;
import com.wonders.tdsc.blockwork.dao.WebDocumentFileDao;
import com.wonders.tdsc.bo.TdscBlockFileApp;
import com.wonders.tdsc.bo.WebDocumentFile;
import com.wonders.tdsc.publishinfo.dao.TdscSendEmailInfoDao;

public class TdscFileService extends BaseSpringManagerImpl {

    private TdscBlockFileAppDao tdscBlockFileAppDao;
    
    private TdscBlockPartDao tdscBlockPartDao;
    
    private TdscPublishRangeInfoDao tdscPublishRangeInfoDao;
    
    private TdscSendEmailInfoDao tdscSendEmailInfoDao;
    
    private WebDocumentFileDao webDocumentFileDao;

    public void setTdscPublishRangeInfoDao(TdscPublishRangeInfoDao tdscPublishRangeInfoDao) {
		this.tdscPublishRangeInfoDao = tdscPublishRangeInfoDao;
	}

	public void setTdscBlockFileAppDao(TdscBlockFileAppDao tdscBlockFileAppDao) {
        this.tdscBlockFileAppDao = tdscBlockFileAppDao;
    }

    public void setTdscBlockPartDao(TdscBlockPartDao tdscBlockPartDao) {
		this.tdscBlockPartDao = tdscBlockPartDao;
	}

    public void setTdscSendEmailInfoDao(TdscSendEmailInfoDao tdscSendEmailInfoDao) {
		this.tdscSendEmailInfoDao = tdscSendEmailInfoDao;
	}
    
    
	public void setWebDocumentFileDao(WebDocumentFileDao webDocumentFileDao) {
		this.webDocumentFileDao = webDocumentFileDao;
	}

	/**
     * 上传附件,同时将数据写入出让文件信息表
     * 
     * @param upLoadFile
     * @return
     */
    public void save(TdscBlockFileApp tdscBlockFileApp) {

//        String filePath = null;
//        // 调用文件上传函数，传入upLoadFile
//        filePath = tdscBlockFileAppDao.upLoadFile(upLoadFile, tdscBlockFileApp.getFileId());
//
//        if (filePath == null) {
//            // 上传文件出错
//            System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            // return mapping.findForward("errorPage");
//        }
//        // 设置文件名为用户上传的文件名
//        tdscBlockFileApp.setFileUrl(upLoadFile.getFileName());

        tdscBlockFileAppDao.saveOrUpdate(tdscBlockFileApp);
    }

    /**
     * 重新上传附件,同时更新出让文件信息表
     * 
     * @param upLoadFile
     * @return
     */
    public void update(TdscBlockFileApp tdscBlockFileApp) {

//        String filePath = null;
//        if (!"".equals(upLoadFile.getFileName()) && upLoadFile.getFileName() != null) {
//            // 调用文件上传函数，传入upLoadFile
//            filePath = tdscBlockFileAppDao.upLoadFile(upLoadFile, tdscBlockFileApp.getFileId());
//
//            if (filePath == null) {
//                // 上传文件出错
//                System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                // return mapping.findForward("errorPage");
//            }
//            // 设置文件名为用户上传的文件名
//            tdscBlockFileApp.setFileUrl(upLoadFile.getFileName());
//        }

        tdscBlockFileAppDao.update(tdscBlockFileApp);
    }

    /**
     * 上传附件
     * 
     * @param upLoadFile
     * @return
     */
    /*
     * public String upLoadFile(FormFile upLoadFile,String fileName) { return tdscBlockFileAppDao.upLoadFile(upLoadFile,fileName); }
     */

    /**
     * 通过业务ID取出附件地址
     * 
     * @param fileId
     * @return
     */
    public String getFileName(String fileId) {
        TdscBlockFileApp tdscBlockFileApp = (TdscBlockFileApp) tdscBlockFileAppDao.get(fileId);
        if (tdscBlockFileApp == null)
            return null;
        String fileName = null;
        fileName = tdscBlockFileApp.getFileUrl();

        return fileName;
    }

    public TdscBlockFileApp getBlockFileAppById(String fileId) {

        TdscBlockFileApp tdscBlockFileApp = (TdscBlockFileApp) tdscBlockFileAppDao.get(fileId);

        return tdscBlockFileApp;
    }

    public void rollBack(String appId, TdscBlockFileApp lastTdscBlockFileApp) {

        // 如果lastTdscBlockFileApp==null出让文件没有制做过
        if (lastTdscBlockFileApp != null) {
            tdscBlockFileAppDao.update(lastTdscBlockFileApp);
        } else {
            TdscBlockFileApp tdscBlockFileApp = (TdscBlockFileApp) tdscBlockFileAppDao.get(appId);
            if (tdscBlockFileApp != null)
                tdscBlockFileAppDao.delete(tdscBlockFileApp);
        }

    }
    
    //根据blockId取得所有子弟快列表
    public List getBlockPartByBlockId(String blockId) {
        List tdscBlockPartList = (List) tdscBlockPartDao.getTdscBlockPartList(blockId);
        return tdscBlockPartList;
    }
    
    public List getBlockPartByBlockIdList(List blockidList) {
        List tdscBlockPartList = (List) tdscBlockPartDao.queryBidderInfoListByAppId(blockidList);
        return tdscBlockPartList;
    }
    
    
    public List getListByPlanId(String planId) {
    	List tempList  = (List)tdscPublishRangeInfoDao.getListByPlanId(planId);
    	return tempList;
    }
    
    public List getListByNoticeid(String noticeid) {
    	List tempList  = (List)tdscPublishRangeInfoDao.getListByNoticeid(noticeid);
    	return tempList;
    }
    
    /**
     * 出让公告发布及邮件发送
     * @param 
     */
    public void saveAndUpdate(String noticeId,String userId,String recordID,String modeNameEn,List tdscSendEmailList) {

    	//保存出让公告
    	TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
    	if (!"".equals(noticeId)) {
			tdscBlockFileOld = this.getBlockFileAppById(noticeId);

			if(tdscBlockFileOld!=null){
				tdscBlockFileOld.setFilePerson(userId);
				tdscBlockFileOld.setFileDate(new Date(System.currentTimeMillis()));
				//说明已经保存过，update下
				this.update(tdscBlockFileOld);
			}else{
				TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
	    		tdscBlockFileApp.setRecordId(recordID);
	    		tdscBlockFileApp.setFileId(noticeId);
	    		tdscBlockFileApp.setFileUrl(modeNameEn);//将文件类型 保存在附件地址中
	    		tdscBlockFileApp.setFilePerson(userId);
	    		tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
        	    //保存出让文件信息  TDSC_BLOCK_FILE_APP
    		    this.save(tdscBlockFileApp);
			}
        }
    	
    	//保存邮件发送信息
    	if(noticeId!=null&&tdscSendEmailList!=null&&tdscSendEmailList.size()>0){
    		tdscSendEmailInfoDao.saveSendEmailList(tdscSendEmailList,noticeId,"1");
    	}
  }

	public TdscBlockFileApp getBlockFileAppByRecordId(String recordId) {
		return tdscBlockFileAppDao.getBlockFileAppByRecordId(recordId);
	}

	public WebDocumentFile getWebFileByRecordId(String recordId){
		return webDocumentFileDao.getWebFileByRecordId(recordId);
	}
	
}
