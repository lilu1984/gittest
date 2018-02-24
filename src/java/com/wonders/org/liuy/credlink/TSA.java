package com.wonders.org.liuy.credlink;


/**
 * ʵ��credlink TSA����
 * 
 * @author liuy
 * @version 2010-009-28
 */
public class TSA extends ServerConntion{
	
	
	/**
	 * ��ʼ��TSAģ�顣
	 * �粻�Ӳ�������ʹ��setIPAndPort����ip��port
	 * 
	 */
	public TSA()
	{
		
	}
	
	/**
	 * ����credlink IP��ַ�Ͷ˿�
	 * 
	 */
	public TSA(String ip,int port)
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
	 * ǩ��ʱ���
	 * @param   digest HEX��ʽɢ����Ϣ
	 * @param   algo   ɢ���㷨
	 * @return  ��Ϣ��Ӧ�� 200��ʾ�ɹ�
	 */
	public  String createTSA(String digest,String algo)
	{
		setUrl("tsac.svr");
		add("digest", digest);
		add("algo", algo);	
		return getHttpPostRou(query.toString());
	}
	
	/**
	 * ��֤ʱ���
	 * @param   tsr Base64ʱ�����Ϣ
	 * @param   digest HEX��ʽɢ����Ϣ
	 * @param   algo   ɢ���㷨
	 * @return  ��Ϣ��Ӧ�� 200��ʾ�ɹ�
	 */
	public  String verifyTSA(String tsr,String digest,String algo)
	{
		setUrl("tsav.svr");
		add("tsr", tsr);
		add("digest", digest);
		add("algo", algo);	
		return getHttpPostRou(query.toString());
	}
	
	
}
