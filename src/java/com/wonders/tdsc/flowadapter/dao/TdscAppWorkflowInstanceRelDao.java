package com.wonders.tdsc.flowadapter.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscAppWorkflowInstanceRel;

public class TdscAppWorkflowInstanceRelDao extends BaseHibernateDaoImpl{
    
    protected Class getEntityClass(){
        return TdscAppWorkflowInstanceRel.class;
    }
    
    /**
     * ͨ��appId��businessCodeId��ѯҵ��������ʵ����Ŷ�Ӧ����Ϣ
     * @param appId
     * 			ҵ��ID
     * @param businessCodeId
     * 			����ģ��ID
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
