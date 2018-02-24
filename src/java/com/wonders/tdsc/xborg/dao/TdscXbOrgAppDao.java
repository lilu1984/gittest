package com.wonders.tdsc.xborg.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscXbOrgApp;
import com.wonders.tdsc.bo.condition.TdscXbOrgAppCondition;

public class TdscXbOrgAppDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscXbOrgApp.class;
	}
	
	private List paralist = null; 

	public PageList findOrgAppPageListByCondition(TdscXbOrgAppCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscXbOrgApp yxoa where 1=1 ");
		hql.append(makeWhereClause(condition));
		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by yxoa.orgAppId desc";
		PageList pageList = this.findPageListWithHql(countHql, queryHql, paralist, 10, condition.getCurrentPage());
		return pageList;
	}

	private String makeWhereClause(TdscXbOrgAppCondition condition) {
		StringBuffer hql = new StringBuffer();
		if (paralist != null) paralist.clear();
		else paralist = new ArrayList();
		if (StringUtils.isNotEmpty(condition.getOrgName())) {
			hql.append(" and yxoa.orgName like ?");
			paralist.add("%" + sqlFilter_first(condition.getOrgName()) + "%");
		}
		if (StringUtils.isNotEmpty(condition.getOrgZhucr())) {
			hql.append(" and yxoa.orgZhucr like ?");
			paralist.add("%" + sqlFilter_first(condition.getOrgZhucr()) + "%");
		}
		if (StringUtils.isNotEmpty(condition.getOrgLinkMan())) {
			hql.append(" and yxoa.orgLinkMan like ?");
			paralist.add("%" + sqlFilter_first(condition.getOrgLinkMan()) + "%");
		}
		return hql.toString();
	}
	
	public  String sqlFilter_first(String hql){
        hql = hql.replaceAll("%", "/%");
        hql = hql.replaceAll("_", "/_");
        return hql;
    }

	public List findOrgAppListByCondition(TdscXbOrgAppCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscXbOrgApp yxoa where 1=1 ");
		hql.append(makeWhereClause(condition));
		List list = this.findList(hql.toString(), paralist);
		return list;
	}
	
}
