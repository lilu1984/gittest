package com.wonders.engine.sms;

import java.util.Map;
/**
 * �������Žӿ�.
 * @author sunxin
 *
 */
public interface SmsSupport {
	/**
	 * ���Ͷ��Žӿ�.
	 * @param commandNo ������
	 * @param paramMap ����map
	 */
	public void sendSms(String commandNo,Map paramMap);
	
	
	/**
	 * ���Ͷ���
	 * @param mobiles
	 * @param msg
	 */
	public void sendSms(String mobiles, String msg);
}
