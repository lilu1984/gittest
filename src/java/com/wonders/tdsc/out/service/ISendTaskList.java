package com.wonders.tdsc.out.service;

import com.wonders.tdsc.out.service.bean.TaskListBean;

public interface ISendTaskList {
	
	/**
	 * ����ʼ�����øýӿڡ�
	 * @param 1. id 2. taskTitle 3. senderName 4. personId ������ID, ��CA�д��ڵ�user 5. taskUrl ���ӵ����ϵͳ�� URL
	 * @return boolean
	 * @throws Exception
	 */
	public boolean send(TaskListBean task) throws Exception;
	
	/**
	 * ������ɣ����øýӿڡ�
	 * @param taskId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean finish(int appId) throws Exception;
}
