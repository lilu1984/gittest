package com.wonders.wsjy.bank;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.safehaus.uuid.UUIDGenerator;

import com.wonders.esframework.common.util.DateUtil;
import com.wonders.esframework.common.util.StringUtil;
import com.wonders.tdsc.common.util.AppContextUtil;
import com.wonders.tdsc.thirdpart.castor.util.CastorFactory;
import com.wonders.wsjy.bank.bo.BankBody;
import com.wonders.wsjy.bank.bo.BankHead;
import com.wonders.wsjy.bank.bo.BankPart;

public class TestBank {
	private static String MAP_PATH = "D://myproject//devprojects//wxeq//web//WEB-INF//classes//wonders//wdtdscmap//bankMap.xml";
	public static void main(String[] args) throws Exception {
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><Head><TransCode>G00003</TransCode><TransDate>20130522</TransDate><TransTime>171317</TransTime><SeqNo>1cc7b91b371e40e3aeb936644c5d4630</SeqNo></Head><body><Result>00</Result><AcctNo>14400701000036202900032</AcctNo><AddWord>交易成功:A503086618</AddWord></body></root>";
		BankPart result = (BankPart)CastorFactory.unMarshalBean(MAP_PATH, xml);
		if(result==null){
			
		}
		//Socket server =new Socket("127.0.0.1",5678);   
		//DataInputStream in = new DataInputStream(server.getInputStream());   
		//DataOutputStream out = new DataOutputStream(server.getOutputStream());
		//String xml = "000402<?xml version='1.0' encoding='GBK'?><root><head><TransCode>G00001</TransCode><TransDate>20060816</TransDate ><TransTime>174300</TransTime><SeqNo>2005081600000001</SeqNo></head><body><InstCode>632501040009782</InstCode><InDate>20120912165600</InDate><InAmount>500</InAmount><InName>竞买人1</InName><InAcct>222222222</InAcct><InMemo>123456</InMemo><InBankFLCode>2005081600000001</InBankFLCode></body></root>";

		//String str = null;
		//String strText = "";
		//while(true){
		//	out.writeUTF(xml);
		//	strText = in.readUTF();
		//	if(StringUtils.isNotEmpty(strText)){
		//		break;
		//	}
		//}
		//System.out.print("server:" + strText);
		//server.close(); 
		//TestBank service = new TestBank();
		//service.sendException2Client("G00007", "信息有误!","01");		
	}
	/**
	 * 向客户端发送错误消息
	 * @param code 发送消息的业务代码
	 * @param msg
	 * @return
	 */
	public String sendException2Client(String code,String msg,String errCode){
		BankPart resultPart = new BankPart();
		resultPart.setBankHead(this.getHead(code));
		BankBody resultBankBody = new BankBody();
		resultBankBody.setResult(errCode);
		resultBankBody.setAddWord(msg);
		resultPart.setBankBody(resultBankBody);
		String resultXml = CastorFactory.marshalBean(resultPart, MAP_PATH);
		//		发送报文前补齐6位大小
		resultXml = StringUtils.leftPad(String.valueOf(resultXml.length()), 6, "0") + resultXml;
		return resultXml;
	}
	/**
	 * 获取 head
	 * @param code
	 * @return
	 */
	private BankHead getHead(String code){
		BankHead head = new BankHead();
		head.setTransCode(code);
		head.setTransDate(DateUtil.date2String(new Date(), "yyyyMMdd"));
		head.setTransTime(DateUtil.date2String(new Date(), "HHmmss"));
		head.setSeqNo(UUIDGenerator.getInstance().generateRandomBasedUUID().toString().replaceAll("-", ""));
		return head;
	}
}
