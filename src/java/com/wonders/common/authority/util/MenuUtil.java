package com.wonders.common.authority.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.wonders.common.authority.bo.AuthorityTree;
import com.wonders.tdsc.common.util.AuthorityCheckUtil;

/**
 * 菜单工具类。
 */

public class MenuUtil {

    /** 日志 */
    private static Logger logger = Logger.getLogger(MenuUtil.class);

    /**
     * 取得一级菜单下的二级、三级菜单
     * 
     * @param menuId
     *            一级菜单代码
     * @param menuList
     *            菜单列表
     * @return 取得的二级、三级菜单代码列表
     */
    public static Map getSubMenu(String menuId, List menuList) {
        LinkedHashMap menuMap = new LinkedHashMap();

        try {
            for (Iterator iter1 = menuList.iterator(); iter1.hasNext();) {
                AuthorityTree menu1 = (AuthorityTree) iter1.next();
                if (menu1.getNodePid().equals(menuId)) {
                    String secondMenuId = menu1.getNodeId();
                    List secondList = new ArrayList();

                    for (Iterator iter2 = menuList.iterator(); iter2.hasNext();) {
                        AuthorityTree menu2 = (AuthorityTree) iter2.next();
                        if (menu2.getNodePid().equals(secondMenuId)) {
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
     * 过滤掉没有权限的主菜单
     * 
     * @param menuList
     *            待过滤的主菜单列表
     * @param request
     *            HttpServletRequest
     * @return 过滤后的菜单列表
     */
    public static List filterMainMenuByAuthority(List menuList, HttpServletRequest request) {
        if (menuList == null || menuList.size() < 1) {
            return menuList;
        }

        try {
            for (int i = menuList.size() - 1; i >= 0; i--) {
                AuthorityTree menu2 = (AuthorityTree) menuList.get(i);
                if (AuthorityCheckUtil.hasMenuPriv(request, menu2.getNodeId()) == false) {
                    menuList.remove(i);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return menuList;
    }

    /**
     * 过滤掉没有权限的子菜单
     * 
     * @param menuMap
     *            待过滤的子菜单列表
     * @param request
     *            HttpServletRequest
     * @return 过滤后的菜单列表
     */
    public static Map filterSubMenuByAuthority(Map menuMap, HttpServletRequest request) {
        if (menuMap == null || menuMap.size() < 1) {
            return menuMap;
        }

        Map newMap = new LinkedHashMap();

        try {
            for (Iterator iter1 = menuMap.keySet().iterator(); iter1.hasNext();) {
                AuthorityTree menu1 = (AuthorityTree) iter1.next();

                if (AuthorityCheckUtil.hasMenuPriv(request, menu1.getNodeId())) {
                    List menuList = (List) menuMap.get(menu1);
                    for (int j = menuList.size() - 1; j >= 0; j--) {
                        AuthorityTree menu2 = (AuthorityTree) menuList.get(j);

                        if (AuthorityCheckUtil.hasMenuPriv(request, menu2.getNodeId()) == false) {
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

}
