package com.wonders.common.authority.web.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.wonders.common.authority.bo.AuthorityTree;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.common.authority.bo.UserSearchCondition;
import com.wonders.common.authority.service.AuthorityManager;
import com.wonders.common.authority.service.UserManager;
import com.wonders.common.authority.util.ListUtil;
import com.wonders.common.authority.web.form.UserInfoForm;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.SecurityUtil;
import com.wonders.esframework.util.StringUtil;
import com.wonders.tdsc.common.AuthorityConstants;
import com.wonders.tdsc.common.GlobalConstants;
import com.wonders.tdsc.common.web.BaseAction;

/**
 * 用户管理的Action。
 */

public class UserAction extends BaseAction {

    private UserManager userManager;

    private AuthorityManager authorityManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setAuthorityManager(AuthorityManager authorityManager) {
        this.authorityManager = authorityManager;
    }

    /**
     * 获得缺省条件的用户列表。
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
    public ActionForward toDefaultList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 设置条件
        UserSearchCondition condition = new UserSearchCondition();
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        // 查询列表
        request.setAttribute("userPageList", userManager.queryUserList(condition));
        request.setAttribute("condition", condition);

        return mapping.findForward("userList");
    }

    /**
     * 根据条件获得用户列表
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
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String pageNo = request.getParameter("currentPage");
        String userName = request.getParameter("userName");
        String state = request.getParameter("state");
        String operType = request.getParameter("success");

        // 设置条件
        UserSearchCondition condition = new UserSearchCondition();

        if ("success".equals(operType) == false) {
            condition.setUserName(userName);
            condition.setState(state);
        }
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        if (StringUtils.isNotEmpty(pageNo)) {
            condition.setCurrentPage(Integer.parseInt(pageNo));
        }

        // 查询列表
        request.setAttribute("userPageList", userManager.queryUserList(condition));
        request.setAttribute("condition", condition);

        return mapping.findForward("userList");
    }

    /**
     * 查询用户详细信息
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
    public ActionForward queryUserDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得用户编号
        String userId = request.getParameter("userId");

        // 查询用户信息
        UserInfo userInfo = userManager.getUser(userId);

        request.setAttribute("userInfo", userInfo);
        request.setAttribute("groupList", new ArrayList(userInfo.getRole()));

        return mapping.findForward("userInfoDetail");
    }

    /**
     * 增加用户之前的操作
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
    public ActionForward toAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得用户组列表，放到requesst中，以便页面使用
        request.setAttribute("groupListFrom", userManager.getAllGroup());

        return mapping.findForward("userInfo");
    }

    /**
     * 增加用户的操作
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
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得页面数据
        UserInfoForm userInfoForm = (UserInfoForm) form;

        // 用户信息对象
        UserInfo userInfo = new UserInfo();
        this.bindObject(userInfo, userInfoForm);

        // 给用户赋初始密码
        userInfo.setPassword(SecurityUtil.getMD5Password(AuthorityConstants.DEFAULT_PASSWORD));

        // 用户和用户组信息保存到数据库
        userManager.addUser(userInfo, userInfoForm.getUserGroupList(), userInfoForm.getUserAuthorityList());

        return mapping.findForward("success");
    }

    /**
     * 修改用户之前的操作
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
    public ActionForward toModify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得用户编号
        String userId = request.getParameter("userId");

        // 得到用户信息
        UserInfo userInfo = userManager.getUser(userId);

        request.setAttribute("userInfo", userInfo);
        request.setAttribute("groupList", new ArrayList(userInfo.getRole()));
        request.setAttribute("groupListFrom", ListUtil.listMinus(userManager.getAllGroup(), userInfo.getRole()));

        return mapping.findForward("userInfo");
    }

    /**
     * 修改用户操作
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
    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得页面数据
        UserInfoForm userInfoForm = (UserInfoForm) form;

        // 用户信息对象
        UserInfo userInfo = this.userManager.getUser(userInfoForm.getUserId());

        // 将FormBean的值对象中
        this.bindObject(userInfo, userInfoForm);

        // 保存到数据库
        userManager.modifyUser(userInfo, userInfoForm.getUserGroupList(), userInfoForm.getUserAuthorityList());

        return mapping.findForward("success");
    }

    /**
     * 检查登录名是否存在。
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
    public ActionForward checkLogonName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 提示的内容
        String rtnStatus = null;

        // 用户登录名
        // String logonName = request.getParameter("logonName");
        UserInfoForm userInfoForm = (UserInfoForm) form;
        String logonName = StringUtil.gbToUnicode(userInfoForm.getLogonName());
        String userId = request.getParameter("userId");

        // logger.debug("logonName:" + logonName);

        // 检查登录名是否存在
        if (userManager.isLogonNameExist(logonName, userId)) {
            rtnStatus = GlobalConstants.DIC_VALUE_YESNO_YES;
        } else
            rtnStatus = GlobalConstants.DIC_VALUE_YESNO_NO;

        // 将内容设置到输出
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(rtnStatus);
        pw.close();

        return null;
    }

    /**
     * 重置用户密码为初始值。
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward resumeUserPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 提示的内容
        String rtnStatus = null;

        // 用户编号
        String userId = request.getParameter("userId");

        logger.debug("userId:" + userId);

        // 重置用户密码
        rtnStatus = userManager.resumeUserPassword(userId);

        // 将内容设置到输出
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(rtnStatus);
        pw.close();

        return null;
    }

    /**
     * 显示菜单树。
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward showAuthorityTree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获得用户编号
        String userId = request.getParameter("userId");

        // 全部权限列表
        List resourceAuthorityList = authorityManager.queryMenuAuthorityList();

        if (!StringUtils.isEmpty(userId)) {
            // modify或者detail
            request.setAttribute("isDetail", request.getParameter("isDetail"));

            // 用户拥有的权限
            List userAuthorityList = new ArrayList(userManager.getUser(userId).getAuthority());

            // 在全部权限列表中置上已有权限标识
            if (userAuthorityList != null && userAuthorityList.size() > 0) {
                int uaSize = userAuthorityList.size();
                for (int i = 0; i < uaSize; i++) {
                    AuthorityTree authorityTree = (AuthorityTree) userAuthorityList.get(i);
                    if (resourceAuthorityList != null && resourceAuthorityList.size() > 0) {
                        int raSize = resourceAuthorityList.size();
                        for (int j = 0; j < raSize; j++) {
                            AuthorityTree resourceAuthority = (AuthorityTree) resourceAuthorityList.get(j);
                            if (authorityTree.getNodeId().equals(resourceAuthority.getNodeId()))
                                resourceAuthority.setChecked(true);
                        }
                    }
                }
            }
        }

        // 全部权限列表信息放入request中以便显示详细
        request.setAttribute("resourceAuthorityList", resourceAuthorityList);

        return mapping.findForward("tree");
    }

    /**
     * 准备修改用户密码。
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward toModifyUserPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("changPassword");
    }

    /**
     * 修改用户密码。
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward modifyUserPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	System.out.println("开始测试…………………………………………");
        String rtnStr = null;

        // session中的用户信息
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

        // 页面上取来的密码信息
        UserInfoForm userInfoForm = (UserInfoForm) form;

        // 新密码
        String password = userInfoForm.getPassword();

        if (!password.equals(userInfoForm.getConfirmPassword())) {
            // 新密码与确认密码不一致
            rtnStr = "1";
        } else {
            if (!SecurityUtil.getMD5Password(userInfoForm.getOldPassword()).equals(userInfo.getPassword())) {
                // 原密码不正确
                rtnStr = "2";
            } else {
                // 加密用户密码
                userInfo.setPassword(SecurityUtil.getMD5Password(password));
                // 更新密码到数据库
                userManager.changeUserPassword(userInfo);

                rtnStr = "3";
            }
        }
        System.out.println("rtnStr"+rtnStr);
        // 将内容设置到输出
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(rtnStr);
        pw.close();
        return null;
//        request.setAttribute("status", rtnStr);
//        return mapping.findForward("ajaxXml");
        
    }

    /**
     * 重置用户密码为初始值。
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward unlockUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 用户编号
        String userId = request.getParameter("userId");

        // 解除用户锁定
        userManager.unlockUser(userId);

        return mapping.findForward("success");
    }

}