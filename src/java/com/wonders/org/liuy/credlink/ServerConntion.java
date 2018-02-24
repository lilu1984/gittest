package com.wonders.org.liuy.credlink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * ʵ�ַ���������
 * 
 * 
 * @author liuy
 * @version 2010-09-28
 */
class ServerConntion{
	
	protected  StringBuffer query=null;
	private  URL url=null;
	private  String ip=null;
	private  int port=0;
	private String data=null;
	
	private final static Logger logger = Logger.getLogger(ServerConntion.class.getName()) ;
	private void setContent(String data) {
		this.data = data;
	}
	protected ServerConntion()
	{
		query=new StringBuffer();
		url=null;
	}
	/**
	 * ���÷������ݣ�����ƴ��
	 * @param name��ƴ�ӵ�����
	 * @param value��ƴ�ӵ�ֵ
	 * @throws Exception
	 */
	protected  synchronized void add(String name, String value) {
		encode(name, value);
	}

	private  synchronized void encode(String name, String value) 
	{
		try 
		{
			query.append(URLEncoder.encode(name, "UTF-8"));
			query.append('=');
			query.append(URLEncoder.encode(value, "UTF-8"));
			query.append('&');		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	
	/**
	 * ����credlink��ַ
	 * @param ip����ַ
	 * @param port���˿�
	 * @param type������
	 * @return URL
	 * @throws Exception
	 */
	protected  URL setUrl(String type)
	{
		String urlS="http://"+ip+":"+port+"/"+type;
		try {
			url=new URL(urlS);
			return url;
		} catch (MalformedURLException e) {
			logger.error(urlS+":����ʧ�ܣ�����url�Ƿ���ȷ");
		}	
		return null;
	}
	/**
	 * ���÷�������ַ
	 * @param ip����ַ
	 * @param port���˿�
	 */
	protected  void setIpAndPort(String ip,int port)
	{
		this.ip=ip;
		this.port=port;
	}
	
	/**
	 * ���Ӳ�����post����
	 * 
	 */
	protected  String getHttpPostRou(String data)
	{	    
		HttpURLConnection con=null;
		try {
			con = (HttpURLConnection)url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", "5000"); 
			System.setProperty("sun.net.client.defaultReadTimeout", "5000"); 
			con.setRequestMethod("POST");
	        con.setDoOutput(true);
		    OutputStreamWriter out=null;
			out = new OutputStreamWriter(con.getOutputStream(),"ASCII");	
			out.write(data);
		    out.write("\r\n");
		    out.write("\r\n");
			out.flush();
			out.close();
			
			//��ȡ��Ӧͷ
			String statusLine = con.getHeaderField(0);
			statusLine = statusLine.substring(9, 12);
			
			//��ȡ��������
			if(statusLine.equals("200"))
			{
			     InputStream is = con.getInputStream();
			     byte[] info=inputStream2Byte(is);
			     //�ж�info�Ƿ�ΪB64����
			     if (Base64.isArrayByteBase64(info)) 
			     {
			    	 setContent(new String(info));
			      } 
			      else 
			      {
			    	  //setContent(Base64.encodeBase64String(info));
			      }
			}
			else
			{
				logger.error("����������������Ƿ���ȷ��"+data);
			}
			return statusLine;		
		} catch (IOException e) {
			logger.error(url.toString()+":����ʧ�ܣ�",e);
		}	
		 finally 
		 {
			   if (con != null)
			   {
				   con.disconnect();
			   }
			   query.delete(0,query.length());  
		 }
		return null;
	}
	
	/**
	 * ��ȡ��Ӧ����
	 * 
	 */
	public String getContent() {
		return data;
	}
	
	/** 
     * ��InputStreamת����String 
     * @param is 
     * @return 
     * @throws IOException 
     */  
	public static  byte[] inputStream2Byte(InputStream is) throws IOException {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        int i = -1;  
        while ((i = is.read()) != -1) {  
            baos.write(i);  
        }  
        return baos.toByteArray();
    }  

}
