package com.wonders.engine.socket.listener;

import com.wonders.engine.socket.event.ChangeEvent;
/**
 * 监听变化事件.
 * @author sunxin
 *
 */
public interface ChangeListener {
	
	/**
	 * 主要处理引起场景变化的事件.
	 * @param event 变化事件
	 */
	public abstract void handleChangeEvent(ChangeEvent event);
}
