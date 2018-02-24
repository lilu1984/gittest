package com.wonders.org.liuy.credlink;


/**
 * 实现credlink TSA功能
 * 
 * @author liuy
 * @version 2010-009-28
 */
public class TSA extends ServerConntion{
	
	
	/**
	 * 初始化TSA模块。
	 * 如不加参数，需使用setIPAndPort设置ip和port
	 * 
	 */
	public TSA()
	{
		
	}
	
	/**
	 * 设置credlink IP地址和端口
	 * 
	 */
	public TSA(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}
	
	/**
	 * 设置credlink IP地址和端口
	 * 
	 */
	public void setIPAndPort(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}

	/**
	 * 签发时间戳
	 * @param   digest HEX形式散列信息
	 * @param   algo   散列算法
	 * @return  消息回应码 200表示成功
	 */
	public  String createTSA(String digest,String algo)
	{
		setUrl("tsac.svr");
		add("digest", digest);
		add("algo", algo);	
		return getHttpPostRou(query.toString());
	}
	
	/**
	 * 验证时间戳
	 * @param   tsr Base64时间戳信息
	 * @param   digest HEX形式散列信息
	 * @param   algo   散列算法
	 * @return  消息回应码 200表示成功
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
