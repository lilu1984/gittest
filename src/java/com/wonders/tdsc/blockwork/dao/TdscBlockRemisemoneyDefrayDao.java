package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockRemisemoneyDefray;

public class TdscBlockRemisemoneyDefrayDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        // TODO Auto-generated method stub
        return TdscBlockRemisemoneyDefray.class;
    }
    /**
     * ���ݵؿ��Id��ѯ�õؿ����г��ý�֧����Ϣ
     * @param blockId
     * @return
     */
    public List getTdscBlockRemisemoneyDefrayList(String blockId)
    { 
        if(null!=blockId&&!"".equals(blockId)){
           StringBuffer hql = new StringBuffer("from TdscBlockRemisemoneyDefray a where a.blockId ='").append(blockId).append("'");
           List list= findList(hql.toString());
            return list.size()>0?list:null;
        }
        return null;
    }
}
