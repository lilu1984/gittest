package com.wonders.wsjy.bank;
import java.net.*;   
import java.io.*;   

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * ���нӿڷ������˷�����
 * @author Administrator
 *
 */
public class BankServer extends Thread{	
	private Logger logger = Logger.getLogger(BankServer.class);
	private Socket client;
	private BufferedReader m_in;
	private PrintWriter m_out;
	private int m_len;
	private String m_xml;
	private BankService m_bankService;
	public BankServer(Socket c){
		this.client = c;
		logger.info("*******����ͻ�������ʼip��ַΪ:"+this.client.getInetAddress());
		this.m_bankService = new BankService();
	}
	/**
	 * ��ͻ��˷�����Ϣ
	 * @param msg
	 * @throws EngineException
	 */
	private void sendMsg2Client(String msg) throws EngineException{
		try{
			logger.info("------���ؿͻ��˵Ļ�ִ��ϢΪ:"+msg);
			m_out.print(msg);
			m_out.flush();
		}catch(Exception ex){
			throw new EngineException("������Ϣʱ����,�������"+ex.getMessage(),ex);
		}
		
	}
	/**
	 * ��ȡ�ͻ�����Ϣ
	 * @return
	 * @throws EngineException
	 */
	private void getClientMsg() throws EngineException{
		try{
			char[] temp = new char[6];
			m_in.read(temp, 0, 6);
			String len = new String(temp);
			this.m_len = Integer.parseInt(len);
			char[] temp2 = new char[this.m_len];
			m_in.read(temp2, 0, this.m_len);
			this.m_xml =  new String(temp2);
		}catch(Exception ex){
			throw new EngineException("��ȡ�ͻ�����Ϣʱ����,�������"+ex.getMessage(),ex);
		}
	}
	/**
	 * �رտͻ���
	 * @throws EngineException
	 */
	private void closeClient() throws EngineException{
		try{
			if(this.m_in!=null){
				this.m_in.close();
			}
			if(this.m_out!=null){
				this.m_out.close();
			}
			if(client!=null){
				this.client.close();
			}
		}catch(Exception ex){
			throw new EngineException("�رտͻ���ͨ��ʱ����,�������"+ex.getMessage(),ex);
		}
	}
	/**
	 * У��XML�Ƿ��ж���
	 * @return
	 * @throws EngineException 
	 */
	private boolean checkXml() throws EngineException{
		if(this.m_xml.length()!=this.m_len){
			String msg = "xml�ַ������뱨��ͷ��ǵĲ�һ�£�";
			String outStr = this.m_bankService.sendException2Client("", msg, BankService.ERR09);
			this.sendMsg2Client(outStr);
			System.out.print(msg);
			return false;
		}
		return true;
	}
	public void run(){
		try{
			//����������
			this.m_in = new BufferedReader(new InputStreamReader(client.getInputStream(),"GBK")); 
			//���������
			this.m_out= new PrintWriter(new OutputStreamWriter(client.getOutputStream(),"GBK"), true);
			this.getClientMsg();
			if(this.checkXml()){
				logger.info("------��ȡ�ͻ��˱�����Ϣ�����ĳ���Ϊ"+this.m_len+"������ϢΪ-------"+this.m_xml);
				String outStr = this.m_bankService.ExecuteServer(StringUtils.trimToEmpty(this.m_xml));
				this.sendMsg2Client(outStr);
				this.m_bankService.saveWsjyBankLog(this.client.getInetAddress().toString(), this.m_xml, "01", "", outStr, "01");
			}
		    this.closeClient();
		}catch(Exception ex){
			try{
				String errStr = this.m_bankService.sendException2Client("", ex.getMessage(), BankService.ERR99);
				this.sendMsg2Client(errStr);
				this.m_bankService.saveWsjyBankLog(this.client.getInetAddress().toString(), this.m_xml, "01", "", errStr, "02");
				logger.info("------���ܿͻ��˱��ĳ���-------"+ex.toString());
				this.closeClient();
			}catch(Exception exx){
				System.out.print(exx.toString());
			}
		}finally{

		}
	}
}
