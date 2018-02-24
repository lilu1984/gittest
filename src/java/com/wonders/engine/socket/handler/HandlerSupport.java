package com.wonders.engine.socket.handler;

import com.wonders.engine.exception.EngineException;
import com.wonders.engine.socket.ClientPipe;
/**
 * handler接口,即客户端通道处理接口。负责连接处理和命令处理.
 * @author sunxin
 *
 */
public interface HandlerSupport {
	/**
	 * 获取连接.
	 * @param handler 客户端通道
	 * @throws EngineException
	 */
	public void getConnection(ClientPipe handler) throws EngineException;
	/**
	 * 处理命令.
	 * @param handler 客户端通道
	 * @param command 命令
	 * @throws EngineException
	 */
	public void handleCommand(ClientPipe handler,String command) throws EngineException;
	/**
	 * 断开连接.
	 * @param handler
	 * @throws EngineException
	 */
	public void lostConnection(ClientPipe handler) throws EngineException;


}
