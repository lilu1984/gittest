package com.wonders.common.authority.dao;

import java.util.List;

import com.wonders.common.authority.bo.AuthorityTree;
import com.wonders.common.authority.util.ArrayUtil;
import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;

/**
 * �˵���Դ�־ò����
 * 
 * �������ڣ�2007-1-8 13:05:49
 */
public class AuthorityTreeDao extends BaseHibernateDaoImpl {

    /**
     * @see com.wonders.esframework.common.dao.BaseHibernateDaoImpl#getEntityClass()
     */
    protected Class getEntityClass() {
        return AuthorityTree.class;
    }

    /**
     * ȡ��һ��������ϵĲ˵������ò�Σ�
     * 
     * @param level
     *            �˵����
     * @return �˵��б�
     */
    public List findListByLevel(int level) {
        StringBuffer hql = new StringBuffer("from AuthorityTree ");

        hql.append(" where nodeLevel <= ").append(level);
        hql.append(" order by nodeLevel, nodeOrder");

        return this.findList(hql.toString());
    }
    
    public List findListByAuthorities(String[] authorities) {
        return findList("from AuthorityTree a where a.nodeId in (" + ArrayUtil.strArray2StrComma(authorities) + ")");
    }
}
