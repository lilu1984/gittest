package com.wonders.tdsc.xborg.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscXbOrgHistory;
import com.wonders.tdsc.bo.condition.TdscXbOrgHistoryCondition;

public class TdscXbOrgHistoryDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscXbOrgHistory.class;
	}

	public PageList findOrgHistoryPageListByCondition(TdscXbOrgHistoryCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscXbOrgHistory yxoh where 1=1 ");
		hql.append(makeWhereClause(condition));
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by yxoh.acitonDate desc";
		PageList pageList = this.findPageListWithHql(countHql, queryHql, paralist, 10, condition.getCurrentPage());
		return pageList;
	}
	
	private List paralist = null; 

	private String makeWhereClause(TdscXbOrgHistoryCondition condition) {
		StringBuffer hql = new StringBuffer();
		if (paralist != null) paralist.clear();
		else paralist = new ArrayList();
		if (StringUtils.isNotEmpty(condition.getOrgAppId())) {
			hql.append(" and yxoh.orgAppId = ?");
			paralist.add(condition.getOrgAppId());
		}
		if (StringUtils.isNotEmpty(condition.getOrgInfoId())) {
			hql.append(" and yxoh.orgInfoId = ?");
			paralist.add(condition.getOrgInfoId());
		}
		return hql.toString();
	}
	
	public  String sqlFilter_first(String hql){
        hql = hql.replaceAll("%", "/%");
        hql = hql.replaceAll("_", "/_");
        return hql;
    }

	public List findOrgHistoryListByCondition(TdscXbOrgHistoryCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscXbOrgHistory yxoh where 1=1 ");
		hql.append(makeWhereClause(condition));
		List list = this.findList(hql.toString(), paralist);
		return list;
	}

}
