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
 * 用户信息持久对象
 * 
 * 创建日期：2007-1-8 12:50:22
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
	 * 根据条件获得一页的用户列表
	 * 
	 * @param condition
	 *            查询条件对象
	 * @return PageList对象
	 */
	public PageList findPageList(UserSearchCondition condition) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from UserInfo a where 1 = 1");

		// 组装条件语句
		hql.append(makeWhereClause(condition));

		String countHql = "select count(*) " + hql.toString();
		String queryHql = hql.toString() + " order by a.displayName";

		return findPageListWithHql(countHql, queryHql, condition.getPageSize(), condition.getCurrentPage());
	}

	/**
	 * 判断所取的用户登录名是否已经使用。用户修改登录名时，需要带用户编号参数，不要与自己的登录名比较。
	 * 
	 * @param loginName
	 *            用户登录名
	 * @param userId
	 *            用户编号
	 * 
	 * @return 已经使用返回true，否则返回false
	 */
	public boolean isLogonNameExist(String loginName, String userId) {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from UserInfo a where 1 = 1");

		// 组装条件语句

		hql.append(" and a.logonName = '").append(loginName).append("' ");

		if (StringUtils.isNotEmpty(userId)) {
			hql.append(" and a.userId <> '").append(userId).append("'");
		}

		// 查询结果
		List list = findList(hql.toString());

		return (list != null && list.size() > 0);
	}
	
	
	
	
	public List findLogonByPersonId(String logonName) {
		StringBuffer hql = new StringBuffer("from UserInfo u where u.personId = '").append(logonName).append("'").append(" and u.state = '")
				.append(GlobalConstants.DIC_VALUE_VALID_VALIDITY).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * 根据用户登录信息查询用户
	 * 
	 * @param logonName
	 *            登录名
	 * @return 符合条件的用户对象
	 */
	public List findLogon(String logonName) {
		StringBuffer hql = new StringBuffer("from UserInfo u where u.logonName = '").append(logonName).append("'").append(" and u.state = '")
				.append(GlobalConstants.DIC_VALUE_VALID_VALIDITY).append("'");

		return this.findList(hql.toString());
	}

	/**
	 * 根据用户查询条件对象组装条件语句
	 * 
	 * @param condition
	 *            用户查询条件对象
	 * @return String 条件语句
	 */
	private String makeWhereClause(UserSearchCondition condition) {
		StringBuffer whereClause = new StringBuffer();

		// 组装条件语句
		if (StringUtils.isNotEmpty(condition.getUserName())) {
			whereClause.append(" and a.displayName like '%").append(StringUtil.GBKtoISO88591(condition.getUserName().trim())).append("%' ");
		}

		if (StringUtils.isNotEmpty(condition.getState())) {
			whereClause.append(" and a.state = '").append(condition.getState().trim()).append("' ");
		}

		return whereClause.toString();
	}

	/**
	 * 查有效的用户列表
	 * 
	 * @return 用户列表
	 */
	public List findValidityUserList() {
		// 用户信息查询
		StringBuffer hql = new StringBuffer("from UserInfo a where 1 = 1");

		// 查询条件
		hql.append(" and a.state = '").append(GlobalConstants.DIC_VALUE_VALID_VALIDITY).append("'");

		return findList(hql.toString());
	}

	/**
	 * 通过userId数组查询UserInfo对象列表
	 * 
	 * @param users
	 *            userId数组
	 * @return UserInfo对象列表
	 */
	public List findListByUsers(String[] users) {
		return findList("from UserInfo a where a.userId in (" + ArrayUtil.strArray2StrComma(users) + ")");
	}
}
