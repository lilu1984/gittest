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
	//��ĳ���ͻ��˵����ӻỰ����Ҫͨ���������ͻ��˷�������
	private Session session;
	public WsClientPipe(){
		handlerSupport = BaseStore.getHandlerSupport();
	}
    /**
     * ����״̬
     */
	private boolean connection = true;
	/**
	 * ���ӽ����ɹ����õķ���
	 * @param session  ��ѡ�Ĳ�����sessionΪ��ĳ���ͻ��˵����ӻỰ����Ҫͨ���������ͻ��˷�������
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
		System.out.println("�������Ӽ��룡");
	}
	
	/**
	 * ���ӹرյ��õķ���
	 */
	@OnClose
	public void onClose(){
    	
    	//�������Ӷ�ʧ
    	try {
    		CoreService.removeClientPipe(clientNo);
			handlerSupport.lostConnection(this);
			if(this.session!=null&&this.session.isOpen()){
				this.session.close();
			}
			System.out.println("��һ���ӹرգ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("onClose:",e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * �յ��ͻ�����Ϣ����õķ���
	 * @param message �ͻ��˷��͹�������Ϣ
	 * @param session ��ѡ�Ĳ���
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		 try {
    		String commandString = EncodeUtils.decodeBase80(message);
        	handlerSupport.handleCommand(this, commandString);
        	System.out.println("���Կͻ��˵���Ϣ:" +  commandString);
	    } catch (Exception e) {
	    	logger.error("onMessage:",e);
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * ��������ʱ����
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("��������");
		error.printStackTrace();
		logger.error("onError��",error);
		
	}
	

	@Override
	public void sendCommand(String commandString) {
		// TODO Auto-generated method stub
		try {
			System.out.println("������������Ϣ��"+ commandString);
			if(this.session!=null&&this.session.isOpen()){
				this.session.getBasicRemote().sendText(EncodeUtils.encodeBase80(commandString));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
