package com.wonders.engine.socket.listener;

import com.wonders.engine.socket.event.ChangeEvent;
/**
 * �����仯�¼�.
 * @author sunxin
 *
 */
public interface ChangeListener {
	
	/**
	 * ��Ҫ�������𳡾��仯���¼�.
	 * @param event �仯�¼�
	 */
	public abstract void handleChangeEvent(ChangeEvent event);
}
