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
     * �ϴ�����,ͬʱ������д������ļ���Ϣ��
     * 
     * @param upLoadFile
     * @return
     */
    public void save(TdscBlockFileApp tdscBlockFileApp) {

//        String filePath = null;
//        // �����ļ��ϴ�����������upLoadFile
//        filePath = tdscBlockFileAppDao.upLoadFile(upLoadFile, tdscBlockFileApp.getFileId());
//
//        if (filePath == null) {
//            // �ϴ��ļ�����
//            System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            // return mapping.findForward("errorPage");
//        }
//        // �����ļ���Ϊ�û��ϴ����ļ���
//        tdscBlockFileApp.setFileUrl(upLoadFile.getFileName());

        tdscBlockFileAppDao.saveOrUpdate(tdscBlockFileApp);
    }

    /**
     * �����ϴ�����,ͬʱ���³����ļ���Ϣ��
     * 
     * @param upLoadFile
     * @return
     */
    public void update(TdscBlockFileApp tdscBlockFileApp) {

//        String filePath = null;
//        if (!"".equals(upLoadFile.getFileName()) && upLoadFile.getFileName() != null) {
//            // �����ļ��ϴ�����������upLoadFile
//            filePath = tdscBlockFileAppDao.upLoadFile(upLoadFile, tdscBlockFileApp.getFileId());
//
//            if (filePath == null) {
//                // �ϴ��ļ�����
//                System.out.println("file upload error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                // return mapping.findForward("errorPage");
//            }
//            // �����ļ���Ϊ�û��ϴ����ļ���
//            tdscBlockFileApp.setFileUrl(upLoadFile.getFileName());
//        }

        tdscBlockFileAppDao.update(tdscBlockFileApp);
    }

    /**
     * �ϴ�����
     * 
     * @param upLoadFile
     * @return
     */
    /*
     * public String upLoadFile(FormFile upLoadFile,String fileName) { return tdscBlockFileAppDao.upLoadFile(upLoadFile,fileName); }
     */

    /**
     * ͨ��ҵ��IDȡ��������ַ
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

        // ���lastTdscBlockFileApp==null�����ļ�û��������
        if (lastTdscBlockFileApp != null) {
            tdscBlockFileAppDao.update(lastTdscBlockFileApp);
        } else {
            TdscBlockFileApp tdscBlockFileApp = (TdscBlockFileApp) tdscBlockFileAppDao.get(appId);
            if (tdscBlockFileApp != null)
                tdscBlockFileAppDao.delete(tdscBlockFileApp);
        }

    }
    
    //����blockIdȡ�������ӵܿ��б�
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
     * ���ù��淢�����ʼ�����
     * @param 
     */
    public void saveAndUpdate(String noticeId,String userId,String recordID,String modeNameEn,List tdscSendEmailList) {

    	//������ù���
    	TdscBlockFileApp tdscBlockFileOld = new TdscBlockFileApp();
    	if (!"".equals(noticeId)) {
			tdscBlockFileOld = this.getBlockFileAppById(noticeId);

			if(tdscBlockFileOld!=null){
				tdscBlockFileOld.setFilePerson(userId);
				tdscBlockFileOld.setFileDate(new Date(System.currentTimeMillis()));
				//˵���Ѿ��������update��
				this.update(tdscBlockFileOld);
			}else{
				TdscBlockFileApp tdscBlockFileApp = new TdscBlockFileApp();
	    		tdscBlockFileApp.setRecordId(recordID);
	    		tdscBlockFileApp.setFileId(noticeId);
	    		tdscBlockFileApp.setFileUrl(modeNameEn);//���ļ����� �����ڸ�����ַ��
	    		tdscBlockFileApp.setFilePerson(userId);
	    		tdscBlockFileApp.setFileDate(new Date(System.currentTimeMillis()));
        	    //��������ļ���Ϣ  TDSC_BLOCK_FILE_APP
    		    this.save(tdscBlockFileApp);
			}
        }
    	
    	//�����ʼ�������Ϣ
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
