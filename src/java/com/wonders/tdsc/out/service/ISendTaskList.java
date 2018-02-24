package com.wonders.tdsc.out.service;

import com.wonders.tdsc.out.service.bean.TaskListBean;

public interface ISendTaskList {
	
	/**
	 * 任务开始，调用该接口。
	 * @param 1. id 2. taskTitle 3. senderName 4. personId 接收者ID, 在CA中存在的user 5. taskUrl 链接到万达系统的 URL
	 * @return boolean
	 * @throws Exception
	 */
	public boolean send(TaskListBean task) throws Exception;
	
	/**
	 * 任务完成，调用该接口。
	 * @param taskId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean finish(int appId) throws Exception;
}
