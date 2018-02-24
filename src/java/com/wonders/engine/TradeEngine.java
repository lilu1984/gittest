package com.wonders.engine;

import com.wonders.engine.database.DbSupport;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.flex.FlexPolicy;
import com.wonders.engine.listing.ListingSupport;
import com.wonders.engine.sms.SmsSupport;
import com.wonders.engine.socket.SocketServer;
import com.wonders.engine.socket.handler.HandlerSupport;
/**
 * ���Ͻ����������.
 * @author sunxin
 *
 */
public class TradeEngine {
	/**
	 * �汾��
	 */
	private String version = "3.0 beta";
	/**
	 * ��������
	 */
	private String serverName = "������Ͻ������� ";
	/**
	 * �����߳�
	 */
	private Thread engineThread = null;
	/**
	 * ʱ������߳�
	 */
	private Thread timeThread = null;
	/**
	 * SocketServer
	 */
	private SocketServer server = null;
	/**
	 * �˿ں�
	 */
	private int serverPort = 2662;
	/**
	 * ����ʱ��
	 */
	private int socketTimeout = 0;
	/**
	 * ������մ�����
	 */
	private String handlerClass = null;
	/**
	 * ���ݿ⴦����
	 */
	private String dbClass = null;
	/**
	 * ����Ϣ������
	 */
	private String smsClass = null;
	/**
	 * ������Ϣ������
	 */
	private String listingClass = null;

	
	/**
	 * ��������.
	 * @throws EngineException
	 */
	public void startTradeEngine() throws EngineException{
		if (server != null){
			throw new EngineException("��ｻ�׹�Ħ�����Ѿ�����,�����ظ�����");
		}
		//��ʼ��hanlder �� db
		if (handlerClass != null && dbClass != null) {
			try {
				//����ʵ����
				BaseStore.handlerSupport = (HandlerSupport) Class.forName(handlerClass).newInstance();
				//���ݿ�֧����
				BaseStore.dbSupport = (DbSupport) Class.forName(dbClass).newInstance();
				//������
				BaseStore.listingSupport = (ListingSupport) Class.forName(listingClass).newInstance();
				BaseStore.listingSupport.startListing();
			} catch (ClassNotFoundException e) {
				throw new EngineException("Could not load class/s : " + e);
			} catch (InstantiationException e) {
				throw new EngineException("Could not instantiat class/s : " + e);
			} catch (IllegalAccessException e) {
				throw new EngineException("Illegal access to class/s : " + e);
			}
			//���ݳ�ʼ��
			//BaseStore.tradeBlocks = BaseStore.dbSupport.updateEngineBlockInfo();
			//���������߳�
			//server = new SocketServer(serverPort,socketTimeout);
			//engineThread = new Thread(server, serverName+version);
			//engineThread.start();
		} else {
			throw new EngineException("��ע��HandlerSupport��DbSupport");
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
