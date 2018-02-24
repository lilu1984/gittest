package com.wonders.tdsc.out.service.bean;

public class UserInfoBean {

	private String	personId;	// add new column CA personId
	private String	loginName;	// logon_name is LogonName
	private String	userName;	// display_name is PersonName
	private String	password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
