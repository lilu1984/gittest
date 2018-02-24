package com.wonders.wsjy.dijia;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.wonders.engine.floorprice.Scene;
import com.wonders.engine.timer.Task;

public class EngineProcessManager {

	private static EngineProcessManager enigeProcessManager = null;
	
	private EngineProcessManager() {
		timer = new Timer();
		taskMap = new LinkedHashMap();
	}
	
	public static EngineProcessManager getInstance() {
		if (enigeProcessManager == null) {
			synchronized (EngineProcessManager.class) {
				if (enigeProcessManager == null) {
					enigeProcessManager = new EngineProcessManager();
				}
			}
		}
		return enigeProcessManager;
	}

	/**
	 * 计时器
	 */
	private Timer timer;
	
	/**
	 * 计时器池
	 */
	private Map taskMap;
	
	/**
	 * 为任务添加监听
	 * 
	 * 当为新任务添加监听器时，首先检查任务池内是否含有该任务：
	 * 	如果含有该任务，首先将任务取消，其实从池中删除该任务
	 * 
	 * 添加新的任务
	 * 
	 * @param timerTask 任务
	 * @param times 任务执行的频率（毫秒)
	 */
	public void addTimerListener(Task timerTask, int times) {
		if (timerTask == null)
			throw new RuntimeException("任务监听失败");
		// 判断是否已经存在定时器任务
		if (taskMap.containsKey(timerTask.getTaskId())) {
			// 如果存在将原有的定时器任务取出
			synchronized (taskMap) {
				TimerTask task = (TimerTask)taskMap.get(timerTask.getTaskId());
				if (task != null) {
					// 取消原有任务
					task.cancel();
					task = null;
					// 删除
					taskMap.remove(timerTask.getTaskId());
				}
			}
		}
		taskMap.put(timerTask.getTaskId(), timerTask);
		timer.schedule(timerTask, 0, times);
	}
	
	/**
	 * 跳过指定场景中的当前交易
	 * 
	 * @param taskId
	 */
	public void nextElement(String taskId) {
		if (taskMap.containsKey(taskId)) {
			synchronized (taskMap) {
				//TimerTask task = (TimerTask)taskMap.get(taskId);
				Scene scene = (Scene)taskMap.get(taskId);
				scene.next();
			}
		}
	}
	
	/**
	 * 销毁时间管理器
	 *
	 */
	public void destory() {
		timer.cancel();
		timer = null;
		taskMap.clear();
		taskMap = null;
		enigeProcessManager = null;
	}
}
