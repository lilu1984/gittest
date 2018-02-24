package com.wonders.engine.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wonders.engine.CoreService;
import com.wonders.engine.socket.encode.EncodeUtils;
@ServerEndpoint(value = "/wsClient")  
public class GmClientPipe {
	private Logger logger = Logger.getLogger(GmClientPipe.class);
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	private String clientNo;
	
	public GmClientPipe(){
		
	}
    /**
     * 连接状态
     */
	private boolean connection = true;
	
	/**
	 * 观摩的公告
	 */
	public String noticeId;
	
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	/**
	 * 连接建立成功调用的方法
	 * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		
		System.out.println("有新连接加入！");
	}
	
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
    	
    	//设置连接丢失
    	try {
			CoreService.removeGmClientPipe(clientNo);
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
    		System.out.println("WS通道接收来自客户端的消息:" +  commandString);
    		//仅仅接受21与26号命令。之后可以开一个类来重构此方法
    		if(!"".equals(commandString)){
    			JSONObject jsonObject = null;
    			jsonObject = new JSONObject(commandString);
    			String op = jsonObject.getString("op");
    			if("21".equals(op)){
    				String clientNo = jsonObject.getString("clientNo");
    				this.clientNo = clientNo;
    				CoreService.addGmClientPipe(clientNo, this);
    			}else if("26".equals(op)){
    				String noticeId = jsonObject.getString("noticeId");
    				this.noticeId = noticeId;
    			}
    		}
        	
        	
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
		CoreService.removeGmClientPipe(clientNo);
		logger.error("onError：",error);
		
	}
	
	/**
	 * 向客户端管道发送消息
	 * @param commandString
	 */
	public void sendCommand(String commandString) {
		// TODO Auto-generated method stub
		try {
			System.out.println("向WS客户端发送消息："+ commandString);
			if(this.session!=null&&this.session.isOpen()){
				this.session.getBasicRemote().sendText(EncodeUtils.encodeBase80(commandString));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
