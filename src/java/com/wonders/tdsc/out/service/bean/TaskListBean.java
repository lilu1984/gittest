package com.wonders.tdsc.out.service.bean;

import java.sql.Timestamp;

public class TaskListBean {
	// 可以存万达系统的 app_id
	private int			actId;
	// 默认值为0，对万达系统无用
	private int			isSend;

	// 用于记录金土工程相关业务ID,自增长.
	private int			Id;
	// 待办任务的标题
	private String		taskTitle;
	// 任务发送者的姓名
	private String		senderName;
	// 任务的获取者，必须是办公平台的内部ID
	private int			personId;
	// 任务发送日期
	private Timestamp	sendTime;
	// 紧急程度 0-普通；1-紧急；2-加急；3-特急
	private int			urgency;
	// 任务状态 0- 待办；1-已办
	private int			state;
	// 任务执行链接地址
	private String		taskUrl;
	// 任务类型，比如：“金土工程”、“土地交易中心”等等
	private String		taskType;
	// 任务完成日期
	// 该字段在State=0待办模式下必须为空；在State更新成1已办模式时必须同步更新为该任务的完成日期

	private Timestamp	completeTime;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public int getUrgency() {
		return urgency;
	}

	public void setUrgency(int urgency) {
		this.urgency = urgency;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Timestamp getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public int getIsSend() {
		return isSend;
	}

	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}
}
