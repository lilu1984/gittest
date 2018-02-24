package com.wonders.tdsc.flowadapter.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscFlowConfig;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.FlowConstants;

public class TdscFlowConfigDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscFlowConfig.class;
    }

    public List findFlowConfigList(TdscFlowCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscFlowConfig a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.nodeId";

        return this.findList(queryHql.toString());
    }

    public TdscFlowConfig findFlowConfigInfo(TdscFlowCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscFlowConfig a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.nodeId";

        List result = this.findList(queryHql.toString());
        if (result != null && result.size() > 0) {
            return (TdscFlowConfig) result.get(0);
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
    private String makeWhereClause(TdscFlowCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // 组装条件语句
        if (StringUtils.isNotEmpty(condition.getFlowId())) {
            whereClause.append(" and a.flowId = '").append(condition.getFlowId()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getNodeId())) {
            whereClause.append(" and a.nodeId = '").append(condition.getNodeId()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getTransferMode())) {
            whereClause.append(" and a.transferMode in ('00',DECODE('").append(condition.getTransferMode()).append("','").append(GlobalConstants.DIC_TRANSFER_TENDER).append("','").append(FlowConstants.FLOW_TRANSFER_TENDER).append("','").append(GlobalConstants.DIC_TRANSFER_AUCTION).append("','").append(FlowConstants.FLOW_TRANSFER_AUCTION).append("','").append(GlobalConstants.DIC_TRANSFER_LISTING).append("','").append(FlowConstants.FLOW_TRANSFER_LISTING).append("')) ");
        }
        
        if (StringUtils.isNotEmpty(condition.getIsStartNode())) {
            whereClause.append(" and a.isStartNode = '").append(condition.getIsStartNode()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getPostNode())) {
            whereClause.append(" and a.postNode = '").append(condition.getPostNode()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getNodeInitType())) {
            whereClause.append(" and a.nodeInitType = '").append(condition.getNodeInitType()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getCloseType())) {
            whereClause.append(" and a.closeType = '").append(condition.getCloseType()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getHasSubFlow())) {
            whereClause.append(" and a.hasSubFlow = '").append(condition.getHasSubFlow()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getSubFlowStandId())) {
            whereClause.append(" and a.subFlowStandId = '").append(condition.getSubFlowStandId()).append("' ");
        }

        return whereClause.toString();
    }
}