package com.wonders.wsjy.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.util.DateUtil;
import com.wonders.wsjy.bo.TdscTradeView;

public class TdscTradeViewDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscTradeView.class;
	}
	
	/**
	 * 根据appId查询TdscTradeView对象
	 * @param appId 申请主键
	 * @return
	 */
	public TdscTradeView getTdscTradeViewAppById(String appId) {
		StringBuffer hql = new StringBuffer("from TdscTradeView a where a.appId = '").append(appId).append("' order by a.xuHao ");
		List list = this.findList(hql.toString());
		if (list != null && list.size() > 0) {
			return (TdscTradeView) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据noticeId查询TdscTradeView对象
	 * @param appId 申请主键
	 * @return
	 */
	public List getTdscTradeViewAppByNoticeId(String noticeId) {
		StringBuffer hql = new StringBuffer("from TdscTradeView a where a.noticeId = '").append(noticeId).append("'order by a.xuHao ");
		return this.findList(hql.toString());
	}
	
	public List queryBlockViewListTheEnd(String noticeId) {
		StringBuffer hql = new StringBuffer("from TdscTradeView a where a.noticeId = '").append(noticeId).append("' and a.tradeStatus = '0' ").append("order by a.xuHao ");
		return this.findList(hql.toString());
	}
	
	/**
	 * 根据appId查询正在挂牌的地块信息
	 * @param appId 申请主键
	 * @return
	 */
	public List findGuapaiBlockByAppIds(String appIds) {
		if(StringUtils.isBlank(appIds)){
			return null;
		}
		Date nowDate = new Date();
		StringBuffer hql = new StringBuffer("from TdscTradeView a where a.listEndDate > " + getNowTime(nowDate) + " and a.listStartDate <  " + getNowTime(nowDate) + " and a.appId in ('").append(appIds.replaceAll(",", "','")).append("') order by a.xuHao ");
		return this.findList(hql.toString());
	}
	
	private String getNowTime(Date nowDate) {
		String getNowDateStr = "to_date('" + DateUtil.date2String(nowDate, "yyyy-MM-dd HH:mm:ss")+ "','yyyy-MM-dd HH24:MI:SS')";
		return getNowDateStr;
	}
	
	/**
	 * 根据planId查询TdscTradeView对象
	 * @param planId plan主键
	 * @return
	 */
	public List getTdscTradeViewAppByPlanId(String planId) {
		StringBuffer hql = new StringBuffer("from TdscTradeView a where a.planId = '").append(planId).append("' order by a.xuHao asc");
		return this.findList(hql.toString());
	}
	
	public List queryMyTradeNoticeBlock(List planIds, List appIds) {
		 Criteria criteria = this.getSession().createCriteria(TdscTradeView.class);
		 if (planIds != null && planIds.size() > 0) {
	     	criteria.add(Expression.in("planId", planIds));
	     }
		 if (appIds != null && appIds.size() > 0) {
			 criteria.add(Expression.in("appId", appIds));
	     }
		 List result = criteria.list();
		 return result;
	}

	/**
	 * 查找待确认底价的地块列表
	 * 
	 * 状态包含 11， 22
	 * 
	 * @param noticeId
	 * @return
	 */
	public List queryConfirmBlockList(String noticeId) {
		Criteria criteria = getSession().createCriteria(TdscTradeView.class);
		criteria.add(Expression.eq("noticeId", noticeId));
		criteria.add(Expression.in("tradeStatus", new String[]{"11","22","33"}));
		List blockList = criteria.list();
		return blockList;
	}

	public List queryMonitorPlanList() {
		Date date  = new Date();
		StringBuffer hql = new StringBuffer("select a from TdscTradeView a,TdscNoticeApp b where a.noticeId=b.noticeId and b.ifReleased='1' and a.accAppStatDate <=" + getNowTime(date) + " and a.listEndDate+2>="+getNowTime(date));
		hql.append(" order by a.accAppStatDate desc");
		List list = this.findList(hql.toString());
		return list;
	}
}
