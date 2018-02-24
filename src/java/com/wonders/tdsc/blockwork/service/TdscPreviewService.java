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
    
    //勘查会情况表对应的DAO
    private TdscExplorInfoDao tdscExplorInfoDao;

    //出让地块进度执行表对应的DAO
    private TdscBlockScheduleTableDao tdscBlockScheduleTableDao;
    
    //土地用途信息表对应的DAO
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
     * 将现场勘察会土地信息需要录入的数据保存到相应的表中
     * @param 勘查会情况表对象tdscExplorInfo 出让地块进度执行表对象tdscBlockScheduleTable 地块交易信息表的appId
     * 20071121*
     */
    public void savePreviewInfo(TdscBlockScheduleTable tdscBlockScheduleTable,TdscExplorInfo tdscExplorInfo,String appId,String saveType,String transferMode,SysUser user){
        
//      根据appId查询出让地块进度执行表的值是否为空，不为空更新数据，否则保存
        TdscBlockScheduleTable scheduleInfo = tdscBlockScheduleTableDao.findScheduleInfo(appId);
        if (null != scheduleInfo){
            scheduleInfo.setInspDate(tdscBlockScheduleTable.getInspDate());
            scheduleInfo.setInspLoc(tdscBlockScheduleTable.getInspLoc());
            tdscBlockScheduleTableDao.update(scheduleInfo);
        }else{
            tdscBlockScheduleTableDao.save(tdscBlockScheduleTable);
        }
        //保存勘察会信息表
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
     * 通过appId来查询出让地块进度执行表的信息
     * @param  appId
     * @return tdscBlockScheduleTable
     * 20071123*
     */
    public TdscBlockScheduleTable findScheduleInfo(String appId){
        TdscBlockScheduleTable tdscBlockScheduleTable = tdscBlockScheduleTableDao.findScheduleInfo(appId);
        return tdscBlockScheduleTable;
    }
    
    /**
     * 通过appId来查询勘查会情况表的信息
     * @param  appId
     * @return tdscBlockUsedInfoList
     * 20071123*
     */
    public TdscExplorInfo findExplorInfo(String appId){
        TdscExplorInfo tdscExplorInfo = tdscExplorInfoDao.findExplorInfo(appId);
        return tdscExplorInfo;
    }
    
    /**
     * 通过地块ID来查询土地用途信息的list
     * @param  blockId
     * @return tdscBlockUsedInfoList
     * 20071121*
     */
    public List queryTdscBlockUsedInfoList(String blockId){
        //根据blockId得到地块用途信息表中的规划用途和出让年限
        List tdscBlockUsedInfoList = tdscBlockUsedInfoDao.queryTdscBlockUsedInfoListByBlockId(blockId);
        return tdscBlockUsedInfoList;
    }


}
