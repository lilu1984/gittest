package com.wonders.tdsc.out.service.impl;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.wonders.tdsc.out.jdbc.CaUserConnection;
import com.wonders.tdsc.out.service.ISendTaskList;
import com.wonders.tdsc.out.service.bean.TaskListBean;

public class SendTaskListImpl implements ISendTaskList {
	private Connection			conn	= CaUserConnection.getConnection();
	private Statement			st		= null;
	private PreparedStatement	prestmt	= null;
	private ResultSet			rs		= null;

	/**
	 * 任务开始，调用该接口。 注意，由于万达数据库使用是西文，当插入到 表WXGT_LandTaskList中，需要把西文转成汉字
	 * 
	 * @param 1. id 2. taskTitle 3. senderName 4. personId 接收者ID, 在CA中存在的user 5. taskUrl 链接到万达系统的 URL
	 * @return boolean
	 * @throws Exception
	 */
	public boolean send(TaskListBean task) throws Exception {
		StringBuffer sql = new StringBuffer("");
		try {
			sql.append("insert into WXGT_LandTaskList ");
			sql.append("(ID,TaskTitle,SenderName,PersonID,SendTime,Urgency,State,TaskURL,TaskType,CompleteTime,actId,isSend)");
			sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?)");
			prestmt = conn.prepareStatement(sql.toString());

			prestmt.setInt(1, getNextId());// 自增长
			prestmt.setString(2, task.getTaskTitle());
			prestmt.setString(3, task.getSenderName());
			prestmt.setInt(4, task.getPersonId());

			// when send task, send time is current time
			prestmt.setTimestamp(5, getCurrentTime());
			// this field is 0 -- 普通
			prestmt.setInt(6, 0);
			// this field is 0 -- 待办
			prestmt.setInt(7, task.getState());
			prestmt.setString(8, task.getTaskUrl());
			// this field is "土地交易中心"
			prestmt.setString(9, "土地交易中心");
			// when send task, this field is null
			prestmt.setTimestamp(10, null);
			
			prestmt.setInt(11, task.getActId());
			prestmt.setInt(12, 0);

			prestmt.execute();

		} catch (SQLException sqlEx) {
			throw sqlEx;
		} catch (Exception ex) {
			throw ex;
		}

		return true;
	}

	/**
	 * 任务完成，调用该接口。
	 * 
	 * @param taskId
	 * @return boolean
	 * @throws Exception
	 */

	public boolean finish(int appId) throws Exception {
		StringBuffer sql = new StringBuffer("");
		try {
			sql.append("update WXGT_LandTaskList set State=?,CompleteTime=?");
			sql.append(" where actId=?");

			prestmt = conn.prepareStatement(sql.toString());
			prestmt.setInt(1, 1);
			prestmt.setTimestamp(2, getCurrentTime());
			prestmt.setInt(3, appId);

			prestmt.executeUpdate();

		} catch (SQLException sqlEx) {
			throw sqlEx;
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

	private Timestamp getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new Date());
		Timestamp curTime = Timestamp.valueOf(time);
		return curTime;
	}

	private int getNextId() {
		StringBuffer sql = new StringBuffer("");
		int maxId = 0;
		try {
			sql.append("select max(id) from WXGT_LandTaskList");
			st = conn.createStatement();
			rs = st.executeQuery(sql.toString());
			if (rs.next())
				maxId = rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxId + 1;
	}
}
