package com.wonders.tdsc.localtrade.dao;

import java.util.ArrayList;
import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscAuctionApp;

public class TdscAuctionAppDao extends BaseHibernateDaoImpl{

	public Class getEntityClass() {
		return TdscAuctionApp.class;
	}
	
	public List findTdscAuctionAppListByAppId(String appId){
		StringBuffer hql = new StringBuffer("from TdscAuctionInfo a,TdscAuctionApp b where a.appId='").append(appId).append("'");
		hql.append(" and a.auctionId=b.auctionId order by b.auctionSer asc");
		List list = this.findList(hql.toString());
		List tdscAuctionAppList = new ArrayList();
		if(list!=null && list.size()>0){
			for (int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				TdscAuctionApp tdscAuctionApp = (TdscAuctionApp)obj[1];
				tdscAuctionAppList.add(tdscAuctionApp);
			}
		}
		return tdscAuctionAppList;
	}
	
	public List findAppListByAppId(String appId){
		StringBuffer hql = new StringBuffer("from TdscAuctionApp a where a.appId='").append(appId).append("'").append(" order by a.auctionSer asc");
		List list = this.findList(hql.toString());
		return list;
	}
	public List findAppListByAppIdDesc(String appId){
		StringBuffer hql = new StringBuffer("from TdscAuctionApp a where a.appId='").append(appId).append("'").append(" order by a.totalPrice desc");
		List list = this.findList(hql.toString());
		return list;
	}
	
	public List findAppListByAppIddesc(String appId){
		StringBuffer hql = new StringBuffer("from TdscAuctionApp a where a.appId='").append(appId).append("'").append(" order by a.auctionSer desc");
		List list = this.findList(hql.toString());
		return list;
	}
	
}
