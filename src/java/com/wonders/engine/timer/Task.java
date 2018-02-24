package com.wonders.engine.timer;

import java.util.TimerTask;

/**
 * 执行任务
 * 
 * 倒计时定时器执行的任务
 * 
 * @author Gordon
 *
 */
public abstract class Task extends TimerTask {

	/**
	 * 取得当前任务的编号
	 * 
	 * @return
	 */
	public abstract String getTaskId();
	
	/**
	 * 向参与本公告的客户端发送倒计时
	 *
	 */
	public abstract void sendCurrentTimes();
}
