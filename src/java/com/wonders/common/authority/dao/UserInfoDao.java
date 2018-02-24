package com.wonders.common.authority.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wonders.common.authority.bo.UserInfo;
import com.wonders.common.authority.bo.UserSearchCondition;
import com.wonders.common.authority.util.ArrayUtil;
import com.wonders.esframework.common.dao.BaseHibernateDaoImpl;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.common.GlobalConstants;

/**
 * �û���Ϣ�־ö���
 * 
 * �������ڣ�2007-1-8 12:50:22
 */
public class UserInfoDao extends BaseHibernateDaoImpl {

	/**
	 * 
	 * @see com.wonders.esframework.common.dao.BaseHibernateDaoImpl#getEntityClass()
	 */
	protected Class getEntityClass() {
		return UserInfo.class;
	}

	/**
	 * �����������һҳ���û��б�
	 * 
	 * @param condition
	 *            ��ѯ��������
	 * @return PageList����
	 */
	public PageList findPageList(UserSearchCondition condition) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from UserInfo a where 1 = 1");

		// ��װ�������
		hql.append(makeWhereClause(condition));

		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by a.displayName";

		return findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
	}

	/**
	 * �ж���ȡ���û���¼���Ƿ��Ѿ�ʹ�á��û��޸ĵ�¼��ʱ����Ҫ���û���Ų�������Ҫ���Լ��ĵ�¼���Ƚϡ�
	 * 
	 * @param loginName
	 *            �û���¼��
	 * @param userId
	 *            �û����
	 * 
	 * @return �Ѿ�ʹ�÷���true�����򷵻�false
	 */
	public boolean isLogonNameExist(String loginName, String userId) {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from UserInfo a where 1 = 1");

		// ��װ�������

		hql.append(" and a.logonName = '").append(loginName).append("' ");

		if (StringUtils.isNotEmpty(userId)) {
			hql.append(" and a.userId <> '").append(userId).append("'");
		}

		// ��ѯ���
		List list = findList(hql.toString());

		return (list != null && list.size() > 0);
	}
	
	
	
	
	public List findLogonByPersonId(String logonName) {
		StringBuffer hql = new StringBuffer("from UserInfo u where u.personId = '").append(logonName).append("'").append(" and u.state = '")
				.append(GlobalConstants.DIC_VALUE_VALID_VALIDITY).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * �����û���¼��Ϣ��ѯ�û�
	 * 
	 * @param logonName
	 *            ��¼��
	 * @return �����������û�����
	 */
	public List findLogon(String logonName) {
		StringBuffer hql = new StringBuffer("from UserInfo u where u.logonName = '").append(logonName).append("'").append(" and u.state = '")
				.append(GlobalConstants.DIC_VALUE_VALID_VALIDITY).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * �����û���ѯ����������װ�������
	 * 
	 * @param condition
	 *            �û���ѯ��������
	 * @return String �������
	 */
	private String makeWhereClause(UserSearchCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// ��װ�������
		if (StringUtils.isNotEmpty(condition.getUserName())) {
			whereClause.append(" and a.displayName like '%").append(StringUtil.GBKtoISO88591(condition.getUserName().trim())).append("%' ");
		}

		if (StringUtils.isNotEmpty(condition.getState())) {
			whereClause.append(" and a.state = '").append(condition.getState().trim()).append("' ");
		}

		return whereClause.toString();
	}

	/**
	 * ����Ч���û��б�
	 * 
	 * @return �û��б�
	 */
	public List findValidityUserList() {
		// �û���Ϣ��ѯ
		StringBuffer hql = new StringBuffer("from UserInfo a where 1 = 1");

		// ��ѯ����
		hql.append(" and a.state = '").append(GlobalConstants.DIC_VALUE_VALID_VALIDITY).append("'");

		return findList(hql.toString());
	}

	/**
	 * ͨ��userId�����ѯUserInfo�����б�
	 * 
	 * @param users
	 *            userId����
	 * @return UserInfo�����б�
	 */
	public List findListByUsers(String[] users) {
		return findList("from UserInfo a where a.userId in (" + ArrayUtil.strArray2StrComma(users) + ")");
	}
}
