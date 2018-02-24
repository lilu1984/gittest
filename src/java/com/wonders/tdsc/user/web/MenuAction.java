package com.wonders.tdsc.user.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.tdsc.common.AuthorityConstants;
import com.wonders.tdsc.common.web.BaseAction;
import com.wonders.tdsc.user.service.AuthorityResourceManager;
import com.wonders.tdsc.user.util.MenuUtil;

/**
 * �˵�������Action
 */

public class MenuAction extends BaseAction {

    private AuthorityResourceManager resourceManager;

    public void setResourceManager(AuthorityResourceManager resourceManager) {
        this.resourceManager = resourceManager;
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
        Map menuMap = resourceManager.querySubMenuList(firstMenuId);

        // ���˵��û�û��Ȩ�޵Ĳ˵�
        menuMap = MenuUtil.filterSubMenuByAuthority(menuMap, request);

        request.setAttribute("menuMap", menuMap);

        return mapping.findForward("leftMenu");
    }
}
