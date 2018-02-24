package com.wonders.tdsc.bidder.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscBusinessRecord;
import com.wonders.tdsc.bo.condition.TdscBusinessRecordCondition;

public class TdscBusinessRecordDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscBusinessRecord.class;
    }

    /**
     * ͨ��userId���һ������ҵ���¼
     * 
     * @param username
     * @return
     */
    public TdscBusinessRecord getTdscBusinessRecordByRecordId(String recordId, String uname) {
        StringBuffer hql = new StringBuffer("from TdscBusinessRecord a where a.recordId = '").append(recordId).append(
                "'");
        hql.append(" and a.userId = '").append(uname).append("'");
        List list = findList(hql.toString());
        if (list != null && list.size() > 0) {
            TdscBusinessRecord tdscBusinessRecord = (TdscBusinessRecord) list.get(0);
            return tdscBusinessRecord;
        } else {
            return null;
        }
    }

    public TdscBusinessRecord getTdscBusinessRecordByCondition(TdscBusinessRecordCondition condition) {
        StringBuffer hql = new StringBuffer("from TdscBusinessRecord a where 1 = 1");

        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getBusiType())) {
            hql.append(" and a.busiType = '").append(condition.getBusiType()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getBusiId())) {
            hql.append(" and a.busiId = '").append(condition.getBusiId()).append("' ");
        }

        if (StringUtils.isNotEmpty(condition.getUserId())) {
            hql.append(" and a.userId = '").append(condition.getUserId()).append("' ");
        }
        
        //��ѯ���
        List recordList = this.findList(hql.toString());

        return (recordList != null && recordList.size() > 0) ? (TdscBusinessRecord) recordList.get(0) : null;
    }
    
 /*   public void updateProvideCount(TdscBusinessRecordCondition condition) {
        TdscBusinessRecord record = this.getTdscBusinessRecordByCondition(condition);

        if (record == null) {
            // ��������ҵ���¼���һ����¼���������ŷ�����Ϊ��
            record = new TdscBusinessRecord();
            record.setProvideCount(new Integer(1));
            record.setBusiType(condition.getBusiType());
            record.setBusiId(condition.getBusiId());
            record.setUserId(condition.getUserId());
            save(record);
        } else {
            // ���´���ҵ���¼�����ŷ���
            Integer provideCount = record.getProvideCount();

            if (provideCount == null || provideCount.intValue() == 0)
                record.setProvideCount(new Integer(1));
            else
                record.setProvideCount(new Integer(record.getProvideCount().intValue() + 1));
            update(record);
        }
    }
    */
}
