package com.wonders.common.authority.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.common.authority.bo.RoleInfo;
import com.wonders.common.authority.bo.UserSearchCondition;
import com.wonders.common.authority.util.ArrayUtil;
import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;

/**
 * �û�����Ϣ�־ö���
 * 
 * �������ڣ�2007-1-8 12:59:29
 */
public class RoleInfoDao extends BaseHibernateDaoImpl {
    /**
     * @see com.wonders.esframework.common.dao.BaseHibernateDaoImpl#getEntityClass()
     */
    protected Class getEntityClass() {
        return RoleInfo.class;
    }

    /**
     * �����������һҳ���û����б�
     * 
     * @param condition
     *            ��ѯ��������
     * @return PageList����
     */
    public PageList findPageList(UserSearchCondition condition) {
        // �û���Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from RoleInfo as r where 1 = 1");

        // ��װ�������
        if (StringUtils.isNotEmpty(condition.getGroupName())) {
            hql.append(" and r.roleDesc like '%").append(condition.getGroupName().trim()).append("%' ");
        }

        String countHql = "select count(*) " + hql.toString();
        String queryHql = hql.toString() + " order by r.roleDesc";

        return this.findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
    }

    /**
     * �ж���ȡ���û������Ƿ��Ѿ�ʹ�á��û����޸�ʱ����Ҫ���û����Ų�������Ҫ���Լ��Ƚϡ�
     * 
     * @param groupName
     *            �û�����
     * @param groupId
     *            �û�����
     * 
     * @return �Ѿ�ʹ�÷���true�����򷵻�false
     */
    public boolean isGroupNameExist(String groupName, String groupId) {
        // �û�����Ϣ��ѯ
        StringBuffer hql = new StringBuffer("from RoleInfo a where 1 = 1");

        // ��װ�������

        hql.append(" and a.roleDesc = '").append(groupName).append("' ");

        if (StringUtils.isNotEmpty(groupId)) {
            hql.append(" and a.roleId <> '").append(groupId).append("'");
        }

        // ��ѯ���
        List list = findList(hql.toString());

        return (list != null && list.size() > 0);
    }

    /**
     * ͨ��groupId�����ѯGroupInfo�����б�
     * 
     * @param groups
     *            groupId����
     * @return GroupInfo�����б�
     */
    public List findListByGroups(String[] groups) {
        return findList("from RoleInfo a where a.roleId in (" + ArrayUtil.strArray2StrComma(groups) + ")");
    }
}
