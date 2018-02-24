package com.wonders.engine.socket.command;

import java.util.Map;

/**
 * 命令扩展接口
 * @author sunxin
 *
 */
public interface CommandExpand {
	/**
	 * 生成命令
	 * @param paramArray 参数数组
	 * @return
	 */
	public String genCommand(Map paramMap);
}
