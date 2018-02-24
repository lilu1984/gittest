package com.wonders.tdsc.exchange.services;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;


public class JianchaTest {
	 private void test1() throws Exception {
	        //String endpoint = "http://192.168.7.112:7001/fangdi/services/jcService?wsdl";
	        String endpoint = "http://localhost/qdtdsc/services/jcService?wsdl";
	        Service service = new Service();
	        Call call = (Call) service.createCall();

	        call.getMessageContext().setUsername("landaudit"); // 用户名。
	        call.getMessageContext().setPassword("landaudit"); // 密码

	        call.setTargetEndpointAddress(new java.net.URL(endpoint));
	        call.setOperationName("GetBlockBasic");

//          查询	        
//	        String ret = (String) call.invoke(new Object[] {});
//	        System.out.println(ret);
	        
//	        call.addParameter("BLOCK_ID", XMLType.XSD_STRING, ParameterMode.IN);
//	        String BLOCK_ID = "402887f0249ea59801249f2e41b2003a";
//	        System.out.println(call.invoke(new Object[] {BLOCK_ID}));
	        
	        call.addParameter("beginTime", XMLType.XSD_STRING, ParameterMode.IN);
	        call.addParameter("endTime", XMLType.XSD_STRING, ParameterMode.IN);
	        String beginTime = "2009-10-29 00:20:20";
	        String endTime = "2009-10-29 18:00:00";
	        System.out.println(call.invoke(new Object[] {beginTime,endTime}));

	    }
	 

	    public static void main(String[] args) throws Exception {
	        new JianchaTest().test1();
	    }

}
