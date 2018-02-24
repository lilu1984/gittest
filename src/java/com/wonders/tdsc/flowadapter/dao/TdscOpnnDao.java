package com.wonders.tdsc.flowadapter.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscOpnnCondition;

public class TdscOpnnDao extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscOpnn.class;
	}

	public BigDecimal getNextActionNum(String appId) {
		String maxHql = "select max(a.actionNum) from TdscOpnn a where a.appId = '" + appId + "' ";

		BigDecimal resultMax = new BigDecimal(1);
		List result = this.findList(maxHql.toString());
		if (result != null && result.size() > 0) {
			BigDecimal tempNum = (BigDecimal) result.get(0);
			resultMax = new BigDecimal(tempNum.intValue() + 1);
		}
		return resultMax;
	}
	
	public List findOpnnListByNoticeId(String noticeId) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscOpnn a where a.isOpt ='00' and a.appId in(select b.appId from TdscBlockTranApp b where b.noticeId='"+noticeId+"' )");
		return this.findList(hql.toString());
	}

	public List findFlowOpnnList(TdscOpnnCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscOpnn a where 1 = 1 and (a.actionUserId is null or a.actionUserId ='").append(condition.getActionUser())
				.append("') and a.isOpt='").append(condition.getIfOpt()).append("' and a.actionId in (").append(condition.getNodeId()).append(") ");
		String queryHql = hql.toString() + " order by a.actionDate";
		return this.findList(queryHql.toString());
	}

	public List findOpnnList(TdscOpnnCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscOpnn a where 1 = 1");

		// ��װ�������
		hql.append(makeWhereClause(condition));

		String queryHql = hql.toString() + " order by a.actionDate";

		return this.findList(queryHql.toString());
	}

	public TdscOpnn findOpnnInfo(TdscOpnnCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from TdscOpnn a where 1 = 1");

		// ��װ�������
		hql.append(makeWhereClause(condition));

		String queryHql = hql.toString() + " order by a.actionDate";

		List result = this.findList(queryHql.toString());
		if (result != null && result.size() > 0) {
			return (TdscOpnn) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * �����û���ѯ����������װ�������
	 * 
	 * @param condition
	 *            �û���ѯ��������
	 * @return String �������
	 */
	private String makeWhereClause(TdscOpnnCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// ��װ�������
		if (StringUtils.isNotEmpty(condition.getAppId())) {
			whereClause.append(" and a.appId = '").append(condition.getAppId().trim()).append("' ");
		}

		if (condition.getActionNum() != null) {
			whereClause.append(" and a.actionNum = ").append(condition.getActionNum()).append(" ");
		}

		if (StringUtils.isNotEmpty(condition.getActionOrgan())) {
			whereClause.append(" and a.actionOrgan = '").append(condition.getActionOrgan().trim()).append("' ");
		}

		if (StringUtils.isNotEmpty(condition.getActionUser())) {
			whereClause.append(" and a.actionUser like '%").append(condition.getActionUser().trim()).append("%' ");
		}

		if (StringUtils.isNotEmpty(condition.getNodeId())) {
			whereClause.append(" and a.actionId like '%").append(condition.getNodeId().trim()).append("%' ");
		}

		if (StringUtils.isNotEmpty(condition.getIfOpt())) {
			whereClause.append(" and a.isOpt = '").append(condition.getIfOpt().trim()).append("' ");
		}
		if (condition.getIsnull()) {
			whereClause.append(" and a.actionNum is null");
		}

		return whereClause.toString();
	}

	public boolean deleteByAppId(String strAppId) {
		StringBuffer hql = new StringBuffer("delete TdscOpnn a where 1 = 1");

		// ��װ�������
		hql.append(" and appId = '").append(strAppId).append("'");

		this.findList(hql.toString());
		return true;

	}
}