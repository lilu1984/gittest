package com.wonders.engine.socket.command;

import java.util.Map;

/**
 * ������չ�ӿ�
 * @author sunxin
 *
 */
public interface CommandExpand {
	/**
	 * ��������
	 * @param paramArray ��������
	 * @return
	 */
	public String genCommand(Map paramMap);
}
