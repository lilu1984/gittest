package com.wonders.wsjy.wxtrade;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.engine.sms.SmsSupport;

/**
 * 发送短消息服务.
 * 
 * @author sunxin
 * 
 */
public class WxSmsService implements SmsSupport {

	private Logger logger = Logger.getLogger(WxSmsService.class);
	/**
	 * 发送短消息，命令形式.
	 */
	public void sendSms(String commandNo, Map paramMap) {
		logger.info("发送"+commandNo+"命令对应短消息");
		WxSmsSend smsSend = new WxSmsSend(commandNo, paramMap);
		Thread thread = new Thread(smsSend, "smssend");
		thread.start();
		
	}

	/**
	 * 发送短消息，非命令形式.
	 */
	public void sendSms(String mobiles, String msg) {
		logger.info("以非命令形式发送短消息");
		if(StringUtils.isNotBlank(msg)){
			WxSmsSend smsSend = new WxSmsSend();
			smsSend.sendSms(mobiles, msg);
			Thread thread = new Thread(smsSend, "smssend");
			thread.start();
		}
	}

}
