package com.wonders.tdsc.bank.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.bo.TdscBankApp;
import com.wonders.tdsc.bo.condition.TdscBankAppCondition;

public class TdscBankAppDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscBankApp.class;
	}

	public PageList queryTdscBankAppList(TdscBankAppCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscBankApp a where 1=1");
		if (StringUtils.isNotEmpty(condition.getPayName())) {
			hql.append(" and a.payName = '").append(StringUtil.GBKtoISO88591(condition.getPayName().trim())).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getOperator())) {
			hql.append(" and a.operator = '").append(condition.getOperator()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getOrderKey())) {
			hql.append(" order by a.").append(condition.getOrderKey());
		}
		if (StringUtils.isNotEmpty(condition.getOrderType())) {
			hql.append(" " + condition.getOrderType());
		}

		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString();

		PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

		return pageList;
	}

	public TdscBankApp getTdscBankAppById(String tdscBankAppId) {
		StringBuffer hql = new StringBuffer("from TdscBankApp a where a.id = '").append(tdscBankAppId).append("'");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			TdscBankApp TdscBankApp = (TdscBankApp) list.get(0);
			return TdscBankApp;
		} else {
			return null;
		}
	}

	public List findTdscBankAppList(TdscBankAppCondition condition) {
		StringBuffer hql = new StringBuffer("from TdscBankApp a where 1=1 ");

		// ===============开始组装缴纳人名称查询条件===============
		if (StringUtils.isNotEmpty(condition.getNoticeId())) {
			hql.append("and (a.noticeId = '").append(condition.getNoticeId()).append("' or a.noticeId is null) ");
		}
		
		if (StringUtils.isNotEmpty(condition.getPayName())) {
			hql.append("and (a.payName = '").append(StringUtil.GBKtoISO88591(condition.getPayName().trim())).append("' ");
		}
		StringBuffer payNameStr = new StringBuffer("");
		if (StringUtils.isNotEmpty(condition.getPayName1())) {//代缴人1
			payNameStr.append("or a.payName = '").append(StringUtil.GBKtoISO88591(condition.getPayName1().trim())).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getPayName2())) {//代缴人2
			payNameStr.append("or a.payName = '").append(StringUtil.GBKtoISO88591(condition.getPayName2().trim())).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getPayName3())) {//代缴人3
			payNameStr.append("or a.payName = '").append(StringUtil.GBKtoISO88591(condition.getPayName3().trim())).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getPayName4())) {//代缴人4
			payNameStr.append("or a.payName = '").append(StringUtil.GBKtoISO88591(condition.getPayName4().trim())).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getPayName5())) {//代缴人5
			payNameStr.append("or a.payName = '").append(StringUtil.GBKtoISO88591(condition.getPayName5().trim())).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getBidderId())) {//该竞买人的bidderId
			payNameStr.append("or a.bidderId = '").append(condition.getBidderId()).append("' ");
		}
		if(!"".equals(payNameStr)){
			hql.append(payNameStr);
		}
		hql.append(")");
		// ===============缴纳人名称查询条件组装完毕===============

		if (StringUtils.isNotEmpty(condition.getOperator())) {
			hql.append(" and a.operator = '").append(condition.getOperator()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getStatus())) {
			hql.append(" and a.status = '").append(condition.getStatus()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getOrderKey())) {
			hql.append(" order by a.").append(condition.getOrderKey());
		}
		if (StringUtils.isNotEmpty(condition.getOrderType())) {
			hql.append(" " + condition.getOrderType());
		}

		return findList(hql.toString());
	}

	public List queryTdscBankAppListByBidderId(String bidderId) {
		StringBuffer hql = new StringBuffer("from TdscBankApp a where 1=1 and a.bidderId = '" + bidderId + "' ");
		return findList(hql.toString());
	}

}
