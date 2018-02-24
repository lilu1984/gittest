package com.wonders.engine.socket.event;
/**
 * 触发场景变化的事件.
 * @author sunxin
 *
 */
public interface ChangeEvent {
	/**
	 * 价格提交事件
	 */
	public static final String 	PRICE_SUBMIT_EVENT = "22";
	/**
	 * 修改加价幅度事件
	 */
	public static final String PRICE_ADD_EVENT = "23";
	/**
	 * 场景开始事件
	 */
	public static final String STAGE_START_EVENT = "25";
	/**
	 * 场景暂停事件
	 */
	public static final String STAGE_PAUSE_EVENT = "26";
	/**
	 * 场景停止事件
	 */
	public static final String STAGE_STOP_EVENT = "27";
	/**
	 * 场景恢复事件
	 */
	public static final String STAGE_REGAIN_EVENT = "28";
	/**
	 * 选择是否参与摇号提交事件
	 */
	public static final String STAGE_YH_EVENT = "39";
	
	/**
	 * 获取事件类型
	 * @return
	 */
	public String getEventType();
	/**
	 * 获取地块申请主键
	 * @return
	 */
	public String getAppId();


}
