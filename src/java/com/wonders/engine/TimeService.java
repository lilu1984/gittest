package com.wonders.engine;

import com.wonders.engine.socket.command.CommandFactory;
import com.wonders.wsjy.TradeConsts;

/**
 * ϵͳʱ�����,��TradeEngine�п��������Ƿ�����.
 * @author sunxin
 *
 */
public class TimeService extends Thread {
	/**
	 * ���񿪹�:���Ϊ-1���رշ���
	 */
	private int timeSwitch = 0;
	
	//private int tt = 1;
	public void run() {
		try {
			while(true){
				if(timeSwitch == -1){
					break;
				}
			String commandString = CommandFactory.getInstance().genEngineCommand(TradeConsts.ORDER31, null, null);
				CoreService.broadcastCommand(commandString);
				sleep(60*60*1000);
				//tt=tt+1;
				//if(tt>60 && tt<560){
				//	String  commandString= "{op:'11',conNum:'5',price:'6800"+tt+"',priceTime:'2012-02-16 20:21:05',msg:'null',appId:'8a28e915353c07f101353c0d96910066',clientNo:'2012020004',priceNum:'23'}";
				//	
				//	CoreService.broadcastCommand(commandString);
				//}
				//sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public int getTimeSwitch() {
		return timeSwitch;
	}


	public void setTimeSwitch(int timeSwitch) {
		this.timeSwitch = timeSwitch;
	}
	
}
