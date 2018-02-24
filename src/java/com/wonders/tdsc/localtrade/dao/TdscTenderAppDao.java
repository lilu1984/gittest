package com.wonders.tdsc.localtrade.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscTenderApp;

public class TdscTenderAppDao  extends BaseHibernateDaoImpl{
	
	public Class getEntityClass() {
        return TdscTenderApp.class;
    }

	public List findTenderAppList(){
		StringBuffer hql = new StringBuffer("from TdscTenderApp a where 1=1 order by a.tenderDate desc");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	
	public TdscTenderApp findTenderAppListByCertNo(String certNo){
		StringBuffer hql = new StringBuffer("from TdscTenderApp a where a.tenderCert ='").append(certNo).append("'");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){
			TdscTenderApp tdscTenderApp = (TdscTenderApp)list.get(0);
			return tdscTenderApp;
		}else{
			return null;
		}
	}
	
	public List findTenderAppListByTenderId(String tenderId){
		StringBuffer hql = new StringBuffer("from TdscTenderApp a where a.tenderId ='").append(tenderId).append("' order by a.tenderDate desc");
		List list = findList(hql.toString());
		if(null != list && list.size()>0){	
			return list;
		}else{
			return null;
		}
	}
}
