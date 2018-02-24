package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscExplorInfo;

public class TdscExplorInfoDao extends BaseHibernateDaoImpl{
    
    protected Class getEntityClass(){
        return TdscExplorInfo.class;
    }
    
    /**
     * 通过appId查询勘查会情况表信息
     * @param appId
     * 20071115*
     */
    public TdscExplorInfo findExplorInfo(String appId){
        
        StringBuffer hql = new StringBuffer("from TdscExplorInfo a where a.explorId = '").append(appId).append("'");
        String queryHql = hql.toString() + " order by a.explorId";
        List explorInfoList = this.findList(queryHql.toString());
        if(explorInfoList != null && explorInfoList.size() > 0){
            return (TdscExplorInfo) explorInfoList.get(0);
        }
        return null;
    }
    

}
