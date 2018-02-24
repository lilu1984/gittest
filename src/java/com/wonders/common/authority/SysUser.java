package com.wonders.common.authority;

/**
 * �û�����ӿ�
 * 
 * @author weed
 * @since 2008-11-20
 * @version v3.0
 */
public interface SysUser {

    /**
     * ����û���֯����/���ش���
     * 
     * @return �û���֯����/���ش���
     */
    public String getRegionId();

    /**
     * �����û���֯����/���ش���
     * 
     * @param regionId
     */
    public void setRegionId(String regionId);

    /**
     * ����û�����
     * 
     * @return �û�����
     */
    public String getUserId();

    /**
     * �����û�����
     * 
     * @param userId
     */
    public void setUserId(String userId);

    /**
     * ����û���ʾ����
     * 
     * @return �û���ʾ����
     */
    public String getDisplayName();

    /**
     * �����û���ʾ����
     * 
     * @param displayName
     */
    public void setDisplayName(String displayName);
}
