package com.wonders.tdsc.publishinfo.dao;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscReplyConfInfo;
import com.wonders.tdsc.bo.condition.TdscReplyConfInfoCondition;;

public class TdscReplyRecordDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscReplyConfInfo.class;
    }

    public PageList findPageList(TdscReplyConfInfoCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from TdscReplyConfInfo a where 1 = 1");

        // ��װ�������
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.appId";
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
        
        return pageList;
    }

    private String makeWhereClause(TdscReplyConfInfoCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getIfPublish())) {
            //��ѯ����Ϊ���ʱ��
            if("0".equals(condition.getIfPublish())){
                whereClause.append(" and a.ifPublish is null or a.ifPublish like '0'");
            }else{
                whereClause.append(" and a.ifPublish = '").append(condition.getIfPublish()).append("' "); 
            }
        }

        return whereClause.toString();
    }
}
