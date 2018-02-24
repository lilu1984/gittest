package com.wonders.tdsc.common.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.wonders.tdsc.common.GlobalConstants;

/**
 * 权限检查工具类。
 * <p>
 * 
 * @version 1.0
 * @since 1.0
 */

public class AuthorityCheckUtil {

    private AuthorityCheckUtil() {
    }

    /**
     * 判断登录用户是否拥有某菜单的权限
     * 
     * @param request
     *            HttpServletRequest
     * @param menuId
     *            菜单资源编号
     * @return true 有， false 没有
     * @throws Exception
     */
    public static boolean hasMenuPriv(HttpServletRequest request, String menuId) throws Exception {

        // //如果是超级用户，有所有菜单权限
        // UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        // if (user.getUserId().equals(AuthorityConstants.SUPER_USER_ID)) {
        // return true;
        // }

        // 从Session中取得用户的菜单权限列表
        List list = (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_AUTHORITY_MENU);
        if (list == null) {
            return false;
        }

        // 判断用户是否拥有该菜单权限
        if (list.contains(menuId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取得用户有权限的区县代码列表
     * 
     * @param request
     *            HttpServletRequest request
     * @return 区县列表
     * @throws Exception
     */
    public static List getQxList(HttpServletRequest request) throws Exception {
        // //如果是超级用户，有所有区县权限
        // UserInfo user = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);
        // if (user.getUserId().equals(AuthorityConstants.SUPER_USER_ID)) {
        // Map map = DicDataUtil.getInstance().getDic(GlobalConstants.DIC_ID_QX);
        // List list = new ArrayList();
        //            
        // if (map != null && map.size() > 0) {
        // Iterator iter = map.keySet().iterator();
        // while(iter.hasNext()) {
        // list.add(iter.next());
        // }
        // }
        //            
        // return list;
        // }

        return (List) request.getSession().getAttribute(GlobalConstants.SESSION_USER_QX);
    }

}
