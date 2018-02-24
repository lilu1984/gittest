package com.wonders.common.authority.bo;

import com.wonders.esframework.common.bo.BaseBO;

/**
 * �û���Ϣ��ѯ�����ࡣ
 */

public class UserSearchCondition extends BaseBO {

    /** �û��� */
    String userName;

    /** �û�״̬ */
    String state;
    
    /** �û������� */
    String groupName;

    /** ÿҳ���� */
    int pageSize;

    /** ��ǰҳ�� */
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
