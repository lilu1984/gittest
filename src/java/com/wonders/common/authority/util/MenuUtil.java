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
 * �˵������ࡣ
 */

public class MenuUtil {

    /** ��־ */
    private static Logger logger = Logger.getLogger(MenuUtil.class);

    /**
     * ȡ��һ���˵��µĶ����������˵�
     * 
     * @param menuId
     *            һ���˵�����
     * @param menuList
     *            �˵��б�
     * @return ȡ�õĶ����������˵������б�
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
     * ���˵�û��Ȩ�޵����˵�
     * 
     * @param menuList
     *            �����˵����˵��б�
     * @param request
     *            HttpServletRequest
     * @return ���˺�Ĳ˵��б�
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
