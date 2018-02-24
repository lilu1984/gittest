package com.wonders.common.authority.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.wonders.common.authority.bo.RoleInfo;
import com.wonders.common.authority.bo.UserInfo;
import com.wonders.common.authority.bo.UserSearchCondition;
import com.wonders.common.authority.dao.AuthorityTreeDao;
import com.wonders.common.authority.dao.RoleInfoDao;
import com.wonders.common.authority.dao.UserInfoDao;
import com.wonders.common.authority.util.ListUtil;
import com.wonders.esframework.common.exception.BusinessException;
import com.wonders.esframework.common.model.PageList;
import com.wonders.esframework.common.service.BaseSpringManagerImpl;
import com.wonders.esframework.dic.util.DicPropertyUtil;
import com.wonders.esframework.util.SecurityUtil;
import com.wonders.tdsc.common.AuthorityConstants;
import com.wonders.tdsc.common.GlobalConstants;

/**
 * 用户管理业务层对象
 * 
 * 创建日期：2007-1-8 13:15:16
 */
public class UserManager extends BaseSpringManagerImpl {
    private UserInfoDao userInfoDao;

    private RoleInfoDao roleInfoDao;

    private AuthorityTreeDao authorityTreeDao;

    public void setUserInfoDao(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    public void setRoleInfoDao(RoleInfoDao roleInfoDao) {
        this.roleInfoDao = roleInfoDao;
    }

    public void setAuthorityTreeDao(AuthorityTreeDao authorityTreeDao) {
        this.authorityTreeDao = authorityTreeDao;
    }

    /**
     * 根据查询条件获得一页用户的列表
     * 
     * @param condition
     *            查询条件对象
     * @return PageList 一页用户的列表容器对象
     */
    public PageList queryUserList(UserSearchCondition condition) {
        return userInfoDao.findPageList(condition);
    }

    /**
     * 验证登录名是否存在
     * 
     * @param loginName
     *            登录名
     * @param userId
     *            用户编号
     * @return true：存在 false：不存在
     */
    public boolean isLogonNameExist(String loginName, String userId) {
        return userInfoDao.isLogonNameExist(loginName, userId);
    }

    /**
     * 重置用户密码
     * 
     * @param id
     *            用户编号
     * @return 用户登录名
     */
    public String resumeUserPassword(String id) {
        // 得到用户
        UserInfo userInfo = (UserInfo) this.getUser(id);

        // 置初始密码
        userInfo.setPassword(SecurityUtil.getMD5Password(AuthorityConstants.DEFAULT_PASSWORD));

        // 更新数据
        userInfoDao.update(userInfo);

        return userInfo.getLogonName();
    }

    /**
     * 修改用户密码
     * 
     * @param id
     *            用户对象
     * @return void
     */
    public void changeUserPassword(UserInfo userInfo) {
        userInfoDao.update(userInfo);
    }

    /**
     * 增加一个用户（信息包含拥护隶属于的组和用户拥有的权限）
     * 
     * @param user
     *            用户对象
     * @param groupList
     *            组列表
     * @return UserInfo 用户对象
     */
    public UserInfo addUser(UserInfo user, String[] groups, String[] authorities) {
        // 设置锁定状态，默认不锁定
        user.setLockFlag(GlobalConstants.DIC_VALUE_YESNO_NO);

        // 判断用户登录名是否重复
        if (this.userInfoDao.isLogonNameExist(user.getLogonName(), null)) {
            throw new BusinessException("用户登录名 " + user.getLogonName() + " 已经被使用！");
        }

        // 设置所属组和拥有的权限
        user.setRole(new HashSet(roleInfoDao.findListByGroups(groups)));
        user.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // 保存用户对象
        userInfoDao.save(user);

        return user;
    }

    /**
     * 修改一个用户（信息包含拥护隶属于的组）
     * 
     * @param user
     *            用户对象
     * @param groupList
     *            组列表
     * @return UserInfo 用户对象
     */
    public UserInfo modifyUser(UserInfo user, String[] groups, String[] authorities) {

        // 判断用户登录名是否重复
        if (this.userInfoDao.isLogonNameExist(user.getLogonName(), user.getUserId())) {
            throw new BusinessException("用户登录名 " + user.getLogonName() + " 已经被使用！");
        }

        // 设置所属组和拥有的权限
        user.setRole(new HashSet(roleInfoDao.findListByGroups(groups)));
        user.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // 更新用户对象
        userInfoDao.update(user);

        return user;
    }

    /**
     * 删除一个用户（不做物理删除，将状态置为不可用）
     * 
     * @param id
     */
    public void removeUser(String id) {
        // 获得用户对象
        UserInfo userInfo = (UserInfo) userInfoDao.get(id);

        // 将用户状态设为无效
        userInfo.setState(GlobalConstants.DIC_VALUE_VALID_INVALIDITY);

        userInfoDao.update(userInfo);
    }

    /**
     * 根据用户编码获得用户信息
     * 
     * @param id
     *            用户编码
     * @return 用户信息对象
     */
    public UserInfo getUser(String id) {
        UserInfo user = (UserInfo) userInfoDao.loadWithLazy(id, new String[] { "authority", "role" });

        if (user == null) {
            throw new BusinessException("未找到该用户信息！");
        }

        return user;
    }

    /**
     * 增加一个用户组对象
     * 
     * @param group
     *            用户组对象
     * @param userList
     *            用户列表
     * @return 用户组对象
     */
    public RoleInfo addGroup(RoleInfo group, String[] users, String[] authorities) {
        // 判断用户组名称是否重复
        if (this.roleInfoDao.isGroupNameExist(group.getRoleDesc(), null)) {
            throw new BusinessException("用户组名称 " + group.getRoleDesc() + " 已经存在，请重新输入!");
        }

        // 设置包含用户和拥有的权限
        group.setUser(new HashSet(userInfoDao.findListByUsers(users)));
        group.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // 保存用户对象
        roleInfoDao.save(group);

        return group;
    }

    /**
     * 修改一个用户组
     * 
     * @param group
     *            用户组对象
     * @param userList
     *            用户列表
     * @return RoleInfo 用户组对象
     */
    public RoleInfo modifyGroup(RoleInfo group, String[] users, String[] authorities) {
        // 判断用户组名称是否重复
        if (this.roleInfoDao.isGroupNameExist(group.getRoleDesc(), group.getRoleId())) {
            throw new BusinessException("用户组名称 " + group.getRoleDesc() + " 已经存在，请重新输入!");
        }

        // 设置包含用户和拥有的权限
        group.setUser(new HashSet(userInfoDao.findListByUsers(users)));
        group.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // 更新用户对象
        roleInfoDao.update(group);

        return group;
    }

    /**
     * 删除用户组对象
     * 
     * @param id
     *            用户组编号
     */
    public void removeGroup(String id) {
        // 3、删除用户组信息
        roleInfoDao.deleteById(id);
    }

    /**
     * 根据查询条件获得一页用户组的列表
     * 
     * @param condition
     *            查询条件对象
     * @return PageList 一页用户组的列表容器对象
     */
    public PageList queryGroupList(UserSearchCondition condition) {
        return roleInfoDao.findPageList(condition);
    }

    /**
     * 根据用户组编码获得用户组信息
     * 
     * @param id
     *            用户组编码
     * @return 用户组信息对象
     */
    public RoleInfo getGroup(String id) {
        RoleInfo group = (RoleInfo) roleInfoDao.loadWithLazy(id, new String[] { "authority", "user" });

        if (group == null) {
            throw new BusinessException("未找到该用户组信息！");
        }

        return group;
    }

    /**
     * 取得所有的用户组列表
     * 
     * @return list 用户组列表
     */
    public List getAllGroup() {
        return roleInfoDao.findAll();
    }

    /**
     * 取得所有的用户列表
     * 
     * @return list 用户列表
     */
    public List getAllUser() {
        return userInfoDao.findValidityUserList();
    }

    /**
     * 用户登录。
     * 
     * @param logonName
     *            登录名
     * @param password
     *            密码
     * @return Object 用户信息或错误描述信息
     */
    public Object logonByPersonId(String personId) {
        // 取登录用户
        List list = userInfoDao.findLogonByPersonId(personId);
        if (list == null || list.size() < 1) {
            return "用户不存在！";
        }
        UserInfo userInfo = (UserInfo) list.get(0);
        return userInfo;
    }
    /**
     * CA用户登录。
     * @param logonName
     * @return
     */
    public Object logon(String logonName){
        List list = userInfoDao.findLogon(logonName);
        if (list == null || list.size() < 1) {
            return "输入的用户名不存在！";
        }
        UserInfo userInfo = (UserInfo) list.get(0);
        return userInfo;
    }
    
    /**
     * 用户登录。
     * 
     * @param logonName
     *            登录名
     * @param password
     *            密码
     * @return Object 用户信息或错误描述信息
     */
    public Object logon(String logonName, String password) {
        // 取登录用户
        List list = userInfoDao.findLogon(logonName);
        if (list == null || list.size() < 1) {
            return "输入的用户名不存在！";
        }
        UserInfo userInfo = (UserInfo) list.get(0);

        // 锁定状态判断和处理
        // if (userInfo.getLockFlag() != null && userInfo.getLockFlag().equals(GlobalConstants.DIC_VALUE_YESNO_YES)) {
        // // 用户被锁定，取得自动解锁的分钟数
        // int minutes = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
        // GlobalConstants.PROPERTY_USER_UNLOCK_MINUTES)).intValue();
        // if (minutes < 1 || userInfo.getLockDate() == null) {
        // // 无法自动解锁
        // return "用户被锁定，不能登录！";
        // }
        // if (userInfo.getLockDate().getTime() + minutes * 60000 > System.currentTimeMillis()) {
        // // 未到解锁时间
        // return "用户被锁定，不能登录！";
        // }
        //
        // // 已经解锁，重置锁定标志
        // userInfo.setLockFlag(GlobalConstants.DIC_VALUE_YESNO_NO);
        // userInfo.setLockDate(null);
        // }
        // 密码加密
        String sPwd = SecurityUtil.getMD5Password(password);

        // 密码比较
        if (sPwd.equals(userInfo.getPassword()) == false) {
            // 输入的用户密码不正确，判断连续登录失败次数
            int times = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                    GlobalConstants.PROPERTY_USER_lOCK_LOGON_TIMES)).intValue();
            if (times > 0) {
                // 设置连续登录失败次数
                int failTimes = 1;
                if (userInfo.getLogonFailTimes() != null) {
                    failTimes = userInfo.getLogonFailTimes().intValue() + 1;
                }
                userInfo.setLogonFailTimes(new Integer(failTimes));

                if (failTimes >= times) {
                    // 连续登录失败次数达到了锁定要求，锁定用户
                    userInfo.setLockFlag(GlobalConstants.DIC_VALUE_YESNO_YES);
                    userInfo.setLockDate(new Timestamp(System.currentTimeMillis()));
                    logger.info("用户被锁定。登录名：" + logonName);
                }
            }

            this.userInfoDao.update(userInfo);
            return "输入的密码不正确！";
        }

