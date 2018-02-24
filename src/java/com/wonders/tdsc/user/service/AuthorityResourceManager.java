package com.wonders.tdsc.user.service;

import java.util.List;
import java.util.Map;

import com.wonders.esframework.common.service.BaseSpringManager;

public interface AuthorityResourceManager extends BaseSpringManager {

    /**
     * 取得资源列表.
     * 
     * @return List 资源列表
     */
    public List queryAllResource();

    /**
     * 根据资源类型取得资源列表
     * 
     * @return List 资源列表
     */
    public List queryResourceByType(String type);

    /**
     * 根据一级菜单编码，取得二级、三级菜单列表
     * 
     * @param menuId
     *            一级菜单编码
     * @return 二级、三级菜单列表
     */
    public Map querySubMenuList(String menuId);

}