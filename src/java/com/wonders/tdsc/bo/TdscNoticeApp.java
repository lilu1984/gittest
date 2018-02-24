package com.wonders.tdsc.bo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TdscNoticeApp implements Serializable {
	/**
	 * 加虚字段，为了页面取值
	 */
	String stringDate;
	String url;
    /** identifier field */
    private String noticeId;

    /** nullable persistent field */
    private String noticeNo;
    
    /** nullable persistent field */
    private String noticeNoOld;

    /** nullable persistent field */
    private String noticeName;

    /** nullable persistent field */
    private String noticeType;

    /** nullable persistent field */
    private String fileUrl;

    /** nullable persistent field */
    private String linkManName;

    /** nullable persistent field */
    private String linkManAddr;

    /** nullable persistent field */
    private String linkManTel;

    /** nullable persistent field */
    private String tranManName;

    /** nullable persistent field */
    private String tranManAddr;

    /** nullable persistent field */
    private String tranManTel;

    /** nullable persistent field */
    private String ifReleased;
    
    /** nullable persistent field */
    private String ifResultPublish;

    /** nullable persistent field */
    private Timestamp noticeDate;
    
    private String noticeStatus;
    
    private Timestamp statusDate;
    
    /** identifier field */
    private String recordId;
    
    private String planId;
    
    public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	/** nullable persistent field */
    private String ifSendWondertek;
    
    /** RESULT_RECORD_ID */
    private String ifSendBlockWondertek;
    
    //出让文件节点状态id
    private String noticeStatusId;
    
    //出让文件节点id
    private String noticeNodeId;
    
    /** identifier field */
    private String resultRecordId;
    
    /** RESULT_PUBLISH_DATE */
    private Timestamp resultPublishDate;
    
    /** UNITE_BLOCK_NAME 公告所有地块名称（，分隔）*/
    private String uniteBlockName;
    
    /** TRADE_NUM 招拍挂编号 */
    private String tradeNum;
    
    /** TRANSFER_MODE 交易方式 */
    private String transferMode;
    
    /** 设置用户ID */
    private String userId;
    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
    /** LAND_LOCATION 地块坐落（同tdscblockinfo中该字段） */
    private String landLocation;
    
    public String getResultRecordId() {
		return resultRecordId;
	}

	public void setResultRecordId(String resultRecordId) {
		this.resultRecordId = resultRecordId;
	}

	public Timestamp getResultPublishDate() {
		return resultPublishDate;
	}

	public void setResultPublishDate(Timestamp resultPublishDate) {
		this.resultPublishDate = resultPublishDate;
	}

	public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    /** default constructor */
    public TdscNoticeApp() {
    }

    /** minimal constructor */
    public TdscNoticeApp(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeNo() {
        return this.noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNoticeName() {
        return noticeName;
    }
    
    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }
    
    public String getNoticeType() {
        return noticeType;
    }
    
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
    
    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getLinkManName() {
        return this.linkManName;
    }

    public void setLinkManName(String linkManName) {
        this.linkManName = linkManName;
    }

    public String getLinkManAddr() {
        return this.linkManAddr;
    }

    public void setLinkManAddr(String linkManAddr) {
        this.linkManAddr = linkManAddr;
    }

    public String getLinkManTel() {
        return this.linkManTel;
    }

    public void setLinkManTel(String linkManTel) {
        this.linkManTel = linkManTel;
    }

    public String getTranManName() {
        return this.tranManName;
    }

    public void setTranManName(String tranManName) {
        this.tranManName = tranManName;
    }

    public String getTranManAddr() {
        return this.tranManAddr;
    }

    public void setTranManAddr(String tranManAddr) {
        this.tranManAddr = tranManAddr;
    }

    public String getTranManTel() {
        return this.tranManTel;
    }

    public void setTranManTel(String tranManTel) {
        this.tranManTel = tranManTel;
    }

    public String getIfReleased() {
        return ifReleased;
    }
    
    public void setIfReleased(String ifReleased) {
        this.ifReleased = ifReleased;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("noticeId", getNoticeId())
            .toString();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

	public String getIfResultPublish() {
		return ifResultPublish;
	}

	public void setIfResultPublish(String ifResultPublish) {
		this.ifResultPublish = ifResultPublish;
	}

	public String getIfSendBlockWondertek() {
		return ifSendBlockWondertek;
	}

	public void setIfSendBlockWondertek(String ifSendBlockWondertek) {
		this.ifSendBlockWondertek = ifSendBlockWondertek;
	}

	public String getIfSendWondertek() {
		return ifSendWondertek;
	}

	public void setIfSendWondertek(String ifSendWondertek) {
		this.ifSendWondertek = ifSendWondertek;
	}

	public String getNoticeStatusId() {
		return noticeStatusId;
	}

	public void setNoticeStatusId(String noticeStatusId) {
		this.noticeStatusId = noticeStatusId;
	}

	public String getNoticeNodeId() {
		return noticeNodeId;
	}

	public void setNoticeNodeId(String noticeNodeId) {
		this.noticeNodeId = noticeNodeId;
	}

	public String getNoticeNoOld() {
		return noticeNoOld;
	}

	public void setNoticeNoOld(String noticeNoOld) {
		this.noticeNoOld = noticeNoOld;
	}

	public Timestamp getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(Timestamp noticeDate) {
		this.noticeDate = noticeDate;
	}

	public Timestamp getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
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

	public String getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}

	public String getLandLocation() {
		return landLocation;
	}

	public void setLandLocation(String landLocation) {
		this.landLocation = landLocation;
	}
	
	

	public String getStringDate() {
		return stringDate;
	}

	public void setStringDate(String stringDate) {
		this.stringDate = stringDate;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((noticeId == null) ? 0 : noticeId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TdscNoticeApp other = (TdscNoticeApp) obj;
		if (noticeId == null) {
			if (other.noticeId != null)
				return false;
		} else if (!noticeId.equals(other.noticeId))
			return false;
		return true;
	}
	
	
}
