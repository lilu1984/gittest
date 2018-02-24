package com.wonders.wsjy.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscBidderApp;
import com.wonders.wsjy.bo.SubscribeInfo;

public class SubscribeDAO  extends BaseHibernateDaoImpl {

	public List querySubscribeListByUserId(String userId) {
		if (StringUtils.isEmpty(userId))
			return null;
		//Criteria criteria = getSession().createCriteria(TdscBidderApp.class);
		//criteria.add(Expression.eq("userId", userId));
		//criteria.add(Expression.isNotNull("sqNumber"));
		//criteria.addOrder(Order.desc("acceptDate"));
		//return criteria.list();
		String hql = "select a from TdscBidderApp a,TdscBlockTranApp b,TdscNoticeApp c where a.appId=b.appId and b.noticeId=c.noticeId and a.sqNumber is not null and a.userId='"+userId+"' order by c.noticeDate desc,b.blockNoticeNo asc";
		List list =this.findList(hql);
		return list;
	}

	/**
	 * 更新我的申购信息
	 * 
	 * @param userId
	 * @param appId
	 */
	public TdscBidderApp persistentSubscribe(TdscBidderApp bidderApp) {
		return (TdscBidderApp)saveOrUpdate(bidderApp);
	}

	/**
	 * 根据用户编号以及地块编号取得对应的申购信息是否存在
	 * 
	 * @param userId
	 * @param appId
	 * @return
	 */
	public TdscBidderApp getSubscribeInfoByUserIdAndAppId(String userId, String appId) {
		if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(appId))
			return null;
		Criteria criteria = getSession().createCriteria(TdscBidderApp.class);
		criteria.add(Expression.eq("userId", userId));
		criteria.add(Expression.eq("appId", appId));
		TdscBidderApp info = (TdscBidderApp)criteria.uniqueResult();
		return info;
	}
	
	protected Class getEntityClass() {
		return TdscBidderApp.class;
	}

	public TdscBidderApp getSubscribeInfoByBankNumber(String bankNumber) {
		Criteria criteria = getSession().createCriteria(TdscBidderApp.class);
		criteria.add(Expression.eq("bankNumber", bankNumber));
		return (TdscBidderApp)criteria.uniqueResult();
	}

}
