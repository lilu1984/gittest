package com.wonders.org.liuy.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JscaConf extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1869954986308809989L;
	public static HashMap mp=null;
	public JscaConf() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void init() throws ServletException {
		 //��ȡ��ʵ·��
		 String realPath=this.getServletContext().getRealPath("/");
		 //��ȡ��ʼ���������ļ���Ϣ
         String conf=this.getInitParameter("config");
         if(conf!=null)
         {
        	 //ָ��·��
        	 setPara(realPath+conf);
         }
         else
         {
        	 //Ĭ��·��
        	 setPara();
         }
	}
	/**
	 * ��ȡ�����ļ�,��ת��ΪHashMap
	 * Ĭ��·��Ϊsrc�ļ��£�Ĭ������Ϊ��jsca.properties
	 * @throws IOException 
	 * 
	 */
	public final void setPara() 
	{
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream("jsca.properties");
		 setPara(is);
		 if(is != null) 
	     {
	         try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	     }
	}
	
	/**
	 * ��ȡ�����ļ�,��ת��ΪHashMap
	 * Ĭ��·��Ϊsrc�ļ��£�Ĭ������Ϊ��jsca.properties
	 * @throws IOException 
	 * 
	 */
	public final void setPara(String filePath) 
	{
		File f=new File(filePath);
		InputStream is=null;
		try {
			is = new FileInputStream(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 setPara(is);
		 if(is != null) 
	     {
	         try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	     }
	}
	
	/**
	 * ��ȡ�����ļ�,��ת��ΪHashMap
	 * @param fileName �����ļ����ƣ��ļ������src�ļ���
	 * @param field ��������
	 * 
	 */
	public final void setPara(InputStream in)
	{
        Properties prop = new Properties();
        HashMap hm=new HashMap();
        try{
            prop.load(in);
            Enumeration paramNames = prop.propertyNames();  
    		while(paramNames.hasMoreElements()) 
    		{
    			String key = (String)paramNames.nextElement();  
    			String value = prop.getProperty(key);
    			hm.put(key, value);
    		}
            mp= hm;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
