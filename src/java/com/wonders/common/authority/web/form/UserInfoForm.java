package com.wonders.common.authority.web.form;

import org.apache.struts.action.ActionForm;

/**
 * 用户信息struts formbean。
 */

public class UserInfoForm extends ActionForm {

	/** 无锡CA 用户登录使用  personId****/
	private String personId;
	
	
    /** 用户编号 */
    private String userId;

    /** 登陆名 */
    private String logonName;

    /** 用户名 */
    private String displayName;

    /** 性别 */
    private String regionId;

    /** 有效性 */
    private String state;

    /** 密码 */
    private String oldPassword;

    /** 密码 */
    private String password;

    /** 确认密码 */
    private String confirmPassword;

    /** 用户组列表 */
    private String[] userGroupList;

    /** 用户权限列表 */
    private String[] userAuthorityList;

    // =======用户删除条件=========
    private String[] userDeleteId;

    // =======用户查询条件=========
    /** 用户登录名 */
    private String userLogonName;

    /** 用户实名 */
    private String userRealName;

    /** 用户性别 */
    private String userGender;

    /** 用户状态 */
    private String userState;

    /** 用户锁定 */
    private String userIsLock;

    public String getLogonName() {
        return logonName;
    }

    public void setLogonName(String logonName) {
        this.logonName = logonName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String[] getUserAuthorityList() {
        return userAuthorityList;
    }

    public void setUserAuthorityList(String[] userAuthorityList) {
        this.userAuthorityList = userAuthorityList;
    }

    public String[] getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(String[] userGroupList) {
        this.userGroupList = userGroupList;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserIsLock() {
        return userIsLock;
    }

    public void setUserIsLock(String userIsLock) {
        this.userIsLock = userIsLock;
    }

    public String getUserLogonName() {
        return userLogonName;
    }

    public void setUserLogonName(String userLogonName) {
        this.userLogonName = userLogonName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String[] getUserDeleteId() {
        return userDeleteId;
    }

    public void setUserDeleteId(String[] userDeleteId) {
        this.userDeleteId = userDeleteId;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
    
    
}