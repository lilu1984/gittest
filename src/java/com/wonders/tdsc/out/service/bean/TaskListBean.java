package com.wonders.tdsc.out.service.bean;

import java.sql.Timestamp;

public class TaskListBean {
	// ���Դ����ϵͳ�� app_id
	private int			actId;
	// Ĭ��ֵΪ0�������ϵͳ����
	private int			isSend;

	// ���ڼ�¼�����������ҵ��ID,������.
	private int			Id;
	// ��������ı���
	private String		taskTitle;
	// �������ߵ�����
	private String		senderName;
	// ����Ļ�ȡ�ߣ������ǰ칫ƽ̨���ڲ�ID
	private int			personId;
	// ����������
	private Timestamp	sendTime;
	// �����̶� 0-��ͨ��1-������2-�Ӽ���3-�ؼ�
	private int			urgency;
	// ����״̬ 0- ���죻1-�Ѱ�
	private int			state;
	// ����ִ�����ӵ�ַ
	private String		taskUrl;
	// �������ͣ����磺���������̡��������ؽ������ġ��ȵ�
	private String		taskType;
	// �����������
	// ���ֶ���State=0����ģʽ�±���Ϊ�գ���State���³�1�Ѱ�ģʽʱ����ͬ������Ϊ��������������

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
