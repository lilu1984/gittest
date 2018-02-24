package com.wonders.common.authority.bo;

import com.wonders.esframework.common.bo.BaseBO;

/**
 * 用户信息查询条件类。
 */

public class UserSearchCondition extends BaseBO {

    /** 用户名 */
    String userName;

    /** 用户状态 */
    String state;
    
    /** 用户组名称 */
    String groupName;

    /** 每页行数 */
    int pageSize;

    /** 当前页数 */
    int currentPage;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
