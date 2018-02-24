package com.wonders.engine.timer;

import java.util.TimerTask;

/**
 * ִ������
 * 
 * ����ʱ��ʱ��ִ�е�����
 * 
 * @author Gordon
 *
 */
public abstract class Task extends TimerTask {

	/**
	 * ȡ�õ�ǰ����ı��
	 * 
	 * @return
	 */
	public abstract String getTaskId();
	
	/**
	 * ����뱾����Ŀͻ��˷��͵���ʱ
	 *
	 */
	public abstract void sendCurrentTimes();
}
