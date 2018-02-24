package com.wonders.engine.socket.client.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.log4j.Logger;
import com.wonders.engine.BaseStore;
import com.wonders.engine.CoreService;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.socket.ClientPipe;
import com.wonders.engine.socket.encode.EncodeUtils;
@ServerEndpoint(value = "/wsClientBak")  

public class WsClientPipe extends ClientPipe{
	private Logger logger = Logger.getLogger(WsClientPipe.class);
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	public WsClientPipe(){
		handlerSupport = BaseStore.getHandlerSupport();
	}
    /**
     * 连接状态
     */
	private boolean connection = true;
	/**
	 * 连接建立成功调用的方法
	 * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		try {
			handlerSupport.getConnection(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("有新连接加入！");
	}
	
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
    	
    	//设置连接丢失
    	try {
    		CoreService.removeClientPipe(clientNo);
			handlerSupport.lostConnection(this);
			if(this.session!=null&&this.session.isOpen()){
				this.session.close();
			}
			System.out.println("有一连接关闭！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("onClose:",e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		 try {
    		String commandString = EncodeUtils.decodeBase80(message);
        	handlerSupport.handleCommand(this, commandString);
        	System.out.println("来自客户端的消息:" +  commandString);
	    } catch (Exception e) {
	    	logger.error("onMessage:",e);
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("发生错误");
		error.printStackTrace();
		logger.error("onError：",error);
		
	}
	

	@Override
	public void sendCommand(String commandString) {
		// TODO Auto-generated method stub
		try {
			System.out.println("服务器发送消息："+ commandString);
			if(this.session!=null&&this.session.isOpen()){
				this.session.getBasicRemote().sendText(EncodeUtils.encodeBase80(commandString));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
