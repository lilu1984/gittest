package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockScheduleTable;

public class TdscBlockScheduleTableDao extends BaseHibernateDaoImpl{
    
    protected Class getEntityClass(){
        return TdscBlockScheduleTable.class;
    }

    /**
     * 通过appId查询出让地块进度执行表信息
     * @param appId
     * 20071115*
     */
    public TdscBlockScheduleTable findScheduleInfo(String appId){
        
        StringBuffer hql = new StringBuffer("from TdscBlockScheduleTable a where a.appId = '").append(appId).append("'");
        String queryHql = hql.toString() + " order by a.appId";
        List scheduleInfoList = this.findList(queryHql.toString());
        if(scheduleInfoList != null && scheduleInfoList.size() > 0){
            return (TdscBlockScheduleTable) scheduleInfoList.get(0);
        }
        return null;
    } 
    
}
