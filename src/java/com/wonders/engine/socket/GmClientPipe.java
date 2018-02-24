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
	//��ĳ���ͻ��˵����ӻỰ����Ҫͨ���������ͻ��˷�������
	private Session session;
	private String clientNo;
	
	public GmClientPipe(){
		
	}
    /**
     * ����״̬
     */
	private boolean connection = true;
	
	/**
	 * ��Ħ�Ĺ���
	 */
	public String noticeId;
	
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	/**
	 * ���ӽ����ɹ����õķ���
	 * @param session  ��ѡ�Ĳ�����sessionΪ��ĳ���ͻ��˵����ӻỰ����Ҫͨ���������ͻ��˷�������
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		
		System.out.println("�������Ӽ��룡");
	}
	
	/**
	 * ���ӹرյ��õķ���
	 */
	@OnClose
	public void onClose(){
    	
    	//�������Ӷ�ʧ
    	try {
			CoreService.removeGmClientPipe(clientNo);
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
    		System.out.println("WSͨ���������Կͻ��˵���Ϣ:" +  commandString);
    		//��������21��26�����֮����Կ�һ�������ع��˷���
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
	 * ��������ʱ����
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("��������");
		error.printStackTrace();
		CoreService.removeGmClientPipe(clientNo);
		logger.error("onError��",error);
		
	}
	
	/**
	 * ��ͻ��˹ܵ�������Ϣ
	 * @param commandString
	 */
	public void sendCommand(String commandString) {
		// TODO Auto-generated method stub
		try {
			System.out.println("��WS�ͻ��˷�����Ϣ��"+ commandString);
			if(this.session!=null&&this.session.isOpen()){
				this.session.getBasicRemote().sendText(EncodeUtils.encodeBase80(commandString));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
