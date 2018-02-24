package com.wonders.tdsc.bo;

import java.sql.Timestamp;
import java.util.Date;

import com.wonders.esframework.common.bo.BaseBO;

/** @author Hibernate CodeGenerator */
public class TdscBlockSelectApp extends BaseBO {

    /** identifier field */
    private String selectId;

    /** nullable persistent field */
    private String appId;

    /** nullable persistent field */
    private String selectNum;

    /** nullable persistent field */
    private String selectType;

    /** nullable persistent field */
    private String selectedId;

    /** nullable persistent field */
    private Timestamp activityDate;

    /** nullable persistent field */
    private String activityLoc;

    /** nullable persistent field */
    private String nodeId;

    /** nullable persistent field */
    private String nodeStat;

    /** nullable persistent field */
    private Timestamp replyDeadline;

    /** nullable persistent field */
    private Timestamp unlockTime;

    /** nullable persistent field */
    private String replyStatus;

    /** nullable persistent field */
    private Timestamp replyDate;

    /** nullable persistent field */
    private String selectUser;

    /** nullable persistent field */
    private Timestamp selectDate;

    /** nullable persistent field */
    private String isValid;

    /** nullable persistent field */
    private String specialistType;
    
    /**  抽选备注  */
    private String selectRemark;
    
    /**地块公告号*/
  	private String blockNoticeNo; 
  	
  	/**地块名称*/
   	private String blockName;     
   	
   	/**保证金到账截止时间*/
	private Date marginEndDate;  
	
	 /** 挂牌截止时间*/
	private Timestamp listEndDate;  
	
	/**现场竞价时间*/
	private Timestamp sceBidDate;    
	
	/**现场竞价地点*/
	private String sceBidLoc;     

    
    // 操作时间
    // for add
    private Timestamp operateTime = new Timestamp(System.currentTimeMillis());

    /** default constructor */
    public TdscBlockSelectApp() {
    }

    /** minimal constructor */
    public TdscBlockSelectApp(String selectId) {
        this.selectId = selectId;
    }

    public String getSelectId() {
        return this.selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSelectNum() {
        return this.selectNum;
    }

    public void setSelectNum(String selectNum) {
        this.selectNum = selectNum;
    }

    public String getSelectType() {
        return this.selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getSelectedId() {
        return this.selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public Date getActivityDate() {
        return this.activityDate;
    }

    public void setActivityDate(Timestamp activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityLoc() {
        return this.activityLoc;
    }

    public void setActivityLoc(String activityLoc) {
        this.activityLoc = activityLoc;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeStat() {
        return this.nodeStat;
    }

    public void setNodeStat(String nodeStat) {
        this.nodeStat = nodeStat;
    }

    public Date getReplyDeadline() {
        return this.replyDeadline;
    }

    public void setReplyDeadline(Timestamp replyDeadline) {
        this.replyDeadline = replyDeadline;
    }

    public Date getUnlockTime() {
        return this.unlockTime;
    }

    public void setUnlockTime(Timestamp unlockTime) {
        this.unlockTime = unlockTime;
    }

    public String getReplyStatus() {
        return this.replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public Date getReplyDate() {
        return this.replyDate;
    }

    public void setReplyDate(Timestamp replyDate) {
        this.replyDate = replyDate;
    }

    public String getSelectUser() {
        return this.selectUser;
    }

    public void setSelectUser(String selectUser) {
        this.selectUser = selectUser;
    }

    public Date getSelectDate() {
        return this.selectDate;
    }

    public void setSelectDate(Timestamp selectDate) {
        this.selectDate = selectDate;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getSpecialistType() {
        return specialistType;
    }

    public void setSpecialistType(String specialistType) {
        this.specialistType = specialistType;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        // for modify
        this.operateTime = new Timestamp(System.currentTimeMillis());
    }

	public String getSelectRemark() {
		return selectRemark;
	}

	public void setSelectRemark(String selectRemark) {
		this.selectRemark = selectRemark;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getBlockNoticeNo() {
		return blockNoticeNo;
	}

	public void setBlockNoticeNo(String blockNoticeNo) {
		this.blockNoticeNo = blockNoticeNo;
	}

	public Timestamp getListEndDate() {
		return listEndDate;
	}

	public void setListEndDate(Timestamp listEndDate) {
		this.listEndDate = listEndDate;
	}

	public Date getMarginEndDate() {
		return marginEndDate;
	}

	public void setMarginEndDate(Date marginEndDate) {
		this.marginEndDate = marginEndDate;
	}

	public Timestamp getSceBidDate() {
		return sceBidDate;
	}

	public void setSceBidDate(Timestamp sceBidDate) {
		this.sceBidDate = sceBidDate;
	}

	public String getSceBidLoc() {
		return sceBidLoc;
	}

	public void setSceBidLoc(String sceBidLoc) {
		this.sceBidLoc = sceBidLoc;
	}
}
