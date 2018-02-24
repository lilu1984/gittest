package com.wonders.util;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	  private static PropertiesUtil instance = new PropertiesUtil();


	  private Properties p = new Properties();

	  private PropertiesUtil()
	  {
	    try
	    {
	      InputStream is = PropertiesUtil.class.getResourceAsStream("/wonders/wonders_init.properties");

	      this.p.load(is);

	      is.close();
	    } catch (Exception ex) {
	      
	    }
	  }

	  public static PropertiesUtil getInstance()
	  {
	    return instance;
	  }

	  public String getProperty(String pKey)
	  {
	    return this.p.getProperty(pKey);
	  }
}
