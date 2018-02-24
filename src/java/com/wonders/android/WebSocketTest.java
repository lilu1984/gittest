package com.wonders.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DecodeException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.engine.socket.ClientPipe;
import com.wonders.engine.socket.client.websocket.WsClientPipe;
import com.wonders.engine.socket.encode.EncodeUtils;
import com.wonders.util.PropertiesUtil;
import com.wonders.wsjy.bank.BankClient;


@ClientEndpoint
public class WebSocketTest {

	public static Logger log = Logger.getLogger(WebSocketTest.class);
	public Logger logger = WebSocketTest.log;
	private String deviceId;
	private static Map<String,WebSocketTest> clientPipe = new HashMap();
	private Session session;
	
	public static void clertClient(){
		for(String key : clientPipe.keySet()){
			WebSocketTest test =  (WebSocketTest)clientPipe.get(key);
			test.disconnect();
		}
		clientPipe.clear();
	}
	public WebSocketTest () {
		
	}
	
	public WebSocketTest (String deviceId) {
		this.deviceId = deviceId;
	}

	
	
	@OnOpen
	public void init(Session session) {
		this.session = session;
	}


	@OnMessage
	public void onMessage(String userInfo) throws Exception {
		String commandString = EncodeUtils.decodeBase80(userInfo);
		System.out.println("client:"+ commandString);
	}


	@OnError
	public void handleError(Throwable thw) {
		if (thw instanceof DecodeException) {
			logger.error(session.getId()+" Error decoding incoming message: " + ((DecodeException) thw).getText());
		} else {
			logger.error(session.getId()+" Client WebSocket error: " + thw.getMessage());
		}
	}
	public boolean start() {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		String uri = "ws://192.168.1.114/wsClient";
		try {
			session = container.connectToServer(WebSocketTest.class, URI.create(uri));
			String userId = "GM001"+ new Date().getTime()+deviceId;
			clientPipe.put(userId, this);
			String testStr = "{\"op\":\"21\",\"clientNo\":\""+ userId +"\"}";
			sendUserInfo(testStr);
			String noticeId = "a0bf47865b139058015b13ac69260005";
			testStr = "{\"op\":\"23\",\"noticeId\":\"" + noticeId + "\"}\"";
			sendUserInfo(testStr);
			testStr = "{\"op\":\"26\",\"noticeId\":\"" + noticeId + "\"}\"";
			sendUserInfo(testStr);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	* 向服务端发送数据
	* @param userInfo
	*/
	public void sendUserInfo(String text) {
		try {
			this.session.getBasicRemote().sendText(EncodeUtils.encodeBase80(text));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* 断开与服务端的连接
	*/
	public void disconnect() {
		if (this.session != null) {
			try {
				this.session.close();
			} catch (IOException ioe) {
				//System.out.println("Error closing the session: " + ioe);
			}
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "4e05eeyJvcCI6IjIxIiwiY2xpZW50Tm8iOiJHTTAwMWEwYmdf03eY0NzgzNWFkYTJhMGMwMTVhZGEyZTcxNDMwMDA5In0=87478d";
		String result = EncodeUtils.decodeBase80(str);
		System.out.print(result);
		
		String commandString = "{\"op\":\"21\",\"clientNo\":\"GM001a0bf47835ada2a0c015ada2e71430009\"}";
		commandString = EncodeUtils.encodeBase80(commandString);
		System.out.print(commandString);
	}
}