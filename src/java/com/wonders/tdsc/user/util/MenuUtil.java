package com.wonders.tdsc.user.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.tdsc.common.util.AuthorityCheckUtil;
import com.wonders.tdsc.user.model.AuthorityResource;

/**
 * �˵������ࡣ
 */

public class MenuUtil {

    /** ��־ */
    private static Logger logger = Logger.getLogger(MenuUtil.class);

    /**
     * ȡ�������˵�
     * 
     * @param menuId
     *            ��һ�����˵����
     * @param menuList
     *            �˵��б�
     * @return ȡ�õ������˵������б�
     */
    public static Map getSubMenu(String menuId, List menuList) {
        LinkedHashMap menuMap = new LinkedHashMap();

        try {
            for (Iterator iter1 = menuList.iterator(); iter1.hasNext();) {
                AuthorityResource menu1 = (AuthorityResource) iter1.next();
                if (menu1.getParentId().equals(menuId)) {
                    String secondMenuId = menu1.getResourceId();
                    List secondList = new ArrayList();

                    for (Iterator iter2 = menuList.iterator(); iter2.hasNext();) {
                        AuthorityResource menu2 = (AuthorityResource) iter2.next();
                        if (menu2.getParentId().equals(secondMenuId)) {
                            secondList.add(menu2);
                        }
                    }

                    menuMap.put(menu1, secondList);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return menuMap;
    }

    /**
     * ���˵�û��Ȩ�޵��Ӳ˵�
     * 
     * @param menuMap
     *            �����˵��Ӳ˵��б�
     * @param request
     *            HttpServletRequest
     * @return ���˺�Ĳ˵��б�
     */
    public static Map filterSubMenuByAuthority(Map menuMap, HttpServletRequest request) {
        if (menuMap == null || menuMap.size() < 1) {
            return menuMap;
        }

        Map newMap = new LinkedHashMap();

        try {
            for (Iterator iter1 = menuMap.keySet().iterator(); iter1.hasNext();) {
                AuthorityResource menu1 = (AuthorityResource) iter1.next();

                if (AuthorityCheckUtil.hasMenuPriv(request, menu1.getResourceId())) {
                    List menuList = (List) menuMap.get(menu1);
                    for (int j = menuList.size() - 1; j >= 0; j--) {
                        AuthorityResource menu2 = (AuthorityResource) menuList.get(j);

                        if (AuthorityCheckUtil.hasMenuPriv(request, menu2.getResourceId()) == false) {
                            menuList.remove(j);
                        }
                    }

                    newMap.put(menu1, menuList);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return newMap;
    }

    /**
     * ת���˵���Ϣ�е������ַ���Ŀǰ��Ҫ��url�еġ�&�����š�
     * 
     * @param menuMap
     *            �˵��б�
     * @return ת������б�
     */
    public static Map convertString(Map menuMap) {
        if (menuMap == null || menuMap.size() < 1) {
            return menuMap;
        }

        try {
            for (Iterator iter1 = menuMap.keySet().iterator(); iter1.hasNext();) {
                AuthorityResource menu1 = (AuthorityResource) iter1.next();
                if (StringUtils.isNotEmpty(menu1.getResourceUrl())) {
                    menu1.setResourceUrl(StringUtils.replace(menu1.getResourceUrl(), "+", "&"));
                }

                List menuList = (List) menuMap.get(menu1);
                if (menuList != null && menuList.size() > 0) {
                    for (Iterator iter2 = menuList.iterator(); iter2.hasNext();) {
                        AuthorityResource menu2 = (AuthorityResource) iter2.next();
                        if (StringUtils.isNotEmpty(menu2.getResourceUrl())) {
                            menu2.setResourceUrl(StringUtils.replace(menu2.getResourceUrl(), "+", "&"));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return menuMap;
    }

}
