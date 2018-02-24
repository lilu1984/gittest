package com.wonders.tdsc.user.dao;

import java.util.List;

import com.wonders.esframework.common.dao.BaseHibernateDao;

/**
 * 资源接口
 */

public interface AuthorityResourceDao extends BaseHibernateDao {
    /**
     * 根据资源类型取得资源列表
     * 
     * @return List 资源列表
     */
    public List findListByType(String type);
    
    /**
     * 取得一定层次以上的菜单（含该层次）
     * 
     * @param level
     *            菜单层次
     * @return 菜单列表
     */
    public List findListByLevel(int level);
}
