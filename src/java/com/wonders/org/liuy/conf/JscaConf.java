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
		 //读取真实路径
		 String realPath=this.getServletContext().getRealPath("/");
		 //读取初始化的配置文件信息
         String conf=this.getInitParameter("config");
         if(conf!=null)
         {
        	 //指定路径
        	 setPara(realPath+conf);
         }
         else
         {
        	 //默认路径
        	 setPara();
         }
	}
	/**
	 * 读取配置文件,并转化为HashMap
	 * 默认路径为src文件下，默认名称为：jsca.properties
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
	 * 读取配置文件,并转化为HashMap
	 * 默认路径为src文件下，默认名称为：jsca.properties
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
	 * 读取配置文件,并转化为HashMap
	 * @param fileName 配置文件名称，文件需放在src文件下
	 * @param field 属性名称
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
