package com.wonders.engine;

import com.wonders.engine.database.DbSupport;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.flex.FlexPolicy;
import com.wonders.engine.listing.ListingSupport;
import com.wonders.engine.sms.SmsSupport;
import com.wonders.engine.socket.SocketServer;
import com.wonders.engine.socket.handler.HandlerSupport;
/**
 * 网上交易引擎入口.
 * @author sunxin
 *
 */
public class TradeEngine {
	/**
	 * 版本号
	 */
	private String version = "3.0 beta";
	/**
	 * 引擎名称
	 */
	private String serverName = "万达网上交易引擎 ";
	/**
	 * 引擎线程
	 */
	private Thread engineThread = null;
	/**
	 * 时间服务线程
	 */
	private Thread timeThread = null;
	/**
	 * SocketServer
	 */
	private SocketServer server = null;
	/**
	 * 端口号
	 */
	private int serverPort = 2662;
	/**
	 * 阻塞时间
	 */
	private int socketTimeout = 0;
	/**
	 * 命令接收处理类
	 */
	private String handlerClass = null;
	/**
	 * 数据库处理类
	 */
	private String dbClass = null;
	/**
	 * 短消息处理类
	 */
	private String smsClass = null;
	/**
	 * 挂牌消息处理类
	 */
	private String listingClass = null;

	
	/**
	 * 启动引擎.
	 * @throws EngineException
	 */
	public void startTradeEngine() throws EngineException{
		if (server != null){
			throw new EngineException("万达交易观摩引擎已经启动,不能重复启动");
		}
		//初始化hanlder 和 db
		if (handlerClass != null && dbClass != null) {
			try {
				//创建实例类
				BaseStore.handlerSupport = (HandlerSupport) Class.forName(handlerClass).newInstance();
				//数据库支持类
				BaseStore.dbSupport = (DbSupport) Class.forName(dbClass).newInstance();
				//挂牌类
				BaseStore.listingSupport = (ListingSupport) Class.forName(listingClass).newInstance();
				BaseStore.listingSupport.startListing();
			} catch (ClassNotFoundException e) {
				throw new EngineException("Could not load class/s : " + e);
			} catch (InstantiationException e) {
				throw new EngineException("Could not instantiat class/s : " + e);
			} catch (IllegalAccessException e) {
				throw new EngineException("Illegal access to class/s : " + e);
			}
			//数据初始化
			//BaseStore.tradeBlocks = BaseStore.dbSupport.updateEngineBlockInfo();
			//启动引擎线程
			//server = new SocketServer(serverPort,socketTimeout);
			//engineThread = new Thread(server, serverName+version);
			//engineThread.start();
		} else {
			throw new EngineException("请注入HandlerSupport和DbSupport");
		}
		
	}

	public void setBroadcastLevel(int broadcastLevel) {
		BaseStore.broadcastLevel = broadcastLevel;
	}

	public void setDbClass(String dbClass) {
		this.dbClass = dbClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	public void setSmsService(boolean isSmsService) {
		BaseStore.isSmsService = isSmsService;
	}

	public void setTimeService(boolean isTimeService) {
		BaseStore.isTimeService = isTimeService;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setSmsClass(String smsClass) {
		this.smsClass = smsClass;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setListingClass(String listingClass) {
		this.listingClass = listingClass;
	}
	
	
	
}
