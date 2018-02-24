package com.wonders.tdsc.kyq.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.kyq.bo.KyqBaseQueryCondition;
import com.wonders.tdsc.kyq.bo.MiningBidderApp;

public class MiningBidderAppDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return MiningBidderApp.class;
	}

	/**
	 * 查询竞买人姓名信息
	 * 
	 * @param condition
	 * @return
	 */
	public List findBidderList(KyqBaseQueryCondition condition) {
		if (condition != null) {
			List list = new ArrayList();
			StringBuffer hql = new StringBuffer("from MiningBidderApp a where 1=1");
			if (StringUtils.isNotEmpty(condition.getBidderName())) {
				hql.append(" and a.bidderName like :bidderName ");
			}
			try {
				Query query = this.getSession().createQuery(hql.toString());
				if (StringUtils.isNotEmpty(condition.getMiningName())) {
					query.setParameter("bidderName", "%" + (condition.getBidderName()) + "%");
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
	 * 查询矿山ID信息
	 * 
	 * @param miningId
	 * @return
	 */
	public MiningBidderApp getMiningBidderAppByMiningId(String miningId) {
		List list = new ArrayList();
		StringBuffer hql = new StringBuffer("from MiningBidderApp a where a.miningId = '").append(miningId).append("'");
		list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (MiningBidderApp) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查询竞买人ID信息
	 * 
	 * @param miningId
	 * @return
	 */
	public List queryMiningBidderAppListByBidderId(String bidderId) {
		List list = new ArrayList();
		StringBuffer hql = new StringBuffer("from MiningBidderApp a where a.bidderId = '").append(bidderId).append("'");
		list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据矿山Id查询竞买该矿的竞买人
	 * 
	 * @param miningId
	 * @return
	 */
	public List queryMiningBidderAppListByMiningId(String miningId) {
		List list = new ArrayList();
		StringBuffer hql = new StringBuffer("from MiningBidderApp a where a.miningId like '%").append(miningId).append("%'");
		list = findList(hql.toString());
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

}
