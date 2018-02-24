package com.wonders.common.authority.service;

import java.util.List;
import java.util.Map;

import com.wonders.common.authority.dao.AuthorityTreeDao;
import com.wonders.common.authority.util.MenuUtil;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.common.AuthorityConstants;

/**
 * 权限管理业务层对象
 * 
 * 创建日期：2007-1-8 13:15:16
 */
public class AuthorityManager extends BaseSpringManagerImpl {

    private AuthorityTreeDao authorityTreeDao;

    public void setAuthorityTreeDao(AuthorityTreeDao authorityTreeDao) {
        this.authorityTreeDao = authorityTreeDao;
    }

    /**
     * 取得菜单权限列表，包括权限编号、菜单信息。赋菜单权限时使用
     * 
     * @return List 菜单权限列表
     */
    public List queryMenuAuthorityList() {
        return authorityTreeDao.findAll();
    }

    /**
     * 取得一级菜单列表
     * 
     * @return 一级菜单列表
     */
    public List queryMainMenuList() {
        return authorityTreeDao.findListByLevel(AuthorityConstants.MENU_LEVEL_FIRST);
    }

    /**
     * 根据一级菜单编码，取得二级、三级菜单列表
     * 
     * @param menuId
     *            一级菜单编码
     * @return 二级、三级菜单列表
     */
    public Map querySubMenuList(String menuId) {
        List menuList = authorityTreeDao.findListByLevel(AuthorityConstants.MENU_LEVEL_THIRD);
        return MenuUtil.getSubMenu(menuId, menuList);
    }

}