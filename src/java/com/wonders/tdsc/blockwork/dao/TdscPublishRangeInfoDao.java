package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscPublishRangeInfo;

public class TdscPublishRangeInfoDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        // TODO Auto-generated method stub
        return TdscPublishRangeInfo.class;
    }
    
    public List getListByPlanId(String planId) {
        StringBuffer hql = new StringBuffer("from TdscPublishRangeInfo a where a.planId='").append(planId).append("'");
       return this.findList(hql.toString());
    }

    public List getListByNoticeid(String noticeid) {
        StringBuffer hql = new StringBuffer("from TdscPublishRangeInfo a where a.noticeid='").append(noticeid).append("'");
       return this.findList(hql.toString());
    }
}
