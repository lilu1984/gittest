package com.wonders.tdsc.flowadapter.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscAppWorkflowInstanceRel;

public class TdscAppWorkflowInstanceRelDao extends BaseHibernateDaoImpl{
    
    protected Class getEntityClass(){
        return TdscAppWorkflowInstanceRel.class;
    }
    
    /**
     * 通过appId和businessCodeId查询业务编号流程实例编号对应表信息
     * @param appId
     * 			业务ID
     * @param businessCodeId
     * 			流程模版ID
     */
    public TdscAppWorkflowInstanceRel findRelInfo(String appId, String businessCodeId){
        
        StringBuffer hql = new StringBuffer("from TdscAppWorkflowInstanceRel a where a.appId = '").append(appId).append("' and a.businessCodeId = '").append(businessCodeId).append("'");
        String queryHql = hql.toString() + " order by a.processInstanceId";
        List relList = this.findList(queryHql.toString());
        if(relList != null && relList.size() > 0){
            return (TdscAppWorkflowInstanceRel) relList.get(0);
        }else{
            return null;
        }
    }
}
