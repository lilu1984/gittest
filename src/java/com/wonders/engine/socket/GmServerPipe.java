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
	 * 当前通道套接字
	 */
    private Socket socket;
    
	/**
	 * 客户端通道线程
	 */
	private Thread clientThread = null;
	
	/**
	 * 超时时间还未结束的时候
	 */
	private int socketTimeout;
	
	
    /**
     * 当前通道输入流
     */
    private DataInputStream reader;
    
    /**
     * 当前通道输出流
     */
    private DataOutputStream writer;
    
    /**
     * 连接状态
     */
	private boolean connection = true;

    /**
     * 通道编号
     */
	protected String gmServerNo = "99";
	/**
     * 构造函数.
     * @param coreEngine 核心引擎
     * @param socket 当前通道套接字
     * @param socketTimeout 超时时间还未结束的时候
     */
    public GmServerPipe(String gmServerNo,Socket socket,int socketTimeout) {
		this.socket = socket;
		this.socketTimeout = socketTimeout;
		this.gmServerNo = gmServerNo;
		startClientPipe();
	}
	
	/**
	 * 启动客户端通道线程.
	 *
	 */
	protected void startClientPipe() {
		if (clientThread == null) {
			clientThread = new Thread(this, "Wonders Client Monitor");
			clientThread.start();
		}
	}


	/**
	 * 运行客户端管道
	 */
    public void run() {
        try {
        	//设置socketTimeout
			socket.setSoTimeout(socketTimeout);
            //获取套接字的输入流
            reader = new DataInputStream(socket.getInputStream());
            //获取套接字的输出流
            writer = new DataOutputStream(socket.getOutputStream());
            
        	while(connection){
        		String commandString = getCommand();
        		//处理交易系统发送过来的消息，向所有观摩用户全发消息
        		System.out.println("来自网上交易的消息："+EncodeUtils.decodeBase80(commandString));
        		CoreService.sendCommand2AllGmClient(gmServerNo, EncodeUtils.decodeBase80(commandString));
                

        	}
        } catch (Exception e) {
        	System.out.println("用户退出或者发送的信息有错误:"+e.toString());
        	e.printStackTrace();
        	logger.error("clientPipe error:",e);
        } finally {
            try {
            	CoreService.removeGmServerPipe(gmServerNo);
                // 关闭输入输出流及套接字
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
     * 向自身客户端管道发送消息.
     * @param commandString 消息内容
     */
    public void sendCommand(String commandString){
		//System.out.println("to:"+this.conNum+"号牌，证书："+this.clientNo+",命令:"+commandString);
        try {
        	System.out.println("观摩服务器发送消息："+ commandString);
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
     * 销毁当前链接通道
     * @throws Exception 
     */
    public void disconnect(){
    	try{
        	CoreService.removeGmServerPipe(gmServerNo);
            // 关闭输入输出流及套接字
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
    		logger.error("销毁链接通道出错", ex);
    	}

    }
    /**
     * 接受客户端命令.
     * @return
     * @throws IOException
     */
	private String getCommand() throws IOException {				 
		int firstChar = reader.read();
		if (firstChar == -1)
			throw new RuntimeException("用户退出");			
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
