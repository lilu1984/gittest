package com.wonders.tdsc.flowadapter.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscAppNodeStat;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.FlowConstants;

public class TdscAppNodeStatDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscAppNodeStat.class;
	}

	public List findAppNodeInfoListByNoticeId(String noticeId) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscAppNodeStat a where a.nodeId in('16','17','18') and a.appId in(select b.appId from TdscBlockTranApp b where b.noticeId='"+noticeId+"' )");

		return this.findList(hql.toString());
	}

	public TdscAppNodeStat findAppNodeInfo(TdscFlowCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscAppNodeStat a where 1 = 1");

		// 组装条件语句
		hql.append(makeWhereClause(condition));

		String queryHql = hql.toString() + " order by a.id";

		List result = this.findList(queryHql.toString());
		if (result != null && result.size() > 0) {
			return (TdscAppNodeStat) result.get(0);
		} else {
			return null;
		}
	}

	public TdscAppNodeStat findAppNodeStatInfo(String flowId, String nodeId, String appId) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscAppNodeStat a where 1 = 1");

		// 组装条件语句
		if (StringUtils.isNotEmpty(flowId))
			hql.append(" and a.flowId = '").append(flowId).append("' ");

		if (StringUtils.isNotEmpty(nodeId))
			hql.append(" and a.nodeId = '").append(nodeId).append("' ");

		if (StringUtils.isNotEmpty(appId))
			hql.append(" and a.appId = '").append(appId).append("' ");

		String queryHql = hql.toString() + " order by a.id";

		List result = this.findList(queryHql.toString());
		if (result != null && result.size() > 0) {
			return (TdscAppNodeStat) result.get(0);
		} else {
			return null;
		}
	}

	public List findNodeList(TdscFlowCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscAppNodeStat a where 1 = 1");

		// 组装条件语句
		hql.append(makeWhereClause(condition));

		String queryHql = hql.toString() + " order by a.nodeId";

		return this.findList(queryHql.toString());
	}

	/**
	 * 获得活动中的节点列表
	 * 
	 * @author weedlu
	 * @since 2008-06-16
	 * @param condition
	 * @return
	 */
	public List findActiveNodeList(TdscFlowCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from TdscAppNodeStat a where a.nodeStat = '01' ");

		// 组装条件语句
		hql.append(makeWhereClause(condition));

		String queryHql = hql.toString() + " order by a.nodeId";

		return this.findList(queryHql.toString());
	}

	public List findAppList(TdscFlowCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("select a.appId from TdscAppNodeStat a ");
		StringBuffer whereHql = new StringBuffer("where 1 = 1 ");

		if (StringUtils.isNotEmpty(condition.getPlusCondition())) {
			hql.append(", TdscBlockPlanTable b, TdscBlockTranApp e ");
			whereHql.append(" and a.appId=e.appId and b.planId = e.planId and b.status='01' ");
		}
		if (StringUtils.isNotEmpty(condition.getStatusId())) {
			hql.append(", TdscApp c ");
			whereHql.append(" and a.appId=c.tdscAppId ");
		}
		// 挂牌、招标出让方式地块查询特殊组合
		if (FlowConstants.FLOW_NODE_BIDDER_FUND_TENDER.equals(condition.getNodeId()) || FlowConstants.FLOW_NODE_BIDDER_FUND_LISTING.equals(condition.getNodeId())) {
			hql.append(", TdscBlockTranApp d ");
			whereHql.append(" and a.appId=d.appId ");
		}
		hql.append(whereHql);

		// 组装条件语句
		hql.append(makeWhereClause(condition));

		String queryHql = hql.toString() + " order by a.id";

		return findList(queryHql.toString());
	}

	/**
	 * 根据用户查询条件对象组装条件语句
	 * 
	 * @param condition
	 *            用户查询条件对象
	 * @return String 条件语句
	 */
	private String makeWhereClause(TdscFlowCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// 组装条件语句
		if (StringUtils.isNotEmpty(condition.getAppId())) {
			whereClause.append(" and a.appId = '").append(condition.getAppId()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getFlowId())) {
			whereClause.append(" and a.flowId = '").append(condition.getFlowId()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getNodeId())) {
			whereClause.append(" and a.nodeId = '").append(condition.getNodeId()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getEndlessTag()) && GlobalConstants.QUERY_ENDLESS_TAG.equals(condition.getEndlessTag())) {
			if (StringUtils.isNotEmpty(condition.getNodeStat())) {
				whereClause.append(" and a.nodeStat in ('").append(condition.getNodeStat()).append("','").append(FlowConstants.STAT_END).append("') ");
			} else {
				whereClause.append(" and a.nodeStat = '").append(FlowConstants.STAT_END).append("' ");
			}
		} else if (StringUtils.isNotEmpty(condition.getNodeStat())) {
			whereClause.append(" and a.nodeStat = '").append(condition.getNodeStat()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getStartDate())) {
			whereClause.append(" and a.startDate > to_date('").append(condition.getStartDate()).append("','YYYY-MM-DD') ");
		}

		if (StringUtils.isNotEmpty(condition.getEndDate())) {
			whereClause.append(" and a.endDate < to_date('").append(condition.getEndDate()).append("','YYYY-MM-DD') ");
		}

		if (StringUtils.isNotEmpty(condition.getHasSubFlow())) {
			whereClause.append(" and a.hasSubFlow = '").append(condition.getHasSubFlow()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getSubFlowId())) {
			whereClause.append(" and a.subFlowId = '").append(condition.getSubFlowId()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getPlusCondition())) {
			String plusCondition = condition.getPlusCondition();
			plusCondition = plusCondition.replaceAll("TdscBlockPlanTable", "b");
			whereClause.append(" and ").append(plusCondition).append(" ");
		}

		if (StringUtils.isNotEmpty(condition.getStatusId())) {
			whereClause.append(" and c.statusId= '").append(condition.getStatusId()).append("' ");
		}

		return whereClause.toString();
	}
}