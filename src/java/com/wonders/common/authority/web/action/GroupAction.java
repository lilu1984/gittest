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
 * �û�������Action��
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
     * ������������û��б�
     */
    public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        // ȥ��session�е��û���Ϣ
        session.removeAttribute("groupInfo");

        // ��PageInfoҳ�洫����pageNo
        String pageNo = request.getParameter("currentPage");

        // ��������
        UserSearchCondition condition = new UserSearchCondition();

        if (pageNo != null && pageNo.trim().equals("") == false) {
            condition.setCurrentPage(Integer.parseInt(pageNo));
        }
        if (condition.getCurrentPage() < 1) {
            condition.setCurrentPage(1);
        }
        PageList list = userManager.queryGroupList(condition);

        // ��ѯ�б�
        request.setAttribute("groupPagerList", list);
        request.setAttribute("condition", condition);

        return mapping.findForward("groupList");
    }

    /**
     * �޸��û���֮ǰ�Ĳ���
     */
    public ActionForward queryGroupDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ���uri�û�����
        String groupId = request.getParameter("groupId");

        // ��ѯ�û�����Ϣ
        RoleInfo RoleInfo = userManager.getGroup(groupId);

        request.setAttribute("groupInfo", RoleInfo);
        request.setAttribute("userList", new ArrayList(RoleInfo.getUser()));

        return mapping.findForward("roleInfoDetail");
    }

    /**
     * �����û���֮ǰ�Ĳ���
     */
    public ActionForward toAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ����û��б��ŵ�requesst�У��Ա�ҳ��ʹ��
        request.setAttribute("userListFrom", userManager.getAllUser());

        return mapping.findForward("groupInfo");
    }

    /**
     * �����û���Ĳ���
     */
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ���ҳ������
        RoleInfoForm roleInfoForm = (RoleInfoForm) form;

        // �û�����
        RoleInfo roleInfo = new RoleInfo();
        // this.bindObject(RoleInfo, RoleInfoForm);
        roleInfo.setRoleId(roleInfoForm.getGroupId());
        roleInfo.setRoleDesc(roleInfoForm.getGroupDesc());

        // �û�����Ϣ���浽���ݿ�
        userManager.addGroup(roleInfo, roleInfoForm.getUserList(), roleInfoForm.getGroupAuthorityList());

        return mapping.findForward("success");
    }

    /**
     * �޸��û���֮ǰ�Ĳ���
     */
    public ActionForward toModify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ���uri�û�����
        String groupId = request.getParameter("groupId");

        // �õ��û�����Ϣ
        RoleInfo RoleInfo = userManager.getGroup(groupId);

        // ���û�����Ϣ����session���Ա���ʾ��ϸ
        request.getSession().setAttribute("groupInfo", RoleInfo);
        request.setAttribute("userList", new ArrayList(RoleInfo.getUser()));
        request.setAttribute("userListFrom", ListUtil.listMinus(userManager.getAllUser(), RoleInfo.getUser()));

        return mapping.findForward("groupInfo");
    }

    /**
     * �޸��û������
     */
    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ���ҳ������
        RoleInfoForm roleInfoForm = (RoleInfoForm) form;

        // �û���Ϣ����
        RoleInfo roleInfo = (RoleInfo) request.getSession().getAttribute("groupInfo");

        // this.bindObject(RoleInfo, RoleInfoForm);

        roleInfo.setRoleId(roleInfoForm.getGroupId());
        roleInfo.setRoleDesc(roleInfoForm.getGroupDesc());
        
        // ���浽���ݿ�
        userManager.modifyGroup(roleInfo, roleInfoForm.getUserList(), roleInfoForm.getGroupAuthorityList());

        return mapping.findForward("success");
    }
    
    /**
     * ɾ���û���
     */
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ���ҳ������
        RoleInfoForm RoleInfoForm = (RoleInfoForm) form;

        // �õ���ɾ�����û�id
        String[] groupIds = RoleInfoForm.getGroupDeleteId();

        // ɾ������
        for (int i = 0; i < groupIds.length; i++) {
            userManager.removeGroup(groupIds[i]);
        }

        return mapping.findForward("success");
    }

    /**
     * ��ʾ�˵���
     */
    public ActionForward showAuthorityTree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // ���uri�û�����
        String groupId = request.getParameter("groupId");

        // ȫ��Ȩ���б�
        List resourceAuthorityList = authorityManager.queryMenuAuthorityList();

        if (!StringUtils.isEmpty(groupId)) {
            // modify����detail
            request.setAttribute("isDetail", request.getParameter("isDetail"));

            // �û���ӵ�е�Ȩ��
            List groupAuthorityList = new ArrayList(userManager.getGroup(groupId).getAuthority());

            // ��ȫ��Ȩ���б�����������Ȩ�ޱ�ʶ
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
        // ȫ��Ȩ���б���Ϣ����request���Ա���ʾ��ϸ
        request.setAttribute("resourceAuthorityList", resourceAuthorityList);

        return mapping.findForward("tree");
    }

}
