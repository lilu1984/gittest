package com.wonders.tdsc.user.service;

import java.util.List;
import java.util.Map;

import com.wonders.esframework.common.service.BaseSpringManager;

public interface AuthorityResourceManager extends BaseSpringManager {

    /**
     * ȡ����Դ�б�.
     * 
     * @return List ��Դ�б�
     */
    public List queryAllResource();

    /**
     * ������Դ����ȡ����Դ�б�
     * 
     * @return List ��Դ�б�
     */
    public List queryResourceByType(String type);

    /**
     * ����һ���˵����룬ȡ�ö����������˵��б�
     * 
     * @param menuId
     *            һ���˵�����
     * @return �����������˵��б�
     */
    public Map querySubMenuList(String menuId);

}