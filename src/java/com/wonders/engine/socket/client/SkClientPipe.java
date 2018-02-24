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
 * 客户端管道,主要负责客户端和引擎通信.
 * @author sunxin
 *
 */
public class SkClientPipe extends ClientPipe implements Runnable{
	
	private Logger logger = Logger.getLogger(SkClientPipe.class);
	
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
     * 构造函数.
     * @param coreEngine 核心引擎
     * @param socket 当前通道套接字
     * @param socketTimeout 超时时间还未结束的时候
     */
    public SkClientPipe(Socket socket,int socketTimeout) {
		this.socket = socket;
		this.socketTimeout = socketTimeout;
		startClientPipe();
	}
    /**
     * 构造函数.
     * @param coreEngine 核心引擎
     * @param socket 当前通道套接字
     * @param socketTimeout 超时时间还未结束的时候
     * @param handlerSupport 通道连接和数据处理器
     */
	public SkClientPipe(Socket socket,int socketTimeout, HandlerSupport handlerSupport) {
		this.socket = socket;
		this.socketTimeout = socketTimeout;
		this.handlerSupport = handlerSupport;
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
            //通知获取连接
            handlerSupport.getConnection(this);
            
        	while(connection){
        		String commandString = getCommand();
                //如果不是策略请求
                if(!commandString.startsWith("<policy-file-request/>")){
                	handlerSupport.handleCommand(this, EncodeUtils.decodeBase80(commandString));
                	//handlerSupport.handleCommand(this, commandString);
                	System.out.println("来自客户端的消息："+EncodeUtils.decodeBase80(commandString));
                }else{                    
                	System.out.println("无效命令："+commandString);
                }
        	}
        } catch (Exception e) {
        	System.out.println("用户退出或者发送的信息有错误:"+e.toString());
        	e.printStackTrace();
        	logger.error("clientPipe error:",e);
        } finally {
            try {
            	CoreService.removeClientPipe(clientNo);
            	//设置连接丢失
            	handlerSupport.lostConnection(this);
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
        	System.out.println("服务器发送消息："+ commandString);
			writer.writeUTF(EncodeUtils.encodeBase80(commandString));
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
        	CoreService.removeClientPipe(clientNo);
        	//设置连接丢失
        	handlerSupport.lostConnection(this);
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
		return new String(array,"utf-8");
	}

}