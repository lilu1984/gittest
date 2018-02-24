package com.wonders.wsjy.bank;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.hibernate.id.UUIDHexGenerator;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.exchange.castor.CastorFactory;
import com.wonders.wsjy.bank.bo.BankBody;
import com.wonders.wsjy.bank.bo.BankHead;
import com.wonders.wsjy.bank.bo.BankPart;

/**
 * ���нӿڿͻ���������
 * @author Administrator
 *
 */
public class BankClient {
	private Logger logger = Logger.getLogger(BankClient.class);
	private String MAP_PATH = AppContextUtil.getInstance().getCastorMapLocation() + File.separator + "bankMap.xml";
	/**
	 * �����������˺�����
	 */
	public static String G00003 = "G00003";
	/**
	 * ��ȡ��֤��������
	 */
	public static String G00005 = "G00005";
	/**
	 * δ�ɽ���֤��鼯֪ͨ
	 */
	public static String G00007 = "G00007";
	/**
	 * �ɽ���֤��鼯֪ͨ
	 */
	public static String G00008 = "G00008";
	/**
	 * ������·����
	 */
	public static String G00009 = "G00009";
	
	private String m_sendXml;
	private String m_recallXml;
	private Socket m_server;
	public BankClient(Socket server){
		this.m_server = server;
	}
	public String getSendXml(){
		return this.m_sendXml;
	}
	public String getRecallXml(){
		return this.m_recallXml;
	}
	public BankPart sendClient(String code,BankBody body) throws MarshalException, ValidationException, IOException, MappingException{
		BankPart bank = new BankPart();
		bank.setBankHead(this.getHead(code));
		bank.setBankBody(body);
		String strXml = CastorFactory.marshalBean(bank, this.MAP_PATH);
		//���ͱ���ǰ����6λ��С
	    strXml = StringUtils.leftPad(String.valueOf(strXml.length()), 6, "0") + strXml;
	    logger.info("------���͵ı���-------"+strXml);
	    this.m_sendXml = strXml;
		//DataInputStream in = new DataInputStream(m_server.getInputStream());   
		//DataOutputStream out = new DataOutputStream(m_server.getOutputStream());
	    BufferedReader in =new BufferedReader(new InputStreamReader(m_server.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter
                (m_server.getOutputStream(),"GBK"), true); 

		String strText = "";
		out.print(strXml);
		out.flush();
		logger.info("------���ͱ��ĳɹ�-------");
		//strText = in.readUTF();
		String str = null;
		while((str= in.readLine())!=null){
			strText += str;
		}
		logger.info("-----��ȡ���Ļ�ִ�ɹ�:"+strText);
		this.m_recallXml = strText;
		//��ȡxml
		strText = strText.substring(6,strText.length());
		BankPart result = (BankPart)CastorFactory.unMarshalBean(this.MAP_PATH, strText);
		in.close();
		out.close();
		if(result!=null){
			System.out.print("server:" + strText);
		}
		m_server.close(); 
		return result;
	}
	
	public BankHead getHead(String code){
		BankHead head = new BankHead();
		head.setTransCode(code);
		head.setTransDate(DateUtil.date2String(new Date(), "yyyyMMdd"));
		head.setTransTime(DateUtil.date2String(new Date(), "HHmmss"));
		head.setSeqNo(UUIDGenerator.getInstance().generateRandomBasedUUID().toString().replaceAll("-", ""));
		return head;
	}
	
}
