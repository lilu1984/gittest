package com.wonders.tdsc.thirdpart.axis.test;

import org.apache.axis.encoding.Base64;

public class TestByte {
	  public static void main(String[] args) throws Exception {
	        //new TestTdscService().test1();
	      String tempStr = "���������ط־Ӱ�˹������־������������տ��˴���������ˣ����Ⱦ������ˣ����������˿����������ط־�";
		  
	      String tempStr_1 = new String(tempStr.getBytes("GBK"), "ISO-8859-1");
	      
		  System.out.println("tempStr_1====>" + tempStr_1);
	      String tempStr_11 = new String(tempStr_1.getBytes("ISO-8859-1"), "GBK");
	      
		  System.out.println("tempStr_11====>" + tempStr_11);
		  
		  
		  String tempStr_2 = new String(tempStr.getBytes("ISO-8859-1"), "GBK");
		  
		  System.out.println("tempStr_2====>" + tempStr_2);
		  
		  String tempStr_22 = new String(tempStr_2.getBytes("GBK"), "ISO-8859-1");
		  
		  System.out.println("tempStr_22====>" + tempStr_22);
	    }

}
