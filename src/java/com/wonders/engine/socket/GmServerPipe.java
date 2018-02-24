package com.wonders.engine.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.wonders.engine.BaseStore;
import com.wonders.engine.CoreService;
import com.wonders.engine.socket.client.SkClientPipe;
import com.wonders.engine.socket.encode.EncodeUtils;
import com.wonders.engine.socket.handler.HandlerSupport;

public class GmServerPipe implements Runnable{
	private Logger logger = Logger.getLogger(GmServerPipe.class);
	
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
     * ͨ�����
     */
	protected String gmServerNo = "99";
	/**
     * ���캯��.
     * @param coreEngine ��������
     * @param socket ��ǰͨ���׽���
     * @param socketTimeout ��ʱʱ�仹δ������ʱ��
     */
    public GmServerPipe(String gmServerNo,Socket socket,int socketTimeout) {
		this.socket = socket;
		this.socketTimeout = socketTimeout;
		this.gmServerNo = gmServerNo;
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
            
        	while(connection){
        		String commandString = getCommand();
        		//������ϵͳ���͹�������Ϣ�������й�Ħ�û�ȫ����Ϣ
        		System.out.println("�������Ͻ��׵���Ϣ��"+EncodeUtils.decodeBase80(commandString));
        		CoreService.sendCommand2AllGmClient(gmServerNo, EncodeUtils.decodeBase80(commandString));
                

        	}
        } catch (Exception e) {
        	System.out.println("�û��˳����߷��͵���Ϣ�д���:"+e.toString());
        	e.printStackTrace();
        	logger.error("clientPipe error:",e);
        } finally {
            try {
            	CoreService.removeGmServerPipe(gmServerNo);
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
        	System.out.println("��Ħ������������Ϣ��"+ commandString);
        	writer.write(EncodeUtils.encodeBase80(commandString).getBytes());
			//writer.writeUTF(commandString);
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
        	CoreService.removeGmServerPipe(gmServerNo);
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
		String result = new String(array,"utf-8");
		if(result.length()>2){
			result = result.substring(2, result.length());
		}
		
		return result;
	}
}
