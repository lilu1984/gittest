package com.wonders.engine.floorprice;

/**
 * �����ӿ�
 * 
 * 
 * @author Gordon
 *
 */
public interface Scene {

	/**
	 * ����ʱ����
	 */
	void countDown();
	
	/**
	 * �ն˵�ǰִ�й��̣�ִ����һ��
	 */
	void next();
	
	
	/**
	 * ���س������
	 * 
	 * @return
	 */
	public String getSceneId();
	
	
}
