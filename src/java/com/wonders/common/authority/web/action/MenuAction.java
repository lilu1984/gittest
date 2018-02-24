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
 * �˵�������Action��
 */

public class MenuAction extends BaseAction {

    private AuthorityManager authorityManager;

    public void setAuthorityManager(AuthorityManager authorityManager) {
        this.authorityManager = authorityManager;
    }

    /**
     * �������˵�ҳ��
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
        // ȡ�����˵��б�
        List menuList = authorityManager.queryMainMenuList();

        // ���˵��û�û��Ȩ�޵Ĳ˵�
        menuList = MenuUtil.filterMainMenuByAuthority(menuList, request);

        request.setAttribute("menuList", menuList);

        return mapping.findForward("topMenu");
    }

    /**
     * �����Ӳ˵�ҳ��
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
        // ȡ��һ���˵����
        String firstMenuId = request.getParameter("firstMenuId");

        if (firstMenuId == null) {
            firstMenuId = AuthorityConstants.MENU_ID_ROOT;
        }

        // ȡ��һ���˵��µ��Ӳ˵�
        Map menuMap = authorityManager.querySubMenuList(firstMenuId);

        // ���˵��û�û��Ȩ�޵Ĳ˵�
        menuMap = MenuUtil.filterSubMenuByAuthority(menuMap, request);

        request.setAttribute("menuMap", menuMap);

        return mapping.findForward("leftMenu");
    }

}
