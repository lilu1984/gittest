package com.wonders.tdsc.presell.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBlockPresell;
import com.wonders.tdsc.bo.condition.TdscBlockPresellCondition;

public class TdscBlockPresellDao extends BaseHibernateDaoImpl {
	protected Class getEntityClass() {
		return TdscBlockPresell.class;
	}

	public PageList findPageList(TdscBlockPresellCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscBlockPresell a where 1 = 1");

		// 组装条件语句
		if (StringUtils.isNotEmpty(condition.getBlockName())) {
			hql.append(" and a.blockName like '%").append(StringUtil.GBKtoISO88591(condition.getBlockName().trim())).append("%' ");
		}

		String countHql = "select count(*) " + hql.toString() + " order by a.createTime desc";
		String queryHql = hql.toString() + " order by a.createTime desc";

		PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

		return pageList;
	}

	public List findAllFabu() {
		Criteria criteria = getSession().createCriteria(TdscBlockPresell.class);
		criteria.add(Expression.eq("available", "1"));
		List list = criteria.list();
		return list;
	}
	
	public List findByIds(List list) {
		Criteria criteria = getSession().createCriteria(TdscBlockPresell.class);
		criteria.add(Expression.in("presellId", list));
		List result = criteria.list();
		return result;
	}
}
