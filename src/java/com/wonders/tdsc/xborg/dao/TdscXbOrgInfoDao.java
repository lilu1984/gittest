/**
 * 
 */
package com.wonders.tdsc.xborg.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscXbOrgInfo;
import com.wonders.tdsc.bo.condition.TdscXbOrgInfoCondition;

/**
 * @author vipo
 * 
 */
public class TdscXbOrgInfoDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscXbOrgInfo.class;
	}

	private List paralist = null; 
	
	public PageList findOrgAppPageListByCondition(TdscXbOrgInfoCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscXbOrgInfo yxoi where 1=1 ");
		hql.append(makeWhereClause(condition));
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by yxoi.orgInfoId desc";
		PageList pageList = this.findPageListWithHql(countHql, queryHql, paralist, 10, condition.getCurrentPage());
		return pageList;
	}

	private String makeWhereClause(TdscXbOrgInfoCondition condition) {
		StringBuffer hql = new StringBuffer();
		if (paralist != null) paralist.clear();
		else paralist = new ArrayList();
		if (condition.getQueryBeginDate() != null) {
			hql.append(" and yxoi.stratDate >= ?");
			paralist.add(condition.getQueryBeginDate());
		} 
		if (condition.getQueryEndDate() != null) {
			hql.append(" and yxoi.stratDate <= ?");
			paralist.add(condition.getQueryEndDate());
		}
		if (StringUtils.isNotEmpty(condition.getStatus())) {
			hql.append(" and yxoi.status = ?");
			paralist.add(condition.getStatus());
		}
		return hql.toString();
	}
	
	public List findOrgAppListByCondition(TdscXbOrgInfoCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscXbOrgInfo yxoi where 1=1 ");
		hql.append(makeWhereClause(condition));
		return this.findList(hql.toString(), paralist);
	}
	
	public TdscXbOrgInfo getOrgInfoByCondition(TdscXbOrgInfoCondition condition) {
		List list = this.findOrgAppListByCondition(condition);
		if (list != null && list.size() > 0) return (TdscXbOrgInfo) list.get(0);
		return null;
	}
	
	public  String sqlFilter_first(String hql){
        hql = hql.replaceAll("%", "/%");
        hql = hql.replaceAll("_", "/_");
        return hql;
    }
}
