package com.wonders.tdsc.credit.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBidderCxApp;

public class TdscBidderCxAppDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscBidderCxApp.class;
	}
	
	public TdscBidderCxApp queryCxAppByCorpId(String corpId) {
		List list = findBidderCxAppListByCorpId(corpId);
		return list != null && list.size() > 0?(TdscBidderCxApp)list.get(0):null;
	}
	
	public List findBidderCxAppListByCorpId(String corpId) {
		if (StringUtils.isEmpty(corpId)) return null;
		StringBuffer hql = new StringBuffer("from TdscBidderCxApp tbca where tbca.corpId = ? ");
		List paralist = new ArrayList();
		paralist.add(corpId);
		String sql = hql.toString();
		List list = this.findList(sql, paralist);
		return list;
	}
}
