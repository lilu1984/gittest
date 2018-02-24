package com.wonders.tdsc.flowadapter.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscFlowPostStatus;
import com.wonders.tdsc.bo.condition.TdscFlowCondition;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.flowadapter.FlowConstants;

public class TdscFlowPostStatusDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscFlowPostStatus.class;
    }

    public List findStatusConfigList(TdscFlowCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscFlowPostStatus a where 1 = 1");

        // ��װ�������
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.resultId";

        return this.findList(queryHql.toString());
    }

    public TdscFlowPostStatus findStatusConfigInfo(TdscFlowCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscFlowPostStatus a where 1 = 1");

        // ��װ�������
        hql.append(makeWhereClause(condition));

        String queryHql = hql.toString() + " order by a.resultId";

        List result = this.findList(queryHql.toString());
        if (result != null && result.size() > 0) {
            return (TdscFlowPostStatus) result.get(0);
        } else {
            return null;
        }
    }
    /**
     * ���ݳ��÷�ʽ���ؽڵ��б�
     * 
     * @param condition
     * @return
     */
    public List findtransferNodeList(String transferMode) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("select a.nodeId from TdscFlowPostStatus a where 1 = 1");

        // ��װ�������
        if(StringUtils.isNotEmpty(transferMode)){
            hql.append(" and a.transferMode = '").append(transferMode).append("' ");
        }        
        String queryHql = hql.toString() + " group by a.nodeId  order by a.nodeId";

        return this.findList(queryHql.toString());
    }
    
    /**
     * �����û���ѯ����������װ�������
     * 
     * @param condition
     *            �û���ѯ��������
     * @return String �������
     */
    private String makeWhereClause(TdscFlowCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getNodeId())) {
            whereClause.append(" and a.nodeId = '").append(condition.getNodeId()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getTransferMode())) {
            whereClause.append(" and a.transferMode in ('00',DECODE('").append(condition.getTransferMode()).append("','").append(GlobalConstants.DIC_TRANSFER_TENDER).append("','").append(FlowConstants.FLOW_TRANSFER_TENDER).append("','").append(GlobalConstants.DIC_TRANSFER_AUCTION).append("','").append(FlowConstants.FLOW_TRANSFER_AUCTION).append("','").append(GlobalConstants.DIC_TRANSFER_LISTING).append("','").append(FlowConstants.FLOW_TRANSFER_LISTING).append("')) ");
        }
        
        if (StringUtils.isNotEmpty(condition.getActionId())) {
            whereClause.append(" and a.actionId = '").append(condition.getActionId()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getResultId())) {
            whereClause.append(" and a.resultId = '").append(condition.getResultId()).append("' ");
        }
        
        if (StringUtils.isNotEmpty(condition.getIsDefaultResult())) {
            whereClause.append(" and a.isDefaultResult = '").append(condition.getIsDefaultResult()).append("' ");
        }

        return whereClause.toString();
    }
}