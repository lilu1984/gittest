package com.wonders.org.liuy.credlink;


/**
 * ʵ��credlink pcs����
 * 
 * @author liuy
 * @version 2010-009-28
 */
public class PCS extends ServerConntion{
	
	
	/**
	 * ��ʼ��PCSģ�顣
	 * �粻�Ӳ�������ʹ��setIPAndPort����ip��port
	 * 
	 */
	public PCS()
	{
		
	}
	
	/**
	 * ����credlink IP��ַ�Ͷ˿�
	 * 
	 */
	public PCS(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}
	
	/**
	 * ����credlink IP��ַ�Ͷ˿�
	 * 
	 */
	public void setIPAndPort(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}

	/**
	 * ��ȡ������key iD,����Ϊ32 ���ֽ�
	 */
	public  String getKeyID()
	{
		setUrl("sl.svr");
		return getHttpPostRou("2");
	}
	
	
	/**
	 * ����keyID��ȡ��Ӧ֤��
	 * 
	 */
	public  String getCertByKeyID(String keyid)
	{
		setUrl("sg.svr");
		add("id", keyid);
		System.out.println(query.toString());
		return getHttpPostRou(query.toString());
	}
	
	
	/**
	 * ˽Կ����
	 * 
	 */
	public  String encryptByPrivateKey(String keyid,String password,String data)
	{
		setUrl("spe.svr");
		add("id", keyid);
		add("password", password);
		add("data", data);
		return getHttpPostRou(query.toString());
	}
	
	/**
	 * ˽Կ����
	 * 
	 */
	public  String decryptByPrivateKey(String keyid,String password,String data)
	{
		setUrl("spd.svr");
		add("id", keyid);
		add("password", password);
		add("data", data);
		return getHttpPostRou(query.toString());
	}
	 
	/**
	 * ����pkcs1ǩ����
	 * 
	 * @param algo���㷨��
	 * 				1 RSA-SHA1��ȱʡ��
	 *              2 RSA-MD5
	 * @param datt���������͡�
	 * 				0: ԭ�� ��ȱʡ��
	 *				1: ժҪ(�����Ƹ�ʽ)
	 *				2: ժҪ(HEX string ��ʽ)
	 * 
	 */
	public  String createPKCS1(String keyid,String password,String data,String algo,String datt)
	{
		setUrl("smp1.svr");
		add("id", keyid);
		add("passwd", password);
		add("data", data);
		add("algo", algo);
		add("datt", datt);
		return getHttpPostRou(query.toString());
	}
	
	
	 /**
	 * ����pkcs7ǩ��
	 * 
	 * @param data��BASE64 ����Ĵ�ǩ�����ݣ����ݳ��Ȳ���
	 * @param fullchain���Ƿ��������֤����
	 * 				0 ������ ��ȱʡ��
	 *				1 ����
	 */
	public  String createPKCS7(String keyid,String password,String data,String fullchain)
	{
		setUrl("smp7.svr");
		add("id", keyid);
		add("passwd", password);
		add("data", data);
		add("fullchain", fullchain);
		return getHttpPostRou(query.toString());
	}
	 
	 /**
	 * ����XMLǩ��
	 * 
	 * @param data��ԭ��
	 * @param sigmode��ǩ��ģʽ��
	 * 				0 enveloped��ȱʡ��
	 *				1 envelopin
	 */
	public  String createXMLSign(String keyid,String password,String data,String sigmode)
	{
		setUrl("sxs.svr");
		add("id", keyid);
		add("password", password);
		add("data", data);
		add("sigmode", sigmode);
		return getHttpPostRou(query.toString());
	}
}
