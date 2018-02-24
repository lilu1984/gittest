package com.wonders.engine.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wonders.engine.CoreService;
import com.wonders.engine.auction.AbstractAuction;
import com.wonders.engine.bo.TradePrice;
import com.wonders.engine.exception.EngineException;
import com.wonders.engine.socket.command.CommandFactory;
import com.wonders.engine.socket.encode.EncodeUtils;
import com.wonders.engine.socket.handler.HandlerSupport;

/**
 * �ͻ��˹ܵ�,��Ҫ����ͻ��˺�����ͨ��.
 * @author sunxin
 *
 */
public abstract class ClientPipe{
	/**
	 * ��־
	 */
	protected Logger log = Logger.getLogger(AbstractAuction.class);
    /**
     * ͨ�����
     */
	protected String clientNo = "99";
    
    /**
     * ���пɲ��������Ͻ��׶��󣬵����û����Բ���������Ļʱ��ֻ��һ�����ڼ���״̬��
     */
	protected List clientNodes = null;
    
    /**
     * ��ǰ����Ľڵ�
     */
	protected ClientNode activeNode = new ClientNode();
    
	/**
	 * ͨ�����Ӻ����ݴ�����
	 */
	protected HandlerSupport handlerSupport = null;

	/**
	 * �Ƿ�ͬһ���˺��ظ���½
	 */
	private boolean isRepeatLogin = false;
	
	public ClientNode activeNode() {
		if (clientNodes != null) {
			for (int i = 0 ; i < clientNodes.size(); i++) {
				ClientNode temp = (ClientNode)clientNodes.get(i);
				if (temp.isActive()) {
					return temp;
				}
			}
		}
		return null;
	}
	
	/**
	 * �Ƿ�����ĳ�˵ؿ�.
	 * @param appId �ؿ���������
	 * @return
	 */
	public boolean isBidderBlock(String appId) {
		return activeNode.isBidderBlock(appId);
	}
	/**
	 * �Ƿ�����ĳ�˹���.
	 * @param noticeId �ؿ����빫������
	 * @return
	 */
	public boolean isBidderNotice(String noticeId) {
		return activeNode.isBidderNotice(noticeId);
	}
	
	public String getCertNo(String appId) {
		return activeNode.getCertNo(appId);
	}

   /**
    * ��ʼ���ͻ���.
    * @param paramMap ����Map
    */
    public void initClientPipe(Map paramMap) {
    	this.clientNo = (String)paramMap.get("clientNo");
    	activeNode = new ClientNode();
    	activeNode.setActive(true);
    	activeNode.setConNum("");
    	activeNode.setYktBh("");
    	activeNode.setMobile("");
    	activeNode.setBidderAppIds("");
    	activeNode.setNoticeIds("");
//    	this.conNum = (String)paramMap.get("conNum");
//    	this.yktBh = (String)paramMap.get("yktBh");
//    	this.mobile = (String)paramMap.get("mobile");
//    	this.bidderAppIds = (String)paramMap.get("bidderAppIds");
//    	this.noticeIds = (String)paramMap.get("noticeIds");
    	//��������뵽�ͻ��˳���
    	try {
			CoreService.addClientPipe(clientNo, this);
		} catch (EngineException e) {
			e.printStackTrace();
		}
    }
    
    public void initClientPipe(String clientNo, Map paramMap, String flag) {
    	this.clientNo = clientNo;
    	Collection values = paramMap.values();
    	if (values != null) {
    		this.clientNodes = new ArrayList(values);
    	}
    	if (StringUtils.isEmpty(flag)) {
    		//��������뵽�ͻ��˳���
        	try {
    			CoreService.addClientPipe(clientNo, this);
    		} catch (EngineException e) {
    			e.printStackTrace();
    		}
    	} else {
    		try {
    			CoreService.reloadClientPipe(clientNo, this);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
    	}
    }
   
    public void activedClientNode(String noticeId) {
    	if (clientNodes != null) {
    		for (int i = 0 ; i < clientNodes.size(); i++) {
    			ClientNode node = (ClientNode)clientNodes.get(i);
    			if (StringUtils.trimToEmpty(noticeId).equals(node.getNoticeIds())) {
    				node.active();
    				this.activeNode = node;
    			} else {
    				node.disactive();
    			}
    		}
    	}
    }
    
    public void activedClientNodeByAppId(String appId) {
    	if (clientNodes != null) {
    		for (int i = 0 ; i < clientNodes.size(); i++) {
    			ClientNode node = (ClientNode)clientNodes.get(i);
    			if (StringUtils.isNotBlank(node.getCertNo(appId))) {
    				node.active();
    				this.activeNode = node;
    			} else {
    				node.disactive();
    			}
    		}
    	}
    }
    
    /**
     * ����۸�.
     * @param tradePrice
     * @return
     */
    public  boolean pushPrice(TradePrice tradePrice){
    	return CoreService.pushPrice(tradePrice);
    }

    /**
     * ������ͻ��˹ܵ���������.
     * @param commandNo �������
     */
    public void sendCommandByNo(String commandNo,Map paramMap){
    	String commandString = CommandFactory.getInstance().genEngineCommand(commandNo, paramMap, null);
    	sendCommand(commandString);
    }

    /**
     * ������ͻ��˹ܵ�������Ϣ.
     * @param commandString ��Ϣ����
     */
    public abstract void sendCommand(String commandString);
    


    public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	public String getConNum() {
		return activeNode.getConNum();
	}
	public String getYktBh() {
		return activeNode.getYktBh();
	}
	
	public String getBidderAppIds() {
		return activeNode.getBidderAppIds();
	}

	public String getMobile() {
		return activeNode.getMobile();
	}
	
	public String getNoticeIds() {
		return activeNode.getNoticeIds();
	}
	
	public void setNoticeIds(String noticeId) {
		ClientNode temp = new ClientNode();
		temp.setNoticeIds(noticeId);
		temp.active();
		this.activeNode = temp;
	}
	public void reload(ClientPipe pipe) {
		// TODO Auto-generated method stub
		this.clientNo = pipe.getClientNo();
		this.clientNodes = pipe.getClientNodes();
	}
	public List getClientNodes() {
		return clientNodes;
	}
	
	public void setClientNodes(List clientNodes) {
		this.clientNodes = clientNodes;
	}

	public boolean isRepeatLogin() {
		return isRepeatLogin;
	}

	public void setRepeatLogin(boolean isRepeatLogin) {
		this.isRepeatLogin = isRepeatLogin;
	}
	
	
}
