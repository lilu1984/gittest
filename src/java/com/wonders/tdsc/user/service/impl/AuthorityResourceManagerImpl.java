package com.wonders.tdsc.user.service.impl;

import java.util.List;
import java.util.Map;

import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.common.AuthorityConstants;
import com.wonders.tdsc.user.dao.AuthorityResourceDao;
import com.wonders.tdsc.user.service.AuthorityResourceManager;
import com.wonders.tdsc.user.util.MenuUtil;

public class AuthorityResourceManagerImpl extends BaseSpringManagerImpl implements AuthorityResourceManager {
    private AuthorityResourceDao resourceDao;

    public AuthorityResourceDao getResourceDao() {
        return resourceDao;
    }

    public void setResourceDao(AuthorityResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    /**
     * 取得资源列表.
     * 
     * @return List 资源列表
     */
    public List queryAllResource() {
        return this.resourceDao.findAll();
    }

    /**
     * 根据资源类型取得资源列表
     * 
     * @return List 资源列表
     */
    public List queryResourceByType(String type) {
        return resourceDao.findListByType(type);
    }
    
    /**
     * 根据一级菜单编码，取得二级、三级菜单列表
     * 
     * @param menuId
     *            一级菜单编码
     * @return 二级、三级菜单列表
     */
    public Map querySubMenuList(String menuId) {
        List menuList = resourceDao.findListByLevel(AuthorityConstants.MENU_LEVEL_THIRD);
        return MenuUtil.getSubMenu(menuId, menuList);
    }
}