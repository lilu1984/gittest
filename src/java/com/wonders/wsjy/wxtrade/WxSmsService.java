package com.wonders.wsjy.wxtrade;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.engine.sms.SmsSupport;

/**
 * ���Ͷ���Ϣ����.
 * 
 * @author sunxin
 * 
 */
public class WxSmsService implements SmsSupport {

	private Logger logger = Logger.getLogger(WxSmsService.class);
	/**
	 * ���Ͷ���Ϣ��������ʽ.
	 */
	public void sendSms(String commandNo, Map paramMap) {
		logger.info("����"+commandNo+"�����Ӧ����Ϣ");
		WxSmsSend smsSend = new WxSmsSend(commandNo, paramMap);
		Thread thread = new Thread(smsSend, "smssend");
		thread.start();
		
	}

	/**
	 * ���Ͷ���Ϣ����������ʽ.
	 */
	public void sendSms(String mobiles, String msg) {
		logger.info("�Է�������ʽ���Ͷ���Ϣ");
		if(StringUtils.isNotBlank(msg)){
			WxSmsSend smsSend = new WxSmsSend();
			smsSend.sendSms(mobiles, msg);
			Thread thread = new Thread(smsSend, "smssend");
			thread.start();
		}
	}

}
