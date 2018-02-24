package com.wonders.tdsc.retbail.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscReturnBail;

public class TdscReturnBailDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscReturnBail.class;
	}

	public TdscReturnBail getTdscReturnBailByAppIdBidderId(String appId, String bidderId) {
		StringBuffer sql = new StringBuffer("from TdscReturnBail a where 1=1");
		sql.append(" and a.appId = '").append(appId).append("'");
		sql.append(" and a.bidderId = '").append(bidderId).append("'");
		List list = this.findList(sql.toString());
		if (list != null && list.size() > 0)
			return (TdscReturnBail) list.get(0);
		return null;
	}

	public List queryTdscReturnBailList(String bidderId) {
		StringBuffer sql = new StringBuffer("from TdscReturnBail a where 1=1");
		sql.append(" and a.bidderId = '").append(bidderId).append("'");
		return this.findList(sql.toString());
	}

	public List getLastReturnBailActionDateByAppId(String tdscAppId) {
		String sql = "from TdscReturnBail t where t.ifReturn='01' and t.appId= '" + tdscAppId + "'";
		return this.findList(sql);
	}

	public List findTdscReturnBailListByPlanId(String planId) {
		String sql = "from TdscReturnBail t where t.planId='" + planId + "'";
		return this.findList(sql);
	}

}
