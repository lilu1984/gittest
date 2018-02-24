package com.wonders.common.authority.service;

import java.util.List;
import java.util.Map;

import com.wonders.common.authority.dao.AuthorityTreeDao;
import com.wonders.common.authority.util.MenuUtil;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.tdsc.common.AuthorityConstants;

/**
 * Ȩ�޹���ҵ������
 * 
 * �������ڣ�2007-1-8 13:15:16
 */
public class AuthorityManager extends BaseSpringManagerImpl {

    private AuthorityTreeDao authorityTreeDao;

    public void setAuthorityTreeDao(AuthorityTreeDao authorityTreeDao) {
        this.authorityTreeDao = authorityTreeDao;
    }

    /**
     * ȡ�ò˵�Ȩ���б�����Ȩ�ޱ�š��˵���Ϣ�����˵�Ȩ��ʱʹ��
     * 
     * @return List �˵�Ȩ���б�
     */
    public List queryMenuAuthorityList() {
        return authorityTreeDao.findAll();
    }

    /**
     * ȡ��һ���˵��б�
     * 
     * @return һ���˵��б�
     */
    public List queryMainMenuList() {
        return authorityTreeDao.findListByLevel(AuthorityConstants.MENU_LEVEL_FIRST);
    }

    /**
     * ����һ���˵����룬ȡ�ö����������˵��б�
     * 
     * @param menuId
     *            һ���˵�����
     * @return �����������˵��б�
     */
    public Map querySubMenuList(String menuId) {
        List menuList = authorityTreeDao.findListByLevel(AuthorityConstants.MENU_LEVEL_THIRD);
        return MenuUtil.getSubMenu(menuId, menuList);
    }

}