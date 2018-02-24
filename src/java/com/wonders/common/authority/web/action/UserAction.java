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
 * �û������Action��
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
     * ���ȱʡ�������û��б�
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
        // ��������
        UserSearchCondition condition = new UserSearchCondition();
        condition.setPageSize(((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                GlobalConstants.PROPERTY_DEFAULT_PAGE_NUMBER)).intValue());

        // ��ѯ�б�
        request.setAttribute("userPageList", userManager.queryUserList(condition));
        request.setAttribute("condition", condition);

        return mapping.findForward("userList");
    }

    /**
     * ������������û��б�
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

        // ��������
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

        // ��ѯ�б�
        request.setAttribute("userPageList", userManager.queryUserList(condition));
        request.setAttribute("condition", condition);

        return mapping.findForward("userList");
    }

    /**
     * ��ѯ�û���ϸ��Ϣ
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
        // ����û����
        String userId = request.getParameter("userId");

        // ��ѯ�û���Ϣ
        UserInfo userInfo = userManager.getUser(userId);

        request.setAttribute("userInfo", userInfo);
        request.setAttribute("groupList", new ArrayList(userInfo.getRole()));

        return mapping.findForward("userInfoDetail");
    }

    /**
     * �����û�֮ǰ�Ĳ���
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
        // ����û����б��ŵ�requesst�У��Ա�ҳ��ʹ��
        request.setAttribute("groupListFrom", userManager.getAllGroup());

        return mapping.findForward("userInfo");
    }

    /**
     * �����û��Ĳ���
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
        // ���ҳ������
        UserInfoForm userInfoForm = (UserInfoForm) form;

        // �û���Ϣ����
        UserInfo userInfo = new UserInfo();
        this.bindObject(userInfo, userInfoForm);

        // ���û�����ʼ����
        userInfo.setPassword(SecurityUtil.getMD5Password(AuthorityConstants.DEFAULT_PASSWORD));

        // �û����û�����Ϣ���浽���ݿ�
        userManager.addUser(userInfo, userInfoForm.getUserGroupList(), userInfoForm.getUserAuthorityList());

        return mapping.findForward("success");
    }

    /**
     * �޸��û�֮ǰ�Ĳ���
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
        // ����û����
        String userId = request.getParameter("userId");

        // �õ��û���Ϣ
        UserInfo userInfo = userManager.getUser(userId);

        request.setAttribute("userInfo", userInfo);
        request.setAttribute("groupList", new ArrayList(userInfo.getRole()));
        request.setAttribute("groupListFrom", ListUtil.listMinus(userManager.getAllGroup(), userInfo.getRole()));

        return mapping.findForward("userInfo");
    }

    /**
     * �޸��û�����
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
        // ���ҳ������
        UserInfoForm userInfoForm = (UserInfoForm) form;

        // �û���Ϣ����
        UserInfo userInfo = this.userManager.getUser(userInfoForm.getUserId());

        // ��FormBean��ֵ������
        this.bindObject(userInfo, userInfoForm);

        // ���浽���ݿ�
        userManager.modifyUser(userInfo, userInfoForm.getUserGroupList(), userInfoForm.getUserAuthorityList());

        return mapping.findForward("success");
    }

    /**
     * ����¼���Ƿ���ڡ�
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
        // ��ʾ������
        String rtnStatus = null;

        // �û���¼��
        // String logonName = request.getParameter("logonName");
        UserInfoForm userInfoForm = (UserInfoForm) form;
        String logonName = StringUtil.gbToUnicode(userInfoForm.getLogonName());
        String userId = request.getParameter("userId");

        // logger.debug("logonName:" + logonName);

        // ����¼���Ƿ����
        if (userManager.isLogonNameExist(logonName, userId)) {
            rtnStatus = GlobalConstants.DIC_VALUE_YESNO_YES;
        } else
            rtnStatus = GlobalConstants.DIC_VALUE_YESNO_NO;

        // ���������õ����
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(rtnStatus);
        pw.close();

        return null;
    }

    /**
     * �����û�����Ϊ��ʼֵ��
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
        // ��ʾ������
        String rtnStatus = null;

        // �û����
        String userId = request.getParameter("userId");

        logger.debug("userId:" + userId);

        // �����û�����
        rtnStatus = userManager.resumeUserPassword(userId);

        // ���������õ����
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(rtnStatus);
        pw.close();

        return null;
    }

    /**
     * ��ʾ�˵�����
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
        // ����û����
        String userId = request.getParameter("userId");

        // ȫ��Ȩ���б�
        List resourceAuthorityList = authorityManager.queryMenuAuthorityList();

        if (!StringUtils.isEmpty(userId)) {
            // modify����detail
            request.setAttribute("isDetail", request.getParameter("isDetail"));

            // �û�ӵ�е�Ȩ��
            List userAuthorityList = new ArrayList(userManager.getUser(userId).getAuthority());

            // ��ȫ��Ȩ���б�����������Ȩ�ޱ�ʶ
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

        // ȫ��Ȩ���б���Ϣ����request���Ա���ʾ��ϸ
        request.setAttribute("resourceAuthorityList", resourceAuthorityList);

        return mapping.findForward("tree");
    }

    /**
     * ׼���޸��û����롣
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
     * �޸��û����롣
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
    	System.out.println("��ʼ���ԡ�������������������������������");
        String rtnStr = null;

        // session�е��û���Ϣ
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(GlobalConstants.SESSION_USER_INFO);

        // ҳ����ȡ����������Ϣ
        UserInfoForm userInfoForm = (UserInfoForm) form;

        // ������
        String password = userInfoForm.getPassword();

        if (!password.equals(userInfoForm.getConfirmPassword())) {
            // ��������ȷ�����벻һ��
            rtnStr = "1";
        } else {
            if (!SecurityUtil.getMD5Password(userInfoForm.getOldPassword()).equals(userInfo.getPassword())) {
                // ԭ���벻��ȷ
                rtnStr = "2";
            } else {
                // �����û�����
                userInfo.setPassword(SecurityUtil.getMD5Password(password));
                // �������뵽���ݿ�
                userManager.changeUserPassword(userInfo);

                rtnStr = "3";
            }
        }
        System.out.println("rtnStr"+rtnStr);
        // ���������õ����
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
     * �����û�����Ϊ��ʼֵ��
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
        // �û����
        String userId = request.getParameter("userId");

        // ����û�����
        userManager.unlockUser(userId);

        return mapping.findForward("success");
    }

}