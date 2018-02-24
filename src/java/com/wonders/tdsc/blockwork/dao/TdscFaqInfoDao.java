package com.wonders.tdsc.blockwork.dao;

import java.util.ArrayList;
import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscFaqInfo;

public class TdscFaqInfoDao extends BaseHibernateDaoImpl{
    
    protected Class getEntityClass(){
        return TdscFaqInfo.class;
    }
    
    /**
     * 根据appId查询答疑信息表
     * @author wangzhichao
     * @param appId
     * @return returnList
     */
    public List findTdscFaqInfoList(String appId){
        
        List returnList = new ArrayList();
        if(null != appId){
            StringBuffer hql = new StringBuffer("from TdscFaqInfo a where 1 = 1");
            hql.append(" and a.appId = '").append(appId).append("'");
            String queryHql = hql.toString() + " order by a.questionSer";
            returnList = this.findList(queryHql.toString());
        }
        return returnList;
        
    }
    

}
