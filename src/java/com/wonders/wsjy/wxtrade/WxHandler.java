package com.wonders.wsjy.wxtrade;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wonders.engine.CoreService;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.socket.ClientPipe;
import com.wonders.engine.socket.command.CommandFactory;
import com.wonders.engine.socket.event.PriceSubmitEvent;
import com.wonders.engine.socket.event.YhSubmitEvent;
import com.wonders.engine.socket.handler.HandlerSupport;
import com.wonders.wsjy.TradeConsts;
/**
 * 无锡通道实现，用户处理客户端的请求，目前主要处理21和22号命令.
 * @author sunxin
 *
 */
public class WxHandler implements HandlerSupport{

	public void getConnection(ClientPipe handler) throws EngineException {
		//发送初始化命令
		handler.sendCommandByNo(TradeConsts.ORDER19,null);
	}

	public void handleCommand(ClientPipe handler, String command) throws EngineException {
		
		
	}

	private void send31Time(ClientPipe handler) throws EngineException{
		String commandString = CommandFactory.getInstance().genEngineCommand(TradeConsts.ORDER31, null, null);
		handler.sendCommand(commandString);
	}
	
	public void lostConnection(ClientPipe handler) throws EngineException {
		System.out.println("Closing connection : "+ handler.getClientNo());
	}

}