        // 登录成功，重置连续登录失败次数
        // userInfo.setLogonFailTimes(new Integer(0));
        // userInfo.setLastLogonDate(userInfo.getCurrentLogonDate());
        // userInfo.setCurrentLogonDate(new Timestamp(System.currentTimeMillis()));
        // this.userInfoDao.update(userInfo);

        return userInfo;
    }

    /**
     * 解除用户锁定
     * 
     * @param userId
     *            用户编号
     */
    /**
     * @param userId
     */
    public void unlockUser(String userId) {
        UserInfo user = this.getUser(userId);

        user.setLockFlag(GlobalConstants.DIC_VALUE_YESNO_NO);
        user.setLockDate(null);

        userInfoDao.update(user);
    }

    /**
     * 取得用户的权限列表(包括：自己私有的、所属组的)
     * 
     * @param userId
     * @return
     */
    public List getAllAuthorityListOfUser(String userId) {

        // 获得用户对象
        UserInfo user = (UserInfo) this.getUser(userId);

        System.out.println("user.getDisplayName():" + user.getDisplayName());

        // 获得用户权限
        Set authoritySet = user.getAuthority();
        System.out.println("authoritySet.size():" + authoritySet.size());

        // 获得用户所属组的权限
        Set groupSet = user.getRole();
        System.out.println("groupSet.size():" + groupSet.size());

        if (groupSet != null && groupSet.size() > 0) {
            for (Iterator iter = groupSet.iterator(); iter.hasNext();) {
                RoleInfo group = (RoleInfo) iter.next();

                // 组对象
                // RoleInfo groupWithSub = getGroup(group.getRoleId());

                // 组的权限
                Set authorityListOfGroup = group.getAuthority();

                // 将所属组的而用户自己没有的权限加给用户
                ListUtil.mergeList(authoritySet, authorityListOfGroup);
            }
        }

        return new ArrayList(authoritySet);
    }
}
