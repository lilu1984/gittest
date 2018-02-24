package com.wonders.common.authority.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.bo.AuthorityTree;
import com.wonders.common.authority.bo.RoleInfo;
import com.wonders.common.authority.bo.UserSearchCondition;
import com.wonders.common.authority.service.AuthorityManager;
import com.wonders.common.authority.service.UserManager;
import com.wonders.common.authority.util.ListUtil;
import com.wonders.common.authority.web.form.RoleInfoForm;
import com.wonders.esframework.common.model.PageList;
import com.wonders.tdsc.common.web.BaseAction;

/**
 * 用户组管理的Action。
 */

public class GroupAction extends BaseAction {

    private UserManager userManager;

    private AuthorityManager authorityManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setAuthorityManager(AuthorityManager authorityManager) {
        this.authorityManager = authorityManager;
    }

    /**
     * 根据条件获得用户列表
     */
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        // 去除session中的用户信息
        session.removeAttribute("groupInfo");

        // 从PageInfo页面传来的pageNo
        String pageNo = request.getParameter("currentPage");

        // 设置条件
        UserSearchCondition condition = new UserSearchCondition();

        if (pageNo != null && pageNo.trim().equals("") == false) {
            condition.setCurrentPage(Integer.parseInt(pageNo));
        }
        if (condition.getCurrentPage() < 1) {
            condition.setCurrentPage(1);
        }
        PageList list = userManager.queryGroupList(condition);

        // 查询列表
        request.setAttribute("groupPagerList", list);
        request.setAttribute("condition", condition);

        return mapping.findForward("groupList");
    }

    /**
     * 修改用户组之前的操作
     */
    public ActionForward queryGroupDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得uri用户组编号
        String groupId = request.getParameter("groupId");

        // 查询用户组信息
        RoleInfo RoleInfo = userManager.getGroup(groupId);

        request.setAttribute("groupInfo", RoleInfo);
        request.setAttribute("userList", new ArrayList(RoleInfo.getUser()));

        return mapping.findForward("roleInfoDetail");
    }

    /**
     * 增加用户组之前的操作
     */
    public ActionForward toAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得用户列表，放到requesst中，以便页面使用
        request.setAttribute("userListFrom", userManager.getAllUser());

        return mapping.findForward("groupInfo");
    }

    /**
     * 增加用户组的操作
     */
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得页面数据
        RoleInfoForm roleInfoForm = (RoleInfoForm) form;

        // 用户对象
        RoleInfo roleInfo = new RoleInfo();
        // this.bindObject(RoleInfo, RoleInfoForm);
        roleInfo.setRoleId(roleInfoForm.getGroupId());
        roleInfo.setRoleDesc(roleInfoForm.getGroupDesc());

        // 用户组信息保存到数据库
        userManager.addGroup(roleInfo, roleInfoForm.getUserList(), roleInfoForm.getGroupAuthorityList());

        return mapping.findForward("success");
    }

    /**
     * 修改用户组之前的操作
     */
    public ActionForward toModify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得uri用户组编号
        String groupId = request.getParameter("groupId");

        // 得到用户组信息
        RoleInfo RoleInfo = userManager.getGroup(groupId);

        // 将用户组信息放入session中以便显示详细
        request.getSession().setAttribute("groupInfo", RoleInfo);
        request.setAttribute("userList", new ArrayList(RoleInfo.getUser()));
        request.setAttribute("userListFrom", ListUtil.listMinus(userManager.getAllUser(), RoleInfo.getUser()));

        return mapping.findForward("groupInfo");
    }

    /**
     * 修改用户组操作
     */
    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得页面数据
        RoleInfoForm roleInfoForm = (RoleInfoForm) form;

        // 用户信息对象
        RoleInfo roleInfo = (RoleInfo) request.getSession().getAttribute("groupInfo");

        // this.bindObject(RoleInfo, RoleInfoForm);

        roleInfo.setRoleId(roleInfoForm.getGroupId());
        roleInfo.setRoleDesc(roleInfoForm.getGroupDesc());
        
        // 保存到数据库
        userManager.modifyGroup(roleInfo, roleInfoForm.getUserList(), roleInfoForm.getGroupAuthorityList());

        return mapping.findForward("success");
    }
    
    /**
     * 删除用户组
     */
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得页面数据
        RoleInfoForm RoleInfoForm = (RoleInfoForm) form;

        // 得到被删除的用户id
        String[] groupIds = RoleInfoForm.getGroupDeleteId();

        // 删除数据
        for (int i = 0; i < groupIds.length; i++) {
            userManager.removeGroup(groupIds[i]);
        }

        return mapping.findForward("success");
    }

    /**
     * 显示菜单树
     */
    public ActionForward showAuthorityTree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得uri用户组编号
        String groupId = request.getParameter("groupId");

        // 全部权限列表
        List resourceAuthorityList = authorityManager.queryMenuAuthorityList();

        if (!StringUtils.isEmpty(groupId)) {
            // modify或者detail
            request.setAttribute("isDetail", request.getParameter("isDetail"));

            // 用户组拥有的权限
            List groupAuthorityList = new ArrayList(userManager.getGroup(groupId).getAuthority());

            // 在全部权限列表中置上已有权限标识
            if (groupAuthorityList != null && groupAuthorityList.size() > 0) {
                int uaSize = groupAuthorityList.size();
                for (int i = 0; i < uaSize; i++) {
                    AuthorityTree authorityTree = (AuthorityTree) groupAuthorityList.get(i);
                    if (resourceAuthorityList != null && resourceAuthorityList.size() > 0) {
                        int raSize = resourceAuthorityList.size();
                        for (int j = 0; j < raSize; j++) {
                            AuthorityTree resourceAuthority = (AuthorityTree) resourceAuthorityList.get(j);
                            if (authorityTree.getNodeId().equals(resourceAuthority.getNodeId())) {
                                resourceAuthority.setChecked(true);
                            }
                        }
                    }
                }
            }
        }
        // 全部权限列表信息放入request中以便显示详细
        request.setAttribute("resourceAuthorityList", resourceAuthorityList);

        return mapping.findForward("tree");
    }

}
