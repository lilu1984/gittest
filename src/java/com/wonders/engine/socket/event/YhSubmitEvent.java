package com.wonders.engine.socket.event;

/**
 * 选择是否参与摇号提交事件
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
	 * 号牌
	 */
	private String conNum = "";
	/**
	 * 是否参与
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
