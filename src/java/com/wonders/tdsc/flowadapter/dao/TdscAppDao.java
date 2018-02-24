package com.wonders.tdsc.flowadapter.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscApp;
import com.wonders.tdsc.bo.TdscOpnn;
import com.wonders.tdsc.bo.condition.TdscAppCondition;

public class TdscAppDao  extends BaseHibernateDaoImpl {

	protected Class getEntityClass() {
		return TdscApp.class;
	}
	 public List findAppListByNoticeId(String noticeId) {
	        // 用户信息查询
	        StringBuffer hql = new StringBuffer("from TdscApp a where a.tdscAppId in(select b.appId from TdscBlockTranApp b where b.noticeId='"+noticeId+"' )");
	        return this.findList(hql.toString());
	}
    public List findAppList(TdscAppCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscApp a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.appDate";

        return this.findList(queryHql.toString());
    }

    public TdscOpnn findOpnnInfo(TdscAppCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscApp a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.appDate";

        List result = this.findList(queryHql.toString());
        if (result != null && result.size() > 0) {
            return (TdscOpnn) result.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据用户查询条件对象组装条件语句
     * 
     * @param condition
     *            用户查询条件对象
     * @return String 条件语句
     */
    private String makeWhereClause(TdscAppCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // 组装条件语句
        if (StringUtils.isNotEmpty(condition.getTdscAppId())) {
            whereClause.append(" and a.tdscAppId = '").append(condition.getTdscAppId().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getAppType())) {
            whereClause.append(" and a.appType = '").append(condition.getAppType().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getTransferMode())) {
            whereClause.append(" and a.transferMode like '%").append(condition.getTransferMode().trim()).append("%' ");
        }

        if (StringUtils.isNotEmpty(condition.getAppOrgan())) {
            whereClause.append(" and a.appOrgan = '").append(condition.getAppOrgan().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getAppPersonId())) {
            whereClause.append(" and a.appPersonId = '").append(condition.getAppPersonId().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getNodeId())) {
            whereClause.append(" and a.nodeId = '").append(condition.getNodeId().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getStatusId())) {
            whereClause.append(" and a.statusId = '").append(condition.getStatusId().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getAppResult())) {
            whereClause.append(" and a.appResult = '").append(condition.getAppResult().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getLastActnOrgan())) {
            whereClause.append(" and a.lastActnOrgan = '").append(condition.getLastActnOrgan().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getLastActnUserId())) {
            whereClause.append(" and a.lastActnUserId = '").append(condition.getLastActnUserId().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getCurrActnOrgan())) {
            whereClause.append(" and a.currActnOrgan = '").append(condition.getCurrActnOrgan().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getCurrActnUserId())) {
            whereClause.append(" and a.currActnUserId = '").append(condition.getCurrActnUserId().trim()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getAssignUserId())) {
            whereClause.append(" and a.assignUserId = '").append(condition.getAssignUserId().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(condition.getPushMonitorEnd())) {
            whereClause.append(" and a.pushMonitorEnd = '").append(condition.getPushMonitorEnd().trim()).append("' ");
        }
        return whereClause.toString();
    }
}
