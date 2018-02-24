package com.wonders.engine.sms;

import java.util.Map;
/**
 * 无锡短信接口.
 * @author sunxin
 *
 */
public interface SmsSupport {
	/**
	 * 发送短信接口.
	 * @param commandNo 命令编号
	 * @param paramMap 参数map
	 */
	public void sendSms(String commandNo,Map paramMap);
	
	
	/**
	 * 发送短信
	 * @param mobiles
	 * @param msg
	 */
	public void sendSms(String mobiles, String msg);
}
