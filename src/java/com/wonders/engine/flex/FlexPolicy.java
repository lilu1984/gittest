package com.wonders.engine.flex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * 策略线程，向连接引擎的客户端发送策略文件.
 * 
 * @author sunxin
 * 
 */
public class FlexPolicy implements Runnable {

    public static final int PORT = 2663;

    private ServerSocket serverSocket;

    private static final String xml = "<cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\"  to-ports=\"*\"/></cross-domain-policy>";

    private Logger logger = Logger.getLogger(FlexPolicy.class);
    
    public FlexPolicy() {
        init();
    }

    private void init() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (!this.serverSocket.isClosed()) {
            System.out.println("===安全策略端口等待===");

            Socket client = null;
            BufferedReader bin = null;
            BufferedWriter bout = null;

            try {
                client = serverSocket.accept();

                bin = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                bout = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "utf-8"));
                
                
                char[] c = new char[22];
                bin.read(c, 0, 22);
                String s = new String(c);
                if (s.equals("<policy-file-request/>")) {
                    System.out.println("===接收到客户端<policy-file-request/>请求===");
                    bout.write(xml + "\0");
                    bout.flush();
                }
            } catch (Exception e) {
                logger.error("=======策略端口错误========"+e.toString());
                System.out.print("=======策略端口错误========"+e.toString());
                e.printStackTrace();
            } finally {
                try {
                    bin.close();
                    bout.close();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
