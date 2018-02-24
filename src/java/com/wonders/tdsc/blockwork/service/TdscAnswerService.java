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
     * 保存答疑信息表中的数据
     * @param list-答疑信息表list
     * 20071121*
     */
    public void saveAnswerInfo(List list,String appId){
        //清空答疑信息表中的数据
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
     * 归类答疑信息表中的数据
     * @param list-答疑信息表list appId-地块交易信息表
     * 20071121*
     */
    public void classifyAnswerInfo(List list,String appId){
        if(null != list){
            for(int i = 0; i < list.size(); i++){
                TdscFaqInfo tdscFaqInfo = new TdscFaqInfo();
                //从form中获得对象tdscFaqInfo
                tdscFaqInfo = (TdscFaqInfo)list.get(i);
                List ifList = this.tdscFaqInfoDao.findTdscFaqInfoList(appId);
                //从数据库中获得对象faqInfo
                TdscFaqInfo faqInfo = (TdscFaqInfo)ifList.get(i);
                //判断数据库中的值，更新新增的数据
                if(null != faqInfo){
                    //faqInfo.setQuestionSer(BigDecimal.valueOf(i+1));重新排列问题编号
                    faqInfo.setKeyWord(tdscFaqInfo.getKeyWord());
                    faqInfo.setAnswerUnit(tdscFaqInfo.getAnswerUnit());
                    tdscFaqInfoDao.update(faqInfo);
                }else{
                    //数据库中的值为空，保存新增的数据
                    tdscFaqInfoDao.save(tdscFaqInfo);
                }
            }
        }
    }
    
    /**
     * 查询答疑信息表的数据
     * @param appId-地块交易信息表
     * @return returnList
     * 20071121*
     */
    public List findTdscFaqInfoList(String appId){
        List returnList = new ArrayList();
        if(null != appId && !"".equals(appId)){
            //获得答疑信息表的list
            returnList = this.tdscFaqInfoDao.findTdscFaqInfoList(appId);
        }
        return returnList;
    }
    
    /**
     * 将答疑会信息需要录入的数据保存到相应的表中
     * @param 出让地块进度执行表对象tdscBlockScheduleTable 答疑会信息表对象tdscReplyConfInfo 地块交易信息表的appId 需要上传的文件upLoadFile
     * 20071121*
     */
    public void saveReplyInfo(TdscBlockScheduleTable tdscBlockScheduleTable,TdscReplyConfInfo tdscReplyConfInfo,String appId,String modeNameEn,String recordId){
        
        if(null != appId){
//          根据appId查询出让地块进度执行表的值是否为空，不为空更新数据，否则保存
            TdscBlockScheduleTable scheduleInfo = tdscBlockScheduleTableDao.findScheduleInfo(appId);
            if (null != scheduleInfo){
                scheduleInfo.setAnswerDate(tdscBlockScheduleTable.getAnswerDate());
                scheduleInfo.setAnswerLoc(tdscBlockScheduleTable.getAnswerLoc());
                tdscBlockScheduleTableDao.update(scheduleInfo);
            }else{
                tdscBlockScheduleTableDao.save(tdscBlockScheduleTable);
            }
//          调用文件上传函数，传入upLoadFile
//            String fromFileName = tdscReplyConfInfoDao.upLoad(upLoadFile,tdscReplyConfInfo.getAppId());
//            if (fromFileName == null) {
//                // 上传文件出错
//                System.out.println("file upload error!!!!!!!!!!");
//                // return mapping.findForward("errorPage");
//            }else{
//
//            }
//          根据appId查询答疑会信息表的值是否为空，不为空更新数据，否则保存
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
     * 通过appId来查询出让地块进度执行表的信息
     * @param  appId
     * @return tdscBlockScheduleTable
     * 20071124*
     */
    public TdscBlockScheduleTable findScheduleInfo(String appId){
        TdscBlockScheduleTable tdscBlockScheduleTable = tdscBlockScheduleTableDao.findScheduleInfo(appId);
        return tdscBlockScheduleTable;
    }
    
    /**
     * 通过appId查询答疑会信息表信息
     * @param appId
     * @return 
     * 20071124*
     */
    public TdscReplyConfInfo findReplyConfInfo(String appId){
        TdscReplyConfInfo tdscReplyConfInfo = tdscReplyConfInfoDao.findReplyConfInfo(appId);
        return tdscReplyConfInfo;
    }


    

}
