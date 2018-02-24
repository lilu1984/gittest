package com.wonders.common.authority.bo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.wonders.common.authority.SysUser;
import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class UserInfo extends BaseBO implements SysUser {

    /** identifier field */
    private String userId;

    /** nullable persistent field */
    private String logonName;

    /** nullable persistent field */
    private String password;

    /** nullable persistent field */
    private String displayName;

    /** nullable persistent field */
    private String regionId;

    /** nullable persistent field */
    private String state;

    /** nullable persistent field */
    private Timestamp currentLogonDate;

    /** nullable persistent field */
    private Timestamp lastLogonDate;

    /** nullable persistent field */
    private Integer logonFailTimes;

    /** nullable persistent field */
    private String lockFlag;

    /** nullable persistent field */
    private Timestamp lockDate;
    
    /** nullable persistent field */
    private String personId;

    /** persistent field */
    private Set authority = new HashSet();

    /** persistent field */
    private Set role = new HashSet();

    /** default constructor */
    public UserInfo() {
    }

    
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getCurrentLogonDate() {
        return currentLogonDate;
    }

    public void setCurrentLogonDate(Timestamp currentLogonDate) {
        this.currentLogonDate = currentLogonDate;
    }

    public Timestamp getLastLogonDate() {
        return lastLogonDate;
    }

    public void setLastLogonDate(Timestamp lastLogonDate) {
        this.lastLogonDate = lastLogonDate;
    }

    public Integer getLogonFailTimes() {
        return logonFailTimes;
    }

    public void setLogonFailTimes(Integer logonFailTimes) {
        this.logonFailTimes = logonFailTimes;
    }

    public String getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(String lockFlag) {
        this.lockFlag = lockFlag;
    }

    public Timestamp getLockDate() {
        return lockDate;
    }

    public void setLockDate(Timestamp lockDate) {
        this.lockDate = lockDate;
    }

    public Set getAuthority() {
        return authority;
    }

    public void setAuthority(Set authority) {
        this.authority = authority;
    }

    public Set getRole() {
        return role;
    }

    public void setRole(Set role) {
        this.role = role;
    }

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((logonName == null) ? 0 : logonName.hashCode());
		result = PRIME * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserInfo other = (UserInfo) obj;
		if (logonName == null) {
			if (other.logonName != null)
				return false;
		} else if (!logonName.equals(other.logonName))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
    
    
}
