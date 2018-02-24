package com.wonders.tdsc.user.dao.impl;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.tdsc.user.dao.AuthorityResourceDao;
import com.wonders.tdsc.user.model.AuthorityResource;

public class AuthorityResourceDaoImpl extends BaseHibernateDaoImpl implements AuthorityResourceDao {

    public Class getEntityClass() {
        return AuthorityResource.class;
    }

    /**
     * ������Դ����ȡ����Դ�б�
     * 
     * @return List ��Դ�б�
     */
    public List findListByType(String type) {
        StringBuffer hql = new StringBuffer("from AuthorityResource as a where 1 = 1 ");

        if (null != type && !"".equals(type)) {
            hql.append(" and a.type = '").append(type).append("'");
        }

        hql.append(" order by a.resourceLevel, a.sortOrder");

        return this.findList(hql.toString());
    }

    /**
     * ȡ��һ��������ϵĲ˵������ò�Σ�
     * 
     * @param level
     *            �˵����
     * @return �˵��б�
     */
    public List findListByLevel(int level) {
        StringBuffer hql = new StringBuffer("from AuthorityResource as a ");

        hql.append(" where a.resourceLevel <= ").append(level);
        hql.append(" order by a.resourceLevel, a.sortOrder");

        return this.findList(hql.toString());
    }
}