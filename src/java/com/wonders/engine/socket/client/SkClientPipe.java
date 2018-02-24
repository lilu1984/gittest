package com.wonders.engine.socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.engine.auction.AbstractAuction;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.socket.ClientNode;
import com.wonders.engine.socket.ClientPipe;
import com.wonders.engine.socket.command.CommandFactory;
import com.wonders.engine.socket.encode.EncodeUtils;
import com.wonders.engine.socket.handler.HandlerSupport;
import com.wonders.wsjy.wxtrade.WxHandler;

/**
 * �ͻ��˹ܵ�,��Ҫ����ͻ��˺�����ͨ��.
 * @author sunxin
 *
 */
public class SkClientPipe extends ClientPipe implements Runnable{
	
	private Logger logger = Logger.getLogger(SkClientPipe.class);
	
	/**
	 * ��ǰͨ���׽���
	 */
    private Socket socket;
    
	/**
	 * �ͻ���ͨ���߳�
	 */
	private Thread clientThread = null;
	
	/**
	 * ��ʱʱ�仹δ������ʱ��
	 */
	private int socketTimeout;
	
	
    /**
     * ��ǰͨ��������
     */
    private DataInputStream reader;
    
    /**
     * ��ǰͨ�������
     */
    private DataOutputStream writer;
    
    /**
     * ����״̬
     */
	private boolean connection = true;

    
	/**
     * ���캯��.
     * @param coreEngine ��������
     * @param socket ��ǰͨ���׽���
     * @param socketTimeout ��ʱʱ�仹δ������ʱ��
     */
    public SkClientPipe(Socket socket,int socketTimeout) {
		this.socket = socket;
		this.socketTimeout = socketTimeout;
		startClientPipe();
	}
    /**
     * ���캯��.
     * @param coreEngine ��������
     * @param socket ��ǰͨ���׽���
     * @param socketTimeout ��ʱʱ�仹δ������ʱ��
     * @param handlerSupport ͨ�����Ӻ����ݴ�����
     */
	public SkClientPipe(Socket socket,int socketTimeout, HandlerSupport handlerSupport) {
		this.socket = socket;
		this.socketTimeout = socketTimeout;
		this.handlerSupport = handlerSupport;
		startClientPipe();
	}
	
	/**
	 * �����ͻ���ͨ���߳�.
	 *
	 */
	protected void startClientPipe() {
		if (clientThread == null) {
			clientThread = new Thread(this, "Wonders Client Monitor");
			clientThread.start();
		}
	}


	/**
	 * ���пͻ��˹ܵ�
	 */
    public void run() {
        try {
        	//����socketTimeout
			socket.setSoTimeout(socketTimeout);
            //��ȡ�׽��ֵ�������
            reader = new DataInputStream(socket.getInputStream());
            //��ȡ�׽��ֵ������
            writer = new DataOutputStream(socket.getOutputStream());
            //֪ͨ��ȡ����
            handlerSupport.getConnection(this);
            
        	while(connection){
        		String commandString = getCommand();
                //������ǲ�������
                if(!commandString.startsWith("<policy-file-request/>")){
                	handlerSupport.handleCommand(this, EncodeUtils.decodeBase80(commandString));
                	//handlerSupport.handleCommand(this, commandString);
                	System.out.println("���Կͻ��˵���Ϣ��"+EncodeUtils.decodeBase80(commandString));
                }else{                    
                	System.out.println("��Ч���"+commandString);
                }
        	}
        } catch (Exception e) {
        	System.out.println("�û��˳����߷��͵���Ϣ�д���:"+e.toString());
        	e.printStackTrace();
        	logger.error("clientPipe error:",e);
        } finally {
            try {
            	CoreService.removeClientPipe(clientNo);
            	//�������Ӷ�ʧ
            	handlerSupport.lostConnection(this);
                // �ر�������������׽���
                if (reader != null){
                    reader.close();
                }
                if (writer != null){
                    writer.close();
                }
                if (socket != null){
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("clientPipe error:",e);
            }
        }
    }

    /**
     * ������ͻ��˹ܵ�������Ϣ.
     * @param commandString ��Ϣ����
     */
    public void sendCommand(String commandString){
		//System.out.println("to:"+this.conNum+"���ƣ�֤�飺"+this.clientNo+",����:"+commandString);
        try {
        	System.out.println("������������Ϣ��"+ commandString);
			writer.writeUTF(EncodeUtils.encodeBase80(commandString));
			//writer.writeUTF(commandString);
	        writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("clientPipe error:",e);
		}
    }
    /**
     * ���ٵ�ǰ����ͨ��
     * @throws Exception 
     */
    public void disconnect(){
    	try{
        	CoreService.removeClientPipe(clientNo);
        	//�������Ӷ�ʧ
        	handlerSupport.lostConnection(this);
            // �ر�������������׽���
            if (reader != null){
                reader.close();
            }
            if (writer != null){
                writer.close();
            }
            if (socket != null){
                socket.close();
            }
    	}catch(Exception ex){
    		logger.error("��������ͨ������", ex);
    	}

    }
    /**
     * ���ܿͻ�������.
     * @return
     * @throws IOException
     */
	private String getCommand() throws IOException {				 
		int firstChar = reader.read();
		if (firstChar == -1)
			throw new RuntimeException("�û��˳�");			
		int length = reader.available();
		byte[] array = new byte[length + 1];
		array[0] = (byte) firstChar;
		reader.read(array, 1, length);
		return new String(array,"utf-8");
	}

}