package com.wonders.tdsc.blockwork.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscFileSaleInfo;
import com.wonders.tdsc.bo.condition.TdscFileSaleCondition;

public class TdscFileSaleInfoDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		// TODO Auto-generated method stub
		return TdscFileSaleInfo.class;
	}

	public List queryAllAppId(String noticeId, String bidderName) {
		StringBuffer sql = new StringBuffer("from TdscFileSaleInfo a where 1 = 1");
		sql.append(" and a.noticeId='").append(noticeId).append("'");
		sql.append(" and a.bidderName='").append(bidderName).append("'");
		sql.append(" order by a.actionDate desc");
		return findList(sql.toString());
	}
	
	public List findPageList(TdscFileSaleCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscFileSaleInfo a where 1 = 1");

		// 组装条件语句
		hql.append(makeWhereClause(condition));
		hql.append(" order by a.actionDate desc");

		return findList(hql.toString());
	}

	/**
	 * 使用 distinct sql 查询所有买文件的公司 by noticeId
	 */
	public List queryAllCompanyList(String noticeId) {
		//StringBuffer sql = new StringBuffer("select distinct a.bidderName from TdscFileSaleInfo a where 1 = 1");
		StringBuffer sql = new StringBuffer(" from TdscFileSaleInfo a where 1 = 1");
		sql.append(" and a.noticeId='").append(noticeId).append("'");
		return findList(sql.toString());
	}

	private String makeWhereClause(TdscFileSaleCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// 组装条件语句
		if (StringUtils.isNotEmpty(condition.getBidderName())) {
			whereClause.append(" and a.bidderName like '%").append(condition.getBidderName().trim()).append("%' ");
		}
		if (StringUtils.isNotEmpty(condition.getResultName())) {
			whereClause.append(" and a.resultName like '%").append(condition.getResultName().trim()).append("%' ");
		}
		
		if (StringUtils.isNotEmpty(condition.getBidderLxdh())) {
			whereClause.append(" and a.bidderLxdh = '").append(condition.getBidderLxdh().trim()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getRecordId())) {
			whereClause.append(" and a.recordId = '").append(condition.getRecordId()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getNoticeId())) {
			whereClause.append(" and a.noticeId = '").append(condition.getNoticeId()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getIfValidity())) {
			whereClause.append(" and a.ifValidity = '").append(condition.getIfValidity()).append("' ");
		}
		if (StringUtils.isNotEmpty(condition.getAppId())) {
			whereClause.append(" and a.appId = '").append(condition.getAppId()).append("' ");
		}

		return whereClause.toString();
	}

}
