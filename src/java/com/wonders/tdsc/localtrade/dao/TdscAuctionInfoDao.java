package com.wonders.tdsc.localtrade.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscAuctionApp;
import com.wonders.tdsc.bo.TdscAuctionInfo;

public class TdscAuctionInfoDao extends BaseHibernateDaoImpl{

	public Class getEntityClass() {
		return TdscAuctionInfo.class;
	}
	
	public TdscAuctionInfo getTdscAuctionInfoByAppId(String appId){
		StringBuffer hql = new StringBuffer("from TdscAuctionInfo a where a.appId='").append(appId).append("'");
		List list = this.findList(hql.toString());
		if(list!=null && list.size()>0){
			return (TdscAuctionInfo)list.get(0);
		}else{
			return null;
		}
	}

	public List getTdscAuctionAppByAppId(String appId) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer("from TdscAuctionApp a where a.appId='").append(appId).append("'");
		List list = this.findList(hql.toString());
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
}
