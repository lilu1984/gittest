package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBlockPjxxInfo;

public class TdscBlockPjxxInfoDao extends BaseHibernateDaoImpl{
    
    protected Class getEntityClass(){
        return TdscBlockPjxxInfo.class;
    }
    
    /**
     * 根据blockId查询配件信息
     * @param blockId
     * @return
     */
    public TdscBlockPjxxInfo getTdscBlockPjxxInfoByBlockId(String blockId){
    	
		StringBuffer queryHql = new StringBuffer("from TdscBlockPjxxInfo a where  a.blockId ='").append(blockId).append("'");

		List list = this.findList(queryHql.toString());
    	
		if(list != null && list.size() > 0){
			return (TdscBlockPjxxInfo)list.get(0);
		}
		
    	return null;
    }

}
