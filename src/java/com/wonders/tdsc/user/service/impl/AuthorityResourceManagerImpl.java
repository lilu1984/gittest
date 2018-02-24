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
     * ȡ����Դ�б�.
     * 
     * @return List ��Դ�б�
     */
    public List queryAllResource() {
        return this.resourceDao.findAll();
    }

    /**
     * ������Դ����ȡ����Դ�б�
     * 
     * @return List ��Դ�б�
     */
    public List queryResourceByType(String type) {
        return resourceDao.findListByType(type);
    }
    
    /**
     * ����һ���˵����룬ȡ�ö����������˵��б�
     * 
     * @param menuId
     *            һ���˵�����
     * @return �����������˵��б�
     */
    public Map querySubMenuList(String menuId) {
        List menuList = resourceDao.findListByLevel(AuthorityConstants.MENU_LEVEL_THIRD);
        return MenuUtil.getSubMenu(menuId, menuList);
    }
}