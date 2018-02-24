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
 * 客户端管道,主要负责客户端和引擎通信.
 * @author sunxin
 *
 */
public abstract class ClientPipe{
	/**
	 * 日志
	 */
	protected Logger log = Logger.getLogger(AbstractAuction.class);
    /**
     * 通道编号
     */
	protected String clientNo = "99";
    
    /**
     * 所有可操作的网上交易对象，当该用户可以参与多个公告的活动时，只有一个出于激活状态。
     */
	protected List clientNodes = null;
    
    /**
     * 当前激活的节点
     */
	protected ClientNode activeNode = new ClientNode();
    
	/**
	 * 通道连接和数据处理器
	 */
	protected HandlerSupport handlerSupport = null;

	/**
	 * 是否同一个账号重复登陆
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
	 * 是否购买了某此地块.
	 * @param appId 地块申请主键
	 * @return
	 */
	public boolean isBidderBlock(String appId) {
		return activeNode.isBidderBlock(appId);
	}
	/**
	 * 是否购买了某此公告.
	 * @param noticeId 地块申请公告主键
	 * @return
	 */
	public boolean isBidderNotice(String noticeId) {
		return activeNode.isBidderNotice(noticeId);
	}
	
	public String getCertNo(String appId) {
		return activeNode.getCertNo(appId);
	}

   /**
    * 初始化客户端.
    * @param paramMap 参数Map
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
    	//将自身加入到客户端池中
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
    		//将自身加入到客户端池中
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
     * 保存价格.
     * @param tradePrice
     * @return
     */
    public  boolean pushPrice(TradePrice tradePrice){
    	return CoreService.pushPrice(tradePrice);
    }

    /**
     * 向自身客户端管道发送命令.
     * @param commandNo 命令代码
     */
    public void sendCommandByNo(String commandNo,Map paramMap){
    	String commandString = CommandFactory.getInstance().genEngineCommand(commandNo, paramMap, null);
    	sendCommand(commandString);
    }

    /**
     * 向自身客户端管道发送消息.
     * @param commandString 消息内容
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
