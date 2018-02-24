package com.wonders.tdsc.kyq.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.MiningTranApp;

public class MiningTranAppDao extends BaseHibernateDaoImpl {
	protected Class getEntityClass() {
		return MiningTranApp.class;
	}

	/**
	 * 查询所有矿山信息
	 * 
	 * @param condition
	 * @return
	 */
	public List findMiningList(KyqBaseQueryCondition condition) {
		if (condition != null) {
			List list = new ArrayList();
			StringBuffer hql = new StringBuffer("from MiningTranApp a where 1=1");
			if (condition.getTranAppIds()!=null && condition.getTranAppIds().size()>0) {
				hql.append(" and a.tranAppId in (:tranAppIds) ");
			}
			if (StringUtils.isNotEmpty(condition.getMiningName())) {
				hql.append(" and a.miningName like :miningName ");
			}
			if (StringUtils.isNotEmpty(condition.getTransferMode())) {
				hql.append(" and a.transferMode like :transferMode ");
			}
			if (StringUtils.isNotEmpty(condition.getTranStatus())) {
				hql.append(" and a.tranStatus = :tranStatus ");
			}
			// 没有做过实施方案的地块才能被查询
			hql.append(" and a.noticeId is null ");
			
			if (StringUtils.isNotEmpty(condition.getOrderKey())) {
                hql.append(" order by a.").append(condition.getOrderKey()).append(" asc");
            } else {
                hql.append(" order by a.tranAppId asc");
            }
			try {
				Query query = this.getSession().createQuery(hql.toString());
				if (condition.getTranAppIds()!=null && condition.getTranAppIds().size()>0) {
					query.setParameterList("tranAppIds", condition.getTranAppIds());
				}
				if (StringUtils.isNotEmpty(condition.getMiningName())) {
					query.setParameter("miningName", "%" + (condition.getMiningName()) + "%");
				}
				if (StringUtils.isNotEmpty(condition.getTransferMode())) {
					query.setParameter("transferMode", "%" + (condition.getTransferMode()) + "%");
				}
				if (StringUtils.isNotEmpty(condition.getTranStatus())) {
					query.setParameter("tranStatus",(condition.getTranStatus()));
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
	 * 查询业务ID信息
	 * 
	 * @param tranAppId
	 * @return
	 */
	public MiningTranApp getMiningTranAppByTranAppId(String tranAppId) {
		List list = new ArrayList();
		StringBuffer hql = new StringBuffer("from MiningTranApp a where a.tranAppId = '").append(tranAppId).append("'");
		list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (MiningTranApp) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查询公告ID信息
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryMiningTranAppListByNoticeId(String noticeId) {
		List list = new ArrayList();
		StringBuffer hql = new StringBuffer("from MiningTranApp a where a.noticeId = '").append(noticeId).append("'");
		list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	public List queryMiningTranAppListByMiningId(String miningId) {
		List list = new ArrayList();
		StringBuffer hql = new StringBuffer("from MiningTranApp a where a.tranAppId = '").append(miningId).append("'");
		list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
}
