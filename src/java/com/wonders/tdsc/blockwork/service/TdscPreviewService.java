package com.wonders.tdsc.blockwork.service;

import java.util.List;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.blockwork.dao.TdscBlockScheduleTableDao;
import com.wonders.tdsc.blockwork.dao.TdscBlockUsedInfoDao;
import com.wonders.tdsc.blockwork.dao.TdscExplorInfoDao;
import com.wonders.tdsc.bo.TdscBlockScheduleTable;
import com.wonders.tdsc.bo.TdscExplorInfo;
import com.wonders.tdsc.flowadapter.service.AppFlowService;

public class TdscPreviewService extends BaseSpringManagerImpl{
    
    //�����������Ӧ��DAO
    private TdscExplorInfoDao tdscExplorInfoDao;

    //���õؿ����ִ�б��Ӧ��DAO
    private TdscBlockScheduleTableDao tdscBlockScheduleTableDao;
    
    //������;��Ϣ���Ӧ��DAO
    private TdscBlockUsedInfoDao tdscBlockUsedInfoDao;
    
   private AppFlowService appFlowService;
    
    public void setAppFlowService(AppFlowService appFlowService) {
        this.appFlowService = appFlowService;
    }
    
    public void setTdscBlockUsedInfoDao(TdscBlockUsedInfoDao tdscBlockUsedInfoDao) {
        this.tdscBlockUsedInfoDao = tdscBlockUsedInfoDao;
    }

    public void setTdscBlockScheduleTableDao(TdscBlockScheduleTableDao tdscBlockScheduleTableDao) {
        this.tdscBlockScheduleTableDao = tdscBlockScheduleTableDao;
    }

    public void setTdscExplorInfoDao(TdscExplorInfoDao tdscExplorInfoDao) {
        this.tdscExplorInfoDao = tdscExplorInfoDao;
    }
    
    /**
     * ���ֳ������������Ϣ��Ҫ¼������ݱ��浽��Ӧ�ı���
     * @param �������������tdscExplorInfo ���õؿ����ִ�б����tdscBlockScheduleTable �ؿ齻����Ϣ���appId
     * 20071121*
     */
    public void savePreviewInfo(TdscBlockScheduleTable tdscBlockScheduleTable,TdscExplorInfo tdscExplorInfo,String appId,String saveType,String transferMode,SysUser user){
        
//      ����appId��ѯ���õؿ����ִ�б��ֵ�Ƿ�Ϊ�գ���Ϊ�ո������ݣ����򱣴�
        TdscBlockScheduleTable scheduleInfo = tdscBlockScheduleTableDao.findScheduleInfo(appId);
        if (null != scheduleInfo){
            scheduleInfo.setInspDate(tdscBlockScheduleTable.getInspDate());
            scheduleInfo.setInspLoc(tdscBlockScheduleTable.getInspLoc());
            tdscBlockScheduleTableDao.update(scheduleInfo);
        }else{
            tdscBlockScheduleTableDao.save(tdscBlockScheduleTable);
        }
        //���濱�����Ϣ��
         tdscExplorInfoDao.saveOrUpdate(tdscExplorInfo); 
         
         try {
             if ("tempSave".equals(saveType)) {
                 this.appFlowService.tempSaveOpnn(appId, transferMode, user);
             } else if ("submitSave".equals(saveType)) {
                 this.appFlowService.saveOpnn(appId, transferMode, user);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
    
    /**
     * ͨ��appId����ѯ���õؿ����ִ�б����Ϣ
     * @param  appId
     * @return tdscBlockScheduleTable
     * 20071123*
     */
    public TdscBlockScheduleTable findScheduleInfo(String appId){
        TdscBlockScheduleTable tdscBlockScheduleTable = tdscBlockScheduleTableDao.findScheduleInfo(appId);
        return tdscBlockScheduleTable;
    }
    
    /**
     * ͨ��appId����ѯ�������������Ϣ
     * @param  appId
     * @return tdscBlockUsedInfoList
     * 20071123*
     */
    public TdscExplorInfo findExplorInfo(String appId){
        TdscExplorInfo tdscExplorInfo = tdscExplorInfoDao.findExplorInfo(appId);
        return tdscExplorInfo;
    }
    
    /**
     * ͨ���ؿ�ID����ѯ������;��Ϣ��list
     * @param  blockId
     * @return tdscBlockUsedInfoList
     * 20071121*
     */
    public List queryTdscBlockUsedInfoList(String blockId){
        //����blockId�õ��ؿ���;��Ϣ���еĹ滮��;�ͳ�������
        List tdscBlockUsedInfoList = tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockId);
        return tdscBlockUsedInfoList;
    }


}
