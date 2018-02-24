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
 * �û�����ҵ������
 * 
 * �������ڣ�2007-1-8 13:15:16
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
     * ���ݲ�ѯ�������һҳ�û����б�
     * 
     * @param condition
     *            ��ѯ��������
     * @return PageList һҳ�û����б���������
     */
    public PageList queryUserList(UserSearchCondition condition) {
        return userInfoDao.findPageList(condition);
    }

    /**
     * ��֤��¼���Ƿ����
     * 
     * @param loginName
     *            ��¼��
     * @param userId
     *            �û����
     * @return true������ false��������
     */
    public boolean isLogonNameExist(String loginName, String userId) {
        return userInfoDao.isLogonNameExist(loginName, userId);
    }

    /**
     * �����û�����
     * 
     * @param id
     *            �û����
     * @return �û���¼��
     */
    public String resumeUserPassword(String id) {
        // �õ��û�
        UserInfo userInfo = (UserInfo) this.getUser(id);

        // �ó�ʼ����
        userInfo.setPassword(SecurityUtil.getMD5Password(AuthorityConstants.DEFAULT_PASSWORD));

        // ��������
        userInfoDao.update(userInfo);

        return userInfo.getLogonName();
    }

    /**
     * �޸��û�����
     * 
     * @param id
     *            �û�����
     * @return void
     */
    public void changeUserPassword(UserInfo userInfo) {
        userInfoDao.update(userInfo);
    }

    /**
     * ����һ���û�����Ϣ����ӵ�������ڵ�����û�ӵ�е�Ȩ�ޣ�
     * 
     * @param user
     *            �û�����
     * @param groupList
     *            ���б�
     * @return UserInfo �û�����
     */
    public UserInfo addUser(UserInfo user, String[] groups, String[] authorities) {
        // ��������״̬��Ĭ�ϲ�����
        user.setLockFlag(GlobalConstants.DIC_VALUE_YESNO_NO);

        // �ж��û���¼���Ƿ��ظ�
        if (this.userInfoDao.isLogonNameExist(user.getLogonName(), null)) {
            throw new BusinessException("�û���¼�� " + user.getLogonName() + " �Ѿ���ʹ�ã�");
        }

        // �����������ӵ�е�Ȩ��
        user.setRole(new HashSet(roleInfoDao.findListByGroups(groups)));
        user.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // �����û�����
        userInfoDao.save(user);

        return user;
    }

    /**
     * �޸�һ���û�����Ϣ����ӵ�������ڵ��飩
     * 
     * @param user
     *            �û�����
     * @param groupList
     *            ���б�
     * @return UserInfo �û�����
     */
    public UserInfo modifyUser(UserInfo user, String[] groups, String[] authorities) {

        // �ж��û���¼���Ƿ��ظ�
        if (this.userInfoDao.isLogonNameExist(user.getLogonName(), user.getUserId())) {
            throw new BusinessException("�û���¼�� " + user.getLogonName() + " �Ѿ���ʹ�ã�");
        }

        // �����������ӵ�е�Ȩ��
        user.setRole(new HashSet(roleInfoDao.findListByGroups(groups)));
        user.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // �����û�����
        userInfoDao.update(user);

        return user;
    }

    /**
     * ɾ��һ���û�����������ɾ������״̬��Ϊ�����ã�
     * 
     * @param id
     */
    public void removeUser(String id) {
        // ����û�����
        UserInfo userInfo = (UserInfo) userInfoDao.get(id);

        // ���û�״̬��Ϊ��Ч
        userInfo.setState(GlobalConstants.DIC_VALUE_VALID_INVALIDITY);

        userInfoDao.update(userInfo);
    }

    /**
     * �����û��������û���Ϣ
     * 
     * @param id
     *            �û�����
     * @return �û���Ϣ����
     */
    public UserInfo getUser(String id) {
        UserInfo user = (UserInfo) userInfoDao.loadWithLazy(id, new String[] { "authority", "role" });

        if (user == null) {
            throw new BusinessException("δ�ҵ����û���Ϣ��");
        }

        return user;
    }

    /**
     * ����һ���û������
     * 
     * @param group
     *            �û������
     * @param userList
     *            �û��б�
     * @return �û������
     */
    public RoleInfo addGroup(RoleInfo group, String[] users, String[] authorities) {
        // �ж��û��������Ƿ��ظ�
        if (this.roleInfoDao.isGroupNameExist(group.getRoleDesc(), null)) {
            throw new BusinessException("�û������� " + group.getRoleDesc() + " �Ѿ����ڣ�����������!");
        }

        // ���ð����û���ӵ�е�Ȩ��
        group.setUser(new HashSet(userInfoDao.findListByUsers(users)));
        group.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // �����û�����
        roleInfoDao.save(group);

        return group;
    }

    /**
     * �޸�һ���û���
     * 
     * @param group
     *            �û������
     * @param userList
     *            �û��б�
     * @return RoleInfo �û������
     */
    public RoleInfo modifyGroup(RoleInfo group, String[] users, String[] authorities) {
        // �ж��û��������Ƿ��ظ�
        if (this.roleInfoDao.isGroupNameExist(group.getRoleDesc(), group.getRoleId())) {
            throw new BusinessException("�û������� " + group.getRoleDesc() + " �Ѿ����ڣ�����������!");
        }

        // ���ð����û���ӵ�е�Ȩ��
        group.setUser(new HashSet(userInfoDao.findListByUsers(users)));
        group.setAuthority(new HashSet(authorityTreeDao.findListByAuthorities(authorities)));

        // �����û�����
        roleInfoDao.update(group);

        return group;
    }

    /**
     * ɾ���û������
     * 
     * @param id
     *            �û�����
     */
    public void removeGroup(String id) {
        // 3��ɾ���û�����Ϣ
        roleInfoDao.deleteById(id);
    }

    /**
     * ���ݲ�ѯ�������һҳ�û�����б�
     * 
     * @param condition
     *            ��ѯ��������
     * @return PageList һҳ�û�����б���������
     */
    public PageList queryGroupList(UserSearchCondition condition) {
        return roleInfoDao.findPageList(condition);
    }

    /**
     * �����û���������û�����Ϣ
     * 
     * @param id
     *            �û������
     * @return �û�����Ϣ����
     */
    public RoleInfo getGroup(String id) {
        RoleInfo group = (RoleInfo) roleInfoDao.loadWithLazy(id, new String[] { "authority", "user" });

        if (group == null) {
            throw new BusinessException("δ�ҵ����û�����Ϣ��");
        }

        return group;
    }

    /**
     * ȡ�����е��û����б�
     * 
     * @return list �û����б�
     */
    public List getAllGroup() {
        return roleInfoDao.findAll();
    }

    /**
     * ȡ�����е��û��б�
     * 
     * @return list �û��б�
     */
    public List getAllUser() {
        return userInfoDao.findValidityUserList();
    }

    /**
     * �û���¼��
     * 
     * @param logonName
     *            ��¼��
     * @param password
     *            ����
     * @return Object �û���Ϣ�����������Ϣ
     */
    public Object logonByPersonId(String personId) {
        // ȡ��¼�û�
        List list = userInfoDao.findLogonByPersonId(personId);
        if (list == null || list.size() < 1) {
            return "�û������ڣ�";
        }
        UserInfo userInfo = (UserInfo) list.get(0);
        return userInfo;
    }
    /**
     * CA�û���¼��
     * @param logonName
     * @return
     */
    public Object logon(String logonName){
        List list = userInfoDao.findLogon(logonName);
        if (list == null || list.size() < 1) {
            return "������û��������ڣ�";
        }
        UserInfo userInfo = (UserInfo) list.get(0);
        return userInfo;
    }
    
    /**
     * �û���¼��
     * 
     * @param logonName
     *            ��¼��
     * @param password
     *            ����
     * @return Object �û���Ϣ�����������Ϣ
     */
    public Object logon(String logonName, String password) {
        // ȡ��¼�û�
        List list = userInfoDao.findLogon(logonName);
        if (list == null || list.size() < 1) {
            return "������û��������ڣ�";
        }
        UserInfo userInfo = (UserInfo) list.get(0);

        // ����״̬�жϺʹ���
        // if (userInfo.getLockFlag() != null && userInfo.getLockFlag().equals(GlobalConstants.DIC_VALUE_YESNO_YES)) {
        // // �û���������ȡ���Զ������ķ�����
        // int minutes = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
        // GlobalConstants.PROPERTY_USER_UNLOCK_MINUTES)).intValue();
        // if (minutes < 1 || userInfo.getLockDate() == null) {
        // // �޷��Զ�����
        // return "�û������������ܵ�¼��";
        // }
        // if (userInfo.getLockDate().getTime() + minutes * 60000 > System.currentTimeMillis()) {
        // // δ������ʱ��
        // return "�û������������ܵ�¼��";
        // }
        //
        // // �Ѿ�����������������־
        // userInfo.setLockFlag(GlobalConstants.DIC_VALUE_YESNO_NO);
        // userInfo.setLockDate(null);
        // }
        // �������
        String sPwd = SecurityUtil.getMD5Password(password);

        // ����Ƚ�
        if (sPwd.equals(userInfo.getPassword()) == false) {
            // ������û����벻��ȷ���ж�������¼ʧ�ܴ���
            int times = ((Integer) DicPropertyUtil.getInstance().getPropertyValue(
                    GlobalConstants.PROPERTY_USER_lOCK_LOGON_TIMES)).intValue();
            if (times > 0) {
                // ����������¼ʧ�ܴ���
                int failTimes = 1;
                if (userInfo.getLogonFailTimes() != null) {
                    failTimes = userInfo.getLogonFailTimes().intValue() + 1;
                }
                userInfo.setLogonFailTimes(new Integer(failTimes));

                if (failTimes >= times) {
                    // ������¼ʧ�ܴ����ﵽ������Ҫ�������û�
                    userInfo.setLockFlag(GlobalConstants.DIC_VALUE_YESNO_YES);
                    userInfo.setLockDate(new Timestamp(System.currentTimeMillis()));
                    logger.info("�û�����������¼����" + logonName);
                }
            }

            this.userInfoDao.update(userInfo);
            return "��������벻��ȷ��";
        }

        // ��¼�ɹ�������������¼ʧ�ܴ���
        // userInfo.setLogonFailTimes(new Integer(0));
        // userInfo.setLastLogonDate(userInfo.getCurrentLogonDate());
        // userInfo.setCurrentLogonDate(new Timestamp(System.currentTimeMillis()));
        // this.userInfoDao.update(userInfo);

        return userInfo;
    }

    /**
     * ����û�����
     * 
     * @param userId
     *            �û����
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
     * ȡ���û���Ȩ���б�(�������Լ�˽�еġ��������)
     * 
     * @param userId
     * @return
     */
    public List getAllAuthorityListOfUser(String userId) {

        // ����û�����
        UserInfo user = (UserInfo) this.getUser(userId);

        System.out.println("user.getDisplayName():" + user.getDisplayName());

        // ����û�Ȩ��
        Set authoritySet = user.getAuthority();
        System.out.println("authoritySet.size():" + authoritySet.size());

        // ����û��������Ȩ��
        Set groupSet = user.getRole();
        System.out.println("groupSet.size():" + groupSet.size());

        if (groupSet != null && groupSet.size() > 0) {
            for (Iterator iter = groupSet.iterator(); iter.hasNext();) {
                RoleInfo group = (RoleInfo) iter.next();

                // �����
                // RoleInfo groupWithSub = getGroup(group.getRoleId());

                // ���Ȩ��
                Set authorityListOfGroup = group.getAuthority();

                // ��������Ķ��û��Լ�û�е�Ȩ�޼Ӹ��û�
                ListUtil.mergeList(authoritySet, authorityListOfGroup);
            }
        }

        return new ArrayList(authoritySet);
    }
}
