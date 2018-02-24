package com.wonders.common.authority.web.form;


import org.apache.struts.action.ActionForm;

/**
 * 用户组信息struts formbean。
 */

public class RoleInfoForm extends ActionForm {

    /** 用户组编号 */
    private String groupId;

    /** 用户组名称 */
    private String groupName;

    /** 用户组描述 */
    private String groupDesc;

    /** 用户列表 */
    private String[] userList;

    /** 用户权限列表 */
    private String[] groupAuthorityList;

    // =========用户组删除条件============
    private String[] groupDeleteId;

    // =========用户组查询条件============
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