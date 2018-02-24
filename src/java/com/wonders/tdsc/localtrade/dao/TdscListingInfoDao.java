package com.wonders.tdsc.localtrade.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.bo.TdscListingInfo;
import com.wonders.tdsc.bo.condition.TdscListingInfoCondition;

public class TdscListingInfoDao extends BaseHibernateDaoImpl {

    public Class getEntityClass() {
        return TdscListingInfo.class;
    }

    public String findListingId(String appId) {
        StringBuffer hql = new StringBuffer("select a.listingId from TdscListingInfo a where a.appId ='").append(appId)
                .append("'");
        List list = findList(hql.toString());
        if (null != list && list.size() > 0) {
            return (String) list.get(0);
        } else {
            return null;
        }
    }

    public TdscListingInfo findListingIdByAppIdAndYktXh(String appId, String yktXh) {
        StringBuffer hql = new StringBuffer("from TdscListingInfo a where a.appId ='").append(appId).append("'")
                .append("and a.yktXh='").append(yktXh).append("'");
        List list = findList(hql.toString());
        if (null != list && list.size() > 0) {
            return (TdscListingInfo) list.get(0);
        } else {
            return null;
        }
    }

    public TdscListingInfo getTdscListingInfoByAppId(String appId) {
        StringBuffer hql = new StringBuffer("from TdscListingInfo a where a.appId='").append(appId).append("'");
        List list = findList(hql.toString());
        if (null != list && list.size() > 0) {
            return (TdscListingInfo) list.get(0);
        } else {
            return null;
        }
    }

    // 通过appList查询list列表
    public List findAppList(TdscListingInfoCondition condition) {
        if (condition != null) {
            List list = new ArrayList();
            StringBuffer hql = new StringBuffer("from TdscListingInfo a where 1=1");
            if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0)
                hql.append(" and a.appId in (:appIdList) ");
            hql.append(" order by a.listingId asc");

            try {
                Query query = this.getSession().createQuery(hql.toString());
                if (condition.getAppIdList() != null && condition.getAppIdList().size() > 0) {
                    query.setParameterList("appIdList", condition.getAppIdList());
                    list = query.list();
                }
            } catch (HibernateException ex) {
                throw new RuntimeException(ex);
            }
            return list;
        } else
            return null;
    }

    public List findListingInfo(String appId) {
        StringBuffer hql = new StringBuffer("from TdscListingInfo a where a.appId ='").append(appId).append("'");
        List list = findList(hql.toString());
        if (null != list && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }
}
