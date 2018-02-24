package com.wonders.engine.floorprice;

/**
 * 场景接口
 * 
 * 
 * @author Gordon
 *
 */
public interface Scene {

	/**
	 * 倒计时操作
	 */
	void countDown();
	
	/**
	 * 终端当前执行过程，执行下一个
	 */
	void next();
	
	
	/**
	 * 返回场景编号
	 * 
	 * @return
	 */
	public String getSceneId();
	
	
}
