package com.wonders.tdsc.localtrade.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscTenderInfo;

public class TdscTenderInfoDao extends BaseHibernateDaoImpl{
	
	public Class getEntityClass() {
        return TdscTenderInfo.class;
    }
	
	/**
	 * 查询招标会Id通过appId
	 * @param appId
	 * @return
	 */
	public String findTenderIdByAppId(String appId){
		StringBuffer hql = new StringBuffer("select a.tenderId from TdscTenderInfo a where a.appId ='").append(appId).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return (String)list.get(0);
		}else{
			return null;
		}
	}
}
