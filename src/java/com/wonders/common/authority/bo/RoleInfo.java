package com.wonders.common.authority.bo;

import java.util.HashSet;
import java.util.Set;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class RoleInfo extends BaseBO {

    /** identifier field */
    private String roleId;

    /** nullable persistent field */
    private String roleDesc;

    /** persistent field */
    private Set authority = new HashSet();

    /** persistent field */
    private Set user = new HashSet();

    /** default constructor */
    public RoleInfo() {
    }

    /** minimal constructor */
    public RoleInfo(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleDesc() {
        return this.roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Set getAuthority() {
        return authority;
    }

    public void setAuthority(Set authority) {
        this.authority = authority;
    }

    public Set getUser() {
        return user;
    }

    public void setUser(Set user) {
        this.user = user;
    }

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((roleDesc == null) ? 0 : roleDesc.hashCode());
		result = PRIME * result + ((roleId == null) ? 0 : roleId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RoleInfo other = (RoleInfo) obj;
		if (roleDesc == null) {
			if (other.roleDesc != null)
				return false;
		} else if (!roleDesc.equals(other.roleDesc))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		return true;
	}
    
    
}
