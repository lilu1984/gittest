package com.wonders.org.liuy.credlink;

public class SVS extends ServerConntion{
	
	/**
	 * ��ʼ��SVSģ�顣
	 * �粻�Ӳ�������ʹ��setIPAndPort����ip��port
	 * 
	 */
	public SVS()
	{
		super.setIpAndPort("jsca.8866.org", 9188);
	}
	
	/**
	 * ����credlink IP��ַ�Ͷ˿�
	 * 
	 */
	public SVS(String ip,int port)
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
	 * ��֤֤��
	 * @param cert��֤��
	 * @param greenpass ��֤��ɫͨ��
	 * @return ��֤���
	 * @throws Exception
	 */
	public  String getVerifiCert(String cert,String greenpass) 
	{
		//ƴ���ַ�,��query����ƴ�ӽ��
	    add("cert", cert);
	    add("greenpass",greenpass);    
	    setUrl("vc.svr");
	    return getHttpPostRou(query.toString());
	}
	/**
	 * ��֤P1ǩ�����ݣ�����ȡ���ؽ��
	 * @param cert��֤��
	 * @param pkcs1��ǩ����Ϣ
	 * @param sxinput��ǩ������
	 * @return ��֤���
	 * @throws Exception
	 */
	public  String getVerifiPkcs1(String cert,String pkcs1,String sxinput) 
	{
	    return getVerifiPkcs1(cert,pkcs1,sxinput,null,null,null);
	}
	
	/**
	 * ��֤P1ǩ�����ݣ�����ȡ���ؽ��
	 * @param cert��֤��
	 * @param pkcs1��ǩ����Ϣ
	 * @param sxinput��ǩ������
	 * @return ��֤���
	 * @throws Exception
	 */
	public  String getVerifiPkcs1(String cert,String pkcs1,String sxinput,String ip,int port) 
	{
		setIPAndPort(ip,port);
	    return getVerifiPkcs1(cert,pkcs1,sxinput,null,null,null);
	}
	
	
	/**
	 * ��֤P1ǩ�����ݣ�����ȡ���ؽ��
	 * @param cert��֤��
	 * @param pkcs1��ǩ����Ϣ
	 * @param sxinput��ǩ������
	 * @return ��֤���
	 * @throws Exception
	 */
	public  String getVerifiPkcs1(String cert,String pkcs1,String sxinput,String algo,String datt,String greenpass) 
	{
		//ƴ���ַ�,��query����ƴ�ӽ��
	    add("cert", cert);
	    add("signature", pkcs1);
	    add("data", sxinput);
	    if(algo!=null)
	    {
	    	 add("algo", algo);
	    }
	    if(datt!=null)
	    {
	    	 add("datt", datt);
	    }
	    if(greenpass!=null)
	    {
	    	 add("greenpass", greenpass);
	    }  
	    setUrl("vp1.svr");
	    return getHttpPostRou(query.toString());
	}
	/**
	 * ��֤P7ǩ�����ݣ�����ȡ���ؽ��
	 * @param p7data��B64 p7����
	 * @param pkcs1��ǩ����Ϣ
	 * @param sxinput��ǩ������
	 * @return ��֤���
	 * @throws Exception
	 */
	public  String getVerifiPkcs7(String b64P7data) 
	{
	    return getVerifiPkcs7(b64P7data,null);
	}
	
	/**
	 * ��֤P7ǩ�����ݣ�����ȡ���ؽ��
	 * @param p7data��B64 p7����
	 * @param pkcs1��ǩ����Ϣ
	 * @param sxinput��ǩ������
	 * @return ��֤���
	 * @throws Exception
	 */
	public  String getVerifiPkcs7(String b64P7data,String greenpass) 
	{
	    add("p7data", b64P7data);
	    if(greenpass!=null)
	    {
	    	add("greenpass", greenpass);
	    }
		setUrl("vp7.svr");
	    return getHttpPostRou(query.toString());
	}
	
	/**
	 * ��֤XMLǩ��
	 * @param p7data��B64 p7����
	 * @param pkcs1��ǩ����Ϣ
	 * @param sxinput��ǩ������
	 * @return ��֤���
	 * @throws Exception
	 */
	public  String getVerifiXML(String data,String greenpass) 
	{
	    add("data", data);
	    add("greenpass", greenpass);
		setUrl("vx.svr");
	    return getHttpPostRou(query.toString());
	}
}
