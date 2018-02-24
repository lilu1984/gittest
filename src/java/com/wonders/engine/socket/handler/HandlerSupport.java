package com.wonders.engine.socket.handler;

import com.wonders.engine.exception.EngineException;
import com.wonders.engine.socket.ClientPipe;
/**
 * handler�ӿ�,���ͻ���ͨ������ӿڡ��������Ӵ���������.
 * @author sunxin
 *
 */
public interface HandlerSupport {
	/**
	 * ��ȡ����.
	 * @param handler �ͻ���ͨ��
	 * @throws EngineException
	 */
	public void getConnection(ClientPipe handler) throws EngineException;
	/**
	 * ��������.
	 * @param handler �ͻ���ͨ��
	 * @param command ����
	 * @throws EngineException
	 */
	public void handleCommand(ClientPipe handler,String command) throws EngineException;
	/**
	 * �Ͽ�����.
	 * @param handler
	 * @throws EngineException
	 */
	public void lostConnection(ClientPipe handler) throws EngineException;


}
