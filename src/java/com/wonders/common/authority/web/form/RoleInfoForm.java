package com.wonders.common.authority.web.form;


import org.apache.struts.action.ActionForm;

/**
 * �û�����Ϣstruts formbean��
 */

public class RoleInfoForm extends ActionForm {

    /** �û����� */
    private String groupId;

    /** �û������� */
    private String groupName;

    /** �û������� */
    private String groupDesc;

    /** �û��б� */
    private String[] userList;

    /** �û�Ȩ���б� */
    private String[] groupAuthorityList;

    // =========�û���ɾ������============
    private String[] groupDeleteId;

    // =========�û����ѯ����============
    private String groupQueryName;

    public String[] getGroupDeleteId() {
        return groupDeleteId;
    }

    public void setGroupDeleteId(String[] groupDeleteId) {
        this.groupDeleteId = groupDeleteId;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupQueryName() {
        return groupQueryName;
    }

    public void setGroupQueryName(String groupQueryName) {
        this.groupQueryName = groupQueryName;
    }

    public String[] getGroupAuthorityList() {
        return groupAuthorityList;
    }

    public void setGroupAuthorityList(String[] groupAuthorityList) {
        this.groupAuthorityList = groupAuthorityList;
    }

    public String[] getUserList() {
        return userList;
    }

    public void setUserList(String[] userList) {
        this.userList = userList;
    }

}