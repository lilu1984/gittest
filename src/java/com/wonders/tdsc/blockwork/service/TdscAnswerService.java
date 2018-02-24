package com.wonders.tdsc.blockwork.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockScheduleTableDao;
import com.wonders.tdsc.blockwork.dao.TdscFaqInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscReplyConfInfoDao;
import com.wonders.tdsc.bo.TdscBlockScheduleTable;
import com.wonders.tdsc.bo.TdscFaqInfo;
import com.wonders.tdsc.bo.TdscReplyConfInfo;

public class TdscAnswerService extends BaseSpringManagerImpl{
    
    private TdscFaqInfoDao tdscFaqInfoDao;
    
    private TdscReplyConfInfoDao tdscReplyConfInfoDao;
    
    private TdscBlockScheduleTableDao tdscBlockScheduleTableDao;
    
    public void setTdscBlockScheduleTableDao(TdscBlockScheduleTableDao tdscBlockScheduleTableDao) {
        this.tdscBlockScheduleTableDao = tdscBlockScheduleTableDao;
    }
    
    public void setTdscReplyConfInfoDao(TdscReplyConfInfoDao tdscReplyConfInfoDao) {
        this.tdscReplyConfInfoDao = tdscReplyConfInfoDao;
    }

    public void setTdscFaqInfoDao(TdscFaqInfoDao tdscFaqInfoDao) {
        this.tdscFaqInfoDao = tdscFaqInfoDao;
    }
    
    /**
     * ���������Ϣ���е�����
     * @param list-������Ϣ��list
     * 20071121*
     */
    public void saveAnswerInfo(List list,String appId){
        //��մ�����Ϣ���е�����
        if(null != appId){
            List deleteList = tdscFaqInfoDao.findTdscFaqInfoList(appId);
            if(null != deleteList){
                for(int i=0; i<deleteList.size(); i++){
                    TdscFaqInfo deleteInfo = (TdscFaqInfo)deleteList.get(i);
                    String faqId = (String)deleteInfo.getFaqId();
                    tdscFaqInfoDao.deleteById(faqId);
                }
            }
        }
        if(null != list){
            for (Iterator iterator = list.iterator(); iterator.hasNext();){
                TdscFaqInfo tdscFaqInfo = (TdscFaqInfo)iterator.next();
                tdscFaqInfoDao.save(tdscFaqInfo);
            }
        }
    }
    
    /**
     * ���������Ϣ���е�����
     * @param list-������Ϣ��list appId-�ؿ齻����Ϣ��
     * 20071121*
     */
    public void classifyAnswerInfo(List list,String appId){
        if(null != list){
            for(int i = 0; i < list.size(); i++){
                TdscFaqInfo tdscFaqInfo = new TdscFaqInfo();
                //��form�л�ö���tdscFaqInfo
                tdscFaqInfo = (TdscFaqInfo)list.get(i);
                List ifList = this.tdscFaqInfoDao.findTdscFaqInfoList(appId);
                //�����ݿ��л�ö���faqInfo
                TdscFaqInfo faqInfo = (TdscFaqInfo)ifList.get(i);
                //�ж����ݿ��е�ֵ����������������
                if(null != faqInfo){
                    //faqInfo.setQuestionSer(BigDecimal.valueOf(i+1));��������������
                    faqInfo.setKeyWord(tdscFaqInfo.getKeyWord());
                    faqInfo.setAnswerUnit(tdscFaqInfo.getAnswerUnit());
                    tdscFaqInfoDao.update(faqInfo);
                }else{
                    //���ݿ��е�ֵΪ�գ���������������
                    tdscFaqInfoDao.save(tdscFaqInfo);
                }
            }
        }
    }
    
    /**
     * ��ѯ������Ϣ�������
     * @param appId-�ؿ齻����Ϣ��
     * @return returnList
     * 20071121*
     */
    public List findTdscFaqInfoList(String appId){
        List returnList = new ArrayList();
        if(null != appId && !"".equals(appId)){
            //��ô�����Ϣ���list
            returnList = this.tdscFaqInfoDao.findTdscFaqInfoList(appId);
        }
        return returnList;
    }
    
    /**
     * �����ɻ���Ϣ��Ҫ¼������ݱ��浽��Ӧ�ı���
     * @param ���õؿ����ִ�б����tdscBlockScheduleTable ���ɻ���Ϣ�����tdscReplyConfInfo �ؿ齻����Ϣ���appId ��Ҫ�ϴ����ļ�upLoadFile
     * 20071121*
     */
    public void saveReplyInfo(TdscBlockScheduleTable tdscBlockScheduleTable,TdscReplyConfInfo tdscReplyConfInfo,String appId,String modeNameEn,String recordId){
        
        if(null != appId){
//          ����appId��ѯ���õؿ����ִ�б��ֵ�Ƿ�Ϊ�գ���Ϊ�ո������ݣ����򱣴�
            TdscBlockScheduleTable scheduleInfo = tdscBlockScheduleTableDao.findScheduleInfo(appId);
            if (null != scheduleInfo){
                scheduleInfo.setAnswerDate(tdscBlockScheduleTable.getAnswerDate());
                scheduleInfo.setAnswerLoc(tdscBlockScheduleTable.getAnswerLoc());
                tdscBlockScheduleTableDao.update(scheduleInfo);
            }else{
                tdscBlockScheduleTableDao.save(tdscBlockScheduleTable);
            }
//          �����ļ��ϴ�����������upLoadFile
//            String fromFileName = tdscReplyConfInfoDao.upLoad(upLoadFile,tdscReplyConfInfo.getAppId());
//            if (fromFileName == null) {
//                // �ϴ��ļ�����
//                System.out.println("file upload error!!!!!!!!!!");
//                // return mapping.findForward("errorPage");
//            }else{
//
//            }
//          ����appId��ѯ���ɻ���Ϣ���ֵ�Ƿ�Ϊ�գ���Ϊ�ո������ݣ����򱣴�
            TdscReplyConfInfo replyConfInfo = tdscReplyConfInfoDao.findReplyConfInfo(appId);
            if(null != replyConfInfo){
                //replyConfInfo.setAppId(tdscReplyConfInfo.getAppId());
                replyConfInfo.setRecordId(recordId);
                replyConfInfo.setFaqUrl(modeNameEn);
                replyConfInfo.setConfStat(tdscReplyConfInfo.getConfStat());
                tdscReplyConfInfoDao.update(replyConfInfo);
            }else{
                tdscReplyConfInfo.setRecordId(recordId);
                tdscReplyConfInfo.setFaqUrl(modeNameEn);
                tdscReplyConfInfoDao.save(tdscReplyConfInfo);
            }
        }
    }
    
    /**
     * ͨ��appId����ѯ���õؿ����ִ�б����Ϣ
     * @param  appId
     * @return tdscBlockScheduleTable
     * 20071124*
     */
    public TdscBlockScheduleTable findScheduleInfo(String appId){
        TdscBlockScheduleTable tdscBlockScheduleTable = tdscBlockScheduleTableDao.findScheduleInfo(appId);
        return tdscBlockScheduleTable;
    }
    
    /**
     * ͨ��appId��ѯ���ɻ���Ϣ����Ϣ
     * @param appId
     * @return 
     * 20071124*
     */
    public TdscReplyConfInfo findReplyConfInfo(String appId){
        TdscReplyConfInfo tdscReplyConfInfo = tdscReplyConfInfoDao.findReplyConfInfo(appId);
        return tdscReplyConfInfo;
    }


    

}
