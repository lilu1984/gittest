package com.wonders.common.simplequery.bo;

public class QueryCon {
    /** ÿҳ���� */
    private int pageSize;

    /** ��ǰҳ�� */
    private int currentPage;

    /** �����﷨ */
    private String countSql;

    /** ����﷨ */
    private String querySql;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }
    
    
}
