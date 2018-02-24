package com.wonders.engine.socket.event;
/**
 * ���������仯���¼�.
 * @author sunxin
 *
 */
public interface ChangeEvent {
	/**
	 * �۸��ύ�¼�
	 */
	public static final String 	PRICE_SUBMIT_EVENT = "22";
	/**
	 * �޸ļӼ۷����¼�
	 */
	public static final String PRICE_ADD_EVENT = "23";
	/**
	 * ������ʼ�¼�
	 */
	public static final String STAGE_START_EVENT = "25";
	/**
	 * ������ͣ�¼�
	 */
	public static final String STAGE_PAUSE_EVENT = "26";
	/**
	 * ����ֹͣ�¼�
	 */
	public static final String STAGE_STOP_EVENT = "27";
	/**
	 * �����ָ��¼�
	 */
	public static final String STAGE_REGAIN_EVENT = "28";
	/**
	 * ѡ���Ƿ����ҡ���ύ�¼�
	 */
	public static final String STAGE_YH_EVENT = "39";
	
	/**
	 * ��ȡ�¼�����
	 * @return
	 */
	public String getEventType();
	/**
	 * ��ȡ�ؿ���������
	 * @return
	 */
	public String getAppId();


}
