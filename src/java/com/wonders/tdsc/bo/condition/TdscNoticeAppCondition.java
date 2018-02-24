package com.wonders.tdsc.bo.condition;

import java.util.List;

import com.wonders.esframework.common.bo.BaseCondition;

public class TdscNoticeAppCondition extends BaseCondition {
    
	/** 出让公告Id */
    private String noticeId;
    
    private List noticeIdList;
    
    /** 出让公告号 */
    private String noticeNo;

    /** 出让公告名称 */
    private String noticeName;
    
    /** 出让公告状态 */
    private String noticeStatus;
    
    /** 出让公告地块节点 */
    private String noticeNodeId;
    
    /** 出让公告地块节点状态 */
    private String noticeNodeStatus;
    
    /** 业务ID */
    private String appIds[];
    
    /** 是否发布 */
    private String ifReleased;
    
    /** 结果公示是否发布 */
    private String ifResultPublish;
    
    /** 出让方式 */
	private String transferMode;
	
    
	/** UNITE_BLOCK_NAME 公告所有地块名称（，分隔）*/
    private String uniteBlockName;
    
    /** TRADE_NUM 招拍挂编号 */
    private String tradeNum;
    
    /** LAND_LOCATION 地块坐落（同tdscblockinfo中该字段） */
    private String landLocation;

	/** 设置用户ID */
	private String userId;
	
	/** 公告状态LIST */
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
