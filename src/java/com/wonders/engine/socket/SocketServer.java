package com.wonders.engine.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.wonders.engine.BaseStore;
import com.wonders.engine.socket.client.SkClientPipe;

/**
 * 核心引擎.
 * @author sunxin
 *
 */
public class SocketServer implements Runnable {

	private int socketTimeout = 0;// never lost

	private ServerSocket server = null;
	//默认端口1234
	private int serverPort = 2662;
	public SocketServer() {
	}

	public SocketServer(int serverPort,int socketTimeout) {
		this.serverPort = serverPort;
		this.socketTimeout = socketTimeout;
	}

	/**
	 * 运行服务器. 
	 */
	public void run() {
		try {
			//启动引擎
			server = new ServerSocket(serverPort);
			while (true) {
				Socket client = server.accept();
				new SkClientPipe(client, socketTimeout, BaseStore.getHandlerSupport());
			}
		} catch (IOException e) {
			System.err.println("\n\tSocketServer IOError :" + e);
		} catch (Exception e) {
			System.err.println("\n\tSocketServer Error :" + e);
		} finally {
			try {
				if (server != null){
					server.close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
}
