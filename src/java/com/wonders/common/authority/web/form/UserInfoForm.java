package com.wonders.common.authority.web.form;

import org.apache.struts.action.ActionForm;

/**
 * �û���Ϣstruts formbean��
 */

public class UserInfoForm extends ActionForm {

	/** ����CA �û���¼ʹ��  personId****/
	private String personId;
	
	
    /** �û���� */
    private String userId;

    /** ��½�� */
    private String logonName;

    /** �û��� */
    private String displayName;

    /** �Ա� */
    private String regionId;

    /** ��Ч�� */
    private String state;

    /** ���� */
    private String oldPassword;

    /** ���� */
    private String password;

    /** ȷ������ */
    private String confirmPassword;

    /** �û����б� */
    private String[] userGroupList;

    /** �û�Ȩ���б� */
    private String[] userAuthorityList;

    // =======�û�ɾ������=========
    private String[] userDeleteId;

    // =======�û���ѯ����=========
    /** �û���¼�� */
    private String userLogonName;

    /** �û�ʵ�� */
    private String userRealName;

    /** �û��Ա� */
    private String userGender;

    /** �û�״̬ */
    private String userState;

    /** �û����� */
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