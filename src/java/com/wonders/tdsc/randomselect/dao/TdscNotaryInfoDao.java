package com.wonders.tdsc.randomselect.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscNotaryInfo;

public class TdscNotaryInfoDao extends BaseHibernateDaoImpl{
	public Class getEntityClass() {
		return TdscNotaryInfo.class;
	}
	
	public TdscNotaryInfo findNotaryInfoByNotaryId(String notaryId){
		StringBuffer hql = new StringBuffer("from TdscNotaryInfo a where a.notaryId = '").append(notaryId).append("'") ;
		List list = this.findList(hql.toString());
        if(null!=list&&list.size()>0)
            return (TdscNotaryInfo)list.get(0);
        return null;
	}
	
	public TdscNotaryInfo findNotaryInfoByUserId(String notaryUserId){
	    TdscNotaryInfo notaryInfo = new TdscNotaryInfo();
		StringBuffer hql = new StringBuffer("from TdscNotaryInfo a where a.notaryUserId = '").append(notaryUserId).append("'") ;
		List returnList = this.findList(hql.toString());
		if (returnList!=null && returnList.size()>0){
		    notaryInfo = (TdscNotaryInfo) returnList.get(0);
		}
		
		return notaryInfo;
	}
	
}
