package com.wonders.tdsc.kyq.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.KyqNotice;
import com.wonders.tdsc.kyq.service.KyqContent;

public class KyqNoticeDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return KyqNotice.class;
	}

	/**
	 * 查询公告信息
	 * 
	 * @param condition
	 * @return
	 */
	public List findNoticeList(KyqBaseQueryCondition condition) {
		if (condition != null) {
			List list = new ArrayList();
			StringBuffer hql = new StringBuffer("from KyqNotice a where 1=1");
			if (StringUtils.isNotEmpty(condition.getNoticeNumber())) {
				hql.append(" and a.noticeNumber like :noticeNumber ");
			}
			if (StringUtils.isNotEmpty(condition.getNoticeId())) {
				hql.append(" and a.noticeId like :noticeId ");
			}
			if (StringUtils.isNotEmpty(condition.getTransferMode())) {
				hql.append(" and a.transferMode =:transferMode ");
			}
			if (condition.getTranStatusList()!=null && condition.getTranStatusList().size()>0) {
				hql.append(" and a.status in (:tranStatusList) ");
			}
			if (StringUtils.isNotEmpty(condition.getIfResultPublish())) {
				hql.append(" and a.ifResultPublish =:ifResultPublish ");
			}
			try {
				Query query = this.getSession().createQuery(hql.toString());
				if (StringUtils.isNotEmpty(condition.getNoticeNumber())) {
					query.setParameter("noticeNumber", "%" + (condition.getNoticeNumber()) + "%");
				}
				if (StringUtils.isNotEmpty(condition.getTransferMode())) {
					query.setParameter("transferMode", condition.getTransferMode());
				}
				if (StringUtils.isNotEmpty(condition.getNoticeId())) {
					query.setParameter("noticeId", "%" + (condition.getNoticeId()) + "%");
				}
				if (condition.getTranStatusList()!=null && condition.getTranStatusList().size()>0) {
					query.setParameterList("tranStatusList", condition.getTranStatusList());
				}
				if (StringUtils.isNotEmpty(condition.getIfResultPublish())) {
					query.setParameter("ifResultPublish", condition.getIfResultPublish());
				}
				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 查询NoticeId信息
	 * 
	 * @param noticeId
	 * @return
	 */
	public KyqNotice getNoticeByNoticeId(String noticeId) {
		List list = new ArrayList();
		StringBuffer hql = new StringBuffer("from KyqNotice a where a.noticeId = '").append(noticeId).append("'");
		list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (KyqNotice) list.get(0);
		} else {
			return null;
		}
	}

	public List queryMakedNoticeList(KyqBaseQueryCondition condition) {
		if (condition != null) {
			List list = new ArrayList();
			StringBuffer hql = new StringBuffer("from KyqNotice a where 1=1");
			if (StringUtils.isNotEmpty(condition.getNoticeNumber())) {
				hql.append(" and a.noticeNumber like :noticeNumber ");
			}
			if (condition.getTranStatusList()!=null && condition.getTranStatusList().size()>0) {
				hql.append(" and a.status in (:tranStatusList) ");
			}
			
			try {
				Query query = this.getSession().createQuery(hql.toString());
				if (StringUtils.isNotEmpty(condition.getNoticeNumber())) {
					query.setParameter("noticeNumber", "%" + (condition.getNoticeNumber()) + "%");
				}
				if (condition.getTranStatusList()!=null && condition.getTranStatusList().size()>0) {
					query.setParameterList("tranStatusList", condition.getTranStatusList());
				}

				list = (query.list());
			} catch (HibernateException ex) {
				throw new RuntimeException(ex);
			}
			return list;
		} else {
			return null;
		}
	}

	public List queryKyqAllPlanList() {
		StringBuffer hql = new StringBuffer("from KyqNotice a where 1=1");
		hql.append(" and a.status = '").append(KyqContent.KYQ_STATUS_MAKED_PLAN).append("' ");
		Query query = this.getSession().createQuery(hql.toString());
		return query.list();
	}
}
