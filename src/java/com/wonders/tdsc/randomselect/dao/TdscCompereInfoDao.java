package com.wonders.tdsc.randomselect.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscCompereInfo;


public class TdscCompereInfoDao  extends BaseHibernateDaoImpl{
	
	public Class getEntityClass() {
		return TdscCompereInfo.class;
	}
	
	public TdscCompereInfo findCompereInfoByCompereId(String compereId){
		StringBuffer hql = new StringBuffer("from TdscCompereInfo a where a.compereId = '").append(compereId).append("'") ;
        List list=this.findList(hql.toString());
        if(list!=null&&list.size()>0)
        {
            return (TdscCompereInfo)list.get(0);
        }
		return null;
	}
}
