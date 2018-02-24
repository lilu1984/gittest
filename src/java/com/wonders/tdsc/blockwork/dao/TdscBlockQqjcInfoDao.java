package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockQqjcInfo;

public class TdscBlockQqjcInfoDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        // TODO Auto-generated method stub
        return TdscBlockQqjcInfo.class;
    }
    
    public List getTdscBlockQqjcInfoList(String blockId)
    { 
        if(null!=blockId&&!"".equals(blockId)){
           StringBuffer hql = new StringBuffer("from TdscBlockQqjcInfo a where a.blockId ='").append(blockId).append("'");
           List list= findList(hql.toString());
           return list.size()>0?list:null;
        }
        return null;
    }
    
    
    public TdscBlockQqjcInfo getTdscBlockQqjcInfo(String blockId)
    { 
        if(null!=blockId&&!"".equals(blockId)){
           StringBuffer hql = new StringBuffer("from TdscBlockQqjcInfo a where a.blockId ='").append(blockId).append("'");
           List list= findList(hql.toString());
           return list.size()>0?(TdscBlockQqjcInfo)list.get(0):null;
        }
        return null;
    }
    
    
    
}
