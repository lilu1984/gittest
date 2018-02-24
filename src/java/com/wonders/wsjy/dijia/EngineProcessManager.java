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
	 * ��ʱ��
	 */
	private Timer timer;
	
	/**
	 * ��ʱ����
	 */
	private Map taskMap;
	
	/**
	 * Ϊ������Ӽ���
	 * 
	 * ��Ϊ��������Ӽ�����ʱ�����ȼ����������Ƿ��и�����
	 * 	������и��������Ƚ�����ȡ������ʵ�ӳ���ɾ��������
	 * 
	 * ����µ�����
	 * 
	 * @param timerTask ����
	 * @param times ����ִ�е�Ƶ�ʣ�����)
	 */
	public void addTimerListener(Task timerTask, int times) {
		if (timerTask == null)
			throw new RuntimeException("�������ʧ��");
		// �ж��Ƿ��Ѿ����ڶ�ʱ������
		if (taskMap.containsKey(timerTask.getTaskId())) {
			// ������ڽ�ԭ�еĶ�ʱ������ȡ��
			synchronized (taskMap) {
				TimerTask task = (TimerTask)taskMap.get(timerTask.getTaskId());
				if (task != null) {
					// ȡ��ԭ������
					task.cancel();
					task = null;
					// ɾ��
					taskMap.remove(timerTask.getTaskId());
				}
			}
		}
		taskMap.put(timerTask.getTaskId(), timerTask);
		timer.schedule(timerTask, 0, times);
	}
	
	/**
	 * ����ָ�������еĵ�ǰ����
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
	 * ����ʱ�������
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
