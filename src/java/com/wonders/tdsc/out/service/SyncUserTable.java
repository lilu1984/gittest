package com.wonders.tdsc.out.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.wonders.esframework.common.util.StringUtil;
import com.wonders.jdbc.dbmanager.ConnectionManager;
import com.wonders.tdsc.out.jdbc.CaUserConnection;
import com.wonders.tdsc.out.service.bean.UserInfoBean;
import com.wonders.tdsc.out.service.impl.UUIDHexGenerator;

public class SyncUserTable {

	private Connection	orclConn	= ConnectionManager.getInstance().getConnection();
	private Connection	sqlConn		= CaUserConnection.getConnection();

	public static void main(String[] args) {
		// SyncUserTable su = new SyncUserTable();
		// su.getSqlUser("1");
		// su.getSqlUser("0");
		// su.syncUser();
	}

	public boolean syncUser() {
		boolean flag = false;
		// 1. get oracle user;
		List oracleUserList = getOrclUser();
		// 2. get sql user;
		List sqlUserList = getSqlUser("0");
		// 3. get diff user;
		List diffUserList = getDiffUserInfo(sqlUserList, oracleUserList);

		// 4. if (exist) then for each user ( if not exist then insert else next)
		flag = addUser(diffUserList);
		// 5. get SQLSERVER deleted user;
		List sqlDeleteUserList = getSqlUser("1");
		// 6. if exist then delete user ORACLE
		flag = delUser(sqlDeleteUserList);
		return flag;
	}

	private boolean delUser(List diffUsers) {
		try {

			for (int i = 0; i < diffUsers.size(); i++) {
				UserInfoBean ub = (UserInfoBean) diffUsers.get(i);

				String sql = "UPDATE UA_USER_INFO SET STATE=? WHERE PERSON_ID=?";
				PreparedStatement prestmt = orclConn.prepareStatement(sql);
				prestmt.setString(1, "0");
				prestmt.setString(2, ub.getPersonId());

				prestmt.executeUpdate();

				orclConn.commit();
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean addUser(List diffUsers) {
		try {

			for (int i = 0; i < diffUsers.size(); i++) {
				UUIDHexGenerator uuid = new UUIDHexGenerator();

				UserInfoBean ub = (UserInfoBean) diffUsers.get(i);

				StringBuffer sql = new StringBuffer("");
				sql.append("Insert into UA_USER_INFO");
				sql.append(" (USER_ID, LOGON_NAME, PASSWORD, DISPLAY_NAME, REGION_ID, STATE, ");
				sql.append(" CURRENT_LOGON_DATE, LAST_LOGON_DATE, LOGON_FAIL_TIMES, LOCK_FLAG, LOCK_DATE, PERSON_ID)");
				sql.append(" Values (?, ?, ?, ?, NULL, '1', NULL, NULL, NULL, '0', NULL, ?)");

				PreparedStatement prestmt = orclConn.prepareStatement(sql.toString());
				prestmt.setString(1, uuid.generate());
				prestmt.setString(2, ub.getLoginName());
				prestmt.setString(3, ub.getPassword());
				prestmt.setString(4, StringUtil.GBKtoISO88591(ub.getUserName()));
				// prestmt.setString(4, ub.getUserName());
				prestmt.setString(5, ub.getPersonId());

				prestmt.executeUpdate();
				prestmt = null;

				orclConn.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private List getDiffUserInfo(List list1, List list2) {

		List list3 = new ArrayList();
		if (list1 == null || list1.size() == 0) {
			list3 = list2;
		} else if (list2 == null || list2.size() == 0) {
			list3 = list1;
		} else {
			for (int i = 0; i < list1.size(); i++) {// 遍历list1
				boolean isExist = false;
				for (int j = 0; j < list2.size(); j++) {

					// System.out.println("sql personId : " + ((UserInfoBean) list1.get(i)).getPersonId());
					// System.out.println("oracle personId : " + ((UserInfoBean) list2.get(j)).getPersonId());

					if ((((UserInfoBean) list1.get(i)).getPersonId() + "").equals(((UserInfoBean) list2.get(j)).getPersonId() + "")) {
						isExist = true;// 找到相同项，跳出本层循环
						break;
					}

				}
				if (!isExist) {// 不相同，加入list3中
					list3.add(list1.get(i));
					System.out.println("diff user list size is : " + list3.size());
				}
			}

			for (int k = 0; k < list2.size(); k++) {
				list3.add(list2.get(k));
			}
		}
		return list3;

	}

	private List getOrclUser() {
		List orclList = null;
		String orclSql = "SELECT LOGON_NAME FROM UA_USER_INFO WHERE STATE='1'";

		try {
			Statement orclSt = orclConn.createStatement();
			ResultSet orclRs = orclSt.executeQuery(orclSql);

			if (orclRs != null) {
				orclList = new ArrayList();
				int i = 0;
				while (orclRs.next()) {

					UserInfoBean ub = new UserInfoBean();
					ub.setLoginName(orclRs.getString("LOGON_NAME"));
					orclList.add(ub);
					i++;
				}
				// System.out.println(orclList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orclList;
	}

	/**
	 * isDelete 0 available , 1 deleted
	 * 
	 * @param isDelete
	 * @return
	 */
	private List getSqlUser(String isDelete) {
		List sqlList = null;
		String sql = "SELECT ID,LOGONNAME,PERSONNAME, PASSWORD FROM FRAMEPERSON WHERE ISDELETE=" + Integer.parseInt(isDelete) + "";

		try {
			Statement sqlSt = sqlConn.createStatement();
			ResultSet sqlRs = sqlSt.executeQuery(sql);

			if (sqlRs != null) {
				sqlList = new ArrayList();
				int i = 0;
				while (sqlRs.next()) {

					UserInfoBean ub = new UserInfoBean();
					// java.io.Reader reader = (java.io.Reader) sqlRs.getCharacterStream("ID");
					ub.setLoginName(sqlRs.getString("LOGONNAME"));
					ub.setUserName(sqlRs.getString("PERSONNAME"));
					ub.setPersonId(sqlRs.getString("ID"));
					ub.setPassword(sqlRs.getString("PASSWORD"));

					sqlList.add(ub);
					i++;
				}
				System.out.println(sqlList);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sqlList;
	}

}
