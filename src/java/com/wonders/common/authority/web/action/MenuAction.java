package com.wonders.common.authority.web.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.service.AuthorityManager;
import com.wonders.common.authority.util.MenuUtil;
import com.wonders.tdsc.common.AuthorityConstants;
import com.wonders.tdsc.common.web.BaseAction;

/**
 * 菜单操作的Action。
 */

public class MenuAction extends BaseAction {

    private AuthorityManager authorityManager;

    public void setAuthorityManager(AuthorityManager authorityManager) {
        this.authorityManager = authorityManager;
    }

    /**
     * 进入主菜单页面
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward toTopMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 取得主菜单列表
        List menuList = authorityManager.queryMainMenuList();

        // 过滤掉用户没有权限的菜单
        menuList = MenuUtil.filterMainMenuByAuthority(menuList, request);

        request.setAttribute("menuList", menuList);

        return mapping.findForward("topMenu");
    }

    /**
     * 进入子菜单页面
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward toLeftMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 取得一级菜单编号
        String firstMenuId = request.getParameter("firstMenuId");

        if (firstMenuId == null) {
            firstMenuId = AuthorityConstants.MENU_ID_ROOT;
        }

        // 取得一级菜单下的子菜单
        Map menuMap = authorityManager.querySubMenuList(firstMenuId);

        // 过滤掉用户没有权限的菜单
        menuMap = MenuUtil.filterSubMenuByAuthority(menuMap, request);

        request.setAttribute("menuMap", menuMap);

        return mapping.findForward("leftMenu");
    }

}
