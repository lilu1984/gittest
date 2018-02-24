package com.wonders.tdsc.bo.condition;

import java.util.List;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscNoticeAppCondition extends BaseCondition {
    
	/** ���ù���Id */
    private String noticeId;
    
    private List noticeIdList;
    
    /** ���ù���� */
    private String noticeNo;

    /** ���ù������� */
    private String noticeName;
    
    /** ���ù���״̬ */
    private String noticeStatus;
    
    /** ���ù���ؿ�ڵ� */
    private String noticeNodeId;
    
    /** ���ù���ؿ�ڵ�״̬ */
    private String noticeNodeStatus;
    
    /** ҵ��ID */
    private String appIds[];
    
    /** �Ƿ񷢲� */
    private String ifReleased;
    
    /** �����ʾ�Ƿ񷢲� */
    private String ifResultPublish;
    
    /** ���÷�ʽ */
	private String transferMode;
	
    
	/** UNITE_BLOCK_NAME �������еؿ����ƣ����ָ���*/
    private String uniteBlockName;
    
    /** TRADE_NUM ���Ĺұ�� */
    private String tradeNum;
    
    /** LAND_LOCATION �ؿ����䣨ͬtdscblockinfo�и��ֶΣ� */
    private String landLocation;

	/** �����û�ID */
	private String userId;
	
	/** ����״̬LIST */
	private List noticeStatusList;
		
	private String orderKey;
	
	private String orderType;

	public List getNoticeStatusList() {
		return noticeStatusList;
	}

	public void setNoticeStatusList(List noticeStatusList) {
		this.noticeStatusList = noticeStatusList;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

    public String getIfResultPublish() {
		return ifResultPublish;
	}

	public void setIfResultPublish(String ifResultPublish) {
		this.ifResultPublish = ifResultPublish;
	}

	public String[] getAppIds() {
        return appIds;
    }

    public void setAppIds(String[] appIds) {
        this.appIds = appIds;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public String getIfReleased() {
        return ifReleased;
    }

    public void setIfReleased(String ifReleased) {
        this.ifReleased = ifReleased;
    }

	public String getNoticeNodeId() {
		return noticeNodeId;
	}

	public void setNoticeNodeId(String noticeNodeId) {
		this.noticeNodeId = noticeNodeId;
	}

	public String getNoticeNodeStatus() {
		return noticeNodeStatus;
	}

	public void setNoticeNodeStatus(String noticeNodeStatus) {
		this.noticeNodeStatus = noticeNodeStatus;
	}

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getUniteBlockName() {
		return uniteBlockName;
	}

	public void setUniteBlockName(String uniteBlockName) {
		this.uniteBlockName = uniteBlockName;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public List getNoticeIdList() {
		return noticeIdList;
	}

	public void setNoticeIdList(List noticeIdList) {
		this.noticeIdList = noticeIdList;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	
	
}
