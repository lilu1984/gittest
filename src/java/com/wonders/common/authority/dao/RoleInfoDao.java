package com.wonders.common.authority.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.common.authority.bo.RoleInfo;
import com.wonders.common.authority.bo.UserSearchCondition;
import com.wonders.common.authority.util.ArrayUtil;
import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;

/**
 * 用户组信息持久对象
 * 
 * 创建日期：2007-1-8 12:59:29
 */
public class RoleInfoDao extends BaseHibernateDaoImpl {
    /**
     * @see com.wonders.esframework.common.dao.BaseHibernateDaoImpl#getEntityClass()
     */
    protected Class getEntityClass() {
        return RoleInfo.class;
    }

    /**
     * 根据条件获得一页的用户组列表
     * 
     * @param condition
     *            查询条件对象
     * @return PageList对象
     */
    public PageList findPageList(UserSearchCondition condition) {
        // 用户信息查询
        StringBuffer hql = new StringBuffer("from RoleInfo as r where 1 = 1");

        // 组装条件语句
        if (StringUtils.isNotEmpty(condition.getGroupName())) {
            hql.append(" and r.roleDesc like '%").append(condition.getGroupName().trim()).append("%' ");
        }

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by r.roleDesc";

        return this.findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
    }

    /**
     * 判断所取的用户组名是否已经使用。用户组修改时，需要带用户组编号参数，不要与自己比较。
     * 
     * @param groupName
     *            用户组名
     * @param groupId
     *            用户组编号
     * 
     * @return 已经使用返回true，否则返回false
     */
    public boolean isGroupNameExist(String groupName, String groupId) {
        // 用户组信息查询
        StringBuffer hql = new StringBuffer("from RoleInfo a where 1 = 1");

        // 组装条件语句

        hql.append(" and a.roleDesc = '").append(groupName).append("' ");

        if (StringUtils.isNotEmpty(groupId)) {
            hql.append(" and a.roleId <> '").append(groupId).append("'");
        }

        // 查询结果
        List list = findList(hql.toString());

        return (list != null && list.size() > 0);
    }

    /**
     * 通过groupId数组查询GroupInfo对象列表
     * 
     * @param groups
     *            groupId数组
     * @return GroupInfo对象列表
     */
    public List findListByGroups(String[] groups) {
        return findList("from RoleInfo a where a.roleId in (" + ArrayUtil.strArray2StrComma(groups) + ")");
    }
}
