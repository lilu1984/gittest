package com.wonders.wsjy.bank;

import java.net.ServerSocket;

import org.apache.log4j.Logger;


public class BankServerThread extends Thread{
	private Logger logger = Logger.getLogger(BankServerThread.class);
	public void run(){
		try{
			ServerSocket server= new ServerSocket(5678);   
			while(true){   
				BankServer mu=new BankServer(server.accept());   
				mu.start();
			}  
		}catch(Exception ex){
			logger.error(ex.toString());
		}
	}
}
