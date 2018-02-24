package com.wonders.common.authority;

/**
 * 用户对象接口
 * 
 * @author weed
 * @since 2008-11-20
 * @version v3.0
 */
public interface SysUser {

    /**
     * 获得用户组织机构/区县代码
     * 
     * @return 用户组织机构/区县代码
     */
    public String getRegionId();

    /**
     * 设置用户组织机构/区县代码
     * 
     * @param regionId
     */
    public void setRegionId(String regionId);

    /**
     * 获得用户代码
     * 
     * @return 用户代码
     */
    public String getUserId();

    /**
     * 设置用户代码
     * 
     * @param userId
     */
    public void setUserId(String userId);

    /**
     * 获得用户显示名称
     * 
     * @return 用户显示名称
     */
    public String getDisplayName();

    /**
     * 设置用户显示名称
     * 
     * @param displayName
     */
    public void setDisplayName(String displayName);
}
