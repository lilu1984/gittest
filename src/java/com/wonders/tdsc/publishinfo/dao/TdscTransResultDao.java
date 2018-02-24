package com.wonders.tdsc.publishinfo.dao;

import org.apache.commons.lang.StringUtils;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.bo.TdscReplyConfInfo;
import com.wonders.tdsc.bo.condition.TdscReplyConfInfoCondition;;

public class TdscTransResultDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TdscReplyConfInfo.class;
    }

    public PageList findPageList(TdscReplyConfInfoCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from TdscNoticeApp a where 1 = 1");

        // 组装条件语句
        hql.append(makeWhereClause(condition));

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by a.noticeDate";
        PageList pageList = findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());

        return pageList;
    }

    private String makeWhereClause(TdscReplyConfInfoCondition condition) {
        StringBuffer whereClause = new StringBuffer();

        // 组装条件语句
        if (StringUtils.isNotEmpty(condition.getAppId())){
            whereClause.append(" and a.appId like '%").append(condition.getAppId().trim()).append("%' ");
        }

        if (StringUtils.isNotEmpty(condition.getIfPublish())) {
            whereClause.append(" and a.ifPublish = '").append(condition.getIfPublish()).append("' ");
        }

        return whereClause.toString();
    }
}
