package com.wonders.engine.socket.event;

/**
 * ѡ���Ƿ����ҡ���ύ�¼�
 * @author Administrator
 *
 */
public class YhSubmitEvent implements ChangeEvent{

	public YhSubmitEvent(String appId,String conNum,boolean flag){
		this.appId = appId;
		this.conNum = conNum;
		this.flag = flag;
	}
	private String appId = null;
	/**
	 * ����
	 */
	private String conNum = "";
	/**
	 * �Ƿ����
	 */
	private boolean flag = false;
	
	public String getConNum() {
		return conNum;
	}

	public void setConNum(String conNum) {
		this.conNum = conNum;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getAppId() {
		// TODO Auto-generated method stub
		return this.appId;
	}

	public String getEventType() {
		// TODO Auto-generated method stub
		return ChangeEvent.STAGE_YH_EVENT;
	}

}
