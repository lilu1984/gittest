package com.wonders.tdsc.user.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDao;

/**
 * ��Դ�ӿ�
 */

public interface AuthorityResourceDao extends BaseHibernateDao {
    /**
     * ������Դ����ȡ����Դ�б�
     * 
     * @return List ��Դ�б�
     */
    public List findListByType(String type);
    
    /**
     * ȡ��һ��������ϵĲ˵������ò�Σ�
     * 
     * @param level
     *            �˵����
     * @return �˵��б�
     */
    public List findListByLevel(int level);
}
